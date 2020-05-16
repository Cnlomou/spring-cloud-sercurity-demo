package com.example.service;

import com.example.pojo.Permission;

import java.util.List;

/**
 * @author Linmo
 * @create 2020/5/16 12:20
 */
public interface PermissionService{

    List<Permission> findPermissionByUserId(Long userId);
}
