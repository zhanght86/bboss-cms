package com.frameworkset.platform.cms.driver.htmlconverter;

import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;

/**
 * A <code>{@link Div} </code> for flat parsing (vs. nested) which is misued for avoiding the creation of 
 * the corresponding end tag in case the html to parse is not balanced.<p>
 * 
 * @author Achim Westermann
 * 
 * @version $Revision: 1.2 $
 * 
 * @since 6.2.2
 *
 */
public class DivTag extends TagNode implements java.io.Serializable {

    /** 
     * Mimick the same behviour (except nesting of tags) as the tag this one replaces. Caution this field has to be 
     * static or NPE will happen (getIds is called earlier). 
     */
    private static Div m_mimicked = new Div();

    /** Generated serial version UID. */
    private static final long serialVersionUID = -6409422683628200225L;

    /**
     * @see org.htmlparser.nodes.TagNode#getEnders()
     */
    public String[] getEnders() {

        return m_mimicked.getEnders();
    }

    /**
     * @see org.htmlparser.nodes.TagNode#getIds()
     */
    public String[] getIds() {

        return m_mimicked.getIds();
    }

}
