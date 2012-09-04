<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	String userId = control.getUserID();
	if(userId==null || userId.trim().length()==0){
		throw new TemplateManagerException("建立模板的用户名不能为空.");
	}

	Template tplt = new Template();
	tplt.setCreateUserId(Long.parseLong(userId));

	String siteId = request.getParameter("siteId");
	if(siteId==null || siteId.trim().length()==0){
		throw new TemplateManagerException("模板与站点相关,不能没有站点id.");
	}

	
	String name = request.getParameter("templateName");
	if(name==null || name.trim().length()==0){
		throw new TemplateManagerException("模板名字不能为空.");
	}
	tplt.setName(name);


	tplt.setDescription(request.getParameter("templateDesc"));


	String header = request.getParameter("tb_templateHead");
	if(header==null || header.trim().length()==0){
		throw new TemplateManagerException("模板头不能为空.");
	}
	tplt.setHeader(header);

	tplt.setText(request.getParameter("content2"));
	
	tplt.setType(Integer.parseInt(request.getParameter("templateType")));

	tplt.setIncreasePublishFlag(0);

	TemplateManager tm = new TemplateManagerImpl();
	tm.createTemplateofSite(tplt,Integer.parseInt(siteId));
%>
<script language="javascript">
	alert("新建模板成功!");
	parent.openWin.location.reload();
	top.close();
	

</script>
<%
} catch (TemplateManagerException e) {
	e.printStackTrace();
%>
<script language="javascript">
	alert('新建模板出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');
</script>
<%
}
%>
