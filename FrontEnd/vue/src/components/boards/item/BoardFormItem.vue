<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getModifyArticle } from "@/api/board";
import quillEditor from '@/views/TheQuillView.vue';

const router = useRouter();
const route = useRoute();

const props = defineProps({ type: String });

const isUseId = ref(false);

const article = ref({
  articleNo: 0,
  subject: "",
  content: "",
  userId: "",
  userName: "",
  hit: 0,
  registerTime: "",
});

if (props.type === "modify") {
  let { articleno } = route.params;
  console.log(articleno + "번글 얻어와서 수정할거야");
  getModifyArticle(
    articleno,
    ({ data }) => {
      article.value = data;
      isUseId.value = true;
    },
    (error) => {
      console.error(error);
    }
  );
  isUseId.value = true;
}

function onSubmit() {
}

function moveList() {
  router.push({ name: "article-list" });
}
</script>

<template>
  <!-- <form> -->
  <form @submit.prevent="onSubmit">
    <div class="mb-3">
      <label for="userid" class="form-label">작성자 ID : </label>
      <input
        type="text"
        class="form-control"
        v-model="article.userId"
        :disabled="isUseId"
        placeholder="작성자ID..."
      />
    </div>
    <div>
      <quill-editor :articleProps='article' :submitType='props.type'></quill-editor>
    </div>
  </form>
</template>

<style scoped></style>
