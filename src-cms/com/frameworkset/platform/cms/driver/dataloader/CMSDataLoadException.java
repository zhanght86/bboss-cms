package com.frameworkset.platform.cms.driver.dataloader;

public class CMSDataLoadException extends Exception implements java.io.Serializable {
	public CMSDataLoadException(String error)
	{
		super(error);
	}
}
