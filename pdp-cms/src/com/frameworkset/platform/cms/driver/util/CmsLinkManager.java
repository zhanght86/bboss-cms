package com.frameworkset.platform.cms.driver.util;

import java.net.MalformedURLException;
import java.net.URL;

import com.frameworkset.platform.cms.driver.validation.CmsPointerLinkValidationResult;
////import org.opencms.file.CmsObject;
////import org.opencms.file.types.CmsResourceTypeImage;
//import org.opencms.main.CmsException;
//import org.opencms.main.CmsLog;
//import org.opencms.main.OpenCms;
//import org.opencms.site.CmsSite;
//import org.opencms.site.CmsSiteManager;
//import org.opencms.site.CmsSiteMatcher;
//import org.opencms.staticexport.CmsStaticExportManager;
//import org.opencms.staticexport.Messages;
//import org.opencms.util.CmsStringUtil;
//import org.opencms.validation.CmsPointerLinkValidationResult;
//import org.opencms.workplace.CmsWorkplace;

/**
 * Does the link replacement for the &lg;link&gt; tags.<p> 
 *
 * Since this functionality is closely related to the static export,
 * this class resides in the static export package.<p>
 *
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.62 $ 
 * 
 * @since 6.0.0 
 */
public class CmsLinkManager implements java.io.Serializable {

//    /** The log object for this class. */
//    private static final Log LOG = CmsLog.getLog(CmsLinkManager.class);

    /** Base URL to calculate absolute links. */
    private static URL m_baseUrl;

    /** Stores the results of a extern link validation. */
    private CmsPointerLinkValidationResult m_pointerLinkValidationResult;

    /**
     * Public constructor.<p>
     */
    public CmsLinkManager() {

        // empty
    }

    /**
     * Static initializer for the base URL.<p>
     */
    static {
        m_baseUrl = null;
        try {
            m_baseUrl = new URL("http://127.0.0.1");
        } catch (MalformedURLException e) {
            // this won't happen
        }
    }

    /**
     * Calculates the absolute uri for the "relativeUri" with the given absolute "baseUri" as start. <p> 
     * 
     * If "relativeUri" is already absolute, it is returned unchanged.
     * This method also returns "relativeUri" unchanged if it is not well-formed.<p>
     *    
     * @param relativeUri the relative uri to calculate an absolute uri for
     * @param baseUri the base uri, this must be an absolute uri
     * @return an absolute uri calculated from "relativeUri" and "baseUri"
     */
    public static String getAbsoluteUri(String relativeUri, String baseUri) {

        if ((relativeUri == null) || (relativeUri.length() >= 1 && relativeUri.charAt(0) == '/')) {
            // uri is null or already absolute
            return relativeUri;
        }
        try {
            URL url = new URL(new URL(m_baseUrl, baseUri), relativeUri);
            if (url.getQuery() == null) {
                return url.getPath();
            } else {
                StringBuffer result = new StringBuffer(url.getPath().length() + url.getQuery().length() + 2);
                result.append(url.getPath());
                result.append('?');
                result.append(url.getQuery());
                return result.toString();
            }
        } catch (MalformedURLException e) {
            return relativeUri;
        }
    }

    /**
     * Calculates a realtive uri from "fromUri" to "toUri",
     * both uri's must be absolute.<p>
     * 
     * @param fromUri the uri to start
     * @param toUri the uri to calculate a relative path to
     * @return a realtive uri from "fromUri" to "toUri"
     */
    public static String getRelativeUri(String fromUri, String toUri) {

        StringBuffer result = new StringBuffer();
        int pos = 0;

        while (true) {
            int i = fromUri.indexOf('/', pos);
            int j = toUri.indexOf('/', pos);
            if ((i == -1) || (i != j) || !fromUri.regionMatches(pos, toUri, pos, i - pos)) {
                break;
            }
            pos = i + 1;
        }

        // count hops up from here to the common ancestor
        for (int i = fromUri.indexOf('/', pos); i > 0; i = fromUri.indexOf('/', i + 1)) {
            result.append("../");
        }

        // append path down from common ancestor to there
        result.append(toUri.substring(pos));

        if (result.length() == 0) {
            // special case: relative link to the parent folder from a file in that folder
            result.append("./");
        }

        return result.toString();
    }

   

    /**
     * Returns the result of the last extern link validation.<p>
     * 
     * @return the result of the last extern link validation
     */
    public CmsPointerLinkValidationResult getPointerLinkValidationResult() {

        return m_pointerLinkValidationResult;
    }

    /**
     * Sets the result of a extern link validation.<p>
     * 
     * @param externLinkValidationResult the result a extern link validation
     */
    public void setPointerLinkValidationResult(CmsPointerLinkValidationResult externLinkValidationResult) {

        m_pointerLinkValidationResult = externLinkValidationResult;
    }

   
//
//    /**
//     * Substitutes the contents of a link by adding the context path and 
//     * servlet name, and in the case of the "online" project also according
//     * to the configured static export settings.<p>
//     * 
//     * A server prefix is prepended if
//     * <ul>
//     *   <li>the link points to another site</li>
//     *   <li>the link is contained in a normal document and the link references a secure document</li>
//     *   <li>the link is contained in a secure document and the link references a normal document</li>
//     * </ul>
//     * 
//     * @param cms the cms context
//     * @param link the link to process (must be a valid link to a VFS resource with optional parameters)
//     * @param siteRoot the site root of the link
//     * @param forceSecure if <code>true</code> generates always an absolute url (with protocoll and server name) for secure links
//     * 
//     * @return the substituted link
//     */
//    public String substituteLink(CmsObject cms, String link, String siteRoot, boolean forceSecure) {
//
//        if (CmsStringUtil.isEmpty(link)) {
//            // not a valid link parameter, return an empty String
//            return "";
//        }
//        // make sure we have an absolute link        
//        String absoluteLink = CmsLinkManager.getAbsoluteUri(link, cms.getRequestContext().getUri());
//
//        String vfsName;
//        String parameters;
//        int pos = absoluteLink.indexOf('?');
//        // check if the link has parameters, if so cut them
//        if (pos >= 0) {
//            vfsName = absoluteLink.substring(0, pos);
//            parameters = absoluteLink.substring(pos);
//        } else {
//            vfsName = absoluteLink;
//            parameters = null;
//        }
//
//        String resultLink = null;
//        String uriBaseName = null;
//        boolean useRelativeLinks = false;
//
//        // determine the target site of the link        
//        CmsSite targetSite;
//        if (CmsStringUtil.isNotEmpty(siteRoot)) {
//            targetSite = CmsSiteManager.getSite(siteRoot);
//        } else {
//            targetSite = CmsSiteManager.getCurrentSite(cms);
//        }
//        String serverPrefix = "";
//        // if the link points to another site, there needs to be a server prefix
//        if (targetSite != CmsSiteManager.getCurrentSite(cms)) {
//            serverPrefix = targetSite.getUrl();
//        }
//
//        if (cms.getRequestContext().currentProject().isOnlineProject()) {
//
//            CmsStaticExportManager exportManager = OpenCms.getStaticExportManager();
//            // check if we need relative links in the exported pages
//            if (exportManager.relativeLinksInExport(cms.getRequestContext().getSiteRoot()
//                + cms.getRequestContext().getUri())) {
//                // try to get base uri from cache  
//                uriBaseName = exportManager.getCachedOnlineLink(exportManager.getCacheKey(
//                    cms.getRequestContext().getSiteRoot(),
//                    cms.getRequestContext().getUri()));
//                if (uriBaseName == null) {
//                    // base not cached, check if we must export it
//                    if (exportManager.isExportLink(cms, cms.getRequestContext().getUri())) {
//                        // base uri must also be exported
//                        uriBaseName = exportManager.getRfsName(cms,
//                        //cms.getRequestContext().getSiteRoot(),
//                            cms.getRequestContext().getUri());
//                    } else {
//                        // base uri dosn't need to be exported
//                        uriBaseName = exportManager.getVfsPrefix() + cms.getRequestContext().getUri();
//                    }
//                    // cache export base uri
//                    exportManager.cacheOnlineLink(exportManager.getCacheKey(
//                        cms.getRequestContext().getSiteRoot(),
//                        cms.getRequestContext().getUri()), uriBaseName);
//                }
//                // use relative links only on pages that get exported
//                useRelativeLinks = uriBaseName.startsWith(OpenCms.getStaticExportManager().getRfsPrefix(
//                    cms.getRequestContext().getSiteRoot() + cms.getRequestContext().getUri()));
//            }
//
//            // check if we have the absolute vfs name for the link target cached
//            resultLink = exportManager.getCachedOnlineLink(cms.getRequestContext().getSiteRoot() + ":" + absoluteLink);
//            if (resultLink == null) {
//                cms.getRequestContext().saveSiteRoot();
//                cms.getRequestContext().setSiteRoot(targetSite.getSiteRoot());
//                // didn't find the link in the cache
//                if (exportManager.isExportLink(cms, vfsName)) {
//                    // export required, get export name for target link
//                    resultLink = exportManager.getRfsName(cms, vfsName, parameters);
//                    // now set the parameters to null, we do not need them anymore
//                    parameters = null;
//                } else {
//                    // no export required for the target link
//                    resultLink = exportManager.getVfsPrefix().concat(vfsName);
//                    // add cut off parameters if required
//                    if (parameters != null) {
//                        resultLink = resultLink.concat(parameters);
//                    }
//                }
//                cms.getRequestContext().restoreSiteRoot();
//                // cache the result
//                exportManager.cacheOnlineLink(cms.getRequestContext().getSiteRoot() + ":" + absoluteLink, resultLink);
//            }
//
//            // read only properties, if the current site and the target site both do have a secure server
//            if (targetSite.hasSecureServer() || CmsSiteManager.getCurrentSite(cms).hasSecureServer()) {
//                if (!vfsName.startsWith(CmsWorkplace.VFS_PATH_SYSTEM)) {
//
//                    int linkType = -1;
//                    try {
//                        // read the linked resource 
//                        linkType = cms.readResource(vfsName).getTypeId();
//                    } catch (CmsException e) {
//                        // the resource could not be read
//                        if (LOG.isInfoEnabled()) {
//                            String message = Messages.get().getBundle().key(
//                                Messages.LOG_RESOURCE_ACESS_ERROR_3,
//                                vfsName,
//                                cms.getRequestContext().currentUser().getName(),
//                                cms.getRequestContext().getSiteRoot());
//                            if (LOG.isDebugEnabled()) {
//                                LOG.debug(message, e);
//                            } else {
//                                LOG.info(message);
//                            }
//                        }
//                    }
//
//                    // images are always referenced without a server prefix
//                    if (linkType != CmsResourceTypeImage.getStaticTypeId()) {
//                        // check the secure property of the link
//                        boolean secureLink = exportManager.isSecureLink(cms, vfsName, targetSite.getSiteRoot());
//                        boolean secureRequest = exportManager.isSecureLink(cms, cms.getRequestContext().getUri());
//                        // if we are on a normal server, and the requested resource is secure, 
//                        // the server name has to be prepended                        
//                        if (secureLink && (forceSecure || !secureRequest)) {
//                            serverPrefix = targetSite.getSecureUrl();
//                        } else if (!secureLink && secureRequest) {
//                            serverPrefix = targetSite.getUrl();
//                        }
//                    }
//                }
//            }
//            // make absolute link relative, if relative links in export are required
//            // and if the link does not point to another server
//            if (useRelativeLinks && CmsStringUtil.isEmpty(serverPrefix)) {
//                resultLink = getRelativeUri(uriBaseName, resultLink);
//            }
//
//        } else {
//
//            // offline project, no export required
//            if (OpenCms.getRunLevel() >= OpenCms.RUNLEVEL_3_SHELL_ACCESS) {
//                // in unit test this code would fail otherwise
//                resultLink = OpenCms.getStaticExportManager().getVfsPrefix().concat(vfsName);
//            }
//
//            // add cut off parameters and return the result
//            if (parameters != null) {
//                resultLink = resultLink.concat(parameters);
//            }
//
//        }
//        return serverPrefix.concat(resultLink);
//    }

}