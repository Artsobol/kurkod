import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    meta: { title: 'Главная' }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
