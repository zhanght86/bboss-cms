<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div>
 		<img id="pic" src="getProccessPic.page?processId=<pg:cell colName="ID_"/>"  ismap="imagemap" usemap="#imagemap"/>
 		<input type="hidden" value="<pg:cell colName='KEY_'/>" id="key"/>
 		<!-- 给执行的节点加框 -->
 		<map id="imagemap" name="imagemap">
 		
 		<pg:list requestKey="aList">
 		<area alt="节点配置" shape=rect coords=<pg:cell colName='x'/>,<pg:cell  colName='y'/>,<pg:cell expression='{x}+{width}'/>,<pg:cell expression='{y}+{height}'/>  href="javaScript:showNodeinfo('<pg:cell colName="id"/>');" />
					</pg:list>
		</map>
					
 </div>
 <script type="text/javascript">
function showNodeinfo(taskKey){
	var processKey = $('#key').val();
	var url="../test/showActivitiNodeInfo.page?taskKey="+taskKey+"&processKey="+processKey;
	W.$.dialog({ id:'nodeInfoIframe', title:'节点配置',width:800,height:500, content:'url:'+url,parent:api}); 
}
</script>
