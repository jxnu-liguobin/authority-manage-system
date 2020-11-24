/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.Book;
import reactor.core.publisher.Mono;

/**
 * 图书服务接口
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public interface IBookService extends IBaseService<Book, String> {

    /**
     * 根据图书id查找图书
     *
     * @param id
     * @return Book
     */
    Mono<Book> findByBookId(String id);

    /**
     * 根据书名查图书
     *
     * @param bookName
     * @return Book
     */
    Mono<Book> findByBookName(String bookName);

    /**
     * 根据出版社查图书
     *
     * @param bookPress
     * @return Book
     */
    Mono<Book> findByBookPress(String bookPress);

    /**
     * 保存或更新图书信息
     *
     * @param book
     * @return void
     */
    Mono<Book> saveOrUpdate(Book book);

    /**
     * 保存或更新图书信息 携带当前库存
     *
     * @param book
     * @param cInventory
     */
    Mono<Book> saveOrUpdate(Book book, Integer cInventory);
}
