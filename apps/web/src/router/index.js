import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'
import WorkersPage from "@/pages/WorkersPage.vue";
import CellsPage from "@/pages/CellsPage.vue";
import ChickensPage from "@/pages/ChickensPage.vue";
import DietsPage from "@/pages/DietsPage.vue";
import AccountPage from "@/pages/AccountPage.vue";
import SignInPage from "@/pages/SignInPage.vue";
import WorkerPage from "@/pages/WorkerPage.vue";
import ChickenPage from "@/pages/ChickenPage.vue";

const routes = [
  {
    path: '/',
    name: 'Главная',
    component: HomePage,
    meta: { title: 'Home' }
  },
  {
    path: '/employees',
    name: 'Сотрудники',
    component: WorkersPage,
    meta: { title: 'Employees' }
  },
  {
    path: '/cells',
    name: 'Ячейки',
    component: CellsPage,
    meta: { title: 'Cells' }
  },
  {
    path: '/chickens',
    name: 'Курицы',
    component: ChickensPage,
    meta: { title: 'Chickens' }
  },
  {
    path: '/diets',
    name: 'Диеты',
    component: DietsPage,
    meta: { title: 'Diets' }
  },
  {
    path: '/account',
    name: 'Ваш профиль',
    component: AccountPage,
    meta: { title: 'Account' }
  },
  {
    path: '/sign',
    name: 'Sign',
    component: SignInPage,
    meta: { title: 'Sign' }
  },
  {
    path: '/employee/:id',
    name: 'Сотрудник',
    component: WorkerPage,
    meta: { title: 'Employee' }
  },
  {
    path: '/chickens/:id',
    name: 'Курица',
    component: ChickenPage,
    meta: { title: 'Chicken' }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
