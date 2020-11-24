/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.dao.IResourceDao;
import cn.edu.jxnu.base.entity.Resource;
import cn.edu.jxnu.base.entity.ZtreeView;
import cn.edu.jxnu.base.service.IResourceService;
import cn.edu.jxnu.base.service.IRoleService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 资源服务实现类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Service
@Slf4j
@Transactional
public class ResourceServiceImpl extends BaseServiceImpl<Resource, Integer>
        implements IResourceService {

    @Autowired private IResourceDao resourceDao;

    @Autowired private IRoleService roleService;

    @Override
    public IBaseDao<Resource, Integer> baseDao() {
        return this.resourceDao;
    }

    @Override
    @Cacheable(value = "resourceCache", key = "'tree' + #roleId") // 指定缓存在哪个Cache上[使用自定义的key]
    public Flux<ZtreeView> tree(int roleId) {
        log.info("tree: " + roleId);
        final List<ZtreeView> resultTreeNodes = new ArrayList<>();
        return roleService
                .find(roleId)
                .map(
                        role -> {
                            Set<Resource> roleResources = role.getResources();
                            resultTreeNodes.add(new ZtreeView(0L, null, "系统菜单", true));
                            ZtreeView node;
                            Sort sort = new Sort(Direction.ASC, "parent", "id", "sort");
                            List<Resource> all = resourceDao.findAll(sort);
                            for (Resource resource : all) {
                                node = new ZtreeView();
                                node.setId(Long.valueOf(resource.getId()));
                                if (resource.getParent() == null) {
                                    node.setpId(0L);
                                } else {
                                    node.setpId(Long.valueOf(resource.getParent().getId()));
                                }
                                node.setName(resource.getName());
                                if (roleResources != null && roleResources.contains(resource)) {
                                    node.setChecked(true);
                                }
                                resultTreeNodes.add(node);
                            }
                            return resultTreeNodes;
                        })
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * 超级管理员不能分配资源
     *
     * @return
     */
    @Override
    @CacheEvict(value = "resourceCache") // 清除缓存元素外
    public Mono<Resource> saveOrUpdate(final Resource resource) {
        log.info("saveOrUpdate: " + resource.toString());
        if (resource.getId() != null) {
            return find(resource.getId())
                    .map(
                            dbResource -> {
                                dbResource.setUpdateTime(new Date());
                                dbResource.setName(resource.getName());
                                dbResource.setSourceKey(resource.getSourceKey());
                                dbResource.setType(resource.getType());
                                dbResource.setSourceUrl(resource.getSourceUrl());
                                dbResource.setLevel(resource.getLevel());
                                dbResource.setSort(resource.getSort());
                                dbResource.setIsHide(resource.getIsHide());
                                dbResource.setIcon(resource.getIcon());
                                dbResource.setDescription(resource.getDescription());
                                dbResource.setUpdateTime(new Date());
                                dbResource.setParent(resource.getParent());
                                update(dbResource).subscribe();
                                return dbResource;
                            });
        } else {
            resource.setCreateTime(new Date());
            resource.setUpdateTime(new Date());
            return save(resource);
        }
    }

    @Override
    @CacheEvict(value = "resourceCache") // 清除缓存元素外
    public Mono<Boolean> delete(Integer id) {
        log.info("delete: " + id);
        resourceDao.deleteGrant(id);
        return super.delete(id);
    }
}
