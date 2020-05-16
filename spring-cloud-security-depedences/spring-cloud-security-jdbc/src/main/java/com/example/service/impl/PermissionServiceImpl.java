package com.example.service.impl;

import com.example.pojo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.example.mapper.PermissionMapper;
import com.example.service.PermissionService;

import java.util.List;

/**
 * @author Linmo
 * @create 2020/5/16 12:20
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> findPermissionByUserId(Long userId) {
        return permissionMapper.findPermissionByUserId(userId);
    }
}
