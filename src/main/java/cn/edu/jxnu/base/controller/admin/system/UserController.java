package cn.edu.jxnu.base.controller.admin.system;
//package cn.edu.jxnu.base.controller.admin.system;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.cache.Cache;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.edu.jxnu.base.common.JsonResult;
//import cn.edu.jxnu.base.common.MemorandumUtils;
//import cn.edu.jxnu.base.config.shiro.RetryLimitHashedCredentialsMatcher;
//import cn.edu.jxnu.base.controller.BaseController;
//import cn.edu.jxnu.base.entity.Role;
//import cn.edu.jxnu.base.entity.User;
//import cn.edu.jxnu.base.service.IRoleService;
//import cn.edu.jxnu.base.service.IUserService;
//import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
//import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 用户管理控制类
// * 
// * @author 梦境迷离
// * @time 2018年4月10日 下午5:24:52.
// * @version V1.0
// */
//@Controller
//@Slf4j
//@RequestMapping("/admin/user")
//public class UserController extends BaseController {
//
//	/**
//	 * 使用cache的密码缓存
//	 */
//	@Autowired
//	RetryLimitHashedCredentialsMatcher credentialsMatcher;
//	@Autowired
//	private IUserService userService;
//	@Autowired
//	private IRoleService roleService;
//	@Autowired
//	private MemorandumUtils memorandumUtils;
//
//	@RequestMapping(value = { "/", "/index" })
//	public String index() {
//		return "admin/user/index";
//	}
//
//	/**
//	 * 用户管理页面分页
//	 * 
//	 * @time 2018年4月10日 下午5:25:02.
//	 * @version V1.0
//	 * @return Page<User>
//	 */
//	@RequestMapping(value = { "/list" })
//	@ResponseBody
//	public Page<User> list() {
//		SimpleSpecificationBuilder<User> builder = new SimpleSpecificationBuilder<User>();
//		String searchText = request.getParameter("searchText");
//		if (StringUtils.isNotBlank(searchText)) {
//			builder.add("userName", Operator.likeAll.name(), searchText);
//		}
//
//		Page<User> page = userService.findAll(builder.generateSpecification(), getPageRequest());
//		return page;
//	}
//
//	/**
//	 * 打开用户添加页面
//	 * 
//	 * @time 2018年4月10日 下午5:25:20.
//	 * @version V1.0
//	 * @param map
//	 * @return String
//	 */
//	@RequestMapping(value = "/add", method = RequestMethod.GET)
//	public String add(ModelMap map) {
//		return "admin/user/form";
//	}
//
//	/**
//	 * 打开用户修改页面
//	 * 
//	 * @time 2018年4月10日 下午5:25:34.
//	 * @version V1.0
//	 * @param id
//	 * @param map
//	 * @return String
//	 */
//	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
//	public String edit(@PathVariable Integer id, ModelMap map) {
//		User user = userService.find(id);
//		map.put("user", user);
//		map.put("edit", "noCheck");
//		return "admin/user/form";
//	}
//
//	/**
//	 * 修改用户
//	 * 
//	 * @time 2018年4月10日 下午5:25:54.
//	 * @version V1.0
//	 * @param user
//	 * @param map
//	 * @param uCode
//	 *            操作人
//	 * @return JsonResult
//	 */
//	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult edit(User user, ModelMap map, @RequestParam("uCode") String uCode) {
//		try {
//			log.info("inputuser:" + user.toString());
//			userService.saveOrUpdate(user);
//			memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
//					"修改/新增用户", user.getUserCode() + " | " + user.getUserName());
//
//		} catch (Exception e) {
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success();
//	}
//
//	/**
//	 * 删除用户
//	 * 
//	 * @time 2018年4月10日 下午5:26:08.
//	 * @version V1.0
//	 * @param id
//	 * @param map
//	 * @param uCode
//	 *            操作人
//	 * @return JsonResult
//	 */
//	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult delete(@PathVariable Integer id, ModelMap map, @RequestParam("uCode") String uCode) {
//		String res = "";
//		try {
//			User beU = userService.find(id);
//			userService.delete(id);
//			User user = userService.find(id);
//			if (user != null && user.getDeleteStatus() == 1) {
//				res = "已注销";
//				memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
//						"注销用户", beU.getUserCode() + " | " + beU.getUserName());
//			}
//			if (user == null) {
//				res = "已删除";
//				memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
//						"删除用户", beU.getUserCode() + " | " + beU.getUserName());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success(res);
//	}
//
//	/**
//	 * 打开分配角色页面
//	 * 
//	 * @time 2018年4月10日 下午5:26:23.
//	 * @version V1.0
//	 * @param id
//	 * @param map
//	 * @return String
//	 */
//	@RequestMapping(value = "/grant/{id}", method = RequestMethod.GET)
//	public String grant(@PathVariable Integer id, ModelMap map) {
//		User user = userService.find(id);
//		map.put("user", user);
//
//		Set<Role> set = user.getRoles();
//		List<Integer> roleIds = new ArrayList<Integer>();
//		for (Role role : set) {
//			roleIds.add(role.getId());
//		}
//		map.put("roleIds", roleIds);
//
//		List<Role> roles = roleService.findAll();
//		map.put("roles", roles);
//		return "admin/user/grant";
//	}
//
//	/**
//	 * 分配角色
//	 * 
//	 * @time 2018年4月10日 下午5:26:40.
//	 * @version V1.0
//	 * @param id
//	 * @param roleIds
//	 * @param map
//	 * @return JsonResult
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
//	public JsonResult grantRole(@PathVariable Integer id, String[] roleIds, ModelMap map) {
//		try {
//			userService.grant(id, roleIds);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success();
//	}
//
//	/**
//	 * 恢复账号
//	 * 
//	 * @time 2018年4月10日 下午5:27:08.
//	 * @version V1.0
//	 * @param id
//	 * @return JsonResult
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/resume/{id}", method = RequestMethod.POST)
//	public JsonResult resume(@PathVariable Integer id) {
//		Cache<String, AtomicInteger> passwordRetryCache = credentialsMatcher.getPasswordRetryCache();
//		try {
//			User user = userService.find(id);
//			String userCode = user.getUserCode();
//			// 恢复账号或者恢复锁定
//			if (user.getDeleteStatus() == 1 || user.getLocked() == 1) {
//				user.setDeleteStatus(0);
//				user.setLocked(0);
//				userService.saveOrUpdate(user);
//				// 存在缓存，去除，因为可能用户是自动解锁的。
//				if (passwordRetryCache.get(userCode) != null) {
//					passwordRetryCache.remove(userCode);
//				}
//
//			} else {
//				// 不需要恢复
//				return JsonResult.failure("当前账号不可执行此操作");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success();
//	}
//
//	/**
//	 * 验证用户名【学号】是否已经被注册
//	 * 
//	 * @time 2018年4月10日 下午5:27:23.
//	 * @version V1.0
//	 * @param userCode
//	 * @return boolean
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/isExist", method = RequestMethod.GET)
//	public boolean isExist(String userCode) {
//		log.info("UserCode:" + userCode);
//		boolean result = true;
//		if (userCode != null) {
//			User user = userService.findByUserCode(userCode);
//			if (user != null) {
//				result = false;
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 修改时，验证永真
//	 * 
//	 * @time 2018年4月10日 下午5:27:33.
//	 * @version V1.0
//	 * @param userCode
//	 * @return boolean
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/isAllTrue", method = RequestMethod.GET)
//	public boolean isAllTrue(String userCode) {
//		return true;
//	}
//
//	/**
//	 * 验证用户名【学号】是否已经被注册,代理前端的账户验证
//	 * 
//	 * @time 2018年4月10日 下午5:27:45.
//	 * @version V1.0
//	 * @param userCode
//	 * @return boolean
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/isAvailable/{userCode}")
//	public boolean isAvailable(@PathVariable("userCode") String userCode) {
//		log.info("前台验证账户可用代理:" + userCode);
//		boolean result = true;
//		if (userCode != null) {
//			User user = userService.findByUserCode(userCode);
//			if (user != null) {
//				result = false;
//			}
//		}
//		return result;
//	}
//}
