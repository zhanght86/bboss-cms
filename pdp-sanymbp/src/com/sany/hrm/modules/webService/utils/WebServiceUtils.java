package com.sany.hrm.modules.webService.utils;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.commons.lang.StringUtils;
import org.frameworkset.json.JsonTypeReference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.frameworkset.util.StringUtil;
import com.sany.hrm.modules.webService.dto.MessageDto;

/**
 * WebService工具类
 * 
 * @author gw_yaoht
 * @version 2012-8-9
 **/
public class WebServiceUtils {
	/**
	 * 默认地址
	 */
	private static String defaultTargetEndpointAddress = "http://10.0.15.231:8080/HRM/services/UniversalService";

	/**
	 * 调用
	 * 
	 * @param targetEndpointAddress
	 *            目标地址
	 * @param operationName
	 *            操作名称
	 * @return webService call
	 * @throws Exception
	 *             异常
	 */
	public static Call getCall(String targetEndpointAddress, String operationName) throws Exception {
		targetEndpointAddress = StringUtils.defaultIfEmpty(targetEndpointAddress, WebServiceUtils.defaultTargetEndpointAddress);
		
		Service service = new Service();
		
		Call call = (Call) service.createCall();

		call.setTargetEndpointAddress(targetEndpointAddress);

		call.setOperationName(operationName);
		
		SOAPService soapService = new SOAPService();
		
		soapService.setName(operationName);
		
		call.setSOAPService(soapService);

		return call;
	}

	/**
	 * 调用
	 * 
	 * @param warrantId
	 *            凭证标识
	 * @param operationName
	 *            操作名称
	 * @return webService call
	 * @throws Exception
	 *             异常
	 */
	public static Call getCallExt(String warrantId, String operationName) throws Exception {
		return WebServiceUtils.getCallExt(WebServiceUtils.defaultTargetEndpointAddress, warrantId, operationName);
	}

	/**
	 * 调用
	 * 
	 * @param targetEndpointAddress
	 *            目标地址
	 * @param warrantId
	 *            凭证标识
	 * @param operationName
	 *            操作名称
	 * @return webService call
	 * @throws Exception
	 *             异常
	 */
	public static Call getCallExt(String targetEndpointAddress, String warrantId, String operationName) throws Exception {
		Call call = WebServiceUtils.getCall(targetEndpointAddress, operationName);

		call.getMessageContext().setUsername(warrantId);

		return call;
	}

	/**
	 * 调用
	 * 
	 * @param operationName
	 *            操作名称
	 * @param args
	 *            参数
	 * @return 返回值
	 * @throws Exception
	 *             异常
	 */
	public static Object getCallForObject(String operationName, Object[] args) throws Exception {
		return WebServiceUtils.getCallForObject(null, operationName, args);
	}

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
	public static Object getCallForObject(String targetEndpointAddress, String operationName, Object... args) throws Exception {
		Call call = WebServiceUtils.getCall(targetEndpointAddress, operationName);

		return call.invoke(args);
	}

	/**
	 * 获取
	 * 
	 * @param targetEndpointAddress
	 *            目标地址
	 * @param operationName
	 *            操作名称
	 * @param functionId
	 *            方法标识
	 * @param args
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public static <T1, T2> MessageDto<T1> getMessage(String targetEndpointAddress, String operationName, String functionId,
			Object... args) throws Exception {
		String returnJson = (String) WebServiceUtils.getCallForObject(targetEndpointAddress, operationName, args);

		if (StringUtils.isBlank(returnJson)) {
			return new MessageDto<T1>();
		}

		MessageDto<T1> returnMessageDto = (MessageDto<T1>) StringUtil.json2ObjectWithType(returnJson, new JsonTypeReference<MessageDto<T1>>());

		return returnMessageDto;
	}

	/**
	 * 获取
	 * 
	 * @param <T1>
	 *            返回类型
	 * @param <T2>
	 *            输入值类型
	 * @param targetEndpointAddress
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
	public static <T1, T2> MessageDto<T1> getMessage(String targetEndpointAddress, String warrantId, String operationName,
			String functionId, MessageDto<T2> messageDto) throws Exception {
		Call call = WebServiceUtils.getCallExt(targetEndpointAddress, warrantId, operationName);

		String json = StringUtil.object2json(messageDto);

		String returnJson = (String) call.invoke(new Object[] { functionId, json });

		if (StringUtils.isBlank(returnJson)) {
			return new MessageDto<T1>();
		}

		MessageDto<T1> returnMessageDto = (MessageDto<T1>) StringUtil.json2ObjectWithType(returnJson, new JsonTypeReference<MessageDto<T1>>());

		return returnMessageDto;
	}

	/**
	 * 设置
	 * 
	 * @param defaultTargetEndpointAddress
	 *            默认地址
	 */
	public static void setDefaultTargetEndpointAddress(String defaultTargetEndpointAddress) {
		WebServiceUtils.defaultTargetEndpointAddress = StringUtils.defaultIfEmpty(defaultTargetEndpointAddress,
				WebServiceUtils.defaultTargetEndpointAddress);
	}
}