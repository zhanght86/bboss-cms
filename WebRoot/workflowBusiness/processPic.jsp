<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

	<div class="title_1">流程图</div>
	<pg:empty requestKey="task" >
		<div align="center"> 
			<img alt="流程图" src="${pageContext.request.contextPath}/workflowBusiness/business/getProccessPic.page?processKey=${processKey}" />
		</div>
	</pg:empty>
	<pg:notempty requestKey="task" >
		<pg:beaninfo requestKey="task">
			<div align="center">
				<img alt="流程图" src="${pageContext.request.contextPath}/workflowBusiness/business/getProccessActivePic.page?processInstId=<pg:cell colName="instanceId" />" />
			</div>
		</pg:beaninfo>
	</pg:notempty>
