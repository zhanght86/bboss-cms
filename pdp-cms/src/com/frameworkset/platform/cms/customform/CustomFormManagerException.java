package com.frameworkset.platform.cms.customform;

/**
 * 自定义表单管理异常类
 * @author jxw
 *
 */

public class CustomFormManagerException extends Exception implements java.io.Serializable
{
	public CustomFormManagerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CustomFormManagerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
     * @param errormessage
     */
    public CustomFormManagerException(String errormessage) 
    {
    	super(errormessage);
    }
    
    public CustomFormManagerException() 
    {
    	super();
    }

}
