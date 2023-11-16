package com.ssafy.user.model.service;

import org.springframework.stereotype.Service;

import com.ssafy.user.model.UserDto;
import com.ssafy.user.model.mapper.UserMapper;
import com.ssafy.util.Hashing;

@Service
public class UserServiceImpl implements UserService {

	private UserMapper userMapper;

	private UserServiceImpl(UserMapper userMapper) {
		super();
		this.userMapper = userMapper;
	}

	@Override
	public int join(UserDto userDto) {

		userDto.setSalt(Hashing.getInstance().getSalt());
		userDto.setUserPassword(Hashing.getInstance().getEncrypt(userDto.getUserPassword(), userDto.getSalt()));

		return userMapper.join(userDto);
	}

	@Override
	public UserDto login(String userId, String userPassword) {
		// TODO Auto-generated method stub
		String salt = userMapper.selectSalt(userId);

		userPassword = Hashing.getInstance().getEncrypt(userPassword, salt);
		
		return userMapper.login(userId, userPassword);
	}

	@Override
	public void chagePassword(String userId, String newPassword) {
		// TODO Auto-generated method stub
		userMapper.chagePassword(userId, newPassword);
	}

	@Override
	public void delete(String userId) {
		// TODO Auto-generated method stub
		userMapper.delete(userId);

	}

	@Override
	public void modify(UserDto userDto) {
		// TODO Auto-generated method stub
		userMapper.modify(userDto);
	}

	@Override
	public UserDto select(String key, String keyword) {
		// TODO Auto-generated method stub
		return userMapper.select(key, keyword);
	}

}
