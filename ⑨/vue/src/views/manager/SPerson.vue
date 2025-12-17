<template>
  <div style="width: 60%">
    <div class="card" style="padding: 30px">
      <el-form :model="data.user" label-width="100px" style="padding-right: 50px">
        <div style="margin: 20px 0; text-align: center">
          <el-upload :show-file-list="false" class="avatar-uploader" :action="uploadUrl" :on-success="handleFileUpload">
            <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <el-form-item label="账号">
          <el-input disabled v-model="data.user.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="data.user.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="data.user.sex" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="data.user.code" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="data.user.collegeName" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="data.user.specialityName" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="已获学分">
          <el-input v-model="data.user.score" autocomplete="off" disabled/>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </el-form>
    </div>

    <div class="card" style="margin-top: 20px">
      <div class="completed-header">
        <div>
          <h3>已完成课程</h3>
          <p>系统根据已完成课程与专业进行智能推荐</p>
        </div>
        <el-button type="primary" text @click="loadCompleted">刷新</el-button>
      </div>
      <el-table :data="data.completedCourses" stripe v-loading="data.completedLoading">
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="学分" prop="credit" width="80"></el-table-column>
        <el-table-column label="学院" prop="collegeName"></el-table-column>
        <el-table-column label="授课教师" prop="teacherName"></el-table-column>
        <el-table-column label="完成时间" prop="completedAt"></el-table-column>
      </el-table>
      <div class="completed-empty" v-if="!data.completedCourses.length && !data.completedLoading">
        暂无已修课程记录，完成课程后这里会自动汇总。
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {Plus} from "@element-plus/icons-vue";

const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  user: {},
  completedCourses: [],
  completedLoading: false,
})

const handleFileUpload = (file) => {
  data.user.avatar = file.data
}

const emit = defineEmits(["updateUser"])

const save = () => {
  request.put('/student/update', data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      localStorage.setItem('system-user', JSON.stringify(data.user))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadStudent = () => {
  let user = JSON.parse(localStorage.getItem('system-user') || '{}')
  request.get('/student/selectById/' + user.id).then(res => {
    if (res.code === '200') {
      data.user = res.data
      localStorage.setItem('system-user', JSON.stringify(res.data))
      loadCompleted()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadCompleted = () => {
  if (!data.user?.id) return
  data.completedLoading = true
  request.get('/student/completedCourses', { params: { studentId: data.user.id }}).then(res => {
    data.completedLoading = false
    if (res.code === '200') {
      data.completedCourses = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(() => data.completedLoading = false)
}

loadStudent()
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.completed-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.completed-header h3 {
  margin: 0;
}

.completed-header p {
  margin: 0;
  font-size: 13px;
  color: #6b7280;
}

.completed-empty {
  text-align: center;
  padding: 20px;
  color: #9ca3af;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
