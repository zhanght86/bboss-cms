<%
/*
 * <p>Title: 岗位授予页面</p>
 * <p>Description: 岗位授予页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
 %>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl"%>


<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%	
	 AccessControl accessControl = AccessControl.getInstance();
     accessControl.checkManagerAccess(request,response);
     String checks = request.getParameter("checks");
     String[] id =checks.split(",");
     String orgId = request.getParameter("orgId");
     //当前用户所属机构ID
     //String curUserOrgId = accessControl.getChargeOrgId(); 
     UserManager userManager = SecurityDatabase.getUserManager();
     OrgManager orgManager = SecurityDatabase.getOrgManager();
     JobManager jobManager = SecurityDatabase.getJobManager();
     Organization org = orgManager.getOrgById(orgId);
     
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
     //System.out.println(userna);
     List allJob = jobManager.getJobList(org);
     request.setAttribute("existJob", null);
     //List allJob = (List)request.getAttribute("allJob"); 
     
     //权限过滤， 能进行岗位授予的岗位必须是：当前登录用户（包括部门管理员）必须对这些岗位有机构设置的权限。
     List myAllJob = new ArrayList();
     if(allJob != null)
     {
    	for(int i=0;i<allJob.size();i++)
    	{
    		Job job = (Job)allJob.get(i);
    		if(accessControl.checkPermission(job.getJobId(), "jobset", AccessControl.JOB_RESOURCE))
    		{
    			myAllJob.add(job);
    		}
    	}
     }
 	 request.setAttribute("myAllJob", myAllJob);
 	
 	 //管理员与自己不能授予角色
 	 /*
     DBUtil db = new DBUtil();
    
     boolean tag = false;
     boolean tagSelf = false;
     String adminUsers = "以下用户不能进行岗位授予:\\n";
     for(int j=0;j<id.length; j++){
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
		 // 允许部门管理员批量设置岗位  baowen.liu 2008-3-21
	     boolean managerOrgs = orgImpl.isCurOrgManager(orgId, id[j]);
	      if(managerOrgs && curUserOrgId.equals(orgId) && !accessControl.isAdmin()){
	         tag = true;
		     adminUsers += adminUser.getUserName() + " 与当前用户是同等级部门管理员\\n";
	     }
     }
     */
%>
<html>
<head>    
<title>属性容器</title>

		<SCRIPT LANGUAGE="JavaScript">
			var api = parent.frameElement.api, W = api.opener;
				var id = "<%=idStr%>";   
				
				function addRole(){	
					   var n=document.all("roleId").options.length-1;
					   	 	
					   for(var i=0;i<document.all("allist").options.length;i++)
					   {
						   var op=document.all("allist").options[i];
						   if(op.selected)
						   addone(op.text,op.value,n);
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
					var orgId = <%=orgId%>;
					var roleDelId=new Array(length);
								
					for (var m=length-1;m>=0;m--)
					{
					      roleDelId[i++]=document.all("roleId").options[m].value;
				          document.all("roleId").options[m]=null;
				    } 
				    
				    if(i >0)
				    {
				  		send_request('delUserJobAlot.jsp?roleDelId='+roleDelId+'&id='+id+'&orgId='+orgId);    
				  	}
				  	else
				  	{
				  		return ;
				  	}
					
				}
				      
				function addall(flag){
						var n=document.all("roleId").options.length-1;
						var p=document.all("allist").options.length-1;		  
					     for(var i=0;i<document.all("allist").options.length;i++){
					     var op=document.all("allist").options[i];
					     addone(op.text,op.value,n);  
					   }
					    changebox();  
				}
				
				function deleterole(){
					var leng=document.all("roleId").options.length;
					var i = 0;
					var orgId = <%=orgId%>;
					var roleDelId=new Array();
						for (var m=leng-1;m>=0;m--){
						    if(document.all("roleId").options[m].selected)
						    {
						    roleDelId[i++]=document.all("roleId").options[m].value;
					        document.all("roleId").options[m]=null;
					        }
					 	}
					 	//alert("liumeiyu" + roleDelId[0]);
					 	if(i > 0)
					 	{
					 		send_request('delUserJobAlot.jsp?roleDelId='+roleDelId+'&id='+id+'&orgId='+orgId); 
					 	}
					 	else
					 	{
					 		W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					 		return ;
					 	}
				}
				
				
				function changebox(){				 
					var len=document.all("roleId").options.length;			  	 	
				    var roleId=new Array(len);
				    var orgId = <%=orgId%>;
				    for (var i=0;i<len;i++){	      
				        roleId[i]=document.all("roleId").options[i].value;
				    }  
				    if(len > 0)
				    {      		
						send_request('saveUserJobAlot.jsp?roleId='+roleId+'&id='+id+'&orgId='+orgId);
					}
					else
					{
						W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						return ;
					}
				}
				
				function imp()
				{
					var orgId = "<%=orgId%>";
					var url = "../purviewmanager/userorgmanager/user/lusersys_ajax.jsp?orgId=" + orgId;  
					$.dialog({title:'<pg:message code="sany.pdp.sys.post.authorize"/>',width:760,height:560, content:'url:'+url,lock: true});
					//win = window.showModalDialog(url,window,"dialogWidth:"+560+"px;dialogHeight:"+480+"px;help:no;scroll:auto;status:no");
					if(win == "ok"){
						  window.location.href = "allJob_ajax.jsp?checks=<%=checks%>&orgId=<%=orgId%>";
					}
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
							W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						}
						else{
							W.$.dialog.alert("<pg:message code='sany.pdp.server.error'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						}
						//document.all("button1").disabled = false;
						//document.all("button2").disabled = false;
						//document.all("button3").disabled = false;
						//document.all("button4").disabled = false;
						
						//document.all("button31").disabled = false;
						//document.all("button32").disabled = false;
						
						//divProcessing.style.display="none";
						
					}else{
						//document.all("button1").disabled = true;
						//document.all("button2").disabled = true;
						//document.all("button3").disabled = true;
						//document.all("button4").disabled = true;
						
						//document.all("button31").disabled = true;
						//document.all("button32").disabled = true;
						
						//divProcessing.style.display="";
						
					}	
				}
		</SCRIPT>
</head>
<body>
<div>
<form name="AlotUserRoleForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center"><pg:message code="sany.pdp.choose.job"/></td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center"><pg:message code="sany.pdp.exist.job"/></td>
  </tr>
  
  <tr>
     <td  align="right" >
	     <select name="allist"  multiple style="width:80%" 
	     
	                 onDBLclick="addRole(1)" size="15">
	     
					  <pg:list requestKey="myAllJob">
						<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
					  </pg:list>			
		</select>
	</td>				  
		  	
    <td align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
				        <td align="center"><a class="bt_2" onclick="addRole()"><span>&gt;</span></a>              
				        </td>
			      </tr>
			      
			      <tr>
		       			<td align="center">&nbsp;</td>
			      </tr>
			      
			      <tr>
			       		 <td align="center"><a class="bt_2"  
						 onclick="addall()"><span>&gt;&gt;</span></a>  
						 </td>
			      </tr>
			      
			      <tr>
			        	<td align="center">&nbsp;</td>
			      </tr>
			      <tr>
			        	<td align="center"><a class="bt_2"  onclick="deleteall()"><span>&lt;&lt;</span></a></td>
			      </tr>
			      
			      <tr>
			        	<td align="center">&nbsp;</td>
			      </tr>
			      
			      <tr>
			        	<td align="center"><a class="bt_2"  onclick="deleterole()"><span>&lt;</span></a>  
						</td>
			  	  </tr>
		      
			      <tr>
			        	<td align="center">&nbsp;</td>
			      </tr>
		</table>
	</td>
    
    
    <td >
     <select name="roleId"  multiple style="width:80%" onDBLclick="deleterole()"  size="15">
	 </select>
	 	 						
	</td>	
				 
	<tr>
        <td align="center">&nbsp;</td>
    </tr>
      		  
 </table><!--
 <div align="center">
					<a class="bt_1" onclick="imp()"><span><pg:message code="sany.pdp.common.confirm"/></span></a>
		    </div>
--></form>
</div>
</body>
</html>

