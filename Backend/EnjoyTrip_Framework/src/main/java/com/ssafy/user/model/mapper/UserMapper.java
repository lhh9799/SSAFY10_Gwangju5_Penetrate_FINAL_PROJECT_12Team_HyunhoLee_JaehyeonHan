package com.ssafy.user.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.user.model.UserDto;

@Mapper
public interface UserMapper {
	public int join(UserDto userDto);
	public UserDto login(String userId, String userPassword);
	public void modify(UserDto userDto);
	public void chagePassword(String userId, String newPassword);
	public void delete(String userId);
	public UserDto select(String key, String keyword);
	String selectSalt(String userId);
}
