package com.frameworkset.platform.cms.container;

import java.io.FileReader;
import java.util.Date;

public class Template implements java.io.Serializable {
	private int templateId;
	private int siteId;
	
	/**
	 * 模版存储类型
	 * 0-数据库存储类型
	 * 1-文件存储类型
	 */
	private int persistType = 1;
	
	public static final int PERSISTINDB = 0;
	public static final int PERSISTINFILE = 1;
	
	/**
	 * 模板的修改时间戳，对应数据库的存储方式，如果是文件存储方式
	 * modifiedTime无含义
	 */
	public long modifiedTime = -1l;
	
	/**
	 * 模版对应的文件名称，只有以文件的方式存放模版的时候才有含义
	 */
	private String templateFileName = "detail.html";
	
	/**
	 * 模版对应的附件目录（如果模版是以文件的模式存放，同时也对应了模版文件的存放目录）
	 * 
	 */
	private String templatePath = "indexnews";
	
	

	private String name;

	private String description;

	private String header;

	// 模板正文内容
	private String text;

	// 模版类型:0,首页模版;1,概览;2,细览
	private int type = 0;

	private long createUserId;
	
	private String createUserName;
	private Date createTime;
	
	
	//是否是增量发布:1,需要进行增量发布;0,不需要进行增量发布
	private int increasePublishFlag = 0;
	
	//模版类型的子类型风格
	private int style ;
	//模板风格名称
	private String styleName ;

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getIncreasePublishFlag() {
		return increasePublishFlag;
	}

	public void setIncreasePublishFlag(int increasePublishFlag) {
		this.increasePublishFlag = increasePublishFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHeader() {
		return header;
//		return "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">${keywords}</head><body>";
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getText() {
		return text;
//		return "<tr class=\"cms_data_tr\" id=\"<cms:cell colName=\"title\" defaultValue=\"\"/>\">" + 
//				"<td align=center>"+
//					"<cms:cell colName=\"title\" defaultValue=\"\"/>"
//				+"</td> " + 
//				"<td>"+
//				"<cms:cell colName=\"content\" defaultValue=\"\"/>"
//				+"</td> "
//				+"<td>"
//				+"<cms:cell colName=\"docabstract\" defaultValue=\"\" />"
//				+"</td>"
//				+"<td class=\"tablecells\" align=center height='30' width=\"5%\">"
//				+"<cms:cell colName=\"keywords\" defaultValue=\"\"/>"
//				+"</td>"
//				+"</tr>";
	}

	public void setText(String text) {
		this.text = text;
	}
	
//	private Reader reader;
//	public void setTextReader(Reader reader)
//	{
//		this.reader = reader;
//	}
//	
//	public FileReader getReader()
//	{
//		return this.reader;
//	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public int getPersistType() {
		return persistType;
	}

	public void setPersistType(int persistType) {
		this.persistType = persistType;
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public long getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
}
