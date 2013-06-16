<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.User,com.frameworkset.platform.security.authentication.EncrpyPwd"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%	
		UserManager userManager = SecurityDatabase.getUserManager();       
		AccessControl accesscontroler = AccessControl.getInstance();
      	accesscontroler.checkAccess(request,response);     	
      	String userAccount = accesscontroler.getUserAccount();  
		
        User user = userManager.getUserByName(userAccount);   
        
      	String oldPassword = StringUtil.replaceNull(request.getParameter("oldPassword"));
      	String passWord    = StringUtil.replaceNull(request.getParameter("passWord")); 
      	if( !EncrpyPwd.encodePassword(oldPassword).equals(user.getUserPassword())){
        	%><script language="javascript" type="text/javascript">
                	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.modfiy.fail.unmatch"/>');
                </script><%
                return;
      	}
		String errormessage = null;
        try{
			if(oldPassword.equals(passWord))
			{
				errormessage ="新密码和旧密码相同，请输入不同的密码!";
			}
			
			else
			{
	            user.setUserPassword(passWord);
	            UserManager manager =SecurityDatabase.getUserManager();
	           
	            manager.updateUserPassword(user); 
	            accesscontroler.refreshPassword(passWord);
	            accesscontroler.updateMailPassword(user.getUserEmail(),passWord);
	            %>                 
	            <script language="javascript" type="text/javascript">
	                         	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.modfiy.success"/>');
	                         	               	                              
	            </script>              
         <%
			}
			if(errormessage != null)
			{
				%>                 
	            <script language="javascript" type="text/javascript">
	                         	$.dialog.alert('<%=errormessage%>');
	                         	               	                              
	            </script>              
         <%
			}
		}catch(Exception e){
         		%>
             <script language="javascript" type="text/javascript">
                         	$.dialog.alert('<%=e.getMessage()%>');
             </script>                 
	<% }
%>

