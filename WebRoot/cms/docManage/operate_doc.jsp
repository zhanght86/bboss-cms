<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil"%>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		int userId = Integer.parseInt(accesscontroler.getUserID());

		
		String siteId = request.getParameter("siteid");
		String channelId = request.getParameter("channelId");
		String docId = request.getParameter("docId");
		String errorstr = "";//存放不能操作的文档的相关信息
		
		String[] docid;
		if(docId!=null){				//单个文档操作
			docid = new String[1];
			docid[0] = 	docId;
		}
		else 
		{
			docid = request.getParameterValues("ID");					//批量操作	
		}
		
		String  operType = request.getParameter("operType"); 
		//获取操作类型,操作类型如下：
		//1、新增 2、呈送 3、发布 4、归档 
		
		DocumentManager docManager = new DocumentManagerImpl();
		Map tranIdMap;
		//记录文档字符串
		String docidStr = "";
		//将文档id的字符串数组转换为整形数组	
		int[] intDocid = new int[docid.length];
		for(int i=0;i<intDocid.length;i++){
			docidStr = docidStr + docid[i] + ":";	
			intDocid[i] = Integer.parseInt(docid[i]);
		}	
%>
<%
		if(docid != null){
			//处理呈送操作
			if(operType.equals("送审")){
				//先判断是否所有选中的文档都能进行呈送，能执行呈送操作，不能则提示用户检查状态
				tranIdMap = docManager.canTransition(intDocid,2);
				session.removeValue("tranIdMap");
				session.setAttribute("tranIdMap",tranIdMap);
				if(tranIdMap.size()!=0){
					%>
					<script language="javascript">
						//flag为1表示新稿文档的呈送,为2表示反工文档的呈送
						var reValue = showModalDialog("doc_AuditorList.jsp?flag=1&docidStr=<%=docidStr%>&channelId=<%=channelId%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
						if(reValue == "cf"){
							var str = parent.document.location.href;
							var end = str.indexOf("?");
							var strArray;
							if(end != -1)
							 	strArray= str.slice(0,end);
							else
								strArray = str;
							parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
						}
					</script>
					<%
				}else {
					%>
					<script language="javascript">alert("呈送文档失败,请检查所选文档能否呈送!");</script>
					<%
				}
			}
			//处理发布操作
			else if(operType.equals("提交发布")){
				//先判断是否所有选中的文档都能进行提交发布，能执行提交发布操作，不能则提示用户检查状态
				tranIdMap = docManager.canTransition(intDocid,11);
				session.removeValue("tranIdMap");
				session.setAttribute("tranIdMap",tranIdMap);
				if(tranIdMap.size()!=0){
						%>
						<script language="javascript">
							//flag为1表示文档从非已审状态（如新稿或者已归档等状态）提交发布,为2表示审稿通过后直接提交发布
							var reValue = showModalDialog("doc_PublisherList.jsp?flag=1&docidStr=<%=docidStr%>&channelId=<%=channelId%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
							if(reValue == "cf"){
								var str = parent.document.location.href;
								var end = str.indexOf("?");
								var strArray;
								if(end != -1)
								 	strArray= str.slice(0,end);
								else
									strArray = str;
								parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
							}
						</script>
						<%
				}
				else{
					%>
					<script language="javascript">alert("提交发布失败,请检查所选文档能否提交发布!");</script>
					<%
				}	
			}
			//处理归档操作
			else if(operType.equals("归档")){
				Map map = docManager.eliminateUnOpDoc(intDocid);
				errorstr = (String)map.get("errormsg");
				if(!"".equals(errorstr))
					errorstr = "以下文档不能归档：\\n" + errorstr;
				intDocid = (int[])map.get("docids");
				
				//先判断是否所有选中的文档都能进行归档，能则归档操作，不能则提示用户检查状态
				tranIdMap = docManager.canTransition(intDocid,7);
				//聚合文档的归档不走流程，即只要处于已审就可以归档
				//if(tranIdMap.size()!=0){
				
						//初始化递规发布
						
						//定义存储已发布文档列表容器，以便进行递归发布
					    java.util.List docs = new java.util.ArrayList();
					    for(int i=0;i<intDocid.length;i++){
						    Document doc = docManager.getPartDocInfoById(intDocid[i] + "");
							if(doc.getStatus()==5){
								docs.add(doc);
								//删除发布出来的文件
								//publish.deletePublishFile(siteId,intDocids[i],request,response);
								
							}
						}
				
						//归档操作
						docManager.pigeonholeDoc(intDocid,userId);
						docManager.recordDocOperate(intDocid,userId,"归档",tranIdMap,"归档");
						
						
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
							<%
						}
							%>
							<script language="javascript">
								
								<%if(!errorstr.equals("")){%>
									alert("<%=CMSUtil.filterStr(errorstr)%>");
								<%}else{%>
									alert("操作成功！");
								<%}%>
								var str = parent.document.location.href;
								var end = str.indexOf("?");
								var strArray;
								if(end != -1)
								 	strArray= str.slice(0,end);
								else
									strArray = str;
								parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
							</script>
							<%
				//}
			}
			else if(operType.equals("撤销送审")){
				tranIdMap = docManager.canTransition(intDocid,1);
				if(tranIdMap.size()!=0){
						docManager.withdrawDeliver(intDocid);
						
						docManager.recordDocOperate(intDocid,userId,"撤销呈送",tranIdMap,"撤销呈送");
							%>
							<script language="javascript">
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
							<%
				}
				else{
					%>
					<script language="javascript">alert("撤销呈送失败,请检查所选文档能否撤销呈送!");</script>
					<%
				}	
			}
		}
%>


