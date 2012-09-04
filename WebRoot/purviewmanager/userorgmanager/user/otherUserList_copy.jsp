
<%@page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ include file="../../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.platform.sysmgrcore.entity.User,
				com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager,
				com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl" %>

<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	//Integer uid = (Integer) session.getAttribute("currUserId");//当前用户id
	//String uid = (String)request.getAttribute("userId");
	PurviewManager purviewManager = new PurviewManagerImpl();
	String uid = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	String curOrgId = request.getParameter("curOrgId");
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(uid);
	String userName = user.getUserRealname();
	
	request.setAttribute("userId",uid);
%>
<html>
<head>    
 <title>属性容器</title>
<script language="JavaScript" src="../../../sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../../../sysmanager/include/pager.js" type="text/javascript"></script>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<SCRIPT language="javascript">

	var api = parent.parent.frameElement.api, W = api.opener;

	function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
		var cbs = true;
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(cbs){
			selectAll[0].checked=true;
		}else{
			selectAll[0].checked=false;
		}
	}		
	function dealRecord(dealType) {
		var userSelf = "";//复制用户自身资源
		var userRole = "";//复制用户角色资源
		var userJob = "";//复制用户岗位资源
		if(document.all("userSelf").checked){
			userSelf = "self";
		}
		if(document.all("userRole").checked){
			userRole = "role";
		}
		if(document.all("userJob") && document.all("userJob").checked){
			userJob = "job";
		}
	    
	    //选中被复制的用户id
	    var checks = "";
	    var arr = new Array();
	    arr = document.getElementsByName("ID");
	    for(var i = 0; i < arr.length; i++){
	    	if(arr[i].checked){
	    		if(checks == ""){
	    			checks = arr[i].value;
	    		}else{
	    			checks += "," + arr[i].value;
	    		}
	    	}
	    }
	    if (checks != ""){
	    	if (dealType==1){
	    		outMsg = '<pg:message code="sany.pdp.purviewmanager.rolemanager.role.purview.copy.to.role.confirm" arguments="<%=userName%>"/>';
	    		
	    		W.$.dialog.confirm(outMsg, function() {
	    			orgForm.action="selfPurviewCopyUser.jsp?userId=<%=uid%>&checks="+checks+"&userSelf="+userSelf+"&userRole="
    				+ userRole+"&flag=2&userJob="+userJob+"&curOrgId=<%=curOrgId%>";
					orgForm.submit();
 					return true;
	    		});
			} 
	    }else{
	    	W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.one"/>');
	    	return false;
	   }
  		return false;
	}
	
	//复制用户自身权限或用户角色权限
	function userCopy(){
		if(document.all("userSelf").checked || document.all("userRole").checked || (document.all("userJob") && document.all("userJob").checked)){
			var checks = document.getElementsByName("ID");
			for(var i = 0; i < checks.length; i++){
				checks[i].disabled = false;
			}
		}else{
			var checks = document.getElementsByName("ID");
			for(var i = 0; i < checks.length; i++){
				checks[i].disabled = true;
				checks[i].checked = false;
			}
		}
	}
</SCRIPT>
<body class="contentbodymargin" scroll="no">
	<fieldset>
	   <LEGEND align=left><strong><FONT size=2><pg:message code="sany.pdp.common.alert"/></FONT></strong></LEGEND>
		
	    <table><tr><td><pg:message code="sany.pdp.userorgmanager.user.role.copy.current.user"/>
	      </td>
	     </tr>
	    </table>
	  
     </fieldset>
<div align="center">
	
    <form name = "orgForm" target="copy" method="post" action="">	
    <input type="hidden" name="jobId" value ="<%=request.getParameter("jobId")%>">
	<div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">
	<tr><td height='40' class="detailtitle" align=center colspan=3><b><pg:message code="sany.pdp.userorgmanager.user.role.copy.to.user" arguments="<%=userName%>"/></b></td></tr>
		<pg:listdata dataInfo="UserResCopyList" keyName="UserResCopyList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="12"
				  scope="request"  
				  data="UserResCopyList" 
				  isList="false">
				  <tr class="labeltable_middle_td">
				  <td colspan="3">
				  <input name="userSelf" type="checkbox" onclick="userCopy();" />&nbsp;&nbsp;<pg:message code="sany.pdp.role.copy.self"/>
				  &nbsp;&nbsp;&nbsp;
				  <input name="userRole" type="checkbox" onclick="userCopy();" />&nbsp;&nbsp;<pg:message code="sany.pdp.role.copy.role"/>
				  <% 
				  	if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction",false)){	
				  %>
				  &nbsp;&nbsp;&nbsp;
				  <input name="userJob" type="checkbox" onclick="userCopy();"
				  <%if(!orgId.equals(curOrgId)){out.print(" disabled ");}%> 
				  	 />
				  &nbsp;&nbsp;<pg:message code="sany.pdp.role.copy.job"/>
				  <% 
				  	}
				  %>
				  </td>
				  </tr>
				  <pg:equal actual="${UserResCopyList.itemCount}" value="0" >
				 	<tr>
				 		<td colspan="4">
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						</td>
						</tr>
				</pg:equal> 
				<pg:notequal actual="${UserResCopyList.itemCount}"  value="0">
			       <pg:header>
			      <!--设置分页表头-->
			      <th class="headercolor">
				  	<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
				  </th>
				  <th height='20' class="headercolor"><pg:message code="sany.pdp.userorgmanager.user.account"/></th>
			   	  <th height='20' class="headercolor"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.realname"/></th>
				
				  			
			      </pg:header>
			      <pg:param name="orgId"/>
			      <pg:param name="userId"/>
			     		      
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      <%
			      		String listUserId = dataSet.getString("userId");
			      		boolean state = purviewManager.isAdminOrOrgmanager(orgId,listUserId);
			      %>
			      		<tr>	      				
							
			      		    <td  align=center><input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID<%if(state){%>disabled<%}%>" value="<pg:cell colName="userId" defaultValue=""/>" disabled="true"></td>					
							<td  align=left>
							<pg:cell colName="userName" defaultValue=""/>
							</td>
							<td  align=left>
							<pg:cell colName="userRealname" defaultValue=""/>
							</td>
						</tr>			      		
			      </pg:list>
		
	</table>	
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
		<div align=center>
		<a href="javascript:void(0)" class="bt_1" id="addButton"
					onclick="dealRecord(1);"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a>
		</div>
		</pg:notequal>
		</pg:pager>
	</div>
</form>	
</div>
<div style="display:none">
	<IFRAME name="copy" width="0" height="0"></IFRAME>
</div> 
</body>

</html>
