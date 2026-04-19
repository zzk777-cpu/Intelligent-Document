<template>
  <NavTabs />
  <el-card style="margin-top: 16px">
    <!-- 拉取文档列表 -->
    <el-button type="primary" @click="load">刷新</el-button>
    <el-table :data="rows" style="margin-top: 12px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="courseName" label="课程" width="120" />
      <el-table-column prop="category" label="分类" width="120" />
      <el-table-column prop="keywords" label="关键词" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button size="small" @click="analyze(scope.row.id)">重跑分析</el-button>
          <el-button size="small" type="danger" @click="remove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import NavTabs from '../components/NavTabs.vue'
import { deleteDoc, listDocs, runIntelligence } from '../api/doc'

const rows = ref([])

const load = async () => {
  // 默认拉取最近更新的前 50 条
  const { data } = await listDocs({ pageNum: 1, pageSize: 50 })
  rows.value = data.data || []
}

const analyze = async (id) => {
  // 手动重跑分类与关键词提取
  await runIntelligence(id)
  ElMessage.success('分析完成')
  await load()
}

const remove = async (id) => {
  // 删除文档后刷新列表
  await deleteDoc(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>
