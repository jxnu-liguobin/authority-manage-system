package cn.edu.jxnu.base.config.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.jxnu.base.entity.BorrowBook;
import cn.edu.jxnu.base.redis.RedisService;
import cn.edu.jxnu.base.service.IBorrowBookService;
import lombok.extern.slf4j.Slf4j;

/**
 * redis初始化数据
 * 
 * @author 梦境迷离 
 * @time 2018年4月11日10:13:08
 * @version V1.0
 *
 */

@Component
@Slf4j
public class RedisGetDataListener implements ServletContextListener {

	@Autowired
	private RedisService redisService;
	@Autowired
	private IBorrowBookService borrowBookService;

	/**
	 * 容器初始化
	 * 
	 * @author 梦境迷离 
	 * @time 上午10:14:12
	 * @version V1.0
	 * @param sce
	 *
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("开始初始化redis数据->base:0");
		Map<Integer, List<String>> map = new HashMap<>();
		List<BorrowBook> listBook = borrowBookService.findAll();
		List<String> sList;
		for (BorrowBook borrowBook : listBook) {
			// 启动的时候将数据放进redis中
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
		}
		try {
			// 放进redis中
			redisService.putMap("base:0", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("初始化redis数据完毕->base:0");
	}

	/**
	 * @author 梦境迷离 
	 * @time 上午10:14:12
	 * @version V1.0
	 * @param sce
	 *
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
