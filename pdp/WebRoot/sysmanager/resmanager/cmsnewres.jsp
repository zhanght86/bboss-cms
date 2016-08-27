<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*"%>
<%
String isJobExist = "false";
	if ( request.getAttribute("isJobExist") != null){
		isJobExist = "true";
	}
String restype=request.getParameter("restype");
String restypeName=request.getParameter("restypeName");

%>
<html>
<head>     
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
    <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
 <script language="javaScript" src="../validateForm.js"></script> 
<script language="JavaScript">
var isJobExist = "<%=isJobExist%>";
var isSuccess = '<%=request.getParameter("isSuccess")%>';
	if ( isJobExist == "true"){
		alert("已有相同的名称!");
	}
	
	if(isSuccess=="0"){
		alert("资源名称已经存在，请重新进行添加！");
	}

function back(){
var parentId = document.thisform.parentId.value;

window.location.href="<%=rootpath%>/resmanager/resource.do?method=getResInfo&resId="+parentId;
}
 function trim(string){
  var temp="";
  string = ''+string;
  splitstring = string.split(" ");
  for(i=0;i<splitstring.length;i++){
    temp += splitstring[i];
  } 
  return temp;
 }
  
function saveres(){
    	var title=document.forms[0].title.value;
				if (trim(title).length == 0 ){
			    alert("资源名称不能为空！"); 
			    return false;
			    }
		document.all("Submit1").disabled = true;
		document.all("Submit3").disabled = true;	 
		document.forms[0].target="saveRes";
		document.forms[0].action="saveCmsResOp.jsp?opType=new&restypeId=<%=restype%>";
		document.forms[0].submit();

}
</script>
</head>
<body class="contentbodymargin" scroll="no">

<div id="contentborder" align="center">
<br><br>
<center>

<form name="thisform" action="" method="post"  >	
<pg:beaninfo requestKey="newRes">
	
		 <input type="hidden"  name="resId" value="<pg:cell colName="resId"  defaultValue=""/>"/>						 			 		 
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue="-1"/>" />
		
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
		 <input type="hidden"  name="attr27" value="<pg:cell colName="attr27"  defaultValue="<%=restypeName%>"/>" />
		 <input type="hidden"  name="restypeName" value="<pg:cell colName="restypeName"  defaultValue="<%=restypeName%>"/>" />
		 <input type="hidden"  name="restypeId" value="<pg:cell colName="restypeId"  defaultValue="<%=restype%>"/>" />
		 <input type="hidden"  name="roleUsage" value="<pg:cell colName="roleUsage"  defaultValue="0"/>" />		
<div class="detailtitle"><%=restypeName %>在配置文件中配置的ID为：<%=restype %></div>		 			 
<table width="50%" border="0"   cellpadding="0" cellspacing="1" class="thin">
  <tr  >    
    <td align="center"  class="detailtitle">资源名称*</td>
    <td align="left" class="detailcontent">&nbsp;&nbsp;&nbsp;&nbsp;
    <input name="title" type="text" size="40"
				 validator="string" cnname="名称" value="<pg:cell colName="title"  defaultValue=""/>"></td>	
<!--	</tr>	-->
<!--	<tr  >		 -->
<!--	<td align="center" class="detailtitle">是否使用</td>-->
<!--    <td align="left" class="detailcontent">-->
<!--   &nbsp;&nbsp;&nbsp;&nbsp;-->
<!--    <select name="roleUsage"  class="select">			  -->
<!--				<option value="0">否</option>	-->
<!--				<option value="1">是</option>		-->
<!--				-->
<!--	</select>	-->
<!--    </td>	   -->
<!--  </tr>-->
	
	<tr  >		 
	<td align="center" class="detailtitle">资源描述</td>
    <td align="left" class="detailcontent">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input name="path" type="text" size="40"
				  value="<pg:cell colName="<%=restype%>"  defaultValue=""/>"></td>	   
  </tr>
  </table>
<br>
<br>
<table>
  <tr align="center" >     
          <td  class="detailcontent" colspan="2">
             <input name="Submit1" type="button" class="input" value="保存" onclick="saveres()">        	
             <input name="Submit3" type="button" class="input" value="返回" onclick="window.close();">                                       
          </td> 	   
                     
  </tr>
</table>				
</pg:beaninfo>
<iframe name="saveRes" width="0" height="0" ></iframe>
</form>
</center>
</div>
</body>
</html>

