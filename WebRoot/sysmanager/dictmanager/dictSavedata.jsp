ao<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManager"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl"%>
<%@ page import="com.frameworkset.dictionary.Data"%>
<%@ page import="com.frameworkset.dictionary.Item"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%  
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String did = (String)request.getParameter("did");	
	String parentValue = (String)request.getParameter("parentValue");
	String dictDataName = (String)request.getParameter("dictDataName");
	String dictDataValue = (String)request.getParameter("dictDataValue");
	String dictDataValueOrg = (String)request.getParameter("dictDataValueOrg");
	String dictDataValidate_ = (String)request.getParameter("dictDataValidate");
	did = did==null?"":did;
	dictDataName = dictDataName==null?"":dictDataName;
	dictDataValue = dictDataValue==null?"":dictDataValue;
	parentValue = parentValue==null?"":parentValue;
	dictDataValueOrg = dictDataValueOrg==null?"":dictDataValueOrg;
	
	int dictDataValidate = 1;
	try{
	    dictDataValidate = Integer.parseInt(dictDataValidate_);
	}catch(Exception e){
	    dictDataValidate = 1;
	}
	
	DictManager dicManager = new DictManagerImpl();
	Item dictdata = new Item();
	dictdata.setDataId(did);
	dictdata.setName(dictDataName);
	dictdata.setValue(dictDataValue);
	dictdata.setParentId(parentValue);
	dictdata.setDataOrg(dictDataValueOrg);
	dictdata.setDataValidate(dictDataValidate);
//	dictdata.setDataValidate();	 
	//保存
	boolean saveResult = dicManager.addDictdata(dictdata);
	
%>
	<script language="javascript">		
	    <%
	        if(saveResult){
	    %>
				parent.win.alert("操作成功!");					
		<%
		    }else{
		%>	
		    	parent.win.alert("【显示数据】或者【真实值】已经存在!");
		<%
		    }
		%>
		parent.enable();
		parent.afterAddRefresh();
		parent.win.close();
	</script>

 


 


