<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>

<%
    AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
    //新产生的文档ID数组
    int[][] newDocIds = null;
    newDocIds = (int[][])session.getAttribute("muti_newDocIds");
    session.removeAttribute("muti_newDocIds");   
    //当文档的状态选择为"已发布"时,发布这些新文档 
    //文档都会发布到多个站点下        
    WEBPublish publish = new WEBPublish();   
    publish.init(request,response,pageContext,accessControl);
    PublishCallBack callback = new PublishCallBackImpl();
	publish.setPublishCallBack(callback);
    if(newDocIds.length>0){
		publish.publishBatchDocument(newDocIds); 		//批量发布新文档 
    }	
    

%>