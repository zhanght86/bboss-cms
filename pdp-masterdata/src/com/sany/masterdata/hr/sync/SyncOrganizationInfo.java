/*
 * @(#)SyncOrgInfo.java
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
package com.sany.masterdata.hr.sync;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventImpl;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Log;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
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
    private boolean deleteremovedorg = false;
    public boolean isEnablecustom() {
		return enablecustom;
	}
	public void setEnablecustom(boolean enablecustom) {
		this.enablecustom = enablecustom;
	}
	
	private boolean contain(String orgid, List<RemoveOrg> orgKeySet)
	{
		for(int i = 0; orgKeySet != null && i < orgKeySet.size(); i ++)
		{
			RemoveOrg org = orgKeySet.get(i);
			if(org.getOrg_id().equals(orgid))
				return true;
		}
		return false;
	}
	
	private boolean needremove(String orgid, List<MdmOrgLeader> neworgKeySet)
	{
		if(orgid.equals("50126153") || orgid.equals("50126154")|| orgid.equals("50508061"))
		{
			return true;
		}
		for(int i = 0; neworgKeySet != null && i < neworgKeySet.size(); i ++)
		{
			MdmOrgLeader org = neworgKeySet.get(i);
			if(org.getOrgId().equals(orgid))
				return false;
		}
		return true;
	}
	
	private List<RemoveOrg> evalremoves(List<RemoveOrg> orgKeySet, List<MdmOrgLeader> neworgKeySet)
	{
		List<RemoveOrg> removes = new ArrayList<RemoveOrg>();
		for(int i = 0; orgKeySet != null && i < orgKeySet.size(); i ++)
		{
			RemoveOrg org = orgKeySet.get(i);
			if(needremove(org.getOrg_id(), neworgKeySet))
			{
				removes.add(org);
			}
		}
		return removes;
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
		 List<RemoveOrg> removes = null;
        try {
            List<MdmOrgLeader> orgList = mdmService.getOrgLeaderList("19000101", "99000101", "1", "99999999");
            
           
           
            
            
            TransactionManager tm = new TransactionManager();
            PreparedDBUtil savePre = new PreparedDBUtil();
            PreparedDBUtil updatePre = new PreparedDBUtil();;
            try {
                tm.begin();
                List<RemoveOrg> orgKeySet = executor.queryList(RemoveOrg.class, "selectTdSmOrgKey");
                Map<String,String> fixedorginfos = enablecustom?userOrgParamManager.getFixedOrgInfos():null;
                if(fixedorginfos == null)
                	fixedorginfos = new HashMap<String,String>();
                if(deleteremovedorg)
                	removes = evalremoves(orgKeySet, orgList);
                int saveSize = 0;
                int updateSize = 0;
                savePre.preparedInsert(SAVA_SQL);
                updatePre.preparedUpdate(UPDATE_SQL);
                for (MdmOrgLeader temp : orgList) {
                	
                    if (contain(temp.getOrgId(), orgKeySet)) {
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
       
        if(deleteremovedorg && removes != null)
        {
        	
        	TransactionManager tm = new TransactionManager();
        	try {
				tm.begin(TransactionManager.RW_TRANSACTION);
		        for(int i = removes.size()-1; i >= 0; i --)
		        {
		        	try {
						deleteOrg(removes.get(i).getOrg_id());
					} catch (Exception e) {
						logger.error("删除机构"+removes.get(i).getOrg_id()+"失败！", e);
					}
		        }
		        tm.commit();
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	finally
        	{
        		tm.release();
        	}
        }
        // 生成组织树结构级
        OrgTreeLevel.run();
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
        logger.info("Sync org info finished...");
        
    }
    
    public void deleteOrg(String orgId) throws Exception {
		
		
		
		
	 PreparedDBUtil db = new PreparedDBUtil(); 
		
//	   System.out.println(str.toString());
//		删除机构表的基本数据
		StringBuilder sql = new StringBuilder()
		.append(" update TD_SM_ORGANIZATION set remark3=0 where org_id =?");
//			.append("delete from TD_SM_ORGANIZATION a where a.org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH ")
//			.append("t.org_id='").append(orgId)
//			.append("' CONNECT BY PRIOR t.org_id=t.PARENT_ID)");
		    

//		删除机构岗位用户表的基本数据
//		StringBuffer sql1 = new StringBuffer()
		
//			.append("delete from TD_SM_USERJOBORG a where a.org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH")
//			.append(" t.org_id='").append(orgId)
//			.append("' CONNECT BY PRIOR t.org_id=t.PARENT_ID)");
		
		StringBuffer sql1 = new StringBuffer()
		
		.append("delete from TD_SM_USERJOBORG  where org_id =?");
		
////		删除机构岗位表的基本数据
//		StringBuffer sql2 = new StringBuffer()
//			.append("delete from TD_SM_ORGJOB a where a.org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH")
//			.append(" t.org_id='").append(orgId)
//			.append("' CONNECT BY PRIOR t.org_id=t.PARENT_ID)");
//		删除机构岗位表的基本数据
		StringBuffer sql2 = new StringBuffer()
			.append("delete from TD_SM_ORGJOB where org_id =?");

////		删除机构用户表的基本数据
//		StringBuffer sql3 = new StringBuffer()
//			.append("delete from TD_SM_ORGUSER a where a.org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH")
//			.append(" t.org_id='").append(orgId)
//			.append("' CONNECT BY PRIOR t.org_id=t.PARENT_ID)");
		
////	删除机构用户表的基本数据
		StringBuffer sql3 = new StringBuffer()
			.append("delete from TD_SM_ORGUSER where org_id =?");

////		删除机构角色表的基本数据
//		StringBuffer sql4 = new StringBuffer()
//			.append("delete from TD_SM_ORGROLE a where a.org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH")
//			.append(" t.org_id='").append(orgId)
//			.append("' CONNECT BY PRIOR t.org_id=t.PARENT_ID)");
		
////	删除机构角色表的基本数据
		StringBuffer sql4 = new StringBuffer()
			.append("delete from TD_SM_ORGROLE  where org_id =?");
		
////		递归删除机构自身资源操作的基本数据
//		String sql5 = "delete from TD_SM_ROLERESOP  where res_id in "
//			+"(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH t.org_id='"+ orgId +"' "
//			+" CONNECT BY PRIOR t.org_id=t.PARENT_ID) and restype_id='orgunit'";
		
////	递归删除机构自身资源操作的基本数据
		String sql5 = "delete from TD_SM_ROLERESOP  where res_id  =?";
		
////		删除机构岗位角色表中的外键 gao.tang 2007.10.26
////		删除了机构岗位角色表数据
//		StringBuffer sql6 = new StringBuffer()
//			.append("delete from TD_SM_ORGJOBROLE where org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t START WITH")
//			.append(" t.org_id='").append(orgId)
//			.append("' CONNECT BY PRIOR t.org_id=t.PARENT_ID)");
		
////	删除机构岗位角色表中的外键 gao.tang 2007.10.26
////	删除了机构岗位角色表数据
	StringBuffer sql6 = new StringBuffer()
		.append("delete from TD_SM_ORGJOBROLE where org_id =?");
//		
////		删除机构与用户的部门管理员对应关系
//		StringBuffer sql7 = new StringBuffer()
//			.append("delete from td_sm_orgmanager d ")
//			.append("where d.org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t ")
//			.append("START WITH t.org_id='").append(orgId).append("' ")
//			.append("CONNECT BY PRIOR t.org_id=t.PARENT_ID)");
		
//		删除机构与用户的部门管理员对应关系
		StringBuffer sql7 = new StringBuffer()
			.append("delete from td_sm_orgmanager ")
			.append("where org_id =?");
		
//		//地税特有表TD_SM_TAXCODE_ORGANIZATION
//		StringBuffer sql8 = new StringBuffer()
//			.append("delete from TD_SM_TAXCODE_ORGANIZATION d ")
//			.append("where d.org_id in ")
//			.append("(SELECT t.org_id FROM TD_SM_ORGANIZATION t ")
//			.append("START WITH t.org_id='").append(orgId).append("' ")
//			.append("CONNECT BY PRIOR t.org_id=t.PARENT_ID)") ;
		
		//地税特有表TD_SM_TAXCODE_ORGANIZATION
		StringBuffer sql8 = new StringBuffer()
			.append("delete from TD_SM_TAXCODE_ORGANIZATION  ")
			.append("where org_id =?");
		
//		//递归获取当前机构下的所有用户的ID
//		StringBuffer  sql_user = new StringBuffer()
//			.append(" select distinct b.USER_ID from TD_SM_USERJOBORG b where b.ORG_ID in ( ")
//			.append("select distinct a.ORG_ID from TD_SM_ORGANIZATION a start with a.ORG_ID = '")
//			.append(orgId) 
//			.append("' connect by prior a.ORG_ID = a.PARENT_ID)");
		
		//递归获取当前机构下的所有用户的ID
//		StringBuffer  sql_user = new StringBuffer()
//			.append(" select distinct USER_ID from TD_SM_USERJOBORG where ORG_ID in  ")
//			.append(str.toString());
//	   TransactionManager tm = new TransactionManager();
//		try {
//			tm.begin();
//			db.preparedDelete(sql1.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
//			
//			db.preparedDelete(sql2.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
//			
//			db.preparedDelete(sql3.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
//			
//			db.preparedDelete(sql4.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
//			
//			db.preparedDelete(sql5.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
//			
//			db.preparedDelete(sql6.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
//			db.preparedDelete(sql8.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
 
//			db.preparedDelete(sql7.toString());
//			db.setString(1, orgId);
//			db.addPreparedBatch();
 
			
			db.preparedUpdate(sql.toString());
			db.setString(1, orgId);
//			db.addPreparedBatch();
 
//			db.executePreparedBatch();
			db.executePrepared();

//			tm.commit();
//			
//			 
// 
//			
//		} catch (SQLException e) {
//			throw e;
//		} catch (Exception e) {
//			throw e;
//		}
//		finally
//		{
//			tm.release();
//		}
		 
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
        else if (temp.getOrgText().trim().equals("bbossgroups")) {
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
	public boolean isDeleteremovedorg() {
		return deleteremovedorg;
	}
	public void setDeleteremovedorg(boolean deleteremovedorg) {
		this.deleteremovedorg = deleteremovedorg;
	}
}
