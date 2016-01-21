
<%@page import="com.frameworkset.platform.config.ConfigManager"%><%
/**
 * <p>Title: 新增一级机构</p>
 * <p>Description: 新增一级机构页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	//判断是否是cms
	String subSystemId = control.getCurrentSystemID();
	
	//是否出现  "是否税管员" 复选框
	boolean istaxmanager = ConfigManager.getInstance().getConfigBooleanValue("istaxmanager", false);
%>
<html>
<head>
  <title>新增机构</title>
</head>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script language="JavaScript">
	var api = frameElement.api, W = api.opener;
	
	function trim(string){
		  var temp="";
		  string = ''+string;
		  splitstring = string.split(" ");
		  for(i=0;i<splitstring.length;i++){
		    temp += splitstring[i];
		  } 
		  return temp;
	}
	function saveorg()
	{		//alert("dfasd");
		var orgnumber = form1.orgnumber.value;
		var orgName = form1.orgName.value;
		var orgSn = form1.orgSn.value;
		var orgdesc = form1.orgdesc.value;
		var jp = form1.jp.value;
		var qp = form1.qp.value;		
		var remark5 = form1.remark5.value;
		var layer = form1.layer.value;
		
		//modified by hilary on 20101105,validate the * isnot null
		
		if(orgName == "" || orgName.length<1 || orgName.replace(/\s/g,"")=="" 
				|| orgnumber == "" || orgnumber.length<1 || orgnumber.replace(/\s/g,"")==""
				|| remark5 == "" || remark5.length<1 || remark5.replace(/\s/g,"")==""
				)
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.must.input'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false;
			}
		
		//orgnumber.search(/[^0-9A-Za-z]/g)
		if(orgnumber.search(/[^0-9A-Za-z]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.number.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.orgnumber.focus();
		return false;
		}
		if(orgSn.search(/[^0-9]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.sort.number.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.orgSn.focus();
		return false;
		}
		if(jp.search(/[^A-Za-z]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.check.organization.simple.spell.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.jp.focus();
		return false;
		}
		if(qp.search(/[^A-Za-z]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.check.organization.complete.spell.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.qp.focus();
		return false;
		}
		
		var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
		
		if(!re.test(orgName))
		{
		W.$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.name.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		return false; 
		}
		
		if(orgnumber.length>100)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.organiztion.number.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(orgName.length>40)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.organization.name.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(orgdesc.length>300)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.organization.description.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(jp.length>40)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.simple.spell.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(qp.length>40)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.complete.spell.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(remark5.length>100)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.show.name.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		
		
	if(validateForm(form1)){	
		
		  	document.form1.action = "new_org_do.jsp";
			document.form1.target = "hiddenFrame";
			document.form1.submit();
	    }
	}
	function addorg(){
		window.open('../orgmanager/chargeOrg.jsp?displayNameInput=chargeName&displayValueInput=chargeOrgId&displayNameInput1=chargejobName&displayValueInput1=satrapJobId','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')

	}
	function back(){
		window.returnValue = true;
		window.close();
	}
	function doreset() {
		$("#reset").click();
	}
</script>
<body>
<div style="height: 10px">&nbsp;</div>
<div align="center">
<form name="form1" action="" method="post"  >	
<pg:beaninfo requestKey="Organization">
	     
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>" />
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />		
		 <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" />		 
		 
		 <input type="hidden"  name="creator" value="<%=control.getUserID()%>" />
		 <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>">

		<input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" />
		<input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" />
		<input type="hidden"  name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden"  name="remark2" value="<pg:cell colName="remark2"  defaultValue=""/>" />
					
<table border="0" cellpadding="0" cellspacing="0" class="table4">
  <tr>    
    <th><pg:message code="sany.pdp.role.organization.number"/>：</th>
    <td width=140px><input name="orgnumber" type="text" 
				 validator="string"  value="<pg:cell colName="orgnumber"  defaultValue=""/>"><font color="red">*</font></td>	
	<th><pg:message code="sany.pdp.role.organization.name"/>：</th>
    <td width=140px><input name="orgName" type="text" 
				 validator="string"  value="<pg:cell colName="orgName"  defaultValue=""/>"><font color="red">*</font></td>	   
  </tr>
  <tr >
				   <input name="orgSn" type="hidden"  value="1">
	<th><pg:message code="sany.pdp.role.organization.description"/>：</th>
    <td width=140px><input name="orgdesc" type="text" 
				  value="<pg:cell colName="orgdesc"  defaultValue=""/>"></td>	   
  </tr>
  <tr  >
    <th><pg:message code="sany.pdp.simple.spell"/>：</th>
    <td><input name="jp" type="text" 
				  value="<pg:cell colName="jp"  defaultValue=""/>"></td>	
	<th><pg:message code="sany.pdp.complete.spell"/>：</th>
    <td width=140px><input name="qp" type="text" 
				  value="<pg:cell colName="qp"  defaultValue=""/>"></td>	   
  </tr>
  <tr>
    <th><pg:message code="sany.pdp.role.organization.check"/>：</th>
	<td width=140px>
		<select name="remark3">
			<option label="" value="1"><pg:message code="sany.pdp.valid"/>
			<option label="" value="0"><pg:message code="sany.pdp.invalid"/>
		</select>
	</td>
	<th><pg:message code="sany.pdp.show.name"/>：</th> 
    <td width=140px><input name="remark5" type="text" 
				 validator="string"  value="<pg:cell colName="remark5"  defaultValue=""/>"><font color="red">*</font></td>	
	<input type=hidden name=layer value=1>
	<input type=hidden name=ispartybussiness value=0>
	<input type=hidden name=chargeOrgId value=1>
	<input type=hidden name=chargejobName value=1>
  </tr>
</table>	
<div class="btnarea" >
	<a href="javascript:void(0)" class="bt_1" id="addButton"
					onclick="saveorg()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a> <a
					href="javascript:void(0)" class="bt_2" id="resetButton"
					onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a><input type="reset"
					id="reset" style="display: none;" />
</div>				
</pg:beaninfo>
</form>
</div>
</body>
<div style="display:none">
<iframe name="hiddenFrame" width=0 height=0></iframe>
</div>
</html>
