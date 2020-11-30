/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.controller.admin.system;

import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.entity.Role;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.service.IRoleService;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.service.component.MemorandumComponent;
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
import cn.edu.jxnu.base.shiro.RetryLimitHashedCredentialsMatcher;
import cn.edu.jxnu.base.utils.JsonResult;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 用户管理控制类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Controller
@Slf4j
@RequestMapping("/admin/user")
public class UserController extends BaseController {

  /** 使用cache的密码缓存 */
  @Autowired private RetryLimitHashedCredentialsMatcher credentialsMatcher;

  @Autowired private IUserService userService;

  @Autowired private IRoleService roleService;

  @Autowired private MemorandumComponent memorandumComponent;

  /** @return String */
  @RequestMapping(value = {"/", "/index"})
  public String index() {
    return "admin/user/index";
  }

  /**
   * 用户管理页面分页
   *
   * @param request request
   * @return Mono Page
   */
  @RequestMapping(value = {"/list"})
  @ResponseBody
  public Mono<Page<User>> list(HttpServletRequest request) {
    SimpleSpecificationBuilder<User> builder = new SimpleSpecificationBuilder<>();
    String searchText = request.getParameter("searchText");
    if (StringUtils.isNotBlank(searchText)) {
      builder.add("userName", Operator.likeAll.name(), searchText);
    }

    return userService.findAll(builder.generateSpecification(), getPageRequest(request));
  }

  /**
   * 打开用户添加页面
   *
   * @return String
   */
  @RequestMapping(value = "/add")
  public String add() {
    return "admin/user/form";
  }

  /**
   * 打开用户修改页面
   *
   * @param id 用户ID
   * @param map map
   * @return String
   */
  @GetMapping(value = "/edit/{id}")
  public String edit(@PathVariable Integer id, ModelMap map) {
    Mono<User> user = userService.find(id);
    user.log()
        .subscribe(
            u -> {
              map.put("user", u);
              map.put("edit", "noCheck");
            });
    return "admin/user/form";
  }

  /**
   * 修改用户
   *
   * @param user 用户
   * @param uCode 操作者的用户码
   * @return Mono JsonResult
   */
  @RequestMapping(
      value = {"/edit"},
      method = RequestMethod.POST)
  @ResponseBody
  public Mono<JsonResult> edit(User user, @RequestParam("uCode") String uCode) {
    return userService
        .saveOrUpdate(user)
        .flatMap(
            u1 ->
                userService
                    .findByUserCode(uCode)
                    .map(
                        u ->
                            memorandumComponent.saveMemorandum(
                                uCode,
                                u.getUserName(),
                                "修改/新增用户",
                                user.getUserCode() + " | " + user.getUserName())))
        .map(u -> JsonResult.success())
        .log()
        .onErrorResume(error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
  }

  /**
   * 删除用户 TODO 阻塞
   *
   * @param id 用户ID
   * @param uCode 操作者的用户码
   * @return Mono JsonResult
   */
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
  @ResponseBody
  public Mono<JsonResult> delete(@PathVariable Integer id, @RequestParam("uCode") String uCode) {
    Mono<User> beUMono = userService.find(id);
    final User beU = beUMono.block(Duration.ofSeconds(3));
    if (beU == null) {
      return Mono.just(JsonResult.failure("用户不存在！"));
    }
    if (beU.getUserCode().equals(uCode)) {
      return Mono.just(JsonResult.failure("不能删除自己！"));
    }
    return userService
        .delete(id)
        .onErrorReturn(false)
        .log()
        .flatMap(
            t -> {
              if (t) {
                Mono<User> aUMono = userService.find(id);
                Mono<User> adminUserMono = userService.findByUserCode(uCode);
                Mono<String> monoRes =
                    adminUserMono.map(
                        adminUser -> delStatus(beU, aUMono.blockOptional(), adminUser));
                return monoRes
                    .map(JsonResult::success)
                    .log()
                    .onErrorResume(
                        error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
              } else {
                return Mono.just(JsonResult.failure("删除失败"));
              }
            });
  }

  /**
   * @param beUser 删前查询的用户
   * @param afUser 删后查询的用户
   * @param adminUser 操作的管理人
   * @return String
   */
  private String delStatus(User beUser, Optional<User> afUser, User adminUser) {
    String res = "";
    if (afUser.isPresent() && afUser.get().getDeleteStatus() == 1) {
      res = "已注销";
      memorandumComponent
          .saveMemorandum(
              adminUser.getUserCode(),
              adminUser.getUserName(),
              "注销用户",
              beUser.getUserCode() + " | " + beUser.getUserName())
          .subscribe();
    }
    if (!afUser.isPresent()) {
      res = "已删除";
      memorandumComponent
          .saveMemorandum(
              adminUser.getUserCode(),
              adminUser.getUserName(),
              "删除用户",
              beUser.getUserCode() + " | " + beUser.getUserName())
          .subscribe();
    }
    return res;
  }

  /**
   * 打开分配角色页面
   *
   * @param id 用户ID
   * @param map map
   * @return String
   */
  @RequestMapping(value = "/grant/{id}")
  public String grant(@PathVariable Integer id, ModelMap map) {
    Mono<User> userMono = userService.find(id);
    userMono
        .flatMap(
            user -> {
              map.put("user", user);
              Set<Role> set = user.getRoles();
              List<Integer> roleIds = new ArrayList<>();
              for (Role role : set) {
                roleIds.add(role.getId());
              }
              map.put("roleIds", roleIds);
              return roleService.findAll().collectList().map(r -> map.put("roles", r));
            })
        .log()
        .subscribe();
    return "admin/user/grant";
  }

  /**
   * 分配角色
   *
   * @param id 用户ID
   * @param roleIds 角色ID
   * @return Mono JsonResult
   */
  @ResponseBody
  @RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
  public Mono<JsonResult> grantRole(@PathVariable Integer id, String[] roleIds) {
    return userService
        .grant(id, roleIds)
        .map(u -> JsonResult.success())
        .onErrorResume(error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
  }

  /**
   * 恢复账号
   *
   * @param id 用户ID
   * @return Mono JsonResult
   */
  @ResponseBody
  @RequestMapping(value = "/resume/{id}", method = RequestMethod.POST)
  public Mono<JsonResult> resume(@PathVariable Integer id) {
    Cache<String, AtomicInteger> passwordRetryCache = credentialsMatcher.getPasswordRetryCache();
    return userService
        .find(id)
        .flatMap(
            user -> {
              String userCode = user.getUserCode();
              // 恢复账号或者恢复锁定
              if (user.getDeleteStatus() == 1 || user.getLocked() == 1) {
                user.setDeleteStatus(0);
                user.setLocked(0);
                return userService
                    .saveOrUpdate(user)
                    .map(
                        u -> {
                          // 存在缓存，去除，因为可能用户是自动解锁的。
                          if (passwordRetryCache.get(userCode) != null) {
                            passwordRetryCache.remove(userCode);
                          }
                          return JsonResult.success();
                        });

              } else {
                // 不需要恢复
                return Mono.just(JsonResult.failure("当前账号不可执行此操作"));
              }
            })
        .log()
        .onErrorResume(error -> Mono.just(JsonResult.failure(error.getLocalizedMessage())));
  }

  /**
   * 验证用户名【学号】是否已经被注册
   *
   * @param userCode 用户码
   * @return Mono Boolean
   */
  @ResponseBody
  @RequestMapping(value = "/notExist")
  public Mono<Boolean> notExist(String userCode) {
    if (userCode != null) {
      Mono<User> userMono = userService.findByUserCode(userCode);
      // 空的Mono不会执行map
      return userMono.hasElement().map(e -> !e).log();
    } else {
      return Mono.just(false);
    }
  }

  /**
   * 修改时，验证永真
   *
   * @param userCode 用户码
   * @return Mono Boolean
   */
  @ResponseBody
  @RequestMapping(value = "/isAllTrue")
  public Mono<Boolean> isAllTrue(String userCode) {
    return Mono.just(true);
  }

  /**
   * 验证用户名【学号】是否已经被注册，代理前端的账户验证
   *
   * @param userCode 用户码
   * @return Mono Boolean
   */
  @ResponseBody
  @RequestMapping(value = "/isAvailable/{userCode}")
  public Mono<Boolean> isAvailable(@PathVariable("userCode") String userCode) {
    if (userCode != null) {
      return userService.findByUserCode(userCode).hasElement().map(e -> !e).log();
    } else {
      return Mono.just(true);
    }
  }
}
