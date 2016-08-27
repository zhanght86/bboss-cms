<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	int userId = Integer.parseInt(accesscontroler.getUserID());
	
	//从“已发布管理”页面和“频道文档”页面会传来flag参数，
	//值为recursive，表示要递规发布，但仍然需要做判断，只有处于“已发”状态的文档才能递规
	String flag = request.getParameter("flag"); 
	String siteId = request.getParameter("siteId"); 
	String errorstr = "";//存放不能操作的文档的相关信息
	
	//返工文档的回收
	String docidStr = request.getParameter("docidStr");
	String[] docidArr = docidStr.split(";");
	//将文档id的字符串数组转换为整形数组	
	int[] intDocids = new int[docidArr.length];
	for(int i=0;i<docidArr.length;i++){
		String[] id = docidArr[i].split(":");
		intDocids[i] = Integer.parseInt(id[1].trim());		
	}
	DocumentManager docManager = new DocumentManagerImpl();
	
	Map map = docManager.eliminateUnOpDoc(intDocids);
	errorstr = (String)map.get("errormsg");
	if(!"".equals(errorstr))
		errorstr = "以下文档不能回收：\\n" + errorstr;
	intDocids = (int[])map.get("docids");

	//weida
	session.removeAttribute("docIds_garDoc");
	session.setAttribute("docIds_garDoc",intDocids);
	session.removeAttribute("siteId_garDoc");
	session.setAttribute("siteId_garDoc",siteId);
	%>
	<html><body>
	<iframe name="deleteIndexsFrame" src="DeleteIndexsHandle.jsp?action=garDoc"></iframe>
	</body></html>
	<%
	
	//回收文档处理
	
	try{
		Map tranIdMap = docManager.canTransition(intDocids,8);
		if(tranIdMap.size()!=0){
			docManager.garbageDoc(intDocids,userId);
			docManager.recordDocOperate(intDocids,userId,"回收",tranIdMap,"回收");
			
			//递归发布
			if("recursive".equals(flag)){
			    
			    //初始化
	    		WEBPublish publish = new WEBPublish();
				publish.init(request,response,pageContext,accesscontroler);
				PublishCallBack callback = new PublishCallBackImpl();
				publish.setPublishCallBack(callback);
				//定义存储已发布文档列表容器，以便进行递归发布
	            java.util.List docs = new java.util.ArrayList();
				for(int i=0;i<intDocids.length;i++){
				    Document doc = docManager.getPartDocInfoById(intDocids[i] + "");
					if(doc.getStatus()==5){
						docs.add(doc);
						//删除发布出来的文件
						publish.deletePublishFile(siteId,intDocids[i],request,response);
						
					}
				}
				//递归发布文档
				if(docs.size() > 0)
				{
					//publish.recursivePubObjectOfDocuments(siteId,docs,RecursivePublishManager.ACTIONTYPE_GARBAGE);
					int action = RecursivePublishManager.ACTIONTYPE_GARBAGE;
					session.removeAttribute("docs");
					session.setAttribute("docs",docs);
				%>
					<html>
					<body>
					<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteId%>&action=<%=action%>"></iframe>
					</body>
					</html>
				<%
				}
			}
			%>
				<script language = "javascript">
					<%if(!errorstr.equals("")){%>
						alert("<%=CMSUtil.filterStr(errorstr)%>");
					<%}else{%>
						alert("回收文档成功！");
					<%}%>
					var str;
					if(parent.window.dialogArguments != undefined)
					 	str = parent.window.dialogArguments.location.href;
					 else 
					 	str = parent.window.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
					 	strArray= str.slice(0,end);
					else
						strArray = str;
					if(parent.window.dialogArguments != undefined)
						parent.window.dialogArguments.location.href = strArray+"?" + parent.window.dialogArguments.document.all.queryString.value;
					else
						parent.window.location.href = strArray+"?" + parent.document.all.queryString.value;
					window.close();
				</script>
			<%
		}else{
		%>
			<script language = "javascript">
				alert("操作失败，请检查所选文档能否能回收");
				window.close();
			</script>
		<%
		}
	}catch(DocumentManagerException de){
		de.printStackTrace();
		%>
		<script language="javascript">
			alert("数据库操作失败");
			window.close();
		</script>
		<%
	}
%>




