<template>
  <div class="container">
    <h1 class="mt-4">Welcome to the Shopping Mall</h1>
    <p>여기에 메인 페이지의 주요 콘텐츠가 위치합니다.</p>
    <br/><br/>
    <!-- 예시 콘텐츠 -->
    <div class="row">
      <h1 th:text="${#messages.msg('welcome.message')}">메인페이지입니다</h1>
      <br/><br/><br/><br/>
      <h4>- 현재 로케일 미적용</h4>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainPage',
  // 메인 페이지 관련 데이터 및 메소드
};
</script>

<style>
  /* 여기에 메인 페이지의 스타일을 추가합니다 */
</style>
  