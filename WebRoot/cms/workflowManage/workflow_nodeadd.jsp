<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.workflowmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.List"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String workflowId = request.getParameter("workflowId");

	WorkflowManager workflowManager = new WorkflowManagerImpl();

	List allNodelist = workflowManager.getWorkflowAllNodeList();
	request.setAttribute("allNodelist",allNodelist);

	List hasNodelist = workflowManager.getWorkflowNodeListById(workflowId);
	request.setAttribute("hasNodelist",hasNodelist);
%>
<html>
<head>
<title>修改流程节点</title>
<link rel="stylesheet" type="text/css" href="../inc/css/cms.css">
<SCRIPT LANGUAGE="JavaScript"> 
function addNode(){	
   var n=document.all("workflowNodeIds").options.length-1;
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("workflowNodeIds").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("workflowNodeIds").add(op);
   
}
function deleteall(){
	for (var m=document.all("workflowNodeIds").options.length-1;m>=0;m--)
    document.all("workflowNodeIds").options[m]=null
}
      
function addall(){
	var n=document.all("workflowNodeIds").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
}
function deleteNode(){
 for (var m=document.all("workflowNodeIds").options.length-1;m>=0;m--)
	      if(document.all("workflowNodeIds").options[m].selected)
         document.all("workflowNodeIds").options[m]=null;
}

function checkForm()
{
	var nodeids = "";
	for (var m=document.all("workflowNodeIds").options.length-1;m>=0;m--)
		nodeids += document.all("workflowNodeIds").options[m].value + ",";
	document.forms[0].nodeIds.value = nodeids;
	document.forms[0].action = "workflow_nodeadd_do.jsp";
	document.forms[0].target = "updateworkflownodeiframe";
	document.forms[0].submit();
}

</SCRIPT>
	<body bgcolor="#F7F8FC">
		<div id="contentborder" align="center">
			<center>
				<form name="workflowNodeForm" action="" method="post">
					<input type="hidden" name="workflowId" value="<%=workflowId%>">
					<input type="hidden" name="nodeIds" value="">
					<table width="80%" border="0" cellpadding="0" cellspacing="1">
					<tr >
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr >
							<td  width="40%" align="center">
								可选流程节点
							</td>
							<td width="20%"  align="center">
								&nbsp;
							</td>
							<td width="40%" align="center" >
								已选流程节点
							</td>
						</tr>
						<tr >
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr >
							<td align="center" >
								<select name="allist" multiple style="width:90%" onDBLclick="addNode()" size="18">
									<pg:list requestKey="allNodelist" needClear="true">
										<option value="<pg:cell colName="transisionId"/>">
											<pg:cell colName="srcStatusName" />→<pg:cell colName="destStatusName" />
										</option>
									</pg:list>
								</select>
							</td>

							<td align="center" >
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr >
										<td align="center">
											<input name="button1" type="button" class="cms_button" value="&gt;" onclick="addNode()">
										</td>
									</tr>
									<tr >
										<td align="center" >
											&nbsp;
										</td>
									</tr>
									<tr >
										<td align="center" >
											<input name="button2" type="button" class="cms_button" value="&gt;&gt;" onclick="addall()">
										</td>
									</tr>
									<tr >
										<td align="center" >
											&nbsp;
										</td>
									</tr>
									<tr>
										<td align="center" >
											<input name="button3" type="button" class="cms_button" value="&lt;&lt;" onclick="deleteall()">
										</td>
									</tr>
									<tr >
										<td align="center" >
											&nbsp;
										</td>
									</tr>
									<tr >
										<td align="center" >
											<input name="button4" type="button" class="cms_button" value="&lt;" onclick="deleteNode()">
										</td>
									</tr>
									<tr >
										<td align="center" >
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td align="center" >
								<select name="workflowNodeIds" multiple style="width:90%" onDBLclick="deleteNode()" size="18">
									<pg:list requestKey="hasNodelist" needClear="true">
										<option value="<pg:cell colName="transisionId"/>">
											<pg:cell colName="srcStatusName" />→<pg:cell colName="destStatusName" />
										</option>
									</pg:list>
								</select>

							</td>

						</tr>
						<tr >
						    <td  align="center">&nbsp;</td>
						  </tr>
						<tr >
							<td colspan="3" align="center">
								<input name="button3" type="button" class="cms_button" value="保存" onclick="checkForm()">
								<input name="button3" type="button" class="cms_button" value="取消" onclick="window.close();">
							</td>
						</tr>
					</table>
				</form>
			</center>
		</div>
		<iframe name="updateworkflownodeiframe" width="0" height="0" style="display:none"></iframe>
	</body>
</html>
