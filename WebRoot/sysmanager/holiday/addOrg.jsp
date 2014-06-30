<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<style type="text/css">
.a_bg_color{
	color: white;
    cursor:hand;
	background-color: #191970
}
</style>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
   $(document).ready(function() {
	   getOrgTree()
   });    
   
   function CloseDlg(){
	   api.close();
   }
   function confirm() {	
	   var orgId = $("#orgId").val();
	   var orgName = $('#orgName').val();
	   var areaId = $('#areaId').val();
	    $.ajax({
	 	 	type: "POST",
			url : "addOrg.page",
			data :{orgId:orgId,orgName:orgName,areaId:areaId},
			dataType : '',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data == "success") {
					W.$.dialog.alert("添加成功", function() {
						 W.queryOrgList();
						 //api.close(); 
					}, api);
				} else {
					W.$.dialog.alert(data, function() {
					}, api);
				}
			}	
		 }); 
	}
   

	
function getOrgTree(){
	 
	  $("#treeDemo").load("orgtree.jsp");
}
function orgClick(orgid, orgname) {
	$("a").removeClass("a_bg_color");
		$("a[name='"+orgid+"']").addClass("a_bg_color"); 
	$("#orgId").val(orgid);
	$("#orgName").val(orgname);
}	
</script>
</head>
	<body>
			
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
	   		   <tr>
		   		   <td>部门编码</td>
		   		   <td><input type="text" id="orgId" readonly="readonly"/></td>
	   		   </tr>
	   		   <tr>
		   		   <td>部门名称</td>
		   		   <td><input type="text" id="orgName" readonly="readonly"/></td>
	   		   </tr>
	   		   
	           <tr style="display:none">
	           <td><input type="text" id="areaId"  value='${param.areaId}'/></td>
	           </tr>
			   
		</table>
		            <a href="#" class="bt_2"  onclick="confirm()"><span>保存</span></a>
					<a href="#" class="bt_2"  onclick="CloseDlg()"><span>关闭</span></a>
		<div  id="treediv" >
			<fieldset>
			  <legend>组织机构树</legend>
		      <!-- <ul id="treeDemo" class="ztree"></ul>-->
		      <div id="treeDemo" >

							</div>
			</fieldset>
		</div>
	</body>
</html>
