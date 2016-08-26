package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * 哪些文件不能出现在资源列表中
 *
 */
public class ResourceFilter implements FileFilter ,java.io.Serializable {

	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".svn"))
			return false;
		String filename = pathname.getName().toLowerCase();
		if(filename.endsWith(".scc"))
			return false;
		if(filename.endsWith(".bak"))
			return false;
		return true;
	}
}
