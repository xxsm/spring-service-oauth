package com.kairos.springboot.app.oauth.service;

import com.kairos.springboot.app.commons.users.entity.User;

public interface IUserService {
	
	public User findByUsername(String username);
	
	public User update(User user, Long id);
}
