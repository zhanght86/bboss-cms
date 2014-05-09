<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping"%>
<%@ page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ page import="com.frameworkset.platform.ca.CAManager"%>
<%@ page import="com.frameworkset.platform.ca.CookieProperties"%>
<%@ page import="com.frameworkset.platform.framework.Framework,com.sany.webseal.LoginValidate.*"%>
<%@ page import="org.frameworkset.web.servlet.support.WebApplicationContextUtils"%>
<%@ page import="org.frameworkset.spi.support.MessageSource"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	
    String userAccount = request.getParameter("userAccount");
    int expiredays = SecurityDatabase.getUserManager().getUserPasswordDualTimeByUserAccount(userAccount);
   
%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>你的密码已过期，请修改密码-密码有效期为<%=expiredays%>天</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/common/scripts/WebDes.js"></script>
<script language="javascript">
	var api = frameElement.api, W = api.opener;
function dosubmit()
{
	var oldpassword = document.getElementById('oldpassword').value;
	var passWordConfirm = document.getElementById('passWordConfirm').value;
	var passWord = document.getElementById('passWord').value;
	var s = document.getElementById('loginName').value;
	var p = document.getElementById('oldpassword').value;
		 if(s!=""&&p!=""){
 			document.getElementById('oldpassword').value = strEnc(p,s, "", "");
	    
		 }
		 
		 p = document.getElementById('passWord').value;
		 if(s!=""&&p!=""){
 			document.getElementById('passWord').value = strEnc(p,s, "", "");
	    
		 }
		 
		 p = document.getElementById('passWordConfirm').value;
		 if(s!=""&&p!=""){
 			document.getElementById('passWordConfirm').value = strEnc(p,s, "", "");
	    
		 }
		
		$.ajax({
		   type: "POST",
			url : "<%=request.getContextPath()%>/passward/modifyExpiredPassword.page",
			data :formToJson("#cacForm"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					var validated = validateInput(cacForm);
			      	if (validated){
			      		blockUI();	
			      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			      		return true;
			      	}
			      	else
			      	{			      		
			      		return false;
			      	}				 	
				},
			success : function(responseText){
				//去掉遮罩
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("设置密码成功，请用新密码登录系统",function(){	
							
							api.close();
					},api);													
				}else{
					$.dialog.alert("设置密码出错:"+responseText,function(){
						  document.getElementById('oldpassword').value = oldpassword;
						  document.getElementById('passWordConfirm').value = passWordConfirm;
						  document.getElementById('passWord').value =passWord;						
						},api);
				}
			}
		  });
	 }
	function validateInput(formObj){
			var isvalidated = false;
            var loginName = formObj.loginName.value;
        	   if(validateForm(formObj)){
                 	var PASSWORD = formObj.passWord;
                  	var PASSWORD_VALIDATE = formObj.passWordConfirm;
                  	if(PASSWORD.value.length < PASSWORD.minLength){
                        	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.length"/>');
                              	PASSWORD.focus();
                              	isvalidated  = false;
                  	}
                  	else if(PASSWORD.value != PASSWORD_VALIDATE.value){
                         	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.unmatch"/>');
                              	PASSWORD_VALIDATE.focus();
                              	isvalidated = false;
                    }
                  	else
                  	{
						
                  		if(confirm("<pg:message code='sany.pdp.personcenter.person.password.modfiy.confirm.begin'/>"+loginName+"<pg:message code='sany.pdp.personcenter.person.password.modfiy.confirm.end'/>")) {
                  			isvalidated = true;
                  		}
                  	}
                  		
                  		
               }
        	   else{  
        		   isvalidated =  false; 
        	   }
        	   return isvalidated ;
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
	
	var url="<%=request.getContextPath()%>/purviewmanager/common/selectuser.jsp?loginName=loginName";
	$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.select"/>',width:1050,height:550, content:'url:'+url,currentwindow:this}); 
}
</script>
</head>

<body onload="init()">  
<div class="mcontent">

	<form name="cacForm" id="cacForm"  
              >     
              
              <input type="hidden" name="orgId" id="orgId" value="" />  
              <table width="100%" border="0"	cellpadding="0" cellspacing="0" class="table4">
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.loginname"/>：</th>
              			<td width="60%">
              				<input type="text" name="loginName" id="loginName" validator="stringLegal" cnname="<pg:message code='sany.pdp.personcenter.person.loginname'/>" size="25"  class="w120" value="<%=userAccount %>"/>
              				          				
              				&nbsp;&nbsp;
              			</td>
              		</tr>
              		<tr>
              			<th width="40%">旧密码：</th>
              			<td width="60%">
              				<input type="password" name="oldpassword" id="oldpassword" autocomplete = "off" validator="stringLegal" size="25"  class="w120"  />
              			</td>
              		</tr>
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.newpasword"/>：</th>
              			<td width="60%">
              				<input type="password" name="passWord" id="passWord" autocomplete = "off"  size="25" validator="stringLegal" cnname="<pg:message code='sany.pdp.personcenter.person.loginpassword'/>" minLength="6" maxlength="25"  class="w120" />
              			</td>
              		</tr>
              		<tr id='genpwd'  style="display:none">
              			<th width="40%">&nbsp;</th>
              			<td width="60%">
              				<input type="text" name="generatepassword"  class="w120"  value="" /> 请记住这个密码
              			</td>
              		</tr>
              		<tr>
              			<th width="40%"><pg:message code="sany.pdp.personcenter.person.confirm.password"/>：</th>
              			<td width="60%">
              				<input type="password" name="passWordConfirm" id="passWordConfirm" autocomplete = "off"  size="25"  class="w120"  />
              			</td>
              		</tr>
              		
              		
              </table>
              <div class="btnarea" >
			  	<a href="#" class="bt_1"  name="save"  onclick="dosubmit()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a> 
				<a href="#"	class="bt_2"  name="label2"  onclick="resetpwd()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
				<a href="#"	class="bt_2"  name="label2"  onclick="randgen()"><span><pg:message code="sany.pdp.personcenter.person.password.generate"/></span></a>
			</div>	
     </form>
</div>

<iframe id="backgroundDealFrame" border="0" frameborder="0" framespacing="0" marginheight="0" marginwidth="0"
             name="backgroundDealFrame" noResize scrolling="auto" vspale="0" width="0" height="0"
             src="" memo="本页面需要隐式的执行一些页面，为了不新开浏览窗口，所有隐式执行的页面都在此处执行">
</iframe>
	
</body>
</html>


