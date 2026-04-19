<template>
  <NavTabs />
  <el-card style="margin-top: 16px">
    <!-- 关键词检索区域 -->
    <el-form inline>
      <el-form-item label="关键词"><el-input v-model="q" placeholder="输入内容关键词" /></el-form-item>
      <el-form-item><el-button type="primary" @click="search">检索</el-button></el-form-item>
    </el-form>

    <el-timeline>
      <el-timeline-item v-for="item in rows" :key="item.id" :timestamp="item.updatedAt">
        <h4>{{ item.title }}（{{ item.category || '未分类' }}）</h4>
        <p><b>关键词：</b>{{ item.keywords }}</p>
        <p>{{ (item.content || '').slice(0, 160) }}...</p>
      </el-timeline-item>
    </el-timeline>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import NavTabs from '../components/NavTabs.vue'
import { listDocs } from '../api/doc'

const q = ref('')
const rows = ref([])

const search = async () => {
  // 调用后端检索接口，按关键词返回匹配文档
  const { data } = await listDocs({ q: q.value, pageNum: 1, pageSize: 30 })
  rows.value = data.data || []
}
</script>
