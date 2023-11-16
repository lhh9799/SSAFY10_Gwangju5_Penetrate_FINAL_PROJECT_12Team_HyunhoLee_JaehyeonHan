<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/head.jsp" %>
<%@ include file="../include/nav.jsp" %>
<div class="row justify-content-center">
	<div class="col-lg-8 col-md-10 col-sm-12">
    <h2 class="my-3 py-3 shadow-sm bg-light text-center">
		<mark class="sky">여행정보공유</mark>
    </h2>
	</div>
	<div class="col-lg-8 col-md-10 col-sm-12">
    	<div class="row align-self-center mb-2">
	    	<c:if test="${not empty userDto }">
	      		<div class="col-md-2 text-start">
	        		<button type="button" id="btn-mv-register" class="btn btn-outline-primary btn-sm">글쓰기</button>
	      		</div>
	      	</c:if>
			<div class="col-md-7 offset-3">
		        <form class="d-flex" id="form-search" action="${root}/board/list">
					<div class="input-group input-group-sm">
		            	<input type="text" name="word" id="word" class="form-control" placeholder="검색어..." />
		            	<button id="btn-search" class="btn btn-dark" type="button">검색</button>
		          	</div>
					<select name="key" id="key" class="form-select form-select-sm ms-5 me-1 w-50" aria-label="검색조건">
						<option value="" selected>검색조건</option>
		            	<option value="article_no">글번호</option>
		            	<option value="subject">제목</option>
		            	<option value="user_id">작성자</option>
		          	</select>
		          	<select name="sortkey" id="sortkey" class="form-select form-select-sm ms-5 me-1 w-50" aria-label="정렬조건">
			            <option value="" selected>정렬조건</option>
			            <option value="article_no_sort">글번호 순</option>
			            <option value="subject_sort">제목 순</option>
			            <option value="user_id_sort">작성자 순</option>
			            <option value="hit_sort">조회수 순</option>
			            <option value="recommend_sort">추천수 순</option>
			            <option value="registerTime_sort">작성일 순</option>
					</select>
				</form>
			</div>
    	</div>
    	<table class="table table-hover">
			<thead>
				<tr class="text-center">
					<th scope="col">글번호</th>
					<th scope="col">제목</th>
					<th scope="col">작성자</th>
					<th scope="col">조회수</th>
					<th scope="col">추천수</th>
					<th scope="col">작성일</th>
				</tr>
			</thead>
			<tbody>    
				<c:forEach var="article" items="${articles}">    
					<tr class="text-center">
						<th scope="row">${article.articleNo}</th>
							<td class="text-start">
								<a href="#" class="article-title link-dark" data-no="${article.articleNo}" style="text-decoration: none">${article.subject}</a>
							</td>
							<td>${article.userId}</td>
           					<td>${article.hit}</td>
           					<td>${article.recommend}</td>
           					<td>${article.registerTime}</td>
       				</tr>            
				</c:forEach>   
      		</tbody>
    	</table>
  	</div>
	<div class="row">${navigation.navigator}</div>
</div>
<form id="form-param" method="get" action="${root}/board/list">
	<!-- <input type="hidden" id="p-action" name="action" value=""> -->
	<input type="hidden" id="p-pgno" name="pgno" value="">
	<input type="hidden" id="p-key" name="key" value="">
	<input type="hidden" id="p-word" name="word" value="">
</form>

<script>
	let titles = document.querySelectorAll(".article-title");
		titles.forEach(function (title) {
			title.addEventListener("click", function () {
				console.log(this.getAttribute("data-no"));
				location.href = "${root}/board/view?articleno=" + this.getAttribute("data-no");
			});
		});
	document.querySelector("#btn-mv-register").addEventListener("click", function () {
		location.href = "${root}/board/write";
	});
  
	document.querySelector("#btn-search").addEventListener("click", function () {
		let form = document.querySelector("#form-search");
		form.setAttribute("action", "${root}/board/article");
		form.submit();
  	});
  
	document.querySelector("#word").addEventListener('keyup', function (e) {
		if(e.key === 'Enter') {
			let form = document.querySelector("#form-search");
			form.setAttribute("action", "${root}/board/article");
			form.submit();
		}
	});

	let pages = document.querySelectorAll(".page-link");
	pages.forEach(function (page) {
	    page.addEventListener("click", function () {
			console.log(this.parentNode.getAttribute("data-pg"));
			/* document.querySelector("#p-action").value = "list"; */
			document.querySelector("#p-pgno").value = this.parentNode.getAttribute("data-pg");
			document.querySelector("#p-key").value = "${param.key}";
			document.querySelector("#p-word").value = "${param.word}";
			document.querySelector("#form-param").submit();
		});
	});
</script>
<%@ include file="../include/footer.jsp" %>