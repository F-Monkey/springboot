package indi.monkey.web.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import indi.monkey.web.user.entity.User;

@Repository()
public interface UserRepository extends JpaRepository<User, Long>{
	long deleteById(long id);
	
	User findByUserNameAndPassword(String username,String password);
}
