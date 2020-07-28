package cn.edu.jxnu.base.dao;

import org.springframework.stereotype.Repository;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.entity.Role;

/**
 * 角色管理dao
 * 
 * 使用默认方法
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:37:50.
 * @version V1.0
 */
@Repository
public interface IRoleDao extends IBaseDao<Role, Integer> {

}
