package com.example.mapper;

import com.example.pojo.Permission;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Linmo
 * @create 2020/5/16 12:20
 */
public interface PermissionMapper extends Mapper<Permission> {
    List<Permission> findPermissionByUserId(Long userId);
}