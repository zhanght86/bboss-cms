<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
String orgId=request.getParameter("orgId");
String remark5 = request.getParameter("remark5");
String orgnumber = request.getParameter("orgnumber");
if(remark5 == null)remark5="";
if(orgnumber == null)orgnumber="";
%>
<html>
<head>    
 <title>与岗位关联的机构列表</title>
<base target="_self">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<body class="contentbodymargin" scroll="no">
<div id="contentborder" align="center" >
<form name="Org" action="" method="post">

<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
  <tr>
    <td>机构名称</td>
    <td class="detailcontent"><input type="text" name="remark5" value="<%=remark5%>"></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td >机构编号</td>
    <td class="detailcontent"><input type="text" name="orgnumber" value="<%=orgnumber%>"></td>
    <td>
    	<div align="center">
            <input name="Submit4" type="submit" class="input" value="查询">                
            <input name="Submit22" type="button" class="input" value="重置" onclick="document.forms[0].remark5.value='';document.forms[0].orgnumber.value='';" >
            <input name="col" type="button" class="input" value="关闭" onclick="window.close();">
        </div>
    </td>
  </tr>      
</table>
<hr width="100%">	    
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
	<tr><td height='30' align=center colspan=4><b>机构列表</b></td></tr>
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.OrgSetJobList" keyName="OrgList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="15"
				  scope="request"  
				  data="OrgList" 
				  isList="false">
			      <tr >
			      <!--设置分页表头-->
			      	<td class="headercolor">机构名称</td>
			      	<input class="text" type="hidden" name="selectId">
			      	<td class="headercolor">机构编号</td>
			      	<td class="headercolor">机构排序号</td>
			      	<td class="headercolor">机构描述</td>
			      	
			     
			      </tr>
			      <pg:param name="orgName"/>
			      <pg:param name="code"/>
			      <pg:param name="remark5"/>
			      <pg:param name="orgnumber"/> 		
			      <pg:param name="jobId"/>       
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td colspan=100 align='center'>暂时没有接收单位</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      		<%
			      		String thisOrgId = dataSet.getString("orgId");
			      		String thisParentId = dataSet.getString("parentId");
			      		%>
			      		<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onClick="" >	      				
										
							<td  align=left class="tablecells">
							<pg:null colName="remark5"><pg:cell colName="orgName"/></pg:null>
							<pg:notnull colName="remark5">
							<pg:equal colName="remark5" value=""><pg:cell colName="orgName"/></pg:equal>
							<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
							</pg:notnull></td>
							<td  align=left class="tablecells"><pg:cell colName="orgnumber" defaultValue=" "/></td>
							<td  align=left class="tablecells"><pg:cell colName="orgSn" defaultValue=" "/></td>							
							<td  align=left class="tablecells">
							<pg:notnull colName="orgdesc"><pg:cell colName="orgdesc" defaultValue=" "/></pg:notnull>
							
							<pg:null colName="orgdesc">没有描述</pg:null>
							<pg:equal colName="orgdesc" value="">没有描述</pg:equal>
							</td>							
																																		    					    							    				    		
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td colspan=100 align='center' class="detailcontent">
			      	
			      	<pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
        </tr>   			   	      
		</pg:pager>
		
	</table>	
  </form> 
</div>
</body>

</html>
