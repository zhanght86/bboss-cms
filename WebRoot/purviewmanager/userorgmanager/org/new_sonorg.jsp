
<%@page import="com.frameworkset.platform.config.ConfigManager"%><%
/**
 * <p>Title: 新增子机构</p>
 * <p>Description: 新增子机构</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory"%>
<%@page import="java.util.List"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	//判断是否是cms系统
	String subSystemId = control.getCurrentSystemID();
	
	
	//得到上个页面传来的机构名称 2008-3-20 baowen.liu
	String orgName = request.getParameter("orgName");
	
	String orgId = request.getParameter("orgId");
	String parentId = orgId;
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization parentOrg = orgManager.getOrgById(parentId);
	
	List sublist = orgManager.getChildOrgList(parentOrg);
	String suborgstr = "";
	if (sublist != null && sublist.size() > 0) {
		for (int i = 0; i < sublist.size(); i++) {
			Organization suborg = (Organization) sublist.get(i);
			suborgstr += suborg.getRemark5() + ",";
		}
	}
	
	if (!suborgstr.equals(""))
		suborgstr = suborgstr.substring(0, suborgstr.length() - 1);

	if ((parentId == null) || parentId.equals("")) {
		parentId = "0";
	}
	Organization org1 = orgManager.getOrgById(orgId);
	
	String layer = org1.getLayer();
	//如果父机构的layer为空，默认指定为2
	if ((layer == null) || layer.equals("")) {
		layer = "2";
	} else {
		layer = String.valueOf(Integer.parseInt(layer) + 1);
	}
	
	Organization org = new Organization();
	// 吴卫雄修改：为了同步机构到LDAP中去
	org.setParentId(parentId);
	org.setParentOrg(parentOrg);
	// 吴卫雄修改结束

	org.setLayer(layer);

	String isExist = "false";
	if ( request.getAttribute("isExist") != null){
		isExist = "true";
	}
	String parentOrgLevel = parentOrg.getOrg_level();
	String parentOrgXzqm = parentOrg.getOrg_xzqm();
	String parentOrgNumber = parentOrg.getOrgnumber();
	
	session.setAttribute("suborgstr", suborgstr);
	request.setAttribute("Organization", org);
	
	
	//父机构编号
	//String parentOrgNumber = parentOrg.getOrgnumber();
	//默认生成的机构编码
	String orgnumber = "";
	int len = GenerateServiceFactory.getOrgNumberGenerateService().getOrgNumberLen();
	boolean orgNumberHiberarchy = GenerateServiceFactory.getOrgNumberGenerateService().enableOrgNumberGenerate(); 
	if(orgNumberHiberarchy){
		orgnumber = GenerateServiceFactory.getOrgNumberGenerateService().generateOrgNumber(parentOrg);
	}else{
		orgnumber = parentOrgNumber;
	}
	//父机构编号长度
	int parentOrgNumberLength = parentOrgNumber.length();
	int maxOrgNumberLength = parentOrgNumberLength + len;
	
	//是否出现  "是否税管员" 复选框
	boolean istaxmanager = ConfigManager.getInstance().getConfigBooleanValue("istaxmanager", false);
%>
<html>
<head>

<title>机构【<%=orgName%>】新增子机构</title>
</head>
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="../../../include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../../html/js/commontool.js"></script>
<script type="text/javascript" src="../../../html/js/dialog/lhgdialog.js?self=false"></script>
<script language="JavaScript">
	var api = frameElement.api, W = api.opener;
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
		//document.all.divProcessing.style.display = "block";
		var orgnumber = form1.orgnumber.value;
		var orgName = form1.orgName.value;
		var orgSn = form1.orgSn.value;
		var orgdesc = form1.orgdesc.value;
		var jp = form1.jp.value;
		var qp = form1.qp.value;		
		var remark5 = form1.remark5.value;
		var layer = form1.layer.value;
		//orgnumber.search(/[^0-9A-Za-z]/g)
		if(orgName==""){
			W.$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.name.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			form1.orgName.focus();
			return false;
		}
		if(remark5==""){
			W.$.dialog.alert("<pg:message code='sany.pdp.check.organization.show.name.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			form1.remark5.focus();
			return false;
		}
		if(orgnumber==""&&orgnumber.search(/[^0-9A-Za-z]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.number.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.orgnumber.focus();
		return false;
		}
		if(orgSn.search(/[^0-9]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.sort.number.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.orgSn.focus();
		return false;
		}
		if(jp.search(/[^A-Za-z]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.check.organization.simple.spell.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.jp.focus();
		return false;
		}
		if(qp.search(/[^A-Za-z]/g) !=-1){
		W.$.dialog.alert("<pg:message code='sany.pdp.check.organization.complete.spell.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		form1.qp.focus();
		return false;
		}
		
		
		if(orgnumber.length>100)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.organiztion.number.long'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(orgName.length>40)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.organization.name.long'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(orgdesc.length>300)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.organization.description.long'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(jp.length>40)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.simple.spell.long'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		if(qp.length>40)
		{
			W.$.dialog.alert("<pg:message code='sany.pdp.complete.spell.long'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		
		
		var suborgstr = document.forms[0].suborgstr.value;
		var suborgs = suborgstr.split(",");
		if(validateForm(form1)){
		    var orgName = document.forms[0].remark5.value;
	
		    for(var i=0;i<suborgs.length;i++){
		    
		    	if(orgName==suborgs[i]){
		    		//alert("子机构已存在相同的机构显示名称,请重新命名");
		    		//return;
		    	}
		    }
			if(validateForm(form1)){	
			//document.all.saveButton.disabled = true;
			//document.all.resetButton.disabled = true;
			//document.all.backButton.disabled = true;
		  	document.forms[0].action = "new_sonorg_do.jsp";
			document.forms[0].target = "hiddenFrame";
			document.forms[0].submit();
		   }
	    }
	}
	
	function checkOrgNumber(obj){
		var val = obj.value;
		var cruLength = val.length;
		var maxLength = "<%=maxOrgNumberLength%>";
		var subOrg = val.substring(0,<%=parentOrgNumberLength%>);
		if(subOrg != "<%=parentOrgNumber%>"){
			alert("机构编码前几位必须与父机构编码相同\n父机构编码为：<%=parentOrgNumber%>");
			obj.value = "<%=orgnumber%>";
			obj.focus();
			return;
		}
		if(cruLength != maxLength){
			alert("机构编码长度只能是父机构编码长度加<%=len%>位！\n该机构编码长度只能是"+maxLength+"位数字");
			obj.value = val.substring(0,maxLength);
			obj.focus();
			return;
		}
		
	}
</script>
<body class="contentbodymargin">
<div style="height: 10px">&nbsp;</div>
<div id="">
<form name="form1" action="" method="post"  >	
<pg:beaninfo requestKey="Organization">
	      <input type="hidden"  name="suborgstr" value="<%=suborgstr%>" />
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>" />
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />
		 
		 <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" />		 
		 
		 <input type="hidden"  name="creator" value="<%=control.getUserID()%>" />
		 <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>">

		<input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" />
		<input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" />
		<input type="hidden"  name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden"  name="remark2" value="<pg:cell colName="remark2"  defaultValue=""/>" />
		<input type=hidden name=chargeOrgId value=1>
		<input type=hidden name=chargejobName value=1>
		<input type=hidden name=layer value=1>
		
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table4">
  <tr>    
    <th><pg:message code="sany.pdp.role.organization.number"/>：</th>
    <td class="detailcontent"><input name="orgnumber" type="text" 
				  cnname="机构编号" value="<%=orgnumber%>"></td>	
	<th><pg:message code="sany.pdp.role.organization.name"/>：</th>
    <td class="detailcontent"><input name="orgName" type="text" 
				  cnname="机构名称" value=""></td>	   
  </tr>
  <tr >
  <!--  
    <td align="center"  class="detailtitle">机构排序号*</td>
    <td class="detailcontent"><input name="orgSn" type="text" 
				  validator="int" cnname="机构排序号" value=""></td>
  -->	
  	<input name="orgSn" type="hidden" validator="int" cnname="机构排序号" value="1">
	<th><pg:message code="sany.pdp.role.organization.description"/>：</th>
    <td class="detailcontent"><input name="orgdesc" type="text" 
				  value="<pg:cell colName="orgdesc"  defaultValue=""/>"></td>	   
  </tr>
  <tr  >
    <th><pg:message code="sany.pdp.simple.spell"/>：</th>
    <td class="detailcontent"><input name="jp" type="text" 
				  value="<pg:cell colName="jp"  defaultValue=""/>"></td>	
	<th><pg:message code="sany.pdp.complete.spell"/>：</th>
    <td class="detailcontent"><input name="qp" type="text" 
				  value="<pg:cell colName="qp"  defaultValue=""/>"></td>	   
  </tr>
    <tr>
    <th><pg:message code="sany.pdp.role.organization.check"/>：</th>
	<td class="detailcontent">
		<select name="remark3">
			<option label="" value="1"><pg:message code="sany.pdp.valid"/>
			<option label="" value="0"><pg:message code="sany.pdp.invalid"/>
		</select>
	</td>	
	<td align="center" class="detailtitle"><pg:message code="sany.pdp.show.name"/>：</td>
    <td class="detailcontent"><input name="remark5" type="text" 
				 cnname="机构显示名称" value="<pg:cell colName="remark5"  defaultValue=""/>"></td>	  
	<input type=hidden name=ispartybussiness value=0>
	</tr>
	
</table>	
<div class="btnarea" >
          	<a class="bt_1" onclick="saveorg()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a> 
      <a class="bt_2" onclick="reset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
   	    </div>				
</pg:beaninfo>
</form>
</div>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:260px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
		<tr>
			<td bgcolor=#3A6EA5>
				<marquee align="middle" behavior="alternate" scrollamount="5">
					<font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</div>
</body>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
