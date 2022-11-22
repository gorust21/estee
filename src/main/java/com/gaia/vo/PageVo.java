package com.gaia.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageVo<T> {
    @ApiModelProperty(value = "分页编码")
    private Integer page;
    @ApiModelProperty(value = "每页数量")
    private Integer pageCount;
    @ApiModelProperty(value = "分页模块,1 首页 2 商城")
    private Integer type;
    @ApiModelProperty(value = "id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    @ApiModelProperty(value = "分类id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long categoryId;
    @ApiModelProperty(value = "是否当前 0 否 1是")
    private Byte current;
    private T t;
    
}
