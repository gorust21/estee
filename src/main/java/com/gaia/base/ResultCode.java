package com.gaia.base;

/**
 * ResultCode class
 *
 * @author XuJUn
 * @date 2020/1/2
 */
public enum ResultCode {
    //成功返回结果
    SERVICE_OK(200, "成功！"),
    //返回失败结果
    SERVICE_ERR(201, "失败"),
    NOT_DELETE(204, "包含下级，请先删除下级后再删除该节点。"),
    PROJECT_ID_ERR(206, "项目id错误!"),
    NOT_YOU_MSG(207, "您无权修改此信息"),
    HAVE_BEEN_USED(205, "信息已被使用,不能删除!"),
    //没有数据
    NOT_MSG(205, "查询无数据"),
    //服务异常
    SERVICE_EXCEPTION(203, "服务异常"),

    SUCCESS(200, "成功"),

    REPEAT(1101, "数据已存在"),
    ERROR_REQUEST(1400, "错误的请求"),
    UNAUTHORIZED(1401, "没有授权"),
    FORBIDDEN(1403, "没有权限访问"),
    VALIDATE_ERROR(1405,"参数错误"),
    AUTHORIZATION_ERROR(1406,"授权失败"),
    //用户相关
    USER_MISS(14030, "用户不存在"),
    ERROR_USERNAME(14031, "用户名错误"),
    ERROR_PASSWORD(14032, "密码错误"),
    LOGIN_ERROR(140321, "账号或密码错误"),
    TOKEN_EXPIRED(14033, "token失效"),
    TOKEN_ERROR(14037, "无效token"),
    GET_TOKEN_ERROR(14034, "token获取失败"),
    USER_FROZEN(14035, "用户已被禁用"),
    NOT_MEMBER(14036,"你还不是会员"),
    MALFORMED_TOKEN(14038,"非法访问"),

    NOT_FOND(1404, "页面不存在"),
    NOT_FOND_DATA(14041, "没有数据"),
    SERVER_ERROR(1500, "错误"),
    ERROR(9999, "error"),
    SEND_CODE_ERROR(3101, "验证码发送失败"),
    CODE_ERROR(3102, "验证码错误"),
    CODE_EXPIRED(3103, "验证码过期"),
    MOBILE_USED(3202, "该手机号已被使用"),

    FILE_UPLOAD_ERROR(10500, "文件上传失败"),
    FILE_ERROR(10501, "无效文件"),
    WECHAT_AUTH_ERROR(3201, "微信授权失败"),
    //任务相关
    NOT_PARENT_TASK(16001,"该任务不是主任务"),
    INVITE_CODE_ERROR(16002,"任务邀请码错误"),
    NO_ACCESS(16003,"无权限"),
    NO_CREATOR(16004,"你不是任务的创建者"),
    CAN_NOT_INVITE(16005,"该任务被设置为不可加入"),
    TASK_NOT_INEXISTENCE(16006,"任务不存在"),
    TASK_FINISH(16007,"任务已完成"),
    //附件相关
    TASK_NOT_FIND(17001,"任务不存在，请创建任务后上传附件"),
    //支付相关
    APPLE_VERIFY_FAILED(19001,"苹果内购校验失败"),
    APPLE_DEAL_LIST(19002,"当前交易不在交易列表中"),
    APPLE_NOT_FIND_DEAL_LIST(19003,"未能获取获取到交易列表"),
    APPLE_PAY_FAILED(19004,"支付失败，错误码"),
    //商品相关
    GOODS_ID_ERROR(19101,"商品id有误"),
    SPACE_ENOUGH(19102,"已使用空间超过购买空间"),
    SPACE_NOT_FIND(19103,"没有可用空间"),
    SPACE_DEFICIENCY(19104,"空间不足"),
    SPACE_EXPIRE(19105,"空间已过期"),
    //好友组
    USER_GROUP_DEL_ERR(20001,"不能删除自己"),

    ;


    private String msg;
    private int code;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
