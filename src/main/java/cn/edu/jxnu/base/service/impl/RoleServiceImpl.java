/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.dao.IRoleDao;
import cn.edu.jxnu.base.entity.Resource;
import cn.edu.jxnu.base.entity.Role;
import cn.edu.jxnu.base.service.IResourceService;
import cn.edu.jxnu.base.service.IRoleService;
import cn.edu.jxnu.base.service.IUserService;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 角色服务实现类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月25日
 */
@Service
@Slf4j
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements IRoleService {

  @Autowired private IRoleDao roleDao;
  @Autowired private IResourceService resourceService;
  @Autowired private IUserService userService;

  @Override
  public IBaseDao<Role, Integer> baseDao() {
    return this.roleDao;
  }

  @Override
  public Mono<Role> saveOrUpdate(final Role role) {
    // 修改
    if (role.getId() != null) {
      return find(role.getId())
          .flatMap(
              dbRole -> {
                dbRole.setUpdateTime(new Date());
                dbRole.setName(role.getName());
                dbRole.setDescription(role.getDescription());
                dbRole.setUpdateTime(new Date());
                dbRole.setStatus(role.getStatus());
                return update(dbRole);
              })
          .log();
    } else {
      role.setCreateTime(new Date());
      role.setUpdateTime(new Date());
      return save(role).log();
    }
  }

  @Override
  public Mono<Boolean> delete(Integer id) {
    Mono<Role> roleMono = find(id);
    return roleMono.flatMap(
        role -> {
          if ("administrator".equals(role.getRoleKey())) {
            throw new RuntimeException("超级管理员角色不能删除");
          }
          return userService
              .findAll()
              .all(
                  user -> {
                    Set<Role> set = user.getRoles();
                    return set.parallelStream().map(Role::getId).anyMatch(r -> r.equals(id));
                  })
              .defaultIfEmpty(true)
              .flatMap(
                  count -> {
                    if (!count) return super.delete(id);
                    else throw new RuntimeException("该角色下有用户，不可删除");
                  });
        });
  }

  @Override
  public Mono<Role> grant(Integer id, String[] resourceIds) {
    return find(id)
        .flatMap(
            role -> {
              if (role == null) {
                throw new RuntimeException("角色不存在");
              }
              if ("administrator".equals(role.getRoleKey())) {
                throw new RuntimeException("超级管理员角色不能进行资源分配");
              }
              if (resourceIds != null) {
                for (String resourceId : resourceIds) {
                  if (StringUtils.isBlank(resourceId) || "0".equals(resourceId)) {
                    continue;
                  }
                  Integer rid = Integer.parseInt(resourceId);
                  resourceService
                      .find(rid)
                      .map(
                          r -> {
                            Set<Resource> resources = new HashSet<>();
                            resources.add(r);
                            role.setResources(resources);
                            return update(role);
                          });
                }
              }
              // 不更新
              return Mono.just(role);
            })
        .log();
  }
}
