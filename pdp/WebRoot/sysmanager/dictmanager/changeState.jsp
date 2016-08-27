<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	/**
	 * 树形结构的停用启用处理页面
	 */
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
	String did = request.getParameter("did");
	String dictdataValue = request.getParameter("dictdata");
	String flag = request.getParameter("flag");
	String docid = request.getParameter("docid");
	String isRescure = request.getParameter("isRescure")==null?"0":request.getParameter("isRescure");
	String[] doc = docid.split(",");
	DictManager dictManager = new DictManagerImpl();
	//modify by ge.tao
	//新增了是否递归启用/停用的参数
	boolean state = dictManager.changeState(did, dictdataValue, doc, flag,isRescure);
%>

<script>
	if(<%=state%>){
		alert("操作成功！");

  		parent.parent.newDictdata.document.location.href = parent.parent.newDictdata.document.location.href ;
	}else{
		alert("操作失败！");
	}
</script>
