package com.frameworkset.platform.cms.documentmanager.bean;

import java.util.Date;

public class CitedDocument implements java.io.Serializable {
	private String docName;               //引用文档名
	private int docid;					  //引用文档id  或者频道id
	private String srcChannelName;		  //引用文档所在频道名称
	private int srcChannelid;			  //引用文档所在频道id
	private int srcSiteId;				  //引用文档所在站点id
	private String srcSiteName;			  //引用文档所在站点名称
	private String citeUserName;          //引用人名称
	private int citeUserid;				  //引用人id
	private Date citeTime;				  //引用时间
	private int curChannelid;			  //引用文档当前所在的频道id
	private String curChannelName;		  //引用文档当前所在的频道名称
	private int statusid;				  //引用文档的状态id
	private String statusName;            //引用文档的状态名称
	private int citeType;			  //引用类型，频道引用1或者文档引用0
	private int curSiteid;			  //引用文档当前所在的站点id
	private String curSiteName;		  //引用文档当前所在的站点名称
	public int getCurSiteid() {
		return curSiteid;
	}
	public void setCurSiteid(int curSiteid) {
		this.curSiteid = curSiteid;
	}
	public String getCurSiteName() {
		return curSiteName;
	}
	public void setCurSiteName(String curSiteName) {
		this.curSiteName = curSiteName;
	}
	public String getCurChannelName() {
		return curChannelName;
	}
	public void setCurChannelName(String curChannelName) {
		this.curChannelName = curChannelName;
	}
	public Date getCiteTime() {
		return citeTime;
	}
	public void setCiteTime(Date citeTime) {
		this.citeTime = citeTime;
	}
	public int getCiteUserid() {
		return citeUserid;
	}
	public void setCiteUserid(int citeUserid) {
		this.citeUserid = citeUserid;
	}
	public String getCiteUserName() {
		return citeUserName;
	}
	public void setCiteUserName(String citeUserName) {
		this.citeUserName = citeUserName;
	}
	public int getCurChannelid() {
		return curChannelid;
	}
	public void setCurChannelid(int curChannelid) {
		this.curChannelid = curChannelid;
	}
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public int getSrcChannelid() {
		return srcChannelid;
	}
	public void setSrcChannelid(int srcChannelid) {
		this.srcChannelid = srcChannelid;
	}
	public String getSrcChannelName() {
		return srcChannelName;
	}
	public void setSrcChannelName(String srcChannelName) {
		this.srcChannelName = srcChannelName;
	}
	public int getStatusid() {
		return statusid;
	}
	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public int getCiteType() {
		return citeType;
	}
	public void setCiteType(int citeType) {
		this.citeType = citeType;
	}
	public String getSrcSiteName() {
		return srcSiteName;
	}
	public void setSrcSiteName(String srcSiteName) {
		this.srcSiteName = srcSiteName;
	}
	public int getSrcSiteId() {
		return srcSiteId;
	}
	public void setSrcSiteId(int srcSiteId) {
		this.srcSiteId = srcSiteId;
	}
	
}
