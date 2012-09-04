<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	DocumentManager idm = new DocumentManagerImpl();
	String auditorid = accesscontroler.getUserID();
	
	String auditComment = request.getParameter("auditComment");
	String auditFlag = request.getParameter("auditFlag");
	String idStr = request.getParameter("idStr");
	int closeDialog = 1;
	
	if(auditComment==null){
		auditComment = "";
		closeDialog = 0;
	}
	if(auditFlag.equals("1"))
		auditComment = "同意：" + auditComment;
	else
		auditComment = "不同意：" + auditComment;
	
	//将字符串数组转换为整形数组
	int intTaskid;
	int intDocid;
	int intDocChnlid;
	String[] idstr = idStr.split(":");
	intDocid = Integer.parseInt(idstr[1].trim());
	intDocChnlid = Integer.parseInt(idstr[2].trim());
	//频道列表出来的参数中没有审核任务的id
	if(!idstr[0].trim().equals(""))
		intTaskid = Integer.parseInt(idstr[0].trim());
	else intTaskid = idm.hasTask(Integer.parseInt(auditorid),intDocid,1);
	
	//文档审核处理
	try{
		if(auditFlag.equals("1")){
			int tranId = idm.canTransition(intDocid,3);
			if(tranId >= 0){
				idm.audit(intDocid,intTaskid,Integer.parseInt(auditorid),auditComment,1);
				idm.recordDocOperate(intDocid,Integer.parseInt(auditorid),"审核",tranId,"审核通过");
				int docType = idm.getDocType(intDocid);
				if(docType !=0){       //对于不需发布的文档，审核通过后直接变为已发状态
					idm.updateDocumentStatus(intDocid,5);
					idm.updateDocPublishTime(intDocid);
				}
			}
		}
		else if(auditFlag.equals("0")){
			int tranId = idm.canTransition(intDocid,4);
			if(tranId >= 0){
				idm.audit(intDocid,intTaskid,Integer.parseInt(auditorid),auditComment,0);
				idm.recordDocOperate(intDocid,Integer.parseInt(auditorid),"审核",tranId,"审核没通过，返工");
			}
		}
		if(closeDialog == 0){
%>
	<script language = "javascript">
		alert("操作成功!");
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
		}else{
%>
	<script language = "javascript">
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
		}
	}catch(DocumentManagerException de){
		de.printStackTrace();
%>
		<script language="javascript">
			alert("数据库操作失败！");
		</script>
<%
	if(closeDialog == 1){
%>
		<script language="javascript">
			window.close();
		</script>
<%
		}
	}
%>