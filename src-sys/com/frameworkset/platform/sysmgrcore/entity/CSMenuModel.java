/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * <p>Title: CSMenuModel.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-10-18 19:02:13
 * @author ge.tao
 * @version 1.0
 */
public class CSMenuModel implements Serializable {
	//id,parent_id,title,orderno,itemtype,owner_table
    private String id ;
    private String parent_id;
    private String title;
    private String orderno;
    private String type;
    private String owner_table;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getOwner_table() {
		return owner_table;
	}
	public void setOwner_table(String owner_table) {
		this.owner_table = owner_table;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
