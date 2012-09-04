<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    
    //判断操作是否成功
	/*String isok =  (String)request.getAttribute("isOk");
	String resTypeId = request.getParameter("resTypeId");
	String resid = request.getParameter("channelId");
	String resname = request.getParameter("channelName");
	String roleid = (String)session.getAttribute("currRoleId");
	String role_type = (String)session.getAttribute("role_type");*/

	String isok =  (String)request.getAttribute("isOk");
	String resTypeId = request.getParameter("resTypeId2");
	String resid = request.getParameter("resId2");
	String resname = request.getParameter("title");
	String roleid = request.getParameter("resId");
	String role_type = "user";
	String username = request.getParameter("username");
	
	UserManager userManager = SecurityDatabase.getUserManager();
	boolean flag = AccessControl.isAdmin(userManager.getUserById(roleid).getUserName());

	String title = request.getParameter("title");
	
	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resid == null){
		resid = (String)request.getAttribute("resid");
	}
	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List list = resManager.getOperations(resTypeId);
	List hasOper = operManager.getOperResRoleList(role_type,roleid,resid,resTypeId);
	if(list == null){
		list = new ArrayList();
	}	
	request.setAttribute("operList",list);
	if(hasOper == null){
		hasOper = new ArrayList();
	}
	
	session.removeAttribute("resname");
	session.setAttribute("resname",resname);
%>

<html>
<head>    
<title>权限授予</title>
<%@ include file="/include/css.jsp"%>
			<link rel="stylesheet" type="text/css" href="../css/treeview.css">
  
<SCRIPT LANGUAGE="JavaScript"> 
	var ok = <%=isok%>;
	if(ok!=null){
		alert("授予操作项成功！");
	}
	
	//复选框全部选中
	function checkAll(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].parentElement.parentElement.disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
			   if(!o[i].parentElement.parentElement.disabled)
		   	  	  o[i].checked=false;
	   	   }
	   }
	}
	
	function setCheck(currCheck,priority)
	{
	   	var o = document.getElementsByName("alloper");
		var prioritylist = document.getElementsByName("priority"); 
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
	
	function changebox(opid,flag){
	}
	
	function changebox1(currCheck,priority,opid){
		setCheck(currCheck,priority);
	}
	//频道授权
	function okRecord(dealType,id) {
		
		    var isSelect = false;
		    var outMsg;
			var tt;    
		    for (var i=0;i<Form1.elements.length;i++) {
				var e = Form1.elements[i];
					
				if (e.name == 'alloper'){
					if (e.checked){
			       		isSelect=true;
			       		break;
				    }
				 }
				  if (e.name == 'isRecursion'){
					if (e.checked){
			       		tt="1";
			       		
				    }else{
				    	tt ="0";
				    
				    }
				}
		    }
			Form1.action="<%=rootpath%>/accessmanager/securityManager.do?method=resUserAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form1.submit();
			setTimeout("promptMsg()",1000);
				 		
	}
	function promptMsg(){
	    alert('授权成功!');
	}
	function closeWindow(){
	    window.close();
	}
</SCRIPT>
</head>

<body class="contentbodymargin">
<div id="contentborder">
	
			<form target="channel" name="Form1" action="" method="post" >
			<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
				  	<input name="resTypeId" value="<%=resTypeId%>" type=hidden>
				  	<input name="resid" value="<%=resid%>" type=hidden>
				  	<input name="roleid" value="<%=roleid%>" type=hidden>
				  	<input name="role_type" value="<%=role_type%>" type=hidden>
					<input name="resname" value="<%=resname%>" type=hidden>					
				  <tr>
			        <td height='30' colspan="3">
			         <LEGEND align=left><strong><FONT size=2><%if(username != null){out.print(username + " >> ");}%>授予操作项</FONT></strong></LEGEND>
			        </td>
			      </tr>	
				  <tr>
			        <td>
			        <input type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','alloper')">
			        </td>
			        <td>>>>>全选</td>
			        <td>&nbsp;<input name="isRecursion" onclick="" type="checkbox" style="display:none"></td>
			      </tr>
			      <pg:list requestKey="operList" needClear="false">
			      <tr class="tr" 
			      <%
			      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
			      if(!accesscontroler.checkPermission(resid,opId,resTypeId) || flag)
			      out.print(" disabled=\"true\"");
			      %>
			      >
			        <td>
			        <input name="alloper" type="checkbox"
			        <%
			        	//是否为系统管理员
			        	if(flag)
			        	{
			        		out.println(" checked ");
			        	}
			        	else
			        	{
			        	for(int i = 0; i < hasOper.size(); i ++)
			        	{
			        		RoleresopKey id = (RoleresopKey)hasOper.get(i);
			        	
			        		if(id.getOpId().equals(opId) && id.getRoleId().equals(roleid))
			        		{
			        			out.println("checked");
			        			break;
			        		}
			        			
			        	}
			        	}
			        %>
			        value="<pg:cell colName="id"/>" onclick="changebox1(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
			        </td>
			        <td><pg:cell colName="name"/></td>
			        <td>&nbsp;</td>
			        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
			      </tr>
			       </pg:list>
			       
			       <tr>
			        <td></td>
			        <td></td>
			        <td>
			        <input type="submit" value="确定" class="input" onclick="javascript:okRecord(1,'<%=resTypeId%>'); return false;" 
			        <%
			        if(accesscontroler.getUserID().equals(roleid) || flag)
			        out.print(" disabled=\"true\"");
			        %>
			        >
			        <input type="button" value="关闭" class="input" onclick="closeWindow()">
			        </td>
			      </tr>
			    </table>
			</form>
			<div style="display:none">
			<IFRAME name="channel" width="0" height="0">
			</IFRAME>
			</div> 
	
</div>
</body>

</html>


