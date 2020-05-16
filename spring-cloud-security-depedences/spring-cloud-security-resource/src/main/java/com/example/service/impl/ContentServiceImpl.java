package com.example.service.impl;

import com.example.pojo.Content;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.mapper.ContentMapper;
import com.example.service.ContentService;

import java.util.List;

/**
 * @author Linmo
 * @create 2020/5/16 13:45
 */
@Service
public class ContentServiceImpl implements ContentService{

    @Resource
    private ContentMapper contentMapper;

    @Override
    public List<Content> findAll() {
        return contentMapper.selectAll();
    }
}
