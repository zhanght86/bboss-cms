<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<%@ include file="/include/css.jsp"%>

		<script language="javascript">
		function myGroup() {
			var form = document.forms[0];
					
			getPropertiesToolbar().location.href="/sysmanager/groupmanager/properties_toolbar.jsp?groupId=" + form.selectGroup.value;
			//getPropertiesContent().location.href="../groupInfo.do?method=getGroupInfo&groupId=" + form.selectGroup.value;
		}	
		
		function findGroup() {
			getPropertiesContent().location.href="/sysmanager/groupmanager/findGroup.jsp";
		}
		</script>
	</head>

	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<html:form action="groupList.do">
				<table width="100%" height="96%" border="0" cellpadding="0" cellspacing="0" class="menu1">
					<tr>
						<td height="34">
							<div align="center">
								<a href="javascript:void" onclick="findGroup();">组织查询</a>
							</div>
						</td>
					</tr>
					<tr>
						<td class="menu1">
							<DIV align="center">

								<html:select property="selectGroup" style="width:100%; height:100%" multiple="true" onchange="myGroup();">
									<html:optionsCollection property="groupOfLabelAndValues" />
								</html:select>
							</DIV>
						</td>
					</tr>
				</table>
			</html:form>
		</div>
<%@ include file="../sysMsg.jsp"%>
	</body>
</html>

