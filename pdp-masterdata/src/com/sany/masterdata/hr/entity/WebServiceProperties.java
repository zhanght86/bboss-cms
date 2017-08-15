/*
 * @(#)WebServiceProperties.java
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
package com.sany.masterdata.hr.entity;

/**
 * Web Services Properties entity
 * @author caix3
 * @since 2012-03-20
 */
public class WebServiceProperties {

    private String url;

    private String name;

    private String nameSpace;

    private int pageSize;

    public String getName() {
        return name;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
