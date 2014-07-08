/*
 * @(#)SyncOrgInfo.java
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Log;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.UserOrgParamManager;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;
import com.sany.greatwall.MdmService;
import com.sany.greatwall.domain.MdmOrgLeader;
import com.sany.masterdata.utils.MDPropertiesUtil;

/**
 * 同步机构数据
 * @author caix3
 * @since 2012-3-22
 */
public class SyncOrganizationInfo {

    private static final String SAVA_SQL = "insert into td_sm_organization (org_sn, org_name, parent_id, orgnumber, remark3, remark5, org_level, org_xzqm, org_id,orgLeader) values (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
     
    private static final String UPDATE_SQL = "update td_sm_organization set org_sn=?, org_name=?, parent_id=?, orgnumber=?, remark3=?, remark5=?, org_level=?, org_xzqm=?,orgLeader=? where org_id=?";
    
    private static final Integer BATCH_LIMIT = 1000;
    
    private static Logger logger = Logger.getLogger(SyncOrganizationInfo.class);

    private MdmService mdmService;
    
    private ConfigSQLExecutor executor;
    private UserOrgParamManager userOrgParamManager = new UserOrgParamManager();
    /**
     * 是否启用用户调整和组织机构调整功能，启用后一旦管理员手动调整用户和机构关系、组织之间的关系后
     * 将不会自动同步这些关系，以手工调整后的关系为准，默认为开启true，false不开启
     */
    private boolean enablecustom = true;
    
    public boolean isEnablecustom() {
		return enablecustom;
	}
	public void setEnablecustom(boolean enablecustom) {
		this.enablecustom = enablecustom;
	}
    /**
     * 同步所有机构数据
     */
    public void syncAllData() {
        logger.info("Sync org info started...");
        String machinid = "";
        machinid = SimpleStringUtil.getHostIP();
        try {
			LogManager logMgr = SecurityDatabase.getLogManager();
			
//			String operContent = userName + "[" + userId + "] 退出["
//					+ subsystem + "]";
			// modified by hilary on 20101105,for fixing bug 13979,for logout's log  and login's log has same manner
			AccessControl control = AccessControl.getAccessControl();
			String userAccount = "";
			String operContent = "";
			String machineID = "";
			String orgID = "";
			if(control == AccessControl.getGuest())
			{
				machineID = machinid;
				userAccount = "Quartz定时任务";
				operContent = userAccount + "同步组织机构数据开始";
			}
			else
			{
				userAccount = control.getUserAccount();
				String userName = control.getUserName();
				String subsystem = control.getCurrentSystemName();
				machineID = control.getMachinedID();
				operContent = userAccount + "(" + userName + ") 从[" + subsystem + "]同步组织机构数据开始";
			}
			
//			String operContent = userAccount + "(" + userName + ") 退出[" + subsystem + "]";
			
			String operSource = machineID;
			String operModle = "主数据同步";
			logMgr.log(userAccount,orgID,operModle,  operSource,
					operContent ,"", Log.INSERT_OPER_TYPE);		
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		boolean isfailed = false;
		String error = "";
        try {
            List<MdmOrgLeader> orgList = mdmService.getOrgLeaderList("19000101", "99000101", "1", "99999999");
            
           
           
            
            TransactionManager tm = new TransactionManager();
            PreparedDBUtil savePre = new PreparedDBUtil();
            PreparedDBUtil updatePre = new PreparedDBUtil();;
            try {
                tm.begin();
                Set<String> orgKeySet = new HashSet<String>(executor.queryList(String.class, "selectTdSmOrgKey"));
                Map<String,String> fixedorginfos = enablecustom?userOrgParamManager.getFixedOrgInfos():null;
                if(fixedorginfos == null)
                	fixedorginfos = new HashMap<String,String>();
                int saveSize = 0;
                int updateSize = 0;
                savePre.preparedInsert(SAVA_SQL);
                updatePre.preparedUpdate(UPDATE_SQL);
                for (MdmOrgLeader temp : orgList) {
                    if (orgKeySet.contains(temp.getOrgId())) {
                        updateSize ++;
                        addPreBatch(updatePre, temp,fixedorginfos,true);
                        if (saveSize > BATCH_LIMIT) {
                            updateSize = 0;
                            updatePre.executePreparedBatch();
                            updatePre = new PreparedDBUtil();
                            updatePre.preparedUpdate(UPDATE_SQL);
                        }
                    } else {
                        saveSize ++;
                        addPreBatch(savePre, temp,fixedorginfos,false);
                        if (saveSize > BATCH_LIMIT) {
                            saveSize = 0;
                            savePre.executePreparedBatch();
                            savePre = new PreparedDBUtil();
                            savePre.preparedInsert(SAVA_SQL);
                        }
                    }
                }
                
                if (saveSize > 0) { savePre.executePreparedBatch(); } 
                if (updateSize > 0) { updatePre.executePreparedBatch(); }
                
                tm.commit();
                
            } catch (Throwable e) {
            	isfailed = true;
                error = StringUtil.formatBRException(e);  
                logger.error("initialData error", e);
            }
            finally
            {
            	tm.release();
            }
        } catch (Exception e) {
        	isfailed = true;
            error = error + "<br>"+StringUtil.formatBRException(e);  
            logger.error("", e);
        }
        try {
			LogManager logMgr = SecurityDatabase.getLogManager();
			
//			String operContent = userName + "[" + userId + "] 退出["
//					+ subsystem + "]";
			// modified by hilary on 20101105,for fixing bug 13979,for logout's log  and login's log has same manner
			AccessControl control = AccessControl.getAccessControl();
			String userAccount = "";
			String operContent = "";
			String machineID = "";
			String orgID = "";
			if(control == AccessControl.getGuest())
			{
				machineID = machinid;
				userAccount = "Quartz定时任务";
				if(!isfailed)
					operContent = userAccount + "同步组织机构数据结束";
				else
					operContent = userAccount + "同步组织机构数据失败： \r\n" + error;
			}
			else
			{
				userAccount = control.getUserAccount();
				String userName = control.getUserName();
				String subsystem = control.getCurrentSystemName();
				machineID = control.getMachinedID();
				if(!isfailed)
				{
					operContent = userAccount + "(" + userName + ") 从[" + subsystem + "]同步组织机构数据结束";
				}
				else
				{
					operContent = userAccount + "(" + userName + ") 从[" + subsystem + "]同步组织机构数据失败： \r\n" + error;
				}
					
			}
			
//			String operContent = userAccount + "(" + userName + ") 退出[" + subsystem + "]";
			
			String operSource = machineID;
			String operModle = "主数据同步";
			logMgr.log(userAccount,orgID,operModle,  operSource,
					operContent ,"", Log.INSERT_OPER_TYPE);		
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
        // 生成组织树结构级
        OrgTreeLevel.run();
        logger.info("Sync org info finished...");
    }
    
    private void addPreBatch(PreparedDBUtil pre, MdmOrgLeader temp,Map<String,String> fixedorginfos,boolean update) throws Exception {
//        if (temp.getOrgRank() != null) {
//            pre.setString(1, temp.getOrgId());
//        } else {
            pre.setString(1, temp.getOrgId());
//        }
       
        if (temp.getOrgText() == null) {
            pre.setString(2, "未指定");
            String parentOrgid = fixedorginfos.get(temp.getOrgId());
            if(parentOrgid == null)
            {
            	pre.setString(3, "0");
            }
            else
            {
            	pre.setString(3, parentOrgid);
            }
        }
        else if (temp.getOrgText().trim().equals("三一集团")) {
            pre.setString(2, temp.getOrgText().trim());
            String parentOrgid = fixedorginfos.get(temp.getOrgId());
            if(parentOrgid == null)
            {
            	pre.setString(3, "0");
            }
            else
            {
            	pre.setString(3, parentOrgid);
            }
        }
        else {
        	
            pre.setString(2, temp.getOrgText());
            String parentOrgid = fixedorginfos.get(temp.getOrgId());
            if(parentOrgid == null)
            {
            	pre.setString(3, temp.getFatherId());
            }
            else
            {
            	pre.setString(3, parentOrgid);
            }
           
        }
        pre.setString(4, temp.getOrgId());
        pre.setString(5, temp.getUseFlag());
        pre.setString(6, temp.getOrgText());
        pre.setString(7, temp.getOrgLevel());
        pre.setString(8, "31" + temp.getOrgId());        
        System.out.println("temp.getOrgLeader():"+temp.getOrgLeader());
        if(!update)
        {
        	pre.setString(9, temp.getOrgId());
        	pre.setString(10, temp.getOrgLeader());
        }
        else
        {
        	pre.setString(9, temp.getOrgLeader());
        	pre.setString(10, temp.getOrgId());
        }
        
        pre.addPreparedBatch();
    }

    public static void main(String[] args) {
        SyncOrganizationInfo test = (SyncOrganizationInfo) MDPropertiesUtil.getBeanObject("masterdata.hr.syncOrganizationInfo");
        test.syncAllData();
    }
}
