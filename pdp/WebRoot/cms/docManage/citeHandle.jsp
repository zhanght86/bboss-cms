<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userid = accesscontroler.getUserID();

	//flag为1表示从其他频道引用文档过来，为2表示向其他频道推送引用文档,为3表示频道引用,此时docidStr实际上为频道的id串
	String flag = request.getParameter("flag");
	String siteid = request.getParameter("siteid");				//当前站点
	String channelid = request.getParameter("channelid");		//目的频道
	String dstSiteId = request.getParameter("dstSiteId");		//目的站点
	String docidStr = request.getParameter("docidStr");
	String docsiteidstr = request.getParameter("docsiteid");		//引用文档（或引用频道）所在站点

	String[] docid = null;				//暂时存文档id的字符串数组
	int[] intDocid = null;
	int[] intdocsiteid = null;    //对于频道引用，每个频道对应一个站点id
	String[] docsiteid = null;
	if(docidStr != null){
		if("3".equals(flag)){
			docid = docidStr.split(",");
			docsiteid = docsiteidstr.split(",");
			intdocsiteid = new int[docsiteid.length];
		}
		else {docid = docidStr.split(":");}
		//得到文档id的整型数组
		intDocid = new int[docid.length];
		for(int i=0;i<docid.length;i++){
			intDocid[i] = Integer.parseInt(docid[i]);
			if("3".equals(flag)){
				intdocsiteid[i] = Integer.parseInt(docsiteid[i]);}
		}
	}  
	//文档引用处理
	DocumentManager idm = new DocumentManagerImpl();
	try{
		int citeType = 0;
		if(!"3".equals(flag))				//文档引用
			citeType = 0;
		else 								//频道引用
			citeType = 1;
		String canntCiteDocNames = idm.canCite(intDocid,Integer.parseInt(channelid),citeType);
		if(canntCiteDocNames != null && canntCiteDocNames.length()==0){
			if(!"3".equals(flag))
			{
				idm.citeDoc(intDocid,Integer.parseInt(channelid),Integer.parseInt(userid),citeType,Integer.parseInt(docsiteidstr));
				
			}
			else
			{
				for(int j=0;j<intDocid.length;j++)
				{
					idm.citeDoc(intDocid[j],Integer.parseInt(channelid),Integer.parseInt(userid),citeType,intdocsiteid[j]);
				}
			}
			//递归发布处理
			try
			{
				boolean recursiveFlag = false;
				//判断是否需要递归发布
				if(citeType == 1)				//频道引用一定得递归发布
				{
					recursiveFlag = true;
				}
				else
				{
				    for(int i=0;i<intDocid.length;i++){
					    Document doc = idm.getPartDocInfoById(intDocid[i] + "");
						if(doc.getStatus()==5){
							recursiveFlag = true;
							break;
						}
					}
				}
				
				if(recursiveFlag){
					
					int action = RecursivePublishManager.RECURSIVE_PUB_ADD_ARRANGEDOC;
				%>
					<html>
					<body>
					<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=dstSiteId%>&channelId=<%=channelid%>&action=<%=action%>"></iframe>
					</body>
					</html>
				<%
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			%>
			<script language = "javascript">
				alert("操作成功！");
				if(<%=flag%>==1 || <%=flag%>==3){
					var str = window.dialogArguments.dialogArguments.location.href;
					var strArray = str.slice(0,str.indexOf("?"));
					window.dialogArguments.dialogArguments.location.href = strArray+"?"+window.dialogArguments.dialogArguments.document.all.queryString.value;
				}
				window.dialogArguments.close(); window.close();
			</script>
			<%
		}else{
			%>
			<script language = "javascript">
				alert("不能引用已经引用过的文档/频道：<%=canntCiteDocNames%>！");
				window.close();
			</script>
			<%		 
		}
	}catch(DocumentManagerException de){
		de.printStackTrace();
%>
		<script language="javascript">
			alert("数据库操作失败！");
			window.close();
		</script>
<%
	}
%>