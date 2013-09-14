<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<META HTTP-EQUIV="expires" CONTENT="Mon, 23 Jan 1978 20:52:30 GMT">
 <%
   	
    	AccessControl accessControl = AccessControl.getInstance();
		accessControl.checkAccess(request,response);
		
		CMSManager cmsM = new CMSManager();
		cmsM.init(request,session,response,accessControl);
		int siteID = Integer.parseInt(cmsM.getSiteID());
		
    	VoteManagerImpl voteMgr  = new VoteManagerImpl();
		if (!("search".equals((String)request.getParameter("actionType")))){
    	if ("delete".equals((String)request.getParameter("actionType"))){
    		if(voteMgr.deleteSurveyBy((String)request.getParameter("titleID"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request))==1){
    			%><SCRIPT language="javascript">alert("删除成功！");
    			 returnValue="ok";
    			 window.close();
    			</script><%
    		}else{%>
    		<SCRIPT language="javascript">alert("删除失败！");
    			window.close();</script>
    		<%}
    	}
    	if ("settop".equals((String)request.getParameter("actionType"))){
    	    int flag = voteMgr.setSurveyTop((String)request.getParameter("titleID"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request));
    	    
    		if(flag==1){
    			%><SCRIPT language="javascript">alert("置顶成功！");
    		    returnValue="ok";
    			window.close();</script><%
    		}else{
    		if(flag==2){%>
    		<SCRIPT language="javascript">alert("只能置顶已审核且未过期的调查！");
    			window.close();</script>
    		<%}else
    		{%>
    		  <SCRIPT language="javascript">alert("置顶失败！");
    			window.close();</script>
    		<%}
    		}
    	}
    	if ("canceltop".equals((String)request.getParameter("actionType"))){
    	  int flag = voteMgr.cancelSurveyTop((String)request.getParameter("titleID"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request));
    	   if(flag!=1){
    	     if(flag==2){
    			%><SCRIPT language="javascript">alert("您所选调查已经取消了置顶，请重新选择！");
    			window.close();</script><%
    			}else{%>
    			<SCRIPT language="javascript">alert("取消置顶失败！");
    			window.close();</script>
    			<%}
    		}else{%>
    		<SCRIPT language="javascript">alert("取消置顶成功！");
    		    returnValue="ok";
    			window.close();</script>
    		<%}
    	}
  		if ("unactive".equals(request.getParameter("actionType"))){
  		  int flag = voteMgr.unactivateSurveys(request.getParameter("sids"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request));
  			if(flag==1){
  				%><script language="javascript">alert("取消审核成功!");
  				returnValue="ok";
  				window.close();</script><%
  			}else{if(flag==2){%>
    		<SCRIPT language="javascript">alert("该问卷已取消审核，请不要重复取消审核！");
    			window.close();</script>
    		<%}else{%>
    		<SCRIPT language="javascript">alert("取消审核失败！");
    			window.close();</script>
    		<%}
    		}
  		}
    	if ("active".equals((String)request.getParameter("actionType"))){
    	int flag = voteMgr.activateSurveys((String)request.getParameter("sids"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request));
    		if(flag==1){
    			%><SCRIPT language="javascript">alert("审核成功！");
    			returnValue="ok";
    			window.close();</script><%
    		}else{
    		if(flag==2){%>
    		<SCRIPT language="javascript">alert("该问卷已通过审核，请不要重复审核！");
    			window.close();</script>
    		<%}else{%>
    		<SCRIPT language="javascript">alert("审核失败！");
    			window.close();</script>
    		<%}
    		}
    	}
    	if ("cancellook".equals((String)request.getParameter("actionType"))){
    	int flag = voteMgr.cancelSurveysLook((String)request.getParameter("sids"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request));
    		if(flag==1){
    			%><SCRIPT language="javascript">alert("取消查看成功！");
    			returnValue="ok";
    			window.close();</script><%
    		}else{
    		if(flag==2){%>
    		<SCRIPT language="javascript">alert("该问卷已经取消查看，请不要重复取消！");
    			window.close();</script>
    		<%}else{%>
    		<SCRIPT language="javascript">alert("取消查看失败！");
    			window.close();</script>
    		<%}
    		}
    	}
    	if ("look".equals((String)request.getParameter("actionType"))){
    	int flag = voteMgr.setSurveysLook((String)request.getParameter("sids"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request));
    		if(flag==1){
    			%><SCRIPT language="javascript">alert("恢复查看成功！");
    			returnValue="ok";
    			window.close();</script><%
    		}else{
    		if(flag==2){%>
    		<SCRIPT language="javascript">alert("该问卷已经恢复查看，请不要重复恢复！");
    			window.close();</script>
    		<%}else{%>
    		<SCRIPT language="javascript">alert("恢复查看失败！");
    			window.close();</script>
    		<%}
    		}
    	}
    	if ("clearvote".equals((String)request.getParameter("actionType"))){
    		if(voteMgr.clearVote((String)request.getParameter("titleID"),accessControl.getUserAccount(),accessControl.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request))==1){
    			%><SCRIPT language="javascript">alert("投票结果清零成功！");
    			returnValue="ok";
    			window.close();</script><%
    		}else{%>
    		<SCRIPT language="javascript">alert("投票结果清零失败！");
    			window.close();</script>
    		<%}
    	}
    	}

   %>
