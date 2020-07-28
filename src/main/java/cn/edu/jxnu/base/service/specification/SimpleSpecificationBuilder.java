package cn.edu.jxnu.base.service.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import cn.edu.jxnu.base.service.specification.SpecificationOperator.Join;

/**
 * 查询构建类
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:57:35.
 * @version V1.0
 */
public class SimpleSpecificationBuilder<T> {

	/**
	 * 条件列表
	 */
	private List<SpecificationOperator> opers;

	/**
	 * 构造函数，初始化的条件是and
	 * 
	 * @time 2018年4月10日 下午5:57:46.</br>
	 * @version V1.0</br>
	 * @param key
	 * @param oper
	 * @param value</br>
	 */
	public SimpleSpecificationBuilder(String key, String oper, Object value) {
		SpecificationOperator so = new SpecificationOperator();
		so.setJoin(Join.and.name());
		so.setKey(key);
		so.setOper(oper);
		so.setValue(value);
		opers = new ArrayList<SpecificationOperator>();
		opers.add(so);
	}

	public SimpleSpecificationBuilder() {
		opers = new ArrayList<SpecificationOperator>();
	}

	/**
	 * 完成条件的添加
	 * 
	 * @time 2018年4月10日 下午5:58:05.</br>
	 * @version V1.0</br>
	 * @param key
	 * @param oper
	 * @param value
	 * @param join
	 * @return SimpleSpecificationBuilder构建好的查询 this，方便后续的链式调用</br>
	 */
	public SimpleSpecificationBuilder<T> add(String key, String oper, Object value, String join) {
		// System.out.println("key:"+key+","+"oper:"+oper+"join:"+join);
		SpecificationOperator so = new SpecificationOperator();
		so.setKey(key);
		so.setValue(value);
		so.setOper(oper);
		so.setJoin(join);
		so.toString();
		opers.add(so);

		return this;
	}

	/**
	 * 添加or条件的重载
	 * 
	 * @time 2018年4月10日 下午5:58:26.</br>
	 * @version V1.0</br>
	 * @param key
	 * @param oper
	 * @param value
	 * @return this，方便后续的链式调用</br>
	 */
	public SimpleSpecificationBuilder<T> addOr(String key, String oper, Object value) {
		return this.add(key, oper, value, Join.or.name());
	}

	/**
	 * 添加and的条件
	 * 
	 * @time 2018年4月10日 下午5:58:37.</br>
	 * @version V1.0</br>
	 * @param key
	 * @param oper
	 * @param value
	 * @return this，方便后续的链式调用</br>
	 */
	public SimpleSpecificationBuilder<T> add(String key, String oper, Object value) {
		return this.add(key, oper, value, Join.and.name());
	}

	public Specification<T> generateSpecification() {
		Specification<T> specification = new SimpleSpecification<T>(opers);
		return specification;
	}
}