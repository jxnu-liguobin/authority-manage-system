package cn.edu.jxnu.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 图书表
 *
 * @author 梦境迷离
 * @version V1.0
 */
@Entity
@Table(name = "tb_books")
@Data
@EqualsAndHashCode(callSuper = false) // 这里没有继承父类的属性，所以可以使用自己的equals方法。如果有继承则可能造成部分对象比较时不相等
public class Book extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 3275387890061613371L;

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
     * 图书总库存
     */
    private Integer bookInventory;

    /**
     * 图书现有库存
     */
    private Integer currentInventory;

}
