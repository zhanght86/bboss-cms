/*
 *
 * Title:
 *
 * Copyright: Copyright (c) 2004
 *
 * Company: iSany Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-8-11
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 */
package com.frameworkset.platform.sysmgrcore.manager;

/**
 * 在系统启动时将安全接口绑定到JNDI
 *
 * @author biaoping.yin
 * @version 1.0
 */
import java.io.Serializable;

import javax.naming.Context;

/** @modelguid {938C4FDD-B9AD-4DD9-A504-D491760EE957} */
public class SecurityStartup implements Serializable {

    /** @modelguid {4E23E490-D91D-49ED-A0E6-075C1005C3D5} */
    private static String result = "Security interface bind sucessful...";

    /**
     *  Description:初始化底层组件
     * @param arg0
     * @param arg1
     * @return
     * @throws java.lang.Exception
     * @see weblogic.common.T3StartupDef#startup(java.lang.String, java.util.Hashtable)
     * @modelguid {CB8FFA63-085F-4291-85D4-9CEF48939429}
     */
    public static String startup(Context context) throws Exception {
        System.out.println(
                "=============== Starting Bind Security Manager Instance ===============");

        SecurityDatabase.bindManager();
        System.out.println(
                "=============== Finished  Bind Security Manager Instance ===============");
        return result;
    }

    /**
     *  Description:初始化底层组件
     * @param arg0
     * @param arg1
     * @return
     * @throws java.lang.Exception
     * @see weblogic.common.T3StartupDef#startup(java.lang.String, java.util.Hashtable)
     * @modelguid {D3A2EFD6-44E0-4BAF-A3B5-B23E3571C336}
     */
    public static String destroy(Context context) throws Exception {
        System.out.println(
                "=============== Starting Bind Security Manager Instance ===============");
        //SecurityDatabase securityDatabase = new SecurityDatabase();
        //SecurityDatabase.init(context);
        SecurityDatabase.destoryManager();
        System.out.println(
                "=============== Finished  Bind Security Manager Instance ===============");
        return result;
    }
}
