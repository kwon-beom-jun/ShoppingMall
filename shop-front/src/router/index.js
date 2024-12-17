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
import itemMng from '@/views/item/itemMng.vue';
import itemDtl from '@/views/item/itemDtl.vue';

const routes = [
  {
    path: '/vue/',
    name: 'main',
    component: MainPage,
  },
  {
    path: '/vue/members/login',
    name: 'memberLogin',
    component: MemberLogin,
  },
  {
    path: '/vue/members/new',
    name: 'memberRegister',
    component: MemberRegister,
  },
  /** 
   * admin/item/new URL에서 router.currentRoute.value의 params 값을 보면 new가 들어가있고
   * 이로인해 new도 :itemId 값으로 치부되는것으로 추측되어 아래의 코드는 불필요하다고 생각
   * /vue/admin/item/:itemId, /vue/admin/item/new 동일하게 itemForm을 사용
   * 
  */
  {
  //path: '/vue/admin/item/new',
    path: '/vue/admin/item/:itemId',
    name: 'itemForm',
    component: itemForm,
    beforeEnter: async (to, from, next) => {
      const isAdmin = await authAdminCheck();
      // 관리자면 라우트 계속 진행 : 관리자가 아니면 메인 페이지로 리디렉션
      !isAdmin ? next({ name: 'main' }) : next();
    }
  },
  {
    path: '/vue/admin/items',
    name: 'itemMng',
    component: itemMng,
    beforeEnter: async (to, from, next) => {
      const isAdmin = await authAdminCheck();
      !isAdmin ? next({ name: 'main' }) : next();
    }
  },
  {
    path: '/vue/item/:itemId',
    name: 'itemDtl',
    component: itemDtl,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
