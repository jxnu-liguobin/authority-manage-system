/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.Resource;
import cn.edu.jxnu.base.entity.ZtreeView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 资源服务接口
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public interface IResourceService extends IBaseService<Resource, Integer> {

  /**
   * 获取角色的权限树
   *
   * @param roleId 角色ID
   * @return List ZtreeView
   */
  Flux<ZtreeView> tree(int roleId);

  /**
   * 修改或者新增资源
   *
   * @param resource 资源
   * @return Mono Resource
   */
  Mono<Resource> saveOrUpdate(Resource resource);

  /**
   * 根据id查询资源
   *
   * @param integer 资源ID
   * @return Resource Resource
   */
  Mono<Resource> find(Integer integer);
}
