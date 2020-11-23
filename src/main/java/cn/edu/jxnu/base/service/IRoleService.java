package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.Role;

/**
 * 角色服务接口
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public interface IRoleService extends IBaseService<Role, Integer> {

    /**
     * 添加或者修改角色
     *
     * @param role</br>
     */
    void saveOrUpdate(Role role);

    /**
     * 给角色分配资源
     *
     * @param id
     * @param resourceIds</br>
     */
    void grant(Integer id, String[] resourceIds);

}
