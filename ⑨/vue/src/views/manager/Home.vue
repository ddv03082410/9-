<template>
  <div class="home-wrapper">
    <section class="hero card hero-card">
      <div>
        <p class="hero-eyebrow">Hi，{{ data.user.name }}</p>
        <h2>欢迎回到 ⑨号教务系统</h2>
        <p class="hero-desc">智能推荐、全新界面与信工课程资源全部就绪，开启你本学期的修读旅程。</p>
      </div>
      <div class="hero-actions">
        <el-button type="primary" @click="router.push('/course')">去选课</el-button>
        <el-button text @click="router.push('/choice')">查看我的课表</el-button>
      </div>
    </section>

    <section class="panel-row">
      <article class="card panel">
        <div class="panel-header">
          <div>
            <h3>系统公告</h3>
            <p>实时掌握最新教务通知</p>
          </div>
          <el-tag type="info" effect="dark">{{ data.noticeData.length }} 条</el-tag>
        </div>
        <el-timeline>
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
          >
            <strong>{{ item.title }}</strong> · {{ item.content }}
          </el-timeline-item>
        </el-timeline>
      </article>

      <article class="card panel project-panel">
        <h3>系统速览</h3>
        <ul>
          <li>全新配色与玻璃拟态布局，暗色顶部导航。</li>
          <li>课程新增学分、前置课程与智能推荐标签。</li>
          <li>预置人工智能、云原生、安全攻防等热门课程。</li>
        </ul>
        <div class="tech-stack">
          <span>Spring Boot 3</span>
          <span>Vue 3 + Vite</span>
          <span>Element Plus</span>
          <span>MyBatis</span>
        </div>
      </article>
    </section>

    <section v-if="data.user.role === 'STUDENT'" class="card recommend-panel">
      <div class="panel-header">
        <div>
          <h3>智能推荐选课</h3>
          <p>满足前置课程的班次将自动浮现</p>
        </div>
        <el-button text type="primary" @click="router.push('/course')">查看更多</el-button>
      </div>
      <div class="recommend-grid">
        <div v-for="item in data.recommendData" :key="item.id" class="recommend-card">
          <div class="recommend-head">
            <span class="recommend-name">{{ item.name }}</span>
            <el-tag type="success">{{ item.credit }} 学分</el-tag>
          </div>
          <p class="recommend-body">{{ item.content }}</p>
          <div class="recommend-meta">
            <span><el-icon><User /></el-icon>{{ item.teacherName }}</span>
            <span><el-icon><Clock /></el-icon>{{ item.time }}</span>
          </div>
          <div class="recommend-prereq" v-if="item.prerequisiteNames">
            <small>前置课程：{{ item.prerequisiteNames }}</small>
          </div>
        </div>
        <p v-if="!data.recommendData.length" class="empty-tip">
          暂无推荐。完成更多课程后将智能推送进阶学习路线。
        </p>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {Clock, User} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  noticeData: [],
  recommendData: []
})

const router = useRouter()

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadRecommend = () => {
  if (data.user.role !== 'STUDENT') {
    return
  }
  request.get('/course/recommend', { params: { studentId: data.user.id }}).then(res => {
    if (res.code === '200') {
      data.recommendData = res.data || []
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()
loadRecommend()
</script>

<style scoped>
.home-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.hero-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #1d4ed8, #0ea5e9);
  color: #fff;
}

.hero-eyebrow {
  letter-spacing: 2px;
  font-size: 12px;
  text-transform: uppercase;
  opacity: .85;
}

.hero-card h2 {
  margin: 8px 0;
  font-size: 28px;
}

.hero-desc {
  margin: 0;
  max-width: 480px;
  opacity: .9;
}

.hero-actions {
  display: flex;
  gap: 10px;
}

.panel-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 16px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.panel-header h3 {
  margin: 0;
}

.panel-header p {
  margin: 2px 0 0;
  color: #6c7280;
  font-size: 13px;
}

.project-panel ul {
  padding-left: 18px;
  color: #3b3f52;
}

.tech-stack {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.tech-stack span {
  padding: 4px 10px;
  border-radius: 999px;
  background-color: #eef2ff;
  color: #4c1d95;
  font-size: 12px;
}

.recommend-panel {
  margin-top: 8px;
}

.recommend-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 12px;
}

.recommend-card {
  border: 1px solid #eef2ff;
  border-radius: 12px;
  padding: 12px;
  background: #fff;
  box-shadow: 0 8px 16px rgba(15,23,42,.06);
}

.recommend-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.recommend-name {
  font-weight: 600;
  font-size: 16px;
}

.recommend-body {
  color: #4b5563;
  font-size: 13px;
  min-height: 42px;
}

.recommend-meta {
  display: flex;
  gap: 12px;
  color: #0f172a;
  font-size: 12px;
}

.recommend-meta span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.recommend-prereq {
  margin-top: 6px;
  font-size: 12px;
  color: #6b7280;
}

.empty-tip {
  grid-column: 1 / -1;
  text-align: center;
  margin: 0;
  color: #9ca3af;
}

@media (max-width: 768px) {
  .hero-card {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }

  .hero-actions {
    justify-content: center;
  }
}
</style>
