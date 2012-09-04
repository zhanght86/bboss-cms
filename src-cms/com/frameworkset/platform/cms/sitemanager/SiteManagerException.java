package com.frameworkset.platform.cms.sitemanager;

/**
 * 站点管理异常
 * @author huaihai.ou
 * 日期:Dec 19, 2006
 * 版本:1.0
 * 版权所有:三一重工
 */
public class SiteManagerException extends Exception implements java.io.Serializable
{
    
    /**
     * @param errormessage
     */
    public SiteManagerException(String errormessage) 
    {
    	super(errormessage);
    }
    
    public SiteManagerException(String msg,Exception e) 
    {
    	super(msg,e);
    }
}
