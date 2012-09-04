<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%  
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	//字典ID
	String did = (String)request.getParameter("dicttypeId");	
	
	//---修改条件
	String keyName = request.getParameter("keyName");
	String keyValue = request.getParameter("keyValue");
	//---可为空的修改条件
	String parentValue = (String)request.getParameter("parentValue");
	//---changeless start
	String dictDataName = (String)request.getParameter("dictDataName");
	String dictDataValue = (String)request.getParameter("dictDataValue");
	String dictDataValueOrg = (String)request.getParameter("dictDataValueOrg");
	String dictDataValidate_ = request.getParameter("dictDataValidate")==null?"1":request.getParameter("dictDataValidate");
	String primaryCondition = request.getParameter("primaryCondition")==null?"":request.getParameter("primaryCondition");	
	//---changeless end
	
	did = did==null?"":did;
	parentValue = parentValue==null?"":parentValue;
	dictDataValueOrg = dictDataValueOrg==null?"":dictDataValueOrg;
	DictManagerImpl dicManager = new DictManagerImpl();
	dicManager.init(request,response);
	Item dictdata = new Item();
	dictdata.setDataId(did);
	dictdata.setName(dictDataName);
	dictdata.setValue(dictDataValue);
	dictdata.setParentId(parentValue);
	dictdata.setDataOrg(dictDataValueOrg);
	dictdata.setDataValidate(Integer.parseInt(dictDataValidate_));	 
	dictdata.setPrimaryCondition(primaryCondition);
	//保存
	boolean saveResult = false;
	try{
		saveResult = dicManager.updateAdvanceDictdata(dictdata, request, keyName, keyValue, parentValue);
	}catch(Exception e){
		e.printStackTrace();
		String message = e.getMessage();
		if(message != null){
			message.replaceAll("\\n","\\\\n");
			message.replaceAll("\\t","\\\\t");
		}
		%>
		<script type="text/javascript">
		<!--
			if(parent.win){			    	
	 	       parent.win.alert("修改失败，原因如下：\n" + "<%=message%>");
	 	       parent.win.close();
 	        }
 	        parent.afterSave();	
		//-->
		</script>
		<%
	}
	
%>
	<script language="javascript">		
		var api = parent.frameElement.api, W = api.opener;
	
	    <%
	        if(saveResult){
	    %> 
		    W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>', function() {
	    		W.location.reload();
	    		api.close();
	    	}, api);				
		<%
		    }else{
		%>	
		    	//parent.win.alert("【显示数据】或者【真实值】已经存在 \n ");
		    	
		    	W.$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.base.modfiy.fail.exist"/>', function() {
		    		W.location.reload();
		    		api.close();
		    	}, api);
		<%
		    }
		%>
		
	</script>

 


 



 


 


