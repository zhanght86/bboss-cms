<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String userid = accesscontroler.getUserID();
	
	DocumentManager dm = new DocumentManagerImpl();
	String docidStr = request.getParameter("docidStr");
	String[] docidArr = docidStr.split(";");
	//将文档id的字符串数组转换为整形数组	
	int[] intDocids = new int[docidArr.length];
	String[] strChannelids = new String[docidArr.length];
	String[] strSiteids = new String[docidArr.length];
	for(int i=0;i<docidArr.length;i++){
		String[] temp = docidArr[i].split(":");  //站点id：频道id：文档id
		strSiteids[i] = temp[0];
		strChannelids[i] = temp[1];
		intDocids[i] = Integer.parseInt(temp[2]);		
	}
	try{
		Map tranIdMap = new HashMap();
		for(int i=0;i<intDocids.length;i++){
			int tranid = dm.canTransition(intDocids[i],dm.getDocStatus(intDocids[i]));
			if(tranid >= 0){
				tranIdMap.put(new Integer(intDocids[i]),new Integer(tranid));
			}else {
				tranIdMap.put(new Integer(intDocids[i]),new Integer(-1));
			}
		}
		dm.recoverDoc(intDocids);
		dm.recordDocOperate(intDocids,Integer.parseInt(userid),"回收恢复",tranIdMap,"回收恢复");
		//定义存储已发布文档列表容器，以便进行递归发布
	    
	    java.util.List docinfos = new java.util.ArrayList();
	    
		//对于回收前一个状态为”已发“状态的文档，恢复时必须重新发布 
		for(int i=0;i<intDocids.length;i++)
		{
		    Document doc = dm.getPartDocInfoById(intDocids[i]+"");
			if(doc.getStatus() == 5)
			{
			    docinfos.add(new String[] {strSiteids[i],strChannelids[i],intDocids[i] + ""});
			}
		}
		//递归发布文档
		if(docinfos.size() > 0)
		{
			String[][] infos = new String[docinfos.size()][3];
			for(int i = 0; i < docinfos.size(); i ++ )
			{
				infos[i] = (String[])docinfos.get(i);
			}
			//WEBPublish recpublish = new WEBPublish();
			//recpublish.init(request,response,pageContext,accesscontroler);
			//设置强制执行已发布文档的递归发布标识为true			
			//recpublish.forceStatusPublished(true);
			//PublishCallBack reccallback = new PublishCallBackImpl();
			//recpublish.setPublishCallBack(reccallback);
			
			//recpublish.publishBatchDocument(infos,new boolean[] {true,false},new int[] {0});
			session.removeAttribute("docInfos");
			session.setAttribute("docInfos",infos);
		%>
			<html>
			<body>
			<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=&action="></iframe>
			</body>
			</html>
		<%
			
		}
		
		
		
		%>
		<script LANGUAGE="JavaScript"> 
			alert("操作成功！");
			var str = parent.window.dialogArguments.location.href;
			var end = str.indexOf("?");
			var strArray;
			if(end != -1)
			 	strArray= str.slice(0,end);
			else
				strArray = str;
			parent.window.dialogArguments.location.href = strArray+"?" + parent.window.dialogArguments.document.all.queryString.value;
			window.close();
		</script>
	<%}catch(Exception e){
		e.printStackTrace();
	%>		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("恢复文档失败！");
		</script>		
	<%
		}
	%>			