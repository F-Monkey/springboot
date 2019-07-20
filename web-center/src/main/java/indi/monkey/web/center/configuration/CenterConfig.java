package indi.monkey.web.center.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CenterConfig {

	@Bean(name = "dataSource")
	@Primary
	public DataSource defaultDataSource() {
		log.warn("can not find DataSource in spring context, \t using default datasource configuration");
		return DataSourceBuilder.create()
				.driverClassName("com.mysql.cj.jdbc.Driver").username("root").password("root")
				.url("jdbc:mysql://192.168.2.69:3306/test?charset=utf8mb4&useSSL=false&serverTimezone=UTC").build();
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	@ConditionalOnMissingBean(EntityManagerFactory.class)
	public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
			@Qualifier("dataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setPackagesToScan("indi.monkey.web.user.entity");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", "create");
		jpaProperties.put("hibernate.show-sql", "true");
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		factory.setJpaProperties(jpaProperties);
		return factory;
	}

	@Primary
	@Bean(name = "transactionManager")
	@ConditionalOnBean(name = "entityManagerFactory")
	@ConditionalOnMissingBean(PlatformTransactionManager.class)
	public PlatformTransactionManager defaultTransactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		EntityManagerFactory factory = entityManagerFactory.getObject();
		return new JpaTransactionManager(factory);
	}
}
