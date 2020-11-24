/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.dao;

import cn.edu.jxnu.base.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 用户管理dao
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

    /**
     * 根据姓名查询用户
     *
     * @param username
     * @return 用户
     */
    User findByUserName(String username);

    /**
     * 根据账号查询用户
     *
     * @param usercode
     * @return 用户
     */
    User findByUserCode(String usercode);
}
