package cn.edu.jxnu.base.dao;

import cn.edu.jxnu.base.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Dao层数据全部使用Spring Data JPA
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@NoRepositoryBean // 启动时不实例化
public interface IBaseDao<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
// JpaRepository支持基本的CRUD，JpaSpecificationExecutor用在复杂查询
}
