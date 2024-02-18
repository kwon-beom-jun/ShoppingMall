<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="4">
        <v-card class="pa-4" flat>
          <v-form>
            <v-text-field
              label="이메일주소"
              prepend-icon="mdi-email"
              type="email"
              v-model="email"
              placeholder="sample77@shop.com"
            ></v-text-field>
            <v-text-field
              label="비밀번호"
              prepend-icon="mdi-lock"
              type="password"
              v-model="password"
              placeholder="input password"
            ></v-text-field>
            <p v-if="loginErrorMsg" class="red--text">{{ loginErrorMsg }} <br/><br/></p>
            <v-row>
              <v-col cols="6">
                <v-btn color="primary" @click="handleLogin" block>로그인</v-btn>
              </v-col>
              <v-col cols="6">
                <v-btn color="primary" @click="goToRegister" block>회원가입</v-btn>
              </v-col>
            </v-row>
            <v-row justify="center" class="mt-4">
              <v-col cols="12">
                <v-btn color="yellow" dark block>카카오 로그인</v-btn>
              </v-col>
            </v-row>
            <v-row justify="center" class="mt-2">
              <v-col cols="12">
                <v-btn color="red" dark block>구글 로그인</v-btn>
              </v-col>
            </v-row>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
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
        this.$emit('login-success'); // 로그인 성공 이벤트 발생
        this.$router.push({ name: 'main' });
      } catch (error) {
        this.loginErrorMsg = error.message;
        console.log(error);
      }
    },
    goToRegister() {
      this.$router.push({ name: 'memberRegister' });
    }
  }
};
</script>

<style scoped>
.v-container{
  margin-top: 100px;
}
</style>