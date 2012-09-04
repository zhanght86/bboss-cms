package com.frameworkset.platform.cms.documentmanager.bean;

import java.util.Date;

public class DocTemplate implements java.io.Serializable {
	private int docTplId;			//文档模板id
	private String tplName;			//文档模板名
	private String tplCode;			//文档模板代码
	private long chnlId;			//所属频道id
	private int createUser;			//文档模板创建人
	private String description;		//文档模板描述
	private Date createTime;		//文档模板创建时间
	
	private String createUserName;		//用户名
	private String chnlName;		//频道显示名
	
	public String getChnlName() {
		return chnlName;
	}
	public void setChnlName(String chnlName) {
		this.chnlName = chnlName;
	}
	
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public long getChnlId() {
		return chnlId;
	}
	public void setChnlId(long chnlId) {
		this.chnlId = chnlId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCreateUser() {
		return createUser;
	}
	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDocTplId() {
		return docTplId;
	}
	public void setDocTplId(int docTplId) {
		this.docTplId = docTplId;
	}
	public String getTplCode() {
		return tplCode;
	}
	public void setTplCode(String tplCode) {
		this.tplCode = tplCode;
	}
	public String getTplName() {
		return tplName;
	}
	public void setTplName(String tplName) {
		this.tplName = tplName;
	}
}
