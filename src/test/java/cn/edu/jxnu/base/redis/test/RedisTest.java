package cn.edu.jxnu.base.redis.test;

import cn.edu.jxnu.base.Application;
import cn.edu.jxnu.base.redis.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018 梦境迷离. All rights reserved.
 *
 * @author 梦境迷离
 * @since 2018年4月11日 上午9:05:56
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class RedisTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedis() throws Exception {
        redisService.hashPushHashMap("base:0", 1, Arrays.asList("ddd"));
        redisService.hashPushHashMap("base:0", 122, Arrays.asList("bbb"));
        redisService.hashPushHashMap("base:0", 122, Arrays.asList("ccc"));
        redisService.hashPushHashMap("base:0", 222, Arrays.asList("aaa"));
        Map<Integer, List<String>> map = redisService.hashGetAll("base:0");
        map.forEach((x, y) -> {
            if (y instanceof List) {
                System.out.println("List类型");
            }
            System.out.println("k:" + x + " - " + "v:" + y);
        });
        redisService.hashDeleteHashKey("base:0", 222);
        List<String> i = redisService.hashGet("base:0", 122);
        i.stream().forEach(System.out::print);
    }

}
