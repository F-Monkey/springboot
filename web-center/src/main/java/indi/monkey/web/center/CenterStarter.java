package indi.monkey.web.center;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import indi.monkey.web.user.controller.UserController;

@SpringBootApplication
public class CenterStarter {
	
	@Value("spring.datasource.url")
	String dataSourceURL;
	
	@PostConstruct
	void init() {
		System.err.println(dataSourceURL);
	}
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CenterStarter.class, args);
		System.out.println(context.getBean(UserController.class));
	}
}
