<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<%
boolean isadmin = AccessControl.getAccessControl().isAdmin();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>个人设置</title>
<%@ include file="/common/jsp/commonCssScript.jsp"%>
<tab:tabConfig />

</head>
<body style="overflow-y:hidden">
<tab:tabContainer id="jbxxtab-container" selectedTabPaneId="jbxxtab">
	
	<tab:tabPane id="jbxxtab5" tabTitle="桌面快捷项排序" lazeload="false">
		<tab:iframe  id="frame5" src="${webAppPath}/desktop/desktopsort.page?isdefault=false" frameborder="0"
										 width="100%" height="380">
		</tab:iframe>
	</tab:tabPane>
	
	<pg:true actual="<%=isadmin%>">
		<tab:tabPane id="jbxxtab10" tabTitle="缺省快捷项排序" lazeload="false">
		<tab:iframe  id="frame10" src="${webAppPath}/desktop/desktopsort.page?isdefault=true" frameborder="0"
										 width="100%" height="380">
		</tab:iframe>
	</tab:tabPane>
	</pg:true>
	<tab:tabPane id="jbxxtab6" tabTitle="窗口大小设置" lazeload="false">
		<tab:iframe  id="frame6"  src="${webAppPath}/desktop/menucustom/main.jsp" frameborder="0"
										 width="100%" height="380">
		</tab:iframe>
	</tab:tabPane>
	
	
	

</tab:tabContainer>

</body>
<script>
	
</script>
</html>