<template>
  <el-menu :default-active="$route.path" mode="horizontal" router>
    <el-menu-item index="/">文档列表</el-menu-item>
    <el-menu-item index="/upload">上传文档</el-menu-item>
    <el-menu-item index="/search">内容检索</el-menu-item>
  </el-menu>
</template>
