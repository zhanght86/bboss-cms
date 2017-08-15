/*
 * @(#)ParamsAction.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package org.frameworkset.util;

import java.util.List;

import org.frameworkset.util.ParamsHandler.Param;
import org.frameworkset.util.ParamsHandler.Params;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

/**
 * @author gw_hel
 * 参数设置
 */
public class ParamsAction {

	/**
	 * 获取参数列表
	 * @param paramId 参数ID
	 * @param paramType 参数类型
	 * @param handler 操作类别
	 * @param model 作用域模型
	 * @return String
	 */
	public String showParams(String paramId, String paramType, String handler, ModelMap model) {
		ParamsHandler phandler = ParamsHandler.getParamsHandler(handler);
		if(phandler == null)
			return "path:showParams";
		List<Param> paramsList = phandler._getParams(paramId, paramType).getParams();

		model.addAttribute("paramsList", paramsList);
		model.addAttribute("paramId", paramId);
		model.addAttribute("paramType", paramType);
		model.addAttribute("handler", handler);

		return "path:showParams";
	}

	/**
	 * 保存参数列表
	 * @param params 参数列表
	 * @param handler 操作类别
	 * @return String
	 */
	public @ResponseBody
	String saveParams(List<Param> paramsList, String nodeId,String nodeType,String handler) {
		Params params = new Params();
		params.setParams(paramsList);
		params.setParamId(nodeId);
		params.setParamType(nodeType);
		return ParamsHandler.getParamsHandler(handler).saveParams(params) ? "success" : "fail";
	}
}
