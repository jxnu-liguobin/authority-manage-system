package cn.edu.jxnu.base.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ztree树
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:42:40.
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ZtreeView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6237809780035784312L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 父id
	 */
	private Long pId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 是否展开
	 */
	private boolean open;
	/**
	 * 是否已点击，默认false
	 */
	private boolean checked = false;

	public ZtreeView(Long id, Long pId, String name, boolean open) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
	}

	/**
	 * @author	梦境迷离
	 * @time	上午9:10:20
	 * @version V1.0
	 *
	 */
	public ZtreeView() {
	}

	public void setpId(long l) {
		this.pId = l;
	}

}
