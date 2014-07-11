<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/validate_class.js"></script>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;

var id = '${param.id}';
   $(document).ready(function() {
	   //alert($('#frame1').src);
	    document.getElementById("frame1").src = "<%=request.getContextPath()%>/sysmanager/holiday/workTimeIndex.jsp?pid=" + id;
   });  
   
   function CloseDlg(){
	   api.close();
   }
   function confirm() {	
	   var id = $("#id").html();
	   var name = $('#name').val();
	   var desc = $("#desc").html();
	   var startDate = $('#startDate').val();
	   var endDate = $("#endDate").val();
	   if(null == name || "" == name||null == startDate || "" == startDate||null == endDate || "" == endDate){
		   $.dialog.alert("除描述外，其它信息不能为空！");
		   return;
	   }
	   if(!checkDate(startDate,endDate)){
		   $.dialog.alert("结束时间不能小于开始时间");
		   return;
	   }
        var dul_name = checkDateDuplicate(id,startDate,endDate);
	   
	   if(null != dul_name && "" != dul_name){
		   $.dialog.alert("本条与名称为："+dul_name+"的日期设置重复!");
		   return;
	   }
	    $.ajax({
	 	 	type: "POST",
			url : "updateWorkDate.page",
			data :{id:id,name:name,desc:desc,startDate:startDate,endDate:endDate},
			dataType : '',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data == "success") {
					W.$.dialog.alert("修改成功", function() {
						W.queryWorkDateList();
						api.close();
					}, api);
				} else {
					W.$.dialog.alert("修改失败", function() {
					}, api);
				}
			}	
		 }); 
	   	
	}
   function checkDate(startDate,endDate){//检查结束日期不能超过开始日期
	   var s_arr = startDate.split("-");
	   var e_arr = endDate.split("-");
		   if(parseInt(s_arr[0],10)<parseInt(e_arr[0],10)){
			   return true;
		   }else if(parseInt(s_arr[0],10)==parseInt(e_arr[0],10)){
			   if(parseInt(s_arr[1],10)<parseInt(e_arr[1],10)){
				   return true;
			   }else if(parseInt(s_arr[1],10)==parseInt(e_arr[1],10)){
				   if(parseInt(s_arr[2],10)<=parseInt(e_arr[2],10)){
					   return true;
				   }else{
					   return false;
				   }
			   }else{
				   return false;
			   }
		   }else{
			   return false;
		   }
   }
   function checkDateDuplicate(id,startDate,endDate){
	   var trs = W.document.getElementsByTagName("tr");
	   for(var i=1;i<trs.length;i++){
		   var p_id = trs[i].childNodes[0].innerHTML;
		   var name = trs[i].childNodes[2].innerHTML;
		   var sae = trs[i].childNodes[4].innerHTML.split("&nbsp;&nbsp;至&nbsp;&nbsp;");
		   var p_stratDate = sae[0];
		   var p_endDate = sae[1];
		   if(p_id != id){
			   if(checkDate(p_endDate,startDate)||checkDate(endDate,p_stratDate)){
				   continue;
			   }else{
				   return name;
			   }
		   }
		   
	   }
   }
   
</script>
</head>
	<body>
		<form  method="post" id="glform" name="glform" ></form>
			<div class="form_box"></div>
			
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
	   		   
	   		   
	   		   <tr>
		   		   <td>id</td>
		   		   <td id="id">${param.id}</td>
	   		   </tr>
	   		   
	   		   <tr>     
		   		   <td>名称</td>
		           <td><input  id='name' value='${param.name}' /></td>
	           </tr>
	           <tr>     
		   		   <td>描述</td>
		           <td><textarea rows="3" cols="20" id='desc' >${param.periodDesc}</textarea></td>
	           </tr>
	           <tr>
		   		   <td>开始时间</td>
		   		   <td><input type="text" id="startDate" onclick="WdatePicker()" value='${param.startDate}'/></td>
	   		   </tr>
	   		   <tr>
		   		   <td>结束时间</td>
		   		   <td><input type="text" id="endDate" onclick="WdatePicker()" value='${param.endDate}'/></td>
	   		   </tr>
		</table>
			        <a href="#" class="bt_2"  onclick="confirm()"><span>保存</span></a>
					<a href="#" class="bt_2"  onclick="CloseDlg()"><span>取消</span></a>
					<div   style="display:block;">
        <iframe id="frame1" height="680" width="900" scrolling="no" frameborder="0" vspace="0" hspace="0"  ></iframe> 
    </div>
	</body>
</html>
