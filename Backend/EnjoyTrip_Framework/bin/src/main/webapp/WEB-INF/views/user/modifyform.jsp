<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ssafy.user.model.UserDto"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="../include/head.jsp"%>
</head>
<%@ include file="../include/nav.jsp"%>
<body>
	<%
		UserDto userDto = (UserDto) request.getAttribute("userDto");
	%>

	<div class="container-md m-3 p-3">
		<div>
			<h2>회원 정보 수정</h2>
		</div>
		<form id="modifyForm" class='was-validated' method="post" action="${root}/user/modify">
			<div class="input-group mb-3">
				<span class="input-group-text">아이디</span>
				<input type="text" id="userId" name="userId" class="form-control" value="${userDto.userId}" readonly="readonly">
			</div>
			<div class="input-group mb-3">
				<span class="input-group-text">이름</span> <input type="text" id="userName" name="userName" class="form-control" placeholder="이름을 입력해주세요." value="${userDto.userName}" required>
			</div>

			<div class="input-group mb-3">
				<span class="input-group-text">비밀번호 변경</span>
				<div>
					<a type="button" href="${root}/user/pwdform" class="btn btn-outline-success" id="btn-change-pwd">비밀번호 변경하기</a>
				</div>
			</div>
			<div class="input-group mb-3">
				<input type="text" id="emailId" name="emailId" class="form-control" placeholder="이메일을 입력해주세요." value="${userDto.emailId}" required>
				<span class="input-group-text">@</span>
				<input type="text" id="emailDomain" name="emailDomain" class="form-control" value="${userDto.emailDomain}" readonly required="required">
				<select class="form-select" id="modifyEmailDomainSelect">
					<option value='' selected>선택해주세요</option>
					<option value="samsung.com">삼성</option>
					<option value="naver.com">네이버</option>
					<option value="gmail.com">구글</option>
					<option value="userInput">직접 입력</option>
				</select>
			</div>
			<div class='form-row float-end'>
				<button type="submit" class="btn btn-outline-primary">회원 정보 변경</button>
				<button type="reset" class="btn btn-outline-success">초기화</button>
				<button type="button" class="btn btn-outline-danger" onclick="location.href='${root}/user/userInfo'">취소</button>
			</div>
		</form>

	</div>
	<div>
		<br>
	</div>
	<%@ include file="../include/footer.jsp"%>
	<script src="/js/modifyform.js"></script>
</body>
</html>