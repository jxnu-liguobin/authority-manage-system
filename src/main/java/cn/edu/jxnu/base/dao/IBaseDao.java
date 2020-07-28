package cn.edu.jxnu.base.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import cn.edu.jxnu.base.entity.BaseEntity;

/**
 * Dao层数据全部使用Spring Data JPA
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:33:27.
 * @version V1.0
 */
@NoRepositoryBean
/**
 * 启动时不实例化
 */
public interface IBaseDao<T extends BaseEntity, ID extends Serializable>
		/**
		 * JpaRepository支持基本的CRUD，JpaSpecificationExecutor用在复杂查询
		 */
		extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
