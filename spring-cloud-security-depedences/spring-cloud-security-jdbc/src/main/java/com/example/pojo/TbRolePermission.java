package com.example.pojo;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Linmo
 * @create 2020/5/16 12:10
 */
/**
    * 角色权限表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_role_permission")
public class TbRolePermission {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 角色 ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限 ID
     */
    @Column(name = "permission_id")
    private Long permissionId;
}