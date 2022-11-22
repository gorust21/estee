package com.estee.service;

import com.gaia.vo.PageVo;
import com.estee.entity.Doc;
import java.util.List;

public interface IDocService /*extends IService<Doc>*/{

    List<Doc> getList(PageVo<Doc> page);
    
}
