package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.frameworkset.spi.SPIException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:用户机构管理Action
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author feng.jing
 * @version 1.0
 */
public class UserOrgManagerAction    implements Serializable {
	public UserOrgManagerAction() {
	}

	private static Logger log = Logger.getLogger(UserOrgManagerAction.class
			.getName());

	  

	/**
	 * add by hongyu.deng(by ajax to storeanddelete userOrg relation the
	 * reference jsp page is not refresh)
	 * 
	 * @param uid
	 * @param orgIdName
	 * @return
	 * @throws Exception
	 */
	public static String storeAndDeleteUserOrg(Integer uid, String orgIdNames)
			throws Exception {
	
//		UserManager userManager = SecurityDatabase.getUserManager();
//		OrgManager orgManager = SecurityDatabase.getOrgManager();
//		User user = userManager.getUser("userId", uid.toString());
//		System.out.println(".........."+uid);
		try {
//			JobManager jobManager = SecurityDatabase.getJobManager();
			String orgId;
			String jobId;
			
//			for (int i = 0; i < orgIdName.length; i++) {
//				String id = orgIdName[i];
//				System.out.println("..................."+id);
//			}
//			userManager.deleteUserjoborg(user);
			String [] orgIdName = orgIdNames.split(";");
			
				orgId = orgIdName[0];
				jobId = orgIdName[1];
//				System.out.println(orgId);
//				System.out.println(jobId);
				UserManager userManager = SecurityDatabase.getUserManager();
//				DBUtil db = new DBUtil();
//				String sql ="delete from TD_SM_USERJOBORG where job_id ='"+ jobId +"' and" +
//				" org_id ='"+ orgId +"' and user_id ="+ uid +"";
//				String sql1 ="select * from TD_SM_USERJOBORG where job_id ='"+ jobId +"' and" +
//				" org_id ='"+ orgId +"' and user_id ="+ uid +"";
//				db.executeSelect(sql1);
//				db.executeDelete(sql);
				String userId = uid.toString();
				//-----如果该用户的主要单位为该机构，删除用户和该机构关系
				DBUtil db1 = new DBUtil();
				DBUtil db2 = new DBUtil();
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				String strsql ="select org_id from td_sm_orguser where org_id ='"+ orgId +"' and user_id =" + userId + "";
				db1.executeSelect(strsql);
				userManager.deleteUserjoborg(userId,jobId,orgId);
				db2.executeSelect("select * from td_sm_userjoborg where org_id ='"+ orgId +"' and user_id =" + userId + "");
				if(db1.size() > 0 && db2.size()==0){
						orgManager.deleteMainOrgnazitionOfUser(userId);
					
				}
				//------------------------------------------------------

				
				

				
				
				//存数据到历史表TD_SM_USERJOBORG_HISTORY
//				for(int j= 0; j < db.size(); j ++)
//				{
//					int userid=db.getInt(j,"user_id");
//					String jid = db.getString(j,"JOB_ID");
//					String oid = db.getString(j,"ORG_ID");
//					Date starttime = db.getDate(j,"JOB_STARTTIME");
//					
//					
//					
//
//					String sql2 ="insert into TD_SM_USERJOBORG_HISTORY values("+ userid +",'"+ jid +"'," +
//							"'"+ oid +"',"+ DBUtil.getDBDate(starttime)+"," +
//							""+ DBUtil.getDBDate(new Date())+",0)";
////					System.out.println(sql2);
//					db.executeInsert(sql2);
//				}

				
				// 修改，删除所有机构后orgIdName为空，会抛异常
//				if (tmp.length == 2) {
//					orgId = tmp[0];
//					jobId = tmp[1];
//					System.out.println(orgId);
//					System.out.println(jobId);
//					Organization org = orgManager.getOrg("orgId", orgId);
//					Job job = jobManager.getJob("jobId", jobId);
//					if (org != null && job != null) {
//						Userjoborg u = new Userjoborg();
//						u.setOrg(org);
//						u.setUser(user);
//						u.setJob(job);
//						u.setSameJobUserSn(new Integer(0));
//						u.setJobSn(new Integer(0));
//						userManager.storeUserjoborg(u);
//					}
//				}
			

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";

	}

	 
	
	
	public static String storeSetupOrg(Integer uid, String orgIdNames)
	throws Exception {
	//设置用户的主岗位
		try {
			String orgId;
			String jobId;
			String userId = uid.toString();
			String [] orgIdName = orgIdNames.split(";");
		
			orgId = orgIdName[0];
			jobId = orgIdName[1];
			
//			System.out.println("用户ID-----------"+userId);
//			System.out.println("机构ID-----------"+orgId);
//			System.out.println("岗位ID-----------"+jobId);
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			
//			orgManager.deleteMainOrgnazitionOfUser(userId);
			orgManager.addMainOrgnazitionOfUser(userId,orgId);
			
		
	
	} catch (Exception e) {
		e.printStackTrace();
		return "fail";
	}
	return "success";
	
	}
	
	 
	/**
	 * 批量用户加入机构
	 * @param ids
	 * @param jobid
	 * @param orgid
	 * @throws ManagerException
	 * @throws SPIException 
	 */
	public void storeAlotUserOrg(String[] ids, String[] jobid,String orgid) throws ManagerException, SPIException {
		//System.out.println("...................."+orgid);
		
		
		UserManager userManager = SecurityDatabase.getUserManager();
		userManager.storeAlotUserOrg(ids,jobid,orgid);
		
	}
	/**
	 * 批量用户移出机构
	 * @param ids
	 * @param jobid
	 * @param orgId
	 * @throws ManagerException
	 */
	public void delAlotUserOrg(String[] ids, String[] jobids, String orgId) throws ManagerException {
	
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			userManager.delAlotUserOrg(ids, jobids, orgId);
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
