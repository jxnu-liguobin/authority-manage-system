package cn.edu.jxnu.base.config.ds;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 主数据源配置
 * 
 * @author 梦境迷离
 * @version V1.0
 */
@Configuration
/**
 * 开启事务支持
 */
@EnableTransactionManagement
/**
 * 用于取代xml形式的配置文件
 */
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryMaster", transactionManagerRef = "transactionManagerMaster", basePackages = {
		"cn.edu.jxnu.base.dao" }) // 设置repository所在位置
public class RepositoryMasterConfig {

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("master")
	private DataSource master;

	@Bean(name = "entityManagerMaster")
	@Primary
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	}

	@Bean(name = "entityManagerFactoryMaster")
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(master).properties(getVendorProperties(master)).packages("cn.edu.jxnu.base.entity") // 设置实体类所在位置
				.persistenceUnit("masterPersistenceUnit").build();
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

	/**
	 * 事务管理器
	 * 
	 * @time 2018年4月10日 下午5:03:14.</br>
	 * @version V1.0</br>
	 * @param builder
	 * @return</br>
	 * @throws </br>
	 */
	@Bean(name = "transactionManagerMaster")
	@Primary
	PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	}
}