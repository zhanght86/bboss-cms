<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String siteid = request.getParameter("siteid");
	String channelid = request.getParameter("channelid");
	//String[] docids = request.getParameterValues("ID");
	String[] docids = request.getParameter("docids").split(",");
	
	//将字符串数组转换为整形数组
	int[] intDocids = null;
	if(docids!=null && docids.length!=0){
		intDocids = new int[docids.length];
		for(int i=0;i<docids.length;i++){
			intDocids[i] = Integer.parseInt(docids[i]);
		} 
	}
	
	//文档引用处理
	DocumentManager idm = new DocumentManagerImpl();
	try{
		idm.withdrawCite(intDocids,Integer.parseInt(channelid));
		//递归发布处理
		try
		{
			boolean recursiveFlag = true;
			//判断是否需要递归发布
			System.out.println("====="+intDocids.length);
			
		   /* for(int i=0;i<intDocids.length;i++){
		    	if(idm.getDocCitedType(intDocids[i] + "") == 1)
		    	{
		    		recursiveFlag = true;
					break;
		    	}
		    	else{
				    Document doc = idm.getPartDocInfoById(intDocids[i] + "");
					if(doc.getStatus()==5){
						recursiveFlag = true;
						break;
					}
				}
			}*/
			
			if(recursiveFlag)
			{	
				int action = RecursivePublishManager.RECURSIVE_PUB_ADD_ARRANGEDOC;
			%>
				<html>
				<body>
				<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteid%>&channelId=<%=channelid%>&action=<%=action%>"></iframe>
				</body>
				</html>
			<%
			}	
			//初始化发布引擎
		 	//WEBPublish publish = new WEBPublish();
			//publish.init(request,response,pageContext,accesscontroler);
			//PublishCallBack callback = new PublishCallBackImpl();
			//publish.setPublishCallBack(callback);
			//System.out.println(channelid);
			//publish.recursivePubObjectOfChannelAncester(siteid,channelid,RecursivePublishManager.RECURSIVE_PUB_ADD_ARRANGEDOC);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
%>
	<script language = "javascript">
		alert("操作成功！");
		var str = window.dialogArguments.document.location.href;
		var strArray = str.slice(0,str.indexOf("?"));
		window.dialogArguments.document.location.href = strArray+"?" + window.dialogArguments.document.all.queryString.value;
		window.close();
	</script>
<%
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