<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Dicttype" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);

	String dicttypeId = request.getParameter("dicttypeId");
	dicttypeId = dicttypeId==null?"":dicttypeId;
	//数据采集类型
	String dicttype_type = String.valueOf(session.getAttribute("dicttype_type"));
	dicttype_type = dicttype_type==null?"":dicttype_type;
	//session.removeAttribute("dicttype_type");
%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">
	var did="<%=dicttypeId%>"; 
	function bussinessAction(){
		if(did!="" && did!="null"){ 		
		    if(getPropertiesContent()){			
		    	//alert(getPropertiesContent().location.href);			
				//getPropertiesContent().location.href="<%=rootpath%>/dictmanager/dictmanager.do?method=getDictList&did="+did;
		<%				
				DictManager dicManager = new DictManagerImpl();
				boolean is_tree = dicManager.isTreeDictdata(dicttypeId);
				if(is_tree){
		%>
					getPropertiesContent().location.href="dictdataFrame.jsp?did="+did;
		<%		
				}else{
		%>
				    getPropertiesContent().location.href="dictList.jsp?did="+did;
		<%
				}
		%>
		    }
		}else{
		    getPropertiesContent().location.href="orgTaxcode/org_dictdataShow.jsp";
		} 
	}
	bussinessAction();
	
</script>
<style type="text/css">
<!--
.style1 {color: #000000}
-->
</style>
</head>
<body class="toolbarbodymargin" onload="">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left"  nowrap class="text"><%=dicttype_type%>&nbsp;
	    </td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		<table  cellpadding=0 cellspacing=0 border=0>
			<tr>
			      
<!-----------------------------------------------------------------------------------------------------			-->
			        	
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>
