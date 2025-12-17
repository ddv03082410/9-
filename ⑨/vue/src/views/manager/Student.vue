<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" style="width: 250px; margin-right: 10px" placeholder="请输入学生姓名"></el-input>
      <el-select v-model="data.filterCollegeId" style="width: 200px; margin-right: 10px" placeholder="请选择学院" clearable>
        <el-option
            v-for="item in data.collegeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select
          v-model="data.filterSpecialityId"
          style="width: 200px; margin-right: 10px"
          placeholder="请选择专业"
          clearable
          :disabled="!data.filterSpecialityOptions.length">
        <el-option
            v-for="item in data.filterSpecialityOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div class="card">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="账号" prop="username"></el-table-column>
        <el-table-column label="头像" prop="avatar" width="80">
          <template #default="scope">
            <el-image :src="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 50%"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="姓名" prop="name"></el-table-column>
        <el-table-column label="性别" prop="sex"></el-table-column>
        <el-table-column label="学号" prop="code"></el-table-column>
        <el-table-column label="所属学院" prop="collegeName"></el-table-column>
        <el-table-column label="所属专业">
          <template #default="scope">
            {{ scope.row.specialityName || '未设置' }}
          </template>
        </el-table-column>
        <el-table-column label="已获学分" prop="score" width="90"></el-table-column>
        <el-table-column label="操作" align="center" width="260">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="success" @click="openCompletedDrawer(scope.row)">已修课程</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="changePage"/>
    </div>

    <el-dialog title="学生信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="头像" prop="avatar">
          <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess">
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="data.form.sex" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学号" prop="code">
          <el-input v-model="data.form.code" autocomplete="off" placeholder="请输入学号"/>
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
              clearable
              :disabled="!data.specialityOptions.length">
            <el-option
                v-for="item in data.specialityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
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

    <el-drawer
        v-model="data.completedDrawerVisible"
        size="50%"
        :title="`已修课程 · ${data.completedStudent?.name || ''}`"
        destroy-on-close>
      <div class="completed-summary">
        <el-card>
          <div class="stat-title">已获学分</div>
          <div class="stat-value">{{ data.completedStudent?.score || 0 }}</div>
        </el-card>
        <el-card>
          <div class="stat-title">完成课程数</div>
          <div class="stat-value">{{ data.completedCourses.length }}</div>
        </el-card>
      </div>

      <el-form :model="data.completedForm" label-width="120px" style="margin: 20px 0">
        <el-form-item label="添加课程">
          <div style="display: flex; gap: 10px; width: 100%;">
            <el-select
                v-model="data.completedForm.courseIds"
                placeholder="选择已完成的课程"
                multiple
                collapse-tags
                filterable
                remote
                reserve-keyword
                :remote-method="queryCourseOptions"
                :loading="data.courseLoading"
                style="flex: 1">
              <el-option
                  v-for="item in data.courseOptions"
                  :key="item.id"
                  :label="`${item.name}（${item.collegeName || '未分学院'}）`"
                  :value="item.id"
                  :disabled="completedCourseIds.has(item.id)">
              </el-option>
            </el-select>
            <el-button type="primary" :loading="data.completedSaving" @click="saveCompletedCourses">添加</el-button>
          </div>
        </el-form-item>
      </el-form>

      <el-table :data="data.completedCourses" v-loading="data.completedLoading" stripe>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="学分" prop="credit" width="80"></el-table-column>
        <el-table-column label="学院" prop="collegeName"></el-table-column>
        <el-table-column label="教师" prop="teacherName"></el-table-column>
        <el-table-column label="完成时间" prop="completedAt"></el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="danger" text @click="removeCompletedCourse(scope.row.id)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>
  </div>
</template>

<script setup>
import {reactive, watch, computed} from "vue";
import request from "@/utils/request";
import {ElMessageBox, ElMessage} from "element-plus";

const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  formVisible: false,
  form: { collegeId: null, specialityId: null },
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  name: null,
  collegeData: [],
  specialityOptions: [],
  filterCollegeId: null,
  filterSpecialityId: null,
  filterSpecialityOptions: [],
  completedDrawerVisible: false,
  completedStudent: null,
  completedCourses: [],
  completedLoading: false,
  completedSaving: false,
  completedForm: { courseIds: [] },
  courseOptions: [],
  courseLoading: false,
  courseSearch: ''
})

const completedCourseIds = computed(() => new Set((data.completedCourses || []).map(item => item.courseId)))

const load = () => {
  request.get('/student/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      collegeId: data.filterCollegeId,
      specialityId: data.filterSpecialityId
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total || 0
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleSearch = () => {
  data.pageNum = 1
  load()
}

const changePage = (pageNum) => {
  data.pageNum = pageNum
  load()
}

const handleAdd = () => {
  data.form = { collegeId: null, specialityId: null }
  data.formVisible = true
  fetchSpecialities(null)
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  fetchSpecialities(data.form.collegeId, true)
}

const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，确定继续吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/student/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

const add = () => {
  request.post('/student/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/student/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const reset = () => {
  data.name = null
  data.filterCollegeId = null
  data.filterSpecialityId = null
  data.filterSpecialityOptions = []
  data.pageNum = 1
  load()
}

const handleImgSuccess = (res) => {
  data.form.avatar = res.data
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

const fetchSpecialities = (collegeId, preserve = false) => {
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

const fetchFilterSpecialities = (collegeId) => {
  if (!collegeId) {
    data.filterSpecialityOptions = []
    data.filterSpecialityId = null
    return
  }
  request.get(`/speciality/selectByCollege/${collegeId}`).then(res => {
    if (res.code === '200') {
      data.filterSpecialityOptions = res.data || []
      const exists = data.filterSpecialityOptions.find(item => item.id === data.filterSpecialityId)
      if (!exists) {
        data.filterSpecialityId = null
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
    fetchSpecialities(newVal)
  }
})

watch(() => data.filterCollegeId, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    fetchFilterSpecialities(newVal)
  }
})

const openCompletedDrawer = (row) => {
  data.completedStudent = row
  data.completedDrawerVisible = true
  data.completedForm.courseIds = []
  data.courseOptions = []
  data.courseSearch = ''
  loadCompletedCourses(row.id)
  queryCourseOptions('')
}

const loadCompletedCourses = (studentId) => {
  if (!studentId) return
  data.completedLoading = true
  request.get('/student/completedCourses', { params: { studentId } }).then(res => {
    data.completedLoading = false
    if (res.code === '200') {
      data.completedCourses = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(() => data.completedLoading = false)
}

const queryCourseOptions = (keyword = '') => {
  if (!data.completedStudent?.id) {
    data.courseOptions = []
    data.courseLoading = false
    return
  }
  const normalized = (keyword || '').trim()
  data.courseSearch = normalized
  data.courseLoading = true
  request.get('/student/completedCourses/options', {
    params: {
      studentId: data.completedStudent.id,
      name: normalized || undefined
    }
  }).then(res => {
    data.courseLoading = false
    if (res.code === '200') {
      data.courseOptions = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(() => {
    data.courseLoading = false
  })
}

const saveCompletedCourses = () => {
  if (!data.completedStudent?.id || !data.completedForm.courseIds.length) {
    return
  }
  data.completedSaving = true
  request.post('/student/completedCourses', {
    studentId: data.completedStudent.id,
    courseIds: data.completedForm.courseIds
  }).then(res => {
    data.completedSaving = false
    if (res.code === '200') {
      ElMessage.success('已记录为完成课程')
      data.completedForm.courseIds = []
      loadCompletedCourses(data.completedStudent.id)
      queryCourseOptions(data.courseSearch)
      load()
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(() => data.completedSaving = false)
}

const removeCompletedCourse = (recordId) => {
  ElMessageBox.confirm('移除后该课程将不再计入学分，确定继续吗？', '移除确认', { type: 'warning' }).then(() => {
    request.delete(`/student/completedCourses/${recordId}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('已移除完成记录')
        loadCompletedCourses(data.completedStudent.id)
        queryCourseOptions(data.courseSearch)
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

load()
loadCollege()
</script>

<style scoped>
.completed-summary {
  display: flex;
  gap: 12px;
}
.stat-title {
  font-size: 14px;
  color: #6b7280;
}
.stat-value {
  font-size: 24px;
  font-weight: 600;
}
</style>
