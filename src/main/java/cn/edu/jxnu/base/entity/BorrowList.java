package cn.edu.jxnu.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 借阅列表
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:39:42.
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BorrowList extends BaseEntity {

	private static final long serialVersionUID = -1894163644285296223L;

	/**
	 * 用户id
	 */
	private Integer id;
	/**
	 * 待借阅清单
	 */
	private String[] booklist;

}
