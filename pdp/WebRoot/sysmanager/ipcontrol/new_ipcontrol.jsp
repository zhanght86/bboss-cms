
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<html>
    <head>
    <script type="text/javascript">
    var api = frameElement.api, W = api.opener;
function selectUser()
		{
			var url="<%=request.getContextPath()%>/purviewmanager/common/selectuser.jsp?loginName=controluser";
			W.$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.select"/>',width:1150,height:650, content:'url:'+url,currentwindow:this}); 
		}
		
		
	    function saveit(){
	       var  ip = document.all.ip.value;
	        if(ip==''){
	            W.$.dialog.alert('请填写IP范围');
	            document.all.ip.focus();
	            return false;
	        }
	        $.ajax({
		   type: "POST",
			url : "add.page",
			data :formToJson("#form1"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					var validated = $("#form1").form('validate');
			      	if (validated){
			      		blockUI();	
			      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			      	}
			      	else
			      	{
			      		return false;
			      	}
				 	
				},
			success : function(responseText){
				//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
					W.$.dialog.alert("新增记录成功",function(){	
							W.modifyQueryData();
							api.close();
							
					},api);	
					
				}else{
					alert("新增出错");
				}
			}
		  });
	    }
</script>
    <title>新增IP控制</title>
    	<%@ include file="/common/jsp/css-lhgdialog.jsp"%>		
     <script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
    </head>
    <body> 
    	<div style="height: 10px">&nbsp;</div>
    	<div class="form_box">
    		<form name="form1" id="form1" method="post">
    			<table border="0" cellpadding="0" cellspacing="0" class="table4" >
	 				<tr>   					
	   					<th width="20%">用户登录名：</th>
						<td width="80%">
						    <input type="text" name="controluser" id="controluser" class="w120"  style="width: 166px;">
						    <a href="javascript:void(0)" class="bt_2" id="seuser" onclick="selectUser();"><span>选择</span></a>
						    
						</td>
					</tr>
				    <tr><th></th><td><font color="red">不选择用户表示对系统全体用户都起作用</font></td></tr>
				    <tr>
					    <th>
							限制类型：
						</th>
					    <td>
							<select class="select" name="filtertype" id="filtertype" class="w120" style="width: 166px">
							    <option value="0">白名单</option>
							    <option value="1">黑名单</option>
							</select> 
						
						
						</td>
						</tr>
					<tr>
						<th>
							IP范围<font color="red">*</font>：
						</th>
						<td>
							<textarea rows="3" cols="8" name="ip" id="ip"></textarea>			    
						</td>
					</tr>
						<tr><th></th><td><font color="red">格式：192.168.*.*,192.168.0.25,192.0.10-192.0.20</font></td></tr>
					
					    <tr>
					    <th>
							备注：
						</th>
						<td>
							<textarea rows="3" cols="9" name="ipdesc" id="ipdesc"></textarea>	
						</td>
						</tr>
    			</table>
    			<div class="btnarea" >
					<a href="javascript:void(0)" class="bt_1" id="newbt" onclick="saveit()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
					<a href="javascript:void(0)" class="bt_2" id="advance_newbt" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
				</div>
    		</form>
    	</div>
    	
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
