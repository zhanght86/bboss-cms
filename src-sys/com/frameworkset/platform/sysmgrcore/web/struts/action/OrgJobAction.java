package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
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
 * @author hongyu.deng
 * @version 1.0
 */
public class OrgJobAction   {
	private Logger logger = Logger.getLogger(OrgJobAction.class.getName());

	      
	/**
	 * 保存机构及岗位的关系 机构管理－－岗位设置，左边的窗口右边列表的内容。包括排序的先后 修改 通过Ajax来实现
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static String addJobToOrgAjax(String orgId, String jobId,
			String jobSn) throws Exception {

		try {
			OrgManager orgManager = SecurityDatabase.getOrgManager();
	
			int jsn = Integer.parseInt(jobSn);
			Organization org = new Organization();
			org.setOrgId(orgId);
			Job job = new Job();
			job.setJobId(jobId);
			Orgjob orgjob = new Orgjob();
			orgjob.setJob(job);
			orgjob.setOrganization(org);
			orgjob.setJobSn(new Integer(jsn));
			//modify by ge.tao
			//2007-11-07
			//storeOrgjob()方法已经做了记录唯一性判断
			
//			DBUtil db = new DBUtil();
//			String sql  ="select count(*) from td_sm_orgjob where org_id ='"+ orgId +"' and job_id ='"+ jobId +"'";
//		
//			db.executeSelect(sql);
//			if(db.getInt(0,0)==0)
//			{
				orgManager.storeOrgjob(orgjob);
				
//			}
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 保存机构及岗位的关系 机构管理－－岗位设置，左边的窗口右边列表的内容。包括排序的先后 修改 通过Ajax来实现
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static String addJobToSubOrgAjax(String orgId, String jobId,
			String jobSn) throws Exception {

		try {
			OrgManager orgManager = SecurityDatabase.getOrgManager();
	
//			//int jsn = Integer.parseInt(jobSn);
//			Organization org = new Organization();
//			org.setOrgId(orgId);
//			Job job = new Job();
//			job.setJobId(jobId);//此处为jobid集合，like（25，26）
//			Orgjob orgjob = new Orgjob();
//			orgjob.setJob(job);
//			orgjob.setOrganization(org);
			//orgjob.setJobSn(new Integer(jsn));
			
			orgManager.addSubOrgjob(orgId,jobId.split(","));
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	/**
	 * 删除机构及岗位的关系 机构管理－－岗位设置，左边的窗口右边列表的内容。 修改 通过Ajax来实现
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static String deleteJobToOrgAjax(String orgId, String jobId)
			throws Exception {

		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = new Organization();
			org.setOrgId(orgId);
			Job job = new Job();
			job.setJobId(jobId);
			Orgjob orgjob = new Orgjob();
			orgjob.setJob(job);
			orgjob.setOrganization(org);
			if(jobId.equals("1")){
			//机构下的待岗岗位不能删除
				return "fail";
			}else{
				orgManager.deleteOrgjob(orgjob);
				//删除改岗位下人员的3元关系
				userManager.deleteUserjoborg(job,org);
			}
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	/**
	 * 机构下岗位的排序 机构管理－－岗位设置，左边的窗口右边列表的内容。 修改 通过Ajax来实现(只是2个jobsn的替换)
	 * 下一级
	 * gao.tang 修改
	 * @param orgId
	 * @param jobId1
	 * @param jobId2
	 * @param jobSn--对应jobId1的sn，jobId2的sn自动加1
	 * @return
	 * @throws Exception
	 */
	public static String orgJobSnChangeAjax(String orgId, String jobId1,
			String jobId2, String jobSn) throws Exception {
		try {
//			OrgManager orgManager = SecurityDatabase.getOrgManager();
//			Organization org = new Organization();
//			org.setOrgId(orgId);
//			Orgjob orgjob = new Orgjob();
//			orgjob.setOrganization(org);
//			Job job = new Job();
//			// 保存第一个jobid的sn
//			job.setJobId(jobId1);
//			orgjob.setJob(job);
//			orgjob.setJobSn(Integer.valueOf(jobSn));
//			//orgManager.deleteOrgjob(orgjob);
//			orgManager.storeOrgjob(orgjob);
//			
//			UserManager userManager=SecurityDatabase.getUserManager();
//			int id1=Integer.parseInt(jobSn);
//			userManager.getUserSnList(orgId,jobId1,id1);
//			// 保存第二个jobid 的sn
//
//			Orgjob orgjob2 = new Orgjob();
//			orgjob2.setOrganization(org);
//			Job job2 = new Job();
//			job2.setJobId(jobId2);
//			orgjob2.setJob(job2);
//			orgjob2.setJobSn(new Integer((Integer.parseInt(jobSn) + 1)));
//			//orgManager.deleteOrgjob(orgjob2);
//			orgManager.storeOrgjob(orgjob2);
//			
//			int id2=Integer.parseInt(jobSn)+1;
//			userManager.getUserSnList(orgId,jobId2,id2);
////			保证待岗的排序号为null
//			DBUtil db = new DBUtil();
//			String sql ="update td_sm_orgjob set job_sn ='999'  where org_id ='"+ orgId +"' and job_id ='1' ";
//			String sqlujo ="update td_sm_userjoborg set job_sn ='999'  where org_id ='"+ orgId +"' and job_id ='1' ";
//			db.executeUpdate(sql);
//			db.executeUpdate(sqlujo);
			DBUtil db = new DBUtil();
			String job1sql = "update td_sm_orgjob set job_sn=(SELECT job_sn FROM TD_SM_ORGJOB WHERE org_id = '"
				+ orgId + "' AND job_id = '" + jobId2 + "') WHERE org_id = '" + orgId + "' AND job_id = '"
				+ jobId1 + "'";
			db.addBatch(job1sql);
			String job2sql = "update td_sm_orgjob set job_sn=(SELECT job_sn+1 FROM TD_SM_ORGJOB WHERE org_id = '"
				+ orgId + "' AND job_id = '" + jobId1 + "') WHERE org_id = '" + orgId + "' AND job_id = '"
				+ jobId2 + "'";
			db.addBatch(job2sql);
			String sql ="update td_sm_orgjob set job_sn ='999'  where org_id ='"+ orgId +"' and job_id ='1' ";
			db.addBatch(sql);
			String sqlujo ="update td_sm_userjoborg set job_sn ='999'  where org_id ='"+ orgId +"' and job_id ='1' ";
			db.addBatch(sqlujo);
			db.executeBatch();
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	/**
	 * 机构下岗位的排序 机构管理－－岗位设置，左边的窗口右边列表的内容。 修改 通过Ajax来实现(到顶部或者到底部的按钮事件)
	 * 到顶部和到底部
	 * @param orgId
	 * @param jobId
	 * @return
	 * @throws Exception
	 */
	public static String allOrgJobSnChangeAjax(String orgId, String[] jobId)
			throws Exception {
		
		try {
//			OrgManager orgManager = SecurityDatabase.getOrgManager();
//			UserManager userManager = SecurityDatabase.getUserManager();
//			Organization org = new Organization();
//			org.setOrgId(orgId);
//			Orgjob orgjob = new Orgjob();
//			orgjob.setOrganization(org);
//			orgManager.deleteOrgjob(org);
			DBUtil db = new DBUtil();
			StringBuffer sqlorder = new StringBuffer();
			for (int i = 0; jobId != null && i < jobId.length; i++) {
//				Job job = new Job();
//				job.setJobId(jobId[i]);
//				Orgjob orgjob1 = new Orgjob();
//				orgjob1.setOrganization(org);
//				orgjob1.setJob(job);
//				orgjob1.setJobSn( new Integer(i + 1));
//				orgManager.storeOrgjob(orgjob1);
//				userManager.getUserSnList(orgId,jobId[i],i+1);
				sqlorder.append("update td_sm_orgjob set JOB_SN=").append((i+1))
					.append(" where org_id='").append(orgId).append("' and job_id='")
					.append(jobId[i]).append("'");
				db.addBatch(sqlorder.toString());
				sqlorder.setLength(0);
			}
			//保证待岗的排序号为null
//			DBUtil db = new DBUtil();
			String sql ="update td_sm_orgjob set job_sn = '999' where org_id ='"+ orgId +"' and job_id ='1' ";
			String sqlujo ="update td_sm_userjoborg set job_sn ='999'  where org_id ='"+ orgId +"' and job_id ='1' ";
			db.addBatch(sql);
			db.addBatch(sqlujo);
			db.executeBatch();
//			db.executeUpdate(sql);
//			db.executeUpdate(sqlujo);

		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	/**
	 * 机构管理－岗位设置－保存2个人员的sn usersn 为userId1的sn，userId2的sn在usersn的基础上加1
	 * 
	 * @param orgId
	 * @param jobId
	 * @param userId1
	 * @param userId2
	 * @param userSn
	 * @return
	 * @throws Exception
	 */
	public static String storeUserSnJobOrg(String orgId, String jobId,
			String jobSn, String userId1, String userId2, String userSn)
			throws Exception {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();

			// 修改：需要完整用户对象信息
			// Organization org = new Organization();
			// org.setOrgId(orgId);
			OrgManager orgMgr = SecurityDatabase.getOrgManager();
			Organization org = orgMgr.getOrgById(orgId);
			// 修改结束
			
			Job job = new Job();
			job.setJobId(jobId);
			
			// 修改：需要完成用户对象信息
			// User user = new User();
			// user.setUserId(Integer.valueOf(userList[i]));
			User user = userManager.getUserById(userId1);
			// 修改结束
			
			Userjoborg userjoborg = new Userjoborg();
			userjoborg.setUser(user);
			userjoborg.setJob(job);
			userjoborg.setOrg(org);
			userjoborg.setSameJobUserSn(Integer.valueOf(userSn));
			userjoborg.setJobSn(new Integer(Integer.parseInt(jobSn) + 1));
			//userManager.deleteUserjoborg(userjoborg);
			DBUtil db = new DBUtil();
			String str="delete from td_sm_userjoborg where user_id ="+ userId1 +" " +
					"and org_id ='"+ orgId+ "' and job_id='"+ jobId +"'";
			db.executeDelete(str);
			userManager.storeUserjoborg(userjoborg);

			// save the second user
			User user2 = userManager.getUserById(userId2);
			// 修改结束
			Userjoborg userjoborg2 = new Userjoborg();
			userjoborg2.setUser(user2);
			userjoborg2.setJob(job);
			userjoborg2.setOrg(org);
			userjoborg2.setSameJobUserSn( new Integer(Integer
					.parseInt(userSn) + 1));
			userjoborg2.setJobSn(new Integer(Integer.parseInt(jobSn) + 1));
			//userManager.deleteUserjoborg(userjoborg2);
			DBUtil db1 = new DBUtil();
			String str1="delete from td_sm_userjoborg where user_id ="+ userId2 +" " +
					"and org_id ='"+ orgId+ "' and job_id='"+ jobId +"'";
			db1.executeDelete(str1);

			userManager.storeUserjoborg(userjoborg2);
		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	public static String storeAllUserSnJobOrg(String orgId, String jobId,
			String jobSn, String[] userId) throws Exception {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();

			// 修改：需要完整用户对象信息
			// Organization org = new Organization();
			// org.setOrgId(orgId);
			OrgManager orgMgr = SecurityDatabase.getOrgManager();
			Organization org = orgMgr.getOrgById(orgId);
			// 修改结束
			Job job = new Job();
			job.setJobId(jobId);
			// 修改：需要完成用户对象信息
			// User user = new User();
			// user.setUserId(Integer.valueOf(userList[i]));

			// 在保存之前先删除该机构下改岗位的所有用户.然后再添加
			//userManager.deleteUserjoborg(job, org);
			DBUtil db = new DBUtil();
			String str="delete from td_sm_userjoborg where " +
					" org_id ='"+ orgId+ "' and job_id='"+ jobId +"'";
			db.executeDelete(str);

			for (int i = 0; userId != null && i < userId.length; i++) {
				User user = userManager.getUserById(userId[i]);
				// 修改结束
				Userjoborg userjoborg = new Userjoborg();
				userjoborg.setUser(user);
				userjoborg.setJob(job);
				userjoborg.setOrg(org);
				userjoborg.setSameJobUserSn( new Integer(i + 1));
				userjoborg.setJobSn(new Integer(Integer.parseInt(jobSn) + 1));
				userManager.storeUserjoborg(userjoborg);
			}

		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 保存机构下的用户排序
	 * @param orgId
	 * @param userId
	 * @throws Exception 
	 * OrgJobAction.java
	 * @author: ge.tao
	 */
	public static String storeOrgUserOrder(String orgId, String[] userId) throws Exception {
		if(userId == null || userId.length <=0) return "false";
		
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			userManager.storeOrgUserOrder( orgId, userId);
		} catch (Exception e) {
			return "fail";
		}
		return "success";

	}

	/**
	 * 删除用户的机构岗位。机构管理－岗位设置－右边的调出（）
	 * 
	 * @param orgId
	 * @param jobId
	 * @param userList
	 * @return
	 * @throws Exception
	 */
	public static String deleteUserOrgJob(String orgId, String jobId,
			String[] userList) throws Exception {
		try {
			Organization org = new Organization();
			org.setOrgId(orgId);
			UserManager userManager = SecurityDatabase.getUserManager();
			Job job = new Job();
			job.setJobId(jobId);
			if (userList != null) {
				for (int i = 0; i < userList.length; i++) {
					String uid = userList[i];
//					System.out.println("......................"+uid);
					User user = userManager.getUserById(uid);
					if (user != null) {
						Userjoborg userjoborg = new Userjoborg();
						userjoborg.setJob(job);
						userjoborg.setOrg(org);
						userjoborg.setUser(user);
						userManager.deleteUserjoborg(userjoborg);
					}
				}
			}

		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	/**
	 * 保存用户的机构岗位。机构管理－岗位设置－右边的调入（）
	 * 
	 * @param orgId
	 * @param jobId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static String storeUserOrgJob(String orgId, String jobId,
			String userId,String jobSn) throws Exception {
		//System.out.println("jobSn........"+jobSn);
		int id=Integer.parseInt(jobSn)+1;
		try {
			Organization org = new Organization();
			org.setOrgId(orgId);
			UserManager userManager = SecurityDatabase.getUserManager();
			Job job = new Job();
			job.setJobId(jobId);
			User user = userManager.getUserById(userId);
			if (user != null) {
				//判断用户的SameJobUserSn
				List list = userManager.getUserList(org, job);
				if (list != null && list.size() > 0) {
					//用户的sn为最大的
					int n = list.size()+1;
					Userjoborg userjoborg = new Userjoborg();
					userjoborg.setJob(job);
					userjoborg.setOrg(org);
					userjoborg.setUser(user);
					userjoborg.setJobSn(new Integer(id));
					userjoborg.setSameJobUserSn(new Integer(n));
					userManager.storeUserjoborg(userjoborg);
				}
				else{
					Userjoborg userjoborg = new Userjoborg();
					userjoborg.setJob(job);
					userjoborg.setOrg(org);
					userjoborg.setUser(user);
					userjoborg.setJobSn(new Integer(id));
					userjoborg.setSameJobUserSn(new Integer(1));
					userManager.storeUserjoborg(userjoborg);
				}
			}

		} catch (Exception e) {
			//e.printStackTrace();			
			return "fail";
		}
		return "success";
	}
}
