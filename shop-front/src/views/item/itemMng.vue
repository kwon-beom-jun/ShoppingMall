<template>
  <v-container>
    <!-- 검색 필터 섹션 -->
    <v-form @submit.prevent="fetchItems">
      <v-row>
        <v-col cols="2">
          <v-select
            v-model="itemSearchDto.searchDateType"
            :items="dateOptions"
            text="text"
            value="value"
            label="검색 기간"
            ></v-select>
            <!-- return-object -->
        </v-col>
        <v-col cols="2">
          <v-select
            v-model="itemSearchDto.searchSellStatus"
            :items="sellStatusOptions"
            item-value="value"
            item-text="text"
            label="판매 상태"
            dense
          ></v-select>
        </v-col>
        <v-col cols="2">
          <v-select
            v-model="itemSearchDto.searchBy"
            :items="searchByOptions"
            item-value="value"
            item-text="text"
            label="검색 기준"
            dense
          ></v-select>
        </v-col>
        <v-col cols="2">
          <v-text-field
            v-model="itemSearchDto.searchQuery"
            label="검색어"
            dense
          ></v-text-field>
        </v-col>
        <v-col cols="2" class="text-center">
          <v-btn color="primary" @click="searchItems">검색</v-btn>
        </v-col>
      </v-row>
    </v-form>

    <!-- 상품 리스트 테이블 -->
    <v-table dense>
      <thead>
        <tr>
          <th>상품아이디</th>
          <th>상품명</th>
          <th>상태</th>
          <th>등록자</th>
          <th>등록일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="content in contents" :key="content.id">
          <td>{{ content.id }}</td>
          <td>
            <a :href="'/vue/admin/item/' + content.id">{{ content.itemNm }}</a>
          </td>
          <td>{{ content.itemSellStatus === 'SELL' ? '판매중' : '품절' }}</td>
          <td>{{ content.createdBy }}</td>
          <td>{{ content.regTime }}</td>
        </tr>
      </tbody>
    </v-table>

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


// 조회한 상품 데이터 및 페이징 정보
const items = ref([]);

// 검색 시 조건
const itemSearchDto = ref({
  searchDateType: '',
  searchSellStatus: '',
  searchBy: '',
  searchQuery: ''
});

// 페이지 전환 시 조건(마지막 검색 조건)
const itemFetchDto = ref({
  searchDateType: '',
  searchSellStatus: '',
  searchBy: '',
  searchQuery: ''
});
  
// 상품 데이터
const contents = ref([]);
 
// 페이징 정보
const currentPage = ref(1);
const totalPages = ref(1);
  
// 선택지 옵션들
const dateOptions = ['all', '1d', '1w', '1m', '6m'];
const sellStatusOptions = ['SELL', 'SOLD_OUT'];
const searchByOptions = ['itemNm', 'createdBy'];
 
// 데이터 로드 함수
async function searchItems() {
  try {
    const response = await axios.get(`/admin/items`, {
      params: {
        searchDateType: itemSearchDto.value.searchDateType,
        searchSellStatus: itemSearchDto.value.searchSellStatus,
        searchBy: itemSearchDto.value.searchBy,
        searchQuery: itemSearchDto.value.searchQuery,
      },
    });
    items.value = response.data.items;
    itemSearchDto.value = response.data.itemSearchDto;
    contents.value = items.value.content;
    totalPages.value = items.value.totalPages;

    if (currentPage.value > totalPages.value) {
      currentPage.value = totalPages.value;
    }
    
    itemFetchDto.value.searchDateType = itemSearchDto.value.searchDateType;
    itemFetchDto.value.searchSellStatus = itemSearchDto.value.searchSellStatus;
    itemFetchDto.value.searchBy = itemSearchDto.value.searchBy;
    itemFetchDto.value.searchQuery = itemSearchDto.value.searchQuery;
  } catch (error) {
    console.error('Error fetching items:', error);
  }
}
  
// 데이터 로드 함수
async function fetchItems() {
  try {
    const page = currentPage.value - 1;
    const response = await axios.get(`/admin/items/${page}`, {
      params: {
        searchDateType: itemFetchDto.value.searchDateType,
        searchSellStatus: itemFetchDto.value.searchSellStatus,
        searchBy: itemFetchDto.value.searchBy,
        searchQuery: itemFetchDto.value.searchQuery,
      },
    });
    items.value = response.data.items;
    itemSearchDto.value = response.data.itemSearchDto;
    contents.value = items.value.content;
    totalPages.value = items.value.totalPages;
  } catch (error) {
    console.error('Error fetching items:', error);
  }
}
  
// 페이지 초기화 및 아이템 데이터 로드 함수
async function init() {
  try {
    const response = await axios.get('/admin/items');
    items.value = response.data.items;

    itemSearchDto.value.searchDateType = 'all';
    itemSearchDto.value.searchSellStatus = 'SELL';
    itemSearchDto.value.searchBy = 'itemNm';
    itemSearchDto.value.searchQuery = '';
      
    itemFetchDto.value.searchDateType = 'all';
    itemFetchDto.value.searchSellStatus = 'SELL';
    itemFetchDto.value.searchBy = 'itemNm';
    itemFetchDto.value.searchQuery = '';
      
    contents.value = items.value.content;
    totalPages.value = items.value.totalPages;

  } catch (error) {
    alert('아이템을 조회하는 중 오류가 발생했습니다.');
    console.log('Error loading item details:', error);
  }
}
  
onMounted(init);
</script>
  
<style scoped>
  /* .v-container {
    max-width: 100%;
    padding: 20px;
  } */
  /* .text-center {
    text-align: center;
  } */
</style>
  