<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%%>
<html>
	<head>
		<title>属性容器</title>
		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="javascript" src="../scripts/selectTime.js"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<script language="JavaScript">
	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	function getResInfo(e,resId){
	jsAccessControl.setBackColor(e);
	document.all("selectId").value = resId;
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
	
	function dealRecord(dealType) {//删除资源
	    var isSelect = false;
	    var outMsg;
	    
	    for (var i=0;i<ResForm.elements.length;i++) {
			var e = ResForm.elements[i];
			
			if (e.name == 'ID'){
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
	        	ResForm.action="<%=rootpath%>/resmanager/resource.do?method=deleteres";
				ResForm.submit();
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
	
</script>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<form name="ResForm" action="" method="post">
				<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
					<tr valign='top'>
           					<td height='30'valign='middle' colspan="4"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>资源列表</strong></td>
   				  </tr>
					
					<pg:listdata dataInfo="ResSearchList" keyName="ResSearchList" />
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="12" scope="request" data="ResSearchList" isList="false">
						<tr class="labeltable_middle_td">
							<!--设置分页表头-->
							<th class="tablecells" width="10">
				  			<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
				  			</th>
				    		<th height='20' class="headercolor">资源类型</th><input class="text" type="hidden" name="selectId">
							<th height='20' class="headercolor">资源名称</th>
							<th height='20' class="headercolor">路径</th>
					</tr>
							<pg:param name="restypeId" />
							<pg:param name="restypeName" />
							<pg:param name="title" />
							<pg:param name="path" />
						
						<!--检测当前页面是否有记录-->
						<pg:notify>
							<tr height='25' class="labeltable_middle_tr_01">
								<td colspan=100 align='center'>
									暂时没有资源记录
								</td>
							</tr>
						</pg:notify>

						<!--list标签循环输出每条记录-->
						<pg:list>
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="getResInfo(this,'<pg:cell colName="resId" defaultValue=""/>')">
								<td class="tablecells" align=center><input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="resId" defaultValue=""/>"></td>						      				
								<td height='20' align=center class="tablecells"><pg:cell colName="restypeName" defaultValue="" /></td>
								<td height='20' align=center class="tablecells"><pg:cell colName="title" defaultValue="" /></td>
								<td height='20' align=center class="tablecells"><pg:cell colName="path" defaultValue="" /></td>
							</tr>
						</pg:list>
						<tr height="30" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'><div align="right">
						
							  <input type="button" value="删除" class="input"
								onclick="javascript:dealRecord(1); return false;">
							    </div>
							  <pg:index />
							</td>
						</tr>
					</pg:pager>

			  </table>

	          <div align="center"></div>
			</form>
		</div>
		<%@include file="../sysMsg.jsp" %>
	</body>
</html>

