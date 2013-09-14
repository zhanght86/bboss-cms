
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,
				com.frameworkset.platform.resource.ResourceManager"%>
				
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,
				com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
   String orgId = request.getParameter("currOrgId");
   
	String isok =  (String)request.getAttribute("isOk");
	
	String resTypeId = request.getParameter("resTypeId");
	
	String resId = request.getParameter("resId");
	
	String resname = request.getParameter("title");
	if(resname != null)
		resname = URLDecoder.decode(resname);
	String roleId = request.getParameter("currRoleId");
	
	String role_type = request.getParameter("role_type");
	
	String username = request.getParameter("username");
	if(username != null)
		username = URLDecoder.decode(username);
	
	//设置到request
	session.setAttribute("resname",resname) ;
	

	boolean flag = false;

	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List list = resManager.getOperations(resTypeId);
	List hasOper = operManager.getOperResRoleList(role_type,roleId,resId,resTypeId);
	if(list == null){
		list = new ArrayList();
	}	
	request.setAttribute("operList",list);
	if(hasOper == null){
		hasOper = new ArrayList();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>    
<title>权限授予</title>
<pg:config enablecontextmenu="true" enabletree="false"/>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/common/scripts/esbCommon.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
  
<SCRIPT LANGUAGE="JavaScript"> 
	var ok = <%=isok%>;
	var api = null, W = null;
	if(ok!=null){
		alert("授予操作项成功！");
	}//alert(document.URL);
	
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
		    var url =  "<%=request.getContextPath()%>/accessmanager/siteAppAuthorization.page?isRecursion="+tt+"&resTypeId="+id;
		    $.ajax({
				   type: "POST",
					url : url,
					data :formToJson("#Form1"),
					dataType : 'json',
					async:false,
					beforeSend: function(XMLHttpRequest){
							
				      		blockUI();	
				      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				      				 	
						},
					success : function(responseText){
						//去掉遮罩
						unblockUI();
						if(responseText=="success"){
							
							$.dialog.alert("操作成功",function(){	
									
							},api);													
						}else{
							$.dialog.alert("操作出错",function(){},api);
						}
						
						
						
						
					}
				  });
		    
		//Form1.action="<%=request.getContextPath()%>/accessmanager/siteAppAuthorization.page?isRecursion="+tt+"&resTypeId="+id;
			//Form1.submit();
				 		
	}
</SCRIPT>
</head>

<body class="contentbodymargin">
<div id="contentborder">
	
			<form target="channel" name="Form1" id="Form1" action="" method="post" >
			<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
				  	<input name="resTypeId" value="<%=resTypeId%>" type=hidden>
				  	<input name="resid" value="<%=resId%>" type=hidden>
				  	<input name="roleid" value="<%=roleId%>" type=hidden>
				  	<input name="role_type" value="<%=role_type%>" type=hidden>
					<input name="resname" value="<%=resname%>" type=hidden>
				  <tr>
			        <td height='30' colspan="5">
			         <LEGEND align=left><strong><FONT size=2><%if(username != null){out.print(username + " >> ");}%>授予操作项</FONT></strong></LEGEND>
			        </td>
			      </tr>	
				  <tr>
			        <td>
			        <input type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','alloper')">
			        </td>
			        <td align="center" ><strong>授予操作项</strong></td>
			        <td align="center" ><%if(role_type.equals("user")){%><strong>资源来源</strong><%}%></td>
			        <td align="center" ><strong>操作项描述</strong></td>
			        <td>&nbsp;<input name="isRecursion" onclick="" type="checkbox" style="display:none"></td>
			      </tr>
			      <pg:list requestKey="operList" needClear="false">
			      <tr class="tr" 
			      <%
			      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
			      if(!accesscontroler.checkPermission(resId,opId,resTypeId))
			      out.print(" disabled=\"true\"");
			      String returnStr = "";
			      if(role_type.equals("user")){
			       returnStr = accesscontroler.getUserRes_jobRoleandRoleandSelf(orgId,roleId,resname,resTypeId,resId,opId);
			       }
			      String c = "";
			      if(returnStr.equals("1"))
			      	c = "1";
			      %>
			      >
			        <td>
			        <input name="alloper" type="checkbox"
			        <%
			        	for(int i = 0; i < hasOper.size(); i ++)
			        	{
			        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
			        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
			        		{
			        			out.println("checked");
			        			break;
			        		}	
			        	}
			        	
			            String returnStrResouce = "";
			            if(role_type.equals("user")){
			                returnStrResouce = accesscontroler.getSourceUserRes_jobRoleandRoleandSelf(orgId,roleId,resname,resTypeId,resId,opId);
			                if(returnStr.equals("1")){
			                    out.println(" disabled=\"true\" checked ");
			                }
			            }
			        %>
			        value="<pg:cell colName="id"/>" onclick="changebox1(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
			        </td>
			        <td><pg:cell colName="name"/></td>
			        <td><%=returnStrResouce%></td>
			        <td><pg:cell colName="description"/></td>
			        <td>&nbsp;</td>
			        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
			      </tr>
			       </pg:list>
			       
			       <tr>
			        <td></td>
			        <td></td>
			        <td></td>
			        <td></td>
			        <td>
			        <input type="submit" value="确定" class="input" onclick="javascript:okRecord(1,'<%=resTypeId%>'); return false;" 
			        
			        >
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


