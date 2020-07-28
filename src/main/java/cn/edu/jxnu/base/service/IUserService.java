package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.User;

/**
 * 用户服务接口
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:51:17.
 * @version V1.0
 */
public interface IUserService extends IBaseService<User, Integer> {

	/**
	 * 根据姓名查询用户
	 * 
	 * @time 2018年4月10日 下午5:51:46.</br>
	 * @version V1.0</br>
	 * @param username
	 * @return 用户</br>
	 */
	User findByUserName(String username);

	/**
	 * 根据账号查询用户
	 * 
	 * @time 2018年4月10日 下午5:51:33.</br>
	 * @version V1.0</br>
	 * @param userCode
	 * @return 用户</br>
	 */
	User findByUserCode(String userCode);

	/**
	 * 增加或者修改用户
	 * 
	 * @time 2018年4月10日 下午5:53:16.</br>
	 * @version V1.0</br>
	 * @param user</br>
	 */
	void saveOrUpdate(User user);

	/**
	 * 给用户分配角色
	 * 
	 * @time 2018年4月10日 下午5:53:24.</br>
	 * @version V1.0</br>
	 * @param id
	 * @param roleIds</br>
	 */
	void grant(Integer id, String[] roleIds);

}
