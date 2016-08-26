package com.frameworkset.platform.cms.docCommentManager.docCommentDictManager;

public class DocCommentDict implements java.io.Serializable {
	private int wordId;			//词汇id
	private String word;		//词汇
	private String description;	//词汇描述
	private long siteId;		//所属站点id
	private String siteName;		//所属站点名称
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getSiteId() {
		return siteId;
	}
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getWordId() {
		return wordId;
	}
	public void setWordId(int wordId) {
		this.wordId = wordId;
	}
}
