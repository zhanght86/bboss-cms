package com.frameworkset.platform.cms.searchmanager.handler;

/*--
 Copyright (C) @year@ i2a and David Duddleston. All rights reserved.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;

/**
 * <p><code>HTMLHandler</code>
 *  Content handler for HTML documents.
 * </p>
 *
 * @author <a href="mailto:david@i2a.com">David Duddleston</a>
 * @version 1.0
 */
public final class HTMLHandler extends ParserCallback implements ContentHandler{

    // Content
    private String title;
    private String description;
    private String keywords;
    private String categories;
    private long published;
    private String href;
    private String author;
    private StringBuffer contents;
    private ArrayList links;
    
    /*
	 * site			站点名字
	 * channel		频道名字
	 * dockind		文档类型
	 * title		标题
	 * subtitle		显示标题
	 * secondtitle	副标题
	 * author		作者
	 * keywords		关键词
	 * abstracts	摘要内容
	 * copyright	版权
	 * docwtime		编稿时间
	 * */
    private String site;
    private String channel;
    private String dockind;
    private String subtitle;
    private String secondtitle;
    private String abstracts;
    private String copyright;
    private long docwtime;
    
    private String htmlTitle;
    private String metaTitle;

    private String fileType = "notaspx";
    // Robot Instructions
    private boolean robotIndex;
    private boolean robotFollow;

    private static final char space = ' ';
    private char state;
    private static final char NONE = 0;
    private static final char TITLE = 1;
    private static final char HREF = 2;
    private static final char SCRIPT = 3;
    private static final char STYLE = 4;


    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
    private ParserDelegator pd = new ParserDelegator();
    
    
//    private static final HTMLHandler instance = new HTMLHandler();

    /**
     *		Constructor - initializes variables
     */
    public HTMLHandler() {

//        this.reset();

    }
	
//	public static HTMLHandler getInstance()
//	{
//		return instance;
//	}
    /**
     * Parse Content. [24] 320:1
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Return categories (from META tags)
     */
    public String getCategories() {
        return this.categories;
    }
    /**
      * 针对aspx文件出现乱码问题而加的方法
      * @return String
      */
     public String getFileType(){
         return fileType;
     }

     public void setFileType(String fileType){
         this.fileType = fileType;
     }
     /**
      *	Return contents
      */
     public String getContents() {
        if(this.fileType.equals("aspx")){
            byte[] conByte = this.contents.toString().getBytes();
            String con = null;
            try {
                con = new String(conByte, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            return con;
        }else{
            return this.contents.toString();
        }
     }

    /**
     *	Return description (from META tags)
     */
    public String getDescription() {
        return this.description;
    }
    /**
     *	Return META HREF
     */
    public String getHREF() {
        return this.href;
    }
    /**
     * Return keywords (from META tags)
     */
    public String getKeywords() {
        return this.keywords;
    }
    /**
     * Return links
     */
    public List getLinks() {
        return links;
    }
    /**
     *	Return published date (from META tag)
     */
    public long getPublished() {
        return this.published;
    }
    /**
     * Return boolean true if links are to be followed
     */
    public boolean getRobotFollow() {
        return this.robotFollow;
    }
    /**
     * Return boolean true if this is to be indexed
     */
    public boolean getRobotIndex() {
        return this.robotIndex;
    }
    /**
     *		Return page title
     */
    public String getTitle() {
    	String thisTitle = (metaTitle==null || metaTitle=="") ? htmlTitle : metaTitle;
        if(this.fileType.equals("aspx")){
          byte[] tilByte = thisTitle.getBytes();
          String til = null;
          try {
              til = new String(tilByte, "UTF-8");
          } catch (UnsupportedEncodingException ex) {
              ex.printStackTrace();
          }
          return til;
      }else{
          return thisTitle;
      }
    }
    /**
     *		Handle Anchor <A HREF="~"> tags
     */
    public void handleAnchor(MutableAttributeSet attribs) {
        String href = new String();
        href = (String) attribs.getAttribute(HTML.Attribute.HREF);
        if (href == null)
            return;
        links.add(href);
        state = HREF;
    }
    /**
     *		Closing tag
     */
    public void handleEndTag(Tag tag, int pos) {
        if (state == NONE)
            return;
        // In order of precedence == > && > ||
        if (state == TITLE && tag.equals(HTML.Tag.TITLE)) {
            state = NONE;
            return;
        }
        if (state == HREF && tag.equals(HTML.Tag.A)) {
            //links.add(linktext);
            state = NONE;
            return;
        }
        if (state == SCRIPT && tag.equals(HTML.Tag.SCRIPT)) {
            state = NONE;
            return;
        }
         if (state == STYLE && tag.equals(HTML.Tag.STYLE)) {
            state = NONE;
            return;
        }
    }
    /**
     *		Handle META tags
     */
    public void handleMeta(MutableAttributeSet attribs) {
        String name = new String();
        String content = new String();
        name = (String) attribs.getAttribute(HTML.Attribute.NAME);
        content = (String) attribs.getAttribute(HTML.Attribute.CONTENT);
        if (name == null || content == null)
            return;
        name = name.toUpperCase();
        if (name.equals("DESCRIPTION")) {
            description = content;
            return;
        }
        if (name.equals("KEYWORDS")) {
            keywords = content;
            return;
        }
        if (name.equals("CATEGORIES")) {
            categories = content;
            return;
        }
        if (name.equals("PUBLISHED")) {
            try {
                published = dateFormatter.parse(content).getTime();
            } catch(ParseException e) {e.printStackTrace();}
            return;
        }
        if (name.equals("HREF")) {
            href = content;
            return;
        }
        if (name.equals("AUTHOR")) {
            author = content;
            return;
        }
        if (name.equals("ROBOTS")) {

            if (content.indexOf("noindex") != -1) robotIndex = false;
            if (content.indexOf("nofollow") != -1) robotFollow = false;

            author = content;
            return;
        }
        
        /* 生成metas的名字
		 * */
        if (name.equals("TITLE")) {
        	metaTitle = content;
            return;
        }
        if (name.equals("SITE")) {
            site = content;
            return;
        }
        if (name.equals("CHANNEL")) {
            channel = content;
            return;
        }
        if (name.equals("DOCKIND")) {
            dockind = content;
            return;
        }
        if (name.equals("SUBTITLE")) {
            subtitle = content;
            return;
        }
        if (name.equals("SECONDTITLE")) {
            secondtitle = content;
            return;
        }        
        if (name.equals("ABSTRACTS")) {
            abstracts = content;
            return;
        }
        if (name.equals("COPYRIGHT")) {
        	copyright = content;
            return;
        }
        if (name.equals("DOCWTIME")) {
            try {
                published = dateFormatter.parse(content).getTime();
            } catch(ParseException e) {}
            return;
        }
        
    }
    /**
     *	Handle standalone tags
     */
    public void handleSimpleTag(Tag tag, MutableAttributeSet attribs, int pos) {
        if (tag.equals(HTML.Tag.META)) {
            handleMeta(attribs);
        }
    }
    /**
     *		Opening tag
     */
    public void handleStartTag(Tag tag, MutableAttributeSet attribs, int pos) {
        if (tag.equals(HTML.Tag.TITLE)) {
            state = TITLE;
        } else if (tag.equals(HTML.Tag.A)) {
            handleAnchor(attribs);
        } else if (tag.equals(HTML.Tag.SCRIPT)) {
            state = SCRIPT;
        }
        else if (tag.equals(HTML.Tag.STYLE)){
        	state=STYLE;
        }
    }
    /**
     *		Handle page text
     */
    public void handleText(char[] text, int pos) {
        switch (state) {
            case NONE :
                contents.append(text);
                contents.append(space);
                break;
            case TITLE :
                htmlTitle = new String(text);
                break;
            case HREF :
                contents.append(text);
                contents.append(space);
                //linktext = new String(text);
                break;
        }
    }
    /**
     * Parse Content.
     */
    public void parse(InputStream in) {
    	BufferedReader reader = null;
        try {
            reset();
            reader = new BufferedReader(new InputStreamReader(in,CmsEncoder.ENCODING_UTF_8));
            pd.parse(reader, this, true);

        } catch (Exception e) {e.printStackTrace();}
        finally
        {
        	if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					
				}
				if(reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						
					}	
        	
        }

    }

    /**
     * 返回文件类型
     * @return String
     */
    public String getFileFormat(){
        return ContentHandler.TEXT_HTML_FILEFOMAT;
    }

    /**
     *	Return contents
     */
    private void reset() {
        title = null;
        description = null;
        keywords = null;
        categories = null;
        href = null;
        author = null;
        
        /*
         * metas补充
         * */
        site = null;
        channel = null;
        dockind = null;
        subtitle = null;
        secondtitle = null;
        abstracts = null;
        
        metaTitle = null;
        htmlTitle = null;

        contents = new StringBuffer();
        links = new ArrayList();
        published = -1;

        // Robot Instructions
        robotIndex = true;
        robotFollow = true;

        state = NONE;
    }
    
    public static void main(String[] args){
    	 try {
//             java.net.URL url = new java.net.URL("http://news.163.com/07/0711/15/3J4N3UG3000120GU.html");
             java.net.URL url = new java.net.URL("http://localhost:8080/SanyPDP/sitepublish/BPIT/importantnews/content_3.html");
             java.net.HttpURLConnection con = (HttpURLConnection) url.openConnection();
             int rcode = con.getResponseCode();
             //System.out.println(con.getContentType());
    		 InputStream in = con.getInputStream();
    		 //InputStream in = new FileInputStream("D:\\workspace\\cms\\163xinwenTest.html");
             if(rcode == 200)
             {
//	             I_CmsTextExtractor te = CmsExtractorHtml.getExtractor();
//	             I_CmsExtractionResult er = te.extractText(in,"gb2312");
//	             if(er.getMetaInfo() == null){
//	            	 System.out.println("ture");
//	             }
//	             String content = er.getContent();
//	             System.out.println("content:" +  content);
	             
	             HTMLHandler hh = new HTMLHandler();
	             hh.parse(in);
	             String content1 = hh.getContents(); 
	             System.out.println("content:" + content1 );
             }
             
         } catch (Exception ex) {
        	ex.printStackTrace();
         }
    }

	public String getAbstracts() {
		return abstracts;
	}

	public String getChannel() {
		return channel;
	}

	public String getDockind() {
		return dockind;
	}

	public String getSecondtitle() {
		return secondtitle;
	}

	public String getSite() {
		return site;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public long getDocwtime() {
		return docwtime;
	}

	public void setDocwtime(long docwtime) {
		this.docwtime = docwtime;
	}

	public void parse(Map map) {
		// TODO Auto-generated method stub
		
	}

	
}
