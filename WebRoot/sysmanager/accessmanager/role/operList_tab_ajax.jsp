<!--机构资源的操作列表，由于要递归所以分开页面 -->
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
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
	 //判断操作是否成功
	String isok =  (String)request.getAttribute("isOk");
	String resTypeId = request.getParameter("restypeId");
	String resId = request.getParameter("restypeId");
	
	String roleId = (String)session.getAttribute("currRoleId");
	String resName=request.getParameter("resName");

	String role_type = (String)session.getAttribute("role_type");
	String resPath = request.getParameter("resPath");
	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resId == null){
		resId = (String)request.getAttribute("resId");
	}
	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List list = resManager.getOperations(resTypeId);
	
	
	
	
	List hasOper = operManager.getOperResRoleList(role_type,roleId,resName,resTypeId);
	
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
<SCRIPT LANGUAGE="JavaScript"> 
	var ok = <%=isok%>;
	if(ok!=null){
		alert("授予操作项成功！");
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
	
	//机构授权
	function okRecord(dealType) {
		
		    var isSelect = false;
		    var outMsg;
			    
		    for (var i=0;i<Form1.elements.length;i++) {
				var e = Form1.elements[i];
					
				if (e.name == 'alloper'){
					if (e.checked){
			       		isSelect=true;
			       		break;
				    }
				 }
				
		    }
		   
			Form1.action="<%=rootpath%>/accessmanager/roleManager.do?method=tabAuthorization";
			Form1.submit();
				
	}

</SCRIPT>
  <%@ include file="/include/css.jsp"%>
<body class="contentbodymargin">
<div id="contentborder">
<fieldset>
	   <LEGEND align=left><strong><FONT size=2>帮助</FONT></strong></LEGEND>
		<%
	          boolean flag;
	          AccessControl accesscon = AccessControl.getInstance();
	          flag = accesscon.allowIfNoRequiredRoles(resTypeId);
	          //System.out.println("hehelalal" + flag);
	          if(flag == true)
	          {
	    %>
	    <table>
	    <tr><td>tab资源为各模块顶部的可选功能项</td></tr>
	    <tr>
	       <td>该资源在没有授予任何角色或用户（即下面的复选框都处于未被选中状态）的情况下,允许访问该资源，一旦授给某个用户或角色后，其它角色或用户也必须授权后才能访问
	       </td>
	     </tr>
	    </table>
	    <%
        }else{
        %>
	    <table align=left>
	    <tr><td>tab资源为各模块顶部的可选功能项</td></tr>
	    <tr>
	    <td>必须对该资源授权才可以访问该资源，在没有授权的情况下是不能访问该资源</td></tr></table>
        <%}%>
        </fieldset>
<center>
<form name="Form1" target="tab" action="" method="post" >
<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
	   	<input name="resTypeId" value="<%=resTypeId%>" type=hidden>
	  	<input name="resid" value="<%=resId%>" type=hidden>
	  	<input name="roleid" value="<%=roleId%>" type=hidden>
	  	<input name="role_type" value="<%=role_type%>" type=hidden>
	  		<input name="resName" value="<%=resName%>" type=hidden>
	  <tr>
	  <td width="10%"></td>
	  <td width="70%"><strong>给tab资源:<%=resPath%>(<%=resName%>)</strong></td>
	
	  </tr>
      <tr class="tr">
        <td class="td" width="25" height="22" >&nbsp;</td>
        <td class="td" width="40%" height="22" >授予操作项
        <input name="isRecursion" onclick="isRecursionClick()" 
        type="hidden">
        </td>
      
      </tr>
      <pg:list requestKey="operList" needClear="false">
      <tr class="tr" 
      <%
      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
      if(!accesscontroler.checkPermission(resName,opId,resTypeId))
      out.print(" disabled=\"true\"");
      %> 
      >
        <td width="25" class="td" height="22">
        <input name="alloper" type="checkbox"
        <%
        	for(int i = 0; i < hasOper.size(); i ++)
        	{
        		//Roleresop op = (Roleresop)hasOper.get(i);
        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
        		//if(op.getOpId().equals(opId))
        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
        		{
        			out.println("checked");
        			break;
        		}
        			
        	}
        %>
        value="<pg:cell colName="id"/>" onclick="changebox1(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
        </td>
        <td width="80" height="22" class="td"><pg:cell colName="name"/></td>
       
        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
      </tr>
       </pg:list>
      <tr>
      
        <td></td>
        <td align="right">
        <input type="submit" value="确定" class="input" onclick="javascript:okRecord(1); return false;" 
        <%
        if(accesscontroler.getUserID().equals(roleId))
        out.print(" disabled=\"true\"");
        %>
        >
        </td>
      </tr>
    </table>
    <table width="35%" border="0" align="right" cellpadding="0" cellspacing="0" >
        <tr>
          <td><div align="center">
            
          </div></td>
          <td><div align="center">
           
          </div></td>
        </tr>
    </table>
</form>
<div style="display:none">
	<IFRAME name="tab" width="0" height="0"></IFRAME>
</div> 
</center>
</div>
</body>
</html>

