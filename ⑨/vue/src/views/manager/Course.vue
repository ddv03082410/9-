<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入课程名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <el-alert
        v-if="data.user.role === 'STUDENT'"
        type="info"
        :closable="false"
        class="card"
        style="margin-bottom: 5px"
        title="仅展示符合所属学院/专业或跨院通识的课程，跨院课程需优先满足前置要求">
    </el-alert>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px" v-if="data.user.role === 'ADMIN'">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="课程名称" prop="name"></el-table-column>
        <el-table-column label="课程介绍" prop="content" show-overflow-tooltip></el-table-column>
        <el-table-column label="学分" prop="credit" width="80"></el-table-column>
        <el-table-column label="授课教师" prop="teacherName"></el-table-column>
        <el-table-column label="所属学院" prop="collegeName"></el-table-column>
        <el-table-column label="所属专业">
          <template #default="scope">
            {{ scope.row.specialityName || '不限专业' }}
          </template>
        </el-table-column>
        <el-table-column label="课程类型" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.general" type="warning" effect="light">跨院通识</el-tag>
            <el-tag v-else type="info" effect="plain">院内课程</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开课人数" prop="num" width="90"></el-table-column>
        <el-table-column label="上课时间" prop="time"></el-table-column>
        <el-table-column label="上课地点" prop="location"></el-table-column>
        <el-table-column label="已选人数" prop="alreadyNum" width="90"></el-table-column>
        <el-table-column label="前置课程" prop="prerequisiteNames" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" align="center" width="180" v-if="data.user.role !== 'TEACHER'">
          <template #default="scope" v-if="data.user.role === 'ADMIN'">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
          <template #default="scope" v-else>
            <el-button
                type="primary"
                @click="choiceCourse(scope.row)"
                :disabled="scope.row.num === scope.row.alreadyNum"
            >选课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination
          background
          layout="prev, pager, next"
          v-model:page-size="data.pageSize"
          v-model:current-page="data.pageNum"
          :total="data.total"
          @current-change="changePage"
      />
    </div>

    <el-dialog title="课程信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="110px" style="padding-right: 50px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程介绍" prop="content">
          <el-input type="textarea" :rows="4" v-model="data.form.content" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程学分" prop="credit">
          <el-input-number v-model="data.form.credit" :min="0.5" :max="6" :step="0.5" />
        </el-form-item>
        <el-form-item label="授课教师" prop="teacherId">
          <el-select v-model="data.form.teacherId" placeholder="请选择教师">
            <el-option
                v-for="item in data.teacherData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开课人数" prop="num">
          <el-input v-model="data.form.num" autocomplete="off" />
        </el-form-item>
        <el-form-item label="上课时间" prop="time">
          <el-input v-model="data.form.time" autocomplete="off" />
        </el-form-item>
        <el-form-item label="上课地点" prop="location">
          <el-input v-model="data.form.location" autocomplete="off" />
        </el-form-item>
        <el-form-item label="所属学院" prop="collegeId">
          <el-select v-model="data.form.collegeId" placeholder="请选择学院">
            <el-option
                v-for="item in data.collegeData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属专业" prop="specialityId">
          <el-select
              v-model="data.form.specialityId"
              placeholder="请选择专业"
              :disabled="!data.specialityOptions.length"
              clearable>
            <el-option
                v-for="item in data.specialityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="跨院通识" prop="general">
          <div class="general-flag">
            <el-switch v-model="data.form.general" active-text="对所有学院推荐" inactive-text="仅本学院/专业" />
          </div>
        </el-form-item>
        <el-form-item label="前置课程" prop="prerequisiteIds">
          <el-select v-model="data.form.prerequisiteIds" multiple collapse-tags filterable placeholder="请选择前置课程">
            <el-option
                v-for="item in data.prerequisiteOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                :disabled="item.id === data.form.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive, watch} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  tableData: [],
  name: null,
  collegeData: [],
  teacherData: [],
  prerequisiteOptions: [],
  specialityOptions: []
})

const load = () => {
  let teacherId = null
  let studentId = null
  if (data.user.role === 'TEACHER') {
    teacherId = data.user.id
  }
  if (data.user.role === 'STUDENT') {
    studentId = data.user.id
  }
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      teacherId: teacherId,
      studentId: studentId,
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
  })
}

const changePage = (pageNum) => {
  data.pageNum = pageNum
  load()
}

const handleAdd = () => {
  data.form = { credit: 2, prerequisiteIds: [], general: false, specialityId: null }
  data.formVisible = true
  fetchSpecialitiesByCollege(null, false)
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.form.prerequisiteIds = row.prerequisiteIds ? JSON.parse(JSON.stringify(row.prerequisiteIds)) : []
  data.formVisible = true
  fetchSpecialitiesByCollege(data.form.collegeId, true)
}

const add = () => {
  request.post('/course/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      loadPrerequisiteOptions()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/course/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      loadPrerequisiteOptions()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，确定继续吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/course/deleteById/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

const reset = () => {
  data.name = null
  data.pageNum = 1
  load()
}

const loadCollege = () => {
  request.get('/college/selectAll').then(res => {
    if (res.code === '200') {
      data.collegeData = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadTeacher = () => {
  request.get('/teacher/selectAll').then(res => {
    if (res.code === '200') {
      data.teacherData = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadPrerequisiteOptions = () => {
  request.get('/course/selectAll').then(res => {
    if (res.code === '200') {
      data.prerequisiteOptions = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const fetchSpecialitiesByCollege = (collegeId, preserve = false) => {
  if (!collegeId) {
    data.specialityOptions = []
    if (!preserve) {
      data.form.specialityId = null
    }
    return
  }
  request.get(`/speciality/selectByCollege/${collegeId}`).then(res => {
    if (res.code === '200') {
      data.specialityOptions = res.data || []
      if (!preserve) {
        const exists = data.specialityOptions.find(item => item.id === data.form.specialityId)
        if (!exists) {
          data.form.specialityId = null
        }
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

watch(() => data.form.collegeId, (newVal, oldVal) => {
  if (!data.formVisible) {
    return
  }
  if (newVal !== oldVal) {
    fetchSpecialitiesByCollege(newVal, false)
  }
})

const choiceCourse = (row) => {
  const courseData = JSON.parse(JSON.stringify(row))
  courseData.studentId = data.user.id
  request.post('/choice/add', courseData).then(res => {
    if (res.code === '200') {
      ElMessage.success('选课成功，祝学习顺利！')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load()
loadCollege()
loadTeacher()
loadPrerequisiteOptions()
</script>

<style scoped>
.general-flag {
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>
