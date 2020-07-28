//package cn.edu.jxnu.base.controller.admin;
//
//import java.io.IOException;
//import java.util.concurrent.TimeoutException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.SecurityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.edu.jxnu.base.common.Constats;
//import cn.edu.jxnu.base.common.JsonResult;
//import cn.edu.jxnu.base.controller.BaseController;
//import cn.edu.jxnu.base.entity.BorrowBook;
//import cn.edu.jxnu.base.entity.User;
//import cn.edu.jxnu.base.service.IBorrowBookService;
//import cn.edu.jxnu.base.service.IUserService;
//import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
//import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 主页控制层，公共控制层，不需要权限
// * 
// * @author 梦境迷离
// * @time 2018年4月10日 下午5:14:11.
// * @version V1.0
// */
//@Controller
//@Slf4j
//public class AdminIndexController extends BaseController {
//
//	@Autowired
//	private IUserService userService;
//	@Autowired
//	private IBorrowBookService borrowBookService;
//
//	/**
//	 * 登陆
//	 * 
//	 * @time 2018年4月10日 下午5:14:23.
//	 * @version V1.0
//	 * @return String
//	 */
//	@RequestMapping(value = { "/admin/", "/admin/index" })
//	public String index() {
//		log.info("登录界面");
//		return "admin/index";
//	}
//
//	/**
//	 * 首页查询
//	 * 
//	 * @time 2018年4月10日 下午5:14:38.
//	 * @version V1.0
//	 * @return String
//	 */
//	@RequestMapping(value = { "/admin/welcome" })
//	public String welcome() {
//
//		return "admin/welcome";
//	}
//
//	/**
//	 * 用户个人信息页面
//	 * 
//	 * @time 2018年4月10日 下午5:15:22.
//	 * @version V1.0
//	 * @param map
//	 * @param response
//	 * @return String
//	 * @throws TimeoutException
//	 */
//	@RequestMapping(value = { "/admin/info" })
//	public String info(ModelMap map, HttpServletResponse response, Integer id) throws TimeoutException {
//		log.info("用户id:" + id);
//		User u = (User) SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + id);
//		if (u != null) {
//			/**
//			 * 没有过期
//			 */
//			map.put("user", u);
//		} else {
//			try {
//				/**
//				 * 已经过期
//				 */
//				throw new TimeoutException("因超时无法获取您的个人信息,即将退出登录");
//			} catch (Exception e) {
//				map.put("message", e.getMessage());
//			} finally {
//				/**
//				 * 重定向到登录页面
//				 */
//				redirect(response, "/admin/login");
//			}
//		}
//		return "admin/info";
//	}
//
//	/**
//	 * 用户已借阅书籍页面
//	 * 
//	 * @time 2018年4月10日 下午5:15:43.
//	 * @version V1.0
//	 * @return String
//	 */
//	@RequestMapping(value = { "/admin/borrow" })
//	public String borrow() {
//		// 给页面传入必须的已借书籍
//		return "admin/borrow";
//	}
//
//	/**
//	 * 注册，不能拦截
//	 * 
//	 * @time 2018年4月10日 下午5:16:03.
//	 * @version V1.0
//	 * @param map
//	 * @return String
//	 */
//	@RequestMapping(value = { "/assets/regist" })
//	public String regist(ModelMap map) {
//		/**
//		 * 携带一个map,用于添加注册表单数据
//		 */
//		return "admin/regist/form";
//	}
//
//	/**
//	 * 登录页面的注册专用
//	 * 
//	 * @time 2018年4月10日 下午5:16:27.
//	 * @version V1.0
//	 * @param user
//	 * @param map
//	 * @return JsonResult
//	 */
//	@RequestMapping(value = { "/assets/edit" }, method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult registAdd(User user, ModelMap map) {
//		try {
//			log.info("regist:" + user.toString());
//			userService.saveOrUpdate(user);
//		} catch (Exception e) {
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success("注册成功,3秒后自动回到登录页面");
//	}
//
//	/**
//	 * 验证用户名【学号】是否已经被注册,委托给用户控制层
//	 * 
//	 * @time 2018年4月10日 下午5:16:41.
//	 * @version V1.0
//	 * @param userCode
//	 * @param request
//	 * @param response
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	@RequestMapping(value = { "/assets/isAvailable" }, method = RequestMethod.GET)
//	public void isAvailableUse(String userCode, HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		log.info("前台验证账户可用");
//		request.getRequestDispatcher("/admin/user/isAvailable/" + userCode).forward(request, response);
//	}
//
//	/**
//	 * 所有人均可修改的个人信息
//	 * 
//	 * @time 2018年4月10日 下午5:16:55.
//	 * @version V1.0
//	 * @param user
//	 * @param map
//	 * @return JsonResult
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	@ResponseBody
//	@RequestMapping(value = { "/assets/update" }, method = RequestMethod.POST)
//	public JsonResult allCanUpdate(User user, ModelMap map) throws ServletException, IOException {
//		log.info("user:/assets/update:" + user.toString());
//		try {
//			User u = (User) SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + user.getId());
//			if (u == null) {
//				// 已经过期
//				try {
//					throw new TimeoutException("因超时无法获取您的个人信息,即将退出登录");
//				} catch (TimeoutException e) {
//					map.put("message", e.getMessage());
//				} finally {
//					// 重定向到登录页面
//					redirect(response, "/admin/logout");
//				}
//			}
//			userService.saveOrUpdate(user);
//			// 更新session
//			SecurityUtils.getSubject().getSession().setAttribute(Constats.CURRENTUSER + user.getId(), user);
//		} catch (Exception e) {
//			return JsonResult.failure(e.getMessage());
//		}
//
//		return JsonResult.success();
//	}
//
//	/**
//	 * 查询用户已借阅的图书 每个人均可操作，不需要授权。超期不可自主归还
//	 * 
//	 * @time 2018年4月10日 下午5:17:45.
//	 * @version V1.0
//	 * @param map
//	 * @param uCode
//	 *            操作人
//	 * @return Page 类型 BorrowBook
//	 */
//	@RequestMapping(value = { "/assets/borrowList" })
//	@ResponseBody
//	public Page<BorrowBook> borrowList(ModelMap map, @RequestParam(value = "uCode") String uCode) {
//		User u = userService.findByUserCode(uCode);
//		SimpleSpecificationBuilder<BorrowBook> builder = new SimpleSpecificationBuilder<BorrowBook>();
//		String bookName = request.getParameter("inputBookName");
//		String bookAuthor = request.getParameter("inputAuthor");
//		String bookPress = request.getParameter("inputPublication");
//		if (StringUtils.isNotBlank(bookName)) {
//			builder.add("bookName", Operator.likeAll.name(), bookName);
//		}
//		if (StringUtils.isNotBlank(bookAuthor)) {
//			builder.add("bookAuthor", Operator.likeAll.name(), bookAuthor);
//
//		}
//		if (StringUtils.isNotBlank(bookPress)) {
//			builder.add("bookPress", Operator.likeAll.name(), bookPress);
//		}
//		User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + u.getId());
//		if (user.getId() != null) {
//			builder.add("userId", Operator.eq.name(), user.getId());
//		} else {
//			// 已经过期
//			try {
//				throw new TimeoutException("因超时无法获取您的个人信息,即将退出登录");
//			} catch (TimeoutException e) {
//				map.put("message", e.getMessage());
//			} finally {
//				// 重定向到登录页面
//				redirect(response, "/admin/logout");
//			}
//		}
//		// 得到已借阅的书籍
//		Page<BorrowBook> page = borrowBookService.findAll(builder.generateSpecification(), getPageRequest());
//		return page;
//	}
//
//}
