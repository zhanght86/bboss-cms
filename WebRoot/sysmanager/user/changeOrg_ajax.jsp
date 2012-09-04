
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %> 
<%@ page import="java.util.List,java.util.ArrayList" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,
		com.frameworkset.platform.sysmgrcore.entity.OrgJobName,
		com.frameworkset.platform.sysmgrcore.entity.Organization" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.ChargeOrg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager" %>
<%@ page import="com.frameworkset.common.poolman.DBUtil" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	
	AccessControl control = AccessControl.getInstance();
   	control.checkAccess(pageContext); 
    
    //action改造开始
    
  DBUtil db = new DBUtil(); 
	String userId = request.getParameter("userId");
	if(userId == null || userId.equals(""))
	{
		out.print("用户不存在！");
		return ;
	}	 
	
	//获取用户所在当前机构id
	String orgId = request.getParameter("orgId");   
	
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	UserManager userManager = SecurityDatabase.getUserManager();
	//根据用户id获取用户对象
	User user = userManager.getUserById(userId);
	if(user == null )
	{
		out.print("用户不存在！");
		return ;
	}	
	//取用户所在主机构名-----------------------------------------------------------
	String orgname = "";	
	String mainOrgId = "";
	//获取用户的主管机构
	Organization ch = orgManager.getMainOrganizationOfUser(user.getUserName());
	

	 if(ch == null ){
		 request.setAttribute("orgname","未设主机构！");
	 }
	 else
	 {
		 orgname = ch.getRemark5();
		 mainOrgId = ch.getOrgId();//将主机构的id传到页面上,da.wei
		 
	 }
		 
		 JobManager jobManager = SecurityDatabase.getJobManager();
			//获取默认的在职岗位
			Job job = jobManager.getJobById("1"); 
		try {
			List existOrg = new ArrayList();
			String sql ="select org_id,job_id from td_sm_userjoborg where user_id ="+ userId +"";
			db.executeSelect(sql);
			
			for (int i = 0; i < db.size(); i++) {
							
				Organization org1 = OrgCacheManager.getInstance().getOrganization(db.getString(i,"org_id"));
				OrgJobName ojn = new OrgJobName();
				String orgname_ = "";
				if(org1 == null)
				{
					orgname_ = "已被删除的机构[" + db.getString(i,"org_id") + "]";
					
				}
				else
				{
					orgname_ = org1.getRemark5();
				}
				
				Job job1 = jobManager.getJobById(db.getString(i,"job_id"));
				String jobname = "";
				if(job1 == null)
				{
					jobname = "已被删除的岗位[" + db.getString(i,"job_id") + "]";
					
				}
				else
				{
					jobname = job1.getJobName() ;
				}
				
				ojn.setOrgId(db.getString(i,"org_id")+";"+db.getString(i,"job_id"));
				
//				不是取getOrgName,而是要remark5,weida200709121817
//				ojn.setRemark5("("+ orgname_+")"+ jobname);
				ojn.setRemark5("("+ orgname_ + ")" + jobname);
				
				existOrg.add(ojn);
		   }		
		   request.setAttribute("existOrg", existOrg);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			out.print("获取用户隶属机构失败");
			return ;
		}
			
    
    //action改造结束
    
	//登录用户ID
	String logid = control.getUserID();
	String userName = user.getUserRealname();
	String jobName = "";
	String jobid = "";
	if(job != null){
		jobid = job.getJobId();
		jobName = job.getJobName();
	}
	
%>


<head>
<title>用户【<%=user.getUserName()%>(<%=user.getUserRealname()%>)】隶属机构</title>
<script language="JavaScript"> 
	var waitjobid = "<%=jobid%>";
	var win;
	function addorg(){	
		var url = "org2org.jsp?uid=<%=userId%>&logid=<%=logid%>&currentOrgId=<%=orgId%>&mainOrgId=<%=mainOrgId%>";
		win = window.showModalDialog(url,window,"dialogWidth:"+560+"px;dialogHeight:"+480+"px;help:no;scroll:auto;status:no");
		if(win == "ok"){
			var innerurl = "subjectionOrg.jsp?userId=<%=userId%>&orgId=<%=orgId%>";
			window.location.href = innerurl;
		}
	}
	
	function changeorg(){	
		var all = document.all("orgName").value;
		var orgs = all.split("&");
		//因为后面还有一个&,所以长度减一
		for(var i = 0;i <orgs.length-1;i++){
			var orgidname = orgs[i];
			var all2 = orgidname.split(";");
			var op = new Option(all2[1]+"(<%=jobName%>)",all2[0]+";"+waitjobid);
			var isadd = 1;
			for(var j = 0;j<document.all("orgList").length;j++){
				if(op.value ==	document.all("orgList").options[j].value){	
					isadd = 0;
				}
			}
			if(isadd == 1)
			document.all("orgList").add(op);
		}
	   
	}
	
	function deleteorg()
	{	
		//判断属于当前机构而且未被选中的机构岗位是否一个都没有了,如是提示返回,weida
		var count = 0;
		//alert(document.all("orgList").options.length)
		//alert(document.all("orgList").options[0].value)
		//var str = document.all("orgList").options[0].value;
		//alert(str.split(";"))
		for (var n=document.all("orgList").options.length-1;n>=0;n--)
		{
			var thisOrg = document.all("orgList").options[n].value;
			var thisOrgId = thisOrg.split(";")
			if((thisOrgId[0] == "<%=orgId%>") && !(document.all("orgList").options[n].selected))
			     count = count+1;
		}
		if(count == 0)
		{
			alert("要调出当前机构下的所有岗位,请直接在列表中删除用户!");
			return;
		}		
		//判断当前用户的主机构的岗位是否一个都没有了,如是提示返回,da.wei
		count = 0;
		for (var i=document.all("orgList").options.length-1;i>=0;i--)
		{
			var thisOrg = document.all("orgList").options[i].value;
			var thisOrgId = thisOrg.split(";")
			if((thisOrgId[0] == "<%=mainOrgId%>") && !(document.all("orgList").options[i].selected))
			     count = count+1;
		}
		if(count == 0)
		{
			alert("不能全部调出该用户主机构下的所有岗位!");
			return;
		}
		
	
		 var orgIdName;
		 
		 var tag = "noOneSelected";
	
		 for (var m=document.all("orgList").options.length-1;m>=0;m--){
		 		 var option = document.all("orgList").options[m];
			     if(document.all("orgList").options[m].selected){
			     	orgIdName = option.value;		     	
			     	document.all("orgList").options[m]=null;
			     	//send_request('saveUserOrg.jsp?uid=<%=userId%>&orgIdName='+orgIdName); 
			     	OrgJobForm.action="<%=request.getContextPath()%>/sysmanager/user/saveUserOrg.jsp?uid=<%=userId%>&orgIdName=" + orgIdName;
			     	OrgJobForm.target="savaOrg";
			     	OrgJobForm.submit();
			     	divProcessing.style.display = "";
			     	tag = "someOneSelected";      
			     	
		         }	        
		 }
	 
		 if(tag == "noOneSelected")
		 {
		 	alert("请先选择要调出的机构!");
		 }
	
	}
	
	function setup(){
	 var orgIdName;
	 if(document.all("orgList").value==""){
	 	alert("请选择要设置的机构！");
	 }
	 
	 for (var m=document.all("orgList").options.length-1;m>=0;m--){
	 
	 		 var option = document.all("orgList").options[m];
	 		 var orgName = null;
		     if(document.all("orgList").options[m].selected){
		     	orgIdName = option.value;
		     	orgName = option.text;
		     	var mainorgStr = document.all("mainorg").innerText;
		     	var n = mainorgStr.length;
		     	mainorgStr = mainorgStr.substring(5,n);
		     	if(orgName.indexOf(mainorgStr) > 0){
		     		alert(mainorgStr + "已设置为主机构");
		     		return;
		     	}
				//mainorg.innerText = "主机构为:" + orgName;
		     	send_request('saveSetupOrg.jsp?orgIdName='+orgIdName+'&userId='+<%=userId%>);
		     	//document.location.href="../user/userOrg.do?method=storeSetupOrg";
			   
		     	  
	         }
	 }
	
	 
	
	}  
	  
	
	function changebox(){
		var len=document.all("orgList").options.length;
		var orgIdName = new Array(len);
		for (var i=0;i<len;i++){	      
	        orgIdName[i]=document.all("orgList").options[i].value;
	    }
	     send_request('saveUserOrg.jsp?orgIdName='+orgIdName); 
	  
	}
	
	
//------------------------------------------------------------------------------
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
							location.href = location.href;
							alert("操作成功！");    
						
					}
					else{
						alert("对不起，服务器错误");
					}
				}
				
			}
			
	function closed(){
		parent.window.close();
		parent.window.returnValue = "ok"
	}

</script>
<script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/tab.winclassic.css">
</head>

<body class="contentbodymargin" scroll="no" onload="loadDragDiv();">

<div id="contentborder">
	<center>
	<form name="OrgJobForm" action method="post">
	<input name="userId" value="<%=userId%>" type="hidden">
	<input name="orgId" value="<%=orgId%>" type="hidden">
		<table width="31%" border="0" cellpadding="0" cellspacing="1">
			<tr class="tabletop">
				<td colspan="3" align="center">&nbsp;</td>
			</tr>
			<tr class="tabletop">
				<td colspan="3"  align="center"><%=user.getUserName()%>(<%=user.getUserRealname()%>)已授予机构</td>
				
			</tr>
		
			<tr class="tabletop">
				<td colspan="3" width="100%" align="center">&nbsp;
				<input name="orgName" type="hidden" onpropertychange="changeorg()"  value="ss" onChange="changeorg()" >				
				</td>
			</tr>
			<tr>
				<td colspan="3">
				<p align="right"><div class="win" id="dd_1" align="left"  title="下拉选择框的右边可以左右拖动">
				<select name="orgList"  style="width:98%" size="18" title="下拉选择框的右边可以左右拖动">
				  <pg:list requestKey="existOrg" needClear="false">
					<option value="<pg:cell colName="orgId"/>"><pg:cell colName="remark5"/>
				</option>
				  </pg:list>			
	 			</select></div>
				</p>
				</td>
			</tr>
			<tr class="tabletop">
				<td colspan="3"  align="center">&nbsp;</td>
			</tr>
			<TR><td colspan="3" id="mainorg" name="mainorg" align="center">主机构为:<%=orgname%></td></TR>
			<TR><td colspan="3"  align="center">&nbsp;</td></TR>
			<tr>
				<!-- <td align="center"><input name="add" type="button" class="input" value="调入" onclick="addorg()"></td> -->
				<!--  <td align="center"><input name="add1" type="button" class="input" value="调出" onclick="deleteorg()"></td> -->
				<td align="center"><input name="add2" type="button" class="input" value="主机构设置" onclick="setup()"></td>
				<td align="center"><input name="but3" type="button" class="input" value="关闭" onclick="closed();"></td>
			</tr>
			<tr>
				<td colspan="3" align="center">
				  <div id=divProcessing style="width:250px;height:30px;left:200px;top:330px;display:none">
					<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
	    				<tr>
		    				<td bgcolor=#3A6EA5>
			    				<marquee align="middle" behavior="alternate" scrollamount="5">
				    				<font color=#FFFFFF>...处理中...请等待...</font>
								</marquee>
							</td>
						</tr>
					</table>
				  </div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="type1" />
		<input type="hidden" name="type2"  onpropertychange="changeorg()"/>
	</form>
	</center></div>
	<form name="form2" method="POST">
	</form>
	<iframe id="savaOrg" width="" height="" name="savaOrg"></iframe>
</body>



