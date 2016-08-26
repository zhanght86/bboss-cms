package com.frameworkset.platform.cms.upload;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fiyu.edit.RemotePic;
import net.fiyu.edit.UploadBean;

import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.imagemanager.ImageManagerImpl;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.CMSWebHelper;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;


/**
 * <p>
 * Title: FileUpload.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2012-9-12 下午3:00:38
 * @author biaoping.yin
 * @version 1.0.0
 */
public class FileUpload implements org.frameworkset.spi.InitializingBean{
	private static Logger log = Logger.getLogger(FileUpload.class);
	/**
	 * 文档附件上传
	 * @param UploadFileName
	 * @param docpath
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public String docattachupload(MultipartFile UploadFileName[],String docpath,ModelMap model)
	{
		String FileName,FileExt;
		String fileAllName = "";
		net.fiyu.edit.TimeStamp date = new net.fiyu.edit.TimeStamp();
		String approot = CMSUtil.getAppRootPath();
		// Select each file
		for (int i=0;i<UploadFileName.length;i++)
		{

			MultipartFile myFile = UploadFileName[i];
			
			FileName = (String)date.Time_Stamp();
			FileExt = CMSUtil.getFileExt(myFile.getContentType());
			if(StringUtil.isEmpty(FileExt))
			{
				String oname = myFile.getOriginalFilename();
				int idx = oname.lastIndexOf(".");
				if(idx >=0)
				{
					FileExt = oname.substring(idx);
				}
			}
			fileAllName = FileName + "." + FileExt;
			String path = "/cms/siteResource/" + docpath + "/" + fileAllName;
			
			String parentPath = approot + "/cms/siteResource/"+docpath;
			java.io.File parentFile = new java.io.File(parentPath);
			
			if(!parentFile.exists())
			{
				parentFile.mkdirs();
			}
			try {
				myFile.transferTo(new File(approot + path));
				model.addAttribute("fileAllName", fileAllName);
			} catch (IllegalStateException e) {
				log.error("",e);
				model.addAttribute("errormessage", StringUtil.exceptionToString(e));
			} catch (IOException e) {
				log.error("",e);
				model.addAttribute("errormessage", StringUtil.exceptionToString(e));
			}
			
			
		}
		return "path:docattachupload";
	}
	
	public String uploadImageFile_do(MultipartFile file[],HttpServletRequest request,HttpServletResponse response)
	{
		
		net.fiyu.edit.TimeStamp date = new net.fiyu.edit.TimeStamp();
		String fileName = "";
		String errorMsg = null;
		try{
		
			
			String fileFlag = request.getParameter("fileFlag");
			CMSManager cmsmanager = new CMSManager();
			cmsmanager.init(request,request.getSession(false),response,AccessControl.getAccessControl());
	
			String siteId =  cmsmanager.getSiteID();
	
			String pathContext = null;
			if(siteId!=null && siteId.trim().length()!=0){
				String sitepath = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
				if(sitepath!=null && sitepath.trim().length()!=0){
					pathContext = new File(sitepath,"_template").getAbsolutePath();
				}
			}
			
			if(pathContext==null || pathContext.trim().length()==0){
				errorMsg = "没有找到站点文件夹所在的路径!.";
				request.setAttribute("errorMsg", errorMsg)	;
				return "path:uploadImageFile_do";
			}
			String uri = request.getParameter("uri");
			if(fileFlag!=null && fileFlag.equals("1"))//上传首页文件，路径不需要修改，文件名称也不需要修改
			{
				
			}
			else
			{
				if(uri.equals("/"))
					uri = "uploadfiles/"+(String)date.Time_YMD().substring(0,6)+"/";
			}
			
			String coverFlag = request.getParameter("coverflag");
			File parentFolder = null;
			if(uri!=null && uri.trim().length()!=0){
				parentFolder = new File(pathContext,uri);
			}else{
				parentFolder = new File(pathContext);
			}
			
			
			int flag = 0;//校验客户端文件是否存在
			
			
			
			for(int i=0;file!=null&&i<file.length;i++){
				MultipartFile file_ = (MultipartFile)file[i];				
				flag++;				
				//建个文件用来获取名字
				String FileName= null;	
//				if(fileFlag!=null && (fileFlag.equals("1") || fileFlag.equals("flash")))//上传首页文件，路径不需要修改，文件名称也不需要修改
				{
					FileName = file_.getOriginalFilename();
				}
//				else
//				{
//					String fileBottom = CMSUtil.getFileExt(file_.getContentType());	
//					FileName=(String)date.Time_Stamp();
//					FileName = FileName+"."+ fileBottom;
//				}
				
							
				File tempFile = new File(FileName );				
				fileName = tempFile.getName() ;
				
				File f = new File(parentFolder.getAbsoluteFile(),fileName);
				if((coverFlag==null && f.exists())||("false".equals(coverFlag) && f.exists())){	
					errorMsg = "文件已经存在,请在上传之前修改好文件名!";
					request.setAttribute("errorMsg", errorMsg)	;
					return "path:uploadImageFile_do";
				}
				if(!f.getParentFile().exists())
					f.getParentFile().mkdirs();
				f.createNewFile();
				file_.transferTo(f);
				request.setAttribute("fileName", fileName);
			}
		
			if(flag <= 0)
			{
				errorMsg = "文件为空或不存在,请重新上传!";
				request.setAttribute("errorMsg", errorMsg)	;
				return "path:uploadImageFile_do";
			}
		}catch(Exception e){
			
			
			errorMsg = "上传图片发生异常,异常为:" + StringUtil.exceptionToString(e);
			request.setAttribute("errorMsg", errorMsg)	;
			return "path:uploadImageFile_do";
		}
		return "path:uploadImageFile_do";
	}
	
	
	public @ResponseBody String ewebeditupload(MultipartFile uploadfile[],HttpServletRequest request,HttpServletResponse response)
	{
		StringBuffer out = new StringBuffer();
		net.fiyu.edit.TimeStamp date = new net.fiyu.edit.TimeStamp();
		//文档保存的path
		String docpath = null;
		// 参数变量
		String sType = null, sStyleName= null;
		//' 设置变量
		String sAllowExt= null, sUploadDir= null,sBaseUrl= null,sContentPath= null;
		int  nAllowSize = 0;
		//' 接口变量
		String sFileExt = null,sSaveFileName= null,sOriginalFileName= null,sPathFileName= null,FileName= null, nFileNum= null;
		String sAction= null;
		
		docpath = request.getParameter("docpath");
		if(StringUtil.isEmpty(docpath))
			docpath = request.getParameter("cusdir");
		//设置类型
		sType=request.getParameter("type");
		if(StringUtil.isEmpty(sType))
		{
			sType="image";
		}
//		else
//			sType=request.getParameter("type").trim();
		//设置样式
		sStyleName=request.getParameter("style");
		if (StringUtil.isEmpty(sStyleName))
		{
		  sStyleName="blue";
		}
//		else
//		sStyleName=request.getParameter("style").trim();
		//设置动作
		sAction=request.getParameter("action");
		if(StringUtil.isEmpty(sAction))
		{
		  sAction="sun";
		}
//		else
//		sAction=request.getParameter("action").trim();
	
		
		try{
			UploadBean bean = _CMSWebHelper.getUploadBean(sStyleName);
			sUploadDir = bean.getSuploaddir();
	        //System.out.println(sUploadDir);
			if(sType.equalsIgnoreCase("remote"))
			{
				sAllowExt =  bean.getSremoteext();
	                        sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
				nAllowSize =  Integer.parseInt(bean.getSremotesize()) ;
	                        //System.out.println(sAllowExt+nAllowSize);
			}
	        else if(sType.equalsIgnoreCase("file"))
	        {
				sAllowExt = bean.getSfileext();
	                        sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
				nAllowSize = Integer.parseInt(bean.getSfilesize());
	                        //System.out.println(sAllowExt+nAllowSize);
	               }
	               else if(sType.equalsIgnoreCase("media"))
			{
				sAllowExt =  bean.getSmediaext();
	                        sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
				nAllowSize = Integer.parseInt(bean.getSmediasize());
	                        //System.out.println(sAllowExt+nAllowSize);
			}
	                else if(sType.equalsIgnoreCase("flash"))
	                {
				sAllowExt =  bean.getSflashext();
	                        sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
				nAllowSize = Integer.parseInt(bean.getSflashsize());
	                        //System.out.println(sAllowExt+nAllowSize);
	                }
			else
	                {
				sAllowExt =  bean.getSimageext();
	                        sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
				nAllowSize = Integer.parseInt(bean.getSimagesize());
	                        //System.out.println(sAllowExt+"///////////////////////////"+nAllowSize);
	                }
		}
		catch(Exception e){
	        }
		
		String dialogjs = request.getContextPath() + "/cms/editor/eWebeditor/dialog/dialog.js";
		//断开数据库连接
		//sAction = UCase(Trim(Request.QueryString("action"))
		if(sAction.equalsIgnoreCase("remote"))
		{    //远程自动获取
			String sContent;
				String RemoteFileurl=null;
			String Protocol,sUrl;
			int Port;
			String LocalFileurl=null;
			
			String SaveFileName=null;
			sContent=request.getParameter("eWebEditor_UploadText");
		    if(sContent==null)
			{
				sContent="sunshanfeng";
			}
			else
				sContent=new String(request.getParameter("eWebEditor_UploadText"));
			//System.out.println("替换前的html标记为:"+"\n"+sContent);
			if(sAllowExt!="")
			{
			Pattern pRemoteFileurl = Pattern.compile("((http|https|ftp|rtsp|mms):(//|\\\\){1}(([A-Za-z0-9_-])+[.]){1,}(net|com|cn|org|cc|tv|[0-9]{1,3})(\\S*/)((\\S)+[.]{1}("+sAllowExt+")))");//取得网页上URL的正则表达式
		    Matcher mRemoteFileurl = pRemoteFileurl.matcher(sContent);//对传入的字符串进行匹配
			Protocol=request.getProtocol();//取得通讯的协议
			String ProtocolA[]=Protocol.split("/");//取得协议前面的字母，如HTTP/1.1,变为"HTTP","1.1"
			sUrl = ProtocolA[0]+"://"+request.getServerName();//取得本地URL路径,如http://localhost
			//ProtocolA[]=null;
			Port=request.getServerPort();//取得端口值
			if(Port!=80)
			{//查看端口是否为80，如果不是还需要在联接上加上端口
		     sUrl=sUrl+":"+Port;
			}
			String context=request.getContextPath();
			sUrl=sUrl+context+"/"+sUploadDir;
			//System.out.println(sUrl);
			StringBuffer sb=new StringBuffer();
			boolean result=mRemoteFileurl.find();
			int i=0;

		       while(result)
					{

		             i++;
		             RemoteFileurl=mRemoteFileurl.group(0);
					 //System.out.println("需要替换的远程连接："+"\n"+RemoteFileurl);
					 sOriginalFileName=RemoteFileurl.substring(RemoteFileurl.lastIndexOf("/"));
					 Pattern pFileType=Pattern.compile("[.]{1}("+sAllowExt+")");//二次匹配取得文件的类型
					 Matcher mFileType=pFileType.matcher(RemoteFileurl);
					 while(mFileType.find())
						{
						 String SaveFileType=mFileType.group();
						 LocalFileurl=sUploadDir+(String)date.Time_Stamp()+i+SaveFileType;//文件的路径，以时间戳命名
						}

					   String LoadFile=sUploadDir.substring(0,sUploadDir.length()-1);	SaveFileName=approot + ("/")+LoadFile+"\\"+LocalFileurl.substring(LocalFileurl.lastIndexOf("/")+1);
					   //System.out.println("远程文件保存的路径和文件名："+"\n"+SaveFileName);
		                sSaveFileName=LocalFileurl.substring(LocalFileurl.lastIndexOf("/"));
		                RemotePic Down=new RemotePic();
						Down.picurl=RemoteFileurl;
						Down.savepath=SaveFileName;
		             if (Down.download())//如果上载保存成功，则更换html标记里的文件路径
						{
						 mRemoteFileurl.appendReplacement(sb,LocalFileurl);//替换路径
						}
		             result=mRemoteFileurl.find();
					}
					mRemoteFileurl.appendTail(sb);
				sContent=sb.toString();
			}
			sContent=inHTML(sContent);
			//System.out.append("替换后的html标记:"+"\n"+sContent);
			out.append("<HTML><HEAD><TITLE>远程上传</TITLE><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body>");
			out.append("<input type=hidden id=UploadText value=\"");
			out.append(sContent);
			out.append("\">");
			out.append("</body></html>");
			out.append("<script language=javascript>");
			out.append("parent.setHTML(UploadText.value);try{parent.addUploadFile('");//为什么只取一半的值？且只取复制网页插入位置之前的值？
			out.append(sOriginalFileName);
			out.append("', '");
			out.append(sSaveFileName);
			out.append("', '");
			out.append(SaveFileName);
			out.append("');} catch(e){} parent.remoteUploadOK();");
			out.append("</script>");

		  //DoRemote();
		}
		else if(sAction.equalsIgnoreCase("save"))
		{
		  //显示上传菜单
		 out.append("<HTML>");
		        out.append("<HEAD>");
		        out.append("<TITLE>文件上传</TITLE>");
		        out.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		        out.append("<style type=\"text/css\">");
		        out.append("body, a, table, div, span, td, th, input, select{font:9pt;font-family: \"宋体\", Verdana, Arial, Helvetica, sans-serif;}");
		        out.append("body {padding:0px;margin:0px}");
		        out.append("</style>");
		        out.append("<script language=\"JavaScript\" src=\"").append(dialogjs).append("\">");
		        out.append("</script>");
		        out.append("</head>");
		        out.append("<body bgcolor=menu>");
				
		        out.append("<form action=\"?action=save&type=");//注意此处为什么不用append()
		        out.append(sType);
		        out.append("&style=");
		        out.append(sStyleName); 
		        out.append("\" method=post name=myform enctype=\"multipart/form-data\">");
				
		        out.append("<input type=file name=uploadfile size=1 style=\"width:100%\" onchange=\"originalfile.value=this.value\">");		
		        out.append("<input type=hidden name=originalfile value=\"\">");
				out.append("<input type=hidden name=docpath value=\"").append(docpath == null?"":docpath).append("\">");
		        out.append("<input type=hidden name=waterimage value=\"\">");
				out.append("<input type=hidden name=position value=\"\">");
				out.append("<input type=hidden name=waterStr value=\"\">");
		        out.append("<input type=hidden name=backup value=\"\">");
				out.append("<input type=hidden name=addwater value=\"\">");

		        out.append("</form>");

		        out.append("<script language=javascript>");
		        out.append("var sAllowExt = \"");
		        out.append(sAllowExt);
		        out.append("\";");
		        out.append("// 检测上传表单\r\n");
		        out.append("function CheckUploadForm() {");
		        out.append("if (!IsExt(document.myform.uploadfile.value,sAllowExt)){");
		        out.append("parent.UploadError(\"提示：\\n\\n请选择一个有效的文件，\\n支持的格式有（\"+sAllowExt+\"）！\");");
		        out.append("return false;");
		        out.append("}");
		        out.append("return true;");
		        out.append("}");
		        out.append("// 提交事件加入检测表单\r\n");
		        out.append("var oForm = document.myform;");
		        out.append("oForm.attachEvent(\"onsubmit\", CheckUploadForm) ;");
		        out.append("if (! oForm.submitUpload) oForm.submitUpload = new Array() ;");
		        out.append("oForm.submitUpload[oForm.submitUpload.length] = CheckUploadForm ;");
		        out.append("if (! oForm.originalSubmit) {");
		        out.append("oForm.originalSubmit = oForm.submit ;");
		        out.append("oForm.submit = function() {");
		        out.append("if (this.submitUpload) {");
		        out.append("for (var i = 0 ; i < this.submitUpload.length ; i++) {");
		        out.append("this.submitUpload[i]() ;");
		        out.append("			}");
		        out.append("		}");
		        out.append("		this.originalSubmit() ;");
		        out.append("	}");
		        out.append("}");
		        out.append("// 上传表单已装入完成\r\n");
		        out.append("try {");
		        out.append("	parent.UploadLoaded();");
		        out.append("}");
		        out.append("catch(e){");
		        out.append("}");
		        out.append("</script>");
		        out.append("</body>");
		        out.append("</html>");
		try
			{
		  //存文件
		  //DoSave();
//		    SmartUpload up = new SmartUpload();
//		   //初始化上传组件
//		     up.initialize(pageContext);
//		   //设置上传文件大小
//		     up.setMaxFileSize(nAllowSize*1024);
//		   //设置上传文件类型
//		     String setExt=sAllowExt.replace('|',',');
//		     up.setAllowedFilesList(setExt);

			// Upload
//			up.upload();
			//获得上线文根路径
			String rootPath = "";
			CMSManager cmsmanager = new CMSManager();
			cmsmanager.init(request,request.getSession(false),response,AccessControl.getAccessControl());
			String siteId =  cmsmanager.getSiteID();
			String sitepath = "";
			if(siteId!=null && siteId.trim().length()!=0){
				sitepath = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
				if(sitepath!=null && sitepath.trim().length()!=0){
					rootPath = new File(sitepath,"_template").getAbsolutePath();
				}
			}

			ImageManagerImpl impl = new ImageManagerImpl();
		   
			String addwater = request.getParameter("addwater");
		    String waterimage = request.getParameter("waterimage");
			String backup = request.getParameter("backup");
		    String position = request.getParameter("position");
		    String waterStr = request.getParameter("waterStr");
			
			//
			String waterpath = impl.getWATERIMAGE_FORDER();
		    String normalpath = impl.getNORMALIMAGE_FORDER();
			//水印图片的(绝对)全路径
		    waterimage = rootPath + waterpath + waterimage;
			List list = new ArrayList();
			java.io.File parentFile = null;
			String savePath = "";
			
			
			int p = 0;
		    try{
		        p = Integer.parseInt(position);
		    }catch(Exception ee){
		        p = 0;
		    }



			// Select each file
			for (int i=0;i<uploadfile.length;i++){
			// Retreive the current file
				MultipartFile myFile = uploadfile[i];
				File normalFile = null;
					FileName=(String)date.Time_Stamp();
					sOriginalFileName=myFile.getOriginalFilename();
		            
					boolean isold = false;
					if(!"".equals(backup) && backup != null){/* 备份原始图片 */
						parentFile = new java.io.File(rootPath + normalpath);
						if(!parentFile.exists()) parentFile.mkdirs();
						savePath = normalpath + sOriginalFileName;
						normalFile = new File(rootPath + savePath);
						myFile.transferTo(normalFile);
						isold = true;
					}
					String fileExt = CMSUtil.getFileExt(myFile.getContentType());
					if(StringUtil.isEmpty(fileExt))
					{
						
						int idx = sOriginalFileName.lastIndexOf(".");
						if(idx >=0)
						{
							fileExt = sOriginalFileName.substring(idx);
						}
					}
					String path = "/cms/siteResource/"+docpath+"/"+FileName+"."+fileExt;
					String parentPath = approot + "/cms/siteResource/"+docpath;
					parentFile = new File(parentPath);
					
					if(!parentFile.exists())
					{
						parentFile.mkdirs();
					}
					if(isold)
					{
						FileUtil.fileCopy(normalFile, approot+("/")+path);
//						myFile.transferTo(new File(approot+("/")+path));
					}
					else
					{
						myFile.transferTo(new File(approot+("/")+path));
					}
					sSaveFileName=FileName+"."+fileExt;
					sPathFileName=path;
		            
					java.io.File srcImage = new java.io.File(approot+("/")+path);
					list.add(srcImage);			
					//System.ouappend(n("waterimage====" + waterimage);
		            //加水印 文字/图片
					if(!"".equals(addwater) && addwater != null){
						if(waterimage!=null){
							for(int j=0;j<list.size();j++){                        
								java.io.File files = (java.io.File)list.get(j);
								String src = files.getPath();
								impl.genWaterImage(src,waterimage,waterStr,"黑体",Color.red,23,p);
							}
						}
					}
					//System.out.append("sPathFileName"+sPathFileName);
				}
			
			
			}catch(Exception e)
			{
				out.append("<script language=javascript>");
				out.append("alert('文件上传失败！');");
				out.append("window.close();");
				out.append("</script>");
				e.printStackTrace();
				request.setAttribute("outstr", out.toString());
//				return "path:ewebeditupload";
				return out.toString();
			}
			if(sType.equals("image"))
			{
			int imageWidth = 0;
			int imageHeight = 0;
			//ImageManagerImpl imp = new ImageManagerImpl();
		    //imageWidth = imp.getImageWidth(config.getServletContext().getRealPath("/"),sPathFileName);
		    //imageHeight = imp.getImageHeight(config.getServletContext().getRealPath("/"),sPathFileName);
		    
		  	out.append("<script language=javascript>");
			out.append("parent.UploadSaved('");
			out.append(sPathFileName);
			out.append("','");
			out.append(imageWidth);
			out.append("','");
			out.append(imageHeight);
			out.append("');var obj=parent.dialogArguments.dialogArguments;if (!obj) obj=parent.dialogArguments;try{obj.addUploadFile('");
			out.append(sOriginalFileName);
			//System.out.append("sOriginalFileName:" + sOriginalFileName);
			out.append("', '");
			out.append(sSaveFileName);
			//System.out.append("sSaveFileName:" + sSaveFileName);
			out.append("', '");
			out.append(sPathFileName);
			
			out.append("');} catch(e){e.printStackTrace();}");
			out.append(";history.back();</script>");
			}
			else
			{
		   out.append("<script language=javascript>");
			out.append("parent.UploadSaved('");
			out.append(sPathFileName);
			out.append("');var obj=parent.dialogArguments.dialogArguments;if (!obj) obj=parent.dialogArguments;try{obj.addUploadFile('");
			out.append(sOriginalFileName);
			//System.out.append("sOriginalFileName:" + sOriginalFileName);
			out.append("', '");
			out.append(sSaveFileName);
			//System.out.append("sSaveFileName:" + sSaveFileName);
			out.append("', '");
			out.append(sPathFileName);
			
			out.append("');} catch(e){e.printStackTrace();}");
			out.append(";history.back();</script>");
			}
		}
		else
		{
		  //显示上传表单
		         out.append("<HTML>");
		        out.append("<HEAD>");
		        out.append("<TITLE>文件上传</TITLE>");
		        out.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		        out.append("<style type=\"text/css\">");
		        out.append("body, a, table, div, span, td, th, input, select{font:9pt;font-family: \"宋体\", Verdana, Arial, Helvetica, sans-serif;}");
		        out.append("body {padding:0px;margin:0px}");
		        out.append("</style>");
		        out.append("<script language=\"JavaScript\" src=\"").append(dialogjs).append("\">");
		        out.append("</script>");
		        out.append("</head>");
		        out.append("<body bgcolor=menu>");
		        
		        out.append("<form action=\"?action=save&type=");
		        out.append(sType);
		        out.append("&style=");
		        out.append(sStyleName);
				//out.append("&docpath=<script language=javascript>document.write('d:/aa')</script>");
		        out.append("\" method=post name=myform enctype=\"multipart/form-data\">");
		        
		        
		        out.append("<input type=file name=uploadfile size=1 style=\"width:100%\" onchange=\"originalfile.value=this.value\">");
		        out.append("<input type=hidden name=originalfile value=\"\">");
				out.append("<input type=hidden name=docpath value=\"").append(docpath == null?"":docpath).append("\">");
		        out.append("<input type=hidden name=waterimage value=\"\">");
				out.append("<input type=hidden name=position value=\"\">");
				out.append("<input type=hidden name=waterStr value=\"\">");
				out.append("<input type=hidden name=backup value=\"\">");
				out.append("<input type=hidden name=addwater value=\"\">");

		        out.append("</form>");

		        out.append("<script language=javascript>");
		        out.append("var sAllowExt = \"");
		        out.append(sAllowExt);
		        out.append("\";");
		        out.append("// 检测上传表单\r\n");
		        out.append("function CheckUploadForm() {");
		        out.append("	if (!IsExt(document.myform.uploadfile.value,sAllowExt)){");
		        out.append("		parent.UploadError(\"提示：\\n\\n请选择一个有效的文件，\\n支持的格式有（\"+sAllowExt+\"）！\");");
		        out.append("		return false;");
		        out.append("	}");
		        out.append("	return true;");
		        out.append("}");
		        out.append("// 提交事件加入检测表单\r\n");
		        out.append("var oForm = document.myform ;");
		        out.append("oForm.attachEvent(\"onsubmit\", CheckUploadForm) ;");
		        out.append("if (! oForm.submitUpload) oForm.submitUpload = new Array() ;");
		        out.append("oForm.submitUpload[oForm.submitUpload.length] = CheckUploadForm ;");
		        out.append("if (! oForm.originalSubmit) {");
		        out.append("	oForm.originalSubmit = oForm.submit ;");
		        out.append("	oForm.submit = function() {");
		        out.append("		if (this.submitUpload) {");
		        out.append("			for (var i = 0 ; i < this.submitUpload.length ; i++) {");
		        out.append("				this.submitUpload[i]() ;");
		        out.append("			}");
		        out.append("		}");
		        out.append("		this.originalSubmit() ;");
		        out.append("	}");
		        out.append("}");
		        out.append("// 上传表单已装入完成\r\n");
		        out.append("try {");
		        out.append("	parent.UploadLoaded();");
		        out.append("}");
		        out.append("catch(e){");
		        out.append("}");
		        out.append("</script>");
		        out.append("</body>");
		        out.append("</html>");
		        //out.append("123");

		}
		request.setAttribute("outstr", out.toString());
//		return "path:ewebeditupload";
		return out.toString();
	}

	CMSWebHelper _CMSWebHelper;
	String approot ;
	@Override
	public void afterPropertiesSet() throws Exception {
		approot = CMSUtil.getAppRootPath();
//		UploadWebHelper uw = new UploadWebHelper();
//		uw.filename = CMSUtil.getPath(approot,"cms/editor/eWebeditor/config/Style.xml");
//		
//		uw.getInstance();
//		bean = uw.InitPara();
		_CMSWebHelper = new CMSWebHelper();
		_CMSWebHelper.InitPara();
		
		
	}
	
	
	/*' ============================================
	' 去除Html格式，用于从数据库中取出值填入输入框时
	' 注意：value="?"这边一定要用双引号
	' ============================================*/
	public String inHTML(String str)
	{
		String sTemp;
		sTemp = str;
		if(sTemp.equals(""))
		{
			System.exit(0);
		}
		sTemp = sTemp.replaceAll("&", "&amp;");
		sTemp = sTemp.replaceAll("<", "&lt;");
		sTemp = sTemp.replaceAll(">", "&gt;");
		sTemp = sTemp.replaceAll("\"", "&quot;");
		return sTemp;
	}

	

}
