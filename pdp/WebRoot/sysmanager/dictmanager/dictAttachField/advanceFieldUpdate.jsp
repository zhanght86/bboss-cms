<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.dictionary.DictAttachField"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	DictManager dictManager = new DictManagerImpl();	
	String op = request.getParameter("op")==null?"":request.getParameter("op");
	String dicttypeId = (String)request.getParameter("dicttypeId");
	String dictField = (String)request.getParameter("dictField");
	String dictFieldName = (String)request.getParameter("dictFieldName");
	String inputTypeId = (String)request.getParameter("inputTypeId");
	String table_column = (String)request.getParameter("table_column");
	String isnullable_ = request.getParameter("isnullable")==null?"":request.getParameter("isnullable");
	String isunique_ = request.getParameter("isunique")==null?"":request.getParameter("isunique");
	int isnullable = 0;
	try{
	    isnullable = Integer.parseInt(isnullable_);
	}catch(Exception e11){
	    isnullable = 0;
	}
	int isunique = 0;
	try{
	    isunique = Integer.parseInt(isunique_);
	}catch(Exception e11){
	    isunique = 0;
	}
	String selectDicttye = request.getParameter("selectDicttye")==null?"":request.getParameter("selectDicttye");
	String opcode = request.getParameter("opcode")==null?"":request.getParameter("opcode");
	//如果是选择了 "选择字典" 类型 输入框的名称 是 输入框的名称:字典类型Name:操作类型(可见/常用)
	if(selectDicttye.trim().length()>0){
	    dictField = dictField + ":" + selectDicttye + ":" + opcode;

	}
	
	DictAttachField dictatt = new DictAttachField();
	dictatt.setDictField(dictField);
    dictatt.setDictFieldName(dictFieldName);
    dictatt.setDicttypeId(dicttypeId);
    dictatt.setInputTypeId(inputTypeId);
    dictatt.setTable_column(table_column);
    dictatt.setIsnullable(isnullable);
    dictatt.setIsunique(isunique);
	boolean saveFlag = false;
	
	saveFlag = dictManager.updateDictdataAttachField(dictatt);
	
	
%>	
	<script language="javascript">		
		<%
		    if(saveFlag){		    
		%>
		
 	    parent.win.alert("保存成功!");
 	    parent.win.close();
		parent.afterSave();
		<%
		    }else{
		%>
		parent.win.close();
		parent.win.alert("字段或者字段名称已经被使用 \n保存失败!"); 	        
		parent.saveFaild();
		<%
		    }
		%>
	</script>
	
