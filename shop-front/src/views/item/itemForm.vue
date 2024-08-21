<template>
  <v-container>
    <v-form ref="form" @submit.prevent="submitForm">
      <v-row>
        <v-col cols="12">
          <!-- 입력 필드 부분 -->
          <!-- 상품명과 판매 상태 -->
          <v-row dense>
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
          <v-row dense>
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
          <!-- 상품 이미지 등록/수정 -->
          <div class="image-grid">
            <v-row dense>
              <v-col cols="4" v-for="index in 6" :key="index">
                <div class="image-box" @click="showFullScreenImage(index - 1)">
                  <img
                    v-if="previewImageUrls[index - 1]"
                    :src="previewImageUrls[index - 1]"
                    alt="Image Preview"
                  >
                </div>
                <v-btn
                  v-if="!previewImageUrls[index - 1]"
                  @click="triggerFileInput(index - 1)"
                  block
                  small
                >
                  등록
                </v-btn>
                <v-btn
                  v-else
                  @click="triggerFileInput(index - 1)"
                  block
                  small
                >
                  수정
                </v-btn>
                <v-btn
                  v-if="previewImageUrls[index - 1]"
                  @click="removeImage(index - 1)"
                  block
                  small
                  color="error"
                >
                  제거
                </v-btn>
              </v-col>
            </v-row>
          </div>
          <!-- 이미지 아래에 저장 버튼 -->
          <v-row dense>
            <v-col cols="12">
              <v-btn color="primary" block large type="submit">
                {{ isEditMode ? '수정' : '저장' }}
              </v-btn>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-form>
    <!-- 전체 화면 이미지 보기 -->
    <div v-if="isFullScreen" class="full-screen-overlay" @click="closeFullScreenImage">
      <img :src="fullScreenImageUrl" class="full-screen-image">
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

// Vue Router 훅 사용
const router = useRouter();

// 이미지 변경사항 확인용
const itemImgCheckList = ref([]);

// 초기 데이터 저장용
const originalItemFormDto = ref({
  id: -1,
  itemNm: '',
  price: null,
  itemDetail: '',
  stockNumber: null,
  itemSellStatus: 'SELL',
  itemImgDtoList: [],
  itemImgFiles: []
});

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

const labels = {
  itemNm: '상품명',
  price: '가격',
  itemDetail: '상품 상세 내용',
  stockNumber: '재고',
  itemSellStatus: '판매 상태'
};

const getLabel = (key) => {
  return labels[key] || key;
};

const previewImageUrls = ref([]);
const itemSellStatusOptions = ['SELL', 'SOLD_OUT'];
const isEditMode = computed(() => itemFormDto.value.id != -1);
const isFullScreen = ref(false);
const fullScreenImageUrl = ref('');

// 이미지 파일 등록 & 수정
function triggerFileInput(index) {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';
  input.onchange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        previewImageUrls.value[index] = e.target.result;
        itemFormDto.value.itemImgFiles[index] = file;

        if (!itemImgCheckList.value[index]) {
          itemImgCheckList.value[index] = [];
        }
        itemImgCheckList.value[index][0] == 'K' ?
          itemImgCheckList.value[index][1] = 'U' :
          itemImgCheckList.value[index]    = ['I','I'] ;
      };
      reader.readAsDataURL(file);
    }
  };
  input.click();
}

// 이미지 제거
function removeImage(index) {
  previewImageUrls.value[index] = null;
  itemFormDto.value.itemImgFiles.splice(index, 1);
  itemImgCheckList.value[index][0] == 'K' ?
    itemImgCheckList.value[index][1] = 'D' :
    itemImgCheckList.value[index]    = null;
}

// 이미지 클릭 시 전체 화면으로 보기
function showFullScreenImage(index) {
  fullScreenImageUrl.value = previewImageUrls.value[index];
  isFullScreen.value = true;
}

function closeFullScreenImage() {
  isFullScreen.value = false;
  fullScreenImageUrl.value = '';
}

function isDataModified() {
  let hasAddedImages = false;
  let hasModifiedOrDeletedImages = false;
  let allImagesDeleted = true;

  // 이미지 필드 변경 사항 확인
  for (let i = 0; i < itemImgCheckList.value.length; i++) {
      if (itemImgCheckList.value[i] && itemImgCheckList.value[i][0] === 'K') {
          if (itemImgCheckList.value[i][1] === 'U' || itemImgCheckList.value[i][1] === 'D') {
              hasModifiedOrDeletedImages = true; // 기존 이미지가 수정되거나 삭제된 경우
          }
          if (itemImgCheckList.value[i][1] !== 'D') {
              allImagesDeleted = false; // 기존 이미지가 삭제되지 않은 경우
          }
      } else if (itemImgCheckList.value[i] && itemImgCheckList.value[i][0] === 'I') {
          hasAddedImages = true; // 새로운 이미지가 추가된 경우
      }
  }

  // 모든 기존 이미지가 삭제되었고, 새로운 이미지가 추가되지 않은 경우
  if (allImagesDeleted && !hasAddedImages) {
      alert('한장 이상의 상품 이미지가 필요합니다.');
      return false;
  }

  // 이미지 변경 사항이 있는 경우
  if (hasModifiedOrDeletedImages || hasAddedImages) {
      return true;
  }

  // 기본 필드 변경 사항 확인
  const keysToCheck = ['itemNm', 'price', 'itemDetail', 'stockNumber', 'itemSellStatus'];
  for (let key of keysToCheck) {
      if (itemFormDto.value[key] !== originalItemFormDto.value[key]) {
          return true;
      }
  }

  alert('상품 데이터가 변경된 것이 없습니다.');
  return false;
}


// 폼 제출 함수
async function submitForm() {
  const formData = new FormData();
  
  // 수정하거나 등록한 파일이 있으면 formData에 추가
  if (itemFormDto.value.itemImgFiles && itemFormDto.value.itemImgFiles.length > 0) {
    itemFormDto.value.itemImgFiles.forEach((file) => {
      formData.append('itemImgFiles', file);
    });
  }

  if (!isDataModified()) {
    return;
  }

  // itemImgCheckList를 JSON 문자열로 변환하여 FormData에 추가
  if (isEditMode.value) {
    const itemImgCheckListString = JSON.stringify(itemImgCheckList.value);
    formData.append('jsonItemImgCheckList', itemImgCheckListString);
  }

  // FormData 내용 확인 (디버깅용)
  for (let pair of formData.entries()) {
    console.log(pair[0]+ ', ' + pair[1]);
  }

  // itemFormDto의(itemImgDtoList 제외) 기타 필드들을 formData에 추가
  for (const [key, value] of Object.entries(itemFormDto.value)) {
    if (key !== 'itemImgFiles' && key !== 'itemImgDtoList') {
      if (key !== 'id' && key !== 'itemImgFiles' && (value == null || value.toString().trim() === '')) {
        alert('"' + getLabel(key) + '" 을 확인해주세요.');
        return;
      }
      console.log("key >> " + key + " | value >> " + value);
      formData.append(key, value);
    }
    // 등록일때 이미지 상품 확인
    if (key === 'itemImgFiles' && !isEditMode.value) {
      if (value.length == 0) {
        alert('첫번째 상품 이미지는 필수 입력 값 입니다.');
        return;
      }
    }
  }

  try {
    // 등록, 수정 로직
    const response = isEditMode.value ?
      await axios.patch(`/admin/item/${itemFormDto.value.id}`, formData) :
      await axios.post('/admin/item/new', formData);
    
    // 등록 성공 후 메인 페이지로 이동 로직
    if(response.status == 200 || response.statusText == 'OK') {
      console.log('성공');
      router.push({ name: 'main' });
    }
    
  } catch (error) {
    if (error.response) {
      // 서버에서 응답으로 오류 메시지를 보냈을 때
      alert("서버 에러 메시지:" + error.response.data);
    } else if (error.request) {
      // 요청은 이루어졌으나 응답을 받지 못했을 때
      alert("No response received");
    } else {
      // 요청 설정 중 문제가 발생했을 때
      alert("Error" + error.message);
    }
  }
}

// 페이지 초기화 및 아이템 데이터 로드 함수
async function init() {
  
  const itemId = router.currentRoute.value.params.itemId;
  
  if (itemId != 'new') {
    try {
      console.log('수정 페이지 진입')
      const response = await axios.get(`/admin/item/${itemId}`);

      if (response.status === 200) {
        // 하나 이상의 소스 객체로부터 대상 객체로 속성을 복사하는 메서드
        Object.assign(itemFormDto.value, response.data);
        // 원본 데이터 저장
        Object.assign(originalItemFormDto.value, response.data);
        // 이미지 데이터도 로드
        if (response.data.itemImgDtoList && response.data.itemImgDtoList.length > 0) {
          previewImageUrls.value = response.data.itemImgDtoList.map((img, index) => {
            // itemImgCheckList 초기화 및 값 설정
            itemImgCheckList.value[index] = ['K','K'];
            return `${img.imgUrl}`;
          });
        }
      }
      console.table(itemFormDto.value);
      console.log('수정 페이지 데이터 작업 완료')

    } catch (error) {
      alert('아이템을 조회하는중 오류가 발생했습니다.');
      console.log('Error loading item details:', error);
    }
  }
}
 
// 컴포넌트가 마운트되었을 때 init 함수를 호출
onMounted(init);

</script>

<style scoped>
.image-grid {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.image-box {
  width: 100%;
  height: 100px;
  border: 1px solid #ddd;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 5px;
  position: relative;
  cursor: pointer; /* 마우스 포인터 모양 변경 */
}

.image-box img {
  max-width: 100%;
  max-height: 100%;
}

.full-screen-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 80vw; /* 전체 화면보다 조금 작게 */
  height: 80vh; /* 전체 화면보다 조금 작게 */
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 다른 요소 위로 이미지 배치 */
  cursor: pointer;
  margin: auto;
  transform: translate(-50%, -50%);
  top: 50%;
  left: 50%;
}

.full-screen-image {
  max-width: 90%;
  max-height: 90%;
}

.v-container {
  max-width: 70%;
  margin: auto;
}

.v-select, .v-text-field, .v-textarea, .v-file-input {
  background-color: #ffffff;
  margin: 5px;
}

.v-btn {
  height: 40px;
  text-transform: none;
  margin-top: 5px;
  font-size: 12px;
}

.v-col-3, .v-col-9 {
  width: 100%;
  padding: 1px;
}

/* 스크롤바 설정 */
.v-container {
  max-width: 70%;
  margin: auto;
  max-height: 80vh; /* 최대 높이 설정 */
  overflow-y: auto; /* 세로 스크롤바 추가 */
}
</style>
