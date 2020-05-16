package com.example.pojo;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Linmo
 * @create 2020/5/16 13:46
 */
/**
    * 内容分类
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_content_category")
public class ContentCategory {
    /**
     * 类目ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 父类目ID=0时，代表的是一级的类目
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 分类名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 状态。可选值:1(正常),2(删除)
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * 该类目是否为父类目，1为true，0为false
     */
    @Column(name = "is_parent")
    private Boolean isParent;

    /**
     * 创建时间
     */
    @Column(name = "created")
    private Date created;

    /**
     * 创建时间
     */
    @Column(name = "updated")
    private Date updated;
}