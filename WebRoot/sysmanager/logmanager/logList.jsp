<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<% 
	String rootpath = request.getContextPath();
%>
<html>
	<head>
		<title>日志管理</title>

<script language="JavaScript">
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
	
		
	
	function dealRecord(dealType) {//删除日志
	    var isSelect = false;	    
	    for (var i=0;i<LogForm.elements.length;i++) {
			var e = LogForm.elements[i];
			if (e.name == 'ID'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			}
	    }	    
	    if (isSelect){
	    	if (dealType==1){
	    		var msg = '<pg:message code="sany.pdp.dictmanager.dict.delete.confirm"/>';
	        	$.dialog.confirm(msg, function() {
	        		 LogForm.action="deleteLog.jsp";
		            LogForm.target = "deleteLogs"; 
		            document.all("deleteLogs").src = "deleteLog.jsp";                      
		            LogForm.submit();
	        	});
			} 
	    }else{
	    	$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.one"/>');
	    	return false;
	   }
	}
	
	
		function dealAllRecord() {//删除日志
	    var hasLitems = false;	  
	    
	    for (var i=0;i<LogForm.elements.length;i++) {
			if(LogForm.elements[i].name == 'ID')
			{
				hasLitems = true;
				break;
			}
	    }
		
		if(hasLitems)
		{
			$.dialog.confirm('<pg:message code="sany.pdp.dictmanager.dict.delete.confirm"/>', function() {
				LogForm.action="<%=rootpath%>/sysmanager/logmanager/submitLog_do.jsp?flag=2";
				LogForm.submit();
			});
		}else{
	    	$.dialog.alert('<pg:message code="sany.pdp.sysmanager.log.delete.null"/>');
	    	return false;
	   }
  		   
	}		
	function dealRangeRecord(){
        var operUser = parent.document.all("operUser").value;
       if(operUser=="") operUser = "所有操作人";
        var oper = parent.document.all("oper").value;
        if(oper=="") oper = "所有类型";
        var type = parent.document.all("type").value;
        if(type=="") type = "所有内容";
        var startDate = parent.document.all("startDate").value;
        var endDate = parent.document.all("endDate").value;
        if(startDate!="" && endDate!="")
        {
        var msg = '<pg:message code="sany.pdp.sysmanager.log.delete.begin"/>'+" \n" ;
        msg += '<pg:message code="sany.pdp.sysmanager.log.operator.account"/>'+"："+operUser+"\n";
        msg += '<pg:message code="sany.pdp.sysmanager.log.type"/>'+"："+oper+"\n";
        msg += '<pg:message code="sany.pdp.sysmanager.log.content"/>'+"："+type+"\n";
        msg += '<pg:message code="sany.pdp.sysmanager.log.time.begin"/>'+startDate+'<pg:message code="sany.pdp.sysmanager.log.search.to"/>'+endDate+'<pg:message code="sany.pdp.sysmanager.log.time.end"/>'
        $.dialog.confirm(msg, function() {
        	LogForm.action="deleteRangeLogs.jsp?operUser="+operUser+"&&oper="+oper+"&&type="+type+"&&startDate="+startDate+"&&endDate="+endDate;
            LogForm.target = "deleteLogs";
            LogForm.submit();
        });
        }
        else
        {
        	$.dialog.alert('<pg:message code="sany.pdp.sysmanager.log.delete.time"/>');
        }
    
    }
    
    function logDetailQuery(logId){
    	var url="<%=request.getContextPath()%>/sysmanager/logmanager/logDetailList.jsp?logId="+logId;
    	$.dialog({title:'<pg:message code="sany.pdp.sysmanager.log.detail.search"/>',width:800,height:600, content:'url:'+url});   	
    	
    }
</script>
	<body>
		<div id="changeColor">
			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.LogSearchList" keyName="LogSearchList" />
			<pg:pager maxPageItems="12" scope="request" data="LogSearchList" isList="false">
				<pg:param name="operUser"/>
				<pg:param name="logModuel"/>
				<pg:param name="type"/>
				<pg:param name="oper"/>
				<pg:param name="logId"/>
				<pg:param name="startDate"/>
				<pg:param name="endDate"/>
				<pg:param name="curUserId"/>
				<pg:param name="isHis"/>
				<pg:param name="opOrgid"/>
				<pg:param name="logVisitorial"/>
				<pg:param name="isRecursion"/>
				
				<pg:equal actual="${LogSearchList.itemCount}" value="0" >
					<div class="nodata">
					<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
				</pg:equal>
				<pg:notequal actual="${LogSearchList.itemCount}"  value="0">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<input class="text" type="hidden" name="selectId"/>
						<th><input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"/></th>
						<th><pg:message code="sany.pdp.sysmanager.log.operation.module"/></th>
			    		<th><pg:message code="sany.pdp.sysmanager.log.operator.account"/></th>
			    		<th><pg:message code="sany.pdp.sysmanager.log.operator.org"/></th>				    		
						<th><pg:message code="sany.pdp.sysmanager.log.content"/></th>
						<th><pg:message code="sany.pdp.role.operation.type"/></th>
						<th><pg:message code="sany.pdp.sysmanager.log.source"/></th>
						<th><pg:message code="sany.pdp.sysmanager.log.operation.time"/></th>
					</pg:header>
						<%
							//右键菜单
							ContextMenu contextmenu = new ContextMenuImpl();
					 	%>
						<!--list标签循环输出每条记录-->
						<pg:list>
						<% 
							String logId = dataSet.getString("logId");
							Menu menu = new Menu();
							menu.setIdentity("opuser_"+logId);
							Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
							menuitem2.setName(RequestContextUtils.getI18nMessage("sany.pdp.sysmanager.log.detail.view", request));
							menuitem2.setLink("javascript:logDetailQuery("+logId+")");
							menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/new.gif");
							menu.addContextMenuItem(menuitem2);
							
							contextmenu.addContextMenu(menu);
							
						%>
						<tr>	      	
							<td class="td_center">
								<input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="logId" defaultValue=""/>">
							</td>
							<td>
								<pg:cell colName="logModule" defaultValue="" />
							</td>
							<td id="opuser_<%=logId%>"  bgcolor="#F6FFEF" >
								<pg:cell colName="operUser" defaultValue="" />
							</td>
							<td>
								<pg:cell colName="operOrg" defaultValue="" />
							</td>
							<td>
								<pg:cell colName="oper" defaultValue="" />
							</td>
							<td>
								<pg:equal colName="operType" value="0"><pg:message code="sany.pdp.common.operation.null"/></pg:equal>
								<pg:equal colName="operType" value="1"><pg:message code="sany.pdp.common.add"/></pg:equal>
								<pg:equal colName="operType" value="2"><pg:message code="sany.pdp.common.batch.delete"/></pg:equal>
								<pg:equal colName="operType" value="3"><pg:message code="sany.pdp.common.operation.modfiy"/></pg:equal>
								<pg:equal colName="operType" value="4"><pg:message code="sany.pdp.common.operation.other"/></pg:equal>	
							</td>
							<td>
								<pg:cell colName="visitorial" defaultValue="" />
							</td>
							<td>
								<pg:cell colName="operTime" defaultValue="" dateformat="yyyy-MM-dd HH:mm:ss"/>
							</td>
						</tr>
					</pg:list>
					</table>
					<%
						request.setAttribute("opuser",contextmenu);
					%>
					<pg:contextmenu enablecontextmenu="true" context="opuser" scope="request"/> 
					<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
				</pg:notequal>
			</pg:pager>
		</div>
	</body>
</html>

