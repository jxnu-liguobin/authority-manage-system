package cn.edu.jxnu.base.service;

import cn.edu.jxnu.base.entity.BorrowBook;

/**
 * 
 * @author 借阅图书服务接口
 * @time 2018年4月10日 下午5:50:10.
 * @version V1.0
 */
public interface IBorrowBookService extends IBaseService<BorrowBook, String> {

	/**
	 * 通过用户id查询借阅信息
	 * 
	 * @param userId
	 *            用户id
	 * @return BorrowBook[]
	 */
	BorrowBook[] findByUserId(int userId);

	/**
	 * 通过图书id查询借阅信息
	 * 
	 * @param bookId
	 *            图书id
	 * @return BorrowBook[]
	 */
	BorrowBook[] findByBookId(String bookId);

	/**
	 * 通过用户id和图书id查询借阅信息
	 * 
	 * @parame userId 用户id bookId 图书id
	 * @return BorrowBook
	 */
	BorrowBook findByUserIdAndBookId(int userId, String bookId);

	/**
	 * 保存或修改借阅信息
	 * 
	 * @param borrowBook
	 *            借阅信息
	 * @return void
	 */
	void saveOrUpdate(BorrowBook borrowBook);

	/**
	 * 通过用户id和图书id联合主键删除借阅信息
	 * 
	 * @param userId
	 *            用户id bookId 图书id
	 * @return void
	 */
	void deletByUserIdAndBookId(int userId, String bookId);

}
