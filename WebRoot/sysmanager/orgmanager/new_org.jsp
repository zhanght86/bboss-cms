<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String suborgstr = (String) session.getAttribute("suborgstr");
	String isExist = "false";
	if ( request.getAttribute("isExist") != null){
		isExist = "true";
	}
%>
<html>
<head>

<title>系统管理</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
<style type="text/css">
<!--
.style1 {color: #CC0000}
-->
</style>
</head>

<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script language="JavaScript">
	var isExist = "<%=isExist%>";
	if ( isExist == "true" ){
		alert("机构编号已存在，请重新输入!");
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
	function saveorg()
	{		
		var orgnumber = form1.orgnumber.value;
		var orgName = form1.orgName.value;
		var orgSn = form1.orgSn.value;
		var orgdesc = form1.orgdesc.value;
		var jp = form1.jp.value;
		var qp = form1.qp.value;		
		var remark5 = form1.remark5.value;
		var layer = form1.layer.value;
		var org_xzqm = form1.org_xzqm.value;
		
		//if(orgnumber.search(/[^0-9]/g) !=-1){
		//alert("机构编号只能是数字!");
		//form1.orgnumber.focus();
		//return false;
		//}
		//(orgnumber.search(/[^0-9A-Za-z]/g)
		if(orgnumber.search(/[^0-9]/g) !=-1){
		alert("机构编号只能是数字!");
		form2.orgnumber.focus();
		return false;
		}
		if(orgSn.search(/[^0-9]/g) !=-1){
		alert("机构排序号只能是数字!");
		form1.orgSn.focus();
		return false;
		}
		if(jp.search(/[^A-Za-z]/g) !=-1){
		alert("简拼只能包含字母!");
		form1.jp.focus();
		return false;
		}
		if(qp.search(/[^A-Za-z]/g) !=-1){
		alert("全拼只能包含字母!");
		form1.qp.focus();
		return false;
		}
		if(org_xzqm.search(/[^0-9]/g) !=-1){
		alert("行政区码只能是数字!");
		form1.org_xzqm.focus();
		return false;
		}
		
		if(orgnumber.length>100)
		{
			alert("机构编号过长,限制在100字符以内!");
			return;
		}
		if(orgName.length>40)
		{
			alert("机构名称过长,限制在40字符以内!");
			return;
		}
		if(orgdesc.length>300)
		{
			alert("机构描述过长,限制在300字符以内!");
			return;
		}
		if(jp.length>40)
		{
			alert("简拼过长,限制在40字符以内!");
			return;
		}
		if(qp.length>40)
		{
			alert("全拼过长,限制在40字符以内!");
			return;
		}
		if(remark5.length>100)
		{
			alert("主管岗位过长,限制在100字符以内!");
			return;
		}
		if(layer.length>200)
		{
			alert("级次过长,限制在200字符以内!");
			return;
		}
		if(org_xzqm.length>10)
		{
			alert("行政区码过长,限制在10字符以内!");
			return;
		}
		
		var suborgstr = document.forms[0].suborgstr.value;
		var suborgs = suborgstr.split(",");
		if(validateForm(form1)){
			
		    var orgName = document.forms[0].orgName.value;
		  
		    for(var i=0;i<suborgs.length;i++){
		    
		    	if(orgName==suborgs[i]){
		    		alert("子机构已存在相同的名字,请重新命名");
		    		return;
		    	}
		    }
		  	document.forms[0].action="<%=request.getContextPath()%>/orgmanager/org.do?method=saveOrg";
			document.forms[0].submit();
		
	    }
	}
	function addorg(){
		window.open('../orgmanager/chargeOrg.jsp?displayNameInput=chargeName&displayValueInput=chargeOrgId&displayNameInput1=chargejobName&displayValueInput1=satrapJobId','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')

	}
	function back(){
		document.forms[0].action="../orgmanager/organsearch.jsp";
		document.forms[0].submit();
	}
</script>
<body class="contentbodymargin" scroll="no">
<div id="contentborder" align="center">

<!-- <center>新建子机构</center> -->
<form name="form1" action="" method="post"  >	
<pg:beaninfo requestKey="Organization">
	      <input type="hidden"  name="suborgstr" value="<%=suborgstr%>" />
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>" />
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />		
		 <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" />		 
		 
		 <input type="hidden"  name="creator" value="<pg:cell colName="creator"  defaultValue=""/>" />
		 <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>">

		<input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" />
		<input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" />
		<input type="hidden"  name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden"  name="remark2" value="<pg:cell colName="remark2"  defaultValue=""/>" />
					
 <table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
  <tr>    
    <td align="center" class="detailtitle">机构编号*</td>
    <td class="detailcontent"><input name="orgnumber" type="text" 
				  cnname="机构编号" value="<pg:cell colName="orgnumber"  defaultValue=""/>"></td>	
	<td align="center" class="detailtitle">机构名称*</td>
    <td class="detailcontent"><input name="orgName" type="text" 
				 validator="string" cnname="机构名称" value="<pg:cell colName="orgName"  defaultValue=""/>"></td>	   
  </tr>
  <tr >
  <!--  
    <td align="center"  class="detailtitle">机构排序号*</td>
    <td class="detailcontent"><input name="orgSn" type="text" 
				  validator="int" cnname="机构排序号" value="<pg:cell colName="orgSn"  defaultValue=""/>"></td>
  -->	
  <input name="orgSn" type="hidden" 
				  validator="int" cnname="机构排序号" value="1">
	<td align="center" class="detailtitle">机构描述</td>
    <td class="detailcontent"><input name="orgdesc" type="text" 
				  value="<pg:cell colName="orgdesc"  defaultValue=""/>"></td>	   
  </tr>
  <tr  >
    <td align="center" class="detailtitle">简拼</td>
    <td class="detailcontent"><input name="jp" type="text" 
				  value="<pg:cell colName="jp"  defaultValue=""/>"></td>	
	<td align="center" class="detailtitle">全拼</td>
    <td class="detailcontent"><input name="qp" type="text" 
				  value="<pg:cell colName="qp"  defaultValue=""/>"></td>	   
  </tr>
  <tr>
    <td align="center" class="detailtitle">机构有效标志</td>
	<td class="detailcontent">
		<select name="remark3">
			<option label="" value="1">有效
			<option label="" value="0">无效
		</select>
	</td>
	<td align="center" class="detailtitle">机构显示名称*</td> 
    <td class="detailcontent"><input name="remark5" type="text" 
				 validator="string" cnname="机构显示名称" value="<pg:cell colName="remark5"  defaultValue=""/>"></td>	
	<input type=hidden name=layer value=1>
	<input type=hidden name=ispartybussiness value=0>
  </tr>
  
  <tr>
    <td align="center" class="detailtitle">是否直属局</td>
    <td class="detailcontent">
    	<select name="isdirectlyparty">
    		<option label="" value="0">否
    		<option label="" value="1">是
    	</select>
	</td>	
	<td align="center" class="detailtitle">是否涉外局</td>
    <td class="detailcontent">
    	<select name="isforeignparty">
    		<option label="" value="0">否
    		<option label="" value="1">是
    	</select>
	</td>	   
  </tr>
  <tr>
    <td align="center" class="detailtitle">是否稽查局</td>
    <td class="detailcontent">
    	<select name="isjichaparty">
    		<option label="" value="0">否
    		<option label="" value="1">是
    	</select>
	</td>	
	<td align="center" class="detailtitle">是否直接管户单位</td>
    <td class="detailcontent">
    	<select name="isdirectguanhu">
    		<option label="" value="0">否
    		<option label="" value="1">是
    	</select>
	</td>	   
  </tr>
  
  <tr>
	<!-- <td align="center" class="detailtitle">业务机构</td>
    <td class="detailcontent">
	<dict:select type="ispartybussiness" name="ispartybussiness" defaultValue="0"/>
	</td> -->
    <!-- <tr>     
 	<td align="center" class="detailtitle">主管处室</td>
    <td class="detailcontent">
			<input name="chargeOrgId" type="hidden" readonly="true" onClick="addorg()" value="<pg:cell colName="chargeOrgId"  defaultValue=""/>" >
			<input name="chargeName" type="text" readonly="true" width="30%"  onClick="addorg()" value="<pg:cell colName="chargeName"  defaultValue=""/>" >
			
	</td>	
	<td align="center" class="detailtitle">主管岗位</td>
    <td class="detailcontent">
			<input name="satrapJobId" type="hidden"  readonly="true" onClick="addorg()" value="<pg:cell colName="satrapJobId"  defaultValue=""/>" >
			<input name="chargejobName" type="text" readonly="true" width="30%" onClick="addorg()"  value="<pg:cell colName="chargejobName"  defaultValue=""/>" >
	</td>	   
	</tr> -->
	<input type=hidden name=chargeOrgId value=1>
	<input type=hidden name=chargejobName value=1>
 	<!-- <td align="center" class="detailtitle">级次</td>
    <td class="detailcontent"><input name="layer" type="text" readonly="true"
				  value="1"></td>	-->
     
	<td align="center" class="detailtitle">机构级别</td>
    <td class="detailcontent">
    	<select name="org_level">
    		<option label="" value="1">省级
    		<option label="" value="2">市州级
    		<option label="" value="3">县区级
    		<option label="" value="4">科所级
    		<option label="" value="0">内设科室
    	</select>
	</td> 
	<td align="center" class="detailtitle">行政区码*</td>
    <td class="detailcontent">
    	<input name="org_xzqm"  cnname="行政区码" type="text" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    </td>
   </tr>      
   <tr>     
          <td align="center" colspan="4"><div align="center">
             <input name="Submit" type="button" class="input" value="保存" onclick="saveorg()">        	
             <input name="Submit" type="button" class="input" value="返回" onclick="back()">                  
                     
           	    </td>
                     
  </tr>
</table>					
</pg:beaninfo>
</form>
</div>
</body>

</html>
