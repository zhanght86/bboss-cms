package com.frameworkset.platform.cms.driver.htmlconverter;
import java.util.HashMap;
import java.util.Iterator;

import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;

/**
 * 
 * @author biaoping.yin[yin-bp@163.com]
 * @version Revision: 1.0.0 
 * @date Jan 15, 2009 10:03:23 AM 
 * @since 1.0.0
 */
public class CmsLinkTable {

    /** Prefix to identify a link in the content. */
    private static final String LINK_PREFIX = "link";

    /**
     *  The map to store the link table in.
     *  Map<CMSLink> 
     */
    private HashMap m_linkTable;

    /**
     * Creates a new CmsLinkTable.<p>
     */
    public CmsLinkTable() {

        m_linkTable = new HashMap();
    }
    
    HashMap getMLinkTable()
    {
    	return this.m_linkTable;
    }

    /**
     * Adds a new link with a given internal name and internal flag to the link table.<p>
     * 
     * @param link the <code>CMSLink</code> to add
     * @return the new link entry
     */
    public CMSLink addLink(CMSLink link) {
    	if(link.getOrigineLink() != null)
    	{
    		m_linkTable.put(link.getOrigineLink().getHref(), link);
    	}
    	else
    	{
    		m_linkTable.put(link.getHref(), link);
    	}
        return link;
    }

//    /**
//     * Adds a new link to the link table.<p>
//     * 
//     * @param type type of the link
//     * @param targetUri link destination
//     * @param internal flag to indicate if the link is a local link
//     * @return the new link entry
//     */
//    public CmsLink addLink(String type, String targetUri, boolean internal) {
//
//        CmsLink link = new CmsLink(LINK_PREFIX + m_linkTable.size(), type, targetUri, internal);
//        m_linkTable.put(link.getName(), link);
//        return link;
//    }

    /**
     * Returns the CmsLink Entry for a given name.<p>
     * 
     * @param name the internal name of the link
     * @return the CmsLink entry
     */
    public CMSLink getLink(String name) {

        return (CMSLink)m_linkTable.get(name);
    }

    /**
     * Returns if the link table is empty.<p>
     * 
     * @return true if the link table is empty, false otherwise
     */
    public boolean isEmpty() {

        return m_linkTable.isEmpty();
    }

    /**
     * Returns an iterator over the links in the table.<p>
     * 
     * The objects iterated are of type <code>{@link CmsLink}</code>.
     * 
     * @return a string iterator for internal link names
     */
    public Iterator iterator() {

        return m_linkTable.values().iterator();
    }

    /**
     * Returns the size of this link table.<p>
     * 
     * @return the size of this link table
     */
    public int size() {

        return m_linkTable.size();
    }

	public void destroy() {
		
		if(m_linkTable != null)
		{
			m_linkTable.clear();
			m_linkTable = null;
		}
	}

	public void addLinks(CmsLinkTable templateLinkTable) {
		this.m_linkTable.putAll(templateLinkTable.getMLinkTable());
		
	}
}
