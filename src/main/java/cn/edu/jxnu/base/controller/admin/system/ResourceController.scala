package cn.edu.jxnu.base.controller.admin.system

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import cn.edu.jxnu.base.service.IResourceService
import cn.edu.jxnu.base.controller.BaseController
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.PathVariable
import cn.edu.jxnu.base.entity.ZtreeView
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder
import org.apache.commons.lang3.StringUtils
import cn.edu.jxnu.base.entity.Resource
import org.springframework.data.domain.Page
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import cn.edu.jxnu.base.common.JsonResult
import scala.collection.immutable.{ List => _, _ }
import java.util.{ List => JavaList }

/**
 *
 * 系统资源控制类
 *
 * @author 梦境迷离.
 * @time 2018年5月17日
 * @version v2.0
 */
@Controller
@RequestMapping(value = { Array("/admin/resource") })
class ResourceController @Autowired() (resourceService: IResourceService) extends BaseController {

    @RequestMapping(value = { Array("/tree/{resourceId}") })
    @ResponseBody
    def tree(@PathVariable resourceId: Int): JavaList[ZtreeView] = {
        resourceService.tree(resourceId)

    }

    @RequestMapping(value = { Array("/index") })
    def index() = {
        "admin/resource/index"
    }

    @RequestMapping(value = { Array("/list") })
    @ResponseBody
    def list(): Page[Resource] = {
        val builder = new SimpleSpecificationBuilder[Resource]()
        val searchText = request.getParameter("searchText")
        if (StringUtils.isNotBlank(searchText)) {
            builder.add("name", Operator.likeAll.name(), searchText)
        }
        resourceService.findAll(builder.generateSpecification(), getPageRequest())
    }

    @GetMapping(value = { Array("/add") })
    def add(map: ModelMap) = {
        val list = resourceService.findAll()
        map.put("list", list)
        "admin/resource/form"
    }

    @GetMapping(value = { Array("/edit/{id}") })
    def edit(@PathVariable id: Int, map: ModelMap) = {
        val resource = resourceService.find(id)
        map.put("resource", resource)
        val list = resourceService.findAll()
        map.put("list", list)
        "admin/resource/form"
    }

    @ResponseBody
    @PostMapping(value = { Array("/edit") })
    def edit(resource: Resource, map: ModelMap): JsonResult = {
        try {
            resourceService.saveOrUpdate(resource)
            // 可能存在需要在增加资源的时候默认给管理员增加权限
        } catch {
            case e: Exception =>
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }

    @PostMapping(value = { Array("/delete/{id}") })
    @ResponseBody
    def delete(@PathVariable id: Int, map: ModelMap): JsonResult = {
        try {
            resourceService.delete(id)
        } catch {
            case e: Exception =>
                e.printStackTrace()
                return JsonResult.failure(e.getMessage())
        }
        JsonResult.success()
    }
}