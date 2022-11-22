package com.gaia.base;

import com.gaia.util.DateUtil;
import lombok.Data;

@Data
public class Response {
    private boolean success;
    private Integer code = 200;
    private String message = "success";
    private Object data;
    private String currentTime;

    public Response(boolean success, Integer code, String message, String currentTime, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.currentTime = currentTime;
        this.data = data;
    }

    public Response(boolean success, Integer code, String message, String currentTime) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.currentTime = currentTime;
    }

    public Response() {
    }

    public Response(boolean success) {
        this.success = success;
    }

    public Response(boolean success, Object o) {
        this.success = success;
        this.data = o;
    }

    public Response(boolean success, ResultCode apiCode) {
        this.success = success;
        this.code = apiCode.getCode();
        this.message = apiCode.getMsg();
    }

    public Response(boolean success, ResultCode apiCode, String message) {
        this.success = success;
        this.code = apiCode.getCode();
        this.message = message;
    }

    public Response(boolean success, ResultCode apiCode, Object o) {
        this.success = success;
        this.data = o;
        this.code = apiCode.getCode();
        this.message = apiCode.getMsg();
    }

    public static Response returnResponse() {
        return new Response(true, ResultCode.SERVICE_OK);
    }

    public static Response returnResponse(boolean success) {
        return new Response(success);
    }

    public static Response returnResponse(Object data) {
        return new Response(true, data);
    }

    public static Response returnResponse(boolean success, ResultCode apiCode) {
        return new Response(success, apiCode);
    }

    public static Response returnResponse(boolean success, ResultCode apiCode, String message) {
        return new Response(success, apiCode, message);
    }


    public static Response returnResponse(boolean success, ResultCode apiCode, Object data) {
        return new Response(success, apiCode, data);
    }

    public void failEnum(ResultCode resultCode) {
        this.setCode(resultCode.getCode());
        this.setMessage(resultCode.getMsg());
    }

    public void success(String reason) {
        this.setCode(200);
        this.setMessage(reason);
    }
    /////////////////////////////////////////////////////////////////////////////////

    //成功但不返回数据
    public static Response ok() {
        return new Response(
                true,
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMsg(),
                DateUtil.getTime());
    }

    //成功并返回数据
    public static Response ok(Object data) {
        return new Response(
                true,
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMsg(),
                DateUtil.getTime(),
                data);
    }

    //--------------------
    public static Response error(ResultCode apiCode) {
        return new Response(
                false,
                apiCode.getCode(),
                apiCode.getMsg(),
                DateUtil.getTime());
    }

    public static Response error(ResultCode apiCode, String str) {
        return new Response(
                false,
                apiCode.getCode(),
                apiCode.getMsg(),
                DateUtil.getTime(),
                str);
    }

}
