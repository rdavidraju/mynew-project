package com.nspl.app.config;

//import java.util.HashMap;

import javax.inject.Inject;
/*import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;*/
import javax.sql.DataSource;
/*import javax.transaction.TransactionManager;

import org.kie.spring.factorybeans.RuntimeEnvironmentFactoryBean;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;*/
//import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
/*import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;*/
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
/*import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;*/
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/*import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;*/

@Configuration
@ImportResource(value= {"classpath:jbpm/jpa-spring.xml",})
//@EnableJpaRepositories("org.jbpm.services.task.impl.model")
@EnableJpaRepositories(basePackages ="org.jbpm.services.task.impl.model",entityManagerFactoryRef="jbpmEntityManagerFactory",transactionManagerRef="transactionManager")
/*@EnableJpaRepositories(basePackages ="com.nss.apinvoice.repository")*/

public class JbpmConfiguration{
	
	private final Logger log = LoggerFactory.getLogger(JbpmConfiguration.class);
    
/*    @Autowired
    JtaTransactionManager transactionManager;*/
	
	@Inject
	DataSource secondaryDataSource;
	
/*	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(Database.H2);
		return hibernateJpaVendorAdapter;
	}*/
	

/*	@Bean //(name = "jbpmEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean jbpmEntityManagerFactory(EntityManagerFactoryBuilder builder) throws Throwable {
		log.debug("Configuring Jbpm entity manager factory");
		
		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();

		entityManager.setJtaDataSource(secondayDataSource);
		entityManager.setPersistenceUnitName("org.jbpm.persistence.spring.jpa");
		entityManager.setPersistenceXmlLocation("classpath:jbpm/persistence.xml");
		return entityManager;
		
		return builder
	            .dataSource(secondayDataSource)
	            .packages("com.nss.apinvoice.repository")
	            .persistenceUnit("org.jbpm.persistence.spring.jpa")
	            .build();
	}*/
	
/*	@Bean(name = "btmTransactionManager")	
	public TransactionManager transactionManager() {				
		TransactionManagerServices btm = new TransactionManagerServices();
		return btm.getTransactionManager();
		
	}*/
	
	
	/*ClassPathXmlApplicationContext context =
		    new ClassPathXmlApplicationContext("classpath:jbpm/jpa-spring.xml");
	EntityManagerFactory emf = (EntityManagerFactory) context.getBean("JbpmentityManagerFactory");
	

	AbstractPlatformTransactionManager aptm = (AbstractPlatformTransactionManager) context.getBean( "transactionManager" );	
	
*/
	
	

}
