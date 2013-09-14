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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.sany.greatwall.MdmService;
import com.sany.greatwall.domain.MdmUser;
import com.sany.masterdata.utils.MDPropertiesUtil;

/**
 * 同步机构数据
 * @author caix3
 * @since 2012-3-22
 */
public class SyncUserInfo {

    private static final String USER_SAVE_SQL = "insert into td_sm_user (user_sn, user_name, user_password, user_realname, user_sex, user_worktel, user_worknumber, user_mobiletel1, user_birthday, user_idcard, user_isvalid, user_type, user_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String USERORG_SAVE_SQL = "insert into td_sm_orguser (org_id, user_id) values (?,?)";
    private static final String USERORGJOB_SAVE_SQL = "insert into td_sm_userjoborg (org_id, job_id, user_id) values (?, ?, ?)";
    private static final String USER_UPDATE_SQL = "update td_sm_user set user_sn=?, user_name=?, user_password=?, user_realname=?, user_sex=?, user_worktel=?, user_worknumber=?, user_mobiletel1=?, user_birthday=?, user_idcard=?, user_isvalid=?, user_type=? where user_id=?";
    private static final String USERORG_UPDATE_SQL = "update td_sm_orguser set org_id=? where user_id=?";
    private static final String USERORGJOB_UPDATE_SQL = "update td_sm_userjoborg set org_id=?, job_id=?  where user_id=?";
    private static final String exist_org_user = "delete from td_sm_orguser where user_id=?";
    private static final String exist_org_job_user = "delete from td_sm_userjoborg where user_id=?";
    
    private static final Integer BATCH_LIMIT = 1000;
    
    private static final String[] SEX = {"", "M", "F"};
    
    private static final String USER_PS = EncrpyPwd.encodePassword("123456");
    
    private static Logger logger = Logger.getLogger(SyncUserInfo.class);

    private MdmService mdmService;
    
    private ConfigSQLExecutor executor;
    
    /**
     * 同步所有机构数据
     */
    public void syncAllData() {
        logger.info("Sync user info started...");
        try {
            List<MdmUser> userList = mdmService.getUserList("19000101", "99000101", "1", "99999999");
            if(userList == null || userList.size() == 0)
            	return;
            //用户主键索引
            Set<String> userKeySet = new HashSet<String>(executor.queryList(String.class, "selectTdSmUserKey"));
//            //用户机构关系索引
//            Set<String> userOrgKeySet = new HashSet<String>(executor.queryList(String.class, "selectTdSmOrgUserKey"));
//          //用户岗位机构关系索引
//            Set<String> userJobOrgKeySet = new HashSet<String>(executor.queryList(String.class, "selectTdSmOrgJobUserKey"));
            Map<String,Object> newUsers = new HashMap<String,Object>();
            Map<String,List<MdmUser>> usersByNo = new HashMap<String,List<MdmUser>>();
            for(int i = 0; i < userList.size(); i ++)
            {
            	MdmUser u = userList.get(i);
            	List<MdmUser> temp = usersByNo.get(u.getUserNo());
            	if(temp == null)
            	{
            		temp = new ArrayList<MdmUser>();
            		usersByNo.put(u.getUserNo(), temp);
            	}
            	temp.add(u);
            	
            }
            Object dump = new Object();
            TransactionManager tm = new TransactionManager();
            PreparedDBUtil userSavePre = new PreparedDBUtil();
            PreparedDBUtil userOrgSavePre = new PreparedDBUtil();
            PreparedDBUtil userJobOrgSavePre = new PreparedDBUtil();
            PreparedDBUtil userUpdatePre = new PreparedDBUtil();
            PreparedDBUtil userOrgUpdatePre = new PreparedDBUtil();
            PreparedDBUtil userJobOrgUpdatePre = new PreparedDBUtil();
            try {
                tm.begin();
                userSavePre.setBatchOptimize(false);
                userSavePre.preparedInsert(USER_SAVE_SQL);
                userOrgSavePre.preparedInsert(USERORG_SAVE_SQL);
                userOrgSavePre.setBatchOptimize(false);
                userJobOrgSavePre.preparedInsert(USERORGJOB_SAVE_SQL);
                userJobOrgSavePre.setBatchOptimize(false);
                userUpdatePre.preparedUpdate(USER_UPDATE_SQL);
                userUpdatePre.setBatchOptimize(false);
                userOrgUpdatePre.preparedUpdate(USERORG_SAVE_SQL);
                userOrgUpdatePre.setBatchOptimize(false);
                userJobOrgUpdatePre.preparedUpdate(USERORGJOB_SAVE_SQL);
                userJobOrgUpdatePre.setBatchOptimize(false);
                
                int saveSize = 0;
                int updateSize = 0;
                Iterator<Map.Entry<String, List<MdmUser>>> ss = usersByNo.entrySet().iterator();
              while(ss.hasNext()) {
            	  
            	  Map.Entry<String, List<MdmUser>> entry = ss.next();
            	  List<MdmUser> users = entry.getValue();
            	  MdmUser temp = getUser(users);
                    if (userKeySet.contains(exchange(temp.getUserNo()))) {
                        updateSize ++;
                        addPreBatch(userUpdatePre, userOrgUpdatePre, userJobOrgUpdatePre,temp,newUsers);
                        if (updateSize > BATCH_LIMIT) {
                            updateSize = 0;
                            userUpdatePre.executePreparedBatch();
                            userUpdatePre = new PreparedDBUtil();
                            userUpdatePre.setBatchOptimize(false);
                            userUpdatePre.preparedUpdate(USER_UPDATE_SQL);
                            userOrgUpdatePre.executePreparedBatch();
                            userOrgUpdatePre = new PreparedDBUtil();
                            userOrgUpdatePre.setBatchOptimize(false);
                            userOrgUpdatePre.preparedUpdate(USERORG_SAVE_SQL);
                            userJobOrgUpdatePre.executePreparedBatch();
                            userJobOrgUpdatePre = new PreparedDBUtil();
                            userJobOrgUpdatePre.setBatchOptimize(false);
                            userJobOrgUpdatePre.preparedUpdate(USERORGJOB_SAVE_SQL);
                            
                        }
                    } else { 
                        
//                        if(newUsers.containsKey(temp.getUserNo()))
//                		{
//                        	if(temp.getUserId().indexOf('_') < 0)
//                        	{
//	                        	 updateSize ++;
//	                             addPreBatch(userUpdatePre, userOrgUpdatePre, userJobOrgUpdatePre,temp,newUsers);
//	                             if (updateSize > BATCH_LIMIT) {
//	                                 updateSize = 0;
//	                                 userUpdatePre.executePreparedBatch();
//	                                 userUpdatePre = new PreparedDBUtil();
//	                                 userUpdatePre.setBatchOptimize(false);
//	                                 userUpdatePre.preparedUpdate(USER_UPDATE_SQL);
//	                                 userOrgUpdatePre.executePreparedBatch();
//	                                 userOrgUpdatePre = new PreparedDBUtil();
//	                                 userOrgUpdatePre.setBatchOptimize(false);
//	                                 userOrgUpdatePre.preparedUpdate(USERORG_SAVE_SQL);
//	                                 userJobOrgUpdatePre.executePreparedBatch();
//	                                 userJobOrgUpdatePre = new PreparedDBUtil();
//	                                 userJobOrgUpdatePre.setBatchOptimize(false);
//	                                 userJobOrgUpdatePre.preparedUpdate(USERORGJOB_SAVE_SQL);
//	                                 
//	                             }
//                        	}
//                        	
//                		}
//                        else
//                        {
//                        	saveSize ++;
//                        	newUsers.put(temp.getUserNo(), dump);
//                        	addPreBatch(userSavePre, userOrgSavePre, userJobOrgSavePre, temp, newUsers);
//                            if (saveSize > BATCH_LIMIT) {
//                                saveSize = 0;
//                                userSavePre.executePreparedBatch();
//                                
//                                userSavePre = new PreparedDBUtil();
//                                userSavePre.setBatchOptimize(false);
//                                userSavePre.preparedUpdate(USER_SAVE_SQL);
//                                userOrgSavePre.executePreparedBatch();
//                                
//                                userOrgSavePre = new PreparedDBUtil();
//                                userOrgSavePre.setBatchOptimize(false);
//                                userOrgSavePre.preparedUpdate(USERORG_SAVE_SQL);
//                                userJobOrgSavePre.executePreparedBatch();
//                                
//                                userJobOrgSavePre = new PreparedDBUtil();
//                                userJobOrgSavePre.setBatchOptimize(false);
//                                userJobOrgSavePre.preparedUpdate(USERORGJOB_SAVE_SQL);
//                            }
//                        }
                        
                    	saveSize ++;
                    	addPreBatch(userSavePre, userOrgSavePre, userJobOrgSavePre, temp, newUsers);
                        if (saveSize > BATCH_LIMIT) {
                            saveSize = 0;
                            userSavePre.executePreparedBatch();
                            
                            userSavePre = new PreparedDBUtil();
                            userSavePre.setBatchOptimize(false);
                            userSavePre.preparedUpdate(USER_SAVE_SQL);
                            userOrgSavePre.executePreparedBatch();
                            
                            userOrgSavePre = new PreparedDBUtil();
                            userOrgSavePre.setBatchOptimize(false);
                            userOrgSavePre.preparedUpdate(USERORG_SAVE_SQL);
                            userJobOrgSavePre.executePreparedBatch();
                            
                            userJobOrgSavePre = new PreparedDBUtil();
                            userJobOrgSavePre.setBatchOptimize(false);
                            userJobOrgSavePre.preparedUpdate(USERORGJOB_SAVE_SQL);
                        }
                    }
                }
                
                if (saveSize > 0) { 
                    userSavePre.executePreparedBatch(); 
                    userOrgSavePre.executePreparedBatch();
                    userJobOrgSavePre.executePreparedBatch();
                };
                if (updateSize > 0) { 
                    userUpdatePre.executePreparedBatch(); 
                    userOrgUpdatePre.executePreparedBatch();
                    userJobOrgUpdatePre.executePreparedBatch();
                };
                
                tm.commit();
                
            } catch (Throwable e) {
                
                logger.error("initialData error", e);
            }
            finally
            {
            	tm.release();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("Sync user info finished...");
    }
    private MdmUser getUser(List<MdmUser> users) {
		if(users.size() == 1)
			return users.get(0);
		for(MdmUser user:users)
		{
			if(user.getUserId().indexOf("_") < 0 && user.getUserId().indexOf("-") < 0)
			{
				return user;
				
			}
		}
		return users.get(0);
	}
	private Map<String,String> orguserinsert = new HashMap<String,String>();
    private Map<String,String> orgjobuserinsert = new HashMap<String,String>();
    private String v = "v";
    private void addPreBatch(PreparedDBUtil userPre, PreparedDBUtil userOrgPre, PreparedDBUtil userJobOrgPre, MdmUser temp,Map<String,Object> newUsers) throws Exception {
        String userNo = exchange(temp.getUserNo());
        userPre.setString(1, userNo);
        if (temp.getUserId() == null || temp.getUserId().equals("")) {
            userPre.setString(2, temp.getUserNo());
        } else {
            userPre.setString(2, temp.getUserId());
        }
        userPre.setString(3, USER_PS);
        userPre.setString(4, temp.getFirstName() + temp.getLastName());
        
        if (temp.getSex()!= null && !temp.getSex().trim().equals("")) {
            userPre.setString(5, SEX[Integer.parseInt(temp.getSex().trim())]);
        } else {
            userPre.setString(5, "");
        }
        userPre.setString(6, temp.getTelephone());
        userPre.setString(7, temp.getUserNo());
        userPre.setString(8, temp.getMobile());
        if (temp.getBrithday()!= null) {
            userPre.setDate(9, temp.getBrithday());
        } else {
            userPre.setNull(9, java.sql.Types.DATE);
        }
        userPre.setString(10, temp.getIdCard());
        userPre.setInt(11, 2);
        userPre.setString(12, "1");
        userPre.setString(13, temp.getUserNo());
        
        userPre.addPreparedBatch();
       
        
        if (temp.getOrg() != null && !temp.getOrg().equals("")) {
//        	if(userOrgKeySet.contains(userNo) || orguserinsert.containsKey(userNo))
//        	{
//	            userOrgPre.setString(1, temp.getOrg());
//	            userOrgPre.setString(2, temp.getUserNo());
//	            userOrgPre.addPreparedBatch();
//        	}
//        	else
        	{

//        		if(newUsers.containsKey(temp.getUserNo()))
//        		{
//	        		if(temp.getUserId().indexOf('_') < 0)
//	        		{
//		        		SQLExecutor.delete(exist_org_user, userNo);
//		//        		userOrgPre.preparedInsert(USERORG_SAVE_SQL);
//		        		userOrgPre.setString(1, temp.getOrg());
//			            userOrgPre.setString(2, temp.getUserNo());
//			            userOrgPre.addPreparedBatch();
//	        		}
//        		}
//        		else
        		{
        			SQLExecutor.delete(exist_org_user, userNo);
        			//userOrgPre.preparedInsert(USERORG_SAVE_SQL);
	        		userOrgPre.setString(1, temp.getOrg());
		            userOrgPre.setString(2, temp.getUserNo());
		            userOrgPre.addPreparedBatch();
        		}
	           
        	}
            
            if (temp.getPosition() != null && !temp.getPosition().equals("")) {
//            	if(userJobOrgKeySet.contains(userNo) || orgjobuserinsert.containsKey(userNo))
//            	{
//	                userJobOrgPre.setString(1, temp.getOrg());
//	                userJobOrgPre.setString(2, temp.getPosition());
//	                userJobOrgPre.setString(3, temp.getUserNo());
//	                userJobOrgPre.addPreparedBatch();
//            	}
//            	else
            	{
            		
//	                if(newUsers.containsKey(temp.getUserNo()))
//	        		{
//		        		if(temp.getUserId().indexOf('_') < 0)
//		        		{
//		        			SQLExecutor.delete(exist_org_job_user, userNo);
////		            		userJobOrgPre.preparedInsert(USERORGJOB_SAVE_SQL);
//		            		userJobOrgPre.setString(1, temp.getOrg());
//			                userJobOrgPre.setString(2, temp.getPosition());
//			                userJobOrgPre.setString(3, temp.getUserNo());
//			                userJobOrgPre.addPreparedBatch();
//		        		}
//	        		}
//	        		else
	        		{
	        			SQLExecutor.delete(exist_org_job_user, userNo);
//	            		userJobOrgPre.preparedInsert(USERORGJOB_SAVE_SQL);
	            		userJobOrgPre.setString(1, temp.getOrg());
		                userJobOrgPre.setString(2, temp.getPosition());
		                userJobOrgPre.setString(3, temp.getUserNo());
		                userJobOrgPre.addPreparedBatch();
	        		}
            	}
              
            }
        }
    }
    
    
    private String exchange(String temp) {
        if (temp != null && !temp.equals("")) {
            for (int i = 0; i < temp.length(); i ++) {
                if (temp.charAt(i) != '0') {
                    return temp.substring(i);
                }
            }
        }
        return temp;
    }
    public static void main(String[] args) {
        SyncUserInfo test = (SyncUserInfo) MDPropertiesUtil.getBeanObject("masterdata.hr.syncUserInfo");
        test.syncAllData();
    }
}
