package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * 多媒体文件过滤器
 * rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rmvb（13）
 * @author jxw
 * 2007.4.19
 */

public class MediaFileFilter implements FileFilter,java.io.Serializable 
{
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".svn"))
			return false;
		if(pathname.isDirectory())
			return false;
		String lowerName = pathname.getName().toLowerCase();
		if(lowerName.endsWith(".rm") || lowerName.endsWith(".mp3") || lowerName.endsWith(".wav")
				|| lowerName.endsWith(".mid") || lowerName.endsWith(".midi") || lowerName.endsWith(".ra")
				|| lowerName.endsWith(".avi") || lowerName.endsWith(".mpg") || lowerName.endsWith(".mpeg")
				|| lowerName.endsWith(".asf") || lowerName.endsWith(".asx") || lowerName.endsWith(".wma")
				|| lowerName.endsWith(".mov") || lowerName.endsWith(".wmv") || lowerName.endsWith(".rmvb"))
			return true;
		return false;
	}

}
