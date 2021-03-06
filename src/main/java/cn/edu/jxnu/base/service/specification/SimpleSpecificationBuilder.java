/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service.specification;

import cn.edu.jxnu.base.service.specification.SpecificationOperator.Join;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * 查询构建类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public class SimpleSpecificationBuilder<T> {

  /** 条件列表 */
  private List<SpecificationOperator> opers;

  /**
   * 构造函数，初始化的条件是and
   *
   * @param key key
   * @param oper oper
   * @param value value
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
   * @param key key
   * @param oper oper
   * @param value value
   * @param join join
   * @return SimpleSpecificationBuilder构建好的查询 this，方便后续的链式调用
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
   * @param key key
   * @param oper oper
   * @param value value
   * @return this，方便后续的链式调用
   */
  public SimpleSpecificationBuilder<T> addOr(String key, String oper, Object value) {
    return this.add(key, oper, value, Join.or.name());
  }

  /**
   * 添加and的条件
   *
   * @param key key
   * @param oper oper
   * @param value value
   * @return this，方便后续的链式调用
   */
  public SimpleSpecificationBuilder<T> add(String key, String oper, Object value) {
    return this.add(key, oper, value, Join.and.name());
  }

  public Specification<T> generateSpecification() {
    Specification<T> specification = new SimpleSpecification<T>(opers);
    return specification;
  }
}
