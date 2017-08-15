/*
 * @(#)BusinessTypeService.java
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
package com.sany.workflow.service;

import java.util.List;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.BusinessType;
import com.sany.workflow.entity.BusinessTypeCondition;
import com.sany.workflow.entity.TreeNode;

/**
 * 业务类别管理
 * @author caix3
 * @version 2012-4-17,v1.0
 */
public interface BusinessTypeService {

    /**
     * 不启用的业务类别
     */
    public static final Integer USEFLAG_UNUSE = 0;

    /**
     * 启用的业务类别
     */
    public static final Integer USEFLAG_USE = 1;

    /**
     * 全部业务类别
     */
    public static final Integer USEFLAG_ALL = 2;

    /**
     * 非递归调用，即不获取2层以上子节点
     */
    public static final Integer MODE_UNRECURSION = 0;

    /**
     * 递归调用，获取所有子节点
     */
    public static final Integer MODE_RECURSION = 1;

    /**
     * 获取业务类别
     * @return
     */
    public List<BusinessType> findList(BusinessTypeCondition condition);
    
    public boolean hasSonNodes(String parentID);
    
    public List<BusinessType> getSonNodes(String parentID);
    public List<BusinessType> getTopNodes();
    public boolean hasTopNodes();
    

    /**
     * 分页读取业务类别
     * @param offset
     * @param pagesize
     * @param condition
     * @return
     */
    public ListInfo findListPage(long offset, int pagesize, BusinessTypeCondition condition);

    /**
     * 获取单个业务类别
     * @param businessId
     * @return
     */
    public BusinessType findUniqueResult(String businessId);

    /**
     * 插入单个业务类别
     * @param businessType
     * @return
     */
    public int insert(BusinessType businessType);

    /**
     * 更新单个业务类别
     * @param businessType
     * @return
     */
    public int update(BusinessType businessType);

    /**
     * 删除单个业务类别
     * @param businessType
     * @return
     */
    public int delete(BusinessType businessType);

    /**
     * 批量删除业务类别
     * @param businessType
     * @return
     */
    public int batchDelete(List<BusinessType> businessTypeList);

    /**
     * 业务类型下拉树
     * @param parentId
     * @return
     */
	List<TreeNode> queryBusinessNode(String parentId);
}
