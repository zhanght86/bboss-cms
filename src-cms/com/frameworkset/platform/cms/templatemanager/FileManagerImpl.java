package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.RollbackException;

import org.jfree.util.Log;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.imagemanager.ImageManagerImpl;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.FileUtil;

public class FileManagerImpl implements FileManager {
	private String templatePath;

	private String webprjPath;

	private void setSitePath(String siteId) throws TemplateManagerException {
		if (templatePath == null) {
			try {
				templatePath = new SiteManagerImpl()
						.getSiteAbsolutePath(siteId);
			} catch (SiteManagerException e) {
				e.printStackTrace();
				templatePath = null;
				throw new TemplateManagerException(e.getMessage());
			}
			if (templatePath == null || templatePath.trim().length() == 0) {
				throw new TemplateManagerException("根据站点id[" + siteId
						+ "]没有找到站点的路径.");
			}
			templatePath = new File(templatePath, "_template")
					.getAbsolutePath();
		}
	}

	private void setSiteWebProjectPath(String siteId)
			throws TemplateManagerException {
		if (webprjPath == null) {
			try {
				webprjPath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
			} catch (SiteManagerException e) {
				e.printStackTrace();
				webprjPath = null;
				throw new TemplateManagerException(e.getMessage());
			}
			if (webprjPath == null || webprjPath.trim().length() == 0) {
				throw new TemplateManagerException("根据站点id[" + siteId
						+ "]没有找到站点的路径.");
			}
			webprjPath = new File(webprjPath, "_webprj").getAbsolutePath();
		}
	}

	public List getDirectoryResource(String siteId, String uri)
			throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("没有提供站点id,无法找到站点的路径");
		}
		setSitePath(siteId);
		List fileResources = new ArrayList();
		File[] subFiles = FileUtil.getSubDirectories(templatePath, uri);
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			FileResource fr = new FileResource();
			String theURI = "";
			if (uri != null && uri.trim().length() != 0) {
				uri = uri.replace('\\', '/');
				if (!uri.endsWith("/")) {
					theURI = uri + "/";
				} else {
					theURI = uri;
				}
				if (theURI.trim().equals("/")) {
					theURI = "";
				}
			}
			fr.setUri(theURI + subFiles[i].getName());
			fr.setName(subFiles[i].getName());
			fr.setDirectory(true);
			fileResources.add(fr);
		}
		return fileResources;
	}

	public List getWebPrjDirectoryResource(String siteId, String uri)
			throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("没有提供站点id,无法找到站点的路径");
		}
		setSiteWebProjectPath(siteId);
		List fileResources = new ArrayList();
		File[] subFiles = FileUtil.getSubDirectories(webprjPath, uri);
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			FileResource fr = new FileResource();
			String theURI = "";
			if (uri != null && uri.trim().length() != 0) {
				uri = uri.replace('\\', '/');
				if (!uri.endsWith("/")) {
					theURI = uri + "/";
				} else {
					theURI = uri;
				}
				if (theURI.trim().equals("/")) {
					theURI = "";
				}
			}
			fr.setUri(theURI + subFiles[i].getName());
			fr.setName(subFiles[i].getName());
			fr.setDirectory(true);
			fileResources.add(fr);
		}
		return fileResources;
	}

	/**
	 * date 2007-10-17 获取模板包目录下的文件 目录 模板附件通过分析模板文件获取
	 * 
	 * @param siteId
	 * @param uri
	 * @param basePath
	 * @return
	 * @throws TemplateManagerException
	 *             FileManagerImpl.java
	 * @author: ge.tao
	 */
	public List getTempZipDirResource(String siteId, String basePath, String uri)
			throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("没有提供站点id,无法找到站点的路径");
		}
		setSitePath(siteId);
		List fileResources = new ArrayList();
		// 获取文件 目录, 附件重新分析htm模板文件获取
		File[] subFiles = FileUtil.getSubDirectorieAndFiles(basePath, uri);
		String theURI = "";
		if (uri != null && uri.trim().length() != 0) {
			uri = uri.replace('\\', '/');
			if (!uri.endsWith("/")) {
				theURI = uri + "/";
			} else {
				theURI = uri;
			}
			if (theURI.trim().equals("/")) {
				theURI = "";
			}
		}
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			FileResource fr = new FileResource();
			if (subFiles[i].isFile()) {
				// 过滤非htm/html的文件
				// 用正则表达式判断htm页面
				if (subFiles[i].getName().indexOf(".htm") < 0)
					continue;
			}
			fr.setUri(theURI + subFiles[i].getName());
			fr.setName(subFiles[i].getName());
			fr.setDirectory(true);
			fileResources.add(fr);
		}
		return fileResources;
	}

	/**
	 * 用于水印图片的目录读取
	 * 
	 */
	public List getDirectoryResource2(String siteId, String uri)
			throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("没有提供站点id,无法找到站点的路径");
		}
		setSitePath(siteId);
		List fileResources = new ArrayList();
		File[] subFiles = FileUtil.getSubDirectories(templatePath
				+ ImageManagerImpl.getWATERIMAGE_FORDER(), uri);
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			FileResource fr = new FileResource();
			String theURI = "";
			if (uri != null && uri.trim().length() != 0) {
				uri = uri.replace('\\', '/');
				if (!uri.endsWith("/")) {
					theURI = uri + "/";
				} else {
					theURI = uri;
				}
				if (theURI.trim().equals("/")) {
					theURI = "";
				}
			}
			fr.setUri(theURI + subFiles[i].getName());
			fr.setName(subFiles[i].getName());
			fr.setDirectory(true);
			fileResources.add(fr);
		}
		return fileResources;
	}

	/**
	 * 用于图片新闻的目录读取
	 * 
	 * @author canyang.hu
	 */
	public List getDirectoryResource3(String siteId, String uri,
			String uploadpath) throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("没有提供站点id,无法找到站点的路径");
		}
		setSitePath(siteId);
		List fileResources = new ArrayList();
		File[] subFiles = FileUtil.getSubDirectories(templatePath + uploadpath,
				uri);
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			FileResource fr = new FileResource();
			String theURI = "";
			if (uri != null && uri.trim().length() != 0) {
				uri = uri.replace('\\', '/');
				if (!uri.endsWith("/")) {
					theURI = uri + "/";
				} else {
					theURI = uri;
				}
				if (theURI.trim().equals("/")) {
					theURI = "";
				}
			}
			fr.setUri(theURI + subFiles[i].getName());
			fr.setName(subFiles[i].getName());
			fr.setDirectory(true);
			fileResources.add(fr);
		}
		return fileResources;
	}

	public List getFileResource(String siteId, String uri, FileFilter fileFilter)
			throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("没有提供站点id,无法找到站点的路径");
		}
		setSitePath(siteId);

		String theURI = "";
		if (uri != null) {
			theURI = uri.replace('\\', '/');
			if (theURI.startsWith("/")) {
				theURI = theURI.substring(1);
			}
			if (!theURI.endsWith("/")) {
				theURI += "/";
			}
			if (theURI.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
				theURI = "";
			}
		}
		TemplateManager tm = new TemplateManagerImpl();
		List fileResources = new ArrayList();
		File[] subFiles = FileUtil.getSubFiles(templatePath, uri, fileFilter);

		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			FileResource fr = new FileResource();
			fr.setUri(theURI + subFiles[i].getName());
			fr.setName(subFiles[i].getName());
			fr.setDirectory(false);
			if (fr.canbeTemplate()) {
				Template template = tm.getTemplateInfo(siteId, uri, subFiles[i]
						.getName());
				if (template != null) {
					// TODO 模板还有些其他属性没有加进来
					fr.setTemplate(true);
					fr.setTemplate(template);
					fr.setTemplateId("" + template.getTemplateId());
				} else {
					fr.setTemplate(false);
				}
			}

			PreparedDBUtil db = new PreparedDBUtil();
			String sql = "select version,checkout_user,checkout_time,b.user_name FROM td_cms_file_status a inner join TD_SM_USER b  on a.CHECKOUT_USER = b.user_id where site_id=?"
					+ " and uri=? and (checkout_user is not null or checkout_user <>'') and checkout_time is not null";

			try {
				db.preparedSelect(sql);
				db.setString(1, siteId);
				db.setString(2, theURI
						+ subFiles[i].getName());
				db.executePrepared();
				if (db.size() > 0) {
					fr.setCheckoutUser(db.getString(0, "checkout_user"));
					fr.setCheckoutTime(db.getDate(0, "checkout_time"));
					fr.setCheckoutUserName(db.getString(0, "user_name"));
					fr.setLock(true);
				} else {
					fr.setLock(false);
				}
			} catch (Exception e) {
				System.out.print("检查文件是否被锁定失败。" + e);
				throw new TemplateManagerException(e.getMessage());
			}
			fileResources.add(fr);
		}
		return fileResources;
	}

	class MyComp implements Comparator {
		boolean orderBy = true;

		MyComp(boolean ord) {
			this.orderBy = ord;
		}

		MyComp() {

		}

		public int compare(Object a, Object b) {
			String aStr = "";
			String bStr = "";

			FileResource aFileR = (FileResource) a;
			FileResource bFileR = (FileResource) b;

			if (aFileR.isDirectory() && !bFileR.isDirectory()) {
				if (this.orderBy)
					return -1;
				else
					return 1;
			}
			if (!aFileR.isDirectory() && bFileR.isDirectory()) {
				if (this.orderBy)
					return 1;
				else
					return -1;
			} else {
				aStr = aFileR.getName();
				bStr = bFileR.getName();
				return aStr.compareTo(bStr);
			}
		}
	}

	public Set getDirectoryAndFileResource(String siteId, String uri,
			FileFilter fileFilter) throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("没有提供站点id,无法找到站点的路径");
		}
		Set fileResources = null;

		TransactionManager trans = new TransactionManager();
		try
		{
			trans.begin();
			setSitePath(siteId);

			String theURI = "";
			if (uri != null) {
				theURI = uri.replace('\\', '/');
				if (theURI.startsWith("/")) {
					theURI = theURI.substring(1);
				}
				if (!theURI.endsWith("/")) {
					theURI += "/";
				}
				if (theURI.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
					theURI = "";
				}
			}

			TemplateManager tm = new TemplateManagerImpl();
			  fileResources = new TreeSet(new MyComp());
			File[] subFiles = FileUtil.getSubDirectorieAndFiles(templatePath,
					theURI, fileFilter);
			for (int i = 0; subFiles != null && i < subFiles.length; i++) {
				FileResource fr = new FileResource();
				fr.setUri(theURI + subFiles[i].getName());
				fr.setName(subFiles[i].getName());
				fr.setDirectory(subFiles[i].isDirectory());
				if (fr.canbeTemplate()) {
					Template template = tm.getTemplateInfo(siteId, uri, subFiles[i]
							.getName());
	
					if (template != null) {
						fr.setTemplate(true);
						fr.setTemplate(template);
						fr.setTemplateId("" + template.getTemplateId());
					} else {
						fr.setTemplate(false);
					}
				}
	
				PreparedDBUtil db = new PreparedDBUtil();
				String sql =  "select version,checkout_user,checkout_time,b.user_name FROM td_cms_file_status a inner join TD_SM_USER b  on a.CHECKOUT_USER = b.user_id where site_id=? and uri=? and (checkout_user is not null or checkout_user<>'') and checkout_time is not null";
				String theuri = null;
				if ("'".equalsIgnoreCase(theURI + subFiles[i].getName())) {
					theuri = "";
				} else {
					theuri = theURI
							+ subFiles[i].getName();
				}
				try {
					db.preparedSelect(sql);
					db.setString(1, siteId);
					db.setString(2, theuri);
					db.executePrepared();
					if (db.size() > 0) {
						fr.setCheckoutUser(db.getString(0, "checkout_user"));
						fr.setCheckoutTime(db.getDate(0, "checkout_time"));
						fr.setCheckoutUserName(db.getString(0, "user_name"));
						fr.setLock(true);
					} else {
						fr.setLock(false);
					}
				} catch (Exception e) {
					Log.error("检查文件是否被锁定失败。", e);
					throw new TemplateManagerException(e);
				}
				fileResources.add(fr);
			}
			trans.commit();
		} catch (RollbackException e) {
			throw new TemplateManagerException(e);
		} catch (TransactionException e1) {
			throw new TemplateManagerException(e1);
		}
		 
		finally
		{
			trans.release();
		}
		return fileResources;
	}

	/*
	 * 文件是否被锁定
	 * 
	 * @see com.frameworkset.platform.cms.templatemanager.FileManager#isLock(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean isLock(String siteId, String uri)
			throws TemplateManagerException {
		String theURI = "";
		if (uri != null) {
			theURI = uri.replace('\\', '/');
			if (theURI.startsWith("/")) {
				theURI = theURI.substring(1);
			}
			if (theURI.endsWith("/")) {
				theURI = theURI.substring(0, theURI.length() - 1);
			}
			if (theURI.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
				theURI = "";
			}
		}
		DBUtil db = new DBUtil();
		String sql = "select version,checkout_user,checkout_time from TD_CMS_FILE_STATUS where site_id='"
				+ siteId
				+ "' and uri='"
				+ theURI
				+ "' and (checkout_user is not null  or checkout_user<>'') and checkout_time is not null";

		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.print("检查文件是否被锁定失败。" + e);
			throw new TemplateManagerException(e.getMessage());
		}
	}

	/*
	 * 文件是否被某个用户锁定
	 */
	public boolean isLock(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0 || uri == null
				|| uri.trim().length() == 0) {
			throw new TemplateManagerException("站点id、文件路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("用户id不能为空！");
		}
		uri = uri.replace('\\', '/');
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length() - 1);
		}
		if (uri.startsWith("/")) {
			uri = uri.substring(1);
		}

		DBUtil db = new DBUtil();
		String sql = "select 1 from TD_CMS_FILE_STATUS where site_id='"
				+ siteId + "' and uri='" + uri + "' and checkout_user ='"
				+ userId + "' and checkout_time is not null";

		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.print("检查文件是否被锁定失败。" + e);
			throw new TemplateManagerException(e.getMessage());
		}
	}

	/**
	 * 锁定站点下的某个文件(或文件夹),如果对文件夹进行操作,会影响该文件夹下的所有文件，文件夹
	 * 
	 * @param siteId
	 * @param uri
	 * @param userId
	 * @throws TemplateManagerException
	 */
	public void checkOutFile(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("锁定文件时，站点id、文件相对路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("锁定文件时，用户id不能为空！");
		}

		TransactionManager tm = new TransactionManager();
		try
		{
			
			tm.begin();
			String sitepath = null;
			try {
				sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
			} catch (SiteManagerException e) {
				e.printStackTrace();
				throw new TemplateManagerException("根据站点id查找站点路径时发生异常！");
			}
			if (sitepath == null || sitepath.trim().length() == 0) {
				throw new TemplateManagerException("根据站点id没有找到站点的路径!");
			}
			File currFile = new File(new File(sitepath, "_template")
					.getAbsoluteFile(), uri);
			if (currFile.isDirectory()) {
				checkOutAFolder(siteId, uri, userId, currFile);
			} else if (currFile.isFile()) {
				checkoutAFile(siteId, uri, userId);
			} else {
				throw new TemplateManagerException("文件["
						+ currFile.getAbsolutePath().replace('\\', '/') + "]可能不存在!");
			}
			tm.commit();
		} catch (RollbackException e) {
			throw new TemplateManagerException(e);
		} catch (TransactionException e1) {
			throw new TemplateManagerException(e1);
		}
		finally
		{
			tm.release();
		}
			
	}

	/**
	 * 解锁站点下的某个文件(或文件夹),如果对文件夹进行操作,会影响该文件夹下的所有文件，文件夹
	 * 
	 * @param siteId
	 * @param uri
	 * @param userId
	 * @throws TemplateManagerException
	 */
	public void checkInFile(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("解锁文件时，站点id、文件相对路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("解锁文件时，用户id不能为空！");
		}
		String sitepath = null;
		try {
			sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
		} catch (SiteManagerException e) {
			e.printStackTrace();
			throw new TemplateManagerException("根据站点id查找站点路径时发生异常！");
		}
		if (sitepath == null || sitepath.trim().length() == 0) {
			throw new TemplateManagerException("根据站点id没有找到站点的路径!");
		}
		File currFile = new File(new File(sitepath, "_template")
				.getAbsoluteFile(), uri);
		if (currFile.isDirectory()) {
			checkInAFolder(siteId, uri, userId, currFile);
		} else if (currFile.isFile()) {
			checkinAFile(siteId, uri, userId);
		} else {
			throw new TemplateManagerException("文件["
					+ currFile.getAbsolutePath().replace('\\', '/')
					+ "]可能不存在,无法对它进行操作!");
		}
	}

	private void checkOutAFolder(String siteId, String uri, String userId,
			File folder) throws TemplateManagerException {
		checkoutAFile(siteId, uri, userId);

		String theURI = "";
		if (uri != null) {
			theURI = uri.replace('\\', '/');
			if (theURI.startsWith("/")) {
				theURI = theURI.substring(1);
			}
			if (!theURI.endsWith("/")) {
				theURI = theURI + "/";
			}
			if (theURI.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
				theURI = "";
			}
		}
		File[] subFiles = folder.listFiles();
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			if (subFiles[i].isFile()) {
				this.checkoutAFile(siteId, theURI + subFiles[i].getName(),
						userId);
			} else if (subFiles[i].isDirectory()) {
				this.checkOutAFolder(siteId, theURI + subFiles[i].getName(),
						userId, subFiles[i]);
			}
		}
	}

	private void checkInAFolder(String siteId, String uri, String userId,
			File folder) throws TemplateManagerException {
		this.checkinAFile(siteId, uri, userId);

		String theURI = "";
		if (uri != null) {
			theURI = uri.replace('\\', '/');
			if (theURI.startsWith("/")) {
				theURI = theURI.substring(1);
			}
			if (!theURI.endsWith("/")) {
				theURI = theURI + "/";
			}
			if (theURI.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
				theURI = "";
			}
		}
		File[] subFiles = folder.listFiles();
		for (int i = 0; subFiles != null && i < subFiles.length; i++) {
			if (subFiles[i].isFile()) {
				this.checkinAFile(siteId, theURI + subFiles[i].getName(),
						userId);
			} else if (subFiles[i].isDirectory()) {
				this.checkInAFolder(siteId, theURI + subFiles[i].getName(),
						userId, subFiles[i]);
			}
		}
	}

	/**
	 * @param siteId
	 * @param uri
	 * @param userId
	 * @throws TemplateManagerException
	 */
	private void checkoutAFile(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("站点id、文件路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("用户id不能为空！");
		}

		String theURI = uri.replace('\\', '/');
		if (theURI.startsWith("/")) {
			theURI = theURI.substring(1);
		}
		if (theURI.endsWith("/")) {
			theURI = theURI.substring(0, theURI.length() - 1);
		}

		PreparedDBUtil conn = new PreparedDBUtil();
		try {
			// (1) 检查文件是否是被其他人check out了
			String sql = "select a.* ,b.user_Name from TD_CMS_FILE_STATUS a left outer join td_sm_user b on a.CHECKOUT_USER=b.USER_ID "
					+ "where a.site_id=? and a.uri=? ";
					//+ "and a.checkout_user is not null and a.checkout_user<>?  and a.checkout_time is not null";
//			int sid = Integer.parseInt(siteId);
			conn.preparedSelect(sql);
			conn.setString(1, siteId);
			conn.setString(2, theURI);
			//conn.setString(3, userId);
			
			float version = 1.0f;
			conn.executePrepared();
			if (conn.size() > 0) {
				// String user = conn.getString(0,"checkout_user");
				
				Date time = conn.getTimestamp(0, "checkout_time");
				String checkout_user = conn.getString(0, "checkout_user");
				if(time != null && checkout_user != null && !checkout_user.equals(userId))
				{
					String username = conn.getString(0, "user_Name");
					throw new TemplateManagerException("[" + theURI + "]文件(夹)被用户["
							+ username + "]在[" + time + "]检出,无法检出!");
				}
				else
				{
					
					
					
					
					version = 0.1f + conn.getFloat(0, "version");
						
							
						
					
					sql = "update TD_CMS_FILE_STATUS set checkout_user=?, checkout_time =? where site_id=? and  uri=? ";
					conn.preparedUpdate(sql);
					conn.setString(1, userId);
					conn.setTimestamp(2, new Timestamp(new Date().getTime()));
					conn.setString(3, siteId);
					conn.setString(4, theURI);
					conn.executePrepared();
				}
			}
			else
			{
				sql = "insert into TD_CMS_FILE_STATUS(site_id,uri,version,checkout_user,checkout_time)  values(?,?,1.0,?,?)";
				conn.preparedInsert(sql);
				conn.setString(1, siteId);
				
				conn.setString(2, theURI);
				conn.setString(3, userId);
				conn.setTimestamp(4, new Timestamp(new Date().getTime()));
				conn.executePrepared();
			}
//			sql = "merge into TD_CMS_FILE_STATUS a "
//					+ "using (select '"
//					+ siteId
//					+ "' site_id, '"
//					+ theURI
//					+ "' uri from dual) b "
//					+ "on (a.site_id = b.site_id and a.uri = b.uri) "
//					+ "when matched then update set a.checkout_user='"
//					+ userId
//					+ "',a.checkout_time =sysdate "
//					+ "when not matched then insert "
//					+ "(a.site_id,a.uri,a.version,a.checkout_user,a.checkout_time) "
//					+ "values('" + siteId + "','" + theURI + "','1.0','"
//					+ userId + "',sysdate)";
//
//			conn.execute(sql);

			this.logFileChange(siteId, theURI, userId, "", "检出",version);
		} catch (SQLException e) {
			System.out.print("检出文件时发生异常!");
			e.printStackTrace();
			throw new TemplateManagerException("检出文件时发生异常!" + e.getMessage());
		}
	}

	private void checkinAFile(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("检入文件时，站点id、文件路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("检入文件时，用户id不能为空！");
		}

		String theURI = uri.replace('\\', '/');
		if (theURI.startsWith("/")) {
			theURI = theURI.substring(1);
		}
		if (theURI.endsWith("/")) {
			theURI = theURI.substring(0, theURI.length() - 1);
		}

		TransactionManager tm = new TransactionManager();
		PreparedDBUtil conn = new PreparedDBUtil();
		try {
			tm.begin();
			// (1) 检查文件是否是被其他人check out了,如果被别人checkout 无法check in
			String sql = "select * from TD_CMS_FILE_STATUS where site_id=? and uri=? and (checkout_user is not null  or checkout_user<>'') and checkout_user<>?  and checkout_time is not null";
			conn.preparedSelect(sql);
			conn.setString(1, siteId);
			conn.setString(2, theURI);
			conn.setString(3, userId);
			conn.executePrepared();
			float version = 1.0f;
			if (conn.size() > 0) {
				version = 0.1f + conn.getFloat(0, "version");
				String user = conn.getString(0, "checkout_user");
				Date time = conn.getTimestamp(0, "checkout_time");
				throw new TemplateManagerException("文件(夹)[" + theURI + "]被用户["
						+ user + "]在[" + time + "]检出,无法检入该文件!");
			}

			sql = "update TD_CMS_FILE_STATUS set checkout_user = '',checkout_time = ? ,version = ? "
					+ " where site_id = ? and uri=? and checkout_user=?";
			conn.preparedUpdate(sql);
			conn.setNull(1, Types.TIMESTAMP);
			conn.setFloat(2, version);
			conn.setString(3, siteId);
			conn.setString(4, theURI);
			conn.setString(5, userId);
			conn.executePrepared();
			this.logFileChange(siteId, theURI, userId, "", "检入",version);
			tm.commit();
		} catch (Exception e) {
			System.out.print("检入文件时发生异常!");
			e.printStackTrace();
			throw new TemplateManagerException("检入文件时发生异常!" + e.getMessage(),e);
		}
		finally
		{
			tm.release();
		}
	}

	/**
	 * 改变文件的历史记录(具体动作就是:如果没有该文件的日志,插入该文件的日志;如果有,根据需要来决定是否在最高版本的基础上修改版本号.同时置当前文件状态为最新版本号)
	 * 
	 * @param siteId
	 *            站点id
	 * @param uri
	 *            文件(或文件夹的相对路径)
	 * @param userId
	 *            用户id
	 * @param bakfileName
	 *            备份文件名称
	 * @param changeRemarks
	 *            改变的一些备注信息
	 * @param changeVersion
	 *            是否改变版本号(有些情况比如说,只是checkout checkin这些动作不需要改变文件内容,也不需要改变版本)
	 */
	public void logFileChange(String siteId, String uri, String userId,
			String bakfileName, String changeRemarks,float version )
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("站点id、文件路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("用户id不能为空！");
		}
		if (bakfileName == null)
			bakfileName = "";
		if (changeRemarks == null)
			changeRemarks = "";

		String theURI = uri.replace('\\', '/');
		if (theURI.startsWith("/")) {
			theURI = theURI.substring(1);
		}
		if (theURI.endsWith("/")) {
			theURI = theURI.substring(0, theURI.length() - 1);
		}
		PreparedDBUtil conn = new PreparedDBUtil();
		try {
			
					String sql = "INSERT INTO td_cms_file_change_log "
							+ "(site_id,uri,version,user_id,change_time,bak_file_name,change_remark)"
							+ "values " + "(?,?,?,?,?,?,?)";
				
					conn.preparedUpdate(sql);
					conn.setString(1, siteId);
					conn.setString(2, theURI);
					conn.setFloat(3, version );
					conn.setString(4, userId);
					conn.setTimestamp(5, new Timestamp(new Date().getTime()));
					conn.setString(6, bakfileName);
					conn.setString(7, changeRemarks);
					
					conn.executePrepared();
			 
			 

		} catch (SQLException e) {
			System.out.println("更新文件历史记录、文件当前状态发生异常!");
			e.printStackTrace();
			throw new TemplateManagerException("更新文件历史记录、文件当前状态发生异常!"
					+ e.getMessage());
		}

		 
	}

	/**
	 * 删除站点下uri对应的文件或文件夹,如果是文件夹的话,会删除所有子文件.在删除之前,会判断这个文件是否被该用户锁定,只有被该用户锁定才能删除
	 * 
	 * @param siteId
	 * @param uri
	 * @param userId
	 * @throws TemplateManagerException
	 */
	public void deleteFiles(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("删除站点下文件时,站点id、文件相对路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("删除站点下文件时,用户id不能为空！");
		}

		String sitepath = null;
		try {
			sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
		} catch (SiteManagerException e) {
			e.printStackTrace();
			throw new TemplateManagerException("根据站点id查找站点路径时发生异常！");
		}
		if (sitepath == null || sitepath.trim().length() == 0) {
			throw new TemplateManagerException("根据站点id没有找到站点的路径!");
		}
		File currFile = new File(new File(sitepath, "_template")
				.getAbsoluteFile(), uri);
		if (currFile.isDirectory()) {
			this.deleteAFolder(siteId, uri, userId, currFile);
		} else if (currFile.isFile()) {
			this.deleteAFile(siteId, uri, userId, currFile);
		} else {
			throw new TemplateManagerException("文件["
					+ currFile.getAbsolutePath().replace('\\', '/')
					+ "]可能不存在,无法对它进行操作!");
		}
	}

	public void deleteWebprjFiles(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("删除站点下文件时,站点id、文件相对路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("删除站点下文件时,用户id不能为空！");
		}

		String sitepath = null;
		try {
			sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
		} catch (SiteManagerException e) {
			e.printStackTrace();
			throw new TemplateManagerException("根据站点id查找站点路径时发生异常！");
		}
		if (sitepath == null || sitepath.trim().length() == 0) {
			throw new TemplateManagerException("根据站点id没有找到站点的路径!");
		}
		File currFile = new File(new File(sitepath, "_webprj")
				.getAbsoluteFile(), uri);
		if (currFile.isDirectory()) {
			this.deleteAFolder(siteId, uri, userId, currFile);
		} else if (currFile.isFile()) {
			this.deleteAFile(siteId, uri, userId, currFile);
		} else {
			throw new TemplateManagerException("文件["
					+ currFile.getAbsolutePath().replace('\\', '/')
					+ "]可能不存在,无法对它进行操作!");
		}
	}

	private void deleteAFolder(String siteId, String uri, String userId,
			File folder) throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("删除站点下文件时,站点id、文件相对路径不能为空！");
		}
		if (folder != null) {
			if (folder.isFile()) {
				this.deleteAFile(siteId, uri, userId, folder);
			} else {
				String theURI = uri;
				if (!theURI.endsWith("/")) {
					theURI = theURI + "/";
				}
				File[] subfiles = folder.listFiles();
				for (int i = 0; subfiles != null && i < subfiles.length; i++) {
					if (subfiles[i].isFile()) {
						this.deleteAFile(siteId,
								theURI + subfiles[i].getName(), userId,
								subfiles[i]);
					} else {
						this.deleteAFolder(siteId, theURI
								+ subfiles[i].getName(), userId, subfiles[i]);
					}
				}
				this.deleteAFile(siteId, uri, userId, folder);
			}
		}

	}

	private void deleteAFile(String siteId, String uri, String userId, File file)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException("删除站点下文件时,站点id、文件相对路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("删除站点下文件时,用户id不能为空！");
		}

		String theURI = uri.replace('\\', '/');
		if (theURI.startsWith("/")) {
			theURI = theURI.substring(1);
		}
		if (theURI.endsWith("/")) {
			theURI = theURI.substring(0, theURI.length() - 1);
		}
		// System.out.println("<<<<<<<<theURI<<<<<"+theURI);

		// (1)试图对文件进行锁定
		try {
			checkoutAFile(siteId, theURI, userId);
		} catch (TemplateManagerException e) {
			throw new TemplateManagerException("在删除文件之前试图对文件锁定发生错误："
					+ e.getMessage());
		}
		TemplateManager tm = new TemplateManagerImpl();
		// (2)判断是否锁定成功:因为(1)步骤中中锁定的只是文件系统中现有的文件，数据库中可能还些文件系统中没有的文件信息
		DBUtil conn = new DBUtil();
		TransactionManager tran = new TransactionManager();
		try
		{
			tran.begin();
			if (this.canDelete(siteId, theURI, userId)) {
	
				// (3)删除文件对应的模板
				// TODO 可以根据文件扩展名来做一些优化
				/*
				 * String sql = "select a.template_id from " + "TD_CMS_TEMPLATE a
				 * inner join TD_CMS_SITE_TPL b " + "on a.TEMPLATE_ID =
				 * b.template_id " + "where a.PERSISTTYPE=1 and
				 * b.site_id='"+siteId+"' and " +
				 * "(a.TEMPLATEPATH||'/'||a.TEMPLATEFILENAME = '"+theURI+"' or
				 * a.TEMPLATEPATH like '"+theURI+"/%')";
				 */
				// 对文件对应的模板查询进行修改，modify by zhizhong.ding BUG号为：1411
				String sql = " select a.*,c.user_name from td_cms_template a inner join TD_CMS_SITE_TPL b on a.TEMPLATE_ID = b.template_id inner join TD_SM_USER c on a.CREATEUSER = c.user_id where a.PERSISTTYPE=1 and a.TEMPLATEFILENAME ='"
						+ file.getName() + "' ";
				String path = theURI.replaceAll(file.getName(), "");
				boolean root = false;
				if (path != null) {
					String p = path.replace('\\', '/');
					if (p.startsWith("/")) {
						p = p.substring(1);
					}
					if (p.endsWith("/")) {
						p = p.substring(0, p.length() - 1);
					}
					if (p.trim().length() != 0) {
						sql += " and a.TEMPLATEPATH = '" + p + "'";
					} else {
						root = true;
					}
				}
				if (path == null || root) {
					sql += " and (a.TEMPLATEPATH is null or   a.TEMPLATEPATH = '')";
				}
	
				sql += " and b.site_Id = '" + siteId + "'";
				try {
					conn.executeSelect(sql);
					for (int i = 0; i < conn.size(); i++) {
						boolean hasDelete = tm.deleteTemplateofSite(conn.getInt(i,
								"template_id"), Integer.parseInt(siteId));
						if (!hasDelete) {
							throw new TemplateManagerException(
									"删除文件对应的模板不成功，无法删除文件！");
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new TemplateManagerException("试图删除文件对应的模板，但查找模板信息不成功！");
				}
	
				// (4)删除文件
				boolean hasDelete = file.delete();
	
				// (5)删除成功后，取消对文件的锁定
				if (hasDelete) {
					sql = "update TD_CMS_FILE_STATUS set checkout_user = '',checkout_time=''"
							+ " where site_id='"
							+ siteId
							+ "' and uri='"
							+ theURI
							+ "' and (checkout_user is not null  or checkout_user<>'') and checkout_user='"
							+ userId + "'";
	
					try {
						conn.execute(sql);
					} catch (SQLException e) {
						e.printStackTrace();
						throw new TemplateManagerException(
								"删除文件之后,试图修改文件最新状态时发生异常!");
					}
					this.logFileChange(siteId, theURI, userId, "", "删除文件",-1);
				}
			} else {
				throw new TemplateManagerException("该[" + theURI
						+ "]文件你没有锁定,你不能删除它!");
			}
			tran.commit();
		} catch (TransactionException e2) {
			throw new TemplateManagerException(e2);
		} catch (RollbackException e) {
			throw new TemplateManagerException(e);
		}
		finally
		{
			tran.releasenolog();
		}
		
	}

	/**
	 * 如果那个文件被该用户锁定,则该用户能够删除该文件
	 * 
	 * @param siteId
	 * @param uri
	 * @param userId
	 * @return
	 * @throws TemplateManagerException
	 */
	private boolean canDelete(String siteId, String uri, String userId)
			throws TemplateManagerException {
		if (siteId == null
				|| siteId.trim().length() == 0
				|| uri == null
				|| uri.replace('\\', ' ').replace('/', ' ').trim().length() == 0) {
			throw new TemplateManagerException(
					"判断站点下文件是否可以被删除时,站点id、文件相对路径不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("判断站点下文件是否可以被某用户删除时,用户id不能为空！");
		}

		String theURL = uri.replace('\\', '/');
		if (theURL.startsWith("/")) {
			theURL = theURL.substring(1);
		}
		if (theURL.endsWith("/")) {
			theURL = theURL.substring(0, theURL.length() - 1);
		}

		DBUtil conn = new DBUtil();
		try {
			// (1) 检查文件是否被该用户check out了,如果没有被他check out 则没有权力删除
			String sql = "select 1 from TD_CMS_FILE_STATUS where site_id='"
					+ siteId + "' and uri='" + theURL
					+ "' and (checkout_user is not null  or checkout_user<>'') and checkout_user='"
					+ userId + "'  and checkout_time is not null";
			conn.executeSelect(sql);
			if (conn.size() > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.print("检查文件是否可以被删除时发生异常!");
			e.printStackTrace();
			throw new TemplateManagerException("检查文件是否可以被删除时发生异常!"
					+ e.getMessage());
		}
		return false;
	}

	public void reName(String siteId, String uri, String userId,
			String oldName, String newName) throws TemplateManagerException {
		if (siteId == null || siteId.trim().length() == 0) {
			throw new TemplateManagerException("给站点下的某个文件改名时,站点id不能为空！");
		}
		if (userId == null || userId.trim().length() == 0) {
			throw new TemplateManagerException("给站点下的某个文件改名时,用户id不能为空！");
		}

		// 这里的theURI指向文件的父文件夹的相对路径,与文件其他地方指向文件本身的uri有所区别
		String theURI = "";
		if (uri != null) {
			theURI = uri.replace('\\', '/');
			if (theURI.startsWith("/")) {
				theURI = theURI.substring(1);
			}
			if (!theURI.endsWith("/")) {
				theURI += "/";
			}
			if (theURI.trim().equals("/")) {
				theURI = "";
			}
		}
		if (oldName == null || oldName.trim().length() == 0 || newName == null
				|| newName.trim().length() == 0) {
			throw new TemplateManagerException("给站点下的某个文件改名时,请同时提供旧文件名和新文件名！");
		}
		String sitepath = null;
		try {
			sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
		} catch (SiteManagerException e) {
			e.printStackTrace();
			throw new TemplateManagerException("根据站点id查找站点路径时发生异常！无法重新命名！");
		}
		if (sitepath == null || sitepath.trim().length() == 0) {
			throw new TemplateManagerException("根据站点id没有找到站点的路径!无法重新命名！");
		}
		File currFile = new File(new File(sitepath, "_template")
				.getAbsoluteFile(), theURI + oldName);
		File newFile = new File(new File(sitepath, "_template")
				.getAbsoluteFile(), theURI + newName);
		if (newFile.exists()) {
			throw new TemplateManagerException("文件[" + theURI + newName
					+ "]已经存在,无法重命名为该文件名");
		}

		if (currFile.exists()) {
			// (1)将要改名的文件(夹)锁定
			try {
				checkOutFile(siteId, theURI + oldName, userId);
			} catch (TemplateManagerException e) {
				throw new TemplateManagerException("在改文件名之前试图对文件(夹)锁定失败,原因为:"
						+ e.getMessage());
			}
			// (2)改名 modify by zhizhong.ding bug:1416
			TemplateManager tm = new TemplateManagerImpl();
			if (currFile.isFile()) {// 如果为文件则先判断是否为模板文件，是模板文件的话就修改文件的名称
				Template template = tm.getTemplateInfo(siteId, uri, oldName);
				if (template == null) {
					currFile.renameTo(newFile);
				} else {
					template.setTemplateFileName(newName);
					tm.updateTemplate(template);
					currFile.renameTo(newFile);
				}

			} else {// 如果为文件夹的话，则查询出该文件夹路径下所有的模板文件，并对模板文件的模板路径进行修改
				tm.updateTemplatePath(siteId, theURI + oldName, theURI
						+ newName);
				currFile.renameTo(newFile);

			}

			// (3)删除数据库中改变了的文件状态信息,原因:1、文件不存在了，留在那里没有意义；2、留在那里会引起问题
			String sql = "delete td_cms_file_status " + "where site_id = '"
					+ siteId + "' " + "and (uri='" + theURI + oldName
					+ "' or uri like '" + theURI + oldName + "/%')";
			DBUtil conn = new DBUtil();
			try {
				conn.execute(sql);
			} catch (SQLException e) {
				System.out.println("删除文件(夹)后,更新文件(夹)在数据库中相关信息失败!");
				e.printStackTrace();
				throw new TemplateManagerException(
						"删除文件(夹)后,更新文件(夹)在数据库中相关信息失败!");

			}

			// (4)锁定改名后的文件
			this.checkOutFile(siteId, theURI + newName, userId);
		} else {
			throw new TemplateManagerException("文件["
					+ currFile.getAbsolutePath().replace('\\', '/')
					+ "]不存在,无法给它改名!");
		}
	}
}