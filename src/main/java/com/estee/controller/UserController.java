package com.estee.controller;

import com.gaia.base.ResponseStatus;
import com.gaia.base.ResponseZero;
import com.gaia.vo.PageVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import com.estee.service.IUserService;
import com.estee.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("index")
    public String index(){
        return "hello idea tool";
    }

    @ApiOperation(value = "添加")
    @PostMapping("add")
    public ResponseZero add(@RequestBody User foxUser){
        try {
            int result = iUserService.add(foxUser);
            return ResponseZero.getResponse(result>=1? ResponseStatus.SUCCESS:ResponseStatus.FAIL);
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @PostMapping("delete/{id}")
    public ResponseZero delete(@PathVariable("id") Long id){
        try{
            int result =  iUserService.delete(id);
            return ResponseZero.getResponse(result>=1?ResponseStatus.SUCCESS:ResponseStatus.FAIL);
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "更新")
    @PostMapping("update")
    public ResponseZero update(@RequestBody User foxUser){
        try{
            int result =  iUserService.update(foxUser);
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
    public ResponseZero getList(@RequestBody PageVo<User> page){
        try{
            return ResponseZero.getResponse(ResponseStatus.SUCCESS, iUserService.getList(page));
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @PostMapping("getById/{id}")
    public ResponseZero getById(@PathVariable Long id){
        try{
            return ResponseZero.getResponse(ResponseStatus.SUCCESS, iUserService.getById(id));
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public ResponseZero login(@RequestBody User sysUser)throws Exception{
        try{
            log.info("login user:"+sysUser);
            User result = iUserService.login(sysUser);
            log.info("result:"+result);
            if(result!=null){
                return ResponseZero.getResponse(result);
            }
            return ResponseZero.getResponse(ResponseStatus.VOID);
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }
}
