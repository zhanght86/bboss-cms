<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改节点处理人</title>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>

<script language="javascript">
var api = frameElement.api, W = api.opener;

//选择处理人
function setCandidate(node_key){
	//处理人id/域账号/工号
	var candidateName = $("#candidateName"+node_key).val();
	//处理人中文名
	var candidateCNName = $("#candidateCNName"+node_key).val();
	//处理人名称+部门名称
	var realName = $("#realName"+node_key).val();
	//处理部门id
	var candidateOrgId = $("#candidateOrgId"+node_key).val();
	//处理部门名称
	var candidateOrgName = $("#candidateOrgName"+node_key).val();
	
	var url = encodeURI("<%=request.getContextPath()%>/workflowBusiness/choose/toChooseUserPage.page?processKey="+$('#processKey').val()
			+"&candidateName="+candidateName
			+"&candidateCNName="+candidateCNName
			+"&candidateOrgId="+candidateOrgId
			+"&candidateOrgName="+candidateOrgName
			+"&realName="+realName
			+"&nodekey="+node_key
			+"&callBackFunc=updateAfterChoose");
	$.dialog({ id:'nodeInfoIframe', title:'选择处理人',width:1000,height:650, content:'url:'+url}); 
	
}

function updateAfterChoose(accouts,realnames,allnames,orgids,orgnames,node_key){
	
	$("#candidateName"+node_key).val(accouts);
	$("#candidateCNName"+node_key).val(realnames);
	$("#realName"+node_key).val(allnames);
	$("#candidateOrgId"+node_key).val(orgids);
	$("#candidateOrgName"+node_key).val(orgnames);
	
	$("#realnames"+node_key).html(allnames);
}

function dosubmit(){
	
	$.dialog.confirm('确定提交数据？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/businessDemo/udpNodeAssignee.page",
			data: formToJson("#updNodeFrom"),		
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(data){
				if(data=="success"){
					api.close();
				}else{
					$.dialog.alert("修改后续节点处理人出错："+data,function(){},api);
				}
			}	
		 });
	},function(){});    
}

$(document).ready(function() {
	
});
	
</script>
</head>
	
<body>
<div class="main_contain">
	<form id="updNodeFrom" name="updNodeFrom" method="post" >
		<input type="hidden" name="proInsId" value="${processId}" />
		
		<table id="protable" border="0" cellpadding="0" cellspacing="0" class="sany_table">
			<tr>
				<th>节点名</th>
				<th>处理人员</th>
			</tr>
			
        	<pg:list requestKey="actList">
	            <tr id="protr<pg:rowid increament="1" />" >
	            	<pg:notin scope="${filterNode}" colName="actId">
			            <input type="hidden" name="actId" id="actId<pg:rowid increament="1" />" nvl="<pg:cell colName="isEdit" />"
			               value="<pg:cell colName="actId" />"  con="<pg:rowid increament="1" />" />
			             <input type="hidden" name="actName" id="actName<pg:rowid increament="1" />" value="<pg:cell colName="actName" />"/>
			             <input type="hidden" name="candidateName" id="candidateName<pg:rowid increament="1" />" value="<pg:cell colName="candidateName" />" />
			             <input type="hidden" name="candidateCNName" id="candidateCNName<pg:rowid increament="1" />" value="<pg:cell colName="candidateCNName" />" /> 
			             <input type="hidden" name="realName" id="realName<pg:rowid increament="1" />" value="<pg:cell colName="realName" />" />
			             <input type="hidden" name="candidateOrgId" id="candidateOrgId<pg:rowid increament="1" />" value="<pg:cell colName="candidateOrgId" />" /> 
			             <input type="hidden" name="candidateOrgName" id="candidateOrgName<pg:rowid increament="1" />" value="<pg:cell colName="candidateOrgName" />" />
			             <input type="hidden" name="isCopy" id="isCopy<pg:rowid increament="1" />" value="<pg:cell colName="isCopy" />"/>
		             </pg:notin>
			      <td>
				      <pg:cell colName="actName" /> 
			      </td>
			      <td <pg:in scope="${filterNode}" colName="actId">disabled</pg:in>>
				      <span id="realnames<pg:rowid increament="1" />" >
					     <pg:cell colName="realName" />
				      </span> 
				      
				      <pg:notin scope="${filterNode}" colName="actId">
					  <a href="javascript:setCandidate(<pg:rowid increament="1" />)" name="chooseUsersa"><font color="#0a70ed">[选择]</font></a>
					  </pg:notin>
			      </td>
		       </tr>
    		</pg:list>
	    </table>
	    
	    <div class="bottom_area"></div>
	    <div class="submit_operation"> 
		    <a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
			<a href="javascript:void(0)" class="bt_1" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
	    </div>
		
	</form>
</div>
</body>

</html>
