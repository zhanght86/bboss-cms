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
var api = frameElement.api, W = api.opener;
   $(document).ready(function() {
	   queryAppInfo();
	      if("${msg}"!=""){
	    	$.dialog.alert("${msg}");
	    }
	       if("${errMsg}"!=""){
	    	
	    	$.dialog.alert("导入失败,原因为:"+"${errMsg}");
	    } 
   });    
   function Import(){
	   var appId = $("#appId").val();
	   if(null==appId||""==appId){
		   $.dialog.alert("请选择系统！");
		   return;
	   }
	   var filepath = document.getElementById("uploadFileName").value;
		if(filepath=="")
		{
			$.dialog.alert("请选择上传文件！");
			return false;
		}
		var reg =/.+\.([Xx][Ll][Ss][Xx])\b/;
		if(!reg.test(filepath))
		{
			$.dialog.alert("提示：\n\n请选择一个有效的文件，\n支持的格式有（xlsx）！");
			return false;
		}
		
		$("#glform").form('submit', {
		    "url": "pageListbatchInput.page?appId="+appId,
		    onSubmit:function(){			
				//显示遮罩							
				blockUI();	
		    },
		    success:function(data){	
		    	//去掉遮罩	
				unblockUI();
				if(data.indexOf("导入成功")>-1){
					W.$.dialog.alert(data,function(){	
							W.queryList();
							api.close();
					},api);													
				}else{
					W.$.dialog.alert(data,function(){},api);
				}
		    }
		});	
   }
   function CloseDlg(){
	   api.close();
   }
 //查询相应的模块
	function queryAppInfo() {
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/sanylog/pageList/getAllApp.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				$.each(data,function(i,o){
					document.getElementById("appId").options.add(new Option(data[i].appName, data[i].appId));
				});	
			}	
		 });
	}

</script>
</head>
	<body>

		<form  method="post" id="glform" name="glform" enctype="multipart/form-data">
			<div class="form_box">
					<fieldset>
					<legend>功能清单导入</legend>
					<table>
					
					<tr>
						<th>项目名称：</th>
						<td>
							<select id="appId" name="appId"  maxlength="40"><!-- class="w120" -->
								<option value="">&nbsp;&nbsp;&nbsp;无限制&nbsp;&nbsp;&nbsp;</option>
							<select>
						</td>
						<td style="height:50px;">
						<input type="file" name="uploadFileName" id="uploadFileName" size="20" style="height:25px;"/>
						</td>
					</tr>
					<tr>
						<td>
						<a href="javascript:void" class="bt_2"  onclick="Import()"><span>上传</span></a>
						<a href="javascript:void(0)" class="bt_2"  onclick="CloseDlg()"><span>关闭</span></a>
						</td>
					</tr>
							
					</table>		
					</fieldset>	

			</div>
		</form>
		<div id="custombackContainer">
		${errMsg}
		</div>
	</body>
</html>
