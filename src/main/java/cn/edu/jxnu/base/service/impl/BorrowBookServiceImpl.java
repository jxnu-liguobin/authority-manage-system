package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.dao.IBorrowBookDao;
import cn.edu.jxnu.base.entity.BorrowBook;
import cn.edu.jxnu.base.service.IBorrowBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 借阅服务实现类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Service
@Slf4j
@Transactional
public class BorrowBookServiceImpl extends BaseServiceImpl<BorrowBook, String> implements IBorrowBookService {

    @Autowired
    private IBorrowBookDao borrowBookDao;

    @Override
    public BorrowBook[] findByUserId(int userId) {
        log.info("findByUserId:" + userId);
        return borrowBookDao.findByUserId(userId);
    }

    @Override
    public BorrowBook[] findByBookId(String bookId) {
        log.info("findByBookId:" + bookId);
        return borrowBookDao.findByBookId(bookId);
    }

    @Override
    public void saveOrUpdate(BorrowBook borrowBook) {
        log.info("saveOrUpdate:" + borrowBook.toString());
        save(borrowBook);

    }

    @Override
    public IBaseDao<BorrowBook, String> baseDao() {
        return this.borrowBookDao;
    }

    @Override
    public BorrowBook findByUserIdAndBookId(int userId, String bookId) {
        log.info("findByUserIdAndBookId:" + userId + "-" + bookId);
        return borrowBookDao.findByUserIdAndBookId(userId, bookId);
    }

    @Override
    public void deletByUserIdAndBookId(int userId, String bookId) {
        log.info("deletByUserIdAndBookId:" + userId + "-" + bookId);
        borrowBookDao.mDelet(userId, bookId);
    }

}
