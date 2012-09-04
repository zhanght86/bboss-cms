<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*"%>
<%@ include file="../sysmanager/include/global1.jsp"%>
<%@ include file="../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="inc/js/func.js"></script>
<script language="JavaScript" src="../sysmanager/include/pager.js" type="text/javascript"></script>
<script language="javascript" >
 

</script>
<style type="text/css">
<!--
body {
	background-color: #003399;
}
.STYLE1 {color: #0000FF}
.STYLE2 {color: #000099}
-->
</style></head>

<body topmargin="3" rightmargin="0">
<form id="form1" name="form1" method="post" action="">
<table width="810" height="565" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="images/pagebar.jpg" style="background-repeat:no-repeat">
    <table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:通讯录</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
	   <tr>
       <td >
	   
	      <div align="right">|  <a href="main.jsp">欢迎页</a></div></td>
       </tr>
    </table>
    </td>
  </tr>
<!--  <td><div align="center"><img src="images/note.gif" width="16" height="15"></div></td>-->
<tr>
    <td  height="483" valign=top background="images/tile.jpg" bgcolor="#FFFFFF">
  <table width="100%" border="0" align="center">
         				
         				
         				
         				<tr >
         					<td  height='30'valign='middle' align="center"></td>
           				    <td height='30'valign='middle' align="center">姓名：</td>
           					<td height='30'valign='middle' align="center"><input type="text" size="10" name="userName" ></td>
           					<td  height='30'valign='middle' align="center">手机号码：</td>
           					<td  height='30'valign='middle' align="center"><input type="text" size="10"name="userMOBILETEL" ></td>
           					
           					<td  height='30'valign='middle' align="center"></td>
           					<td  height='30'valign='middle' align="center">
           					</td>
           					<td  height='30'valign='middle' align="center"><input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">
           					</td>
           					<td  height='30'valign='middle' align="center">	<input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUser()"></td>
           					<td  height='30'valign='middle' align="left"><input name="newuser" type="button" class="cms_button" value="删除" onClick="newUser()">	</td>
         				</tr>
         				
         				<pg:listdata dataInfo="UseraddressList" keyName="UseraddressList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="5" scope="request" data="UseraddressList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor"  width="3%" align="left"></td>
								<td class="headercolor" width="7%" align="left">
								<P align="left">
								<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on">
								</P>
								</td>
								<td class="headercolor"  width="10%" align="left">登陆名	</td>
								<td class="headercolor"  width="8%" align="left">实名</td>
								<td class="headercolor"  width="12%" align="left">办公电话</td>
								<td class="headercolor"  width="8%" align="left">移动电话</td>
								<td class="headercolor"  width="14%" align="left">Email</td>								
								<td class="headercolor"  width="10%" align="left">传真</td>
								<td class="headercolor"  width="9%" align="left">邮编</td>
								<td class="headercolor"  width="19%" align="left">联系地址</td>
							</tr>
					
							<pg:param name="userName" />
							<pg:param name="userMOBILETEL" />
						
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有用户
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								
								<tr>
									<td class="tablecells" nowrap="nowrap" width="3%">
										<div align="left"><img src="images/note.gif" width="16" height="15"></div>
									</td>
									<td class="tablecells" nowrap="true">
									<P align="left">
										<INPUT type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="userId" defaultValue=""/>' >
									</P>
									</td>
									<td class="tablecells" nowrap="nowrap" width="10%">
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" width="8%">
										<pg:cell colName="userRealname" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userWorktel"><pg:cell colName="userWorktel" defaultValue=" "/></pg:notnull>
										<pg:null colName="userWorktel">保密</pg:null>
										<pg:equal colName="userWorktel" value="">保密</pg:equal>
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userMobiletel1"><pg:cell colName="userMobiletel1" defaultValue=" "/></pg:notnull>
										<pg:null colName="userMobiletel1">保密</pg:null>
										<pg:equal colName="userMobiletel1" value="">保密</pg:equal>
									</td>																		
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userEmail"><pg:cell colName="userEmail" defaultValue=" "/></pg:notnull>
										<pg:null colName="userEmail">没有邮箱</pg:null>
										<pg:equal colName="userEmail" value="">没有邮箱</pg:equal>
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userFax"><pg:cell colName="userFax" defaultValue=" "/></pg:notnull>
										<pg:null colName="userFax">保密</pg:null>
										<pg:equal colName="userFax" value="">保密</pg:equal>
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userPostalcode"><pg:cell colName="userPostalcode" defaultValue=" "/></pg:notnull>
										<pg:null colName="userPostalcode">保密</pg:null>
										<pg:equal colName="userPostalcode" value="">保密</pg:equal>
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userAddress"><pg:cell colName="userAddress" defaultValue=" "/></pg:notnull>
										<pg:null colName="userAddress">保密</pg:null>
										<pg:equal colName="userAddress" value="">保密</pg:equal>
									</td>	
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=10 align='center'>
									<pg:index />
								</td>
								
														    </tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

				  </table>
 </td></tr>
</table> </form>

</body>
</html>
