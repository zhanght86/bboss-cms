<%
/**
  * 站点或频道的扩展字段列表
  *
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String id = request.getParameter("id");//频道id
%>
<html>
	<head>
		<base target=_self><!-- use for submit to self-->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>.::::::::::扩展字段管理::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
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
	//add 扩展字段
	function addextfield()
	{
		openWin("doc_extfield_add.jsp?id=<%=id%>",400,400);
	}
	//del 扩展字段
	function delextfield(type)
	{
		//1为删除引用
		//2为彻底删除
		var all = document.getElementsByName("ID");
		var ids = "";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				ids = ids + all[i].value + ",";
			}
		}
		if(haveSelect('ID')){
			if(window.confirm("删除扩展字段有可能造成数据丢失\n您确定要删除吗？"))
			{
				if(type==2)
				{
					openWin("doc_extfield_del_do.jsp?ids=" + ids,400,500);
				}
				else
				{
					openWin("doc_extfield_del_do.jsp?type=2&id=<%=id%>&ids=" + ids,400,500);
				}
				return true;
			}
			else
			{
				return false;
			}
		}else{
			alert("请选择要删除的项！");
			return false;
		}
		
	}
	//查看所有扩展字段
	function lookallextfield()
	{
		//openWin("doc_extfieldofall_list.jsp",850,580);
		openWin("doc_extfieldlist_frame.jsp?id=<%=id%>",850,580);
	}
	</script>
	
	</head>

	<body topmargin="2" rightmargin="0">
		<form target="allextfieldlist" name="allextfieldlistform" action="" method="post">
		<pg:dtoken/>
			<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bgcolor="#FFFFFF" id="docVerListTable" class="Datalisttable">
				<tr >
					<td colspan="6" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div class="DocumentOperT">扩展字段管理：</div>
					<a style="cursor:hand" onClick="addextfield()"><div class="DocumentOper"><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">新增</div></a>
						<a style="cursor:hand" onClick="delextfield(1)"><div class="DocumentOper"><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">删除</div></a>
						<a style="cursor:hand" onClick="delextfield(2)"><div class="DocumentOper"><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">彻底删除</div></a>
						<a style="cursor:hand" onClick="lookallextfield()"><div class="DocumentOper"><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">已有字段</div></a>
<!--						<a style="cursor:hand" onClick="history.go(-1);"><u><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">返回频道文档列表</u></a>-->
					</td>
				</tr>
				<pg:listdata dataInfo="com.frameworkset.platform.cms.customform.tag.DocExtFieldOfSiteOrChlList" keyName="DocExtFieldOfSiteOrChlList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="15" scope="request" data="DocExtFieldOfSiteOrChlList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td style="width:5%" align=center height='30'>
							<input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"/>
						</td>
						<td width="20%" height='30'>
							字段名称
						</td>
						<td width="20%" height='30'>
							字段label
						</td>
						<td width="10%" height='30'>
							字段类型
						</td>
						<td width="10%" height='30'>
							字段最大长度
						</td>
						<td width="*" height='30'>
							字段描述
						</td>
					</tr>

					<pg:param name="id" />
					<pg:param name="type" />
					
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								还没有扩展字段
							</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<pg:list>
						<tr class="cms_data_tr" onMouseOver="high(this)" onMouseOut="unhigh(this)">
							<input name="fieldId" value="<pg:cell colName="fieldId" defaultValue=""/>" type=hidden>
							<input name="fieldName" value="<pg:cell colName="fieldName" defaultValue=""/>" type=hidden>
							
							<td class="tablecells" align=center height='30' width="5%">
								<input class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="fieldId" defaultValue=""/>">
							</td>
							
							<td class="tablecells" nowrap="nowrap" width="20%" style="cursor:hand" onclick="return openWin('doc_extfield_update.jsp?fieldid=<pg:cell colName="fieldId" defaultValue=""/>',400,400);">
								<pg:cell colName="fieldName" defaultValue="" />
							</td>

							<td class="tablecells" nowrap="nowrap" width="20%">
								<pg:cell colName="fieldLable" defaultValue="" />
							</td>
							
							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:equal colName="fieldType" value="0">number</pg:equal>
								<pg:equal colName="fieldType" value="1">varchar</pg:equal>
								<pg:equal colName="fieldType" value="2">datetime</pg:equal>
								<pg:equal colName="fieldType" value="3">clob</pg:equal>
								<pg:equal colName="fieldType" value="4">file</pg:equal>
								<pg:equal colName="fieldType" value="5">select</pg:equal>
								<pg:equal colName="fieldType" value="6">radiobox</pg:equal>
								<pg:equal colName="fieldType" value="7">checkbox</pg:equal>
							</td>
							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:notequal colName="maxlen" value="0">
								<pg:cell colName="maxlen" defaultValue="" /></pg:notequal>
							</td>
							
							<td class="tablecells" nowrap="nowrap" align="center" width="*">
								<pg:cell colName="fieldDesc" defaultValue="" />
							</td>
						</tr>
					</pg:list>
					<tr class="labeltable_middle_tr_01">
						<td colspan=10 align='center'  height="18px">
							<div class="Data_List_Table_Bottom">
							共
							<pg:rowcount />
							条记录
							<pg:index />
							</div>
						</td>
					</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
		  </table>
		</form>
		<iframe height="0" width="0" name="allextfieldlist"></iframe>
	</body>
</html>


