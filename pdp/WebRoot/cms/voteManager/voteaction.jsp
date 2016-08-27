<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>

<html>
<head>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<META HTTP-EQUIV="expires" CONTENT="Mon, 23 Jan 1978 20:52:30 GMT">
</head>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
%>
<%
	VoteManager voteMgr  = new VoteManagerImpl();
	String[] array = request.getParameterValues("itemids");
	String strOptionID = "";
	int flag = 0;
	int c = 1;
	//get select item
	for (int i=0;array!=null&&i<array.length;i++){
		for (int j=i+1;j<array.length;j++){
			if(array[i].equals(array[j])){
				flag = -1;
				break;
			}
		}
		if (flag == -1){
			flag = 0;
			continue;
		}
		String[] items  = request.getParameterValues(array[i]);
		for (int j=0;items!=null&&j<items.length;j++){
			if (strOptionID.indexOf(items[j]+";")>0)
				continue;
			flag = voteMgr.canVote(Integer.parseInt(array[i].substring(7)),com.frameworkset.util.StringUtil.getClientIP(request));
			if (flag==2){
				%><script language='javascript'>alert("不在投票时间段！感谢您的支持！");</script><%
				break;
			}
			if (flag==3){
				%><script language='javascript'>alert("不在投票IP段！感谢您的支持！");</script><%
				break;
			}
			if (flag==4){
				%><script language='javascript'>alert("您对第<<%=c%>>道投票题已经投过票了！感谢您的支持！");</script><%
				break;
			}
			flag = 0;
			strOptionID += items[j]+";";
		}
		if (flag==2 || flag==3)
			break;
		c++;
	}
	
	//get text answer
	if (flag!=2 && flag!=3){
		c = 1;
		array = request.getParameterValues("textareaids");
		for (int i=0;array!=null&&i<array.length;i++){
			String id = array[i].substring(8);
			String content = request.getParameter(array[i]);
			flag = voteMgr.canVote(Integer.parseInt(id),com.frameworkset.util.StringUtil.getClientIP(request));
			if (flag==2){
				%><script language='javascript'>alert("不在投票时间段！感谢您的支持！");</script><%
				break;
			}
			if (flag==3){
				%><script language='javascript'>alert("不在投票IP段！感谢您的支持！");</script><%
				break;
			}
			if (flag==4){
				%><script language='javascript'>alert("您对第<<%=c%>>道问答题已经回答过了！感谢您的支持！");</script><%
				continue;
			}
			flag = 0;
			
			if (!"".equals(content.replaceAll(" ",""))){
				flag = voteMgr.doAnswer(Integer.parseInt(id),content,com.frameworkset.util.StringUtil.getClientIP(request));
				if (flag!=1){
					%><script language='javascript'>alert("您对第<<%=c%>>道问答题的回答失败！感谢您的支持！");</script><%
					break;
				}
			}
		}
	
		//get vote answer
		if (flag!=2&&flag!=3){
			if (!"".equals(strOptionID)){
				if (voteMgr.doVote(strOptionID,com.frameworkset.util.StringUtil.getClientIP(request))==1){
					%><script language='javascript'>alert("十分感谢您的回答！");</script><%
				}
				else{
					%><script language='javascript'>alert("操作失败！");</script><%
				}
			}else{
				if (flag!=1&&flag!=4){
					%><script language='javascript'>alert("请作选择！");</script><%
				}
				if (flag==1){
					%><script language='javascript'>alert("十分感谢您的回答！");</script><%
				}
			}
		}
	}
%>
<%
	if (!(flag!=2&&flag!=3&&"".equals(strOptionID)&&flag!=1)){
		%><script language='javascript'></script><%
		}
%>

<BODY></BODY>
</html>
