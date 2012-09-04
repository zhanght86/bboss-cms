package com.frameworkset.platform.cms.templatemanager;

public class TemplateManagerException extends Exception implements java.io.Serializable
{

    /**
     * @param errormessage
     */
    public TemplateManagerException(String errormessage) 
    {
    	super(errormessage);
    }
    
    public TemplateManagerException() 
    {
    	super();
    }
}
