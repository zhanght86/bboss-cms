<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.ResourceAction"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<% 
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String orgId=request.getParameter("orgId");
	String intervalType=request.getParameter("intervalType");
	OrgManager orgManager =  SecurityDatabase.getOrgManager();
  	Organization org =orgManager.getOrgById(orgId);
    String resTypeId2 = "orgunit";					//资源类型id
	String resId2 = request.getParameter("orgId");	//资源id
	String title;	//资源名称
	if(org.getRemark5()==null){
		 title = org.getOrgName();	//资源名称
			
	}else{
		 title = org.getRemark5();	//资源名称
	}
	if(resId2 == null){
		resId2 = (String)request.getAttribute("resId2");
	}
	if(resTypeId2 == null){
		resTypeId2 = (String)request.getAttribute("resTypeId2");
	}
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();
	List operList = resManager.getOperations(resTypeId2);
	if (operList == null) {
		operList = new ArrayList();
	}
	request.setAttribute("operList", operList);
	
	String stored = (String)request.getAttribute("stored");
	if(stored == null){
		stored = "0";
	}
	String roleId = "";
	String rid = "";
%>
<html > 
      

<head>				
	<tab:tabConfig/>
	<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
    <script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
	<script language="javascript">
	
	function createorg()
	{
		getPropertiesContent().location.href="../user/userInfo1.jsp?orgId=<%=orgId%>";   
		
	}
	//--------------------------------------------------------------------------------------------------------------
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
			    

				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
				//checked
				if(currCheck.checked){
					send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion);
				}
				//not checked
				else{
					send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion);
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
			    

				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
				var opers = roleList.all("opId"+rid);
				//判断是否选中
				for(var i= 0;i<opers.length;i++){
					opid = opers[i].value;
					if(opers[i].checked){
						var tt = opers[i].checked;						
						send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion);
					}
					if(!opers[i].checked){
						var tt = opers[i].checked;						
						send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion);
					}
					
				}
			}
			
			function changebox2(opid,checked){
				var roleList = document.roleList;
				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
				send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked="+checked+"&title=<%=title%>");
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
            function querytype(e){
                var intervalType = e.value;
	            userForm.action="orgsubuserlist_tab.jsp?orgId=<%=orgId%>&intervalType="+intervalType;
	            userForm.submit();
            }
	</script>
	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
</head>
<body class="contentbodymargin">
<div id="contentborder">
<tab:tabContainer id="foo-bar-container" selectedTabPaneId="org-tab">
		<tab:tabPane id="org-tab" tabTitle="机构用户">
		<select name="intervalType" class="select"  onchange="querytype(this)"> 
   			<option value="0" >不递归机构显示</option>          						
   			<option value="1" >递归机构显示</option>								
		</select>
		<FORM name="userForm" method="post" action="">			    
		   	<INPUT name="orgId" value="<%=orgId%>" type="hidden">
			<TABLE width="98%" border="0" cellpadding="0" cellspacing="1" class="thin">
			<TR><TD class="detailtitle" align="center" colspan="8"><B>用户列表</B></TD></TR>
				<pg:listdata dataInfo="OrgSubUserList" keyName="OrgSubUserList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="5" id="OrgSubUserList" scope="request" data="OrgSubUserList" isList="false">
				<pg:param name="intervalType"/>
						  <!--检测当前页面是否有记录-->					      			    
					      <!--list标签循环输出每条记录-->			      
					      <TR class="labeltable_middle_td">
					      <!--设置分页表头-->
										      
					      	<td class="headercolor">登录名</td>	
					      	<INPUT class="text" type="hidden" name="selectId">
					      	
					      	<td class="headercolor">用户名</td>
					      	<td class="headercolor" colspan="2">性别</td>
					      	<!-- <td class="headercolor">所属岗位</td> -->
							<td class="headercolor">所在机构以及岗位</td>	
					      	
					      </TR><pg:param name="orgId" /><pg:notify>
					      <TR height="18px">
					      	<TD class="detailcontent" colspan="100" align="center">暂时没有用户</TD>
					      </TR>
					      </pg:notify><pg:list>	
					      		<TR class="labeltable_middle_tr_01" onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	      				
										
					      	<!-- 	<td class="tablecells" align=center><input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="userId" defaultValue=""/>"></td>-->					
									<TD class="tablecells" align="left"><pg:cell colName="userName" defaultValue="" /></TD>
									<TD class="tablecells" align="left"><pg:cell colName="userRealname" defaultValue="" /></TD>																											    					    							    				    		
							  		<TD class="tablecells" align="left" colspan="2">
							  		<pg:equal colName="userSex" value="F">女</pg:equal>
							  		 <pg:equal colName="userSex" value="M">男</pg:equal>																										    					    							    				    		
							  		 <pg:equal colName="userSex" value="-1">未知</pg:equal>
							  		</TD>	
							  		<!-- <TD class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobName" defaultValue="" />
									</TD>						-->			
									<TD class="tablecells">
										<pg:cell colName="orgName" defaultValue="" />
									</TD>							  		
							  	</TR>			      		
					      </pg:list>
					   <TR height="18px">
					      	<TD class="detailcontent" colspan="4" align="center" nowrap="nowrap">
					      
					      	<pg:index /><INPUT type="hidden" name="queryString" value="<pg:querystring/>"></TD>
					      	<TD class="detailcontent" align="left">
					      	<%
					      	if(accesscontroler.checkPermission(orgId,
	                                    "adduser", AccessControl.ORGUNIT_RESOURCE ))
					      	{
					      	%>
						    	<INPUT type="button" value="新增用户" class="input" onclick="createorg()">		
						    <%}%>				    	
						  </TD>
					   </TR>   			   	      
				</pg:pager>
				
		   </TABLE>	
		</FORM>	
	</tab:tabPane>
	</tab:tabContainer>
</div>
</body>
    <!-- ---------------------------------------------------------------------------------------------------------- -->
	
	
	<!-- ------------------------------------------------------------------------------------------------------------- -->
	
	
<!-- 	 ---------------------------注释于 2007.9.29 chunqiu.zhao  ------------------------------------ --> 
	<!--<tab:tabPane id="res_tab" tabTitle="机构授权情况">
		<input name="orgId" value="<%=orgId%>" type="hidden">
		<fieldset>
	   <LEGEND align=left><strong><FONT size=2>帮助</FONT></strong></LEGEND>
		<%
	          boolean flag;
	          AccessControl accesscon = AccessControl.getInstance();
	          flag = accesscon.allowIfNoRequiredRoles(resTypeId2);
	          //System.out.println("hehelalal" + flag);
	          if(flag == true)
	          {
	    %>
	    <table><tr><td>该资源在没有授予任何角色或用户（即下面的复选框都处于未被选中状态）的情况下,允许访问该资源，一旦授给某个用户或角色后，其它角色或用户也必须授权后才能访问
	      </td>
	     </tr>
	    </table>
	    <%
        }else{
        %>
	    <table align=left><tr><td>必须对该资源授权才可以访问该资源，在没有授权的情况下是不能访问该资源</td></tr></table>
        <%}%>
        </fieldset>
	    <FORM name="roleList" method="post" >
				
				分页显示开始,分页标签初始化
				<pg:listdata dataInfo="RoleList" keyName="RoleList" /><pg:pager id="RoleList" maxPageItems="5"
				  scope="request"  
				  data="RoleList" 
				  isList="false">
				  <pg:param name="resTypeId2"/>
				  <pg:param name="resId2"/>
				  <pg:param name="orgId"/>
				  <pg:param name="title"/>
				  
			<table cellspacing="1" cellpadding="0" border="0" width=98% class="thin">
				 <input name="resTypeId" type="hidden" value="<%=resTypeId2%>">
				 <input name="resId" type="hidden" value="<%=resId2%>">
				 <input name="title" type="hidden" value="<%=title%>">
				<TR ><td colspan="3" align="center"><span class="STYLE1"><%=title%></span>&nbsp;作为资源授给角色列表(直接选择复选框即可，系统实时保存)</td>
				</TR>

				<TR>
				<td height='20' class="headercolor">角色名称</td>
				<td height='20' class="headercolor">操作列表</td>
				<td height='20' class="headercolor">是否递归</td>
				</TR>
				<pg:list>
					<%
						boolean flag1;
						//判断给定的角色是否是超级管理员角色
						flag1 = accesscon.isAdministratorRole(dataSet.getString("roleName"));
						if(flag1==true)	
						{				
					%>
						<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
						<td align="left">
							<pg:cell colName="roleName"/>
							<%
								
								rid = rowid;
								roleId = dataSet.getString("roleId");	
								List hasOper = operManager.getOperResRoleList(roleId,resId2,resTypeId2);							
							%>
						</td>
						<td align="center">
							<pg:list requestKey="operList" needClear="false" declare="false">
								<input name="opId<%=rid%>" type="checkbox"
								<%
						        	String opId = dataSet.getString("id");
						        	
						        	
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		
						        		//System.out.println(i+":::opId2="+op.getId().getOpId());
						        		//System.out.println(i+":::rId="+op.getId().getRoleId());
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
								递归授权子机构
						</td>
						
					</TR>
					<%
			        }else{
			        	boolean flag2;
			        	//判断资源是否是未受保护的资源
						flag2 = accesscon.isUnprotected(resId2,resTypeId2);
						boolean flag4;
			        	//判断资源所有操作是否只有超级管理员才能访问
						flag4 = accesscon.isExcluded(resId2,resTypeId2);
						if(flag2==true){
					%>
			    		<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
						<td align="left">
							<pg:cell colName="roleName"/>
							<%
								
								rid = rowid;
								roleId = dataSet.getString("roleId");	
								List hasOper = operManager.getOperResRoleList(roleId,resId2,resTypeId2);							
							%>
						</td>
						<td align="center">
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

								value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')"  checked disabled="true">

								&gt; <pg:cell colName="name" />
								<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</pg:list>
						</td>
						<td>
							<input name="isRecursion" onclick="isRecursionClick(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>')" type="checkbox"  checked disabled="true">
								递归授权子机构
						</td>
						
					</TR>
			    	<%
			       	} else if(accesscon.isExcluded(resId2,resTypeId2)==true){%>
			       			<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
						<td align="left">
							<pg:cell colName="roleName"/>
							<%
								
								rid = rowid;
								roleId = dataSet.getString("roleId");	
								List hasOper = operManager.getOperResRoleList(roleId,resId2,resTypeId2);							
							%>
						</td>
						<td align="center">
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

								value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')"   disabled="true">

								&gt; <pg:cell colName="name" />
								<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							</pg:list>
						</td>
						<td>
							<input name="isRecursion" onclick="isRecursionClick(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>')" type="checkbox"   disabled="true">
								递归授权子机构
						</td>
						
					</TR>
			       	  <%}else{%>
			       	  		<TR onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
						<td align="left">
							<pg:cell colName="roleName"/>
							<%
								
								rid = rowid;
								roleId = dataSet.getString("roleId");	
								List hasOper = operManager.getOperResRoleList(roleId,resId2,resTypeId2);							
							%>
						</td>
						<td align="center">
							<pg:list requestKey="operList" needClear="false" declare="false">
							<%
								boolean flag3;
					        	//判断资源是否是未受保护的资源
								flag3 = accesscon.isUnprotected(resId2, dataSet.getString("id"),resTypeId2);
								if(flag3==true){
								
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

								value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')"   checked disabled="true">

								&gt; <pg:cell colName="name" />
								<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
							<%}else if(accesscon.isExcluded(resId2, dataSet.getString("id"),resTypeId2)==true){%>
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

								value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')"    disabled="true">

								&gt; <pg:cell colName="name" />
								<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
								
							<%}else{%>
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
								<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
								
							<%}%>
							</pg:list>
							
						</td>
						<td>
							<input name="isRecursion" onclick="isRecursionClick(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>')" type="checkbox">
								递归授权子机构
						</td>
						
					</TR>
					 <%}
			       	
					 
					 }%>
				</pg:list>
			
				<tr><td colspan="3" align="right">共<pg:rowcount/>条记录 <pg:index/></td></tr>
			</table>
			</pg:pager>
		</FORM>
	
		
	</tab:tabPane> 
-->

<script language="JavaScript">
    var intervalType = "<%=intervalType%>";
    var ops = document.all("intervalType");
    for(var i=0;i<ops.options.length;i++){
        if(ops.options[i].value==intervalType) {
          ops.options[i].selected=true;
          break;
        }
    }
</script>
</html>

