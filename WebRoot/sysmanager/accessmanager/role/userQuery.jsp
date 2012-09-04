<%@include file="../../include/global1.jsp"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
	String hasKey = (String)request.getAttribute("hasKey");
	if(hasKey == null)
	hasKey="0";
	/*
	String userName = (String)request.getAttribute("userName");
	if(userName == null)
	hasKey="0";
	*/
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../sysmanager/include/pager.js" type="text/javascript"></script>
<SCRIPT LANGUAGE="JavaScript">

var flag = <%=hasKey%>; 
function query(){	
   if(document.all("userName").value.length<1){
   		alert("请输入查询内容");
   		return;
   }
   if(flag == ""){
   		document.forms[0].action="../../../accessmanager/roleManager.do?method=userQuery";
   }
   else
   		document.forms[0].action="roleManager.do?method=userQuery";
   document.forms[0].submit();
}
function getUserInfo(e,userId){
	
	document.all("selectId").value = userId;
}
</SCRIPT>
<link href="Images/style.css" rel="stylesheet" type="text/css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
</head>
<body class="middleleft">
<form name="form1" method="post" action="">
<table width="100%" border="0" class="table" cellpadding="0" cellspacing="1">
  <tr class="tr">
    <td class="td" align="center"><strong>用户查询</strong></td>
  </tr>
</table>
<table width="100%" >
  
  <tr class="tr">
    <td width="52%" class="td"><table class="table" width="100%%" height="25" border="0" cellpadding="0" cellspacing="0">
      <tr class="tr">
        <td class="detailtitle" width="16%"><div align="center">用户名称</div></td>
        <td class="detailcontent" width="30%">
          <div align="left">
            <input type="text" class="text" name="userName">
          </div></td>
        
        <td class="td">
              <div align="left">              </div></td>
        </tr>
    </table>    </td>
  </tr>
  <tr class="tr">
    <td height="25" class="td"><div align="center">
        <input name="Submit22" type="button" class="text" onClick="query()" value="查询">
        <input name="Submit3" type="reset" class="text" value="重置">
    </div></td>
  </tr>
  <%
  	if(hasKey.equals("1")){  
  %>
   <td height='30' class="detailtitle" align=center colspan=4>
  <b>用户列表</b></td>
      
      <pg:listdata dataInfo="UserSearchList" keyName="UserSearchList"/>
		<!--分页显示开始,分页标签初始化-->
		<td width="4%"><pg:pager maxPageItems="15"
				  scope="request"  
				  data="UserSearchList" 
				  url="roleManager.do"
				  isList="false">
			      <tr class="tabletop">
			      <!--设置分页表头-->				    
				   <th height='20' class="headercolor">用户名称</td>
				   <th height='20' class="headercolor">用户密码</td>    	     		    		    
				   <th height='20' class="headercolor">用户实名</td>
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
						<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="getUserInfo(this,'<pg:cell colName="userId" defaultValue=""/>')" >	      				
								      				
							<td class="tablecells" align=center><pg:cell colName="userName" defaultValue=""/></td>
							<td class="tablecells" align=center>******</td>
							<td class="tablecells" align=center><pg:cell colName="userRealname" defaultValue=""/></td>
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
</body>
<%@ include file="../../sysMsg.jsp"%>
</html>

