<script setup>
import { ref, onMounted } from "vue";
import { useMemberStore } from "@/stores/member";
import { storeToRefs } from "pinia";
const memberStore = useMemberStore();

import Withdraw from "./UserWithdraw.vue";
import Modify from "./ModifyPwd.vue";

const { userInfo } = storeToRefs(memberStore);

const showModify = ref(false);
const showWithdraw = ref(false);

onMounted(async () => {
  const { userInfo: userInfoData } = storeToRefs(memberStore);
  userInfoData;
  userInfo.value = userInfoData.value;
});
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="orange">내정보</mark>
        </h2>
      </div>
      <div class="col-lg-10">
        <div class="card mt-3 m-auto" style="max-width: 700px">
          <div class="row g-0">
            <div class="col-md-4">
              <img
                src="https://source.unsplash.com/random/250x250/?food"
                class="img-fluid rounded-start"
                alt="..."
              />
            </div>
            <div class="col-md-8">
              <div class="card-body text-start">
                <ul class="list-group list-group-flush">
                  <li class="list-group-item">{{ userInfo.userId }}</li>
                  <li class="list-group-item">{{ userInfo.userName }}</li>
                  <li class="list-group-item">{{ userInfo.emailId }}@{{ userInfo.emailDomain }}</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div>
          <button type="button" class="btn btn-outline-secondary mt-2" @click="showModify = true">
            비밀번호 변경
          </button>
          <Modify v-if="showModify" @close="showModify = false"> </Modify>
          <div>
            <button
              type="button"
              class="btn btn-outline-secondary mt-2"
              style="color: white; background-color: red"
              @click="showWithdraw = true"
            >
              탈퇴
            </button>
            <Withdraw v-if="showWithdraw" @close="showWithdraw = false">
              <h3 slot="header">모달 창 제목</h3>
            </Withdraw>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
