//package cn.edu.jxnu.base.controller.admin.system;
//
//import org.apache.commons.lang3.StringUtils;
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
//import cn.edu.jxnu.base.controller.BaseController;
//import cn.edu.jxnu.base.entity.Role;
//import cn.edu.jxnu.base.service.IRoleService;
//import cn.edu.jxnu.base.service.IUserService;
//import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
//import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
//
///**
// * 系统角色控制类
// * 
// * @author 梦境迷离
// * @time 2018年4月10日 下午5:21:54.
// * @version V1.0
// */
//@Controller
//@RequestMapping("/admin/role")
//public class RoleController extends BaseController {
//
//	@Autowired
//	private IRoleService roleService;
//
//	@Autowired
//	private MemorandumUtils memorandumUtils;
//
//	@Autowired
//	private IUserService userService;
//
//	/**
//	 * 打开角色管理首页页面
//	 * 
//	 * @time 2018年4月10日 下午5:22:03.
//	 * @version V1.0
//	 * @return String
//	 */
//	@RequestMapping(value = { "/", "/index" })
//	public String index() {
//		return "admin/role/index";
//	}
//
//	/**
//	 * 角色管理分页
//	 * 
//	 * @time 2018年4月10日 下午5:22:15.
//	 * @version V1.0
//	 * @return Page 类型 Role
//	 */
//	@RequestMapping(value = { "/list" })
//	@ResponseBody
//	public Page<Role> list() {
//		SimpleSpecificationBuilder<Role> builder = new SimpleSpecificationBuilder<Role>();
//		String searchText = request.getParameter("searchText");
//		if (StringUtils.isNotBlank(searchText)) {
//			builder.add("name", Operator.likeAll.name(), searchText);
//			builder.addOr("description", Operator.likeAll.name(), searchText);
//		}
//		Page<Role> page = roleService.findAll(builder.generateSpecification(), getPageRequest());
//		return page;
//	}
//
//	/**
//	 * 打开添加角色页面
//	 * 
//	 * @time 2018年4月10日 下午5:22:26.
//	 * @version V1.0
//	 * @param map
//	 * @return String
//	 */
//	@RequestMapping(value = "/add", method = RequestMethod.GET)
//	public String add(ModelMap map) {
//		Role role = new Role();
//		/**
//		 * 传入role实体，提交的时候才有默认值
//		 */
//		map.put("role", role);
//		return "admin/role/form";
//	}
//
//	/**
//	 * 打开角色修改页面
//	 * 
//	 * @time 2018年4月10日 下午5:22:38.
//	 * @version V1.0
//	 * @param id
//	 * @param map
//	 * @retur String
//	 */
//	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
//	public String edit(@PathVariable Integer id, ModelMap map) {
//		System.out.println("role id:" + id);
//		Role role = roleService.find(id);
//		map.put("role", role);
//		return "admin/role/form";
//	}
//
//	/**
//	 * 添加或修改角色
//	 * 
//	 * @time 2018年4月10日 下午5:23:10.
//	 * @version V1.0
//	 * @param role
//	 * @param map
//	 * @param uCode
//	 *            操作人
//	 * @return JsonResult
//	 */
//	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult edit(Role role, ModelMap map, @RequestParam("uCode") String uCode) {
//		try {
//			roleService.saveOrUpdate(role);
//			memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
//					"修改/新增角色", role.getRoleKey() + " | " + role.getName());
//		} catch (Exception e) {
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success();
//	}
//
//	/**
//	 * 删除角色
//	 * 
//	 * @time 2018年4月10日 下午5:23:32.
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
//		try {
//			memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
//					"删除角色", roleService.find(id).getRoleKey() + " | " + roleService.find(id).getName());
//			roleService.delete(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success();
//	}
//
//	/**
//	 * 打开授权页面
//	 * 
//	 * @time 2018年4月10日 下午5:23:40.
//	 * @version V1.0
//	 * @param id
//	 * @param map
//	 * @return String
//	 */
//	@RequestMapping(value = "/grant/{id}", method = RequestMethod.GET)
//	public String grant(@PathVariable Integer id, ModelMap map) {
//		Role role = roleService.find(id);
//		map.put("role", role);
//		return "admin/role/grant";
//	}
//
//	/**
//	 * 授权
//	 * 
//	 * @time 2018年4月10日 下午5:24:27.
//	 * @version V1.0
//	 * @param id
//	 * @param resourceIds
//	 * @param map
//	 * @return JsonResult
//	 */
//	@RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult grant(@PathVariable Integer id, @RequestParam(required = false) String[] resourceIds,
//			ModelMap map) {
//		try {
//			roleService.grant(id, resourceIds);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success();
//	}
//}
