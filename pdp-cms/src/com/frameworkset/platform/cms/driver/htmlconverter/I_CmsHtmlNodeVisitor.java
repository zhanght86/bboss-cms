package com.frameworkset.platform.cms.driver.htmlconverter;

import java.util.List;

import org.htmlparser.Remark;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.ParserException;

/**
 * 
 * Interface for a combination of a visitor of HTML documents along with the hook to start the
 * parser / lexer that triggers the visit.
 * <p>
 * 
 * 
 * 
 * @author biaoping.yin
 * 
 * @version $Revision: 1.3 $
 * 
 * @since 6.1.3
 * 
 */
public interface I_CmsHtmlNodeVisitor  {

    /**
     * Returns the configuartion String of this visitor or the empty String if was not provided
     * before.
     * <p>
     * 
     * @return the configuartion String of this visitor - by this contract never null but an empty
     *         String if not provided.
     * 
     * @see #setConfiguration(String)
     */
    String getConfiguration();

    /**
     * Returns the text extraction result.
     * <p>
     * 
     * @return the text extraction result
     */
    String getResult();
    
    /**
     * Extracts the text from the given html content, assuming the given html encoding.
     * <p>
     * 
     * @param html the content to extract the plain text from
     * @param encoding the encoding to use
     * 
     * @return the text extracted from the given html content
     * 
     * @throws ParserException if something goes wrong
     */
    String process(String html, String encoding) throws ParserException;

    /**
     * Set a configuartion String for this visitor.
     * <p>
     * 
     * This will most likely be done with data from an xsd, custom jsp tag, ...
     * <p>
     * 
     * @param configuration the configuration of this visitor to set.
     */
    void setConfiguration(String configuration);

    /**
     * Sets a list of upper case tag names for which parsing / visitng should not correct missing closing tags.<p> 
     * 
     * This has to be used before <code>{@link #process(String, String)}</code> is invoked to take an effect.<p>
     * 
     * @param noAutoCloseTags a list of upper case tag names for which parsing / visiting 
     *      should not correct missing closing tags to set.
     */
    void setNoAutoCloseTags(List noAutoCloseTags);

    /**
     * Visitor method (callback) invoked when a closing Tag is encountered.
     * <p>
     * 
     * @param tag the tag that is ended.
     * 
     * @see org.htmlparser.visitors.NodeVisitor#visitEndTag(org.htmlparser.Tag)
     */
    void visitEndTag(Tag tag);

    /**
     * Visitor method (callback) invoked when a remark Tag (HTML comment) is encountered.
     * <p>
     * 
     * @param remark the remark Tag to visit.
     * 
     * @see org.htmlparser.visitors.NodeVisitor#visitRemarkNode(org.htmlparser.Remark)
     */
    void visitRemarkNode(Remark remark);

    /**
     * 
     * Visitor method (callback) invoked when a remark Tag (HTML comment) is encountered.
     * <p>
     * 
     * @param text the text that is visited.
     * 
     * @see org.htmlparser.visitors.NodeVisitor#visitStringNode(org.htmlparser.Text)
     */
    void visitStringNode(Text text);
    
    /**
     * Visitor method (callback) invoked when a starting Tag (HTML comment) is encountered.
     * <p>
     * 
     * @param tag the tag that is visited.
     * 
     * @see org.htmlparser.visitors.NodeVisitor#visitTag(org.htmlparser.Tag)
     */
    void visitTag(Tag tag);

}