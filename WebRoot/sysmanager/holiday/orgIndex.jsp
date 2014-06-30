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
	   queryOrgList();
   });  
     
       
     function queryOrgList(){
  	   if(null == areaId || "" == areaId){
  		   return;
  	   }
  	   
  	   $("#custombackContainer").load("queryOrgList.page #customContent", {areaId:areaId},function(){loadjs();});
  	  
     }
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	 
	 function deleteOrg (areaId,orgId){
		   $.ajax({
		 	 	type: "POST",
				url : "deleteOrg.page",
				data :{orgId:orgId,areaId:areaId},
				dataType : '',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if (data == "success") {
						W.$.dialog.alert("删除成功", function() {
							queryOrgList();
							 /*api.close(); */
						}, api);
					} else {
						W.$.dialog.alert("删除失败", function() {
						}, api);
					}
				}	
			 }); 
	   }
	 function addOrg(areaId){
		   $.dialog({
				  id : '',
				  title : '新增所辖部门',
				  width : 450,
				  height : 500,
				  content : "url:" + "<%=request.getContextPath()%>/sysmanager/holiday/addOrg.jsp?areaId=" + areaId
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
				<a href="#" class="bt_small" id="addButton" onclick="javascript:addOrg('${param.areaId}');"><span>新增部门</span></a>
				<a href="#" class="bt_2"  onclick="CloseDlg()"><span>关闭</span></a>
				</div>
				
				<strong>区域列表</strong>				
			</div>
			<div id="custombackContainer"  style="overflow:auto"></div>
		</div>
	</body>
</html>
