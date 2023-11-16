package com.ssafy.user.model.dao;

import com.ssafy.user.model.UserDto;

public interface UserDao {
	public int join(UserDto userDto);
	public UserDto login(String userId, String userPassword);
	public UserDto modify(UserDto userDto);
	public int chagePassword(String userId, String newPassword);
	public void delete(String userId);
	public UserDto select(String key, String keyword);
	String selectSalt(String userId);
}
