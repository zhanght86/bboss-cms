<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    //判断操作是否成功
	String isok =  (String)request.getAttribute("isOk");
	String resTypeId = request.getParameter("resTypeId");
	String resid = request.getParameter("channelId");
	String resname = request.getParameter("channelName");
	String roleid = (String)session.getAttribute("currRoleId");
	String role_type = (String)session.getAttribute("role_type");
	
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
%>

<html>
<head>    
<title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
  <tab:tabConfig/>
<SCRIPT LANGUAGE="JavaScript"> 
	var ok = <%=isok%>;
	if(ok!=null){
		alert("授予操作项成功！");
		//刷新左边的资源树
	    parent.parent.orgTree.resource_bridge.location = parent.parent.orgTree.resource_bridge.location;
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
		   
			Form1.action="<%=rootpath%>/accessmanager/securityManager.do?method=channelAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form1.submit();
				 		
	}
<!-- -------------------------------------------------------------------------------------------------------------------	-->
	//复选框全部选中
	function checkAll2(totalCheck,checkName){
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
	
	function setCheck1(currCheck,priority)
	{
	   	var o = document.getElementsByName("alloper2");
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
	
	
	
	function changebox2(currCheck,priority,opid){
		setCheck1(currCheck,priority);
	}
	//频道授权
	function docRecord(dealType,id) {
		    var isSelect = false;
		    var outMsg;
			var tt;    
		    for (var i=0;i<Form2.elements.length;i++) {
				var e = Form2.elements[i];
					
				if (e.name == 'alloper2'){
					if (e.checked){
			       		isSelect=true;
			       		break;
				    }
				 }
				  if (e.name == 'isRecursion2'){
					if (e.checked){
			       		tt="1";
			       		
				    }else{
				    	tt ="0";
				    
				    }
				}
		    }
		   
			Form2.action="<%=rootpath%>/accessmanager/securityManager.do?method=docAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form2.submit();
				 		
	}
	

</SCRIPT>
</head>

<body class="contentbodymargin">
<div id="contentborder">
	<tab:tabContainer id="foo-channel-container" selectedTabPaneId="channel_foo">
		<tab:tabPane id="cnl_bar" tabTitle="频道类权限">
			<form target="channel" name="Form1" action="" method="post" >
			<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
				  	<input name="resTypeId" value="<%=resTypeId%>" type=hidden>
				  	<input name="resid" value="<%=resid%>" type=hidden>
				  	<input name="roleid" value="<%=roleid%>" type=hidden>
				  	<input name="role_type" value="<%=role_type%>" type=hidden>
				  <tr>
			        <td height='30' colspan="3">
			         <LEGEND align=left><strong><FONT size=2>授予操作项</FONT></strong></LEGEND>
			        </td>
			      </tr>	
				  <tr>
			        <td>
			        <input type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','alloper')">
			        </td>
			        <td>>>>>全选</td>
			        <td>&nbsp;<input name="isRecursion" onclick="" type="checkbox">递归授权子频道</td>
			      </tr>
			      <pg:list requestKey="operList" needClear="false">
			      <tr class="tr"
			      <%
			      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
			      if(!accesscontroler.checkPermission(resid,opId,resTypeId))
			      out.print(" disabled=\"true\"");
			      %>
			      >
			        <td>
			        <input name="alloper" type="checkbox"
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
			        //if(accesscontroler.getUserID().equals(roleid))
			        //out.print(" disabled=\"true\"");
			        %>
			        >
			        </td>
			      </tr>
			    </table>
			</form>
			<div style="display:none">
			<IFRAME name="channel" width="0" height="0">
			</IFRAME>
			</div> 
		</tab:tabPane>
	<!-- ----------------------------------------------------------------------------	-->
		<tab:tabPane id="doc_bar" tabTitle="文档类权限">
			<%
			    //判断操作是否成功
				isok =  (String)request.getAttribute("isOk");
				resTypeId = "channeldoc";
				resid = request.getParameter("channelId");
				resname = request.getParameter("channelName");
				roleid = (String)session.getAttribute("currRoleId");
				role_type = (String)session.getAttribute("role_type");
				
				
				if(resid == null){
					resid = (String)request.getAttribute("resid");
				}
				
				
				list = resManager.getOperations("channeldoc");
				hasOper = operManager.getOperResRoleList(role_type,roleid,resid,resTypeId);
				if(list == null){
					list = new ArrayList();
				}	
				request.setAttribute("operList",list);
				if(hasOper == null){
					hasOper = new ArrayList();
				}
			%>
			<form target="channeldoc" name="Form2" action="" method="post" >
			<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
				  	<input name="resid" value="<%=resid%>" type=hidden>
				  	<input name="roleid" value="<%=roleid%>" type=hidden>
				  	<input name="role_type" value="<%=role_type%>" type=hidden>
				  <tr>
			        <td height='30' colspan="3">
			         <LEGEND align=left><strong><FONT size=2>授予操作项</FONT></strong></LEGEND>
			        </td>
			      </tr>	
				  <tr>
			        <td>
			        <input type="checkBox" hideFocus=true name="checkBoxAll2" onClick="checkAll2('checkBoxAll2','alloper2')">
			        </td>
			        <td>>>>>全选</td>
			        <td>&nbsp;<input name="isRecursion2" onclick="" type="checkbox">递归授权子频道</td>
			      </tr>
			      <pg:list requestKey="operList" needClear="false">
			      <tr class="tr"
			      <%
			      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
			      if(!accesscontroler.checkPermission(resid,opId,resTypeId))
			      out.print(" disabled=\"true\"");
			      %>
			      >
			        <td>
			        <input name="alloper2" type="checkbox"
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
			        value="<pg:cell colName="id"/>" onclick="changebox2(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
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
			        <input type="submit" value="确定" class="input" onclick="javascript:docRecord(1,'<%=resTypeId%>'); return false;" 
			        <%
			        //if(accesscontroler.getUserID().equals(roleid))
			        //out.print("disabled=\"true\"");
			        %>
			        >
			        </td>
			      </tr>
			    </table>
			</form>
			<div style="display:none">
			<IFRAME name="channeldoc" width="0" height="0">
			</IFRAME>
			</div> 
		</tab:tabPane>
	</tab:tabContainer>
</div>
</body>

</html>


