package com.frameworkset.platform.cms.container;

/**
 * 文档级别
 * @作者 xinwang.jiao 
 * @日期 2007-1-23 9:47:31
 * @版本 v1.0
 * @版权所有 bbossgroups
 */

public class DocLevel implements java.io.Serializable
{
	/**
	 * 文档级别编码
	 */
	private int id;
	/**
	 * 文档级别名称
	 */
	private String name;
	/**
	 * 级别，数字越小，级别越高
	 */
	private int level;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
