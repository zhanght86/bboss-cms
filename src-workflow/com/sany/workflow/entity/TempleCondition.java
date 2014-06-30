package com.sany.workflow.entity;

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

	private String createTime1;

	private String createTime2;

	private String creator;

	private String lastUpdatetime;

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

	public String getCreateTime1() {
		return createTime1;
	}

	public void setCreateTime1(String createTime1) {
		this.createTime1 = createTime1;
	}

	public String getCreateTime2() {
		return createTime2;
	}

	public void setCreateTime2(String createTime2) {
		this.createTime2 = createTime2;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLastUpdatetime() {
		return lastUpdatetime;
	}

	public void setLastUpdatetime(String lastUpdatetime) {
		this.lastUpdatetime = lastUpdatetime;
	}

}
