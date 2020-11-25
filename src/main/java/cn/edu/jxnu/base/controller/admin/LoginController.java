/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.controller.admin;

import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.service.component.MemorandumComponent;
import cn.edu.jxnu.base.shiro.RetryLimitHashedCredentialsMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录控制类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Controller
@Slf4j
public class LoginController extends BaseController {

    @Autowired private IUserService userService;

    @Autowired private MemorandumComponent memorandumComponent;

    @Autowired private RetryLimitHashedCredentialsMatcher credentialsMatcher;

    /**
     * 打开登陆表单
     *
     * @return String
     */
    @RequestMapping(value = {"/admin/login"})
    public String login() {
        return "admin/login";
    }

    /**
     * 登陆验证 垃圾代码，请勿模仿
     *
     * @param usercode usercode
     * @param password password
     * @param model model
     * @param rememberMe rememberMe
     * @return String
     */
    @RequestMapping(
            value = {"/admin/login"},
            method = RequestMethod.POST)
    public String login(
            @RequestParam(value = "usercode", required = false) String usercode,
            @RequestParam("password") String password,
            ModelMap model,
            boolean rememberMe) {
        Subject subject = null;
        UsernamePasswordToken token = null;
        log.info("传进来的rememberMe: " + rememberMe);
        try {
            subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) { // 没有进行认证
                token = new UsernamePasswordToken(usercode, password);
                token.setRememberMe(rememberMe);
                subject.login(token); // 调用主体的login方法进行认证登录
                log.info("password: " + password);
                // 记录
                userService
                        .findByUserCode(usercode)
                        .subscribe(
                                u ->
                                        memorandumComponent.saveMemorandum(
                                                usercode, u.getUserName(), "登陆"));
            }
            return redirect("/admin/index");
        } catch (UnknownAccountException e) {
            if (e.getMessage().equals("账号已被锁定")) {
                // 锁定状态，但是没有缓存，则继续回调登陆
                UsernamePasswordToken finalToken = token;
                Subject finalSubject = subject;
                assert finalSubject != null;
                userService
                        .findByUserCode(usercode)
                        .subscribe(
                                user -> {
                                    if (user.getLocked() == 1) {
                                        if (credentialsMatcher.getPasswordRetryCache().get(usercode)
                                                == null) {
                                            user.setLocked(0);
                                            userService.saveOrUpdate(user).subscribe();
                                            finalSubject.login(finalToken); // 调用主体的login方法进行认证登录
                                            redirect("/admin/index");
                                        } else {
                                            model.put("message", e.getMessage()); // 认证登录失败就进入登录页面
                                        }
                                    }
                                });
            } else {
                model.put("message", e.getMessage()); // 认证登录失败就进入登录页面
            }

        } catch (ExcessiveAttemptsException e) {
            userService
                    .findByUserCode(usercode)
                    .subscribe(
                            user -> {
                                if (user.getLocked() == 0) {
                                    user.setLocked(1);
                                    userService.saveOrUpdate(user).subscribe();
                                    log.info("锁定状态");
                                }
                            });
            model.put("message", e.getMessage()); // 认证登录失败就进入登录页面
        } catch (AuthenticationException e) {
            model.put("message", e.getMessage()); // 认证登录失败就进入登录页面
        }
        return "admin/login";
    }

    /**
     * 安全退出
     *
     * @param uCode 用户码
     * @return String
     */
    @RequestMapping(value = {"/admin/logout/{uCode}"})
    public String logout(@PathVariable("uCode") String uCode) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout(); // 调用主体的logout方法进行登出
        userService
                .findByUserCode(uCode)
                .subscribe(u -> memorandumComponent.saveMemorandum(uCode, u.getUserName(), "登出"));
        return redirect("admin/login");
    }
}
