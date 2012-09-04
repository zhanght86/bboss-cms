package com.frameworkset.platform.cms.documentmanager.bean;

//引用文档的原频道
public class CitedDocSrcChannel implements java.io.Serializable {
	private int channelid;
	private String channelName;
	
	public int getChannelid() {
		return channelid;
	}
	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
