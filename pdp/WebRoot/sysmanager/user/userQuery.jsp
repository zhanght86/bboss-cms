<%@include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
	String hasKey = (String)request.getAttribute("hasKey");
	if(hasKey == null)
	hasKey="0";	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理</title>
<SCRIPT LANGUAGE="JavaScript"> 
function query(){	
   if(document.all("userName").value.length<1){
   		alert("请输入查询内容");
   		return;
   }     
   document.forms[0].action="../user/userManager.do?method=userQuery";
   document.forms[0].submit();
}
</SCRIPT>
<link href="Images/style.css" rel="stylesheet" type="text/css">
</head>
<body class="middleleft">
<form name="form1" method="post" action="">
<table width="100%" border="0" class="table" cellpadding="0" cellspacing="1">
  <tr class="tr">
    <td class="td" align="center"><strong>用户查询</strong></td>
  </tr>
</table>
<table width="100%" class="table">
  
  <tr class="tr">
    <td class="td"><table class="table" width="100%%" height="25" border="0" cellpadding="0" cellspacing="0">
      <tr class="tr">
        <td class="td">用户名称</td>
        <td class="td"><input type="text" class="text" name="userName"></td>
        
        <td class="td"><input name="Submit22" type="button" class="input" onClick="query()" value="查询"></td>
        <td class="td"><input name="Submit3" type="reset" class="input" value="重置"></td>
      </tr>
    </table>    </td>
  </tr>
  <tr class="tr">
    <td height="25" class="td"><hr width="98%"></td>
  </tr>
  <%
  	if(hasKey.equals("1")){  
  %>
  <tr onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" class="tr">
    <td class="td"><table width="100%%" border="0" cellpadding="0" cellspacing="0" class="table">
      
      <pg:listdata dataInfo="UserSearchList" keyName="UserSearchList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="5"
				  scope="request"  
				  data="UserSearchList" 
				  url="../user/userManager.do"
				  isList="false">
			      <tr class="tabletop">
			      <!--设置分页表头-->				    
				    <td align="center">用户名称</td>
				    <td align="center">用户ID</td>    	     		    		    
				    <td align="center">基本信息</td>
			      </tr>
			      <pg:param name="method" value="userQuery"/>
			      <pg:param name="userName"/>
				  
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" class="labeltable_middle_tr_01">
			      	<td colspan=100 align='center'>暂时没有用户</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
						<tr class="labeltable_middle_tr_01"  >		      				
							<td align=center><pg:cell colName="userName" defaultValue=""/></td>
							<td align=center><pg:cell colName="userSn" defaultValue=""/></td>
							<td align=center><pg:cell colName="userRealname" defaultValue=""/></td>
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" class="labeltable_middle_tr_01">
			      	<td colspan=4 align='center'><pg:index/></td>
			      	<td align='left'>					
				</td>
			      </tr>   		
			      <input name="queryString" value="<pg:querystring/>" type="hidden">
		</pg:pager>
    </table>
    </td>
  </tr>
  <%
	}  
  %>
</table>
</form>
<%@ include file="../sysMsg.jsp"%>
</body>
</html>

