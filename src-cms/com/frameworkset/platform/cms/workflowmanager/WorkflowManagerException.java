package com.frameworkset.platform.cms.workflowmanager;

/**
 * 流程操作异常类
 * @author jxw
 *
 * 2007-9-25
 */
public class WorkflowManagerException extends Exception implements java.io.Serializable {
	
	WorkflowManagerException(String msg)
	{
		super(msg);
	}
	
	WorkflowManagerException(String msg,Exception e)
	{
		super(msg,e);
	}

}
