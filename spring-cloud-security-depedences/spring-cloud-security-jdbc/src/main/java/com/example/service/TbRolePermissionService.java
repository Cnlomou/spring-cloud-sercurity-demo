package com.example.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.mapper.TbRolePermissionMapper;
/**
 * @author Linmo
 * @create 2020/5/16 12:10
 */
@Service
public class TbRolePermissionService{

    @Resource
    private TbRolePermissionMapper tbRolePermissionMapper;

}
