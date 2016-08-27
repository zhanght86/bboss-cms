<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	int summitId = Integer.parseInt(accesscontroler.getUserID());        //提交人id
	
	//flag为1表示文档从非已审状态（如新稿或者已归档等状态）提交发布,为2表示审稿通过后直接提交发布
	String flag = request.getParameter("flag");
	String[] publishers = request.getParameterValues("checkBoxOne");
	//得到审核人的整型数组
	int[] intPublishers = null;
	if(publishers!=null&& publishers.length>0){
	 	intPublishers = new int[publishers.length];
		for(int i=0;i<publishers.length;i++){
			intPublishers[i] = Integer.parseInt(publishers[i]);
		}
	}	
	
	try{
		//文档提交发布处理
		DocumentManager idm = new DocumentManagerImpl();
		if(flag.equals("2")){
			//falg为2传来的参数 
			String docid = request.getParameter("docid");
			String pretaskid = request.getParameter("taskid");     //前驱任务
			String auditComment = "同意：" + request.getParameter("auditComment"); 
			
			//先审核，即更新文档为已审
			int tranId = idm.canTransition(Integer.parseInt(docid),3);
			if(tranId >= 0){
				idm.audit(Integer.parseInt(docid),Integer.parseInt(pretaskid),summitId,auditComment,1);
				idm.recordDocOperate(Integer.parseInt(docid),summitId,"审核",tranId,"审核通过");
				//通过后提交发布
				int tranId2 = idm.canTransition(Integer.parseInt(docid),11);
				if(tranId2 >= 0){
					idm.subPublishDoc(Integer.parseInt(docid),intPublishers,summitId,Integer.parseInt(pretaskid));
					idm.recordDocOperate(Integer.parseInt(docid),summitId,"提交发布",tranId2,"提交发布");
				}
			}
			%>
			<script language="javascript">
				alert("操作成功！");
				window.close();	
			</script>
			<%
		}
		else if(flag.equals("1")){
			String docidStr = request.getParameter("docidStr");  //多个文档id的字符串
			Map tranIdMap = (HashMap)session.getAttribute("tranIdMap");
			session.removeAttribute("tranIdMap");
			//得到文档id数据,为字符串数组
			String[] docids = null;
			if(docidStr!= null){
				docids = docidStr.split(":");
			}
			//得到文档id的整型数组
			int[] intDocids = new int[docids.length];
			for(int i=0;i<docids.length;i++){
				intDocids[i] = Integer.parseInt(docids[i]);
			}
			
			//提交发布
			idm.subPublishDoc(intDocids,intPublishers,summitId,0);   
			idm.recordDocOperate(intDocids,summitId,"提交发布",tranIdMap,"提交发布");
		%>
		<script language="javascript">
			alert("操作成功！");
			window.close();	
		</script>
		<%
		}
	}catch(DocumentManagerException de){
		de.printStackTrace();
		%>
		<script language="javascript">
			alert("操作失败！");
			window.close();	
		</script>
		<%
	}
%>