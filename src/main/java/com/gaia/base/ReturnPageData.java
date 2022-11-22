package com.gaia.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页返回数据公共类
 *
 * @author XuJUn
 * @date 2020/1/2
 */
public class ReturnPageData {

    public static Map<String, Object> fillingData(long count, Object list) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("data", list);
        return map;
    }
}
