package com.frameworkset.platform.cms.documentmanager;

public class DocumentSource implements java.io.Serializable {
	
	private String srcname;
	
	private String srcdesc;
	
	private String srclink;
	
	private int cruser;
	
	private String crtime;
	
	private int docsource_id;
	
	public String getCrtime() {
		return crtime;
	}
	public void setCrtime(String crtime) {
		this.crtime = crtime;
	}
	public int getCruser() {
		return cruser;
	}
	public void setCruser(int cruser) {
		this.cruser = cruser;
	}
	public int getDocsource_id() {
		return docsource_id;
	}
	public void setDocsource_id(int docsource_id) {
		this.docsource_id = docsource_id;
	}
	public String getSrcdesc() {
		return srcdesc;
	}
	public void setSrcdesc(String srcdesc) {
		this.srcdesc = srcdesc;
	}
	public String getSrclink() {
		return srclink;
	}
	public void setSrclink(String srclink) {
		this.srclink = srclink;
	}
	public String getSrcname() {
		return srcname;
	}
	public void setSrcname(String srcname) {
		this.srcname = srcname;
	}
	}
