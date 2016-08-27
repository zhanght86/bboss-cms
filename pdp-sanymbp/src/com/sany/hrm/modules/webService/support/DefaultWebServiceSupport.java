package com.sany.hrm.modules.webService.support;

import org.apache.commons.lang.StringUtils;

import com.sany.hrm.modules.webService.dto.MessageDto;
import com.sany.hrm.modules.webService.utils.WebServiceUtils;

/**
 * webService支持
 * 
 * @author gw_yaoht
 * @version 2012-8-13
 **/
public class DefaultWebServiceSupport implements WebServiceSupport {

	/**
	 * 操作名称
	 */
	private String operationName = "functionForString";

	/**
	 * 目标地址
	 */
	private String targetEndpointAddress = "http://10.0.15.231:8080/HRM/services/UniversalService";

	@Override
	public Object getCallForObject(String operationName, Object... args) throws Exception {
		operationName = StringUtils.defaultIfEmpty(operationName, this.operationName);

		return WebServiceUtils.getCallForObject(this.targetEndpointAddress, operationName, args);
	}

	@Override
	public <T1, T2> MessageDto<T1> getMessage(String warrantId, String functionId, MessageDto<T2> messageDto) throws Exception {
		return this.getMessage(warrantId, null, functionId, messageDto);
	}

	@Override
	public <T1, T2> MessageDto<T1> getMessage(String warrantId, String operationName, String functionId, MessageDto<T2> messageDto)
			throws Exception {
		operationName = StringUtils.defaultIfEmpty(operationName, this.operationName);

		return WebServiceUtils.getMessage(this.targetEndpointAddress, warrantId, operationName, functionId, messageDto);
	}

	/**
	 * 设置
	 * 
	 * @param operationName
	 *            操作名称
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	/**
	 * 设置
	 * 
	 * @param targetEndpointAddress
	 *            目标地址
	 */
	public void setTargetEndpointAddress(String targetEndpointAddress) {
		this.targetEndpointAddress = targetEndpointAddress;
	}
}