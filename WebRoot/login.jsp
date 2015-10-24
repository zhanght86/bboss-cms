<%@ page session="false" language="java"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.authenticate.LoginUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.liferay.portlet.iframe.action.WebDes"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@page import="org.frameworkset.web.interceptor.AuthenticateFilter"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping,org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%@ page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ page import="com.frameworkset.platform.ca.CAManager"%>
<%@ page import="com.frameworkset.platform.ca.CookieProperties"%>
<%@ page import="com.frameworkset.platform.framework.Framework,com.sany.webseal.LoginValidate.*"%>
<%@ page import="org.frameworkset.web.servlet.support.WebApplicationContextUtils"%>
<%@ page import="org.frameworkset.spi.support.MessageSource"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
	
<%@ page import="java.util.*"%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${defaultmodulename }</title>
<!--[if IE 6]> 
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/html/login/js/png.js"></script>
<script type="text/javascript">
DD_belatedPNG.fix('div');
</script>
<![endif]-->
<script type="text/javascript" src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
<link href="${pageContext.request.contextPath}/html/login/stylesheet/login.css"	rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/commontool.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/common/scripts/WebDes.js"></script>

<script language="JavaScript">
	//最大化窗口
	/**
	var wHeight = window.screen.height-1;
	var wWidth = window.screen.width-1;
	window.moveTo(0,0)
	window.resizeTo(wWidth, wHeight);	

	
   if (window.opener) {
		window.opener.opener=null; 
		window.opener.open('','_self');
		window.opener.close();
	}
	*/
	
	function modifyExpiredPassword(){
		
		
  		var url="${pageContext.request.contextPath}/sysmanager/password/modifyExpiredUserPWD.jsp?userAccount=${userName  }";
  		$.dialog({close:reloadhref,title:'对不起，${userName  }的密码已经失效（密码有效期为${expiredays  }天），过期时间为${expriedtime_}!',width:1050,height:550, content:'url:'+url,currentwindow:this}); 
  		
		
		
	}
	
	function reloadhref()
	{
		location.href = "login.page";
	}
	
	function changcode()
	{
		
		$("#img1").attr("src","Kaptcha.jpg?"+Math.random());
		
	}
	
	
	function getName(){
		loginForm.userName.focus();
		if(document.all.userName.value == ""){
			var aCookie = document.cookie.split("; ");
			for (var i=0; i < aCookie.length; i++){
				var pairSplit=aCookie[i].split("#");
				var un = pairSplit[0].split("=");
				var pn = pairSplit[1].split("=");
				//alert(pairSplit[0]);
				if(un[0]=="USERNAME"){// file://查找n
					loginForm.userName.value=un[1];//  file://取到n的值
				}
				if(pn[0]=="PASSWORD"){
					loginForm.password.value=pn[1];//  file://取到n的值
				}
			}
			loginForm.userName.focus();
		}
	}
	
	function saveName(event){
	
		var s = $("#userName").val();
		var p = $("#password").val();
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			return;
		}
		if((s==""&&p!="")||(s==""&&p=="")){
			$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	$("#userName").focus();
	    	event.returnValue=false;
		    }else if(p==""&&s!=""){
		    $.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	$("#password").focus();
	    	event.returnValue=false;
		    }
		    
	    if(s!=""&&p!=""){
 			document.getElementById('password').value = strEnc(p,s, "", "");
	    	$("#loginForm").submit();
		 }
		   
		/*var ischecked = document.getElementById("remeberpassword").checked;	
		if(ischecked){
			if (s!="" && p!=""){
				s="USERNAME="+s;
				p="PASSWORD="+p;
				document.cookie=s+"#"+p;  // 将保存到客户机中
				//alert(s);
			}
		}*/
		
	}
	
	function reset(){
		$("#loginForm").reset();
	}
	
	function enterKeydowngoU(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(event.keyCode == 13){
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			return;
		}
			if(userName == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else{
				$("#password").focus();
				event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoP(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(event.keyCode == 13){
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			return;
		}
			if(userName == "" ){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else if(userName != "" && password == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#password").focus();
				event.returnValue=false;
			}else if(userName != "" && password != ""){
				//loginForm.subsystem_id.focus();
				
				document.getElementById('password').value = strEnc(password,userName, "", "");
				$("#loginForm").submit();
				event.returnValue=false;
			}
		}
	}
	
	
	function enterKeydowngoV(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(event.keyCode == 13){
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			$("#rand").focus();
			return;
		}
			if(userName == "" ){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else if(userName != "" && password == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#password").focus();
				event.returnValue=false;
			}else if(userName != "" && password != ""){
				//loginForm.subsystem_id.focus();
				
				document.getElementById('password').value = strEnc(password,userName, "", "");
				$("#loginForm").submit();
				event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoS(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(window.event.keyCode == 13){
			if(userName == "" ){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else if(userName != "" && password == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#password").focus();
				event.returnValue=false;
			}else if(userName != "" && password != ""){
				loginForm.ok.focus();
				event.returnValue=false;
			}
		}
	}
	function changeLan(){
		window.location.href="${pageContext.request.contextPath}/sanydesktop/cookieLocale.page?language="+$('#language').val() ;
	}
</script>
</HEAD>
<body>
<body id="logging_bg">
  <div class="c_log">
	<div class="c_logWrapper">
		<div class="c_logContent">
           <div class="c_log_top">		   </div>
		   <!--从这里开始-->
		   <div class="c_log_min">
		   	<div class="c_log_left" style="margin-left: 12px">
			  <div class="logo"><img src="html/login/images/sany_logo.jpg" />
			  </div>
			</div>
			<form id="loginForm" name="loginForm" action="login.page" method="post">
			<pg:dtoken/>
			<input type="hidden" name="flag" value="yes" />
			<input name="macaddr_" type="hidden" />
			<input name="machineName_" type="hidden" />
			<input name="machineIp_" type="hidden" />
			<pg:notempty actual="${successRedirect  }">
				<input name="<%=AuthenticateFilter.referpath_parametername %>" type="hidden" value="${successRedirect  }"/>
				
				
			</pg:notempty>
			<ul class="c_log_right">
				<pg:null actual="${ errorMessage}" evalbody="true">
					<pg:yes></pg:yes>
					<pg:no>
						<pg:equal actual="${ errorMessage}" value="PasswordExpired" evalbody="true">
							<pg:yes><li><label></label><font color='red'>密码已经失效（有效期为${ expiredays}天），过期时间为${expriedtime_}!<a href='#' onclick='javascript:modifyExpiredPassword()'>点击修改</a></font></li></pg:yes>
							<pg:no><li><label></label><font color='red'>${ errorMessage}</font></li></pg:no>
						</pg:equal>
					</pg:no>
				</pg:null>
				 
				<li><label><pg:message code="sany.pdp.user.login.name"/>：</label><input id="userName" name="userName" type="text"   onkeydown="enterKeydowngoU(event)"  /></li>
				<li><label><pg:message code="sany.pdp.login.password"/>：</label><input id="password" name="password" type="password" type="text"	onkeydown="enterKeydowngoP(event)" autocomplete = "off"/></li>
				<pg:true actual="${enable_login_validatecode }">
					<li><label>验证码：</label>
					<input id="rand" name="rand" type="text" style="width:120px" onkeydown="enterKeydowngoV(event)"/><a onclick="changcode()">看不清,换一张</a>
					
					</li>
					<li >
					<img src="Kaptcha.jpg" height="25" width="200" id="img1">
					</li>
				</pg:true>
				<pg:notempty actual="${systemList }" evalbody="true">
					<pg:yes>
					<li><label><pg:message code="sany.pdp.system"/>：</label>
					
					<select name="subsystem_id" style="width:160px;margin-left:-110px;">
						<pg:list actual="${systemList }">
							<option value="<pg:cell colName="sysid"/>"
								<pg:true colName="selected">
								selected </pg:true>
								>
								<pg:cell colName="name"/>
							</option>
						</pg:list>
						
						
						
					</select>
					
					</li>
					</pg:yes>
					<pg:no>
						<input type="hidden" name="subsystem_id" value="module"/>
					</pg:no>
				</pg:notempty>
				
				<pg:notempty actual="${loginStyle }" evalbody="true">
					<pg:yes>
						<li><label><pg:message code="sany.pdp.style"/>：</label>               
						<select name="loginPath"  style="width:160px;margin-left:-110px;">
							<pg:case actual="${loginStyle }">
								<option value="5"
									<pg:empty>selected</pg:empty>
									<pg:equal value="5">selected</pg:equal>>
									common
								</option>
								<option value="6"
									<pg:equal value="6">selected</pg:equal>>
									common-fixwidth
								</option>
								<option value="3"
									<pg:equal value="3">selected</pg:equal>>
									ISany
								</option>
								<option value="1"
									<pg:equal value="1">selected</pg:equal>>
									<pg:message code="sany.pdp.tradition"/>
								</option>
								<option value="2"
									<pg:equal value="2">selected</pg:equal>>
									Desktop
								</option>
								<option value="4"
									<pg:equal value="4">selected</pg:equal>>
									WEBIsany
								</option>
							</pg:case>
						</select>
						</li>
					</pg:yes>
					<pg:no>
						<input type="hidden" name="loginPath" value="5"/>
					</pg:no>
				</pg:notempty>
                
                
                <pg:notempty actual="${language }" evalbody="true">
					<pg:yes>
						<li>
							<label><pg:message code="sany.pdp.language"/>：</label>
							<select name="language" id="language" style="width:160px;margin-left:-110px;" onchange="changeLan()">
							<pg:case actual="${language }">
								<option value="zh_CN" <pg:equal value="zh_CN">selected</pg:equal>>
									<pg:message code="sany.pdp.language.chinese"/>
								</option>
								<option value="en_US" <pg:equal value="en_US">selected</pg:equal>>
									<pg:message code="sany.pdp.language.english"/>
								</option>
								
							</pg:case>	
							</select>
						</li>
					</pg:yes>
					<pg:no>
						<input type="hidden" name="language" value="zh_CN"/>
					</pg:no>
				</pg:notempty>
                
				
				<li class="log_bts" ><a href="javascript:void(0)" class="log_bt c_20"  onclick="saveName(event)"><span><pg:message code="sany.pdp.login"/></span></a><a href="javascript:void(0)" class="log_bt log_cancel" onclick="reset()" ><span><pg:message code="sany.pdp.common.operation.reset"/></span></a></li>
			</ul>
			</form>			
			<div class="Zclear"></div> 			
		  </div>
		   <div class="c_log_bot"></div>
		   <p class="c_edition">BBoss V1.0</p>
      </div>
        <div class="c_logHeight"></div>
	</div>
</div>		

</body>
</html>

