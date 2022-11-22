package com.estee.service;

import com.estee.entity.Main;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaia.vo.PageVo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xu Jun
 * @since 2021-09-22
 */
public interface IMainService extends IService<Main> {

    /**
     * 查询分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<FoxAdviceMain>
     */
    ArrayList<ArrayList<String>> getList(/*PageVo<Main> page*/) throws IOException;

    /**
     * 添加
     *
     * @param foxAdviceMain 
     * @return int
     */
    int add(Main foxAdviceMain);

    /**
     * 删除
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改
     *
     * @param foxAdviceMain 
     * @return int
     */
    int update(Main foxAdviceMain);

    /**
     * id查询数据
     *
     * @param id id
     * @return FoxAdviceMain
     */
    Main getById(Long id);
}
