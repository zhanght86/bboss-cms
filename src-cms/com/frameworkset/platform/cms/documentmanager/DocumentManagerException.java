//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemanager\\SiteManagerException.java

package com.frameworkset.platform.cms.documentmanager;

/**
 * 文档管理异常
 * @author zhuo.wang
 * 日期:Dec 26. 2006
 * 版本:1.0
 * 版权所有:三一重工
 */
public class DocumentManagerException extends Exception implements java.io.Serializable
{
    
    /**
     * @param errormessage
     */
    public DocumentManagerException(String errormessage) 
    {
    	super(errormessage);
    }
    
    public DocumentManagerException() 
    {
    	super();
    }

	public DocumentManagerException(String string, Exception e) {
		super(string,e);
	}
}
