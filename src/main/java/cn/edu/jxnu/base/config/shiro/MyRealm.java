package cn.edu.jxnu.base.config.shiro;

import cn.edu.jxnu.base.entity.Resource;
import cn.edu.jxnu.base.entity.Role;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义的Shiro Realm
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Component
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    /**
     * 构造方法，传入一个缓冲管理器，用于给初始化密码次数限制功能使用
     */
    public MyRealm() {
        setAuthenticationTokenClass(UsernamePasswordToken.class);
        log.info("生成passwordRetryCache");
        // FIXME: 暂时禁用Cache
        setCachingEnabled(false);
    }

    /**
     * 根据用户名来添加相应的权限和角色
     *
     * @param principals
     * @return 授权用户信息 AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User dbUser = userService.findByUserName(user.getUserName());
        Set<String> shiroPermissions = new HashSet<>();// 存放用户权限
        Set<String> roleSet = new HashSet<String>();// 存放用户角色名
        Set<Role> roles = dbUser.getRoles();// 从用户信息中获取用户拥有的角色
        for (Role role : roles) {
            // 对于用户的每个角色，获取角色拥有的资源
            Set<Resource> resources = role.getResources();
            for (Resource resource : resources) {
                // 添加角色拥有的资源的全部id到集合中
                shiroPermissions.add(resource.getSourceKey());

            }
            // 保存用户的角色的id
            roleSet.add(role.getRoleKey());
        }
        authorizationInfo.setRoles(roleSet);
        authorizationInfo.setStringPermissions(shiroPermissions);
        return authorizationInfo;
    }

    /**
     * 密码验证,验证账号不存在或已被注销
     *
     * @param token
     * @return 验证用户信息 AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleAuthenticationInfo info = null;
        String userCode = (String) token.getPrincipal();
        User user = userService.findByUserCode(userCode);
        // 账号不存在
        if (user == null) {
            log.info("用户: " + userCode + " 不存在");
            throw new UnknownAccountException("账号或密码不正确");
        }
        if (user.getLocked() == 1) {
            log.info("账号已被锁定");
            throw new UnknownAccountException("账号已被锁定");
        }
        // 用户名存在，但被注销。
        if (user.getDeleteStatus() == 1) {
            log.info("用户: " + user.toString() + " 账号已注销");
            throw new UnknownAccountException("账号已注销");
        }
        info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        return info;
    }

    /**
     * 添加自动刷新授权缓存
     */
    public void clearCachedAuthorization(Integer id) {
        User user = userService.find(id);
        // 清空指定用户的权限缓存
        SimpleAuthenticationInfo info;
        info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());

        clearCachedAuthorizationInfo(info.getPrincipals());

        /** 以上需要刷新生效. */
    }
}