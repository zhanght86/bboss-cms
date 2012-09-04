package com.frameworkset.platform.task;

import java.io.Serializable;

public class ScheduleServiceException extends Exception implements Serializable {
	
	public ScheduleServiceException()
	{
		super();
	}
	
	public ScheduleServiceException(String msg)
	{
		super(msg);
	}

}
