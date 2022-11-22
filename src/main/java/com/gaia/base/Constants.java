package com.gaia.base;

import com.gaia.util.CommonUtil;
import com.gaia.util.DateUtil;
import org.springframework.stereotype.Component;

@Component
public final class Constants {
	public final static String VERIFICATION_CODE = "vcode";  //验证码


	//畅客-找回密码验证码
	public final static String FIND_PASSWORD_CODE = "SMS_183745213";

	//畅客-登录验证码
	public final static String LOGIN_CODE = "SMS_183730332";

	/**
	 * OSS新闻
	 */
	public final static String NEWS_PATH = "news/"+ DateUtil.getDays()+"/";

	/**
	 * OSS头像目录
	 */
	public final static String IMAGE_PATH = "building/"+ DateUtil.getDays()+"/";

	/**
	 * OSS头像目录
	 */
	public final static String AVATAR_PATH = "avatar/"+ DateUtil.getDays()+"/";

	/**
	 * 任务封面OSS目录
	 */
	public final static String COVER_PATH = "cover/"+ DateUtil.getDays()+"/";
	/**
	 * OSS图片附件目录
	 */
	public final static String ATTACHMENT_PATH = "attachment/"+ DateUtil.getDays()+"/";

	/**
	 * OSS缩略图后缀
	 */
	public final static String resize ="-resize.jpg";

	/**
	 * OSS访问地址
	 */
	public final static String OSS_PATH = "https://oss.98ck.com/";
	public final static String OSS_PROCESS_PATH = "https://itask.oss-cn-shanghai.aliyuncs.com/";
	/*
	 * 获取OSS图片url
	 */
	//public final static String PIC_PATH = "http://" + CommonUtil.getIp() + ":32857" + "/file/getPicture/";
	//public final static String PIC_OSS_PATH = "http://" + CommonUtil.getIp() + ":32857" + "/file/getImage/";
	//public final static String PIC_PATH = "http://"+"itask.98ck.com"+"/file/getPicture/";
	/**
	 * 播放视频url
	 */
	public final static String VIDEO_PATH = "http://" + CommonUtil.getIp() + ":32857" + "/file/sendVideo/";
	//public final static String VIDEO_PATH = "http://"+"itask.98ck.com"+"/file/sendVideo/";




}
