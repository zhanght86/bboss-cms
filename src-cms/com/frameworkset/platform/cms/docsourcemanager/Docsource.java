package com.frameworkset.platform.cms.docsourcemanager;

import java.io.Serializable;

public class Docsource implements Serializable{
	private String SRCNAME;
	private String SRCDESC;
	private String SRCLINK;
	private int CRUSER;
	private String username;
	private String CRTIME;
	private int DOCSOURCE_ID;

	public String getSRCNAME(){
		return SRCNAME;
	}
	public void setSRCNAME(String srcname) {
		SRCNAME = srcname;
	}
	public String getSRCDESC(){
		return SRCDESC;
	}
	public void setSRCDESC(String recdesc) {
		SRCDESC = recdesc;
	}
	public String getSRCLINK(){
		return SRCLINK;
	}
	public void setSRCLINK(String srclink){
		SRCLINK = srclink;
	}
	public int getCRUSER(){
		return CRUSER;
	}
	public void setCRUSER(int cruser) {
		CRUSER = cruser;
	}
	public String getCRTIME(){
		return CRTIME;
	}
	public void setCRTIME(String crtime) {
		CRTIME = crtime;
	}
	public int getDOCSOURCE_ID(){
		return DOCSOURCE_ID;
	}
	public void setDOCSOURCE_ID(int docsource_id) {
		DOCSOURCE_ID = docsource_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
