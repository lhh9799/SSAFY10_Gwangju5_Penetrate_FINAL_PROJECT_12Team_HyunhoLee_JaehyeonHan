<script setup>
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { writeComment } from '@/api/board';
const props = defineProps({ articleNo: Number});

const router = useRouter();
const route = useRoute();
const today = ref(getToday());
const commentUserIdErrMsg = ref();
const commentContentErrMsg = ref();
const newWrittenComment = ref({
  articleNo: props.articleNo,
  commentNo: 0,
  userId: "",
  content: "",
  registerTime: "",
});

// 오늘 날짜를 문자열로 반환
function getToday() {
  const today = new Date();

  return `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`;
}

//댓글 작성자를 로그인 아이디로부터 가져옴
newWrittenComment.value.userId = JSON.parse(localStorage.getItem("memberStore")).userInfo.userId;

watch(
  () => newWrittenComment.value.content,
  (value) => {
    let len = value.length;
    if(len == 0 || len > 500) {
      commentContentErrMsg.value = '내용을 확인해주세요!!!';
    } else {
      commentContentErrMsg.value = '';
    }
  },
  {immediate: true}
);

watch(
  () => newWrittenComment.value.userId,
  (value) => {
    let len = value.length;
    if(len == 0 || len > 16) {
      commentUserIdErrMsg.value = '아이디를 확인해주세요!!!';
    } else {
      commentUserIdErrMsg.value = '';
    }
  },
  {immediate: true}
)

function onSubmit() {
  if(commentContentErrMsg.value) {
    alert(commentContentErrMsg.value);
  } else if(commentUserIdErrMsg.value) {
    alert(commentUserIdErrMsg.value);
  } else {
    newWrittenComment.value.articleNo = props.articleNo;

    // API 호출
    writeComment(newWrittenComment.value,
    // writeComment(newWrittenComment,
      (success) => {
        console.log('댓글 작성 성공');
        location.reload();
      },
      (error) => {
        console.log('댓글 작성 실패');
      }
    );
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
  <form @submit.prevent='onSubmit'>
  <!-- <form> -->
    <input type='hidden' :v-model='articleNo' :value='articleNo'>
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
                  <span class="fw-bold">{{ newWrittenComment.userId }}</span> <br />
                  <span class="text-secondary fw-light">
                    작성일: {{ today }}
                  </span>
                </p>
              </div>
            </div>
            <div class="divider mb-3"></div>
            <div class="text-secondary">
              <input type='hidden' id='commentNo'>
              <textarea class='commentEdit' v-model='newWrittenComment.content'></textarea>
            </div>
            <div class="divider mt-3 mb-3"></div>
            <div class="d-flex justify-content-end">
              <button type="submit" class="btn btn-outline-success mb-3 ms-1">
                댓글작성
              </button>
              <button type="reset" class="btn btn-outline-danger mb-3 ms-1" @click="onClearComment">
                초기화
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<style scoped>
a {
  text-decoration: none;
}
</style>
