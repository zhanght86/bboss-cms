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
    String fileName = "";
    int isbackup = 0;
    //水印图片的可扩充属性
    //String fontFamily = "";
    //Color Color.red ;
    //String fontSize = "";
	try{		
		//存文件
		//SmartUpload up = new SmartUpload();
		//初始化上传组件
		//up.initialize(pageContext);
		//设置上传文件大小
		//up.setMaxFileSize(5000*1024);
		//设置上传文件类型
		//String setExt=sAllowExt.replace('|',',');
		//up.setAllowedFilesList(setExt);
		//up.upload();		
        //request传递数据
        //com.jspsmart.upload.Request uploadRequest = up.getRequest();
        String backup = request.getParameter("backup");
        String srcimg = request.getParameter("srcimg"); 
        String waterStr = request.getParameter("waterStr"); 
        waterStr = waterStr==null?"":waterStr;
        String waterimage = request.getParameter("waterimage");  
        String position = request.getParameter("position");
        //加水印 或者 重新加水印
        String opt = request.getParameter("opt");
        opt = opt==null?"":opt;
        int p = 0;
        try{
            p = Integer.parseInt(position);
        }catch(Exception ee){
            p = 0;
        }   
        // Select each file
		
		fileName = waterimage;
        //被加水印图片文件名
        String srcFilename = srcimg.substring(srcimg.lastIndexOf("/"));
        //被加水印图片路径        
        String srcPath = CMSUtil.getPath(rootPath, srcFilename);        
        //备份图片文件夹
        String backupForld = impl.getNORMALIMAGE_FORDER();   
        //备份图片路径       
        String backupPath = CMSUtil.getPath(CMSUtil.getPath(rootPath , backupForld) , srcFilename+".bak");
        //水印图片路径             
        String waterPath = CMSUtil.getPath(CMSUtil.getPath(rootPath , impl.getWATERIMAGE_FORDER() ), waterimage);        
        
        if("readd".equals(opt)){//重新加水印
            //先删除现在图片   
            impl.deleteFile(srcPath);                     
            /* 恢复 备份图片 */  
            if(!"".equals(backup) && backup != null){//备份 就拷贝
                impl.copyFile(backupPath,srcPath);
                //impl.ReName(srcPath,".bak");
            }else{//不备份就移动
                //移动图片 不备份
                boolean flag = impl.moveFile(backupPath,srcPath);
            }     
            impl.logWaterImageMsg("文档管理","重新加水印",request,response);    
        }else{//新加水印
            /* 备份原始图片 */
            if(!"".equals(backup) && backup != null){
                java.io.File parentFile = new java.io.File(CMSUtil.getPath(rootPath ,backupForld));
                if(!parentFile.exists()) parentFile.mkdir();
                impl.copyFile(srcPath,backupPath);
                isbackup = 1;
            }            
            /* 记录到数据库 */
            srcPath =  srcPath.toLowerCase();
            
            StringBuffer insert = new StringBuffer()
                .append("insert into TD_CMS_ADDWATERIMAGE(url,isbackup) ")
                .append("(select '").append(srcPath).append("' as url, ")
                .append(isbackup).append(" as isbackup from dual where ")
                .append("not exists (select * from TD_CMS_ADDWATERIMAGE where ")
                .append("url='").append(srcPath).append("' and ")
                .append("isbackup=")
                .append(isbackup).append("))");
			//System.out.println(insert.toString());
            impl.execute(insert.toString()); 
            
        }
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
        impl.genWaterImage(srcPath,waterPath,waterStr,"黑体",Color.red,23,p);
        impl.logWaterImageMsg("文档管理","添加水印",request,response);
		
%>
<SCRIPT LANGUAGE="JavaScript">
	//调用父窗口的结束方法
	var fname = "<%=fileName%>";
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