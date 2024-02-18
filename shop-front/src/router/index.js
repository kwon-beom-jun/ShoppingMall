// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import { authAdminCheck } from '@/utils/auth.js';

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
    beforeEnter: async (to, from, next) => {
      const isAdmin = await authAdminCheck();
      if (!isAdmin) {
        next({ name: 'main' }); // 관리자가 아니면 메인 페이지로 리디렉션
      } else {
        next(); // 관리자면 라우트 계속 진행
      }
    }
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
