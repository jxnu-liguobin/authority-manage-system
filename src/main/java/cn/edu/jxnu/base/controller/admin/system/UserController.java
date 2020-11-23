package cn.edu.jxnu.base.controller.admin.system;

import cn.edu.jxnu.base.config.shiro.RetryLimitHashedCredentialsMatcher;
import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.entity.Role;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.service.IRoleService;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
import cn.edu.jxnu.base.utils.JsonResult;
import cn.edu.jxnu.base.utils.MemorandumUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * 使用cache的密码缓存
     */
    @Autowired
    private RetryLimitHashedCredentialsMatcher credentialsMatcher;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private MemorandumUtils memorandumUtils;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "admin/user/index";
    }

    /**
     * 用户管理页面分页
     */
    @RequestMapping(value = {"/list"})
    @ResponseBody
    public Mono<Page<User>> list(HttpServletRequest request) {
        SimpleSpecificationBuilder<User> builder = new SimpleSpecificationBuilder<User>();
        String searchText = request.getParameter("searchText");
        if (StringUtils.isNotBlank(searchText)) {
            builder.add("userName", Operator.likeAll.name(), searchText);
        }

        Mono<Page<User>> page = userService.findAll(builder.generateSpecification(), getPageRequest(request));
        return page;
    }

    /**
     * 打开用户添加页面
     */
    @RequestMapping(value = "/add")
    public String add(ModelMap map) {
        return "admin/user/form";
    }

    /**
     * 打开用户修改页面
     */
    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Integer id, ModelMap map) {
        Mono<User> user = userService.find(id);
        user.subscribe(u -> {
            map.put("user", u);
            map.put("edit", "noCheck");
        });
        return "admin/user/form";
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public Mono<JsonResult> edit(User user, ModelMap map, @RequestParam("uCode") String uCode) {
        try {
            log.info("inputuser:" + user.toString());
            userService.saveOrUpdate(user).subscribe();
            userService.findByUserCode(uCode).subscribe(u -> memorandumUtils.saveMemorandum(memorandumUtils, uCode, u.getUserName(),
                    "修改/新增用户", user.getUserCode() + " | " + user.getUserName()));
        } catch (Exception e) {
            return Mono.just(JsonResult.failure(e.getMessage()));
        }
        return Mono.just(JsonResult.success());
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Mono<JsonResult> delete(@PathVariable Integer id, ModelMap map, @RequestParam("uCode") String uCode) {
        Mono<Mono<Mono<String>>> monoRes;
        try {
            Mono<User> beUMono = userService.find(id);
            userService.delete(id);
            Mono<User> userMono = userService.find(id);
            monoRes = beUMono.map(beU -> userMono.map(user -> userService.findByUserCode(uCode).map(u -> {
                String res = "";
                if (user != null && user.getDeleteStatus() == 1) {
                    res = "已注销";
                    memorandumUtils.saveMemorandum(memorandumUtils, uCode, u.getUserName(),
                            "注销用户", beU.getUserCode() + " | " + beU.getUserName());
                }
                if (user == null) {
                    res = "已删除";
                    memorandumUtils.saveMemorandum(memorandumUtils, uCode, u.getUserName(),
                            "删除用户", beU.getUserCode() + " | " + beU.getUserName());
                }
                return res;
            })));
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(JsonResult.failure(e.getMessage()));
        }

        return Objects.requireNonNull(monoRes.map(Mono::block).block()).map(JsonResult::success);
    }

    /**
     * 打开分配角色页面
     */
    @RequestMapping(value = "/grant/{id}")
    public String grant(@PathVariable Integer id, ModelMap map) {
        Mono<User> userMono = userService.find(id);
        userMono.subscribe(user -> {
            map.put("user", user);
            Set<Role> set = user.getRoles();
            List<Integer> roleIds = new ArrayList<>();
            for (Role role : set) {
                roleIds.add(role.getId());
            }
            map.put("roleIds", roleIds);
            Flux<Role> roles = roleService.findAll();
            roles.subscribe(r -> map.put("roles", r));
        });
        return "admin/user/grant";
    }

    /**
     * 分配角色
     */
    @ResponseBody
    @RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
    public Mono<JsonResult> grantRole(@PathVariable Integer id, String[] roleIds, ModelMap map) {
        try {
            userService.grant(id, roleIds).subscribe();
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(JsonResult.failure(e.getMessage()));
        }
        return Mono.just(JsonResult.success());
    }

    /**
     * 恢复账号
     */
    @ResponseBody
    @RequestMapping(value = "/resume/{id}", method = RequestMethod.POST)
    public Mono<JsonResult> resume(@PathVariable Integer id) {
        Cache<String, AtomicInteger> passwordRetryCache = credentialsMatcher.getPasswordRetryCache();
        try {
            Mono<User> userMono = userService.find(id);
            return userMono.map(user -> {
                String userCode = user.getUserCode();
                // 恢复账号或者恢复锁定
                if (user.getDeleteStatus() == 1 || user.getLocked() == 1) {
                    user.setDeleteStatus(0);
                    user.setLocked(0);
                    userService.saveOrUpdate(user).subscribe();
                    // 存在缓存，去除，因为可能用户是自动解锁的。
                    if (passwordRetryCache.get(userCode) != null) {
                        passwordRetryCache.remove(userCode);
                    }

                } else {
                    // 不需要恢复
                    return JsonResult.failure("当前账号不可执行此操作");
                }
                return JsonResult.success();
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(JsonResult.failure(e.getMessage()));
        }
    }

    /**
     * 验证用户名【学号】是否已经被注册
     */
    @ResponseBody
    @RequestMapping(value = "/isExist")
    public Mono<Boolean> isExist(String userCode) {
        log.info("UserCode: " + userCode);
        if (userCode != null) {
            Mono<User> userMono = userService.findByUserCode(userCode);
            // 空的Mono不会执行map
            if (userMono.hasElement().block()) {
                return Mono.just(true);
            } else {
                return Mono.just(false);
            }
        } else {
            return Mono.just(true);
        }
    }

    /**
     * 修改时，验证永真
     */
    @ResponseBody
    @RequestMapping(value = "/isAllTrue")
    public Mono<Boolean> isAllTrue(String userCode) {
        return Mono.just(true);
    }

    /**
     * 验证用户名【学号】是否已经被注册，代理前端的账户验证
     */
    @ResponseBody
    @RequestMapping(value = "/isAvailable/{userCode}")
    public Mono<Boolean> isAvailable(@PathVariable("userCode") String userCode) {
        log.info("UserCode: " + userCode);
        if (userCode != null) {
            Mono<User> userMono = userService.findByUserCode(userCode);
            if (userMono.hasElement().block()) {
                return Mono.just(false);
            } else {
                return Mono.just(true);
            }
        } else {
            return Mono.just(true);
        }
    }
}
