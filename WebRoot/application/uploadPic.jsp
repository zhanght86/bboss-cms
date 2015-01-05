<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
</head>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
function Import(){
	   var filepath = document.getElementById("uploadFileName").value;
		if(filepath=="")
		{
			$.dialog.alert("请选择上传文件！");
			return false;
		}
		var reg =/.+\.([Pp][Nn][Gg])\b/;
		if(!reg.test(filepath))
		{
			$.dialog.alert("提示：\n\n请选择一个有效的图片，\n支持的格式有（png）！");
			return false;
		}
		
		$("#glform").form('submit', {
		    "url": "uploadAppPic.page",
		    onSubmit:function(){			
				//显示遮罩							
				blockUI();	
		    },
		    success:function(data){	
		    	//去掉遮罩	
				unblockUI();
				if(data.indexOf("导入成功")>-1){
					W.$.dialog.alert(data,function(){	
							api.close();
					},api);													
				}else{
					$.dialog.alert(data);
				}
		    }
		});	
}
function CloseDlg(){
	   api.close();
}
</script>
<body>
	<div class="form_box">
		<form  method="post" id="glform" name="glform" enctype="multipart/form-data">
			<div class="form_box">
					<fieldset>
					<legend>系统图片上传</legend>
					<table>
					<tr><td style="height:50px;">
					<input type="file" name="uploadFileName" id="uploadFileName" size="20" style="height:25px;"/>
					</td></tr>
                    <%-- <tr style="display:none">
                    <td><input type="text" name="appInfoId" id="appInfoId" value="${param.appInfoId}"/></td>
                    </tr> --%>
                    
					<tr><td>
					<a href="javascript:void" class="bt_2"  onclick="Import()"><span>上传</span></a>
					<a href="javascript:void(0)" class="bt_2"  onclick="CloseDlg()"><span>关闭</span></a>
					</td></tr>
							
					</table>		
					</fieldset>	

			</div>
		</form>
	</div>
</body>
<script language="javascript">
	
	function doreset() {
		$("#reset").click();
	}
	
</script>