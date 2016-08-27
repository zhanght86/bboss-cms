<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
/**
 * 
 * <p>Title: 资源反查询，查询条件页面</p>
 *
 * <p>Description: 资源反查询，查询条件页面</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: bboss</p>
 * @Date 2008-11-4
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.config.model.ResourceInfo"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>

<%
	AccessControl control = AccessControl.getInstance();
	if(!control.checkManagerAccess(request, response)){
		return;
	} 
	
	ResourceManager resManager = new ResourceManager();
	List resQueue = resManager.getResourceInfos();
	List resList = new ArrayList();
	String name = "";
	for(int i = 0; i < resQueue.size(); i ++){
		ResourceInfo res = (ResourceInfo)resQueue.get(i);
		name = res.getId(); 
		if(name.equals("report_column")
			|| name.equals("cs_column")
			|| name.equals("column")){
			resList.add(res);
		}
	}
	//设置资源类型列表
	request.setAttribute("resList",resQueue);
%>

<html>
<head>
	<title>资源操授予查询</title>
	
	<script type="text/javascript" language="Javascript">
		//根据资源类型的改变，来改变操作类型下拉选择项。注：不同的资源类型有不同的资源操作项，由配置文件config-manager.xml配置而成
		function getOperateType(){
			var restypeIdValue = document.all.restypeId.value;
			var restypeIdValues = restypeIdValue.split("$$");
			var restypeId = restypeIdValues[0]; 
			var auto = restypeIdValues[1];
			var maintaindata = restypeIdValues[2];
			 
			if(restypeId == "column" || restypeId == "report_column" || restypeId == "cs_column"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.menu.select.name"/>'+"：";
			}else if(restypeId == "orgunit"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.type.org.selected"/>'+"：";
			}else if(restypeId == "role"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.role.select.name"/>'+"：";
			}else if(restypeId == "job"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.post.select.name"/>'+"：";
			}else if(restypeId == "group"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.usergroup.select.name"/>'+"：";
			}else if(restypeId == "dict"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.dict.select.name"/>'+"：";
			}else if(restypeId == "app_column"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.siteapp.select.name"/>'+"：";
			}else if(maintaindata == "true" && auto == "false"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.custom.select.name"/>'+"：";
			}else if(restypeId.substring(0,7) == "channel"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.channel.select.name"/>'+"：";
			}else if(restypeId.substring(0,4) == "site"){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.site.select.name"/>'+"：";
			}else if(restypeId == null || "".equals(restypeId)){
				document.all.objsel.innerText = '<pg:message code="sany.pdp.sysmanager.resource.input"/>'+"res_id：";
			}
			getopergroup.location.href = "resChange.jsp?restypeId="+restypeId;
			document.all.resId.value = "";
			document.all.resName.value = "";
		}
		//选择不同的授予对象
		function objChange(){
			var typeval = document.all.type.value;
			document.all.selname.value = "";
			document.all.selid.value = "";
			if(typeval == "user" || typeval == "org" || typeval == "orgjob"){
				document.all.objName.innerText = '<pg:message code="sany.pdp.sysmanager.resource.type.org.selected"/>'+"：";
				//document.all.divProcessing.style.display = "block";
			}
			if(typeval == "role"){
				document.all.objName.innerText = '<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.select"/>'+"：";
				//document.all.divProcessing.style.display = "none";
			}
		}
		//选择菜单
		function selectMenu(){
			
			var restypeIdValue = document.all.restypeId.value;
			var restypeIdValues = restypeIdValue.split("$$");
			var restypeId = restypeIdValues[0];
			
			//channel
			var auto = restypeIdValues[1];
			var maintaindata = restypeIdValues[2];
			if(restypeId==""){
				$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.select"/>');
				return;
			}
			var returnvalue = "";
			var url = "";
			
			if(restypeId == "column" || restypeId == "report_column" || restypeId == "cs_column"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_menu.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId == "orgunit"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_org.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId == "role"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_roletype.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId == "job"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_job.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId == "dict"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_dict.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId == "group"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_group.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId == "app_column"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_siteApp.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(maintaindata == "true" && auto == "false"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_list_res.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId.substring(0,7) == "channel"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_channel.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else if(restypeId.substring(0,4) == "site"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_site.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}else{
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/input_res.jsp?tag1=resId&tag2=resName&restypeId="+restypeId;
			}
			
			$.dialog({title:'<pg:message code="sany.pdp.common.operation.select"/>',width:616,height:500, content:'url:'+url});  
			
			/*
			if(returnvalue != "" && returnvalue != undefined){
				var val = returnvalue.split(":"); 
				document.all.resId.value = val[0];
				document.all.resName.value = val[1]; 
				if(val.length == 3){
					getopergroup.location.href = "resChange.jsp?restypeId="+restypeId+"&global="+val[2];
				}else{
					getopergroup.location.href = "resChange.jsp?restypeId="+restypeId+"&global=op";
				}
			} 
			*/
		}
		//资源查询
		function queryRes(){
			var resName = document.all.resName.value;
			var selname = document.all.selname.value;
			var typeval = document.all.type.value;
			var restypeIdValue = document.all.restypeId.value;
			var restypeIdValues = restypeIdValue.split("$$");
			var restypeId = restypeIdValues[0]; 
			var auto = restypeIdValues[1];
			var maintaindata = restypeIdValues[2];
			if(resName == ""){
				if(restypeId == "column" || restypeId == "report_column" || restypeId == "cs_column"){
					$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.menu.select"/>');
				}else if(restypeId == "orgunit"){
					$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.org.select"/>');
				}else if(restypeId == "role"){
					$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.role.select"/>');
				}else if(restypeId == "job"){
					$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.post.select"/>');
				}else if(restypeId == "dict"){
					$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.dict.select"/>');
				}else
				{
					$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.resource.select"/>');
				}
				
				document.all.selname.focus();
				return;
			}
			if(selname == ""){
				if(typeval == "user" || typeval == "org" || typeval == "orgjob"){
					$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.org.select"/>');
					document.all.selname.focus();
					return;
				}
			}
			var isRecursionobj = document.all.isRecursion;
			var isRecursion = 0;
			if(isRecursionobj){
				if(isRecursionobj.checked){
					isRecursion = 1;
				}
			}
			queryCondition.target="forDocList";
			queryCondition.action="res_querylist.jsp?isRecursion="+isRecursion;
			queryCondition.submit();
		}
		
		function chooseorg_role(){
			var typeval = document.all.type.value;
			var selid = document.all.selid.value;
			var returnvalue = "";
			var url = "";
			
			if(typeval == "user" || typeval == "org" || typeval == "orgjob"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_org.jsp?tag1=selid&tag2=selname";
			}
			if(typeval == "role"){
				url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_role.jsp?tag1=selid&tag2=selname";
			}
			
			$.dialog({title:'<pg:message code="sany.pdp.common.operation.select"/>',width:616,height:500, content:'url:'+url});
			
			//if(returnvalue != "" && returnvalue != undefined){
				//var val = returnvalue.split(":"); 
				//document.all.selid.value = val[0];
				//document.all.selname.value = val[1];
			//}
			
		}

	  function	reset_(){
		  getopergroup.location.href = "resChange.jsp?restypeId=";
		  
		  $("#reset").click();
	  }
		
		
	</script>
</head>
<body>
<div class="mcontent">
	<div id="searchblock">
		<div  class="search_top">
    		<div class="right_top"></div>
    		<div class="left_top"></div>
    	</div>
    	<div class="search_box">
    		<form name ="userreslist"  method="post" target="forDocList">
    			<table width="98.5%" border="0" cellspacing="0" cellpadding="0">
    				<tr>
      					<td class="left_box"></td>
      					<td>
      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
      							<tr>
      								<th><pg:message code="sany.pdp.role.resource.type"/>：</th>
      								<td>
      									<select class="select" id="restypeId" name="restypeId" onChange="getOperateType()" class="w120">
									      	<option value="">--<pg:message code="sany.pdp.common.operation.select"/>--</option>
									      	<pg:list requestKey="resList" needClear="false">
											    <option value="<pg:cell colName="id"/>$$<pg:cell colName="auto"/>$$<pg:cell colName="maintaindata"/>">
											        <pg:cell colName="name"/>
												</option>
											</pg:list>
										</select>
      								</td>
      								<th id="objsel"><pg:message code="sany.pdp.common.operation.selected"/>：</th>
      								<td>
      									<input name="resName" width="100%" type="text" readonly="true" onclick="selectMenu()" class="w120"/>
										<input name="resId" type="hidden" value=""/>
      								</td>
      								<th><pg:message code="sany.pdp.role.operation.type"/>：</th>
      								<td>
      									<select class="select" name="operategroup" id="operategroup" class="w120">
											<option value="">--<pg:message code="sany.pdp.common.operation.select"/>--</option>
										</select>
      								</td>
      							</tr>
      							<tr>
      								<th><pg:message code="sany.pdp.sysmanager.resource.authorize.object"/>：</th>
      								<td>
      									<select class="select" name="type" onchange="objChange()" class="w120">
											<option value="user"><pg:message code="sany.pdp.sys.user"/></option>
											<option value="role"><pg:message code="sany.pdp.sys.role"/></option>
											<%
											if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", false)){
											%>
												<option value="org"><pg:message code="sany.pdp.sys.org"/></option>
											<%
											}
											%>
											<%
											if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", true)){
											%>
												<option value="orgjob"><pg:message code="sany.pdp.sys.org.post"/></option>
											<%
											}
											%>
										</select>
      								</td>
      								<th id="objName"><pg:message code="sany.pdp.sysmanager.resource.type.org.selected"/>：</th>
      								<td id="objInput">
      									<input name="selname" value="" type="text" onclick="chooseorg_role()" width="100%" readonly="true" class="w120"/>
										<input name="selid" value="" type="hidden"/>
										&nbsp;&nbsp;
										<input name="isRecursion" type="checkbox"/><pg:message code="sany.pdp.sysmanager.resource.type.org.recursion"/>
      								</td>
      								<th>&nbsp;</th>
      								<td>
			      						<a href="javascript:void(0)" class="bt_1" name="search"  onclick="queryRes()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      						<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="reset_()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
      									<input type="reset" id="reset" style="display: none" />
      								</td>
      							</tr>
      						</table>
      					</td>
      					<td class="right_box"></td>
      				</tr>	
    			</table>
    		</form>
    	</div>
    	<div class="search_bottom">
			<div class="right_bottom"></div>
			<div class="left_bottom"></div>
		</div>
	</div>
</div>


<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
</body>
</html>
