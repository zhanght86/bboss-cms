/*
 * @(#)BusinessTypeManager.java
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
package com.sany.workflow.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.BusinessType;
import com.sany.workflow.entity.BusinessTypeCondition;
import com.sany.workflow.entity.TreeNode;
import com.sany.workflow.service.BusinessTypeService;

/**
 * 业务类别管理
 * @author caix3
 * @version 2012-4-17,v1.0
 */
public class BusinessTypeServiceImpl implements BusinessTypeService {

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

    private static Logger logger = Logger.getLogger(BusinessTypeServiceImpl.class);

    private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

    /**
     * 获取业务类别,主要用于获取该业务类别的子类别
     * @return
     */
    public List<BusinessType> findList(BusinessTypeCondition condition) {

        List<BusinessType> businessList = null;
        try {
            businessList = executor.queryListBean(BusinessType.class, "selectBusinessList", condition);
        } catch (Exception e) {
            logger.error(e);
        }
        return businessList;
    }

    /**
     * 分页获取业务类别
     */
    public ListInfo findListPage(long offset, int pagesize, BusinessTypeCondition condition) {

        ListInfo listInfo = null;
        try {
        	if (condition.getBusinessCode() != null && !condition.getBusinessCode().equals("")) {
                condition.setBusinessCode("%" + condition.getBusinessCode() + "%");
            }
            if (condition.getBusinessName() != null && !condition.getBusinessName().equals("")) {
                condition.setBusinessName("%" + condition.getBusinessName() + "%");
            }
            listInfo = executor
                    .queryListInfoBean(BusinessType.class, "selectBusinessList", offset, pagesize, condition);
        } catch (Exception e) {
            logger.error(e);
        }

        return listInfo;
    }

    /**
     * 获取单个业务类别
     * @param businessId
     * @return
     */
    public BusinessType findUniqueResult(String businessId) {

        BusinessType businessType = null;
        try {
            businessType = new BusinessType();
            businessType.setBusinessId(businessId);
            businessType = executor.queryObjectBean(BusinessType.class, "selectBusinessList", businessType);
        } catch (Exception e) {
            logger.error(e);
        }
        return businessType;
    }

    /**
     * 插入单个业务类别
     * @param businessType
     * @return
     */
    public int insert(BusinessType businessType) {

        int result = 0;
        try {
        	if(businessType.getParentId().isEmpty()){
        		businessType.setParentId("0");
        	}
            executor.insertBean("insertBusinessType", businessType);
            result = 1;
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    /**
     * 更新单个业务类别
     * @param businessType
     * @return
     */
    public int update(BusinessType businessType) {

        int result = 0;
        try {
            System.out.println(businessType.getUseFlag());
            executor.updateBean("updateBusinessType", businessType);
            result = 1;
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    /**
     * 删除单个业务类别
     * @param businessType
     * @return
     */
	public int delete(BusinessType businessType) {
		int result = 0;
		try {

			// 查看当前类别是否有子节点
			HashMap sonMap = executor.queryObject(HashMap.class,
					"querySonBusiness", businessType.getBusinessId());

			if (sonMap == null) {
				// 查看当前类别是否与流程关联
				HashMap proMap = executor.queryObject(HashMap.class,
						"queryProBusinesstype", businessType.getBusinessId());

				if (proMap == null) {
					executor.deleteBean("deleteBusinessType", businessType);
					result = 1;
				} else {
					result = 3;
				}
			} else {
				result = 2;
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

    /**
     * 批量删除业务类别
     * @param businessType
     * @return
     */
    public int batchDelete(List<BusinessType> businessTypeList) {
        int result = 0;
        try {
            executor.deleteBeans("deleteBusinessType", businessTypeList);
            result = 1;
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }
    
    @Override
    public List<TreeNode> queryBusinessNode(String parentId){
    	try {
			return executor.queryList(TreeNode.class, "queryBusinessNode", parentId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    @Override
	public boolean hasSonNodes(String parentID) {
		try {
			int count = executor.queryObject(int.class, "hasSonNodes", parentID);
			return count > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<BusinessType> getSonNodes(String parentID) {
		try {
			return executor.queryList(BusinessType.class, "selectSonBusinessList", parentID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<BusinessType> getTopNodes() {
		
		try {
			return executor.queryList(BusinessType.class, "selectTopBusinessList");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean hasTopNodes() {
		try {
			int count = executor.queryObject(int.class, "hastopNodes");
			return count > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
