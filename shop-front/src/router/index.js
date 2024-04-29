// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import { authAdminCheck } from '@/utils/auth.js';

// main
import MainPage from '@/views/MainPage.vue';

// member
import MemberLogin from '@/views/member/MemberLogin.vue';
import MemberRegister from '@/views/member/MemberRegister.vue';

// item
import itemForm from '@/views/item/itemForm.vue';
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
    name: 'itemForm',
    component: itemForm,
    beforeEnter: async (to, from, next) => {
      const isAdmin = await authAdminCheck();
      // 관리자면 라우트 계속 진행 : 관리자가 아니면 메인 페이지로 리디렉션
      !isAdmin ? next({ name: 'main' }) : next();
    }
  },
  {
    path: '/admin/item/new2',
    name: 'itemRegister',
    component: ItemRegister,
    beforeEnter: async (to, from, next) => {
      const isAdmin = await authAdminCheck();
      !isAdmin ? next({ name: 'main' }) : next();
    }
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
