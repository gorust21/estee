package com.estee.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gaia.vo.PageVo;
import com.estee.entity.Doc;
import com.estee.service.IDocService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommonServiceImpl  implements IDocService{

    @Value("${file.imgUrl}")
    private String imageUrl;

    @Value("${file.path}")
    private String path;      
    
    @Override
    public List<Doc> getList(PageVo<Doc> page){
       String[] fs = new File(path).list();
       List<Doc> arr = new ArrayList<>();
       for(String name:fs){
           Doc doc = new Doc();
           doc.setName(imageUrl+name);
           arr.add(doc);
       }
        return arr;
    }
    
}
