<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	//字典类型ID
	String ids = request.getParameter("did");
	String typeName = request.getParameter("typeName");	
	//机构ID
	String orgId = request.getParameter("orgId");
	ids = ids==null?"":ids;
	typeName = typeName==null?"":typeName;	
	orgId = orgId==null?"":orgId;
	String dicttype_name = "";
	String did = "";
	//实际上是datavalue 父数据项的值
	String dictdataValue = "";
	String dictdataName = "";
	session.setAttribute("dictTabId", "2");
	Data dtype = null;	
	if(!"".equals(ids)){
		DictManager dictManager = new DictManagerImpl();
		did = ids;
		dtype = dictManager.getDicttypeById(did);
		dicttype_name = dtype.getName();
	}
	if(dtype == null){
		dtype = new Data();
	}	
	
	if("root".equals(dictdataValue)){
	    dictdataValue="0";
	}
	
%>
<html>
	<head>
		<title>属性容器</title>
		<link href="<%=rootpath%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
  
<SCRIPT language="javascript">
	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	var win;
	var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
	

</SCRIPT>		
	<body>
		<form name="dictList" method="post" target="hiddenFrame">			
			<p><%=typeName%>
			<table cellspacing="1" cellpadding="0" border="0" width=100%  class="thin">
				<pg:listdata dataInfo="com.frameworkset.platform.dictionary.tag.DictList" keyName="DictList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="1500" scope="request" data="DictList" isList="false">					
					<pg:param name="did" />
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>
								暂时没有数据
							</td>
						</tr>
					</pg:notify>
					<!--list标签循环输出每条记录-->
					<pg:list>
					<%
						String dataId = dataSet.getString("dataId");
						String value = dataSet.getString("value");
						DictManager dictManager2 = new DictManagerImpl();
						boolean state = dictManager2.getOrgisReadRes(orgId,dataId,value);
						if(state){
					%>
						<tr>							
							<td class="tablecells" style="width:20%" nowrap="nowrap">
								<input type="checkbox" name="dictdataValue" value="<pg:cell colName='value' defaultValue='' />:<pg:cell colName='name' defaultValue='' />" />
							</td>									
							<td class="tablecells" style="width:80%;text-align:left"  nowrap="nowrap" colspan="2">								
								<pg:cell colName="name" defaultValue="" />
							</td>
						</tr>
					<%}else if(dictManager2.getDicttype_type(dataId)==1){%>
						<tr>							
							<td class="tablecells" style="width:20%" nowrap="nowrap">
								<input type="checkbox" name="dictdataValue" value="<pg:cell colName='value' defaultValue='' />:<pg:cell colName='name' defaultValue='' />" />
							</td>									
							<td class="tablecells" style="width:80%;text-align:left"  nowrap="nowrap" colspan="2">								
								<pg:cell colName="name" defaultValue="" />
							</td>
						</tr>
					<%}%>
					</pg:list>
					<input name="dictTypeId" value="<%=did%>" type="hidden">
				</pg:pager>
                
		  </table>
		</form>
		<iframe height="0" width="0" name="hiddenFrame"></iframe>
	</body>
	<script>
	<%
	    List selectedValues = new ArrayList();
		if(!"".equals(orgId) && !"".equals(did)){
		    DictManager dictManager = new DictManagerImpl();    
		    selectedValues = dictManager.getDictdatasByOrgIdAndTypeId2(orgId,did);
		    for(int i=0;i<selectedValues.size();i++){
		        String value = (String)selectedValues.get(i);
	%>
		        var arr = new Array();
		        arr = document.getElementsByName("dictdataValue");
		        for(var j=0;j<arr.length;j++){
		            if(arr[j].value=="<%=value%>"){
		                arr[j].checked = true;
		                break;
		            }
		        }
	<%	        
		    }	    
		}
	
	%>
	</script>  
		
</html>


