import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

const app = createApp(App);

// Axios 요청 인터셉터 추가
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('jwtToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  console.log('main.js에서 JWT Axios 인터셉터 추가 에러')
  return Promise.reject(error);
});

// axios.defaults.withCredentials = true;

app.config.globalProperties.$axios = axios; // 전역으로 axios 설정
app.use(router);

app.mount('#app');
