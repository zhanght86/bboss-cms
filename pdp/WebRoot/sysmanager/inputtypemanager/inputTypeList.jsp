<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ page import="com.frameworkset.platform.dictionary.input.InputTypeManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript">
		function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
		var cbs = true;
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(cbs){
			selectAll[0].checked=true;
		}else{
			selectAll[0].checked=false;
		}
	}	
	//对记录进行更新
	/*
	function updateRecord()
	{
	  var tableObj=document.all.inputTypeList;
	  
	  var n=0,index=0;
	  for(var i=1;i<tableObj.rows.length;i++)
	  {
	    if(tableObj.rows[i].cells[0].children[0].checked)
	    {
	   	    n++; 
	   	    index=i;
	        if(n>2)break;
	  	}
	  }	
	  if(n==1)
	  {
	  	var parameterObj=new Object();
	  	parameterObj.inputTypeId=tableObj.rows[index].cells[0].children[0].value;
	  	parameterObj.typeName=tableObj.rows[index].cells[1].innerText;
	  	parameterObj.typeDesc=tableObj.rows[index].cells[2].innerText;
	  	parameterObj.sourcePath=tableObj.rows[index].cells[3].innerText;
	  	showDialog(parameterObj,"update");
	  }
	  else
	  {
	  	$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.select.one"/>');
	  }
	}
	*/
	//对记录进行更新
	function updateRecord()
	{
	  var id = document.getElementsByName("ID")
	  var n=0;
	  var idValue;
	  for(var i=0;i<id.length;i++)
	  {
	    if(id[i].checked)
	    {
	   	    n++; 
	   	    idValue = id[i].value;
	   	    if(n>1){
	   	    	break;
	   	    }
	  	}
	  }	
	  if(n==1)
	  {
	  	showDialog(idValue,"update");
	  }
	  else
	  {
	  	$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.select.one"/>');
	  }
	}
	
	//开启模态对话编辑框
	function showDialog(idValue,actiontype)
	{
	  	var url="<%=request.getContextPath()%>/sysmanager/inputtypemanager/inputTypeEdit.jsp?actiontype="+ actiontype + "&id="+idValue;
		$.dialog({title:'<pg:message code="sany.pdp.dictmanager.dict.key.inputtype.add"/>',width:300,height:270, content:'url:'+url});   		
	
	  	//var obj=window.showModalDialog('inputTypeEdit.jsp?actiontype='+ actiontype + '&id='+idValue,window,'dialogwidth:500px;dialogheight:400px;');
	  	//if(obj!=null)
	  	//{
	  	//window.location.href = window.location.href;
	 // }
	}
	function dealRecord() {//删除
	    var isSelect = false;
	    var inputTypeIds="";	    
	    for (var i=0;i<InputTypeForm.elements.length;i++) {
			var e = InputTypeForm.elements[i];
			if (e.name == 'ID'){
				if (e.checked){
					inputTypeIds+=","+e.value;
			    }
			}
	    }	    
	    if (inputTypeIds!="")
	    {
	    		var msg = '<pg:message code="sany.pdp.dictmanager.dict.delete.confirm"/>';
	    		
	    		$.dialog.confirm(msg, function() {
	    		 	InputTypeForm.action="inputTypeSubmit.jsp?flag=3&inputTypeId="+inputTypeIds.substring(1,inputTypeIds.length);
		            InputTypeForm.target = "deleteContainer";                     
		            InputTypeForm.submit();
	    		});
	    }else{
	    	$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.one"/>');
	    	return false;
	   }
	}
	</script>
	<body class="contentbodymargin">
		<form name="InputTypeForm" action="" method="post">
		<div class="title_box">
			<div class="rightbtn">
				<%
					if(accessControl.getUserID().equals("1")){ 
			 	%>
			 		<a href="javascript:void(0)" class="bt_small" onclick="showDialog('','add')"><span><pg:message code="sany.pdp.common.add"/></span> </a>
			 		<a href="javascript:void(0)" class="bt_small" onclick="updateRecord()"><span><pg:message code="sany.pdp.common.operation.modfiy"/></span> </a>
			 		<a href="javascript:void(0)" class="bt_small" onclick="dealRecord()"><span><pg:message code="sany.pdp.common.batch.delete"/></span> </a>
			 	<% 
					}
				%>
			</div>
			<strong><pg:message code="sany.pdp.dictmanager.dict.key.inputtype.list"/></strong>
		</div>
		
		<div id="changeColor">
			<pg:listdata dataInfo="com.frameworkset.platform.dictionary.tag.InputTypeList" keyName="InputTypeList" />
			<pg:pager maxPageItems="12" scope="request" data="InputTypeList" isList="false">
				<pg:param name="inputTypeName"/>
				<pg:param name="inputTypeId"/>
				<pg:param name="inputTypeDesc"/>
				<pg:param name="dataSourcePath"/>
			
			<pg:equal actual="${InputTypeList.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${InputTypeList.itemCount}"  value="0">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<th width="30px"><input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"></th>
				    	<th><pg:message code='sany.pdp.dictmanager.dict.key.inputtype.name'/><input class="text" type="hidden" name="selectId"></th>
						<th><pg:message code='sany.pdp.dictmanager.dict.key.inputtype.description'/></th>
						<th><pg:message code='sany.pdp.dictmanager.dict.key.inputtype.page'/></th>
					</pg:header>
					<pg:list>
						<tr>
							<td class="td_center">
					    	<%
					        	//文本类型  选择字典  选择时间  主键生成  选择机构  选择人员
					        	String inputTypeName = dataSet.getString("inputTypeName");
					        	if(InputTypeManager.BASE_INPUTTYPE_TEXT.equals(inputTypeName) || InputTypeManager.BASE_INPUTTYPE_DICT.equals(inputTypeName) || 
					           	InputTypeManager.BASE_INPUTTYPE_DATE.equals(inputTypeName) || InputTypeManager.BASE_INPUTTYPE_PK.equals(inputTypeName)|| 
					           	InputTypeManager.BASE_INPUTTYPE_ORG.equals(inputTypeName) || InputTypeManager.BASE_INPUTTYPE_USER.equals(inputTypeName) ||
					           	InputTypeManager.BASE_INPUTTYPE_CURRENT_USER.equals(inputTypeName) || InputTypeManager.BASE_INPUTTYPE_CURRENT_ORG.equals(inputTypeName) ||
					           	InputTypeManager.BASE_INPUTTYPE_CURRENT_DATE.equals(inputTypeName)
					        	){
					    	%>
					    		<input onClick="checkOne('checkBoxAll','ID')" type="checkbox" disabled="true" name="ID" value="<pg:cell colName="inputTypeId" defaultValue=""/>">
					    	<%    
					        	}else{
					    	%>
					    		<input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="inputTypeId" defaultValue=""/>">
					    	<%
					        	}
					    	%>
							</td>						      				
							<td><pg:cell colName="inputTypeName" defaultValue="" /></td>
							<td><pg:cell colName="inputTypeDesc" defaultValue="" /></td>
							<td><pg:cell colName="dataSourcePath" defaultValue="" /></td>			
				</tr>
					</pg:list>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			</pg:notequal>
		</pg:pager>
		</div>
	
		</form>
	</body>
	<iframe src="" name="deleteContainer" width=0 height=0 style="display:none"></iframe>
</html>

