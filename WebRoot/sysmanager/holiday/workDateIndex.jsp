<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>节假日管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>

<script type="text/javascript">
var api = frameElement.api, W = api.opener;

var areaId = '${param.areaId}';

   $(document).ready(function() {
	   queryWorkDateList();
   });  
     
       
     function queryWorkDateList(){
  	   if(null == areaId || "" == areaId){
  		   return;
  	   }
  	   
  	   $("#custombackContainer").load("queryWorkDateList.page #customContent", {areaId:areaId},function(){loadjs();});
  	  
     }
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	 
	
	 function addWorkDate(areaId){
		   $.dialog({
				  id : '',
				  title : '新增工作日期设置',
				  width : 450,
				  height : 500,
				  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/addWorkDate.jsp?areaId=" + areaId
			  });
	   }
	 
	 function editWorkDate(id,areaId,name,periodDesc,startDate,endDate){
		 $.dialog({
			  id : '',
			  title : '修改工作日期设置',
			  width : 450,
			  height : 500,
			  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/editWorkDate.jsp?id=" + id +"&name=" +name +"&periodDesc=" +periodDesc +"&startDate=" +startDate +"&endDate=" + endDate
		  });
	 }
	 function editWorkTime(pid){
		 $.dialog({
			  id : '',
			  title : '工作时间设置',
			  width : 750,
			  height : 500,
			  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/workTimeIndex.jsp?pid=" + pid 
		  });
	 }
	 function CloseDlg(){
		   api.close();
	   }
</script>
</head>
	<body>
		<div class="mcontent">
			<div class="title_box">
				<div class="rightbtn">
				<a href="#" class="bt_small" id="addButton" onclick="javascript:addWorkDate('${param.areaId}');"><span>新增工作日期</span></a>
				<a href="#" class="bt_2"  onclick="CloseDlg()"><span>关闭</span></a>
				</div>
				
				<strong></strong>				
			</div>
			<div id="custombackContainer"  style="overflow:auto"></div>
		</div>
	</body>
</html>
