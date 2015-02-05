package com.frameworkset.platform.cms.votemanager.ws;

import java.io.Serializable;
import java.util.Date;

public class VoteTitle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private Date timeEnd;

	private String timeEndStr;

	private String disposedep;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getDisposedep() {
		return disposedep;
	}

	public void setDisposedep(String disposedep) {
		this.disposedep = disposedep;
	}

	public String getTimeEndStr() {
		return timeEndStr;
	}

	public void setTimeEndStr(String timeEndStr) {
		this.timeEndStr = timeEndStr;
	}

}
