<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../include/head.jsp" %>
</head>
<body>
	<%@ include file="../include/nav.jsp" %>

	<%-- 페이지만의 내용 --%>
	<div class="container p-4">
	
	<c:choose>
		<c:when test="${not empty msg}">
			${msg}
		</c:when>
		<c:otherwise>
			오류가 발생했습니다.
		</c:otherwise>
	</c:choose>
	
	<div>
		<a href="${root}/">메인 화면으로 이동</a>
	</div>
	</div>
<%@ include file="../include/footer.jsp" %>