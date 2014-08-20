/*
 * @(#)ActivitiBusinessTypeAction.java
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
package com.sany.workflow.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import bboss.org.jgroups.util.UUID;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.BusinessType;
import com.sany.workflow.entity.BusinessTypeCondition;
import com.sany.workflow.entity.TreeNode;
import com.sany.workflow.service.BusinessTypeService;

/**
 * business type action
 * @author caix3
 * @version 2012-4-20,v1.0
 *          2012-4-23,v2.0
 */
public class ActivitiBusinessTypeAction {

    private static Logger logger = Logger.getLogger(ActivitiBusinessTypeAction.class);

    private BusinessTypeService businessTypeService;

    /**
     * 主页面
     * @return
     */
    public String index() {
        return "path:index";
    }

    /**
     * 分页查询列表
     * @param offset
     * @param pagesize
     * @return
     */
    public String queryListPage(@PagerParam(name = PagerParam.OFFSET) long offset,
            @PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
            BusinessTypeCondition condition, ModelMap model) {

    	
        ListInfo listInfo = businessTypeService.findListPage(offset, pagesize, condition);
        model.addAttribute("businessTypeList", listInfo);
        return "path:queryListPage";
    }

    /**
     * 删除单个业务类别
     * @param condition
     * @param model
     * @return
     */
	public @ResponseBody
	String delete(BusinessTypeCondition condition, ModelMap model) {

		String responseBody = "";
		try {
			BusinessType businessType = new BusinessType();
			businessType.setBusinessId(condition.getBusinessId());

			int res = businessTypeService.delete(businessType);
			if (res == 0) {
				responseBody = "删除失败";
			} else if (res == 1) {
				responseBody = "删除成功";
			} else if (res == 2) {
				responseBody = "该业务类型存在子类型,不能删除";
			} else if (res == 3) {
				responseBody = "该业务类型与流程关联,不能删除";
			}
		} catch (Exception e) {
			logger.error("delete bussinessType error", e);
			responseBody = e.getMessage();
		}
		return responseBody;
	}

    /**
     * 批量删除
     * @param idString
     * @param model
     * @return
     */
    public @ResponseBody
    String batchDelete(String idString, ModelMap model) {

        String responseBody = "";
        try {
            String[] idList = idString.split(",");
            List<BusinessType> businessTypeList = new ArrayList<BusinessType>();
            for (String id : idList) {
                BusinessType temp = new BusinessType();
                temp.setBusinessId(id);
                businessTypeList.add(temp);
            }

            int res = businessTypeService.batchDelete(businessTypeList);
            if (res == 0) {
                responseBody = "删除失败";
            } else {
                responseBody = "删除成功";
            }
        } catch (Exception e) {
            logger.error("batch delte businessType error", e);
            responseBody = e.getMessage();
        }
        return responseBody;
    }

    /**
     * 新增
     * @param businessType
     * @param model
     * @return
     */
    public @ResponseBody
    String addType(BusinessType businessType, ModelMap model) {

        String responseBody = "";
        try {
        	businessType.setBusinessId(UUID.randomUUID().toString());
            int res = businessTypeService.insert(businessType);
            if (res == 0) {
                responseBody = "fail";
            } else {
//                responseBody = "添加成功";
                responseBody = "success";
            }
        } catch (Exception e) {
            logger.error("add businessType error", e);
            responseBody = "fali:"+e.getMessage();
        }
        return responseBody;
    }

    /**
     * 获取修改前资料
     * @param businessId
     * @param model
     * @return
     */
    public String queryDataModify(String businessId, ModelMap model) {

        BusinessType businessType = businessTypeService.findUniqueResult(businessId);
        model.addAttribute("businessType", businessType);
        return "path:queryDataModify";
    }

    /**
     * 修改
     * @param businessType
     * @param model
     * @return
     */
    public @ResponseBody
    String update(BusinessType businessType, ModelMap model) {

        String responseBody = "";
        try {
            int res = businessTypeService.update(businessType);
            if (res == 0) {
                responseBody = "fail";
            } else {
                responseBody = "success";
            }
        } catch (Exception e) {
            logger.error("update businessType error", e);
            responseBody = "fail:"+e.getMessage();
        }
        return responseBody;
    }
    
    public @ResponseBody (datatype = "json")
    List<TreeNode> showComboxBusinessTree(String id){
    	if(id==null||id.isEmpty()){
    		id="0";
    	}
    	return businessTypeService.queryBusinessNode(id);
    }
}
