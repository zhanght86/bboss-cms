<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);

String userId = (String)request.getParameter("userId");
String typeName = (String)request.getParameter("typeName");
//模块-功能项中的个人信息修改中引用了该页面--如果flag为all说明是个人信息修改模块中在调用该页面
String flag = request.getParameter("flag")==null?"":request.getParameter("flag");

//根据userId查询用户名称
UserManager userManager = SecurityDatabase.getUserManager();
User user = userManager.getUserById(userId);
String userName = user.getUserName();
StringBuffer isAdmin = new StringBuffer();
//判断你选择的用户是否是管理员，为管理员的话，直接弹出对话框，提示用户拥有所有权限
if(!"all".equals(flag)){
DBUtil dbUtil = new DBUtil();
StringBuffer hsql = new StringBuffer("select count(*) from td_sm_userrole t where t.role_id = '1' and t.user_id = '"+userId+"'");
dbUtil.executeSelect(hsql.toString());

//dbUtil.getInt(0,0)
if(dbUtil.getInt(0,0) > 0){
	isAdmin.append("alert('该用户是管理员，拥有所有权限！');window.close();");
}else{
	isAdmin.append("");
}
}


%>
<HTML>
 <HEAD>
   <title>用户[<%=userName%>]资源权限查询</title>
   <script language="javascript">
		<%=isAdmin.toString()%>
   </script>
 </HEAD>
  <frameset rows="38,*" border=0>
	<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="forQuery" src="userres_query.jsp?userId=<%=userId%>&typeName=<%=typeName%>"></frame>		
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="forDocList" src="userres_querylist.jsp?userId=<%=userId%>&typeName=<%=typeName%>"></frame>
	</frameset>	
</HTML>
