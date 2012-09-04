<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil"%>
<%@page import="java.util.Iterator,java.util.List,java.util.Set,com.frameworkset.common.poolman.sql.TableMetaData"%>
<select name="table_name" class="cms_select" style="width:180" onChange="">
	<option value="">请选择表</option>						
<%
	String db_name = request.getParameter("db_name");
	if(db_name==null || db_name.equals(null))
		db_name = "";
		
	String table_name = request.getParameter("table_name");
	if(table_name==null || table_name.equals(null))
		table_name = "";

try {
	
	if(!db_name.equals("")){
		DBUtil db = new DBUtil();
		Set tablesSet = db.getTableMetaDatas(db_name);
		Iterator tablesIter = tablesSet.iterator();
		while (tablesIter.hasNext()){
			TableMetaData table = (TableMetaData)tablesIter.next();
			String thisTable = table.getTableName().toLowerCase();
			if((thisTable == table_name) || (thisTable.equals(table_name))){
%>
	<option value="<%=thisTable%>" selected><%=thisTable%></option>
<%
			}else{
%>
	<option value="<%=thisTable%>"><%=thisTable%></option>
<%	
			}
		}
	}
} catch (Exception e) {
	e.printStackTrace();
}							
%>
</select>