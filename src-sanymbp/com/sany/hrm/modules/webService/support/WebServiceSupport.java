package com.sany.hrm.modules.webService.support;

import com.sany.hrm.modules.webService.dto.MessageDto;

/**
 * webService支持
 * 
 * @author gw_yaoht
 * @version 2012-8-13
 **/
public interface WebServiceSupport {
	/**
	 * 调用
	 * 
	 * @param targetEndpointAddress
	 *            目标地址
	 * @param operationName
	 *            操作名称
	 * @param args
	 *            参数
	 * @return 返回值
	 * @throws Exception
	 *             异常
	 */
	Object getCallForObject(String operationName, Object... args) throws Exception;

	/**
	 * 获取
	 * 
	 * @param <T1>
	 *            返回类型
	 * @param <T2>
	 *            目标地址
	 * @param warrantId
	 *            凭证标识
	 * @param operationName
	 *            操作名称
	 * @param functionId
	 *            方法标识
	 * @param messageDto
	 *            输入参数
	 * @return 消息
	 * @throws Exception
	 */
	<T1, T2> MessageDto<T1> getMessage(String warrantId, String operationName, String functionId, MessageDto<T2> messageDto)
			throws Exception;

	/**
	 * 获取
	 * 
	 * @param <T1>
	 *            返回类型
	 * @param <T2>
	 *            目标地址
	 * @param warrantId
	 *            凭证标识
	 * @param functionId
	 *            方法标识
	 * @param messageDto
	 *            输入参数
	 * @return 消息
	 * @throws Exception
	 */
	<T1, T2> MessageDto<T1> getMessage(String warrantId, String functionId, MessageDto<T2> messageDto) throws Exception;
}
