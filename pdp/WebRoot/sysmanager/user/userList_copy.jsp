<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.platform.sysmgrcore.entity.User,com.frameworkset.platform.sysmgrcore.manager.UserManager" %>

<%
	
	//Integer uid = (Integer) session.getAttribute("currUserId");//当前用户id
	String uid = (String)request.getAttribute("userId");
	
	String orgId = request.getParameter("orgId");
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(uid);
	String userName = user.getUserName();
	
	
%>
<html>
<head>    
 <title>属性容器</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">
	function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
		var cbs = true;
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(cbs){
			selectAll[0].checked=true;
		}else{
			selectAll[0].checked=false;
		}
	}		
	function dealRecord(dealType) {
	    var isSelect = false;
	    var outMsg;
	    
	    for (var i=0;i<orgForm.elements.length;i++) {
			var e = orgForm.elements[i];
			
			if (e.name == 'ID'){
				if (e.checked){
			       		isSelect=true;
			       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要复制( <%=userName%> )的权限给所选用户吗？";
	        	if (confirm(outMsg)){
	        	orgForm.action="../user/userManager.do?method=userResCopy&orgId=<%=orgId%>&userId=<%=uid%>";
				orgForm.submit();
				alert("用户权限复制成功！");
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
</SCRIPT>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
<body class="contentbodymargin" scroll="no">
	<fieldset>
	   <LEGEND align=left><strong><FONT size=2>提示</FONT></strong></LEGEND>
		
	    <table><tr><td>将复制当前用户的自身权限和所属角色给所选用户
	      </td>
	     </tr>
	    </table>
	  
     </fieldset>
<div id="contentborder" align="center">
	
    <form name = "orgForm" target="copy" method="post" action="">	
    <input type="hidden" name="jobId" value ="<%=request.getParameter("jobId")%>">
	<table width="100%"  cellpadding="1" cellspacing="1" class="thin">
	<tr><td height='40' class="detailtitle" align=center colspan=3><b>复制用户：<%=userName%>   的权限</b></td></tr>
		<pg:listdata dataInfo="UserResCopyList" keyName="UserResCopyList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="12"
				  scope="request"  
				  data="UserResCopyList" 
				  isList="false">
			      <tr class="labeltable_middle_td">
			      <!--设置分页表头-->
			      <td class="headercolor">
				  	<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
				  </td>
				  <td height='20' class="headercolor">用户帐号</td>
			   	  <td height='20' class="headercolor">用户实名</td>
				
				  			
			      </tr>
			      <pg:param name="orgId"/>
			     		      
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td class="detailcontent" colspan=100 align='center'>暂时没有用户</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      		<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">	      				
							
			      		    <td  align=center><input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="userId" defaultValue=""/>"></td>					
							<td  align=left>
							<pg:cell colName="userName" defaultValue=""/>
							</td>
							<td  align=left>
							<pg:cell colName="userRealname" defaultValue=""/>
							</td>
						</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="detailcontent" colspan=2 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			      	<td class="detailcontent" align='left'>
			      	
					  	
					 <input type="button" value="确定" class="input"
								onclick="javascript:dealRecord(1); return false;">
								
				</td>
			      </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>
<div style="display:none">
	<IFRAME name="copy" width="0" height="0"></IFRAME>
</div> 
</body>

</html>
