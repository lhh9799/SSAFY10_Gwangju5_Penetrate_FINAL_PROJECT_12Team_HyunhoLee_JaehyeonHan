//이메일 도메인 Select - Option
document.getElementById("modifyEmailDomainSelect").addEventListener("change", () => {
	let emailDomainSelectElement = document.querySelector("#modifyEmailDomainSelect");
	let emailDomainSelect = emailDomainSelectElement.value;
	let emailDomainElement = document.querySelector("#emailDomain");
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