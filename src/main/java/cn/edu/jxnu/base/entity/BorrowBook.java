package cn.edu.jxnu.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 借书表
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:39:29.
 * @version V1.0
 */
@IdClass(BorrowBook.class) // 复合主键（user_id，book_id）
@Embeddable // 表示此类可被插入到其他类
@Entity
@Table(name = "tb_borrow_books")
@Data
@EqualsAndHashCode(callSuper=false)
public class BorrowBook extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7107955948085904241L;

	/**
	 * 用户id
	 */
	@Id
	@Column(name = "user_id", nullable = false)
	private Integer userId;

	/**
	 * 图书id
	 */
	@Id
	@Column(name = "book_id", nullable = false)
	private String bookId;

	/**
	 * 图书名
	 */
	private String bookName;

	/**
	 * 作者
	 */
	private String bookAuthor;

	/**
	 * 出版社
	 */
	private String bookPress;

	/**
	 * 更新时间
	 */
	private Date updateTime;

}
