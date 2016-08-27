<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String dicttypeId = (String)request.getParameter("dicttypeId");	
	dicttypeId = dicttypeId==null?"":dicttypeId;
	Data dtype;
	String dicttype_name= "";
	String description= "";
	String dicttypeName_ = "";
	int dicttypeName;
	String dictTableName= "";
	if(null != dicttypeId && !"".equals(dicttypeId))
	{
		DictManager dictManager = new DictManagerImpl();
		dtype = dictManager.getDicttypeById(dicttypeId);
		//字典名称
		 dicttype_name = dtype.getName();
		 description = dtype.getDescription();
		//字典类型种类名称
		 dicttypeName = dtype.getDicttype_type();
		 dicttypeName_ = "";
		//字典类型对应的表
		 dictTableName = dtype.getDataTableName();
		if(dicttypeName==DictManager.BASE_DICTTYPE){
		    dicttypeName_ = "基础字典";
		}else if(dicttypeName==DictManager.ALLREAD_BUSINESS_DICTTYPE){
		    dicttypeName_ = "通用业务字典";
		}else if(dicttypeName==DictManager.PARTREAD_BUSINESS_DICTTYPE){
		    dicttypeName_ = "授权业务字典";
		}else if(dicttypeName==DictManager.BUSINESS_DICTTYPE_POWERONLY){
		    dicttypeName_ = "不维护数据授权业务字典";
		}else{
		    dicttypeName_ = "不维护数据通用业务字典";
		}
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
		<SCRIPT language="JavaScript" SRC="../../user/validateForm.js"></SCRIPT>
  
<SCRIPT language="javascript">
	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	var win;
	var featrue = "dialogWidth=700px;dialogHeight=600px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-700)/2+";dialogTop="+(screen.availHeight-600)/2;
	function unable(){
	    document.all.newbt.disabled = true;
		document.all.up.disabled = true;
        document.all.down.disabled = true;        
        document.all.top.disabled = true;
        document.all.bottom.disabled = true;
        document.all.save_order.disabled = true;
        document.all.deletebt.disabled = true;
	}
	function closeWin(){
	    win.close();
	}
	function enable(){	  
	    document.all.newbt.disabled = false;  
        document.all.up.disabled = false;
        document.all.down.disabled = false;        
        document.all.top.disabled = false;
        document.all.bottom.disabled = false;
        document.all.save_order.disabled = false;
        document.all.deletebt.disabled = false;
	}
	function afterAddRefresh(){
	    
	}
	function afterDeleteRefresh(){
	    
	    document.location.href = document.location.href;
	}
	function deleteField(dealType) {
	    var isSelect = false;
	    var outMsg;
		var selecet_value = "";   
		var arr = document.getElementsByName("table_column");
	    for (var i=0;i<arr.length;i++) {
			if (arr[i].checked){
		       	isSelect=true;
		       	if(selecet_value=="") selecet_value = selecet_value + arr[i].value;
		       	else selecet_value = selecet_value + "," + arr[i].value;
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
					var path = "advanceFieldDelete.jsp?dicttypeId=<%=dicttypeId%>&tableColumn="+selecet_value;			
					document.fieldList.target = "hiddenFrame";
					document.fieldList.action = path;
					win = window.showModelessDialog("../doing.jsp","",featrue);
					document.fieldList.submit();
					
				}
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		//return false;
	}	
	function newField(){
		var path = "advanceFieldSet.jsp?dicttypeId=<%=dicttypeId%>";
		//window.open(path);
		var win = window.showModalDialog(path,window,featrue);		
		if(win=="ok"){
		    window.location.href = window.location.href;
		}
	}
	function editField(){
	    var isSelect = false;
	    var selecet_value = "";
	    var arr = document.getElementsByName("table_column");
	    var count = 0;
	    for (var i=0;i<arr.length;i++) {
			if (arr[i].checked){
		       	isSelect=true;
		       	selecet_value = arr[i].value;		       	
                break;	
			}
	    }
	    for (var i=0;i<arr.length;i++) {
			if (arr[i].checked){
		       	count = count + 1;
			}
	    }
	    if(count>1){
	        count = 0;
	        alert("最多只能选择一条记录！");	        
	    	return false;
	    }
	    if (isSelect){			
			var path = "advanceFieldSet.jsp?dicttypeId=<%=dicttypeId%>&tableColumn="+selecet_value;	
			var win = window.showModalDialog(path,window,featrue);
			if(win=="ok"){
			    window.location.href = window.location.href;
			}
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
	}
	
	//排序操作
	function topTr()
	{
		var all = document.getElementsByName("table_column");
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				flag ++;
				if(flag > 1)
				{
					alert("请选择单个高级字段移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的高级字段！");
			return false;
		}
		for(var i=0;i<all.length;i++){
			upTr();
		}
	}
	function upTr()
	{
		var all = document.getElementsByName("table_column");
		var row;
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				row = all[i];
				flag ++;
				if(flag > 1)
				{
					alert("请选择单个高级字段移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的高级字段！");
			return false;
		}
		var tmp = row;
		upRow(row);
		tmp.checked = true;
	}
	function downTr()
	{
		var all = document.getElementsByName("table_column");
		var row;
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				row = all[i];
				flag ++;
				if(flag > 1)
				{
					alert("请选择单个高级字段移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的高级字段！");
			return false;
		}
		var tmp = row;
		downRow(row);
		tmp.checked = true;
	}
	function base()
	{
		var all = document.getElementsByName("table_column");
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				flag ++;
				if(flag > 1)
				{
					alert("请选择单个高级字段移动！");
					return false;
				}
			}
		}
		if(flag < 1)
		{
			alert("请选择要移动的高级字段！");
			return false;
		}
		for(var i=0;i<all.length;i++){
			downTr();
		}
	}
	//表格row上移
	function upRow(e){
		var _row=e.parentElement.parentElement;
		if(_row.previousSibling.previousSibling)
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
		if(_row.nextSibling.nextSibling)
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
		if(_t1){
			_parent.insertBefore(node2,_t1);
		}else{
			_parent.appendChild(node2);
		}
		//将node1插入到原来node2的位置
		if(_t2){
			_parent.insertBefore(node1,_t2);
		}else{
			_parent.appendChild(node1);
		}
	}
	//保存置顶顺序
	function subform()
	{
		var all = document.getElementsByName("table_column");
		var docid="";
		var falg = false;
		for(var i=0;i<all.length;i++){
			if(falg){
				docid += "," + all[i].value;
			}else{
				docid += all[i].value;
				falg = true;
			}
		}
		if(docid == "")
		{
			alert("该字典类型暂时没有字典数据！");
			return false;
		}
		//alert(docid)
		document.forms[0].action = "saveAdvanceFieldArr.jsp?dicttypeId=<%=dicttypeId%>&docid="+docid;
		document.forms[0].target = "hiddenFrame";
		document.forms[0].submit();
	}
	
</SCRIPT>		
	<body>
		<form name="fieldList" method="post" target="hiddenFrame">
			<table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
			    <tr cols=5>
 				    <td colspan="5" height='30' colspan="1" align="center"   >  					
					    <img src='../../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>【<%=dicttype_name%>-<%=description%>】字典附加字段 </strong>
					</td>
					
				</tr>
				<tr>
					<td height='30' width="50%" colspan="1" align="center"   >  					
					    <strong>字典类型种类:&nbsp;&nbsp;&nbsp;&nbsp;<%=dicttypeName_%></strong>
					</td>
					<td height='30' colspan="1" align="center"   >  					
					    <strong>对应字典表:&nbsp;&nbsp;&nbsp;&nbsp;<%=dictTableName%></strong>
					</td>
 				</tr>
 			</table>
 			<table cellspacing="1" cellpadding="0" border="0" width=98%  class=""> 
 				<tr>
 				    <td height='30' colspan="1"  style="text-align:left" >  					
					    <input name="newbt" type="button" class="input" value="添加" onClick="newField()">
					    <input name="newbt" type="button" class="input" value="修改" onClick="editField()">	
					    <input name="deletebt" type="button" value="删除" class="input" onclick="deleteField(1)">
					    <input name="top" type="button" class="input" value="移至顶部" onClick="topTr()">	
 				        <input name="up" type="button" class="input" value="上移" onClick="upTr()">	
 				        <input name="down" type="button" class="input" value="下移" onClick="downTr()">	
 				        <input name="bottom" type="button" class="input" value="移至底部" onClick="base()">
 				        <input name="bottsub" type="button" class="input" value="保存排序" onClick="subform()">							
					</td>
 				</tr>				
			</table>
			
			<table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
				<pg:listdata dataInfo="com.frameworkset.platform.dictionary.tag.DictAttachFieldList" keyName="DictList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="15" scope="request" data="DictList" isList="false">
					<tr>
						<!--设置分页表头-->
						<td class="headercolor" style="text-align:center"  >
							<input type="checkBox"  name="checkBoxAll" onClick="checkAll('checkBoxAll','table_column')">
						</td>							
						<td class="headercolor" >字典字段显示名称</td>
						<td class="headercolor" >输入类型</td>
						<td class="headercolor" >字典字段对应的表字段</td>
						<td class="headercolor" >能否为空</td>
					</tr>
					

					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>
								暂时没有数据
							</td>
						</tr>
					</pg:notify>
					<!--翻页-->
                    <pg:param name="dicttypeId" />
					<!--list标签循环输出每条记录-->
					<pg:list>						
						<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
							<td class="tablecells" nowrap="nowrap">
								<input type="checkBox" name="table_column" onClick="checkOne('checkBoxAll','table_column')" value="<pg:cell colName='table_column' defaultValue='' />" width="10">
								
							</td>								
							<td class="tablecells" nowrap="nowrap" colspan="1">
							    <pg:cell colName="dictFieldName" defaultValue="" />								
							</td>
							<td class="tablecells" nowrap="nowrap" colspan="1">
							    <pg:cell colName="inputTypeName" defaultValue="" />								
							</td>
							<td class="tablecells" nowrap="nowrap" colspan="1">
							    <pg:cell colName="table_column" defaultValue="" />								
							</td>
							<td class="tablecells" nowrap="nowrap" colspan="1">
							    <pg:equal colName="isnullable" value="1">不可为空</pg:equal>	
							    <pg:equal colName="isnullable" value="0">可为空</pg:equal>				
							</td>
						</tr>
					</pg:list>
					<tr height="18px" class="labeltable_middle_tr_01">
						<td colspan=5 align='center'>
						 <pg:index />
					<input name="queryString" value="<pg:querystring/>" type="hidden">
						</td></tr>
					
				</pg:pager>

		  </table>
		</form>		
	</body>
	<iframe height="0" width="0" name="hiddenFrame"></iframe>	
</html>


