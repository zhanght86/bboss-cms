package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl;
import com.frameworkset.platform.sysmgrcore.web.struts.form.OrgJobForm;
import com.frameworkset.common.poolman.DBUtil;

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
public class OrgJobAction extends BasicAction {
	private Logger logger = Logger.getLogger(OrgJobAction.class.getName());

	/**
	 * 保存机构及岗位的关系 机构管理－－岗位设置，左边的窗口右边列表的内容。包括排序的先后 修改
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	public ActionForward addorgandjob(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();//request.getRemoteAddr();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		String orgId = request.getParameter("orgId");
		OrgJobForm orgjobForm = (OrgJobForm) form;
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		JobManager jobManager = SecurityDatabase.getJobManager();
		UserManager userManager = SecurityDatabase.getUserManager();
		String[] jobId = orgjobForm.getJobId();
		Organization org = orgManager.getOrgById(orgId);
		try {
			// 岗位不为空就先删除旧的然后再一个一个保存
			if (org != null)
				orgManager.deleteOrgjob(org);
			for (int i = 0; (jobId != null) && (i < jobId.length); i++) {
				Orgjob orgjob = new Orgjob();
				Job job = new Job();
				// 新添加的岗位，必须把；去掉
				if (jobId[i].split(";").length > 1) {
					String[] tmp = jobId[i].split(";");
					job.setJobId(tmp[0]);
				}
				// 已经有的jobid就直接set
				else
					job.setJobId(jobId[i]);
				orgjob.setJob(job);
				orgjob.setOrganization(org);
				orgjob.setJobSn(new Integer(i + 1));
				orgManager.storeOrgjob(orgjob);
				
				//--岗位管理写操作日志	
				operContent="存储机构岗位,岗位："+orgjob.getJob().getJobName()+" 机构："+LogGetNameById.getOrgNameByOrgId(orgId); 						
				description="";
				logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
				//--
			}
			// 清除掉3元关系里面没有效的用户，即用户的岗位id在二元关系已经不存在了
			List job2 = jobManager.getJobList(org);
			List job3 = jobManager.getJobList(org);
			boolean isdel = true;
			for (int i = 0; i < job3.size(); i++) {
				Job tjob3 = (Job) job3.get(i);
				for (int j = 0; j < job2.size(); j++) {
					Job tjob2 = (Job) job2.get(j);
					if (tjob3.getJobId().equals(tjob2.getJobId())) {
						// 在二元里面找到了3元里面的jobid就不删了
						isdel = false;
						break;
					}
				}
				// 如果isdel为真就是上面没有找到。要删掉
				if (isdel && org != null) {
					userManager.deleteUserjoborg(tjob3, org);
				}
			}

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		HttpSession session = request.getSession();

		List joblist = jobManager.getJobList();
		List orgjoblist = null;
		if (org != null) {
			orgjoblist = jobManager.getJobList(org);
		}
		session.setAttribute("joblist", joblist);
		session.setAttribute("orgjoblist", orgjoblist);
		session.setAttribute("orgjobForm", orgjobForm);
		return mapping.findForward("showJobList");
	}

	public ActionForward showOrgallJob(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String orgId = request.getParameter("orgId");
		OrgJobForm orgjobForm = (OrgJobForm) form;
		orgjobForm.setOrgId(orgId);

		JobManager jobManager = SecurityDatabase.getJobManager();
		Orgjob orgjob = new Orgjob();
		List joblist = jobManager.getJobList();
		Organization org = new Organization();
		org.setOrgId(orgId);

		List orgjoblist = jobManager.getJobList(org);
		HttpSession session = request.getSession();
		session.setAttribute("joblist", joblist);
		session.setAttribute("orgjoblist", orgjoblist);
		session.setAttribute("orgjobForm", orgjobForm);
		// session.setAttribute("ccc_orgId",orgId);
		// request.setAttribute("joblist", joblist);
		// request.setAttribute("orgjoblist", orgjoblist);
		// request.setAttribute("orgjobForm", orgjobForm);
		return (mapping.findForward("showOrgallJob"));
	}

	public ActionForward deleteOrgJob(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
		OrgJobForm orgjobForm = (OrgJobForm) form;
		OrgManager orgManager = new OrgManagerImpl();
		Orgjob orgjob = new Orgjob();
		//--岗位管理写操作日志	
		operContent="删除机构岗位,岗位名称："+orgjob.getJob().getJobName()+" 机构名称："+orgjob.getOrganization().getOrgName(); ; 						
		description="";
		logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
		//--		
		orgManager.deleteOrgjob(orgjob);
		

		return (mapping.findForward("deleteOrgJob"));
	}

	/**
	 * 得到一个机构下某一岗位的用户列表 修改
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getUserList(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {// 得到机构岗位下的用户

		String orgId = request.getParameter("orgId");
		String jobId = request.getParameter("curjobid");
		Organization org = new Organization();
		org.setOrgId(orgId);
		// JobManager jobManager = SecurityDatabase.getJobManager();
		// List joblist = jobManager.getJobList();
		// List orgjoblist = jobManager.getJobList(org);
		OrgJobForm orgJobForm = (OrgJobForm) form;
		orgJobForm.setOrgId(orgId);
		orgJobForm.setCurJobId(jobId);

		Job job = new Job();
		job.setJobId(jobId);
		UserManager userManager = SecurityDatabase.getUserManager();
		//List userList = userManager.getUserList(org, job);
//		List userList=userManager.getUserList("from User user where user.userId in (select ujo.id.userId from Userjoborg ujo where "
//								+ "ujo.id.jobId = '"
//								+ jobId
//								+ "' and ujo.id.orgId = '"
//								+ orgId
//								+ "')order by user.userSn asc");
		String sql="select a.* from td_sm_user a , (select t.user_id from td_sm_userjoborg t where t.org_id='"
			+ orgId + "' and t.job_id='"+ jobId +"' order by t.same_job_user_sn asc) " +
					" b where a.user_id in b.user_id ";
		List userList=userManager.getUserList(sql);
		request.setAttribute("userList", userList);
		request.setAttribute("orgJobForm", orgJobForm);
		// 将右边所有的按钮设置为可用
		request.setAttribute("dflag", "1");
		return (mapping.findForward("showUserList"));
	}

	public ActionForward adduser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {// 添加用户机构和岗位
		
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        

		String orgId = request.getParameter("orgId");
		String jobId = request.getParameter("jobId");
		String userId = request.getParameter("userId");
		try {
			UserManagerImpl userMgr = new UserManagerImpl();
			User user = new User();
			user.setUserId(Integer.valueOf(userId));
			Job job = new Job();
			job.setJobId(jobId);
			Organization org = new Organization();
			org.setOrgId(orgId);
			Userjoborg userjoborg = new Userjoborg();
			userjoborg.setUser(user);
			userjoborg.setJob(job);
			userjoborg.setOrg(org);
			userMgr.storeUserjoborg(userjoborg);
			//--岗位管理写操作日志	
			operContent="添加用户到机构岗位,用户："+LogGetNameById.getUserNameByUserId(userId)+" ,机构："+LogGetNameById.getOrgNameByOrgId(orgId)+" 岗位："+LogGetNameById.getJobNameByJobId(jobId); 						
			description="";
			logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
			//--
			org.setOrgId(orgId);
			JobManager jobManager = SecurityDatabase.getJobManager();
			List joblist = jobManager.getJobList();
			List orgjoblist = jobManager.getJobList(org);
			UserManager userManager = SecurityDatabase.getUserManager();
			List userList = userManager.getUserList(org, job);
			request.setAttribute("userList", userList);
			request.setAttribute("joblist", joblist);
			request.setAttribute("orgjoblist", orgjoblist);
			request.setAttribute("orgId", orgId);
			request.setAttribute("curjobid", jobId);
		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return (mapping.findForward("showOrgallJob"));
	}
	
	/**
	 * 未使用的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	public ActionForward deleteJob(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
        
        
		String orgId = request.getParameter("orgId");
		String jobId = request.getParameter("curjobid");
		OrgJobForm form1 = (OrgJobForm) form;
		String[] jobId1 = form1.getJobId();
		String all = request.getParameter("all");
		Organization org = new Organization();
		org.setOrgId(orgId);
		UserManager userManager = SecurityDatabase.getUserManager();
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		try {
			if (all != null && all.equals("1")) {
				orgManager.deleteOrgjob(org);
				// userManager.deleteUserjoborg(org);
			} else {
				Job job = new Job();
				job.setJobId(jobId);
				Userjoborg userjoborg = new Userjoborg();
				userjoborg.setJob(job);
				userjoborg.setOrg(org);
				Orgjob orgjob = new Orgjob();
				orgjob.setJob(job);
				orgjob.setOrganization(org);
				// delete
				orgManager.deleteOrgjob(orgjob);
				
				//--岗位管理写操作日志	
				operContent="删除岗机构岗位，岗位："+LogGetNameById.getJobNameByJobId(jobId)+" 机构："+ LogGetNameById.getOrgNameByOrgId(orgId); 						
				description="";
				logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
				//--
				userManager.deleteUserjoborg(job, org);
				List userList = userManager.getUserList(org, job);
				request.setAttribute("userList", userList);
			}
			JobManager jobManager = SecurityDatabase.getJobManager();
			List joblist = jobManager.getJobList();
			List orgjoblist = jobManager.getJobList(org);
			HttpSession session = request.getSession();
			session.setAttribute("joblist", joblist);
			session.setAttribute("orgjoblist", orgjoblist);
			session.setAttribute("orgId", orgId);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return (mapping.findForward("showOrgallJob"));
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	public ActionForward addJob(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		

		String orgId = request.getParameter("orgId");
		// String jobId=request.getParameter("jobId");
		OrgJobForm form1 = (OrgJobForm) form;
		Organization org = new Organization();
		org.setOrgId(orgId);
		String[] jobId1 = form1.getAllist();
		String all = request.getParameter("all");
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		try {
			if (all != null && all.equals("1")) {
				orgManager.deleteOrgjob(org);
				for (int i = 0; i < jobId1.length; i++) {
					String jobId = jobId1[i];
					Job job = new Job();
					job.setJobId(jobId);

					Orgjob orgjob = new Orgjob();
					orgjob.setJob(job);
					orgjob.setOrganization(org);
					// delete
					orgManager.storeOrgjob(orgjob);
					
					//--岗位管理写操作日志	
					operContent="存储岗位: "+orgjob.getJob().getJobName(); 						
					description="";
					logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
					//--
					
					request.setAttribute("curjobid", jobId1[0]);
				}
			}
			// add one
			else {
				String onejob = request.getParameter("jobId");
				Job job = new Job();
				job.setJobId(onejob);
				Orgjob orgjob = new Orgjob();
				orgjob.setJob(job);
				orgjob.setOrganization(org);
				// add
				orgManager.storeOrgjob(orgjob);
				
				//--岗位管理写操作日志	
				operContent="存储岗位: "+orgjob.getJob().getJobName(); 						
				description="";
				logManager.log(control.getUserAccount()+":"+userName,operContent,openModle,operSource,description);       
				//--
				request.setAttribute("curjobid", onejob);
			}
			JobManager jobManager = SecurityDatabase.getJobManager();
			List joblist = jobManager.getJobList();
			List orgjoblist = jobManager.getJobList(org);

			HttpSession session = request.getSession();
			session.setAttribute("joblist", joblist);
			session.setAttribute("orgjoblist", orgjoblist);
			session.setAttribute("orgId", orgId);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return (mapping.findForward("showOrgallJob"));
	}

	/**
	 * 机构管理==>岗位设置==>删除选中岗位下的用户 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteUser(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END

		OrgJobForm form1 = (OrgJobForm) form;
		String orgId = form1.getOrgId();
		String jobId = form1.getCurJobId();
		String[] userList = form1.getUserList();
		Organization org = new Organization();
		org.setOrgId(orgId);
		UserManager userManager = SecurityDatabase.getUserManager();
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		JobManager jobManager = SecurityDatabase.getJobManager();
		String dflag="1";
		request.setAttribute("dflag",dflag);
		try {
			Job job = new Job();
			job.setJobId(jobId);
			if (userList != null) {
				for (int i = 0; i < userList.length; i++) {
					String uid = userList[i];
					DBUtil db = new DBUtil();
					DBUtil db2 = new DBUtil();
					String sql ="select * from TD_SM_USERJOBORG where job_id ='"+ jobId +"' and" +
					" org_id ='"+ orgId +"' and user_id ="+ uid +"";
					db2.executeSelect(sql);
					for(int j= 0; j < db2.size(); j ++)
						{
							int userid=db2.getInt(j,"user_id");
							String jid = db2.getString(j,"JOB_ID");
							String oid = db2.getString(j,"ORG_ID");
							Date starttime = db2.getDate(j,"JOB_STARTTIME");
							String sql2 ="insert into TD_SM_USERJOBORG_HISTORY values("+ userid +",'"+ jid +"'," +
									"'"+ oid +"',"+ DBUtil.getDBDate(starttime)+"," +
									""+ DBUtil.getDBAdapter().to_date(new Date())+",0)";
							//System.out.println(sql2);
							db2.executeInsert(sql2);
						}
					
					
					
//					DBUtil db1 = new DBUtil();
//					String str ="delete from TD_SM_USERJOBORG where job_id ='"+ jobId +"' and" +
//					" org_id ='"+ orgId +"' and user_id ="+ uid +"";
//					db1.executeDelete(str);
					Organization org1 = orgManager.getOrgById(orgId);
//					Job job1 = jobManager.getJob("jobId",jobId);
					Job job1 = jobManager.getJobById(jobId);
					User user1 = userManager.getUserById(uid);
					Userjoborg u = new Userjoborg();
					u.setOrg(org1);
					u.setUser(user1);
					u.setJob(job1);
					
					//--岗位管理写操作日志	
					operContent="删除岗位下的用户,岗位："+LogGetNameById.getJobNameByJobId(jobId)+" 用户："+LogGetNameById.getUserNameByUserId(uid); 						
					description="";
					logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
					//--
					
					userManager.deleteUserjoborg(u);
					
					
					User user = userManager.getUserById(uid);
					
						
						//-----如果该用户的主要单位为该机构，删除用户和该机构关系
						
						String strsql ="select * from td_sm_orguser where org_id ='"+ orgId +"' and user_id =" + uid + "";
						db.executeSelect(strsql);
						if(db.size()>0){
							orgManager.deleteMainOrgnazitionOfUser(uid);
						}
						//------------------------------------------------------
						
//						Userjoborg userjoborg = new Userjoborg();
//						userjoborg.setJob(job);
//						userjoborg.setOrg(org);
//						userjoborg.setUser(user);
//						userManager.deleteUserjoborg(userjoborg);
					
//					存数据到历史表TD_SM_USERJOBORG_HISTORY---------------------------------
					
//					for(int j= 0; j < db.size(); j ++)
//					{
//						int userid=db.getInt(j,"user_id");
//						String jid = db.getString(j,"JOB_ID");
//						String oid = db.getString(j,"ORG_ID");
//						Date starttime = db.getDate(j,"JOB_STARTTIME");
//						String sql2 ="insert into TD_SM_USERJOBORG_HISTORY values("+ userid +",'"+ jid +"'," +
//								"'"+ oid +"',"+ DBUtil.getDBAdapter().sysdate()+"," +
//								""+ DBUtil.getDBAdapter().sysdate()+",0)";
//						//System.out.println(sql2);
//						db.executeInsert(sql2);
//					}
					//---------------------------------------------------------------------------

				}
			}
			List userList1 = userManager.getUserList(org, job);
			request.setAttribute("userList", userList1);
			request.setAttribute("orgJobForm", form1);
	
		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return (mapping.findForward("showUserList"));
	}

	/**
	 * 机构管理==>岗位设置==>保存选中岗位和机构的关系 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward storeOrgjob(ActionMapping mapping, OrgJobForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="岗位管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		String orgId = request.getParameter("orgId");
		OrgJobForm form1 = (OrgJobForm) form;
		String[] jobid = form1.getJobId();
		Organization org = new Organization();
		org.setOrgId(orgId);
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		try {
			if (jobid != null) {

				for (int i = 0; i < jobid.length; i++) {
					Job job = new Job();
					job.setJobId(jobid[i]);
					Orgjob orgjob = new Orgjob();
					orgjob.setJob(job);
					orgjob.setOrganization(org);
					orgjob.setJobSn(new Integer(i + 1));
					orgManager.storeOrgjob(orgjob);
					
					//--岗位管理写操作日志	
					operContent="保存选中岗位和机构的关系,岗位："+LogGetNameById.getJobNameByJobId(jobid[i])+" 机构："+LogGetNameById.getOrgNameByOrgId(orgId); 						
					description="";
					logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
					//--
				}
			}

			JobManager jobManager = SecurityDatabase.getJobManager();
			List joblist = jobManager.getJobList();
			List orgjoblist = jobManager.getJobList(org);
			HttpSession session = request.getSession();
			session.setAttribute("joblist", joblist);
			session.setAttribute("orgjoblist", orgjoblist);
			session.setAttribute("orgId", orgId);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return (mapping.findForward("showOrgallJob"));
	}

	/**
	 * 机构管理==>岗位设置==>保存选中用户、岗位和机构的关系
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward storeUserjoborg(ActionMapping mapping,
			OrgJobForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		OrgJobForm form1 = (OrgJobForm) form;
		String orgId = form1.getOrgId();
		String jobId = form1.getCurJobId();
		String[] userList = form1.getUserList();

		UserManager userManager = SecurityDatabase.getUserManager();

		// 修改：需要完整用户对象信息
		// Organization org = new Organization();
		// org.setOrgId(orgId);
		OrgManager orgMgr = SecurityDatabase.getOrgManager();
		Organization org = orgMgr.getOrgById(orgId);
		// 修改结束
		try {
			Job job = new Job();
			job.setJobId(jobId);
			if (userList != null) {
				userManager.deleteUserjoborg(job, org);
				String userLists_log = "";
				for (int i = 0; i < userList.length; i++) {
					// 修改：需要完成用户对象信息
					// User user = new User();
					// user.setUserId(Integer.valueOf(userList[i]));
					User user = userManager.getUserById(userList[i]);
					// 修改结束

					Userjoborg userjoborg = new Userjoborg();
					userjoborg.setUser(user);
					userjoborg.setJob(job);
					userjoborg.setOrg(org);
					userjoborg.setSameJobUserSn(new Integer(i + 1));
					userjoborg.setJobSn(new Integer(Integer.parseInt(form1
							.getJobSn()) + 1));
					userLists_log += LogGetNameById.getUserNameByUserId(userList[i])+" ";
					userManager.storeUserjoborg(userjoborg);
					

				}
				//--岗位管理写操作日志	
				operContent="修改用户岗位机构关系,用户："+userLists_log+" ，岗位："+LogGetNameById.getJobNameByJobId(jobId)+" ,机构："+LogGetNameById.getOrgNameByOrgId(orgId); 						
				description="";
				logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);       
				//--
			}
			List userList1 = userManager.getUserList(org, job);
			request.setAttribute("userList", userList1);
			request.setAttribute("orgJobForm", form1);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}
		return (mapping.findForward("showUserList"));
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
