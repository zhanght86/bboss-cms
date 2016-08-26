/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.frameworkset.platform.cms.driver.config.impl;

import java.util.Collection;

import javax.servlet.ServletContext;

import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfiguration;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.dataloader.CMSDetailDataLoader;
import com.frameworkset.platform.cms.driver.publish.PublishEngine;
import com.frameworkset.platform.cms.driver.services.CMSService;
import com.frameworkset.platform.cms.driver.services.impl.resource.CMSServiceImpl;
import com.frameworkset.platform.cms.driver.url.CMSURLParser;
import com.frameworkset.platform.cms.driver.url.impl.CMSURLParserImpl;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;

/**
 * Encapsulation of the Pluto Driver ResourceConfig.
 *
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 * @version 1.0
 * @since Sep 23, 2004
 */
public class DriverConfigurationImpl
    implements DriverConfiguration {
	 
//    private PortalURLParser portalUrlParser;
//    private PropertyConfigService propertyService;
//    private RegistryService registryService;
//    private RenderConfigService renderService;
    private CMSDetailDataLoader cmsCellDataLoader;
    private CMSService CMSService ;
    
    private CMSURLParser CMSURLParser; 
    
    private PublishEngine publishEngine;
    
//    private SupportedModesService supportedModesService;
//
//    // Container Services
//    private CMSCallbackService portalCallbackService;
//    private PortletPreferencesService portletPreferencesService;
    public DriverConfigurationImpl()
    {
    	
    }
    
    public DriverConfigurationImpl(PublishEngine publishEngine)
    {
    	this.publishEngine = publishEngine;
    }

//    public DriverConfigurationImpl(//PortalURLParser portalUrlParser,
////                                   PropertyConfigService propertyService,
////                                   RegistryService registryService,
////                                   RenderConfigService renderService,
////                                   CMSCallbackService portalCallback,
////                                   SupportedModesService supportedModesService
//                                   ,CMSCellDataLoader cmsCellDataLoader) {
////        this.portalUrlParser = portalUrlParser;
//        this.propertyService = propertyService;
//        this.registryService = registryService;
//        this.renderService = renderService;
////        cmsService = 
////        this.portalCallbackService = portalCallback;
////        this.supportedModesService = supportedModesService;
//    }

    /**
     * Standard Getter.
     * @return the name of the portal.
     */
    public String getCMSName() {
//        return propertyService.getCMSName();
    	return "";
    }

    /**
     * Standard Getter.
     * @return the portal version.
     */
    public String getCMSVersion() {
//        return propertyService.getCMSVersion();
    	return "";
    }

    /**
     * Standard Getter.
     * @return the name of the container.
     */
    public String getContainerName() {
//        return propertyService.getContainerName();
    	return "";
    }

    /**
     * Standard Getter.
     * @return the names of the supported portlet modes.
     */
    public Collection getSupportedCMSModes() {
//        return propertyService.getSupportedCMSModes();
    	return null;
    }

    /**
     * Standard Getter.
     * @return the names of the supported window states.
     */
    public Collection getSupportedWindowStates() {
//        return propertyService.getSupportedWindowStates();
    	return null;
    }

//    /**
//     * Standard Getter.
//     * @return the configuration data of all configured portlet applications.
//     */
//    public Collection getPortletApplications() {
//        return registryService.getPortletApplications();
//    }

//   /**
//     * Retrieve the portlet application with the given id.
//     * @param id the id of the portlet application.
//     * @return the portlet application configuration data.
//     */
//    public PortletApplicationConfig getPortletApp(String id) {
//        return registryService.getPortletApplication(id);
//    }

//    /**
//     * Retrieve the window configuration associated with the given id.
//     * @param id the id of the portlet window.
//     * @return the portlet window configuration data.
//     */
//    public PortletWindowConfig getPortletWindowConfig(String id) {
//        return registryService.getPortlet(id);
//    }

    /**
     * Standard Getter.
     * @return the render configuration.
     */
    public Collection getPages() {
//        return renderService.getPages();
    	return null;
    }

//    public PageConfig getPageConfig(String pageId) {
//        return renderService.getPage(pageId);
//    }
//    
//    public boolean isPortletModeSupportedByPortal(String mode) {
//        return supportedModesService.isPortletModeSupportedByPortal(mode);
//    }
    
//    public boolean isPortletModeSupportedByPortlet(String portletId, String mode) {
//        return supportedModesService.isPortletModeSupportedByPortlet(portletId, mode);
//    }
//    
//    public boolean isPortletModeSupported(String portletId, String mode) {
//        return supportedModesService.isPortletModeSupported(portletId, mode);
//    }

    public void init(ServletContext context) throws DriverConfigurationException {
//        this.propertyService.init(context);
//        this.registryService.init(context);
//        this.renderService.init(context);
    }

    public void destroy() throws DriverConfigurationException {
//        if(propertyService != null)
//            propertyService.destroy();
//
//        if(registryService != null)
//            registryService.destroy();
//
//        if(renderService != null)
//            renderService.destroy();
    }

	public Collection getCMSApplications() {
		// TODO Auto-generated method stub
		return null;
	}  

	public  CMSService getCMSService() {
		
		
		if(CMSService == null)
		{
			CMSService = new CMSServiceImpl();
//			services.put(AttributeKeys.CMSSERVICKEY,CMSService);
		}
		
		return CMSService;
	}

	public Collection getSupportedDocumentStatuses() {
		return null;//DocumentStatus.SUPPORTSTATUSES;
	}

	public Collection getSupportedSitePublishStatus(String siteid) throws DriverConfigurationException {
		// TODO Auto-generated method stub
		try {
			return this.getCMSService().getSiteManager().getEnablePublishStatus(siteid);
		} catch (SiteManagerException e) {
			throw new DriverConfigurationException(e.getMessage());
		}
	}

	public Collection getSupportedChannelPublishStatus(String siteid, String channelid) throws DriverConfigurationException {
		try {
			return this.getCMSService().getChannelManager().getSupportedChannelPublishStatus(siteid);
		} catch (ChannelManagerException e) {
			throw new DriverConfigurationException(e.getMessage());
		}
	}
	
	/**
	 * 获取内容管理系统中的所有站点
	 */
	public Collection getSupportedSites() {
		
		return null;
	}

	/**
	 * 判断文档的状态是否可以发布
	 */
	public boolean isDocumentStatusPublishedByChannel(String status) {
		
		return false;
	}

	public CMSDetailDataLoader getCmsCellDataLoader() {
		return cmsCellDataLoader;
	}

	public void setCmsCellDataLoader(CMSDetailDataLoader cmsCellDataLoader) {
		this.cmsCellDataLoader = cmsCellDataLoader;
	}

	public CMSDetailDataLoader getCMSCellDataLoader() {
		
		return cmsCellDataLoader;
	}

	
	public CMSURLParser getCMSUrlParser() {
		
		return CMSURLParserImpl.getParser();
	}

	public PublishEngine getPublishEngine() {
		
		return this.publishEngine;
	}

//	public CMSURLParser getCMSURLParser() {
//		return CMSURLParserImpl.getParser();
//	}

	

// 
// Portal Driver Services
//

//    public PortalURLParser getPortalUrlParser() {
//        return portalUrlParser;
//    }
//
//    public void setPortalUrlParser(PortalURLParser portalUrlParser) {
//        this.portalUrlParser = portalUrlParser;
//    }       

//
// Container Services
////
//    public CMSCallbackService getPortalCallbackService() {
//        return portalCallbackService;
//    }
//
//    public void setPortalCallbackService(CMSCallbackService portalCallbackService) {
//        this.portalCallbackService = portalCallbackService;
//    }
//
//    public PortletPreferencesService getPortletPreferencesService() {
//        return portletPreferencesService;
//    }

//    public void setPortletPreferencesService(PortletPreferencesService portletPreferencesService) {
//        this.portletPreferencesService = portletPreferencesService;
//    }
}

