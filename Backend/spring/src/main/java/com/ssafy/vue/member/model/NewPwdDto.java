package com.ssafy.vue.member.model;

public class NewPwdDto {
	private String userId;
	private String newUserPwd;
	private String newUserPwdCheck;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNewUserPwd() {
		return newUserPwd;
	}
	public void setNewUserPwd(String newUserPwd) {
		this.newUserPwd = newUserPwd;
	}
	public String getNewUserPwdCheck() {
		return newUserPwdCheck;
	}
	public void setNewUserPwdCheck(String newUserPwdCheck) {
		this.newUserPwdCheck = newUserPwdCheck;
	}
	
	@Override
	public String toString() {
		return "NewPwd [userId=" + userId + ", newUserPwd=" + newUserPwd + ", newUserPwdCheck=" + newUserPwdCheck
				+ ", getUserId()=" + getUserId() + ", getNewUserPwd()=" + getNewUserPwd() + ", getNewUserPwdCheck()="
				+ getNewUserPwdCheck() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
