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
	
	String siteId = request.getParameter("siteId");
	String channelName = request.getParameter("channelName");
	String outlineTemplateName = request.getParameter("outlineTemplateName");
	String otId = request.getParameter("otId");
	String detailTemplateName = request.getParameter("detailTemplateName");
	String dtId = request.getParameter("dtId");
	String tName = "";
	String tId = "";
	if(null!=outlineTemplateName && outlineTemplateName.equals("")){
		tName = outlineTemplateName;
	} else if (null!=detailTemplateName && detailTemplateName.equals("")) {
		tName = detailTemplateName;
	} else { 
		tName = "当前没有选择任何模板";
	}
	
	if(null!=otId && otId.equals("")) {
		tId = otId;
	} else if (null!=dtId && dtId.equals("")) {
		tId = dtId;
	} else {
		tId = "";
	}
	
	String type = request.getParameter("type");
	String action = request.getParameter("action");
	//System.out.println("SID: "+siteId+"     CID: "+channelId+"    type: "+type+"    action: "+action);
	String typeName = "";
	if(type.equals("0")) {
		typeName="首页模版";
	} else if (type.equals("1")) {
		typeName="概览模版";
	} else {
		typeName="细览模版";
	}
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
		function subform(){
			alert(manageDocVerform.action.value);
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
		//判断是否有选择
		function haveSelect(elName){
			var isSelect = false;
			var ch = document.getElementsByName(elName);
			for (var i=0;i<ch.length;i++) {
				if (ch[i].checked){
					isSelect=true;
					break;
				}
			}
			return isSelect;
		}
	</script>
	
	</head>
	<script language="javascript">
		document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
	</script>
	<body scroll="yes">
		<form target="managedocarrange" name="manageDocVerform" action="" method="post">
			<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bgcolor="#FFFFFF" id="docArrangeListTable"  class="Datalisttable">
				<tr>
					<td height='25' colspan="7" background="<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg" style="text-align:left; background:url(<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
						<!-- div  class="DocumentOperT">
							查询条件
						</div-->
						<input type="hidden" name="siteId" id="siteId" value="<%=siteId%>"/>
						<input type="hidden" name="type" id="type" value="<%=type%>"/>
						<input type="hidden" name="action" id="action" value="search"/>
						<br>
						&nbsp;&nbsp;模板名称:<input name="name" type="text"  class="cms_text" id="name" style="width:130px">
						&nbsp;&nbsp;模板风格:
						<select name="templateStyle" class="cms_select" id="templateStyle">
							<option value="-2">--所有风格--</option>
							<%
								TemplateStyleManager tsm = new TemplateStyleManagerImpl();
								List list1 = tsm.getAllTemplateStyleIdAndName();
								for(int i=0;i<list1.size();i++){
									TemplateStyleInfo tsi = (TemplateStyleInfo)list1.get(i);
							%>
									<option value="<%= tsi.getStyleId() %>"><%= tsi.getStyleName() %></option>
							<%
								}
							%>
						</select>
						&nbsp;&nbsp;<input type="button" name="search" value="查询" onClick="" class="cms_button"/>
						&nbsp;&nbsp;当前频道：<%=channelName%>&nbsp;&nbsp;当前模板：<%=tName%>
						<!-- a style="cursor:hand" onClick=""><div class="DocumentOper"><%=siteId%></div></a-->
						<br><br>
					</td>
				</tr>
				<pg:listdata dataInfo="TemplateofSiteList" keyName="TemplateofSiteList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="TemplateofSiteList" isList="false">
					<pg:param name="siteId" />
					<pg:param name="name" />
					<pg:param name="action" />
					<pg:param name="type" />
					<pg:param name="templateStyle" />
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
								该频道暂时没有<%=typeName%>
							</td>
						</tr>
					</pg:notify>

					<pg:list>

						<tr class="cms_data_tr" id="<pg:cell colName="documentId" defaultValue=""/>">
							<td class="tablecells" align=center height='30' width="5%">
								<input name="templateId" value="<pg:cell colName="templateId" defaultValue=""/>" type=hidden>
								<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="templateId" defaultValue=""/>">
							</td>
							
							<td class="tablecells" align=center height='30'>
								<pg:cell colName="name" defaultValue="" />
							
							</td>

							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="styleName" defaultValue="" />
							</td>

							<td class="tablecells" nowrap="nowrap" >
								<pg:cell colName="templatePath" defaultValue="" />
							</td>
                            <td class="tablecells" nowrap="nowrap" >
                            	<pg:cell colName="description" defaultValue="" />
							</td>
						</tr>
					</pg:list>
					<tr><td colspan="5"><div class="Data_List_Table_Bottom">共<pg:rowcount />个模板<pg:index /></div></td></tr>
					<tr class="labeltable_middle_tr_01">
						<td align="right" colspan=7><div class="Data_List_Table_Bottom">
						<input type="button" name="ok" value="确定" onClick="subform();" class="cms_button"/>
						<input type="button" name="closeb" value="返回" onClick="window.close();" class="cms_button"/></div>
						</td>
					</tr>
				</pg:pager>

		  </table>
		</form>
	</body>
</html>