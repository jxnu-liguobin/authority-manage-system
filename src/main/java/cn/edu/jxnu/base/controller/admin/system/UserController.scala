package cn.edu.jxnu.base.controller.admin.system

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import cn.edu.jxnu.base.controller.BaseController
import org.springframework.beans.factory.annotation.Autowired
import cn.edu.jxnu.base.config.shiro.RetryLimitHashedCredentialsMatcher
import cn.edu.jxnu.base.service.IUserService
import cn.edu.jxnu.base.service.IRoleService
import cn.edu.jxnu.base.common.MemorandumUtils
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.data.domain.Page
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder
import cn.edu.jxnu.base.entity.User
import cn.edu.jxnu.base.entity.Role
import org.apache.commons.lang3.StringUtils
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import cn.edu.jxnu.base.common.JsonResult
import org.springframework.web.bind.annotation.RequestParam
import java.util.ArrayList
//导入自动集合转换
import scala.collection.JavaConversions._

/**
 * 用户管理控制类
 *
 * @author 梦境迷离
 * @time 2018年5月17日
 * @version V2.0
 */
@Controller
@RequestMapping(value = { Array("/admin/user") })
class UserController @Autowired() (credentialsMatcher: RetryLimitHashedCredentialsMatcher, userService: IUserService,
    roleService: IRoleService, memorandumUtils: MemorandumUtils) extends BaseController {

    @RequestMapping(value = { Array("/", "/index") })
    def index() = {
        "admin/user/index"
    }

    @RequestMapping(value = { Array("/list") })
    @ResponseBody
    def list(): Page[User] = {
        val builder = new SimpleSpecificationBuilder[User]()
        val searchText = request.getParameter("searchText")
        if (StringUtils.isNotBlank(searchText)) {
            builder.add("userName", Operator.likeAll.name(), searchText)
        }

        userService.findAll(builder.generateSpecification(), getPageRequest())
    }

    @GetMapping(value = { Array("/add") })
    def add(map: ModelMap) = {
        "admin/user/form"
    }

    @GetMapping(value = { Array("/edit/{id}") })
    def edit(@PathVariable id: Int, map: ModelMap) = {
        val user = userService.find(id)
        map.put("user", user)
        map.put("edit", "noCheck")
        "admin/user/form"
    }

    @PostMapping(value = { Array("/edit") })
    @ResponseBody
    def edit(user: User, map: ModelMap, @RequestParam("uCode") uCode: String): JsonResult = {
        try {
            println("inputuser:" + user.toString())
            userService.saveOrUpdate(user)
            memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
                "修改/新增用户", user.getUserCode() + " | " + user.getUserName())

        } catch {
            case e: Exception =>
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }

    @PostMapping(value = { Array("/delete/{id}") })
    @ResponseBody
    def delete(@PathVariable id: Int, map: ModelMap, @RequestParam("uCode") uCode: String): JsonResult = {
        var res: String = ""
        try {
            val beU = userService.find(id)
            userService.delete(id)
            val user = userService.find(id)
            if (user != null && user.getDeleteStatus() == 1) {
                res = "已注销"
                memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
                    "注销用户", beU.getUserCode() + " | " + beU.getUserName())
            }
            if (user == null) {
                res = "已删除"
                memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
                    "删除用户", beU.getUserCode() + " | " + beU.getUserName())
            }
        } catch {
            case e: Exception =>
                e.printStackTrace()
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success(res)
    }

    @GetMapping(value = { Array("/grant/{id}") })
    def grant(@PathVariable id: Int, map: ModelMap) = {
        val user = userService.find(id)
        map.put("user", user)
        val set: java.util.Set[Role] = user.getRoles()
        val roleIds = new ArrayList[Int]()
        for (r <- set) roleIds.add(r.getId())
        map.put("roleIds", roleIds)
        val roles = roleService.findAll()
        map.put("roles", roles)
        "admin/user/grant"
    }

    @ResponseBody
    @PostMapping(value = { Array("/grant/{id}") })
    def grantRole(@PathVariable id: Int, roleIds: Array[String], map: ModelMap): JsonResult = {
        try {
            userService.grant(id, roleIds)
        } catch {
            case e: Exception =>
                e.printStackTrace()
                //return不能省略
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }

    @ResponseBody
    @PostMapping(value = { Array("/resume/{id}") })
    def resume(@PathVariable id: Int): JsonResult = {
        val passwordRetryCache = credentialsMatcher.getPasswordRetryCache()
        try {
            val user = userService.find(id)
            val userCode = user.getUserCode()
            // 恢复账号或者恢复锁定
            if (user.getDeleteStatus() == 1 || user.getLocked() == 1) {
                user.setDeleteStatus(0)
                user.setLocked(0)
                userService.saveOrUpdate(user)
                // 存在缓存，去除，因为可能用户是自动解锁的。
                if (passwordRetryCache.get(userCode) != null) {
                    passwordRetryCache.remove(userCode)
                }

            } else {
                // 不需要恢复
                return JsonResult.failure("当前账号不可执行此操作")
            }
        } catch {
            case e: Exception =>
                //e.printStackTrace()
                JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }

    @ResponseBody
    @GetMapping(value = { Array("/isExist") })
    def isExist(userCode: String): Boolean = {
        println("UserCode:" + userCode)
        var result = true
        if (userCode != null) {
            val user = userService.findByUserCode(userCode)
            if (user != null) {
                result = false
            }
        }
        result
    }

    @ResponseBody
    @GetMapping(value = { Array("/isAllTrue") })
    def isAllTrue(userCode: String) = true

    @ResponseBody
    @RequestMapping(value = { Array("/isAvailable/{userCode}") })
    def isAvailable(@PathVariable("userCode") userCode: String): Boolean = {
        println("前台验证账户可用代理:" + userCode)
        var result = true
        if (userCode != null) {
            val user = userService.findByUserCode(userCode)
            if (user != null) {
                result = false
            }
        }
        result
    }
}
