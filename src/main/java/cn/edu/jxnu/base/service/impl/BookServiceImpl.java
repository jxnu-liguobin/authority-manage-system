/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.dao.IBookDao;
import cn.edu.jxnu.base.entity.Book;
import cn.edu.jxnu.base.service.IBookService;
import cn.edu.jxnu.base.utils.UUIDUtils;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 图书服务层实现
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Service
@Slf4j
@Transactional
public class BookServiceImpl extends BaseServiceImpl<Book, String> implements IBookService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired private IBookDao bookDao;

    @Override
    public IBaseDao<Book, String> baseDao() {
        return this.bookDao;
    }

    @Override
    public Mono<Book> findByBookId(String id) {
        logger.info("findById: " + id);
        return Mono.justOrEmpty(bookDao.findByBookId(id));
    }

    @Override
    public Mono<Book> findByBookName(String bookName) {
        logger.info("findByName: " + bookName);
        return Mono.justOrEmpty(bookDao.findByBookName(bookName));
    }

    @Override
    public Mono<Book> findByBookPress(String bookPress) {
        logger.info("findByPress: " + bookPress);
        return Mono.justOrEmpty(bookDao.findByBookPress(bookPress));
    }

    // 库存修改的时候，需要根据库存修改，动态修改可用库存，所以需要保留原有总库存
    @Override
    public Mono<Book> saveOrUpdate(Book book, Integer cInventory) {
        logger.info("saveOrUpdate: " + book.toString());
        if (book.getBookId() == null || book.getBookId().length() == 0) {
            book.setBookId(UUIDUtils.makeID());
            book.setCurrentInventory(book.getBookInventory());
            return save(book);
        } else {
            return findByBookId(book.getBookId())
                    .map(
                            mBook -> {
                                mBook.setBookName(book.getBookName());
                                mBook.setBookAuthor(book.getBookAuthor());
                                mBook.setBookPress(book.getBookPress());
                                mBook.setBookInventory(book.getBookInventory());

                                // 这里需要在库存增加的时候增加可用库存
                                int temp = book.getBookInventory() - cInventory;
                                log.info("当前库存差：" + temp);
                                int oldInventoryTemp =
                                        cInventory - book.getCurrentInventory(); // 原有库存-可用
                                if (book.getBookInventory() < oldInventoryTemp) {
                                    throw new RuntimeException("库存量不能小于已借出数量");
                                }
                                mBook.setCurrentInventory(temp + book.getCurrentInventory());
                                update(mBook).subscribe();
                                return mBook;
                            });
        }
    }

    @Override
    public Mono<Book> saveOrUpdate(Book book) {
        logger.info("saveOrUpdate: " + book.toString());
        if (book.getBookId() == null || book.getBookId().length() == 0) {
            book.setBookId(UUIDUtils.makeID());
            book.setCurrentInventory(book.getBookInventory());
            return save(book);
        } else {
            return findByBookId(book.getBookId())
                    .map(
                            mBook -> {
                                mBook.setBookName(book.getBookName());
                                mBook.setBookAuthor(book.getBookAuthor());
                                mBook.setBookPress(book.getBookPress());
                                mBook.setBookInventory(book.getBookInventory());
                                // 这里需要在库存增加的时候增加可用库存
                                mBook.setCurrentInventory(book.getCurrentInventory());
                                update(mBook).subscribe();
                                return mBook;
                            });
        }
    }
}
