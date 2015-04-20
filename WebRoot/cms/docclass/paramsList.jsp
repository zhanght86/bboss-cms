<%--
	描述：参数管理
	作者：gw_hel
	版本：1.0
	日期：2012-08-07
--%>
<%@ page language="java"  pageEncoding="utf-8"%>
<%@ page import="com.frameworkset.platform.framework.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,java.util.List,com.frameworkset.platform.framework.MenuHelper"%>
<%@page  import="com.frameworkset.platform.cms.sitemanager.SiteManager,com.frameworkset.platform.cms.sitemanager.SiteManagerImpl"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>文档内部分类管理</title>
<script language="javascript">
	//增加一行
	function addRow() {
		 var tr = $("#tb1 tr").eq(0).clone();
	
		 tr.show();
         tr.appendTo("#tb");
	}
	
	//删除被指定的行
	function deleteRow() {
		var codes="";
		
	   	$("#tb tr:gt(0)").each(function() {
		 
	   	if ($(this).find("#CK").get(0).checked == true) {
	   			
	   				$(this).remove();
	   			
	   		} 
	  	});
	   
	}
	
	function doSubmit() {
		
	
		$("#paramForm").form('submit', {
		    "url": "saveClasses.page",
		    onSubmit:function(){		
		    },
		    success:function(responseText){	
		    	if(responseText=="success"){
					$.dialog.alert("保存文档内部分类成功");													
				}else{
					$.dialog.alert("保存文档内部分类出错:"+responseText);
				}
		    	//document.location.href = document.location.href ; 
		    }
		});	
	}
</script>
</head>
<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="right">
					<a href="javascript:void(0)" onclick="addRow()" class="bt_2" ><span>+</span></a> 
					<a href="javascript:void(0)" onclick="deleteRow()" class="bt_2" ><span>-</span></a>
					<a href="javascript:void(0)" onclick="doSubmit()" class="bt_1" ><span>保存</span></a>
				</td>
			</tr>
		</table>
		 <table width="100%" border="0" cellpadding="0" cellspacing="0"  id="tb1">
		 	<tr style="display:none">
		   		    <td class="td_center" ><input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" /><input type="hidden" name="site_id" value="${currentSiteid}"/></td>
		        	<td><input id="class_name" name="class_name" type="text" value="" class="W120" style="width:100%"/></td>  
		        	<td><input id="class_desc" name="class_desc" type="text" value="" class="W120" style="width:100%" /></td>
		        	
		     </tr>
		  </table>
		   <form id="paramForm" name="paramForm">
	
			
			<div id="changeColor">
			 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
		        <tr>
		            <th align=center width="40px"><input id="CKA" name="CKA" type="checkbox" 
									onClick="checkAll('CKA','CK')"></th>
		       		<th>文档内部分类名称</th>
		       		<th>文档内部分类描述</th>
		       	</tr>	
		       
		
		      <pg:list requestKey="datas">
		   		<tr>
		   		    <td class="td_center"><input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" /><input type="hidden" name="site_id" value="${currentSiteid}"/></td>
		        	<td><input id="class_name" name="class_name" type="text" value="<pg:cell colName='class_name' />" class="W120" style="width:100%"/></td>  
		        	<td><input id="class_desc" name="class_desc" type="text" value="<pg:cell colName='class_desc' />" class="W120" style="width:100%" /></td>
		        </tr>
			 </pg:list>
		    </table>
		    </div>
		    </form>
</body>
<html>

