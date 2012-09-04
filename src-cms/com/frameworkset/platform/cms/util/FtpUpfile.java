package com.frameworkset.platform.cms.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.FTPConfig;
import com.frameworkset.platform.cms.driver.distribute.DistributeDestination;


public class FtpUpfile {
	
	private FTPClient ftpClient;
	
	private String ipAddress;

	private int ipPort;

	private String userName;

	private String PassWord;

	private String localrootpath;

	private String remoterootpath;
	
	
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;   
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;
    
    
    /**
	 * 构造函数
	 * 
	 * @param ip
	 *            String 机器IP，默认端口为21
	 * @param username
	 *            String FTP用户名
	 * @param password
	 *            String FTP密码
	 * @throws Exception
	 */
	public FtpUpfile(String ip, String username, String password)
			throws Exception {
		this(ip,21,username,password);  
	}
	
	
	public FtpUpfile(String ip,int port,String username,String password){
		ftpClient = new FTPClient();
		this.ipAddress = ip;
		this.ipPort = port ;
		this.userName = username;
		this.PassWord = password;
		
		//用于测试
		localrootpath = "F:";
		remoterootpath = "/abc" ;
		
	
		
	}

	
	public FtpUpfile() {
		super();
	}
    
	/**
	 * 登录FTP服务器
	 * 
	 * @throws Exception
	 */
	public boolean login() throws Exception {
		ftpClient.connect(ipAddress, ipPort); 
		boolean flag = ftpClient.login(userName,PassWord);
		//System.out.println(flag);
		ftpClient.setFileType(FtpUpfile.BINARY_FILE_TYPE);
		return flag ;
	}
	
	/**
	 * 退出FTP服务器
	 * @throws Exception
	 */
	public void logout() throws Exception{   
        if (ftpClient.isConnected()){   
            ftpClient.disconnect();   
        }   
	}   

    /**
     * 类型
     * @param fileType
     * @throws IOException
     */	
    public void setFileType(int fileType) throws IOException {   
        ftpClient.setFileType(fileType);   
    }  
    
    /**
     * 创建目录
     * @param pathName : 绝对路径
     * @return true : 成功 false: 失败
     * @throws IOException 
     */
    public boolean createDirectory(String pathName) throws IOException {   
        return ftpClient.makeDirectory(pathName);   
    } 
    
    /**
     * 删除指定的目录
     * @param path
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path) throws IOException {   
        return ftpClient.removeDirectory(path);   
    }   
    
    /**
     * 删除指定的文件
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String pathName) throws IOException {   
        return ftpClient.deleteFile(pathName);   
    }  
    
    /**
     * 判断是否是根目录
     * @param path
     * @return
     */
    private boolean isRoot(String path){
    	return "/".equals(path) ;
    }
    
    /**
     * 删除文件及目录
     * @param path 
     * @param isAll true : 删除全部的文件及目录  false : 删除空目录
     * @return
     * @throws IOException
     */
    private boolean removeDirectory(String path, boolean isAll)   
    throws IOException {   
		if (!isAll) {   
		    return removeDirectory(path);   
		}   
		
		FTPFile[] ftpFileArr = null;
		
		if(isRoot(path)){
			ftpFileArr = ftpClient.listFiles();  
		}else{
			ftpFileArr = ftpClient.listFiles(path); 
		}
		
		
		System.out.println(ftpFileArr.length);
		
		if (ftpFileArr == null || ftpFileArr.length == 0) {   
		    return removeDirectory(path);   
		}   

		for (int i=0; i<ftpFileArr.length; i++) {   
		    String name = ftpFileArr[i].getName();   
		    if (ftpFileArr[i].isDirectory()) {
		    	if(!name.equals(".") && !name.equals("..")){
		    		if(isRoot(path)){
		    			removeDirectory(name, true);
		    		}else{
		    			removeDirectory(path + "/" + name, true);
		    		}
		    		   
		    	}
		    } else if (ftpFileArr[i].isFile()) {  
		    	if(isRoot(path)){
		    		deleteFile(name);
		    	}else{
		    		deleteFile(path + "/" + name);
		    	}
		           
		    } else if (ftpFileArr[i].isSymbolicLink()) {   
		
		    } else if (ftpFileArr[i].isUnknown()) {   
		
		    }   
		}   
			return ftpClient.removeDirectory(path);   
		}  
    
    /**
     * 删除指定下的所有文件
     * @param path
     * @return
     * @throws IOException 
     */
    public boolean deleteAll(String path) throws IOException{
    	if(isRoot(path)){
    		return this.removeDirectory(path,true);
    	}
    	return this.removeDirectory(path.substring(1),true);
    }
    
    
    /**
     * 上传目录
     * @param path
     */
	public void updir(String path) {
		String dirname;
		String dirnameftp;
		dirname = localrootpath + path;
		dirnameftp = remoterootpath + path.replace('\\', '/');
		try {
			File source = new File(dirname);
			String[] filename = source.list();
			for (int i = 0; i < filename.length; i++) {
				File filemen = new File(source.getPath(), filename[i]);
				if (filemen.isDirectory()) {
					updir(path + "\\" + filemen.getName());
				} else {
					// 上传文件
					String sourcefile = dirname + "\\" + filemen.getName();
					String desicfile = dirnameftp + "/" + filemen.getName();
					upFile(sourcefile, desicfile);
				}
			}
		} catch (Exception ei) {
				ei.printStackTrace();
		}
	}
	
	/**
	 * 上传文件
	 * @param localrootpath 本地路径
	 * @param remoterootpath 远程路径
	 * @param p 
	 */
	public void updir(String localrootpath, String remoterootpath, String p) {
		String dirname;
		String dirnameftp;
		dirname = localrootpath;
		dirnameftp = remoterootpath;
	
		try {
			try {
				buildList(dirnameftp);
			} catch (Exception e123) {
				e123 = null;
			}
			File source = new File(dirname);
			String[] filename = source.list();
			for (int i = 0; i < filename.length; i++) {
				File filemen = new File(source.getPath(), filename[i]);
				if (filemen.isDirectory()) {
					// 在ftp服务器上建目录,或上传目录下的文件.
					try {
						if(dirnameftp.equals("/")){
							createDirectory(filemen.getName()) ;
						}else{
							createDirectory(dirnameftp+ "/" + filemen.getName()) ;
						}
					} catch (Exception e12) {
						e12 = null;
						e12.printStackTrace();
					}
					
					if(remoterootpath.equals("/")){
						updir(localrootpath + "\\" + filemen.getName(),
								 "/" + filemen.getName(), filemen
										.getName());
					}else{
						updir(localrootpath + "\\" + filemen.getName(),
								remoterootpath + "/" + filemen.getName(), filemen
										.getName());
					}
					

				} else {
					// 上传文件
					String sourcefile = dirname + "\\" + filemen.getName();
					
					String	desicfile = "";
					
					if(dirnameftp.equals("/")){
						desicfile = "/" +filemen.getName();
					}else{
						desicfile = dirnameftp + "/" + filemen.getName();
					}
					
					
					upFile(sourcefile,desicfile);
				}
			}
		} catch (Exception ei) {
			ei.printStackTrace();
		}

		}
	
	/**
	 * 上传指定文件
	 * @param sourcefile 原文件的具体名称 路径 + 文件名
	 * @param desicfile  路径 +文件名
	 * @param pakg
	 */
	public void uploadFtp(String sourcefile, String desicfile, String pakg) {
		try {
			upFile(sourcefile,desicfile);
		} catch (FileNotFoundException e) {
			String message = "上传" + sourcefile + "文件失败! ";
			System.out.println(message + e);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
	
	
	/**
	 * 上传文件
	 * @param source
	 * @param destination
	 * @throws Exception
	 */
	public void upFile(String source, String destination) throws Exception {
		buildList(destination.substring(0, destination.lastIndexOf("/")));
		
		 InputStream iStream = null;   
	        try {   
	            iStream = new FileInputStream(source);   
	             ftpClient.storeFile(destination.substring(1), iStream);   
	        } catch (IOException e) {   
	        	e.printStackTrace(); 
	        } finally {   
	            if (iStream != null) {   
	                iStream.close();   
	            }   
	        }   
	}
	
	/**
	 * 构建目录
	 * @param pathList
	 * @throws Exception
	 */
	public void buildList(String pathList) throws Exception {
		
		StringTokenizer s = new StringTokenizer(pathList, "/"); // sign
		String pathName = "";
		while (s.hasMoreElements()) 
		{
			if(pathName.equals(""))
			{
				pathName = (String) s.nextElement();
			}
			else
			{
				pathName = pathName + "/" + (String) s.nextElement();
			}
		
			createDirectory(pathName);		
		}
	}
	
	 /**
	  *上传文件
	  *输入参数localfilename:\\sitea\\ml\abc.htm
	  */
	 public void upfilename(String localfilename) throws Exception{
	     String sourcefile=localrootpath+localfilename;
	      File file1=new File(sourcefile);
	      if (file1.isFile()){
	    	  String desicfile=remoterootpath+localfilename.replace('\\','/');
	    	  upFile(sourcefile,desicfile);	
	       }
	    	
	    }
	 
	 
	 /**
	     * 取得指定目录下的所有文件名，不包括目录名称
	     * @param fullPath String
	     * @return ArrayList
	     * @throws Exception
	     */
	    public ArrayList fileNames(String fullPath) throws Exception {
	    	FTPFile[] ftpFiles= null ;
	    	if(isRoot(fullPath)){
	    		ftpFiles = ftpClient.listFiles();
	    	}else{
	    		ftpFiles = ftpClient.listFiles(fullPath.substring(1));
	    	}
	    	    
	         ArrayList retList = new ArrayList();   
	         if (ftpFiles == null || ftpFiles.length == 0) {   
	             return retList;   
	         }   
	         for (int i=0; i<ftpFiles.length; i++) {   
	             if (ftpFiles[i].isFile()) {   
	                 retList.add(ftpFiles[i].getName());   
	             }   
	         }   
	         return retList;   
	 }
	    
	    /**
	     * JSP中的流上传到FTP服务器,
	     * 上传文件只能使用二进制模式，当文件存在时再次上传则会覆盖
	     * 字节数组做为文件的输入流,此方法适用于JSP中通过
	     * request输入流来直接上传文件在RequestUpload类中调用了此方法，
	     * destination路径以FTP服务器的"/"开始，带文件名
	     * @param sourceData byte[]
	     * @param destination String 路径＋文件名
	     * @throws Exception
	     */
	    public void upFile(byte[] sourceData, String destination) throws Exception {
	        buildList(destination.substring(0, destination.lastIndexOf("/")));
	        
	        ByteArrayInputStream bis = null; 
	        try {   
	            bis =  new ByteArrayInputStream(sourceData);
	             ftpClient.storeFile(destination.substring(1), bis);   
	        } catch (IOException e) {   
	        	e.printStackTrace(); 
	        } finally {   
	            if (bis != null) {   
	                bis.close();   
	            }   
	        }   
	    }   
	    
	    
	    /**
	     *从FTP文件服务器上下载文件，输出到字节数组中
	     * @param SourceFileName String
	     * @return byte[]
	     * @throws Exception
	     */
	    public byte[] downFile(String sourceFileName) throws
	            Exception {
	    	InputStream is = null ;
	    	ByteArrayOutputStream byteOut = null;
	    	byte[] return_arraybyte = null;
	    	
	    	try{
		    	is = ftpClient.retrieveFileStream(sourceFileName.substring(1));
		        byteOut =  new ByteArrayOutputStream();
		        byte[] buf = new byte[204800];
		        int bufsize = 0;
		        
		        while ((bufsize = is.read(buf, 0, buf.length)) != -1) {
		            byteOut.write(buf, 0, bufsize);
		        }
		       return_arraybyte = byteOut.toByteArray();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		byteOut.close();
	    		is.close();
	    	}
	        return return_arraybyte;
	    }
	    /**
		 * 从FTP文件服务器上下载文件SourceFileName，到本地destinationFileName 所有的文件名中都要求包括完整的路径名在内
		 * 
		 * @param SourceFileName
		 *            String
		 * @param destinationFileName
		 *            String
		 * @throws Exception
		 */
		public void downFile(String sourceFileName, String destinationFileName)
				throws Exception {
			
			byte[] temp = downFile(sourceFileName);
			FileOutputStream ftpOut = new FileOutputStream(destinationFileName);
			ftpOut.write(temp, 0, temp.length);
			ftpOut.close();
		}
	    
	    /**
		 * 删除ftp服务器上的文件 单个文件删除
		 * 
		 * @param desc_dir
		 *            ftp服务器上的相对路径
		 */
	    public void deleteRemoteFile(String desc_dir) {
	    	try {
				this.deleteFile(desc_dir.substring(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    
	    
	    /**
		 * 指定本地路径和远程路径的ftp目录发布
		 * 
		 * @author ge.tao
		 * @param context
		 *            上下文
		 */
		public void do_ftp_upload(Context context) {

			FTPConfig ftpconfig = context.getFTPConfig();
			String src_dir = context.getPublishTemppath() + "/"
					+ context.getSiteDir();
			String desc_dir = ftpconfig.getFtpFolder();
			String ftp_id = ftpconfig.getFtpip();
			String username = ftpconfig.getUsername();
			String password = ftpconfig.getPassword();
			FtpUpfile ftpUtil;
			try {
				ftpUtil = new FtpUpfile(ftp_id, username, password);
				ftpUtil.login();
				ftpUtil.updir(src_dir, desc_dir, "");
				ftpUtil.logout();
			} catch (Exception ei) {
			}

		}
	    
		public void upload(DistributeDestination distributeDestination) {

			FTPConfig ftpconfig = distributeDestination.getFtpconfig();
			String src_dir = distributeDestination.getPublishTemppath() + "/"
					+ distributeDestination.getSite().getSiteDir();
			String desc_dir = ftpconfig.getFtpFolder();
			String ftp_id = ftpconfig.getFtpip();
			String username = ftpconfig.getUsername();
			String password = ftpconfig.getPassword();
			FtpUpfile ftpUtil;
			try {
				ftpUtil = new FtpUpfile(ftp_id, username, password);
				ftpUtil.login();
				ftpUtil.updir(src_dir, desc_dir, "");
				ftpUtil.logout();
			} catch (Exception ei) {
			}
		}  
		
		public boolean testftp() throws Exception {
			boolean ret = false;
			try {

				// 登录Ftp服务器
				ret = login();
				//ret = createDirectory("test");
				logout();
				//ret = true;

			} catch (Exception ex) {

				ret = false;
				//System.out.print(ex);
				throw new Exception(ex);

			}
			return ret;
		}
	    
	public static void main(String[] args) throws Exception{
		FtpUpfile ftp = new FtpUpfile("172.16.168.192","taodd","tao");
		
		//ftp.login();
		
		System.out.println(ftp.testftp());
		//ftp.upFile("D:/aa/tao.txt","/web/tao.txt");
		//ftp.updir("aa");
//		List list = ftp.fileNames("test/image");
//		
//		for (Iterator iter = list.iterator(); iter.hasNext();) {
//			String fileNames = (String) iter.next();
//			System.out.println(fileNames);
//			
//		}
		
		//上传指定目录下的文件及文件夹
		//ftp.updir("d:/aa","/java","");
		
		//删除指定下所有文件及文件夹
		//ftp.deleteAll("/java");
		
		//ftp.logout();
		//System.out.println(ftp.testftp());
	}
    
    
    
    
}
