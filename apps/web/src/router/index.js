import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'
import EmployeesPage from "@/pages/EmployeesPage.vue";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    meta: { title: 'Home' }
  },
  {
    path: '/employees',
    name: 'Employees',
    component: EmployeesPage,
    meta: { title: 'Employees' }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
