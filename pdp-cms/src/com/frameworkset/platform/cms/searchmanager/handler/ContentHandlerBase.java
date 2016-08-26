package com.frameworkset.platform.cms.searchmanager.handler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;
import com.frameworkset.platform.cms.searchmanager.extractors.I_CmsExtractionResult;
import com.frameworkset.common.poolman.sql.ColumnMetaData;

public class ContentHandlerBase implements ContentHandler {
	protected String version;
	//	 Content
	protected String title;
    protected String description;
    protected String keywords;
    protected String categories;
    protected long published = -1;
    protected String href;
    protected String author;
    protected StringBuffer contents;
    protected ArrayList links;
    protected String fileFormat;
   
    /*
     * meta
     * */
    private String site;
    private String channel;
    private String dockind;
    private String subtitle;
    private String secondtitle;
    private String abstracts;
    private String copyright;
    private long docwtime = -1;

    protected String fileType = "notaspx";
    // Robot Instructions
    protected boolean robotIndex;
    protected boolean robotFollow;
    
    
    
    protected Map metoInfo;
    
	public String getAuthor() {
		
		if(metoInfo != null){
			author = (String)this.metoInfo.get(I_CmsExtractionResult.META_AUTHOR);
    	}

    	if(this.author == null)
    		this.author = "";
    	
		return this.author;
	}

	public String getCategories() {
		if(metoInfo != null){
			categories = (String)this.metoInfo.get(I_CmsExtractionResult.META_CATEGORY);
    	}

    	if(this.categories == null)
    		this.categories = "";
    	
		return this.categories;
	}

	public String getContents() {
		return this.contents.toString();
	}

	public String getDescription() {
		if(metoInfo != null){
			description = (String)this.metoInfo.get(I_CmsExtractionResult.META_COMMENTS);
    	}

    	if(this.description == null)
    		this.description = "";
    	
		return this.description;
	}

	public String getHREF() {
		return this.href;
	}

	public String getKeywords() {
		if(metoInfo != null){
			keywords = (String)this.metoInfo.get(I_CmsExtractionResult.META_KEYWORDS);
    	}

    	if(this.keywords == null)
    		this.keywords = "";
    	
		return this.keywords;
	}

	public List getLinks() {
		return this.links;
	}

	public long getPublished() {
		if(metoInfo != null){
			Date publishDate = (Date)this.metoInfo.get(I_CmsExtractionResult.META_DATE_LASTMODIFIED);
			if(publishDate == null)
				publishDate = (Date)this.metoInfo.get(I_CmsExtractionResult.META_DATE_CREATED);
			if(publishDate != null)
				published = publishDate.getTime();
    	}

    	if(this.published == 0)
    		this.published = -1;
    	
		return this.published;
	}

	public boolean getRobotFollow() {
		return false;
	}

	public boolean getRobotIndex() {
		return false;
	}

	public String getTitle() {
		if(metoInfo != null){
    		title = (String)this.metoInfo.get(I_CmsExtractionResult.META_TITLE);
    	}

    	if(this.title == null)
    		this.title = "";
    	
		return this.title;
	}

	public String getFileFormat() {
		return this.fileFormat;
	}

	public void parse(InputStream in) {
		// TODO Auto-generated method stub

	}
	
	public void parse(Map map) {
		// TODO Auto-generated method stub

	}
	
	protected void reset() {
		title = "";
	    description = "";
	    keywords = "";
	    categories = "";
	    published = -1;
	    href = null;
	    author = "";
	    contents = new StringBuffer();
	    links = null;
	    fileFormat = "";

	    fileType = "notaspx";
	    // Robot Instructions
	    robotIndex = false;
	    robotFollow = false;
	}

	public String getAbstracts() {
		// TODO Auto-generated method stub
		return this.abstracts;
	}

	public String getChannel() {
		// TODO Auto-generated method stub
		return this.channel;
	}

	public String getDockind() {
		// TODO Auto-generated method stub
		return this.dockind;
	}

	public String getSecondtitle() {
		// TODO Auto-generated method stub
		return this.secondtitle;
	}

	public String getSite() {
		// TODO Auto-generated method stub
		return this.site;
	}

	public String getSubtitle() {
		// TODO Auto-generated method stub
		return this.subtitle;
	}

	public String getCopyright() {
		// TODO Auto-generated method stub
		return this.copyright;
	}

	public long getDocwtime() {
		// TODO Auto-generated method stub
		return this.docwtime;
	}
	
}
