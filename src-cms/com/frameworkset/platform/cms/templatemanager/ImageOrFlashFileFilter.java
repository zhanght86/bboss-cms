package com.frameworkset.platform.cms.templatemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * 只接受Flash格式的文件
 * swf
 * 和只接受图片格式的文件
 * gif|jpg|jpeg|bmp|png
 */
public class ImageOrFlashFileFilter implements FileFilter,java.io.Serializable {
		public boolean accept(File pathname) {
			if(pathname.getName().endsWith(".svn"))
				return false;
			if(pathname.isDirectory())
				return false;
			String lowerName = pathname.getName().toLowerCase();
			if(lowerName.endsWith(".swf")||lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")
					 || lowerName.endsWith(".gif") || lowerName.endsWith(".bmp") || lowerName.endsWith(".png") )
				return true;
			return false;
		}

	}

