<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<%@ include file="../include/head.jsp"%>
	</head>
	
	<body>
		<%@ include file="../include/nav.jsp"%>
		<!-- 모달 시작 -->
		<div class="modal" id="modifyModal">
			<div class="modal-dialog">
				<div class="modal-content">
	
					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">비밀번호 확인</h4>
						<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
					</div>
	
					<!-- Modal body -->
					<div class="modal-body">
						<!-- 폼 시작 -->
						<form id="modifyform" class='was-validated' method="post" action="${root}/user/modifyform">
							<div class="input-group mb-3">
								<span class="input-group-text">비밀번호</span>
								<input type="password" id="modifyPassword" name="inputPassword" class="form-control" placeholder="비밀번호를 입력해주세요." required>
							</div>
							<div class='form-row float-end'>
								<button type="submit" class="btn btn-outline-primary">확인</button>
								<button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal">취소</button>
							</div>
						</form>
						<!-- 폼 끝 -->
					</div>
	
					<!-- Modal footer -->
					<div class="modal-footer"></div>
	
				</div>
			</div>
		</div>
		<!-- 모달 끝 -->
		<!-- 모달 시작 -->
		<div class="modal" id="deleteModal">
			<div class="modal-dialog">
				<div class="modal-content">
	
					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">비밀번호 확인</h4>
	
						<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
					</div>
	
					<!-- Modal body -->
					<div class="modal-body">
						<h5>계정을 삭제하시면 복구하실 수 없습니다.</h5>
						<!-- 폼 시작 -->
						<form id="deleteForm" class='was-validated' method="post" action="${root}/user/delete">
							<input type="hidden" name="action" value="delete" />
							<div class="input-group mb-3">
								<span class="input-group-text">비밀번호</span>
								<input type="password" id="deletePassword" name="inputPassword" class="form-control" placeholder="비밀번호를 입력해주세요." required>
							</div>
							<div class='form-row float-end'>
								<button type="submit" class="btn btn-outline-primary">확인</button>
								<button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal">취소</button>
							</div>
						</form>
						<!-- 폼 끝 -->
					</div>
	
					<!-- Modal footer -->
					<div class="modal-footer"></div>
	
				</div>
			</div>
		</div>
		<!-- 모달 끝 -->
		<div class="container-md mt-3">
			<div>
				<h2>${userDto.userName}님의정보</h2>
				<div class="float-right">
					<button class="btn btn-outline-success" id="btn-change" data-bs-toggle="modal" data-bs-target="#modifyModal">회원 정보 변경하기</button>
				</div>
			</div>
	
			<table class="table table-hover">
				<tbody>
					<tr>
						<td>아이디</td>
						<td>${userDto.userId}</td>
					</tr>
					<tr>
						<td>이름</td>
						<td>${userDto.userName}</td>
					</tr>
					<tr>
						<td>이메일</td>
						<td>${userDto.emailId}@${userDto.emailDomain}</td>
					</tr>
					<tr>
						<td>가입 날짜</td>
						<td>${userDto.joinDate}</td>
					</tr>
				</tbody>
			</table>
			<div class="col-auto text-center">
				<button type="button" id="btn-delete" class="btn btn-danger mb-3" data-bs-toggle="modal" data-bs-target="#deleteModal" onclick="document.querySelector('#deletePassword').focus()">계정 삭제</button>
			</div>
		</div>
	
		<%@ include file="../include/footer.jsp"%>
		<script src="/js/userinfo.js"></script>
	</body>
</html>