<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.ResourceAction"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.tag.OperList" %>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>

<%//http://localhost:8081/creatorcms/cms/siteManage/res_manager.jsp?resId2=200&resTypeId2=site&resTypeName=郴州市政府门户网站(勿删!)&title=郴州市政府门户网站(勿删!)
//http://localhost:8081/creatorcms/sysmanager/accessmanager/role/operList_site_tab.jsp?selected=254&classType=site&resName=测试&resTypeId=site&resId=254&sitename=测试&siteid=254
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	AccessControl accesscon = AccessControl.getInstance();
	String resTypeId2 = request.getParameter("resTypeId2");
	String resId2 = request.getParameter("resId2");
	String resTypeName = request.getParameter("resTypeName");
	String title = request.getParameter("title");
	if (resId2 == null) {
		resId2 = (String) request.getAttribute("resId2");
	}
	if (resTypeId2 == null) {
		resTypeId2 = (String) request.getAttribute("resTypeId2");
	}
	OperManager operManager = SecurityDatabase.getOperManager();
	UserManager userManager = SecurityDatabase.getUserManager();
	ResourceManager resManager = new ResourceManager();
	List operList = null;
	List hasOper = null;//
	
	String userid = request.getParameter("userid");//用户
	String username = request.getParameter("username");
	String roleids = request.getParameter("roleids");//角色
	String rolename = request.getParameter("rolename");
	String orgids = request.getParameter("orgids");//机构
	String orgname = request.getParameter("orgname");
	String userOrRole = request.getParameter("userOrRole");//是用户还是角色标识位

	String[] usersStr = null;//用户
	String[] roleStr = null;//角色
	String[] orgStr = null; //机构

	ChannelManager chlimpl = new ChannelManagerImpl();
	usersStr = chlimpl.getUsersOfChl(resId2);
	roleStr = chlimpl.getRolesOfChl(resId2);
	orgStr = chlimpl.getOrgsOfChl(resId2);
	
	String useridsStr = usersStr[0];//用户
	String usernamesStr = usersStr[1];
	String userType = usersStr[2];
	String roleidsStr = roleStr[0];//角色
	String rolenamesStr = roleStr[1];
	String roleType = roleStr[2];
	String orgidsStr = orgStr[0];//机构
	String orgnamesStr = orgStr[1];
	String orgType = orgStr[2];
	//String userOrRoleStr = usersStr[2];

	if(userid!=null&&!userid.equals(""))//用户
	{
		String[] eids = userid.split(",");
		String[] enames = username.split(",");

		if(useridsStr!=null&&!useridsStr.equals(""))
		{
			for(int i=0;i<eids.length;i++)
			{
				if((","+useridsStr).indexOf(","+eids[i]+",")==-1)
				{
					useridsStr += eids[i] + ",";
					usernamesStr += enames[i] + ",";
					userType += "user" + ",";
				}
			}
		}
		else
		{
			useridsStr = userid;
			usernamesStr = username;
			userType = "";
			for(int i=0;i<eids.length;i++)
			{
				userType += "user" + ",";
			}
		}
	}
	if(roleids!=null&&!roleids.equals(""))//角色
	{
		String[] eids = roleids.split(",");
		String[] enames = rolename.split(",");

		if(roleidsStr!=null&&!roleidsStr.equals(""))
		{
			for(int i=0;i<eids.length;i++)
			{
				if((","+roleidsStr).indexOf(","+eids[i]+",")==-1)
				{
					roleidsStr += eids[i] + ",";
					rolenamesStr += enames[i] + ",";
					roleType += "role" + ",";
				}
			}
		}
		else
		{
			roleidsStr = roleids;
			rolenamesStr = rolename;
			roleType = "";
			for(int i=0;i<eids.length;i++)
			{
				roleType += "role" + ",";
			}
		}
	}
	if(orgids!=null&&!orgids.equals(""))//机构
	{
		String[] eids = orgids.split(",");
		String[] enames = orgname.split(",");

		if(orgidsStr!=null&&!orgidsStr.equals(""))
		{
			for(int i=0;i<eids.length;i++)
			{
				if((","+orgidsStr).indexOf(","+eids[i]+",")==-1)
				{
					orgidsStr += eids[i] + ",";
					orgnamesStr += enames[i] + ",";
					orgType += "organization" + ",";
				}
			}
		}
		else
		{
			orgidsStr = orgids;
			orgnamesStr = orgname;
			orgType = "";
			for(int i=0;i<eids.length;i++)
			{
				orgType += "organization" + ",";
			}
		}
	}
	String[] users = useridsStr.split(",");//所有有权限的用户
	String[] usernames = usernamesStr.split(",");
	String[] usertypes = userType.split(",");
	String[] roles = roleidsStr.split(",");//所有有权限的角色
	String[] rolenames = rolenamesStr.split(",");
	String[] roletypes = roleType.split(",");
	String[] orgs = orgidsStr.split(",");//所有有权限的机构
	String[] orgnames = orgnamesStr.split(",");
	String[] orgtypes = orgType.split(",");
	int idCount = 0;//
	int usernum = ("".equals(useridsStr))?0:users.length;
	int rolenum = ("".equals(roleidsStr))?0:roles.length;
	int orgnum = ("".equals(orgidsStr))?0:orgs.length;
	%>
<html>
	<head>
		<title>内容管理权限管理</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<tab:tabConfig/>
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<SCRIPT language="javascript">
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
					else{alert(http_request.status);
						alert("对不起，服务器错误");
					}
				}
			}

			function changebox(currCheck,rid,priority,opid,userOrRole,restypeid){
				var resid = document.forms[0].resId.value;
				//var restypeid = document.forms[0].resTypeId.value;
				var urlstr = "";
				//判断保存的是用户还是角色
				if(userOrRole == "role")
					urlstr = '../../sysmanager/resmanager/saveRoleOp.jsp?resId=';
				if(userOrRole == "user")
					urlstr = "../../sysmanager/resmanager/saveRoleOpOfUser.jsp?resId=";
				if(userOrRole == "organization")
					urlstr = "../../sysmanager/resmanager/saveRoleOpOfOrg.jsp?resId=";
				//checked
				if(currCheck.checked){
					send_request(urlstr+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&resTypeName=<%=resTypeName%>");
				}
				//not checked
				else{
					send_request(urlstr+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&resTypeName=<%=resTypeName%>");
				}

				setCheck(currCheck,rid,priority,userOrRole,restypeid);
			}

			function changebox2(opid,checked,userOrRole,restypeid){
				var resid = document.forms[0].resId.value;
				//var restypeid = document.forms[0].resTypeId.value;
				var urlstr = "";
				//判断保存的是用户还是角色
				if(userOrRole == "role")
					urlstr = '../../sysmanager/resmanager/saveRoleOp.jsp?resId=';
				if(userOrRole == "user")
					urlstr = "../../sysmanager/resmanager/saveRoleOpOfUser.jsp?resId=";
				if(userOrRole == "organization")
					urlstr = "../../sysmanager/resmanager/saveRoleOpOfOrg.jsp?resId=";

			send_request(urlstr+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked="+checked+"&title=<%=title%>&resTypeName=<%=resTypeName%>");
			}

			function setCheck(currCheck,rid,priority,userOrRole,restypeid)
			{
			   	var o = document.getElementsByName(currCheck.name);
				var prioritylist = document.getElementsByName("priority"+rid);
				var urlstr = "";
				
				if (currCheck.checked==true && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;

						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1)&& priority.substring(1,2) == v.substring(1,2) )
						{
							o[i].checked=true;
							changebox2(o[i].value,1,userOrRole,restypeid);
							//alert("3:"+o[i].value);
							//o[i].disabled=true;
						}
					}

					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(1,2) != v.substring(1,2) )
						{
							o[i].checked=false;
							changebox2(o[i].value,0,userOrRole,restypeid);
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
			//add user
			function addUsers()
			{
				window.open('../../sysmanager/schedularmanage/selectexecutor.jsp?displayNameInput=username&displayValueInput=userid','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250');
			}
			//changeRows()
			function changeRows()
			{
				document.forms[0].submit();
			}
			//add user
			function addRoles()
			{
				window.open('../../sysmanager/accessmanager/role/roleList_cms.jsp?displayNameInput=username&displayValueInput=userid','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250');
			}
			//增加机构
			function addOrgs()
			{
				window.open('../orgManage/org_tree_multi_select.jsp?displayNameInput=username&displayValueInput=userid','newWin','scrollbars=auto,status=no,titlebar=no,toolbar=no,z-lock=yes,width=350,height=450,top=150,left=250');
			}
			//checkAll();
			function checkAll(cur,page,rows,id)
			{
				if(cur.checked)
				{
					var all = document.getElementsByName("opId"+(page*rows+id));
					for(var j=0;j<all.length;j++)
					{
						if(!all[j].checked)
							all[j].click();
					}
				}
				else
				{
					var all = document.getElementsByName("opId"+(page*rows+id));
					for(var j=0;j<all.length;j++)
					{
						if(all[j].checked)
							all[j].click();
					}
				}
			}
			//del
			function del(rows,id)
			{
				for(var i=0;i<2;i++)
				{
					var all = document.getElementsByName("opId"+(i*rows+id));
					for(var j=0;j<all.length;j++)
					{
						if(all[j].checked)
						{
							all[j].click();
						}
					}
				}
				for(var i=0;i<2000000;i++){}//延时
				document.location.href = document.URL;
			}
			//递归 
			function recursionOp(cur)
			{
				if(cur.checked)
				{
					var count = "<%=usernum+rolenum%>";
					if(count<=0)
						return false;
					if(confirm('您确定要递归授权子频道吗？'))
					{
						document.forms[0].target = "saveop";
						document.forms[0].action = "../../sysmanager/resmanager/recursionSaveOp.jsp";
						document.forms[0].submit();
					}
				}
			}
		</SCRIPT>
		<style type="text/css">
<!--
body{background-color: #ffffff;
scrollbar-face-color: #C9D1E4; 
scrollbar-shadow-color: #6B74B7; 
scrollbar-highlight-color: white; 
scrollbar-3dlight-color: #E100E1; 
scrollbar-darkshadow-color:#E100E1; 
scrollbar-arrow-color:#003492; 
scrollbar-base-color: #E100E1; 
scrollbar-track-color: #E9EDF3;}
.STYLE1 {padding:4px; font-size:14px; font-weight:bold; color:#ffffff}
-->
        </style>
	</head>
	<body scroll="auto">
		<div id="contentborder" align="center">
			<FORM name="roleList" method="post">
				<input type="hidden" name="resTypeName" value="<%=resTypeName%>" />
				<input type="hidden" name="restypeId" value="<%=resTypeId2%>" />
				<input name="resTypeId" type="hidden" value="<%=resTypeId2%>">
				<input name="resId" type="hidden" value="<%=resId2%>">
				<input name="title" type="hidden" value="<%=title%>">
				<input name="usernum" type="hidden" value="<%=usernum%>">
				<input name="rolenum" type="hidden" value="<%=rolenum%>">
				<input name="orgnum" type="hidden" value="<%=orgnum%>">
				<input type="hidden" name="userid" value="<%=useridsStr.equals("")?"":useridsStr.substring(0,useridsStr.length()-1)%>" onpropertychange="changeRows();"/>
				<input type="hidden" name="username" value="<%=usernamesStr.equals("")?"":usernamesStr.substring(0,usernamesStr.length()-1)%>"/>
				<input type="hidden" name="roleids" value="<%=roleidsStr.equals("")?"":roleidsStr.substring(0,roleidsStr.length()-1)%>" onpropertychange="changeRows();"/>
				<input type="hidden" name="rolename" value="<%=rolenamesStr.equals("")?"":rolenamesStr.substring(0,rolenamesStr.length()-1)%>"/>
				<input type="hidden" name="orgids" value="<%=orgidsStr.equals("")?"":orgidsStr.substring(0,orgidsStr.length()-1)%>" onpropertychange="changeRows();"/>
				<input type="hidden" name="orgname" value="<%=orgnamesStr.equals("")?"":orgnamesStr.substring(0,orgnamesStr.length()-1)%>"/>
				<tab:tabContainer id="res-managerofchl" selectedTabPaneId="res-manager-chl">
					<tab:tabPane id="res-managerofchl-chl" tabTitle="频道类权限">
						<table cellspacing="0" cellpadding="3" border="0" width="100%" class="Datalisttable">
							<%
								operList = resManager.getOperations("channel");
								if (operList == null) {
									operList = new ArrayList();
								}
								request.setAttribute("operList", operList);
						    %>
							<TR>
								<td height="30" colspan="<%=operList.size()+3%>" align="center" bgcolor="#ACC1D4" class="authority_title" style="color:#fff">【<%=resTypeName%>】频道&nbsp;操作权限列表(直接选择复选框即可，系统实时保存)</td>
							</TR>
							<tr style="text-align:left;">
								<td height='25' style="text-align:left;" colspan="<%=operList.size()+3%>">
								<input type="checkbox" name="isRecursion" hideFocus onclick="recursionOp(this)" >递归授权子频道
								</td>
							</tr>
							<tr>
								<td height='25' colspan="<%=operList.size()+3%>" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
								<a style="cursor:hand" onClick="addUsers()"><div class="DocumentOper"><img 
											class="operStyle" src="../../sysmanager/images/plan.gif">添加用户</div></a>
								</td>
							</tr>

							<TR class="cms_report_tr">
								<td height='30' class="headercolor">
									<strong>全选</strong>
								</td>
								<td height='30' class="headercolor">
									<strong>删除</strong>
								</td>
								<td height='30' class="headercolor">
									<strong>用户</strong>
								</td>
								<%
									for(int num=0;num<operList.size();num++){
								%>
									<td class="headercolor" align="center">
										<strong><%=((com.frameworkset.platform.config.model.Operation)operList.get(num)).getName()%></strong>
									</td>
								<%}%>
							</TR>
							<%
						for(int aa=0;aa<users.length;aa++)
						{
							if(users[aa]==null||users[aa].equals(""))
								continue;

							String roleid = users[aa];

							hasOper = operManager.getOperResRoleList(usertypes[aa],roleid,resId2,"channel");
							if(hasOper == null){
								hasOper = new ArrayList();
							}
						%>
						<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td align="center">
							<input type="checkbox" id="<%=aa%>" hideFocus onClick="checkAll(this,0,<%=usernum+rolenum+orgnum%>,<%=aa%>)"/>
						</td>
						<td align="center">
							<a href="#" onclick="del(<%=usernum+rolenum%>,<%=aa%>)"><u>删除</u></a>
						</td>
						<td align="center">
						<input type="hidden" name="userno<%=aa%>" value="user"/>
						<input type="hidden" name="curuserid" value="<%=users[aa]%>"/>
						<%=usernames[aa]%>
						</td>
						
						<pg:list requestKey="operList" needClear="false">
							<td align="center">
							<%
								String rid = rowid;
								String opId = dataSet.getString(Integer.parseInt(rowid),"id");
								
							%>
							<input name="opId<%=idCount+aa%>" type="checkbox"
							<%
								if(AccessControl.isAdmin(userManager.getUserById(users[aa]).getUserName()))
								{
									out.print(" disabled=\"true\" checked ");
								}
								else
								{//System.out.println(resId2+"|"+opId+"|"+resTypeId2);
									if(!accesscontroler.checkPermission(resId2,opId,resTypeId2) 
									|| accesscontroler.getUserID().equals(users[aa]))
									{
										out.print(" disabled=\"true\" ");
									}//System.out.println(" disabled=\"true\" " + users[aa]);
									for(int i = 0; i < hasOper.size(); i ++)
									{
										RoleresopKey id = (RoleresopKey)hasOper.get(i);
										if(id.getOpId().equals(opId) && id.getRoleId().equals(users[aa]))
										{
											out.println(" checked ");
											break;
										}
											
									}
								}
							%>
							value="<%=roleid%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=idCount+aa%>','<pg:cell colName="priority" defaultValue=""/>','<%=users[aa]%>;<pg:cell colName="id"/>','<%=usertypes[aa]%>','channel')">
							<input name="priority<%=idCount+aa%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</td>
						  </pg:list>
						  
					   </tr>
					   <%
						   } 
						   idCount += usernum;//
					   %>
					   <!--角色-->
					   <tr>
							<td height='25' colspan="<%=operList.size()+3%>" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
							<a style="cursor:hand" onClick="addRoles()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">添加角色</div></a>
							</td>
						</tr>
					   <TR class="cms_report_tr">
							<td height='30' class="headercolor">
								<strong>全选</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>删除</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>角色</strong>
							</td>
							<%
								for(int num=0;num<operList.size();num++){
							%>
								<td class="headercolor" align="center">
									<strong><%=((com.frameworkset.platform.config.model.Operation)operList.get(num)).getName()%></strong>
								</td>
							<%}%>
						</TR>
							<%
						for(int aa=0;aa<roles.length;aa++)
						{
							if(roles[aa]==null||roles[aa].equals(""))
								continue;

							String roleid = roles[aa];

							hasOper = operManager.getOperResRoleList(roletypes[aa],roleid,resId2,"channel");
							if(hasOper == null){
								hasOper = new ArrayList();
							}
						%>
						<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td align="center">
							<input type="checkbox" id="<%=usernum+aa%>" hideFocus onClick="checkAll(this,0,<%=usernum+rolenum+orgnum%>,<%=usernum+aa%>)"/>
						</td>
						<td align="center">
							<a href="#" onclick="del(<%=usernum+rolenum%>,<%=usernum+aa%>)"><u>删除</u></a>
						</td>
						<td align="center">
						<input type="hidden" name="roleno<%=aa%>" value="role"/>
						<input type="hidden" name="curuserid" value="<%=roles[aa]%>"/>
						<%=rolenames[aa]%>
						</td>
						
						<pg:list requestKey="operList" needClear="false">
							<td align="center">
							<%
								String rid = rowid;
								String opId = dataSet.getString(Integer.parseInt(rowid),"id");
								
							%>
							<input name="opId<%=idCount+aa%>" type="checkbox"
							<%
								for(int i = 0; i < hasOper.size(); i ++)
								{
									RoleresopKey id = (RoleresopKey)hasOper.get(i);
									if(id.getOpId().equals(opId) && id.getRoleId().equals(roles[aa]))
									{
										out.println("checked");
										break;
									}
										
								}
							%>
							value="<%=roleid%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=idCount+aa%>','<pg:cell colName="priority" defaultValue=""/>','<%=roles[aa]%>;<pg:cell colName="id"/>','<%=roletypes[aa]%>','channel')">
							<input name="priority<%=idCount+aa%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</td>
						  </pg:list>
						  
					   </tr>
					   <%
						   } 
						   idCount += rolenum;//
					   %>
					   <!--/////机构/////-->
					   <tr>
							<td height='25' colspan="<%=operList.size()+3%>" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
							<a style="cursor:hand" onClick="addOrgs()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">添加机构</div></a>
							</td>
						</tr>
					   <TR class="cms_report_tr">
							<td height='30' class="headercolor">
								<strong>全选</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>删除</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>机构</strong>
							</td>
							<%
								for(int num=0;num<operList.size();num++){
							%>
								<td class="headercolor" align="center">
									<strong><%=((com.frameworkset.platform.config.model.Operation)operList.get(num)).getName()%></strong>
								</td>
							<%}%>
						</TR>
							<%
						for(int aa=0;aa<orgs.length;aa++)
						{
							if(orgs[aa]==null||orgs[aa].equals(""))
								continue;

							String orgid = orgs[aa];

							hasOper = operManager.getOperResRoleList(orgtypes[aa],orgid,resId2,"channel");
							if(hasOper == null){
								hasOper = new ArrayList();
							}
						%>
						<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td align="center">
							<input type="checkbox" id="<%=orgnum+aa%>" hideFocus onClick="checkAll(this,0,<%=usernum+rolenum+orgnum%>,<%=usernum+rolenum+aa%>)"/>
						</td>
						<td align="center">
							<a href="#" onclick="del(<%=orgnum+rolenum%>,<%=orgnum+aa%>)"><u>删除</u></a>
						</td>
						<td align="center">
						<input type="hidden" name="orgno<%=aa%>" value="org"/>
						<input type="hidden" name="curorgid" value="<%=orgs[aa]%>"/>
						<%=orgnames[aa]%>
						</td>
						
						<pg:list requestKey="operList" needClear="false">
							<td align="center">
							<%
								String rid = rowid;
								String opId = dataSet.getString(Integer.parseInt(rowid),"id");
								
							%>
							<input name="opId<%=idCount+aa%>" type="checkbox"
							<%
								for(int i = 0; i < hasOper.size(); i ++)
								{
									RoleresopKey id = (RoleresopKey)hasOper.get(i);
									if(id.getOpId().equals(opId) && id.getRoleId().equals(orgs[aa]))
									{
										out.println("checked");
										break;
									}
										
								}
							%>
							value="<%=orgid%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=idCount+aa%>','<pg:cell colName="priority" defaultValue=""/>','<%=orgs[aa]%>;<pg:cell colName="id"/>','<%=orgtypes[aa]%>','channel')">
							<input name="priority<%=idCount+aa%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</td>
						  </pg:list>
						  
					   </tr>
					   <%
						   } 
						   idCount += orgnum;//
					   %>
					  </table>
					</tab:tabPane>
					<tab:tabPane id="res-managerchl-document" tabTitle="文档类权限">
						<table cellspacing="0" cellpadding="3" border="0" width="100%" class="Datalisttable">
							<%
								operList = resManager.getOperations("channeldoc");
								if (operList == null) {
									operList = new ArrayList();
								}
								request.removeAttribute("operList");
								request.setAttribute("operList", operList);
						    %>
							<TR>
								<td height="30" colspan="<%=operList.size()+3%>" align="center" bgcolor="#ACC1D4" class="authority_title" style="color:#fff">【<%=resTypeName%>】频道&nbsp;操作权限列表(直接选择复选框即可，系统实时保存)</td>
							</TR>
							<tr style="text-align:left;">
								<td height='25' style="text-align:left;" colspan="<%=operList.size()+3%>">
								<input name="isRecursion" hideFocus onclick="recursionOp(this)" type="checkbox">递归授权子频道
								</td>
							</tr>
							<tr>
								<td height='25' colspan="<%=operList.size()+3%>" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
								<a style="cursor:hand" onClick="addUsers()"><div class="DocumentOper"><img 
											class="operStyle" src="../../sysmanager/images/plan.gif">添加用户</div></a>
								</td>
							</tr>

							<TR class="cms_report_tr">
								<td height='30' class="headercolor">
									<strong>全选</strong>
								</td>
								<td height='30' class="headercolor">
									<strong>删除</strong>
								</td>
								<td height='30' class="headercolor">
									<strong>用户</strong>
								</td>
								<%
									for(int num=0;num<operList.size();num++){
								%>
									<td class="headercolor" align="center">
										<strong><%=((com.frameworkset.platform.config.model.Operation)operList.get(num)).getName()%></strong>
									</td>
								<%}%>
							</TR>
							<%
						for(int aa=0;aa<users.length;aa++)
						{
							if(users[aa]==null||users[aa].equals(""))
								continue;

							String roleid = users[aa];

							hasOper = operManager.getOperResRoleList(usertypes[aa],roleid,resId2,"channeldoc");
							if(hasOper == null){
								hasOper = new ArrayList();
							}
						%>
						<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td align="center">
							<input type="checkbox" id="<%=aa%>" hideFocus onClick="checkAll(this,1,<%=usernum+rolenum+orgnum%>,<%=aa%>)"/>
						</td>
						<td align="center">
							<a href="#" onclick="del(<%=usernum+rolenum%>,<%=aa%>)"><u>删除</u></a>
						</td>
						<td align="center">
						<input type="hidden" name="userno<%=aa%>" value="user"/>
						<input type="hidden" name="curuserid" value="<%=users[aa]%>"/>
						<%=usernames[aa]%>
						</td>
						
						<pg:list requestKey="operList" needClear="false">
							<td align="center">
							<%
								String rid = rowid;
								String opId = dataSet.getString(Integer.parseInt(rowid),"id");
								
							%>
							<input name="opId<%=idCount+aa%>" type="checkbox"
							<%
								if(AccessControl.isAdmin(userManager.getUserById(users[aa]).getUserName()))
								{
									out.print(" disabled=\"true\" checked ");
								}
								else
								{//System.out.println(resId2+"|"+opId+"|"+resTypeId2);
									if(!accesscontroler.checkPermission(resId2,opId,resTypeId2) 
									|| accesscontroler.getUserID().equals(users[aa]))
									{
										out.print(" disabled=\"true\" ");
									}//System.out.println(" disabled=\"true\" " + users[aa]);
									for(int i = 0; i < hasOper.size(); i ++)
									{
										RoleresopKey id = (RoleresopKey)hasOper.get(i);
										if(id.getOpId().equals(opId) && id.getRoleId().equals(users[aa]))
										{
											out.println(" checked ");
											break;
										}
											
									}
								}
							%>
							value="<%=roleid%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=idCount+aa%>','<pg:cell colName="priority" defaultValue=""/>','<%=users[aa]%>;<pg:cell colName="id"/>','<%=usertypes[aa]%>','channeldoc')">
							<input name="priority<%=idCount+aa%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</td>
						  </pg:list>
						  
					   </tr>
					   <%
						   } 
						   idCount += usernum;//
					   %>
					   <!--角色-->
					   <tr>
							<td height='25' colspan="<%=operList.size()+3%>" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
							<a style="cursor:hand" onClick="addRoles()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">添加角色</div></a>
							</td>
						</tr>
					   <TR class="cms_report_tr">
							<td height='30' class="headercolor">
								<strong>全选</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>删除</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>角色</strong>
							</td>
							<%
								for(int num=0;num<operList.size();num++){
							%>
								<td class="headercolor" align="center">
									<strong><%=((com.frameworkset.platform.config.model.Operation)operList.get(num)).getName()%></strong>
								</td>
							<%}%>
						</TR>
							<%
						for(int aa=0;aa<roles.length;aa++)
						{
							if(roles[aa]==null||roles[aa].equals(""))
								continue;

							String roleid = roles[aa];

							hasOper = operManager.getOperResRoleList(roletypes[aa],roleid,resId2,"channeldoc");
							if(hasOper == null){
								hasOper = new ArrayList();
							}
						%>
						<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td align="center">
							<input type="checkbox" id="<%=usernum+aa%>" hideFocus onClick="checkAll(this,1,<%=usernum+rolenum+orgnum%>,<%=usernum+aa%>)"/>
						</td>
						<td align="center">
							<a href="#" onclick="del(<%=usernum+rolenum%>,<%=usernum+aa%>)"><u>删除</u></a>
						</td>
						<td align="center">
						<input type="hidden" name="roleno<%=aa%>" value="role"/>
						<input type="hidden" name="curuserid" value="<%=roles[aa]%>"/>
						<%=rolenames[aa]%>
						</td>
						
						<pg:list requestKey="operList" needClear="false">
							<td align="center">
							<%
								String rid = rowid;
								String opId = dataSet.getString(Integer.parseInt(rowid),"id");
								
							%>
							<input name="opId<%=idCount+aa%>" type="checkbox"
							<%
								for(int i = 0; i < hasOper.size(); i ++)
								{
									RoleresopKey id = (RoleresopKey)hasOper.get(i);
									if(id.getOpId().equals(opId) && id.getRoleId().equals(roles[aa]))
									{
										out.println("checked");
										break;
									}
										
								}
							%>
							value="<%=roleid%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=idCount+aa%>','<pg:cell colName="priority" defaultValue=""/>','<%=roles[aa]%>;<pg:cell colName="id"/>','<%=roletypes[aa]%>','channeldoc')">
							<input name="priority<%=idCount+aa%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</td>
						  </pg:list>
						  
					   </tr>
					   <%
						   } 
						   idCount += rolenum;//
					   %>
					   <!--/////机构/////-->
					   <tr>
							<td height='25' colspan="<%=operList.size()+3%>" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
							<a style="cursor:hand" onClick="addOrgs()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">添加机构</div></a>
							</td>
						</tr>
					   <TR class="cms_report_tr">
							<td height='30' class="headercolor">
								<strong>全选</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>删除</strong>
							</td>
							<td height='30' class="headercolor">
								<strong>机构</strong>
							</td>
							<%
								for(int num=0;num<operList.size();num++){
							%>
								<td class="headercolor" align="center">
									<strong><%=((com.frameworkset.platform.config.model.Operation)operList.get(num)).getName()%></strong>
								</td>
							<%}%>
						</TR>
							<%
						for(int aa=0;aa<orgs.length;aa++)
						{
							if(orgs[aa]==null||orgs[aa].equals(""))
								continue;

							String orgid = orgs[aa];

							hasOper = operManager.getOperResRoleList(orgtypes[aa],orgid,resId2,"channel");
							if(hasOper == null){
								hasOper = new ArrayList();
							}
						%>
						<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td align="center">
							<input type="checkbox" id="<%=orgnum+aa%>" hideFocus onClick="checkAll(this,1,<%=usernum+rolenum+orgnum%>,<%=usernum+rolenum+aa%>)"/>
						</td>
						<td align="center">
							<a href="#" onclick="del(<%=orgnum+rolenum%>,<%=orgnum+aa%>)"><u>删除</u></a>
						</td>
						<td align="center">
						<input type="hidden" name="orgno<%=aa%>" value="org"/>
						<input type="hidden" name="curorgid" value="<%=orgs[aa]%>"/>
						<%=orgnames[aa]%>
						</td>
						
						<pg:list requestKey="operList" needClear="false">
							<td align="center">
							<%
								String rid = rowid;
								String opId = dataSet.getString(Integer.parseInt(rowid),"id");
								
							%>
							<input name="opId<%=idCount+aa%>" type="checkbox"
							<%
								for(int i = 0; i < hasOper.size(); i ++)
								{
									RoleresopKey id = (RoleresopKey)hasOper.get(i);
									if(id.getOpId().equals(opId) && id.getRoleId().equals(orgs[aa]))
									{
										out.println("checked");
										break;
									}
										
								}
							%>
							value="<%=orgid%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=idCount+aa%>','<pg:cell colName="priority" defaultValue=""/>','<%=orgs[aa]%>;<pg:cell colName="id"/>','<%=orgtypes[aa]%>','channeldoc')">
							<input name="priority<%=idCount+aa%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</td>
						  </pg:list>
						  
					   </tr>
					   <%
						   } 
						   idCount += orgnum;//
					   %>
					  </table>
					</tab:tabPane>
				</tab:tabContainer>
			</FORM>
			<iframe name="saveop" width="0" height="0"></iframe>
		</div>
	</body>
</html>
