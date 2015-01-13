<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="java.util.*, com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Operation,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	String role_type = (String)session.getAttribute("role_type");
	//System.out.print("role_type........."+role_type);
	String resTypeId = "user";
	String jobid ="";
	String resId = request.getParameter("resId");	//这个是orgid+":"+jobId
	String[] tmp = resId.split(":");				//获得jobId
	if(tmp.length == 2){
		 jobid = tmp[1];
	}
	String orgId = request.getParameter("orgId");	
	//根据机构和岗位取用户列表
	UserManager userManager = SecurityDatabase.getUserManager();

	
	String roleId = (String)session.getAttribute("currRoleId");//角色Id
	//System.out.print("//roleId........."+roleId);
	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resId == null){
		resId = (String)request.getAttribute("resId");
	}
	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List list = resManager.getOperations(resTypeId);
	

	
	if(list == null){
		list = new ArrayList();
	}	
	request.setAttribute("operList",list);	//操作列表
	
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	String uid="";
	String userId="";
%>


<html>
	<head>
		<title>系统管理</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
		<SCRIPT language="javascript">
			var resTypeId ="user";
			var resId ="<%=resId%>";
			var orgId="<%=orgId%>";
			var roleId="<%=roleId%>";
			var role_type ="<%=role_type%>";
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
			function changebox1(currCheck,uid,priority,opid){
				if(currCheck.checked){				
					send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveURRO.jsp?resId='+resId+'&orgId='+orgId+'&opId='+opid+'&roleId='+roleId+"&flag=1&role_type="+role_type);
				}
				else{
					send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveURRO.jsp?resId='+resId+'&orgId='+orgId+'&opId='+opid+'&roleId='+roleId+"&flag=0&role_type="+role_type);
				
				}
				setCheck(currCheck,uid,priority);
			}
			
			function changebox(opid,flag){
				//alert(opid);						
				send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveURRO.jsp?resId='+resId+'&orgId='+orgId+'&opId='+opid+'&roleId='+roleId+"&flag="+flag+"&role_type="+role_type);
			}
			//--------------------------------------------------------
			function setCheck(currCheck,uid,priority)
			{
			   	//当前点击的checkbox名称
			   	var o = document.getElementsByName(currCheck.name);
			   	//当前checkbox对应的优先级的列表
				var prioritylist = document.getElementsByName("priority"+uid); 
				if (currCheck.checked==true && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
			
						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1)&& priority.substring(1,2) == v.substring(1,2) )
						{
							o[i].checked=true;
							
							
							changebox(o[i].value,1);
							//o[i].disabled=true;
						}
					}  
				
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(1,2) != v.substring(1,2) )
						{
							o[i].checked=false;
							
							changebox(o[i].value,0);
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

			
		</SCRIPT>
		
	    <style type="text/css">
<!--
.STYLE1 {color: #0000FF}
-->
        </style>
</head>
	<body>
		<FORM name="roleList" method="post" >
			<table cellspacing="0" cellpadding="0" border="1" bordercolor="#EEEEEE" width=98%>
				<TR ><td colspan="2" align="center"><span class="STYLE1">用户及操作权限列表(直接选择复选框即可，系统实时保存)</span></td></TR>
					<pg:listdata dataInfo="OrgJobUserList" keyName="userlist"/>
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="10"
							  scope="request"  
							  data="userlist" 
							  isList="false">
				    <pg:param name="resId"/>
				    <pg:param name="orgId"/>
				
				<TR>
					<th height='20' class="headercolor">用户名称</th>
					<th height='20' class="headercolor">操作列表</th>
				</TR>
				<pg:list>				
					<TR onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" >
						<td align="center" width="30%">
							<pg:cell colName="userName"/>
						
						</td>
						<td align="center">
							<pg:list requestKey="operList" declare="false">
								<input name="opId<pg:rowid index="0"/>" type="checkbox" 
								<%
						        	String opId = dataSet.getString("id");
						        	//用户资源标识
						        	String resId2 = resId+":"+dataSet.getString("userId",0);
					        		List hasOper = operManager.getOperResRoleList(role_type,roleId,resId2,resTypeId);
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		RoleresopKey id = (RoleresopKey)hasOper.get(i);
						        		if(id.getOpId().equals(opId) && id.getRoleId().equals(roleId))
						        		{
						        			out.println("checked");
						        			break;
						        		}
						        	}
						        %>						
								value="<pg:cell index="0" colName="userId"/>;<pg:cell colName="id"/>;<pg:cell index="0" colName="userName"/>" onclick="changebox1(this,'<pg:rowid index="0"/>','<pg:cell colName="priority" defaultValue=""/>','<pg:cell index="0" colName="userId"/>;<pg:cell colName="id"/>;<pg:cell index="0" colName="userName"/>')">
								<pg:cell colName="name" />
								<input name="priority<pg:rowid index="0"/>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">  
							</pg:list>
						</td>
					</TR>
				</pg:list>
				<tr><td colspan="2" align="center">
				<input name="resTypeId" type="hidden" value="<%=resTypeId%>">
				<input name="resId" type="hidden" value="<%=resId%>">
				</td></tr>
				<tr><td colspan="2" align="right"><pg:index/></td></tr>
				</pg:pager>
			</table>
		</FORM>
	</body>
</html>

