package com.frameworkset.platform.cms.documentmanager.bean;

import java.util.Date;
/**
 * 文档的相关文档
 * @作者 xinwang.jiao 
 * @日期 2007-1-24 8:47:31
 * @版本 v1.0
 * @版权所有 bbossgroups
 */

public class DocRelated implements java.io.Serializable {
	/**
	 * 主文档ID
	 */
	private int docId;
	/**
	 * 相关的文档ID
	 */
	private int relatedDocId;
	/**
	 * 相关的文档name
	 */
	private String relatedDocName;
	/**
	 * 操作人ID
	 */
	private int opUserId;
	/**
	 * 操作时间
	 */
	private Date opTime;
	/**
	 * 相关文档所属频道显示名称
	 */
	private String chlDisplayName;
	
	/**
	 * 标识文档是否被回收
	 *   true-回收
	 *   false-未回收，正常状态
	 */
	private boolean deleted = false;
	
	public int getDocId() 
	{
		return docId;
	}
	
	public void setDocId(int docId) 
	{
		this.docId = docId;
	}
	
	public Date getOpTime() 
	{
		return opTime;
	}
	
	public void setOpTime(Date opTime) 
	{
		this.opTime = opTime;
	}
	
	public int getOpUserId() 
	{
		return opUserId;
	}
	
	public void setOpUserId(int opUserId) 
	{
		this.opUserId = opUserId;
	}
	
	public int getRelatedDocId() 
	{
		return relatedDocId;
	}
	
	public void setRelatedDocId(int relatedDocId) 
	{
		this.relatedDocId = relatedDocId;
	}

	public String getRelatedDocName() 
	{
		return relatedDocName;
	}

	public void setRelatedDocName(String relatedDocName) 
	{
		this.relatedDocName = relatedDocName;
	}

	public String getChlDisplayName() {
		return chlDisplayName;
	}

	public void setChlDisplayName(String chlDisplayName) {
		this.chlDisplayName = chlDisplayName;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
