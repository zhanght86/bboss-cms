package com.sany.hrm.personnel.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sany.hrm.modules.webService.dto.MessageDto;
import com.sany.hrm.modules.webService.support.WebServiceSupport;
import com.sany.hrm.workflow.dto.WfNodeDto;

/**
 * 
 * 
 * @author gw_yaoht
 * @version 2012-8-15
 **/
public class DefaultPersonnelService implements PersonnelService {
	/**
	 * WebService支持
	 */
	private WebServiceSupport webServiceSupport;

	@Override
	public List<WfNodeDto> findDimissionTaskBackNode(String warrantId, String taskId) throws Exception {
		MessageDto<String> messageDto = new MessageDto<String>();

		messageDto.setData(taskId);

		MessageDto<List<WfNodeDto>> returnMessageDto = this.webServiceSupport.getMessage(warrantId,
				"com.sany.pensonnel.function.FindDimissionTaskBackNodeFunction", messageDto);

		if (StringUtils.isNotBlank(returnMessageDto.getMessage())) {
			throw new Exception(returnMessageDto.getMessage());
		}

		return returnMessageDto.getData();
	}

	@Override
	public Map<String, Object> getDimissionTask(String warrantId, String taskId) throws Exception {
		MessageDto<String> messageDto = new MessageDto<String>();

		messageDto.setData(taskId);

		MessageDto<Map<String, Object>> returnMessageDto = this.webServiceSupport.getMessage(warrantId,
				"com.sany.pensonnel.function.GetDimissionTaskFunction", messageDto);

		if (StringUtils.isNotBlank(returnMessageDto.getMessage())) {
			throw new Exception(returnMessageDto.getMessage());
		}

		return returnMessageDto.getData();
	}

	/**
	 * 设置
	 * 
	 * @param webServiceSupport
	 *            WebService支持
	 */
	public void setWebServiceSupport(WebServiceSupport webServiceSupport) {
		this.webServiceSupport = webServiceSupport;
	}

	@Override
	public String submitDimissionTask(String warrantId, Map<String, Object> model) throws Exception {
		MessageDto<Map<String, Object>> messageDto = new MessageDto<Map<String, Object>>();

		messageDto.setData(model);

		MessageDto<String> returnMessageDto = this.webServiceSupport.getMessage(warrantId,
				"com.sany.pensonnel.function.SubmitDimissionTaskFunction", messageDto);

		if (StringUtils.isNotBlank(returnMessageDto.getMessage())) {
			throw new Exception(returnMessageDto.getMessage());
		}

		return returnMessageDto.getData();
	}
}
