import { ref, defineProps, onMounted } from 'vue';
import { useRoute, useRouter } from "vue-router";
import hljs from "highlight.js";
import debounce from "lodash/debounce";
import { QuillEditor } from '@vueup/vue-quill';
import { registArticle, modifyArticle } from "@/api/board";
import { watch } from 'vue';

export default await (async () => {
const { defineSlots, defineEmits, defineExpose, defineModel, defineOptions, withDefaults, } = await import('vue');
const router = useRouter();
const route = useRoute();

const myTextEditor = ref();
//대소문자 주의! (잘 안될 때)
const props = defineProps(
{
articleProps: Object,
submitType: String,
}
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
if (props.articleProps.content) {
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
article.value.userId = 'ssafy';
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
setTimeout(function () { loadArticle(); }, 100);
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

const __VLS_componentsOption = {
QuillEditor,
};

const __VLS_name = "quill-example-snow" as const;
function __VLS_template() {
let __VLS_ctx!: InstanceType<__VLS_PickNotAny<typeof __VLS_internalComponent, new () => {}>> & {};
/* Components */
let __VLS_otherComponents!: NonNullable<typeof __VLS_internalComponent extends { components: infer C; } ? C : {}> & typeof __VLS_componentsOption;
let __VLS_own!: __VLS_SelfComponent<typeof __VLS_name, typeof __VLS_internalComponent & (new () => { $slots: typeof __VLS_slots; })>;
let __VLS_localComponents!: typeof __VLS_otherComponents & Omit<typeof __VLS_own, keyof typeof __VLS_otherComponents>;
let __VLS_components!: typeof __VLS_localComponents & __VLS_GlobalComponents & typeof __VLS_ctx;
/* Style Scoped */
type __VLS_StyleScopedClasses = {} &
{ 'example'?: boolean; };
let __VLS_styleScopedClasses!: __VLS_StyleScopedClasses | keyof __VLS_StyleScopedClasses | (keyof __VLS_StyleScopedClasses)[];
/* CSS variable injection */
/* CSS variable injection end */
let __VLS_resolvedLocalAndGlobalComponents!: {} &
__VLS_WithComponent<'QuillEditor', typeof __VLS_localComponents, "QuillEditor", "quillEditor", "quill-editor">;
({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div; ({} as __VLS_IntrinsicElements).div;
({} as __VLS_IntrinsicElements).label; ({} as __VLS_IntrinsicElements).label;
({} as __VLS_IntrinsicElements).input;
__VLS_components.QuillEditor; __VLS_components.quillEditor; __VLS_components["quill-editor"];
// @ts-ignore
[QuillEditor,];
({} as __VLS_IntrinsicElements).button; ({} as __VLS_IntrinsicElements).button;
{
const __VLS_0 = ({} as __VLS_IntrinsicElements)["div"];
const __VLS_1 = __VLS_elementAsFunctionalComponent(__VLS_0);
({} as __VLS_IntrinsicElements).div;
({} as __VLS_IntrinsicElements).div;
const __VLS_2 = __VLS_1({ ...{}, class: ("example"), }, ...__VLS_functionalComponentArgsRest(__VLS_1));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_0, typeof __VLS_2> & Record<string, unknown>) => void)({ ...{}, class: ("example"), });
const __VLS_3 = __VLS_pickFunctionalComponentCtx(__VLS_0, __VLS_2)!;
let __VLS_4!: __VLS_NormalizeEmits<typeof __VLS_3.emit>;
{
const __VLS_5 = ({} as __VLS_IntrinsicElements)["div"];
const __VLS_6 = __VLS_elementAsFunctionalComponent(__VLS_5);
({} as __VLS_IntrinsicElements).div;
({} as __VLS_IntrinsicElements).div;
const __VLS_7 = __VLS_6({ ...{}, }, ...__VLS_functionalComponentArgsRest(__VLS_6));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_5, typeof __VLS_7> & Record<string, unknown>) => void)({ ...{}, });
const __VLS_8 = __VLS_pickFunctionalComponentCtx(__VLS_5, __VLS_7)!;
let __VLS_9!: __VLS_NormalizeEmits<typeof __VLS_8.emit>;
{
const __VLS_10 = ({} as __VLS_IntrinsicElements)["div"];
const __VLS_11 = __VLS_elementAsFunctionalComponent(__VLS_10);
({} as __VLS_IntrinsicElements).div;
({} as __VLS_IntrinsicElements).div;
const __VLS_12 = __VLS_11({ ...{}, class: ("mb-3"), }, ...__VLS_functionalComponentArgsRest(__VLS_11));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_10, typeof __VLS_12> & Record<string, unknown>) => void)({ ...{}, class: ("mb-3"), });
const __VLS_13 = __VLS_pickFunctionalComponentCtx(__VLS_10, __VLS_12)!;
let __VLS_14!: __VLS_NormalizeEmits<typeof __VLS_13.emit>;
{
const __VLS_15 = ({} as __VLS_IntrinsicElements)["label"];
const __VLS_16 = __VLS_elementAsFunctionalComponent(__VLS_15);
({} as __VLS_IntrinsicElements).label;
({} as __VLS_IntrinsicElements).label;
const __VLS_17 = __VLS_16({ ...{}, for: ("subject"), class: ("form-label"), }, ...__VLS_functionalComponentArgsRest(__VLS_16));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_15, typeof __VLS_17> & Record<string, unknown>) => void)({ ...{}, for: ("subject"), class: ("form-label"), });
const __VLS_18 = __VLS_pickFunctionalComponentCtx(__VLS_15, __VLS_17)!;
let __VLS_19!: __VLS_NormalizeEmits<typeof __VLS_18.emit>;
(__VLS_18.slots!).default;
}
{
const __VLS_20 = ({} as __VLS_IntrinsicElements)["input"];
const __VLS_21 = __VLS_elementAsFunctionalComponent(__VLS_20);
({} as __VLS_IntrinsicElements).input;
const __VLS_22 = __VLS_21({ ...{}, type: ("text"), class: ("form-control"), value: ((__VLS_ctx.article.subject)), placeholder: ("제목..."), }, ...__VLS_functionalComponentArgsRest(__VLS_21));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_20, typeof __VLS_22> & Record<string, unknown>) => void)({ ...{}, type: ("text"), class: ("form-control"), value: ((__VLS_ctx.article.subject)), placeholder: ("제목..."), });
const __VLS_23 = __VLS_pickFunctionalComponentCtx(__VLS_20, __VLS_22)!;
let __VLS_24!: __VLS_NormalizeEmits<typeof __VLS_23.emit>;
}
(__VLS_13.slots!).default;
}
{
let __VLS_25!: 'QuillEditor' extends keyof typeof __VLS_ctx ? typeof __VLS_ctx.QuillEditor : 'quillEditor' extends keyof typeof __VLS_ctx ? typeof __VLS_ctx.quillEditor : 'quill-editor' extends keyof typeof __VLS_ctx ? (typeof __VLS_ctx)['quill-editor'] : (typeof __VLS_resolvedLocalAndGlobalComponents)['QuillEditor'];
const __VLS_26 = __VLS_asFunctionalComponent(__VLS_25, new __VLS_25({ ...{ onChange: {} as any, onBlur: {} as any, onFocus: {} as any, onReady: {} as any, }, class: ("editor"), ref: ("myTextEditor"), disabled: ((false)), value: ((__VLS_ctx.content)), options: ((__VLS_ctx.editorOption)), }));
({} as { QuillEditor: typeof __VLS_25; }).QuillEditor;
const __VLS_27 = __VLS_26({ ...{ onChange: {} as any, onBlur: {} as any, onFocus: {} as any, onReady: {} as any, }, class: ("editor"), ref: ("myTextEditor"), disabled: ((false)), value: ((__VLS_ctx.content)), options: ((__VLS_ctx.editorOption)), }, ...__VLS_functionalComponentArgsRest(__VLS_26));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_25, typeof __VLS_27> & Record<string, unknown>) => void)({ ...{ onChange: {} as any, onBlur: {} as any, onFocus: {} as any, onReady: {} as any, }, class: ("editor"), ref: ("myTextEditor"), disabled: ((false)), value: ((__VLS_ctx.content)), options: ((__VLS_ctx.editorOption)), });
const __VLS_28 = __VLS_pickFunctionalComponentCtx(__VLS_25, __VLS_27)!;
let __VLS_29!: __VLS_NormalizeEmits<typeof __VLS_28.emit>;
// @ts-ignore
(__VLS_ctx.myTextEditor);
let __VLS_30 = { 'change': __VLS_pickEvent(__VLS_29['change'], ({} as __VLS_FunctionalComponentProps<typeof __VLS_26, typeof __VLS_27>).onChange) };
__VLS_30 = {
change: (__VLS_ctx.onEditorChange)
};
let __VLS_31 = { 'blur': __VLS_pickEvent(__VLS_29['blur'], ({} as __VLS_FunctionalComponentProps<typeof __VLS_26, typeof __VLS_27>).onBlur) };
__VLS_31 = {
blur: $event => {
__VLS_ctx.onEditorBlur($event);
}
};
let __VLS_32 = { 'focus': __VLS_pickEvent(__VLS_29['focus'], ({} as __VLS_FunctionalComponentProps<typeof __VLS_26, typeof __VLS_27>).onFocus) };
__VLS_32 = {
focus: $event => {
__VLS_ctx.onEditorFocus($event);
}
};
let __VLS_33 = { 'ready': __VLS_pickEvent(__VLS_29['ready'], ({} as __VLS_FunctionalComponentProps<typeof __VLS_26, typeof __VLS_27>).onReady) };
__VLS_33 = {
ready: $event => {
__VLS_ctx.onEditorReady($event);
}
};
}
{
const __VLS_34 = ({} as __VLS_IntrinsicElements)["div"];
const __VLS_35 = __VLS_elementAsFunctionalComponent(__VLS_34);
({} as __VLS_IntrinsicElements).div;
({} as __VLS_IntrinsicElements).div;
const __VLS_36 = __VLS_35({ ...{}, class: ("output ql-snow"), }, ...__VLS_functionalComponentArgsRest(__VLS_35));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_34, typeof __VLS_36> & Record<string, unknown>) => void)({ ...{}, class: ("output ql-snow"), });
const __VLS_37 = __VLS_pickFunctionalComponentCtx(__VLS_34, __VLS_36)!;
let __VLS_38!: __VLS_NormalizeEmits<typeof __VLS_37.emit>;
{
const __VLS_39 = ({} as __VLS_IntrinsicElements)["div"];
const __VLS_40 = __VLS_elementAsFunctionalComponent(__VLS_39);
({} as __VLS_IntrinsicElements).div;
({} as __VLS_IntrinsicElements).div;
const __VLS_41 = __VLS_40({ ...{}, }, ...__VLS_functionalComponentArgsRest(__VLS_40));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_39, typeof __VLS_41> & Record<string, unknown>) => void)({ ...{}, });
const __VLS_42 = __VLS_pickFunctionalComponentCtx(__VLS_39, __VLS_41)!;
let __VLS_43!: __VLS_NormalizeEmits<typeof __VLS_42.emit>;
__VLS_directiveFunction(__VLS_ctx.vHtml)((__VLS_ctx.content));
}
(__VLS_37.slots!).default;
}
{
const __VLS_44 = ({} as __VLS_IntrinsicElements)["button"];
const __VLS_45 = __VLS_elementAsFunctionalComponent(__VLS_44);
({} as __VLS_IntrinsicElements).button;
({} as __VLS_IntrinsicElements).button;
const __VLS_46 = __VLS_45({ ...{ onClick: {} as any, }, }, ...__VLS_functionalComponentArgsRest(__VLS_45));
({} as (props: __VLS_FunctionalComponentProps<typeof __VLS_44, typeof __VLS_46> & Record<string, unknown>) => void)({ ...{ onClick: {} as any, }, });
const __VLS_47 = __VLS_pickFunctionalComponentCtx(__VLS_44, __VLS_46)!;
let __VLS_48!: __VLS_NormalizeEmits<typeof __VLS_47.emit>;
let __VLS_49 = { 'click': __VLS_pickEvent(__VLS_48['click'], ({} as __VLS_FunctionalComponentProps<typeof __VLS_45, typeof __VLS_46>).onClick) };
__VLS_49 = {
click: (__VLS_ctx.submitArticle)
};
(__VLS_47.slots!).default;
}
(__VLS_8.slots!).default;
}
(__VLS_3.slots!).default;
}
if (typeof __VLS_styleScopedClasses === 'object' && !Array.isArray(__VLS_styleScopedClasses)) {
__VLS_styleScopedClasses["example"];
__VLS_styleScopedClasses["mb-3"];
__VLS_styleScopedClasses["form-label"];
__VLS_styleScopedClasses["form-control"];
__VLS_styleScopedClasses["editor"];
__VLS_styleScopedClasses["output"];
__VLS_styleScopedClasses["ql-snow"];
}
var __VLS_slots!: {};
// @ts-ignore
[article, article, content, editorOption, content, editorOption, content, editorOption, myTextEditor, onEditorChange, onEditorBlur, onEditorFocus, onEditorReady, content, submitArticle,];
return __VLS_slots;
}
const __VLS_internalComponent = (await import('vue')).defineComponent({
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
["bold", "italic", "underline", "strike"],
["blockquote", "code-block"],
[{ header: 1 }, { header: 2 }],
[{ list: "ordered" }, { list: "bullet" }],
[{ script: "sub" }, { script: "super" }],
[{ indent: "-1" }, { indent: "+1" }],
[{ direction: "rtl" }],
[{ size: ["small", false, "large", "huge"] }],
[{ header: [1, 2, 3, 4, 5, 6, false] }],
[{ font: [] }],
[{ color: [] }, { background: [] }],
[{ align: [] }],

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
onEditorChange: debounce(function (value) {
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

// created() 
setup() {
return {
$props: __VLS_makeOptional(props),
...props,
myTextEditor: myTextEditor as typeof myTextEditor,
article: article as typeof article,
submitArticle: submitArticle as typeof submitArticle,
QuillEditor: QuillEditor as typeof QuillEditor,
};
},
});
return (await import('vue')).defineComponent({
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
["bold", "italic", "underline", "strike"],
["blockquote", "code-block"],
[{ header: 1 }, { header: 2 }],
[{ list: "ordered" }, { list: "bullet" }],
[{ script: "sub" }, { script: "super" }],
[{ indent: "-1" }, { indent: "+1" }],
[{ direction: "rtl" }],
[{ size: ["small", false, "large", "huge"] }],
[{ header: [1, 2, 3, 4, 5, 6, false] }],
[{ font: [] }],
[{ color: [] }, { background: [] }],
[{ align: [] }],

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
onEditorChange: debounce(function (value) {
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

// created() 
setup() {
return {
$props: __VLS_makeOptional(props),
...props,
};
},
});
})();
