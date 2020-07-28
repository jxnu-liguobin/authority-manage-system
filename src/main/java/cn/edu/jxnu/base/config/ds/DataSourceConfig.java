package cn.edu.jxnu.base.config.ds;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 多数据源总的配置
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午4:58:50
 * @version V1.0
 */
@Configuration
public class DataSourceConfig {
	@Bean(name = "master")
	@Qualifier("master")
	/**
	 * 「多数据源配置的时候注意，必须要有一个主数据源，用 Primary 标志该 Bean」
	 * 
	 * @time 2018年4月10日 下午4:59:04
	 * @version V1.0
	 * @return DataSource
	 */
	@Primary /* @Primary 标志这个 Bean 如果在多个同类 Bean 候选时，该 Bean 优先被考虑 */
	@ConfigurationProperties(prefix = "spring.datasource.master")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
	}

	/**
	 * 从数据源
	 * 
	 * @time 2018年4月10日 下午4:59:27 .
	 * @version V1.0
	 * @return DataSource
	 */
	@Bean(name = "cluster")
	@Qualifier("cluster")
	@ConfigurationProperties(prefix = "spring.datasource.cluster")
	public DataSource secondaryDataSource() {
		return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
	}
}