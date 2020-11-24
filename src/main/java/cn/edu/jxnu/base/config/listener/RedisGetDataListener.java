/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.config.listener;

import cn.edu.jxnu.base.entity.BorrowBook;
import cn.edu.jxnu.base.redis.RedisService;
import cn.edu.jxnu.base.service.IBorrowBookService;
import cn.edu.jxnu.base.utils.Constats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * redis初始化数据
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Component
@Slf4j
public class RedisGetDataListener implements ServletContextListener {

    @Autowired private RedisService redisService;

    @Autowired private IBorrowBookService borrowBookService;

    /**
     * 容器初始化
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("开始初始化redis数据");
        Map<Integer, List<String>> map = new HashMap<>();
        Flux<BorrowBook> listBook = borrowBookService.findAll();
        listBook.subscribe(
                borrowBook -> {
                    // 启动的时候将数据放进redis中
                    List<String> sList;
                    if (map.containsKey(borrowBook.getUserId())) {
                        sList = map.get(borrowBook.getUserId());
                        sList.add(borrowBook.getBookId());
                        map.put(borrowBook.getUserId(), sList);
                    } else {
                        // 本来没有这条记录
                        sList = new ArrayList<>();
                        sList.add(borrowBook.getBookId());
                        map.put(borrowBook.getUserId(), sList);
                    }
                });
        try {
            // 放进redis中
            redisService.putMap(Constats.BOOK_REDIS_KEY, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("初始化redis数据完毕");
    }

    /** @param sce */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
