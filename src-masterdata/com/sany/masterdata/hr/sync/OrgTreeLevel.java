/*
 * @(#)OrgTreeLevel.java
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
package com.sany.masterdata.hr.sync;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.masterdata.hr.dao.TdSmOrganizationDao;
import com.sany.masterdata.hr.entity.TdSmOrganization;

/**
 * 构建组织机构树层级
 * @author caix3 
 * @version 2012-3-31,v1.0 
 */
public class OrgTreeLevel {

    private static final String TREE_BASE = "0";
    
    private static final String CUT_UP = "|";
    
    private static Logger logger = Logger.getLogger(OrgTreeLevel.class);
    
    /**
     * 构建组织机构树层级
     */
    public static void run() {
        
        //获取组织机构数据并转换为map
        TdSmOrganizationDao dao = new TdSmOrganizationDao();
        List<TdSmOrganization> orgList = dao.findOrgList(); 
        LinkedHashMap<String, TdSmOrganization> orgMap = new LinkedHashMap<String, TdSmOrganization>();
        for (TdSmOrganization temp : orgList) {
            orgMap.put(temp.getOrgId(), temp);
        }

        //构建层级关系
        for (String temp : orgMap.keySet()) {
            try {
                getTreeLevel(orgMap, temp);
            } catch(Exception e) {
                logger.error("find [" + orgMap.get(temp).getOrgId() + "] tree level", e);
            }
        }
        
        //回写数据库
        TransactionManager tm = new TransactionManager();
        PreparedDBUtil pre = new PreparedDBUtil();
        try {
            tm.begin(TransactionManager.RW_TRANSACTION);
            pre.setBatchOptimize(true);
            pre.preparedUpdate("update td_sm_organization set org_tree_level=? where org_id=?");
            
            for (String temp : orgMap.keySet()) {
                pre.setString(1, orgMap.get(temp).getOrgTreeLevel());
                pre.setString(2, orgMap.get(temp).getOrgId());
                pre.addPreparedBatch();
            }
            
            pre.executePreparedBatch();
            tm.commit();
            
        } catch (Throwable e) {
            try {
                tm.rollback();
            } catch (RollbackException e1) {
                logger.error("transaction manager roll back error", e1);
            }
            logger.error("update organization tree level info error", e);
        }
        
    }

    /**
     * 递归求出父结构层级，并保存
     * @param temp
     */
    private static void getTreeLevel(LinkedHashMap<String, TdSmOrganization> orgMap, String key) {
        
        TdSmOrganization temp = orgMap.get(key);
        if(temp == null)
        {
        	return;
        }
        if (temp.getOrgTreeLevel() != null && temp.getOrgTreeLevel().equals("")) {
            return;
        } else if (temp.getParentId().equals(TREE_BASE)) {
            temp.setOrgTreeLevel(TREE_BASE + CUT_UP + temp.getOrgSn());
        } else {
            getTreeLevel(orgMap, temp.getParentId());
            if(orgMap.get(temp.getParentId()) != null)
            {
            	temp.setOrgTreeLevel(orgMap.get(temp.getParentId()).getOrgTreeLevel() + CUT_UP + temp.getOrgSn());
            }
            else
            {
            	temp.setOrgTreeLevel(TREE_BASE + CUT_UP + temp.getOrgSn());
            }
        }
        
    }
   
    
    
    public static void main(String[] args) {
        OrgTreeLevel.run();
    }
    
    
}
