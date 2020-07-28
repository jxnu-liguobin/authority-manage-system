package cn.edu.jxnu.base

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import scala.Unit

/**
 * 获得环境变量
 *
 * @author 梦境迷离
 * @time 2018年5月16日
 * @version V2.0
 *
 */
@Configuration
class EnvironmentAwareUtil extends EnvironmentAware {

    /**
     * 1、extends实现Java接口
     * 2、覆盖Java的抽象方法
     * 3、override可选，入参和返回值必须相同
     * 4、如果方法访问权限是protected则可能需要使用Scala包级访问控制 protected[packageName]
     */
    override def setEnvironment(env: Environment): Unit = {
        // 通过 environment 获取到系统属性.
        println(env.getProperty("JAVA_HOME"));
        // 通过 environment 同样能获取到application.properties配置的属性.
        println(env.getProperty("spring.datasource.master.url"));
        println(env.getProperty("spring.datasource.cluster.url"));
        // 获取到前缀是"spring.datasource." 的属性列表值.
        val relaxedPropertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        println("spring.datasource.master.driver-class-name="
            + relaxedPropertyResolver.getProperty("master.driver-class-name"));
    }

}