<template>
  <!-- 顶部导航 -->
  <NavTabs />
  <el-card style="margin-top: 16px">
    <!-- 文档上传表单：本示例直接上传文本内容 -->
    <el-form :model="form" label-width="90">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="课程名"><el-input v-model="form.courseName" /></el-form-item>
      <el-form-item label="文档类型"><el-input v-model="form.docType" /></el-form-item>
      <el-form-item label="作者"><el-input v-model="form.author" /></el-form-item>
      <el-form-item label="正文"><el-input type="textarea" :rows="8" v-model="form.content" /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">上传并智能分析</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { createDoc } from '../api/doc'
import NavTabs from '../components/NavTabs.vue'

const form = reactive({ title: '', courseName: '', docType: '', author: '', content: '' })

const submit = async () => {
  // 提交后端创建接口，后端会自动调用 NLP 进行分析
  const { data } = await createDoc(form)
  if (data.code === 0) {
    ElMessage.success(`上传成功，分类：${data.data.category || '待分析'}`)
  } else {
    ElMessage.error(data.message)
  }
}
</script>
