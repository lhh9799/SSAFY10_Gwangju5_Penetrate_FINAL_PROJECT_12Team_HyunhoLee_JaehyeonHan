<script setup>
import { modifyComment, deleteComment } from '@/api/board';
const props = defineProps({ comment: Object });

const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));

function onModifyComment() {
  // API 호출
  modifyComment(props.comment,
    (success) => {
      alert('댓글이 수정되었습니다.');
      location.reload();
    },
    (error) => {
      alert('댓글 수정 중 문제가 발생했습니다.');
    }
  )
}

function onDeleteComment() {
  // API 호출
  if(confirm('댓글을 삭제하시겠습니까?')) {
    deleteComment(props.comment.commentNo);
    location.reload();
  }
}
</script>

<style>
  .commentEdit {
    resize: none;
    width: 100%;
    height: 100%;
    border-radius: 5px;
  }
</style>

<template>  
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10"></div>
      <div class="col-lg-10 text-start">
        <div class="row">
          <div class="col-md-8">
            <div class="clearfix align-content-center">
              <img
                class="avatar me-2 float-md-start bg-light p-2"
                src="https://raw.githubusercontent.com/twbs/icons/main/icons/person-fill.svg"
              />
              <p>
                <span class="fw-bold">{{ comment.userId }}</span> <br />
                <span class="text-secondary fw-light">
                  {{ comment.registerTime }}
                </span>
              </p>
            </div>
          </div>
          <div class="divider mb-3"></div>
          <div class="text-secondary">
            <textarea class='commentEdit' id='comment' name='comment' v-model='comment.content'></textarea>
          </div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-end">
            <div v-if='(comment.userId == memberStoreData.userInfo.userId)'>
              <button type="button" class="btn btn-outline-success mb-3 ms-1" @click="onModifyComment">
                댓글수정
              </button>
              <button type="button" class="btn btn-outline-danger mb-3 ms-1" @click="onDeleteComment">
                댓글삭제
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
a {
  text-decoration: none;
}
</style>
