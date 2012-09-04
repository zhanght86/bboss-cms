<%
/*
 * <p>Title: 为岗位选择机构页面</p>
 * <p>Description: 为岗位选择机构页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-25
 * @author baowen.liu
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	javax.servlet.http.HttpServletRequest,
    javax.servlet.http.HttpServletResponse,
	com.frameworkset.platform.sysmgrcore.entity.Job,
    com.frameworkset.platform.sysmgrcore.entity.Organization,
    com.frameworkset.platform.sysmgrcore.manager.OrgManager,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.platform.sysmgrcore.manager.UserManager,java.util.*,
	com.frameworkset.platform.sysmgrcore.web.struts.action.JobManagerAction,
	java.util.List"%>
<%	
	  AccessControl control = AccessControl.getInstance();
	  control.checkManagerAccess(request,response);
      String jobId = (String)request.getParameter("jobId");
	  JobManager jm = SecurityDatabase.getJobManager();
	  Job job = jm.getJobById(jobId);
	  String jobName = job.getJobName();
      //System.out.println("orgId="+orgId);
     
		request.setAttribute("jobId", jobId);

		String orgId = (String)request.getParameter("orgId");
		//System.out.println("orgId="+orgId);
		if (orgId == null)
			orgId = "0";
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		
		
		
		String existOrgSql = "select * from td_sm_organization a,td_sm_orgjob b " + 
				"where a.org_id=b.org_id and job_id='"+job.getJobId()+"' and b.org_id='" +
				orgId + "'";
		List existOrg = null;
		if(control.isAdmin()){
			existOrg = orgManager.getOrgList(job); // 该岗位关联机构
		}else{
			existOrg = orgManager.getOrgListBySql(existOrgSql);
		}
		List allOrg;
		Organization org = new Organization();
		org.setOrgId(orgId);
		orgManager.isContainChildOrg(org);// 检查指定的机构是否包含子机构
		if (orgManager.isContainChildOrg(org)) {
			allOrg = orgManager.getOrgListBySql("select * from td_sm_organization o where o.parent_id='"
							+ orgId + "'  or o.org_id='" + orgId + "'");
		} else {
			allOrg = orgManager.getOrgListBySql("select * from td_sm_organization o where o.org_id='" + orgId
							+ "'");
		}
		request.setAttribute("existOrg", existOrg);
		request.setAttribute("allOrg", allOrg);
  
	
%>
<html>
<head>    
  <title>属性容器</title>
	<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../html/js/commontool.js"></script>
<script type="text/javascript" src="../../html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="../../html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
  
<SCRIPT LANGUAGE="JavaScript"> 
	var api = parent.frameElement.api, W = api.opener;
	//添加单个岗位
	function addOrg(){	
	   //alert("dfasd");
	   //已授予的机构列表
	   var alreadyOrgArr = document.all("orgId") ;
       var n=document.all("orgId").options.length-1; 
   	   var count=0;	
   	   var orgIds = "";
   	   var alreadyJobs = "";  
	   for(var i=0;i<document.all("allist").options.length;i++){
	      var op=document.all("allist").options[i];
	      if(op.selected){
	        //flag的值为false则不包含已授予机构
	        var flag = false;
	        //判断已授予的机构列表中是否包含了选中的机构
	        for(var j = 0; j < alreadyOrgArr.length; j++){
	   	   	   if(op.text==alreadyOrgArr.options[j].text){
	   	   	   		flag = true;
	   	   	   }
		    }
		    //如果选中的机构在不在已授予的机构列表中
		    if(!flag){
		   		addone(op.text,op.value,n);
				if(orgIds=="") orgIds = orgIds + op.value;
				else orgIds = orgIds + "," + op.value;
		    }
		    //如果选中的机构在已授予的机构列表中
		    else{
		   		if(alreadyJobs=="") alreadyJobs = alreadyJobs + op.text;
				else alreadyJobs = alreadyJobs + "\n" + op.text;
		    }
	      }
	   }
   
	    if(orgIds != ""){
	  	  send_request('savaJobOrg.jsp?jobId=<%=jobId%>&orgId='+orgIds+'&flag=1');
	    }else if(alreadyJobs != ""){
	    	W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.organization.setted'/>：\n" + alreadyJobs,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  	  return;
	    }else{
	    	W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.want.choose.organization'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  	  return;
	    }
    }
    
	function addone(name,value,n){
	
	   for(var i=n;i>=0;i--){
			if(value==document.all("orgId").options[i].value){
			  return;
			}
		}
	   var op=new Option(name,value);
	   document.all("orgId").add(op);
	   
	}
	//删除所有岗位
	 function deleteall(){
	   var orgIds = "";
	   var k=document.all("orgId").options.length;
	   if(k<1){
	   		W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.no.choose.organization'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
	   }
	   W.$.dialog.confirm("<pg:message code='sany.pdp.delete.comfirm'/>?",function (){
		   for(var m=document.all("orgId").options.length-1;m>=0;m--){
	   			if(orgIds==""){
	   		    	orgIds = document.all("orgId").options[m].value;
	   		    }else{
	   		    	orgIds += "," + document.all("orgId").options[m].value;
	   		    }
	   			document.all("orgId").options[m]=null;
		   }
		   send_request('savaJobOrg.jsp?jobId=<%=jobId%>&orgId='+orgIds+'&flag=0')
	   },null,null,"<pg:message code='sany.pdp.common.alert'/>")
	   
	}
	   //添加所有的岗位
	 function addall(){
	 	//已授予的机构列表
	   var alreadyOrgArr = document.all("orgId") ;
       var n=document.all("orgId").options.length-1; 
   	   var count=0;	
   	   var orgIds = "";
   	   var alreadyJobs = "";  
	   for(var i=0;i<document.all("allist").options.length;i++){
	      var op=document.all("allist").options[i];
	      
	        //flag的值为false则不包含已授予机构
	        var flag = false;
	        //判断已授予的机构列表中是否包含了选中的机构
	        for(var j = 0; j < alreadyOrgArr.length; j++){
	   	   	   if(op.text==alreadyOrgArr.options[j].text){
	   	   	   		flag = true;
	   	   	   }
		    }
		    //如果选中的机构在不在已授予的机构列表中
		    if(!flag){
		   		addone(op.text,op.value,n);
				if(orgIds=="") orgIds = orgIds + op.value;
				else orgIds = orgIds + "," + op.value;
		    }
		    //如果选中的机构在已授予的机构列表中
		    else{
		   		if(alreadyJobs=="") alreadyJobs = alreadyJobs + op.text;
				else alreadyJobs = alreadyJobs + "\n" + op.text;
		    }
	      
	   }
   
	    if(orgIds != ""){
	  	  send_request('savaJobOrg.jsp?jobId=<%=jobId%>&orgId='+orgIds+'&flag=1');
	    }else if(alreadyJobs != ""){
	  	 parent.parent.parent.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.organization.setted'/>：\n" + alreadyJobs,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  	  return;
	    }
	}
	//删除单个岗位
	function deleteorg(){
	   var orgIds = "";
	   var n=document.all("orgId").options.length;
	   if(n<1){
			W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.no.choose.organization'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
	   }
	   if(document.all("orgId").selectedIndex==-1){
	   		W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.want.choose.organization'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
	   }
	   parent.parent.parent.$.dialog.confirm("<pg:message code='sany.pdp.delete.comfirm'/>?",function (){
	   	for(var m=document.all("orgId").options.length-1;m>=0;m--){
		   		if(document.all("orgId").options[m].selected){
		   			if(orgIds==""){
		   		    	orgIds = document.all("orgId").options[m].value;
		   		    }else{
		   		    	orgIds += "," + document.all("orgId").options[m].value;
		   		    }
		   			document.all("orgId").options[m]=null
		   		}
		   }
		   send_request('savaJobOrg.jsp?jobId=<%=jobId%>&orgId='+orgIds+'&flag=0');
	   })
	}

//---------------------------------------
	//初始化，指定处理的函数，发送请求的函数
	function send_request(url){
		document.RoleOrgForm.action = url;
		document.RoleOrgForm.target = "hiddenFrame";
		document.RoleOrgForm.submit();
	}
			
	function changebox(){				 
	   	 var len=document.all("orgId").options.length;			  	 	
	     		 //var roleId = document.all("roleId").value;
	     		 var jobId="<%=jobId%>";
	     	
	     		 var orgId=new Array(len)
	     		 for (var i=0;i<len;i++){	      
	     		   orgId[i]=document.all("orgId").options[i].value;
	      		 }   
	      		
		 send_request('savaJobOrg.jsp?jobId=<%=jobId%>'+'&orgId='+orgId);
	}
</SCRIPT>
  <script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
 
<body class="contentbodymargin" onload="loadDragDiv()">
<div id="">
<center>
<form name="RoleOrgForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center"><pg:message code="sany.pdp.jobmanage.choose.organization"/></td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">【<%=jobName%>】<pg:message code="sany.pdp.jobmanage.exist.organization"/></td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right">
     <div class="win" id="dd_1" align="left" title="下拉选择框的右边可以左右拖动">
     <select name="allist"  multiple style="width:98%" onDBLclick="addOrg()"  size="18" title="下拉选择框的右边可以左右拖动">
		  <pg:list requestKey="allOrg">
			<option value="<pg:cell colName="orgId"/>">
			<pg:null colName="remark5"><pg:cell colName="remark5"/></pg:null>
			<pg:notnull colName="remark5">
				<pg:equal colName="remark5" value=""><pg:cell colName="remark5"/></pg:equal>
				<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
			</pg:notnull>
			</option>
		  </pg:list>			
	</select>
	</div>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><a class="bt_2"  onclick="addOrg()"><span>&gt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2"  onclick="addall()"><span>&gt;&gt;</span></a></td>
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
        <td align="center"><a class="bt_2"  onclick="deleteorg()"><span>&lt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
    <div class="win" id="dd_2" align="left" title="下拉选择框的右边可以左右拖动">
     <select name="orgId"  multiple style="width:98%" onDBLclick="deleteorg()"  size="18" title="下拉选择框的右边可以左右拖动">
				  <pg:list requestKey="existOrg">
					
					<option value="<pg:cell colName="orgId"/>">
					<pg:null colName="remark5"><pg:cell colName="remark5"/></pg:null>
					<pg:notnull colName="remark5">
					<pg:equal colName="remark5" value=""><pg:cell colName="remark5"/></pg:equal>
					<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
					</pg:notnull>
					</option>
				  </pg:list>			
	 </select>				
	</div>				
	</td>				 
				  
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    
  </tr>
  <tr><td colspan="3" align="center">
  
  
  </td> </tr>
 </table>

	<input type="hidden" name="jobId" value="<%=jobId%>"/>
	
	
	</form>
	</center>
	</div>
	
   <div id=divProcessing style="width:200px;height:30px;position:absolute;left:170px;top:400px;display:none">
			<table align="center" border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
			    <tr>
				    <td bgcolor=#3A6EA5>
					    <marquee align="middle" behavior="alternate" scrollamount="5">
						    <font color=#FFFFFF>...处理中...请等待...</font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
</body>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
