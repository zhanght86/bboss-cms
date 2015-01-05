<%
/*
 * <p>Title: 机构资源查询页面</p>
 * <p>Description: 机构资源查询页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-26
 * @author liangbing.tao
 * @version 1.0
 *
 */
 %>
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager,
				com.frameworkset.platform.config.ConfigManager"%>



<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

	String userId = (String)request.getParameter("userId");
	String orgId = (String)request.getParameter("orgId");
	String typeName = (String)request.getParameter("typeName");

	//查询资源列表
	List resQueue = null;
	ResourceManager resManager = new ResourceManager();
	resQueue = resManager.getResourceInfos();
	
	request.setAttribute("resList",resQueue);


%>
<html>
	<head>    
 		<title>用户机构权限资源查询</title>
		<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>		
		<script language="JavaScript" src="../../scripts/pager.js" type="text/javascript"></script>
		<SCRIPT language="javascript">
		var api = parent.frameElement.api, W = api.opener;
		
		function sub(){
			var resId = userreslist.resId.value;
			var resName = userreslist.resName.value;

			var testreg = /^[A-Za-z]+$/;
			var reg_ = /^[A-Za-z\u4e00-\u9fa5]+$/;

			var flag = testreg.test(resId);
			var flag1 = reg_.test(resName);
			
			if(resId = "" || resId.length<1 || resId.replace(/\s/g,"") == ""){
				
			}else{
				//var flag = testreg.test(resId);
				/*
				if(!flag)
				{
					W.$.dialog.alert('<pg:message code="sany.pdp.role.check.resource.identifying.invalid"/>!');
					return false;
				}
				*/
			}
			if(resName = "" || resName.length<1 || resName.replace(/\s/g,"") == ""){
				
			}else{
				/*
				if(!flag1)
					{
					W.$.dialog.alert('<pg:message code="sany.pdp.role.check.resource.name.invalid"/>!');
					return false;
					}
				*/
			}
			
			userreslist.action="userres_querylist.jsp?restypeId="
				+ document.all("resourcetype").value + "&resId="
				+ document.all("resId").value + "&opId="
				+ document.all("operategroup").value+ "&resName="
				+ document.all("resName").value + "&typeName=<%=typeName%>";
				
			userreslist.submit();
		}
		
		function getOperateType(id){
				getopergroup.location.href = "resChange.jsp?restypeId="+id;
		}
		function doreset() {
			document.getElementById("reset").click();
		}
		</SCRIPT>
	</head>

	<body scrolling="no">
		<div  valign="top" align="center" >
		<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
			<form name = "userreslist" method="post" target="forDocList">
			<input type="hidden" name="userId" value="<%=userId%>">
			<input type="hidden" name="orgId" value="<%=orgId%>">
		
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
					<tr>
						<th>
							<pg:message code="sany.pdp.role.resource.identifying"/>：
						</th>
						<td>
						<input type="text" name="resId" size="15"/>
						</td>
						<th>
							<pg:message code="sany.pdp.role.resource.name"/>：
						</th>
						<td>
							<input type="text" name="resName" size="15"/>
						</td>
						<th>
							&nbsp;
						</th>
						<td>
						&nbsp;
						</td>
							</tr>
							<tr>
							<th>
							<pg:message code="sany.pdp.role.resource.type"/>：
							</th>
							<td>
						    <select class="select" id="resourcetype" name="resourcetype" onchange="getOperateType(this.options[this.selectedIndex].value)" style="width:100px">
						    	<option value=""></option>
						      	<pg:list requestKey="resList" needClear="false">
								    	<option value="<pg:cell colName="id"/>">
								        	<pg:cell colName="name"/>
										</option>
								</pg:list>
							</select>
							</td>
							 <th>
							<pg:message code="sany.pdp.role.resource.operation"/>：
							</th>
							<td>
							<select class="select" name="operategroup" id="operategroup" style="width:100px">
							</select>
							</td> 
							<th>
							<pg:message code="sany.pdp.role.resource.category"/>：
							</th>
							<td>
							<% 
							if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
							{
							%>
							<select class="select" name="auto" id="auto" style="width:100px">
								<option value=""></option>
								<option value="0"><pg:message code="sany.pdp.role.system.resource"/></option>
								<option value="1"><pg:message code="sany.pdp.role.user-defined.resource"/></option>
							</select>
							<% 
							}
							%>
						</td>
						<td>
							<a href="javascript:void" class="bt_1" id="addButton" onclick="javascript:sub()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
							<a href="javascript:void" class="bt_2" id="delBatchButton" onclick="javascript:doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
							<input type="reset" id="reset" style="display: none;" />
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
	<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
	</body>

</html>
