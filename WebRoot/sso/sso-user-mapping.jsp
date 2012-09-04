<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(pageContext);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>应用系统信息列表</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<%@ include file="/include/css.jsp"%>
		
		<script language="javascript" type="text/javascript">
		//删除
		function deleteInfo(){
			var id = document.getElementsByName("checkBoxOne");
			var checkId = "";
			var outMsg;
			
			if(id.length){
				for(var i = 0; i < id.length; i ++){
					if(!id[i].checked){
						continue;
					}else{				
						checkId += "'" + id[i].value.split("-")[0] + "',";
						var idValue = id[i].value ;
					}
				}
				if(checkId == ""){
					alert("至少选择一条信息进行删除！");
					return;
				} else {
					outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
			    	if (confirm(outMsg)){
			    		if (checkId.substr((checkId.length - 1),1) == ",") {
			    			checkId = checkId.substring(0, (checkId.length - 1));	//去掉字符串最后面的","
			    		}
			    		form1.action="sso-user-mapping-delete.jsp?ID="+checkId;
			    		form1.submit();
					}
				}
			}
		}
		
		//
		function add() {
			form1.action="sso-user-mapping-add.jsp";
			form1.submit();
		}
</script>
	</head>

	<body>
		<form name="form1" method="post">
			<table width="100%" height="30" border="0" cellpadding="0"
				cellspacing="0" class="thin">
				<tr>
					<td align="center" valign="middle">
						应用系统信息列表
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td height="30" align="right">
						<label>
							<input type="button" name="sss"
								onClick="add();" value="新 增" class="input">
							&nbsp;&nbsp;
						</label>
						<label>
							<input type="button" name="deletemap"
								onClick="return deleteInfo();"
								value="删 除" class="input">
						</label>
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
			</table>
			<pg:listdata
				dataInfo="com.liferay.portlet.iframe.action.UserMappingList"
				keyName="UserMappingList" />
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="15" scope="request" data="UserMappingList" isList="false">
				<table width="100%" border="0" cellpadding="1" cellspacing="0"
					 class="thin" bordercolor="#EEEEEE">
					<tr>
						<td align="center" valign="middle" class="headercolor">
							<div align="center">
								选择
							</div>
						</td>
						<td align="center" valign="middle" class="headercolor">
							<div align="center">
								应用系统A
							</div>
						</td>
						<td align="center" valign="middle" class="headercolor">
							<div align="center">
								A系统用户名
							</div>
						</td>
						<td align="center" valign="middle" class="headercolor">
							<div align="center">
								应用系统B
							</div>
						</td>
						<td align="center" valign="middle" class="headercolor">
							<div align="center">
								B用户名
							</div>
						</td>
						<td align="center" valign="middle" class="headercolor">
							<div align="center">
								B用户密码
							</div>
						</td>
						<td align="center" valign="middle" class="headercolor">
							<div align="center">
								备注
							</div>
						</td>
					</tr>
					<pg:notify>
						<tr height="33px" class="labeltable_middle_tr_01">
							<td colspan=9 align='center'>
								暂时没有信息
							</td>
						</tr>
					</pg:notify>
					<pg:list>
						<tr class="Ctable">
							<td width="5">
								<div align="center">
									<INPUT type="checkbox" name="checkBoxOne"
										onClick="checkOne('checkBoxAll','checkBoxOne')"
										value='<pg:cell colName="ID" defaultValue=""/>'>
								</div>
							</td>
							<td width="80">
								<div align="left">
									<a href="sso-user-mapping-modify.jsp?ID=<pg:cell colName='ID' />"> <pg:cell colName="A_APP_ID"
											defaultValue="" maxlength="30" replace="..." /> </a>
								</div>
							</td>
							<td>
								<div align="center" id="A_USER_NAME">
									<pg:cell colName="A_USER_NAME" defaultValue="" maxlength="30"
										replace="..." />
								</div>
							</td>
							<td>
								<div align="center" id="B_APP_ID">
									<pg:cell colName="B_APP_ID" defaultValue="" maxlength="30"
										replace="..." />
								</div>
							</td>
							<td>
								<div align="center" id="B_USER_NAME">
									<pg:cell colName="B_USER_NAME" defaultValue="" maxlength="30"
										replace="..." />
								</div>
							</td>
							<td>
								<div align="center" id="B_USER_PASSWORD">
									<pg:cell colName="B_USER_PASSWORD" defaultValue="" maxlength="30"
										replace="..." />
								</div>
							</td>
							<td>
								<div align="center" id="USER_REMARK">
									<pg:cell colName="USER_REMARK" defaultValue="" maxlength="30"
										replace="..." />
								</div>
							</td>
						</tr>
					</pg:list>
				</table>
				<table align="center">
					<tr>
						<td align="center">
						
							<pg:index />
						</td>
					</tr>
				</table>
				<input id="queryString" name="queryString" value="<pg:querystring/>"
					type="hidden">
				</input>
			</pg:pager>
			</table>
		</form>
	</body>
</html>

