<%
/*
 * <p>Title: 用户组信息页面</p>
 * <p>Description: 显示用户组信息与查询</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	

	
	String groupName = request.getParameter("groupName");
	String groupDesc = request.getParameter("groupDesc");
	String groupOwnerName = request.getParameter("groupOwnerName");

	
	groupName = groupName == null ? "" : groupName ;
	groupDesc = groupDesc == null ? "" : groupDesc ;
	groupOwnerName = groupOwnerName == null ? "" : groupOwnerName ;
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="javascript">
			function validateInfo()
			{
				var gn = groupForm.groupName.value;
				var gd = groupForm.groupDesc.value;
				var gon = groupForm.groupOwnerName.value;
				var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
				
				if((gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="") 
					&& (gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
					&& (gon == "" || gon.length<1 || gon.replace(/\s/g,"")==""))
				{
					$.dialog.alert("<pg:message code='sany.pdp.groupmanage.query.can.not.null'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				
				if(gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="")
					{
					
					}
				else if(!re.test(gn))
				{
					$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.name.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false; 
				}
				
				if(gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
					{
					
					}
				else if(!re.test(gd))
				{
					$.dialog.alert("<pg:message code='sany.pdp.groupmanage.group.description.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false; 
				}
				var regUser = /^\w+$/;
				groupForm.action = "groupInfo.jsp";
				groupForm.submit();
			}
			
			function clearInfo()
			{
				document.getElementById("groupName").value = '';
				document.getElementById("groupDesc").value = '';
				document.groupForm.groupOwnerName.value="";
			}
		</script>
		
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.GroupSearchList" keyName="GroupSearchList" />
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10" scope="request" data="GroupSearchList" isList="false">
		
		<body>	
			<div id="" align="center">
			<form name="groupForm" method="post" >
			<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">		
				
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
						<tr height=30>
							<th>
								<pg:message code="sany.pdp.groupmanage.group.name"/>： 
							</th>
							<td>
								<input type="text" name="groupName" value="<%=groupName%>" >
							</td>
							<th>
								<pg:message code="sany.pdp.groupmanage.group.description"/>：
							</th>
							<td>
							 <input type="text" name="groupDesc" value="<%=groupDesc%>" >
							</td>
							
							<th>
								<pg:message code="sany.pdp.workflow.creater.username"/>：
							</th>
							<td>
							<input type="text" name="groupOwnerName" value="<%=groupOwnerName%>" >
							</td>
							<td width="25%" align="center" nowrap>
								<a class="bt_1"  onclick="validateInfo()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
								<a class="bt_2" onclick="clearInfo()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
							</td>
						</tr>
					</table>
								</td>
								<td class="right_box"></td>
							</tr>
						</table>
				</div>
					<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
				<div class="title_box">
						<strong><pg:message code="sany.pdp.groupmanage.group.list"></pg:message>
		       			</strong>	
					</div>
				</div>
				<pg:equal actual="${GroupSearchList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:equal> 
					<pg:notequal actual="${GroupSearchList.itemCount}"  value="0">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
				
						<pg:param name="groupName"/>
						<pg:param name="groupDesc"/>
						
							<!--设置分页表头-->
						<pg:header>
							<th>
								<pg:message code="sany.pdp.groupmanage.group.name"/>
							</th>
							<th>
								<pg:message code="sany.pdp.groupmanage.group.description"/>
							</th>
							<th>
								<pg:message code="sany.pdp.workflow.creater.username"/>
							</th>
						</pg:header>
						<pg:param name="groupName" />
						<pg:param name="groupDesc" />
						<pg:param name="groupOwnerName" />
						
						<!--list标签循环输出每条记录-->
						<pg:list>
							<% 
							//只显示这个用户创建的用户组 2008-4-8 baowen.liu
	                          //     String  userId=accesscontroler.getUserID();						
								String data = (String)dataSet.getString("owner_id");
								UserManager userManager=new UserManagerImpl();
								User user=userManager.getUserById(data);
								String userName=user.getUserName();
								String userRealName=user.getUserRealname();
							  //  if(userId.equals(data)){
							%>
							<tr class="labeltable_middle_td"  
								onmouseover="this.className='mouseover'"
								onmouseout="this.className= 'mouseout'">
								<td height='20' align=left class="tablecells">
										<pg:cell colName="groupName" defaultValue=""/>
								</td>
								<td height='20' align=left class="tablecells">
									<pg:cell colName="groupDesc" defaultValue="" />
								</td>
								<td height='20' align=left class="tablecells">
									<%=userName%>【<%=userRealName%>】
								</td>
							</tr>
							<%
							// }
							%>
						</pg:list>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
				  </div>
				</pg:notequal>
			</form>
		</div>
	</body>
	</pg:pager>
</html>
