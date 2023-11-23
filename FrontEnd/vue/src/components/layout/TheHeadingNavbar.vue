<script setup>
import { ref } from "vue";

import { useMenuStore } from "@/stores/menu";
import { storeToRefs } from "pinia";
import { useMemberStore } from "@/stores/member";
import { useRouter } from "vue-router";

const router = useRouter();
const menuStore = useMenuStore();
const memberStore = useMemberStore();

const { menuList } = storeToRefs(menuStore);
const { changeMenuState } = menuStore;
const { userLogout } = memberStore;

const logout = () => {
  console.log("로그아웃!!!!");
  userLogout();
  changeMenuState();
};

let input = ref("");
const boardList = [
  { id: 1, name: "놀이터", route: "playground" },
  { id: 2, name: "HELP DESK", route: "2" },
  { id: 3, name: "게시판", route: "board" },
  { id: 4, name: "여행 계획 짜기", route: "map" },
];

function filteredList() {
  return boardList.filter((board) => board.name.toLowerCase().includes(input.value.toLowerCase()));
}

const navigateToRoute = (id) => {
  const selectedBoard = boardList.find((board) => board.id === id);
  if (selectedBoard) {
    // Adjust this line based on your router setup
    router.push(selectedBoard.route);
    clearInput(); // 클릭 시 검색어를 초기화
  }
};

const clearInput = () => {
  setTimeout(() => {
    input.value = "";
  }, 50);
};
</script>

<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary sticky-top">
    <div class="container-fluid">
      <router-link :to="{ name: 'main' }" class="navbar-brand">
        <a class="navbar-brand">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            fill="currentColor"
            class="bi bi-airplane"
            viewBox="0 0 16 16"
          >
            <path
              d="M6.428 1.151C6.708.591 7.213 0 8 0s1.292.592 1.572 1.151C9.861 1.73 10 2.431 10 3v3.691l5.17 2.585a1.5 1.5 0 0 1 .83 1.342V12a.5.5 0 0 1-.582.493l-5.507-.918-.375 2.253 1.318 1.318A.5.5 0 0 1 10.5 16h-5a.5.5 0 0 1-.354-.854l1.319-1.318-.376-2.253-5.507.918A.5.5 0 0 1 0 12v-1.382a1.5 1.5 0 0 1 .83-1.342L6 6.691V3c0-.568.14-1.271.428-1.849Zm.894.448C7.111 2.02 7 2.569 7 3v4a.5.5 0 0 1-.276.447l-5.448 2.724a.5.5 0 0 0-.276.447v.792l5.418-.903a.5.5 0 0 1 .575.41l.5 3a.5.5 0 0 1-.14.437L6.708 15h2.586l-.647-.646a.5.5 0 0 1-.14-.436l.5-3a.5.5 0 0 1 .576-.411L15 11.41v-.792a.5.5 0 0 0-.276-.447L9.276 7.447A.5.5 0 0 1 9 7V3c0-.432-.11-.979-.322-1.401C8.458 1.159 8.213 1 8 1c-.213 0-.458.158-.678.599Z"
            />
          </svg>
          EnjoyTrip
        </a>
      </router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarScroll"
        aria-controls="navbarScroll"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarScroll">
        <ul
          class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll"
          style="--bs-scroll-height: 100px"
        >
          <li class="nav-item">
            <router-link :to="{ name: 'playground' }" class="nav-link">놀이터</router-link>
          </li>
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              HELP DESK
            </a>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="#">공지사항</a></li>
              <li><a class="dropdown-item" href="#">FAQ</a></li>
              <li>
                <hr class="dropdown-divider" />
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'board' }" class="nav-link">게시판</router-link>
          </li>
          <!-- 이현호 테스트 여행지 정보 -->
          <li class="nav-item">
            <router-link :to="{ name: 'map' }" class="nav-link">여행 계획 짜기</router-link>
          </li>
        </ul>
        <form class="d-flex position-relative" role="search">
          <input
            type="text"
            v-model="input"
            placeholder="찾길 원하는 메뉴를 검색해주세요."
            @blur="clearInput"
          />
          <div class="search-results" v-if="input && filteredList().length">
            <div class="result-dropdown">
              <div
                class="result-item"
                v-for="board in filteredList()"
                :key="board.id"
                @click="navigateToRoute(board.id)"
              >
                <img class="card-img-top" src="@/assets/images/magnifier.png" />
                <div>{{ board.name }}</div>
              </div>
            </div>
          </div>
        </form>
        <!-- <div class="item error" v-if="input && !filteredList().length">
              <p>No results found!</p>
            </div> -->
        <!-- <button class="btn btn-outline-success" type="button">search</button> -->
        <!-- </form> -->
        <ul
          class="navbar-nav ms-auto my-2 my-lg-0 navbar-nav-scroll"
          style="--bs-scroll-height: 100px"
        >
          <template v-for="menu in menuList" :key="menu.routeName">
            <template v-if="menu.show">
              <template v-if="menu.routeName === 'user-logout'">
                <li class="nav-item">
                  <router-link to="/" @click.prevent="logout" class="nav-link">{{
                    menu.name
                  }}</router-link>
                </li>
              </template>
              <template v-else>
                <li class="nav-item">
                  <router-link :to="{ name: menu.routeName }" class="nav-link">{{
                    menu.name
                  }}</router-link>
                </li>
              </template>
            </template>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Montserrat&display=swap");

input {
  display: block;
  width: 350px;
  margin: 20px auto;
  padding: 10px 45px;
  background-size: 15px 15px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  box-shadow: rgba(50, 50, 93, 0.25) 0px 2px 5px -1px, rgba(0, 0, 0, 0.3) 0px 1px 3px -1px;
}

.error {
  background-color: tomato;
}

.result-dropdown {
  position: absolute;
  top: calc(100% + 5px); /* Adjust the distance from the input field */
  left: 0;
  z-index: 1000;
  width: 100%;
  height: 10;
  background-color: white;
  border: 1px solid #d4d4d4;
  border-radius: 4px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.result-item {
  text-align: center;
  padding: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  align-items: center;
}

.result-item:hover {
  background-color: #f8f8f8;
}

.card-img-top {
  width: 20px; /* Adjust the width as needed */
  height: auto; /* Maintain the aspect ratio */
  margin-right: 8px; /* Adjust the spacing between image and text */
}
</style>
