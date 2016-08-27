<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<html>
	<head>
		<title>属性容器101</title>
		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<script language="JavaScript">
	function queryType()
	{	
		document.all.inputTypeList.src="inputTypeList.jsp?typeName="+document.all.typeName.value;
	}
    function resetForm(){
        document.all("typeDesc").value = "";
    }
</script>
	<body class="contentbodymargin">
	<sany:menupath menuid="inputtypemanage"/>
	<div class="mcontent">
		<div id="searchblock">
			<div  class="search_top">
		    	<div class="right_top"></div>
		    	<div class="left_top"></div>
      		</div>
      		<div class="search_box">
      			<table width="98.7%" border="0" cellspacing="0" cellpadding="0">
      					<tr>
		      				<td class="left_box"></td>
		      				<td>
		      					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
		      						<tr>
		      							<th><pg:message code="sany.pdp.dictmanager.dict.key.inputtype.input"/>：</th>
		      							<td><input type="text" name="typeName" class="w120" /></td>
		      							<th>&nbsp;</th>
		      							<td>
		      								<a href="javascript:void(0)" class="bt_1" id="search"  onclick="queryType()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      							<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="document.all.typeName.value='';"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
		      							</td>
		      						</tr>
		      					</table>
		      				</td>
		      				<td class="right_box"></td>
		      			<tr>
      			</table>
      		</div>
      		<div class="search_bottom">
				<div class="right_bottom"></div>
				<div class="left_bottom"></div>
			</div>
		</div>
		
			<iframe name="inputTypeList" frameborder="0" width="100%" height="580" src="inputTypeList.jsp"></iframe>
	</div>
	
	</body>
</html>

