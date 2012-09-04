<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkManagerAccess(request,response);
%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户密码修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script language="javascript">
	function validateInput(formObj){
            var loginName = formObj.loginName.value;
        	   if(validateForm(formObj)){
                 	var PASSWORD = formObj.passWord;
                  	var PASSWORD_VALIDATE = formObj.passWordConfirm;
                  	if(PASSWORD.value.length < PASSWORD.minLength){
                        	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.length"/>');
                              	PASSWORD.focus();
                              	return false;
                  	}

                  	if(PASSWORD.value != PASSWORD_VALIDATE.value){
                         	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.unmatch"/>');
                              	PASSWORD_VALIDATE.focus();
                              	return false;
                        }
						
                  		$.dialog.confirm("<pg:message code='sany.pdp.personcenter.person.password.modfiy.confirm.begin'/>"+loginName+"<pg:message code='sany.pdp.personcenter.person.password.modfiy.confirm.end'/>", function() {
                  			return true;
                  		});
                  		return false;
                  		
                    }else{  return false; }
	}
function init() {
  document.cacForm.loginName.focus();
}
function randgen(){
	document.forms[0].generatepassword.value=(Math.random().toString()).substr(2,6);
	genpwd.style.display="block";  
	document.forms[0].passWord.value=document.forms[0].generatepassword.value;
	document.forms[0].passWordConfirm.value=document.forms[0].generatepassword.value;
}
function resetpwd(){
	genpwd.style.display="none";
	document.forms[0].passWord.value="";
	document.forms[0].passWordConfirm.value="";
}

function openChoose(){
	
	var url="<%=request.getContextPath()%>/sysmanager/password/chooseOrgUser.jsp";
	$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.select"/>',width:1050,height:550, content:'url:'+url}); 
	
	//var v = window.showModalDialog("chooseOrgUser.jsp",window,"dialogWidth:900px;dialogHeight:550px;help:no;scroll:auto;status:no");
	//if(v!="undefined" && v!=null){
		//cacForm.loginName.value = v;
	//}
	
}
</script>
</head>

<body onload="init()">  
<div class="mcontent">
	<sany:menupath menuid="sysuserpassword"/>
	<form name="cacForm" action="saveModifyUserPWD.jsp" 
              onsubmit="return validateInput(cacForm)">     
              
              <input type="hidden" name="orgId" id="orgId" value="" />  
              <table width="100%" border="0"	cellpadding="0" cellspacing="0" class="table4">
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.loginname"/>：</th>
              			<td width="60%">
              				<input type="text" name="loginName" validator="stringLegal" cnname="<pg:message code='sany.pdp.personcenter.person.loginname'/>" size="25"  class="w120" />
              				&nbsp;&nbsp;<a href="#"  class="bt_2" onclick="openChoose()"><span><pg:message code="sany.pdp.personcenter.person.select"/></span></a>
              			</td>
              		</tr>
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.newpasword"/>：</th>
              			<td width="60%">
              				<input type="password" name="passWord"  size="25" validator="stringLegal" cnname="<pg:message code='sany.pdp.personcenter.person.loginpassword'/>" minLength="6" maxlength="25"  class="w120" />
              			</td>
              		</tr>
              		<tr id='genpwd'  style="display:none">
              			<th width="40%">&nbsp;</th>
              			<td width="60%">
              				<input type="text" name="generatepassword"  class="w120"  value="" /> 
              			</td>
              		</tr>
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.confirm.password"/>：</th>
              			<td width="60%">
              				<input type="password" name="passWordConfirm"  size="25"  class="w120"  />
              			</td>
              		</tr>
              </table>
              <div class="btnarea" >
			  	<a href="#" class="bt_1"  name="save"  onclick="cacForm.submit()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a> 
				<a href="#"	class="bt_2"  name="label2"  onclick="resetpwd()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
				<a href="#"	class="bt_2"  name="label2"  onclick="randgen()"><span><pg:message code="sany.pdp.personcenter.person.password.generate"/></span></a>
			</div>	
     </form>
</div>

<iframe id="backgroundDealFrame" border="0" frameborder="0" framespacing="0" marginheight="0" marginwidth="0"
             name="backgroundDealFrame" noResize scrolling="auto" vspale="0" width="0" height="0"
             src="" memo="本页面需要隐式的执行一些页面，为了不新开浏览窗口，所有隐式执行的页面都在此处执行">
</iframe>
	<%@ include file="../sysMsg.jsp"%>
</body>
</html>


