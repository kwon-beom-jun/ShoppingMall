<template>
  <nav class="navbar navbar-expand-sm bg-primary navbar-dark">

    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarTogglerDemo03" aria-controls="navbarTogglerDemo03"
            aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <a class="navbar-brand" href="/">Shop</a>

    <div class="collapse navbar-collapse" id="navbarTogglerDemo03">
      <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
        <li class="nav-item admin-item" v-if="isAdmin">
          <a class="nav-link" href="/admin/item/new">상품 등록</a>
        </li>
        <li class="nav-item admin-item" v-if="isAdmin">
          <a class="nav-link" href="/admin/items">상품 관리</a>
        </li>
        <li class="nav-item user-item" v-if="isUser">
          <a class="nav-link" href="/cart">장바구니</a>
        </li>
        <li class="nav-item user-item" v-if="isUser">
          <a class="nav-link" href="/orders">구매이력</a>
        </li>

        <li class="nav-item" v-if="!isLoggedIn">
          <a class="nav-link" href="/members/login">로그인</a>
        </li>
        <li class="nav-item" v-if="!isLoggedIn">
          <a class="nav-link" href="/members/new">회원가입</a>
        </li>
        <li class="nav-item" v-if="isLoggedIn">
          <a class="nav-link" @click="logout">로그아웃</a>
        </li>
      </ul>
    </div>
    
    <div>
      <input v-model="searchQuery" class="form-control mr-sm-2" type="search" placeholder="검색" aria-label="검색">
    </div>
    <div>
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">검색</button>
    </div>
  
    <div class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button"
        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        &nbsp;Language&nbsp;
      </a>
      <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
        <a class="dropdown-item" onclick="setLang('en_US')">영어</a>
        <a class="dropdown-item" onclick="setLang('ko_KR')">한국어</a>
      </div>
    </div>

  </nav>
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
    logout() {
      logout();
      location.reload();
      this.$router.push({name: 'main'});
    }
  }
};
</script>
  