/**
获取指定路径和文件后缀名的文件名列表 Exception
Author:GongChunQuan
Date:2003.05.16
*/

package com.frameworkset.platform.cms.util;

public class GetFileNamesException extends FileOperException implements java.io.Serializable
{
	public GetFileNamesException()
    {
        super("获取指定路径和文件后缀名的文件名列表.失败.");
    }

	
}