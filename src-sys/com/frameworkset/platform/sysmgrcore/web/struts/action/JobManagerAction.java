//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.LogGetNameById;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;

/**
 * MyEclipse Struts Creation date: 03-10-2006
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/jobManager" name="jobManagerForm"
 *                input="/form/jobManager.jsp" scope="request" validate="true"
 */
public class JobManagerAction extends DispatchAction implements Serializable {

	public JobManagerAction() {
	}

//	public ActionForward getJobInfo(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		Job job = (Job) form;
//
//		String selected = request.getParameter("jobId");
//		JobManager jobManager = SecurityDatabase.getJobManager();
//
////		job = jobManager.getJob("jobId", selected);
//		job = jobManager.getJobById(selected);
//		request.setAttribute("Job", job);
//
//		ActionForward forward = mapping.findForward("main");
//		StringBuffer path = new StringBuffer(forward.getPath());
//		boolean isQuery = (path.indexOf("?") >= 0);
//
//		if (isQuery) {
//			path.append("&jobId=" + selected);
//		} else {
//			path.append("?jobId=" + selected);
//		}
//		return new ActionForward(path.toString());
//
//	}
//
//	public ActionForward deleteJob(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//		//---------------START--岗位管理写操作日志
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=control.getMachinedID();//request.getRemoteAddr();
//        String openModle="岗位管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 		
//		//---------------END
//        
//		Job job = (Job) form;
//		String jobId = job.getJobId();
//		String jobName = job.getJobName();
//		
//		JobManager jobManager = SecurityDatabase.getJobManager();
//
//		if (jobId.equals("1")) {
//			// return new ActionForward(mapping.getInput());
//			request.setAttribute("isJobExist1", "true");
//			return mapping.findForward("main");
//		} else {
//			
//
//			//--岗位管理写操作日志	
//			operContent="删除岗位: "+jobName;						
//			description="";
//			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
//			//--
//			jobManager.deleteJob(job);
//			request.setAttribute("action","delete");
//			ActionForward forward = mapping.findForward("info");
//			StringBuffer path = new StringBuffer(forward.getPath());
//
//			boolean isQuery = (path.indexOf("?") >= 0);
//
//			if (isQuery) {
//				path.append("&method=getJobInfo&jobId=" + job.getJobId()
//						+ "&action=update&jobId=" + job.getJobId());
//			} else {
//				path.append("?method=getJobInfo&jobId=" + job.getJobId()
//						+ "&action=update&jobId=" + job.getJobId());
//			}
//			return new ActionForward(path.toString());
//		}
//
//	}

//	public ActionForward updateJob(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//		//---------------START--岗位管理写操作日志
//		AccessControl control = AccessControl.getInstance();
//		control.checkAccess(request,response);
//		String operContent="";        
//        String operSource=control.getMachinedID();
//        String openModle="岗位管理";
//        String userName = control.getUserName();
//        String description="";
//        LogManager logManager = SecurityDatabase.getLogManager(); 		
//		//---------------END
//		
//
//		Job job = (Job) form;
//		JobManager jobManager = SecurityDatabase.getJobManager();
//		String jobName = job.getJobName();
//		String jobNumber = job.getJobNumber();
//		String jobId = job.getJobId();
//		
//		boolean b = false;
//		boolean b1 = false;
//		try {
//			b = jobManager.isJobExist(jobName);
//			b1 = jobManager.isJobExistNumber(jobId, jobNumber);
//
//		} catch (ManagerException e1) {
//			e1.printStackTrace();
//		}
//		if (b1 == true) {
//			request.setAttribute("isJobExistNumber", "true");
//			return mapping.findForward("main");
//		}
//		if (jobId.equals("1")) {
//			request.setAttribute("isJobExist", "true");
//			return mapping.findForward("main");
//		}
//		jobManager.storeJob(job);
//		
//		
//		//--岗位管理写操作日志	
//		operContent="修改岗位:"+job.getJobName(); 						
//		description="";
//		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
//		//--
//		
//
//		ActionForward forward = mapping.findForward("updatejob");
//		StringBuffer path = new StringBuffer(forward.getPath());
//
//		boolean isQuery = (path.indexOf("?") >= 0);
//
//		if (isQuery) {
//			path.append("&method=getJobInfo&jobId=" + job.getJobId()
//					+ "&action=update&jobId=" + job.getJobId());
//		} else {
//			path.append("?method=getJobInfo&jobId=" + job.getJobId()
//					+ "&action=update&jobId=" + job.getJobId());
//		}
//		return new ActionForward(path.toString());
//		// return (mapping.findForward("main"));
//	}

	public ActionForward deletejobid(ActionMapping mapping, ActionForm form,
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
        
		String jobid = request.getParameter("jobId");
		request.setAttribute("jobId", jobid);
		JobManager jobManager = SecurityDatabase.getJobManager();
		if (jobid.equals("1")) {
			return mapping.findForward("jobsucceed");
		}
		
		//		--岗位管理写操作日志	
		operContent="删除岗位："+LogGetNameById.getJobNameByJobId(jobid); 						
		description="";
		logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
		//--
		
		jobManager.deleteJob(jobid);
		ActionForward forward = mapping.findForward("info");
		StringBuffer path = new StringBuffer(forward.getPath());

		boolean isQuery = (path.indexOf("?") >= 0);

		if (isQuery) {
			path.append("&method=getJobInfo&jobId=" + jobid
					+ "&action=update&jobId=" + jobid);
		} else {
			path.append("?method=getJobInfo&jobId=" + jobid
					+ "&action=update&jobId=" + jobid);
		}
		return new ActionForward(path.toString());
		

	}

	public ActionForward deletesuborg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {// 删除机构和岗位的关系
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
		
		
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		String[] orgid = request.getParameterValues("ID");
		String jobId = request.getParameter("jobId");
		request.setAttribute("jobId", jobId);
		String  orgids_log = "";
		if (orgid != null) {
			for (int i = 0; i < orgid.length; i++) {
				OrgManagerImpl orgMgr = new OrgManagerImpl();
				Job job = new Job();
				job.setJobId(jobId);
				Organization org = new Organization();
				org.setOrgId(orgid[i]);
				Orgjob oj = new Orgjob();
				oj.setJob(job);
				oj.setOrganization(org);
				orgids_log += LogGetNameById.getGroupNameByGroupId(orgid[i])+ " ";
				orgMgr.deleteOrgjob(oj);
			}
			String jobNames = LogGetNameById.getJobNameByJobId(jobId);
			
			//--岗位管理写操作日志	
			operContent="删除机构岗位,岗位名称："+jobNames+" ,机构名称："+orgids_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
		}

		return mapping.findForward("jobsuborglist");

	}

	/**
	 * 得到机构列表和该岗位下拥有的机构列表 2006-07-11
	 * 
	 */
	public ActionForward getOrgList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String jobId = request.getParameter("jobId");
		request.setAttribute("jobId", jobId);

		String orgId = request.getParameter("orgId");
		// System.out.println("jobId........"+jobId);
		if (orgId == null)
			orgId = "0";
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Job job = new Job();
		job.setJobId(jobId);
		List existOrg = orgManager.getOrgList(job); // 该岗位关联机构
		List allOrg;
		Organization org = new Organization();
		org.setOrgId(orgId);
		orgManager.isContainChildOrg(org);// 检查指定的机构是否包含子机构
		if (orgManager.isContainChildOrg(org)) {
			allOrg = orgManager
					.getOrgList("select * from td_sm_organization where parent_id='"
							+ orgId + "' or org_id='" + orgId + "'");
		} else {
			allOrg = orgManager
					.getOrgList("select * from td_sm_organization o where o.orgId='" + orgId
							+ "'");
		}
		request.setAttribute("existOrg", existOrg);
		request.setAttribute("allOrg", allOrg);

		return mapping.findForward("orgajax");
	}

	/**
	 * 存储岗位机构--ajax 2006-07-11
	 */
	public static String storeJobOrgAjax(String jobId, String[] orgIds) {
		// System.out.println("jobId............."+jobId);
		try {
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			JobManager jobManager = SecurityDatabase.getJobManager();
			Job job = jobManager.getJob("jobId", jobId);
			jobManager.deleteOrgjob(job);

			for (int i = 0; (orgIds != null) && (i < orgIds.length); i++) {
				Organization org = orgManager.getOrgById(orgIds[i]);
				if (org != null) {
					Orgjob oj = new Orgjob();
					oj.setOrganization(org);
					oj.setJob(job);
					/**
					 * add by ge.tao
					 * date 2007-10-25 
					 * 设置 TD_SM_ORGJOB表中, 岗位的排序号, 缺省都给1
					 */					
					oj.setJobSn(new Integer(1));
					orgManager.storeOrgjob(oj);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	public ActionForward deleteJobAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] id = request.getParameterValues("checkBoxOne");
		
		if (id != null) {
			for (int i = 0; i < id.length; i++) {
				
				JobManager jobManager = SecurityDatabase.getJobManager();
//				Job job = jobManager.getJob("jobId",id[i]);
				Job job = jobManager.getJobById(id[i]);
				jobManager.deleteJob(job);
			}
		}
		request.setAttribute("action","update");		
		return mapping.findForward("info");
	}
}
