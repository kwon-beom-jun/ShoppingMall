<template>
    <div class="content-mg">
      <h2 class="mb-4">구매 이력</h2>
  
      <!-- 주문 목록 -->
      <div v-for="order in orders.content" :key="order.orderId">
        <div class="d-flex mb-3 align-self-center">
          <h4>{{ order.orderDate }} 주문</h4>
          <div class="ml-3">
            <button 
              v-if="order.orderStatus === 'ORDER'" 
              type="button" 
              class="btn btn-outline-secondary" 
              @click="cancelOrder(order.orderId)">
              주문취소
            </button>
            <h4 v-else>(취소 완료)</h4>
          </div>
        </div>
  
        <div class="card d-flex">
          <div v-for="orderItem in order.orderItemDtoList" :key="orderItem.id" class="d-flex mb-3">
            <div class="repImgDiv">
              <img :src="orderItem.imgUrl" class="rounded repImg" :alt="orderItem.itemNm" />
            </div>
            <div class="align-self-center w-75">
              <span class="fs24 font-weight-bold">{{ orderItem.itemNm }}</span>
              <div class="fs18 font-weight-light">
                <span>{{ orderItem.orderPrice }}원</span>
                <span>{{ orderItem.count }}개</span>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 페이지네이션 -->
      <div class="pagination-container">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ 'disabled': orders.number === 0 }">
            <a 
              href="#" 
              @click.prevent="changePage(orders.number - 1)"
              aria-label="Previous" 
              class="page-link">
              <span aria-hidden="true">Previous</span>
            </a>
          </li>
  
          <li 
            v-for="page in paginationRange" 
            :key="page" 
            class="page-item" 
            :class="{ 'active': orders.number === page - 1 }">
            <a 
              @click.prevent="changePage(page - 1)" 
              class="page-link">
              {{ page }}
            </a>
          </li>
  
          <li class="page-item" :class="{ 'disabled': orders.number + 1 >= orders.totalPages }">
            <a 
              href="#" 
              @click.prevent="changePage(orders.number + 1)"
              aria-label="Next" 
              class="page-link">
              <span aria-hidden="true">Next</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue';
  import axios from 'axios';
  
  const orders = ref({
    content: [],
    number: 0,
    totalPages: 0
  });
  const maxPage = 3; // 페이지 링크 갯수 
  const currentPage = ref(0);
  
  // 페이지네이션 계산
  const paginationRange = computed(() => {
    const start = Math.floor(currentPage.value / maxPage) * maxPage + 1;
    const end = Math.min(start + maxPage - 1, orders.value.totalPages);
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  });
  
  // 페이지 변경 함수
  function changePage(page) {
    if (page >= 0 && page < orders.value.totalPages) {
      currentPage.value = page;
      fetchOrders();
    }
  }
  
  // 주문 목록 가져오기
  async function fetchOrders() {
    try {
      const response = await axios.get(`/orders/${currentPage.value}`);
      orders.value = response.data;
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  }
  
  // 주문 취소 함수
  async function cancelOrder(orderId) {
    try {
      // const response = await axios.post(`/vue/order/${orderId}/cancel`);
      await axios.post(`/order/${orderId}/cancel`);
      alert('주문이 취소되었습니다.');
      fetchOrders();  // 주문 목록 갱신
    } catch (error) {
      if (error.response.status === 401) {
        alert('로그인 후 이용해주세요');
        window.location.href = '/vue/members/login';  // 로그인 페이지로 리디렉션
      } else {
        alert(error.response.data);
      }
    }
  }
  
  // 초기 데이터 로드
  onMounted(() => {
    fetchOrders();
  });
  </script>
  
  <style scoped>
  .content-mg {
    margin-left: 30%;
    margin-right: 30%;
    margin-top: 2%;
    margin-bottom: 100px;
  }
  .repImgDiv {
    margin-right: 15px;
    margin-left: 15px;
    height: auto;
  }
  .repImg {
    height: 100px;
    width: 100px;
  }
  .card {
    width: 750px;
    height: 100%;
    padding: 30px;
    margin-bottom: 20px;
  }
  .fs18 {
    font-size: 18px;
  }
  .fs24 {
    font-size: 24px;
  }
  .pagination-container {
    margin-top: 20px;
  }
  .pagination .page-item.active .page-link {
    background-color: #007bff;
    border-color: #007bff;
  }
  </style>
  