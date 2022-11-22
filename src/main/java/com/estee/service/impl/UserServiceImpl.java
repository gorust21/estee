package com.estee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estee.dao.UserMapper;
import com.estee.entity.User;
import com.estee.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gaia.util.MD5Util;
import com.gaia.vo.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xu Jun
 * @since 2021-09-23
 */
@Log4j2
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Override
    public IPage<User> getList(PageVo<User> page){
        IPage<User> wherePage = new Page<>(page.getPage(), page.getPageCount());
        User where = new User();
        return baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(User foxUser){
        return baseMapper.insert(foxUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public User login(User foxUser) {
        foxUser.setPassword(MD5Util.encode(foxUser.getPassword(),"UTF-8"));
        QueryWrapper<User> query = Wrappers.query(foxUser).eq("username",foxUser.getUsername()).eq("password",
                foxUser.getPassword());
        foxUser = baseMapper.selectOne(query);
        foxUser.setLoginTime(LocalDateTime.now());
        baseMapper.updateById(foxUser);
        return foxUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(User foxUser){
        return baseMapper.updateById(foxUser);
    }

    @Override
    public User getById(Long id){
        return  baseMapper.selectById(id);
    }
}
