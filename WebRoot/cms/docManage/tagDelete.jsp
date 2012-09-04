<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.sql.*,java.util.*,java.io.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.tag.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%  
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    TagAttachementUtil tagUtil = new TagAttachementUtil();
    String folderName = tagUtil.getRarFold();
    String rootPath = config.getServletContext().getRealPath("/");
    /*删除处理*/
    String[] id = request.getParameterValues("id");    
    for(int i=0;i<id.length;i++){
        String allPath = rootPath + folderName + "/"+ id[i];
        File file = new File(allPath);
        if(file.exists()){
            file.delete();
        }
    }
%>
<html> 
    <SCRIPT LANGUAGE="JavaScript">
    <!--
        window.onload=function fresh(){
            window.parent.location.href="tagManager.jsp";
        }
    
    
    //-->
    </SCRIPT>
</html>


