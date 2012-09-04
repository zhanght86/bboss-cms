
<%@page import="com.frameworkset.platform.security.AccessControl"%><%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*" %>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	String dealType = request.getParameter("dealType");
	if(dealType==null)dealType="";
	String orgId = request.getParameter("orgId");
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org = orgManager.getOrgById(orgId);
	String orgName = org.getRemark5(); 
	String parentId = org.getParentId();
	
	
%>
<html>
<head>    
 <title>属性容器</title>
 <script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
<SCRIPT language="javascript">
	
	//var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	function getOrgInfo(e,orgId){

		document.all("selectId").value = orgId;
	}	
	function dealRecord(dealType) {
	    var isSelect = false;
	    var outMsg;
	    document.forms[0].dealType.value=dealType;
	    for (var i=0;i<Organzition.elements.length;i++) {
			var e = Organzition.elements[i];
			
			if (e.name == 'ID'){
				if (e.checked){
			       		isSelect=true;
			       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    	    
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	    		
	        	if (confirm(outMsg)){
	        	//getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=<%=orgId%>&collapse=<%=orgId%>&request_scope=session";
	        	//modify by ge.tao
	        	//2008-01-14
	        	Organzition.action="<%=rootpath%>/orgmanager/org.do?method=deletesuborg";
				Organzition.submit();
				
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
	function updateAfter(){
		//getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=<%=orgId%>&collapse=<%=orgId%>&request_scope=session";
		//modify by ge.tao
	    //2008-01-14
	    //关键是 parentId
	    getPropertiesContent().location.href = "<%=rootpath%>/sysmanager/orgmanager/orginfo.jsp?orgId=<%=orgId%>&action=update&parentId=<%=parentId%>&resName=";
	    
	    
	}
</SCRIPT>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
<body class="contentbodymargin" onLoad="<%=dealType.equals("1")?"updateAfter()":""%>" scroll="no">
<div id="contentborder" align="center">
	<form name = "Organzition" method="post" action="">	
    <input type="hidden" name="dealType" value="">
    <input type="hidden" name="orgId" value="<%=orgId%>">
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
	<tr><td  class="detailtitle" align=center colspan=5><b>[<%=orgName%>] 的子机构列表</b></td></tr>
		<pg:listdata dataInfo="SbOrgList" keyName="SubOrgList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="18"
				  scope="request"  
				  data="SubOrgList" 
				  isList="false">
			      <tr >
			      <!--设置分页表头-->
			       <td class="headercolor" width="10">
				  	<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
				  </td>
			     <!--  <pg:header>			      	
	     		    <pg:title className="tablecells" title="序号" align="left" />	     		  
			    	<pg:title className="tablecells" title="机构名称" align="left"/>
			    	<pg:title className="tablecells" title="机构代码" align="left"/> 
			      </pg:header>-->
			     <input class="text" type="hidden" name="selectId">
			      <td class="headercolor">机构名称</td>
			      <td class="headercolor" colspan="2">机构编号</td>
			     
			      </tr>
			      <pg:param name="orgId"/>
			     		      
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td class="detailcontent" colspan=100 align='center'>暂时没有直属子机构</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      		<tr class="labeltable_middle_tr_01" onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >	      				
						
			      		    <td class="tablecells" align="left"><input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="orgId" defaultValue=""/>"></td>										
							<td class="tablecells" align="left">
							
							<pg:null colName="remark5"><pg:cell colName="orgName"/></pg:null>
							<pg:notnull colName="remark5">
							<pg:equal colName="remark5" value=""><pg:cell colName="orgName"/></pg:equal>
							<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
							</pg:notnull>
						
							</td>
							<td class="tablecells" align="left" colspan="2"><pg:cell colName="orgnumber" defaultValue=""/></td>																											    					    							    				    		
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="detailcontent" colspan=3 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			      	<td class="detailcontent" align='left'>
			      	
				<% 
					if(accessControl.isAdmin()){
				%>
					<input type="button" value="删除" class="input"
								onclick="javascript:dealRecord(1)">
				<%} %>
								
				</td>
			
			      </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>   

</body>

</html>

