package com.ssafy.user.model.service;
import org.springframework.stereotype.Service;

import com.ssafy.user.model.UserDto;

@Service
public interface UserService {
	public int join(UserDto userDto);
	public UserDto login(String userId, String userPassword);
	public void chagePassword(String userId, String newPassword);
	public void delete(String userId);
	public void modify(UserDto userDto);
	public UserDto select(String key, String keyword);
}
