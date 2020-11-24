/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.User;
import reactor.core.publisher.Mono;

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
     * @return 用户
     */
    Mono<User> findByUserName(String username);

    /**
     * 根据账号查询用户
     *
     * @param userCode
     * @return 用户
     */
    Mono<User> findByUserCode(String userCode);

    /**
     * 增加或者修改用户
     *
     * @param user
     */
    Mono<User> saveOrUpdate(User user);

    /**
     * 给用户分配角色
     *
     * @param id
     * @param roleIds
     */
    Mono<User> grant(Integer id, String[] roleIds);
}
