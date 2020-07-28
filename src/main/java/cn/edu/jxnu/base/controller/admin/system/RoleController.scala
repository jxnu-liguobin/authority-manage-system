package cn.edu.jxnu.base.controller.admin.system

import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder
import org.springframework.web.bind.annotation.RequestMapping
import cn.edu.jxnu.base.service.IRoleService
import cn.edu.jxnu.base.common.MemorandumUtils
import cn.edu.jxnu.base.service.IUserService
import cn.edu.jxnu.base.controller.BaseController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.data.domain.Page
import cn.edu.jxnu.base.entity.Role
import org.apache.commons.lang3.StringUtils
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import cn.edu.jxnu.base.common.JsonResult
import org.springframework.stereotype.Controller

/**
 * 系统角色控制类
 *
 * @author 梦境迷离
 * @time 2018年5月16日
 * @version V2.0
 */
@Controller
@RequestMapping(value = { Array("/admin/role") })
class RoleController @Autowired() (roleService: IRoleService, memorandumUtils: MemorandumUtils, userService: IUserService)
    extends BaseController {

    @RequestMapping(value = { Array("/", "/index") })
    def index() = {
        "admin/role/index"
    }

    @RequestMapping(value = { Array("/list") })
    @ResponseBody
    def list(): Page[Role] = {
        val builder = new SimpleSpecificationBuilder[Role]()
        val searchText = request.getParameter("searchText")
        if (StringUtils.isNotBlank(searchText)) {
            builder.add("name", Operator.likeAll.name(), searchText)
            builder.addOr("description", Operator.likeAll.name(), searchText)
        }
        roleService.findAll(builder.generateSpecification(), getPageRequest())
    }

    @GetMapping(value = Array("/add"))
    def add(map: ModelMap) = {
        val role = new Role()
        map.put("role", role)
        "admin/role/form"
    }

    @GetMapping(value = Array("/edit/{id}"))
    def edit(@PathVariable id: Int, map: ModelMap) = {
        println("role id:" + id)
        val role = roleService.find(id)
        map.put("role", role)
        "admin/role/form"
    }

    @PostMapping(value = { Array("/edit") })
    @ResponseBody
    def edit(role: Role, map: ModelMap, @RequestParam("uCode") uCode: String): JsonResult = {
        try {
            roleService.saveOrUpdate(role)
            memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
                "修改/新增角色", role.getRoleKey() + " | " + role.getName())
        } catch {
            case e: Exception =>
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }

    @PostMapping(value = { Array("/delete/{id}") })
    @ResponseBody
    def delete(@PathVariable id: Int, map: ModelMap, @RequestParam("uCode") uCode: String): JsonResult = {
        try {
            memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
                "删除角色", roleService.find(id).getRoleKey() + " | " + roleService.find(id).getName())
            roleService.delete(id)
        } catch {
            case e: Exception =>
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }

    @GetMapping(value = { Array("/grant/{id}") })
    def grant(@PathVariable id: Int, map: ModelMap) = {
        val role = roleService.find(id)
        map.put("role", role)
        "admin/role/grant"
    }

    @PostMapping(value = { Array("/grant/{id}") })
    @ResponseBody
    def grant(@PathVariable id: Int, @RequestParam(required = false) resourceIds: Array[String],
        map: ModelMap): JsonResult = {
        try {
            roleService.grant(id, resourceIds)
        } catch {
            case e: Exception =>
                e.printStackTrace()
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }
}