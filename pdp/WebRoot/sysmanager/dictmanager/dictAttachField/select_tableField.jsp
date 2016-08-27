<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.dictionary.input.InputTypeManagerImpl" %>
<%@ page import="com.frameworkset.platform.dictionary.input.InputTypeManager" %>
<%@ page import="com.frameworkset.platform.dictionary.input.InputType" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String dicttypeId = (String)request.getParameter("dicttypeId");
	
	
    dicttypeId = dicttypeId==null?"":dicttypeId;
    DictManager dictManager = new DictManagerImpl();  
      
    Data dicttype = null;
    String dictName = "";
    String dbname = "";
	String tablename = "";
	String selected_column = (String)request.getParameter("column"); 
	String selected_dicttypeField= request.getParameter("fieldName");
    if(dicttypeId.trim().length()>0){
    	dicttype = dictManager.getDicttypeById(dicttypeId);
    	dictName = dicttype.getName();
    	dbname = dicttype.getDataDBName();
        tablename = dicttype.getDataTableName();
    }

	dbname = dbname==null?"":dbname;
	tablename = tablename==null?"":tablename;
	selected_column = selected_column==null?"":selected_column;
	
	TableMetaData nameObj = DBUtil.getTableMetaData(dbname,tablename);    
    //外键
    Set foreignKeys = nameObj.getForeignKeys();
    String foreignPrompt = "";
    Iterator foreignIt =  foreignKeys.iterator();
    boolean hasFK = false;
    if(foreignIt.hasNext()){
        hasFK = true;        
    	foreignPrompt = "<span style='color:red'>(有外键关联,不能单独维护!)</span>";
    }

	Set set = DBUtil.getColumnMetaDatas(dbname,tablename);	
	Iterator it = set.iterator();
	
%>
<html>
    <head>
        <title>选择数据库字段</title>
        <link href="<%=rootpath%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
    </head>
    <body>
		<form name="dictList" method="post" >
			<table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
 				<tr valign='top'>
   					<td height='30'valign='middle' colspan="8"><img src='../../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>选择数据库字段,请注意类型是否匹配<%=foreignPrompt%></strong></td>
 				</tr>
 				<tr>
					<!--设置分页表头-->
					<td class="headercolor" width="10"></td>
					<td class="headercolor" >能否为空</td>
					<td class="headercolor" >字段名称</td>					
					<td class="headercolor" >字段类型</td>
					<td class="headercolor" >字段长度</td>
					<td class="headercolor" >字段说明</td>
					<td class="headercolor" >是否主键</td>
					<td class="headercolor" >是否外键</td>
					
				</tr>
 			<%
 			while(it.hasNext()){
			    ColumnMetaData  metaData = (ColumnMetaData)it.next();		
			    
				int flag = dictManager.advanceColumnUseStatue(dicttype,metaData.getColumnName(),selected_column);
				String checkStr = "";
				if(flag == DictManager.COLUMN_SELECTED){
				    checkStr = "checked=true";
				}else if(flag == DictManager.COLUMN_USED){
				    checkStr = "disabled=true";
				}
				else if(flag == DictManager.COLUMN_ADVANCE_USED)	
				{
					if(selected_dicttypeField != null && selected_dicttypeField.equals("field_parentid"))
					{
					}
					else
					{
						checkStr = "disabled=true";
					}
				}
				String isnullable = "可为空";
			    if("no".equalsIgnoreCase(metaData.getNullable())){
			        isnullable = "不能为空";
			    }
			    String isunique = "nounique";
			    if(nameObj.getPrimaryKeyMetaData(metaData.getColumnName())!=null){
			    	isunique = "unique";
			    }
 			    
 			%>	
	 			<tr>	
	 				<td class="tablecells" style="text-align:left" nowrap="nowrap">
						<input type="radio" name="columnname" onClick="selectOne('columnname',this)" value="<%=metaData.getColumnName()%>:<%=isnullable%>:<%=isunique%>" <%=checkStr%> width="10">
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
						<%if(isnullable.equals("不能为空")){%>
					    	<span style='color:red'><%=isnullable%><span>
					    <%}else{%>
					    	<%=isnullable%>
					    <%}%>
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=metaData.getColumnName()%>
					</td>
					
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=metaData.getTypeName()%>
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=metaData.getColunmSize()%>
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=metaData.getRemarks()%>
					</td>
					<!--是否主键-->
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%
					        if(nameObj.getPrimaryKeyMetaData(metaData.getColumnName())!=null){
					    %>
					    <span style='color:red'>是主键</span>
					    <%        
					        }
					    %>
					</td>
					<!--是否外键-->
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%
					        if(nameObj.getForeignKeyMetaData(metaData.getColumnName())!=null){
					    %>
					    <span style='color:red'>是外键</span>
					    <%        
					        }
					    %>
					</td>
					
				</tr>
			<%
			    }
			%>
		    </table>
		    
		    <!--外键信息列表-->
		    <%
		    if(hasFK){
		    %>
		    <table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
 				<tr valign='top'>
   					<td height='30'valign='middle' colspan="7"><img src='../../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>外键信息</strong></td>
 				</tr>
 				<tr>
					<!--设置分页表头-->
					<td class="headercolor" >外键关联表名</td> 
					<td class="headercolor" >外键关联字典名称</td> 
					<td class="headercolor" >外键在外键表字段名</td>
					<td class="headercolor" >主表名称</td>
					<td class="headercolor" >主表字段名</td>					
					<td class="headercolor" >外键名称</td>
				</tr>
				
				<%
	 			    while(foreignIt.hasNext()){
	 			        ForeignKeyMetaData fmeta = (ForeignKeyMetaData)foreignIt.next(); 			    
 			    %>
 			    <tr>    
 			        <td class="tablecells" nowrap="nowrap">
					    <span style="color:red"><%=fmeta.getPKTABLE_NAME()%></span>
					</td>
					<td class="tablecells" nowrap="nowrap">
					    <span style="color:red"><%=dictManager.getDicttypeNames(fmeta.getPKTABLE_NAME())%></span>
					</td>
					<td class="tablecells" nowrap="nowrap">
					    <span style="color:red"><%=fmeta.getPKCOLUMN_NAME()%></span>
					</td>					
					<td class="tablecells" nowrap="nowrap">
					    <%=fmeta.getFKTABLE_NAME()%>
					</td>
					<td class="tablecells" nowrap="nowrap">
					    <%=fmeta.getFKCOLUMN_NAME()%>
					</td>
					<td class="tablecells" nowrap="nowrap">
					    <%=fmeta.getFK_NAME()%>
					</td>
                </tr>			
 			    <%    
 			       }
 			    %>		    
		    </table>
		    <strong>提示:如果指定维护的表,有外键关联,必须先保证外键关联表也被配置成了字典,<br>
		              同时,必须把外键在【字典高级字段配置】模块中,配置成高级字段,<br>
		              而且,要求把输入类型配置成【选择字典】<br>
		              最后,还要求把字典的类型选择成"外键关联表"配置成的字典类型!
		    </strong>
		    <%
		    }
		    %>
		    
			<table cellspacing="1" cellpadding="0" border="0" width=98%  >
			    <tr height="18px" class="labeltable_middle_tr_01">
			        <td align='center'></td>
					<td align='right'></td>
			    </tr>
			    <tr height="18px" class="labeltable_middle_tr_01">
					<td align='center'></td>
					<td  align="center" colspan="1" nowrap>
					    <input type="button" value="确定" class="input" onclick="window.close()">
						<input name="new" type="button" class="input" value="清除" onclick="removeValue('columnname')">
						<input name="new" type="button" class="input" value="关闭" onclick="window.close()">
					</td>
				</tr>
		    </table>	
		</form>
 	</body>
 	<script> 	    
 	    window.onunload = function setValue(){
 	        var select_value = "";
 	        var arr = new Array();
 	        arr = document.getElementsByName("columnname");
 	        for(var i=0;i<arr.length;i++){
 	            if(arr[i].checked==true){
 	                select_value = arr[i].value;
 	                break;
 	            }
 	        }
 	        window.returnValue = select_value;
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
 	        arr = document.getElementsByName(checkbox_name);
 	        for(var i=0;i<arr.length;i++){ 	            
 	            arr[i].checked = false; 	            
 	        }
 	    }	    
 	   
 	</script>
</html>
