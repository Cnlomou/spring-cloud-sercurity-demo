package com.example.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.mapper.ContentCategoryMapper;
import com.example.service.ContentCategoryService;
/**
 * @author Linmo
 * @create 2020/5/16 13:46
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

    @Resource
    private ContentCategoryMapper contentCategoryMapper;

}
