/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.vcode;

import cn.edu.jxnu.base.redis.RedisService;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Gif验证码请求处理
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Controller
@Slf4j
public class GifCodeController {

    @Autowired private RedisService redisService;

    /**
     * 获取验证码（Gif版本）
     *
     * @param response response
     */
    @RequestMapping("getGifCode")
    public void getGifCode(HttpServletResponse response) {
        try {

            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            /** gif格式动画验证码 宽，高，位数。 */
            Captcha captcha = new GifCaptcha(130, 30, 4);
            // 输出
            captcha.out(response.getOutputStream());
            /** 第一次请求的时候没有session */
            // 存入shiro Session 10分钟，改使用 redis 来替代
            redisService.set("_code", captcha.text().toLowerCase(), 30);
            log.info("后端产生的验证码：" + captcha.text());
        } catch (Exception e) {
            log.info("获取验证码异常：" + e.getMessage());
        }
    }
}
