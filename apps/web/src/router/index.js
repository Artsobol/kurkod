import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'
import EmployeesPage from "@/pages/EmployeesPage.vue";
import CellsPage from "@/pages/CellsPage.vue";
import ChickensPage from "@/pages/ChickensPage.vue";

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
  {
    path: '/cells',
    name: 'Cells',
    component: CellsPage,
    meta: { title: 'Cells' }
  },

  {
    path: '/chickens',
    name: 'Chickens',
    component: ChickensPage,
    meta: { title: 'Chickens' }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
