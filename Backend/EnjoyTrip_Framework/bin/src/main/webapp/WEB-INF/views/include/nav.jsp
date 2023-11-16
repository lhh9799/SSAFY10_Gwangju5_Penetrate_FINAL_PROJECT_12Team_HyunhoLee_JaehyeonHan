<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="root" value="${pageContext.request.contextPath}"/>

<!-- nav 시작 -->
<nav class="navbar navbar-expand-sm shadow-sm  sticky-lg-top bg-body-tertiary rounded ">
	<div class="container-fluid m-auto">
		<i class="fa-solid fa-suitcase-rolling"></i>
	   	<a class="navbar-brand" href="${root}/">
			<i class="bi bi-airplane"></i>
			EnjoyTrip
	   	</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			<ul class="navbar-nav">
				<li class="nav-item">
					<a class="nav-link" href="${root}/attraction/attraction">지역별관광정보</a>
	           </li>
	           <li class="nav-item">
	               <a class="nav-link" href="${root}/board/list">여행정보공유</a>
	           </li>
			</ul>
			
			<!-- 로그인 되어있지 않으면 나타나는 메뉴 (로그인, 회원가입) -->
			<c:if test="${empty userDto }">
				<ul id='logoutState' class="navbar-nav ms-auto" >
					<li class="nav-item">
						<a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#loginModal" id="loginModalNavItem">로그인</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#joinModal" id="joinModalNavItem">회원가입</a>
					</li>
				</ul>
			</c:if>
			
			<!-- 로그인 되어있으면 나타나는 메뉴 (회원정보, 로그아웃) 시작 -->
			<c:if test="${not empty userDto }">
				<ul id='loginState' class="navbar-nav ms-auto" >
					<li class="nav-item">
						<a class="nav-link" id="nowLonginMessage">${userDto.userName}님 환영합니다.</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${root}/user/userInfo">회원정보</a>
					</li>
					<li class="nav-item">
						<a name="logoutbtn" id="logoutbtn" href="${root}/user/logout" class="nav-link" >로그아웃</a>
					</li>
				</ul>
			</c:if>
		</div>
	</div>
</nav>
<!-- nav 끝 -->
    
<!-- 회원가입 모달 시작 -->
<div class="modal" id="joinModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<!-- Modal Header -->
			<div class="modal-header">
				<h4 class="modal-title">회원가입</h4>
					<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>
			<!-- Modal body -->
			<div class="modal-body">
				<!-- 회원가입 폼 시작 -->
				<form id="registrationForm" onsubmit="return false" method="POST" action="${root}/user/join">
					<div class="input-group mb-3">
						<span class="input-group-text">이름</span>
						<input type="text" id="registerName" name="registerName" class="form-control" placeholder="이름을 입력해주세요." required>
					</div>
					<div class="input-group mb-3">
						<span class="input-group-text" id="idArea">아이디</span>
						<input type="text" id="registerId" name="registerId" class="form-control" placeholder="아이디를 입력해주세요." required>
						<div class="valid-feedback">Good!</div>
						<div class="invalid-feedback">아이디는 4~16자의 영문, 숫자로만 구성되어야 합니다.</div>
					</div>
					<div class="input-group mb-3">
						<span class="input-group-text">비밀번호</span>
						<input type="password" id="registerPassword" name="registerPassword" class="form-control" placeholder="비밀번호를 입력해주세요." required>
						<div class="input-group-text">
							<input class="input" type="checkbox" id="isShow" name="remember">
							<label for='isShow' class='ms-2'>
								<i class="bi bi-eye-fill"></i>
							</label>
						</div>
					</div>
					<div class="input-group mb-3">
						<span class="input-group-text">비밀번호 확인</span>
						<input type="password" id="registerPasswordConfirm" name="registerPasswordConfirm" class="form-control" placeholder="비밀번호와 똑같이 입력해주세요" required>
					</div>
					<div class="input-group mb-3">
						<input type="text" id="registerEmailId" name="registerEmailId" class="form-control" placeholder="이메일을 입력해주세요." required>
						<span class="input-group-text">@</span>
						<input type="text" id="registerEmailDomain" name="registerEmailDomain" class="form-control" readonly required="required">
						<select class="form-select" id="emailDomainSelect" required>
							<option value='' selected>선택해주세요</option>
							<option value="samsung.com">삼성</option>
							<option value="naver.com">네이버</option>
							<option value="gmail.com">구글</option>
							<option value="userInput">직접 입력</option>
						</select>
					</div>
					<div class='form-row float-end'>
						<button type="submit" id="submitButton" class="btn btn-outline-primary">회원가입</button>
						<button type="reset" class="btn btn-outline-success">초기화</button>
						<button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal">취소</button>
					</div>
				</form>
				<!-- 회원가입 폼 끝 -->
			</div>
			<!-- Modal footer -->
			<div class="modal-footer"></div>
		</div>
	</div>
</div>
<!-- 회원가입 모달 끝 -->

<!-- 로그인 모달 시작-->
<div class="modal" id="loginModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<!-- Modal Header -->
			<div class="modal-header">
				<h4 class="modal-title">로그인</h4>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>
			<!-- Modal body -->
			<div class="modal-body">
				<!-- 로그인 폼 시작 -->
				<form id="loginForm" method="POST" action="${root}/user/login">
					<div class="mb-3 mt-3">
						<label for="loginId" class="form-label">아이디</label>
						<input type="text" class="form-control" id="loginId" name="loginId" placeholder="아이디를 입력해주세요" required="required">
						<div class="valid-feedback">입력 완료</div>
						<div class="invalid-feedback">아이디를 입력해주세요</div>
					</div>
					<div class="mb-3">
						<label for="userPassword" class="form-label">비밀번호:</label>
						<input type="password" class="form-control" id="userPassword" name="userPassword" placeholder="비밀번호를 입력해주세요" required="required">
						<div class="valid-feedback">입력 완료</div>
						<div class="invalid-feedback">비밀번호를 입력해주세요</div>
					</div>
					<button type="submit" class="btn btn-outline-primary">로그인</button>
				</form>
				<!-- 로그인 폼 끝 -->
			</div>
			<!-- Modal footer -->
			<div class="modal-footer">
				<input type="button" class='btn btn-outline-success' value="비밀번호 찾기">
				<button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal">취소</button>
			</div>
		</div>
	</div>
</div>
<!-- 로그인 모달 끝 -->

<script src="/js/nav.js"></script>
<script>
	var userDto = '<%=session.getAttribute("userDto")%>';
	
	if(userDto == 'null') {
		//'회원가입'클릭 시 '이름' 입력 란 포커스
		document.querySelector("#joinModalNavItem").addEventListener("click", (event) => {
		    document.querySelector("#registerName").focus();
		});
		
		//'로그인' 클릭 시 '아이디' 입력 란 포커스
		document.querySelector("#loginModalNavItem").addEventListener("click", (event) => {
		    document.querySelector("#loginId").focus();
		});
	}
	
	
</script>