<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.sql.*,java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil" %>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>

<%	
	
	//新增
	//db.executeSql("alter table test1 add efg132 varchar2(10)");
	//db.executeSql("comment on column  test1.efg132 is 'tt' ");
	
	//删除
	//db.executeSql("ALTER TABLE test1 DROP COLUMN efg132 ");
	
	//修改 改名 类型 长度 备注
	//db.executeSql("ALTER   TABLE   test1  RENAME  COLUMN   EFG132   TO   abc  ");
	//db.executeSql("alter   table   test1   modify(abc  number)");
	//db.executeSql("comment on column  test1.abc is 'tt'   ");

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);    
    DocumentExtColumnManager extDoc = new DocumentExtColumnManager();		
    String operate_type = request.getParameter("operate_type");
    String[] sqlstr = extDoc.createOperateSQL(request);
    DBUtil db = new DBUtil();
    String db_name = "bspf";
    String table_name = "td_cms_document";
	try {			
		for(int i=0;i<sqlstr.length;i++){
			if("".equals(sqlstr[i]) || sqlstr[i]==null) continue;
		    db.addBatch(sqlstr[i]);
		}
		db.executeBatch();
		/*刷新缓存里面的数据*/
		DocumentExtColumnManager.updateTableMetaData(table_name,db_name);
	%>
	<SCRIPT LANGUAGE="JavaScript">
		<!--
			alert("操作成功!");
		//-->
	</SCRIPT>
<%
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		%>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
			if("<%=operate_type%>"=="add") alert("表中已经存在要添加的列!");
		    else if("<%=operate_type%>"=="edit") alert("表中列已经被引用,不能被修改!");
			else alert("表中列已经被引用,不能被删除!");
		//-->
		</SCRIPT>
		<%
	}
	
%>
<html>
<body> 
</body>
<SCRIPT LANGUAGE="JavaScript">
	window.onload=function abc(){
		if("<%=operate_type%>"=="delete"){
			window.parent.location.href="docExtColumnManage.jsp";
		}else{
			window.parent.returnValue='True'
	        window.parent.close();
		}
	}
</SCRIPT>
</html>