/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.dao.IUserDao;
import cn.edu.jxnu.base.entity.BorrowBook;
import cn.edu.jxnu.base.entity.Role;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.service.IBorrowBookService;
import cn.edu.jxnu.base.service.IRoleService;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.utils.MD5Utils;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * 用户服务实现类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Service
@Slf4j
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements IUserService {

    @Autowired private IUserDao userDao;

    @Autowired private IRoleService roleService;

    @Autowired private IBorrowBookService borrowBookService;

    @Override
    public IBaseDao<User, Integer> baseDao() {
        return this.userDao;
    }

    @Override
    public Mono<User> findByUserName(String username) {
        log.info("findByUserName: " + username);
        return Mono.justOrEmpty(userDao.findByUserName(username));
    }

    @Override
    public Mono<User> findByUserCode(String usercode) {
        log.info("findByUserCode: " + usercode);
        return Mono.justOrEmpty(userDao.findByUserCode(usercode));
    }

    @Override
    public Mono<User> saveOrUpdate(final User user) {
        log.info("saveOrUpdate: " + user);
        if (user.getId() != null) {
            return find(user.getId())
                    .map(
                            dbUser -> {
                                dbUser.setUserName(user.getUserName());
                                // 20是前端的密码最大长度，考虑不周
                                if (user.getPassword().length() < 20) {
                                    // 避免二次加密
                                    dbUser.setPassword(MD5Utils.md5(user.getPassword()));
                                }
                                dbUser.setTelephone(user.getTelephone());
                                dbUser.setLocked(user.getLocked());
                                dbUser.setUpdateTime(new Date());
                                log.info("userinfo: " + user.toString());
                                update(dbUser).subscribe();
                                return dbUser;
                            });
        } else {
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setDeleteStatus(0);
            user.setPassword(MD5Utils.md5(user.getPassword()));
            // 设定默认分权为学生权限
            Set<Role> roles = new HashSet<Role>();
            Mono<Role> role = roleService.find(2);
            role.map(roles::add).subscribe();
            user.setRoles(roles);
            log.info("userinfo: " + user.toString());
            return save(user);
        }
    }

    /**
     * 删除分两种：真正删除和转换为设置一个删除标志位，数据仍然保存
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Boolean> delete(Integer id) {
        log.info("delete: " + id);
        return find(id).map(
                        user -> {
                            // false抛异常
                            // Assert.state(user.getDeleteStatus() == 0, "已删除用户不可重复删除");
                            Assert.state(!"admin".equals(user.getUserName()), "超级管理员用户不能删除");
                            // super.delete(id);
                            if (user.getDeleteStatus() == 1) {
                                BorrowBook[] u = borrowBookService.findByUserId(id);
                                // 用户借过书，不允许直接删除信息。
                                Assert.state((u.length == 0), "用户还有借书记录，请还书后操作");
                                // 已被删除的，此次将真的删除。
                                delete(user);
                            } else {
                                // 置位1
                                user.setDeleteStatus(1);
                                update(user);
                            }
                            return true;
                        });
    }

    /**
     * 授权管理
     *
     * @param id
     * @param roleIds
     */
    @Override
    public Mono<User> grant(Integer id, String[] roleIds) {
        log.info("grant" + id);
        return find(id).map(
                        user -> {
                            Assert.notNull(user, "用户不存在");
                            Assert.state(!"admin".equals(user.getUserName()), "超级管理员用户不能修改管理角色");
                            Set<Role> roles = new HashSet<>();
                            if (roleIds != null) {
                                Arrays.stream(roleIds)
                                        .forEach(
                                                x -> {
                                                    // 已删除的不可分配
                                                    roleService
                                                            .find(Integer.parseInt(x))
                                                            .subscribe(
                                                                    r -> {
                                                                        log.info(
                                                                                "role->Id: "
                                                                                        + r
                                                                                                .toString());
                                                                        Assert.state(
                                                                                r.getStatus() == 0,
                                                                                "角色已被禁用，无法进行该操作");
                                                                    });
                                                });
                                Arrays.stream(roleIds)
                                        .forEach(
                                                x -> {
                                                    Integer rid = Integer.parseInt(x);
                                                    roleService
                                                            .find(rid)
                                                            .map(roles::add)
                                                            .subscribe();
                                                });
                            }

                            user.setRoles(roles);
                            update(user).subscribe();
                            return user;
                        });
    }
}
