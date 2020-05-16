package com.example.service;

import com.example.pojo.TbUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.example.mapper.TbUserMapper;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Linmo
 * @create 2020/5/16 12:08
 */
@Service
public class TbUserService {

    @Resource
    private TbUserMapper tbUserMapper;

    public TbUser findUserByUsername(String userName) {
        Example example = new Example(TbUser.class);
        example.createCriteria().andEqualTo("username", userName);
        return tbUserMapper.selectByExample(example).get(0);
    }
    public void storeUser(TbUser tbUser){
        tbUserMapper.insertSelective(tbUser);
    }
}
