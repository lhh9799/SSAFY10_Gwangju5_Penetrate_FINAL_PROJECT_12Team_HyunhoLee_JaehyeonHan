<script setup>
import { ref, defineEmits } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const emit = defineEmits(["transferId"]);

import { useMemberStore } from "@/stores/member";
const memberStore = useMemberStore();
const { duplicateId } = memberStore;

const tempId = ref();
const resultMessage = ref("");
let result;

async function onCheck() {
  if (!tempId.value) {
    alert("아이디를 입력해주세요!");
    return;
  }
  console.log(tempId.value);

  try {
    result = await duplicateId(tempId.value);

    if (result === 200) {
      resultMessage.value = "중복된 ID가 있습니다.";
    } else if (result === 201) {
      resultMessage.value = "중복된 ID가 없습니다.";
    } else {
      resultMessage.value = "서버에서 알 수 없는 오류가 발생했습니다.";
    }

    console.log(resultMessage.value);
  } catch (error) {
    console.error("An error occurred:", error);
    resultMessage.value = "서버와 통신 중 오류가 발생했습니다.";
  }
}

function onSubmit() {
  if (result === 201) {
    emit("transferId", tempId.value);
  } else if (result === 200) {
    alert("중복되지 않은 ID를 입력해주세요!");
  }
}

async function onCancel() {
  emit("transferId");
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
                <h2>아이디 중복 검사</h2>
              </div>
              <input type="hidden" name="action" value="modifyPwd" />
              <div class="input-group mb-3"></div>
              <div class="input-group mb-3"></div>
              <div class="input-group mb-3">
                <span class="input-group-text">아이디</span>
                <input
                  type="text"
                  id="tempId"
                  name="tempId"
                  class="form-control"
                  placeholder="아이디를 입력해주세요.."
                  v-model="tempId"
                  required
                />
                <button class="button2" @click.prevent="onCheck">중복 검사</button>
              </div>
              <div>{{ resultMessage }}</div>
              <div class="form-row float-end">
                <button
                  type="button"
                  id="btn-pwd-change"
                  class="btn btn-outline-primary"
                  @click.prevent="onSubmit"
                >
                  확인
                </button>
                <button type="button" class="btn btn-outline-danger" @click.prevent="onCancel">
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

.form-control {
  width: 80%;
}

.button2 {
  border: none;
}
</style>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap');

* {
    font-family: 'Noto Sans KR', sans-serif;
}
</style>