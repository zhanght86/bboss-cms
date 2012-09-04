<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>  
  <title>属性容器</title>
  <title>岗位操作</title>
   <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="sysmanager/css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="sysmanager/css/tab.winclassic.css">
   
  
  <script language="javascript">
  getNavigatorContent().location.href ="sysmanager/jobmanager/navigator_content.jsp";
  	function updateAfter(){
		getNavigatorContent().location.href ="sysmanager/jobmanager/navigator_content.jsp"; 
	}	
	
    function disableButton(){        
        while(!getPropertiesToolbar().tools1){
            ;
        }
        getPropertiesToolbar().tools1.disabled = true;            
        getPropertiesToolbar().tools2.disabled = true;
        getPropertiesToolbar().tools3.disabled = true;           
    }
   window.location.href="<%=basePath%>/sysmanager/jobmanager/A03/jobinfo.jsp?action=add";
    </script>
</head>
</html>

