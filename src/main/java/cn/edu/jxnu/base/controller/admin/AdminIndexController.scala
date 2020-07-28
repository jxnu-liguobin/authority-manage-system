package cn.edu.jxnu.base.controller.admin

import cn.edu.jxnu.base.controller.BaseController
import org.springframework.beans.factory.annotation.Autowired
import cn.edu.jxnu.base.service.IUserService
import cn.edu.jxnu.base.service.IBorrowBookService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.ui.Model
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.apache.shiro.SecurityUtils
import cn.edu.jxnu.base.common.Constats
import java.util.concurrent.TimeoutException
import cn.edu.jxnu.base.entity.User
import cn.edu.jxnu.base.common.JsonResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.data.domain.Page
import cn.edu.jxnu.base.entity.BorrowBook
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder
import org.apache.commons.lang3.StringUtils
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator
import org.springframework.beans.factory.annotation.Required

/**
 * 注意：Scala类的文件名和类名可以不不同
 * @author 梦境迷离.
 * @time 2018年5月15日
 * @version v2.0
 */
@Controller
class AdminIndexController @Autowired() (userService: IUserService, borrowBookService: IBorrowBookService)
    extends BaseController {

    /**
     * 以下没有显示声明返回类型，所以默认返回最后的元素数据类型
     */
    //登陆
    @RequestMapping(value = { Array("/admin/", "/admin/index") })
    def index(model: Model) = {
        "admin/index"
    }

    //首页
    @RequestMapping(value = { Array("/admin/welcome") })
    def welcome() = {
        "admin/welcome"
    }

    //用户个人信息
    @RequestMapping(value = { Array("/admin/info") })
    def info(map: ModelMap, response: HttpServletResponse, @RequestParam(value    = "id", required = false) id: Int) = {

        val subject = SecurityUtils.getSubject();
        print("个人信息页面，rememberMe:" + subject.isRemembered())
        println("用户id:" + id)
        val u = SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + id)
        if (u != null) {
            map.put("user", u)
        } else {
            try {
                throw new TimeoutException("因超时无法获取您的个人信息,即将退出登录")
            } catch {
                case e: Exception => map.put("message", e.getMessage())
            } finally {
                redirect(response, "/admin/login")
            }
        }
        "admin/info"
    }

    //书籍借阅页面
    @RequestMapping(value = { Array("/admin/borrow") })
    def borrow() = {
        "admin/borrow"
    }

    //注册，不能拦截
    @RequestMapping(value = { Array("/assets/regist") })
    def regist(map: ModelMap) = {
        "admin/regist/form"
    }

    //登录页面的注册专用
    @ResponseBody
    @PostMapping(value = { Array("/assets/edit") })
    def registAdd(user: User, map: ModelMap): JsonResult = {
        try {

            println("regist:" + user.toString())
            userService.saveOrUpdate(user)
        } catch {
            case e: Exception => return JsonResult.failure(e.getMessage())
        }
        JsonResult.success("注册成功,3秒后自动回到登录页面")
    }

    //验证用户名【学号】是否已经被注册,委托给用户控制层
    @GetMapping(value = { Array("/assets/isAvailable") })
    def isAvailableUse(userCode: String, request: HttpServletRequest, response: HttpServletResponse) = {
        println("前台验证账户可用")
        request.getRequestDispatcher("/admin/user/isAvailable/" + userCode).forward(request, response)
    }

    //所有人均可修改的个人信息
    @ResponseBody
    @PostMapping(value = { Array("/assets/update") })
    def allCanUpdate(user: User, map: ModelMap): JsonResult = {
        println("user:/assets/update:" + user.toString())
        try {
            val u = SecurityUtils.getSubject().getSession().getAttribute(Constats.CURRENTUSER + user.getId())
            if (u == null) {
                // 已经过期
                try {
                    throw new TimeoutException("因超时无法获取您的个人信息,即将退出登录")
                } catch {
                    case e: TimeoutException => map.put("message", e.getMessage())
                } finally {
                    // 重定向到登录页面
                    Thread.sleep(1000)
                    redirect(response, "/admin/logout")
                }
            }
            userService.saveOrUpdate(user)
            // 更新session
            SecurityUtils.getSubject().getSession().setAttribute(Constats.CURRENTUSER + user.getId(), user)
        } catch {
            case e: Exception => return JsonResult.failure(e.getMessage())
        }

        JsonResult.success("修改成功，下次登陆生效")
    }

    //查询用户已借阅的图书 每个人均可操作，不需要授权。超期不可自主归还
    @ResponseBody
    @RequestMapping(value = { Array("/assets/borrowList") })
    def borrowList(map: ModelMap, @RequestParam(value = "uCode") uCode: String): Page[BorrowBook] = {
        val u = userService.findByUserCode(uCode)
        val builder = new SimpleSpecificationBuilder[BorrowBook]()
        val bookName = request.getParameter("inputBookName")
        val bookAuthor = request.getParameter("inputAuthor")
        val bookPress = request.getParameter("inputPublication")
        if (StringUtils.isNotBlank(bookName)) {
            builder.add("bookName", Operator.likeAll.name(), bookName)
        }
        if (StringUtils.isNotBlank(bookAuthor)) {
            builder.add("bookAuthor", Operator.likeAll.name(), bookAuthor)

        }
        if (StringUtils.isNotBlank(bookPress)) {
            builder.add("bookPress", Operator.likeAll.name(), bookPress)
        }
        val user: User = SecurityUtils.getSubject().getSession()
            .getAttribute(Constats.CURRENTUSER + u.getId()).asInstanceOf[User]
        if (user.getId() != null) {
            builder.add("userId", Operator.eq.name(), user.getId())
        } else {
            try {
                throw new TimeoutException("因超时无法获取您的个人信息,即将退出登录")
            } catch {
                case e: TimeoutException => map.put("message", e.getMessage())
            } finally {
                redirect(response, "/admin/logout")
            }
        }
        borrowBookService.findAll(builder.generateSpecification(), getPageRequest())

    }
}