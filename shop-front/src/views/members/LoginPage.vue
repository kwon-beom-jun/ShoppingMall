<template>
  <div class="form-group">
    <label for="email">이메일주소</label>
    <input type="email" id="email" v-model="email" class="form-control" placeholder="sample77@shop.com">
    <label for="password">비밀번호</label>
    <input type="password" id="password" v-model="password" class="form-control" placeholder="input password">
    <p id="loginErrorMsg" class="error">{{ loginErrorMsg }}</p>
    <button type="button" class="btn btn-primary" @click="handleLogin">로그인</button>
    <button type="button" class="btn btn-primary" @click="goToRegister">회원가입</button>
  </div>
</template>

<script>
import { login } from '@/utils/auth.js';

export default {
  data() {
    return {
      email: '',
      password: '',
      loginErrorMsg: ''
    };
  },
  methods: {
    async handleLogin() {
      try {
        this.loginErrorMsg = '';
        const response = await login(this.email, this.password);
        console.log(response.data.token);
        // this.$router.push('/');
      } catch (error) {
        this.loginErrorMsg = error.message;
        console.log(error)
      }
    },
    goToRegister() {
      this.$router.push({ name: 'register' });
    }
  }
};
</script>
