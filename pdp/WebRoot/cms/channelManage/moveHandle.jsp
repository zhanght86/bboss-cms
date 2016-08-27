<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    String  siteId = request.getParameter("siteId");   //这个站点id是文档所在的站点id 
    
    String  docidstr = request.getParameter("docidStr"); 
    String  channelid = request.getParameter("channelid");
    
    DocumentManager idm = new DocumentManagerImpl();
    //得到文档id数据,为字符串数组
    String[] docid;
    int[] intDocid = null;
    if(docidstr != null){
        docid = docidstr.split(":");
        //得到文档id的整型数组
        intDocid = new int[docid.length];
        for(int i=0;i<docid.length;i++){
            intDocid[i] = Integer.parseInt(docid[i]);
        }
    }

    //weida
    session.removeAttribute("docIds_moveDoc");
    session.setAttribute("docIds_moveDoc",intDocid);
    session.removeAttribute("siteId_moveDoc");
    session.setAttribute("siteId_moveDoc",siteId);
    %>
    <html><body>
    <iframe name="deleteIndexsFrame" src="DeleteIndexsHandle.jsp?action=moveDoc"></iframe>
    </body></html>
    <%
    
    //移动操作处理
    try{
                
        //初始化递规发布
        WEBPublish publish = new WEBPublish();
        //定义存储已发布文档列表容器，以便进行递归发布
        java.util.List docs = new java.util.ArrayList();
        for(int i=0;i<intDocid.length;i++){
            Document doc = idm.getPartDocInfoById(intDocid[i] + "");
            if(doc.getStatus()==5){
                docs.add(doc);
                //删除发布出来的文件
                publish.deletePublishFile(siteId,intDocid[i],request,response);
            }
        }
        
        idm.moveDoc(request,intDocid,Integer.parseInt(channelid));
        
        //递归发布文档
        if(docs.size() > 0)
        {
            int action = RecursivePublishManager.ACTIONTYPE_DELETE;
            session.removeAttribute("docs");
            session.setAttribute("docs",docs);
        %>
            <html>
            <body>
            <iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteId%>&action=<%=action%>"></iframe>
            </body>
            </html>
        <%
        }   
%>
<script language="javascript">
    alert("操作成功！");
    window.close();
    var str = window.dialogArguments.location.href;
    var end = str.indexOf("?");
    var strArray;
    if(end != -1)
        strArray= str.slice(0,end);
    else
        strArray = str;
    window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
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