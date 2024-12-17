<template>
  <v-app-bar app color="primary" dark>
    <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

    <v-toolbar-title>
      <router-link to="/vue" class="router-link">Beom Shop</router-link>
    </v-toolbar-title>

    <v-spacer></v-spacer>

    <v-text-field
      v-model="searchQuery"
      label="검색"
      single-line
      hide-details
    ></v-text-field>

    <v-btn icon>
      <v-icon>mdi-magnify</v-icon>
    </v-btn>

    <v-menu offset-y>
      <template #activator="{ props }">
        <v-btn text v-bind="props">
          Language
        </v-btn>
      </template>
      <v-list>
        <v-list-item @click="setLang('en_US')">
          <v-list-item-title>영어</v-list-item-title>
        </v-list-item>
        <v-list-item @click="setLang('ko_KR')">
          <v-list-item-title>한국어</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>

  <v-navigation-drawer v-model="drawer" app color="#FDFBFF">
    <v-list>
      <v-list-item v-if="isAdmin && isLoggedIn" href="/vue/admin/item/new">
        <v-list-item-title>상품 등록</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isAdmin && isLoggedIn" href="/vue/admin/items">
        <v-list-item-title>상품 관리</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isUser && isLoggedIn" href="/vue/cart">
        <v-list-item-title>장바구니</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isUser && isLoggedIn" href="/vue/orders">
        <v-list-item-title>구매이력</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="!isLoggedIn" href="/vue/members/login">
        <v-list-item-title>로그인</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="!isLoggedIn" href="/vue/members/new">
        <v-list-item-title>회원가입</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isLoggedIn" @click="logoutHandler">
        <v-list-item-title>로그아웃</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { logout } from '@/utils/auth.js';

const drawer = ref(false);
const searchQuery = ref('');

// 로그인 상태 확인
const isLoggedIn = computed(() => {
  return !!localStorage.getItem('jwtToken');
});

// 관리자 여부 확인
const isAdmin = computed(() => {
  return localStorage.getItem('userRole') === 'ROLE_ADMIN';
});

// 관리자 여부 확인
const isUser = computed(() => {
  return localStorage.getItem('userRole') === 'ROLE_USER';
});

const router = useRouter();

const setLang = (lang) => {
  console.log(lang);
};

// 검색 로직
// const search = () => {};

const logoutHandler = async () => {
  logout();
  await router.push({ name: 'main' });
  location.reload();
};
</script>

<style scoped>
.router-link {
  text-decoration: none; /* 하이퍼링크 텍스트에 밑줄 제거 */
  color: inherit; /* 부모 요소의 글자색을 상속받음 */
  cursor: pointer; /* 커서 모양을 포인터로 변경하여 클릭 가능한 것처럼 보이도록 함 */
}
.v-text-field {
  max-width: 20%;
}
</style>
