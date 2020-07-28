package cn.edu.jxnu.base.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import cn.edu.jxnu.base.common.JsonResult;
import cn.edu.jxnu.base.common.MemorandumUtils;
import cn.edu.jxnu.base.controller.BaseController;
import cn.edu.jxnu.base.entity.Book;
import cn.edu.jxnu.base.entity.BorrowBook;
import cn.edu.jxnu.base.entity.BorrowList;
import cn.edu.jxnu.base.entity.User;
import cn.edu.jxnu.base.redis.RedisService;
import cn.edu.jxnu.base.service.IBookService;
import cn.edu.jxnu.base.service.IBorrowBookService;
import cn.edu.jxnu.base.service.IUserService;
import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;

/**
 * 图书管理控制类
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:28:05.
 * @version V1.0
 */
@Controller
@RequestMapping("/web/books")
public class BookController extends BaseController {

	@Autowired
	private RedisService redisService;

	@Autowired
	private IBookService bookService;
	@Autowired
	private IBorrowBookService borrowBookService;
	@Autowired
	private IUserService userService;
	@Autowired
	private MemorandumUtils memorandumUtils;

	/**
	 * 默认索引页
	 * 
	 * @return index页面
	 */
	@RequestMapping("/index")
	public String index() {
		return "/admin/books/index";
	}

	/**
	 * 添加图书
	 * 
	 * @return addform页面
	 */
	@RequestMapping(value = { "/addBook" }, method = RequestMethod.GET)
	public String addBook() {
		return "admin/books/addform";
	}

	/**
	 * 删除图书
	 * 
	 * 考虑书已经被借出去了，不能再删除
	 * 
	 * @time 2018年4月10日 下午5:28:38.
	 * 
	 * @version V1.0
	 * @param id
	 * @param map
	 * @param uCode
	 *            操作人
	 * @return JsonResult
	 * 
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable String id, ModelMap map, @RequestParam("uCode") String uCode) {
		try {
			List<User> ls = userService.findAll();
			for (User user : ls) {
				// 得到所有用户，如果有，则不能删除
				List<String> bookIdList = redisService.hashGet("base:0", user.getId());
				if (bookIdList != null) {
					for (String string : bookIdList) {
						// 当前需要删除的书籍，存在一条redis中的记录，则不能删除
						if (string.equals(id)) {
							return JsonResult.failure("该书有用户借阅，暂时不可删除");
						}
					}
				}
			}
			Book temp = bookService.findByBookId(id);
			bookService.delete(id);
			memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
					"删除图书", temp.getBookId() + " | " + temp.getBookName());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure("删除失败");
		}
		return JsonResult.success();
	}

	/**
	 * 借书
	 * 
	 * @time 2018年4月10日 下午5:29:10.
	 * 
	 * @version V1.0
	 * @param borrowlist
	 *            Json类型的借书表，数据集
	 * @param map
	 * @return JsonResult
	 */
	@RequestMapping(value = { "/borrowlist/{borrowlist}" }, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult borrowList(@PathVariable String borrowlist, ModelMap map) {

		if (!borrowlist.equals("undefined")) {
			Gson gson = new Gson();
			BorrowList mBorrowList = gson.fromJson(borrowlist, BorrowList.class);
			int count = mBorrowList.getBooklist().length;
			BorrowBook[] borrowBook = new BorrowBook[count];
			if (count == 0) {
				// 没有任何的提交同样需要提示
				return JsonResult.failure("未选择要借阅的书籍！");
			}
			Book[] book = new Book[mBorrowList.getBooklist().length];
			int i = 0;
			while (i < mBorrowList.getBooklist().length) {
				borrowBook[i] = new BorrowBook();
				book[i] = new Book();
				borrowBook[i].setUserId(mBorrowList.getId());
				borrowBook[i].setBookId(mBorrowList.getBooklist()[i]);
				book[i] = bookService.findByBookId(mBorrowList.getBooklist()[i]);
				// 一本书只能借一次，因此需要判断一下该用户是否已经借过该书
				BorrowBook isBorrowBook = borrowBookService.findByUserIdAndBookId(mBorrowList.getId(),
						mBorrowList.getBooklist()[i]);
				if (book[i].getCurrentInventory() > 0) {
					if (isBorrowBook == null) {
						book[i].setCurrentInventory(book[i].getCurrentInventory() - 1);
						// 添加额外的属性
						Book book2 = bookService.findByBookId((book[i].getBookId()));//
						borrowBook[i].setBookName(book2.getBookName());
						borrowBook[i].setBookAuthor((book2.getBookAuthor()));
						borrowBook[i].setBookPress((book2.getBookPress()));
						// 更新库存
						bookService.saveOrUpdate(book[i]);
						borrowBookService.save(borrowBook[i]);
						// 添加到redis中
						List<String> bookIdList = redisService.hashGet("base:0", mBorrowList.getId());
						if (bookIdList == null) {
							// 不存在添加。userId<--->bookId。
							bookIdList = new ArrayList<>();
							bookIdList.add(book[i].getBookId());
							redisService.hashPushHashMap("base:0", mBorrowList.getId(), bookIdList);
						} else {
							// 存在,继续添加到redis
							bookIdList.add(book[i].getBookId());
							redisService.hashPushHashMap("base:0", mBorrowList.getId(), bookIdList);
						}
					} else {
						return JsonResult.failure("您已经借阅该书！");
					}

				} else {
					return JsonResult.failure("库存不足请重新选择图书！");
				}

				i++;
			}
			i = 0;
			return JsonResult.success();
		} else {
			return JsonResult.failure("未选择要借阅的书籍！");
		}

	}

	/**
	 * 还书表
	 * 
	 * @time 2018年4月10日 下午5:29:44.
	 * 
	 * @version V1.0
	 * @param id
	 *            借书用户id
	 * @param map
	 * @return String
	 */
	@RequestMapping(value = { "/returnBookList/{id}" }, method = RequestMethod.POST)
	@ResponseBody
	public String returnBookList(@PathVariable String id, ModelMap map) {

		BorrowBook[] borrowBooks = borrowBookService.findByUserId(Integer.parseInt(id));
		Book[] books = new Book[borrowBooks.length];
		// Date date=null;
		for (int i = 0; i < books.length; i++) {
			books[i] = bookService.findByBookId(borrowBooks[i].getBookId());
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("borrowBooks", borrowBooks);
		resultMap.put("books", books);
		Gson gson = new Gson();
		String jsonStr = gson.toJson(resultMap);

		return jsonStr;
	}

	/**
	 * 管理员归还图书
	 * 
	 * @time 2018年4月10日 下午5:30:39.
	 * 
	 * @version V1.0
	 * @param borrowlist
	 *            Json类型的借书表
	 * @return JsonResult
	 * @throws Exception
	 */
	@RequestMapping(value = { "/returnBook/{borrowlist}" }, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult returnBook(@PathVariable String borrowlist) throws Exception {

		Gson gson = new Gson();
		BorrowList mBorrowList = gson.fromJson(borrowlist, BorrowList.class);
		BorrowBook[] borrowBook = new BorrowBook[mBorrowList.getBooklist().length];
		Book[] book = new Book[mBorrowList.getBooklist().length];
		int i = 0;
		// 同样需要更新,首先得到用户id
		List<String> bookIdList = redisService.hashGet("base:0", mBorrowList.getId());
		while (i < mBorrowList.getBooklist().length) {
			borrowBook[i] = new BorrowBook();
			book[i] = new Book();
			borrowBook[i].setUserId(mBorrowList.getId());
			borrowBook[i].setBookId(mBorrowList.getBooklist()[i]);
			book[i] = bookService.findByBookId(mBorrowList.getBooklist()[i]);
			book[i].setCurrentInventory(book[i].getCurrentInventory() + 1);
			bookService.saveOrUpdate(book[i]);
			if (bookIdList.contains(mBorrowList.getBooklist()[i])) {
				// 判断借的书中有没有记录，有，删除
				bookIdList.remove(mBorrowList.getBooklist()[i]);
				Map<Integer, List<String>> map = new HashMap<>();
				map.put(mBorrowList.getId(), bookIdList);
				redisService.putMap("base:0", map);
			}

			borrowBookService.deletByUserIdAndBookId(borrowBook[i].getUserId(), borrowBook[i].getBookId());
			;
			i++;
		}
		i = 0;
		return JsonResult.success();
	}

	/**
	 * 无授权的归还一本图书
	 * 
	 * @time 2018年4月10日 下午5:30:59.
	 * 
	 * @version V1.0
	 * @param jsonDate
	 *            Json类型的借书表
	 * @return JsonResult
	 */
	@RequestMapping(value = { "/returnOneBook/{jsonDate}" }, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult returnOneBook(@PathVariable String jsonDate) {
		try {
			Gson gson = new Gson();
			@SuppressWarnings("unchecked")
			ArrayList<String> date = gson.fromJson(jsonDate, ArrayList.class);
			String bookId = null;
			int userId = 0;
			if (date.size() == 2) {
				userId = Integer.parseInt(date.get(0));
				bookId = date.get(1);
			} else {
				return JsonResult.failure("无效的参数");
			}
			// 这里自主还书的时候也需要更新一下redis中的记录
			List<String> bookIdList = redisService.hashGet("base:0", userId);
			bookIdList.remove(bookId);// 去除这本书
			Map<Integer, List<String>> map = new HashMap<>();
			map.put(userId, bookIdList);
			System.out.println("当前还需还的书：" + bookIdList);
			// 重写添加到redis中
			redisService.putMap("base:0", map);
			Book book = new Book();
			book = bookService.findByBookId(bookId);
			book.setCurrentInventory(book.getCurrentInventory() + 1);// 增加库存
			bookService.saveOrUpdate(book);// 更新库存
			borrowBookService.deletByUserIdAndBookId(userId, bookId);// 注意这里是组合主键
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure("未知原因，导致操作失败");
		}
		return JsonResult.success();
	}

	/**
	 * 修改图书响应请求
	 * 
	 * @time 2018年4月10日 下午5:31:18.
	 * 
	 * @version V1.0
	 * @param id
	 *            修改的图书id
	 * @param map
	 * @return String addform页面
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable String id, ModelMap map) {

		Book book = bookService.findByBookId(id);
		map.put("book", book);
		return "admin/books/addform";
	}

	/**
	 * 修改图书
	 * 
	 * @time 2018年4月10日 下午5:31:46.
	 * 
	 * @version V1.0
	 * @param book
	 *            图书实体，数据集
	 * @param map
	 * @return JsonResult 是否修改成功
	 */
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Book book, ModelMap map, @RequestParam("uCode") String uCode,
			@RequestParam("cInventory") Integer cInventory) {
		try {

			bookService.saveOrUpdate(book, cInventory);
			memorandumUtils.saveMemorandum(memorandumUtils, uCode, userService.findByUserCode(uCode).getUserName(),
					"修改/新增图书", book.getBookId() + " | " + book.getBookName());
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
	/*
		*//**
			 * index页面中BootStrapTable请求列表响应
			 * 
			 * @parameter
			 * @return Page<Book>
			 *//*
				 * @RequestMapping(value = { "/list" })
				 * 
				 * @ResponseBody public Page<Book> list() {
				 * 
				 * SimpleSpecificationBuilder<Book> builder = new
				 * SimpleSpecificationBuilder<Book>(); String searchText =
				 * request.getParameter("searchText"); if (StringUtils.isNotBlank(searchText)) {
				 * builder.add("bookName", Operator.likeAll.name(), searchText); } Page<Book>
				 * page = bookService.findAll(builder.generateSpecification(),
				 * getPageRequest()); return page; }
				 */

	/**
	 * 前台查询图书
	 * 
	 * @time 2018年4月10日 下午5:32:30.
	 * 
	 * @version V1.0
	 * @return Page 类型 Book
	 */
	@RequestMapping(value = { "/findlist" })
	@ResponseBody
	public Page<Book> findList() {

		SimpleSpecificationBuilder<Book> builder = new SimpleSpecificationBuilder<Book>();
		String bookName = request.getParameter("inputBookName");
		String bookAuthor = request.getParameter("inputAuthor");
		String bookPress = request.getParameter("inputPublication");
		if (StringUtils.isNotBlank(bookName)) {
			builder.add("bookName", Operator.likeAll.name(), bookName);
		}
		if (StringUtils.isNotBlank(bookAuthor)) {
			builder.add("bookAuthor", Operator.likeAll.name(), bookAuthor);

		}
		if (StringUtils.isNotBlank(bookPress)) {
			builder.add("bookPress", Operator.likeAll.name(), bookPress);
		}
		Page<Book> page = bookService.findAll(builder.generateSpecification(), getPageRequest());
		return page;
	}

}
