<script>
import hljs from "highlight.js";
import debounce from "lodash/debounce";
import { QuillEditor } from '@vueup/vue-quill'
import { registArticle } from "@/api/board";
import { ref } from 'vue';

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
    onEditorBlur(editor) {
      console.log("editor blur!", editor);
    },
    onEditorFocus(editor) {
      console.log("editor focus!", editor);
    },
    onEditorReady(editor) {
      console.log("editor ready!", editor);
    },
  },
  computed: {
    editor() {
      return this.$refs.myTextEditor.quill;
    },
    contentCode() {
      return hljs.highlightAuto(this.content).value;
    },
  },
  mounted() {
    console.log("this is Quill instance:", this.editor);
  },
};
</script>

<script setup>
// import { ref, defineProps, onMounted } from 'vue';
// import { ref, defineProps, onMounted, toRaw } from 'vue';
// import { ref, defineProps, onMounted, toRaw, toRefs } from 'vue';
import { ref, defineProps, onMounted, toRaw, toRefs, computed } from 'vue';

const myTextEditor = ref();

// defineProps({articleProps: String});

// const articleProps = defineProps({articleProps: String});
const articleProps = defineProps({articleProps: Object});

const article = ref({
  articleNo: 0,
  subject: "",
  content: "",
  userId: "",
  userName: "",
  hit: 0,
  registerTime: "",
});

// myTextEditor.value.root.editor.innerHTML = articleProps.articleProps.content;

// const propContent = ref(articleProps.articleProps.content);
// console.log('propContent.value 1');
// console.log(propContent.value);

//GPT
// const { articleProps } = toRefs(defineProps(['articleProps']));
// const propContent = ref(articleProps.value.content);

//나
// const propContent = ref(toRefs(articleProps).value.content);
// console.log('propContent.value');
// console.log(propContent.value);

//Bard
const props = computed(() => articleProps);

function test() {
  console.log('articleProps.articleProps');
  console.log(articleProps);
  console.log(articleProps.articleProps);
  // document.querySelector("#test").value = articleProps.articleProps.content;
  // myTextEditor.value.setContents(articleProps.articleProps.content);
  // document.querySelector(".ql-editor").value = articleProps.articleProps.content;
  // document.querySelector(".ql-editor").innerHTML = articleProps.articleProps.content;
  // document.querySelector(".ql-editor").innerHTML = articleProps.articleProps.content + '';

  //Bard
  // Set the editor's contents to the articleProps.content
  myTextEditor.value.editor.innerHTML = articleProps.articleProps.content;
}

onMounted(() => {
  // document.querySelector(".ql-editor").innerHTML = item.value.content
  // document.querySelector(".ql-editor").innerHTML = 'hi';
  // console.log('articleProps');
  // console.log(articleProps);
  // console.log(articleProps.articleProps);
  // console.log(typeof(articleProps));
  // document.querySelector(".ql-editor").innerHTML = toRaw(articleProps.articleProps);
  // document.querySelector(".ql-editor").innerHTML = articleProps.articleProps;
  // console.log(toRaw(articleProps)['articleProps']);
  // console.log(toRaw(articleProps).articleProps);
  // console.log(typeof(toRaw(articleProps)));
  // console.log(typeof(toRaw(articleProps).toString()));
  // console.log(articleProps.articleProps);
  // console.log(articleProps["articleProps"]);
  // console.log(articleProps.content);
  // console.log(articleProps.data);
  // document.querySelector(".ql-editor").innerHTML = articleProps;
  // document.querySelector(".ql-editor").innerHTML = toRaw(articleProps);
  // document.querySelector(".ql-editor").innerHTML = JSON.stringify(articleProps);
  // document.querySelector(".ql-editor").innerHTML = articleProps;
  // document.querySelector(".ql-editor").innerHTML = articleProps;
  // document.querySelector(".ql-editor").innerHTML = articleProps;
  // document.querySelector("#test").innerHTML = toRaw(articleProps);
  // document.querySelector("#test").value = articleProps.articleProps;
  // document.querySelector("#test").innerHTML = articleProps.articleProps;
  // document.querySelector("#test").value = toRaw(articleProps);
  // console.log('JSON.stringify(articleProps)');
  // console.log(JSON.stringify(articleProps));
  // console.log(JSON.parse(articleProps));
  // document.querySelector("#test").value = articleProps.articleProps.content;

  // console.log('articleProps.articleProps.content');
  // console.log(articleProps.articleProps.content);
  // myTextEditor.value.editor.innerHTML = articleProps.articleProps.content;
  // test();
  // myTextEditor.value.editor.innerHTML = 'hi';

  // console.log('propContent.value 2');
  // console.log(propContent.value);
  // myTextEditor.value.editor.innerHTML = propContent.value;

  // console.log('props');
  // console.log(props);
  // console.log('props.value');
  // console.log(props.value);
  // myTextEditor.value.editor.innerHTML = props.props.value.content;
  // myTextEditor.value.editor.innerHTML = articleProps.articleProps.content;

  setTimeout(function() { test() }, 100);
}),

function writeArticle() {
  //article.value.subject는 양방향 바인딩 되어있음 (별도 할당 불필요)
  article.value.userId = 'ssafy'
  article.value.content = myTextEditor.value.editor.outerHTML;

  const deleteTarget1 = '<input type="text" data-formula="e=mc^2" data-link="https://quilljs.com" data-video="Embed URL">';
  const deleteTarget2 = '<input type="text" data-formula="e=mc^2" data-link="https://quilljs.com" data-video="Embed URL" placeholder="Embed URL">';
  article.value.content = article.value.content.replaceAll(deleteTarget1, '');
  article.value.content = article.value.content.replaceAll(deleteTarget2, '');

  console.log('보낼 데이터');
  console.log(article.value);

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

    <button @click='writeArticle()'>작성하기</button>
    <button @click='test()'>테스트</button>
    <input type='text' id='test' />
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