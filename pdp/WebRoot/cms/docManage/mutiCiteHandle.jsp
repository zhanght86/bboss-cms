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
	String siteid = request.getParameter("docsiteid");				//当前站点
	
	String channelid = request.getParameter("channelid");		//目的频道//channelid site1:channelid1,site2:channelid2,....
	String[] mixids =  channelid.split(",");
	int[] channelids = new int[mixids.length];
	int[] siteids = new int[mixids.length];
	for(int i=0;i<mixids.length;i++){
	    String[] tmp = mixids[i].split(":");
	    siteids[i] = Integer.parseInt(tmp[0]);
	    channelids[i] = Integer.parseInt(tmp[1]);
	}
	
	String docidStr = request.getParameter("docidStr");
	String docsiteidstr = request.getParameter("docsiteid");		//引用文档（或引用频道）所在站点

	String[] docid = null;				//暂时存文档id的字符串数组
	int[] intDocid = null;
	if(docidStr != null){		
		docid = docidStr.split(":");
		//得到文档id的整型数组
		intDocid = new int[docid.length];
		for(int i=0;i<docid.length;i++){
			intDocid[i] = Integer.parseInt(docid[i]);			
		}
	}  	
	
	//文档引用处理
	DocumentManager idm = new DocumentManagerImpl();
	try{
		int citeType = 0;		
		for(int i=0;i<channelids.length;i++){
			String canntCiteDocNames = idm.canCite(intDocid,channelids[i],citeType);
			if(canntCiteDocNames != null && canntCiteDocNames.length()==0){
				idm.citeDoc(intDocid,channelids[i],Integer.parseInt(userid),citeType,Integer.parseInt(siteid));
				//递归发布处理
				try
				{
					boolean recursiveFlag = false;				
					
				    for(int j=0;j<intDocid.length;j++){
					    Document doc = idm.getPartDocInfoById(intDocid[j] + "");
						if(doc.getStatus()==5){
							recursiveFlag = true;
							break;
						}
					}
									
					if(recursiveFlag){
						
						int action = RecursivePublishManager.RECURSIVE_PUB_ADD_ARRANGEDOC;
					%>
						<html>
						<body>
						<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteids[i]%>&channelId=<%=channelids[i]%>&action=<%=action%>"></iframe>
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
			    canntCiteDocNames = canntCiteDocNames.substring(0,canntCiteDocNames.length()-1);
				%>
				<script language = "javascript">
					alert("不能引用已经引用过的文档/频道：<%=canntCiteDocNames%>！");
					window.close();
				</script>
				<%		 
			}
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