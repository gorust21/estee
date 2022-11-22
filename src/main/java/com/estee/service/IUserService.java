package com.estee.service;

import com.estee.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaia.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xu Jun
 * @since 2021-09-23
 */
public interface IUserService extends IService<User> {

    /**
     * 查询分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<FoxUser>
     */
    IPage<User> getList(PageVo<User> page);

    /**
     * 添加
     *
     * @param foxUser 
     * @return int
     */
    int add(User foxUser);

    /**
     * 删除
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 登录
     * @param sysUser
     * @return
     */
    User login(User foxUser);

    /**
     * 修改
     *
     * @param foxUser 
     * @return int
     */
    int update(User foxUser);

    /**
     * id查询数据
     *
     * @param id id
     * @return FoxUser
     */
    User getById(Long id);
}
