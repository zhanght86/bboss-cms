package com.frameworkset.platform.cms.documentmanager;

import java.io.Serializable;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.documentmanager.Attachment.java</p>
 *
 * <p>Description: 存放附件信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-24
 * @author biaoping.yin
 * @version 1.0
 */
public class Attachment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7850214525122230405L;
	/**
	 * id主键
	 */
	private int id;
	/**
	 * document_id
	 */
	private int documentId;
	
	private Document document;
	/**
	 * 文档附件的URL
	 */
	private String url;
	/**
	 * 附件类型:
	 * 1:编辑器中加的附件
	 * 2:文档相关附件
	 * 3:文档相关图片
	 */
	private int type;
	/**
	 * 附件说明
	 */
	private String description;
	/**
	 * 附件原文件名
	 */
	private String originalFilename;
	/**
	 * 有效标记：
	 * 1：有效，
	 * 0：无效
	 * 默认值为1
	 */
	private String valid;
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	public int getDocumentId() 
	{
		return documentId;
	}
	
	public void setDocumentId(int documentId) 
	{
		this.documentId = documentId;
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getOriginalFilename() 
	{
		return originalFilename;
	}
	
	public void setOriginalFilename(String originalFilename) 
	{
		this.originalFilename = originalFilename;
	}
	
	public int getType() 
	{
		return type;
	}
	
	public void setType(int type) 
	{
		this.type = type;
	}
	
	public String getUrl() 
	{
		return url;
	}
	public void setUrl(String url) 
	{
		this.url = url;
	}
	
	public String getValid() 
	{
		return valid;
	}
	
	public void setValid(String valid) 
	{
		this.valid = valid;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
}
