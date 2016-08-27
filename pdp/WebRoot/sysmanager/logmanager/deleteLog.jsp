<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import = "com.frameworkset.platform.sysmgrcore.manager.db.LogManagerImpl"%>

<%
    
    String[] logid = request.getParameterValues("ID");
    String forwardStr = "logList.jsp";
    String queryString = request.getParameter("querystring");
    boolean tag = false;
    if(logid != null && logid.length>0){
		if (logid != null) {
		    LogManagerImpl logMgr=new LogManagerImpl();
			tag = logMgr.deleteLog(logid);
		}
		if(queryString!="")forwardStr = "logList.jsp?" + queryString;
%>

<script language="JavaScript">
    if(<%=tag%> == true)
    {
    	alert('删除成功');
    }
    else
    {
     	alert('删除失败');
    }
    window.parent.location.href = "<%=forwardStr%>";
</script>

<%}%>
</body>
</html>
