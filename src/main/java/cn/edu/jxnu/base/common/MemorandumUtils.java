package cn.edu.jxnu.base.common;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.edu.jxnu.base.entity.Memorandum;
import cn.edu.jxnu.base.service.IMemorandumService;
import lombok.extern.slf4j.Slf4j;

/**
 * 记录备忘录的工具
 * 
 * 同步单利
 * 
 * @author 梦境迷离.
 * @time 2018年5月2日
 * @version v1.0
 */

@Component
@Lazy
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON) // 单例模式，在整个应用中只能创建一个实例
public class MemorandumUtils {

	@Autowired
	private IMemorandumService memorandumService;

	public synchronized final IMemorandumService instance() {
		if (memorandumService == null) {
			int i = 3;
			while (i-- > 0) {
				try {
					Thread.sleep(1000);
					if (memorandumService != null)
						return memorandumService;
				} catch (InterruptedException e) {
				}
				log.error("缺少IMemorandumService,等待20ms 第{}次", 3 - i);
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
	 * 回调
	 * 
	 * @author 梦境迷离.
	 * @time 2018年5月2日
	 * @version v1.0
	 * @param memorandumUtils
	 * @param usercode
	 * @param userName
	 * @param type
	 */
	public synchronized final void saveMemorandum(MemorandumUtils memorandumUtils, String usercode, String userName,
			String type) {
		Memorandum m = new Memorandum(0, new Date(), usercode, userName, type + " | " + usercode);
		log.info("保存操作记录:{}", m.toString());
		memorandumUtils.instance().save(m);
	}

	/**
	 * 
	 * @author 梦境迷离.
	 * @time 2018年5月2日
	 * @version v1.0
	 * @param memorandumUtils
	 * @param usercode
	 *            执行操作的管理员账号
	 * @param userName
	 *            执行操作的管理员姓名
	 * @param type
	 *            操作类型
	 * @param ucode
	 *            被执行操作的用户账号
	 */
	public synchronized final void saveMemorandum(MemorandumUtils memorandumUtils, String usercode, String userName,
			String type, String ucode) {
		Memorandum m = new Memorandum(0, new Date(), usercode, userName, type + " | " + ucode);
		log.info("保存操作记录:{}", m.toString());
		memorandumUtils.instance().save(m);
	}

}
