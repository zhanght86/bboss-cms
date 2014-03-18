<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：IP限制
作者：侯婷婷
版本：1.0
日期：2014-03-17
 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>ip限制</title>
	<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
		<script language="javascript">	
	$(document).ready(function() {
		$("#wait").hide();
		   queryList();
		$('#queryButton').click(function() {
			queryData();
		});
		$('#addButton').click(function() {
		ipcontrol_new（）；
    });            
});

	function queryList(){	
	$("#custombackContainer").load("queryListIpContorl.page?filtertype=2 #customContent",function(){loadjs();suitHeight();})
     }
     
	function queryData()
	{	
	var controluser=$("#controluser_query").val();
	var filtertype=$("#filtertype_query").val();
	if(containSpecial(controluser)){
		$.dialog.alert('查询字符串含有非法字符集,请检查输入条件！');
		return;
	}
	$("#custombackContainer").load("queryListIpContorl.page #customContent",
	{controluser:controluser,filtertype:filtertype},function(){loadjs()});
	}
	
	function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
    return ( containSpecial.test(s) );      
}
	//新增
	function ipcontrol_new(){
	    var url="<%=request.getContextPath()%>/sysmanager/ipcontrol/new_ipcontrol.jsp";
		$.dialog({title:'新增IP限制',width:450,height:320, content:'url:'+url});   	
	}
	
	
	//修改
	function ipcontrol_update(){
      var obj=document.getElementsByName("id");
		var checkValue = "";
		var n = 0;
		var win = "";
		var validateState = "";
		for(var i = 0; i < obj.length; i++){
			if(obj[i].checked){
				checkValue = obj[i].value;
				n++;
				if(n > 1){
					$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.modfiy.select.one"/>');
					return;
				}
			}
		}
		if(checkValue!=""){
			var url="<%=request.getContextPath()%>/sysmanager/ipcontrol/updatePre.page?id="+checkValue;
			$.dialog({title:'修改IP限制',width:450,height:320, content:'url:'+url});   	
			
	    }else{
	    	$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.modfiy.select.one"/>');
	    	return;
	    }
	}
	
	function ipcontrol_delete() {
	    var isSelect = false;
	    var outMsg;
		var selecet_value = "";   
		var arr = document.getElementsByName("id");
	    for (var i=0;i<arr.length;i++) {
			if (arr[i].checked){
		       	isSelect=true;
		       	if(selecet_value=="") selecet_value = selecet_value + arr[i].value; 
		       	else selecet_value = selecet_value + "," + arr[i].value;
			}
	    }
	    if (isSelect){	
	     $.dialog.confirm('<pg:message code="sany.pdp.common.operation.remove.confirm"/>', function(){
     	 $.ajax({
		 	 	type: "POST",
				url : "deletebatch.page",
				data :{"ids":selecet_value},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
			 		modifyQueryData();
			 		//alert("成功删除记录");
				}	
			 });
      },function(){
      		
      });      
		
	    }else{
	    	$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.one"/>');
	    	return false;
	    }
		return false;
	}
	
	function modifyQueryData()
{	
	$("#custombackContainer").load("queryListIpContorl.page?"+$("#querystring").val()+" #customContent");
}
	
	function cleanAll(){
		document.all.controluser.value = "";
		document.all.filtertype.value = 2;
	}
	</script>
	</head>
	<body>
		<sany:menupath menuid="ipcontrol" />
		<div class="mcontent">
			<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<form name="ipList" id="ipList" method="post">
						<table width="96%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
										<tr>
											<th>
												<pg:message code="sany.pdp.sysmanager.user.loginname" />
												：
											</th>
											<td>
												<input type="text" name="controluser" id="controluser_query" value=""
													class="w120" />
											</td>
											<th>
												限制类型：
											</th>
											<td>
												<select name="filtertype" id="filtertype_query"  class="w120" >
													<option value="2">
														所有类型
													</option>
													<option value="0">
														白名单
													</option>
													<option value="1" >
														黑名单
													</option>
												</select>
											</td>
											<td>
												<a href="javascript:void(0)" class="bt_1" id="search"
													onclick="queryData()"><span><pg:message
															code="sany.pdp.common.operation.search" />
												</span> </a>
												<a href="javascript:void(0)" class="bt_2" id="Submit22"
													onclick="cleanAll()"><span><pg:message
															code="sany.pdp.common.operation.reset" />
												</span> </a>
											</td>
										</tr>
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
						</table>
					</form>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>

			<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void(0)" class="bt_small" id="ipcontrol_new"  onclick="ipcontrol_new()"><span><pg:message code="sany.pdp.common.operation.add"/></span> </a>							    
						    <a href="javascript:void(0)" class="bt_small" id="ipcontrol_update"  onclick="ipcontrol_update()"><span><pg:message code="sany.pdp.common.operation.modfiy"/></span> </a>							    
						    <a href="javascript:void(0)" class="bt_small" id="ipcontrol_delete"  onclick="javascript:ipcontrol_delete();"><span><pg:message code="sany.pdp.common.batch.delete"/></span> </a>							    
				</div>
				<strong>IP限制列表</strong>
			</div>
        <div id="custombackContainer" style="overflow:auto">
			</div>
		</div>
	</body>
</html>	