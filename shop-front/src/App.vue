<!-- BootStrap Vue : https://bootstrap-vue.org/docs/components/form-select -->
<!-- Vuetify : https://vuetifyjs.com/en/components/combobox/#props -->
<template>
  <div id="app">

    <HeaderLayout :key="componentKey" />
    
    <div class="content">
      <router-view @login-success="handleLoginSuccess" />
    </div>
    
    <FooterLayout />
    
  </div>
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
html {
    position: relative;
    min-height: 100%;
    margin: 0;
}
body {
    min-height: 100%;
}
.content{
    margin-bottom:100px;
    margin-top: 50px;
    margin-left: 200px;
    margin-right: 200px;
}
</style>
