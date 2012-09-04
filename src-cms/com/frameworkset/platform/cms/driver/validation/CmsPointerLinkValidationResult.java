package com.frameworkset.platform.cms.driver.validation;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.frameworkset.platform.cms.driver.i18n.CmsMessages;
import com.frameworkset.platform.cms.driver.i18n.Messages;



/**
 * Stores the result of a pointer link validation. <p>
 * 
 * @author Jan Baudisch 
 * 
 * @version $Revision: 1.7 $ 
 * 
 * @since 6.0.0 
 */
public class CmsPointerLinkValidationResult implements java.io.Serializable {

    /**  The broken links that were found.<p> */
    private Map m_brokenLinks;

    /**  The date of the validation.<p> */
    private Date m_validationDate;

    /**
     * Constructs a new pointer link validation result.<p>
     * 
     * @param brokenLinks a list of the broken links
     */
    public CmsPointerLinkValidationResult(Map brokenLinks) {

        m_brokenLinks = brokenLinks;
        m_validationDate = new Date();
    }

    /**
     * Returns a Html representation of this pointer link validation result.<p>
     * 
     * @param locale the Locale to display the result in
     * 
     * @return a Html representation of this external link validation result
     */
    public String toHtml(Locale locale) {

        CmsMessages mg = Messages.get().getBundle(locale);
        if (m_brokenLinks.size() > 0) {
            StringBuffer result = new StringBuffer(1024);
            Iterator brokenLinks = m_brokenLinks.entrySet().iterator();
            result.append(mg.key(Messages.GUI_LINK_VALIDATION_RESULTS_INTRO_1, new Object[] {m_validationDate})).append(
                "<ul>");
            while (brokenLinks.hasNext()) {
                Entry link = (Map.Entry)brokenLinks.next();
                String linkPath = (String)link.getKey();
                String linkUrl = (String)link.getValue();
                String msg = mg.key(Messages.GUI_LINK_POINTING_TO_2, new Object[] {linkPath, linkUrl});
                result.append("<li>").append(msg).append("</li>");
            }
            return result.append("</ul>").toString();
        } else {
            return mg.key(Messages.GUI_LINK_VALIDATION_RESULTS_ALL_VALID_1, new Object[] {m_validationDate});
        }
    }
}