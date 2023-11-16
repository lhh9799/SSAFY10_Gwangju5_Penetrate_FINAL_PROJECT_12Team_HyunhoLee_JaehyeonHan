import { localAxios } from "@/util/http-commons";

const local = localAxios(); //axios instance

const url = "/board";

function listArticle(param, success, fail) {
  local.get(`${url}`, { params: param }).then(success).catch(fail);
}

function detailArticle(articleno, success, fail) {
  local.get(`${url}/${articleno}`).then(success).catch(fail);
}

function registArticle(article, success, fail) {
  console.log("boardjs article", article);
  local.post(`${url}`, JSON.stringify(article)).then(success).catch(fail);
  listArticle();
}

function getModifyArticle(articleno, success, fail) {
  local.get(`${url}/modify/${articleno}`).then(success).catch(fail);
}

function modifyArticle(article, success, fail) {
  local.put(`${url}`, JSON.stringify(article)).then(success).catch(fail);
}

function deleteArticle(articleno, success, fail) {
  local.delete(`${url}/${articleno}`).then(success).catch(fail);
  listArticle();
}

function modifyComment(comment, success, fail) {
  local.put(`${url}/comment`, JSON.stringify(comment)).then(success).catch(fail);
}

function writeComment(comment, success, fail) {
  local.post(`${url}/comment`, JSON.stringify(comment)).then(success).catch(fail);
}

function deleteComment(commentNo, success, fail) {
  local.delete(`${url}/comment/${commentNo}`).then(success).catch(fail);
}

function getBlogArticle(blogUrl, success, fail) {
  local.get(`${url}/blog/${blogUrl}`).then(success).catch(fail);
}

export {
  listArticle,
  detailArticle,
  registArticle,
  getModifyArticle,
  modifyArticle,
  deleteArticle,
  modifyComment,
  writeComment,
  deleteComment,
  getBlogArticle,
};
