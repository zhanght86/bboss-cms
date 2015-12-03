package com.frameworkset.platform.cms.templatemanager;

public class TemplateManagerException extends Exception 
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

	public TemplateManagerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public TemplateManagerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TemplateManagerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
