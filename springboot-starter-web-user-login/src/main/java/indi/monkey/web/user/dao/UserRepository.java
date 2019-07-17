package indi.monkey.web.user.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import indi.monkey.web.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	@Transactional
	long deleteById(long id);
	
	User findByUserNameAndPassword(String username,String password);
}
