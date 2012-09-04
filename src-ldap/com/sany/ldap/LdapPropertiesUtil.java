/*
 * @(#)LdapPropertiesUtil.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.ldap;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

/**
 * 读取LDAP配置
 * 
 * @author caix3
 * @since 2012-03-10
 */
public class LdapPropertiesUtil {

    private static final String LDAP_CONFIG_PROPERTIES = "property-ldap.xml";

    private static BaseApplicationContext ldapContext = null;

    private static Logger logger = Logger.getLogger(LdapPropertiesUtil.class);

    static {

        try {
            if (ldapContext == null) {
                ldapContext = DefaultApplicationContext.getApplicationContext(LDAP_CONFIG_PROPERTIES);
            }
        } catch (Exception e) {
            logger.error("get ldap application context error", e);
        }
    }

    public static LdapBean getLdapBean(String beanId) {
        
        if (beanId == null || !beanId.equals("")) {
            beanId = "default";
        }
        
        return ldapContext.getTBeanObject(beanId, LdapBean.class);
    }
}
