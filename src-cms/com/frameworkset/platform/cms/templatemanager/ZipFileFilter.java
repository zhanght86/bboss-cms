package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * Zip文件过滤器
 * zip
 * @author jxw
 * 2007.4.19
 */

public class ZipFileFilter implements FileFilter,java.io.Serializable 
{
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".svn"))
			return false;
		if(pathname.isDirectory())
			return false;
		String lowerName = pathname.getName().toLowerCase();
		if(lowerName.endsWith(".zip"))
			return true;
		return false;
	}

}
