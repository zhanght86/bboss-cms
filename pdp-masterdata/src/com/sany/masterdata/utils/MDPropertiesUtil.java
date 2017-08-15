/*
 * @(#)MasterDataPropertiesUtil.java
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.masterdata.utils;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

import com.sany.greatwall.MdmService;

/**
 * load master data properties
 * 
 * @author caix3
 * @since 2012-03-19
 */
public class MDPropertiesUtil {

    private static final String LDAP_CONFIG_PROPERTIES = "bboss-masterdata-humanResource.xml";

    private static BaseApplicationContext context = null;

    private static Logger logger = Logger.getLogger(MDPropertiesUtil.class);

    static {

        try {
            if (context == null) {
                context = DefaultApplicationContext.getApplicationContext(LDAP_CONFIG_PROPERTIES);
            }
        } catch (Exception e) {
            logger.error("get master data application context error", e);
        }
    }


    public static MdmService getMdmService()
    {
    	return context.getTBeanObject("mdmservice", MdmService.class);
    }

    public static String getPropertie(String id) {
        return context.getProperty(id);
    }
    
    public static Object getBeanObject(String beanId) {
        return context.getBeanObject(beanId);
    }
}
