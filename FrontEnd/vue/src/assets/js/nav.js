/*
 * JSP 바깥에서는 아래 코드 사용 불가 
 * var userDto = '<%=session.getAttribute("userDto")%>';
 * console.log로 출력 시 문자열 그대로 '<%=session.getAttribute("userDto")%>' 출력됨 -> JSP 파일 내부에 <script> 태그 사용
 */

/*
var userDto = '<%=session.getAttribute("userDto")%>';
console.log('userDto', userDto);

//'회원가입'클릭 시 '이름' 입력 란 포커스
document.querySelector("#joinModalNavItem").addEventListener("click", (event) => {
    document.querySelector("#registerName").focus();
});

//'로그인' 클릭 시 아이디 입력 란 포커스
document.querySelector("#loginModalNavItem").addEventListener("click", (event) => {
    document.querySelector("#loginId").focus();
});
*/