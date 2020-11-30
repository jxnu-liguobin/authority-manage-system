/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.BorrowBook;

/**
 * @author 借阅图书服务接口
 * @version V2.0 2020年11月20日
 */
public interface IBorrowBookService extends IBaseService<BorrowBook, String> {

  /**
   * 通过用户id查询借阅信息
   *
   * @param userId 用户id
   * @return BorrowBook[]
   */
  BorrowBook[] findByUserId(int userId);

  /**
   * 通过图书id查询借阅信息
   *
   * @param bookId 图书id
   * @return BorrowBook[]
   */
  BorrowBook[] findByBookId(String bookId);

  /**
   * 通过用户id和图书id查询借阅信息
   *
   * @return BorrowBook
   * @param userId 用户id bookId 图书id
   * @param bookId 书ID
   */
  BorrowBook findByUserIdAndBookId(int userId, String bookId);

  /**
   * 保存或修改借阅信息
   *
   * @param borrowBook 借阅信息
   */
  void saveOrUpdate(BorrowBook borrowBook);

  /**
   * 通过用户id和图书id联合主键删除借阅信息
   *
   * @param userId 用户id bookId 图书id
   * @param bookId 书ID
   */
  void deletByUserIdAndBookId(int userId, String bookId);
}
