package cn.edu.jxnu.base.controller.admin;

import cn.edu.jxnu.base.config.shiro.RetryLimitHashedCredentialsMatcher;
import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.utils.MemorandumUtils;
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
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Controller
@Slf4j
public class LoginController extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private MemorandumUtils memorandumUtils;

    @Autowired
    private RetryLimitHashedCredentialsMatcher credentialsMatcher;

    /**
     * 打开登陆表单
     */
    @RequestMapping(value = {"/admin/login"})
    public String login() {
        return "admin/login";
    }

    /**
     * 登陆验证
     */
    @RequestMapping(value = {"/admin/login"},method = RequestMethod.POST)
    public String login(@RequestParam(value = "usercode", required = false) String usercode,
                        @RequestParam("password") String password, ModelMap model, boolean rememberMe) {
        Subject subject = null;
        UsernamePasswordToken token = null;
        log.info("传进来的rememberMe: " + rememberMe);
        try {
            subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {// 没有进行认证
                token = new UsernamePasswordToken(usercode, password);
                token.setRememberMe(rememberMe);
                subject.login(token);// 调用主体的login方法进行认证登录
                log.info("password: " + password);
                // 记录
                memorandumUtils.saveMemorandum(memorandumUtils, usercode,
                        userService.findByUserCode(usercode).getUserName(), "登陆");
            }
            return redirect("/admin/index");
        } catch (UnknownAccountException e) {
            if (e.getMessage().equals("账号已被锁定")) {
                // 锁定状态，但是没有缓存，则继续回调登陆
                User user = userService.findByUserCode(usercode);
                if (user.getLocked() == 1) {
                    if (credentialsMatcher.getPasswordRetryCache().get(usercode) == null) {
                        user.setLocked(0);
                        userService.saveOrUpdate(user);
                        subject.login(token);// 调用主体的login方法进行认证登录
                        return redirect("/admin/index");
                    } else {
                        model.put("message", e.getMessage());// 认证登录失败就进入登录页面
                    }
                }
            } else {
                model.put("message", e.getMessage());// 认证登录失败就进入登录页面
            }

        } catch (ExcessiveAttemptsException e) {
            User user = userService.findByUserCode(usercode);
            if (user.getLocked() == 0) {
                user.setLocked(1);
                userService.saveOrUpdate(user);
                log.info("锁定状态");
            }
            model.put("message", e.getMessage());// 认证登录失败就进入登录页面
        } catch (AuthenticationException e) {
            model.put("message", e.getMessage());// 认证登录失败就进入登录页面
        }
        return "admin/login";
    }

    /**
     * 安全退出
     */
    @RequestMapping(value = {"/admin/logout/{uCode}"})
    public String logout(@PathVariable("uCode") String uCode) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();// 调用主体的logout方法进行登出
        memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(), "登出");

        return redirect("admin/login");
    }

}
