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
    * 用户角色表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user_role")
public class TbUserRole {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 用户 ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 角色 ID
     */
    @Column(name = "role_id")
    private Long roleId;
}