<template>
  <v-app>
    <!-- <v-app-bar app></v-app-bar> -->
    <HeaderLayout :key="componentKey" />
    <v-main class="main-content">
      <v-container class="content">
        <router-view />
      </v-container>
    </v-main>
    <v-footer app>
      <FooterLayout />
    </v-footer>
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
.main-content {
  min-height: calc(100vh - 64px); /* 전체 화면에서 헤더와 푸터를 제외한 영역 */
  display: flex;
  flex-direction: column;
}

.content {
  flex-grow: 1; /* 컨텐츠가 부족할 때도 공간을 채우도록 설정 */
}
</style>
