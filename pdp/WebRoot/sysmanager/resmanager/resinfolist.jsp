<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	
	String title = "";
	title = request.getParameter("title");
	if(title == null)
	{
		title = "";
	}
%>
<html>
	<head>
		<title>属性容器</title>
		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<script language="JavaScript">


	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	function getResInfo(e,resId){
	jsAccessControl.setBackColor(e);
	document.all("selectId").value = resId;
	}
	

	</script>
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder" align="center">
			<form name="Res" action="" method="post">
				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
					<!--<tr>
					 	<td class="detailtitle" width="16%">
							资源类型
						</td>
						<td class="detailcontent" width="30%">
							<input type="text" name=restypeId style="width:100%">
						</td>					
					</tr>-->
					<tr>
						<td class="detailtitle" width="16%" align="right">
							资源名称：
						</td>
						<td class="detailcontent" width="30%">
							<input type="text" name="title" style="width:80%" value="<%=title%>">
						</td>
						<td>
							<div align="center">
								<input name="Submit4" type="submit" class="input" value="查询">
								<input name="Submit22" type="button" class="input" value="重置" onclick="javascript:document.Res.title.value='';">
								
          
							</div>
							<div align="center"></div>
						</td>
					</tr>
				</table>
				<hr width="100%">
				<table cellspacing="1" cellpadding="0" border="0" width=100%  class="thin">
					<tr>
						<td height='30' class="detailtitle" align=center colspan=4>
							<b>资源列表</b>
						</td>
					</tr>
					<pg:listdata dataInfo="ResSubList" keyName="ResSubList" />
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="18" scope="request" data="ResSubList" isList="false">
						<tr class="labeltable_middle_td">
							<!--设置分页表头-->
							
				    		<td class="headercolor">资源类型</td>
				    		<input class="text" type="hidden" name="selectId">
							<td class="headercolor">资源名称</td>
							<td class="headercolor">资源描述</td>
							
						</tr>
						<pg:param name="restype" />
						<pg:param name="title" />
						<pg:param name="resId" />
						<!--检测当前页面是否有记录-->
						<pg:notify>
							<tr height='20' class="labeltable_middle_tr_01">
								<td colspan=100 align='center'>
									暂时没有资源项
								</td>
							</tr>
						</pg:notify>

						<!--list标签循环输出每条记录-->
						<pg:list>
							<tr class="labeltable_middle_tr_01" onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	      				
								<td height='20' class="tablecells" nowrap="nowrap" align=lift>
									<pg:cell colName="restypeName" defaultValue="" />									
									
								</td>

								<td height='20' class="tablecells" nowrap="nowrap" align=lift>
									<pg:cell colName="title" defaultValue="" />
								</td>
								<td height='20' class="tablecells" nowrap="nowrap" align=lift>
									<pg:cell colName="path" defaultValue="暂时为空" />
								</td>
							</tr>
						</pg:list>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'><div align="right">
					    </div>
							   <pg:index />
							</td>
						</tr>
					</pg:pager>

			  </table>

	          <div align="center">                </div>
			</form>
		</div>
		
	</body>
</html>

