<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page autoFlush="true" %>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.mailmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response); 	
%>
<html>
<head>
<title>邮件服务器管理</title>
<tab:tabConfig/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/include/pager.js" type="text/javascript"></script>
<script language="JavaScript">
    //动画窗口对象
	var win = null;
	//关闭动画窗口
	function closewin()
	{
		win.close();
	}
	
	function editMsi(msiId){		
		var re = openWin("edit.jsp?msiId="+msiId,600,400);
		if(re == "cf"){
			var str = document.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			document.location.href = strArray+"?"+document.all.queryString.value;
		}
	}
	
	function addMsi(){
		var re = openWin("add.jsp",600,400);
		if(re == "cf"){
			var str = document.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			document.location.href = strArray+"?"+document.all.queryString.value;
		}
	}
	
	function deleteMsi(){		
		if(haveSelect('msiID')){
			if(confirm("确定删除?")){
				myform.action = "handle.jsp?action=delete";
				myform.target = "msiFrame";
				myform.submit();
				win = window.open("<%=request.getContextPath()%>/cms/doing.html","doinghtml","height="+(screen.availHeight-200)+",width="+(screen.availWidth-300)+",top=100,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
			}	
			else{
				return false;
			}
		}else{
			alert("请至少要选择一条记录！");
		}
		
	}
	
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" bottommargin=0 rightmargin-0 leftmargin=0 topmargin="0">
<form method="post" name="myform">
<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" >
  <tr valign="middle" align="center">     
    <td colspan="2" width="99%"  >
	  
		<table bordercolor="#B7CBE4"  class="Datalisttable" width="100%">
		  			<tr>
						<td height='25' colspan="10" background="<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg" style="text-align:left; background:url(<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
						<div  class="DocumentOperT">操作：</div>				
							<a style="cursor:hand" onClick="addMsi()" style="cursor:hand"><div class="DocumentOper"><img 
										src="../images/new_doc.gif" class="operStyle">增加</div></a>
							<a style="cursor:hand" onClick="deleteMsi()" style="cursor:hand"><div class="DocumentOper"><img 
										src="../images/new_doc.gif" class="operStyle">删除</div></a>
						</td>
					</tr>
	  				<pg:listdata dataInfo="MailServerList" keyName="MailServerList" />
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="10" scope="request" data="MailServerList" isList="false">
						<tr class="cms_report_tr">
							<!--设置分页表头-->
	
							<td width="3%" height='30' align=center style="width:3%">
								<input  class="checkbox" type="checkBox" hideFocus=true name="msiCheckBoxAll" onClick="checkAll('msiCheckBoxAll','msiID')">					  </td>
							<td width="14%">名称</td>
							<td width="14%">邮件服务器</td>
							<td width="20%">域名</td>
							<td width="14%">发送人</td>
							<td width="14%">发送人邮箱</td>
							<td width="14%">注释</td>
							<td width="10%">操作</td>
						</tr>
						
						<!--检测当前页面是否有记录-->
						<pg:notify>
							<tr  class="labeltable_middle_tr_01" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
								<td colspan=100 align='center'>
									暂无邮件服务器记录							</td>
							</tr>
						</pg:notify>
	
						<!--list标签循环输出每条记录-->
						<pg:list>
							<%
								String msiId = dataSet.getString("id");
							%>
							<tr class="cms_data_tr"  onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'" id="<%=msiId%>">
								
								<td class="tablecells" align=center height='30'>
									<input   class="checkbox" hideFocus onClick="checkOne('msiCheckBoxAll','msiID')" type="checkbox" name="msiID" value="<%=msiId%>">
								</td>
								<td class="tablecells"  align="center">
									<pg:cell colName="name" defaultValue="" />
								</td>
								<td class="tablecells"  align="center" style="word-wrap:break-word;word-break:break-all;">
									<pg:cell colName="smtpServer" defaultValue="" />
								</td>
								<td class="tablecells"  align="center">
									<pg:cell colName="mailDomain" defaultValue="" />
								</td>
								<td class="tablecells"  align="center" style="word-wrap:break-word;word-break:break-all;">
									<pg:cell colName="mailSenderName" defaultValue="" />
								</td>
								<td class="tablecells"  align="center">
									<pg:cell colName="mailSender" defaultValue="" />
								</td>
								<td class="tablecells"  align="center" style="word-wrap:break-word;word-break:break-all;">
									<pg:cell colName="description" defaultValue="" />
								</td>
								<td class="tablecells"  align="center">
									<font color="blue"><a style="cursor:hand" onclick="editMsi('<%=msiId%>')">修改</a></font>
								</td>
							</tr>
						</pg:list>
						<tr class="labeltable_middle_tr_01">
							<td colspan=10><div class="Data_List_Table_Bottom">
								共
								<pg:rowcount />
								条记录
								<pg:index /></div>
							</td>
						</tr>
						<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
					</pg:pager>
			</table>
	</td>      
  </tr>    
</table> 
</form> 
<div style="display:none;"> 
<iframe width=0 height=0 name="msiFrame"></iframe>   
</div> 
</body>     
</html> 
