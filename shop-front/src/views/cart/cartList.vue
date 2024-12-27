<template>
  <div class="content-mg">
    <h2 class="mb-4">장바구니 목록</h2>

    <table class="table">
      <colgroup>
        <col width="15%" />
        <col width="70%" />
        <col width="15%" />
      </colgroup>
      <thead>
        <tr class="text-center">
          <td>
            <input type="checkbox" id="checkall" v-model="checkAll" @change="toggleAll" /> 전체선택
          </td>
          <td>상품정보</td>
          <td>상품금액</td>
        </tr>
      </thead>
      <tbody>
        <tr v-for="cartItem in cartItems" :key="cartItem.cartItemId" class="text-center">
          <td class="text-center align-middle">
            <input type="checkbox" v-model="selectedItems" :value="cartItem.cartItemId" />
          </td>
          <td class="d-flex">
            <div class="repImgDiv align-self-center">
              <img :src="cartItem.imgUrl" class="rounded repImg" :alt="cartItem.itemNm" />
            </div>
            <div class="align-self-center">
              <span class="fs24 font-weight-bold">{{ cartItem.itemNm }}</span>
              <div class="fs18 font-weight-light">
                <span :id="'price_' + cartItem.cartItemId" :data-price="cartItem.price">{{ cartItem.price }}원</span>&nbsp;
                <input 
                  type="number" 
                  v-model.number="cartItem.count" 
                  min="1" 
                  @change="updateCartItemCount(cartItem)" 
                  class="form-control mr-2" 
                />
                <button type="button" class="close" @click="deleteCartItem(cartItem)">×</button>
              </div>
            </div>
          </td>
          <td class="text-center align-middle">
            <span :id="'totalPrice_' + cartItem.cartItemId">{{ cartItem.price * cartItem.count }}원</span>
          </td>
        </tr>
      </tbody>
    </table>

    <h2 class="text-center totalPrice">
      총 주문 금액 : <span id="orderTotalPrice" class="text-danger">{{ orderTotalPrice }}원</span>
    </h2>

    <div class="text-center mt-3">
      <button type="button" class="btn btn-primary btn-lg" @click="orders">주문하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const cartItems = ref([]);
const selectedItems = ref([]);
const checkAll = ref(false);
const orderTotalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => {
    if (selectedItems.value.includes(item.cartItemId)) {
      return total + item.price * item.count;
    }
    return total;
  }, 0);
});

async function fetchCartItems() {
  try {
    const response = await axios.get('/cart');
    cartItems.value = response.data;
  } catch (error) {
    console.error('Failed to fetch cart items:', error.response.data);
  }
}

function toggleAll() {
  if (checkAll.value) {
    selectedItems.value = cartItems.value.map(item => item.cartItemId);
  } else {
    selectedItems.value = [];
  }
}

async function updateCartItemCount(cartItem) {
  if (cartItem.count <= 0) {
    alert("수량은 1개 이상이어야 합니다.");
    return;
  }
  try {
    // PATCH 요청으로 수량 변경
    await axios.patch(`/cartItem/${cartItem.cartItemId}?count=${cartItem.count}`);
    alert("수량이 업데이트되었습니다.");
  } catch (error) {
    console.error('Failed to update cart item:', error.response.data);
    alert('수량 업데이트에 실패했습니다.');
  }
}

async function deleteCartItem(cartItem) {
  try {
    await axios.delete(`/cartItem/${cartItem.cartItemId}`);
    alert("장바구니에서 삭제되었습니다.");
    fetchCartItems(); // Refresh cart after deletion
  } catch (error) {
    console.error('Failed to delete cart item:', error.response.data);
  }
}

async function orders() {
  const selectedItemsData = selectedItems.value.map(cartItemId => {
    const item = cartItems.value.find(cartItem => cartItem.cartItemId === cartItemId);
    return { cartItemId: item.cartItemId, count: item.count };
  });

  try {
    await axios.post('/cart/orders', { cartOrderDtoList: selectedItemsData });
    alert('주문이 완료되었습니다.');
    // Redirect to the order page or another page
  } catch (error) {
    console.error('Failed to place order:', error.response.data);
    alert('주문에 실패했습니다.');
  }
}

onMounted(fetchCartItems);
</script>

<style scoped>
.content-mg {
  margin: 0 auto;  /* 좌우 여백 자동 설정으로 중앙 정렬 */
  margin-top: 2%;
  margin-bottom: 100px;
  max-width: 1200px; /* 최대 너비 설정 (원하는 대로 조정 가능) */
  width: 100%; /* 부모 컨테이너 너비를 100%로 설정 */
}

.table {
  width: 100%;
  border-collapse: collapse; /* 테이블 셀 간 간격 제거 */
}

.table td, .table th {
  padding: 15px;
  text-align: center; /* 텍스트를 가운데 정렬 */
  vertical-align: middle; /* 세로 정렬 */
}

.totalPrice {
  margin-top: 50px;
}

.form-control {
  width: 50px;
  text-align: right; /* 수량 입력 박스 내 값 오른쪽 정렬 */
}

.d-flex {
  width: 100%; /* 전체 너비 사용 */
  justify-content: center; /* 가운데 정렬 */
  align-items: center; /* 세로 가운데 정렬 */
}

.repImgDiv {
  margin-right: 30px;
  margin-left: 30px;
  height: auto;
}

.repImg {
  height: 100px;
  width: 100px;
}

.fs18 {
  font-size: 18px;
}

.fs24 {
  font-size: 24px;
}
</style>
