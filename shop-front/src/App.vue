<template>
  <div id="app">
    <HeaderLayout />
    <router-view/>
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
