/*
 * @(#)AdAccountBind.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.ldap.ad;

import java.util.Map;

import org.apache.log4j.Logger;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import com.sany.ldap.LdapBean;
import com.sany.ldap.LdapPropertiesUtil;

/**
 * AD登录认证
 * @author caix3
 * @since 2012-03-12
 */
public class AdAccountLogin {

    private Logger logger = Logger.getLogger(AdAccountLogin.class);
    
    /**
     * 检验用户名密码是否正确
     * @param userId
     * @param password
     * @param ldap
     * @return
     */
    @SuppressWarnings("deprecation")
    public Map<String, String> validateUser(String userId, String password, String ldap) {
        
        LDAPConnection lc = new LDAPConnection();
        Map<String, String> response = null;
        try {
            // get account dn
            AdAccountSearch search = new AdAccountSearch();
            response = search.getUserData(userId, ldap);

            if (response.get("distinguishedName") != null && !response.get("distinguishedName").equals("")) {

                LdapBean ldapBean = LdapPropertiesUtil.getLdapBean(ldap);
                lc.connect(ldapBean.getUrl(), ldapBean.getPort());
                String dn = response.get("distinguishedName");
                lc.bind(LDAPConnection.LDAP_V3, response.get("distinguishedName"), password);//如果口令为空，则会导致转化为匿名用户
                if(lc.getAuthenticationDN() != null && lc.getAuthenticationDN().equals(dn))                	
                	response.put("successFlag", "1");
                else
                {
                	 response.put("successFlag", "0");
                	 response.put("errMessage", new StringBuilder().append("Invalid credentials:required ").append(dn).append(",but reponse [").append(lc.getAuthenticationDN()).append("]").toString());
                }
            } else {
                throw new Exception("Ineffective account id");
            }

        } catch (Exception e) {
        	if(!userId.equals("admin"))
        		logger.debug("validate user error", e);
            
            response.put("successFlag", "0");
            if (e.getMessage().contains("52e")) { response.put("errMessage", "Invalid credentials"); }
            else if (e.getMessage().contains("530")) { response.put("errMessage", "Not permitted to logon at this time"); }
            else if (e.getMessage().contains("531")) { response.put("errMessage", "Not permitted to logon from this workstation"); }
            else if (e.getMessage().contains("532")) { response.put("errMessage", "Password Expires"); }
            else if (e.getMessage().contains("533")) { response.put("errMessage", "Account disabled"); }
            else if (e.getMessage().contains("701")) { response.put("errMessage", "Account Expires"); }
            else if (e.getMessage().contains("773")) { response.put("errMessage", "User must reset password"); }
            else if (e.getMessage().contains("775")) { response.put("errMessage", "Account locked out"); }
            else { response.put("errMessage", e.getMessage()); }
            
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
    
    public static void main(String[] args) {
        AdAccountLogin test = new AdAccountLogin();
        System.out.println(test.validateUser("liwp8", "69hfT7Y#", null));
    }
}
