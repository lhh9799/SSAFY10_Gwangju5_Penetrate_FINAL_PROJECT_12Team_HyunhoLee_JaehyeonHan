package com.ssafy.vue.member.model.service;

import java.sql.SQLException;

import com.ssafy.vue.member.model.MemberDto;

public interface MemberService {

	MemberDto login(MemberDto memberDto) throws Exception;
	MemberDto userInfo(String userId) throws Exception;
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;
	//이현호 추가
	void join(MemberDto memberDto) throws SQLException;
}
