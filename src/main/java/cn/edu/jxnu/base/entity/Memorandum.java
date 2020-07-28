package cn.edu.jxnu.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * 系统操作备忘录
 * 
 * 记录信息
 * 
 * @author: 梦境迷离
 * @version 1.0
 * @time 2018年5月1日
 */

@Entity
@Table(name = "tb_memorandum")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString /** 添加toString测试用. */
public class Memorandum extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Memorandum(int id, Date time, String userCode, String userName, String resourceName) {
		this.id = id;
		this.time = time;
		this.userCode = userCode;
		this.userName = userName;
		this.resourceName = resourceName;
	}

	public Memorandum() {
	}

	/**
	 * id
	 */
	@Id
	@Column(name = "id", nullable = false)
	private Integer id;

	/** 操作时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date time;

	/** 操作人账号 */
	private String userCode;

	/** 操作人姓名 */
	private String userName;

	/** 操作名称. */
	private String resourceName;

}
