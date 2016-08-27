<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    String channelid = request.getParameter("channelid");
    String docidStr = request.getParameter("docidStr");
    String newDoc = request.getParameter("newDoc");
    
    String[] docid;             //暂时存文档id的字符串数组
    int[] intDocid = null;
    if(docidStr != null){
        docid = docidStr.split(":");
        //得到文档id的整型数组
        intDocid = new int[docid.length];
        for(int i=0;i<docid.length;i++){
            intDocid[i] = Integer.parseInt(docid[i]);
        }
    } 
    
    //文档移动处理
    DocumentManager idm = new DocumentManagerImpl();
    try{
    idm.copyDoc(request,intDocid,Integer.parseInt(channelid));
%>
<script language = "javascript">
    alert("操作成功！");
    window.close();
    <%if("true".equals(newDoc)){%>
        var str = window.dialogArguments.location.href;
        var end = str.indexOf("?");
        var strArray;
        if(end != -1)
            strArray= str.slice(0,end);
        else
            strArray = str;
        window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
    <%}%>
</script>
<%
    }catch(DocumentManagerException de){
        de.printStackTrace();
%>
        <script language="javascript">
            alert("数据库操作失败！");
            window.close();
        </script>
<%
    }
%>