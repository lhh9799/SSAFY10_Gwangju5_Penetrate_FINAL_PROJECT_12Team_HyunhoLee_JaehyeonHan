<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { detailArticle, deleteArticle } from "@/api/board";
import CommentModifyItem from "./item/CommentModifyItem.vue";
import CommentWriteItem from "./item/CommentWriteItem.vue";

const route = useRoute();
const router = useRouter();
const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));

const { articleno } = route.params;

const article = ref({});

onMounted(() => {
  getArticle();
});

const getArticle = () => {
  // const { articleno } = route.params;
  // console.log(articleno + "번글 얻으러 가자!!!");
  // API 호출
  detailArticle(
    articleno,
    (data) => {
      console.log("글 가져오기 성공");
      console.log(data);
      article.value.articleNo = data.data.articleNo;
      article.value.subject = data.data.subject;
      article.value.content = data.data.content;
      article.value.userId = data.data.userId;
      article.value.userName = data.data.userName;
      article.value.hit = data.data.hit;
      article.value.registerTime = data.data.registerTime;
      article.value.comments = data.data.comments;
    },
    (error) => {
      console.log("글 가져오기 실패");
    }
  );
};

function moveList() {
  router.push({ name: "article-list" });
}

function moveModify() {
  router.push({ name: "article-modify", params: { articleno } });
}

function onDeleteArticle() {
  if (!confirm('글을 삭제하시겠습니까?')) {
    return;
  }

  // const { articleno } = route.params;
  // console.log(articleno + "번글 삭제하러 가자!!!");
  // API 호출
  deleteArticle(
    articleno,
    (success) => {
      console.log("글 삭제 성공");
    },
    (error) => {
      console.log("글 삭제 실패");
    }
  );

  router.replace({ name: "article-list" }).then(() => {
    location.reload();
  });
}
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="sky">질문</mark>
        </h2>
      </div>
      <div class="col-lg-10 text-start">
        <div class="row my-2">
          <h2 class="text-secondary px-5">{{ article.articleNo }}. {{ article.subject }}</h2>
        </div>
        <div class="row">
          <div class="col-md-8">
            <div class="clearfix align-content-center">
              <img
                class="avatar me-2 float-md-start bg-light p-2"
                src="https://raw.githubusercontent.com/twbs/icons/main/icons/person-fill.svg"
              />
              <p>
                <span class="fw-bold">{{ article.userId }}</span> <br />
                <span class="text-secondary fw-light">
                  {{ article.registerTime }} 조회 : {{ article.hit }}
                </span>
              </p>
            </div>
          </div>
          <div class="col-md-4 align-self-center text-end">댓글 : {{ article.comments ? article.comments.length : 0 }}</div>
          <div class="divider mb-3"></div>
          <div>
            <p v-html='article.content'></p>
          </div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-outline-primary mb-3" @click="moveList">
              글목록
            </button>
            <div v-if='(article.userId == memberStoreData.userInfo.userId)'>
              <button type="button" class="btn btn-outline-success mb-3 ms-1" @click="moveModify">
                글수정
              </button>
              <button type="button" class="btn btn-outline-danger mb-3 ms-1" @click="onDeleteArticle">
                글삭제
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-warning text-center">
          <mark class="white">답변</mark>
        </h2>
      </div>
      <div class="container text-center mt-3">
        <CommentModifyItem
          v-for="comment in article.comments"
          :key="comment.commentNo"
          :comment="comment"
        ></CommentModifyItem>
        <CommentWriteItem :articleNo="article.articleNo"></CommentWriteItem>
      </div>
    </div>
  </div>
</template>

<style scoped>
mark.white {
  background: linear-gradient(to top, #ffffff 20%, transparent 30%);
}
</style>
