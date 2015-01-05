<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：Demo 演示首页
	作者：尹标平
	版本：1.0
	日期：2012-11-01
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Demo 演示首页</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
       $(document).ready(function() {
    		 $("#wait").hide();
       	 queryList();
       	 $('#queryButton').click(function() {
           	queryData();
           });
           $('#delBatchButton').click(function() {
           	delBatch();
           });
           
           $('#addButton').click(function() {  
         	 var url="<%=request.getContextPath()%>/demo/addPre.page";
		 	 $.dialog({ id:'iframeNewId', title:'DEMO演示-添加窗口',width:740,height:560, content:'url:'+url});   			 		  
           });            
       });
       
     function queryList(){	
		$("#custombackContainer").load("queryDemos.page #customContent",function(){loadjs();suitHeight();})
	}
	
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	
	//查找
	function queryData()
	{	var name=$("#name").val();
		
		if(containSpecial(name)){
			$.dialog.alert('查询字符串含有非法字符集,请检查输入条件！');
			return;
		}
		$("#custombackContainer").load("queryDemos.page #customContent",
		{name:name},function(){loadjs()});
	

	}
	
	function modifyQueryData()
	{	
		$("#custombackContainer").load("queryDemos.page?"+$("#querystring").val()+" #customContent");
	}
	function editDemo(id){
		var url="<%=request.getContextPath()%>/demo/updatePre.page?id="+id;
		$.dialog({ title:'修改Demo信息',width:740,height:560, content:'url:'+url,lock: true}); 		
	}
	function viewDemo(id){
		var url="<%=request.getContextPath()%>/demo/viewDemo.page?id="+id;		
		$.dialog({ title:'查看Demo信息',width:740,height:560, content:'url:'+url,lock: true}); 	
	}
	   function doreset(){
	   	$("#reset").click();
	   }
	
	function delBatch(){
		var ids="";
		var cks = document.getElementsByName("CK");
		var selnums = 0;
		if(!cks)
		{
			
		}		
		else if(cks.length)
		{
			
			for(var i =0; i < cks.length; i ++ )
			{
				var ck = cks[i];
				if (ck.checked == true)
				{
					if(selnums == 0)
						ids = ck.value;
					else
					{
						ids = ids + "," + ck.value;
					}
					selnums ++;
					
				}
			}
		}
		else
		{
			if (cks.checked == true)
			{
				ids = cks.value;
				selnums ++;
			}
		}
		if(selnums <= 0)
		{
			 $.dialog.alert('请选择需要删除的记录！');
             return false;
		}
		
        $.dialog.confirm('确定要删除记录吗？', function(){
            	$.ajax({
			 	 	type: "POST",
					url : "<%=request.getContextPath()%>/demo/deleteDemo.page",
					data :{"ids":ids},
					dataType : 'json',
					async:false,
					beforeSend: function(XMLHttpRequest){
						 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
						},
					success : function(data){
						
						if(data == "success")
				 			modifyQueryData();
						else
							$.dialog.alert(data);
				 		
					}	
				 });
             },function(){
             		
             });         
	}
</script>
</head>
	<body>
		<div class="mcontent">
			<sany:menupath />
			<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<form id="queryForm" name="queryForm">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
										<tr>
											<th>名称：</th>
											<td><input id="name" name="name" type="text"
													value="" class="w120"/></td>
											
										
											<th>
												&nbsp;
											</th>
											<td>
												<a href="javascript:void" class="bt_1" id="queryButton"><span>查询</span>
												</a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span>
												</a>
												<input type="reset" id="reset" style="display:none"/>
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
				<a href="javascript:void" class="bt_small" id="addButton"><span>添加</span></a>
				<a href="javascript:void" class="bt_small" id="delBatchButton"><span>删除</span></a>				
				
				</div>
				
				<strong>Demo信息列表</strong>
				<img id="wait" src="../common/images/wait.gif" />				
			</div>
			<div id="custombackContainer">
			
			</div>
		</div>
	</body>
</html>
