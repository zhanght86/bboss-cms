package com.sany.hrm.modules.webService.dto;

/**
 * 消息交互
 * 
 * @author gw_yaoht
 * @version 2012-8-9
 **/
public class MessageDto<T> implements java.io.Serializable {

	/**
	 * 系列化值
	 */
	private static final long serialVersionUID = 817019663116511822L;

	/**
	 * 数据
	 */
	private T data;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 获取
	 * 
	 * @return 数据
	 */
	public T getData() {
		return this.data;
	}

	/**
	 * 获取
	 * 
	 * @return 消息
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * 设置
	 * 
	 * @param data
	 *            数据
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * 设置
	 * 
	 * @param message
	 *            消息
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
