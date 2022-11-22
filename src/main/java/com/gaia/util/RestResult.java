package com.gaia.util;

import java.util.Date;

public class RestResult {
	/**
	 * 请求是否成功
	 */
	private boolean success;

	/**
	 * 成功时返回的数据，失败时返回具体的异常信息
	 */
	private Object data;
	/**
	 * 请求失败返回的提示信息，给前端进行页面展示的信息
	 */
	private Object errorMessage;
	/**
	 * 服务器当前时间（添加该字段的原因是便于查找定位请求时间，因为实际开发过程中服务器时间可能跟本地时间不一致，加上这个时间戳便于日后定位）
	 */
	private Date currentTime;

	public RestResult() {
	}

	public RestResult(boolean success, Object data, Object errorMessage, Date currentTime) {
		super();
		this.success = success;
		this.data = data;
		this.errorMessage = errorMessage;
		this.currentTime = currentTime;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Object errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	

}