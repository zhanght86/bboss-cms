//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.0/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.JobManagerImpl;
import com.frameworkset.platform.sysmgrcore.web.struts.form.JobaddForm;
/**
 * MyEclipse Struts Creation date: 03-14-2006
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/jobAdd" name="jobAddForm"
 *                input="/jobmanager/A03/addjob.jsp" scope="request"
 *                validate="true"
 */
public class JobAddAction extends Action implements Serializable{
	private static Logger logger = Logger.getLogger(JobAddAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, JobaddForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//---------------START--岗位管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();//request.getRemoteAddr();
        String openModle="岗位管理";
        String userName = control.getUserName();
        String description="";  
        LogManager logManager = SecurityDatabase.getLogManager();
        	
		//---------------END

		boolean b1 = false;
		boolean b2 = false;
		JobaddForm jobAddForm = (JobaddForm) form;
	
		String jobName = jobAddForm.getJobname();
		String jobDesc = jobAddForm.getJobdesc();
		String jobFunction = jobAddForm.getJobFunction();
		String jobAmount = jobAddForm.getJobAmount();
		String jobNumber = jobAddForm.getJobNumber();
		String jobCondition = jobAddForm.getJobCondition();
		String jobRank = jobAddForm.getJobRank();
		
		JobManagerImpl jobMgr = new JobManagerImpl();
		try {
			b1 = jobMgr.isJobExist(jobName.trim());//判断岗位名称是否重复
			b2 = jobMgr.isJobNumber(jobNumber.trim());//判断岗位编号是否重复
		} catch (ManagerException e1) {
			e1.printStackTrace();
		}

		Job job = new Job();

		if (b1 == false&&b2==false) {
			job.setJobName(jobName);
			job.setJobDesc(jobDesc);
			job.setJobFunction(jobFunction);
			job.setJobAmount(jobAmount);
			job.setJobNumber(jobNumber);
			job.setJobCondition(jobCondition);
			job.setJobRank(jobRank);
			job.setOwner_id(Integer.parseInt(control.getUserID()));
		} else {
			request.setAttribute("isJobExist", "true");
			request.setAttribute("isJobNumber","true");
			return mapping.findForward("addjob");

		}

		try {
			jobMgr.saveJob(job);
			
			//--岗位管理写操作日志	
			operContent="修改岗位："+job.getJobName(); 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
			//--
			String fromtype = request.getParameter("fromtype");
			String orgId = request.getParameter("orgId");
			if(fromtype != null && fromtype.equals("user"))
			{
				//把新建的岗位赋与当前机构
				OrgJobAction.addJobToOrgAjax(orgId,job.getJobId(),"1");//暂时为1
				
				ActionForward forward = mapping.findForward("addjob");
				StringBuffer path = new StringBuffer(forward.getPath());
				path.append("?status=ok");
				return new ActionForward(path.toString());
			}
			ActionForward forward = mapping.findForward("properties");
			StringBuffer path = new StringBuffer(forward.getPath());

			boolean isQuery = (path.indexOf("?") >= 0);

			if (isQuery) {
				path.append("&method=getJobInfo&jobId=" + job.getJobId()
						+ "&action=update&jobId=" + job.getJobId());
			} else {
				path.append("?method=getJobInfo&jobId=" + job.getJobId()
						+ "&action=update&jobId=" + job.getJobId());
			}
			return new ActionForward(path.toString());
		} catch (ManagerException e) {
			e.printStackTrace();

		}

		return new ActionForward(mapping.getInput());
	}

}
