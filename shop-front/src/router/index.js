// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';

// main
import MainPage from '@/views/MainPage.vue';

// member
import MemberLogin from '@/views/member/MemberLogin.vue';
import MemberRegister from '@/views/member/MemberRegister.vue';

// item
import ItemRegister from '@/views/item/ItemRegister.vue';

const routes = [
  {
    path: '/',
    name: 'main',
    component: MainPage,
  },
  {
    path: '/members/login',
    name: 'memberLogin',
    component: MemberLogin,
  },
  {
    path: '/members/new',
    name: 'memberRegister',
    component: MemberRegister,
  },
  {
    path: '/admin/item/new',
    name: 'itemRegister',
    component: ItemRegister,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
