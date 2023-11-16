import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "../views/TheMainView.vue";
// import TheBoardView from "../views/TheBoardView.vue";
import TheElectricChargingStation from '@/views/TheElectricChargingStation.vue';
import TheAttractionView from '@/components/attraction/Attraction.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: TheMainView,
    },
    {
      path: "/estations",
      name: "estations",
      component: TheElectricChargingStation,
    },
    {
      path: "/board",
      name: "board",
      // component: TheBoardView,
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/TheBoardView.vue"),
      redirect: { name: "article-list" },
      children: [
        {
          path: "list",
          name: "article-list",
          component: () => import("@/components/board/BoardList.vue"),
        },
        {
          path: "view/:articleno",
          name: "article-view",
          component: () => import("@/components/board/BoardDetail.vue"),
        },
        {
          path: "write",
          name: "article-write",
          component: () => import("@/components/board/BoardWrite.vue"),
        },
        {
          path: "modify/:articleno",
          name: "article-modify",
          component: () => import("@/components/board/BoardModify.vue"),
        },
      ],
    },
    {
      path: "/attraction",
      name: "attraction",
      component: TheAttractionView,
    },
  ],
});

export default router;
