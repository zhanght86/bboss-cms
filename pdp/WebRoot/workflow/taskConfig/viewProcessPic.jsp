<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div>
 		<img id="pic"  src="<%=request.getContextPath()%>/workflow/repository/getProccessPic.page?processId=<pg:cell colName="ID_"/>"  ismap="imagemap" usemap="#imagemap"/>
 		<input type="hidden" value="<pg:cell colName='KEY_'/>" id="key"/>
 		<input type="hidden" value="" id="taskKey"/>
 		<!-- 给执行的节点加框 -->
 		<map id="imagemap" name="imagemap">
 		
 		<pg:list requestKey="aList">
 		<area style="background-color: red" alt="节点配置" shape=rect coords=<pg:cell colName='x'/>,<pg:cell  colName='y'/>,<pg:cell expression='{x}+{width}'/>,<pg:cell expression='{y}+{height}'/>  href="javaScript:showNodeinfo('<pg:cell colName="id"/>');" />
					</pg:list>
		</map>
					
 </div>
 <script type="text/javascript">
function showNodeinfo(taskKey){
	$("#taskKey").val(taskKey);
	var processKey = $('#key').val();
	var orgId = $("#orgId").val();
	//$("#nodeconfig").load("showActivitiNodeInfo.page?taskKey="+taskKey+"&processKey="+processKey+"&orgId="+orgId);
}

</script>
