package com.frameworkset.platform.cms.driver.htmlconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Remark;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.lexer.JspTagAware;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.tags.BaseHrefTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ResourceTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.driver.util.CmsStringUtil;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * Base utility class for OpenCms <code>{@link org.htmlparser.visitors.NodeVisitor}</code>
 * implementations, which provides some often used utility functions.
 * <p>
 * 
 * This base implementation is only a "pass through" class, that is the content is parsed, but the
 * generated result is exactly identical to the input.
 * <p>
 * 
 * @author Alexander Kandzior
 * @author biaoping.yin
 * 
 * @version $Revision: 1.3 $
 * 
 * @since 6.2.0
 */
public class CmsHtmlParser extends NodeVisitor implements I_CmsHtmlNodeVisitor,JspTagAware {

    /** List of upper case tag name strings of tags that should not be auto-corrected if closing divs are missing. */
    private List m_noAutoCloseTags;
    /**
	 * 标识页面中是否包含jsp标签
	 */
	protected boolean containJspTag = false;
    /** The array of supported tag names. */
    // important: don't change the order of these tags in the source, subclasses may expect the tags
    // at the exact indices give here
    // if you want to add tags, add them at the end
    protected static final String[] TAG_ARRAY = new String[] {
        "H1",
        "H2",
        "H3",
        "H4",
        "H5",
        "H6",
        "P",
        "DIV",
        "SPAN",
        "BR",
        "OL",
        "UL",
        "LI",
        "TABLE",
        "TD",
        "TR",
        "TH",
        "THEAD",
        "TBODY",
        "TFOOT"};

    /** The list of supported tag names. */
    protected static final List TAG_LIST = Arrays.asList(TAG_ARRAY);

    /** Indicates if "echo" mode is on, that is all content is written to the result by default. */
    protected boolean m_echo;

    /** The buffer to write the out to. */
    protected StringBuffer m_result;

    /** The providable configuration - never null by contract of interface. */
    private String m_configuration = "";

    /**
     * Creates a new instance of the html converter with echo mode set to <code>false</code>.
     * <p>
     */
    public CmsHtmlParser() {

        this(false);
    }

    /**
     * Creates a new instance of the html converter.
     * <p>
     * 
     * @param echo indicates if "echo" mode is on, that is all content is written to the result
     */
    public CmsHtmlParser(boolean echo) {

        m_result = new StringBuffer(1024);
        m_echo = echo;
        m_noAutoCloseTags = CMSUtil.getNoAutoCloseTagList();
    }


    /**
     * Degrades Composite tags that do have children in the DOM tree 
     * to simple single tags. This allows to avoid auto correction of unclosed HTML tags.<p>
     * 
     * @return A node factory that will not autocorrect open tags specified via <code>{@link #setNoAutoCloseTags(List)}</code>
     */
    private PrototypicalNodeFactory configureNoAutoCorrectionTags() {

        PrototypicalNodeFactory factory = new PrototypicalNodeFactory();

        String tagName;
        Iterator it = m_noAutoCloseTags.iterator();
        Div div = new Div();
        org.htmlparser.tags.ScriptTag scriptTag = new ScriptTag();  
        org.htmlparser.tags.LinkTag a = new LinkTag();
        List aNames =  Arrays.asList(a.getIds());
        List divNames = Arrays.asList(div.getIds());
        List scriptTags = Arrays.asList(scriptTag.getIds());
        while (it.hasNext()) {
            tagName = ((String)it.next());
            // div
            if (divNames.contains(tagName)) {
                factory.unregisterTag(new Div());
                factory.registerTag(new DivTag());
                
            }
            //a
            if (aNames.contains(tagName)) {
            	factory.remove(tagName);
                
            }
            
            if (scriptTags.contains(tagName)) {
            	factory.remove(tagName);
                
            }
            
            // TODO: add more tags for flat parsing / non correction of missing closing tags here
        }
//        factory.clear();
//        factory.remove("BASE");
        return factory;
    }

    /**
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#getConfiguration()
     */
    public String getConfiguration() {

        return m_configuration;
    }

    /**
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#getResult()
     */
    public String getResult() {

        return m_result.toString();
    }

    /**
     * Returns the HTML for the given tag itself (not the tag content).
     * <p>
     * 
     * @param tag the tag to create the HTML for
     * 
     * @return the HTML for the given tag
     */
    public String getTagHtml(Tag tag) {

        StringBuffer result = new StringBuffer(32);
        String text = tag.getText();
        if(!tag.isResource())
        {
        	if(tag.isEndTag() && 
        			(tag.getParent() != null 
        			&& tag.getParent().isResource()))
        	{
		        result.append('[');
		        result.append(text);
		        result.append(']');
        	}
        	else
        	{
        		result.append('<');
		        result.append(text);
		        result.append('>');
        	}
        }
        else 
        {
        	if(tag instanceof RemarkNode)
        	{
        		result.append('<');
		        result.append(text);
		        result.append('>');
        	}
        	else
        	{	
	        	result.append('[');
		        result.append(text);
		        result.append(']');
        	}
        }
        return result.toString();
    }

    /**
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#process(java.lang.String, java.lang.String)
     */
    public String process(String html, String encoding) throws ParserException {
    	if(html == null || html.length() == 0)
    		return "";
        m_result = new StringBuffer();
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        lexer.setJspTagAware(this);
        // initialize the page with the given charset
        Page page = new Page(html, encoding);
        lexer.setPage(page);
        parser.setLexer(lexer);

        if(CMSUtil.autoCloseHtmlTag())
        {
        	if (m_noAutoCloseTags != null && m_noAutoCloseTags.size() > 0) {
	            // Degrade Composite tags that do have children in the DOM tree 
	            // to simple single tags: This allows to finish this tag with openend HTML tags without the effect 
	            // that htmlparser will generate the closing tags. 
	            PrototypicalNodeFactory factory = configureNoAutoCorrectionTags();
	            lexer.setNodeFactory(factory);
	        }
        }
        else
        {
        	 PrototypicalNodeFactory factory = new PrototypicalNodeFactory(true);
	         lexer.setNodeFactory(factory);
        }

        // process the page using the given visitor
        parser.visitAllNodesWith(this);
        // 获取 the result
        String result = getResult();
        //对result中的标签进行校正处理
        
        return result;
    }
    
    public String process(File file, String encoding) throws ParserException
    {
    	InputStream in = null;
		try {
			
			if(file == null || !file.exists() || !file.isFile())
	    		return "";
			in = new FileInputStream(file);
	        m_result = new StringBuffer();
	        Parser parser = new Parser();
	        Lexer lexer = new Lexer();
	        lexer.setJspTagAware(this);
	        // initialize the page with the given charset
	        Page page = new Page(in, encoding);
	        lexer.setPage(page);
	        parser.setLexer(lexer);
	        if(CMSUtil.autoCloseHtmlTag())
	        {
		        if (m_noAutoCloseTags != null && m_noAutoCloseTags.size() > 0) {
		            // Degrade Composite tags that do have children in the DOM tree 
		            // to simple single tags: This allows to finish this tag with openend HTML tags without the effect 
		            // that htmlparser will generate the closing tags. 
		            PrototypicalNodeFactory factory = configureNoAutoCorrectionTags();
		            lexer.setNodeFactory(factory);
		        }
	        }
	        else
	        {
	        	 PrototypicalNodeFactory factory = new PrototypicalNodeFactory(true);
		         lexer.setNodeFactory(factory);
	        }
	        

	        // process the page using the given visitor
	        parser.visitAllNodesWith(this);
	        // 获取 the result
	        String result = getResult();
	        //对result中的标签进行校正处理
	        
	        return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return "";
		
	}

    /**
     * 
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#setConfiguration(java.lang.String)
     */
    public void setConfiguration(String configuration) {

        if (CmsStringUtil.isNotEmpty(configuration)) {
            m_configuration = configuration;
        }

    }

    /**
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#visitEndTag(org.htmlparser.Tag)
     */
    public void visitEndTag(Tag tag) {

        if (m_echo && !(tag instanceof BaseHrefTag)) {
            m_result.append(getTagHtml(tag));
        }
    }

    /**
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#visitRemarkNode(org.htmlparser.Remark)
     */
    public void visitRemarkNode(Remark remark) {

        if (m_echo) {
            m_result.append(remark.toHtml());
        }
    }

    /**
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#visitStringNode(org.htmlparser.Text)
     */
    public void visitStringNode(Text text) {

        if (m_echo) {
        	if(text.getParent() instanceof StyleTag)
        	{
        		
        		StyleTag style = (StyleTag)text.getParent();
        		if(style.changed())
        			m_result.append(style.getStyleCode());
        		else
        			m_result.append(text.getText());
        	}
        	else if(text.getParent() instanceof ResourceTag)
        	{
        		
        		ResourceTag style = (ResourceTag)text.getParent();
        		if(style.isChanged())
        			m_result.append(style.getResourceText());
        		else
        			m_result.append(text.getText());
        	}
        	else
        	{
        		m_result.append(text.getText());
        	}
        }
    }

    /**
     * @see com.frameworkset.platform.cms.driver.htmlconverter.I_CmsHtmlNodeVisitor#visitTag(org.htmlparser.Tag)
     */
    public void visitTag(Tag tag) {
    	
    	
        if (m_echo && !(tag instanceof BaseHrefTag)) {
        	if(tag instanceof StyleTag)
        	{
        		StyleTag style = (StyleTag)tag;
        		style.setStyleCode(style.getStyleCode());
        	}
            m_result.append(getTagHtml(tag));
        }
    }

    /**
     * Collapse HTML whitespace in the given String.
     * <p>
     * 
     * @param string the string to collapse
     * 
     * @return the input String with all HTML whitespace collapsed
     */
    protected String collapse(String string) {

        int len = string.length();
        StringBuffer result = new StringBuffer(len);
        int state = 0;
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
                // see HTML specification section 9.1 White space
                // http://www.w3.org/TR/html4/struct/text.html#h-9.1
                case '\u0020':
                case '\u0009':
                case '\u000C':
                case '\u200B':
                case '\r':
                case '\n':
                    if (0 != state) {
                        state = 1;
                    }
                    break;
                default:
                    if (1 == state) {
                        result.append(' ');
                    }
                    state = 2;
                    result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Returns a list of upper case tag names for which parsing / visiting will not correct missing closing tags.<p>
     * 
     * 
     * @return a List of upper case tag names for which parsing / visiting will not correct missing closing tags
     */
    public List getNoAutoCloseTags() {

        return m_noAutoCloseTags;
    }

    /**
     * Sets a list of upper case tag names for which parsing / visiting should not correct missing closing tags.<p> 
     * 
     * @param noAutoCloseTagList a list of upper case tag names for which parsing / visiting 
     *      should not correct missing closing tags to set.
     */
    public void setNoAutoCloseTags(List noAutoCloseTagList) {

        // ensuring upper case
//        m_noAutoCloseTags.clear();
//        if (noAutoCloseTagList != null) {
//            Iterator it = noAutoCloseTagList.iterator();
//            while (it.hasNext()) {
//                m_noAutoCloseTags.add(((String)it.next()).toUpperCase());
//            }
//        }
    	this.m_noAutoCloseTags = noAutoCloseTagList;
    }
    
    public static void main(String[] args)
    {
//    	String html = "<TR><style>.ccc {background:url(image)}</style>"
//			+"<TD width=5 height=5><IMG height=12 src=\"content_files/20073121014370.gif\" width=12></TD>"
//			+"<TD background=http://www.pconline.com.cn/product/images/productbg7_4.gif></TD>"
//			+"<TD width=12><IMG height=12 src=\"content_files/20073121014531.gif\" width=12></TD></TR>";
    	
    	String html = "<style>.titleStyle" +
"{" +
"/*background-color:#88aa88;" +
"   background-image:url(drag.gif);" +
"background-repeat:no-repeat;" +
"background-position:3% 60%;*/" +
"border-left:0px solid #FFFFF;" +
"border-top:0px solid #FFFFFF;" +
"border-right:0px solid #cccccc;" +
"border-bottom:0px solid #aaaaaa;" +
"font-size:11pt;" +
"cursor:hand;" +
"}</style>";
    	CmsHtmlParser htl = new CmsHtmlParser(true);
    	try {
			String result = htl.process(html,CmsEncoder.ENCODING_ISO_8859_1);
			System.out.println("input html: " + html);
			System.out.println("result html: " + result);
			
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
	public void setJspTagAware(boolean jspTagAware) {
		this.containJspTag = jspTagAware;		
	}
    
	/**
	 * 判断当前解析的代码是否包含jsp标签
	 * 包含则返回true，否则返回false
	 * @return
	 */
	public boolean containJspTag() {
		return containJspTag;
	}
}