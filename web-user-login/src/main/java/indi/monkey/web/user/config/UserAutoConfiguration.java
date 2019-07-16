package indi.monkey.web.user.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties("spring-boot.user.auto.enabled")
@Slf4j
public class UserAutoConfiguration {

	@Value("spring-boot.user.auto.enabled")
	String userModuleEnabled;

	@Bean
	@ConditionalOnMissingBean(DataSource.class)
	public DataSource buildDataSource() {
		log.info("can not find DataSource in spring context");
		return null;
	}
}
