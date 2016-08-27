<%response.setHeader("Pragma","No-cache");response.setHeader("Cache-Control","no-cache");response.setDateHeader("Expires", 0);%>
<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.dictionary.input.*"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
    String id = (String)request.getParameter("id");
    String actiontype = request.getParameter("actiontype");
    InputTypeManager inputImpl = new InputTypeManagerImpl();
    InputType type = inputImpl.getInputTypeInfoById(id);
    request.setAttribute("type", type);
%>
<html>
	<head>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript">
		var api = frameElement.api, W = api.opener;
		
		var message="";
		//提交数据
		function SaveData()
		{
		    if(!check())
		    {
		    	W.$.dialog.alert(message);
		    	return;
		    }
		    //flag    1新增；2修改
		    var flag=2;
		    if(document.all.actiontype.value!="update") {
		        flag=1;
		    }
		    InputTypeForm.target="hiddenframe";
			InputTypeForm.action="inputTypeSubmit.jsp?flag="+flag;
			InputTypeForm.submit();
		}
		
		//对录入信息进行检查
		function check()
		{
		    message="";
		    var allowSubmit=true;
		    var typeDescValue=document.all.typeDesc.value;
		    var sourcePathValue=document.all.sourcePath.value;
		    var typeNameValue=document.all.typeName.value;
		    if(typeNameValue=="")
		    {
		    	message+='<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.name.notnull"/>'+"\n";
		    	allowSubmit=false;
		    }
		    	    	
		    if(typeDescValue.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			{
				message+='<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.description.nospecialcharacter.begin"/>'+"\\/|:*?<>\"'!"+'<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.description.nospecialcharacter.end"/>'+"\n";
				allowSubmit=false;
			}
			if(sourcePathValue.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			{
				message+='<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.page.nospecialcharacter.begin"/>'+"\\/|:*?<>\"'!"+'<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.page.nospecialcharacter.end"/>'+"\n";
				allowSubmit=false;
			}			
			if(typeDescValue.length>200)
			{
				message+='<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.description.toolong"/>'+"\n";
				document.all.typeDesc.value=typeDescValue.substring(0,200);
				allowSubmit=false;
			}
			if(sourcePathValue.length>200)
			{
				message+='<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.page.toolong"/>'+"\n";
				document.all.sourcePath.value=sourcePathValue.substring(0,200);
				allowSubmit=false;
			}
			return allowSubmit;
		}
		//初始化窗体
		/*
		function loadForm()
		{
			var obj = window.dialogArguments;
			if(obj!=null)
			{
				//对模态窗体传递的参数进行接收
				document.all.inputTypeId.value=obj.inputTypeId;
				document.all.typeDesc.value=obj.typeDesc;
				document.all.sourcePath.value=obj.sourcePath;
				document.all.typeName.value=obj.typeName
			}
		}*/
		</script>
	<body>
		<div style="height: 10px">&nbsp;</div>
		<div class="form_box">
			<form name="InputTypeForm" action="" method="post">
				<table border="0" cellpadding="0" cellspacing="0" class="table4" >
					<tr>
						<th><pg:message code="sany.pdp.dictmanager.dict.key.inputtype.name"/>：</th>
						<td>
							<input type="text" id="typeName" name="typeName"  class="w120" value="${type.inputTypeName}" /><font color="red">*</font>
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.dictmanager.dict.key.inputtype.script"/>：</th>
						<td>
							<textarea type="text" id="typeScript" name="typeScript" rows="3"  style="width: 120">${type.inputScript}</textarea>
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.dictmanager.dict.key.inputtype.description"/>：</th>
						<td>
							<textarea type="text" id="typeDesc" name="typeDesc" rows="3"  style="width: 120">${type.inputTypeDesc}</textarea>
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.dictmanager.dict.key.inputtype.page"/>：</th>
						<td>
							<input type="text"  id="sourcePath" name="sourcePath"  class="w120"  value="${type.dataSourcePath}" />
						</td>
					</tr>
				</table>
				<input type="hidden" name="inputTypeId" value="<%=id%>"/>
				<input type="hidden" name="actiontype" value="<%=actiontype%>"/>
				<div class="btnarea" >
					<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="SaveData()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
					<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
				</div>
			</form>
		</div>
	
	<IFRAME name="hiddenframe" width="0" height="0"></IFRAME>
	</body>
</html>

