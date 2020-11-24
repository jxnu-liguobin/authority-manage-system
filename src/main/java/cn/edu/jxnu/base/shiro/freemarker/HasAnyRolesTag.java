/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.shiro.freemarker;

import org.apache.shiro.subject.Subject;

/**
 * Displays body content if the current user has any of the roles specified.
 *
 * <p>Equivalent to {@link org.apache.shiro.web.tags.HasAnyRolesTag}
 *
 * @since 0.2
 */
public class HasAnyRolesTag extends RoleTag {
    // Delimeter that separates role names in tag attribute
    private static final String ROLE_NAMES_DELIMETER = ",";

    protected boolean showTagBody(String roleNames) {
        boolean hasAnyRole = false;
        Subject subject = getSubject();

        if (subject != null) {
            // Iterate through roles and check to see if the user has one of the roles
            for (String role : roleNames.split(ROLE_NAMES_DELIMETER)) {
                if (subject.hasRole(role.trim())) {
                    hasAnyRole = true;
                    break;
                }
            }
        }

        return hasAnyRole;
    }
}
