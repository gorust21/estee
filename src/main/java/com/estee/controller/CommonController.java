package com.estee.controller;

import com.gaia.base.ResponseStatus;
import com.gaia.base.ResponseZero;
import com.gaia.util.RedisUtil;
import com.estee.entity.Vote;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.estee.entity.Doc;
import com.gaia.vo.PageVo;
import com.estee.service.IDocService;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.StringJoiner;
import javax.annotation.Resource;

@Log4j2
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${file.imgUrl}")
    private String imgUrl;

    @Value("${file.path}")
    private String path;    

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource 
    private IDocService docService;

    // 添加数据到redis
    @PostMapping("/redis/addstring")
    public String addToRedis(String name, String value) {

//        ValueOperations valueOperations = redisTemplate.opsForValue();
//
//        valueOperations.set(name, value);
        redisUtil.set(name,value);
        return "向redis添加string类型的数据";
    }

    @ApiOperation(value = "获取投票总数")
    @GetMapping("/getVote")
    public ResponseZero getData(String key) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object v = valueOperations.get(key);
        return ResponseZero.getResponse(ResponseStatus.SUCCESS,v);
    }


    @ApiOperation(value = "投票")
    @PostMapping("add")
    public ResponseZero add(@RequestBody Vote vote){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object obj = valueOperations.get(vote.getName());
        if(obj!=null) {
            valueOperations.set(vote.getName(), String.valueOf(Integer.parseInt(obj.toString())+1));
        }else{
            valueOperations.set(vote.getName(), "1");
        }
        return ResponseZero.getResponse(ResponseStatus.SUCCESS,valueOperations.get(vote.getName()));
    }

    @ApiImplicitParams({ @ApiImplicitParam(name = "files", value = "文件数组") })
    @ApiOperation(value = "文件上传")
    @PostMapping("/uploadFiles")
    @ResponseBody
    public ResponseZero uploadFiles(@RequestParam(value="files") MultipartFile[] files,HttpServletRequest request) throws IOException {
        StringJoiner sj = new StringJoiner(",");
        log.info(files.length);

        for (MultipartFile file : files) {
            String originFileName = file.getOriginalFilename();
            log.info(path +  originFileName);
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            File localFile = new File(path, fileName);
            log.info(localFile.getAbsolutePath());
            file.transferTo(localFile);
            log.info(request.getRequestURI());
            sj.add(imgUrl + fileName);
        }      
        return ResponseZero.getResponse(ResponseStatus.SUCCESS,sj.toString());
    }  
    
    @ApiOperation(value = "根据分页查询数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码"),
        @ApiImplicitParam(name = "pageCount", value = "每页条数")
    })
    @PostMapping("getFiles")
    public ResponseZero getFiles(@RequestBody PageVo<Doc> page){
        try{
            return ResponseZero.getResponse(ResponseStatus.SUCCESS,docService.getList(page));
        }catch (Exception e) {
            log.info(e.getMessage());
            return ResponseZero.getResponse(ResponseStatus.ERROR, e.getMessage());
        }
    }  
    
    @RequestMapping("/download")
    public void downloadFiles(@RequestParam("file") String downUrl, HttpServletRequest request, HttpServletResponse response){
      OutputStream outputStream=null;
      InputStream inputStream=null;
      BufferedInputStream bufferedInputStream=null;
      byte[] bytes=new byte[1024];
      File file = new File(downUrl);
      String fileName = file.getName();
      log.info("本次下载的文件为" + downUrl);
      // 获取输出流
      try {
          // StandardCharsets.ISO_8859_1 *=UTF-8'
          // response.setHeader("Content-Disposition", "attachment;filename=" +  new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
          response.setHeader("Content-Disposition", "attachment;filename=" +  URLEncoder.encode(fileName, "UTF-8"));
          // 以流的形式返回文件
          response.setContentType("application/octet-stream;charset=utf-8");
          inputStream = new FileInputStream(file);
          bufferedInputStream = new BufferedInputStream(inputStream);
          outputStream = response.getOutputStream();
          int i = bufferedInputStream.read(bytes);
          while (i!=-1){
              outputStream.write(bytes,0,i);
              i = bufferedInputStream.read(bytes);
          }
      } catch (IOException e) {
          e.printStackTrace();
      }finally {
          try {
              if (inputStream!=null){
                  inputStream.close();
              }
              if (outputStream!=null){
                  outputStream.close();
              }
              if (bufferedInputStream!=null){
                  bufferedInputStream.close();
              }
          } catch (IOException e) {
              log.info(e.getMessage());
          }
    
      }
    }
  
}
