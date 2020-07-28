package cn.edu.jxnu.base.service.specification;

import lombok.Data;

/**
 * 操作符实体类
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:59:15.
 * @version V1.0
 */

@Data
public class SpecificationOperator {
	/**
	 * 这个类中存储了键值对和操作符号，另外存储了连接下一个条件的类型是and还是or 创建时通过
	 * id>=7,其中id就是key,>=就是oper操作符，7就是value 特殊的自定义几个操作符(:表示like
	 * %v%，b:表示v%,:b表示%v)
	 */
	/**
	 * 操作符的key，如查询时的name,id之类
	 */
	private String key;
	/**
	 * 操作符的value，具体要查询的值
	 */
	private Object value;
	/**
	 * 操作符，自己定义的一组操作符，用来方便查询
	 */
	private String oper;
	/**
	 * 连接的方式：and或者or
	 */
	private String join;

	public static enum Operator {
		eq, ge, le, gt, lt, likeL, likeR, likeAll, isNull, isNotNull, notEqual;
		@Override
		public String toString() {
			return name();
		}
	}

	public static enum Join {
		and, or;
		@Override
		public String toString() {
			return name();
		}
	}

}