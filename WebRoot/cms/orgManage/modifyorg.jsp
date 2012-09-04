<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager,com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
		String isExist = "false";
		if ( request.getAttribute("isExist") != null){
			isExist = "true";
		}
		
		String isExistOrgName = "false";
		if ( request.getAttribute("isExistOrgName") != null){
			isExistOrgName = "true";
		}
		
		String isExistRemark5 = "false";
		if ( request.getAttribute("isExistRemark5") != null){
			isExistRemark5 = "true";
		}
		
		
		String orgId = StringUtil.replaceNull(request.getParameter("orgId"),"");
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgId);
		String orgNumber = org.getOrgnumber();
		String remark1 = org.getChargeOrgId();
		String remark2 = org.getSatrapJobId();
		String remark3=org.getSatrapJobId();
		String orgname;
		String jobname ;//
		if(remark1==null || "null".equals(remark1) || "".equals(remark1)){
			orgname = "不详";
		}else{
			Organization org1 = orgManager.getOrgById(remark1);
			if(org1==null){
				orgname = "不详";
			}else{
				orgname = org1.getOrgName();	
			}
		}
	
		if(remark2==null || "null".equals(remark2) || "".equals(remark2)){
			jobname = "不详";
		}else{
			JobManager jobManager = SecurityDatabase.getJobManager();
			Job job = jobManager.getJobById(remark2);
			if(job==null){
				jobname = "不详";
			}else{
				jobname = job.getJobName();
			}
		}
		
		String ispartybussiness = org.getIspartybussiness();
%>
<html>
<head>     
<title>修改机构属性</title>
<script language="javaScript" src="<%=request.getContextPath()%>/sysmanager/scripts/validateForm.js"></script> 
<script language="JavaScript">
//焦点放在第一个text
function init(){
		form2.orgnumber.focus();
	}
	
function back(){
		document.forms[0].action="<%=rootpath%>/orgmanager/org.do?method=getOrgInfo&orgId=<%=orgId%>";
		document.forms[0].submit();
	}

var isExist = "<%=isExist%>";
	if ( isExist == "true" )
	{
		alert("机构编号已存在，请重新输入!");
	}
	
var isExistOrgName = "<%=isExistOrgName%>";
	if ( isExistOrgName == "true" )
	{
		alert("机构名称已存在，请重新输入!");
	}	

var isExistRemark5 = "<%=isExistRemark5%>";
	if ( isExistRemark5 == "true" )
	{
		alert("同级机构显示名称不能相同，请重新输入!");
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
function modifyorg(){
	var orgnumber=document.forms[0].orgnumber.value;
	var orgName=document.forms[0].orgName.value;
	var orgSn=document.forms[0].orgSn.value;
	var remark5=document.forms[0].remark5.value;
	var jp=document.forms[0].jp.value;
	var qp=document.forms[0].qp.value;
	
	if(orgnumber.length<1)
		{
		  alert("机构编号不能为空!");
		  form2.orgnumber.focus();
		  return false;
		}else
		{
		  var patternstr=/^(\d*|\w*)$/;
		  if(!patternstr.test(orgnumber))
		  {
		    alert("机构编号只能由数字和字母组成!");
		    form2.orgnumber.focus();
		    return false;
		  }
		}
		if (trim(orgName).length == 0 ){
	    alert("机构名称不能为空！"); 
		return false;
		} 
		if (trim(orgSn).length == 0 ){
	    alert("机构排序号不能为空！"); 
		return false;
		}
		if(orgSn.search(/[^0-9]/g) !=-1){
		alert("机构排序号只能是数字!");
		form2.orgSn.focus();
		return false;
		}
		if (trim(remark5).length == 0 ){
	    alert("机构显示名称不能为空！"); 
		return false;
		}
		if(jp.search(/[^A-Za-z]/g) !=-1){
		alert("简拼只能包含字母!");
		form2.jp.focus();
		return false;
		}
		if(qp.search(/[^A-Za-z]/g) !=-1){
		alert("全拼只能包含字母!");
		form2.qp.focus();
		return false;
		}    
	var orgId= document.forms[0].orgId.value;	
	
	document.forms[0].action="<%=rootpath%>/orgmanager/org.do?method=updateOrg&iscms=iscms";
	document.forms[0].submit();
	}
	
	function addorg(){
		window.open('<%=request.getContextPath()%>/sysmanager/orgmanager/chargeOrg.jsp?displayNameInput=chargeName&displayValueInput=chargeOrgId&displayNameInput1=chargejobName&displayValueInput1=satrapJobId','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')

	}	
</script>
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">

</head>

<body class="contentbodymargin" onload="init()">
<div id="contentborder" >

<form name="form2" target="updateorginfo" action="" method="post"  >	
<pg:beaninfo requestKey="updateOrgInfo">
<%
String remark4="";
if(beaninfo.getString("remark3")!=null)
{
  remark4=beaninfo.getString("remark3");
}
String remarkvalue="可发布";
if(remark4!=null && !remark4.equals(""))
{
    if(remark4.equals("1"))
    {
      remarkvalue="不能发布";
    }
}
%>
	     <input type="hidden"  name="iscms" value="iscms"/><!--专用于CMS中的新建机构--> 
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/> 
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />
		   <input type="hidden"  name="orgNumber" value="<%=orgNumber%>" /> 
		  <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" /> 
		  <input type="hidden"  name="creator" value="<pg:cell colName="creator"  defaultValue=""/>" /> 
		  <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>"> 
		   <input type="hidden"  name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" /> 
		   <input type="hidden"  name="remark2" value="<pg:cell colName="remark2"  defaultValue=""/>" /> 
		  <input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" /> 
		  <input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" /> 
		  <input type="hidden"  name="ispartybussiness" value="<pg:cell colName="ispartybussiness"  defaultValue=""/>" /> 
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
  <tr >    
    <td  class="detailtitle">机构编号</td>
    <td class="detailcontent"><input name="orgnumber" type="text" 
				 validator="string" cnname="机构编号" value="<pg:cell colName="orgnumber"  defaultValue=""/>"></td>	
	<td  class="detailtitle">机构全称</td>
    <td class="detailcontent"><input name="orgName" type="text" 
				 validator="string" cnname="机构名称" value="<pg:cell colName="orgName"  defaultValue=""/>"></td></tr>
  <tr>
    <td  class="detailtitle">机构排序号</td>
    <td class="detailcontent"><input name="orgSn" type="text" 
				  value="<pg:cell colName="orgSn"  defaultValue=""/>"></td>	
	<td  class="detailtitle">机构描述</td>
    <td class="detailcontent"><input name="orgdesc" type="text" 
				  value="<pg:cell colName="orgdesc"  defaultValue=""/>"></td></tr>
  <tr>
    <td  class="detailtitle">简拼</td>
    <td class="detailcontent"><input name="jp" type="text" 
				  value="<pg:cell colName="jp"  defaultValue=""/>"></td>	
	<td  class="detailtitle">全拼</td>
    <td class="detailcontent"><input name="qp" type="text" 
				  value="<pg:cell colName="qp"  defaultValue=""/>"></td>
 </tr>
	<tr>
    <td  class="detailtitle">机构有效标志</td>
	<td class="detailcontent">
								<P align="left">
									<SELECT name="remark3">
										<OPTION label="" value="1" <pg:equal colName="remark3" value="1">selected</pg:equal>>
											有效
										</OPTION>
										<OPTION label="" value="0" <pg:equal colName="remark3" value="0">selected</pg:equal>>
											无效
										</OPTION>
									</SELECT>
								</P>
							</td>		
	<td  class="detailtitle">机构显示名称</td>
    <td class="detailcontent"><input name="remark5" type="text" 
				 validator="string" cnname="机构显示名称" value="<pg:cell colName="remark5"  defaultValue=""/>"></td>
   </tr>
   <!-- <tr>
    <td  class="detailtitle">主管处室</td>
    <td class="detailcontent">-->
<!--    		<input name="remark1" type="text" value="<pg:cell colName="remark1"  defaultValue=""/>">-->
		<!-- 	<input name="chargeOrgId" type="hidden" readonly="true" onClick="addorg()" value="<pg:cell colName="chargeOrgId"  defaultValue=""/>">
			<input name="chargeName" type="text" readonly="true" width="30%" onClick="addorg()" value="<%=orgname%>" >
			
    </td>	
	<td  class="detailtitle">主管岗位</td>
    <td class="detailcontent">-->
<!--    	<input name="remark2" type="text" value="<pg:cell colName="remark2"  defaultValue=""/>">-->
		<!-- 	<input name="satrapJobId" type="hidden" readonly="true" onClick="addorg()" value="<pg:cell colName="satrapJobId"  defaultValue=""/>">
			<input name="chargejobName" type="text" readonly="true" width="30%"  onClick="addorg()" value="<%=jobname%>" >
			
	</td>
  </tr>
	
	<tr>     
 	<td  class="detailtitle">级次</td>
    <td class="detailcontent"><input name="layer" type="text" readonly="true"
				  value="<pg:cell colName="layer"  defaultValue=""/>"></td>	
	<td  class="detailtitle">业务机构</td>
    <td class="detailcontent">
	<dict:select type="ispartybussiness" name="ispartybussiness" defaultValue="<%=ispartybussiness%>"/>
	</td>
	</tr>     -->
	
	 <tr>
	 <input name="layer" type="hidden" readonly="true"
				  value="<pg:cell colName="layer"  defaultValue=""/>">
	<td  class="detailtitle">机构级别</td>
    <td align="left" class="detailcontent" >
								<P align="left">
									<SELECT name="org_level">
										<OPTION label="" value="1" <pg:equal colName="org_level" value="1">selected</pg:equal>="" selected="true">
											副处级
										</OPTION>
										<OPTION label="" value="2" <pg:equal colName="org_level" value="2">selected</pg:equal>="">
											正科级
										</OPTION>
										<OPTION label="" value="3" <pg:equal colName="org_level" value="3">selected</pg:equal>="">
											副科级
										</OPTION>
										<OPTION label="" value="4" <pg:equal colName="org_level" value="4">selected</pg:equal>="">
											正股级
										</OPTION>
										<OPTION label="" value="5" <pg:equal colName="org_level" value="5">selected</pg:equal>="">
											副股级
										</OPTION>
										<OPTION label="" value="6" <pg:equal colName="org_level" value="6">selected</pg:equal>="">
											未定级
										</OPTION>
										<OPTION label="" value="7" <pg:equal colName="org_level" value="7">selected</pg:equal>="">
											其它
										</OPTION>
									</SELECT>
								</P>
							</td> 
	<td  class="detailtitle">行政区码</td>
    <td class="detailcontent">
    	<input name="org_xzqm" validator="stringNull" cnname="行政区码" type="text" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    </td>
   </tr> 
   
   <tr>     
 		<td  class="detailtitle">机构属性</td>
    	<td   align="left" class="detailcontent">
								<P align="left">
									<SELECT name="isjichaparty">
										<OPTION label="" value="0" <pg:equal colName="isjichaparty" value="0">selected</pg:equal>="" selected="true">
											行政机关
										</OPTION>
										<OPTION label="" value="1" <pg:equal colName="isjichaparty" value="1">selected</pg:equal>="">
											企事业单位
										</OPTION>
									</SELECT>
								</P>
							</td>	
		<td class="detailtitle" >是否公开信息</td>
		<td align="left" class="detailcontent" >
								<P align="left">
									<SELECT name="isforeignparty">
										<OPTION label="" value="0" <pg:equal colName="isforeignparty" value="0">selected</pg:equal>="" selected="true">
											是
										</OPTION>
										<OPTION label="" value="1" <pg:equal colName="isforeignparty" value="1">selected</pg:equal>="">
											否
										</OPTION>
									</SELECT>
								</P>
							</td>
	</tr>  
  
  <tr>
     <td  colspan="4">               	
      <input name="Submit1" type="button" class="cms_button" value="保存" onclick="modifyorg()"> 
      <input name="Submit2" type="button" class="cms_button" value="返回" onclick="window.close();">
      </td></tr></table></pg:beaninfo>
</form>
</div>
<iframe name="updateorginfo" width="0" height="0"></iframe>

</body>
</html>