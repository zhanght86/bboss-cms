/*
 * @(#)DaoHandler.java
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
package com.sany.masterdata.hr.dao;

import org.frameworkset.spi.DefaultApplicationContext;

import com.frameworkset.common.poolman.ConfigSQLExecutor;

/**
 * Abstract DAO Handler. All handler should be extends from this class for simples the operation.
 * user for human resource master data DAO.
 * @author caix3
 * @since 2012-03-21
 */
public abstract class DaoHandler {

    protected DefaultApplicationContext context = DefaultApplicationContext
            .getApplicationContext("bboss-masterdata-humanResource.xml");

    protected ConfigSQLExecutor executor = context.getTBeanObject("masterdata.hr.configSqlExecutor",
            ConfigSQLExecutor.class);

    // protected ConfigSQLExecutor executor;

    public ConfigSQLExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ConfigSQLExecutor executor) {
        this.executor = executor;
    }
}
