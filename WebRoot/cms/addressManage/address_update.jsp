<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="../../WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.addressmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String addressBookId = request.getParameter("addressBookId");
	if(addressBookId == null && "".equals(addressBookId)){
%>
		<script language="javascript" >
			alert("请您选择一个联系人!");
			window.close();
		</script>
<%
		return;
	}
	AddressManager am = new AddressManagerImpl();
	Address address = am.getAddressById(addressBookId);
	request.setAttribute("address",address);
	System.out.println(address.getUserId());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<tab:tabConfig/>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>更新联系人</title>
<script src="../inc/js/func.js"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="javascript" >
function subForm(){
	var v = userupdate.personName.value;
	if(v==null || v == ""){
		alert("联系人姓名不能为空!")
		return false;
	}
	userupdate.submit();
}
</script>
</head>
<body>

<form id="userupdate" name="userupdate" method="post" target="updateFrame" action="address_ud.jsp">
<input type="hidden" name="addressBookId" id="addressBookId" value="<%=addressBookId%>" />
<table width="100%" height="100%" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat">
	    <table width="100%" border="0">
	      <tr>
	        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:通讯录－更新联系人</td>
	        <td>&nbsp;</td>
	      </tr>
	      <tr>
	        <td>&nbsp;</td>
	        <td>&nbsp;</td>
	      </tr>
		   <tr>
	       <td >
		      <div align="right">
		      	<input type="button" name="insertbtn" value="保存" onclick="subForm()" class="cms_button" />&nbsp;&nbsp;
		      	<input type="reset" name="resetbtn" value="重置" class="cms_button" />&nbsp;&nbsp;
			  </div></td>
	       </tr>
	    </table>
    </td>
  </tr>
	<tr>
		<td  height="100%" align="left" valign=top background="../images/tile.jpg" bgcolor="#FFFFFF">
			<pg:beaninfo  requestKey="address">
					<tab:tabContainer id="personinfo-bar-container" selectedTabPaneId="personinfo-tab">
						<tab:tabPane id="personinfo-1" tabTitle="基本信息">
							<table width="100%" height="100%" border="0"   cellpadding="0" align="center" cellspacing="0">
								<tr>
									<td align="right">
										姓名：
									</td>
									<td>
										<input type="text" name="personName" id="personName" size="16"
										value="<pg:cell colName="personName" defaultValue=""/>" /><span style="color: red">*</span>
									</td>
									<td align="right">
										生日：
									</td>
									<td>
										<input type="text" name="personBirthday" id="personBirthday" size="16"
										 onClick="showdate(document.all('personBirthday'))" readonly
										 value="<pg:cell colName="personBirthday" defaultValue=""/>" />
									</td>
									<td align="right">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td align="right">
										手机1：
									</td>
									<td>
										<input type="text" name="personMobileTel1" id="personMobileTel1" size="16" 
										value="<pg:cell colName="personMobileTel1" defaultValue=""/>" />
									</td>
									<td align="right">
										手机2：
									</td>
									<td>
										<input type="text" name="personMobileTel2" id="personMobileTel2" size="16" 
										value="<pg:cell colName="personMobileTel2" defaultValue=""/>" />
									</td>
									<td align="right">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td align="right">
										QICQ：
									</td>
									<td>
										<input type="text" name="personQicq" id="personQicq" size="16" 
										value="<pg:cell colName="personQicq" defaultValue=""/>" />
									</td>
									<td align="right">
										传真：
									</td>
									<td>
										<input type="text" name="personFax" id="personFax" size="16" 
										value="<pg:cell colName="personFax" defaultValue=""/>" />
									</td>
									<td align="right">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td align="right">
										个人主页：
									</td>
									<td colspan="4">
										<input type="text" name="personWeb" id="personWeb" size="59" 
										value="<pg:cell colName="personWeb" defaultValue=""/>" />
									</td>
								</tr>
								<tr>
									<td align="right">
										Email：
									</td>
									<td colspan="4">
										<input type="text" name="personEmail" id="personEmail" size="59" 
										value="<pg:cell colName="personEmail" defaultValue=""/>" />
									</td>
								</tr>
								<tr>
									<td align="right">
										备注：
									</td>
									<td colspan="4">
										<textarea style="width: 365;height: 80" name="personRemark" id="personRemark"><pg:cell colName="personRemark" defaultValue=""/></textarea>
									</td>
								</tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
							</table>
						</tab:tabPane>
							
						<tab:tabPane id="personinfo-2" tabTitle="相关信息">
						<fieldset>
							<LEGEND align=left>
								住宅
							</LEGEND><br>
							<table width="100%" height="100%" border="0"   cellpadding="3" align="center" cellspacing="3">
								<tr>
									<td align="right">
										住宅电话：
									</td>
									<td>
										<input type="text" name="personHomeTel" id="personHomeTel" size="16" 
										value="<pg:cell colName="personHomeTel" defaultValue=""/>" />
									</td>
									<td align="right">
										邮编：
									</td>
									<td>
										<input type="text" name="personPostalCode" id="personPostalCode" size="16" 
										value="<pg:cell colName="personPostalCode" defaultValue=""/>" />
									</td>
									<td align="right">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td align="right">
										住宅地址：
									</td>
									<td colspan="4">
										<input type="text" name="personHomeAddress" id="personHomeAddress" size="59" 
										value="<pg:cell colName="personHomeAddress" defaultValue=""/>" />
									</td>
								</tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
							</table>
							</fieldset><br><br>
							<fieldset>
								<LEGEND align=left>
									公司
								</LEGEND><br>
							<table width="100%" height="100%" border="0"   cellpadding="3" align="center" cellspacing="3">
								<tr>
									<td align="right">
										公司名称：
									</td>
									<td>
										<input type="text" name="personCompany" id="personCompany" size="16" 
										value="<pg:cell colName="personCompany" defaultValue=""/>" />
									</td>
									<td align="right">
										部门：
									</td>
									<td>
										<input type="text" name="personDept" id="personDept" size="16" 
										value="<pg:cell colName="personDept" defaultValue=""/>" />
									</td>
									<td align="right">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td align="right">
										公司电话：
									</td>
									<td>
										<input type="text" name="personWorkTel" id="personWorkTel" size="16" 
										value="<pg:cell colName="personWorkTel" defaultValue=""/>" />
									</td>
									<td align="right">
										职务：
									</td>
									<td>
										<input type="text" name="personDuty" id="personDuty" size="16" 
										value="<pg:cell colName="personDuty" defaultValue=""/>" />
									</td>
								</tr>
								<tr>
									<td align="right">
										公司地址：
									</td>
									<td colspan="4">
										<input type="text" name="personCoAddress" id="personCoAddress" size="59" 
										value="<pg:cell colName="personCoAddress" defaultValue=""/>" />
									</td>
								</tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
							</table>
						</fieldset>
						<br><br><br><br>
						</tab:tabPane>
					</tab:tabContainer>
				</pg:beaninfo>
		</td>
	</tr>
</table>

</form>
<iframe frameborder="0" marginheight="0" marginwidth="0" height="0" width="0" name="updateFrame" />
</body>
</html>
