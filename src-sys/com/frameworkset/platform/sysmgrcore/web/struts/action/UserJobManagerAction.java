package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
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
import com.frameworkset.platform.sysmgrcore.web.struts.form.UserJobManagerForm;


/**
 * 
 * <p>
 * Title:
 * </p>
 * 
 * <p> 
 * Description: 用户岗位管理Action
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
public class UserJobManagerAction extends DispatchAction implements Serializable {

	private static Logger logger = Logger.getLogger(UserJobManagerAction.class
			.getName());

	public UserJobManagerAction() {
	}

	/**
	 * 获得岗位列表，包括所有岗位和用户已经有的岗位
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
	public ActionForward getJobList(ActionMapping mapping, UserJobManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		//Integer uid = (Integer) session.getAttribute("currUserId");
		String uid2 = request.getParameter("userId");
		session.setAttribute("userId",uid2);
		Integer uid = Integer.valueOf(uid2);
		if (uid == null) {
			return mapping.findForward("noUser");
		}
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		String orgid = request.getParameter("orgId");

		Organization org = orgManager.getOrgById(orgid);
		UserJobManagerForm userJobForm = (UserJobManagerForm) form;
		userJobForm.setOrgId(orgid);
		userJobForm.setUserId(uid.toString());
		JobManager jobManager = SecurityDatabase.getJobManager();
		User user = new User();
		user.setUserId(uid);		
		List existJob = jobManager.getJobList(org,user);//用户在当前机构下的岗位
		List allJob = jobManager.getJobList(org);
		session.setAttribute("allJob", allJob);
		session.setAttribute("existJob", existJob);
		session.setAttribute("userJobForm", userJobForm);
		return mapping.findForward("jobDetail");
	}

	/**
	 * 保存用户岗位的更改
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
	public ActionForward storeUserJob(ActionMapping mapping, UserJobManagerForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//---------------START--用户组管理写操作日志
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
		String orgid = request.getParameter("orgId");
		UserManager userManager = SecurityDatabase.getUserManager();
		User user = userManager.getUserById(uid.toString());

		JobManager jobManager = SecurityDatabase.getJobManager();
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgid);
//		Job noJob = jobManager.getJob("jobId", "1");
		Job noJob = jobManager.getJobById("1");
		try {
			if (user != null && org != null) {
				// delete all job of this user
				userManager.deleteUserjoborg(user);
				UserJobManagerForm userJobForm = (UserJobManagerForm) form;
				String[] jobid = userJobForm.getJobId();
				String jobidss = "";
				for (int i = 0; (jobid != null) && (i < jobid.length); i++) {
//					Job job = jobManager.getJob("jobId", jobid[i]);
					Job job = jobManager.getJobById(jobid[i]);
					if (job != null) {
						// 用户除了待岗外还有其他岗位就要自动删除待岗的岗位
						if (job.getJobId().equals(noJob.getJobId())
								&& jobid.length > 1) {
							Userjoborg tmp = new Userjoborg();
							tmp.setUser(user);
							tmp.setOrg(org);
							tmp.setJob(job);
							userManager.deleteUserjoborg(tmp);
							continue;
						}
						Userjoborg userjoborg = new Userjoborg();
						userjoborg.setUser(user);
						userjoborg.setOrg(org);
						userjoborg.setJob(job);
						jobidss += LogGetNameById.getJobNameByJobId(jobid[i])+" ";
						userManager.storeUserjoborg(userjoborg);			
					}
				}
				//--资源管理写操作日志	
				operContent="存储用户岗位机构: 用户："+LogGetNameById.getUserNameByUserId(uid.toString())+" 机构："+LogGetNameById.getOrgNameByOrgId(orgid)+" 岗位："+jobidss; 
	
				 description="";
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
				//--
			}// end if
			else if (user == null) {
				return mapping.findForward("noUser");
			}

			// 将保存后的结果放到request中
			List existJob = jobManager.getJobList(user);
			List allJob = jobManager.getJobList(org);
			UserJobManagerForm userJobForm = (UserJobManagerForm) form;
			userJobForm.setOrgId(orgid);
			userJobForm.setUserId(uid.toString());
			request.setAttribute("allJob", allJob);
			request.setAttribute("existJob", existJob);
			request.setAttribute("userJobForm", userJobForm);

			return mapping.findForward("jobDetail");
		} catch (Exception e) {
			logger.error(e);
			return mapping.findForward("fail");
		}
	}
	/**隶属岗位－－保存用户岗位机构的关系
	 * store and delete userOrgJob by ajax the reference page is refresh auto (hongyu.deng)
	 * @param uid
	 * @param orgId
	 * @param jobid
	 * @return
	 */
	public static String storeUserOrgJob(Integer uid,String orgId,String[] jobid){
		
//			System.out.println("uid..........."+uid);
//			System.out.println("orgId............"+orgId);

			try {
				UserManager userManager = SecurityDatabase.getUserManager();
				return userManager.storeUserOrgJob(uid, orgId, jobid);
					
			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
		
	}
	/**
	 * 存储人员岗位和机构的关系，用户管理隶属机构中的调入
	 */
	public static String storeUJOAjax(String uid, String[] jobIds,String orgId) {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();
			return userManager.storeUJOAjax(uid, jobIds, orgId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		

		return "success";
	}
	/**隶属岗位－－删除用户岗位机构的关系
	 * store and delete userOrgJob by ajax the reference page is refresh auto (hongyu.deng)
	 * @param uid
	 * @param orgId
	 * @param jobid
	 * @return
	 */
	public static String deleteUserOrgJob(Integer uid,String orgId,String[] jobid){
	
	try {
		UserManager userManager = SecurityDatabase.getUserManager();
		return userManager.deleteUserOrgJob(uid, orgId, jobid);
	} catch (Exception e) {
		e.printStackTrace();
		return "fail";
	}
	}
	/**
	 * 删除人员岗位和机构的关系，用户管理隶属机构中的调入
	 */
	public static String deleteUJOAjax(String uid, String[] jobIds,String orgId) {
     
       try
       {
			UserManager userManager = SecurityDatabase.getUserManager();
			return userManager.deleteUJOAjax(uid,jobIds,orgId);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	public ActionForward getAllJobList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String[] id = request.getParameterValues("checkBoxOne");
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			String orgid = request.getParameter("orgId");
			Organization org = orgManager.getOrgById(orgid);
			JobManager jobManager = SecurityDatabase.getJobManager();
			List allJob = jobManager.getJobList(org);
			request.setAttribute("orgId",orgid);
			request.setAttribute("allJob", allJob);
			request.setAttribute("existJob", null);
            request.setAttribute("id", id);

		} catch (Exception e) {
			return mapping.findForward("fail");
		}

		return mapping.findForward("alljob");
		
	}
	public void storeUJOAjax_batch(String[] ids, String[] jobid,String orgid) throws ManagerException, SPIException {
	
		UserManager userManager = SecurityDatabase.getUserManager();
		try {
			userManager.storeUJOAjax_batch(ids, jobid, orgid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId) throws ManagerException {
		
		
		try {
			
			UserManager userManager = SecurityDatabase.getUserManager();
			userManager.deleteUJOAjax_batch(ids, jobid, orgId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
