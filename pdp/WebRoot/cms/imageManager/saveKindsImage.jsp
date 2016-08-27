<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.awt.Color"%>
<%@ page import="com.jspsmart.upload.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>

<%
   
    AccessControl control = AccessControl.getInstance();    
    control.checkAccess(request, response);
    
    ImageManagerImpl impl = new ImageManagerImpl();
    String waterForld = impl.getWATERIMAGE_FORDER();
    String backupForld = impl.getNORMALIMAGE_FORDER();
    //获得_template目录绝对路径
    String basePath = "";
    String rootPath = "";
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
    
    String uri = "";
    String backup = "";
    String addwater = "";
    String waterStr = "";
    String position = "";
    String waterimage = "";
    String coverFlag="";
    int isbackup = 0;
    //水印图片的可扩充属性
    //String fontFamily = "";
    //Color Color.red ;
    //String fontSize = "";
    try{        
        //存文件
        SmartUpload up = new SmartUpload();
        //初始化上传组件
        up.initialize(pageContext);
        //设置上传文件大小
        up.setMaxFileSize(5000*1024);
        up.upload();
        //request传递数据        
        com.jspsmart.upload.Request uploadRequest = up.getRequest();
        uri = uploadRequest.getParameter("uri");
        backup = uploadRequest.getParameter("backup");
        addwater = uploadRequest.getParameter("addwater");
        waterStr = uploadRequest.getParameter("waterStr"); 
        waterimage = uploadRequest.getParameter("waterimage"); 
        position = uploadRequest.getParameter("position");
        waterStr = waterStr==null?"":waterStr;        
        coverFlag = uploadRequest.getParameter("coverFlag");
        int p = 0;
        try{
            p = Integer.parseInt(position);
        }catch(Exception ee){
            p = 0;
        } 
        java.io.File parentFile = null;
        String savePath = "";
        
        String filename = "";
        
        for (int i=0;i<up.getFiles().getCount();i++){
        // Retreive the current file
            com.jspsmart.upload.File myFile = up.getFiles().getFile(i);
            if (!myFile.isMissing()) {
                 filename = myFile.getFileName();
                 filename = filename.replaceAll(" ","_") ;
                String fieldname = myFile.getFieldName();
                /* 备份原始图片 */
                if(!"".equals(backup) && backup != null && "files".equals(fieldname)){
                    parentFile = new java.io.File(CMSUtil.getPath(rootPath, backupForld));
                    if(!parentFile.exists()) parentFile.mkdirs();
                    savePath = CMSUtil.getPath(backupForld , filename+".bak");
                    myFile.saveAs(CMSUtil.getPath(rootPath , savePath));
                    isbackup = 1;
                }
                //直接保存图片 根目录就是_template 再加相对路径 uri,再加文件名; 
                savePath = CMSUtil.getPath(CMSUtil.getPath(rootPath , uri), filename);  
                parentFile = new java.io.File(savePath);
                out.println("<script language=\"javascript\">var allowUpload=true;</script>");
               	if(coverFlag==null && parentFile.exists())
               	{
                   out.println("<script language=\"javascript\">allowUpload=false;");
				   out.println("alert('当前文件在服务器中已经存在！');window.parent.divProcessing.style.display='none';");
				   out.println("</script>");
               	}   
               	else
               	{
               		myFile.saveAs(savePath);
               	}
                //加水印 文字/图片
                if(!"".equals(addwater) && addwater != null){
                    if(waterimage != null){
                        waterimage = CMSUtil.getPath(CMSUtil.getPath(rootPath ,waterForld), waterimage);                        
                        /**
                        * 生成加图片水印图片
                        * @param String img 被加水印图片路径
                        * @param String watermark 水印图片路径
                        * @param String waterStr 水印文字描述
                        * @param String fontType 水印文字字体类型
                        * @param Color fontColor 水印文字字体颜色
                        * @param int fontSize 水印文字字体大小
                        * @param int position 水印文字/图片粘贴位置
                        * ImageManagerImpl.java
                        * @author: 陶格
                        */
                        impl.genWaterImage(savePath,waterimage,waterStr,"黑体",Color.red,23,p);
                        impl.logWaterImageMsg("文档管理","添加水印",request,response); 
                        /* 记录到数据库 */
                        savePath = savePath.toLowerCase();
                        savePath = savePath.replaceAll("//","/");
                        
                        StringBuffer insert = new StringBuffer()
                            .append("insert into TD_CMS_ADDWATERIMAGE(url,isbackup) ")
                            .append("(select '").append(savePath).append("' as url, ")
                            .append(isbackup).append(" as isbackup from dual where ")
                            .append("not exists (select * from TD_CMS_ADDWATERIMAGE where ")
                            .append("url='").append(savePath).append("' and ")
                            .append("isbackup=")
                            .append(isbackup).append("))");
                        impl.execute(insert.toString());                  
                    }
                }
            }
        }
        
%>
<SCRIPT LANGUAGE="JavaScript">
    if(allowUpload)
    {
    //调用父窗口的结束方法
    window.returnValue='<%=filename%>';
    
    window.close();}
    //parent.UploadLoaded(fname);
</SCRIPT>
<%
    }catch(Exception e)
    {
        out.println("<script language=javascript>");
        out.println("alert('文件上传失败！')");
        out.println("window.close();");
        out.println("</script>");
        e.printStackTrace();
        return;
    }
%>