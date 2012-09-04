<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import = "com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String curUserId = accesscontroler.getUserID();
%>
<%!
    public boolean isNotNull(String str){
        boolean flag = true;
        if(str == null || str.trim().length()==0 || "null".equalsIgnoreCase(str)){
            flag = false;
        }
        return flag;
    }
%>
<%
	boolean tag = false;

    String operUser=request.getParameter("operUser");    
    String type=request.getParameter("type");
    String oper=request.getParameter("oper");     
    String startDate=request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    operUser = operUser==null?"":operUser;
    type = type==null?"":type;
    oper = oper==null?"":oper;
    startDate = startDate==null?"":startDate;
    endDate = endDate==null?"":endDate;
    DBUtil db = new DBUtil();
    StringBuffer sql = new StringBuffer();
    if(accesscontroler.isAdmin()){
    	sql.append("delete TD_SM_LOG lg where 1=1 ");
    }else{
    	sql.append("delete from (select * ")
	       .append(" from TD_SM_LOG where log_operuser in(select user_name || ':' || user_realname ")
	       .append("from td_sm_user where user_id in(select user_id from td_sm_userjoborg ")
    	   .append("where org_id in (select distinct org.org_id from td_sm_organization org ")
    	   .append("start with org.org_id in (select o.org_id from td_sm_organization o, ")
    	   .append("td_sm_orgmanager om where o.org_id = om.org_id and om.user_id = '")
    	   .append(curUserId).append("') ")
    	   .append("connect by prior org.org_id = org.parent_id)))) lg where 1=1 ");
    }
    String queryStr = "1=1";
    if (isNotNull(operUser) && !operUser.equals("所有操作人")) {
        sql.append(" and lg.log_operUser like '%").append(operUser).append("%' ");
        queryStr += "&operUser="+operUser;
    }
    if (isNotNull(oper) && !oper.equals("所有类型")) {
        sql.append(" and lg.log_oper like '%").append(oper).append("%' ");
        queryStr += "&oper="+oper;
    }
    if (isNotNull(type) && !type.equals("所有内容")) {
        sql.append(" and lg.log_type like '%").append(type).append("%' ");
        queryStr += "&type="+type;
    }
    if (isNotNull(startDate)) {
        sql.append(" and lg.log_operTime >=to_date('").append(startDate).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
        queryStr += "&startDate="+startDate;
    }
    if (isNotNull(endDate)) {
        sql.append(" and lg.log_operTime <=to_date('").append(endDate).append(" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
        queryStr += "&endDate="+endDate;
    }
    if(isNotNull(operUser) || isNotNull(oper) || isNotNull(type) || isNotNull(startDate) || isNotNull(endDate)){
        try{        
            db.executeDelete(sql.toString());
            tag = true;
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
%>
<html>
<body>
</body>
<%
if(tag)
{
%><script>alert("删除成功");</script><%
}
else
{
%><script>alert("删除失败");</script><%
}
%>
<script>
    window.onload=function readys(){
        window.parent.document.location.href = window.parent.document.location.href;
    }
</script>
</html>
