package com.sany.hrm.workflow.service;

import java.util.List;
import java.util.Map;

import com.sany.hrm.modules.webService.dto.MessageDto;

/**
 * 工作流
 * 
 * @author gw_yaoht
 * @version 2012-8-14
 **/
public class DefaultWorkflowService implements WorkflowService {
	/**
	 * WebService支持
	 */
//	private WebServiceSupport webServiceSupport;

	@Override
	public List<Map<String, Object>> findTask(String warrantId, int firstResult, int maxResults) throws Exception {
		MessageDto<String> messageDto = new MessageDto<String>();

		messageDto.setData("dimission_0,dimission_1");

//		MessageDto<List<Map<String, Object>>> returnMessageDto = this.webServiceSupport.getMessage(warrantId,
//				"com.sany.workflow.function.FindTaskFunction", messageDto);
//
//		if (StringUtils.isNotBlank(returnMessageDto.getMessage())) {
//			throw new Exception(returnMessageDto.getMessage());
//		}

//		return returnMessageDto.getData();
		List<Map<String, Object>> ret = null;
		return ret;
	}

	@Override
	public List<Map<String, Object>> findWorkflowLog(String warrantId, String taskId) throws Exception {
		MessageDto<String> messageDto = new MessageDto<String>();

		messageDto.setData(taskId);

//		MessageDto<List<Map<String, Object>>> returnMessageDto = this.webServiceSupport.getMessage(warrantId,
//				"com.sany.workflow.function.FindWorkflowLogFunction", messageDto);
//
//		if (StringUtils.isNotBlank(returnMessageDto.getMessage())) {
//			throw new Exception(returnMessageDto.getMessage());
//		}
//
//		return returnMessageDto.getData();
		List<Map<String, Object>> ret = null;
		return ret;
	}

	@Override
	public Integer readTaskCount(String warrantId) throws Exception {
		MessageDto<String> messageDto = new MessageDto<String>();

		messageDto.setData("dimission_0,dimission_1");

//		MessageDto<Integer> returnMessageDto = this.webServiceSupport.getMessage(warrantId,
//				"com.sany.workflow.function.FindTaskCountFunction", messageDto);
//
//		if (StringUtils.isNotBlank(returnMessageDto.getMessage())) {
//			throw new Exception(returnMessageDto.getMessage());
//		}
//
//		return returnMessageDto.getData();
//		List<Map<String, Object>> ret = null;
		return new Integer(10);
	}

//	/**
//	 * 设置
//	 * 
//	 * @param webServiceSupport
//	 *            WebService支持
//	 */
//	public void setWebServiceSupport(WebServiceSupport webServiceSupport) {
//		this.webServiceSupport = webServiceSupport;
//	}
}
