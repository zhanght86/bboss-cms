package com.frameworkset.platform.cms.driver.jsp;

import java.io.File;

/**
 * 记录文件对应的模板文件时间戳，系统进行发布操作时，
 * 检查是否需要重新生成临时发布文件的依据就是看模板文件的时间戳和临时文件的时间戳是否相同，
 * 如果不同则需要重新生成临时发布文件
 * @author Administrator
 *
 */
public class FileTimestamp implements java.io.Serializable
{
	private long modifytime = -1l;
	private File file;
	public FileTimestamp(long lastModified) {
		this.modifytime = lastModified;
		
	}

	public long getModifytime() {
		return modifytime;
	}

	public void setModifytime(long modifytime) {
		this.modifytime = modifytime;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public boolean modified()
	{
		if(!file.exists() || file.lastModified() != this.modifytime)
			return true;
		else
			return false;
	}

}
