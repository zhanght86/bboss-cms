package com.frameworkset.platform.cms.customform;

/**
 * 自定义表单Bean类
 * @author jxw
 *
 */

public class CustomForm implements java.io.Serializable {
	/**
	 * 1:siteId;2:channelId
	 */
	private long targetId;
	/**
	 * 1:site;2:channel
	 */
	private String targetType;
	/**
	 * 1:采集,2:修改，3：列表
	 */
	private String formType;
	/**
	 * 
	 */
	private String fileName;
	/**
	 * 
	 */
	private String description;
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	public String getFileName() 
	{
		return fileName;
	}
	
	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}
	
	public String getFormType() 
	{
		return formType;
	}
	
	public void setFormType(String formType) 
	{
		this.formType = formType;
	}
	
	public long getTargetId() 
	{
		return targetId;
	}
	
	public void setTargetId(long targetId) 
	{
		this.targetId = targetId;
	}
	
	public String getTargetType() 
	{
		return targetType;
	}
	
	public void setTargetType(String targetType) 
	{
		this.targetType = targetType;
	}
	

}

