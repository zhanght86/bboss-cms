<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleManager" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleManagerImpl" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.tag.TemplateofSiteList" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String isSite = request.getParameter("isSite");
	String siteId = request.getParameter("siteId");
	String type = request.getParameter("type");
	String action = request.getParameter("action");
	String name = request.getParameter("name");
	String templateStyle = request.getParameter("templateStyle");
	String typeName = request.getParameter("typeName");
	String channelId = request.getParameter("channelId");
	//System.out.println("List: "+channelId);
	String tId = request.getParameter("tId");
	if(tId != null)
		tId = tId.trim();
	//System.out.println("------tid------"+tId);
%>
<html>
	<head>
	<base target=_self><!-- use for submit to self-->
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>.::::::::::频道模板设置:::::::::::::::::::::::::::::::::::::..</title>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<script language="javascript">
		var tName = "";
		
		var doc = "doc"
		function saveChoice()
		{
			var all = document.getElementsByName("checkedID");
			var flag = 0;
			var tId = "";
			for(var i=0;i<all.length;i++){
				if(all[i].checked == true){
					var isSite = "<%=isSite%>";
					var temps = all[i].value.split("|");
					//if(isSite==doc){
					returnTemplateIdAndName(temps);
					//}
					//tId = temps[0];
					//tName = temps[1];
				}
			}
			//templateSetForm.action="channel_templateSet_do.jsp?type=<%=type%>&channelId=<%=channelId%>&tId="
			//						+tId+"&tName="+tName+"&siteId=<%=siteId%>";
			//templateSetForm.submit();
		}
	
		function updateParentForm()
		{
			window.returnValue = tName;
		}
		
		function returnTemplateIdAndName(t){
			window.returnValue = t;
			window.close();
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
			window.open("../templateManage/template_pub_preview.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=preview","templatePreview");
			//alert(templatePreview.location.href);
		}
	</script>
	</head>
	<body scroll="yes">
		<form target="templatePreview" name="templateSetForm" id="templateSetForm" action="" method="post">
			
			<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bgcolor="#FFFFFF" id="docArrangeListTable"  class="Datalisttable">
				<pg:listdata dataInfo="TemplateofSiteList" keyName="TemplateofSiteList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="TemplateofSiteList" isList="false">
					<pg:param name="siteId" />
					<pg:param name="name" />
					<pg:param name="action" />
					<pg:param name="type" />
					<pg:param name="templateStyle" />
					<pg:param name="tId" />
					<pg:param name="channelId" />
					<pg:param name="isSite" />
					<pg:header>
						<tr class="cms_report_tr">
							<!--设置分页表头-->
	
							<td style="width:5%" align=center height='30'>
								<!-- input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"-->
							</td>
							<td width="25%" height='30'>
								模板名称
							</td>
							<td width="10%" height='30'>
								模板风格
							</td>
							<td width="10%" height='30'>
								模板预览
							</td>
							<td width="50%" height='30'>
								模板描述
							</td>
						</tr>
					</pg:header>
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该站点/频道暂时没有您所查找的模板
							</td>
						</tr>
					</pg:notify>

					<pg:list>

						<tr class="cms_data_tr" id="<pg:cell colName="documentId" defaultValue=""/>">
							<td class="tablecells" align=center height='30' width="5%">
								<input name="templateId" value="<pg:cell colName="templateId" defaultValue=""/>" type=hidden>
								<input <pg:notempty actual="<%=tId%>"><pg:equal colName="templateId" value="<%=tId%>">checked</pg:equal></pg:notempty>
								 class="checkbox" hideFocus type="radio" name="checkedID" 
									value='<pg:cell colName="templateId" defaultValue=""/>|<pg:cell colName="name" defaultValue="" />' />
							</td>
							
							<td class="tablecells" align=center height='30'>
								<pg:cell colName="name" defaultValue="" />
							
							</td>

							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="styleName" defaultValue="" />
							</td>

							<td class="tablecells" nowrap="nowrap" >
								<img src="../images/preview.gif" alt="预览" width="19" height="18" style="cursor:hand"  onClick="pubPreviewFile('<pg:cell colName="templateFileName" defaultValue=""/>','<pg:cell colName="templatePath" defaultValue=""/>','<%=siteId%>')" />
							</td>
                            <td class="tablecells" nowrap="nowrap" >
                            	<pg:cell colName="description" defaultValue="" />
							</td>
						</tr>
					</pg:list>
					<tr><td colspan="5"><div class="Data_List_Table_Bottom">共<pg:rowcount />个模板<pg:index /></div></td></tr>
					<tr class="labeltable_middle_tr_01">
						<td align="right" colspan=7><div class="Data_List_Table_Bottom">
						<input type="button" name="ok" value="确定" onClick="saveChoice();" class="cms_button"/>
						<input type="button" name="closeb" value="返回" onClick="window.close();" class="cms_button"/></div>
						</td>
					</tr>
				</pg:pager>

		  </table>
		</form>
		<iframe name="templatePreview" width="0" height="0" style="display:none"></iframe>
	</body>
</html>