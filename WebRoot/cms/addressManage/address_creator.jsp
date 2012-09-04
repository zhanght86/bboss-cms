<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>             
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<tab:tabConfig/>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>添加联系人</title>
<script src="../inc/js/func.js"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="javascript" >
function subForm(){
	var v = usercreator.personName.value;
	if(v==null || v == ""){
		alert("联系人姓名不能为空!")
		return false;
	}
	usercreator.submit();
}
</script>
</head>
<body>

<form id="usercreator" name="usercreator" method="post" target="addFrame" action="address_add.jsp">
<table width="100%" height="100%" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat">
	    <table width="100%" border="0">
	      <tr>
	        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:通讯录－新增联系人</td>
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
					<tab:tabContainer id="personinfo-bar-container" selectedTabPaneId="personinfo-tab">
						<tab:tabPane id="personinfo-1" tabTitle="基本信息">
							<table width="100%" height="100%" border="0"   cellpadding="0" align="center" cellspacing="0">
								<tr>
									<td align="right">
										姓名：
									</td>
									<td>
										<input type="text" name="personName" id="personName" size="16" /><span style="color: red">*</span>
									</td>
									<td align="right">
										生日：
									</td>
									<td>
										<input type="text" name="personBirthday" id="personBirthday" size="16"
										 onClick="showdate(document.all('personBirthday'))" readonly/>
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
										<input type="text" name="personMobiletTel1" id="personMobiletTel1" size="16" />
									</td>
									<td align="right">
										手机2：
									</td>
									<td>
										<input type="text" name="personMobiletTel2" id="personMobiletTel2" size="16" />
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
										<input type="text" name="personQicq" id="personQicq" size="16" />
									</td>
									<td align="right">
										传真：
									</td>
									<td>
										<input type="text" name="personFax" id="personFax" size="16" />
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
										<input type="text" name="personWeb" id="personWeb" size="59" />
									</td>
								</tr>
								<tr>
									<td align="right">
										Email：
									</td>
									<td colspan="4">
										<input type="text" name="personEmail" id="personEmail" size="59" />
									</td>
								</tr>
								<tr>
									<td align="right">
										备注：
									</td>
									<td colspan="4">
										<textarea style="width: 365;height: 80" name="personRemark" id="personRemark"></textarea>
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
										<input type="text" name="personHomeTel" id="personHomeTel" size="16" />
									</td>
									<td align="right">
										邮编：
									</td>
									<td>
										<input type="text" name="personPostalCode" id="personPostalCode" size="16" />
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
										<input type="text" name="personHomeAddress" id="personHomeAddress" size="59" />
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
										<input type="text" name="personCompany" id="personCompany" size="16" />
									</td>
									<td align="right">
										部门：
									</td>
									<td>
										<input type="text" name="personDept" id="personDept" size="16" />
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
										<input type="text" name="personWorkTel" id="personWorkTel" size="16" />
									</td>
									<td align="right">
										职务：
									</td>
									<td>
										<input type="text" name="personDuty" id="personDuty" size="16" />
									</td>
								</tr>
								<tr>
									<td align="right">
										公司地址：
									</td>
									<td colspan="4">
										<input type="text" name="personCoAddress" id="personCoAddress" size="59" />
									</td>
								</tr>
								<tr><td align="right" colspan="5">&nbsp;</td></tr>
							</table>
						</fieldset>
						<br><br><br><br>
						</tab:tabPane>
					</tab:tabContainer>
		</td>
	</tr>
</table>

</form>
<iframe frameborder="0" marginheight="0" marginwidth="0" height="0" width="0" name="addFrame" />
</body>
</html>
