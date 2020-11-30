/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.entity;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

/**
 * 基类-----属性类型需要为引用类型，判断条件为null,原生类型为0
 *
 * @author 梦境迷离
 * @version V1.0
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = -250118731239275742L;
}
