package com.ssafy.user.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ssafy.user.model.UserDto;
import com.ssafy.util.DBUtil;

public class UserDaoImpl implements UserDao {
	static private UserDao instance = new UserDaoImpl();
	private UserDaoImpl() {
		// TODO Auto-generated constructor stub
	}
	static public UserDao getInstance(){
		return instance;	
	}
	@Override
	public int join(UserDto userDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		
		try {
			conn = DBUtil.getInstance().getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("insert into members(user_id, user_name, user_password, email_id, email_domain, have_car , salt) \n");
			sql.append("values(?, ?, ?, ?, ?,?,?) \n");

			pstmt = conn.prepareStatement(sql.toString());
	
			pstmt.setString(1, userDto.getUserId());
			pstmt.setString(2, userDto.getUserName());
			pstmt.setString(3, userDto.getUserPassword());
			pstmt.setString(4, userDto.getEmailId());
			pstmt.setString(5, userDto.getEmailDomain());
			pstmt.setString(6, userDto.getSalt());
			return pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(pstmt, conn);
		}	
		
		return 0;
	}

	@Override
	public UserDto login(String userId, String userPassword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		UserDto userDto = null;
		sql.append("select * from members\n");
		sql.append("where user_id = ? && user_password=?");
		try {
			conn = DBUtil.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, userPassword);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			userDto = new UserDto();
			userDto.setUserId(rs.getString("user_id"));
			userDto.setUserName(rs.getString("user_name"));
			userDto.setUserPassword(rs.getString("user_password"));
			userDto.setEmailId(rs.getString("email_id"));
			userDto.setEmailDomain(rs.getString("email_domain"));
			userDto.setJoinDate(rs.getString("join_date"));
			
			return userDto;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public int chagePassword(String userId, String newPassword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int cnt=0;
		
		try {
			conn = DBUtil.getInstance().getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("update members \n");
			sql.append("set user_password=? \n");
			sql.append("where user_id =? \n");

			pstmt = conn.prepareStatement(sql.toString());
	
			pstmt.setString(1, newPassword);
			pstmt.setString(2, userId);
			cnt =pstmt.executeUpdate();

			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(pstmt, conn);
		}
		
		return cnt;
	}

	/*
	 * delete from members
		where user_id ='kim';
	 */
	@Override
	public void delete(String userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("delete from members \n");
			sql.append("where user_id =? \n");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, userId);
			pstmt.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(pstmt, conn);
		}
		
	}
	
	/*update members
	 * set user_name='3333', email_id='3333', email_domain='3333', have_car='0'
	where user_id ='1111';
	 * */
	@Override
	public UserDto modify(UserDto userDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		UserDto tmpDto = new UserDto();
		
		
		try {
			conn = DBUtil.getInstance().getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("update members \n");
			sql.append("set user_name=?, email_id=?, email_domain=?, have_car=? \n");
			sql.append("where user_id =? \n");

			pstmt = conn.prepareStatement(sql.toString());
	
			pstmt.setString(1, userDto.getUserName());
			pstmt.setString(2, userDto.getEmailId());
			pstmt.setString(3, userDto.getEmailDomain());
			pstmt.setString(4, userDto.getUserId());
			pstmt.executeUpdate();

			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(pstmt, conn);
		}
		
		return select("user_id",userDto.getUserId());
	}
	@Override
	public UserDto select(String key, String keyword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		UserDto userDto = null;
		sql.append("select * from members\n");
		sql.append("where user_id = ?");
		try {
			conn = DBUtil.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			userDto = new UserDto();
			userDto.setUserId(rs.getString("user_id"));
			userDto.setUserName(rs.getString("user_name"));
			userDto.setUserPassword(rs.getString("user_password"));
			userDto.setEmailId(rs.getString("email_id"));
			userDto.setEmailDomain(rs.getString("email_domain"));
			userDto.setJoinDate(rs.getString("join_date"));
			
			return userDto;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String selectSalt(String userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		UserDto userDto = null;
		sql.append("select * from members\n");
		sql.append("where user_id = ? ");
		try {
			conn = DBUtil.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
				rs.next(); 
				userDto = new UserDto();
				userDto.setSalt(rs.getString("salt"));
				
			
			
			return userDto.getSalt();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
