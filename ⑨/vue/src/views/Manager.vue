<template>
  <div class="app-shell">
    <header class="shell-header">
      <div class="brand">
        <img src="@/assets/imgs/logo.png" alt="logo">
        <div class="brand-copy">
          <div class="brand-title">⑨号教务系统</div>
          <div class="brand-subtitle">Nine EDU · 智能选课助手</div>
        </div>
      </div>
      <div class="user-info">
        <img :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
        <div>
          <div class="user-name">{{ data.user.name }}</div>
          <div class="user-role">{{ data.user.role }}</div>
        </div>
      </div>
    </header>

    <div class="shell-body">
      <aside class="shell-sider">
        <el-menu
            router
            class="nav-menu"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['/home', '2']"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><Promotion /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/notice" v-if="data.user.role === 'ADMIN'"><el-icon><Bell /></el-icon><span>公告信息</span></el-menu-item>
            <el-menu-item index="/college"><el-icon><OfficeBuilding /></el-icon><span>学院信息</span></el-menu-item>
            <el-menu-item index="/speciality"><el-icon><School /></el-icon><span>专业信息</span></el-menu-item>
            <el-menu-item index="/course"><el-icon><Reading /></el-icon><span>课程信息</span></el-menu-item>
            <el-menu-item index="/choice"><el-icon><Reading /></el-icon><span>选课信息</span></el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Avatar /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin"><el-icon><User /></el-icon><span>管理员信息</span></el-menu-item>
            <el-menu-item index="/teacher"><el-icon><User /></el-icon><span>教师信息</span></el-menu-item>
            <el-menu-item index="/student"><el-icon><User /></el-icon><span>学生信息</span></el-menu-item>
          </el-sub-menu>
          <el-menu-item v-if="data.user.role === 'ADMIN'" index="/person"><el-icon><User /></el-icon><span>个人资料</span></el-menu-item>
          <el-menu-item v-if="data.user.role === 'TEACHER'" index="/tPerson"><el-icon><User /></el-icon><span>个人资料</span></el-menu-item>
          <el-menu-item v-if="data.user.role === 'STUDENT'" index="/sPerson"><el-icon><User /></el-icon><span>个人资料</span></el-menu-item>
          <el-menu-item index="/password">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <main class="shell-content">
        <router-view @updateUser="updateUser" />
      </main>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
})

if (!data.user?.id) {
  ElMessage.error('请登录！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('system-user') || '{}')
}

const logout = () => {
  ElMessage.success('退出成功')
  localStorage.removeItem('system-user')
  router.push('/login')
}
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
  background: radial-gradient(circle at top, #1a2a6c 0%, #1f2a47 35%, #0f172a 100%);
  display: flex;
  flex-direction: column;
}

.shell-header {
  height: 72px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #f8fbff;
  background: linear-gradient(120deg, rgba(59,130,246,.9), rgba(14,116,144,.95));
  box-shadow: 0 6px 12px rgba(15,23,42,.35);
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand img {
  width: 46px;
  height: 46px;
  border-radius: 8px;
  background-color: rgba(255,255,255,.2);
  padding: 4px;
}

.brand-title {
  font-size: 22px;
  font-weight: 700;
}

.brand-subtitle {
  font-size: 12px;
  opacity: .85;
  letter-spacing: 1px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  text-align: right;
}

.user-info img {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  border: 2px solid rgba(255,255,255,.5);
}

.user-name {
  font-weight: 600;
}

.user-role {
  font-size: 12px;
  opacity: .85;
}

.shell-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.shell-sider {
  width: 220px;
  background: linear-gradient(180deg, #5bc0f8 0%, #1e9edb 60%, #1985c1 100%);
  border-right: 1px solid rgba(255,255,255,.12);
  box-shadow: inset -6px 0 16px rgba(7, 46, 79, .25);
}

.nav-menu {
  border: none;
  background: transparent;
  color: #d7e2ff;
}

.shell-content {
  flex: 1;
  padding: 18px;
  background: #f4f7fb;
  overflow-y: auto;
}

.el-menu-item.is-active {
  background: rgba(59,130,246,.15) !important;
}

.el-menu-item:hover {
  color: #38bdf8;
}

:deep(th)  {
  color: #333;
}
</style>
