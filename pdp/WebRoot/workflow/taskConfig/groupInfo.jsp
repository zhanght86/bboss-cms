
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
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>
<%	
	

	String groupName = request.getParameter("groupName");
	String groupDesc = request.getParameter("groupDesc");
	String groupOwnerName = request.getParameter("groupOwnerName");

	
	groupName = groupName == null ? "" : groupName ;
	groupDesc = groupDesc == null ? "" : groupDesc ;
	groupOwnerName = groupOwnerName == null ? "" : groupOwnerName ;
%>
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
					alert('查询条件不能为空!');
					return false;
				}
				
				if(gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="")
					{
					
					}
				else if(!re.test(gn))
				{
					alert('用户组名称不能有非数字、中文、字母的字符');
					return false; 
				}
				
				if(gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
					{
					
					}
				else if(!re.test(gd))
				{
					alert('用户组描述不能有非数字、中文、字母的字符');
					return false; 
				}
				groupForm.action = "groupInfo.jsp";
				groupForm.submit();
			}
			
			function clearInfo()
			{
				document.getElementById("groupName").value = '';
				document.getElementById("groupDesc").value = '';
				document.groupForm.groupOwnerName.value="";
			}
			
			var tempObj = null;
			function changeRowColor(obj) {
			   if(obj.flag == "true")
			   {
			   		obj.flag = "false";
			   		obj.style.removeAttribute("backgroundColor");  
				    obj.style.color = 'black';
				    tempObj = null;
				    return ;
			   }
			   else
			   {
			   		obj.flag = "true";
			   		obj.style.background='#191970';   //把点到的那一行变希望的颜色; 
			   		obj.style.color = 'white';
			   }
			   if(tempObj!=null && tempObj.flag == "true"){
			        tempObj.flag = "false";
			        tempObj.style.removeAttribute("backgroundColor");  
				    tempObj.style.color = 'black';
			   }
			   tempObj = obj;
			}
			
			function returnUserName(){
				var obj = null;
				var rows = document.all.table1.rows;
				for(var i=0;rows!=null && i<rows.length;i++){
					if(rows[i].flag == "true"){
						obj = rows[i];
						break;
					}
				}
				if(obj!=null){
					window.returnValue = obj.id;
					window.close();
				}else{
					alert("请选择一个用户组!");
				}
			}
			
			function dbClickChoose(obj){
				window.returnValue = obj.id;
				window.close();
			}
			
			$(document).ready(function() {
				 $("#chooosegrouplistContainer").load("queryCandidateGroupByNames.page #choosegrouplistContent", {groups:$("#groups")}); 
			   });
		</script>

<pg:listdata
	dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.GroupSearchList"
	keyName="GroupSearchList" />
<!--分页显示开始,分页标签初始化-->
<pg:pager maxPageItems="10" scope="request" data="GroupSearchList"
	isList="false">

		<div id="contentborder" align="center">
			<form name="groupForm" method="post">
				<input type="hidden" value="${groups}" id="groups" name="groups"/>
				<input type="hidden" value="${nodeinfoId}" id="nodeinfoId" name="nodeId"/>
				<table cellspacing="1" cellpadding="0" border="0"
					bordercolor="#EEEEEE" width=100% class="thin">
					<tr valign='top'>
						<td height='30' valign='middle' colspan="4" nowrap><strong>用户组列表
								<div align="right">
									总用户组数为：
									<pg:rowcount />
								</div>
						</strong></td>
					</tr>
					<tr height=30>
						<td width="25%" align="center" nowrap>用户组名称: <input
							type="text" name="groupName" value="<%=groupName%>">
						</td>
						<td width="25%" align="center" nowrap>用户组描述: <input
							type="text" name="groupDesc" value="<%=groupDesc%>">
						</td>
						<td width="25%" align="center" nowrap><input type="button"
							value="查询" onclick="validateInfo()" class="input"> <input
							type="button" value="清空" onclick="clearInfo()" class="input">
						</td>
					</tr>
				</table>

				<hr width="100%">
				<table width="100%" cellspacing="1" cellpadding="0" border="0"
					bordercolor="#EEEEEE" width=100% class="thin" id="table1">

					<pg:param name="groupName" />
					<pg:param name="groupDesc" />

					<tr class="labeltable_middle_td"
						onmouseover="this.className='mouseover'"
						onmouseout="this.className= 'mouseout'">
						<!--设置分页表头-->
						<td height='30' bgcolor="#EDEFF6" class="headercolor" width="40%">
							用户组名称</td>
						<td height='30' bgcolor="#EDEFF6" class="headercolor">用户组描述</td>
						<td height='30' bgcolor="#EDEFF6" class="headercolor">用户组创建人
						</td>
					</tr>
					<pg:param name="groupName" />
					<pg:param name="groupDesc" />
					<pg:param name="groupOwnerName" />
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height='25' class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>暂时没有用户组</td>
						</tr>
					</pg:notify>

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
						<tr id="<pg:cell colName="groupName" defaultValue="" />"
							class="labeltable_middle_td"
							onmouseover="this.className='mouseover'"
							onclick="changeRowColor(this);"
							onmouseout="this.className= 'mouseout'"
							ondblclick="dbClickChoose(this)" flag="false">
							<td height='20' align=left class="tablecells"><pg:cell
									colName="groupName" defaultValue="没有名称" /></td>
							<td height='20' align=left class="tablecells"><pg:cell
									colName="groupDesc" defaultValue="没有描述" /></td>
							<td height='20' align=left class="tablecells"><%=userName%>【<%=userRealName%>】
							</td>
						</tr>
						<%
							// }
							%>
					</pg:list>
					<tr height="30px">
						<td class="detailcontent" colspan=4 align='center'><pg:index />
							<input type="hidden" name="queryString" value="<pg:querystring/>">
						</td>
					</tr>
				</table>
			</form>
			<div id="chooosegrouplistContainer">
			</div>
			<table id="table3">
				<tr>
					<td align="right"><input type="button" name="but" value="确定"
						class="input" onclick="returnUserName()"></td>
				</tr>
			</table>
		</div>
</pg:pager>
