<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.sql.*,java.util.*,java.io.*"%>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%  
    AccessControl accesscontroler = AccessControl.getInstance();
    //accesscontroler.checkAccess(request, response);
    ImageManagerImpl imageUtil = new ImageManagerImpl();
    //String folderName = imageUtil.getNORMALIMAGE_FORDER();
	String folderName = ImageManagerImpl.getWATERIMAGE_FORDER();
    
    CMSManager cmsmanager = new CMSManager();
    cmsmanager.init(request,session,response,accesscontroler);
    String siteId =  cmsmanager.getSiteID();
    String rootPath = null;
    if(siteId!=null && siteId.trim().length()!=0){
        String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
        if(temp!=null && temp.trim().length()!=0){
            rootPath = new java.io.File(temp,"_template/" ).getAbsolutePath();
        }
    }     
    
    /*删除处理*/
    String[] id = request.getParameterValues("id"); 
    for(int i=0;i<id.length;i++){
		if(id[i].length()==0) continue;
        String wholePath = rootPath + folderName + "/"+ id[i];
		File file = new File(wholePath);
        if(file.exists()){
            file.delete();
        }
    }
    //public void logWaterImageMsg(String openModle,String operContent , request, response)
    try{
        imageUtil.logWaterImageMsg("站点管理.图片管理.水印图片管理","删除水印图片",request,response);//只保留"站点管理.图片管理.水印图片管理",weida
    }catch(Exception e){
        e.printStackTrace();
    }
%>
<html> 
    <SCRIPT LANGUAGE="JavaScript">
    <!--
        window.onload=function fresh(){
            window.parent.location.href="imageManager.jsp";
        }
    
    
    //-->
    </SCRIPT>
</html>


