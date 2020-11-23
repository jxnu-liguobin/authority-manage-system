package cn.edu.jxnu.base.dao;

import cn.edu.jxnu.base.entity.BorrowBook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 借书管理dao
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Repository
public interface IBorrowBookDao extends IBaseDao<BorrowBook, String> {
    /**
     * 根据用户查询借阅列表
     *
     * @param userId
     * @return 借书列表</ br>
     */
    BorrowBook[] findByUserId(int userId);

    /**
     * 根据图书查询借阅列表
     *
     * @param bookId
     * @return 借书列表</ br>
     */
    BorrowBook[] findByBookId(String bookId);

    /**
     * 根据用户ID和图书ID查询唯一一条借书记录
     *
     * @param userId
     * @param bookId
     * @return 一条借书记录</ br>
     */
    BorrowBook findByUserIdAndBookId(int userId, String bookId);

    /**
     * 事务删除或修改操作，不支持新增/插入
     */
    @Modifying
    @Query("DELETE FROM BorrowBook b WHERE b.userId = ?1 and b.bookId= ?2")
    void mDelet(int userId, String bookId);

}
