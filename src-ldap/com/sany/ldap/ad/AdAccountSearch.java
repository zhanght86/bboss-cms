/*
 * @(#)AdSearch.java
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
package com.sany.ldap.ad;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
import com.sany.ldap.LdapBean;
import com.sany.ldap.LdapPropertiesUtil;
import com.sany.ldap.common.DESCipher;

/**
 * 用户搜索
 * @author caix3
 * @since 2012-03-12
 */
public class AdAccountSearch {

    private Logger logger = Logger.getLogger(AdAccountSearch.class);
    
    /**
     * 获取用户信息
     * @param userId
     * @param ldap
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("deprecation")
    public Map<String, String> getUserData(String userId, String ldap) throws Exception {
        
        //get ldap bean
        LdapBean ldapBean = LdapPropertiesUtil.getLdapBean(ldap);
        if (ldapBean == null) {
            throw new Exception("Ineffective Ldap Bean ID");       
        }
        
        Map<String, String> response = new HashMap<String, String>();
        LDAPConnection lc = new LDAPConnection();
        try {
            lc.connect(ldapBean.getUrl(), ldapBean.getPort());
            lc.bind(LDAPConnection.LDAP_V3, ldapBean.getSecurityId(), new DESCipher().decrypt(ldapBean.getSecurityPsw()));

            String filter = "(&(objectCategory=" + ldapBean.getSearchObjectCategory() + ")(cn=" + userId + "))";

            LDAPSearchResults lsrs = lc.search(ldapBean.getSearchDc(), LDAPConnection.SCOPE_SUB, filter, null, false);

            if (lsrs.hasMore()) {
                LDAPEntry ent = lsrs.next();
                
                response.put("distinguishedName", ent.getAttribute("distinguishedName").getStringValue());
                response.put("displayName", ent.getAttribute("displayName").getStringValue());
                response.put("pwdLastSet", ent.getAttribute("pwdLastSet").getStringValue());
            }
        } catch (Exception e) {
            logger.warn("getSearchDn error", e);
        } finally {
            if (lc.isConnected()) {
                try {
                    lc.disconnect();
                } catch (LDAPException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return response;
    }
}
