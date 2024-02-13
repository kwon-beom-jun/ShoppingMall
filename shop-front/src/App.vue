<template>
  <div id="app">

    <HeaderLayout />
    
    <div class="content">
      <router-view/>
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
