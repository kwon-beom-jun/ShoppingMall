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

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '@/utils/auth.js';

const email = ref('');
const password = ref('');
const loginErrorMsg = ref('');
const router = useRouter();

const handleLogin = async () => {
  try {
    loginErrorMsg.value = '';
    const response = await login(email.value, password.value);
    console.log(response.data.token);
    router.push({ name: 'main' });
    const event = new CustomEvent('login-success');
    window.dispatchEvent(event); // 이벤트를 window 객체에 전달
  } catch (error) {
    loginErrorMsg.value = error.message;
    console.log(error);
  }
};

const goToRegister = () => {
  router.push({ name: 'memberRegister' });
};
</script>

<style scoped>
.v-container {
  margin-top: 100px;
}
</style>
