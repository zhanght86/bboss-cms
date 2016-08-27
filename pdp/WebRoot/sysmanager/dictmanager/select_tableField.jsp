<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManager"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String dbname = (String)request.getParameter("dbname");
	String tablename = (String)request.getParameter("tablename");
		String selected_column = (String)request.getParameter("column").toUpperCase();
	String other_selected = (String)request.getParameter("otherSelected");
	//域,当是排序字段的时候,只能选number型的 fieldName=="field_order"
	String fieldName = request.getParameter("fieldName")==null?"":request.getParameter("fieldName");
	dbname = dbname==null?"":dbname;
	tablename = tablename==null?"":tablename;
	selected_column = selected_column==null?"":selected_column;
	other_selected = other_selected==null?"":other_selected;
	
	TableMetaData nameObj = DBUtil.getTableMetaData(dbname,tablename);
    
    //外键
    Set foreignKeys = nameObj.getForeignKeys();
    String foreignPrompt = "";
    Iterator foreignIt =  foreignKeys.iterator();
    boolean hasFK = false;
    if(foreignIt.hasNext()){
        hasFK = true;        
    	foreignPrompt = "<span style='color:red'>"+RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.table.foreignkey.title", request)+"</span>";
    }
	
	Set set = DBUtil.getColumnMetaDatas(dbname,tablename);	
	Iterator it = set.iterator();
	
	String did = request.getParameter("didcolumn");
	DictManager dicManager = new DictManagerImpl();
	String columnName = dicManager.getColumnName(did);
	
%>
<html>
    <head>
        <title>选择数据库字段</title>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
    </head>
    <body>
    	<div id="changeColor"> 
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
 				<tr>
					<!--设置分页表头-->
					<th width="30px"></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.null"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.name"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.type"/></th> 
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.length"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.description"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.primarykey"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey"/></th>
				</tr>
 			<%
			String setPKColumnName = "";
		    boolean hasSetPKColumn = false;
		    setPKColumnName = dicManager.getOtherDicttypePKColumnByTable(dbname,tablename);
 			while(it.hasNext()){
			    ColumnMetaData  metaData = (ColumnMetaData)it.next();
			    String isnullable = RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.table.field.null.yes", request);
			    boolean isDateFlag = false;
			    if(metaData.getTypeName().equalsIgnoreCase("date")){
			        isDateFlag = true;
			    }
			    if("no".equalsIgnoreCase(metaData.getNullable())){
			        isnullable = "<span style='color:red'>"+RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.table.field.null.no", request)+"<span>";
			    }
			    
			    request.setAttribute("columnName", metaData.getColumnName());
			    
			    
			    //判断是否为主键 提示信息
			    
			    //判断是否是外键 提示信息
 			%>	
	 			<tr>	
	 				<td>
	 				    <%
	 				    //"field_value" 不是选择值(主键)字段
	 				    if(!"field_value".equalsIgnoreCase(fieldName)){
	 				        if(isDateFlag){
	 				        
	 				    %>
	 				        
	 				    <%
	 				        }else{
	 				            //排序字段,并且column不是number,不提供radio.--字典定义时外键也需过滤

	 				            if("field_order".equalsIgnoreCase(fieldName)){
	 				                if("number".equalsIgnoreCase(metaData.getTypeName()) || "long".equalsIgnoreCase(metaData.getTypeName())){
	 				                	//System.out.println("外键吗？ = " + nameObj.getForeignKeyMetaData(metaData.getColumnName()));
	 				                	if(nameObj.getForeignKeyMetaData(metaData.getColumnName())==null){	 				                	
	 				    %>
	 				    				<pg:equal actual="${param.column}"  value="${columnName}">
					 						<input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" checked="checked" /> 
					 					</pg:equal>
					 					<pg:notequal actual="${param.column}"  value="${columnName}">
					 						 <input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" /> 
					 					</pg:notequal>
	 				                   
	 				    <%            
	 				                    }
	 				                }
	 				            }
	 				            //gao.tang update 20100507  如果为字典类型字段选择字段，并且选择的表为td_sm_dictdata，那么可以选择外键字段
	 				            else if("field_typeid".equalsIgnoreCase(fieldName) 
	 				            		&& "td_sm_dictdata".equalsIgnoreCase(tablename)){
	 				     %>
						            <pg:equal actual="${param.column}"  value="${columnName}">
				 						<input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" checked="checked" /> 
				 					</pg:equal>
				 					<pg:notequal actual="${param.column}"  value="${columnName}">
				 						 <input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" /> 
				 					</pg:notequal>
						<%        	
	 				            }else{
	 				            	//modify by ge.tao
	 				                //date 2008-01-24
	 				                //外键不允许配置, 但是field_create_orgId所属机构字段 和 field_parentid父类ID字段 可以选择外键: 
	 				                //if(nameObj.getForeignKeyMetaData(metaData.getColumnName())==null || "field_parentid".equalsIgnoreCase(fieldName)){	
	 				            	if(nameObj.getForeignKeyMetaData(metaData.getColumnName())==null || "field_parentid".equalsIgnoreCase(fieldName) || "field_create_orgId".equalsIgnoreCase(fieldName)){
	 				    %>
						            <pg:equal actual="${param.column}"  value="${columnName}">
				 						<input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" checked="checked" /> 
				 					</pg:equal>
				 					<pg:notequal actual="${param.column}"  value="${columnName}">
				 						 <input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" /> 
				 					</pg:notequal>
						<%
						            }
						        }
						    }
						}else{//选择值(主键)字段
						    //判断这个数据库表是否被其他字典类型映射, 如果映射了, 那么其他字典指定的值(主键)字段 是那个字段.
						    
						    if(!"".equals(setPKColumnName.trim()) && metaData.getColumnName().equalsIgnoreCase(setPKColumnName.trim())){
						        hasSetPKColumn = true;
						%>
						        <pg:equal actual="${param.column}"  value="${columnName}">
				 						<input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" checked="checked" /> 
				 					</pg:equal>
				 					<pg:notequal actual="${param.column}"  value="${columnName}">
				 						 <input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" /> 
				 					</pg:notequal>
						<%
						    }else if("".equals(setPKColumnName.trim())){
						%>
						        <pg:equal actual="${param.column}"  value="${columnName}">
				 						<input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" checked="checked" /> 
				 					</pg:equal>
				 					<pg:notequal actual="${param.column}"  value="${columnName}">
				 						 <input type="radio" style="text-align:left" name="columnname" onClick="doReturn(value)" value="<%=metaData.getColumnName()%>" width="10" /> 
				 					</pg:notequal>
						<%    
						    }
						}
						%>
						
					</td>
					<td>
					    <%=isnullable%>
					</td>
					<td>
					    <%=metaData.getColumnName()%>
					</td>					
					<td>
					    <%=metaData.getTypeName()%>
					</td>
					<td >
					    <%=metaData.getColunmSize()%>
					</td>
					<td>
					    <%=metaData.getRemarks()%>
					</td>
					<!--是否主键-->
					<td>
					    <%
					        if(nameObj.getPrimaryKeyMetaData(metaData.getColumnName())!=null){
					    %>
					    <span style='color:red'><pg:message code="sany.pdp.dictmanager.db.table.field.primarykey.yes"/></span>
					    <%        
					        }
					    %>
					</td>
					<!--是否外键-->
					<td>
					    <%
					        if(nameObj.getForeignKeyMetaData(metaData.getColumnName())!=null){
					    %>
					    <span style='color:red'><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.yes"/></span>
					    <%        
					        }
					    %>
					</td>
				</tr>
			<%
			    }
			%>
		    </table>

			<%
		        if(hasSetPKColumn){
		    %>		    
		    	<span style="color:red">
		    	<strong>
		    	<pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.yes" arguments="<%=tablename%>"/>, <br>
		    	&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		    	<pg:message code="sany.pdp.dictmanager.db.table.field.define" arguments="<%=setPKColumnName%>"/> 
		    	</strong></span>		    
		    <%
		        }
		    %>
		    
		    <!--外键信息列表-->
		    <%
		    if(hasFK){
		    %>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
 				<tr>
					<!--设置分页表头-->
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.reference.table" /></th> 
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.field" /></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.reference.table.master" /></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.reference.table.master.field" /></th>					
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.reference.keyname" /></th>
				</tr>
				
				<%
	 			    while(foreignIt.hasNext()){
	 			        ForeignKeyMetaData fmeta = (ForeignKeyMetaData)foreignIt.next(); 			    
 			    %>
 			    <tr>    
 			        <td>
					    <span style="color:red"><%=fmeta.getPKTABLE_NAME()%></span>
					</td>
					<td>
					    <span style="color:red"><%=fmeta.getPKCOLUMN_NAME()%></span>
					</td>					
					<td>
					    <%=fmeta.getFKTABLE_NAME()%>
					</td>
					<td>
					    <%=fmeta.getFKCOLUMN_NAME()%>
					</td>
					<td>
					    <%=fmeta.getFK_NAME()%>
					</td>
                </tr>			
 			    <%    
 			       }
 			    %>		    
		    </table>
		    <strong><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey.reference.title" />
		    </strong>
		    <%
		    }
		    %>
		  </div>
 	</body>
 	<script> 
 		var api = frameElement.api, W = api.opener;
 		
 		function doReturn(value) {
 			W.document.getElementById("${param.fieldName}").value = value;
 		}
 	
 		/*
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
 	    window.onload = function autoRun(){
 	        var selected_value = "<%=selected_column%>";
 	        var arr = new Array();
 	        arr = document.getElementsByName("columnname");
 	        for(var i=0;i<arr.length;i++){
 	            if(arr[i].value==selected_value){
 	                arr[i].checked = true;
 	                //arr[i].disabled = true;
 	            }else{
 	                if(arr[i].value==window.dialogArguments.createTypeForm.db_name.value.toUpperCase() ||
				       arr[i].value==window.dialogArguments.createTypeForm.table_name.value.toUpperCase() || 
				       arr[i].value==window.dialogArguments.createTypeForm.field_name.value.toUpperCase() || 
				       arr[i].value==window.dialogArguments.createTypeForm.field_value.value.toUpperCase() || 
				       arr[i].value==window.dialogArguments.createTypeForm.field_order.value.toUpperCase() || 
				       arr[i].value==window.dialogArguments.createTypeForm.field_typeid.value.toUpperCase() ||
				       arr[i].value==window.dialogArguments.createTypeForm.field_data_validate.value.toUpperCase() ||
				       arr[i].value==window.dialogArguments.createTypeForm.field_create_orgId.value.toUpperCase() ||
				       (window.dialogArguments.createTypeForm.field_parentid !=null &&
				       arr[i].value==window.dialogArguments.createTypeForm.field_parentid.value.toUpperCase()) ){
				          arr[i].disabled = true;
				    }
				    var colum = "<%=columnName%>";
				    if(colum!=""){
				    	var colums = colum.split(",");
				    	for(var j=0;j<colums.length;j++){
				            if(arr[i].value==colums[j]){
				                arr[i].disabled = true;
				            }
				        }
				    }
				    var other_selected = "<%=other_selected%>";
				    if(other_selected!=""){
				        var others = other_selected.split(",");
				        for(var j=0;j<others.length;j++){
				            if(arr[i].value==others[j]){
				                arr[i].disabled = true;
				            }
				        }
				    }
				    
 	            }
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
 	        arr = document.getElementsByName(checkbox_name);
 	        for(var i=0;i<arr.length;i++){ 	            
 	            arr[i].checked = false; 	            
 	        }
 	    }
 	    */
 	</script>
</html>
