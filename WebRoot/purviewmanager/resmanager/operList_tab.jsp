<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@page import="java.util.Map"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    String rootpath = request.getContextPath();
	
	String isok =  (String)request.getAttribute("isOk");
	String resTypeId = request.getParameter("resTypeId2");
	String resid = request.getParameter("resId2");
	String resname = request.getParameter("title");
	String roleid = request.getParameter("resId");
	
	String role_type = "organization";


	String title = request.getParameter("title");
	
	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resid == null){
		resid = (String)request.getAttribute("resid");
	}
	Organization org = OrgCacheManager.getInstance().getOrganization(roleid);
	String orgname = org != null?org.getOrgName():"";
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();
	String isGlobal=request.getParameter("isGlobal");
	if(isGlobal == null) isGlobal = "false";
	List list = isGlobal.equals("true")?resManager.getGlobalOperations(resTypeId):resManager.getOperations(resTypeId);
	//List hasOper = operManager.getOperResRoleList(role_type,roleid,resid,resTypeId);
	Map hasOper = operManager.getResOperMapOfRole(roleid,resid,resTypeId,"organization");
	if(list == null){
		list = new ArrayList();
	}	
	request.setAttribute("operList",list);
	//if(hasOper == null){
	//	hasOper = new ArrayList();
	//}
	
	String resName2 = request.getParameter("resName2");
	//是否批量
	String isBatch = request.getParameter("isBatch");
	
%>

<html>
<head>    
<title>属性容器</title>
<SCRIPT LANGUAGE="JavaScript"> 
	var ok = <%=isok%>;
	if(ok!=null){
		alert("<pg:message code='sany.pdp.common.operation.success'/>");
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
			Form1.action="<%=rootpath%>/accessmanager/securityManager.do?method=resAuthorization&isRecursion="+tt+"&resTypeId="+id;
			Form1.submit();
				 		
	}
	
	function saveReolresop(){
		var obj = document.getElementsByName("alloper");
		var isRecursionObj = document.getElementsByName("isRecursion");
		var isRecursion = false;
		if(isRecursionObj[0].checked){
			isRecursion = true;
		}
		//alert(obj.length);
		var checks = "";
		var un_checks = "";
		for(var count = 0; count < obj.length; count++){
			if(!obj[count].disabled){
				//alert(obj[count].checked + " : " + obj[count].value);
				if(obj[count].checked){
					if(checks==""){
						checks = obj[count].value;
					}else{
						checks += "^#^" + obj[count].value;
					}
				}else{
					if(un_checks==""){
						un_checks = obj[count].value;
					}else{
						un_checks += "^#^" + obj[count].value;
					}
				}
			}
		}
		//alert("checks = " + checks);
		//alert("un_checks = " + un_checks);
		document.all("checks").value = checks;
		document.all("un_checks").value = un_checks;
		document.Form1.target = "saveHidden";
		document.Form1.action = "saveOrgRoleresop.jsp?resName2=<%=resName2%>&types=organization&resId=<%=resid%>&restypeId=<%=resTypeId%>&isBatch=<%=isBatch%>&isRecursion="+isRecursion;
		//alert(document.userForm.action);
		document.Form1.submit();

	}

</SCRIPT>
</head>

<body class="contentbodymargin">
<div id="">
	
			<form target="channel" name="Form1" action="" method="post" >
			<div class="title_box">
				<div class="rightbtn">
					<input name="isRecursion" onclick="" type="checkbox"><pg:message code="sany.pdp.resourcemanage.recursion.confer"/>
				</div>
				<strong>
				<%if(orgname != null){out.print(orgname + " >> ");}%><pg:message code="sany.pdp.resourcemanage.operation"/>
								</strong>
				
			</div>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
				  	<input name="resTypeId" value="<%=resTypeId%>" type=hidden>
				  	<input name="resid" value="<%=resid%>" type=hidden>
				  	<input name="roleid" value="<%=roleid%>" type=hidden>
				  	<input name="role_type" value="<%=role_type%>" type=hidden>
					<input name="resname" value="<%=resname%>" type=hidden>
					<input name="un_checks" value="" type=hidden>
					<input name="checks" value="" type=hidden>
				  <pg:header>
			        <th>
			        <input type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','alloper')">
			        </th>
			        <th><pg:message code="sany.pdp.resourcemanage.authority"/></th>
			      </pg:header>
			      <pg:list requestKey="operList" needClear="false">
			      <tr class="tr" >
			       <td>
			       <div align="center">
			        <input name="alloper" type="checkbox"
			      <%
			      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
			      //判断该资源是否受保护：true表示不受保护；false表示受保护的
	        	  boolean isUnProtected = accesscontroler.isUnprotected(resid,opId,resTypeId);
	        	  //判断是否是超级管理员拥有：true表示只有超级管理员才有权；false根据具体授权来决定
				  boolean isExclude = accesscontroler.isExcluded(resid,opId,resTypeId);
				  //只能是超级管理员拥有的权限
					if(isExclude){
						out.print(" disabled=\"true\" title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.purview.admin", request)+"\" ");
					}else if(isUnProtected){//不受保护的资源
						out.print(" disabled=\"true\" title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.purview.resource.protect.no", request)+"\" checked ");
					}else{
				      if(!accesscontroler.checkPermission(resid,opId,resTypeId)){
				      	out.print(" disabled=\"true\"");
				      }
	        		  if(hasOper.size() > 0 && hasOper.get(opId) != null)
	        		  {
	        			 out.println("checked");
	        		  }
        		   }
			        	
			       %>
			        value="<pg:cell colName="id"/>" onclick="changebox1(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
			        </div>
			        </td>
			        <td>
			        <div align="center"><pg:cell colName="name"/></div>
			        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
			        </td>
			      </tr>
			       </pg:list>
			       
			       <tr>
			        <td></td>
			        <td></td>
			      </tr>
			    </table>
			    <div align="center">
			    	<%
			        if(accesscontroler.isAdmin() && !accesscontroler.getChargeOrgId().equals(roleid)){
			        %>
			    	<a class="bt_1" onclick="saveReolresop();" ><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
			    	<%
			    	}
			    	%>
			    </div>
			</form>
			<div style="display:none">
			<IFRAME name="channel" width="0" height="0">
			</IFRAME>
			<IFRAME name="saveHidden" width="0" height="0">
			</IFRAME>
			</div> 
	
</div>
</body>

</html>


