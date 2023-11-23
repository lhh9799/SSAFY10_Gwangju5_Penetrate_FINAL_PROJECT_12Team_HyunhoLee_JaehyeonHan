<script setup></script>

<template>
  <div class="container text-center mt-3">
    <div class="alert alert-info" role="alert">여행 후기 게시판</div>
    <router-view></router-view>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap');

* {
    font-family: 'Noto Sans KR', sans-serif;
}
</style>

<style>
mark.sky {
  background: linear-gradient(to top, #54fff9 20%, transparent 30%);
}
</style>
