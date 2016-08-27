<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.util.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(pageContext);
    DocumentManagerImpl doc = new DocumentManagerImpl();
    //频道模板修改前,是否有文档存在
    boolean hasOldDocument = true;
    String tmpleteId = request.getParameter("tmpleteId");
    String chnlId = request.getParameter("chnlId");
    tmpleteId = tmpleteId==null?"":tmpleteId;
    chnlId = chnlId==null?"":chnlId;
    if("".equals(chnlId) && "".equals(tmpleteId)){        
    }else{
        hasOldDocument = doc.channelHasDoc(chnlId);
        if(hasOldDocument) doc.updatePreviousDocInChannel(chnlId,tmpleteId);
    }
%>
<script language="javascript">
    
    <%
    if(hasOldDocument){
        out.println("alert(\"更新完毕!\");");
        
    }
    %>
    //window.close();
</script>