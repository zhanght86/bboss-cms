<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<html>
   <head>
   <title>岗位操作</title>
   <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
     <script language="javascript">
  	getNavigatorContent().location.href ="../sysmanager/jobmanager/navigator_content.jsp?anchor=0&collapse=0&request_scope=session";
  	function updateAfter(){
		getNavigatorContent().location.href ="../sysmanager/jobmanager/navigator_content.jsp?anchor=0&expand=0&request_scope=session"; 
	}
    </script>
  </head>
   <body class="contentbodymargin">
  <div id="contentborder" >
 <table>
 <tr>
 <td class="detailtitle">
操作岗位信息成功!!!
 </td>
 </tr>
 </table>
 </div>
 </body>
   
</html>

