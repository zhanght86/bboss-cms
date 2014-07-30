<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
 <%@ include file="/common/jsp/css.jsp"%> 
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<link href="stylesheet/system.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var api = frameElement.api;
$(document).ready(function() {
	 getSysPendingNum();
}); 
/*
获取各系统待办数目
 */
function getSysPendingNum(){
	 var titles = document.getElementsByName("sys_title");
		for(var i=0;i<titles.length;i++){
			var id = titles[i].value;
			var name = document.getElementById(id+"_nameEN").value;
			var type = document.getElementById(id+"_type").value;
			$.getJSON("getSysPendingNum.page", {id:id,name:name,type:type},
		           function(data) {
				       //$("#"+data.split(":")[0]+"_num").text(data.split(":")[1]) ;
		        	   $("#"+data.id).text(data.num) ;
		           });
		}
}
 
  function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
      return ( containSpecial.test(s) );      
 }
  
  
	function CloseDlg(){
		   api.close();
	 }
	
/*获取用户的待办详细信息 	 */
function getPendingDetail(id){
	    var num = $("#"+id+"_num").text();
	      //没有待办则不触发请求
		 if(null == num || "0"==num){
			return;
		} 
	      //页面已经打开，再点击则关闭
		if(null==document.getElementById(id+"_detail").style.display||"none"!=document.getElementById(id+"_detail").style.display){
			document.getElementById(id+"_detail").style.display="none";
			return;
		}
	    //页面已经打开过，则重新打开，但不发出请求
		if(""!=document.getElementById(id+"_detail").innerHTML&&"none"==document.getElementById(id+"_detail").style.display){
			document.getElementById(id+"_detail").style.display="";
			return;
		} 
	    //页面没有打开则发起请求  
		$.ajax({
	 	 	type: "POST",
			url : "getSysPending.page",
			data :{id:id},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data) {
					var content = wrapDatas(data);
					document.getElementById(id+"_detail").style.display="";
					document.getElementById(id+"_detail").innerHTML = content;
				} 
			}	
		 });
}
/* 封装数据为HTML形式       */
function wrapDatas(datas){
	var tableTH="<tr>"+
    "<th scope=\"col\" width=\"30\">序号</th>"+
    "<th scope=\"col\" width=\"160\">申请人</th>"+
    "<th scope=\"col\">内容</th>"+
    "<th scope=\"col\" width=\"160\">申请日期</th>"+
    "<th scope=\"col\" width=\"80\">操作</th>"+
    "</tr>";
	
	var tableHead="<table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"system_table\">";
	var tableFoot="</table>";
	var content = tableTH;
	for(var i=0;i<datas.length;i++){
		var trId = i+1;
		var tr= "<tr>"+
	    "<td>"+trId+"</td>"+
	    "<td>"+datas[i].sender+"("+datas[i].userAccount+")"+"</td>"+
	   " <td>"+datas[i].title+"</td>"+
	   " <td>"+datas[i].createTime+"</td>"+
	   " <td><a href=\"#\" onclick=\"openSysUrl('"+datas[i].url+"','"+datas[i].title+"')\">"+datas[i].dealButtionName+"</a></td>"+
	  "</tr>";
		content = content + tr;
	}
	return tableHead + content + tableFoot;
	
}
   function subscribePending(){
	   $.dialog({
			id : 'subscribePending',
			title : '待办订阅',
			width : 900,
			height : 450,
			content : 'url:' + "<%=request.getContextPath()%>/workflow/pending/subscribePending.page"
		});
   }
   
   function openSysUrl(url,title){
	   
	   //window.open(url,'newwindow','height=800,width=1400,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
	    $.dialog({
			id : '',
			title : title,
			width : 1400,
			height : 800,
			content : 'url:' + url
		});  
 
   }
   
   function modifyQueryData(sysid){
	   var id = document.getElementById(sysid).value;
	   //页面没有打开则发起请求  
		$.ajax({
	 	 	type: "POST",
			url : "getSysPending.page",
			data :{id:id},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data) {
					var content = wrapDatas(data);
					document.getElementById(id+"_detail").style.display="";
					document.getElementById(id+"_detail").innerHTML = content;
				} 
			}	
		 });
   }
   function findPic(picName){
	   if(null == picName || ""==picName){
		   return;
	   }
	   $.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/application/findPic.page",
			data :{picName:picName},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				 
			}	
		 });
   }
</script>
</head>
	<body>
<div class="contain_system">
  <!-- <div class="top"><img src="images/top_sys.gif" width="1000" height="65" alt="顶部" /></div>    <pg:cell colName="pendingNum"/>-->
  <div class="main_contain">
    <div class="sys_dot"><a href="#" onclick="subscribePending()"><img src="images/dot_sys.gif" width="13" height="11" />&nbsp;&nbsp;待办订阅</a></div> 
      <pg:list  autosort="false" requestKey="datas">
      <div class="system_msg"  id="<pg:cell colName="id"/>" onclick="getPendingDetail('<pg:cell colName="id"/>')">
      <div class="system_img"><img src="${pageContext.request.contextPath}/application/app_images/<pg:cell colName="picName"/>" width="82" height="82"  onerror="findPic('<pg:cell colName="picName"/>')"/></div>
      <span class="system_name"><pg:cell colName="nameEN"/>&nbsp;&nbsp;<pg:cell colName="nameCH"/></span><br/>
      您共有<span class="num" id="<pg:cell colName="id"/>_num"></span>条待办</div>
      <div class="do_list" style="display:none"  id="<pg:cell colName="id"/>_detail"></div>
      <input type="text" id="<pg:cell colName="id"/>_nameEN" value="<pg:cell colName="nameEN"/>" style="display:none" >
      
      <input type="text" id="<pg:cell colName="id"/>_type" value="<pg:cell colName="pendingType"/>" style="display:none" >
      <input type="text" value="<pg:cell colName="id"/>" name="sys_title" style="display:none" >
      <input type="text" value="<pg:cell colName="id"/>" id='<pg:cell colName="nameEN"/>' style="display:none" >
	 </pg:list>
    
    
</div>
</body>

</html>
