/*
 * Created on 2005-1-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.frameworkset.platform.holiday;

import java.io.Serializable;
import java.util.Observable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class ValueObject extends Observable implements Serializable {
	private boolean insertFlag = false;
	private boolean updateFlag = false;
	private boolean deleteFlag = false;
	private long rowVersion = 0;
	
	/**
	 * @return Returns the deleteFlag.
	 */
	public boolean isDeleteFlag() {
		return deleteFlag;
	}
	/**
	 * @param deleteFlag The deleteFlag to set.
	 */
	public void setDeleteFlag(boolean deleteFlag) {
		this.insertFlag = false;
		this.updateFlag = false;
		this.deleteFlag = deleteFlag;
	}
	/**
	 * @return Returns the insertFlag.
	 */
	public boolean isInsertFlag() {
		return insertFlag;
	}
	/**
	 * @param insertFlag The insertFlag to set.
	 */
	public void setInsertFlag(boolean insertFlag) {
		this.insertFlag = insertFlag;
		this.updateFlag = false;
		this.deleteFlag = false;
	}
	/**
	 * @return Returns the rowVersion.
	 */
	public long getRowVersion() {
		return rowVersion;
	}
	/**
	 * @param rowVersion The rowVersion to set.
	 */
	public void setRowVersion(long rowVersion) {
		this.rowVersion = rowVersion;
	}
	/**
	 * @return Returns the updateFlag.
	 */
	public boolean isUpdateFlag() {
		return updateFlag;
	}
	/**
	 * @param updateFlag The updateFlag to set.
	 */
	public void setUpdateFlag(boolean updateFlag) {
		this.insertFlag = false;
		this.updateFlag = updateFlag;
		this.deleteFlag = false;
	}
	
	public void resetFlags(){
		this.insertFlag = false;
		this.updateFlag = false;
		this.deleteFlag = false;
	}
}
