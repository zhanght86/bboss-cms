package com.frameworkset.platform.cms.channelmanager;

public class ChannelManagerException extends Exception implements java.io.Serializable {

	public ChannelManagerException(String errormessage) {
		super(errormessage);
	}

	public ChannelManagerException(String msg, Exception e) {
		super(msg, e);
	}
}
