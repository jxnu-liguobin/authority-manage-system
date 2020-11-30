/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据源总的配置
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"cn.edu.jxnu"})
public class DataSourceConfig {

  @Bean(name = "myDataSource")
  @Qualifier("myDataSource")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource getMyDataSource() {
    return DataSourceBuilder.create().build();
  }
}
