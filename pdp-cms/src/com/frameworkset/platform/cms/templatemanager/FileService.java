package com.frameworkset.platform.cms.templatemanager;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.AccessException;

/**
 * dreamweaver客服端调用的模板文件上传WEBSRVICE接口
 * @author zengjianxiong
 *
 */
public class FileService implements java.io.Serializable {

	public FileService() {
//		 TODO 自动生成构造函数存根
	}
	
	/**
	 * 模板文件上传方法
	 * @param userInfo 用户信息
	 * @param docBinaryArray 模板文件流
	 * @param fileName 模板文件全路径
	 * @return 异常信息
	 * @throws SiteManagerException
	 */
	public String UploadTemplateFile (String[] userInfo,byte[] docBinaryArray,String fileName)throws SiteManagerException
	{
		  //对参数合法性进行判断
		  if(userInfo==null || userInfo.length!=2)return "非法用户";
		  String message="";
		  //进行权限验证
		  AccessControl control = AccessControl.getInstance();
		  try
		  {
			  //获取请求对象
			  MessageContext mc=MessageContext.getCurrentContext();       
		  	  HttpServletRequest request=(HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);		  
			  control.logindw(request,null,userInfo[0], userInfo[1], "0");
			  //获取WEB应用程序的主目录
			  String approot = CMSUtil.getAppRootPath();
		  	  File file=new File(approot+fileName);
			  //将文件流写入服务器站点模板目录
			  java.io.FileOutputStream outputStream=new java.io.FileOutputStream(file);
			  outputStream.write(docBinaryArray);
			  outputStream.close();
			  //注销登录
			  //control.checkAccess(request, null);
			  control.logoutdw();
		  }
		  //文件操作异常
		  catch(java.io.IOException e)
		  {
			  message=e.getMessage();
		  }
		  catch (AccessException ex) {        
			  return "远程访问用户名和口令验证失败";
		  }
		  return message;
	}
	/**
	 * 
	 * @param userInfo
	 * @param docBinaryArray
	 * @param recordId
	 * @param tableName
	 * @param primaryKeyName
	 * @param fileColumnName
	 * @return
	 * @throws Exception
	 */
	public String  saveFile(String[] userInfo,byte[] docBinaryArray,String recordId,String tableName,String primaryKeyName,String fileColumnName)throws Exception
	{		
		  String message="";
		  byte[] file=null;
		  try
		  {			  
			  PreparedDBUtil db=new PreparedDBUtil();
			  db.preparedUpdate("update "+tableName+" set "+fileColumnName+"=? where "+primaryKeyName+"='"+recordId+"'");
			  db.setBytes(1, docBinaryArray);
			  db.executePrepared();
			  //注销登录
			  //control.checkAccess(request, null);
			  //control.logoutdw();			  
		  }
		  catch (Exception ex) 
		  {        
			  return "文件保存失败";
		  }		  
		  return message;
	}
	/**
	 * 
	 * @param userInfo
	 * @param recordId
	 * @param tableName
	 * @param primaryKeyName
	 * @param columnName
	 * @return
	 * @throws Exception
	 */
	public byte[] getFile(String[] userInfo,String recordId,String tableName,String primaryKeyName,String fileColumnName)throws Exception
	{
		  //对参数合法性进行判断
		  //if(userInfo==null || userInfo.length!=2)return null;
		  String message="";
		  //进行权限验证
		  //AccessControl control = AccessControl.getInstance();
		  byte[] file=null;
		  try
		  {
			  DBUtil db=new DBUtil();
			  db.executeSelect("select "+fileColumnName+" from "+tableName+" where "+primaryKeyName+"='"+recordId+"'");
			  if(db.size()>0)file=db.getByteArray(0,fileColumnName);
			  //注销登录
			  //control.checkAccess(request, null);
			  //control.logoutdw();
		  }
		  catch (Exception ex) {        
			  return null;
		  }
		  return file;
	}
}