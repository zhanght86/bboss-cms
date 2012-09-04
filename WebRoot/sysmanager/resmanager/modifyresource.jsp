<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.util.StringUtil"%>
<html>
<head>     
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script language="JavaScript">
 function trim(string){
  var temp="";
  string = ''+string;
  splitstring = string.split(" ");
  for(i=0;i<splitstring.length;i++){
    temp += splitstring[i];
  } 
  return temp;
 }
function modifyres(){
	if(validateForm(form2)){
		var title= document.forms[0].title.value;
		if (trim(title).length == 0 ){
	    alert("请录入资源名称！"); 
	    return false;
	    } 
		document.forms[0].action="<%=rootpath%>/resmanager/resource.do?method=modifyresource";
		document.forms[0].submit();
	}
}
function back(){
	var resId=document.forms[0].resId.value;
	window.location.href="<%=rootpath%>/resmanager/resource.do?method=getResInfo&resId="+resId;
}
</script>
</head>

<body class="contentbodymargin" >
<div id="contentborder" >
<br><br>
<center>

<form name="form2" action="" method="post"  >	
<pg:beaninfo requestKey="modifyresinfo" needClear="false">
	
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
</pg:beaninfo>	 
 <pg:beaninfo requestKey="restype">
    <td align="center" class="detailtitle">资源类型</td>
    <td align="left" class="detailcontent">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <select name="restypeId"  class="select">			
    <pg:list requestKey="restypelist">
    
				<option value="<pg:cell colName="restypeId"/>" 
				<pg:equal index="0" colName="restypeId" value="<%=dataSet.getString(Integer.parseInt(rowid),"restypeId")%>">selected</pg:equal>>
					<pg:cell colName="restypeName"/>
				</option>
	</pg:list>	
	</select>	
	</td>	
 </pg:beaninfo>	
 <pg:beaninfo requestKey="modifyresinfo" >      				 
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
<table width="80%">
  <tr align="center" >     
          <td  class="detailcontent" colspan="2">                            	
              <input name="Submit3" type="button" class="input" value="保存" onclick="modifyres()">                  
              <input name="Submit2" type="button" class="input" value="返回" onclick="back()">  
                         
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

