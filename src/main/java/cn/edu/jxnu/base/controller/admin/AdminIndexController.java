package cn.edu.jxnu.base.controller.admin;

import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.entity.BorrowBook;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.service.IBorrowBookService;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
import cn.edu.jxnu.base.utils.Constats;
import cn.edu.jxnu.base.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 主页控制层，公共控制层，不需要权限
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Controller
@Slf4j
public class AdminIndexController extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IBorrowBookService borrowBookService;

    /**
     * 登陆
     */
    @RequestMapping(value = {"/admin/", "/admin/index"})
    public String index() {
        log.info("登录界面");
        return "admin/index";
    }

    /**
     * 首页查询
     */
    @RequestMapping(value = {"/admin/welcome"})
    public String welcome() {
        return "admin/welcome";
    }

    /**
     * 用户个人信息页面
     */
    @RequestMapping(value = {"/admin/info"})
    public String info(ModelMap map, HttpServletResponse response, Integer id) {
        log.info("用户id: " + id);
        User u = (User) SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + id);
        if (u != null) {
            map.put("user", u);
        } else {
            map.put("message", "因超时无法获取您的个人信息,即将退出登录");
            redirect(response, "/admin/login");
        }
        return "admin/info";
    }

    /**
     * 用户已借阅书籍页面
     */
    @RequestMapping(value = {"/admin/borrow"})
    public String borrow() {
        // 给页面传入必须的已借书籍
        return "admin/borrow";
    }

    /**
     * 注册，不能拦截
     */
    @GetMapping(value = {"/assets/regist"})
    public String regist(ModelMap map) {
        // 携带一个map，用于添加注册表单数据
        return "admin/regist/form";
    }

    /**
     * 登录页面的注册专用
     */
    @RequestMapping(value = {"/assets/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public Mono<JsonResult> registAdd(User user, ModelMap map) {
        try {
            log.info("regist:" + user.toString());
            userService.saveOrUpdate(user).subscribe();
        } catch (Exception e) {
            return Mono.just(JsonResult.failure(e.getMessage()));
        }
        return Mono.just(JsonResult.success("注册成功,3秒后自动回到登录页面"));
    }

    /**
     * 验证用户名【学号】是否已经被注册，委托给用户控制层
     */
    @RequestMapping(value = {"/assets/isAvailable"})
    public void isAvailableUse(String userCode, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("前台验证账户可用: /assets/isAvailable");
        request.getRequestDispatcher("/admin/user/isAvailable/" + userCode).forward(request, response);
    }

    /**
     * 所有人均可修改的个人信息
     */
    @ResponseBody
    @RequestMapping(value = {"/assets/update"}, method = RequestMethod.POST)
    public Mono<JsonResult> allCanUpdate(User user, ModelMap map, HttpServletResponse response) {
        log.info("path: /assets/update, user: " + user.toString());
        try {
            User u = (User) SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + user.getId());
            if (u == null) {
                // 已经过期
                // 重定向到登录页面
                map.put("message", "因超时无法获取您的个人信息，即将退出登录");
                redirect(response, "/admin/logout");
            }
            userService.saveOrUpdate(user).subscribe();
            // 更新session
            SecurityUtils.getSubject().getSession().setAttribute(Constats.CURRENTUSER + user.getId(), user);
        } catch (Exception e) {
            return Mono.just(JsonResult.failure(e.getMessage()));
        }

        return Mono.just(JsonResult.success());
    }

    /**
     * 查询用户已借阅的图书 每个人均可操作，不需要授权。超期不可自主归还
     */
    @RequestMapping(value = {"/assets/borrowList"})
    @ResponseBody
    public Mono<Page<BorrowBook>> borrowList(ModelMap map, @RequestParam(value = "uCode") String uCode, HttpServletRequest request,
                                             HttpServletResponse response) {
        Mono<User> uMono = userService.findByUserCode(uCode);
        SimpleSpecificationBuilder<BorrowBook> builder = new SimpleSpecificationBuilder<>();
        String bookName = request.getParameter("inputBookName");
        String bookAuthor = request.getParameter("inputAuthor");
        String bookPress = request.getParameter("inputPublication");
        if (StringUtils.isNotBlank(bookName)) {
            builder.add("bookName", Operator.likeAll.name(), bookName);
        }
        if (StringUtils.isNotBlank(bookAuthor)) {
            builder.add("bookAuthor", Operator.likeAll.name(), bookAuthor);

        }
        if (StringUtils.isNotBlank(bookPress)) {
            builder.add("bookPress", Operator.likeAll.name(), bookPress);
        }
        uMono.map(u -> (User) SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + u.getId())).subscribe(user -> {
            if (user.getId() != null) {
                builder.add("userId", Operator.eq.name(), user.getId());
            } else {
                // 已经过期
                try {
                    throw new TimeoutException("因超时无法获取您的个人信息,即将退出登录");
                } catch (TimeoutException e) {
                    map.put("message", e.getMessage());
                } finally {
                    // 重定向到登录页面
                    redirect(response, "/admin/logout");
                }
            }
        });
        // 得到已借阅的书籍
        return borrowBookService.findAll(builder.generateSpecification(), getPageRequest(request));
    }

}
