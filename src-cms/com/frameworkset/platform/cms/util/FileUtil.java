package com.frameworkset.platform.cms.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.driver.util.CmsLinkManager;
import com.frameworkset.platform.cms.driver.util.CmsStringUtil;


/**
 * 
 * <p>Title: FileUtil</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class FileUtil implements java.io.Serializable {

	public static File createNewFile(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return file;
		File dir = file.getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		try {
			file.createNewFile();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;

	}

	public static File createNewFileOnExist(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			file.delete();
		File dir = file.getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		try {
			file.createNewFile();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;

	}

	public static File createNewDirectory(String directorPath) {
		File dir = new File(directorPath);
		if (dir.exists())
			return dir;
		dir.mkdirs();
		return dir;
	}
	
	
	public static void copy(File sourceFile, String destinction)
	throws IOException {
//File sourceFile = new File(source);
		if (!sourceFile.exists())
			return;
		File dest_f = new File(destinction);
		if(!dest_f.exists())
			dest_f.mkdirs();
			
		
		if (sourceFile.isDirectory()) {
			java.io.File[] files = sourceFile.listFiles();
			for(int i = 0; files != null && i < files.length; i ++)
			{
				File temp = files[i];
				if(temp.isDirectory())
				{
					String fileName = temp.getName();
					copy(temp,destinction + "/" + fileName);
				}
				else
				{
					fileCopy(temp.getAbsolutePath(), destinction + "/" + temp.getName());
				}
				
			}
		} else {
			File destinctionFile = new File(destinction);
			if (!destinctionFile.exists()) {
				destinctionFile.mkdirs();
			}
			String dest = destinction + "/" + sourceFile.getName();
			fileCopy(sourceFile, dest);
		}

//File destinctionFile = new File(destinction);
//if (destinctionFile.exists())
//	;

	}

	/**
	 * 目录拷贝,用于对目录的所有文件和子目录进行递归拷贝
	 * 
	 * @param source
	 * @param destinction
	 *            必须为目录
	 * @throws IOException
	 */
	public static void copy(String source, String destinction)
			throws IOException {
		File sourceFile = new File(source);
		copy(sourceFile,  destinction);

	}

	public static void makeFile(String destinctionFile)
	{
		File f = new File(destinctionFile);
		File pf = f.getParentFile();
		if(f.exists())
			return ;
		if(!pf.exists())
		{
			pf.mkdirs();
		}
		

		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void fileCopy(String sourcefile, String destinctionFile)throws IOException 
	{
		fileCopy(new File(sourcefile), destinctionFile);
	}
	
	
	public static void fileCopy(File sourcefile, String destinctionFile)throws IOException 
	{

		FileInputStream stFileInputStream = null;
		
		FileOutputStream stFileOutputStream  = null;
		
		try
		{
			makeFile(destinctionFile);
			
			stFileInputStream = new FileInputStream(sourcefile);
		
			stFileOutputStream = new FileOutputStream(
					destinctionFile);
		
			int arraySize = 1024;
			byte buffer[] = new byte[arraySize];
			int bytesRead;
			while ((bytesRead = stFileInputStream.read(buffer)) != -1)
			{
				stFileOutputStream.write(buffer, 0, bytesRead);
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(stFileInputStream != null)
				try {
					stFileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(stFileOutputStream != null)
				try {
					stFileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	private static final int EOF = -1;

	// 获取指定路径和文件后缀名的文件名列表
	public Vector getFileNames(String pathName, String suffix)
			throws GetFileNamesException {
		Vector v = new Vector();
		String[] fileNames = null;

		File file = new File(pathName);

		fileNames = file.list();
		if (fileNames == null)
			throw new GetFileNamesException();

		for (int i = 0; i < fileNames.length; i++) {
			if (suffix.equals("*")
					|| fileNames[i].toLowerCase()
							.endsWith(suffix.toLowerCase()))
				v.addElement(fileNames[i]);
		}

		return v;
	}

	/**
	 * 删除文件目录下的所有子文件和子目录，操作一定要小心
	 * @param publishTemppath
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if(!file.exists() || file.isFile())
			return;
		if(file.isDirectory())
			deleteSubfiles(file.getAbsolutePath()) ;
		
		
		file.delete();			
		
		
	}
	
	/**
	 * 只删除目标文件
	 * @param path 文件绝对路径
	 * @author da.wei200710171007
	 */
	public static void deleteFileOnly(String path) {
		File file = new File(path);
		if(file.exists() && file.isFile()){
			file.delete();
		}	
	}

	
	/**
	 *  移动文件
	 */
	public static void moveFile(String sourceFileName, String destPath)
			throws FileMoveException {
		File src = new File(sourceFileName);
//		File dest = new File(destPath );
		if(!src.exists())
		{
			throw new FileMoveException("save file["+sourceFileName+"] to file["+destPath+"] failed:" + sourceFileName + " not exist.");
		}
//		if (dest.exists()) {
//			if (!dest.delete())
//			{
//				System.out.println("delete dest file failed:" + dest.getAbsolutePath() 
//						+ " the file is read= " + dest.canRead()
//						+ " the file is write= " + dest.canWrite());
//			}
////				throw new FileMoveException(
////						"Dest file already exists,delete fail!");
//		}
//		src = null;
//		dest = null;
		try {
			FileUtil.fileCopy(sourceFileName, destPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("save file["+sourceFileName+"] to file["+destPath+"]" + e.getMessage());
			e.printStackTrace();
		}
		
//		if (!src.renameTo(dest))
//		{
//			try {
//				System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(new File("d:/test.log"))));
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			System.out.println("src.getAbsolutePath():" + src.getAbsolutePath());
//			System.out.println("src.exist():" + src.exists());
//			System.out.println("dest.getAbsolutePath():" + dest.getAbsolutePath());
//			System.out.println("dest.exist():" + dest.exists());
////			Runtime.getRuntime().halt(1);
//			
//			throw new FileMoveException("Move file fail!");
//		}
	}
	
	public static void main(String[] args)
	{
		try {
			FileUtil.fileCopy("D:/workspace/cms20080416/src/com/frameworkset/platform/cms/util/FileUtil.java", "D:/workspace/cms20080416/src/com/frameworkset/platform/cms/util/FileUtil1.java");
			FileUtil.fileCopy("D:/workspace/cms20080416/src/com/frameworkset/platform/cms/util/FileUtil.java", "D:/workspace/cms20080416/src/com/frameworkset/platform/cms/util/FileUtil1.java");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void moveSubFiles(String sourceFileName, String destPath )
	{
		File src = new File(sourceFileName);
		File dest = new File(destPath); 
		if(!dest.exists())
			dest.mkdirs();
		if(src.isFile())
			return;
		else
		{
			File[] files = src.listFiles();
			String destFile = null;
			for(int i = 0; files != null && i < files.length; i ++)
			{
				if(files[i].isDirectory())
				{
					String temp_name = files[i].getName();
					try
					{
						moveSubFiles(files[i].getAbsolutePath(),destPath + "/" + temp_name);
					}
					catch(Exception e)
					{
						e.printStackTrace();
						continue;
					}
				}
				else
				{
					
					destFile = destPath + "/" + files[i].getName();
					try
					{
						moveFile( files[i].getAbsolutePath(),  destFile);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	

	public static List upzip(ZipInputStream zip,String destPath)throws ZipException, IOException{
		List fileNames = new ArrayList();
		ZipEntry azipfile = null;
		while((azipfile = zip.getNextEntry())!=null){
			//String name = new String(azipfile.getName().getBytes("UTF-8"),"GBK");
			String name = azipfile.getName();
			fileNames.add(name);
			if(!azipfile.isDirectory()){	
				File targetFile = new File(destPath,name);
				targetFile.getParentFile().mkdirs();
				if(targetFile.exists()){
					targetFile.delete();
				}
				targetFile.createNewFile();
				BufferedOutputStream diskfile = new BufferedOutputStream(new FileOutputStream(targetFile));
				byte[] buffer =new byte[1024];
				int read;
				while((read = zip.read(buffer))!=-1){
					diskfile.write(buffer,0,read);
				}
				diskfile.close();					
			}
		}		
		return fileNames;
	}

	/**
	 * 将zip文件解压到destPath路径下面
	 * @param sourceFileName
	 * @param destPath
	 * @return
	 * @throws ZipException
	 * @throws IOException 
	 * FileUtil.java
	 * @author: ge.tao
	 */

	public static List unzip(String sourceFileName, String destPath)
			throws ZipException, IOException {
		if(sourceFileName.endsWith(".zip")){
			ZipFile zf = new ZipFile(sourceFileName);
			Enumeration en = zf.entries();
			List v = new ArrayList();
	
			while (en.hasMoreElements()) {
				ZipEntry zipEnt = (ZipEntry) en.nextElement();
				saveEntry(destPath, zipEnt, zf);
				if(!zipEnt.isDirectory()){
					//添加zip文件的信息到列表中
					v.add(zipEnt.getName());
				}
			}
			zf.close();
			return v;
		}else{
			return null;
		}
	}

	public static void saveEntry(String destPath, ZipEntry target, ZipFile zf)
			throws ZipException, IOException {
		try {
			File file = new File(destPath + "/" + target.getName());
			if (target.isDirectory()) {
				file.mkdirs();
			} else {
				InputStream is = zf.getInputStream(target);
				BufferedInputStream bis = new BufferedInputStream(is);
				File dir = new File(file.getParent());
				dir.mkdirs();
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				int c;
				while ((c = bis.read()) != EOF) {
					bos.write((byte) c);
				}
				bos.close();
				fos.close();
			}
		} catch (ZipException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	// 创建目录
	public static  boolean createDir(String dirName) {
		File file = new File(dirName);
		if (!file.exists())
			return file.mkdir();
		return true;
	}

	//
	public static void createFile(String fileName) throws IOException {
		File file = new File(fileName);
		
		if (!file.exists()) {
			if (!file.createNewFile())
				throw new IOException("Create file fail!");
		}

	}

	public static void writeFile(String fileName, String text) throws IOException {
		FileWriter fw = new FileWriter(fileName, true);
		try {
			fw.write(text, 0, text.length());
		} catch (IOException ioe) {
			throw new IOException("Write text to " + fileName + " fail!");
		} finally {
			fw.close();
		}

	}
	
	public static void writeFile(String fileName, String text,boolean isAppend) throws IOException {
		FileWriter fw = new FileWriter(fileName, isAppend);
		try {
			fw.write(text, 0, text.length());
		} catch (IOException ioe) {
			throw new IOException("Write text to " + fileName + " fail!");
		} finally {
			fw.close();
		}

	}

	/**
	 * 删除文件目录下的所有子文件和子目录，操作一定要小心
	 * @param publishTemppath
	 */
	public static void deleteSubfiles(String publishTemppath) {
		File file = new File(publishTemppath);
		if(!file.exists() || file.isFile())
			return;
		File[] files = file.listFiles();
		for(int i = 0; files != null && i < files.length; i ++)
		{
			File temp = files[i];
			if(temp.isDirectory())
			{
				deleteSubfiles(temp.getAbsolutePath()) ;
			}
			temp.delete();			
		}
		
	}
	
	public static String getFileExtByFileName(String fileName)
	{
		if(fileName == null)
			return "";
		else
		{
			int idx = fileName.lastIndexOf(".");
			if(idx != -1)
				return fileName.substring(idx + 1);
			else
				return "";
		}
	}
	
	/**
     * Returns the normalized file path created from the given URL.<p>
     * 
     * The path part {@link URL#getPath()} is used, unescaped and 
     * normalized using {@link #normalizePath(String, char)} using {@link File#separatorChar}.<p>
     * 
     * @param url the URL to extract the path information from
     * 
     * @return the normalized file path created from the given URL using {@link File#separatorChar}
     * 
     * @see #normalizePath(URL, char)
     */
    public static String normalizePath(URL url) {

        return normalizePath(url, File.separatorChar);
    }
    
    /**
     * Returns the normalized file path created from the given URL.<p>
     * 
     * The path part {@link URL#getPath()} is used, unescaped and 
     * normalized using {@link #normalizePath(String, char)}.<p>
     * 
     * @param url the URL to extract the path information from
     * @param separatorChar the file separator char to use, for example {@link File#separatorChar}
     * 
     * @return the normalized file path created from the given URL
     */
    public static String normalizePath(URL url, char separatorChar) {

        // get the path part from the URL
        String path = new File(url.getPath()).getAbsolutePath();
        // trick to get the OS default encoding, taken from the official Java i18n FAQ
        String systemEncoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
        // decode url in order to remove spaces and escaped chars from path
        return normalizePath(CmsEncoder.decode(path, systemEncoding), separatorChar);
    }
    
    /**
     * Normalizes a file path that might contain <code>'../'</code> or <code>'./'</code> or <code>'//'</code> 
     * elements to a normal absolute path.<p>
     * 
     * Can also handle Windows like path information containing a drive letter, 
     * like <code>C:\path\..\</code>.<p>
     * 
     * @param path the path to normalize
     * @param separatorChar the file separator char to use, for example {@link File#separatorChar}
     * 
     * @return the normalized path
     */
    public static String normalizePath(String path, char separatorChar) {

        if (CmsStringUtil.isNotEmpty(path)) {
            // ensure all File separators are '/'
            path = path.replace('\\', '/');
            String drive = null;
            if ((path.length() > 1) && (path.charAt(1) == ':')) {
                // windows path like C:\home\
                drive = path.substring(0, 2);
                path = path.substring(2);
            } else if ((path.length() > 1) && (path.charAt(0) == '/') && (path.charAt(1) == '/')) {
                // windows path like \\home\ (network mapped drives)
                drive = path.substring(0, 2);
                path = path.substring(2);
            }
            if (path.charAt(0) == '/') {
                // trick to resolve all ../ inside a path
                path = '.' + path;
            }
            // resolve all '../' or './' elements in the path
            path = CmsLinkManager.getAbsoluteUri(path, "/");
            // still some '//' elements might persist
            path = CmsStringUtil.substitute(path, "//", "/");
            // re-append drive if required
            if (drive != null) {
                path = drive.concat(path);
            }
            // switch '/' back to OS dependend File separator if required
            if (separatorChar != '/') {
                path = path.replace('/', separatorChar);
            }
        }
        return path;
    }
    
    /**
     * 将字符串得值写入到文件中
     * @param filePath 文件得绝对路径
     * @param fileContent 内容
     * @throws IOException
     */
    public static void saveFile(String filePath, String fileContent,String charSet)
			throws IOException
    {
    	
		Reader reader = null;
		Writer fwriter = null;
		try
		{
			File file = new File(filePath);
			if(!file.exists())
			{
				File parent = file.getParentFile();
				if(!parent.exists())
					parent.mkdirs();
				
				file.createNewFile();
			}
//			fwriter = new FileWriter(file);
			fwriter = new OutputStreamWriter( 
					new FileOutputStream(filePath),charSet); 
			fwriter.write(fileContent, 0, fileContent.length());
//			reader = new StringReader(fileContent);
////			VelocityContext fcontext = new VelocityContext();
////			Velocity.evaluate(fcontext,fwriter,"",reader);
//			com.frameworkset.util.FileUtil.writeFile(fileName, text)
			fwriter.flush();
			
		
	    } catch (FileNotFoundException e) {
	    	//e.printStackTrace();
			throw e;
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			throw e;
		}
		finally
		{
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(fwriter != null)
				try {
					fwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
    }
    
    /**
     * 获取文件得内容
     * @param filePath 文件得物理路径
     * @return
     * @throws IOException 
     */
    public static String getFileContent(String filePath,String charSet) throws IOException {
		return com.frameworkset.util.FileUtil.getFileContent(filePath, charSet);
	}
   
    
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean hasSubDirectory(String path,String uri)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(path);
		}else{
			file = new File(path,uri);
		}
		
		if(!file.exists() || file.isFile())
			return false;
		File[] subFiles = file.listFiles(new FileFilter()
				{

					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn"))
							return false;
						if(pathname.isDirectory())
							return true;
						else
							return false;
					}
			
				});
		
		return subFiles.length > 0;
		
	}
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean hasSubDirectory(String path)
	{
		return hasSubDirectory(path,null);
	}
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean hasSubFiles(String path,String uri)
	{
		File file = new File(path,uri);
		if(!file.exists() || file.isFile())
			return false;
		File[] subFiles = file.listFiles(new FileFilter()
				{

					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn"))
							return false;
						if(!pathname.isDirectory())
							return true;
						else
							return false;
					}
			
				});
		
		return subFiles.length > 0;
		
	}
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean hasSubFiles(String path)
	{
		File file = new File(path);
		if(!file.exists() || file.isFile())
			return false;
		File[] subFiles = file.listFiles(new FileFilter()
				{

					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn"))
							return false;
						if(!pathname.isDirectory())
							return true;
						else
							return false;
					}
			
				});
		
		return subFiles.length > 0;
		
	}
	
	
	public static File[] getSubDirectories(String parentpath,String uri)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		File[] subFiles = file.listFiles(new FileFilter()
				{

					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn"))
							return false;
						if(pathname.isDirectory())
							return true;
						else
							return false;
					}
			
				});
		return subFiles;
	}

	
	public static File[] getSubDirectories(String parentpath)
	{
		return getSubDirectories(parentpath,null);
	}
	
	/**
	 * 获取某个路径下的所有文件(不包括文件夹)
	 */
	public static File[] getSubFiles(String parentpath){
		return getSubFiles(parentpath,(String)null);
	}		
	
	/**
	 * 获取某个路径下的所有文件(不包括文件夹)
	 */	
	public static File[] getSubFiles(String parentpath,String uri)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		File[] subFiles = file.listFiles(new FileFilter()
				{
					public boolean accept(File pathname) {
						if(pathname.getName().endsWith(".svn") || pathname.getName().endsWith(".db"))
							return false;
						if(pathname.isFile())
							return true;
						else
							return false;
					}
			
				});
		return subFiles;
	}
	
	public static File[] getSubFiles(String parentpath,FileFilter fileFilter){
		return getSubFiles(parentpath,null,fileFilter);
	}	
	
	public static File[] getSubFiles(String parentpath,String uri,FileFilter fileFilter)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		
		File[] files = null;
		if(fileFilter!=null){
			files = file.listFiles(fileFilter);
		}else{
			files = file.listFiles();
		}
		
		//预防传递进来的FileFilter没有把文件过滤掉
		int rLen = 0;
		for(int i=0;files!=null&&i<files.length;i++){
			if(files[i].isFile()){
				files[rLen] = files[i]; 
				rLen++;
			}
		}
		File[] r = new File[rLen];
		System.arraycopy(files,0,r,0,rLen);
		return r;
	}
	
	/**
	 * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)方法
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath)
	{
		return getSubDirectorieAndFiles(parentpath,null,null);
	}

	/**
	 * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)方法
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath,String uri)
	{
		return getSubDirectorieAndFiles(parentpath,uri,null);
	}
	
	/**
	 * 参考getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)方法
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath,FileFilter fileFilter)
	{
		return getSubDirectorieAndFiles(parentpath,null,fileFilter);
	}
	
	/**
	 * 获取某个路径下的文件
	 * @param parentpath 绝对路径 
	 * @param uri 相对与 parentpath的相对路径
	 * @param fileFilter 过滤某些文件,这个权力交给了使用该方法的用户
	 * @return
	 */
	public static File[] getSubDirectorieAndFiles(String parentpath,String uri,FileFilter fileFilter)
	{
		File file = null;
		if(uri==null || uri.trim().length()==0){
			file = new File(parentpath);
		}else{
			file = new File(parentpath,uri);
		}
		if(!file.exists() || file.isFile())
			return null;
		if(fileFilter!=null){
			return file.listFiles(fileFilter);
		}else{
			return file.listFiles();
		}
	}

	public static String getFileContent(File file,String charSet) {
		try {
			return com.frameworkset.util.FileUtil.getFileContent(file, charSet);
			
		} catch (IOException e) {
			return com.frameworkset.util.StringUtil.exceptionToString(e);
		}
//		Writer swriter = null;
//		Reader reader = null;
//		try {
//			reader = new FileReader(file);
//			swriter = new StringWriter();
//
//			int len = 0;
//			char[] buffer = new char[1024];
//			while ((len = reader.read(buffer)) > 0) {
//				swriter.write(buffer, 0, len);
//			}
//			swriter.flush();
//			return swriter.toString();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return "";
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "";
//		} finally {
//			if (reader != null)
//				try {
//					reader.close();
//				} catch (IOException e) {
//				}
//			if (swriter != null)
//				try {
//					swriter.close();
//				} catch (IOException e) {
//				}
//		}
		
	}
	
	/**
	 * 从输入流中读取字节数组
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFully(InputStream in) throws IOException {

        if (in instanceof ByteArrayInputStream) {
            // content can be read in one pass
            int size = in.available();
            byte[] bytes = new byte[size];

            // read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < size) {
                numRead = in.read(bytes, offset, size - offset);
                if (numRead >= 0) {
                    offset += numRead;
                } else {
                    break;
                }
            }
            return bytes;
        }

        // copy buffer
        byte[] xfer = new byte[2048];
        // output buffer
        ByteArrayOutputStream out = new ByteArrayOutputStream(xfer.length);

        // transfer data from input to output in xfer-sized chunks.
        for (int bytesRead = in.read(xfer, 0, xfer.length); bytesRead >= 0; bytesRead = in.read(xfer, 0, xfer.length)) {
            if (bytesRead > 0) {
                out.write(xfer, 0, bytesRead);
            }
        }
        in.close();
        out.close();
        return out.toByteArray();
    }
}
