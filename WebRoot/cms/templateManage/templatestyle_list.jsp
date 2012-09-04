
<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*,com.frameworkset.platform.cms.templatemanager.tag.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	if(!accesscontroler.checkAccess(request, response)){
		return ;
	}

	//String addurl = "doc_extfield_add.jsp";
%>
<html>
	<head>
		<base target=_self><!-- use for submit to self-->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>.::::::::::模板风格管理::::::::::::::::::::::::::::::..</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript">
			
 			function addaStyle(){
 				var re = openWin("templatestyle_add.jsp?action=add",400,300);
 				if(re=="cf"){
 					var str = document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						strArray= str.slice(0,end);
					else
						strArray = str;
					document.location.href = strArray+"?"+document.all.queryString.value;
 				}
 			}
 			function deleteStyles(){
 				if(haveSelect('ID')){
 					var ids = getStyleIds();
	 				templateStyleForm.action="templateStyleHandle_do.jsp?action=delete&ids="+ids;
					templateStyleForm.submit();
				}else{
					alert("请选择要删除的模板风格！");
					return false;
				}
				var str = document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						strArray= str.slice(0,end);
					else
						strArray = str;
					document.location.href = strArray+"?"+document.all.queryString.value;
 			}
 			//更新模板风格
			function updateaStyle(styleId,styleName,siteId,styleDesc)
			{
				var re = openWin("templatestyle_edit.jsp?action=edit&styleId=" + styleId + "&siteId=" + siteId + "&styleName=" + styleName + "&styleDesc=" + styleDesc,400,300);
 				if(re=="cf"){
 					var str = document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						strArray= str.slice(0,end);
					else
						strArray = str;
					document.location.href = strArray+"?"+document.all.queryString.value;
 				}
			}
			
			
			function top()
			{
				var all = document.getElementsByName("ID");
				var flag = 0;
				for(var i=0;i<all.length;i++){
					if(all[i].checked == true){
						flag ++;
						if(flag > 1)
						{
							alert("请选择单类模板风格移动！");
							return false;
						}
					}
				}
				if(flag < 1)
				{
					alert("请选择要移动的模板风格！");
					return false;
				}
				for(var i=0;i<all.length;i++){
					up();
				}
				
				//设置保存排序按钮不可见
				var div = document.getElementById("saveorder");
				div.style.visibility = "visible";
			}
			
			function up()
			{
				var all = document.getElementsByName("ID");
				var row;
				var flag = 0;
				for(var i=0;i<all.length;i++){
					if(all[i].checked == true){
						row = all[i];
						flag ++;
						if(flag > 1)
						{
							alert("请选择单类模板风格移动！");
							return false;
						}
					}
				}
				if(flag < 1)
				{
					alert("请选择要移动的模板风格！");
					return false;
				}
				var tmp = row;
				upRow(row);
				tmp.checked = true;
				
				var div = document.getElementById("saveorder");
				div.style.visibility = "visible";
			}
			
			function down()
			{
				var all = document.getElementsByName("ID");
				var row;
				var flag = 0;
				for(var i=0;i<all.length;i++){
					if(all[i].checked == true){
						row = all[i];
						flag ++;
						if(flag > 1)
						{
							alert("请选择单类模板风格移动！");
							return false;
						}
					}
				}
				if(flag < 1)
				{
					alert("请选择要移动的模板风格！");
					return false;
				}
				var tmp = row;
				downRow(row);
				tmp.checked = true;
				
				var div = document.getElementById("saveorder");
				div.style.visibility = "visible";
			}
			function base()
			{
				var all = document.getElementsByName("ID");
				var flag = 0;
				for(var i=0;i<all.length;i++){
					if(all[i].checked == true){
						flag ++;
						if(flag > 1)
						{
							alert("请选择单类模板风格移动！");
							return false;
						}
					}
				}
				if(flag < 1)
				{
					alert("请选择要移动的模板风格！");
					return false;
				}
				for(var i=0;i<all.length;i++){
					down();
				}
				
				var div = document.getElementById("saveorder");
				div.style.visibility = "visible";
			}
			//表格row上移
			function upRow(e){
				var _row=e.parentElement.parentElement;
				if(_row.previousSibling.previousSibling.previousSibling.previousSibling.previousSibling.previousSibling)
				{
					//alert(_row.previousSibling.outerHTML);
					swapNode(_row,_row.previousSibling);
				}
			}
			//表格row下移(说明注释性的文字也算 一个nextSibling）
			function downRow(e)
			{
				//通过链接对象获取表格行的引用
				var _row=e.parentElement.parentElement;
				//如果不是最后一行，则与下一行交换顺序
				if(_row.nextSibling.nextSibling.nextSibling.nextSibling)
				{	
					//alert(_row.nextSibling.outerHTML);
					swapNode(_row,_row.nextSibling);
				}
		
			}
			//交换
			function swapNode(node1,node2){
				//取得将要交换的两行内的复选框的值styleOrder排序号,然后交换
				//if(node1.cells(0).all.length>0&&node2.cells(0).all.length>0){
				var cv = node1.cells(0).all[1].value;
				node1.cells(0).all[1].value = node2.cells(0).all[1].value;
				node2.cells(0).all[1].value = cv;
				//}
				//alert(node1.cells(0).all[1].value+"------------"+node2.cells(0).all[1].value);
				//获取父结点
				var _parent=node1.parentNode;
				//获取两个结点的相对位置
				var _t1=node1.nextSibling;
				var _t2=node2.nextSibling;
				//将node2插入到原来node1的位置
				if(_t1)_parent.insertBefore(node2,_t1);
				else _parent.appendChild(node2);
				//将node1插入到原来node2的位置
				if(_t2)_parent.insertBefore(node1,_t2);
				else _parent.appendChild(node1);
			}
			
			//保存排序
			function saveOrder(){
				var allCheck = document.getElementsByName("ID");
				var allHidden = document.getElementsByName("styleId");
				var orderNum = "";
				var styleId = "";
				if(allCheck.length == allHidden.length){	
					for(var i=0;i<allCheck.length;i++){
						orderNum += allCheck[i].value+":";
						styleId += allHidden[i].value+":";
					}
				}
				
				templateStyleForm.action="templateStyleHandle_do.jsp?action=saveOrder&orderNum="
											+orderNum+"&styleIds="+styleId;
				//alert(templateStyleForm.action);
				templateStyleForm.submit();
				
				var div = document.getElementById("saveorder");
				div.style.visibility = "hidden";
				
				var str = document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						strArray= str.slice(0,end);
					else
						strArray = str;
					document.location.href = strArray+"?"+document.all.queryString.value;
			}
			
			function getStyleIds(){
				var allCheck = document.getElementsByName("ID");
				var allHidden = document.getElementsByName("styleId");
				var styleIds = "";
				if(allCheck.length == allHidden.length){	
					for(var i=0;i<allCheck.length;i++){
						if(allCheck[i].checked==true)
							styleIds += allHidden[i].value+":";
					}
				}
				return styleIds;
			}
			
			//设置保存排序按钮不可见
           function loadSaveOrder() {
				var div = document.getElementById("saveorder");
				div.style.visibility = "hidden";
			}
			
			function test() {
				//checkOne('checkBoxAll','ID')
				var allCheck = document.getElementsByName("ID");
				for(var i=0;i<allCheck.length;i++){
					if(allCheck[i].checked==true)
						alert(allCheck[i].value);
				}
			}
			
		</script>
	</head>

	<body topmargin="2" rightmargin="0" onload="loadSaveOrder()">
		<form name="templateStyleForm" id="templateStyleForm" action="" method="post" target="templateStyleFrame">
			<table width="100%" border="0" align=center cellpadding="3" cellspacing="0" id="docVerListTable" class="Datalisttable">
				<tr>
					<td colspan="7" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div class="DocumentOperT">
						模板风格管理：</div>
						<a style="cursor:hand" onClick="addaStyle()"><div class="DocumentOper">新增</div></a>
						<a style="cursor:hand" onClick="deleteStyles()"><div class="DocumentOper">删除</div></a>
						<a style="cursor:hand" onClick="top();"><div class="DocumentOper">移至顶部</div></a>
						<a style="cursor:hand" onClick="up();"><div class="DocumentOper">上移</div></a>
						<a style="cursor:hand" onClick="down();"><div class="DocumentOper">下移</div></a>
						<a style="cursor:hand" onClick="base();"><div class="DocumentOper">移至底部</div></a>
						<div id="saveorder">
						<a style="cursor:hand" onClick="saveOrder()"><div class="DocumentOper">保存排序</div></a>
						<div>
					</td>
				</tr>
				<pg:listdata dataInfo="com.frameworkset.platform.cms.templatemanager.tag.TemplateStyleList" 
								keyName="TemplateStyleList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="TemplateStyleList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->
						<td width="10%" height='30' align=center style="width:5%">
							<input class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
 					    </td>
						<td width="30%">风格名称</td>
						<td width="65%">风格描述</td>
					</tr>
					
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr  class="labeltable_middle_tr_01" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
							<td colspan=100 align='center'>还没有风格</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<pg:list>
						<%
							int styleId = dataSet.getInt("styleId");
						%>
						<tr class="cms_data_tr"  onMouseOver="this.style.background='#E9EDF3'"
							 onMouseOut="this.style.background='#FFFFFF'" id="<pg:cell colName="styleId" defaultValue=""/>">
							<td class="tablecells" align=center height='30'>
								<input name="styleId" value="<pg:cell colName="styleId" defaultValue=""/>" type=hidden>
								<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="styleOrder" defaultValue=""/>">
							</td>
							
							<td class="tablecells" align="center" style="cursor:hand" onclick="updateaStyle('<%=styleId%>','<pg:cell colName="styleName" defaultValue="" />','<pg:cell colName="siteId" defaultValue="" />','<pg:cell colName="styleDesc" defaultValue="" />')">
								<pg:cell colName="styleName" defaultValue="" />
							</td>
							<td class="tablecells" align="center">
								<pg:cell colName="styleDesc" defaultValue="" />
							</td>
						</tr>
					</pg:list>
					<tr class="labeltable_middle_tr_01">
						<td colspan=10><div class="Data_List_Table_Bottom">
							共
							<pg:rowcount />
							条记录
							<pg:index /></div>
						</td>
					</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
					<tr></tr>
				</pg:pager>

		  </table>
		</form>
		<iframe name="templateStyleFrame" width="0" height="0"></iframe>
	</body>
</html>