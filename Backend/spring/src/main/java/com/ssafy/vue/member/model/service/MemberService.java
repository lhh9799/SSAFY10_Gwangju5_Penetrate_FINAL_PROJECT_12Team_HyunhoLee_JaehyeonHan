package com.ssafy.vue.member.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.vue.member.model.ItineraryDto;
import com.ssafy.vue.member.model.MemberDto;
import com.ssafy.vue.member.model.NewPwdDto;

public interface MemberService {

	MemberDto login(MemberDto memberDto) throws Exception;
	MemberDto userInfo(String userId) throws Exception;
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;
	//이현호 추가
	void join(MemberDto memberDto) throws SQLException;
	//한재현 추가
	void delete(String userId) throws SQLException;
	//한재현 추가
	String getPwdFromId(String userId) throws SQLException;
	//한재현 추가
	void updatePwd(NewPwdDto newPwd) throws SQLException;
	//이현호 추가
	void registPlan(ItineraryDto itineraryDto) throws SQLException;
	//이현호 추가
	List<ItineraryDto> getPlan(String userId) throws SQLException;
	//이현호 추가
	void deleteOneDayPlan(Map<String, Object> map) throws SQLException;
	//한재현 추가
	int duplicateCheck(String userId) throws SQLException;
}
