package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.Role;

/**
 * 角色服务接口
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:50:59.
 * @version V1.0
 */
public interface IRoleService extends IBaseService<Role, Integer> {

	/**
	 * 添加或者修改角色
	 * 
	 * @time 2018年4月10日 下午5:53:44.</br>
	 * @version V1.0</br>
	 * @param role</br>
	 */
	void saveOrUpdate(Role role);

	/**
	 * 给角色分配资源
	 * 
	 * @time 2018年4月10日 下午5:53:36.</br>
	 * @version V1.0</br>
	 * @param id
	 * @param resourceIds</br>
	 */
	void grant(Integer id, String[] resourceIds);

}
