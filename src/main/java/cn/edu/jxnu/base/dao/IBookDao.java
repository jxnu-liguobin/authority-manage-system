package cn.edu.jxnu.base.dao;

import org.springframework.stereotype.Repository;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.entity.Book;

/**
 * 图书管理dao
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:33:58.
 * @version V1.0
 */
@Repository
public interface IBookDao extends IBaseDao<Book, String> {

	/**
	 * 根据图书ID查询图书
	 * 
	 * @time 2018年4月10日 下午5:34:07.</br>
	 * @version V1.0</br>
	 * @param bookId
	 * @return 图书</br>
	 */
	Book findByBookId(String bookId);

	/**
	 * 根据图书名字，查询图书
	 * 
	 * @time 2018年4月10日 下午5:34:25.</br>
	 * @version V1.0</br>
	 * @param bookName
	 * @return 图书</br>
	 */
	Book findByBookName(String bookName);

	/**
	 * 根据出版社查询图书
	 * 
	 * @time 2018年4月10日 下午5:34:38.</br>
	 * @version V1.0</br>
	 * @param bookPress
	 * @return 图书</br>
	 */
	Book findByBookPress(String bookPress);

}
