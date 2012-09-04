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
    </head>
  	<body>
		<form name="dictList" method="post" target="hiddenFrame">
			<div style="width:100%;height:440;overflow:auto"> 
			<table cellspacing="1" cellpadding="0" border="0" width=100%  class="thin">
				<pg:listdata dataInfo="com.frameworkset.platform.dictionary.tag.DictList" keyName="DictList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="13" scope="request" data="DictList" isList="false">
					<tr>
						<!--设置分页表头-->					
						<td class="headercolor" nowrap><%=data_name_cn%></td>								
						<td class="headercolor" colspan="2" nowrap ><%=data_value_cn%></td>
						<%
						    if(showDataValidate){
						%>
						    <td class="headercolor" colspan="1" nowrap >是否有效</td>
						<%
						    }
						%>
						<%
						    if(showDataValueOrg){
						%>
						    <td class="headercolor" colspan="4" nowrap >采集机构</td>
						<%
						    }
						%>
						<!--附加字段处理-->
		 				<%
		 				    for(int i=0;i<dictatts.size();i++){
		 				        DictAttachField dictatt = (DictAttachField)dictatts.get(i);
								//InputType inputType = dictatt.getInputType();	
						%>
							<td class="headercolor" nowrap ><%=dictatt.getDictFieldName()%></td>
						<%}%>
					</tr>
					<pg:param name="did" />
					<pg:param name="selected" />
					<pg:param name="dicttypeId" />
					<pg:param name="showdata" />
					<pg:param name="realitydata" />
					<pg:param name="occurOrg" />
					<pg:param name="isaVailability" />
					
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>
								暂时没有数据
							</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<pg:list>		
						<%
							String value = dataSet.getString("value");
							String name = dataSet.getString("name");
						%>				
						<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="name" defaultValue="" />
							</td>									
							<td class="tablecells" nowrap="nowrap" colspan="2">
								<pg:cell colName="value" defaultValue="" />
							</td>
							<%
							    if(showDataValidate){
							%>
							    <td class="tablecells" nowrap="nowrap" colspan="1">
									<pg:equal colName="dataValidate" value="1">启用</pg:equal>	
  									<pg:equal colName="dataValidate" value="0">停用</pg:equal>
								</td>
							<%
							    }
							%>
							<%
							    if(showDataValueOrg){
							%>
							    <td class="tablecells" nowrap="nowrap" colspan="4">
									<pg:cell colName="dataOrg" defaultValue="" />
								</td>
							<%
							    }
							%>
							<!--附加字段值处理-->
							<%
		 				    for(int i=0;i<dictatts.size();i++){
		 				        DictAttachField dictatt = (DictAttachField)dictatts.get(i);
								//InputType inputType = dictatt.getInputType();
								String valueAttach = dictManager.getAttachValue(dicttypeId,name,value,dictatt.getDictField());	
							%>
							<td class="tablecells" nowrap="nowrap" ><%=valueAttach%></td>
							<%}%>
						</tr>
					</pg:list>
					
					<input name="queryString" value="<pg:querystring/>" type="hidden">
					<input name="did" value="<%=ids%>" type="hidden">
					<input name="selected" value="<%=selected%>" type="hidden">
					<input name="dicttypeId" value="<%=dicttypeId%>" type="hidden">
				</pg:pager>
				<tr height="30" class="labeltable_middle_tr_01">
					<td colspan=100 align='center'><pg:index />
				</tr>
		  </table>
		  </div>
		</form>
		<iframe height="0" width="0" name="hiddenFrame"></iframe>
	</body>
		
</html>
