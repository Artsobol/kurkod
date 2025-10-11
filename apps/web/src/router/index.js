import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'
import EmployeesPage from "@/pages/EmployeesPage.vue";
import CellsPage from "@/pages/CellsPage.vue";
import ChickensPage from "@/pages/ChickensPage.vue";
import DietsPage from "@/pages/DietsPage.vue";
import AccountPage from "@/pages/AccountPage.vue";

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
  {
    path: '/diets',
    name: 'Diets',
    component: DietsPage,
    meta: { title: 'Diets' }
  },
  {
    path: '/account',
    name: 'Account',
    component: AccountPage,
    meta: { title: 'Account' }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
