package com.frameworkset.platform.cms.driver.config;

import java.util.Collection;

import javax.servlet.ServletContext;

import com.frameworkset.platform.cms.driver.dataloader.CMSDetailDataLoader;
import com.frameworkset.platform.cms.driver.publish.PublishEngine;
import com.frameworkset.platform.cms.driver.services.CMSService;
import com.frameworkset.platform.cms.driver.url.CMSURLParser;



public interface DriverConfiguration extends java.io.Serializable {
	/**
     * Initialization method used to place the driver
     * configuration into service.
     * @throws DriverConfigurationException when an error occurs during startup.
     * @param context
     */
    void init(ServletContext context) throws DriverConfigurationException;

    /**
     * Shutdown method used to remove the driver
     * configuration from service;
     * @throws DriverConfigurationException when an error occurs during shutdown.
     */
    void destroy() throws DriverConfigurationException;
    
    PublishEngine getPublishEngine();
    
//    CMSService getCMSService();

//
// Service / Configuration Methods
//

    /**
     * Retrieve the name of the portal
     * as should be returned in
     * {@link javax.portlet.PortalContext#getPortalInfo()}
     * @return the name of the portal.
     */
    String getCMSName();

    /**
     * Retrieve the version of the portal
     * as should be returned in
     * {@link javax.portlet.PortalContext#getPortalInfo()}
     * @return the portal version.
     */
    String getCMSVersion();

    /**
     * Retrieves the name of the container which
     * pluto should create and embed.
     * @return the container name.
     */
    String getContainerName();
    
    /**
     * 获取站点文档所有状态
     * @return Collection<DocumentStatus>
     */
    Collection getSupportedDocumentStatuses() throws DriverConfigurationException;
    
    /**
     * 获取站点文档可发布的状态
     * @return Collection<DocumentStatus>
     */
    Collection getSupportedSitePublishStatus(String siteid) throws DriverConfigurationException;
    
    /**
     * 获取频道文档可发布的状态
     * @return Collection<DocumentStatus>
     */
    Collection getSupportedChannelPublishStatus(String siteid,String channelid) throws DriverConfigurationException;

    Collection getSupportedWindowStates() throws DriverConfigurationException;
    
    Collection getSupportedSites() throws DriverConfigurationException;

    Collection getCMSApplications() throws DriverConfigurationException;
    
    CMSService getCMSService() throws DriverConfigurationException;
    
    /**
     * 判断状态是否为允许的发布状态
     * @param status
     * @return
     */
    boolean isDocumentStatusPublishedByChannel(String status) throws DriverConfigurationException;
    

//    CMSApplicationConfig getCMSApp(String id);
//
//    CMSWindowConfig getCMSWindowConfig(String id);

//    Collection getPages();

//    PageConfig getPageConfig(String pageId);
    
//    boolean isPortletModeSupportedByPortal(String mode);
//    
//    boolean isPortletModeSupportedByPortlet(String portletId, String mode);
//    
//    boolean isPortletModeSupported(String portletId, String mode);

//
// Utility methods for the container
//
//    CMSCallbackService getCMSCallbackService();
//
//    CMSPreferencesService getCMSPreferencesService();

//    CMSURLParser getCMSUrlParser();
    
    CMSDetailDataLoader getCMSCellDataLoader() throws DriverConfigurationException;

	CMSURLParser getCMSUrlParser();

//	/**
//	 * 获取站点的发布路径
//	 * @return
//	 */
//	String getPublishTemppath();

}
