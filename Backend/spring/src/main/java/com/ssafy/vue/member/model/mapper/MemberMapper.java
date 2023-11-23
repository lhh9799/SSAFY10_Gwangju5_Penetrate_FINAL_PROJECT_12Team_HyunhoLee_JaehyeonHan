package com.ssafy.vue.member.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.vue.member.model.ItineraryDto;
import com.ssafy.vue.member.model.MemberDto;
import com.ssafy.vue.member.model.NewPwdDto;

@Mapper
public interface MemberMapper {

	MemberDto login(MemberDto memberDto) throws SQLException;
	MemberDto userInfo(String userId) throws SQLException;
	void saveRefreshToken(Map<String, String> map) throws SQLException;
	Object getRefreshToken(String userid) throws SQLException;
	void deleteRefreshToken(Map<String, String> map) throws SQLException;
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
