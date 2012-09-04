/**
获取指定路径和文件后缀名的文件名列表 Exception
Author:GongChunQuan
Date:2003.05.16
*/

package com.frameworkset.platform.cms.util;

public class FileOperException extends Exception implements java.io.Serializable
{
    public FileOperException()
    {
        super();
    }	

    public FileOperException(String msg)
    {
        super(msg);
    }	
	
}
