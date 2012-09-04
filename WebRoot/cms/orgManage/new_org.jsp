<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
    String flag=request.getParameter("flag");//建一级机构的标志
    flag= flag == null ? "" : flag ;
    //System.out.println("flag=" + flag);
	String suborgstr = (String) session.getAttribute("suborgstr");
	String suborgremark5 = (String) session.getAttribute("suborgremark5");
	if("1".equals(flag)){
		suborgstr="";
		suborgremark5 ="";
	}
	//System.out.println("suborgremark5="+suborgremark5);
	String isExist = "false";
	if ( request.getAttribute("isExist") != null){
		isExist = "true";
	}
	//新建一级机构时，判断机构名称是否存在
	//2008-4-23 baowen.liu
	String isExistOrgName="false";
	if( request.getAttribute("isExistOrgName") != null){
	  isExistOrgName="true";
	}
	//新建一级机构时，判断机构显示名称是否存在
	//2008-4-23 baowen.liu
	String isExistRemark5="false";
	if( request.getAttribute("isExistRemark5") != null ){
	  isExistRemark5="true";
	}
	
	//建子机构时得到父机构的机构编号
	//2008-5-4 baowen.liu
	String orgNumber =(String)request.getAttribute("orgNumber");
	orgNumber= orgNumber == null ? "" : orgNumber;
%>
<html>
<head>

<title>新建机构</title>
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
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
		alert("机构编号已存在，请重新输入");
		
	}
	//新建机构时，判断机构名称是否存在
	var isExistOrgName = "<%=isExistOrgName%>";
	if ( isExistOrgName == "true" ){
		alert("机构名称已存在，请重新输入!");
		
	}
	
	//新建一级机构时，判断机构显示名称是否存在
	//2008-4-23 baowen.liu
	var isExistRemark5 = "<%=isExistRemark5%>";
	if ( isExistRemark5 == "true" ){
		alert("一级机构显示名称已存在!!!，请重新输入！");
		
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
		var jp = form1.jp.value;
		var qp = form1.qp.value;
		var orgSn = form1.orgSn.value;
		var orgnumber = form1.orgnumber.value;
		
		//if(orgnumber.search(/[^0-9]/g) !=-1){
		//alert("机构编号只能是数字!");
		//form1.orgnumber.focus();
		//return false;
		//}
		if(orgnumber.length<1)
		{
		  alert("机构编号不能为空!");
		  form1.orgnumber.focus();
		  return false;
		}else
		{
		  patternstr=/^(\d*|\w*)$/;
		  if(!patternstr.test(orgnumber))
		  {
		    alert("机构编号只能由数字和字母组成!");
		    form1.orgnumber.focus();
		    return false;
		  }
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
		
		if(form1.remark3.value==""){
		 alert("请选择机构类型！");
		 return false;
		
		}
		var suborgstr = document.forms[0].suborgstr.value;
		var suborgs = suborgstr.split(",");
		
		var suborgremark5 = document.forms[0].suborgremark5.value;
		var suborgremarks = suborgremark5.split(",");
		if(validateForm(form1)){
			
		    var orgName = document.forms[0].orgName.value;
		  
		    for(var i=0;i<suborgs.length;i++){
		    
		    	if(orgName==suborgs[i]){
		    		alert("子机构已存在相同的名字,请重新命名!");
		    		return;
		    	}
		    }
		    
		    //新建子机构时，判断机构显示名称是否存在
	        //2008-4-23 baowen.liu
		    var remark5=document.forms[0].remark5.value;
		    
		     for(var j=0;j<suborgremarks.length;j++){
		    
		    	if(remark5==suborgremarks[j]){
		    		alert("子机构已存在相同的机构显示名称,请重新命名!");
		    		return;
		    	}
		    }
		    
		  	document.forms[0].action="<%=request.getContextPath()%>/orgmanager/org.do?method=saveOrg";
			document.forms[0].submit();
	    }
	}
	function addorg(){
		window.open('<%=request.getContextPath()%>/sysmanager/orgmanager/chargeOrg.jsp?displayNameInput=chargeName&displayValueInput=chargeOrgId&displayNameInput1=chargejobName&displayValueInput1=satrapJobId','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')

	}
</script>
<body scroll="no" bgcolor="#F7F8FC">
<div id="contentborder" align="center">
<br>
<form name="form1" target="addorginfo" action="" method="post"  >	
<pg:beaninfo requestKey="Organization">
		 <input type="hidden"  name="iscms" value="iscms"/><!--专用于CMS中的新建机构-->
	     <input type="hidden"  name="suborgstr" value="<%=suborgstr%>" />
	     <input type="hidden"  name="suborgremark5" value="<%=suborgremark5%>" />
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>" />
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />		
		 <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" />		 
		 
		 <input type="hidden"  name="creator" value="<%=control.getUserID()%>" />
		 <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>">

		<input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" />
		<input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" />
		<input type="hidden"  name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden"  name="remark2" value="<pg:cell colName="remark2"  defaultValue=""/>" />
		<input type="hidden"  name="ispartybussiness" value="<pg:cell colName="ispartybussiness"  defaultValue=""/>" />	
		<% if("1".equals(flag)){%> <input type="hidden"  name="suborg" value="sub" >	<%}%>	
 <table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="">
  <tr>    
    <td align="left">机构编号</td>
    <td ><input name="orgnumber" type="text" 
				 validator="string" cnname="机构编号" value='<pg:cell colName="orgnumber"  defaultValue="<%=orgNumber%>"/>'><span class="red_star">&nbsp;*</span></td>	
	<td align="left">机构全称</td>
    <td ><input name="orgName" type="text" 
				 validator="string" cnname="机构名称" value='<pg:cell colName="orgName"  defaultValue=""/>'><span class="red_star">&nbsp;*</span></td>	   
  </tr>
  <tr >
    <td align="left">机构排序号</td>
    <td >
	<input name="orgSn" type="text" validator="int" cnname="机构排序号" value='<pg:cell colName="orgSn"  defaultValue=""/>'><span class="red_star">&nbsp;*</span>
	</td>	
	<td align="left" class="detailtitle">机构描述</td>
    <td ><input name="orgdesc" type="text" 
				  value='<pg:cell colName="orgdesc"  defaultValue=""/>'>
				  <span class="red_star">&nbsp;&nbsp;</span></td>	   
  </tr>
  <tr  >
    <td align="left">简拼</td>
    <td ><input name="jp" type="text" 
				  value='<pg:cell colName="jp"  defaultValue=""/>'>
				  <span class="red_star">&nbsp;&nbsp;</span></td>	
	<td align="left">全拼</td>
    <td ><input name="qp" type="text" 
				  value='<pg:cell colName="qp"  defaultValue=""/>'>
				  <span class="red_star">&nbsp;&nbsp;</span></td>	   
  </tr>
   <tr>
    <td align="center" class="detailtitle">机构有效标志</td>
	<td class="detailcontent">
		<select name="remark3">
			<option label="" value="1">有效
			<option label="" value="0">无效
		</select>
	</td>	
	<td align="left">机构显示名称</td>
    <td >
	<input name="remark5" type="text" 
				 validator="string" cnname="机构显示名称" value='<pg:cell colName="remark5"  defaultValue=""/>'>
				 <span class="red_star">*</span>
				 </td>	   
    </tr>
    <!-- 
    <tr>     
 	<td align="left">主管处室</td>
    <td >
    -->
			<input name="chargeOrgId" type="hidden" readonly="true" onClick="addorg()" value='<pg:cell colName="chargeOrgId"  defaultValue=""/>' >
			<input name="chargeName" type="hidden" readonly="true" width="30%"  onClick="addorg()" value='<pg:cell colName="chargeName"  defaultValue=""/>' >
	<!-- 
			<span class="red_star">&nbsp;&nbsp;</span>
	</td>	
	<td align="left">主管岗位</td>
    <td>
    -->
			<input name="satrapJobId" type="hidden"  readonly="true" onClick="addorg()" value='<pg:cell colName="satrapJobId"  defaultValue=""/>' >
			<input name="chargejobName" type="hidden" readonly="true" width="30%" onClick="addorg()"  value='<pg:cell colName="chargejobName"  defaultValue=""/>' >
	<!-- 		<span class="red_star">&nbsp;&nbsp;</span>
	</td>	   
	</tr>
	--> 
	<!--<tr>     
 	<td align="left">级次</td>
    <td><input name="layer" type="text" readonly="true"
				  value='<pg:cell colName="layer"  defaultValue="1"/>'>
				  <span class="red_star">&nbsp;&nbsp;</span></td>	
		   
	  <td align="center" class="detailtitle">业务机构</td>
    <td class="detailcontent">
	<dict:select type="ispartybussiness" name="ispartybussiness" defaultValue="1"/>
	</td>
   
   </tr> --> 
    <tr>
    <input name="layer" type="hidden" readonly="true"
				  value='<pg:cell colName="layer"  defaultValue="1"/>'>
	<td  class="detailtitle">机构级别</td>
    <td class="detailcontent">
    	<select name="org_level">
	    		<option label="" value="1" >副处级</option>
	    		<option label="" value="2" selected>正科级</option>
	    		<option label="" value="3" >副科级</option>
	    		<option label="" value="4" >正股级</option>
	    		<option label="" value="5" >副股级</option>
	    		<option label="" value="6" >未定级</option>
	    		<option label="" value="7" >其它</option>
    	</select>
	</td> 
	<td  class="detailtitle">行政区码</td>
    <td class="detailcontent">
    	<input name="org_xzqm" validator="stringNull" cnname="行政区码" type="text" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    	<font color='red'>*</font>
    </td>
   </tr> 
   
   <tr>     
 		<td  class="detailtitle">机构属性</td>
    	<td class="detailcontent">
			<select name="isjichaparty">
				<option label="" value="0" <pg:equal colName="isjichaparty" value="0">selected</pg:equal> >行政机关
	    		<option label="" value="1" <pg:equal colName="isjichaparty" value="1">selected</pg:equal>>企事业单位</select>
		</td>	
		<td class="detailtitle" >是否公开信息</td>
		<td class="detailcontent">
			<select name="isforeignparty">
				<option label="" value="0" <pg:equal colName="isforeignparty" value="0">selected</pg:equal> >是
	    		<option label="" value="1" <pg:equal colName="isforeignparty" value="1">selected</pg:equal>>否
    		</select>
		</td>
	</tr>
   
  <tr></tr>
  <tr></tr>
  <tr></tr>
   <tr>     
	 <td align="left" colspan="4"><div align="center">
		 <input name="Submit" type="button" class="cms_button" value="保存" onclick="saveorg()">
		 <input name="close" type="button" class="cms_button" value="返回" onclick="window.close()">
	 </td>
                     
  </tr>
</table>					
</pg:beaninfo>
</form>
<iframe name="addorginfo" width="0" height="0"></iframe>
</div>
</body>

</html>