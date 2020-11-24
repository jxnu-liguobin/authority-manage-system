/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.controller.admin.system;

import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.entity.Resource;
import cn.edu.jxnu.base.entity.ZtreeView;
import cn.edu.jxnu.base.service.IResourceService;
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
import cn.edu.jxnu.base.utils.JsonResult;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 系统资源控制类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Controller
@RequestMapping("/admin/resource")
public class ResourceController extends BaseController {

    @Autowired private IResourceService resourceService;

    /** 资源树 */
    @RequestMapping("/tree/{resourceId}")
    @ResponseBody
    public Flux<ZtreeView> tree(@PathVariable Integer resourceId) {
        return resourceService.tree(resourceId);
    }

    /** 打开资源管理首页 */
    @RequestMapping("/index")
    public String index() {
        return "admin/resource/index";
    }

    /** 资源管理分页 */
    @RequestMapping(
            value = "/list",
            method = {RequestMethod.POST})
    @ResponseBody
    public Mono<Page<Resource>> list(HttpServletRequest request) {
        SimpleSpecificationBuilder<Resource> builder = new SimpleSpecificationBuilder<>();
        String searchText = request.getParameter("searchText");
        if (StringUtils.isNotBlank(searchText)) {
            builder.add("name", Operator.likeAll.name(), searchText);
        }
        return resourceService.findAll(builder.generateSpecification(), getPageRequest(request));
    }

    /** 打开资源添加页面 */
    @RequestMapping(value = "/add")
    public String add(ModelMap map) {
        Flux<Resource> list = resourceService.findAll();
        list.subscribe(l -> map.put("list", l));
        return "admin/resource/form";
    }

    /** 打开资源修改页面 */
    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable Integer id, ModelMap map) {
        Mono<Resource> resource = resourceService.find(id);
        resource.subscribe(r -> map.put("resource", r));
        Flux<Resource> list = resourceService.findAll();
        list.subscribe(l -> map.put("list", l));
        return "admin/resource/form";
    }

    /** 资源添加或修改 */
    @RequestMapping(
            value = {"/edit"},
            method = {RequestMethod.POST})
    @ResponseBody
    public Mono<JsonResult> edit(Resource resource) {
        resourceService.saveOrUpdate(resource).subscribe();
        // 可能存在需要在增加资源的时候默认给管理员增加权限
        return Mono.just(JsonResult.success());
    }

    /** 资源删除 */
    @RequestMapping(
            value = "/delete/{id}",
            method = {RequestMethod.POST})
    @ResponseBody
    public Mono<JsonResult> delete(@PathVariable Integer id) {
        resourceService.delete(id).subscribe();
        return Mono.just(JsonResult.success());
    }
}
