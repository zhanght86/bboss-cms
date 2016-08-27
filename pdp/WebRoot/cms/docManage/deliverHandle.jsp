
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userId = accesscontroler.getUserID();
	//flag为1表示新稿文档的送审,为2表示返工文档的送审,3表继续送审
    String flag = request.getParameter("flag"); 
	String[] auditors = request.getParameterValues("checkBoxOne");
	//得到审核人的整型数组
	int[] intAuditors = null;
	if(auditors!=null&& auditors.length>0){
	 	intAuditors = new int[auditors.length];
		for(int i=0;i<auditors.length;i++){
			intAuditors[i] = Integer.parseInt(auditors[i]);
		}
	}
	
	DocumentManager docManager = new DocumentManagerImpl();
	try{
		if(flag.equals("1")){
	    	String docidStr = request.getParameter("docidStr");  //频道列表中也只对一个文档送审
	    	Map tranIdMap = (HashMap)session.getAttribute("tranIdMap");
			session.removeAttribute("tranIdMap");
			//得到文档id数据,为字符串数组
			String[] docids = null;
			if(docidStr!= null){
				docids = docidStr.split(":");
			}
			//频道列表中也只对一个文档送审
			//int intDocid = Integer.parseInt(docids[0]);
			int[] intDocid = new int[docids.length];
			for(int i=0;i<docids.length;i++){
				intDocid[i] = Integer.parseInt(docids[i]);
			}
			//数组中值表示当前文档是否有任务。对于返工文档，值为返工任务id，若为新稿，值为0
			int[] taskids = docManager.hasTask(Integer.parseInt(userId),intDocid,2);
			//做呈送处理
			docManager.deliverDoc(intDocid,intAuditors,Integer.parseInt(userId),taskids);   
			docManager.recordDocOperate(intDocid,Integer.parseInt(userId),"送审",tranIdMap,"送审");
%>
			<script language="javascript">
				alert("操作成功！");
				window.close();
			</script>
<%
	    }
	    else if(flag.equals("2")||flag.equals("3")){
	    	String docid = request.getParameter("docid");  //单个文档id
	    	String taskid = request.getParameter("taskid");
	    	if(flag.equals("3")){
	    		String auditComment = request.getParameter("auditComment");
    			//先审核
    			int tranId3 = docManager.canTransition(Integer.parseInt(docid),3);
				if(tranId3 >= 0){
					docManager.audit(Integer.parseInt(docid),Integer.parseInt(taskid),Integer.parseInt(userId),auditComment,1);
					docManager.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userId),"审核",tranId3,"审核通过");
				}
	    	}
	    	int tanid = docManager.canTransition(Integer.parseInt(docid),2);
	    	if(tanid >= 0){
		    	docManager.deliverDoc(Integer.parseInt(docid),intAuditors,Integer.parseInt(userId),Integer.parseInt(taskid));
		    	docManager.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userId),"送审",tanid,"送审");
%>
			<script language = "javascript">
				alert("操作成功！");
				window.close();
				if(<%=flag%>==2){
					var str = window.dialogArguments.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						 strArray= str.slice(0,end);
					else
						strArray = str;
					window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
				}
			</script>
<%
			}
			else{
%>
			<script language = "javascript">
			alert("操作失败！");
			window.close();
			</script>
<%
			}
	    }
	}
	catch(DocumentManagerException de){
		de.printStackTrace();
%>
		<script language="javascript">
			alert("数据库操作失败！");
			window.close();
		</script>
<%
	}
%>




