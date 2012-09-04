<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkAccess(request,response);
%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<html>
<head>
<title>个人密码修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script language="javascript">
function validateInput(formObj){
           	
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
						
						$.dialog.confirm('<pg:message code="sany.pdp.personcenter.person.password.modfiy.confirm"/>', function() {
							return true;
						});
						return false;
						
            	}else{  return false; }
	}
	
function init() {
  document.cacForm.oldPassword.focus();
}

function resetpwd(){
	
	document.forms[0].oldPassword.value="";
	document.forms[0].passWord.value="";
	document.forms[0].passWordConfirm.value="";
}
</script>
</head>

<body onload="init()">   	
<div class="mcontent">
	<sany:menupath menuid="personsecretpassword"/>
	<form name="cacForm" action="saveModifySelfPWD.jsp" target="backgroundDealFrame"
              onsubmit="return validateInput(cacForm)"> 
              <table width="100%" border="0"	cellpadding="0" cellspacing="0" class="table4">
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.oldpasword"/>：</th>
              			<td width="60%">
              				<input type="password" name="oldPassword"   size="25" class="w120" />
              			</td>
              		</tr>
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.newpasword"/>：</th>
              			<td width="60%">
              				<input type="password" name="passWord"  size="25" validator="stringLegal" cnname="<pg:message code='sany.pdp.personcenter.person.loginpassword'/>" minLength="6" maxlength="25"  class="w120" />
              			</td>
              		</tr>
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.confirm.password"/>：</th>
              			<td width="60%">
              				<input type="password" name="passWordConfirm"  size="25"  class="w120" />
              			</td>
              		</tr>
              </table>
              <div class="btnarea" >
			  	<a href="#" class="bt_1"  name="save"  onclick="cacForm.submit()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a> 
				<a href="#"	class="bt_2"  name="label2"  onclick="resetpwd()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
			</div>	
    </form>
</div>

<iframe id="backgroundDealFrame" border="0" frameborder="0" framespacing="0" marginheight="0" marginwidth="0"
             name="backgroundDealFrame" noResize scrolling="auto" vspale="0" width="0" height="0"
             src="" memo="本页面需要隐式的执行一些页面，为了不新开浏览窗口，所有隐式执行的页面都在此处执行">
</iframe>

</body>
</html>
