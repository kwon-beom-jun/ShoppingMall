<template>
  <input type="hidden" v-model="itemFormDto.id" />
  <div class="container">
    <v-row>
      <v-col cols="6" class="previewImg">
        <div class="repImgDiv">
          <v-img 
						v-if="itemFormDto && itemFormDto.itemImgDtoList && itemFormDto.itemImgDtoList[0]"
						:src="itemFormDto.itemImgDtoList[0].imgUrl" 
						class="rounded repImg" 
						:alt="itemFormDto.itemNm" />
        </div>
      </v-col>
      <v-col cols="6">
        <v-badge :color="itemFormDto.itemSellStatus === 'SELL' ? 'primary' : 'red'" class="mgb-15">
          <template #badge>{{ itemFormDto.itemSellStatus === 'SELL' ? '판매중' : '품절' }}</template>
        </v-badge>

        <v-divider class="my-4"></v-divider>

        <div class="h4">{{ itemFormDto.itemNm }}</div>

        <div class="h4 text-danger text-left">
          <input type="hidden" v-model="price" />
          <span>{{ itemFormDto.price }}원</span>
        </div>

        <v-row class="mgt-30">
          <v-col cols="12" md="6">
            <v-text-field v-model="count" label="수량" type="number" :min="1" />
          </v-col>
        </v-row>

        <v-divider class="my-4"></v-divider>

        <div class="text-right mgt-50">
          <h5>결제 금액</h5>
          <h3 class="font-weight-bold" v-text="totalPrice"></h3>
        </div>

        <v-row v-if="itemFormDto.itemSellStatus === 'SELL'" class="text-right cart-order">
          <v-btn @click="addCart" class="btn-light border border-primary btn-lg">장바구니 담기</v-btn>
          <v-btn @click="order" class="btn-primary btn-lg">주문하기</v-btn>
        </v-row>
        <v-row v-else class="text-right">
          <v-btn class="btn-danger btn-lg">품절</v-btn>
        </v-row>
      </v-col>
    </v-row>

    <v-jumbotron class="mgt-30">
      <div class="detail">
        <h4 class="display-5">상품 상세 설명</h4>
        <v-divider class="my-4"></v-divider>
        <p class="lead">{{ itemFormDto.itemDetail }}</p>
      </div>
    </v-jumbotron>

    <v-row class="text-center" v-for="itemImg in itemFormDto.itemImgDtoList" :key="itemImg.id">
      <img v-if="itemImg.imgUrl" :src="itemImg.imgUrl" class="rounded mgb-20 item-img" alt="상품 이미지" />
    </v-row>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

// Vue Router 훅 사용
const router = useRouter();

const itemFormDto = ref({
  id: -1,
  itemNm: '',
  price: null,
  itemDetail: '',
  stockNumber: null,
  itemSellStatus: 'SELL',
  itemImgDtoList: [],
  itemImgFiles: []
});

// Data
const price = computed(() => itemFormDto.value.price);
const count = ref(1);
const totalPrice = ref(0);

// Computed
const calculateTotalPrice = computed(() => price.value * count.value);

// Watchers
watch([count, price], () => {
  totalPrice.value = calculateTotalPrice.value;
});

async function addCart() {
  const url = '/cart';
  const paramData = {
    itemId: itemFormDto.value.id,
    count: count.value
  };

  await axios.post(url, paramData)
  .then((result) => {
		console.log(result);
		alert('상품을 장바구니에 담았습니다.');
		// use router.push directly since we don't need `this`
		router.push('/vue');  // useRouter is used directly in <script setup>
	})
	.catch((error) => {
		if (error.status === 401) {
			alert('로그인 후 이용해주세요');
			router.push('/vue/members/login'); // use router.push directly
		} else {
			alert(error.responseText);
		}
	});
}

async function order() {
  const url = '/order';
  const paramData = {
    itemId: itemFormDto.value.id,
    count: count.value
  };

  await axios.post(url, paramData)
    .then((result) => {
      console.log(result);
      alert('주문이 완료 되었습니다.');
      // use router.push directly since we don't need `this`
      router.push('/vue');  // useRouter is used directly in <script setup>
    })
    .catch((result) => {
      if (result.response.status === 401) {
        alert('로그인 후 이용해주세요');
        router.push('/vue/members/login'); // use router.push directly
      } else {
        alert(result.response.data);
      }
    });
}

// 페이지 초기화 및 아이템 데이터 로드 함수
async function init() {

  const itemId = router.currentRoute.value.params.itemId;
	try {
		const response = await axios.get(`/item/${itemId}`);

		if (response.status === 200) {
			// 하나 이상의 소스 객체로부터 대상 객체로 속성을 복사하는 메서드
			Object.assign(itemFormDto.value, response.data);
			// 이미지 데이터도 로드
			// if (response.data.itemImgDtoList && response.data.itemImgDtoList.length > 0) {
			// 	previewImageUrls.value = response.data.itemImgDtoList.map((img, index) => {
			// 		return `${img.imgUrl}`;
			// 	});
			// }
		}
	} catch (error) {
		alert('아이템을 조회하는중 오류가 발생했습니다.');
		console.log('Error loading item details:', error);
	}
}

// Mounted lifecycle hook
onMounted(() => {
	init();
});
</script>

<style scoped>
.mgb-15 {
  margin-bottom: 0px;
  margin-top: 20px;
}

.mgt-30 {
  margin-top: 10px;
}

.mgt-50 {
  margin-top: 50px;
}

.previewImg {
  display: flex;
  justify-content: center;
}

.repImgDiv {
  height: auto;
  width: 70%;
}

.repImg {
  width: 100%;
  height: 400px;
}

.wd50 {
  height: auto;
  width: 50%;
}

.detail {
  margin-top: 20px;
  margin-bottom: 50px;
}

.container {
  margin-top: 20px;
  margin-bottom: 50px;
}

.cart-order {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}

.item-img {
  width: 40%;
  height: auto;
}

.text-center {
  display: flex;
  justify-content: center !important;
}
</style>
