package org.laladev.moneyjinn.businesslogic.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.UserData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.IUserDaoMapper;

@Named
public class UserDao {

	@Inject
	IUserDaoMapper mapper;

	public UserData getUserById(final Long id) {
		final UserData userData = this.mapper.getUserById(id);
		return userData;
	}

	public List<Character> getAllUserInitials() {
		return this.mapper.getAllUserInitials();
	}

	public Integer countAllUsers() {
		return this.mapper.countAllUsers();
	}

	public List<UserData> getAllUsers() {
		return this.mapper.getAllUsers();
	}

	public List<UserData> getAllUsersByInitial(final Character initial) {
		return this.mapper.getAllUsersByInitial(initial);
	}

	public UserData getUserByName(final String name) {
		final UserData userData = this.mapper.getUserByName(name);
		return userData;
	}

	public Long createUser(final UserData userData) {
		this.mapper.createUser(userData);
		return userData.getId();
	}

	public void updateUser(final UserData userData) {
		this.mapper.updateUser(userData);

	}

	public void setPassword(final Long userId, final String password) {
		this.mapper.setPassword(userId, password);

	}

	public void resetPassword(final Long userId, final String password) {
		this.mapper.resetPassword(userId, password);

	}

	public void deleteUser(final Long userId) {
		this.mapper.deleteUser(userId);

	}

}
