<%@page session="false" contentType="text/html;charset=UTF-8"%><%@page import="com.frameworkset.platform.security.AccessControl
                ,com.frameworkset.platform.security.authorization.AccessException"%><%
  AccessControl accesscontroler = AccessControl.getInstance();
  boolean success = accesscontroler.checkAccess(request, response);
  accesscontroler.logout(false);
  //out.flush();
  //out.close();   
%>
