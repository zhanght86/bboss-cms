package com.frameworkset.platform.cms.searchmanager.bean;

import java.util.Date;

/*-- 
 Copyright (C) @year@ i2a and David Duddleston. All rights reserved.
 */

/**
 * <p><code>CMSSearchHit</code>
 *	Represents a document returned 
 * </p>
 * 
 * @author 
 * @version 1.0
 */
public final class CMSSearchHit implements java.io.Serializable {
    
	private String categories;
	private String contentType;
	private String href;
	private String keywords;
	private Date published;
	private String title;
	private float score;
	
	private String description;
	private String url;
	private String content;
	
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Date getPublished() {
		return published;
	}
	public void setPublished(Date published) {
		this.published = published;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
    
}
