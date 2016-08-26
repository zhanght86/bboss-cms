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
package com.frameworkset.platform.cms.driver.services.container;

import com.frameworkset.platform.cms.OptionalContainerServices;
import com.frameworkset.platform.cms.RequiredContainerServices;
import com.frameworkset.platform.cms.driver.config.DriverConfiguration;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.spi.optional.PortletEnvironmentService;

/**
 * The Portal Driver's <code>PortletContainerServices</code> implementation. The
 * <code>PortletContainerServices</code> interface is the main integration point
 * between the pluto container and the surrounding portal.
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 * @version 1.0
 * @since Sep 21, 2004
 */
public class ContainerServicesImpl
implements RequiredContainerServices, OptionalContainerServices {


    private CMSContext context;
    private DriverConfiguration driverConfig;


    /**
     * Default Constructor.
     */
    public ContainerServicesImpl(CMSContext context,
                                 DriverConfiguration driverConfig) {
        this.context = context;
        this.driverConfig = driverConfig;
    }

    /**
     * Standard Getter.
     * @return the CMS context for the CMS which we service.
     */
    public CMSContext getCMSContext() {
        return context;
    }

//    /**
//     * The PortletPreferencesService provides access to the CMS's
//     * PortletPreference persistence mechanism.
//     * @return a PortletPreferencesService instance.
//     */
//    public PreferencesService getPortletPreferencesService() {
//        return driverConfig.getPortletPreferencesService();
//    }

    /**
     * The CMSCallbackService allows the container to communicate
     * actions back to the CMS.
     * @return a CMSCallbackService implementation.
     */
//    public CMSCallbackService getCMSCallbackService() {
//        return driverConfig.getCMSCallbackService();
//    }

    public PortletEnvironmentService getPortletEnvironmentService() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

//    public PortletInvokerService getPortletInvokerService(InternalCMSWindow window) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
}

