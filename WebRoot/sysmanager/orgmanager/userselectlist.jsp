<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%String orgId=request.getParameter("orgId");%>
<html>
<head>    
 <title>属性容器</title>
<SCRIPT language="javascript">

	
	function checkAll(totalCheck,checkName){
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
	//各个删除按钮行为
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
	function returnList(){
		document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=showOrgallJob&orgId=<%=orgId%>";
		document.forms[0].submit();	         
	}
</SCRIPT>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<body class="contentbodymargin">

<div id="contentborder" align="center">
    <form name = "userForm" method="post" action="">	
    <input type="hidden" name="orgId" value ="<%=request.getParameter("orgId")%>">
	<table width="100%"  cellpadding="1" cellspacing="1" >
	<tr><td class="detailtitle" align=center colspan=5><b>可选用户列表</b></td></tr>
		<pg:listdata dataInfo="OrgSubUserList" keyName="OrgSubUserList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10"
				  scope="request"  
				  data="OrgSubUserList" 
				  isList="false">
			      <tr class="labeltable_middle_td">
			      <!--设置分页表头-->
			      <th class="tablecells" width="10">
				  	<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
				  </th>
			      <pg:header>			        
	     		    <pg:title className="tablecells" title="序号"/>
	     		    <pg:title className="tablecells" title="登录名"/>
			    	<pg:title className="tablecells" title="登录密码"/>
			    	<pg:title className="tablecells" title="用户名"/> 
			    			    
			      </pg:header>
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
			      		<tr height="18px" >	
			      		    <td class="tablecells" align=center><input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="userId" defaultValue=""/>"></td>					
							<td class="tablecells" align=center><pg:cell colName="userSn" defaultValue=""/></td>
							<td class="tablecells" align=center><pg:cell colName="userName" defaultValue=""/></td>
							<td class="tablecells" align=center><pg:cell colName="userPassword" defaultValue=""/></td>
							<td class="tablecells" align=center><pg:cell colName="userRealname" defaultValue=""/></td>																											    					    							    				    		
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="detailcontent" colspan=4 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			      	<td class="detailcontent" align='left'>
			      	
					  <input name="Submit" type="button" class="input" value="返回" onclick=" returnList()">
            		  <input name="Submit" type="button" class="input" onClick="adduser()" value="保存">
								
				</td>
			      </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>
</body>

</html>
