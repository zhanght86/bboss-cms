package com.frameworkset.platform.cms.flowmanager;

public class FlowManagerException extends Exception implements java.io.Serializable 
{
	public FlowManagerException(String errormessage) 
    {
    	super(errormessage);
    }
    
    public FlowManagerException(String msg,Exception e) 
    {
    	super(msg,e);
    }

}
