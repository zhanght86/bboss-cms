package com.frameworkset.platform.cms.driver.url;

//import javax.portlet.PortletMode;
//import javax.portlet.WindowState;
import java.util.Collection;
import java.util.Map;

import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.publish.PublishMode;

/**
 * Created by IntelliJ IDEA.
 * User: ddewolf
 * Date: Sep 4, 2006
 * Time: 5:17:34 PM 
 * To change this template use File | Settings | File Templates.
 */
public interface CMSURL extends Cloneable,java.io.Serializable {
    void setRenderPath(String renderPath);

    String getRenderPath();

    void addParameter(CMSURLParameter param);

    Collection getParameters();

    void setActionWindow(String actionWindow);

    String getActionWindow();

    /**
     * 当前页面上所有jsplet的publishMode
     * @return Map<windowid,PublishMode>
     */
    Map getPublishModes();

//    PortletMode getPortletMode(String windowId);
//
//    void setPortletMode(String windowId, PortletMode portletMode);

    Map getWindowStates();

//    WindowState getWindowState(String windowId);
//
//    void setWindowState(String windowId, WindowState windowState);

    void clearParameters(String windowId);

    String toString();

    String getServerURI();

    String getServletPath();

    Object clone();

	void setPublishMode(String windowid, PublishMode mode);

	void setDocumentStatus(String windowid, DocumentStatus state);
    
}
