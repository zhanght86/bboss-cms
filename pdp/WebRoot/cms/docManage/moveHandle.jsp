<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
	int userId = Integer.parseInt(accesscontroler.getUserID());
	
	String siteId = request.getParameter("siteId");   //这个站点id是文档所在的站点id 		
	String docidstr = request.getParameter("docidStr");// docid1:docid2:docid3.... 
	String statutype = request.getParameter("statutype"); //选择的文档状态	
	String  mixid = request.getParameter("channelid");//channelid site1:channelid1,site2:channelid2,....
    String[] mixids = mixid.split(",");
    
    int[] channelids = new int[mixids.length];
	int[] siteids = new int[mixids.length];
	for(int i=0;i<mixids.length;i++){
	    String[] tmp = mixids[i].split(":");	    
	    siteids[i] = Integer.parseInt(tmp[0]);
	    channelids[i] = Integer.parseInt(tmp[1]);
	}
	
	DocumentManagerImpl idm = new DocumentManagerImpl();
	//得到文档id数据,为字符串数组
	String[] docid;
	int[] intDocid = null;
	if(docidstr != null){
		docid = docidstr.split(":");
		//得到文档id的整型数组
		intDocid = new int[docid.length];
		for(int i=0;i<docid.length;i++){
			intDocid[i] = Integer.parseInt(docid[i]);
		}
	}	
	//移动操作处理
	try{	
	    //初始化递规发布 引用文档的其他文档
		WEBPublish publish = new WEBPublish();
		//定义存储已发布文档列表容器，以便进行递归发布
        java.util.List docs = new java.util.ArrayList();        
        //移动到指定的站点的频道下 多个频道, 指定状态 重新设置流程	
        //拷贝后返回新产生文档的ID数组, 发布这些文档
        //当文档的状态选择为"待审","待发布"时, 分配任务
        //待审分配任务	
		//待发布分配任务	
		//在文档复制|移动时处理
        //发布在隐藏帧里执行
        int[][] newDocIds = null; 
	    newDocIds = idm.copyDocs(request,intDocid,channelids,siteids,Integer.parseInt(statutype),userId);	    
	    if(Integer.parseInt(statutype)==5){
	        //session存放新产生的文档ID数组
	        session.removeAttribute("muti_newDocIds");	    
	    	session.setAttribute("muti_newDocIds",newDocIds);	
	    %>	        		
	    	<html>
			<body>
			<iframe name="publishMutiDocsHandle" src="publishMutiDocsHandle.jsp"></iframe>
			</body>
			</html>
	    		    
	    <%
	    }    
	    /**add by liwei 2010-03-30*/
	    if(Integer.parseInt(statutype)==5){
	    	int action = RecursivePublishManager.PUBOBJECTTYPE_CHANNEL;	
		    for(int i=0;i<siteids.length;i++)
		    {
		    	%>
				<html>
				<body>
				<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteids[i]%>&channelId=<%=channelids[i]%>&action=<%=action%>&flag=channel"></iframe>
				</body>
				</html>
			<%		
		    }
	    }
	    /**add by liwei end 2010-03-30*/
        
        //删除改文档发布出来的文件 递归发布引用这个文档的其他文档
		for(int i=0;i<intDocid.length;i++){
		    Document doc = idm.getPartDocInfoById(intDocid[i] + "");
			if(doc.getStatus()==5){
				docs.add(doc);				
				publish.deletePublishFile(siteId,intDocid[i],request,response);				
			}
		}	
		//递归发布引用原来文档的其他文档
		if(docs.size() > 0){
			int action = RecursivePublishManager.ACTIONTYPE_DELETE;	
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
		       
        	
		
		
		
		//当作删除处理, 删除原来的文档
		try{		    
			idm.deleteDoc(request, intDocid, siteId);			
		}catch(Exception e){
		    e.printStackTrace();
		}
        
		
		
%>
<script language="javascript">
	alert("操作成功！");
	window.close();
	var str = window.dialogArguments.location.href;
	var end = str.indexOf("?");
	var strArray;
	if(end != -1)
		strArray= str.slice(0,end);
	else
		strArray = str;
	window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
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