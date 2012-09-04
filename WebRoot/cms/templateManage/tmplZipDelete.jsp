<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.sql.*,java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%  
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);  
    ServletContext context =session.getServletContext();
    //模板存放的路径
    String templatePath = application.getRealPath("cms/siteResource/siteTemplate");
    templatePath = templatePath.replaceAll("\\\\","/");
    TemplateManager tmplUtil = new TemplateManagerImpl();
    
    String[] ids = request.getParameterValues("id");
    for(int i=0;i<ids.length;i++){
        if("".equals(ids[i])) continue;
        tmplUtil.deleteExportTmplRecord(ids[i],templatePath);
    }
    
        
%>
<script>
    alert("删除成功");
    window.parent.location.href = window.parent.location;
</script>