<template>
    <div class="form-group">
      <form @submit.prevent="submitForm">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" class="form-control" placeholder="이름을 입력해주세요">
        <label for="email">이메일주소</label>
        <input type="email" id="email" v-model="email" class="form-control" placeholder="이메일을 입력해주세요">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" class="form-control" placeholder="비밀번호 입력">
        <label for="address">주소</label>
        <input type="text" id="address" v-model="address" class="form-control" placeholder="주소를 입력해주세요">
        <div style="text-align: center">
          <button type="submit" class="btn btn-primary">Submit</button>
        </div>
      </form>
    </div>
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
  