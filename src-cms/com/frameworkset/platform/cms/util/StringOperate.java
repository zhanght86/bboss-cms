package com.frameworkset.platform.cms.util;

public class StringOperate implements java.io.Serializable {
	
	public static String getFileExt(String fileName)
	 {
	  int lastIndex = fileName.lastIndexOf(".");
	  if  (lastIndex == -1) return "";
	  return fileName.substring(lastIndex+1);
	 }

	 public static String[] getFileNameAndExtName(String fileName)
	 {
		  String[] FileNameAndExtName = new String[2];
		  int lastIndex = fileName.lastIndexOf(".");
		  if  (lastIndex == -1) return null;
		  FileNameAndExtName[0] =fileName.substring(0, lastIndex);
		  FileNameAndExtName[1] = fileName.substring(lastIndex+1);
		  return FileNameAndExtName;//fileName.substring(lastIndex+1);
	 }
}
