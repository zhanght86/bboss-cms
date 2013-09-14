<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
  
   function saveNode(obj){
	   $.dialog.confirm("是否要保存？",function(){ 
		   var i_row = obj.parentElement.parentElement;
			var appName = i_row.cells(0).childNodes[0].value;
			var pages = i_row.cells(1).childNodes[0].value;
			$.ajax({
		 	 	type: "POST",
				url : "<%=request.getContextPath()%>/sanylog/pageList/savePages.page",
				data :{appName:appName,pages:pages},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					$.dialog.alert(data);
					queryList();
				}	
			 }); 
			
			
			
		},function(){ //取消按钮回调函数
				}) 
   }
   
   function queryList(){
	   document.getElementById("glform").action="<%=request.getContextPath()%>/sanylog/pageList/maintainPage.page";
	document.getElementById("glform").submit();
   }



</script>
</head>
	<body>
	<sany:menupath />
		<form  method="post" id="glform" name="glform" >
			<div class="form_box">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
      <pg:header>
      
      <th>项目名称</th>
      <th>上线功能数</th>
       <th>保存</th>
      </pg:header>
      <pg:list autosort="false" requestKey="datas">
   		        <tr>
   		        <td><input type="text"  id="appName" value="<pg:cell colName="appName"/>" readonly="readonly"></td>
                <td><input type="text"  id="pages"   value="<pg:cell colName="pages"/>" ></td>
                <td><a href='#'  onclick='saveNode(this)'>保存</a></td>
                </tr>
		</pg:list>
		 
		</table>	
		</div>
		</form>
		
	</body>
</html>
