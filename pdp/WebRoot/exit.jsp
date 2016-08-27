<%@page session="false" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.AccessControl
                ,com.frameworkset.platform.security.authorization.AccessException"%>
 <pg:dtoken cache="false"/>
 <%
  AccessControl accesscontroler = AccessControl.getInstance();
  boolean success = accesscontroler.checkAccess(request, response);
  accesscontroler.logout(false);
  //out.flush();
  //out.close();   
%>
