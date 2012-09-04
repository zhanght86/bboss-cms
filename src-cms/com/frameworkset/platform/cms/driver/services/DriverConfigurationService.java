package com.frameworkset.platform.cms.driver.services;

import javax.servlet.ServletContext;

import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;



public interface DriverConfigurationService extends java.io.Serializable {
	/**
     * Initialize the service for use by the driver.
     * This method allows the service to retrieve required
     * resources from the context and instantiate any required
     * services.
     *
     * @param ctx the Portal's servlet context in which the
     * service will be executing.
     *
     */
    void init(ServletContext ctx) throws DriverConfigurationException;

    /**
     * Destroy the service, notifying it of shutdown.
     */
    void destroy() throws DriverConfigurationException;

}
