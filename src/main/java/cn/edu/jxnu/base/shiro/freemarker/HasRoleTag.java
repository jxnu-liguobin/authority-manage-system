/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.shiro.freemarker;

/** Equivalent to {@link org.apache.shiro.web.tags.HasRoleTag} */
public class HasRoleTag extends RoleTag {
  protected boolean showTagBody(String roleName) {
    return getSubject() != null && getSubject().hasRole(roleName);
  }
}
