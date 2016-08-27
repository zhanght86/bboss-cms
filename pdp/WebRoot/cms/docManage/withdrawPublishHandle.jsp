
<!--撤销发布处理页面-->

<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	int userId = Integer.parseInt(accesscontroler.getUserID());

	String docId = request.getParameter("docId");
	String siteId = request.getParameter("siteId");

	//weida
	session.removeAttribute("docId_drawDoc");
	session.setAttribute("docId_drawDoc",docId);
	session.removeAttribute("siteId_drawDoc");
	session.setAttribute("siteId_drawDoc",siteId);
	%>
	<html><body>
	<iframe name="deleteIndexsFrame" src="DeleteIndexsHandle.jsp?action=drawDoc"></iframe>
	</body></html>
	<%
	
	int successFlag = 0;
	//文档发布处理
	DocumentManager idm = new DocumentManagerImpl();
	try{
		int tranId = idm.canTransition(Integer.parseInt(docId),6);
		if(tranId>0){
			WEBPublish publish = new WEBPublish();
			publish.init(request,response,pageContext,accesscontroler);
			//PublishCallBack callback = new PublishCallBackImpl();
			//publish.setPublishCallBack(callback);
			
			//撤销发布处理，更新文档发布状态为"已撤发"
			publish.withdrawPublish(siteId,docId);
			//publish.recursivePubObjectOfDocument(siteId,docId,RecursivePublishManager.ACTIONTYPE_WITHDRAW_PUBLISH);
	
			int action = RecursivePublishManager.ACTIONTYPE_WITHDRAW_PUBLISH;
		%>
			<html>
			<body>
			<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteId%>&docId=<%=docId%>&action=<%=action%>"></iframe>
			</body>
			</html>
		<%
		
			//记录操作日志
			idm.recordDocOperate(Integer.parseInt(docId),userId,"撤销呈送",tranId,"撤销呈送");
			successFlag = 1;
		}
	}catch(DocumentManagerException de){
		de.printStackTrace();
	}
	if(successFlag == 1){%>
	<script language = "javascript">
		alert("操作成功！");
		var str = parent.document.location.href;
		var end = str.indexOf("?");
		var strArray;
		if(end != -1)
			strArray= str.slice(0,end);
		else
			strArray = str;
		parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
	</script>
<%	} else{%>
		<script language="javascript">
			alert("撤销发布失败,请检查所选文档能否撤销发布!");
		</script>
<%}%>
