//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemanager\\SiteManagerException.java

package com.frameworkset.platform.cms.docsourcemanager;

/**
 * 文档管理异常
 * @author zhuo.wang
 * 日期:Dec 26. 2006
 * 版本:1.0
 * 版权所有:三一重工
 */
public class DocsourceManagerException extends Exception implements java.io.Serializable
{
    
    /**
     * @param errormessage
     */
    public DocsourceManagerException(String errormessage) 
    {
    	super(errormessage);
    }
    
    public DocsourceManagerException() 
    {
    	super();
    }
}
