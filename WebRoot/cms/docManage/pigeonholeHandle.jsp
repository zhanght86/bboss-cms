<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	int userId = Integer.parseInt(accesscontroler.getUserID());
	
	String siteId = request.getParameter("siteId");
	
	//文档的归档
	String docidStr = request.getParameter("docidStr");
	String[] docidArr = docidStr.split(";");
	//将文档id的字符串数组转换为整形数组	
	int[] intDocids = new int[docidArr.length];
	for(int i=0;i<docidArr.length;i++){
		intDocids[i] = Integer.parseInt(docidArr[i]);
	}

	//weida
	session.removeAttribute("docIds_pigDoc");
	session.setAttribute("docIds_pigDoc",docidArr);
	session.removeAttribute("siteId_pigDoc");
	session.setAttribute("siteId_pigDoc",siteId);
	%>
	<html><body>
	<iframe name="deleteIndexsFrame" src="DeleteIndexsHandle.jsp?action=pigDoc"></iframe>
	</body></html>
	<%
	
	//回收文档处理
	DocumentManager docManager = new DocumentManagerImpl();
	try{
		
		//初始化递规发布
		//WEBPublish publish = new WEBPublish();
		//publish.init(request,response,pageContext,accesscontroler);
		//PublishCallBack callback = new PublishCallBackImpl();
		//publish.setPublishCallBack(callback);
		
		//定义存储已发布文档列表容器，以便进行递归发布
	    java.util.List docs = new java.util.ArrayList();
	    for(int i=0;i<intDocids.length;i++){
		    Document doc = docManager.getPartDocInfoById(intDocids[i] + "");
			if(doc.getStatus()==5){
				docs.add(doc);
				//删除发布出来的文件
				//publish.deletePublishFile(siteId,intDocids[i],request,response);
				
			}
		}
		
		//归档操作
		Map tranIdMap = docManager.canTransition(intDocids,7);
		//能看到回归这个操作按钮的一般是能归档的（聚合文档和连接文档无需迁移判断）
		docManager.pigeonholeDoc(intDocids,userId);
		
		
		//递归发布文档
		if(docs.size() > 0)
		{
			int action = RecursivePublishManager.ACTIONTYPE_ARCHIVE;
			session.removeAttribute("docs");
			session.setAttribute("docs",docs);
		%>
			<html>
			<body>
			<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteId%>&action=<%=action%>"></iframe>
			</body>
			</html>
			<!-- publish.recursivePubObjectOfDocuments(siteId,docs,RecursivePublishManager.ACTIONTYPE_ARCHIVE);-->
		<%
		}
			%>
				<script language = "javascript">
					alert("操作成功！");
					var str = parent.document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
					 	strArray= str.slice(0,end);
					else
						strArray = str;
					parent.document.location.href = strArray+"?" + parent.document.all.queryString.value;
				</script>
			<%
	}catch(Exception de){
		de.printStackTrace();
		%>
		<script language="javascript">
			alert("数据库操作失败！");
			window.close();
		</script>
		<%
	}
%>




