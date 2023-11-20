<script setup>
import { ref } from "vue";
import { useMemberStore } from "@/stores/member";
import { useRouter } from "vue-router";

const memberStore = useMemberStore();
const router = useRouter();

const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));

const { userModifyPwd } = memberStore;
const modifyPwd = ref({
  userId: "",
  newUserPwd: "",
  newUserPwdCheck: "",
});

async function onSubmit() {
  modifyPwd.value.userId = memberStoreData.userInfo.userId;
  if (modifyPwd.value.newUserPwd != modifyPwd.value.newUserPwdCheck) {
    console.log("비밀번호 일치확인 걸림");
    alert("비밀번호와 비밀번호 확인을 확인해주세요!");
  } else {
    await userModifyPwd(JSON.stringify(modifyPwd.value));
    router.replace("/");
  }
}
</script>

<template>
  <transition name="modal">
    <div class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <div class="modal-header">
            <div class="modal-body">
              <div class="container-md m-3 p-3">
                <h2>비밀번호 수정</h2>
              </div>
              <input type="hidden" name="action" value="modifyPwd" />
              <div class="input-group mb-3"></div>
              <div class="input-group mb-3">
                <span class="input-group-text">새로운 비밀번호</span>
                <input
                  type="password"
                  id="newUserPwd"
                  class="form-control"
                  placeholder="새로운 비밀번호를 입력해주세요"
                  v-model="modifyPwd.newUserPwd"
                  required
                />
              </div>
              <div class="input-group mb-3">
                <span class="input-group-text">새로운 비밀번호 확인</span>
                <input
                  type="password"
                  id="newUserPwdCheck"
                  class="form-control"
                  placeholder="새로운 비밀번호와 똑같이 입력해주세요"
                  v-model="modifyPwd.newUserPwdCheck"
                  required
                />
              </div>
              <div class="form-row float-end">
                <button
                  type="button"
                  id="btn-pwd-change"
                  class="btn btn-outline-primary"
                  @click.prevent="onSubmit"
                >
                  변경
                </button>
                <button
                  type="button"
                  class="btn btn-outline-danger"
                  data-bs-dismiss="modal"
                  @click="$emit('close')"
                >
                  취소
                </button>
              </div>
            </div>
            <div></div>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<style>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
  color: #555555;
}

.modal-container {
  width: 700px;
  margin: 0px auto;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
  font-family: Helvetica, Arial, sans-serif;
}

.modal-header h3 {
  margin-top: 0;
  color: #42b983;
}

.modal-body {
  margin: 20px 0;
}

.modal-default-button {
  float: right;
}

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
}

.modal-enter .modal-container,
.modal-leave-active .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
