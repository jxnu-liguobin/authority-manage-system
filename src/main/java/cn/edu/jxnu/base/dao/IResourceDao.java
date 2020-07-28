package cn.edu.jxnu.base.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.entity.Resource;

/**
 * 资源管理dao
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:36:54.
 * @version V1.0
 */
@Repository
public interface IResourceDao extends IBaseDao<Resource, Integer> {

	/**
	 * :param 匹配@Param参数名
	 * 
	 * 删除角色的权限
	 * 
	 * @time 2018年4月10日 下午5:37:01.</br>
	 * @version V1.0</br>
	 * @param id</br>
	 */
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM tb_role_resource WHERE resource_id = :id")
	void deleteGrant(@Param("id") Integer id);

	/**
	 * 查询一个资源
	 * 
	 * @author 梦境迷离.
	 * @time 上午9:52:06
	 * @version V1.0
	 * @param id
	 * @return Resource
	 *
	 */
	Resource findById(@Param("id") Integer id);

}
