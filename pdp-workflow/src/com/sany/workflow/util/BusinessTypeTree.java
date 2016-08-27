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
package com.sany.workflow.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.sany.workflow.entity.BusinessType;
import com.sany.workflow.service.BusinessTypeService;

/**
 * 业务类别树
 * @author caix3
 * @version 2012-5-2,v1.0
 */
public class BusinessTypeTree extends COMTree {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(BusinessTypeTree.class);

    private BusinessTypeService businessTypeService;

    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        this.businessTypeService = org.frameworkset.web.servlet.support.WebApplicationContextUtils
                .getWebApplicationContext().getTBeanObject("workflow.businesstype.service", BusinessTypeService.class);
    }

    /**
     * 检查是否有子节点
     */
    public boolean hasSon(ITreeNode father) {

        boolean flag = false;
        try {
        	if(father.getId().equals("0"))
        	{
        		return businessTypeService.hasSonNodes(father.getId());
        	}
        	else
        	{
        		return businessTypeService.hasSonNodes(father.getId());
        	}

            
            
            
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
        	List<BusinessType> businessTypeList = null;
        	if(father.getId().equals("0"))
        	{
        		businessTypeList = businessTypeService.getSonNodes(father.getId());
        	}
        	else
        	{
        		businessTypeList = businessTypeService.getSonNodes(father.getId());
        	}
            if (businessTypeList != null) {

                for (BusinessType temp : businessTypeList) {
                    String businessTypeId = temp.getBusinessId();
                    String businessTypeName = temp.getBusinessName();
                    
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("nodeLink", "javascript:query('"+businessTypeId+"','"+WorkFlowConstant.BUSINESS_TYPE_BUSINESSTYPE+"','"+businessTypeName+"');");
                    addNode(father, businessTypeId, businessTypeName, "businessType", true, curLevel, (String) null,
                            (String) businessTypeId, (String) businessTypeId,map);
                }
            }

        } catch (Exception e) {
            logger.error("set business type tree son node error", e);
        }
        return false;
    }

   
}
