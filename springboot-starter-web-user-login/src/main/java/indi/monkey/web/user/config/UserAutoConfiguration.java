package indi.monkey.web.user.config;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import indi.monkey.web.user.service.UserService;
import indi.monkey.web.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties("spring-boot.user.auto.enabled")
@Slf4j
@ComponentScan({ "indi.monkey.web.user.controller", "indi.monkey.web.user.config" })
@EnableJpaRepositories(basePackages = "indi.monkey.web.user.dao", 
					   entityManagerFactoryRef = "${spring-boot.jpa.entityManagerFactoryRef}", 
					   transactionManagerRef = "${spring-boot.jpa.transactionManagerRef}")
public class UserAutoConfiguration {

	@Value("${spring-boot.user.auto.enabled}")
	String userModuleEnabled;

	@Autowired
	Environment env;

	@Autowired(required = false)
	@Qualifier("dataSource")
	DataSource dataSource;
	
	@Value("${spring-boot.jpa.entityManagerFactoryRef}")
	String managerFactoryName;
	@Value("${spring-boot.jpa.transactionManagerRef}")
	String transactionManagerName;
	
	@PostConstruct
	void init() {
		System.err.println(managerFactoryName);
		System.err.println(transactionManagerName);
	}
	

	@Bean(name = "defaultDataSource")
	@ConditionalOnMissingBean(DataSource.class)
	public DataSource defaultDataSource() {
		log.warn("can not find DataSource in spring context, \t using default datasource configuration");
		return DataSourceBuilder.create().
				driverClassName("com.mysql.cj.jdbc.Driver")
				.username("root")
				.password("root")
				.url("jdbc:mysql://192.168.2.69:3306/test?charset=utf8mb4&useSSL=false&serverTimezone=UTC").build();
	}

	@Bean(name = "defaultEntityManagerFactory")
	@ConditionalOnMissingBean(EntityManagerFactory.class)
	public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		if (dataSource == null) {
			factory.setDataSource(defaultDataSource());
		} else {
			factory.setDataSource(dataSource);
		}
		factory.setPackagesToScan("indi.monkey.web.user.entity");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", "create");
		jpaProperties.put("hibernate.show-sql", "true");
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		factory.setJpaProperties(jpaProperties);
		return factory;
	}

	@Bean(name = "defaultTransactionManager")
	@ConditionalOnBean(name = "defaultEntityManagerFactory")
	@ConditionalOnMissingBean(PlatformTransactionManager.class)
	public PlatformTransactionManager defaultTransactionManager() {
		EntityManagerFactory factory = defaultEntityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}

	@Bean
	@ConditionalOnMissingBean(UserService.class)
	public UserService userService() {
		if (String.valueOf(Boolean.TRUE).equalsIgnoreCase(userModuleEnabled)) {
			UserServiceImpl userServiceImpl = new UserServiceImpl();
			return userServiceImpl;
		}
		return new UserService() {
		};
	}
}
