<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.framework.*"%>
<%@page import="java.util.*,com.frameworkset.platform.cms.util.CMSUtil" %>
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
	String resid = request.getParameter("siteid");
	String roleid = (String)session.getAttribute("currRoleId");//System.out.println(roleid);
	String resname = request.getParameter("sitename");
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
<html >
<head>
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
	//模态窗口
	function openWin(url,swidth,sheight){
		var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
	 	return w;
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
	
	function changebox1(currCheck,priority,opid){
		try
		{
			setCheck(currCheck,priority);
		}
		catch(e)
		{
		}
	}
	//站点授权
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
		   
			Form1.action="<%=rootpath%>/accessmanager/securityManager.do?method=siteAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form1.submit();
				
	}
	//--------------------------------------------------------------------------------------------------------------------------------
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
	   	var o = document.getElementsByName("alloper1");
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
		try
		{
			setCheck(currCheck,priority);
		}
		catch(e)
		{
		}
	}
	
	
	//站点频道授权
	function sitecnlRecord(dealType,id) {
			var isSelect = false;
		    var outMsg;
			var tt;    
		    for (var i=0;i<Form2.elements.length;i++) {
				var e = Form2.elements[i];
					
				if (e.name == 'alloper1'){
					if (e.checked){
			       		isSelect=true;
			       		break;
				    }
				 }
				  if (e.name == 'isRecursion1'){
					if (e.checked){
			       		tt="1";
			       		
				    }else{
				    	tt ="0";
				    
				    }
				}
		    }
		   
			Form2.action="<%=rootpath%>/accessmanager/securityManager.do?method=sitecnlAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form2.submit();
	}
	//--------------------------------------------------------------------------------------------------------------------------------
	//复选框全部选中
	function checkAll3(totalCheck,checkName){
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
	
	function setCheck2(currCheck,priority)
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
	
	function changebox3(currCheck,priority,opid){
		try
		{
			setCheck(currCheck,priority);
		}
		catch(e)
		{
		}
	}
	
	
	//站点模板授权
	function sitetplRecord(dealType,id) {
		    var isSelect = false;
		    var outMsg;
			var tt;    
		    for (var i=0;i<Form3.elements.length;i++) {
				var e = Form3.elements[i];
					
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
			Form3.action="<%=rootpath%>/accessmanager/securityManager.do?method=sitetplAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form3.submit();
	}
	//--------------------------------------------------------------------------------------------------------------------------------
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
	
	function setCheck3(currCheck,priority)
	{
	   	var o = document.getElementsByName("alloper3");
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
		try
		{
			setCheck(currCheck,priority);
		}
		catch(e)
		{
		}
	}
	
	
	//站点模板视图授权
	function sitedocRecord(dealType,id) {
		
		    var isSelect = false;
		    var outMsg;
			var tt;    
		    for (var i=0;i<Form4.elements.length;i++) {
				var e = Form4.elements[i];
					
				if (e.name == 'alloper3'){
					if (e.checked){
			       		isSelect=true;
			       		break;
				    }
				 }
				  if (e.name == 'isRecursion3'){
					if (e.checked){
			       		tt="1";
			       		
				    }else{
				    	tt ="0";
				    
				    }
				}
		    }
			Form4.action="<%=rootpath%>/accessmanager/securityManager.do?method=sitedocAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form4.submit();
	}
	//--------------------------------------------------------------------------------------------------------------------------------
	//复选框全部选中
	function checkAll5(totalCheck,checkName){
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
	
	function changebox5(currCheck,priority,opid){
		try
		{
			setCheck(currCheck,priority);
		}
		catch(e)
		{
		}
	}
	
	
	//站点文件视图授权
	function sitefileRecord(dealType,id) {
			var isSelect = false;
		    var outMsg;
			var tt;    
		    for (var i=0;i<Form5.elements.length;i++) {
				var e = Form5.elements[i];
					
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
			Form5.action="<%=rootpath%>/accessmanager/securityManager.do?method=sitefileAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form5.submit();
				
	}
</SCRIPT>
</head>
<body>
<div id="contentborder">
<tab:tabContainer id="foo-site-container" selectedTabPaneId="site_foo">
	<tab:tabPane id="site_bar" tabTitle="站点类权限">
		<form target="site" name="Form1" action="" method="post" >
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
		        <input type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','alloper')">
		        </td>
		        <td>>>>>全选</td>
		        <td>&nbsp;<input name="isRecursion" onclick="" type="checkbox">递归授权子站点</td>
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
		<IFRAME name="site" width="0" height="0"></IFRAME>
		</div> 
	</tab:tabPane>
	
	<!-- ---------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="sitetpl_bar" tabTitle="模板类权限">
		<%
		
			 //判断操作是否成功
			 isok =  (String)request.getAttribute("isOk");
			 resTypeId = "sitetpl";
			 resid = request.getParameter("siteid");
			 roleid = (String)session.getAttribute("currRoleId");
			 resname=request.getParameter("sitename");
			 role_type = (String)session.getAttribute("role_type");
		
			if(resid == null){
				resid = (String)request.getAttribute("resid");
			}
			list = resManager.getOperations("sitetpl");
			hasOper = operManager.getOperResRoleList(role_type,roleid,resid,resTypeId);
			if(list == null){
				list = new ArrayList();
			}	
			request.setAttribute("operList1",list);
			if(hasOper == null){
				hasOper = new ArrayList();
			}
		
		%>
		<form target="sitetpl" name="Form3" action="" method="post" >
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
		        <input type="checkBox" hideFocus=true name="checkBoxAll3" onClick="checkAll3('checkBoxAll3','alloper2')">
		        </td>
		        <td>>>>>全选</td>
		        <td>&nbsp;<input name="isRecursion2" onclick="" type="checkbox">递归授权子站点</td>
		      </tr>
		      <pg:list requestKey="operList1" needClear="false">
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
		        value="<pg:cell colName="id"/>" onclick="changebox3(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
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
		        <input type="submit" value="确定" class="input" onclick="javascript:sitetplRecord(1,'<%=resTypeId%>'); return false;" 
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
			<IFRAME name="sitetpl" width="0" height="0"></IFRAME>
		</div> 
	</tab:tabPane>
	
	<!-- ---------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="sitefile_bar" tabTitle="文件视图类权限">
		<%
		
			 //判断操作是否成功
			 isok =  (String)request.getAttribute("isOk");
			 resTypeId = "sitefile";
			 resid = request.getParameter("siteid");
			 roleid = (String)session.getAttribute("currRoleId");
			 resname=request.getParameter("sitename");
			 role_type = (String)session.getAttribute("role_type");
		
			if(resid == null){
				resid = (String)request.getAttribute("resid");
			}
			list = resManager.getOperations("sitefile");
			hasOper = operManager.getOperResRoleList(role_type,roleid,resid,resTypeId);
			if(list == null){
				list = new ArrayList();
			}	
			request.setAttribute("operList1",list);
			if(hasOper == null){
				hasOper = new ArrayList();
			}
		
		%>
		<form target="sitefile" name="Form5" action="" method="post" >
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
		        <input type="checkBox" hideFocus=true name="checkBoxAll5" onClick="checkAll5('checkBoxAll5','alloper4')">
		        </td>
		        <td>>>>>全选</td>
		        <td>&nbsp;<input name="isRecursion4" onclick="" type="checkbox">递归授权子站点</td>
		      </tr>
		      <pg:list requestKey="operList1" needClear="false">
		      <tr class="tr"
		      <%
		      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
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
		        value="<pg:cell colName="id"/>" onclick="changebox5(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
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
		        <input type="submit" value="确定" class="input" onclick="javascript:sitefileRecord(1,'<%=resTypeId%>'); return false;" 
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
			<IFRAME name="sitefile" width="0" height="0"></IFRAME>
		</div> 
	</tab:tabPane>
	<!-- ---------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="sitecnl_bar" tabTitle="频道类权限">
		<%
		
			 //判断操作是否成功
			 isok =  (String)request.getAttribute("isOk");
			 resTypeId = "site.channel";
			 resid = request.getParameter("siteid");
			 roleid = (String)session.getAttribute("currRoleId");
			 resname=request.getParameter("sitename");
			 role_type = (String)session.getAttribute("role_type");
		
			if(resid == null){
				resid = (String)request.getAttribute("resid");
			}
			list = resManager.getOperations("channel");
			hasOper = operManager.getOperResRoleList(role_type,roleid,resid,resTypeId);
			if(list == null){
				list = new ArrayList();
			}	
			request.setAttribute("operList1",list);
			if(hasOper == null){
				hasOper = new ArrayList();
			}
		
		%>
		<form target="sitecnl" name="Form2" action="" method="post" >
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
		        <input type="checkBox" hideFocus=true name="checkBoxAll2" onClick="checkAll2('checkBoxAll2','alloper1')">
		        </td>
		        <td>>>>>全选</td>
		        <td>&nbsp;<input name="isRecursion1" onclick="" type="checkbox">递归授权子站点</td>
		      </tr>
		      <pg:list requestKey="operList1" needClear="false">
		      <tr class="tr"
		      <%
		      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
		      if(!accesscontroler.checkPermission(resid,opId,resTypeId))
		      out.print(" disabled=\"true\"");
		      %>
		      >
		        <td>
		        <input name="alloper1" type="checkbox"
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
		        <input type="submit" value="确定" class="input" onclick="javascript:sitecnlRecord(1,'<%=resTypeId%>'); return false;" 
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
			<IFRAME name="sitecnl" width="0" height="0"></IFRAME>
		</div> 
	</tab:tabPane>
	<!-- ---------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="sitedoc_bar" tabTitle="文档类权限">
		<%
		
			 //判断操作是否成功
			 isok =  (String)request.getAttribute("isOk");
			 resTypeId = "site.doc";
			 resid = request.getParameter("siteid");
			 roleid = (String)session.getAttribute("currRoleId");
			 resname=request.getParameter("sitename");
			 role_type = (String)session.getAttribute("role_type");
		
			if(resid == null){
				resid = (String)request.getAttribute("resid");
			}
			list = resManager.getOperations("channeldoc");
			hasOper = operManager.getOperResRoleList(role_type,roleid,resid,resTypeId);
			if(list == null){
				list = new ArrayList();
			}	
			request.setAttribute("operList1",list);
			if(hasOper == null){
				hasOper = new ArrayList();
			}
		
		%>
		<form target="siteview" name="Form4" action="" method="post" >
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
		        <input type="checkBox" hideFocus=true name="checkBoxAll4" onClick="checkAll4('checkBoxAll4','alloper3')">
		        </td>
		        <td>>>>>全选</td>
		        <td>&nbsp;<input name="isRecursion3" onclick="" type="checkbox">递归授权子站点</td>
		      </tr>
		      <pg:list requestKey="operList1" needClear="false">
		      <tr class="tr"
		      <%
		      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
		      if(!accesscontroler.checkPermission(resid,opId,resTypeId))
		      out.print(" disabled=\"true\"");
		      %>
		      >
		        <td>
		        <input name="alloper3" type="checkbox"
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
		       
		       <tr>
		        <td></td>
		        <td></td>
		        <td>
		        <input type="submit" value="确定" class="input" onclick="javascript:sitedocRecord(1,'<%=resTypeId%>'); return false;" 
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
			<IFRAME name="siteview" width="0" height="0"></IFRAME>
		</div> 
	</tab:tabPane>
	<!-- ---------------------------------------------------------------------------------------------------------- -->
	<tab:tabPane id="sitedoc_bar" tabTitle="应用类权限">
		<%
			String parentPath = "cms::menu://sysmenu$root/appManager$module";
	        SiteManagerImpl simpl = new SiteManagerImpl();
	        List items = simpl.getSubItems(parentPath);   
	        List isSetedList = simpl.getSetedAppInSite(resid); //resid 即为 siteid
	    %>
		<table width="100%" border="0" cellpadding="5" cellspacing="0" border="1" class="thin">
			<tr class="cms_report_tr">
			    <td align="center">应用名称</td>
			    <td align="center">应用ID</td>
			    <td align="center">授予授予</td>
			</tr>
			<%
			
			for(int i=0;i<items.size();i++){
				String flag = "";
				String itemId = "";
				String itemName = "";
				String itemPath = "";
				int dolarcount = 1;
				String type = "";
			    if(items.get(i)  instanceof  Item){
					Item item = (Item)items.get(i);
					
					itemId = item.getId();
					itemName = item.getName();
					itemPath = item.getPath();
					type = "item";
					dolarcount = CMSUtil.count(itemPath,'$') - 2;
					
				}else if(items.get(i) instanceof Module){
				    Module module = (Module)items.get(i);
					itemId = module.getId();
					itemName = module.getName();
					itemPath = module.getPath();
					type = "module";
					dolarcount = CMSUtil.count(itemPath,'$') - 2;
				}
				String setId = itemPath + "^" + itemId;
				for(int j=0;j<isSetedList.size();j++){							    
				    String sid = (String)isSetedList.get(j);					
				    if(setId.equals(sid)) {
				        flag = "checked";
				        break;
				    }
				}
				if(!flag.equals("checked"))
				{
					continue;
				}
	        %>
			<tr>
			    <td style="text-align:left">
			    <%
				for(int j = 0; j < dolarcount; j ++)
				{
					
					out.println( "<img src='../../images/space.gif'/>");
				}
				out.println("<img src='../../images/" + type +".gif'/>");
				out.println(itemName);
				%></td>
				<td><%=itemId%></td>
				<%
					if(flag.length()>0){
				%>
				<td onclick="openWin('operList_siteapp_tab.jsp?resId2=<%=resid%>&resTypeId2=<%=itemId%>&resTypeName=<%=itemId%>&title=<%=itemName%>&username=<%=itemName%>',600,500)">
				    <img src="../../images/new_doc.gif" border="0"  style="cursor:hand">
				</td>
				<%
					}else{
				%>
				<td>&nbsp;</td>
				<%
					}
				%>
			</tr>    
			<%
	        }
			%>        
		</table>
	</tab:tabPane>
</tab:tabContainer>
</div>
</body>
</html>

