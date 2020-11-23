package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.User;

/**
 * 用户服务接口
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public interface IUserService extends IBaseService<User, Integer> {

    /**
     * 根据姓名查询用户
     *
     * @param username
     * @return 用户</ br>
     */
    User findByUserName(String username);

    /**
     * 根据账号查询用户
     *
     * @param userCode
     * @return 用户</ br>
     */
    User findByUserCode(String userCode);

    /**
     * 增加或者修改用户
     *
     * @param user</br>
     */
    void saveOrUpdate(User user);

    /**
     * 给用户分配角色
     *
     * @param id
     * @param roleIds</br>
     */
    void grant(Integer id, String[] roleIds);

}
