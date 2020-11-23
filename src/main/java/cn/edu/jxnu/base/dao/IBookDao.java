package cn.edu.jxnu.base.dao;

import cn.edu.jxnu.base.entity.Book;
import org.springframework.stereotype.Repository;

/**
 * 图书管理dao
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Repository
public interface IBookDao extends IBaseDao<Book, String> {

    /**
     * 根据图书ID查询图书
     *
     * @param bookId
     * @return 图书</ br>
     */
    Book findByBookId(String bookId);

    /**
     * 根据图书名字，查询图书
     *
     * @param bookName
     * @return 图书</ br>
     */
    Book findByBookName(String bookName);

    /**
     * 根据出版社查询图书
     *
     * @param bookPress
     * @return 图书</ br>
     */
    Book findByBookPress(String bookPress);

}
