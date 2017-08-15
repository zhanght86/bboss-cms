package com.frameworkset.platform.cms.docCommentManager;
/**
 * <p>
 * Title: CommentResult.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2012-9-6 下午3:32:55
 * @author biaoping.yin
 * @version 1.0.0
 */
public class CommentResult {
	private String msg;
	private String aduitSwitchFlag;
	private String error;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAduitSwitchFlag() {
		return aduitSwitchFlag;
	}
	public void setAduitSwitchFlag(String aduitSwitchFlag) {
		this.aduitSwitchFlag = aduitSwitchFlag;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
