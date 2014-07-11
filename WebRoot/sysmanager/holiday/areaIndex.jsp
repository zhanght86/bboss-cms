<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>节假日管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>

<script type="text/javascript">
     
     $(document).ready(function() {
    	 queryList();
     }); 
     function queryList(){	
    	 $("#custombackContainer").load("queryAreaList.page #customContent", {},function(){loadjs();});
	 }
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	 
	
	 
	 function editArea(areaId){
		 $.dialog({
			  id : '',
			  title : '修改区域信息',
			  width : 550,
			  height : 250,
			  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/editArea.jsp?areaId=" + areaId 
		  });
	 }
	 function addArea(){
		 $.dialog({
			  id : '',
			  title : '新增区域',
			  width : 550,
			  height : 250,
			  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/addArea.jsp"
		  });
	 }
	 
	 function editOrgList(areaId,areaName){
		 $.dialog({
			  id : '',
			  title : areaName+'所辖部门',
			  width : 550,
			  height : 500,
			  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/orgIndex.jsp?areaId=" +areaId
		  });
	 }
	 function editHoliday(areaId,areaName){
		 $.dialog({
			  id : '',
			  title : areaName+'假日安排',
			  width : 1024,
			  height : 800,
			  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/arrange.jsp?areaId=" +areaId,
			  maxState:true
		 });
	 }
	 function queryWorkDate(areaId,areaName){
		 $.dialog({
			  id : '',
			  title : areaName+'工作时间设置',
			  width : 800,
			  height : 500,
			  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/workDateIndex.jsp?areaId=" +areaId
			  	  
		  });
	 }
</script>
</head>
	<body>
		<div class="mcontent">
			<sany:menupath menuid="holidaymanager" />
			
			<div class="title_box">
				<div class="rightbtn">
				<a href="#" class="bt_small" id="addButton" onclick="javascript:addArea();"><span>新增区域</span></a>
				</div>
				
				<strong>区域列表</strong>				
			</div>
			<div id="custombackContainer"  style="overflow:auto">
			
			</div>
		</div>
	</body>
</html>
