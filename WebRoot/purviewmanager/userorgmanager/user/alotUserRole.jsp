<%
/*
 * <p>Title: 角色授权处理页面 </p>
 * <p>Description: 批量角色授权处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<html>
<head>    
    <title>属性容器</title>
<%	
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkManagerAccess(request,response);
    
	String checks = request.getParameter("checks");
	String[] id = checks.split(",");
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	String sql = "";
	if("1".equals(accessControl.getUserID())){
		sql = "select * from td_sm_role where role_id not in('2','3','4') order by role_name";
	}else{
		sql = "select * from td_sm_role where role_id not in('1','2','3','4') order by role_name";
	}
    List list = roleManager.getRoleList(sql);
	List allRole = null;
	// 角色列表加权限
	for (int i = 0; list != null && i < list.size(); i++) {
		Role role = (Role) list.get(i);
		if (accessControl.checkPermission(role.getRoleId(),
				"roleset",
				AccessControl.ROLE_RESOURCE)) {
			if (allRole == null)
				allRole = new ArrayList();
			allRole.add(role);
		}
	}
    request.setAttribute("allRole", allRole);

    UserManager userManager = SecurityDatabase.getUserManager();
	OrgManagerImpl orgImpl = new OrgManagerImpl();
	
     String idStr = "";
     String usern = "";
     String userna = "";
     for(int i = 0;i < id.length; i++)
     {
     	idStr += id[i]+",";
     	User user = userManager.getUserById(id[i]);
     	usern= user.getUserRealname();
     	//System.out.println(usern);
     	userna += usern + ",";
     }
     if(idStr.length() > 1)
     idStr = idStr.substring(0,idStr.length()-1);
     if(userna.length() > 1)
     userna = userna.substring(0,userna.length()-1);
     
     //管理员角色与自己不能授予角色
     String orgId = request.getParameter("orgId");
     //当前用户所属机构ID
     String curUserOrgId = accessControl.getChargeOrgId();
     DBUtil db = new DBUtil();
     //是否可以授角色的 标示
     boolean tag = false;
     
     boolean tagSelf = false;
     String adminUsers = "以下用户不能进行角色授予:\\n";
     
     for(int j=0;j<id.length; j++)
     {
     	//本身是不是有管理员角色
     	//String sqlUser ="select count(*) from td_sm_userrole where user_id ="+ id[j] +" and role_id='1'";
		//db.executeSelect(sqlUser);
		User adminUser = userManager.getUserById(id[j]);
		if(accessControl.isAdminByUserid(id[j])){//有管理员角色		    
			tag = true;						
			adminUsers += adminUser.getUserName() + " 是超级管理员\\n";
		}
		
		//没有管理员角色, 但是给自己授权
		if(accessControl.getUserID().equals(id[j])){
			tag = true;
			adminUsers += adminUser.getUserName() + " 不能给自己授予角色\\n";
		}
		
		 //是部门管理员, 也不允许授权
	     // 允许部门管理员批量设置角色  baowen.liu 2008-3-21
	      List managerOrgs = orgImpl.getUserManageOrgs(id[j]);
	      if(managerOrgs.size()>0 && curUserOrgId.equals(orgId) && !accessControl.isAdmin()){
	         tag = true;
		     adminUsers += adminUser.getUserName() + " 与当前用户是同等级部门管理员\\n";
	     }

     }
     if(tag){
%>
     	<SCRIPT LANGUAGE="JavaScript">
     	
     	parent.$.dialog.alert("<%=String.valueOf(adminUsers)%>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		</SCRIPT>
<%
     }
   
%>
 
<SCRIPT LANGUAGE="JavaScript">
	var api = frameElement.api, W = api.opener;
	var id = "<%=idStr%>";   
	function addRole()
	{	
	   var n=document.all("roleId").options.length-1;
	   var ch = false; 	
	   var size = 0;
	   for(var i=0;i<document.all("allist").options.length;i++)
	   {
		   var op=document.all("allist").options[i];
		   if(op.selected)
		   {
		   		addone(op.text,op.value,i);
		   		size ++ ;
		   }
		   ch = true;
	   }
	  if(!ch )
	  {
	  		W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.roles'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  	   return;
	  }
	  	
	  if(size != 0)
	  {
	   	changebox();  
	  }
	  else
	  {
	  	W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.roles'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  }
	}
	
function addone(name,value,n){
   for(var i=n;i>=0;i--){
		if(document.all("roleId").options[i]&&value==document.all("roleId").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("roleId").add(op);
}
function deleteall(){
	var length=document.all("roleId").options.length;
	var i = 0;
	if(length == 0)
	{
		W.$.dialog.alert("<pg:message code='sany.pdp.no.roles.delete'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		return false;
	}
	var roleDelId=new Array(length);
	for (var m=length-1;m>=0;m--)
	{
	      roleDelId[i++]=document.all("roleId").options[m].value;
          document.all("roleId").options[m]=null;
    } 
    send_request('delUserRoleAlot.jsp?orgId=<%=orgId%>&roleDelId='+roleDelId+'&id='+id); 
}
      
function addall(){
		var n=document.all("roleId").options.length-1;
		var p=document.all("allist").options.length-1;
		if(p <= 0)
		{
			
		}	  
	     for(var i=0;i<document.all("allist").options.length;i++){
	     var op=document.all("allist").options[i];
	     addone(op.text,op.value,n);  
	     
	   }
	    changebox();  
}
function deleterole(){
	var leng=document.all("roleId").options.length;
	var i = 0;
	var size = 0;
	var roleDelId=new Array();
	for (var m=leng-1;m>=0;m--){
	    if(document.all("roleId").options[m].selected)
	    {
	    roleDelId[i++]=document.all("roleId").options[m].value;
        document.all("roleId").options[m]=null;
        size ++;
        }
 	}
 	if(size > 0){
	 	send_request('delUserRoleAlot.jsp?orgId=<%=orgId%>&roleDelId='+roleDelId+'&id='+id);
	 }else{
	 	parent.$.dialog.alert("<pg:message code='sany.pdp.to.choose.roles'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	 }
}


function changebox(){				 
	var len=document.all("roleId").options.length;			  	 	
    var roleId=new Array(len);
    for (var i=0;i<len;i++){	      
        roleId[i]=document.all("roleId").options[i].value;
    }    		
	send_request('saveUserRoleAlot.jsp?orgId=<%=orgId%>&roleId='+roleId+'&id='+id);
}

function send_request(url){
	http_request = false;
	if(window.XMLHttpRequest){//Mozilla
			http_request = new XMLHttpRequest();
			if(http_request.overrideMimeType){//??MIME??
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
				return false;
			}
			http_request.onreadystatechange = processRequest;
			http_request.open("GET",url,true);
			http_request.send(null);
			}
			
function processRequest(){
	if(http_request.readyState == 4){				
		if(http_request.status == 200){		
			var strflag = http_request.responseText;
			if(strflag.indexOf("true") != -1){
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			else{
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.failed'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
		}
		else{
			W.$.dialog.alert("<pg:message code='sany.pdp.server.error'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		}
		//document.all("button1").disabled = false;
		//document.all("button2").disabled = false;
		//document.all("button3").disabled = false;
		//document.all("button4").disabled = false;
		//document.all("button32").disabled = false;
		//divProcessing.style.display="none";
		
	}else{
		//document.all("button1").disabled = true;
		///document.all("button2").disabled = true;
		//document.all("button3").disabled = true;
		//document.all("button4").disabled = true;
		//document.all("button32").disabled = true;
		//divProcessing.style.display="";
		
	}
	
}
</SCRIPT>
   <%@ include file="/common/jsp/css-lhgdialog.jsp"%>
</head>
<body class="contentbodymargin" scroll="no">
	<div id="">
	<center>
		<form name="AlotUserRoleForm" action="" method="post" >
			<table width="80%" border="0" cellpadding="0" cellspacing="1">
				<tr class="tabletop">
				    <td colspan align="center">&nbsp;</td>
				</tr>
				<tr class="tabletop">
				    <td width="40%" align="center">&nbsp;</td>
				    <td width="20%" align="center">&nbsp;</td>
				    <td width="40%" align="center">&nbsp;</td>
				  </tr>
				  <tr class="tabletop">
				    <td width="40%" align="center"><pg:message code="sany.pdp.choose.role"/></td>
				    <td width="20%" align="center">&nbsp;</td>
				    <td width="40%" align="center"><pg:message code="sany.pdp.exist.role"/></td>
				  </tr>
				  
				  <tr >
				     <td  align="right" >
				     <select name="allist"  multiple style="width:100%" onDBLclick="addRole()" size="15">
				     
								  <pg:list requestKey="allRole">
									<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
								  </pg:list>			
					</select>
					</td>				  
						  	
				    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
				      <tr>
				        <td align="center"><a class="bt_2"  onclick="addRole()"><span>&gt;</span></a>              
				        </td>
				      </tr>
				      <tr>
				        <td align="center">&nbsp;</td>
				      </tr>
				      <tr>
				        <td align="center"><a class="bt_2"  onclick="addall()"><span>&gt;&gt;</span></a>
				        </td>
				      </tr>
				      <tr>
				        <td align="center">&nbsp;</td>
				      </tr>
				      <tr>
				        <td align="center"><a class="bt_2"  onclick="deleteall()"><span>&lt;&lt;</span></a>
				        </td>
				      </tr>
				      <tr>
				        <td align="center">&nbsp;</td>
				      </tr>
				      <tr>
				        <td align="center"><a class="bt_2" onclick="deleterole()"><span> &lt;</span></a>
				        </td>
				      </tr>
				      <tr>
				        <td align="center">&nbsp;</td>
				      </tr>
				    </table></td>
				    <td >
				     <select name="roleId"  multiple style="width:100%" onDBLclick="deleterole()" size="15">
					 </select>
					 	 						
					</td>				 
					<tr>
				        <td align="center">&nbsp;</td>
				      </tr>
				      		  
				  <tr>		
				     <td colspan="3">
						<div align="center">
							<a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
						</div>
					</td>							
				  </tr>
			 </table>
		</form>
	</center>
	</div>
	<br/>
	<div id=divProcessing style="width:200px;height:30px;position:absolute;left:300px;top:350px;display:none">
		<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
			<tr>
				<td bgcolor=#3A6EA5>
					<marquee align="middle" behavior="direction" scrollamount="5">
						<font color=#FFFFFF><pg:message code="sany.pdp.common.operation.processing"/></font>
					</marquee>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>

