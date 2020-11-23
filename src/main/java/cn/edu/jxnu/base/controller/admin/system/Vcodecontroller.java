package cn.edu.jxnu.base.controller.admin.system;

import cn.edu.jxnu.base.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 * @author 梦境迷离.
 * @version v2.0 2020年11月20日
 */
@Slf4j
@Controller
public class Vcodecontroller {

    @Autowired
    private RedisService redisService;

    /**
     * 这里有个BUG，第一次请求的时候验证码2秒发送一个请求是正常的，但是验证码错误，进行第二次输入时，每次输入单个字母就发送请求。未知原因
     */
    @ResponseBody
    @RequestMapping(value = "/isTrue")
    public Mono<Boolean> isTrue(@RequestParam("vcode") String vcode) {
        boolean result = false;
        log.info("前台验证码: " + vcode);
        if (vcode == null || vcode == "") {
            return Mono.just(result);

        }
        String rcode = redisService.get("_code");
        // 转化成小写字母
        if (rcode == null) {
            return Mono.just(result);
        }
        vcode = vcode.toLowerCase();
        // 还可以读取一次后把验证码清空，这样每次登录都必须获取验证码
        if (!vcode.equals(rcode)) {
            return Mono.just(result);// 验证失败
        }
        if (redisService.del("_code")) {
            // 先删除再设置验证成功
            result = true;
        }
        return Mono.just(result);

    }
}
