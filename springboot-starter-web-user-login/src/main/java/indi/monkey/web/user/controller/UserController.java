package indi.monkey.web.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indi.monkey.web.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/login")
	public Object login(String userName, String password) {
		try {
			return userService.login(userName, password);
		} catch (Exception e) {
			return "user login error";
		}
	}
	
	@GetMapping("/add")
	public Object add(String userName, String password) {
		try {
			return userService.add(userName, password);
		} catch (Exception e) {
			return "user login error";
		}
	}
}
