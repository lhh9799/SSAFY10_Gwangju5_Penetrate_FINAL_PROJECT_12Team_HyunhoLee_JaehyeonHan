<template>
  <div>
    <!-- <h1>{{ content }}</h1> -->
    <!-- <div :v-html="content"></div> -->
    <!-- <p :v-html="content"></p> -->
    <p v-html='content'></p>
    <!-- <p v-html='sub'></p> -->
  </div>
</template>

<script setup>
import { getBlogArticle } from '@/api/board';
import { ref, onMounted } from 'vue';

const content = ref('');

const sub = ref();

onMounted(() => {
  getBlogArticle('lhh9799',
    (data) => {
      console.log('네이버 블로그 글 가져오기 성공');
      console.log(data);
      // content.value = data;
      content.value = data.data;
    },
    (error) => {
      console.log('네이버 블로그 글 가져오기 실패');
    }
  );

  sub.value = '<p class="subtext">이 글의 공감수도 함께 차감됩니다.</p>';
});
</script>
