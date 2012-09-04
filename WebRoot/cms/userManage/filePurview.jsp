<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List,com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	 //判断操作是否成功
	String isok =  (String)request.getAttribute("isOk");
	String resTypeId = "sitefile";
	String resid = request.getParameter("siteid");
	String roleid = (String)session.getAttribute("currRoleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleid);
	String rolename = role.getRoleName();
	String resname=request.getParameter("sitename");
	String role_type = (String)session.getAttribute("role_type");
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
	
%>
<html>
<head>    
<title>属性容器</title>
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
  <tab:tabConfig/>
<SCRIPT LANGUAGE="JavaScript"> 
	var ok = <%=isok%>;
	if(ok!=null){
		alert("授予操作项成功！");
	}
	//复选框全部选中
	function checkAll4(totalCheck,checkName){
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
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	
	function setCheck4(currCheck,priority)
	{
	   	var o = document.getElementsByName("alloper4");
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
	
	
	
	function changebox4(currCheck,priority,opid){
		setCheck4(currCheck,priority);
	}
	//文件视图授权
	function sitefileRecord(dealType,id) {
		    var isSelect = false;
		    var outMsg;
			var tt;    
		    for (var i=0;i<Form4.elements.length;i++) {
				var e = Form4.elements[i];
					
				if (e.name == 'alloper4'){
					if (e.checked){
			       		isSelect=true;
			       		break;
				    }
				 }
				  if (e.name == 'isRecursion4'){
					if (e.checked){
			       		tt="1";
			       		
				    }else{
				    	tt ="0";
				    
				    }
				}
		    }
		   
			Form4.action="<%=rootpath%>/accessmanager/securityManager.do?method=sitefileAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form4.submit();
				 		
	}
	function back(){
		parent.window.close();
	}
</SCRIPT>
</head>

<body class="contentbodymargin">
<div id="contentborder">
			<form target="sitefile" name="Form4" action="" method="post" >
			<table border="0" cellpadding="3" cellspacing="0">
					<tr>
						<td align="right">
							<strong>权限设置:</strong>
						</td>
						<td>
							<div align="center">
								<img src="../images/doc_aggr.gif" width="20" height="20">
							</div>
						</td>
						<td align="right">
							<a href="sitePurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>"> 
							站点类权限</a>
						</td>
						<td>
							<div align="center">
								<img src="../images/doc_aggr.gif" width="20" height="20">
							</div>
						</td>
						<td align="right">
							<a href="tplPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
							 模板类权限 </a>
						</td>
						<td>
							<div align="center">
								<img src="../images/doc_aggr.gif" width="20" height="20">
							</div>
						</td>
						<td>
							<a href="filePurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
							 文件视图类权限 </a>
						</td>
						<td>
							<div align="center">
								<img src="../images/doc_aggr.gif" width="20" height="20">
							</div>
						</td>
						<td>
							<a href="channelPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
							 频道类权限 </a>
						</td>
						<td>
							<div align="center">
								<img src="../images/doc_aggr.gif" width="20" height="20">
							</div>
						</td>
						<td>
							<a href="docPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
							 文档类权限 </a>
						</td>
						<td>
							<div align="center">
								<img src="../images/doc_aggr.gif" width="20" height="20">
							</div>
						</td>
						<td>
							<a href="siteappPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
							 应用类权限 </a>
						</td>
					</tr>
				</table>
			<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
				  	<input name="resid" value="<%=resid%>" type=hidden>
				  	<input name="roleid" value="<%=roleid%>" type=hidden>
				  	<input name="role_type" value="<%=role_type%>" type=hidden>
				  <tr>
			        <td height='30' colspan="3">
			         <LEGEND align=left><strong><FONT size=2>授予操作项</FONT></strong></LEGEND>
			        </td>
			      </tr>	
			       <%boolean flag1;
					//判断给定的角色是否是超级管理员角色
					flag1 = accesscontroler.isAdministratorRole(rolename);
					if (flag1 == true) {%>
							  <tr>
			        <td>
			        <input type="checkBox" hideFocus=true name="checkBoxAll4" onClick="checkAll4('checkBoxAll4','alloper4')"checked disabled="true">
			        </td>
			        <td>>>>>全选</td>
			        <td>&nbsp;<input name="isRecursion4" onclick="" type="checkbox"checked disabled="true">递归授权子站点</td>
			      </tr>
			      <pg:list requestKey="operList" needClear="false">
			      <tr class="tr">
			        <td>
			        <input name="alloper4" type="checkbox"
			        <%
			        	String opId = dataSet.getString(Integer.parseInt(rowid),"id");
			        	
			        	for(int i = 0; i < hasOper.size(); i ++)
			        	{
			        		RoleresopKey id = (RoleresopKey)hasOper.get(i);
			        	
			        		if(id.getOpId().equals(opId) && id.getRoleId().equals(roleid))
			        		{
			        			out.println("checked");
			        			break;
			        		}
			        			
			        	}
			        %>
			        value="<pg:cell colName="id"/>" onclick="changebox4(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')"checked disabled="true">
			        </td>
			        <td><pg:cell colName="name"/></td>
			        <td>&nbsp;</td>
			        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
			      </tr>
			       </pg:list>
					<%}else{%>
							  <tr>
			        <td>
			        <input type="checkBox" hideFocus=true name="checkBoxAll4" onClick="checkAll4('checkBoxAll4','alloper4')">
			        </td>
			        <td>>>>>全选</td>
			        <td>&nbsp;<input name="isRecursion4" onclick="" type="checkbox">递归授权子站点</td>
			      </tr>
			      <pg:list requestKey="operList" needClear="false">
			      <tr class="tr" 
			      <%
					String opId = dataSet.getString(Integer.parseInt(rowid),"id");//System.out.println(resid+"|"+opId+"|"+resTypeId);
					if(!accesscontroler.checkPermission(resid,opId,resTypeId))
					out.print(" disabled=\"true\"");
				  %>
			      >
			        <td>
			        <input name="alloper4" type="checkbox"
			        <%
			        	for(int i = 0; i < hasOper.size(); i ++)
			        	{
			        		RoleresopKey id = (RoleresopKey)hasOper.get(i);
			        	
			        		if(id.getOpId().equals(opId) && id.getRoleId().equals(roleid))
			        		{
			        			out.println("checked");
			        			break;
			        		}
			        			
			        	}
			        %>
			        value="<pg:cell colName="id"/>" onclick="changebox4(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
			        </td>
			        <td><pg:cell colName="name"/></td>
			        <td>&nbsp;</td>
			        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
			      </tr>
			       </pg:list>
					<%}%>
				
			       
			       <tr>
			        <td></td>
			        <td></td>
			        <td>
			        <input type="submit" value="确定" class="input" onclick="javascript:sitefileRecord(1,'<%=resTypeId%>'); return false;">
			        <input type="submit" value="关闭" class="input" onclick="back()">
			        </td>
			      </tr>
			    </table>
			</form>
			<div style="display:none">
			<IFRAME name="sitefile" width="0" height="0">
			</IFRAME>
			</div> 
		
		
</div>
</body>

</html>
