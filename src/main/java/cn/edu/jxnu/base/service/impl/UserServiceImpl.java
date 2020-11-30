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
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
    if (user.getId() != null) {
      return find(user.getId())
          .flatMap(
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
                return update(dbUser);
              })
          .log();
    } else {
      user.setCreateTime(new Date());
      user.setUpdateTime(new Date());
      user.setDeleteStatus(0);
      user.setPassword(MD5Utils.md5(user.getPassword()));
      // 设定默认分权为学生权限
      return roleService
          .find(2)
          .flatMap(
              r -> {
                Set<Role> roles = new HashSet<Role>();
                roles.add(r);
                user.setRoles(roles);
                return save(user);
              });
    }
  }

  /** 删除分两种：真正删除和转换为设置一个删除标志位，数据仍然保存 */
  @Override
  public Mono<Boolean> delete(Integer id) {
    return find(id)
        .flatMap(
            user -> {
              if ("admin".equals(user.getUserName())) {
                throw new RuntimeException("超级管理员用户不能删除");
              }
              if (user.getDeleteStatus() == 1) {
                BorrowBook[] u = borrowBookService.findByUserId(id);
                // 用户借过书，不允许直接删除信息。
                if (u.length == 0) {
                  throw new RuntimeException("用户还有借书记录，请还书后操作");
                }
                // 已被删除的，此次将真的删除。
                user.setUpdateTime(new Date());
                return delete(user).flatMap(f -> Mono.just(true));
              } else {
                // 置位1
                user.setDeleteStatus(1);
                user.setUpdateTime(new Date());
                return update(user).map(Objects::nonNull);
              }
            });
  }

  /**
   * 授权管理
   *
   * @param id 用户ID
   * @param roleIds 角色ID
   * @return Mono User
   */
  @Override
  public Mono<User> grant(Integer id, String[] roleIds) {
    return find(id)
        .flatMap(
            user -> {
              if (user == null) {
                throw new RuntimeException("用户不存在");
              }
              if ("admin".equals(user.getUserName())) {
                throw new RuntimeException("超级管理员用户不能修改管理角色");
              }
              if (roleIds != null) {
                Flux.fromArray(roleIds)
                    .flatMap(
                        x ->
                            roleService
                                .find(Integer.parseInt(x))
                                .map(
                                    r -> {
                                      if (r.getId() == 0) {
                                        return new RuntimeException("角色已被禁用，无法进行该操作");
                                      } else {
                                        return true;
                                      }
                                    }))
                    .all(c -> true)
                    .map(can -> isCanDelete(can, roleIds, user));
              }
              return Mono.just(user);
            })
        .log();
  }

  private Mono<User> isCanDelete(boolean can, String[] roleIds, User user) {
    if (can) {
      return Flux.fromArray(roleIds)
          .collectList()
          .map(
              x -> {
                Set<Role> roles = new HashSet<>();
                for (String rId : x) {
                  Integer rid = Integer.parseInt(rId);
                  roleService
                      .find(rid)
                      .map(
                          r -> {
                            roles.add(r);
                            user.setRoles(roles);
                            return update(user);
                          })
                      .log()
                      .subscribe();
                }
                return user;
              });
    } else {
      return Mono.just(user);
    }
  }
}
