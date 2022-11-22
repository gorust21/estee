package com.gaia.base;


/**
 * @Author: Xu Jun
 * @Date: 2020/2/26 12:07
 */

public enum ResponseStatus {
    SUCCESS(200,"success"),
    FAIL(10001,"fail"),
    ERROR(10002,"service error"),
    REJECT(10003,"no token, login required"),
    VOID(10004,"please input area"),
    BAD(400,"bad");

    private int code;
    private String msg;

    private ResponseStatus(int code,String msg){
         this.code = code;
         this.msg=msg;
    }


    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
