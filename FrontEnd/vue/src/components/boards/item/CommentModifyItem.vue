<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { modifyComment, deleteComment } from '@/api/board';
// defineProps({ comment: Object });
const props = defineProps({ comment: Object });

const router = useRouter();
const route = useRoute();

const modifiedComment = ref({
  articleNo: 0,
  commentNo: 0,
  userId: "",
  content: "",
  registerTime: "",
})

function onModifyComment() {
  var commentNoElement = document.querySelector('#commentNo');
  var commentElement = document.querySelector('#comment');

  modifiedComment.value.commentNo = commentNoElement.value;
  modifiedComment.value.content = commentElement.value; //수정된 댓글 내용

  // API 호출
  modifyComment(modifiedComment.value,
    (success) => {
      console.log('댓글 수정 성공');
      alert('댓글 수정 성공');
      location.reload();
    },
    (error) => {
      alert('댓글 수정 실패');
      console.log('댓글 수정 실패');
    }
  )
}

function onDeleteComment() {
  // API 호출
  if(confirm('댓글을 삭제하시겠습니까?')) {
    deleteComment(props.comment.commentNo);
    console.log('댓글 삭제 성공');
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
            <input type='hidden' id='commentNo' :value='comment.commentNo'>
            <textarea class='commentEdit' id='comment' name='comment' :value=comment.content></textarea>
          </div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-end">
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
</template>

<style scoped>
a {
  text-decoration: none;
}
</style>
