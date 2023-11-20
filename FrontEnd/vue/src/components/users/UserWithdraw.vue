<script setup>
import { useMemberStore } from "@/stores/member";

import { useMenuStore } from "@/stores/menu";
import { useRouter } from "vue-router";

const memberStore = useMemberStore();

const menuStore = useMenuStore();
const router = useRouter();

const { userWithdraw, userLogout } = memberStore;
const { changeMenuState } = menuStore;

const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));

const withDraw = () => {
  if (memberStoreData.userInfo) {
    userWithdraw(memberStoreData.userInfo.userId);
    userLogout();
    changeMenuState();
    sessionStorage.clear();
    router.push({ name: "main" });
  } else {
    console.error("UserInfo가 없습니다.!!");
  }
};
</script>

<template>
  <transition name="modal">
    <div class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <div class="modal-header">
            <div class="modal-body">
              <h5>계정을 삭제하시면 복구하실 수 없습니다.</h5>
              <!-- 폼 시작 -->
              <div class="form-row float-end">
                <button type="submit" class="btn btn-outline-primary" @click.prevent="withDraw">
                  확인
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
  width: 500px;
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
