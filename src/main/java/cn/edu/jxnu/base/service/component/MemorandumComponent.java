/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service.component;

import cn.edu.jxnu.base.entity.Memorandum;
import cn.edu.jxnu.base.service.IMemorandumService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 记录备忘录的工具
 *
 * <p>同步单利
 *
 * @author 梦境迷离
 * @version v2.0 2020年11月20日
 */
@Component
@Lazy
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON) // 单例模式，在整个应用中只能创建一个实例
public class MemorandumComponent {

    @Autowired private IMemorandumService memorandumService;

    public final synchronized IMemorandumService instance() {
        if (memorandumService == null) {
            int i = 3;
            while (i-- > 0) {
                try {
                    Thread.sleep(1000);
                    if (memorandumService != null) return memorandumService;
                } catch (InterruptedException ignored) {
                }
                log.error("缺少IMemorandumService，等待20ms 第{}次", 3 - i);
                if (3 - i == 3) {
                    throw new NullPointerException("缺少IMemorandumService");
                }
            }
        }
        return memorandumService;
    }

    /**
     * 自主登陆或者注册使用，即本身就是操作人
     *
     * @param usercode 用户码
     * @param userName 用户名
     * @param type 操作类型
     */
    public final synchronized void saveMemorandum(String usercode, String userName, String type) {
        Memorandum m = new Memorandum(0, new Date(), usercode, userName, type + " | " + usercode);
        log.info("保存操作记录: {}", m.toString());
        this.instance().save(m).subscribe();
    }

    /**
     * @param usercode 执行操作的管理员账号
     * @param userName 执行操作的管理员姓名
     * @param type 操作类型
     * @param ucode 被执行操作的用户账号
     */
    public final synchronized void saveMemorandum(
            String usercode, String userName, String type, String ucode) {
        Memorandum m = new Memorandum(0, new Date(), usercode, userName, type + " | " + ucode);
        log.info("保存操作记录: {}", m.toString());
        this.instance().save(m).subscribe();
    }
}
