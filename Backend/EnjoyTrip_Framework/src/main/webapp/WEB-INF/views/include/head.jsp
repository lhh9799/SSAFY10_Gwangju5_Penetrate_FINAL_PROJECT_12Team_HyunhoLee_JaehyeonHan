<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- jstl 사용하기 위한 코드 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 프로젝트의 context 경로를 편하게 사용하기 위한 코드 --%>
<c:set var="root" value="${pageContext.request.contextPath}"/>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EnjoyTrip</title>
<!-- Failed to load resource: the server responded with a status of 404 () 해결 위함 -->
<link rel="shortcut icon" href="#">
<%-- 부트스트랩 사용을 위한 코드 --%>
<!-- CSS only -->
<!-- https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css -> Failed to load resource: the server responded with a status of 404 () -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
<link rel="stylesheet" href="/css/include.css">
<%-- request 객체에 msg가 들어있을 때 해당 내용 알림창 띄우기 --%>
<c:if test="${not empty msg}">
	<script>
		alert("${msg}");
	</script>
</c:if>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>