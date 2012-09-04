<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.jspsmart.upload.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>

<%  
    /**
    * 先删除水印图片,然后复制备份图片
    */
    AccessControl control = AccessControl.getInstance();    
    control.checkAccess(request, response);
    ImageManagerImpl impl = new ImageManagerImpl();
    //获得_template目录绝对路径
    String basePath = "";
    String rootPath = "";
    String filename = request.getParameter("filename");
    String url = request.getParameter("url");
    
    CMSManager cmsmanager = new CMSManager();
    cmsmanager.init(request,session,response,control);
    String siteId =  cmsmanager.getSiteID();
    String sitepath = "";
    if(siteId!=null && siteId.trim().length()!=0){
        sitepath = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
        if(sitepath!=null && sitepath.trim().length()!=0){
            rootPath = new java.io.File(sitepath,"_template").getAbsolutePath();
        }
    }
    /* replaceAll()正则表达式,被转译了两次 */
    rootPath = rootPath.replaceAll("\\\\","/");
    //获得上线文根路径 D:\workspace\CMS\creatorcms\cms
    basePath = config.getServletContext().getRealPath("/");
    basePath = basePath.replaceAll("\\\\","/");     
    try{ 
        //被加水印图片路径
        String oldPath = CMSUtil.getPath(CMSUtil.getPath(rootPath,url),filename);
        oldPath = oldPath.toLowerCase();
        oldPath = oldPath.replaceAll("//","/");
        //备份图片路径             
        String backupPath = CMSUtil.getPath(CMSUtil.getPath(rootPath,impl.getNORMALIMAGE_FORDER()),filename+".bak");
        /* 恢复原始图片 */   
        impl.deleteFile(oldPath); 
        //impl.copyFile(backupPath,oldPath);
        //impl.ReName(backupPath,".bak");
        boolean flag = impl.moveFile(backupPath,oldPath);
        /*从数据库里去掉该记录*/
        String sqlstr = "delete TD_CMS_ADDWATERIMAGE where url='"
                      + oldPath + "'"; 
        impl.execute(sqlstr);
        impl.logWaterImageMsg("文档管理","去除水印",request,response);
        
%>
<SCRIPT LANGUAGE="JavaScript">
    //调用父窗口的结束方法
    window.returnValue='True'
    window.close();
    //parent.UploadLoaded(fname);
</SCRIPT>
<%
    }catch(Exception e)
    {
%>
<SCRIPT LANGUAGE="JavaScript">
    alert("上传文件失败！");
    window.close();
</SCRIPT>
<%
    e.printStackTrace();
    }
%>