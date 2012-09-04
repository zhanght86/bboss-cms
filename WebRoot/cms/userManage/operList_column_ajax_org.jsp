<!--菜单资源的操作列表，由于要递归所以分开页面 -->
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	 //判断操作是否成功
	String isok =  (String)request.getAttribute("isOk");
	String role_type = (String)session.getAttribute("role_type");
	String resTypeId = request.getParameter("resTypeId");
	String resId = request.getParameter("resId");
	String roleId = (String)session.getAttribute("currRoleId");
	String resName=request.getParameter("resName");
	String menuPath  = request.getParameter("nodePath");
	//out.println(resName);
	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resId == null){
		resId = (String)request.getAttribute("resId");
	}
	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List list = resManager.getOperations(resTypeId);
	//List list = operManager.getOperList(resTypeId);
	
	List hasOper = operManager.getOperResRoleList(role_type,roleId,resId,resTypeId);
	//List hasOper = operManager.getOperResRoleList(roleId,resId,resTypeId);
	
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
			Form1.action="operList_column_ajax_do.jsp?isRecursion="+tt;
			Form1.target="hiddenFrame";
			Form1.submit();
				
	}

</SCRIPT>
  <link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><body class="contentbodymargin">
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
	    <table><tr><td>该资源在没有授予任何角色或用户（即下面的复选框都处于未被选中状态）的情况下,允许访问该资源，一旦授给某个用户或角色后，其它角色或用户也必须授权后才能访问
	      </td>
	     </tr>
	    </table>
	    <%
        }else{
        %>
	    <table align=left><tr><td>必须对该资源授权才可以访问该资源，在没有授权的情况下是不能访问该资源</td></tr></table>
        <%}%>
        </fieldset>
<center>
<form name="Form1" target="menu" action="" method="post" >
<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
	   	<input name="resTypeId" value="<%=resTypeId%>" type=hidden>
	  	<input name="resid" value="<%=resId%>" type=hidden>
	  	<input name="roleid" value="<%=roleId%>" type=hidden>
	  	<input name="role_type" value="<%=role_type%>" type=hidden>
	  	<input name="resName" value="<%=resName%>" type=hidden>
	  	<input name="menuPath" value="<%=menuPath%>" type=hidden>
      <tr class="tr">
        <td class="td" width="25" height="22" >&nbsp;</td>
        <td class="td" width="100" height="22" >授予操作项</td>
        <td height="22" class="td" >&nbsp;<input name="isRecursion" onclick="" type="checkbox">递归授权子菜单</td>
      </tr>
      <pg:list requestKey="operList" needClear="false">
      <tr class="tr"
      <%
      String opId = dataSet.getString(Integer.parseInt(rowid),"id");
      if(!accesscontroler.checkPermission(resId,opId,resTypeId))
      out.print(" disabled=\"true\"");
      %>
      >
        <td width="25" class="td" height="22">
        <input name="alloper" type="checkbox"
        <%
        	for(int i = 0; i < hasOper.size(); i ++)
        	{
        		RoleresopKey id = (RoleresopKey)hasOper.get(i);
        	
        		if(id.getOpId().equals(opId) && id.getRoleId().equals(roleId))
        		{
        			out.println("checked");
        			break;
        		}
        			
        	}
        %>
        value="<pg:cell colName="id"/>" onclick="changebox1(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
        </td>
        <td width="80" height="22" class="td"><pg:cell colName="name"/></td>
        <td height="22">&nbsp;</td>
        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
      </tr>
       </pg:list>
      <tr>
        <td></td>
        <td></td>
        <td>
        <input type="submit" value="确定" class="input" onclick="javascript:okRecord(1); return false;" 
        <%
        //if(accesscontroler.getUserID().equals(roleId))
        //out.print(" disabled=\"true\"");
        %>
        >
        <input type="button" value="关闭" class="input" onclick="javascript:window.close();" >
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
	<IFRAME name="menu" width="0" height="0"></IFRAME>
</div> 
</center>
</div>
</body>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
