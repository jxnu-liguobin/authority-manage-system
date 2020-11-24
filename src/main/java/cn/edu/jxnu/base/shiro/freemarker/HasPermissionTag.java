/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.shiro.freemarker;

/**
 * Equivalent to {@link org.apache.shiro.web.tags.HasPermissionTag}
 *
 * @since 0.1
 */
public class HasPermissionTag extends PermissionTag {
    protected boolean showTagBody(String p) {
        return isPermitted(p);
    }
}
