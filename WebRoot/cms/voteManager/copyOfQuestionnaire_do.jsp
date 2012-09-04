<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    
    <title>My JSP 'questionnaire_do.jsp' starting page</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <base target="_self">
  <body>
  <script language="javascript">
    <%
    	AccessControl accessControl = AccessControl.getInstance();
		accessControl.checkAccess(request,response);
		CMSManager cmsM = new CMSManager();
		cmsM.init(request,session,response,accessControl);
		int siteID = Integer.parseInt(cmsM.getSiteID());
    	VoteManager voteMgr  = new VoteManagerImpl();
    		Title oneTitle = new Title();
    	
    		List ipCtrls = VoteMngrProtocol.getIpCtrlFromString((String)request.getParameter("ipStartString"),
    														(String)request.getParameter("ipEndString"));
    		List timeCtrls = VoteMngrProtocol.getTimeCtrlFromString((String)request.getParameter("timeStartString"),
    														(String)request.getParameter("timeEndString"));
    														
    		oneTitle.setName((String)request.getParameter("titleName"));
    		oneTitle.setContent(request.getParameter("content"));
    		String picpath = request.getParameter("picpath");
    		String depart_id = request.getParameter("depart_id");
    		if(depart_id==null)
    		{
    		  depart_id="";
    		}
    		String ctime = request.getParameter("ctime");
    		if(picpath==null||picpath.equals(""))
    		{
    			picpath = request.getParameter("hiddenPath");
    		}
    		oneTitle.setPicpath(picpath);
    		oneTitle.setDepart_id(depart_id);
    		oneTitle.setFoundDate(ctime);
    		if (request.getParameter("channelid")!=null)
    			oneTitle.setChannelID((String)request.getParameter("channelid"));
    		if (request.getParameter("channelName")!=null)
    			oneTitle.setChannelName((String)request.getParameter("channelName"));
    		oneTitle.setTimeCtrls(timeCtrls);
    		oneTitle.setIpCtrls(ipCtrls);
    		oneTitle.setSiteid(siteID);
    		oneTitle.setQuestions(VoteMngrProtocol.getQuestionFromString((String)request.getParameter("questionString")));
    		if (request.getParameter("can_repeat")==null){
    			oneTitle.setIpRepeat(1);
    			oneTitle.setTimeGap(-1);
    		}
    		else{
    			oneTitle.setIpRepeat(0);
    			oneTitle.setTimeGap(Integer.parseInt((String)request.getParameter("selectGap")));
    		}
    			oneTitle.setFounderID(Integer.parseInt(accessControl.getUserID()));
    			if(voteMgr.insertSurvey(oneTitle,accessControl.getUserAccount(),accessControl.getUserName(),request.getRemoteAddr())==1){
    				%>alert("增添问卷成功！");
    				  returnValue="ok";<%
    			}else{
    				%>alert("增添问卷失败！");<%
    			}
    %>
    window.close();
    </script>
    </base>
  </body>
</html>
