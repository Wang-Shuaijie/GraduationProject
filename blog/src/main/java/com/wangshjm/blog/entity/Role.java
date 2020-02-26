package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色表实体
 */
@Data
@NoArgsConstructor
@Table(name = "tb_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //角色名称
    private String roleName;
    //角色值
    private String roleValue;

    //角色是否可用
    private String enabled;
    //备注
    private String remark;

}