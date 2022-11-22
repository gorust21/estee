package com.estee.service.impl;

import com.estee.entity.Main;
import com.estee.dao.MainMapper;
import com.estee.service.IMainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gaia.util.ExcelData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.log4j.Log4j2;

import com.gaia.vo.PageVo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xu Jun
 * @since 2021-09-22
 */
@Log4j2
@Service
public class MainServiceImpl extends ServiceImpl<MainMapper, Main> implements IMainService {


    @Override
    public ArrayList<ArrayList<String>> getList(/*PageVo<Main> page*/) throws IOException {
//        IPage<Main> wherePage = null;
//        if(page==null){
//            wherePage = new Page<>(1, 500);
//        }else {
//            wherePage = new Page<>(page.getPage(), page.getPageCount());
//        }
//        File file = new File("src/main/resources/Mobile_Food_Facility_Permit.csv");
//        FileInputStream is = new FileInputStream(file);
//        List<List<String>> data = ExcelData.readExcelByInputStream(is);
        ArrayList<ArrayList<String>> data = ExcelData.CSV2Array("src/main/resources/Mobile_Food_Facility_Permit.csv");
        log.info("---------------"+data);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Main main){
        return baseMapper.insert(main);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Main main){
        return baseMapper.updateById(main);
    }

    @Override
    public Main getById(Long id){
        return  baseMapper.selectById(id);
    }
}
