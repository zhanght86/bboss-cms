package com.frameworkset.platform.cms.searchmanager.handler;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p><code>ContentHandler</code>
 *	Interface for content handlers.
 * </p>
 *
 * @author <a href="mailto:david@i2a.com">David Duddleston</a>
 * @version 1.0
 */
public interface ContentHandler extends java.io.Serializable {

   static final String WORD_FILEFOMAT = "application/msword";
   static final String TEXT_HTML_FILEFOMAT = "text/html";
   static final String PDF_FILEFOMAT = "application/pdf";
   static final String PPT_FILEFOMAT = "application/vnd.ms-powerpoint";
   static final String EXCEL_FILEFOMAT = "application/vnd.ms-excel";
   static final String RTF_FILEFOMAT = "application/rtf";
   public static final String VERSION_2003 = "2003";
   public static final String VERSION_2007 = "2007";
   //库表类型
   static final String DBT_FILEFOMAT = "database/Table";
   

    /**
     * Return author
     */
    public String getAuthor();
    /**
     * Return categories (from META tags)
     */
    public String getCategories();
    /**
     *	Return contents
     */
    public String getContents();
    /**
     *	Return description (from META tags)
     */
    public String getDescription();
    /**
     *	Return META HREF
     */
    public String getHREF();
    /**
     * Return keywords (from META tags)
     */
    public String getKeywords();
    /**
     * Return links
     */
    public List getLinks();
    /**
     *	Return published date (from META tag)
     */
    public long getPublished();
    /**
     *	Return description (from META tags)
     */
    public boolean getRobotFollow();
    /**
     *	Return description (from META tags)
     */
    public boolean getRobotIndex();
    /**
     *		Return page title
     */
    public String getTitle();

    /**
     * 文件类型
     * @return String
     */
    public String getFileFormat();
    /**
     * Parse Content.
     */
    public void parse(InputStream in);
    public void parse(Map map);
    
    /*
     * meta补充
     * */
    public String getAbstracts();

	public String getChannel();

	public String getDockind();

	public String getSecondtitle();

	public String getSite();

	public String getSubtitle();
	public String getCopyright();
	public long getDocwtime();
}
