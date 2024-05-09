<template>
  <v-container>
    <v-form ref="form" @submit.prevent="submitForm">
      <v-row>
        <v-col cols="9"> <!-- 입력 필드 부분 -->
          <!-- 상품명과 판매 상태 -->
          <v-row>
            <v-col cols="9">
              <v-text-field
                v-model="itemFormDto.itemNm"
                label="상품명"
                required
                solo
                dense
                :rules="[v => !!v || '상품명 입력은 필수입니다']"
              ></v-text-field>
            </v-col>
            <v-col cols="3">
              <v-select
                v-model="itemFormDto.itemSellStatus"
                :items="itemSellStatusOptions"
                label="판매 상태"
                required
                solo
                dense
              ></v-select>
            </v-col>
          </v-row>
          <!-- 가격과 재고 -->
          <v-row>
            <v-col cols="9">
              <v-text-field
                v-model="itemFormDto.price"
                label="가격"
                type="number"
                required
                solo
                dense
                :rules="[v => !!v || '가격 입력은 필수입니다']"
              ></v-text-field>
            </v-col>
            <v-col cols="3">
              <v-text-field
                v-model="itemFormDto.stockNumber"
                label="재고"
                type="number"
                required
                solo
                dense
                :rules="[v => !!v || '재고 수 입력은 필수입니다']"
              ></v-text-field>
            </v-col>
          </v-row>
          <!-- 상품 상세 내용 -->
          <v-textarea
            v-model="itemFormDto.itemDetail"
            label="상품 상세 내용"
            required
            solo
            dense
            :rules="[v => !!v || '상세 내용 입력은 필수입니다']"
          ></v-textarea>
          <!-- 상품 이미지 업로드 -->
          <v-file-input
            v-model="itemFormDto.itemImgFiles"
            label="상품 이미지"
            accept="image/*"
            multiple
            clearable
            show-size
            dense
            :rules="fileRules"
            placeholder="파일을 선택하세요"
            @change="handleFileUpload"
            @input="updateFileInput"
          ></v-file-input>
          <!-- 저장 버튼 -->
          <v-btn color="primary" block large type="submit">
            {{ itemFormDto.id ? '수정' : '저장' }}
          </v-btn>
        </v-col>
        <v-col cols="3"> <!-- 이미지 미리보기 부분 -->
          <div class="image-preview-container" style="height: 100%; overflow-y: auto;">
            <v-row v-for="(url, index) in previewImageUrls" :key="index">
              <v-col cols="12" class="image-preview">
                <img :src="url" alt="Image Preview">
              </v-col>
            </v-row>
          </div>
        </v-col>
      </v-row>
    </v-form>
  </v-container>
</template>



<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

// 상태 및 상수 정의
const itemFormDto = ref({
  id: null, // 수정 시 해당 아이템의 ID. 새로 등록 시에는 null.
  itemNm: '',
  price: null,
  itemDetail: '',
  stockNumber: null,
  itemSellStatus: 'SELL',
  itemImgFiles: []
});
const previewImageUrls = ref([]);
const itemSellStatusOptions = ['SELL', 'SOLD_OUT'];
const fileRules = [
  v => !v || (v.length === 0 || v.some(file => file.size <= 20000000)) || '파일 크기는 20MB 이하여야 합니다.'
];

// 수정인지 등록인지 판별하는 계산된 속성
const isEditMode = computed(() => itemFormDto.value.id != null);

// 파일 업로드 처리 함수
function handleFileUpload(event) {
  const files = event.target.files;
  previewImageUrls.value = [];
  Array.from(files).forEach(file => {
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader();
      reader.onload = (e) => {
        previewImageUrls.value.push(e.target.result);
      };
      reader.readAsDataURL(file);
    }
  });
}

// 파일 입력 변경 처리 함수
function updateFileInput(value) {
  if (!value || value.length === 0) {
    previewImageUrls.value = [];
  }
}

// 폼 제출 함수
async function submitForm() {

  const formData = new FormData();
  itemFormDto.value.id = 11;

  // itemFormDto의 기타 필드들을 formData에 추가
  for (const [key, value] of Object.entries(itemFormDto.value)) {
    if (key !== 'itemImgFiles') { // 파일이 아닌 필드들을 처리
      formData.append(key, value);
    }
  }


  // 파일이 없는 경우 등록 하라고 alert 추가해주기


  // 파일이 있을 경우 formData에 추가
  if (itemFormDto.value.itemImgFiles && itemFormDto.value.itemImgFiles.length > 0) {
    itemFormDto.value.itemImgFiles.forEach((file) => {
      formData.append('itemImgFiles', file);
    });
  }

  try {
    if (isEditMode.value) {
      // 수정 로직을 구현
      const response = await axios.patch(`/api/admin/item/${itemFormDto.value.id}`, formData);
      console.log('수정 성공');
    } else {
      // 등록 로직을 구현
      const response = await axios.post('/api/admin/item/new', formData);
      console.log('등록 성공');
    }
  } catch (error) {
    if (error.response) {
      // 서버에서 응답으로 오류 메시지를 보냈을 때
      console.log("서버 에러 메시지:", error.response.data);
      // 여기서 error.response.data가 "상품 값을 확인해 주세요."를 포함하고 있을 수 있습니다.
    } else if (error.request) {
      // 요청은 이루어졌으나 응답을 받지 못했을 때
      console.log("No response received");
    } else {
      // 요청 설정 중 문제가 발생했을 때
      console.log("Error", error.message);
    }
  }
}


// 페이지 초기화 및 아이템 데이터 로드 함수
function init() {
  // 예를 들어, 라우트 파라미터에서 ID를 가져오는 로직을 구현할 수 있음
  // 이 ID에 따라서 itemFormDto.value.id를 설정
  // Vue Router를 사용하는 경우 아래와 같이 사용
  // const route = useRoute();
  // itemFormDto.value.id = route.params.itemId;

  if (isEditMode.value) {
    // 수정 모드일 때, 해당 아이템의 데이터를 로드
  }
}

// 컴포넌트가 마운트되었을 때 init 함수를 호출
onMounted(init);

</script>




<style scoped>
.image-preview-container {
  border: 1px solid #ddd;
  background-color: #f8f8f8; /* 배경색 추가 */
  box-shadow: 0 4px 6px rgba(0,0,0,0.1); /* 그림자 효과 추가 */
  overflow-y: auto; /* 스크롤 가능하도록 설정 */
  max-height: 600px; /* 컨테이너 최대 높이 설정 */
  margin-left: 3%;
}

.image-preview {
  margin-top: 10px; /* 이미지 하단 마진 추가 */
  padding: 10px; /* 이미지 주변 패딩 추가 */
}

/* 이미지 레이아웃 */
.v-col-12 {
  width: 100%;
  padding: 0px;
  background-color: rgb(53, 53, 53);
}

img {
  width: 100%; /* 이미지 너비를 컨테이너에 맞춤 */
  height: auto; /* 이미지 높이 자동 조절 */
}

.v-container {
  max-width: 100%;
  margin: auto;
}

.v-select, .v-text-field, .v-textarea, .v-file-input {
  background-color: #ffffff;
  margin: 10px;
}

.v-btn {
  height: 50px;
  text-transform: none;
}

.v-col-3, .v-col-9 {
  width: 100%;
  padding: 1px;
}
</style>






