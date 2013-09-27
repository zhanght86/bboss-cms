package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * 只接受Flash格式的文件
 * swf
 */
public class FlashFileFilter implements FileFilter,java.io.Serializable{

	public boolean accept(File pathname) {
		if(pathname.isDirectory())
			return false;
		String lowerName = pathname.getName().toLowerCase();
		if(lowerName.endsWith(".swf") || lowerName.endsWith(".flv"))
			return true;
		return false;
	}

}
