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
	//已设置的关键字段
	Map map = dictKeyWordManager.getAllKeyWords(dictTypeId);
	//字典换表出现的多余关键字项
	Map mapAllKey = dictKeyWordManager.getInvalidationKeys(dictTypeId,dbname,tablename);
	//字典所有设置的字段选项，字段为“字典名称字段”与“字典值字段”与字典高级字段
	Map mapAll = dictKeyWordManager.getAllDictFields(dictTypeId);
%>
<html>
    <head>
        <title>字典【<%=name%>】关键字段定义</title>
        <link href="<%=rootpath%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
    </head>
    <body onunload="window.returnValue='ok';">
		<form name="dictList" method="post" >
			<table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
			<input name="checkValues" type="hidden" value="" />
				<tr>
				<td colspan="9"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>
					以下关键字段没有被配置为字典字段请删除（失效原因换表造成或者修改字典时去掉了已定义为关键字段的字段）
				</strong>
				</td>
				</tr>
 				<tr>
					<!--设置分页表头-->
					<td class="headercolor" width="10">
					<P align="left">
					<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on">
					</P>
					</td>
					<td class="headercolor" >能否为空</td>
					<td class="headercolor" >字段名称</td>
					<td class="headercolor" >字段类型</td> 
					<td class="headercolor" >字段长度</td>
					<td class="headercolor" >字段说明</td>
					<td class="headercolor" >是否主键</td>
					<td class="headercolor" >是否外键</td>
					<td class="headercolor" >java属性名称</td>
				</tr>
 			<%
			String setPKColumnName = "";
		    boolean hasSetPKColumn = false;
		    setPKColumnName = dicManager.getOtherDicttypePKColumnByTable(dbname,tablename);
 			while(it.hasNext()){
			    ColumnMetaData  metaData = (ColumnMetaData)it.next();
			    String isnullable = "可为空";
			    boolean isDateFlag = false;
			    if(metaData.getTypeName().equalsIgnoreCase("date")){
			        isDateFlag = true;
			    }
			    if("no".equalsIgnoreCase(metaData.getNullable())){
			        isnullable = "<span style='color:red'>不能为空<span>";
			    }
			    String columnName = metaData.getColumnName();
			    if(mapAll.get(columnName) == null && map.get(columnName) != null){
			    	keyWord = (KeyWord)map.get(columnName);
			    //判断是否为主键 提示信息
			    
			    //判断是否是外键 提示信息
 			%>	
	 			<tr>	
	 				<td class="tablecells" nowrap="nowrap">
 				    	<P align="left">
							<input type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<%=metaData.getColumnName()%>'
							 />
						</P>
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=isnullable%>
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
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=map.get(columnName)==null?"":keyWord.getJavaProperty()%>
					</td>
				</tr>
				
			<%
			    }
			    }
			    if(mapAllKey.size() > 0 && mapAllKey != null){
			    	Iterator iterator = mapAllKey.keySet().iterator();
			    	while(iterator.hasNext()){
			    		keyWord = (KeyWord)map.get(iterator.next().toString());
			%>
			<tr>	
	 				<td class="tablecells" nowrap="nowrap">
 				    	<P align="left">
							<input type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<%=keyWord.getFieldName()%>'
							 />
						</P>
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=keyWord.getFieldName()%>(换表形成的失效关键字段)
					</td>					
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					</td>
					<!--是否主键-->
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					</td>
					<!--是否外键-->
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					</td>
					<td class="tablecells" style="text-align:left" nowrap="nowrap">
					    <%=keyWord.getJavaProperty()%>
					</td>
				</tr>
			<%
			    	}
			    }
			%>
			<tr>
			<td  align="left" colspan="9" nowrap>
				<input type="button" value="删除" class="input" onclick="delKeyField()">
			</td>
			</tr>
		    </table>
		</form>
	
 	</body>
 	<iframe name="hiddenFrame" width="0" height="0"></iframe>
 	<script> 	    
 	   function delKeyField(){
 	   		var obj = document.getElementsByName("checkBoxOne");
 	   		var checkValues = "";
 	   		if(obj.length < 1){
 	   			alert("没有可删除的数据！");
 	   			return false;
 	   		}
 	   		for(var i = 0; i < obj.length; i++){
 	   			if(obj[i].checked){
 	   				if(checkValues == ""){
 	   					checkValues = obj[i].value;
 	   				}else{
 	   					checkValues += "-#-" + obj[i].value;
 	   				}
 	   			}
 	   		}
 	   		document.dictList.checkValues.value = checkValues;
 	   		document.dictList.target = "hiddenFrame";
 	   		document.dictList.action = "keydel_handle.jsp?dictTypeId=<%=dictTypeId%>";
 	   		document.dictList.submit();		
 	   }
 	   
 	   function winClose(){
 	   		window.close();
 	   		window.returnValue = "ok";
 	   }
 	</script>
</html>
