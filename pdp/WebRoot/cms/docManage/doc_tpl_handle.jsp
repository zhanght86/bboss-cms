<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String userid = accesscontroler.getUserID();
	
	String action = request.getParameter("action");
	String closeRefresh = request.getParameter("closeRefresh");
	if(closeRefresh==null)
		closeRefresh = "1";
	DocumentManager dm = new DocumentManagerImpl();	
	int successflag = 0;
	String closeWinFlag = "0";         //关闭窗口标志
	
	try{
		if("add".equals(action)){
			String docTplName = request.getParameter("docTplName");
			String docTplDescription = request.getParameter("docTplDescription");
			String tplcontent = request.getParameter("content");
			String channelId = request.getParameter("channelId");
			String channelIds = request.getParameter("channelIds");
			if((channelId==null || "".equals(channelId)) && channelIds!=null){
				if(channelIds.endsWith(","))
					channelId = channelIds.substring(0,channelIds.length()-1);
				else
					channelId = channelIds;
			}	
			DocTemplate doctpl = new DocTemplate();
			doctpl.setChnlId(Long.parseLong(channelId));
			doctpl.setCreateUser(Integer.parseInt(userid));
			doctpl.setDescription(docTplDescription);
			doctpl.setTplCode(tplcontent);
			doctpl.setTplName(docTplName);
			dm.addDocTPL(doctpl);
			successflag = 1;
			closeWinFlag = "1";
		}else if("delete".equals(action)){
			String[] docTplIds = request.getParameterValues("ID");
			if(docTplIds!=null){
				dm.deleteDocTPLs(docTplIds);
			}
			successflag = 1;
		}else if("update".equals(action)){
			String docTplId = request.getParameter("docTplId");
			String docTplName = request.getParameter("docTplName");
			String docTplDescription = request.getParameter("docTplDescription");
			String tplcontent = request.getParameter("content");
			String channelIds = request.getParameter("channelIds");
			System.out.println("docTplName:" + docTplName);
			System.out.println("docTplDescription:" + docTplDescription);
			System.out.println("tplcontent:" + tplcontent);
			System.out.println("channelIds:" + channelIds);
			String channelId = "";
			if(channelIds.endsWith(","))
				channelId = channelIds.substring(0,channelIds.length()-1);
			else
				channelId = channelIds;
			DocTemplate doctpl = new DocTemplate();
			doctpl.setDocTplId(Integer.parseInt(docTplId));
			doctpl.setChnlId(Long.parseLong(channelId));
			doctpl.setDescription(docTplDescription);
			doctpl.setTplCode(tplcontent);
			doctpl.setTplName(docTplName);
			dm.updateDocTPL(doctpl);
			successflag = 1;
			closeWinFlag = "1";	
		}	
	}catch(DocumentManagerException e){
		e.printStackTrace();
	}
%>
<%		 
	if(successflag == 1){
%>
		<script language = "javascript">
			alert("文档模板操作成功！");
			if("<%=closeRefresh%>"=="1"){
				if("<%=closeWinFlag%>"=="1"){
					window.close();
					window.returnValue="cf";	
				} else {
					var str = parent.document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						strArray= str.slice(0,end);
					else
						strArray = str;
					parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
				}
			}
		</script>
<%
	}else if(successflag == 0){
%>
		<script language="javascript">
			alert("文档模板保存失败！");
		</script>
<%
	}
%>			