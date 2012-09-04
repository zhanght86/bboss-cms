/*
 * @(#)LdapBean.java
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

/**
 * LDAP Bean
 * @author caix3
 * @since 2012-03-10
 */
public class LdapBean {

    private String url;
    private int port;
    private String securityId;
    private String securityPsw;
    private String searchDc;
    private String searchObjectCategory;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public String getSecurityPsw() {
        return securityPsw;
    }

    public void setSecurityPsw(String securityPsw) {
        this.securityPsw = securityPsw;
    }

    public String getSearchDc() {
        return searchDc;
    }

    public void setSearchDc(String searchDc) {
        this.searchDc = searchDc;
    }

    public String getSearchObjectCategory() {
        return searchObjectCategory;
    }

    public void setSearchObjectCategory(String searchObjectCategory) {
        this.searchObjectCategory = searchObjectCategory;
    }

}
