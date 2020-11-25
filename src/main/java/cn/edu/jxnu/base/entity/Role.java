/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.entity;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色表
 *
 * @author 梦境迷离
 * @version V1.0
 */
@Entity
@Table(name = "tb_role")
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseEntity {

    private static final long serialVersionUID = -1894163644285296223L;

    /** 角色id */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    /** 角色名称 */
    private String name;

    /** 角色key */
    private String roleKey;

    /** 角色状态,0：正常；1：禁用, */
    private Integer status;

    /** 角色描述 */
    private String description;

    /** 创建时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 角色-资源 多对多关系，设置级联删除，懒加载，中间表tb_role_resource，[role_id-resource_id] */
    @ManyToMany(
            cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_role_resource",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "resource_id")})
    private java.util.Set<Resource> resources;
}
