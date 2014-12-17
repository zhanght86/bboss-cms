<%     
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
<%
/**
 * 
 * <p>Title: 关键字设置页面</p>
 *
 * <p>Description: 关键字设置页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
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
<%@ page import="com.frameworkset.platform.dictionary.DictManager"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl,
				com.frameworkset.dictionary.Data,
 				com.frameworkset.platform.dictionary.DictKeyWordManager,
 				com.frameworkset.platform.dictionary.DictKeyWordManagerImpl,
 				com.frameworkset.platform.dictionary.KeyWord,
 				java.util.Map"%>
 				
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	DictManager dicManager = new DictManagerImpl();

	String dictTypeId = request.getParameter("dictTypeId");//字典类型ID
	String dbname = (String)request.getParameter("dbname");//数据库名称
	String tablename = (String)request.getParameter("tablename");//表名称
	
	TableMetaData nameObj = DBUtil.getTableMetaData(dbname,tablename);
	Set set = DBUtil.getColumnMetaDatas(dbname,tablename);	
	Iterator it = set.iterator();
	
	Data data = dicManager.getDicttypeById(dictTypeId);
	String name = data.getName();//字典名称
	DictKeyWordManager dictKeyWordManager = new DictKeyWordManagerImpl();
	KeyWord keyWord = null;
	Map map = dictKeyWordManager.getAllKeyWords(dictTypeId);
	Map mapAll = dictKeyWordManager.getAllDictFields(dictTypeId);
	//字典换表出现的多余关键字项
	Map mapAllKey = dictKeyWordManager.getInvalidationKeys(dictTypeId,dbname,tablename);
	boolean isAppear = false;
%>
<html>
    <head>
    <base target="_self">
        <title>字典【<%=name%>】关键字段定义</title>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
    </head>
    <body onunload="window.returnValue='ok';">
    	<div id="changeColor">
		<form name="dictList" method="post" >
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
			<input name="checkValues" type="hidden" value="" />
 				<tr>
					<th width="30px">
						<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on">
					</th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.null"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.name"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.type"/></th> 
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.length"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.description"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.primarykey"/></th>
					<th><pg:message code="sany.pdp.dictmanager.db.table.field.foreignkey"/></th>
					<th ><pg:message code="sany.pdp.dictmanager.db.table.field.java"/></th>
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
			        isnullable = "<span style='color:red'><pg:message code='sany.pdp.dictmanager.db.table.field.null.no'/><span>";
			    }
			    String columnName = metaData.getColumnName();
			    if(mapAll.get(columnName) != null){
			    //判断是否为主键 提示信息
			    
			    //判断是否是外键 提示信息
 			%>	
	 			<tr>	
	 				<td class="td_center">
							<input type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<%=metaData.getColumnName()%>'
							<%
								if(map.get(columnName) != null){
									keyWord = (KeyWord)map.get(columnName);
							%>
								checked="true"
							<%
								}
							%>
							 />
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
					<td>
					    <%=metaData.getColunmSize()%>
					</td>
					<td >
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
					<td>
					    <input type="text" name="javaProperty" value="<%=map.get(columnName)==null?"":keyWord.getJavaProperty()%>">
					</td>
				</tr>
				
			<%
			    }else{
			    	if(map.get(columnName) != null){
			    		isAppear = true;
			    	}
			    }
			    }
			%>
		    </table>
		    
		    <div class="btnarea" >
		    	<a href="javascript:void(0)" class="bt_1" id="resetButton" onclick="saveKeyField()"><span><pg:message code="sany.pdp.common.operation.ok"/></span> </a>
		    	<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
		    </div>
		    
		    <%
		    	//换表之后形成的多余关键字段与字典换了已设置好的关键字段的显示
		    	if(isAppear || (mapAllKey.size() > 0 && mapAllKey != null)){
		    %>
		    
		    <table cellspacing="1" cellpadding="0" border="0" width=98% height="100%"  class="thin">
		    <tr>
		    <td>
		    	<iframe src="invalidationKey_iframe.jsp?dictTypeId=<%=dictTypeId%>&dbname=<%=dbname%>&tablename=<%=tablename%>" name="invalidationKey" style="width:100%" height="100%" scrolling="no" frameborder="0" marginwidth="1" marginheight="1"></iframe>
		    </td>
		    </tr>
		    </table>
		    <%
		    	}
		    %>
		</form>
		</div>
 	</body>
 	<iframe name="hiddenFrame" width="0" height="0"></iframe>
 	<script> 	    
 		var api = frameElement.api, W = api.opener;
 	
 	   function saveKeyField(){
 	   		var obj = document.getElementsByName("checkBoxOne");
 	   		var obj2 = document.getElementsByName("javaProperty");
 	   		var checkValues = "";
 	   		for(var i = 0; i < obj.length; i++){
 	   			if(obj[i].checked){
 	   				if(checkValues == ""){
 	   					checkValues = obj[i].value+"#-#"+obj2[i].value+" ";
 	   				}else{
 	   					checkValues += "-#-" + obj[i].value+"#-#"+obj2[i].value+" ";
 	   				}
 	   			}
 	   		}
 	   		document.dictList.checkValues.value = checkValues;
 	   		document.dictList.target = "hiddenFrame";
 	   		document.dictList.action = "keySave_handle.jsp?dictTypeId=<%=dictTypeId%>";
 	   		document.dictList.submit();		
 	   }
 	   
 	   function winClose(){
 	   		window.close();
 	   		window.returnValue = "ok";
 	   }
 	   
 	   function parentSubmit(){
 	   	document.forms[0].action = document.location.href;
	   	document.forms[0].submit();
	   }
 	</script>
</html>
