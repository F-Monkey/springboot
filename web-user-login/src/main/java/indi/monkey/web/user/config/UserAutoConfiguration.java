package indi.monkey.web.user.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties("spring-boot.user.auto.enabled")
@Slf4j
public class UserAutoConfiguration {

	@Value("spring-boot.user.auto.enabled")
	String userModuleEnabled;

	@Autowired
	Environment env;

	@Bean
	@ConfigurationProperties(prefix = "default.datasource")
	public DataSourceProperties defaultDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "defaultDataSource")
	@ConditionalOnMissingBean(DataSource.class)
	public DataSource defaultDataSource() {
		log.warn("can not find DataSource in spring context");
		DataSourceProperties defaultDataSourceProperties = defaultDataSourceProperties();
		return DataSourceBuilder.create()
				.driverClassName(defaultDataSourceProperties.getDriverClassName())
				.username(defaultDataSourceProperties.getUsername())
				.password(defaultDataSourceProperties.getPassword())
				.url(defaultDataSourceProperties.getUrl()).build();
	}

	@Bean(name = "defaultEntityManagerFactory")
	@ConditionalOnBean(name = "defaultDataSource")
	public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(defaultDataSource());
		factory.setPackagesToScan("");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		jpaProperties.put("hibernate.show-sql", env.getProperty("hibernate.show-sql"));
		factory.setJpaProperties(jpaProperties);
		return factory;
	}

	@Bean(name = "defaultTransactionManager")
	@ConditionalOnBean(name = "defaultEntityManagerFactory")
	public PlatformTransactionManager defaultTransactionManager() {
		EntityManagerFactory factory = defaultEntityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}
}
