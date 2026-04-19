import { createRouter, createWebHistory } from 'vue-router'
import DocListView from '../views/DocListView.vue'
import DocUploadView from '../views/DocUploadView.vue'
import SearchView from '../views/SearchView.vue'

const routes = [
  { path: '/', component: DocListView },
  { path: '/upload', component: DocUploadView },
  { path: '/search', component: SearchView }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
