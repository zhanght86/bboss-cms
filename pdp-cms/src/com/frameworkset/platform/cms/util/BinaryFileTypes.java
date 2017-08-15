package com.frameworkset.platform.cms.util;

import java.util.ListResourceBundle;

/**
 * 二进制文件的类型和相应的mimetype映射关系
 * <p>Title: BinaryFileTypes</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-4-19 15:12:54
 * @author biaoping.yin
 * @version 1.0
 */
public class BinaryFileTypes extends ListResourceBundle implements java.io.Serializable {

	protected Object[][] getContents() {		
		return contents;
	}
	
	private final static Object contents[][] = { { "", "content/unknown" },
			{ "a", "application/octet-stream" },
			{ "ai", "application/postscript" }, { "aif", "audio/x-aiff" },
			{ "aifc", "audio/x-aiff" }, { "aiff", "audio/x-aiff" },
			{ "arc", "application/octet-stream" }, { "au", "audio/basic" },
			{ "avi", "video/x-msvideo" }, { "asf", "video/x-ms-asf" },
			{ "bcpio", "application/x-bcpio" },
			{ "bin", "application/octet-stream" }, { "c", "text/plain" },
			{ "c++", "text/plain" }, { "cc", "text/plain" },
			{ "cdf", "application/x-netcdf" },
			{ "cpio", "application/x-cpio" },
			{ "dump", "application/octet-stream" },
			{ "dvi", "application/x-dvi" },
			{ "eps", "application/postscript" }, { "etx", "text/x-setext" },
			{ "exe", "application/octet-stream" }, { "gif", "image/gif" },
			{ "gtar", "application/x-gtar" },
			{ "gz", "application/octet-stream" }, { "h\t", "text/plain" },
			{ "hdf", "application/x-hdf" },
			{ "hqx", "application/octet-stream" }, 
			{ "ief", "image/ief" },
			{ "java", "text/plain" }, { "jfif", "image/jpeg" },
			{ "jfif-tbnl", "image/jpeg" }, { "jpe", "image/jpeg" },
			{ "jpeg", "image/jpeg" }, { "jpg", "image/jpeg" },
			{ "png", "image/png" }, { "bmp", "image/bmp" },
			{ "js", "application/x-javascript" },
			{ "latex", "application/x-latex" },
			{ "man", "application/x-troff-man" },
			{ "me", "application/x-troff-me" }, { "mime", "message/rfc822" },
			{ "mov", "video/quicktime" }, { "movie", "video/x-sgi-movie" },
			{ "mpe", "video/mpeg" }, { "mpeg", "video/mpeg" },
			{ "mpg", "video/mpeg" }, { "mp3", "audio/mpeg" },
			{ "ms", "application/x-troff-ms" }, { "mv", "video/x-sgi-movie" },
			{ "nc", "application/x-netcdf" },
			{ "o", "application/octet-stream" }, { "oda", "application/oda" },
			{ "pbm", "image/x-portable-bitmap" }, { "pdf", "application/pdf" },
			{ "pgm", "image/x-portable-graymap" }, { "pl", "text/plain" },
			{ "rm", "application/vnd.rn-realmedia" },
			{ "rmvb", "application/vnd.rn-realmedia" },
			{ "ram", "audio/x-pn-realaudio" }, { "ra", "audio/x-realaudio" },
			{ "rpm", "audio/x-pn-realaudio-plugin" },
			{ "pnm", "image/x-portable-anymap" },
			{ "ppm", "image/x-portable-pixmap" },
			{ "ps", "application/postscript" }, { "qt", "video/quicktime" },
			{ "ras", "image/x-cmu-rast" }, { "rgb", "image/x-rgb" },
			{ "roff", "application/x-troff" }, { "rtf", "application/rtf" },
			{ "rtx", "application/rtf" },
			{ "saveme", "application/octet-stream" },
			{ "sh", "application/x-shar" }, { "shar", "application/x-shar" },
			{ "snd", "audio/basic" }, { "src", "application/x-wais-source" },
			{ "sv4cpio", "application/x-sv4cpio" },
			{ "sv4crc", "application/x-sv4crc" },
			{ "swf", "application/x-shockwave-flash" },
			{ "t", "application/x-troff" }, { "tar", "application/x-tar" },
			{ "tex", "application/x-tex" },
			{ "texi", "application/x-texinfo" },
			{ "texinfo", "application/x-texinfo" }, { "text", "text/plain" },
			{ "tif", "image/tiff" }, { "tiff", "image/tiff" },
			{ "tr", "application/x-troff" },
			{ "tsv", "text/tab-separated-values" }, { "txt", "text/plain" },
			{ "ustar", "application/x-ustar" },
			{ "uu", "application/octet-stream" }, { "wav", "audio/x-wav" },
			{ "wsrc", "application/x-wais-source" },
			{ "xbm", "image/x-xbitmap" }, { "xpm", "image/x-xpixmap" },
			{ "xwd", "image/x-xwindowdump" },
			{ "z", "application/octet-stream" }, { "zip", "application/zip" },
			{ "rar", "application/rar" }, { "xls", "application/msexcel" },
			{ "ppt", "application/vnd.ms-powerpoint" },
			{ "doc", "application/msword" }, { "wmv", "video/x-msvideo" } ,	 
	{ "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },			
	{ "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
	{ "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" } };


}
