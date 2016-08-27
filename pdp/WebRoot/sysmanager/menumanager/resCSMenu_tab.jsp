<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.framework.MenuItem"%>
<%@ page import="java.util.List,java.util.ArrayList,com.frameworkset.platform.framework.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.*,com.frameworkset.platform.sysmgrcore.manager.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<% 
    String resId = request.getParameter("resId");
	String resName = request.getParameter("resName");
	String columnID = request.getParameter("columnID");
	String menuPath = request.getParameter("nodePath");
	String resTypeId2 = request.getParameter("resTypeId"); //资源类型id cs_column
	String resId2 = resId; //资源id
	String title = resName; //资源名称
    
    CSMenuManager csmenu = new CSMenuManagerImpl();
    CSMenuModel csmenuModel =  csmenu.getCSMenuModelInfoById(resId);
    
	//得模块和子栏目
	String mouseupimg = "";
	String mouseoverimg = "";
	String mouseclickimg = "";
	String mouseoutimg = "";
	String headimg = "";
	String WorkspaceContent = "";
	String WorkspaceToolbar = "";
	String Left = "";
	String Top = "";
	boolean used =true;
	String Title = "................... ";
	String Id = "";
	String Name = csmenuModel.getTitle();
	String NavigatorToolbar = "";
	String NavigatorContent = "";
	String ParentPath = "";
	String Path = "";
	String StatusContent = "";
	String StatusToolbar = "";    
	
    //资源ID
	if (resId2 == null) {
		resId2 = (String) request.getAttribute("resId2");
	}
	
	
	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List operList = resManager.getOperations("column");
	//	List allRole = roleManager.getRoleList();
	if (operList == null) {
		operList = new ArrayList();
	}
	request.setAttribute("operList", operList);
	//request.setAttribute("allRole",allRole);
	String stored = (String) request.getAttribute("stored");
	if (stored == null) {
		stored = "0";
	}
	String roleId = "";
	String rid = "";

%>
<html>


	<head>
		<tab:tabConfig />
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="javascript">
	
		    var resTypeId2 ="<%=resTypeId2%>";
			var resId2 ="<%=resId2%>";
			var http_request = false;
			//初始化，指定处理的函数，发送请求的函数
			function send_request(url){
				http_request = false;
				//开始初始化XMLHttpRequest对象
				if(window.XMLHttpRequest){//Mozilla
					http_request = new XMLHttpRequest();
					if(http_request.overrideMimeType){//设置MIME类别
						http_request.overrideMimeType("text/xml");
					}
				}
				else if(window.ActiveXObject){//IE
					try{
						http_request = new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							http_request = new ActiveXObject("Microsoft.XMLHTTP");
						}catch(e){
						}
					}
				}
				if(!http_request){
					alert("不能创建XMLHttpRequest对象");
					return false;
				}
				http_request.onreadystatechange = processRequest;
				http_request.open("GET",url,true);
				http_request.send(null);
			}

			function processRequest(){
				if(http_request.readyState == 4){                  
					if(http_request.status == 200){
						//alert(http_request.responseText);
					}
					else{
						alert("对不起，服务器错误");
					}
				}
			}
			//------------------------------------------------------------------------------------------
			function changebox(currCheck,rid,priority,opid){
				var roleList = document.roleList;
				var isRecursion = 0;
				//是否递归

				if(roleList.isRecursion.length)				
				{
				if(roleList.isRecursion[rid].checked)
						isRecursion = 1;
				}
			   else if(roleList.isRecursion.checked)
			    {
			    	isRecursion = 1;
			    }
			    
				var menuPath="<%=menuPath%>";
				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
				//checked
				if(currCheck.checked){
					send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion+"&menuPath="+menuPath);
				}
				//not checked
				else{
					send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion+"&menuPath="+menuPath);
				}

				setCheck(currCheck,rid,priority);
			}
			
			function isRecursionClick(currCheck,rid,priority){
		
				var roleList = document.roleList;
				var isRecursion = 0;
				var opid = 0;
				//是否递归

				if(roleList.isRecursion.length)				
				{
					if(roleList.isRecursion[rid].checked)
						isRecursion = 1;
				}
			    else if(roleList.isRecursion.checked)
			    {
			    	isRecursion = 1;
			    }
			    
				var menuPath="<%=menuPath%>";
				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
					
					
				var opers = roleList.all("opId"+rid);
				//没有复选框
  				if(!opers){  	
  					alert("您没有选择操作");
  					return;
  				}
  				var n=roleList.all("opId"+rid).length;
  				//只有一个复选框
  				if(!n){
	  				opid = roleList.all("opId"+rid).value;
	  			
					if(roleList.all("opId"+rid).checked){
						send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion+"&menuPath="+menuPath);
					}
					else
						send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion+"&menuPath="+menuPath);
				}
				//有多个复选框
				for(var i= 0;i<n;i++){
				
					opid = opers[i].value;
					if(opers[i].checked){						
						send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion+"&menuPath="+menuPath);
					}
					else
						send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion+"&menuPath="+menuPath);
				}
			}
			
			function changebox2(opid,checked){
				var isRecursion = 0;
				//是否递归
				if(document.forms[0].isRecursion.checked)
					isRecursion = 1;
				var roleList = document.roleList;
				var resid = "<%=resId2%>";
				var menuPath="<%=menuPath%>";
				var restypeid = roleList.resTypeId.value;
				send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked="+checked+"&title=<%=title%>&menuPath="+menuPath+"&isRecursion="+isRecursion);
			}

			function setCheck(currCheck,rid,priority)
			{
			   	var o = document.getElementsByName(currCheck.name);
				var prioritylist = document.getElementsByName("priority"+rid);
				if (currCheck.checked==true && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;

						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1)&& priority.substring(1,2) == v.substring(1,2) )
						{
							o[i].checked=true;
							changebox2(o[i].value,1);
							//alert("3:"+o[i].value);
							//o[i].disabled=true;
						}
					}

					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(1,2) != v.substring(1,2) )
						{
							o[i].checked=false;
							changebox2(o[i].value,0);
							//alert("4:"+o[i].value);
							//o[i].disabled=false
						}
					}
				}
				if (currCheck.checked==false  && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if ( v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1) )
						{
							if ( o[i].checked==true ){
								//currCheck.checked==true;
								//o[i].disabled=false;
							}
						}
					}

				}
			}

	</script>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
	<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
	</head>
	

	<body>
		<tab:tabContainer id="foo-menu-container" selectedTabPaneId="foo-menu">
			<tab:tabPane id="foo-menu" tabTitle="菜单信息查看">
				<FORM name="11" method="post">
					<tr>
						<td height="35" align=center colspan=5>
							<P>
								<B>菜单信息明细</B>
							</P>
							<P>
								<STRONG></STRONG>
							</P>
							<P></P>
							<P></P>
						</td>
					</tr>

					<fieldSet id="1" title="333">
						<LEGEND align=left>
							<strong><FONT size=2>菜单基本信息</FONT></strong>
						</LEGEND>
						<table>
							<tr>
								<td width="100" height="28" class="detailtitle" wiidth="50%">
									Id:
								</td>
								<td class="detailcontent">
									<%=csmenuModel.getId()%>
								</td>
							</tr>
							<tr>
								<td width="100" height="28" class="detailtitle" wiidth="50%">
									used:
								</td>
								<td class="detailcontent">
									<%=used%>
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									Name:
								</td>
								<td class="detailcontent">
									<%=Name%>
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									Title:
								</td>
								<td class="detailcontent">
									<%=Title%>
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									Path:
								</td>
								<td class="detailcontent">
									<%=csmenu.getCSMenuPath(resId)%>
								</td>
							</tr>
							
							<tr>
								<td class="detailtitle" height="28">
									Type:
								</td>
								<td class="detailcontent">
									<%=csmenuModel.getType()%>
								</td>
							</tr>

							
						</table>
					</fieldSet>					
					<fieldSet title="菜单图片信息">
						<LEGEND align=left>
							<strong><FONT size=2>菜单图片信息</FONT></strong>
						</LEGEND>
						<table>
							<tr>
								<td class="detailtitle" height="28" wiidth="40%">
									mouseupimg:
								</td>
								<td class="detailcontent">
									<%=mouseupimg%>
								</td>
							</tr>

							<tr>
								<td class="detailtitle" height="28">
									mouseoverimg:
								</td>
								<td class="detailcontent">
									<%=mouseoverimg%>
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									mouseclickimg:
								</td>
								<td class="detailcontent">
									<%=mouseclickimg%>
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									mouseoutimg:
								</td>
								<td class="detailcontent">
									<%=mouseoutimg%>
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									headimg:
								</td>
								<td class="detailcontent">
									<%=headimg%>
								</td>
							</tr>

						</table>
					</fieldSet>


				</FORM>
			</tab:tabPane>


			<tab:tabPane id="bar-menu" tabTitle="菜单资源授权">
				<input name="resId" value="<%=resId%>" type="hidden">
				<fieldset>
					<LEGEND align=left>
						<strong><FONT size=2>帮助</FONT></strong>
					</LEGEND>
					<%boolean flag;
			AccessControl accesscon = AccessControl.getInstance();
			flag = accesscon.allowIfNoRequiredRoles(resTypeId2);
			//System.out.println("hehelalal" + flag);
			if (flag == true) {

			%>
					<table>
						<tr>
							<td>
								该资源在没有授予任何角色或用户（即下面的复选框都处于未被选中状态）的情况下,允许访问该资源，一旦授给某个用户或角色后，其它角色或用户也必须授权后才能访问
							</td>
						</tr>
					</table>
					<%} else {

			%>
					<table align=left>
						<tr>
							<td>
								必须对该资源授权才可以访问该资源，在没有授权的情况下是不能访问该资源
							</td>
						</tr>
					</table>
					<%}%>
				</fieldset>
				<FORM name="roleList" method="post">

					<!--				分页显示开始,分页标签初始化-->
					<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.RoleList" keyName="RoleList" />
					<pg:pager maxPageItems="16" scope="request" data="RoleList" isList="false">
						<pg:param name="resTypeId" />
						<pg:param name="resId" />
						<pg:param name="resName" />
						<pg:param name="columnID" />
						<pg:param name="nodePath" />

						<table cellspacing="1" cellpadding="0" border="0" width=98% class="thin">
							<TR>
								<td colspan="3" align="center">
									<span class="STYLE1"><%=title%></span>&nbsp;作为资源授给角色列表(直接选择复选框即可，系统实时保存)
								</td>
							</TR>

							<TR>
								<td height='20' class="headercolor">
									角色名称
								</td>
								<td height='20' class="headercolor">
									操作列表
								</td>
								<td height='20' class="headercolor">
									是否递归
								</td>
							</TR>
							<pg:list>
								<%boolean flag1;
			//判断给定的角色是否是超级管理员角色
			flag1 = accesscon
					.isAdministratorRole(dataSet.getString("roleName"));
			if (flag1 == true) {

				%>
								<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
									<td align="left">
										<pg:cell colName="roleName" />
										<%rid = rowid;
				roleId = dataSet.getString("roleId");
				List hasOper = operManager.getOperResRoleList(roleId, resId2,
						resTypeId2);

				%>
									</td>
									<td align="left">
										<pg:list requestKey="operList" needClear="false" declare="false">
											<input name="opId<%=rid%>" type="checkbox" checked disabled="true"
												<%
						        	String opId = dataSet.getString("id");
						        	
						        	
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
						        		{
						        			out.println("checked");
						        			break;
						        		}

						        	}
						        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')">

								&gt; <pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
										</pg:list>


									</td>
									<td>
										<input name="isRecursion" onclick="isRecursionClick(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>')" type="checkbox" checked disabled="true">
										递归授权子菜单
									</td>

								</TR>
								<%} else {
				boolean flag2;
				//判断资源是否是未受保护的资源
				flag2 = accesscon.isUnprotected(resId2, resTypeId2);
				boolean flag4;
				//判断资源所有操作是否只有超级管理员才能访问
				flag4 = accesscon.isExcluded(resId2, resTypeId2);
				if (flag2 == true) {

					%>
								<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
									<td align="left">
										<pg:cell colName="roleName" />
										<%rid = rowid;
					roleId = dataSet.getString("roleId");
					List hasOper = operManager.getOperResRoleList(roleId,
							resId2, resTypeId2);

					%>
									</td>
									<td align="left">
										<pg:list requestKey="operList" needClear="false" declare="false">
											<input name="opId<%=rid%>" type="checkbox"
												<%
						        	String opId = dataSet.getString("id");
						        	
						        	
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
						        		{
						        			out.println("checked");
						        			break;
						        		}

						        	}
						        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" checked disabled="true">

								&gt; <pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
										</pg:list>


									</td>
									<td>
										<input name="isRecursion" onclick="isRecursionClick(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>')" type="checkbox" checked disabled="true">
										递归授权子菜单
									</td>
								</TR>
								<%} else if (accesscon.isExcluded(resId2, resTypeId2) == true) {%>
								<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
									<td align="left">
										<pg:cell colName="roleName" />
										<%rid = rowid;
					roleId = dataSet.getString("roleId");
					List hasOper = operManager.getOperResRoleList(roleId,
							resId2, resTypeId2);

					%>
									</td>
									<td align="left">
										<pg:list requestKey="operList" needClear="false" declare="false">
											<input name="opId<%=rid%>" type="checkbox"
												<%
						        	String opId = dataSet.getString("id");
						        	
						        	
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
						        		{
						        			out.println("checked");
						        			break;
						        		}

						        	}
						        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" disabled="true">

								&gt; <pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
										</pg:list>


									</td>
									<td>
										<input name="isRecursion" onclick="isRecursionClick(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>')" type="checkbox" disabled="true">
										递归授权子菜单
									</td>
								</TR>
								<%} else {%>
								<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
									<td align="left">
										<pg:cell colName="roleName" />
										<%rid = rowid;
					roleId = dataSet.getString("roleId");
					List hasOper = operManager.getOperResRoleList(roleId,
							resId2, resTypeId2);

					%>
									</td>
									<td align="left">
										<pg:list requestKey="operList" needClear="false" declare="false">
											<%boolean flag3;
					//判断资源是否是未受保护的资源
					flag3 = accesscon.isUnprotected(resId2, dataSet
							.getString("id"), resTypeId2);
					if (flag3 == true) {

						%>
											<input name="opId<%=rid%>" type="checkbox"
												<%
						        	String opId = dataSet.getString("id");
						        	
						        	
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
						        		{
						        			out.println("checked");
						        			break;
						        		}

						        	}
						        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" checked disabled="true">

								&gt; <pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
											<%} else if (accesscon.isExcluded(resId2, dataSet
							.getString("id"), resTypeId2) == true) {%>
											<input name="opId<%=rid%>" type="checkbox"
												<%
						        	String opId = dataSet.getString("id");
						        	
						        	
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
						        		{
						        			out.println("checked");
						        			break;
						        		}

						        	}
						        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" disabled="true">

								&gt; <pg:cell colName="name" />
											<%} else {%>
											<input name="opId<%=rid%>" type="checkbox"
												<%
						        	String opId = dataSet.getString("id");
						        	
						        	
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
						        		{
						        			out.println("checked");
						        			break;
						        		}

						        	}
						        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')">

								&gt; <pg:cell colName="name" />
											<%}%>
										</pg:list>


									</td>
									<td>
										<input name="isRecursion" onclick="isRecursionClick(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>')" type="checkbox">
										递归授权子菜单
									</td>
								</TR>
								<%}
			}%>

							</pg:list>
							<tr>
								<td colspan="3" align="center">
									<input name="resTypeId" type="hidden" value="<%=resTypeId2%>">
									<input name="resId" type="hidden" value="<%=resId2%>">
									<input name="title" type="hidden" value="<%=title%>">
								</td>
							</tr>
							<tr>
								<td colspan="3" align="right">
									<pg:index />
								</td>
							</tr>
						</table>
					</pg:pager>
				</FORM>
			</tab:tabPane>
		</tab:tabContainer>
	</body>
</html>

