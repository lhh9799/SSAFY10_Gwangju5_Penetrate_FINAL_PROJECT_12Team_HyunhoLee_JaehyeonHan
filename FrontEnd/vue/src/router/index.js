import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "../views/TheMainView.vue";
import TheElectricChargingStationView from "@/views/TheElectricChargingStationView.vue";
import { useMemberStore } from "@/stores/member";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: TheMainView,
    },
    {
      path: "/user",
      name: "user",
      component: () => import("@/views/TheUserView.vue"),
      children: [
        {
          path: "/login",
          name: "user-login",
          component: () => import("@/components/users/UserLogin.vue"),
        },
        {
          path: "/join",
          name: "user-join",
          component: () => import("@/components/users/UserRegister.vue"),
        },
        {
          path: "/check",
          name: "check",
          component: () => import("@/components/users/CheckPwd.vue"),
        },
        {
          path: "/mypage",
          name: "user-mypage",
          component: () => import("@/components/users/UserMyPage.vue"),
        },
      ],
    },
    {
      path: "/board",
      name: "board",
      component: () => import("../views/TheBoardView.vue"),
      redirect: { name: "article-list" },
      children: [
        {
          path: "list",
          name: "article-list",
          component: () => import("@/components/boards/BoardList.vue"),
        },
        {
          path: "view/:articleno",
          name: "article-view",
          component: () => import("@/components/boards/BoardDetail.vue"),
        },
        {
          path: "write",
          name: "article-write",
          component: () => import("@/components/boards/BoardWrite.vue"),
        },
        {
          path: "modify/:articleno",
          name: "article-modify",
          component: () => import("@/components/boards/BoardModify.vue"),
        },
      ],
    },
    {
      path: "/estations",
      name: "estations",
      component: TheElectricChargingStationView,
    },
    {
      path: "/todos",
      name: "todos",
      component: () => import("@/views/TheTodoView.vue"),
    },
    //이현호 테스트 Vue Draggable
    {
      path: "/draggable",
      name: "draggable",
      component: () => import("@/views/TheDraggableView.vue"),
    },
    //이현호 테스트 Vue quill
    {
      path: "/quill",
      name: "quill",
      component: () => import("@/views/TheQuillView.vue"),
    },
    //이현호 테스트 quill 작성 글 보기
    {
      path: "/quillView",
      name: "quillView",
      component: () => import("@/views/TheTestView.vue"),
    },
  ],
});

// 버전1. 로그인하지 않고 접속시 로그인 하라는 알림창과 함께 로그인 페이지로 리다이렉트

router.beforeEach((to, from, next) => {
  const memberStore = useMemberStore();

  const loginRequiredRoutes = [
    "user-mypage",
    "article-write",
    "article-modify",
    "estations",
    "article-list",
    "article-view",
  ];

  if (!memberStore.isLogin && loginRequiredRoutes.includes(to.name)) {
    alert("로그인을 먼저 해주세요.");
    next({ name: "user-login" });
  } else {
    next();
  }
});

export default router;

// 버전2 main페이지와 login 페이지만 접속 가능함

// const isUserLoggedIn = () => {
//   const memberStore = useMemberStore();
//   return memberStore.isLogin;
// };

// router.beforeEach((to, from, next) => {
//   if (!isUserLoggedIn() && to.name !== "user-login") {
//     next({ name: "user-login" });
//   } else {
//     next();
//   }
// });

// export { isUserLoggedIn };
