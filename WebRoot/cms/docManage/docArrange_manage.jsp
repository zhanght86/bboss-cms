<%
/**
  *	当前频道下所有的置顶文档列表
  * 
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String siteid = request.getParameter("siteid");
	String channelid = request.getParameter("channelid");
%>
<html>
	<head>
		<base target=_self><!-- use for submit to self-->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>.::::::::::文档置顶管理:::::::::::::::::::::::::::::::::::::..</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript">
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
	//修改置顶时间
	function updateArrangeDoc()
	{
		var all = document.getElementsByName("ID");
		var docid = "";
		var f = 0;//暂时只考虑只保存一篇文档的版本
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				f++;
				if(f>1)
				{
					alert("一次只能选择一篇文档进行操作!");
					return;
				}
				docid = docid + all[i].value;// + ":";
			}
		}
		if(haveSelect('ID')){
			openWin("<%=rootpath%>/cms/docManage/docArrange_update.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>&docid=" + docid,555,240);
			return true;
		}else{
	    	alert("请选择要求修改置顶时间的文档！");
	    	return false;
	   	}
	}
	//取消置顶
	function delDocArrange()
	{
		var all = document.getElementsByName("ID");
		var docid="";
		var f = 0;//暂时只考虑只保存一篇文档的版本
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				f++;
				docid = docid + all[i].value + ":";
			}
		}
		if(haveSelect('ID')){
			if(window.confirm("您确定要取消该文档的置顶吗？"))
			{
				document.forms[0].action = "<%=rootpath%>/cms/docManage/docArrange_del_submit.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>&docid=" + docid;
				document.forms[0].target = "managedocarrange";
				document.forms[0].submit();
			}
		}else{
			alert("请选择要取消置顶的文档！");
			return false;
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
					alert("请选择单篇文档移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的文档！");
			return false;
		}
		for(var i=0;i<all.length;i++){
			up();
		}
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
					alert("请选择单篇文档移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的文档！");
			return false;
		}
		var tmp = row;
		upRow(row);
		tmp.checked = true;
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
					alert("请选择单篇文档移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的文档！");
			return false;
		}
		var tmp = row;
		downRow(row);
		tmp.checked = true;
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
					alert("请选择单篇文档移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的文档！");
			return false;
		}
		for(var i=0;i<all.length;i++){
			down();
		}
	}
	//表格row上移
	function upRow(e){
		var _row=e.parentElement.parentElement;
		if(_row.previousSibling.previousSibling.previousSibling)
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
		if(_row.nextSibling.nextSibling.nextSibling)
		{	
			//alert(_row.nextSibling.outerHTML);
			swapNode(_row,_row.nextSibling);
		}

	}
	//
	function swapNode(node1,node2){
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
	//保存置顶顺序
	function subform()
	{
		var all = document.getElementsByName("ID");
		var docid="";
		for(var i=0;i<all.length;i++){
			docid = docid + all[i].value + ":";
		}
		if(docid == "")
		{
			alert("该频道暂时没有置顶文档！");
			return false;
		}
		document.forms[0].action = "<%=rootpath%>/cms/docManage/docArrange_no_submit.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>&docid=" + docid;
		document.forms[0].target = "managedocarrange";
		document.forms[0].submit();
	}
	</script>
	
	</head>
	<script language="javascript">
		document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
	</script>
	<body topmargin="2" rightmargin="0">
		<form target="managedocarrange" name="manageDocVerform" action="" method="post">
			<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bgcolor="#FFFFFF" id="docArrangeListTable"  class="Datalisttable">
				<tr>
					<td height='25' colspan="7" background="<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg" style="text-align:left; background:url(<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
					<div  class="DocumentOperT">文档置顶管理：</div>
						<a style="cursor:hand" onClick="updateArrangeDoc()"><div class="DocumentOper">修改时间</div></a>
						<a style="cursor:hand" onClick="delDocArrange()"><div class="DocumentOper">取消置顶</div></a>
						<a style="cursor:hand" onClick="top();"><div class="DocumentOper">移至顶部</div></a>
						<a style="cursor:hand" onClick="up();"><div class="DocumentOper">上移</div></a>
						<a style="cursor:hand" onClick="down();"><div class="DocumentOper">下移</div></a>
						<a style="cursor:hand" onClick="base();"><div class="DocumentOper">移至底部</div></a>
					</td>
				</tr>
				<pg:listdata dataInfo="DocArrangeList" keyName="DocArrangeList" />
				<pg:pager scope="request" data="DocArrangeList" isList="true">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td style="width:5%" align=center height='30'>
							<input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
						</td>
						<td width="40%" height='30'>
							文档标题
						</td>
						<td width="10%" height='30'>
							开始置顶时间
						</td>
						<td width="10%" height='30'>
							结束置顶时间
						</td>
						<td width="15%" height='30'>
							是否过期
						</td>
						<td width="*" height='30'>
							操作人
						</td>
					</tr>

					<pg:param name="docid" />
					
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该频道暂时没有置顶文档
							</td>
						</tr>
					</pg:notify>

					<pg:list>

						<tr class="cms_data_tr" id="<pg:cell colName="documentId" defaultValue=""/>">
							<td class="tablecells" align=center height='30' width="5%">
								<input name="docid" value="<pg:cell colName="documentId" defaultValue=""/>" type=hidden>
								<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="documentId" defaultValue=""/>">
							</td>
							
							<td class="tablecells" nowrap="nowrap" width="42%" style="cursor:hand" onMouseOver="high(this)" onMouseOut="unhigh(this)" onClick="viewDoc(<pg:cell colName="documentId" defaultValue=""/>,manageDocVerform,'managedocarrange')">
								<pg:cell colName="doctitle" defaultValue="" />
							
							</td>

							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:cell colName="startTime" defaultValue="" />
							</td>

							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:cell colName="endTime" defaultValue="" />
							</td>
                            <td class="tablecells" nowrap="nowrap" width="7%">
                            <% 
								if(dataSet.getInt("overtime")==1)
								{
								%>
								<a style="Color:red">过期</a>
								<%
								}
								%>
							</td>
							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:cell colName="username" defaultValue="" />
							</td>
						</tr>
					</pg:list>
					<tr class="labeltable_middle_tr_01">
						<td align="right" colspan=7><div class="Data_List_Table_Bottom">
						<input type="button" name="ok" value="保存顺序" onClick="subform();" class="cms_button"/>
						<input type="button" name="closeb" value="关闭窗口" onClick="window.close();" class="cms_button"/></div>
						</td>
					</tr>
					<tr></tr>
				</pg:pager>

		  </table>
		</form>
		<iframe height="0" width="0" name="managedocarrange"></iframe>
	</body>
</html>