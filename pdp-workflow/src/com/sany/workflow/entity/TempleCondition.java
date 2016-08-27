package com.sany.workflow.entity;

import java.sql.Timestamp;

import org.frameworkset.util.annotations.RequestParam;

import com.frameworkset.orm.annotation.Column;

/**
 * @todo 模板查询条件
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public class TempleCondition {

	private String templeId;

	private String templeType;

	private String templeTitle;

	@Column(type = "clob")
	private String templeContent;

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createTime1;

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createTime2;

	private String creator;

	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp lastUpdatetime;

	public String getTempleId() {
		return templeId;
	}

	public void setTempleId(String templeId) {
		this.templeId = templeId;
	}

	public String getTempleType() {
		return templeType;
	}

	public void setTempleType(String templeType) {
		this.templeType = templeType;
	}

	public String getTempleTitle() {
		return templeTitle;
	}

	public void setTempleTitle(String templeTitle) {
		this.templeTitle = templeTitle;
	}

	public String getTempleContent() {
		return templeContent;
	}

	public void setTempleContent(String templeContent) {
		this.templeContent = templeContent;
	}

	public Timestamp getCreateTime1() {
		return createTime1;
	}

	public void setCreateTime1(Timestamp createTime1) {
		this.createTime1 = createTime1;
	}

	public Timestamp getCreateTime2() {
		return createTime2;
	}

	public void setCreateTime2(Timestamp createTime2) {
		this.createTime2 = createTime2;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getLastUpdatetime() {
		return lastUpdatetime;
	}

	public void setLastUpdatetime(Timestamp lastUpdatetime) {
		this.lastUpdatetime = lastUpdatetime;
	}

}
