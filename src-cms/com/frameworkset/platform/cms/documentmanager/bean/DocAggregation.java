package com.frameworkset.platform.cms.documentmanager.bean;
/**
 * 聚合文档实体
 * @author zhuo.wang
 *
 */
public class DocAggregation implements java.io.Serializable {
	/**
	 * 聚合文档ID
	 */	
	private int aggrdocid;
	/**
	 * 序号
	 */
	private int seq;
	/**
     * 被聚合的内容ID
	 */
	private int idbyaggr;
	/**
	 * 内容标题
	 */
	private String title;
	/**
	 * 类型1:文档 2：频道
	 */
	private String type;
	/**
     * 文档subtitle
	 * 数据库没有对应的字段
	 */
	private String docsubtitle;
	/**
     * 文档chlId
	 * 数据库没有对应的字段
	 * 用于发布
	 */
	private String chlId;
	
	/**
     * 文档chlName
	 * 数据库没有对应的字段
	 * 
	 */
	private String chlName;
	
	/**
	 * 是否要标记为new文档
	 * 1：是，0：否 ，缺省为0
	 * add by xinwang.jiao 2007.09.19
	 */
	private int isNew = 0;
	
	/**
	 * new标记图片路径
	 * add by xinwang.jiao 2007.09.19
	 */
	private String newPicPath;
	
	/*
	 * 文档发布路径
	 * 
	 */
	private String docpuburl;
	  

	public String getDocpuburl() {
		return docpuburl;
	}

	public void setDocpuburl(String docpuburl) {
		this.docpuburl = docpuburl;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public String getNewPicPath() {
		return newPicPath;
	}

	public void setNewPicPath(String newPicPath) {
		this.newPicPath = newPicPath;
	}

	public int getAggrdocid() 
	{
		return aggrdocid;
	}
	
	public void setAggrdocid(int aggrdocid) 
	{
		this.aggrdocid = aggrdocid;
	}
	
	public int getIdbyaggr() 
	{
		return idbyaggr;
	}
	
	public void setIdbyaggr(int idbyaggr) 
	{
		this.idbyaggr = idbyaggr;
	}
	
	public int getSeq() 
	{
		return seq;
	}
	
	public void setSeq(int seq) 
	{
		this.seq = seq;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getType() 
	{
		return type;
	}
	
	public void setType(String type) 
	{
		this.type = type;
	}
	
	public String getDocsubtitle() 
	{
		return docsubtitle;
	}
	
	public void setDocsubtitle(String docsubtitle) 
	{
		this.docsubtitle = docsubtitle;
	}

	public String getChlId() {
		return chlId;
	}

	public void setChlId(String chlId) {
		this.chlId = chlId;
	}

	public String getChlName() {
		return chlName;
	}

	public void setChlName(String chlName) {
		this.chlName = chlName;
	}
	 
}
