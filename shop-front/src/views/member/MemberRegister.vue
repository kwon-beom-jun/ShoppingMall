<template>
  <v-container>
    <!-- v-card의 테두리 제거 및 폼 크기 조정 -->
    <v-row justify="center">
      <v-col cols="12" md="4">
        <v-card class="pa-4" flat>
          <v-form @submit.prevent="submitForm">
            <v-text-field
              label="이름"
              prepend-icon="mdi-account"
              v-model="form.name"
              placeholder="이름을 입력해주세요"
            ></v-text-field>
            <v-text-field
              label="이메일주소"
              prepend-icon="mdi-email"
              type="email"
              v-model="form.email"
              placeholder="이메일을 입력해주세요"
            ></v-text-field>
            <v-text-field
              label="비밀번호"
              prepend-icon="mdi-lock"
              type="password"
              v-model="form.password"
              placeholder="비밀번호 입력"
            ></v-text-field>
            <v-text-field
              label="주소"
              prepend-icon="mdi-home"
              v-model="form.address"
              placeholder="주소를 입력해주세요"
            ></v-text-field>
            <v-row justify="center">
              <v-btn color="primary" type="submit" block>Submit</v-btn>
            </v-row>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios'; // Axios 임포트

const form = reactive({
  name: '',
  email: '',
  password: '',
  address: ''
});

const router = useRouter();

const submitForm = async () => {
  try {
    const response = await axios.post('/members/new', {
      name: form.name,
      email: form.email,
      password: form.password,
      address: form.address
    });
    // 성공적인 응답 처리
    alert(response.data);
    router.push({ name: 'main' });
  } catch (error) {
    if (error.response && error.response.data) {
      const errors = error.response.data;
      let message = '';
      // 오류 메시지가 객체인 경우, 각 필드의 오류 메시지를 추출
      if (typeof errors === 'object' && errors !== null) {
        for (const field in errors) {
          message += `${field} : ${errors[field]}\n`;
        }
      } else {
        // 오류 메시지가 단순 문자열인 경우
        message = errors;
      }
      alert(message);
    } else {
      alert("오류가 발생했습니다.");
    }
  }
};
</script>

<style scoped>
.v-container {
  margin-top: 100px;
}
</style>
