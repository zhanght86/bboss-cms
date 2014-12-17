<% 
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
/**
 * 
 * <p>Title: 选择资源列表</p>
 *
 * <p>Description: 选择资源列表</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: bboss</p>
 * @Date 2008-11-4
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	if(!accessControl.checkManagerAccess(request, response)){
		return;
	}
	
	String restypeId = request.getParameter("restypeId");
%>

<html>
<head>
	<title></title>
	<base target="_self">	
	<script type="text/javascript" language="Javascript">
		var api = frameElement.api, W = api.opener;
		
		function okadd(){	
			selectNode = document.all.item("checkBoxOne");
			if(selectNode==null){
 				W.$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.resource.select"/>'); return;
 			}    
 			var ret ;
 	
 			if(selectNode.length)
 			{
 				for(var i = 0;  i < selectNode.length; i ++)
 				{
 					if(selectNode[i].checked)
 					{
 						ret = selectNode[i].value;
 					}
 			
 				}
 			}
 			else if(selectNode)
 			{
 				if(selectNode.checked)
				{
					ret = selectNode.value;
				}
 			}
 			if(!ret)
 			{
 				W.$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.type.resource.select"/>'); return;
 			}    
 			
 			W.document.getElementById("${param.tag1}").value = ret.split(":")[0];
 			W.document.getElementById("${param.tag2}").value = ret.split(":")[1];
 			if (ret.split(":").length == 3) {
 				W.document.getElementById("getopergroup").src = "resChange.jsp?restypeId=${param.restypeId}&global="+ret[2];
 			} else {
 				W.document.getElementById("getopergroup").src = "resChange.jsp?restypeId=${param.restypeId}&global=op";
 			}
 	
 			api.close();
 			
			//window.returnValue = ret;   
			//window.close();
		}
	
	</script>
</head>

<body class="contentbodymargin"  scroll="no">
<div id="changeColor">
    <pg:listdata dataInfo="ResSearchList" keyName="ResSearchList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="10" scope="request" data="ResSearchList" isList="false">
							<pg:param name="restypeId" />
							<pg:param name="title" />
							<pg:param name="path" />

							<pg:equal actual="${ResSearchList.itemCount}" value="0" >
								<div class="nodata">
									<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
							</pg:equal>
							<pg:notequal actual="${ResSearchList.itemCount}"  value="0">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
									<pg:header>
										<t>
										<th><input class="text" type="hidden" name="selectId"></h>
										<th><pg:message code='sany.pdp.purviewmanager.rolemanager.role.resource.type'/></th>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.name"/></th>
										<th><pg:message code="sany.pdp.sysmanager.resource.description"/></th>
								
										</tr>
									</pg:header>

							<!--list标签循环输出每条记录-->
							<pg:list>
								<% 
									String title = dataSet.getString("title");
									String path = dataSet.getString("path");
									if(path == null || "".equals(path)){
										path = title;
									}
								%>
								<tr>
									<td>
										<input type="radio" name="checkBoxOne"  value="<%=title+":"+path %>"  width="10" />
									</td>
									<td>
										<pg:cell colName="restypeName" defaultValue="" />
									</td>
									
									<!-- 资源修改，只能是拥有超级管理员角色的用户能进行此项操作 --> 
									<td>
									<input type="hidden" name="title" value="<pg:cell colName="title" defaultValue="" />"/>
										<pg:cell colName="title" defaultValue="" />
									</td>
									<td>
										<pg:equal colName="path" value="">
											<pg:cell colName="title"  maxlength="14" />
										</pg:equal>
										<pg:cell colName="path"  maxlength="14" />
									</td>
									
								</tr>
							</pg:list>
							</table>
							<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
							<div class="btnarea" >
								<a href="javascript:void(0)" class="bt_1" id="add" name="add" onclick="okadd()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a>
								<a href="javascript:void(0)" class="bt_2" id="cancel" name="cancel" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
							</div>
						</pg:notequal>
						</pg:pager>
</div>
</body>

</html>
