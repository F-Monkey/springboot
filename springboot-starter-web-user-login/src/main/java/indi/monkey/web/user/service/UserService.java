package indi.monkey.web.user.service;

import indi.monkey.web.user.exception.UnSupportException;

public interface UserService {
	
	default Object login(String userName, String password) throws Exception {
		throw new UnSupportException();
	}
	
	default boolean add(String userName,String password) throws Exception {
		throw new UnSupportException();
	}
	
}
