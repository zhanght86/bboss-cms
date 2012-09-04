<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.util.StringUtil"%>
<%
String action = StringUtil.replaceNull(request.getParameter("action"));
String resId = StringUtil.replaceNull(request.getParameter("resId"));
String parentId = StringUtil.replaceNull(request.getParameter("parentId"));
String restypeId=request.getParameter("restypeId");
if(parentId.equals(""))parentId="0";
%>
<html>
<head>     
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
<script language="javaScript" src="../validateForm.js"></script> 
<script language="JavaScript">
	
var action="<%=action%>";
var resId="<%=resId%>";
var parentId ="<%=parentId%>";

//if(action=="update")
//getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/resmanager/navigator_content.jsp?anchor=<%=parentId%>&collapse=<%=parentId%>&request_scope=session&selectedNode=<%=resId%>";
//function updateAfter(){
//getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/resmanager/navigator_content.jsp?anchor=<%=parentId%>&expand=<%=parentId%>&request_scope=session&selectedNode=<%=resId%>"; 
//}

function createres()
{	
    var resId=document.form2.resId.value;   
	window.location.href="<%=rootpath%>/resmanager/resource.do?method=newresource&resId="+resId;
	
}
function deleteres(){
    
	document.forms[0].action="<%=rootpath%>/resmanager/resource.do?method=deleteresource&resId="+resId;
	document.forms[0].submit();
}
function modifyres(){
	
		document.forms[0].action="<%=rootpath%>/resmanager/resource.do?method=modifyresource&restypeId=<%=restypeId%>&resId="+resId;
		document.forms[0].submit();
	
}
</script>
</head>

<body class="contentbodymargin">
<div id="contentborder" align="center">
<br><br>
<center>

<form name="form2" action="" method="post"  >	
<pg:beaninfo requestKey="Res" needClear="false">
	
		 <input type="hidden"  name="resId" value="<pg:cell colName="resId"  defaultValue=""/>"/>
		 		 			 		 
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />
		
		 <input type="hidden"  name="marker" value="<pg:cell colName="marker"  defaultValue=""/>" />
		 <input type="hidden"  name="reserved1" value="<pg:cell colName="reserved1"  defaultValue=""/>" />
		 <input type="hidden"  name="reserved3" value="<pg:cell colName="reserved3"  defaultValue=""/>" />
		 <input type="hidden"  name="reserved4" value="<pg:cell colName="reserved4"  defaultValue=""/>" />
		 <input type="hidden"  name="reserved5" value="<pg:cell colName="reserved5"   defaultValue=""/>">
		 <input type="hidden"  name="attr1" value="<pg:cell colName="attr1"  defaultValue=""/>" />
		 <input type="hidden"  name="attr2" value="<pg:cell colName="attr2"  defaultValue=""/>" />
		 <input type="hidden"  name="attr3" value="<pg:cell colName="attr3"  defaultValue=""/>" />
		 <input type="hidden"  name="attr4" value="<pg:cell colName="attr4"  defaultValue=""/>" />
		 <input type="hidden"  name="attr5" value="<pg:cell colName="attr5"  defaultValue=""/>" />
		 <input type="hidden"  name="attr6" value="<pg:cell colName="attr6"  defaultValue=""/>" />
		 <input type="hidden"  name="attr7" value="<pg:cell colName="attr7"  defaultValue=""/>" />
		 <input type="hidden"  name="attr8" value="<pg:cell colName="attr8"  defaultValue=""/>" />
		 <input type="hidden"  name="attr9" value="<pg:cell colName="attr9"  defaultValue=""/>" />
		 <input type="hidden"  name="attr10" value="<pg:cell colName="attr10"  defaultValue=""/>" />
		 <input type="hidden"  name="attr11" value="<pg:cell colName="attr11"  defaultValue=""/>" />
		 <input type="hidden"  name="attr12" value="<pg:cell colName="attr12"  defaultValue=""/>" />
		  <input type="hidden"  name="attr13" value="<pg:cell colName="attr13"  defaultValue=""/>" />
		 <input type="hidden"  name="attr14" value="<pg:cell colName="attr14"  defaultValue=""/>" />
		 <input type="hidden"  name="attr15" value="<pg:cell colName="attr15"  defaultValue=""/>" />
		 <input type="hidden"  name="attr16" value="<pg:cell colName="attr16"  defaultValue=""/>" />
		 <input type="hidden"  name="attr17" value="<pg:cell colName="attr17"  defaultValue=""/>" />
		 <input type="hidden"  name="attr18" value="<pg:cell colName="attr18"  defaultValue=""/>" />
		 <input type="hidden"  name="attr19" value="<pg:cell colName="attr19"  defaultValue=""/>" />
		 <input type="hidden"  name="attr20" value="<pg:cell colName="attr20"  defaultValue=""/>" />
		 <input type="hidden"  name="attr21" value="<pg:cell colName="attr21"  defaultValue=""/>" />
		 <input type="hidden"  name="attr22" value="<pg:cell colName="attr22"  defaultValue=""/>" />
		 <input type="hidden"  name="attr23" value="<pg:cell colName="attr23"  defaultValue=""/>" />
		 <input type="hidden"  name="attr24" value="<pg:cell colName="attr24"  defaultValue=""/>" />
		 <input type="hidden"  name="attr25" value="<pg:cell colName="attr25"  defaultValue=""/>" />
		 <input type="hidden"  name="attr26" value="<pg:cell colName="attr26"  defaultValue=""/>" />
		 <input type="hidden"  name="attr27" value="<pg:cell colName="attr27"  defaultValue=""/>" />
		 <input type="hidden"  name="restypeId" value="<pg:cell colName="restypeId"  defaultValue="<%=restypeId%>"/>" />
		 			 
<table width="80%"  cellpadding="0" cellspacing="1" >
  
  <tr  >    
    <td align="center" class="detailtitle">名称</td>
    <td align="left" class="detailcontent">
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <input name="title" type="text" size="40" 
				 validator="string" cnname="名称" value="<pg:cell colName="title"  defaultValue=""/>"></td>	
	</tr>	
	<tr  >		 
	<td align="center" class="detailtitle">是否使用</td>
    <td align="left" class="detailcontent">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   
    <select name="roleUsage"  class="select">			  
				<option value="0" <pg:equal colName="roleUsage" value="0">selected</pg:equal>>否</option>	
				<option value="1" <pg:equal colName="roleUsage" value="1">selected</pg:equal>>是</option>		
				
	</select>	
    </td>	    
  </tr>


	<tr  >		 
	<td align="center"class="detailtitle">路径</td>
    <td align="left" class="detailcontent">
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <input name="path" type="text" size="40"  
				  value="<pg:cell colName="path"  defaultValue=""/>"></td>	   
  </tr>
  </table>
<br>
<br>
<table width="80%"  cellpadding="0" cellspacing="1">
  <tr align="center" >     
          <td  class="detailcontent" colspan="2">
          	<!-- 
             <input name="Submit1" type="button" class="input" value="创建" onclick="createres()">  
            -->
             <%if(!resId.equals("0")){%>      	
              <input name="Submit3" type="button" class="input" value="修改" onclick="modifyres()">                  
              <input name="Submit2" type="button" class="input" value="删除" onclick="deleteres()">  
              <%}%>                  
           	    </td>
                     
  </tr>
</table>				
  </pg:beaninfo>
</form>
</center>
</div>
		<%@include file="../sysMsg.jsp" %>
</body>

</html>

