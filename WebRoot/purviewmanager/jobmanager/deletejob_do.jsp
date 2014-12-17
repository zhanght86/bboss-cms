<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck"%>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.DefaultBussinessCheckImpl"%>
<%
/*
 * <p>Title: 岗位修改处理页面</p>
 * <p>Description: 岗位修改处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		LogManager logManager = SecurityDatabase.getLogManager();
		BussinessCheck bussinessCheck = new DefaultBussinessCheckImpl();
		//如果为true就有岗位没有被删除
		boolean tag = false;
		StringBuffer notice = new StringBuffer(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.delete.job.faild", request)+"：\\n");
		//日志操作模块
		
		String opModle = RequestContextUtils.getI18nMessage("sany.pdp.jobmanage", request);
		//日志操作内容
		StringBuffer operContent = new StringBuffer()
			.append(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.user", request)+"【").append(control.getUserAccount()).append("】"+RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.delete.job", request)+"【");
		String[] id = request.getParameterValues("checkBoxOne");
		//如果为true就有岗位被删除了
		boolean isLog = false;
		boolean state = false;
		if (id != null) 
		{
			for (int i = 0; i < id.length; i++)
			 {					
				JobManager jobManager = SecurityDatabase.getJobManager();
				Job job = jobManager.getJobById(id[i]);
				if(bussinessCheck.jobDeleteCheck(id[i])){//删除岗位成功，该岗位没有被机构设置
					operContent.append(job.getJobName()).append(",");
					isLog = true;
				}else{//该岗位与机构关联，不能删除
					notice.append(job.getJobName()).append("\\n");
					tag = true;
					state = true;
				}
				
				//如果岗位与机构没有关联,删除机构 
				if(!tag){
					tag = jobManager.deleteJob(job);
				}
				
			}
		}
		operContent.append("】");
		if(isLog){//没有任何岗位被删除，就不需要记录日志
			logManager.log(operContent.toString(),opModle);
		}
		if (tag && state) //有不能被删除的岗位
		{
		%>
			<script>
				parent.window.location.reload();
			</script>
		<%
		} 
		else if(isLog)//所选岗位都能被删除
		{
		%>
			<script>
				parent.window.location.reload();
			</script>
		<%
		}
%>

	<script>
	    window.onload = function prompt()
	    {
	    }
	</script>

