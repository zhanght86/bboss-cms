<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,java.util.*" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,
			com.frameworkset.platform.sysmgrcore.manager.RoleManager,
			com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,
			com.frameworkset.platform.sysmgrcore.entity.Role,
			com.frameworkset.platform.security.AccessControl,
			com.frameworkset.common.poolman.DBUtil" %>
<html>
<head>    
    <title>属性容器</title>
    <script language="JavaScript" src="../../../sysmanager/scripts/ajax.js" type="text/javascript"></script> 
<%	
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkManagerAccess(request,response);
    
	String[] id = request.getParameterValues("checkBoxOne");
	
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
				AccessControl.WRITE_PERMISSION,
				AccessControl.ROLE_RESOURCE)
				|| accessControl.checkPermission(role.getRoleId(),
						AccessControl.READ_PERMISSION,
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
     DBUtil db = new DBUtil();
     //是否可以授角色的 标示
     boolean tag = false;
     
     boolean tagSelf = false;
     String adminUsers = "以下用户不能进行角色授予:\\n";
     
     for(int j=0;j<id.length; j++)
     {
     	//本身是不是有管理员角色
     	String sqlUser ="select count(*) from td_sm_userrole where user_id ="+ id[j] +" and role_id='1'";
		db.executeSelect(sqlUser);
		User adminUser = userManager.getUserById(id[j]);
		
		if(db.getInt(0,0)>0){//有管理员角色		    
			tag = true;						
			adminUsers += adminUser.getUserName() + " 是超级管理员\\n";
		}
		
		//没有管理员角色, 但是给自己授权
		if(accessControl.getUserID().equals(id[j])){
			tag = true;
			adminUsers += adminUser.getUserName() + " 不能给自己授予角色\\n";
		}
		
		 //是部门管理员, 也不允许授权
	     List managerOrgs = orgImpl.getUserManageOrgs(id[j]);
	     if(managerOrgs.size()>0){
	         tag = true;
		     adminUsers += adminUser.getUserName() + " 是部门管理员\\n";
	     }
		
     }
     if(tag){
%>
     	<SCRIPT LANGUAGE="JavaScript">
     	var promptStr = "<%=String.valueOf(adminUsers)%>";
		alert(promptStr); 
		getPropertiesContent().location.href="../user/userManager.do?method=getUserList&orgId=<%=orgId%>";
		</SCRIPT>
<%
     }
   
%>
 
<SCRIPT LANGUAGE="JavaScript">
	var id = "<%=idStr%>";   
	function addRole(){	
	   var n=document.all("roleId").options.length-1;
	   var ch = false; 	
	   for(var i=0;i<document.all("allist").options.length;i++){
	   var op=document.all("allist").options[i];
	   if(op.selected)
	   			addone(op.text,op.value,n);
	   			ch = true;
	  	}
	  	if(!ch )
	  	{
	  		alert("<pg:message code='sany.pdp.to.choose.roles'/>");
	  		return;
	  	}
	   changebox();  
}
function addone(name,value,n){
   for(var i=n;i>=0;i--){
		if(value==document.all("roleId").options[i].value){
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
		alert("<pg:message code='sany.pdp.no.roles.delete'/>");
	}
	var roleDelId=new Array(length);
	for (var m=length-1;m>=0;m--)
	{
	      roleDelId[i++]=document.all("roleId").options[m].value;
          document.all("roleId").options[m]=null;
    } 
    send_request('delUserRoleAlot.jsp?roleDelId='+roleDelId+'&id='+id); 
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
	var roleDelId=new Array();
	for (var m=leng-1;m>=0;m--){
	    if(document.all("roleId").options[m].selected)
	    {
	    roleDelId[i++]=document.all("roleId").options[m].value;
        document.all("roleId").options[m]=null;
        }
 	}
 	send_request('delUserRoleAlot.jsp?roleDelId='+roleDelId+'&id='+id);
}


function changebox(){				 
	var len=document.all("roleId").options.length;			  	 	
    var roleId=new Array(len);
    for (var i=0;i<len;i++){	      
        roleId[i]=document.all("roleId").options[i].value;
    }    		
	send_request('saveUserRoleAlot.jsp?roleId='+roleId+'&id='+id);
}
</SCRIPT>
 <%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<body class="contentbodymargin" scroll="no">
<div id="">
<center>
<form name="AlotUserRoleForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
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
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall()">
        </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall()">
        </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleterole()">
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
    <td align="center">&nbsp;</td>	
     <td align="center">&nbsp;</td>						
     <td>
		<div align="center">
			<a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
		</div>
	</td>							
  </tr>

 </table>
</form>
</center>
</div>
</body>
</html>

