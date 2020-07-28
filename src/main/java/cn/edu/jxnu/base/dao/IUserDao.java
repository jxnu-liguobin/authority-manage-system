package cn.edu.jxnu.base.dao;

import org.springframework.stereotype.Repository;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.entity.User;

/**
 * 用户管理dao
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:38:09.
 * @version V1.0
 */
@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

	/**
	 * 根据姓名查询用户
	 * 
	 * @time 2018年4月10日 下午5:38:16.</br>
	 * @version V1.0</br>
	 * @param username
	 * @return 用户</br>
	 */
	User findByUserName(String username);

	/**
	 * 根据账号查询用户
	 * 
	 * @time 2018年4月10日 下午5:38:36.</br>
	 * @version V1.0</br>
	 * @param usercode
	 * @return 用户</br>
	 */
	User findByUserCode(String usercode);

}
