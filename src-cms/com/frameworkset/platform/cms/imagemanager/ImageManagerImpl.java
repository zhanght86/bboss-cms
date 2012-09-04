package com.frameworkset.platform.cms.imagemanager;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;

public class ImageManagerImpl implements ImageManager{
	private CMSUtil util = new CMSUtil();
	private final static String WATERIMAGE_FORDER = "/waterImages/";
	private final static String NORMALIMAGE_FORDER = "/normalImages/";
	private final static String MIXIMAGE_FORDER = "/mixImages/";
	protected final static int NOWATER = 0;//无水印
	protected final static int WATERED_NOBACKUP = 1;//有水印 无备份
	protected final static int WATERED_BACKUP = 2;//有水印 有备份
    
	
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
	public void genWaterImage(String img,
			                  String watermark,
			                  String waterStr,
			                  String fontType,
			                  Color fontColor,
                              int fontSize,
                              int position){  
		util.genWaterImage(img,watermark,waterStr, fontType,fontColor,fontSize,position);
	}
    
    /**
     * 把原始图片拷贝到 normalImages 文件夹下 以备恢复
     * @param img 
     * ImageManagerImpl.java
     * @author: 陶格
     */
    public void backupImg(String rootPath,String img){
    	String src = rootPath + WATERIMAGE_FORDER + img;
    	String desc = rootPath + NORMALIMAGE_FORDER + img;
    	File srcFile = new File(src);
    	if(srcFile.exists()){
    		this.copyFile(src,desc);    		
    	}
    }
    
    /**
     * 关键 from-in to-out
     * @param srcFile
     * @param descFile
     * @param suffix 备份文件的后缀 
     * ImageManagerImpl.java
     * @author: 陶格
     */
    public void copyFile(String srcFile,String descFile){
    	File in = new File(srcFile);
    	File out = new File(descFile);
    	InputStream inStream = null;
	    OutputStream outStream = null;
    	if(in.exists()){
    		try{
    		    inStream = new FileInputStream(in);    		    
    		    outStream = new FileOutputStream(out);
    		    byte[] bytes = new byte[1024];
    		    int size = 0;
    		    while((size=inStream.read(bytes))!=-1){
    		    	//outStream.write(size);
    		    	outStream.write(bytes,0,size);
    		    	outStream.flush();
    		    }
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					inStream = null;
					e.printStackTrace();
				} 
    		    try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					outStream = null;
					e.printStackTrace();
				}
    		}
    		
    	}
    }
    
    /**
     * 重命名
     * @param fileName
     * @param suffix 
     * ImageManagerImpl.java
     * @author: ge.tao
     */
    public void ReName(String fileName,String suffix){
    	String newFileName = fileName.substring(0,fileName.lastIndexOf(suffix));
    	File oldF = new File(fileName);
    	File newF = new File(newFileName);
    	oldF.renameTo(newF);
    }
    
    /**
     * 移动文件
     * @param from
     * @param to 
     * ImageManagerImpl.java
     * @author: ge.tao
     */
    public boolean moveFile(String from ,String to){
    	File f = new File(from);
    	File t = new File(to);
    	return f.renameTo(t);
    	
    }
    
    /**
     * 删除文件
     * @param path 
     * ImageManagerImpl.java
     * @author: 陶格
     */
    public boolean deleteFile(String path){
    	File file = new File(path);
    	if(file.exists()){
    		file.delete();
    		return true;
    	}else{
    		return false;
    	}
    }
	
    /**
     * 
     * @param rootPath
     * @param imageType 0: 原始图片; 1: 水印图片; 2: 混合后的图片
     * @return List
     * ImageManagerImpl.java
     * @author: 陶格
     */
	public List getImagesList(String rootPath,int imageType){
		List list = new ArrayList();
		String path = "";
		if(imageType == 0){
			path = rootPath + NORMALIMAGE_FORDER;
		}else if(imageType == 1){
			path = rootPath + WATERIMAGE_FORDER;
		}else{
			path = rootPath + MIXIMAGE_FORDER;
		}
		File file = null;
		try {
			file = new File(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File[] files = {};
		if (file.isDirectory()) {
			files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File tmp = files[i];
				try {
					String absolutePath = tmp.getAbsolutePath();
					String fileName = tmp.getName();
					list.add(new String[]{fileName,absolutePath});					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return list;
	}
	
	/**
	 * 对数据库表进行操作
	 * @param sqlstr 
	 * ImageManagerImpl.java
	 * @author: 陶格
	 */
	public void execute(String sqlstr){
		DBUtil db = new DBUtil();
		try {
			//System.out.println("sql in imageManagerImpl.java-------------------"+sqlstr);
			db.execute(sqlstr);			
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	/**
	 * 图片是否被加水印
	 * @param path
	 * @return flag 
	 *         0 NOWATER          //无水印
	 *         1 WATERED_NOBACKUP //有水印 无备份
	 *         2 WATERED_BACKUP   //有水印 有备份
	 * ImageManagerImpl.java
	 * @author: 陶格
	 */
	public int isAddedWaterImage(String path){
		int flag = this.NOWATER;
		StringBuffer sqlstr =  new StringBuffer();
		sqlstr.append("select isbackup  from TD_CMS_ADDWATERIMAGE where url='")
		      .append(path).append("'");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
			if(db.size()>0) {
			    int isbackup = db.getInt(0,"isbackup");
			    if(isbackup == 1) flag = this.WATERED_BACKUP;
			    else flag = this.WATERED_NOBACKUP;
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return flag;
	}
	
	/**
	 * 判断记录是否存在
	 * @param url
	 * @param backup
	 * @return 
	 * ImageManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean isExist(String url,int backup){
		String sqlstr = "select count(*) as num from TD_CMS_ADDWATERIMAGE where "
			          + "URL='"+url+"' and ISBACKUP="+backup;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
			if(db.size()>0) {
				return db.getInt(0,0)>0;
			}else{
				return false;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 记录日志
	 * @param openModle 模块名称
	 * @param operContent 操作内容
	 * @param request
	 * @param response
	 * @throws SiteManagerException 
	 * ImageManagerImpl.java
	 * @author: ge.tao
	 */
	public void logWaterImageMsg(String openModle,String operContent ,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException 
	{
		try
		{	 
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request,response);
	        String operSource=control.getMachinedID();//request.getRemoteAddr();
	        String userName = control.getUserName();
	        String description="";
	        LogManager logManager = SecurityDatabase.getLogManager();
	        logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);    
		}
        catch(Exception e)
        {
        	e.printStackTrace();
        	throw new SiteManagerException(e.getMessage());
        }
	}

	public static String getNORMALIMAGE_FORDER() {
		return NORMALIMAGE_FORDER;
	}

	public static String getWATERIMAGE_FORDER() {
		return WATERIMAGE_FORDER;
	}
	
	

	public static String getMIXIMAGE_FORDER() {
		return MIXIMAGE_FORDER;
	}
	
	public static void main(String args[]){
		
	}
	//获取图片宽度
	//param base_path 根目录
	//param path 图片路径
	public int getImageWidth(String base_path,String path)
	{
		java.io.File file = new java.io.File(base_path+path.substring(1));
		  Image src = null;
		try {
			src = javax.imageio.ImageIO.read(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}                     //构造Image对象
		    
		    return src.getWidth(null);
	}
	
//	获取图片高度
	//param base_path 根目录
	//param path 图片路径
	public int getImageHeight(String base_path,String path)
	{
		java.io.File file = new java.io.File(base_path+path.substring(1));
		  Image src = null;
		try {
			src = javax.imageio.ImageIO.read(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}                     //构造Image对象
		    return src.getHeight(null);
	}
	
}
