<template>
  <v-app>
    <v-main>
      <HeaderLayout :key="componentKey" />
      <v-container class="content">
        <router-view />
      </v-container>
      <FooterLayout />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import HeaderLayout from './components/HeaderLayout.vue';
import FooterLayout from './components/FooterLayout.vue';

const componentKey = ref(0);

const handleLoginSuccess = () => {
  // HeaderLayout 인스턴스 폐기 후 새로 생성(컴포넌트 리로드 효과)
  componentKey.value += 1;
  console.log("componentKey updated:", componentKey.value);
};

onMounted(() => {
  window.addEventListener('login-success', handleLoginSuccess);
});

onUnmounted(() => {
  window.removeEventListener('login-success', handleLoginSuccess);
});
</script>

<style scoped>
.content {
  margin-top: 50px;
}
</style>
