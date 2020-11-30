/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.shiro.freemarker;

/** Equivalent to {@link org.apache.shiro.web.tags.LacksRoleTag} */
public class LacksRoleTag extends RoleTag {
  protected boolean showTagBody(String roleName) {
    boolean hasRole = getSubject() != null && getSubject().hasRole(roleName);
    return !hasRole;
  }
}
