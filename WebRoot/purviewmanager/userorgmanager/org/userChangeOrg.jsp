<%
/*
 * <p>Title: 用户调出操作界面</p>
 * <p>Description: 用户调出操作界面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-20
 * @author liangbing.tao
 * @version 1.0
 */
 %>
 <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager,
				 com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl,
				 java.util.List"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	UserManager userManager = SecurityDatabase.getUserManager();
	String CuuserId = control.getUserID();
	String ids = request.getParameter("checkBoxOne");
	String orgId = request.getParameter("orgId");
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org = orgManager.getOrgById(orgId);
	String orgName = org.getOrgName();
	String[] userid = ids.split(",");
	String script = "";
	String script2 = "";
	String userReturnInfo = "";
	for(int i = 0; i < userid.length; i++){
		List list = PurviewManagerImpl.getBussinessCheck().userMoveCheck(control,userid[i]);
			
	    String userOrgInfoMsg = PurviewManagerImpl.buildMessage(list);
		if(!"".equals(userOrgInfoMsg)){
		    //如果返回值！= "" ， 累加提示信息
		    User user = userManager.getUserById(userid[i]);
			if(userReturnInfo.equals("")){
				userReturnInfo = user.getUserRealname()+"("+user.getUserName()+"):"+userOrgInfoMsg;
			}else{
				userReturnInfo += "\\n" + user.getUserRealname()+"("+user.getUserName()+"):" + userOrgInfoMsg;
			}
		}
	}
	if(!"".equals(userReturnInfo)){
%>
	<script language="Javascript">
		var msg = '<pg:message code="sany.pdp.user.can.not.move.out"/>：';
	    msg += "\n<%=userReturnInfo%>";
		$.dialog.alert(msg, function() {
			closeDlg();
		});
	</script>
<% 
	}else{
%>
<html>
<head>
<title>从机构【<%=orgName%>】将用户调出到机构</title>
<script language="javascript">
var api = frameElement.api, W = api.opener;
function addorg(){
	var orgValues="";
	var state = false;
	var orgold = "<%=orgId%>";
	if(document.all.orgIdName){
		if(document.all.orgIdName.length){
		  	for(var i = 0; i < document.all.orgIdName.length; i++){
		  		if(document.all.orgIdName[i].checked){
		  			var orgValue = document.all.orgIdName[i].value;
		  			var orgValueId = orgValue.split(";");
		  			if(orgold==orgValueId[0]){
		  				W.$.dialog.alert("<pg:message code='sany.pdp.user.has.exist'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		  				return;
		  			}
		  			if(state){
		  				orgValues += "," + orgValueId[0];
		  			}else{
		  				orgValues = "" + orgValueId[0];
		  				state = true;
		  			}
		  		}
		  	}
	  	}else{
	  		if(document.all.orgIdName.checked){
	  			orgValues = document.all.orgIdName.value.substring(0,document.all.orgIdName.value.lastIndexOf(";"));
	  		}
	  	}
  	}
  	<%if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){%>
	  	if(orgValues!=""){
	  		document.OrgJobForm.target="getOrg";
		  	document.OrgJobForm.action="../user/foldDisperse.jsp?userIds=<%=ids%>&orgIds="+orgValues+"&orgId=<%=orgId%>&flag=0";
		  	document.OrgJobForm.submit();
	  	}else{
	  		$.dialog.alert("<pg:message code='sany.pdp.choose.move.in.organization'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
	  	}
  	<%}else{%>
	  	if(orgValues!=""){
	  		if(confirm("<pg:message code='sany.pdp.is.reserve.user'/>")){
		  		document.OrgJobForm.target="getOrg";
			  	document.OrgJobForm.action="../user/foldDisperse.jsp?userIds=<%=ids%>&orgIds="+orgValues+"&orgId=<%=orgId%>&flag=1";
			  	document.OrgJobForm.submit();
		  	}else{
		  		document.OrgJobForm.target="getOrg";
			  	document.OrgJobForm.action="../user/foldDisperse.jsp?userIds=<%=ids%>&orgIds="+orgValues+"&orgId=<%=orgId%>&flag=0";
			  	document.OrgJobForm.submit();		  	
		  	}
	  	}else{
	  		W.$.dialog.alert("<pg:message code='sany.pdp.choose.move.in.organization'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
	  	}
  	<%}%>
}

</script>
</head> 
<body class="contentbodymargin" onload="checkFold()">
<div id="">
<center>
<form id="OrgJobForm" name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<input type="hidden" name="userIds" value="<%=ids%>" />
  <tr class="tabletop">
    <td width="40%" align="center"></td>    
  </tr>
  <tr >
     <td >
     
    <tree:tree tree="user_org_tree111"
    	           node="user_org_tree111.node"
    	           imageFolder="../../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
				   href=""
    			   target=""
    			   mode="static-dynamic"
    			   >   
    			   <tree:param name="checkBoxOne"/>
    			   <tree:param name="orgId"/>
                   <%if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){%>
                   <tree:radio name="orgIdName" />   
                   <%}else{%>  
				   <tree:checkbox name="orgIdName" />
				   <%}%>
    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.DisperseOrgJob"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   rootNameCode="sany.pdp.organization.tree.name"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />

    	</tree:tree>
	</td>			 
				  
  </tr>
  <tr>
  <td align="center" >
  <a class="bt_1" onclick="addorg()"><span><pg:message code="sany.pdp.common.confirm"/></span></a>
  <a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
  </td>
  </tr>
<iframe name="getOrg" width="0" height="0" ></iframe>  
</table>
</form>
</center>
</div>
<div style="display:none">
<IFRAME name="delDisperse" height="0" width="0"></IFRAME>
</div>
</body>
<script>
function checkFold()
{
<%if(!script.equals(""))
  {
%>
	<%=script%>
<%}
 else if(!script2.equals(""))
 {
%>
	<%=script2%>
<%
 }
%>
}
</script>
</html>
<%
	}
%>

