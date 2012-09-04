<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.Map"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	DocumentManager dm = new DocumentManagerImpl();
	
	//从“已发布管理”页面和“频道文档”页面会传来flag参数，
	//值为recursive，表示要递规发布，但仍然需要做判断，只有处于“已发”状态的文档才能递规
	String flag = request.getParameter("flag"); 
	String siteId = request.getParameter("siteId");
	String errorstr = "";//存放不能删除的文档的相关信息
	
	String userid = accesscontroler.getUserID();
	String docidStr = request.getParameter("docidStr");	
	String[] docidArr = docidStr.split(";");
	//将文档id的字符串数组转换为整形数组	
	int[] intDocids = new int[docidArr.length];
	for(int i=0;i<docidArr.length;i++){
		intDocids[i] = Integer.parseInt(docidArr[i].trim());
	}
	Map map = dm.eliminateUnOpDoc(intDocids);
	errorstr = (String)map.get("errormsg");
	if(!"".equals(errorstr))
		errorstr = "以下文档不能删除：\\n" + errorstr;
	intDocids = (int[])map.get("docids");
	try{
		StringBuffer doctitles = new StringBuffer();
		java.util.List recursivedocs = new java.util.ArrayList();
		
		//获取要递归发布的文档
		if("recursive".equals(flag))
		{
			java.util.List docs = dm.getDocInfoByIds(docidStr);	
					
			for(int i=0;i<docs.size();i++)
			{
				
				Document recdoc = (Document)docs.get(i);
				doctitles.append(recdoc.getSubtitle()).append(";");
				if(recdoc.getStatus() == 5 && recdoc.getDocflag() == 0)
				{
					recursivedocs.add(recdoc);
				}
			}
		}
		
		try
		{
			dm.deleteDoc(request, intDocids, siteId);
		}
		catch(DocumentManagerException e)
		{
			errorstr = e.getMessage();
			e.printStackTrace();
		}

		
		//记录操作日志
		LogManager logManager = SecurityDatabase.getLogManager();
		logManager.log(accesscontroler.getUserAccount(),"删除文档.文档标题:" + doctitles.toString(),"文档管理",request.getRemoteAddr(),"");
		if("recursive".equals(flag))
		{
			if(recursivedocs.size() > 0)
			{
				int action = RecursivePublishManager.ACTIONTYPE_DELETE;
				session.removeAttribute("docs");
				session.setAttribute("docs",recursivedocs);
			%>
				<html>
				<body>
				<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteId%>&action=<%=action%>"></iframe>
				</body>
				</html>
				<!-- publish.recursivePubObjectOfDocuments(siteId,docs,RecursivePublishManager.ACTIONTYPE_ARCHIVE);-->
			<%
			}
		}

		%>
		<SCRIPT LANGUAGE="JavaScript">
			<%if(!errorstr.equals("")){%>
				alert("<%=CMSUtil.filterStr(errorstr)%>");
			<%}else{%>
				alert("删除文档成功！");
			<%}%>
			var str = parent.document.location.href;
			var end = str.indexOf("?");
			var strArray;
			if(end != -1)
			 	strArray= str.slice(0,end);
			else
				strArray = str;
			parent.document.location.href = strArray+"?" + parent.document.all.queryString.value;
		</script>
		
	<%}catch(Exception e){
		e.printStackTrace();
	%>		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("删除文档失败！");
		</script>		
	<%
		}
	%>			