
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.security.authorization.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    //flag为1表示新稿文档的呈送,为2表示返工文档的呈送,为3表示继续送审,为4表示内容采集时直接送审，此时还没有文档id，文档还没保存
    String flag = request.getParameter("flag"); 
    int preAuditorid = -1;
    String redict = "";
    if(flag.equals("1")){
    	String docidStr = request.getParameter("docidStr");
    	redict = "deliverHandle.jsp?flag=1&docidStr=" + docidStr;
    }
    else if(flag.equals("2")){
    	String docid = request.getParameter("docid");
    	String taskid = request.getParameter("taskid");
    	//获取当前返工任务的前驱待审任务的审核人
    	DocumentManager docManager = new DocumentManagerImpl();
    	preAuditorid = docManager.getPreSubmitterId(Integer.parseInt(taskid));
    	redict = "deliverHandle.jsp?flag="+ flag +"&docid=" + docid + "&taskid=" + taskid;
    }
    else if(flag.equals("3")){
    	String docid = request.getParameter("docid");
    	String taskid = request.getParameter("taskid");
    	String auditComment = request.getParameter("taskid");
    	//获取当前审核人
    	preAuditorid = Integer.parseInt(accesscontroler.getUserID());
    	redict = "deliverHandle.jsp?flag="+ flag +"&docid=" + docid + "&taskid=" + taskid + "&auditComment=" + auditComment;
    }
	ChannelManager cmi=new ChannelManagerImpl();
	String channelId = request.getParameter("channelId");
	List list = cmi.getAuditorList(channelId);		//审核人列表
	List roleList = cmi.getAuditRoleList(channelId);	//审核角色列表
	List orgList = cmi.getAuditOrgList(channelId);	//审核机构列表
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>审核人列表</title>
	<tab:tabConfig/>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<script language="javascript">
		//提交	
		function sub(){
			if(document.getElementsByName('checkBoxOne').length !=0){
					if(haveSelect('checkBoxOne')){
						if("<%=flag%>"=="4"){				//内容采集时直接送审，将审核人信息回填
							var temp = "";
							//审核人
							var checkBoxes  = document.getElementsByName("checkBoxOne");
							for(var i=0;i<checkBoxes.length;i++){
								if (checkBoxes[i].checked){
									var auditor = checkBoxes[i].value;
									temp = temp + "<input type='hidden' value=" + auditor +" id='auditor' name='auditor'>";
								}
							}
							window.dialogArguments.document.all.auditorDiv.innerHTML = temp;
							window.dialogArguments.window.saveform(10);
							window.close();
						}
						else if("<%=flag%>"=="44"){				//内容修改时直接送审，将审核人信息回填,flag 44等同于原来的flag 2
							var temp = "";
							//审核人
							var checkBoxes  = document.getElementsByName("checkBoxOne");
							for(var i=0;i<checkBoxes.length;i++){
								if (checkBoxes[i].checked){
									var auditor = checkBoxes[i].value;
									temp = temp + "<input type='hidden' value=" + auditor +" id='auditor' name='auditor'>";
								}
							}
							window.dialogArguments.document.all.auditorDiv.innerHTML = temp;
							window.dialogArguments.window.saveform(100);
							window.close();
						}
						else{			//送审处理
							form1.action="<%=redict%>";
							form1.target="deliverHandleFrame";
							form1.submit();
							window.returnValue = "cf";
						}
					}else{
						alert("请至少选择一个审核人");
					}
			}else{
					alert("此频道暂时还没有审核人,请指定审核人后在送审!");
					window.close();					
			}
		}
		function winclose(){
			window.close();
		}
		function chechAuditor(ob,elName){
			var auditorCheckBoxes = document.getElementsByName("checkBoxOne");			
			var els = document.getElementsByName(elName);
			for(var i=0;i<els.length;i++){				
				for(var j=0;j<auditorCheckBoxes.length;j++){
					if(auditorCheckBoxes[j].value == els[i].value)
						auditorCheckBoxes[j].checked = ob.checked;
				}
			}
		}
	</script>
	<style type="text/css">
		body {
			background-color:#FFFFFF;
		}
	</style>
</head>
<body background="../images/grayline_bg.jpg">
<form name="form1"  method="post">
<table width="99%">
  <tr>
   <td>
	<tab:tabContainer id="foo-extfield-add" selectedTabPaneId="foo-extfield">
	  <tab:tabPane id="foo-extfield" tabTitle="审核人列表">
		<table width="99%"  border="0" cellspacing="0" cellpadding="3" align="center" class="Datalisttable" bgcolor="#FFFFFF">
		  <th height="30" colspan="2" background="../images/rolemanage_CT_bg.jpg"  class="authority_title"  bgcolor="#ACC1D4">
		  	<font color="#FFFFFF">请选择审核人</font></th>
		  <tr class="cms_report_tr">
		    <td align="center" height="30" width="15%" >
				<input name="checkBoxAll" type="checkbox" hidefocus onClick="checkAll('checkBoxAll','checkBoxOne')">
			</td>
		    <td align="center" height="30">
				审 核 人
			</td>
		  </tr>
		  <%
		  		int count=0;
			  	for(int i=0;i<list.size();i++){
					User user =(User)list.get(i);
					String username = user.getUserName();
					int useid = user.getUserId().intValue();
					if((useid == preAuditorid && flag.equals("2")) || (useid==1 && !flag.equals("2"))){
						count++;
					  %>
					  <tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
					    <td align="center" height="30"  class="tablecells">
							<input name="checkBoxOne" checked type="checkbox" value="<%=useid%>" hidefocus onClick="checkOne('checkBoxAll','checkBoxOne')">
						</td>
					    <td align="center" height="30"  class="tablecells">
							<%=username%>
						</td>
					  </tr>
					  <%
		  			}else if(!(useid == preAuditorid && flag.equals("3"))){
		  				count++;
					   %>
					  <tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
					    <td align="center" height="30"  class="tablecells">
							<input name="checkBoxOne" type="checkbox" value="<%=useid%>" hidefocus onClick="checkOne('checkBoxAll','checkBoxOne')">
						</td>
					    <td align="center" height="30"  class="tablecells">
							<%=username%>
						</td>
					  </tr>
					  <%			
					}  			
		  		}
		  		if(count==0){
		  %>
		  	<tr>
		  		<td colspan = "2" align="center" height="30">
		  			该频道暂时还没指定审核人或者没有其它审核人！
		  		</td>
		  	</tr>
		  <%
		  		}
		  %>
		  <tr>
		  </tr>
		</table>
	  </tab:tabPane>
	  <tab:tabPane id="bar-extfield-role" tabTitle="角色列表">
	  	<table width="99%"  border="0" cellspacing="0" cellpadding="3" align="center" class="Datalisttable" bgcolor="#FFFFFF">
		  <th height="30" colspan="2" background="../images/rolemanage_CT_bg.jpg"  class="authority_title"  bgcolor="#ACC1D4">
		  	<font color="#FFFFFF">请选择角色</font></th>
		  <tr class="cms_report_tr">
		    <td align="center" height="30" width="15%" >
				<input name="roleCheckBoxAll" type="checkbox" hidefocus onClick="checkAll('roleCheckBoxAll','roleCheckBoxOne')">
			</td>
		    <td align="center" height="30">
				角 色
			</td>
		  </tr>
		  <%
			  	for(int i=0;i<roleList.size();i++){
					Role role =(Role)roleList.get(i);
					String roleName = role.getRoleName();
					String roleId = role.getRoleId();
					  %>
					  <tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
					    <td align="center" height="30"  class="tablecells">
							<input name="roleCheckBoxOne" type="checkbox" value="<%=roleId%>" hidefocus onClick="checkOne('roleCheckBoxAll','roleCheckBoxOne');" onpropertychange="chechAuditor(this,'<%="auditorOfRole"+roleId%>');">
						</td>
					    <td align="center" height="30"  class="tablecells">
							<%=roleName%>
						</td>
					  </tr>					 
					  <%	
					List auditorOfRole = SecurityDatabase.getUserManager().getUserList(role);
					for(int j=0;j<auditorOfRole.size();j++){	
						int auditorIdOfRole = ((User)auditorOfRole.get(j)).getUserId().intValue();
						out.println("<input name='auditorOfRole" + roleId +"' type='hidden' value='" + auditorIdOfRole + "'>");
					} 				
		  		}
		  	if(roleList.size()==0){
		  %>
		  	<tr>
		  		<td colspan = "2" align="center" height="30">
		  			该频道暂时还没指定具有审核权限的角色
		  		</td>
		  	</tr>
		  <%
		  	}
		  %>
		  <tr>
		  </tr>
		</table>
	  </tab:tabPane>
	  <tab:tabPane id="bar-extfield-org" tabTitle="机构列表">
	  	<table width="99%"  border="0" cellspacing="0" cellpadding="3" align="center" class="Datalisttable" bgcolor="#FFFFFF">
		  <th height="30" colspan="2" background="../images/rolemanage_CT_bg.jpg"  class="authority_title"  bgcolor="#ACC1D4">
		  	<font color="#FFFFFF">请选择机构</font></th>
		  <tr class="cms_report_tr">
		    <td align="center" height="30" width="15%" >
				<input name="orgCheckBoxAll" type="checkbox" hidefocus onClick="checkAll('orgCheckBoxAll','orgCheckBoxOne')">
			</td>
		    <td align="center" height="30">
				机 构
			</td>
		  </tr>
		  <%
			  	for(int i=0;i<orgList.size();i++){
					Organization org =(Organization)orgList.get(i);
					String orgName = org.getOrgName();
					String orgId = org.getOrgId();
					  %>
					  <tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
					    <td align="center" height="30"  class="tablecells">
							<input name="orgCheckBoxOne" type="checkbox" value="<%=orgId%>" hidefocus onClick="checkOne('orgCheckBoxAll','orgCheckBoxOne');" onpropertychange="chechAuditor(this,'<%="orgOfRole"+orgId%>');">
						</td>
					    <td align="center" height="30"  class="tablecells">
							<%=orgName%>
						</td>
					  </tr>					 
					  <%	
					  List auditorOfOrg = SecurityDatabase.getUserManager().getUserList(org);
					  for(int j=0;j<auditorOfOrg.size();j++){	
						int auditorIdOfOrg = ((User)auditorOfOrg.get(j)).getUserId().intValue();			
						out.println("<input name='orgOfRole" + orgId +"' type='hidden' value='" + auditorIdOfOrg + "'>");
					  } 					
		  		}
		  	if(orgList.size()==0){
		  %>
		  	<tr>
		  		<td colspan = "2" align="center" height="30">
		  			该频道暂时还没指定具有审核权限的机构
		  		</td>
		  	</tr>
		  <%
		  	}
		  %>
		  <tr>
		  </tr>
		</table>
	  </tab:tabPane>
	</tab:tabContainer>
   </td>
  </tr>
  <tr>
   <td colspan="2" align="center" height="30">
	 <input name="submit1" type="button" value="提交" class="cms_button" onClick="sub()">
	 &nbsp;
	 <input name="close1" type="button" value="返回" class="cms_button" onClick="winclose()">
   </td>
  </tr>
</table>
</form>
<div style="display:none">
	<iframe name="deliverHandleFrame" width="0" height="0">
	</iframe>
</div> 
</body>
</html>