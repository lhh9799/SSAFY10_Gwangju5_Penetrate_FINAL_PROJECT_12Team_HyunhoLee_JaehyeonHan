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
			<h2>비밀번호 수정</h2>
		</div>
		<form id="pwdForm" class='was-validated' method="post"
			action="${root}/user/modifyPwd">
			<input type="hidden" name="action" value="modifyPwd" />
			<div class="input-group mb-3">
				<span class="input-group-text">기존 비밀번호</span> <input type="password"
					id="inputPassword" name="inputPassword" class="form-control"
					placeholder="기존 비밀번호를 입력해주세요" required>
				<div class="input-group-text">
					<input class="input" type="checkbox" id="isPassShow" name="remember">
					<label for='is' class='ms-2'> <i class="bi bi-eye-fill"></i>
						<!-- 
                                글씨로 남기기에는 공간이 부족해서 아이콘으로 구해왔습니다
                                자바스크립트로 아래 아이콘으로 바꾸시면 될 것 같습니다
                                <i class="bi bi-eye-slash-fill"></i> 
                            -->
					</label>
				</div>
			</div>
			<div class="input-group mb-3">
				<span class="input-group-text">새로운 비밀번호</span> <input type="password"
					id="newPassword" name="newPassword" class="form-control"
					placeholder="새로운 비밀번호를 입력해주세요" required>
			</div>
			<div class="input-group mb-3">
				<span class="input-group-text">새로운 비밀번호 확인</span> <input type="password"
					id="newPwConfirm" class="form-control"
					placeholder="새로운 비밀번호와 똑같이 입력해주세요" required>
			</div>
			<div class='form-row float-end'>
				<button type="button" id="btn-pwd-change" class="btn btn-outline-primary">
					변경</button>
				<button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal">취소</button>
			</div>
		</form>

	</div>
	<div>
		<br>
	</div>
	<%@ include file="../include/footer.jsp"%>
	<script>
	document.getElementById("isPassShow").addEventListener("change", function() {
        let memberPwElement = document.querySelector("#inputPassword");
        let newPwElement = document.querySelector("#newPassword");
        let memberPwConfirmElement = document.querySelector("#newPwConfirm");

        // 비밀번호 보이기/감추기 체크박스
        let isShowElement = document.querySelector("#isPassShow");

        if (isShowElement.checked) {
            memberPwElement.type = "text";
            newPwElement.type = "text";
            memberPwConfirmElement.type = "text";
        } else {
            memberPwElement.type = "password";
            newPwElement.type = "password";
            memberPwConfirmElement.type = "password";
        }
    });
	document.getElementById("btn-pwd-change").addEventListener("click", function() {
		if(document.querySelector("#newPassword").value ===document.querySelector("#newPwConfirm").value){
			document.getElementById("pwdForm").submit();
		}
		else{
			alert("새로운 비밀번호와 확인이 다릅니다");
		}
		
	});
	</script>
</body>
</html>