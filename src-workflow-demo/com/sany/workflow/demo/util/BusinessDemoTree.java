/*
 * @(#)BusinessTypeTree.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.workflow.demo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.util.StringUtil;
import com.sany.workflow.demo.entity.BusinessDemoTreeEntity;
import com.sany.workflow.demo.service.BusinessDemoService;

/**
 * 业务类别树
 * 
 * @author caix3
 * @version 2012-5-2,v1.0
 */
public class BusinessDemoTree extends COMTree {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(BusinessDemoTree.class);

	private BusinessDemoService demoService;

	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		this.demoService = org.frameworkset.web.servlet.support.WebApplicationContextUtils
				.getWebApplicationContext().getTBeanObject(
						"workflow.demo.demoService", BusinessDemoService.class);
	}

	/**
	 * 检查是否有子节点
	 */
	public boolean hasSon(ITreeNode father) {

		boolean flag = false;
		try {
			if (father.getType().equals("process"))
				return false;
			else
				return demoService.hasSonNodes(father.getId());

		} catch (Exception e) {
			logger.error("business type tree check hasSon error", e);
		}
		return flag;
	}

	/**
	 * 设置子节点
	 */
	public boolean setSon(ITreeNode father, int curLevel) {

		try {
			List<BusinessDemoTreeEntity> businessDemoList = demoService
					.getSonNodes(father.getId());

			if (businessDemoList != null) {

				for (BusinessDemoTreeEntity temp : businessDemoList) {
					String nodeId = temp.getNodeId();
					String nodeName = temp.getNodeName();

					String processname = temp.getProcessname();
					String processkey = temp.getProcesskey();

					Map<String,String> map = new HashMap<String,String>();
					if (StringUtil.isNotEmpty(processkey)) {
						addNode(father, processkey, processname + "(" + processkey
								+ ")", "process", true, curLevel,
								(String) null, (String) processkey,
								(String) processkey, map);
					} else {
						addNode(father, nodeId, nodeName, "business", true,
								curLevel, (String) null, (String) null,
								(String) null, map);
					}

				}
			}

		} catch (Exception e) {
			logger.error("set business type tree son node error", e);
		}
		return false;
	}
}
