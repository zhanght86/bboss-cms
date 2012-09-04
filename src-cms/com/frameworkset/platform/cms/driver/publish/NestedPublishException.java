package com.frameworkset.platform.cms.driver.publish;

public class NestedPublishException extends RuntimeException {
	public NestedPublishException(String msg)
	{
		super(msg);
	}
	
	public NestedPublishException(String msg,Throwable throwe)
	{
		super(msg);
		super.initCause(throwe);
	}
	
	public NestedPublishException(Throwable throwe)
	{
		super(throwe);
//		super.initCause(throwe);
	}

}
