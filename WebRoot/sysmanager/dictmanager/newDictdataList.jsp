<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%@ page import="com.frameworkset.platform.dictionary.input.InputType"%>
<%@ page import="com.frameworkset.platform.dictionary.DictAttachField,
				com.frameworkset.platform.dictionary.input.InputTypeScript,
				com.frameworkset.platform.dictionary.input.InputTypeScriptImpl"%>
<%@ page import="java.util.*"%>
<%	 
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String currentOrgName = accesscontroler.getChargeOrg() != null ?accesscontroler.getChargeOrg().getRemark5():"";
	String currentOrgIdNO = accesscontroler.getChargeOrgCode();
	String ids = request.getParameter("did");	
	String selected = request.getParameter("selected");
	String dicttypeId = request.getParameter("dicttypeId");
	ids = ids==null?"":ids;
	if("".equals(ids)){
	    ids = request.getParameter("selected");
	}
	String dicttype_name = "";
	String did = "";
	//实际上是datavalue 父数据项的值
	String dictdataValue = "";
	String dictdataName = "";
	session.setAttribute("dictTabId", "2");
	Data dtype = null;	
	DictManagerImpl dictManager = new DictManagerImpl();
	dictManager.init(request,response);
	if(!"".equals(ids)){
		
		String[] ids_ = ids.split(":");
		if(ids_.length>1){
			did = ids_[0];	
			dictdataValue = ids_[1];
			if(!"root".equals(ids_[2])){
			    dictdataName = ":"+ids_[2];
			}
		}else{
		    did = ids;	
			dictdataValue = "root";
		}
		dtype = dictManager.getDicttypeById(did);
		dicttype_name = dtype.getName();
	}
	if(dtype == null){
		dtype = new Data();
	}	
	String dbname = dtype.getDataDBName();
    String tablename = dtype.getDataTableName();
    String dataNameField = dtype.getDataNameField();
    String dataValueField = dtype.getDataValueField();
    
    //字典数据:名称字段对象
    ColumnMetaData nameObj = DBUtil.getColumnMetaData(dbname,tablename,dataNameField);
    //字典数据:值字段对象
    ColumnMetaData valueObj = DBUtil.getColumnMetaData(dbname,tablename,dataValueField);
    //数据项是否有效,在列表中体现,同时显示"停用/启用功能"
    boolean showDataValidate = false;
    String dataValidate = dtype.getData_validate_field();
    //数据项所属机构,可多个,缺省是登陆用户的机构,在新增的时候体现,在列表体现
    boolean showDataValueOrg = false;
    String dataValueOrg = dtype.getData_create_orgid_field();
    
    //是否有排序字段,决定是否显示排序按钮
    boolean showOrderButton = false;
    String dataOrderField = dtype.getDataOrderField();
    if(dataOrderField != null && dataOrderField.trim().length()>0){
        showOrderButton = true;
    }
    
    //新维护两个字段
    String data_name_cn = dtype.getField_name_cn()==""?"名称":dtype.getField_name_cn();
    String data_value_cn = dtype.getField_value_cn()==""?"真实值":dtype.getField_value_cn();
	
	if("root".equals(dictdataValue)){
	    dictdataValue="";
	}
	String nameValidType = dictManager.getValidatorTypeByColumnMetaData(nameObj);
    String valueValidType = dictManager.getValidatorTypeByColumnMetaData(valueObj);
    
    String dataValidateType = "";
    String dataValidateTypeName = "";
    String dataOrgType = "";
    String dataOrgTypeName = "";
    if(dataValidate != null && dataValidate.trim().length()>0){
        //字段数据:是否有效
        showDataValidate = true;
    	ColumnMetaData validateObj = DBUtil.getColumnMetaData(dbname,tablename,dataValidate);
        dataValidateType = dictManager.getValidatorTypeByColumnMetaData(validateObj);
        dataValidateTypeName = validateObj.getTypeName();
    }
    if(dataValueOrg != null && dataValueOrg.trim().length()>0){        
    	//字段数据:数据项所属机构
    	showDataValueOrg = true;
    	ColumnMetaData dataOrgObj = DBUtil.getColumnMetaData(dbname,tablename,dataValueOrg);
        dataOrgType = dictManager.getValidatorTypeByColumnMetaData(dataOrgObj);
        dataOrgTypeName = dataOrgObj.getTypeName();
    }
    
    
    List dictatts = dictManager.getDictdataAttachFieldList(dicttypeId,-1);
%>

<html>
	<head>
		<base target="_self">
		<title>【<%=dicttype_name%><%=dictdataName%>】高级数据列表</title>
		<link href="<%=rootpath%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
        <SCRIPT language="JavaScript" SRC="../user/validateForm.js"></SCRIPT>
<SCRIPT language="javascript">
	
	function queryDictItem(){
	  var tablesFrame= document.getElementsByName("theIframe");
	  var showdata = document.all.showdata.value;
		var realitydata = document.all.realitydata.value;
		var occurOrg = "";
		if(document.all.occurOrg){
			occurOrg = document.all.occurOrg.value;
		}
		var isaVailability = "-1";
		if(document.all.isaVailability){
			isaVailability = document.all.isaVailability.value;
		}
	  tablesFrame[0].src = "newDictdataList_iframe.jsp?did=<%=ids%>&selected=<%=selected%>&dicttypeId=<%=did%>&showdata="+showdata+"&realitydata="+realitydata+"&occurOrg="+occurOrg+"&isaVailability="+isaVailability;
	}
	
	function resetQuery(){
		document.all.showdata.value="";
		document.all.realitydata.value="";
		if(document.all.occurOrg){
			document.all.occurOrg.value="";
		}
		if(document.all.isaVailability){
			document.all.isaVailability.value="-1";
		}
	}
</SCRIPT>
    </head>
  	<body>
		<form name="dictList" method="post" target="hiddenFrame">
		<table  width="98%" border="0"   cellpadding="0" cellspacing="1" class="thin">
			    <tr>
   					<td height='30'  colspan="2"><img src='../images/edit.gif' height="16" border=0 >
   					    &nbsp;<strong>【<%=dicttype_name%><%=dictdataName%>】高级数据列表</strong>
   					</td>
 				</tr>
 				<tr>
 				<td colspan="2" align="right" style="text-align:left">
 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=data_name_cn%>：<input name="showdata" value="" type="text" />
 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 				&nbsp;&nbsp;&nbsp;<%=data_value_cn%>：<input name="realitydata" value="" type="text" />
 				</td>
 				</tr>
 				<tr>
 				<td colspan="2" align="right" style="text-align:left">
 				<%if(showDataValueOrg){%>
 				采集机构：<input name="occurOrg" value="" type="text" />
 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 				<%}%>
 				<%
 					 if(showDataValidate){
 				%>
 				是否启用：<select name="isaVailability">
			    		<option value="-1">--请选择--</option>
			    		<option value="1">启用</option>
			    		<option value="0">停用</option>
			    	    </select>
			   	<%
			   		}
			   	%>
			    </td>
 				</tr>
			    <tr>
			    <td colspan="2" align="right" style="text-align:right">
			    <input name="queryBut" type="button" value="查询" class="input" onClick="queryDictItem();" />
			    <input name="resetBut" type="button" value="重置" class="input" onclick="resetQuery();" />
			    <input name="closeBut" type="button" value="关闭" class="input" onclick="window.close();" />
			    </td>
			    </tr>
 		</table>
			<table cellspacing="1" cellpadding="0" border="0" width=98% height=100%  class="thin">
				<tr><td>
				<iframe name="theIframe" src="newDictdataList_iframe.jsp?did=<%=ids%>&selected=<%=selected%>&dicttypeId=<%=did%>" style="width:100%" height="100%" scrolling="no" frameborder="0" marginwidth="1" marginheight="1"></iframe>
				</td></tr>
		  </table>
		</form>
		<iframe height="0" width="0" name="hiddenFrame"></iframe>
	</body>
		
</html>
