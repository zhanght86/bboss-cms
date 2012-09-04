<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@page import="com.frameworkset.platform.sysmgrcore.exception.ManagerException,com.frameworkset.platform.security.authentication.EncrpyPwd"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
				com.frameworkset.platform.sysmgrcore.entity.Organization,
				com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%	   
		System.out.println("hello");
      	String loginName = StringUtil.replaceNull(request.getParameter("loginName"));
      	String passWord    = StringUtil.replaceNull(request.getParameter("passWord"));
      	AccessControl accesscontroler = AccessControl.getInstance();
      	accesscontroler.checkManagerAccess(request,response);
      	
      	
		
		UserManager userManager = SecurityDatabase.getUserManager();
		
        try{
            User user = userManager.getUserByName(loginName);
            String orgAdmin = null;
            if(user==null){
	            %>
	            <script language="javascript" type="text/javascript">
	            	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.modfiy.fail.noexist"/>', function() {
	            		history.back();
	            	});
	            </script>  
	            <%
	            return;
            }
            OrgManager orgManager = SecurityDatabase.getOrgManager();
	        Organization org = orgManager.getMainOrganizationOfUser(loginName);
	      	String orgId = org.getOrgId();
	      	List  secondOrgs = null;
	      	 if(!accesscontroler.isAdmin())
	      	 {
		      	if(org == null)
		      	{
					secondOrgs = orgManager.getSecondOrganizationsOfUser(loginName);
					boolean flag = false;
					for(int i = 0; secondOrgs !=null && i < secondOrgs.size(); i ++)
					{
						Organization secondOrg = (Organization)secondOrgs.get(i);
						if( new OrgAdministratorImpl().userAdminOrg(accesscontroler.getUserID(),secondOrg.getOrgId()))	      		
						{
						
							flag = true;
							break;
						}
			      	}
			      	
			      	if(!flag){
			      			orgAdmin = RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.password.modfiy.fail.noadmin.begin", request) + loginName +RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.password.modfiy.fail.noadmin.end", request);
			      			 %>
			            	<script language="javascript" type="text/javascript">
			            			$.dialog.alert("<%=orgAdmin%>", function() {
			            				history.back();
			            			});
			            	</script> 
		            		<%
		            		return;
			      		}	
		      	}
		      	else
		      	{
		           
	            	if(! new OrgAdministratorImpl().userAdminOrg(accesscontroler.getUserID(),orgId)){
		      			orgAdmin = RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.password.modfiy.fail.noadmin.begin", request) + loginName + RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.password.modfiy.fail.noadmin.end", request);
		      			 %>
		            	<script language="javascript" type="text/javascript">
		            			$.dialog.alert("<%=orgAdmin%>", function() {
		            				history.back();
		            			});
		            	</script> 
	            		<%
	            		return;
		      		}
		      		            
		      	}
		      }
            user.setUserPassword(passWord);
            UserManager manager =SecurityDatabase.getUserManager();
            manager.updateUserPassword(user);
            //如果是系统帐户修改自己的密码,则将刷新session中的密码
            if(loginName.equals(accesscontroler.getUserAccount())) 
            accesscontroler.refreshPassword(EncrpyPwd.encodePassword(passWord));
            accesscontroler.updateMailPassword(user.getUserEmail(),passWord);
            System.out.println("hello");
             %>                 
            <script language="javascript" type="text/javascript">
            			$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.modfiy.success.login"/>', function() {
            				history.back();
            			});
            </script>      	
                     
         <%}
         catch(Exception e){
         		e.printStackTrace();%>
             <script language="javascript" type="text/javascript">
                         	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.password.modfiy.fail.login"/>', function() {
                         		history.back();
                         	});
             </script>                 
	<% }
%>

