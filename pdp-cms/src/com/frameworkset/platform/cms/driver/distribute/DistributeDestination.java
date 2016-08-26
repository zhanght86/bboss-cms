package com.frameworkset.platform.cms.driver.distribute;

import com.frameworkset.platform.cms.driver.context.FTPConfig;
import com.frameworkset.platform.cms.sitemanager.Site;

/**
 * 分发目的地，用来存放发布站点发布的最终目的地
 * 包括
 * <p>Title: DistributeDestination</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-19 9:58:22
 * @author biaoping.yin
 * @version 1.0
 */
public class DistributeDestination implements java.io.Serializable {
	/**
	 * 站点对象
	 */
	private Site site;
	/**站点发布产生文件的临时目录*/
	private String publishTemppath;
	/**
	 * 站点发布产生的文件的本机存放目录
	 */
	private String publishRootPath;
	
	/**
	 * 站点的发布产生文件的ftp地址配置
	 */
	private FTPConfig ftpconfig; 
	
	/**
	 * 
	 * @param site 站点对象
	 * @param publishTemppath 本站发布产生的文件的临时存放路径
	 * @param publishRootPath 本站发布产生的文件的本地存放路径
	 * @param ftpconfig  站点发布产生的文件的远程ftp上传配置
	 */
	public DistributeDestination(Site site,
								 String publishTemppath,
								 String publishRootPath,FTPConfig ftpconfig)
	{
		this.site = site;
		this.publishRootPath = publishRootPath;
		this.publishTemppath = publishTemppath;
		this.ftpconfig = ftpconfig;
	}
	
	public String getPublishRootPath() {
		return publishRootPath;
	}
	
	public String getPublishTemppath() {
		return publishTemppath;
	}
	
	public Site getSite() {
		return site;
	}

	public FTPConfig getFtpconfig() {
		return ftpconfig;
	}
	
	public boolean equals(Object other)
	{
		if(other == null)
			return false;
		if(other instanceof DistributeDestination)
		{
			DistributeDestination _temp = (DistributeDestination)other;
			return this.site.equal(_temp.getSite());
		}
		return false;
	}
	
	public int hashCode()
	{
		return site.hashCode();
	}
}
