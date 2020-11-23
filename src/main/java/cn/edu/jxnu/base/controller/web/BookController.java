package cn.edu.jxnu.base.controller.web;

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
import cn.edu.jxnu.base.utils.Constats;
import cn.edu.jxnu.base.utils.JsonResult;
import cn.edu.jxnu.base.utils.MemorandumUtils;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图书管理控制类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
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
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "/admin/books/index";
    }

    /**
     * 添加图书
     *
     * @return
     */
    @RequestMapping(value = {"/addBook"})
    public String addBook() {
        return "admin/books/addform";
    }

    /**
     * 删除图书
     * <p>
     * 考虑书已经被借出去了，不能再删除
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Mono<JsonResult> delete(@PathVariable String id, ModelMap map, @RequestParam("uCode") String uCode) {
        try {
            Flux<User> ls = userService.findAll();
            ls.map(user -> {
                // 得到所有用户，如果有，则不能删除
                List<String> bookIdList = redisService.hashGet(Constats.BOOK_REDIS_KEY, user.getId());
                if (bookIdList != null) {
                    for (String string : bookIdList) {
                        // 当前需要删除的书籍，存在一条redis中的记录，则不能删除
                        if (string.equals(id)) {
                            return Mono.just(JsonResult.failure("该书有用户借阅，暂时不可删除"));
                        }
                    }
                }
                return null;
            }).subscribe();
            Mono<Book> tempMono = bookService.findByBookId(id);
            tempMono.subscribe(temp -> {
                bookService.delete(id).subscribe();
                userService.findByUserCode(uCode).subscribe(u -> memorandumUtils.saveMemorandum(memorandumUtils, uCode, u.getUserName(),
                        "删除图书", temp.getBookId() + " | " + temp.getBookName()));
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(JsonResult.failure("删除失败"));
        }
        return Mono.just(JsonResult.success());
    }

    /**
     * 借书
     */
    @RequestMapping(value = {"/borrowlist/{borrowlist}"}, method = RequestMethod.POST)
    @ResponseBody
    public Mono<JsonResult> borrowList(@PathVariable String borrowlist, ModelMap map) {

        if (!borrowlist.equals("undefined")) {
            Gson gson = new Gson();
            BorrowList mBorrowList = gson.fromJson(borrowlist, BorrowList.class);
            int count = mBorrowList.getBooklist().length;
            BorrowBook[] borrowBook = new BorrowBook[count];
            if (count == 0) {
                // 没有任何的提交同样需要提示
                return Mono.just(JsonResult.failure("未选择要借阅的书籍！"));
            }
            Book[] book = new Book[mBorrowList.getBooklist().length];
            int i = 0;
            while (i < mBorrowList.getBooklist().length) {
                borrowBook[i] = new BorrowBook();
                book[i] = new Book();
                borrowBook[i].setUserId(mBorrowList.getId());
                borrowBook[i].setBookId(mBorrowList.getBooklist()[i]);
                int finalI = i;
                bookService.findByBookId(mBorrowList.getBooklist()[i]).subscribe(b -> book[finalI] = b);
                // 一本书只能借一次，因此需要判断一下该用户是否已经借过该书
                BorrowBook isBorrowBook = borrowBookService.findByUserIdAndBookId(mBorrowList.getId(),
                        mBorrowList.getBooklist()[i]);
                if (book[i].getCurrentInventory() > 0) {
                    if (isBorrowBook == null) {
                        book[i].setCurrentInventory(book[i].getCurrentInventory() - 1);
                        // 添加额外的属性
                        int finalI1 = i;
                        bookService.findByBookId((book[i].getBookId())).subscribe(b -> {
                            borrowBook[finalI1].setBookName(b.getBookName());
                            borrowBook[finalI1].setBookAuthor((b.getBookAuthor()));
                            borrowBook[finalI1].setBookPress((b.getBookPress()));
                        });
                        // 更新库存
                        bookService.saveOrUpdate(book[i]).subscribe();
                        borrowBookService.save(borrowBook[i]).subscribe();
                        // 添加到redis中
                        List<String> bookIdList = redisService.hashGet(Constats.BOOK_REDIS_KEY, mBorrowList.getId());
                        if (bookIdList == null) {
                            // 不存在添加。userId<--->bookId。
                            bookIdList = new ArrayList<>();
                            bookIdList.add(book[i].getBookId());
                            redisService.hashPushHashMap(Constats.BOOK_REDIS_KEY, mBorrowList.getId(), bookIdList);
                        } else {
                            // 存在,继续添加到redis
                            bookIdList.add(book[i].getBookId());
                            redisService.hashPushHashMap(Constats.BOOK_REDIS_KEY, mBorrowList.getId(), bookIdList);
                        }
                    } else {
                        return Mono.just(JsonResult.failure("您已经借阅该书！"));
                    }

                } else {
                    return Mono.just(JsonResult.failure("库存不足请重新选择图书！"));
                }

                i++;
            }
            return Mono.just(JsonResult.success());
        } else {
            return Mono.just(JsonResult.failure("未选择要借阅的书籍！"));
        }

    }

    /**
     * 还书表
     */
    @RequestMapping(value = {"/returnBookList/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public Mono<String> returnBookList(@PathVariable String id, ModelMap map) {

        BorrowBook[] borrowBooks = borrowBookService.findByUserId(Integer.parseInt(id));
        Book[] books = new Book[borrowBooks.length];
        // Date date=null;
        for (int i = 0; i < books.length; i++) {
            int finalI = i;
            bookService.findByBookId(borrowBooks[i].getBookId()).subscribe(b -> books[finalI] = b);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("borrowBooks", borrowBooks);
        resultMap.put("books", books);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(resultMap);

        return Mono.just(jsonStr);
    }

    /**
     * 管理员归还图书
     */
    @RequestMapping(value = {"/returnBook/{borrowlist}"}, method = RequestMethod.POST)
    @ResponseBody
    public Mono<JsonResult> returnBook(@PathVariable String borrowlist) throws Exception {

        Gson gson = new Gson();
        BorrowList mBorrowList = gson.fromJson(borrowlist, BorrowList.class);
        BorrowBook[] borrowBook = new BorrowBook[mBorrowList.getBooklist().length];
        Book[] book = new Book[mBorrowList.getBooklist().length];
        int i = 0;
        // 同样需要更新，首先得到用户id
        List<String> bookIdList = redisService.hashGet(Constats.BOOK_REDIS_KEY, mBorrowList.getId());
        while (i < mBorrowList.getBooklist().length) {
            borrowBook[i] = new BorrowBook();
            book[i] = new Book();
            borrowBook[i].setUserId(mBorrowList.getId());
            borrowBook[i].setBookId(mBorrowList.getBooklist()[i]);
            int finalI = i;
            bookService.findByBookId(mBorrowList.getBooklist()[i]).subscribe(b -> book[finalI] = b);
            book[i].setCurrentInventory(book[i].getCurrentInventory() + 1);
            bookService.saveOrUpdate(book[i]).subscribe();
            if (bookIdList.contains(mBorrowList.getBooklist()[i])) {
                // 判断借的书中有没有记录，有，删除
                bookIdList.remove(mBorrowList.getBooklist()[i]);
                Map<Integer, List<String>> map = new HashMap<>();
                map.put(mBorrowList.getId(), bookIdList);
                redisService.putMap(Constats.BOOK_REDIS_KEY, map);
            }

            borrowBookService.deletByUserIdAndBookId(borrowBook[i].getUserId(), borrowBook[i].getBookId());
            ;
            i++;
        }
        i = 0;
        return Mono.just(JsonResult.success());
    }

    /**
     * 无授权的归还一本图书
     */
    @RequestMapping(value = {"/returnOneBook/{jsonDate}"})
    @ResponseBody
    public Mono<JsonResult> returnOneBook(@PathVariable String jsonDate) {
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
                return Mono.just(JsonResult.failure("无效的参数"));
            }
            // 这里自主还书的时候也需要更新一下redis中的记录
            List<String> bookIdList = redisService.hashGet(Constats.BOOK_REDIS_KEY, userId);
            bookIdList.remove(bookId);// 去除这本书
            Map<Integer, List<String>> map = new HashMap<>();
            map.put(userId, bookIdList);
            System.out.println("当前还需还的书：" + bookIdList);
            // 重写添加到redis中
            redisService.putMap(Constats.BOOK_REDIS_KEY, map);
            Mono<Book> book = bookService.findByBookId(bookId);
            book.subscribe(b -> {
                b.setCurrentInventory(b.getCurrentInventory() + 1);// 增加库存
                bookService.saveOrUpdate(b).subscribe();// 更新库存
            });
            borrowBookService.deletByUserIdAndBookId(userId, bookId);// 注意这里是组合主键
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(JsonResult.failure("未知原因，导致操作失败"));
        }
        return Mono.just(JsonResult.success());
    }

    /**
     * 修改图书响应请求
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable String id, ModelMap map) {
        bookService.findByBookId(id).subscribe(b -> map.put("book", b));
        return "admin/books/addform";
    }

    /**
     * 修改图书
     */
    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public Mono<JsonResult> edit(Book book, ModelMap map, @RequestParam("uCode") String uCode,
                                 @RequestParam("cInventory") Integer cInventory) {
        try {

            bookService.saveOrUpdate(book, cInventory).subscribe();
            userService.findByUserCode(uCode).subscribe(u -> {
                memorandumUtils.saveMemorandum(memorandumUtils, uCode, u.getUserName(),
                        "修改/新增图书", book.getBookId() + " | " + book.getBookName());
            });
        } catch (Exception e) {
            return Mono.just(JsonResult.failure(e.getMessage()));
        }
        return Mono.just(JsonResult.success());
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
     */
    @RequestMapping(value = {"/findlist"})
    @ResponseBody
    public Mono<Page<Book>> findList(HttpServletRequest request) {
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
        return bookService.findAll(builder.generateSpecification(), getPageRequest(request));
    }

}
