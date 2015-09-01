<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%
/**
 * <p>Title: 机构查询</p>
 * <p>Description: 机构查询页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String orgId = request.getParameter("orgId");
	String remark5 = request.getParameter("remark5");
	String orgcreator = request.getParameter("orgcreator");
	String orgnumber = request.getParameter("orgnumber");
	String isEffective = request.getParameter("isEffective");
	
	if (remark5 == null)
		remark5 = "";
	if (orgnumber == null)
		orgnumber = "";
	if (orgcreator == null)
		orgcreator = "";
	if (isEffective == null)
	    isEffective = "";
%>
<html>
	<head>
		<title>机构查询</title>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
			<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<script language="JavaScript">
			function resetSearch()
			{
				document.all.remark5.value = "";
				document.all.orgnumber.value = "";
				document.all.orgcreator.value="";
				document.all.isEffective.value="";
			}
			function orgSearch()
			{
				var orgName = document.all.remark5.value;
				var orgnumber = document.all.orgnumber.value;
				var orgcreator = document.all.orgcreator.value;
				
				var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
				if(orgName == "" || orgName.length<1 || orgName.replace(/\s/g,"") == ""){
				}else{
					if(!re.test(orgName))
					{
					
					$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.name.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false; 
					}
				}
				
				if(orgnumber.search(/[^0-9A-Za-z]/g) !=-1){
					$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.number.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					Org.orgnumber.focus();
					return false;
					}
				if(orgcreator == "" || orgcreator.length<1 || orgcreator.replace(/\s/g,"") == ""){
					
				}else{
					var re_ = /^\w+$/; 
					if(!re_.test(orgcreator))
					{
						$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.creater.invalid'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false; 
					}
				}
				
				document.all.orgList.src="organsearchlist.jsp?remark5="+document.all.remark5.value
				+"&orgnumber="+document.all.orgnumber.value+"&orgcreator="+document.all.orgcreator.value
				+"&isEffective="+document.all.isEffective.value;
			}
		</script>
	</head>
	<body>
		<div style="height: 10px">&nbsp;</div>
			<div class="mcontent">
				<div id="searchblock">
						<div  class="search_top" >
				    		<div class="right_top"></div>
				    		<div class="left_top"></div>
			     		</div>
			     		<div class="search_box">
			     			<form name="Org" action="" method="post">
			     				<table width="98.4%" border="0" cellspacing="0" cellpadding="0">
			     					<tr>
				      					<td class="left_box"></td>
				      					<td>
				      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
				      							<tr>
													<th>
														<pg:message code="sany.pdp.role.organization.name"></pg:message>：
														<input type="text" name="remark5" value="<%=remark5%>" class="w120" />
													</th>
													<th>
														<pg:message code="sany.pdp.role.organization.number"></pg:message>：
														<input type="text" name="orgnumber" value="<%=orgnumber%>" class="w120" />
													</th>
													<th>
														<pg:message code="sany.pdp.role.organization.creater"></pg:message>：
														<input type="text" name="orgcreator" value="<%=orgcreator%>" class="w120" />
													</th>
													<th>
														<pg:message code="sany.pdp.role.organization.check"></pg:message>：
														<select name="isEffective" class="w120">
															<option value="" selected <pg:equal colName="isEffective" value="0">selected </pg:equal>><pg:message code="sany.pdp.all"/></option>
															<option value="1" <pg:equal colName="isEffective" value="1">selected </pg:equal>><pg:message code="sany.pdp.valid"/></option>
															<option value="0" <pg:equal colName="isEffective" value="0">selected </pg:equal>><pg:message code="sany.pdp.invalid"/></option>
															
															
														</select>
													</th>
													<td>
														<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="orgSearch()"><span><pg:message code="sany.pdp.common.operation.search"/></span>
														</a>
														<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="resetSearch()"><span><pg:message code ="sany.pdp.common.operation.reset"/></span>
														</a>
														<input type="reset" id="reset" style="display:none"/>
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
			
			<br/>
			
			<iframe width="102%" height=505px frameborder=0 noResize scrolling="false" marginWidth=0 name="orgList" src="organsearchlist.jsp"></iframe>
			</div>
	</body>
</html>

