<script setup>
const redirectToRoullette = () => {
  window.location.href = "http://localhost:5173/playground";
};

const redirectToBoard = () => {
  window.location.href = "http://localhost:5173/board/list";
};

const redirectToMap = () => {
  window.location.href = "http://localhost:5173/map";
};
</script>

<template>
  <div id="wrapper">
    <!-- 메인 페이지 상단 시작 -->
    <div class="row container-xl" style="float: none; margin: auto">
      <!-- 캐러셀 시작 -->
      <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-indicators">
          <button
            type="button"
            data-bs-target="#carouselExampleAutoplaying"
            data-bs-slide-to="0"
            class="active"
            aria-current="true"
            aria-label="Slide 1"
          ></button>
          <button
            type="button"
            data-bs-target="#carouselExampleAutoplaying"
            data-bs-slide-to="1"
            aria-label="Slide 2"
          ></button>
          <button
            type="button"
            data-bs-target="#carouselExampleAutoplaying"
            data-bs-slide-to="2"
            aria-label="Slide 3"
          ></button>
        </div>

        <div class="carousel-inner">
          <div class="carousel-item active">
            <img src="@/assets/images/main1.jpg" class="d-block w-100" alt="..." />
          </div>
          <div class="carousel-item">
            <img src="@/assets/images/main2.jpg" class="d-block w-100" alt="..." />
          </div>
          <div class="carousel-item">
            <img src="@/assets/images/main3.jpg" class="d-block w-100" alt="..." />
          </div>
        </div>
        <button
          class="carousel-control-prev"
          type="button"
          data-bs-target="#carouselExampleAutoplaying"
          data-bs-slide="prev"
        >
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Previous</span>
        </button>
        <button
          class="carousel-control-next"
          type="button"
          data-bs-target="#carouselExampleAutoplaying"
          data-bs-slide="next"
        >
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Next</span>
        </button>
      </div>
    </div>
    <!-- 캐러셀 끝 -->

    <p></p>

    <div>
      <!-- 카드 그룹 시작 -->
      <div class="row container-xl" style="float: none; margin: auto">
        <h2 style="display: flex; justify-content: center">Service</h2>
        <p></p>
        <div class="card col-md-4 p-2" @click="redirectToBoard" style="cursor: pointer">
          <img class="card-img-top" src="@/assets/images/community.png" />
          <div class="card-body">
            <h5 class="card-title">여행 게시판</h5>
            <p class="card-text">여행에 관한 정보를 이야기 해보세요.</p>
          </div>
          <div class="card-footer">
            <small class="text-muted">2023/09/09</small>
          </div>
        </div>
        <div class="card col-md-4 p-2" @click="redirectToRoullette" style="cursor: pointer">
          <img class="card-img-top" src="@/assets/images/roulette.png" />
          <div class="card-body">
            <h5 class="card-title">룰렛</h5>
            <p class="card-text">여행지가 고민 될 때 룰렛을 돌려보세요.</p>
          </div>
          <div class="card-footer">
            <small class="text-muted">2023/09/05</small>
          </div>
        </div>
        <div class="card col-md-4 p-2" @click="redirectToMap" style="cursor: pointer">
          <img class="card-img-top" src="@/assets/images/travel.png" />
          <div class="card-body">
            <h5 class="card-title">여행 계획</h5>
            <p class="card-text">여행 계획 짜는 것을 도와드려요.</p>
          </div>
          <div class="card-footer">
            <small class="text-muted">2023/08/12</small>
          </div>
        </div>
        <div class="card col-md-4 p-2">
          <img class="card-img-top" src="@/assets/images/blank.png" />
          <div class="card-body">
            <h5 class="card-title">BLANK</h5>
            <p class="card-text">BLANK.</p>
          </div>
          <div class="card-footer">
            <small class="text-muted">2023/08/02</small>
          </div>
        </div>
      </div>
      <!-- 카드 그룹 끝 -->
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap');

* {
    font-family: 'Noto Sans KR', sans-serif;
}
.row.container-xl {
  display: flex;
  justify-content: center;
}
.card {
  margin-right: 100px; /* Adjust the value as needed */
  margin-bottom: 50px;
}

.card:nth-child(even) {
  margin-right: 0; /* Remove margin for even-numbered cards */
}
</style>