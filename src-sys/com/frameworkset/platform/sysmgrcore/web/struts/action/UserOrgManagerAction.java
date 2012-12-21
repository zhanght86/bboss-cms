package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.OrgJobName;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm;
import com.frameworkset.common.poolman.DBUtil;

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
public class UserOrgManagerAction extends DispatchAction  implements Serializable {
	public UserOrgManagerAction() {
	}

	private static Logger log = Logger.getLogger(UserOrgManagerAction.class
			.getName());

	public ActionForward getOrgList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DBUtil db = new DBUtil(); 
		String uid2 = request.getParameter("userId");
		 request.setAttribute("userId",uid2);
		 Integer uid = Integer.valueOf(uid2);
		 String orgId = request.getParameter("orgId");
		 request.setAttribute("orgId",orgId);
		 OrgManager orgManager = SecurityDatabase.getOrgManager();
		 UserManager userManager = SecurityDatabase.getUserManager();
		 User user1 = userManager.getUserById(uid.toString());
		 Organization ch = orgManager.getMainOrganizationOfUser(user1.getUserName());
//    	 System.out.println(ch);
//		 System.out.println("||||||||||||||||||"+ch.getRemark5());
		 if(ch==null || ch.equals("null")){
			 request.setAttribute("orgname","未设主机构！");
		 }else{
			 request.setAttribute("orgname",ch.getRemark5());
			 request.setAttribute("mainOrgId",ch.getOrgId());//将主机构的id传到页面上,da.wei
			 
		 }
		 
		try {
			if (uid == null) {
				return mapping.findForward("noUser");
			}

			UserOrgManagerForm userOrgForm = null;
			User user = new User();
			user.setUserId(uid);
			List existOrg = new ArrayList();
			JobManager jobManager = SecurityDatabase.getJobManager();
//			Job job = jobManager.getJob("jobId", "1"); 
			Job job = jobManager.getJobById("1"); 
			
//			user = userManager.loadAssociatedSet(uid.toString(),
//					UserManager.ASSOCIATED_USERJOBORGSET);
			if (user != null) {
//				Iterator iterator = user.getUserjoborgSet().iterator();
//				while (iterator.hasNext()) {
//					userOrgForm = new UserOrgManagerForm();
//					Userjoborg userjoborg = (Userjoborg) iterator.next();
//					
//					userOrgForm.setOrgId(userjoborg.getOrg().getOrgId() + ";"
//							+ userjoborg.getJob().getJobId());
//					
//					userOrgForm.setOrgName(userjoborg.getOrg().getOrgName()
//							+ "(" + userjoborg.getJob().getJobName() + ")");
//					userOrgForm.setRemark5(userjoborg.getOrg().getRemark5()
//							+ "(" + userjoborg.getJob().getJobName() + ")"); 
//					existOrg.add(userOrgForm);
//				}
				String sql ="select org_id,job_id from td_sm_userjoborg where user_id ="+ uid2 +"";
				db.executeSelect(sql);
				for (int i = 0; i < db.size(); i++) {
				Organization org1 = orgManager.getOrgById(db.getString(i,"org_id"));
//				Job job1 = jobManager.getJob("jobId",db.getString(i,"job_id"));
				Job job1 = jobManager.getJobById(db.getString(i,"job_id"));
				OrgJobName ojn = new OrgJobName();
				ojn.setOrgId(db.getString(i,"org_id")+";"+db.getString(i,"job_id"));
				
//				不是取getOrgName,而是要remark5,weida200709121817
//				ojn.setRemark5("("+ org1.getOrgName()+")"+ job1.getJobName());
				ojn.setRemark5("("+ org1.getRemark5() + ")" + job1.getJobName());
				
				existOrg.add(ojn);
	        }
				request.setAttribute("existOrg", existOrg);
				request.setAttribute("jobid", job.getJobId());
				
			} else {
				return mapping.findForward("noUser");
			}
		} catch (Exception e) {
			return mapping.findForward("fail");
		}
			return mapping.findForward("orgDetail");
	}

	/**
	 * 获取机构列表包括用户已经属于的机构
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward storeUserOrg(ActionMapping mapping, UserOrgManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();//request.getRemoteAddr();
        String openModle="用户管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		HttpSession session = request.getSession();
		Integer uid = (Integer) session.getAttribute("currUserId");
		if (uid == null) {
			return mapping.findForward("noUser");
		}
		UserOrgManagerForm userOrgForm = (UserOrgManagerForm) form;
		String[] orgIdName = userOrgForm.getOrgList();
		UserManager userManager = SecurityDatabase.getUserManager();
		User user = userManager.getUserById(uid.toString());
		List existOrg = new ArrayList();
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		try {
			if (user != null) {
				JobManager jobManager = SecurityDatabase.getJobManager();
				String orgId;
				String jobId;
				userManager.deleteUserjoborg(user);
				
				////-- 日志用到的局部变量
				String orgIds_log = "";
				String jobIds_log = "";
				////
				for (int i = 0; orgIdName != null && i < orgIdName.length; i++) {
					String[] tmp = orgIdName[i].split(";");
					orgId = tmp[0];
					jobId = tmp[1];
					orgIds_log += LogGetNameById.getOrgNameByOrgId(orgId)+" ";
					jobIds_log += LogGetNameById.getJobNameByJobId(jobId)+" ";
					Organization org = orgManager.getOrgById(orgId);
//					Job job = jobManager.getJob("jobId", jobId);
					Job job = jobManager.getJobById(jobId);				
					if (org != null && job != null) {
						Userjoborg u = new Userjoborg();
						u.setOrg(org);
						u.setUser(user);
						u.setJob(job);
						userManager.storeUserjoborg(u);
					}
				}
				
				//--添加用户时候写日志	
				operContent="存储用户岗位机构,用户："+user.getUserName()+" 机构:"+orgIds_log+" 岗位:"+jobIds_log; 
				
				 description="";
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
				//--

			}

			// update session
			userManager = SecurityDatabase.getUserManager();
			user = userManager.loadAssociatedSet(uid.toString(),
					UserManager.ASSOCIATED_USERJOBORGSET);

			Iterator iterator = user.getUserjoborgSet().iterator();
			while (iterator.hasNext()) {
				userOrgForm = new UserOrgManagerForm();
				Userjoborg userjoborg = (Userjoborg) iterator.next();
				userOrgForm.setOrgId(userjoborg.getOrg().getOrgId() + ";"
						+ userjoborg.getJob().getJobId());
				userOrgForm.setOrgName(userjoborg.getOrg().getOrgName() + "("
						+ userjoborg.getJob().getJobName() + ")");
				existOrg.add(userOrgForm);
			}
			request.setAttribute("existOrg", existOrg);
			return mapping.findForward("orgDetail");
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("fail");
		}
	}

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

	/**
	 * 删除一个机构
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward deleteUserOrg(ActionMapping mapping, UserOrgManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="用户管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		HttpSession session = request.getSession();
		Integer uid = (Integer) session.getAttribute("currUserId");
		if (uid == null) {
			return mapping.findForward("noUser");
		}
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			User user = userManager.getUserById(uid.toString());
			UserOrgManagerForm userOrgForm = (UserOrgManagerForm) form;
			String[] all = userOrgForm.getOrgList();
			List existOrg = new ArrayList();
			OrgManager orgManager = SecurityDatabase.getOrgManager();

			if (user != null) {
				JobManager jobManager = SecurityDatabase.getJobManager();
				
				////-- 日志用到的局部变量
				String orgNames_log = "";
				String jobNames_log = "";
				////
				for (int i = 0; all != null && i < all.length; i++) {
					String[] tmp = all[i].split(";");
					String orgId = tmp[0];					
					String jobId = tmp[1];
					orgNames_log += LogGetNameById.getOrgNameByOrgId(orgId)+"";
					jobNames_log += LogGetNameById.getJobNameByJobId(jobId)+"";
//					Job job = jobManager.getJob("jobId", jobId);
					Job job = jobManager.getJobById(jobId);
					Organization org = orgManager.getOrgById(orgId);
					if (job != null && org != null) {
						Userjoborg u = new Userjoborg();
						u.setOrg(org);
						u.setUser(user);
						u.setJob(job);
						userManager.deleteUserjoborg(u);
					}
				}
				
				
				//--添加用户时候写日志	
				operContent="删除用户与用户机构岗位,用户："+user.getUserName()+" 机构："+orgNames_log+" 岗位："+jobNames_log; 
				
				 description="";
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
				//--
			}
			// update session
			userManager = SecurityDatabase.getUserManager();
			user = userManager.loadAssociatedSet(uid.toString(),
					UserManager.ASSOCIATED_USERJOBORGSET);

			Iterator iterator = user.getUserjoborgSet().iterator();
			while (iterator.hasNext()) {
				userOrgForm = new UserOrgManagerForm();
				Userjoborg userjoborg = (Userjoborg) iterator.next();
				userOrgForm.setOrgId(userjoborg.getOrg().getOrgId() + ";"
						+ userjoborg.getJob().getJobId());
				userOrgForm.setOrgName(userjoborg.getOrg().getOrgName() + "("
						+ userjoborg.getJob().getJobName() + ")");
				existOrg.add(userOrgForm);
			}
			request.setAttribute("existOrg", existOrg);
			// request.setAttribute("jobid", job.getJobId());
			request.setAttribute("flag", "1");

			return mapping.findForward("orgDetail");
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("fail");
		}
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
	
	public ActionForward getAllOrgList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
			String[] id = request.getParameterValues("checkBoxOne");
			
            request.setAttribute("id", id);

		
//		System.out.println("aaaaaaaaaaa");
		return mapping.findForward("batch2org");		
	}
	
	public ActionForward getbatchOrgList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		
		String orgId=request.getParameter("orgId");
//		System.out.println("........................"+orgId);
		request.setAttribute("orgId",orgId);
		String userId=request.getParameter("uid");
		request.setAttribute("uid",userId);
		String username = request.getParameter("username");
		request.setAttribute("username",username);
//		System.out.println("........................"+userId);
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		
		JobManager jobManager=SecurityDatabase.getJobManager();
		Organization org = orgManager.getOrgById(orgId);
	
		List allOrg =jobManager.getJobList(org);		//根据机构取岗位列表
		request.setAttribute("allOrg", allOrg);

		return mapping.findForward("batchorg");
		
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
