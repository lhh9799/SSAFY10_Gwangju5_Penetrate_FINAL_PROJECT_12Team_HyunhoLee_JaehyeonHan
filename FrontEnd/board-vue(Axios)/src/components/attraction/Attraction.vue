<script setup>
// import { ref, onMounted } from "vue";
// import { useRoute, useRouter } from "vue-router";
// import { detailArticle, deleteArticle } from '@/api/board';

// import attraction_maker from '@/map/attraction_maker.js'

// onMounted(() => {
// });
</script>

<template>
  <div class="container-lg">
    <form class="d-flex my-3" onsubmit="return false;" role="search">
      <div class="input-group">
        <select id="search-area" class="form-select me-2">
          <option value="0" selected>검색 할 지역 선택</option>
        </select>
        <select id="search-content-id" class="form-select me-2">
          <option value="0" selected>관광지 유형</option>
          <option value="12">관광지</option>
          <option value="14">문화시설</option>
          <option value="15">축제공연행사</option>
          <option value="25">여행코스</option>
          <option value="28">레포츠</option>
          <option value="32">숙박</option>
          <option value="38">쇼핑</option>
          <option value="39">음식점</option>
        </select>
        <input
          id="search-keyword"
          class="form-control me-2"
          type="search"
          placeholder="검색어"
          aria-label="검색어"
        />
        <button id="btn-search" class="btn btn-outline-success" type="button">
          검색
        </button>
      </div>
    </form>
    <!-- kakao map start -->
    <div id="map" class="mt-3" style="width: 100%; height: 400px"></div>
    <!-- kakao map end -->
    <div class="row">
      <table class="table table-striped" style="display: none">
        <thead>
          <tr>
            <th>대표이미지</th>
            <th>관광지명</th>
            <th>주소</th>
            <th>전화번호</th>
            <th width="75px"></th>
          </tr>
        </thead>
        <tbody id="trip-list"></tbody>
      </table>
    </div>
  </div>
  <!-- 관광지 검색 끝 -->
  <!-- <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=d819f7ebec33822a522caf7fed96316a&libraries=services,clusterer,drawing"></script>
  <script src="/js/attraction_maker.js"></script> -->
</template>

<style scoped></style>
