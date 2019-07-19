package indi.monkey.web.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import indi.monkey.web.user.dao.UserRepository;
import indi.monkey.web.user.entity.User;
import indi.monkey.web.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository repository;
	
	@Override
	public Object login(String userName, String password) throws Exception {
		return repository.findByUserNameAndPassword(userName,password);
	}

	@Override
	@Transactional(transactionManager = "defaultTransactionManager")
	public boolean add(String userName, String password) throws Exception {
		
		try {
			User u = new User();
			u.setUserName(userName);
			u.setPassword(password);
			repository.save(u);
			return true;
		} catch (Exception e) {
			log.error("error:",e);
		}
		return false;
	}
	
}
