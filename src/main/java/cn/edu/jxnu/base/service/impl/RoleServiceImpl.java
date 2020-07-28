package cn.edu.jxnu.base.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.dao.IRoleDao;
import cn.edu.jxnu.base.entity.Resource;
import cn.edu.jxnu.base.entity.Role;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.service.IResourceService;
import cn.edu.jxnu.base.service.IRoleService;
import cn.edu.jxnu.base.service.IUserService;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色服务实现类
 * 
 * @author 梦境迷离 
 * @time 2018年4月10日 下午5:56:04.
 * @version V1.0
 */
@Service
@Slf4j
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements IRoleService {

	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IResourceService resourceService;
	@Autowired
	private IUserService userService;

	@Override
	public IBaseDao<Role, Integer> getBaseDao() {
		return this.roleDao;
	}

	@Override
	public void saveOrUpdate(Role role) {
		log.info("saveOrUpdate:" + role);
		//修改
		if (role.getId() != null) {
			Role dbRole = find(role.getId());
			dbRole.setUpdateTime(new Date());
			dbRole.setName(role.getName());
			dbRole.setDescription(role.getDescription());
			dbRole.setUpdateTime(new Date());
			dbRole.setStatus(role.getStatus());
			update(dbRole);
		} else {
			role.setCreateTime(new Date());
			role.setUpdateTime(new Date());
			save(role);
		}
	}

	@Override
	public void delete(Integer id) {
		log.info("delete:" + id);
		Role role = find(id);
		Assert.state(!"administrator".equals(role.getRoleKey()), "超级管理员角色不能删除");
		List<User> list = userService.findAll();
		for (User user : list) {
			Set<Role> set = user.getRoles();
			for (Role r : set) {
				Assert.state((r.getId() != id), "该角色下有用户，不可删除");
			}
		}
		super.delete(id);
	}

	@Override
	public void grant(Integer id, String[] resourceIds) {
		log.info("grant:" + id);
		Role role = find(id);
		Assert.notNull(role, "角色不存在");

		Assert.state(!"administrator".equals(role.getRoleKey()), "超级管理员角色不能进行资源分配");
		Resource resource;
		Set<Resource> resources = new HashSet<Resource>();
		if (resourceIds != null) {
			for (int i = 0; i < resourceIds.length; i++) {
				if (StringUtils.isBlank(resourceIds[i]) || "0".equals(resourceIds[i])) {
					continue;
				}
				Integer rid = Integer.parseInt(resourceIds[i]);
				resource = resourceService.find(rid);
				resources.add(resource);
			}
		}
		role.setResources(resources);
		update(role);
	}

	@Override
	public Role find(String id) {
		return null;
	}

}
