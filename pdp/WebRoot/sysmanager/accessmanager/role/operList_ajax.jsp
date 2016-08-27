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

	String role_type = (String)session.getAttribute("role_type");
	String resTypeId = request.getParameter("resTypeId");
	String resId = request.getParameter("resId");
	String resName=request.getParameter("resName");
	//out.println(resName);
	String roleId = (String)session.getAttribute("currRoleId");
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
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<SCRIPT LANGUAGE="JavaScript"> 


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
//-------------------------------------------------
var http_request = false;
			//初始化，指定处理的函数，发送请求的函数
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
						//alert(http_request.readyState == 4);
						//alert(http_request.responseText);
					}
					else{
						//alert("对不起，服务器错误");
					}
				}
			}
			
			function changebox(opid,flag){
				//alert(1);
				var resId = "<%=resId%>";
				var resTypeId = "<%=resTypeId%>";
				var roleId = "<%=roleId%>";
				var resName="<%=resName%>";
				var role_type ="<%=role_type%>";
				//send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveRRO.jsp?resId='+resId+'&resTypeId='+resTypeId+'&opId='+opid+'&roleId='+roleId+"&flag="+flag+"&resName="+resName+"&role_type="+role_type);
		
			}
			function changebox1(currCheck,priority,opid){				
				//alert(2)
				var resId = "<%=resId%>";
				var resTypeId = "<%=resTypeId%>";
				var roleId = "<%=roleId%>";
				var resName="<%=resName%>";
				var role_type ="<%=role_type%>";
				//if(currCheck.checked){
				//send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveRRO.jsp?resId='+resId+'&resTypeId='+resTypeId+'&opId='+opid+'&roleId='+roleId+"&flag=1"+"&resName="+resName+"&role_type="+role_type);
				//}else{
				//send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveRRO.jsp?resId='+resId+'&resTypeId='+resTypeId+'&opId='+opid+'&roleId='+roleId+"&flag=0"+"&resName="+resName+"&role_type="+role_type);
				
				//}
				setCheck(currCheck,priority);
			}
			
//gao.tang 2007.11.21
function sub(dealType){
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
		   
	if(confirm("确认提交吗？")){
		Form1.action="permission_handle.jsp";
		Form1.submit();
	}
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
<form name="Form1" target="theIframe" method="post" >
	<table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
	  <input name="resid" value="<%=resId%>" type=hidden>
	  <input name="resTypeId" value="<%=resTypeId%>" type=hidden>
	  <input name="roleid" value="<%=roleId%>" type=hidden>
	  <input name="resName" value="<%=resName%>" type=hidden>
	  <input name="role_type" value="<%=role_type%>" type=hidden>
      <tr class="tr">
        <td height="22" colspan="3" class="td" align="center"><b>授予操作项</b></td>
        </tr>
      <pg:list requestKey="operList" needClear="false">
      <tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" class="tr">
        <td width="25" class="td" height="22">
        <input name="alloper" type="checkbox"
        <%
        	String opId = dataSet.getString(Integer.parseInt(rowid),"id");
        	for(int i = 0; i < hasOper.size(); i ++)
        	{
        		//Roleresop op = (Roleresop)hasOper.get(i);
        		RoleresopKey id = (RoleresopKey)hasOper.get(i);
        		//if(op.getOpId().equals(opId))
        		if(id.getOpId().equals(opId) && id.getRoleId().equals(roleId))
        		{
        			out.println("checked");
        			break;
        		}
        			
        	}
        %>
        value="<pg:cell colName="id"/>" onclick="changebox1(this,'<pg:cell colName="priority" defaultValue=""/>','<pg:cell colName="id"/>')">
        </td>
        <td width="100" height="22" class="td"><pg:cell colName="name"/></td>
        <td height="22">&nbsp;</td>
        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
      </tr>
       </pg:list>
      <tr>
      	<td>&nbsp;</td>
      	<td>&nbsp;</td>
      	<td colspan="1" align="left">
      		<input type="submit" value="确定" onclick="javascript:sub(1); return false; " class="input" 
      		<%
        	if(accesscontroler.getUserID().equals(roleId))
        		out.print(" disabled=\"true\"");
        	%>
      		/>
      		<input name="but2" type="button" value="关闭" onclick="javascript:parent.window.close();parent.returnValue='ok';" class="input" />
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
</center>
</div>
<IFRAME name="theIframe" width="0" height="0" ></IFRAME>
</body>
</html>

