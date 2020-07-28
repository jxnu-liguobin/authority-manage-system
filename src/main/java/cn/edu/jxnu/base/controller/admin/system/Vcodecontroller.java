package cn.edu.jxnu.base.controller.admin.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jxnu.base.redis.RedisService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 梦境迷离.
 * @time 2018年6月11日
 * @version v1.0
 */
@Slf4j
@Controller
public class Vcodecontroller {

	@Autowired
	private RedisService redisService;

	/**
	 * 这里有个BUG，第一次请求的时候验证码2秒发送一个请求是正常的，但是验证码错误，进行第二次输入时，每次输入单个字母就发送请求。未知原因
	 * 
	 * @param vcode
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/isTrue")
	public boolean isTrue(@RequestParam("vcode") String vcode) {
		boolean result = false;
		log.info("前台验证码:" + vcode);
		if (vcode == null || vcode == "") {
			return result;

		}
		String rcode = redisService.get("_code");
		// 转化成小写字母
		if (rcode == null) {
			return result;
		}
		vcode = vcode.toLowerCase();
		// 还可以读取一次后把验证码清空，这样每次登录都必须获取验证码
		if (!vcode.equals(rcode)) {
			return result;// 验证失败
		}
		if (redisService.del("_code")) {
			// 先删除再设置验证成功
			result = true;
		}
		return result;

	}
}
