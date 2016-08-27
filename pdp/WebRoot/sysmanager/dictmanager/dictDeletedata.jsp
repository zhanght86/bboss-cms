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
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String did = (String)request.getParameter("did");
	String mutiInfo = (String)request.getParameter("infos");
	String queryString = request.getParameter("queryString");
	did = did==null?"":did;
	mutiInfo = mutiInfo==null?"":mutiInfo;
	String[] infos = mutiInfo.split(",");
	DictManager dicManager = new DictManagerImpl();
	//删除数据项,包含递归删除子数据项
	boolean state = dicManager.deletedictdatas(did,infos);
	
	
%>
	<script language="javascript">	
	<%if(state){%>
		
		$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>', function() {
			parent.location.reload();
		});
		
		//parent.win.alert("操作成功!");		
		//parent.enable();
		//parent.afterDeleteRefresh(<%=queryString%>);
		//parent.win.close();
	<%}else{%>
	
		$.dialog.alert('<pg:message code="sany.pdp.dictmanager.data.delete.fail"/>', function() {
			parent.location.reload();
		};
	
		//parent.win.alert("操作失败!该数据项有子数据项或者该数据项已经被授权引用！");	
		//parent.enable();
		//parent.win.close();	
	<%}%>
	</script>

 


 


