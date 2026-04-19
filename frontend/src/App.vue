<template>
  <!-- 全局页面框架：头部 + 路由页面内容 -->
  <el-container style="height: 100vh">
    <el-header>
      <h2>课程文档智能管理系统</h2>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>
