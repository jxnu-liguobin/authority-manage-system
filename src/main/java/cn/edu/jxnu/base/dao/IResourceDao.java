package cn.edu.jxnu.base.dao;

import cn.edu.jxnu.base.entity.Resource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 资源管理dao
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Repository
public interface IResourceDao extends IBaseDao<Resource, Integer> {

    /**
     * :param 匹配@Param参数名
     * <p>
     * 删除角色的权限
     *
     * @param id
     */
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_role_resource WHERE resource_id = :id")
    void deleteGrant(@Param("id") Integer id);

    /**
     * 查询一个资源
     *
     * @param id
     * @return Resource
     */
    Optional<Resource> findById(@Param("id") Integer id);

}
