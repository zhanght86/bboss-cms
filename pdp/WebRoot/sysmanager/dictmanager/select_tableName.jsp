<%
/**
 * 项目：系统管理 
 * 描述：对于没有指定类型ID的字典类型,只能对应一个表.对于指定了类型ID的字典类型,才能多个类型共存于一个表
 * 版本：1.0 
 * 日期：2007.12.13
 * 公司：三一集团信息
 * @author ge.tao
 */
%>
<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManager"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	DictManager dictImpl = new DictManagerImpl();	
	String dbname = (String)request.getParameter("dbname");	
	String selected_tablename = request.getParameter("tablename");
	dbname = dbname==null?"":dbname;	
	selected_tablename = selected_tablename==null?"":selected_tablename;
	
%>
<html>
    <head>
        <title>选择数据库表</title>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
    </head>
    
    <body>
		<form name="dictList" method="post" >
		    <input type="hidden" name="dbname" value="<%=dbname%>">
		    <input type="hidden" name="selected_tablename" value="<%=selected_tablename%>">		
			<table cellspacing="0" cellpadding="0" border="0" width="100%"  class="thin">
 				<tr valign='top'>
   					<td height='30' colspan="5"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>选择数据库表</strong></td>
 				</tr>
 				<tr class="labeltable_middle_tr_01">
					<td nowrap>
					    表名<input type="text" name="queryTableName" value="<%=selected_tablename%>" style="width:150px">
					    <input type="button" value="查询" class="input" onclick="querytable()">	
					</td>
				</tr> 			
		    </table>
		</form>
	    <iframe src="" name="tabelListFrame" frameborder="0" width="99%" height="300px"></iframe>
	    </table>
		<table cellspacing="1" cellpadding="0" border="0" width="100%"  >
		    <tr height="18px" class="labeltable_middle_tr_01">
		        <td align='center'></td>
				<td align='right'></td>
		    </tr>
		    <tr height="18px" class="labeltable_middle_tr_01">
				<td align='center'></td>
				<td  align="center" colspan="1" nowrap>
				    <input type="button" value="确定" class="input" onclick="window.close()">
					<input name="new" type="button" class="input" value="清除" onclick="removeValue('tablename')">
					<input name="new" type="button" class="input" value="关闭" onclick="window.close()">
				</td>
			</tr>
	    </table>
		
 	</body>
 	<script> 	    
 		/*
 	    window.onunload = function setValue(){
 	        var new_select_table = tabelListFrame.getRadioValue(0);
 	        var ustate = window.dialogArguments.createTypeForm.dicttypeUseTabelState.value;
 	        var old_select_table = "<%=selected_tablename%>";
 	        if(new_select_table != old_select_table){
				window.dialogArguments.createTypeForm.field_name.value = "";
				window.dialogArguments.createTypeForm.field_value.value = "";
				window.dialogArguments.createTypeForm.field_order.value = ""; 
				window.dialogArguments.createTypeForm.field_typeid.value = ""; 
				window.dialogArguments.createTypeForm.field_parentid.value = "";
				window.dialogArguments.createTypeForm.field_data_validate.value = "";
				window.dialogArguments.createTypeForm.field_create_orgId.value = "";
				window.dialogArguments.createTypeForm.is_tree.checked=false;
 	        }
 	        var infos = new Array();
 	        var select_value = tabelListFrame.getRadioValue(1);
 	        infos = select_value.split(":");
 	        if(infos.length==2){
 	            window.dialogArguments.createTypeForm.table_name.value = infos[0];
 	            window.dialogArguments.createTypeForm.dicttypeUseTabelState.value = infos[1]; 	            
 	        }
 	        
 	    }
 	    
 	    function selectOne(checkbox_name,e){
 	        arr = document.getElementsByName(checkbox_name);
 	        for(var i=0;i<arr.length;i++){
 	            if(arr[i].value==e.value){
 	                arr[i].checked = true;
 	            }else{
 	                arr[i].checked = false;
 	            }
 	        }
 	    }
 	    function removeValue(checkbox_name){
 	        //var obj = document.all.tabelListFrame;
 	        tabelListFrame.removeValue(checkbox_name);
 	    }
 	    function querytable(){
 	        var ustate = window.dialogArguments.createTypeForm.dicttypeUseTabelState.value; 	
 	        var path = "tableList.jsp"; 	        
 	        if(ustate != ""){
 	            path = "tableList.jsp?ustate="+ustate;
 	        }
 	        document.dictList.target = "tabelListFrame";
 	        document.dictList.action = path;
 	        document.dictList.submit();
 	    }
 	    querytable();
 	    */
 	</script>
</html>
