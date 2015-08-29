package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.UserData;

public interface IUserDaoMapper {
	public UserData getUserById(Long id);

	public UserData getUserByName(String name);

	public List<Character> getAllUserInitials();

	public Integer countAllUsers();

	public List<UserData> getAllUsers();

	public List<UserData> getAllUsersByInitial(Character initial);

	public void createUser(UserData userData);

	public void updateUser(UserData userData);

	public void setPassword(@Param("userId") Long userId, @Param("password") String password);

	public void resetPassword(@Param("userId") Long userId, @Param("password") String password);

	public void deleteUser(Long userId);

}
