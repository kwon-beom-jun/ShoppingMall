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
              v-model="name"
              placeholder="이름을 입력해주세요"
            ></v-text-field>
            <v-text-field
              label="이메일주소"
              prepend-icon="mdi-email"
              type="email"
              v-model="email"
              placeholder="이메일을 입력해주세요"
            ></v-text-field>
            <v-text-field
              label="비밀번호"
              prepend-icon="mdi-lock"
              type="password"
              v-model="password"
              placeholder="비밀번호 입력"
            ></v-text-field>
            <v-text-field
              label="주소"
              prepend-icon="mdi-home"
              v-model="address"
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

<script>
export default {
  data() {
    return {
      name: '',
      email: '',
      password: '',
      address: ''
    };
  },
  methods: {
    submitForm() {
      // 회원가입 로직
    }
  }
};
</script>

<style scoped>
.v-container{
  margin-top: 100px;
}
</style>