<%
/**
 * <p>Title: 机构岗位设置岗位下用户设置页面</p>
 * <p>Description: 机构岗位设置岗位下用户设置页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.util.StringUtil
				,com.frameworkset.platform.security.AccessControl
				,com.frameworkset.platform.sysmgrcore.entity.Organization
				,com.frameworkset.common.poolman.DBUtil
				,com.frameworkset.platform.sysmgrcore.entity.Job
				,com.frameworkset.platform.sysmgrcore.entity.User
				,com.frameworkset.platform.sysmgrcore.manager.UserManager
				,com.frameworkset.platform.sysmgrcore.manager.RoleManager
				,java.util.List
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
				,com.frameworkset.platform.sysmgrcore.web.struts.form.OrgJobForm"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkManagerAccess(request,response);
    String orgId = request.getParameter("orgId");
	String jobId = request.getParameter("curjobid");
	Organization org = new Organization();
	org.setOrgId(orgId);
	

	//OrgJobForm的数据应该来自页面Form，此处还需修改
	OrgJobForm orgJobForm = (OrgJobForm) new OrgJobForm();	
	
	orgJobForm.setOrgId(orgId);
	orgJobForm.setCurJobId(jobId);
	Job job = new Job();
	job.setJobId(jobId);
	

	RoleManager roleManager = SecurityDatabase.getRoleManager();
	List roleList = roleManager.getJobListByRoleJob(jobId,orgId);
	request.setAttribute("roleList", roleList);
	
	UserManager userManager = SecurityDatabase.getUserManager();
	String sql="select a.* from td_sm_user a , (select t.user_id from td_sm_userjoborg t where t.org_id='"
		+ orgId + "' and t.job_id='"+ jobId +"' order by t.same_job_user_sn asc) " +
				" b where a.user_id in (b.user_id )";
	List userList=userManager.getUserList(sql);
	request.setAttribute("userList", userList);
	request.setAttribute("orgJobForm", orgJobForm);
	//request.setAttribute("dflag", "1");
	    
	//String dflag = (String)request.getAttribute("dflag");
	//if(dflag == null || dflag.length() == 0){
	//	dflag = "0";
	//}
	
	String dflag = "1";
%>
<html>
	<head>
		<title></title>
		<SCRIPT LANGUAGE="JavaScript">
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
				$.dialog.alert("<pg:message code='sany.pdp.no.httprequest'/>");
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
					$.dialog.alert("<pg:message code='sany.pdp.server.error'/>");
				}
			}
		} 
		var dflag = "<%=dflag%>"; 
		function init(){
		if(dflag == "0"){
			//document.forms[0].b1new.disabled=true;   
			//document.forms[0].b3del.disabled=true;   
			//document.forms[0].button1.disabled=true; 
			//document.forms[0].button2.disabled=true; 
			//document.forms[0].button3.disabled=true; 
			//document.forms[0].button4.disabled=true;
		}
		}
	    function deleteUser(){
		    var orgId = document.all("orgId").value;
		    var jobId = document.all("curJobId").value;
	    	var isselected = 0;
	    	var len=document.all("userList").options.length;
			for (var i=0;i<len;i++){
				if(document.all("userList").options[i].selected)
					isselected = 1;
		    }
		    if(isselected == 0){
		   		$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption.person'/>");
		   		return;
		   	}
		   	
		   	$.dialog.confirm("<pg:message code='sany.pdp.dictmanager.dict.delete.confirm'/>", function() {
		   		var userId=new Array()
		   		var s = 0;
			    for (var j=0;j<len;j++){       
		        	if(document.all("userList").options[j].selected){
		        		userId[s]=document.all("userList").options[j].value;
		        	
		        		//s ++;
		        		//document.all("userList").options[j] = null;
		        	}
	        	}   
	        	///orgmanager/orgoperjob.do?method=deleteUser
			   document.forms[0].action="";
         		document.forms[0].submit();
		   	});
		}
		
		function saveUser(){	    	
	    	var len=document.all("userList").options.length;
			for (var i=0;i<len;i++){
				document.all("userList").options[i].selected = true;					
		    }
		    ///orgmanager/orgoperjob.do?method=storeUserjoborg
		   	document.forms[0].action="";
         	document.forms[0].submit();
		}
		
		function adduser(){	    	
	    	var orgId = document.all("orgId").value;
			var jobId = document.all("curJobId").value;
			
			window.open('org2user.jsp?orgId='+orgId+'&jobId='+jobId,'newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
			
		}
		
		function sendUser(){     
	       var orgId = document.all("orgId").value;
	       var len=document.all("userList").options.length;
	       var jobSn = document.all("jobSn").value;
		   var jobId = document.all("curJobId").value;
	       var userId=new Array()
	       for (var i=0;i<len;i++){       
	         userId[i]=document.all("userList").options[i].value;
	        }   
		   send_request('usersnchange.jsp?orgId='+orgId+'&jobId='+jobId+'&jobSn='+jobSn+'&userId='+userId);
   		}
   
		function up1() {
			var len=document.all("userList").options.length;
			var isselected = 0;
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			isselected = 1;
		   		}
		   	}
		   	if(isselected == 0){
		   		$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption.job'/>");
		   		return;
		   	}
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			var tmp = new Option(op.text,op.value);
		   			if(i>0){
			   			var dest = document.all("userList").options[i-1];
			   			var userId1 = tmp.value;	   				
		   				var orgId = document.all("orgId").value;
	   					var userId2 = dest.value;
	   					var jobSn = document.all("jobSn").value;
	   					var jobId = document.all("curJobId").value;
	   					var userSn = i;
		   				document.all("userList").options[i-1] = tmp;
		   				document.all("userList").options[i] = dest;
		   				document.all("userList").options[i-1].selected=true;
		   				send_request('usersnchange.jsp?userId1='+userId1+'&userId2='+userId2+'&orgId='+orgId+'&jobSn='+jobSn + '&userSn='+userSn +'&jobId='+jobId);
		   			}
		   		}
		    }
		}
		function upall() {
			var len=document.all("userList").options.length;	
			var isselected = 0;
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			isselected = 1;
		   		}
		   	}
		   	if(isselected == 0){
		   		$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption.job'/>");
		   		return;
		   	}
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			var tmp = new Option(op.text,op.value);
		   			op.selected = false;   			
		   			var j=i;   			
		   			for(;j>=1;j--){     				
		   				var atmp =  document.all("userList").options[j-1];   				
		   				var btmp = new Option(atmp.text,atmp.value);   				
		   				document.all("userList").options[j] = btmp;   				
		   			}   			
		   			document.all("userList").options[0] = tmp;	
		   		}
		    }
		    sendUser();
		    document.all("userList").options[0].selected=true;
		}
		
		function down1() {
			var len=document.all("userList").options.length;
			var isselected = 0;
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			isselected = 1;
		   		}
		   	}
		   	if(isselected == 0){
		   		$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption.job'/>");
		   		return;
		   	}
			var flag = 0;
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			var tmp = new Option(op.text,op.value);   			
		   			op.selected=false;
		   			if(i == len-1){
		   				flag = len-2;
		   			}
		   			else {
		   				flag = i;
		   			}
		   			if(i<len-1){
		   				var dest = document.all("userList").options[i+1];
			   			var userId2 = tmp.value;	   				
		   				var orgId = document.all("orgId").value;
	   					var userId1 = dest.value;
	   					var jobSn = document.all("jobSn").value;
	   					var jobId = document.all("curJobId").value;
	   					var userSn = i;
		   				document.all("userList").options[i+1] = tmp;
		   				document.all("userList").options[i] = dest;
		   				send_request('usersnchange.jsp?userId1='+userId1+'&userId2='+userId2+'&orgId='+orgId+'&jobSn='+jobSn + '&userSn='+userSn +'&jobId='+jobId);   				
		   			}
		   		}
		    }
		    document.all("userList").options[flag+1].selected=true;
		}
		
		function downall() {
			var len=document.all("userList").options.length;
			var isselected = 0;
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			isselected = 1;
		   		}
		   	}
		   	if(isselected == 0){
		   		$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption.job'/>");
		   		return;
		   	}	
			for (var i=0;i<len;i++){
				var op=document.all("userList").options[i];
		   		if(op.selected){   			
		   			var tmp = new Option(op.text,op.value);
		   			op.selected = false;   			
		   			var j=i;   			
		   			for(;j<len-1;j++){     				
		   				var atmp =  document.all("userList").options[j+1];   				
		   				var btmp = new Option(atmp.text,atmp.value);   				
		   				document.all("userList").options[j] = btmp;   				
		   			}   			
		   			document.all("userList").options[len-1] = tmp;	
		   		}
		    }
		    sendUser();
		    document.all("userList").options[len-1].selected=true;
		}
		
		function changeorg(){	
			var username = document.all("type1").value;
			var userid = document.all("type2").value;
			var orgId = document.all("orgId").value;
			var jobSn = document.all("jobSn").value;
			var jobId = document.all("curJobId").value;
			//alert(username + "===" + userid);
			//alert();
			var usernames = username.split(",");
			var userids = userid.split(",");
			//因为后面还有一个&,所以长度减一
			for(var i = 0;i <usernames.length;i++){
				var ausername = usernames[i];
				var auserid = userids[i];				
				var op = new Option(ausername,auserid);
				var isadd = 1;
				for(var j = 0;j<document.all("userList").length;j++){
					if(op.value ==	document.all("userList").options[j].value){	
						isadd = 0;
					}
				}
				if(isadd == 1){
					document.all("userList").add(op);
	   				send_request('userjobchange.jsp?auserId='+op.value+'&orgId='+orgId+'&jobSn='+jobSn + '&jobId='+jobId);   				
				}
			}
		   
		}
	</SCRIPT>
    <script language="javascript" src="../../scripts/dragdiv.js"></script>
	</head>	
	<body onload="loadDragDiv()" onunload="parentFrame()">
		<form name="form1" method="post">
			<table width="184" height="341" border="0" align="center">
				<tr>
					<td height="16" colspan="3" valign="top">
						<div align="center">
							<pg:message code="sany.pdp.userorgmanager.user.org.jobs.user.list"/>
						</div>
				  </td>
				</tr>
				<tr>
					<td colspan="3" valign="top" align="center">
						<div align="center" class="win" id="dd_1" style="width: 160">
							<select name="userList" multiple style="width:96%"  size="18">
								<pg:list requestKey="userList">
									<option value="<pg:cell colName="userId"/>">
										<pg:cell colName="userName" />(<pg:cell colName="userRealname" />)
									</option>
								</pg:list>
							</select>
						</div>
				  </td>
				</tr>
				
				<tr>
					<td height="16" colspan="3" valign="top">
						<div align="center">
							<pg:message code="sany.pdp.userorgmanager.user.org.jobs.role.list"/>
						</div>
				  </td>
				</tr> 
				<tr>
					<td colspan="3" valign="top" align="center">
						<div align="center" class="win" id="dd_2" style="width: 160">
							<select name="roleList" multiple style="width:96%"  size="15">
								<pg:list requestKey="roleList">
									<option value="<pg:cell colName="roleId"/>">
										<pg:cell colName="roleName" />
									</option>
								</pg:list>
							</select>
						</div>
				  </td>
				</tr>
				<tr>
					<td>
						<pg:beaninfo requestKey="orgJobForm">
							<input type="hidden" name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>" />
							<input type="hidden" name="curJobId" value="<pg:cell colName="curJobId"  defaultValue=""/>" />
							<input type="hidden" name="jobSn" value="<pg:cell colName="jobSn"  defaultValue=""/>" />
							<input type="hidden" name="type1" />
							<input type="hidden" name="type2"  onpropertychange="changeorg()"/>
						</pg:beaninfo> 
					</td>
				</tr>
				
		  </table>
		</form>
		<script type="text/javascript" language="JavaScript">
			function parentFrame(){
				parent.frames[0].document.all("jobId").disabled = false;
				parent.frames[0].document.all.divProcessing.style.display="none";
				//parent.frames[0].document.all("jobId").focus();
			}
		</script>
	</body>
</html>
