package cn.edu.jxnu.base.service.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.jxnu.base.Application;
import cn.edu.jxnu.base.entity.Memorandum;
import cn.edu.jxnu.base.service.IMemorandumService;


/**
 * @author 梦境迷离.
 * @time 2018年5月1日
 * @version v1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MemorandumServiceImplTest {

	@Autowired
	private IMemorandumService memorandumService;

	@Test
	public void testFindString() {
		Memorandum memorandum = memorandumService.find(1);
		System.out.println(memorandum);
	}

	@Test
	public void testSave() {
		Memorandum memorandum = new Memorandum(1, new Date(), "admin", "admin", "图书管理");
		memorandumService.save(memorandum);
	}

}
