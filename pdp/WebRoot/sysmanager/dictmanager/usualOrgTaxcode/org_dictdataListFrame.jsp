<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String orgId = request.getParameter("orgId");
	String did = request.getParameter("did");
	String typeName = request.getParameter("typeName");
	
	Data dtype = null;	
	DictManager dictManager = null;
	if(!"".equals(did)){
		dictManager = new DictManagerImpl();
		dtype = dictManager.getDicttypeById(did);
		typeName = dtype.getDescription().trim();
	}
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../../css/navigator.css">
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
		<%@ include file="/include/css.jsp"%>		
	</head>
	<!-- <frameset name="orgTaxcodeDataMain"  rows="50,50" frameborder="no" border="0" framespacing="0" >
	
	    
	    <frame name="dictList"  src="org_dictdataList.jsp?orgId=<%=orgId%>&did=<%=did%>&typeName=<%=typeName%>"  scrolling="yes" noresize="noresize" />
	    <frame name="dataSelected" src="org_dictdataSelectedList.jsp?orgId=<%=orgId%>&did=<%=did%>&typeName=<%=typeName%>"  scrolling="yes" noresize="noresize"  />
		
		
	</frameset>	
	<noframes>
	<body>
	</body>
	
	</noframes> -->
	<body>
	<p align="center"><font size=2><strong><%=typeName%>(<%=dtype.getDataTableName()%>)</strong></font>
	<br/>
	<table width=100% height=100%>
	<tr height=100%>
	<td width=50%>
	<iframe name="dictList"  src="org_dictdataList.jsp?orgId=<%=orgId%>&did=<%=did%>&typeName=<%=typeName%>&identifier=sealed"  style="width:100%" height="100%" scrolling="no" frameborder="0" marginwidth="1" marginheight="1"></iframe>
	</td>
	<td width=50%>
	<iframe name="dataSelected" src="org_dictdataSelectedList.jsp?orgId=<%=orgId%>&did=<%=did%>&typeName=<%=typeName%>&identifier=selected" style="width:100%" height="100%" scrolling="no" frameborder="0" marginwidth="1" marginheight="1"></iframe>
	</td>
	</tr>
	</table>
	<SCRIPT>
	function refurbishIframe(){
  		var tablesFrame= document.getElementsByName("dataSelected");
  		tablesFrame[0].src = "org_dictdataSelectedList.jsp?orgId=<%=orgId%>&did=<%=did%>&typeName=<%=typeName%>&identifier=selected";
	}
	function refurbishIframeSelected(){
  		var tablesFrame= document.getElementsByName("dictList");
  		tablesFrame[0].src = "org_dictdataList.jsp?orgId=<%=orgId%>&did=<%=did%>&typeName=<%=typeName%>&identifier=sealed";
	}
	</SCRIPT>
	</body>
</html>
