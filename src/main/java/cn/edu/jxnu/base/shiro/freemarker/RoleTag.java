/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.shiro.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Map;

/** Equivalent to {@link org.apache.shiro.web.tags.RoleTag} */
public abstract class RoleTag extends SecureTag {
    String getName(@SuppressWarnings("rawtypes") Map params) {
        return getParam(params, "name");
    }

    @Override
    public void render(
            Environment env, @SuppressWarnings("rawtypes") Map params, TemplateDirectiveBody body)
            throws IOException, TemplateException {
        boolean show = showTagBody(getName(params));
        if (show) {
            renderBody(env, body);
        }
    }

    protected abstract boolean showTagBody(String roleName);
}
