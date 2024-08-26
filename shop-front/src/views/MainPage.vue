<template>
  <v-container>
    <!-- 캐러셀 -->
    <v-carousel hide-delimiter-background height="350px" class="margin" cycle interval="3000">
      <v-carousel-item v-for="(slide, i) in slides" :key="i">
        <v-img
          :src="slide.src"
          class="banner"
          alt="Banner Image"
          cover
        ></v-img>
      </v-carousel-item>
    </v-carousel>

    <!-- 검색 결과 표시 -->
    <v-row v-if="itemSearchDto.searchQuery" class="center">
      <v-col>
        <p class="h3 font-weight-bold">{{ itemSearchDto.searchQuery }} 검색 결과</p>
      </v-col>
    </v-row>

    <!-- 상품 리스트 카드 -->
    <v-row>
      <v-col cols="12" md="4" v-for="item in items" :key="item.id" class="margin">
        <v-card>
          <v-img
            :src="item.imgUrl"
            class="card-img-top"
            :alt="item.itemNm"
            height="400"
          ></v-img>
          <v-card-title>{{ item.itemNm }}</v-card-title>
          <v-card-text class="card-text">{{ item.itemDetail }}</v-card-text>
          <v-card-subtitle class="text-danger">{{ item.price }}원</v-card-subtitle>
          <v-btn :href="'/vue/item/' + item.id" color="primary" class="ma-2">자세히 보기</v-btn>
        </v-card>
      </v-col>
    </v-row>

    <!-- 페이징 -->
    <v-pagination
      v-model="currentPage"
      :length="totalPages"
      @update:modelValue="fetchItems"
      class="text-center"
    ></v-pagination>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const items = ref([]);
const itemSearchDto = ref({
  searchQuery: ''
});

const currentPage = ref(1);
const totalPages = ref(1);

const slides = ref([
  { src: require('@/assets/images/banner-1.jpg') },
  { src: require('@/assets/images/banner-2.jpg') },
  { src: require('@/assets/images/banner-3.jpg') }
]);

async function fetchItems() {
  try {
    const response = await axios.get('/main/item', {
      params: {
        searchQuery: itemSearchDto.value.searchQuery,
        page: currentPage.value - 1
      }
    });
    items.value = response.data.items.content;
    totalPages.value = response.data.items.totalPages;
  } catch (error) {
    console.error('Error fetching items:', error);
  }
}

async function init() {
  try {
    const response = await axios.get('/main/item');
    items.value = response.data.items.content;
    totalPages.value = response.data.items.totalPages;
  } catch (error) {
    console.error('Error fetching items:', error);
  }
}

onMounted(init);
</script>

<style scoped>
.margin {
  margin-bottom: 30px;
}
.banner {
  height: 300px;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.card-text {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.center {
  text-align: center;
}
</style>
