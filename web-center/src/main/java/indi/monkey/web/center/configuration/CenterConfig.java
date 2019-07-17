package indi.monkey.web.center.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CenterConfig {
	
//	@Bean(name = "dataSource")
	public DataSource createDataSource() {
		return DataSourceBuilder.create()
				.driverClassName("com.mysql.cj.jdbc.Driver")
				.username("root")
				.password("root")
				.url("jdbc:mysql://192.168.2.69:3306/test?charset=utf8mb4&useSSL=false&serverTimezone=UTC").build();
	}
}
