
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager" %>
				
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%
	//AccessControl control = AccessControl.getInstance();
	//control.checkAccess(request,response);	
	response.setHeader("Cache-Control", "no-cache"); 
    response.setHeader("Pragma", "no-cache"); 
    response.setDateHeader("Expires", -1);  
    response.setDateHeader("max-age", 0);
	//UserInfoForm user = (UserInfoForm)request.getAttribute("currUser");
	int i = 0;
	//String orgName =(String) session.getAttribute("orgName");
	//String orgId = (String) session.getAttribute("orgId");
	//if(orgId==null || orgId==""){
	//	orgId="";
	//	orgName="";
	//}
	//else{
	//	session.setAttribute("orgName",null);
	//	session.setAttribute("orgId",null);
	//}
	//用户登录名的长度--order by gao.tang
	String userNamelength = ConfigManager.getInstance().getConfigValue("userNameLength");
	String userType = request.getParameter("userType");
	if(userType==null){
		userType="0";
	}
%>
<html>
<head>
	<title>添加用户</title>
	<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
	<tab:tabConfig/>
	<script language="javascript">
		function storeUser()
		{
				if(document.UserInfoForm.userName.value.length<1)
				{
		   		   alert("登陆名不能为空，请输入！");
		   		   return;
		        }
		        
		        if(document.UserInfoForm.userName.value.search(/\W/g)!=-1 )
		        {
					alert("登陆名只能为数字、字母和下划线!");
					document.UserInfoForm.userName.focus();
					return false;
				}
		        
		        if(document.UserInfoForm.userRealname.value.length<1){
		   		   alert("真实名称不能为空，请输入！");
		   		   return;
		        }
		        
				if(document.UserInfoForm.userPassword.value.length<1)
				{
		   		   alert("新密码不能为空，请输入！");
		   		   return;
		        }
		        
		      if(document.UserInfoForm.userPassword1.value.length<1)
		      {
		   		   alert("请确认新密码！");
		   		   return;
		      }  
		      if(document.UserInfoForm.userPassword.value!=document.UserInfoForm.userPassword1.value)
		      {
		   		   alert("新密码两次输入不相等,请重新输入！");
		   		   return;
		      }
	        if(document.UserInfoForm.ou.value.length<1)
	        {
	   		   alert("机构不能为空，请输入！");
	   		   return;
	        } 
	        
	        if(document.UserInfoForm.userIdcard.value != "")
	        {
	        	var value = document.UserInfoForm.userIdcard.value ;
	        	
	        	if(!/(^[^\$\*&'=<#>%\^@~\s]+$)|(^$)/.test(value))
	        	{
	        		alert("按要求输入 “身份证号码”的数据！\n数据必须为：合法的字符串。除『$*&'=<#>%^@~ 』");
	        		return false ;
	        	}
	        
	        	var len = document.UserInfoForm.userIdcard.value.length;	
	        	if(len != 15 && len != 18)
	        	{
	        		alert("身份证号码的位数只能是15位或18位!");
	        		return false ;
	        	}
	        }
		        
	        if (validateForm(UserInfoForm) )
	        {
			  	if(document.UserInfoForm.remark3.checked)
		    		document.UserInfoForm.remark3.value="是";
				else document.UserInfoForm.remark3.value="否";
				document.UserInfoForm.target = "saveuser"; 	
				document.UserInfoForm.action="../userManage/userManager.do?method=storeUser";
				document.UserInfoForm.submit();	
			}	
				
		}
		
		  
			function back(){
				window.close();
			}
			
			function showOrg(obj,param){
		        
		        var x=offsetLeft(obj)
		        var y=offsetTop(obj)*1-50;
		        var p=location.href;
		        var i=0;
		        //while(p.indexOf("/")>=0){
		        //    p=p.replace("/","");
		        //    i=i+1;
		        //}
		        //i=i-4;
		        //p="";
		   //   for (var j=0;j<i ;j++ )
		   //   {
		        //  p=p+"../"
		   //   }
		        window.showModalDialog( "<%=request.getContextPath()%>/cms/userManage/orgList.jsp?orgIdName="+param,window,'dialogheight:500px;dialogwidth:180px;dialogLeft:'+x+';dialogTop:'+y+'status:no;help:no');
		        //openWin('../userManage/orgList.jsp',200,400,x,y);
		        return;
		    }
		</script>
	</head>
	<body  bgcolor="#F7F8FC" >
	
		
			
				    <form name="UserInfoForm" method="post">
	
			
			<table width="90%" border="0" cellpadding="0" cellspacing="0" class="" align=center>
				
				<tr>						
					<td height="25" class="detailtitle">
						<strong></strong>登陆名:<span class="STYLE1">*</span>
					</td>
					<td height="25">
						<input type="text" name="userName" value="" validator="string" cnname="登陆名" maxlength="<%=userNamelength%>">
					</td>
					<td height="25" class="detailtitle">
								<STRONG></STRONG>真实名称:<span class="STYLE1">*</span></td>
						<td height="25">
						<input type="text" name="userRealname" value="" validator="string" cnname="真实名称" maxlength="100">
					</td>					
				</tr>
				<tr>						
					<td height="25" class="detailtitle">
								<STRONG></STRONG>密码:<span class="STYLE1">*</span>	</td>
					<td height="25">
						<input type="password" name="userPassword" value="" validator="string" cnname="密码" maxlength="100">
					</td>
					<td height="25" class="detailtitle">
								<STRONG></STRONG>确认密码:<span class="STYLE1">*</span></td>
						<td height="25">
						<input type="password" name="userPassword1" value="" validator="string" cnname="确认密码" maxlength="100">
					</td>					
				</tr>
				<tr>						
					
			        <td height="25" class="detailtitle">
			        	<strong></strong>机构名称:<span class="STYLE1">*</span>
			        </td>
					<td height="25">
							<input type="hidden" name="orgId" value="" />
							<input type="text" name="ou" value="" onclick="showOrg(this,document.all('orgId').value+';'+document.all('ou').value)" readonly="true" validator="string" cnname="组织机构" maxlength="100">
					</td>		  
					<td height="25" class="detailtitle">
								<STRONG> </STRONG>身份证号码:
					</td>
					<td height="25">
						<input type="text" name="userIdcard" value=""   maxlength="18">
					</td>					
				</tr>
				
				<tr>
							<td height="25" class="detailtitle">
								<STRONG> </STRONG>单位电话:</td>						
					<td height="25">
						<input type="text" name="userWorktel" value=""  validator="intNull" cnname="单位电话" maxlength="13"></td>
					<td height="25" class="detailtitle">
								<STRONG> </STRONG>性别:</td>
					<td height="25">					  					    
						    <dict:select type="sex" name="userSex" defaultValue="-1" textValue="--请选择性别--"/>
					</tr>
				
				<tr>
							<td height="25" class="detailtitle">
								<STRONG> </STRONG>家庭电话:</td>
					<td height="25">
								<input type="text" name="homePhone" value=""  validator="intNull" cnname="家庭电话" maxlength="13"></td>				
					<td height="25" class="detailtitle">
								<STRONG> </STRONG>电子邮件:</td>
					<td height="25">
								<input type="text" name="mail" value="" validator="emailNull" cnname="电子邮件" maxlength="40"></td>
				</tr>
				
				<tr>
					<td height="25" class="detailtitle">
								 移动电话1:
							</td>
					<td height="25">
								<input type="text" name="mobile" value="" validator="intNull" cnname="移动电话" maxlength="13"></td>
					<td height="25" class="detailtitle"><STRONG> </STRONG>移动电话1归属地:</td>
					<td height="25">
								<input type="text" name="remark4" value="" validator="stringNull" cnname="移动电话1归属地" maxlength="100"></td>
				</tr>
				
				<tr><td height="25" class="detailtitle"> 移动电话2</td>
					<td height="25">
						<input type="text" name="userMobiletel2" value="" validator="intNull" cnname="移动电话2" maxlength="13"></td>
					<td height="25" class="detailtitle"> 移动电话2归属地:</td>
					<td height="25">
								<input type="text" name="remark5" value="" validator="stringNull" cnname="移动电话2归属地" maxlength="100"></td>
			   </tr>
			      
			   <tr><td height="25" class="detailtitle"> 用户类型:</td>
				   <td height="25">
								<dict:select type="userType" name="userType" defaultValue="<%=userType%>" textValue="请选择用户类型"/></td>						
				   <td height="25" class="detailtitle"> 邮政编码:</td>
				   <td height="25">
								<input type="text" name="tlb" value="" validator="intNull" cnname="邮政编码" maxlength="7"></td>	
			   </tr>
						<tr>
							<td height="25" class="detailtitle">
								 传真:
							</td>
							<td height="25">
								<input type="text" name="userFax" value="" validator="intNull" cnname="传真" maxlength="40">
							</td>
							<td height="25" class="detailtitle">
								 OICQ:
							</td>
							<td height="25">
								<input type="text" name="userOicq" value="" validator="intNull" cnname="OICQ" maxlength="13">
							</td>
						</tr>
						<tr>
							<td height="25" class="detailtitle">
								 生日:
							</td>
							<td height="25">
								<input type="text" name="userBirthday" onclick="showdate(document.all('userBirthday'))" readonly="true" value="" validator="stringNull" cnname="生日" maxlength="40">
							</td>
							<td height="25" class="detailtitle">
								 用户地址:
							</td>
							<td height="25">
								<input type="text" name="userAddress" value="" validator="stringNull" cnname="用户地址" maxlength="200">
							</td>
						</tr>					
						<tr>
							<td height="25" class="detailtitle">
								<STRONG> </STRONG>手机短号码:
							</td>
							<td height="25">
								<input type="text" name="shortMobile" value="" validator="intNull"  cnname="手机短号码" maxlength="40">
							</td>
							<td height="25" class="detailtitle">
								开通用户
							</td>
							<td height="25">
								<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" />
							</td>
						</tr>
						<tr>
						<td height="25" class="detailtitle" colspan="4">
					
							<input type="checkbox" name="remark3" value="on">
							个人信息是否保密
						
						</td>	
						</tr>							
			</table>
			 
			</form>
	
		
			<table width="100%"  cellpadding="2" cellspacing="0" >
				<tr>
					<td>
	
					<div align="center">
						<input name="resave" type="button" class="cms_button" value="保存" onClick="storeUser()">
						<input name="resave" type="button" class="cms_button" value="关闭" onClick="back()">									
					</div>
							
					</td>
				</tr>
			</table>
				
	  <iframe name="saveuser" width="0" height="0"></iframe>
	</body>
</html>
