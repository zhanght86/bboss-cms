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
    //flag为1表示文档从非已审状态（如新稿或者已归档等状态）提交发布,为2表示审稿通过后直接提交发布,
    //为4表示内容采集时直接提交发布，此时还没有文档id，文档还没保存
    String flag = request.getParameter("flag"); 
    String channelId = request.getParameter("channelId"); 
    String auditComment = request.getParameter("auditComment"); 
    
    String redict = "";
    //flag为1时传来的参数
    if(flag.equals("1")){
    	String docidStr = request.getParameter("docidStr");
    	redict = "subPublishHandle.jsp?flag=1&docidStr=" + docidStr;
    }
    //flag为2时传来的参数
    if(flag.equals("2")){
	    String docid = request.getParameter("docid");
	    String taskid = request.getParameter("taskid");
	    redict = "subPublishHandle.jsp?flag=2&docid=" + docid + "&taskid=" + taskid + "&auditComment=" + auditComment;
    }
    
    ChannelManager cmi=new ChannelManagerImpl();
	List list = cmi.getPublisherList(channelId);		//发布人列表	
	List roleList = cmi.getPublishRoleList(channelId);	//发布角色列表
	List orgList = cmi.getPublishOrgList(channelId);	//发布机构列表
%>
<html>
  <head>
    <title>发布人列表</title> 
    <tab:tabConfig/> 
    <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
    <script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
    <script language="javascript">
    	//提交	
		function sub(){
			if("<%=flag%>"=="4"){				//内容采集时直接提交发布，将审核人信息回填
				var checkBoxes = document.getElementsByName("checkBoxOne");
				var temp = "";
				for(var i=0;i<checkBoxes.length;i++){
					if (checkBoxes[i].checked){
						var publisher = checkBoxes[i].value;
						temp = temp + "<input type='hidden' value=" + publisher +" id='publisher' name='publisher'>";
					}
				}
				window.dialogArguments.document.all.auditorDiv.innerHTML = temp;
				window.dialogArguments.window.saveform(11);
				window.close();
			}else{
				if(document.getElementsByName('checkBoxOne').length !=0 ){
					if(haveSelect('checkBoxOne')){
						form1.action="<%=redict%>";
						form1.target="publisherListFrame";
						form1.submit();
						window.returnValue = "cf";
					}
					else{
						alert("请至少选择一个发布人");
					}
				}
				else{
					alert("此频道暂时还没有发布人,请指定发布人后在提交发布!");
					window.close();
					
				}
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
  </head>
  
<body background="../images/grayline_bg.jpg">
<form name="form1"  method="post">
 <table width="99%">
  <tr>
   <td>
   	<tab:tabContainer id="foo-extfield-add" selectedTabPaneId="foo-extfield">
	  <tab:tabPane id="foo-extfield" tabTitle="发布人列表"> 
		<table width="99%" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" align="center" class="Datalisttable">
		  <th colspan="2" height="30"  class="cms_title_blue" bgcolor="#ACC1D4">
		  	<font color="#FFFFFF">请选择发布人</font>
		  </th>
		  <tr class="cms_report_tr">
		    <td align="center" height="30" width="15%" >
				<input class="checkbox" name="checkBoxAll" type="checkbox" hidefocus onClick="checkAll('checkBoxAll','checkBoxOne')">
			</td>
		    <td align="center" height="30">
				发布人
			</td>
		  </tr>
		  <%
		  	int count=0;
			  	for(int i=0;i<list.size();i++){
			  		User user =(User)list.get(i);
					String username = user.getUserName();
					int useid = user.getUserId().intValue();
					if(useid == 1){
						count++;
						%>
						 <tr class="cms_data_tr">
						    <td align="center" height="30"  class="tablecells">
								<input class="checkbox" name="checkBoxOne" checked type="checkbox" value="<%=useid%>" hidefocus onClick="checkOne('checkBoxAll','checkBoxOne')">
							</td>
						    <td align="center" height="30"  class="tablecells">
								<%=username%>
							</td>
						  </tr>
						<%
					}else{
		  				count++;
					   %>
					  	<tr class="cms_data_tr">
						    <td align="center" height="30"  class="tablecells">
								<input class="checkbox" name="checkBoxOne" type="checkbox" value="<%=useid%>" hidefocus onClick="checkOne('checkBoxAll','checkBoxOne')">
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
	<iframe name="publisherListFrame" width="0" height="0"></iframe>
</div>  
</body>
</html>
