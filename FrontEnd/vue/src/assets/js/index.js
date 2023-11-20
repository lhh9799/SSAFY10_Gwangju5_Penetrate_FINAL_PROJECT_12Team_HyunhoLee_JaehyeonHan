//비밀번호 보이기/감추기 체크박스 이벤트
document.getElementById("isShow").addEventListener("change", function() {
	let registerPasswordElement = document.querySelector("#registerPassword");
	let registerPasswordConfirmElement = document.querySelector("#registerPasswordConfirm");
	let isShowElement = document.querySelector("#isShow");
	
	if (isShowElement.checked) {
		registerPasswordElement.type = "text";
		registerPasswordConfirmElement.type = "text";
	} else {
		registerPasswordElement.type = "password";
		registerPasswordConfirmElement.type = "password";
	}
});

//이메일 도메인 Select - Option
document.getElementById("emailDomainSelect").addEventListener("change", () => {
	let emailDomainSelectElement = document.querySelector("#emailDomainSelect");
	let emailDomainSelect = emailDomainSelectElement.value;
	let emailDomainElement = document.querySelector("#registerEmailDomain");
	let emailDomain = emailDomainElement.value;
	
	switch(emailDomainSelect) {
		case "none":
			emailDomainElement.value = "";
			emailDomainElement.readOnly = "readOnly";
			break;
		case "userInput":
			emailDomainElement.readOnly = "";
			emailDomainElement.value = '';
			emailDomainElement.focus();
			break;
		default:
			emailDomainElement.value = emailDomainSelect;
			break;
	}
});

//회원가입 폼 (공백 검사)
document.querySelector("#submitButton").addEventListener("click", function () {
	if (!document.querySelector("#registerName").value) {
		alert("아이디 입력!!");
	} else if (!document.querySelector("#registerPassword").value) {
		alert("비밀번호 입력!!");
	} else if (!document.querySelector("#registerPasswordConfirm").value) {
		alert("비밀번호 확인 입력!!");
	} else if (!document.querySelector("#registerEmailId").value) {
		alert("이메일 입력!!");
	} else if (!document.querySelector("#registerEmailDomain").value === '선택해주세요') {
		alert("이메일 도메인 입력!!");
	}
	
	//비밀번호, 비밀번호 확인 문자열 일치 시
	else {
		if(document.querySelector("#registerPassword").value == document.querySelector("#registerPasswordConfirm").value) {
			let form = document.querySelector("#registrationForm");
			
			form.submit();
		} else {
			alert("비밀번호, 비밀번호 확인 불일치!!");
		}
	}
});

//유효성 검사 정규식
//이름 정규식 추가 필요, 비밀번호 확인 정규식 필요
const idRegex = /^[a-zA-Z0-9_]{4,16}$/;																			//아이디
const passwordRegex = /^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\d~!@#$%^&*()_+=]{10,}$/;	//비밀번호 (영문, 숫자, 특수문자 중 2가지 이상 조합하여 10자리 이내의 암호 정규식 ( 2 가지 조합))
const mobileRegex = /^\d{3}-\d{4}-\d{4}$/;																		//휴대폰 번호
const emailRegex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;			//이메일

const registerIdElement = document.querySelector("#registerId");

//아이디 유효성 검사 함수
function validateId(id) {
	//정규식과 아이디가 일치하는지 확인
	return idRegex.test(id);
}

//아이디 입력 시 유효성 검사 (정규식)
registerIdElement.addEventListener("keyup", (event) => {
	if (!validateId(registerIdElement.value)) {
		registerIdElement.classList.add("is-invalid");
		registerIdElement.classList.remove("is-valid");
	} else {
		registerIdElement.classList.add("is-valid");
		registerIdElement.classList.remove("is-invalid");
	}
});