<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.*"  %>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<% 
    AccessControl accesscontrol = AccessControl.getInstance();
    accesscontrol.checkAccess(request,response);
  	String siteId=request.getParameter("siteId");	
  		
	String action = request.getParameter("action");
	String name = request.getParameter("name");		
	String creatorUser = request.getParameter("creatorUser");
	String type = request.getParameter("type");
	String templateStyle = request.getParameter("templateStyle");
	//添加三个查询条件
	String TimeBgin=request.getParameter("TimeBgin");
	String TimeEnd=request.getParameter("TimeEnd");
	//String channelIds=request.getParameter("channelIds");
	

           
%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title></title>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="javascript" src="../../scripts/selectTime.js"></script>

<script language="JavaScript">
	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	function getLogInfo(e,logId){
	jsAccessControl.setBackColor(e);
	document.all("selectId").value = logId;
	}
	function test()
	{
	}
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
	
	
	function deleteTemplate(dealType) {//删除日志
	    var isSelect = false;
	    var outMsg;
	    
	    for (var i=0;i<LogForm.elements.length;i++) {
			var e = LogForm.elements[i];
			
			if (e.name == 'checkBoxOne'){
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
				var tplIds="";        	
					    for (var i=0;i<LogForm.elements.length;i++) {
						var e = LogForm.elements[i];			
						if (e.name == 'checkBoxOne'){
							if (e.checked){
			       				tplIds+=e.value+";";
			   				 }
						 }		
						 }						 
				 var vReturnValue = window.showModalDialog("template_del.jsp?action=deleteTpl&tplIds="+tplIds+"&siteId=<%=siteId%>");
				 if(vReturnValue == "ok")
				 {
					if("<%=action%>"=="search")
					{
			
					  parent.queryFrame.queryTpl();
					}
					else
					{
				
					parent.queryFrame.queryUserAll();
					}
				 }					 		
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
	
	function replaceTemplate(templateId,siteId){
		//alert(templateId+";"+siteId);
		var url = 'template_import_replace.jsp?siteId='+siteId+"&templateId="+templateId;
		openWin(url,500,400)
	}
	function editTemplate(templateId,siteId){
		//alert(templateId+";"+siteId);
		var url = 'template_edit2.jsp?siteId='+siteId+"&templateId="+templateId;
		openWin(url,screen.availWidth,screen.availHeight);
	}
	
	function exportTemplate(siteId){
	 	var currTemplateId = "";
	    var isSelect = false;
		var num = 0;
			    
	    for (var i=0;i<LogForm.elements.length;i++) {
			var e = LogForm.elements[i];
			if (e.name == 'checkBoxOne'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			}
		}
		
	    if(isSelect){
			for (var i=0;i<LogForm.elements.length;i++) {
				var e = LogForm.elements[i];			
				if (e.name == 'checkBoxOne'){
					if(e.checked){
	       				num++;
	       				currTemplateId = currTemplateId + e.value + ",";
	   				}
				}
			}
	    }else{
	    	alert("请选择一个模板!");
	    	return false;
	   }
	   
	   
	   var url = "select_exportTemplate.jsp?siteId="+siteId+"&templateIds="+currTemplateId;
	   openWin(url,400,300);
	   
	}
	function pubPreviewFile(filename,uri,siteId) {
		var u = uri.replace("\\", "/").replace(/\s+/img,"");
		if(u.charAt(u.length - 1)!="/")
		{
			u = u+"/";
		}
		if(u=="/")
		{
			u = "";
		}
		hidFrm.location.href = "template_pub_preview.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=preview";
	}
	function seeTemplateCited(templateId,siteId,templateType){
		var url = "see_template_cited.jsp?siteId="+siteId+"&templateId="+templateId+"&templateType="+templateType;
		openWin(url,600,500);
	}
	function importTemp(){
	    var first_url = "select_import_first.jsp?siteId=<%=siteId%>";
	    var second_url = "";
	    //var win = window.open(url,"_importHtml","scrollbars=no,status=yes,titlebar=no,toolbar=no,z-lock=yes,width=400,height=350,top=150,left=250");
	    //第一步
	    var first = openWin(first_url,400,300);    
	    if(first!=null && first == "ok")//是已有的模板文件,直接导入成功
		 {
			if("<%=action%>"=="search")
			{
	
			  parent.queryFrame.queryTpl();
			}
			else
			{		
			parent.queryFrame.queryUserAll();
			}
		 }	
	}
	/*打开没有描述文件的模板导入界面*/
	function openImport(impfile,htmlfile){
	    var path = "template_import.jsp?impfile="+impfile+"&htmlfile="+htmlfile;		
		var rstr = openWin(path,600,500);
	    if(rstr != null && rstr == "ok")
		 {
			if("<%=action%>"=="search")
			{
	
			  parent.queryFrame.queryTpl();
			}
			else
			{
		
			parent.queryFrame.queryUserAll();
			}
		 }else if(rstr != null && rstr.length >0 && rstr != "ok"  ){
		     importTemp();
		 }	
	}
</script>
</head>
<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form id="LogForm" name="LogForm" method="post" action="">
<table width="100%" border="1" cellpadding="3" cellspacing="0" class="Datalisttable">
	<tr>
		<td  align="right" style="padding-right:5px; text-align:left"  height="30" >
			<%
				if (accesscontrol.checkPermission(
				   siteId,AccessControl.SITE_TEMPLATEVIEWIMP_PERMISSION,AccessControl.SITETPL_RESOURCE)){
			%>
			<!--input name="Submit" onClick="openWin('template_import.jsp?siteId=<%=request.getParameter("siteId")%>',500,400)" type="button" class="cms_button" value="导入" /-->
			<input name="Submit" onClick="importTemp()" type="button" class="cms_button" value="导入" />
			<%
				}
				if (accesscontrol.checkPermission(
				   siteId,AccessControl.SITE_TEMPLATEVIEWEXP_PERMISSION,AccessControl.SITETPL_RESOURCE)){			
			%>		
			<input name="Submit2" type="button" class="cms_button" onClick="exportTemplate('<%=request.getParameter("siteId")%>')" value="导出" />
			<%
			}
			if (accesscontrol.checkPermission(
			   siteId,AccessControl.SITE_TEMPLATEVIEWADD_PERMISSION,AccessControl.SITETPL_RESOURCE)){			
			%>	
			<input name="Submit42" type="button" class="cms_button" onClick="openWin('template_add2.jsp?siteId=<%=request.getParameter("siteId")%>',screen.availWidth,screen.availHeight)" value="添加" />
			<%
			}
			if (accesscontrol.checkPermission(
			   siteId,AccessControl.SITE_TEMPLATEVIEWDEL_PERMISSION ,AccessControl.SITETPL_RESOURCE)){			
			%>	
			<input name="Submit43" type="button" class="cms_button" value="删除" onClick="deleteTemplate(1)">
			<%
				}
			%>
	  </td>
	</tr>
	<tr>
		<td align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="4">
				<pg:listdata dataInfo="com.frameworkset.platform.cms.templatemanager.tag.TemplateofSiteList" keyName="TemplateofSiteList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="TemplateofSiteList" isList="false">
				<pg:param name="siteId" />										
				<pg:param name="action" />
				<pg:param name="name" />
				<pg:param name="creatorUser" />
				<pg:param name="type" />
				<pg:param name="templateStyle" />
				<pg:param name="TimeBgin" />
				<pg:param name="TimeEnd" />
				<pg:header>
				<tr class="cms_report_tr">				
					<td width="4%" align="left"  height="30">
					<input type="checkBox" class="checkbox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')">													
					</td>
					<pg:title title="模板名称" align="center" sort="true" nowrap="true" width="20%" colName="name"/>	
					<td width="5%" height="19" align="center">预览</td>
					<pg:title title="类型" align="center" sort="true" nowrap="true" width="7%" colName="type"/>
					<td align="center">风格</td>
					<td width="8%" align="center">创建人</td>
					<td width="35%" align="center">模板描述</td>
					<td width="5%" align="center">引用次数</td>
					<td width="16%" align="center">创建时间</td>
					
				</tr>
				</pg:header>
				<!--检测当前页面是否有记录-->
				<pg:notify>
					<tr class="labeltable_middle_tr_01" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td colspan=100 align='center'>暂时没有模板</td>
					</tr>
				</pg:notify>
				<!--list标签循环输出每条记录-->
				<%
						ContextMenu contextmenu = new ContextMenuImpl();
				%>
				<pg:list>
				<%
						//在模板名称上面加右键菜单
						int templateId = dataSet.getInt("templateId");
						int templateType = dataSet.getInt("type");
						Menu menu = new Menu();
						menu.setIdentity("tpl"+templateId);
						
						if (accesscontrol.checkPermission(
				   		templateId+"",AccessControl.TEMPLATE_TPLEDIT_PERMISSION,AccessControl.TEMPLATE_RESOURCE)){	
							Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
							menuitem1.setName("编辑");
							//menuitem1.setLink(request.getContextPath() +"/cms/templateManage/template_edit2.jsp?siteId="+siteId+"&templateId="+templateId);
							menuitem1.setLink("javascript:editTemplate('" + templateId + "','" + siteId + "')");
							menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_edit.gif");
							menu.addContextMenuItem(menuitem1);
						}
						
						Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
						menuitem3.setName("引用查看");
						menuitem3.setLink("javascript:seeTemplateCited('" + templateId + "','" + siteId + "','" + templateType + "')");
						menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_edit.gif");
						menu.addContextMenuItem(menuitem3);
						
						if (accesscontrol.checkPermission(
				   		templateId+"",AccessControl.TEMPLATE_TPLREIMP_PERMISSION,AccessControl.TEMPLATE_RESOURCE)){
							Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
							menuitem2.setName("重新导入");
							//menuitem2.setLink(request.getContextPath() +"/cms/templateManage/template_import_replace.jsp?siteId="+siteId+"&templateId="+templateId);
							menuitem2.setLink("javascript:replaceTemplate('" + templateId + "','" + siteId + "')");
							menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_reimport.gif");
							menu.addContextMenuItem(menuitem2);
						}
						contextmenu.addContextMenu(menu);
						
						TemplateManager tlpm = new TemplateManagerImpl();
						int citedCount = tlpm.getTplCtiedCount(templateId+"",0);				
				%>
					<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td class="tablecells" nowrap="nowrap" align="left">
							<INPUT class="checkbox" type="checkbox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')"  value="<pg:cell colName="templateId" defaultValue=""/>"></td>
				
						<td id="tpl<%=templateId%>" class="tablecells" nowrap="true"  onClick="editTemplate('<pg:cell colName="templateId" defaultValue="" />','<%=request.getParameter("siteId")%>')" style="cursor:hand">
						<pg:cell colName="name" defaultValue="" />
				  </td>
						<td class="tablecells" align="center">
						<img src="../images/preview.gif" alt="预览" width="19" height="18" style="cursor:hand"  onClick="pubPreviewFile('<pg:cell colName="templateFileName" defaultValue=""/>','<pg:cell colName="templatePath" defaultValue=""/>','<%=siteId%>')" />																						
						</td>
						<td class="tablecells" >
							<pg:equal colName="type" value="0">首页模版</pg:equal>
							<pg:equal colName="type" value="1">概览</pg:equal>
							<pg:equal colName="type" value="2">细览</pg:equal>												
						</td>
						<td class="tablecells" nowrap="nowrap" align="center">
							<pg:cell colName="styleName" defaultValue="" />
						</td>
						<td class="tablecells" nowrap="nowrap" align="center">
							<pg:cell colName="createUserName" defaultValue="" />
						</td>
						<td class="tablecells" align="center">
							<pg:cell colName="description" defaultValue="" />
						</td>
						<td class="tablecells" align="center">
							<%=citedCount%>
						</td>
						<td class="tablecells" align="center">		
							<pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm"  defaultValue="" />
						</td>
					
<!--							<a style="cursor:hand" onclick="editTemplate('<pg:cell colName="templateId" defaultValue="" />','<%=request.getParameter("siteId")%>')"><u>编辑</u></a>-->
<!--							<a style="cursor:hand" onclick="replaceTemplate('<pg:cell colName="templateId" defaultValue="" />','<%=request.getParameter("siteId")%>')"><u>重新导入</u></a>-->
						
					</tr>
				  </pg:list>
					<%
							request.setAttribute("opdoc",contextmenu);
					%>
					<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
					<tr  class="labeltable_middle_tr_01">
						<td colspan=9 align='center'><div class="Data_List_Table_Bottom">共<pg:rowcount />个模板<pg:index /></div></td>
					</tr>
					<input name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
			</table>									
		</td>
	</tr>	
</table>
</form>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>
