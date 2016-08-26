/**
文件移动 Exception
Author:GongChunQuan
Date:2003.05.16
*/

package com.frameworkset.platform.cms.util;

public class FileMoveException extends FileOperException implements java.io.Serializable
{
	public FileMoveException()
    {
        super();
    }	

    public FileMoveException(String msg)
    {
        super(msg);
    }	

	
}
