<template>
  <v-footer color="primary">
    <v-row justify="center" no-gutters>
      <v-btn
        v-for="link in links"
        :key="link"
        variant="text"
        class="mx-2"
        rounded="xl"
      >
        {{ link }}
      </v-btn>
      <v-col class="text-center mt-4" cols="12">
        {{ new Date().getFullYear() }} Shopping Mall Example WebSite
      </v-col>
    </v-row>
  </v-footer>
</template>

<script setup>
import { reactive } from 'vue'; // Vue 3의 Composition API에서 reactive 함수를 가져옵니다.

// links 변수를 반응형으로 만듭니다.
const links = reactive([
  'HOME',
  'ABOUT US',
  'TEMA',
  'GITHUB',
  'SERVICES',
  'BLOG',
  'CONTACT US'
]);
</script>

<style>
/* 추가한 스타일 */
.v-footer {
  bottom: 0;
  width: 100%;
}
</style>
