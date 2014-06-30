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

var pid = '${param.pid}';
$(document).ready(function() {
	   queryWorkTimeList();
});  
  
    
  function queryWorkTimeList(){
	   if(null == pid || "" == pid){
		   return;
	   }
	   
	   $("#custombackContainer").load("queryWorkTimeList.page #customContent", {pid:pid},function(){loadjs();});
	  
  }
  function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
      return ( containSpecial.test(s) );      
 }
  
  
  function addWorkTime(){
		var content ="<tr>"+     
		    "<td  style=\"display:none\"></td>"+
		    "<td><input type=\"text\" /></td>"+ 
		    "<td><input type=\"text\"   onclick=\"WdatePicker({dateFmt:'HH:mm'})\"/></td>"+
		    "<td><input type=\"text\"   onclick=\"WdatePicker({dateFmt:'HH:mm'})\"/></td>"+
		    "<td><a href=\"#\" onclick=\"confirm(this)\"><span>保存</span></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href=\"#\" onclick=\"deleteTime(this)\"><span>删除</span></a></td>"+
		    "</tr>"  ;
	   $("#workTimeList").append(content);
}
  function deleteTime(obj){
	  var index = obj.parentNode.parentNode.rowIndex;
	  var id = obj.parentNode.parentNode.childNodes[0].innerHTML;
	  if(null != id && "" != id){
		  deleteWorkTime(id,obj,index);
	  }else{
		  obj.parentNode.parentNode.parentNode.deleteRow(index);
	  }
	  
  }
  function deleteWorkTime(id,obj,index){
	  $.ajax({
	 	 	type: "POST",
			url : "deleteWorkTime.page",
			data :{id:id},
			dataType : '',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data == "success") {
					W.$.dialog.alert("删除成功", function() {
						obj.parentNode.parentNode.parentNode.deleteRow(index);
					}, api);
				} else {
					W.$.dialog.alert("删除失败", function() {
						obj.parentNode.parentNode.style.backgroundColor="#ffcc00";
					}, api);
				}
			}	
		 });
  }
  function confirm(obj){
		 var id = obj.parentNode.parentNode.childNodes[0].innerHTML;
		 var name = obj.parentNode.parentNode.childNodes[1].childNodes[0].value;
		 var startTime = obj.parentNode.parentNode.childNodes[2].childNodes[0].value;
		 var endTime = obj.parentNode.parentNode.childNodes[3].childNodes[0].value;
		 var index = obj.parentNode.parentNode.rowIndex;
		 if(null == name || ""== name || null == startTime|| "" == startTime || null == endTime || "" == endTime){
			 $.dialog.alert('输入内容不得为空！');
			 return;
		 }
		
		 if(! checkTime(startTime,endTime)){
			 $.dialog.alert('结束时间不得小于开始时间！');
			 return;
		 }
		 var dul_index = checkTimeDuplicate(startTime,endTime,index);
		 if(null != dul_index && "" != dul_index){
			 $.dialog.alert('本条与第'+dul_index+"条时间设置重复");
			 return;
		 }
		 saveOrUpdateWorkTime(pid,id,name,startTime,endTime,obj);
	}
  function saveOrUpdateWorkTime(pid,id,name,startTime,endTime,obj){
	  $.ajax({
	 	 	type: "POST",
			url : "saveOrUpdateWorkTime.page",
			data :{pid:pid,id:id,name:name,startTime:startTime,endTime:endTime},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data.result == "success") {
					W.$.dialog.alert("保存成功", function() {
						obj.parentNode.parentNode.childNodes[0].innerHTML = data.id;
						obj.parentNode.parentNode.style.backgroundColor="";
					}, api);
				} else {
					W.$.dialog.alert("保存失败", function() {
						obj.parentNode.parentNode.style.backgroundColor="#ffcc00";
					}, api);
				}
			}	
		 });
  }
  function checkTime(startTime,endTime){
	 var s_arr = startTime.split(":");
	 var e_arr = endTime.split(":");
	 if(parseInt(s_arr[0],10)<parseInt(e_arr[0],10)){
		 return true;
	 }else if(parseInt(s_arr[0],10)==parseInt(e_arr[0],10)){
		 if(parseInt(s_arr[1],10)<=parseInt(e_arr[1],10)){
			 return true;
		 }else{
			 return false;
		 }
	 } else{
		 return false;
	 }
  }
  
  function checkTimeDuplicate(startTime,endTime,index){
	  var trs= document.getElementsByTagName("tr");
	  for(var i = 1;i<trs.length;i++){
		  var r_index = trs[i].rowIndex;
		  var r_startTime = trs[i].childNodes[2].childNodes[0].value;
			 var r_endTime = trs[i].childNodes[3].childNodes[0].value;
			 if(r_index != index){
				 if(null != r_startTime&& "" != r_startTime && null != r_endTime && "" != r_endTime){
					 if(checkTime(endTime,r_startTime)||checkTime(r_endTime,startTime)){
						 continue;
					 }else{
						 return r_index;
					 }
				 }else if(null != r_startTime&& "" != r_startTime && (null == r_endTime || "" == r_endTime)){
					 if(checkTime(endTime,r_startTime)){
						 continue;
					 }else{
						 return r_index;
					 }
				 }else if((null == r_startTime|| "" == r_startTime) && null != r_endTime && "" != r_endTime){
					 if(checkTime(r_endTime,startTime)){
						 continue;
					 }else{
						 return r_index;
					 }
				 }
			 }
			 
	  }
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
				<a href="#" class="bt_small" id="addButton" onclick="javascript:addWorkTime();"><span>新增工作时间</span></a>
				<a href="#" class="bt_2"  onclick="CloseDlg()"><span>关闭</span></a>
				</div>
				
				<strong></strong>				
			</div>
			<div id="add"></div>
			<div id="custombackContainer"  style="overflow:auto"></div>
		</div>
	</body>
</html>
