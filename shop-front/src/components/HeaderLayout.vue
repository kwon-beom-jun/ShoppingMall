<template>
  <v-app-bar app color="primary" dark>
    <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

    <v-toolbar-title>
      <router-link to="/" class="router-link">Shop</router-link>
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
      <template v-slot:activator="{ on, attrs }">
        <v-btn text v-bind="attrs" v-on="on">
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
      <v-list-item v-if="isAdmin" href="/admin/item/new">
        <v-list-item-title>상품 등록</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isAdmin" href="/admin/items">
        <v-list-item-title>상품 관리</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isUser" href="/cart">
        <v-list-item-title>장바구니</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isUser" href="/orders">
        <v-list-item-title>구매이력</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="!isLoggedIn" href="/members/login">
        <v-list-item-title>로그인</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="!isLoggedIn" href="/members/new">
        <v-list-item-title>회원가입</v-list-item-title>
      </v-list-item>
      <v-list-item v-if="isLoggedIn" @click="logout">
        <v-list-item-title>로그아웃</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import { computed } from 'vue';
import { logout } from '@/utils/auth.js';

export default {
  
  setup() {
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

    return {
      isLoggedIn,
      isAdmin,
      isUser
    };
  },

  data() {
    return {
      drawer: false,
      // isLoggedIn: false, // 사용자 로그인 상태
      // isAdmin: false, // 관리자 여부
      // isUser: false, // 일반 사용자 여부
      searchQuery: '' // 검색 쿼리
    };
  },

  methods: {
    setLang(lang) {
      // 다국어 설정 로직
      console.log(lang);
    },
    search() {
      // 검색 로직
    },
    async logout() {
      logout();
      await this.$router.push({name: 'main'});
      location.reload();
    }
  }
};
</script>

<style scoped>
.router-link {
  text-decoration: none; /* 하이퍼링크 텍스트에 밑줄 제거 */
  color: inherit; /* 부모 요소의 글자색을 상속받음 */
  cursor: pointer; /* 커서 모양을 포인터로 변경하여 클릭 가능한 것처럼 보이도록 함 */
}
.v-text-field{
  max-width: 20%;
}
</style>