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
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * 角色服务实现类
 *
 * @author 梦境迷离
 * @version V1.0
 * @time 2018年4月10日 下午5:56:04.
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
        log.info("saveOrUpdate: " + role);
        // 修改
        if (role.getId() != null) {
            return find(role.getId())
                    .map(
                            dbRole -> {
                                dbRole.setUpdateTime(new Date());
                                dbRole.setName(role.getName());
                                dbRole.setDescription(role.getDescription());
                                dbRole.setUpdateTime(new Date());
                                dbRole.setStatus(role.getStatus());
                                update(dbRole).subscribe();
                                return dbRole;
                            });
        } else {
            role.setCreateTime(new Date());
            role.setUpdateTime(new Date());
            return save(role);
        }
    }

    @Override
    public Mono<Boolean> delete(Integer id) {
        log.info("delete: " + id);
        Mono<Role> roleMono = find(id);
        roleMono.subscribe(
                role -> {
                    Assert.state(!"administrator".equals(role.getRoleKey()), "超级管理员角色不能删除");
                    userService
                            .findAll()
                            .subscribe(
                                    user -> {
                                        Set<Role> set = user.getRoles();
                                        for (Role r : set) {
                                            Assert.state((!r.getId().equals(id)), "该角色下有用户，不可删除");
                                        }
                                    });
                });
        return super.delete(id);
    }

    @Override
    public Mono<Role> grant(Integer id, String[] resourceIds) {
        log.info("grant: " + id);
        return find(id).map(
                        role -> {
                            Assert.notNull(role, "角色不存在");
                            Assert.state(
                                    !"administrator".equals(role.getRoleKey()), "超级管理员角色不能进行资源分配");
                            Set<Resource> resources = new HashSet<Resource>();
                            if (resourceIds != null) {
                                for (String resourceId : resourceIds) {
                                    if (StringUtils.isBlank(resourceId) || "0".equals(resourceId)) {
                                        continue;
                                    }
                                    Integer rid = Integer.parseInt(resourceId);
                                    Mono<Resource> resource = resourceService.find(rid);
                                    resource.map(resources::add).subscribe();
                                }
                            }
                            role.setResources(resources);
                            update(role).subscribe();
                            return role;
                        });
    }
}
