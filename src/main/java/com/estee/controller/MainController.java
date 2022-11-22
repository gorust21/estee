package com.estee.controller;

import com.estee.entity.Main;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.*;
import com.estee.service.IMainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import com.gaia.base.ResponseZero;
import com.gaia.base.ResponseStatus;
import com.gaia.vo.PageVo;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xu Jun
 * @since 2022-11-22
 */
@Log4j2
@Api(tags = {""})
@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private IMainService iMainService;

    @ApiOperation(value = "添加")
    @PostMapping("add")
    public ResponseZero add(@RequestBody Main foxAdviceMain){
        try {
            int result = iMainService.add(foxAdviceMain);
            return ResponseZero.getResponse(result>=1?ResponseStatus.SUCCESS:ResponseStatus.FAIL);
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @PostMapping("delete/{id}")
    public ResponseZero delete(@PathVariable("id") Long id){
        try{
            int result =  iMainService.delete(id);
            return ResponseZero.getResponse(result>=1?ResponseStatus.SUCCESS:ResponseStatus.FAIL);
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "更新")
    @PostMapping("update")
    public ResponseZero update(@RequestBody Main foxAdviceMain){
        try{
            int result =  iMainService.update(foxAdviceMain);
            return ResponseZero.getResponse(result>=1?ResponseStatus.SUCCESS:ResponseStatus.FAIL);
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据分页查询数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码"),
        @ApiImplicitParam(name = "pageCount", value = "每页条数")
    })
    @PostMapping("getList")
    public ResponseZero getList(/*@RequestBody PageVo<Main> page*/){
        try{
            log.info(iMainService+"pppppppp");
            return ResponseZero.getResponse(ResponseStatus.SUCCESS,iMainService.getList());
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @PostMapping("getById/{id}")
    public ResponseZero getById(@PathVariable Long id){
        try{
            return ResponseZero.getResponse(ResponseStatus.SUCCESS,iMainService.getById(id));
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }
}
