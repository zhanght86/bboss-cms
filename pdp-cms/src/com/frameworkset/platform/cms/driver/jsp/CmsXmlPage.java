package com.frameworkset.platform.cms.driver.jsp;

public class CmsXmlPage implements java.io.Serializable {

    /** Name of the name attribute of the elements node. */
    public static final String ATTRIBUTE_ENABLED = "enabled";

    /** Name of the internal attribute of the link node. */
    public static final String ATTRIBUTE_INTERNAL = "internal";

    /** Name of the language attribute of the elements node. */
    public static final String ATTRIBUTE_LANGUAGE = "language";

    /** Name of the name attribute of the elements node. */
    public static final String ATTRIBUTE_NAME = "name";

    /** Name of the type attribute of the elements node. */
    public static final String ATTRIBUTE_TYPE = "type";

    /** Name of the anchor node. */
    public static final String NODE_ANCHOR = "anchor";

    /** Name of the element node. */
    public static final String NODE_CONTENT = "content";

    /** Name of the elements node. */
    public static final String NODE_ELEMENTS = "elements";

    /** Name of the link node. */
    public static final String NODE_LINK = "link";

    /** Name of the links node. */
    public static final String NODE_LINKS = "links";

    /** Name of the page node. */
    public static final String NODE_PAGE = "page";

    /** Name of the page node. */
    public static final String NODE_PAGES = "pages";

    /** Name of the query node. */
    public static final String NODE_QUERY = "query";

    /** Name of the target node. */
    public static final String NODE_TARGET = "target";

    /** Property to check if relative links are allowed. */
    public static final String PROPERTY_ALLOW_RELATIVE = "allowRelativeLinks";

//    /** The DTD address of the OpenCms xmlpage. */
//    public static final String XMLPAGE_XSD_SYSTEM_ID = CmsConfigurationManager.DEFAULT_DTD_PREFIX + "xmlpage.xsd";

  

    /** Name of the element node. */
    private static final String NODE_ELEMENT = "element";

    /** Indicates if relative Links are allowed. */
    private boolean m_allowRelativeLinks;

}
