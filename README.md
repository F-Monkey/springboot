

### springboot 自定义starter
-- 自定义starter可以方便模块的管理、测试、以及松耦合


#### 规范：
	-- 1、module命名：spring-boot-starter-{groupName}-{moduleName}
	
#### 代码：
	-- 1、自定义Configuration：  
		1)注释：  
		-- @Configuration  // 标注是一个配置类  
		-- @ComponentScan  // 尽量标明需要依赖springIOC功能的packages，尽量细化，不宜太笼统  
		-- 其他注解：  
			 @EnableJpaRepositories(basePackages={}) // 启动springJpa功能，扫描JpaRepository  
	
		2)ConditionalOn*** :  
		-- @ConditionalOnBean  // 当有该类的bean就执行
		-- @ConditionalOnMissingBean  //当木有该类的bean就执行
		-- ***
	
	-- 2、如何配置多个数据源：以及事务管理？
		-- 以springboot JPA为例
		***
			@EnableJpaRepositories(basePackages = "${spring-boot.jpa.repository.packages}", 
					   entityManagerFactoryRef = "${spring-boot.jpa.entityManagerFactoryRef}", 
					   transactionManagerRef = "${spring-boot.jpa.transactionManagerRef}")
			public class UserAutoConfiguration {
				// ***CODE***
				@Bean(name = "${spring-boot.jpa.entityManagerFactoryRef}")
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

				@Bean(name = "${spring-boot.jpa.transactionManagerRef}")
				@ConditionalOnBean(name = "${spring-boot.jpa.entityManagerFactoryRef}")
				@ConditionalOnMissingBean(PlatformTransactionManager.class)
				public PlatformTransactionManager defaultTransactionManager() {
					EntityManagerFactory factory = defaultEntityManagerFactory().getObject();
					return new JpaTransactionManager(factory);
				}
			}
		***
		