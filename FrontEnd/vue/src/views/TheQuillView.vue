<script>
import hljs from "highlight.js";
import debounce from "lodash/debounce";
import { QuillEditor } from '@vueup/vue-quill'
import { registArticle, modifyArticle } from "@/api/board";
import { ref, watch } from 'vue';

// import theme style
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";

export default {
  name: "quill-example-snow",
  title: "Theme: snow",
  components: {
    QuillEditor,
  },
  data() {
    return {
      editorOption: {
        placeholder: "본문을 입력하세요.",
        modules: {
          toolbar: [
            ["bold", "italic", "underline", "strike"], // <strong>, <em>, <u>, <s>
            ["blockquote", "code-block"], // <blockquote>, <pre class="ql-syntax" spellcheck="false">
            [{ header: 1 }, { header: 2 }], // <h1>, <h2>
            [{ list: "ordered" }, { list: "bullet" }],
            [{ script: "sub" }, { script: "super" }], // <sub>, <sup>
            [{ indent: "-1" }, { indent: "+1" }], // class제어
            [{ direction: "rtl" }], //class 제어
            [{ size: ["small", false, "large", "huge"] }], //class 제어 - html로 되도록 확인
            [{ header: [1, 2, 3, 4, 5, 6, false] }], // <h1>, <h2>, <h3>, <h4>, <h5>, <h6>, normal
            [{ font: [] }], // 글꼴 class로 제어
            [{ color: [] }, { background: [] }], //style="color: rgb(230, 0, 0);", style="background-color: rgb(230, 0, 0);"
            [{ align: [] }], // class
            // ["clean"],
            ["link", "image", "video"],
          ],
          syntax: {
            highlight: (text) => hljs.highlightAuto(text).value,
          },
        },
      },
      content: "",
    };
  },
  methods: {
    onEditorChange: debounce(function(value) {
      this.content = value.html;
    }, 466),
    // onEditorBlur(editor) {
    //   console.log("editor blur!", editor);
    // },
    // onEditorFocus(editor) {
    //   console.log("editor focus!", editor);
    // },
    // onEditorReady(editor) {
    //   console.log("editor ready!", editor);
    // },
  },
  computed: {
    editor() {
      return this.$refs.myTextEditor.quill;
    },
    contentCode() {
      return hljs.highlightAuto(this.content).value;
    },
  },
  // mounted() {
  //   console.log("this is Quill instance:", this.editor);
  // },
};
</script>

<script setup>
import { ref, defineProps, onMounted } from 'vue';
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();

const myTextEditor = ref();
//대소문자 주의! (잘 안될 때)
const props = defineProps(
  {
    articleProps:Object,
    submitType: String,
  },
);

const article = ref({
  articleNo: 0,
  subject: "",
  content: "",
  userId: "",
  userName: "",
  hit: 0,
  registerTime: "",
});

function moveList() {
  router.push({ name: "article-list" });
}

function loadArticle() {
  //Bard
  // Set the editor's subject and contents from the props.articleProps Object
  if(props.articleProps.content) {
    article.value.subject = props.articleProps.subject;
    myTextEditor.value.editor.innerHTML = props.articleProps.content;
  }
}

function writeArticle() {
  // console.log("글등록하자!!", article.value);
  registArticle(
    article.value,
    (response) => {
      let msg = "글등록 처리시 문제 발생했습니다.";
      if (response.status == 201) msg = "글등록이 완료되었습니다.";
      alert(msg);
      moveList();
    },
    (error) => console.error(error)
  );
}

function updateArticle() {
  // console.log(article.value.articleNo + "번글 수정하자!!", article.value);
  let { articleno } = route.params;
  article.value.articleNo = articleno;

  modifyArticle(
    article.value,
    (response) => {
      let msg = "글수정 처리시 문제 발생했습니다.";
      if (response.status == 200) msg = "글정보 수정이 완료되었습니다.";
      alert(msg);
      moveList();
    },
    (error) => console.log(error)
  );
}

const submitArticle = () => {
  //article.value.subject는 양방향 바인딩 되어있음 (별도 할당 불필요)
  article.value.userId = 'ssafy'
  article.value.content = myTextEditor.value.editor.outerHTML;

  //Trims unnecessary codes (quill editor)
  const deleteTarget1 = '<input type="text" data-formula="e=mc^2" data-link="https://quilljs.com" data-video="Embed URL">';
  const deleteTarget2 = '<input type="text" data-formula="e=mc^2" data-link="https://quilljs.com" data-video="Embed URL" placeholder="Embed URL">';
  article.value.content = article.value.content.replaceAll(deleteTarget1, '');
  article.value.content = article.value.content.replaceAll(deleteTarget2, '');

  props.submitType === "regist" ? writeArticle() : updateArticle();
};

onMounted(() => {
  //Loads original article after 100ms.
  setTimeout(function() { loadArticle() }, 100);
});

const subjectErrMsg = ref("");
const contentErrMsg = ref("");
watch(
  () => article.value.subject,
  (value) => {
    let len = value.length;
    if (len == 0 || len > 30) {
      subjectErrMsg.value = "제목을 확인해 주세요!!!";
    } else subjectErrMsg.value = "";
  },
  { immediate: true }
);
watch(
  () => article.value.content,
  (value) => {
    let len = value.length;
    if (len == 0 || len > 500) {
      contentErrMsg.value = "내용을 확인해 주세요!!!";
    } else contentErrMsg.value = "";
  },
  { immediate: true }
);
</script>

<template>
  <div class="example">
    <div>
      <div class="mb-3">
        <label for="subject" class="form-label">제목 : </label>
        <input type="text" class="form-control" v-model="article.subject" placeholder="제목..." />
      </div>
      <quill-editor
        class="editor"
        ref="myTextEditor"
        :disabled="false"
        :value="content"
        :options="editorOption"
        @change="onEditorChange"
        @blur="onEditorBlur($event)"
        @focus="onEditorFocus($event)"
        @ready="onEditorReady($event)"
      />

      <div class="output ql-snow">
        <div v-html="content"></div>
      </div>
    </div>
    <div style='height: 50px;'></div>
    <div class="col-auto text-center">
      <button @click='submitArticle' class="btn btn-outline-primary mb-3" v-if="props.submitType === 'regist'">
        글작성
      </button>
      <button @click='submitArticle' class="btn btn-outline-success mb-3" v-else>글수정</button>
      <button type="button" class="btn btn-outline-danger mb-3 ms-1" @click="moveList">
        목록으로이동...
      </button>
    </div>
  </div>
</template>

<style scoped>
.example {
  /* width: 80%; */
  /* margin: auto; */
  min-width: 0px;
}
</style>