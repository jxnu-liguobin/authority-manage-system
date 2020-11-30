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

  /**
   * 资源树
   *
   * @param resourceId 资源ID
   * @return Flux ZtreeView
   */
  @RequestMapping("/tree/{resourceId}")
  @ResponseBody
  public Flux<ZtreeView> tree(@PathVariable Integer resourceId) {
    return resourceService.tree(resourceId);
  }

  /**
   * 打开资源管理首页
   *
   * @return String
   */
  @RequestMapping("/index")
  public String index() {
    return "admin/resource/index";
  }

  /**
   * 资源管理分页
   *
   * @param request request
   * @return Mono Page
   */
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
    return resourceService.findAll(builder.generateSpecification(), getPageRequest(request)).log();
  }

  /**
   * 打开资源添加页面
   *
   * @param map map
   * @return String
   */
  @RequestMapping(value = "/add")
  public String add(ModelMap map) {
    Flux<Resource> list = resourceService.findAll();
    list.log().subscribe(l -> map.put("list", l));
    return "admin/resource/form";
  }

  /**
   * 打开资源修改页面
   *
   * @param id 资源ID
   * @param map map
   * @return String
   */
  @RequestMapping(value = "/edit/{id}")
  public String edit(@PathVariable Integer id, ModelMap map) {
    resourceService
        .find(id)
        .map(
            r -> {
              map.put("resource", r);
              return resourceService.findAll().collectList().map(l -> map.put("list", l));
            })
        .log()
        .subscribe();
    return "admin/resource/form";
  }

  /**
   * 资源添加或修改
   *
   * @param resource 资源
   * @return Mono JsonResult
   */
  @RequestMapping(
      value = {"/edit"},
      method = {RequestMethod.POST})
  @ResponseBody
  public Mono<JsonResult> edit(Resource resource) {
    // 可能存在需要在增加资源的时候默认给管理员增加权限
    return resourceService
        .saveOrUpdate(resource)
        .map(r -> JsonResult.success())
        .log()
        .onErrorResume(error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
  }

  /**
   * 资源删除
   *
   * @param id 资源ID
   * @return Mono JsonResult
   */
  @RequestMapping(
      value = "/delete/{id}",
      method = {RequestMethod.POST})
  @ResponseBody
  public Mono<JsonResult> delete(@PathVariable Integer id) {
    return resourceService
        .delete(id)
        .map(r -> r ? JsonResult.success() : JsonResult.failure("删除失败"))
        .log()
        .onErrorResume(error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
  }
}
