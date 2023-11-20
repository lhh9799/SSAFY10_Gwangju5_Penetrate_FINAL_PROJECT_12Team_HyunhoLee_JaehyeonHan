<script setup>
import { ref, onMounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import { useMemberStore } from "@/stores/member";

const memberStore = useMemberStore();
const { userJoin } = memberStore;
const router = useRouter();

const focusDesignator = ref();

const joinUser = ref({
  userId: "",
  userName: "",
  userPwd: "",
  pwdcheck: "",
  emailId: "",
  emailDomain: "",
});

async function onSubmit() {
  if (joinUser.value.userPwd != joinUser.value.pwdcheck) {
    console.log("비밀번호 일치확인 걸림");
    alert("비밀번호와 비밀번호 확인을 확인해주세요!");
  } else if (!joinUser.value.emailDomain) {
    alert("이메일 도메인을 입력해주세요!");
  } else {
    //API 호출
    await userJoin(JSON.stringify(joinUser.value));
    router.replace("/");
  }
}

onMounted(async () => {
  await nextTick();
  focusDesignator.value.focus();
});
</script>

<template>
  <div class="container" @submit.prevent="onSubmit">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="orange">회원가입</mark>
        </h2>
      </div>
      <div class="col-lg-10 text-start">
        <form>
          <div class="mb-3">
            <label for="username" class="form-label">이름 : </label>
            <input
              type="text"
              class="form-control"
              id="username"
              placeholder="이름..."
              required="required"
              v-model="joinUser.userName"
              ref="focusDesignator"
            />
          </div>
          <div class="mb-3">
            <label for="userid" class="form-label">아이디 : </label>
            <input
              type="text"
              class="form-control"
              id="userid"
              placeholder="아이디..."
              required="required"
              v-model="joinUser.userId"
            />
          </div>
          <div class="mb-3">
            <label for="userpwd" class="form-label">비밀번호 : </label>
            <input
              type="password"
              class="form-control"
              id="userpwd"
              placeholder="비밀번호..."
              required="required"
              v-model="joinUser.userPwd"
            />
          </div>
          <div class="mb-3">
            <label for="pwdcheck" class="form-label">비밀번호확인 : </label>
            <input
              type="password"
              class="form-control"
              id="pwdcheck"
              placeholder="비밀번호확인..."
              required="required"
              v-model="joinUser.pwdcheck"
            />
          </div>
          <div class="mb-3">
            <label for="emailid" class="form-label">이메일 : </label>
            <div class="input-group">
              <input
                type="text"
                class="form-control"
                id="emailid"
                placeholder="이메일아이디"
                required="required"
                v-model="joinUser.emailId"
              />
              <span class="input-group-text">@</span>
              <select
                class="form-select"
                id="emaildomain"
                aria-label="이메일 도메인 선택"
                v-model="joinUser.emailDomain"
              >
                <option disabled value="">선택</option>
                <option value="ssafy.com">싸피</option>
                <option value="google.com">구글</option>
                <option value="naver.com">네이버</option>
                <option value="kakao.com">카카오</option>
              </select>
            </div>
          </div>
          <div class="col-auto text-center">
            <button type="submit" class="btn btn-outline-primary mb-3">회원가입</button>
            <button type="reset" class="btn btn-outline-success ms-1 mb-3">초기화</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
