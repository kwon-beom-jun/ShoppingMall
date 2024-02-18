<template>
  <v-app>
    <v-main>

      <!-- 여기에서 Vue 새로 고쳐서 저장하면 무한루프 에러문구 나오는것같음 -->
      <HeaderLayout :key="componentKey" />

      <v-container class="content">
        <router-view @login-success="handleLoginSuccess" />
      </v-container>

      <FooterLayout />
      
    </v-main>
  </v-app>
</template>

<script>
import HeaderLayout from './components/HeaderLayout.vue';
import FooterLayout from './components/FooterLayout.vue';

export default {
  name: 'App',
  
  components: {
    HeaderLayout,
    FooterLayout
  },

  data() {
    return {
      componentKey: 0
    };
  },

  methods: {
    handleLoginSuccess() {
      this.componentKey += 1; // HeaderLayout 인스턴스 폐기 후 새로 생성(컴포넌트 리로드 효과)
    }
  }
};
</script>

<style scoped>
.content {
  margin-top: 50px;
}
</style>
