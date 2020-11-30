/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.controller.admin.system;

import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.entity.Role;
import cn.edu.jxnu.base.service.IRoleService;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.service.component.MemorandumComponent;
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
import cn.edu.jxnu.base.utils.JsonResult;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 系统角色控制类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

  @Autowired private IRoleService roleService;

  @Autowired private MemorandumComponent memorandumComponent;

  @Autowired private IUserService userService;

  /**
   * 打开角色管理首页页面
   *
   * @return String
   */
  @RequestMapping(value = {"/", "/index"})
  public String index() {
    return "admin/role/index";
  }

  /**
   * 角色管理分页
   *
   * @param request request
   * @return Mono Page
   */
  @RequestMapping(value = {"/list"})
  @ResponseBody
  public Mono<Page<Role>> list(HttpServletRequest request) {
    SimpleSpecificationBuilder<Role> builder = new SimpleSpecificationBuilder<>();
    String searchText = request.getParameter("searchText");
    if (StringUtils.isNotBlank(searchText)) {
      builder.add("name", Operator.likeAll.name(), searchText);
      builder.addOr("description", Operator.likeAll.name(), searchText);
    }
    return roleService.findAll(builder.generateSpecification(), getPageRequest(request)).log();
  }

  /**
   * 打开添加角色页面
   *
   * @param map map
   * @return String
   */
  @RequestMapping(value = "/add")
  public String add(ModelMap map) {
    Role role = new Role();
    // 传入role实体，提交的时候才有默认值
    map.put("role", role);
    return "admin/role/form";
  }

  /**
   * 打开角色修改页面
   *
   * @param id 角色ID
   * @param map map
   * @return String
   */
  @RequestMapping(value = "/edit/{id}")
  public String edit(@PathVariable Integer id, ModelMap map) {
    Mono<Role> role = roleService.find(id);
    role.log().subscribe(r -> map.put("role", r));
    return "admin/role/form";
  }

  /**
   * 添加或修改角色
   *
   * @param role role
   * @param uCode 用户码
   * @return Mono JsonResult
   */
  @RequestMapping(
      value = {"/edit"},
      method = RequestMethod.POST)
  @ResponseBody
  public Mono<JsonResult> edit(Role role, @RequestParam("uCode") String uCode) {
    try {
      return roleService
          .saveOrUpdate(role)
          .flatMap(
              r ->
                  userService
                      .findByUserCode(uCode)
                      .map(
                          u ->
                              memorandumComponent.saveMemorandum(
                                  uCode,
                                  u.getUserName(),
                                  "修改/新增角色",
                                  role.getRoleKey() + " | " + role.getName())))
          .log()
          .map(r -> JsonResult.success());
    } catch (Exception e) {
      e.printStackTrace();
      return Mono.error(e);
    }
  }

  /**
   * 删除角色
   *
   * @param id 用户ID
   * @param uCode 用户码
   * @return Mono JsonResult
   */
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
  @ResponseBody
  public Mono<JsonResult> delete(@PathVariable Integer id, @RequestParam("uCode") String uCode) {
    return userService
        .findByUserCode(uCode)
        .flatMap(
            u ->
                roleService
                    .find(id)
                    .flatMap(
                        r -> {
                          memorandumComponent
                              .saveMemorandum(
                                  uCode,
                                  u.getUserName(),
                                  "删除角色",
                                  r.getRoleKey() + " | " + r.getName())
                              .log()
                              .subscribe();
                          return roleService.delete(id);
                        }))
        .log()
        .map(r -> r ? JsonResult.success() : JsonResult.failure("删除失败"))
        .onErrorResume(error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
  }

  /**
   * 打开授权页面
   *
   * @param id 角色ID
   * @param map map
   * @return String
   */
  @RequestMapping(value = "/grant/{id}")
  public String grant(@PathVariable Integer id, ModelMap map) {
    Mono<Role> role = roleService.find(id);
    role.log().subscribe(r -> map.put("role", r));
    return "admin/role/grant";
  }

  /**
   * 授权
   *
   * @param id 角色ID
   * @param resourceIds 资源ID
   * @return Mono JsonResult
   */
  @RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
  @ResponseBody
  public Mono<JsonResult> grant(
      @PathVariable Integer id, @RequestParam(required = false) String[] resourceIds) {
    return roleService
        .grant(id, resourceIds)
        .map(r -> JsonResult.success())
        .log()
        .onErrorResume(error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
  }
}
