package com.gaia.base;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: Xu Jun
 * @Date: 2020/2/26 9:25
 */
@Data
@ToString
public class ResponseZero<Code,Body> implements Serializable {
    private static final ResponseZero response = new ResponseZero<>();

    private Code code;
    private Body body;
    private long timestamp;

    private ResponseZero(){}

    private ResponseZero(Code code, Body body){
        this.code = code;
        this.body = body;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResponseZero getResponse(ResponseStatus status){
        response.code = status.getCode();
        response.body = status.getMsg();
        response.timestamp = System.currentTimeMillis();
        return response;
    }

    public static <T> ResponseZero getResponse(Object body){
        if(body!=null){
            response.code = ResponseStatus.SUCCESS.getCode();
        }else{
            response.code = ResponseStatus.FAIL.getCode();
        }
        response.body = body;
        response.timestamp = System.currentTimeMillis();
        return response;
    }

    public static <T> ResponseZero getResponse(int code,Object body){
        response.code = code;
        response.body = body;
        response.timestamp = System.currentTimeMillis();
        return response;
    }

    public static <T> ResponseZero getResponse(ResponseStatus status,Object body){
        response.code = status.getCode();
        response.body = body;
        response.timestamp = System.currentTimeMillis();
        return response;
    }
}
