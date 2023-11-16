// import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import { createPersistedStatePlugin } from "pinia-plugin-persistedstate-2";

import App from "./App.vue";
import router from "./router";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

const app = createApp(App);
const pinia = createPinia();
const PersistedStatePlugin = createPersistedStatePlugin();
pinia.use((context) => PersistedStatePlugin(context));

app.use(pinia);
app.use(router);

app.mount("#app");

/*
//이현호 추가 시작 --[방법 2] quill 전역 설정
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
app.component('QuillEditor', QuillEditor)
//이현호 추가 끝
*/