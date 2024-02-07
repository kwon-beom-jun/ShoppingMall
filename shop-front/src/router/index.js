// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/views/members/LoginPage.vue';
import RegisterPage from '@/views/members/RegisterPage.vue';
import MainPage from '@/views/MainPage.vue';

const routes = [
  {
    path: '/members/login',
    name: 'login',
    component: LoginPage,
  },
  {
    path: '/members/new',
    name: 'register',
    component: RegisterPage,
  },
  {
    path: '/',
    name: 'main',
    component: MainPage,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
