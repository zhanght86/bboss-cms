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
 * Created on 2004-5-20
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 */
package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;

/**
 * To change for your class or interface
 *
 * @author biaoping.yin
 * @version
 * @modelguid {EE9B80C8-5F89-4B47-95F1-1FDE74D81CC3}
 */
public class SecurityConstants implements Serializable {

    //用户管理接口
    /** @modelguid {CF639625-7B07-4FE2-8383-B3D9BFF53F6A} */
    public final static String USER_MANAGEMENT_TYPE = "UserManagement";

    //角色管理接口
    /** @modelguid {F14A4A55-B3E3-48BD-A590-881AC3A4A14B} */
    public final static String ROLE_MANAGEMENT_TYPE = "RoleManagement";

    //用户组管理接口
    /** @modelguid {DC578F30-F198-4DF6-9984-A1F76EF7E5AA} */
    public final static String GROUP_MANAGEMENT_TYPE = "GroupManagement";
    
    //输入类型管理接口
    public final static String INPUTTYPE_MANAGEMENT_TYPE = "InputTypeManagement";

    //权限管理接口
    /** @modelguid {877CC649-9A41-463B-A6B8-D142DFF9A47D} */
    public final static String PERMISSION_MANAGEMENT_TYPE =
            "PermissionManagement";

    //资源管理接口
    /** @modelguid {3C811F1F-923D-40FB-B484-C6962614B0E3} */
    public final static String ENTRY_MANAGEMENT_TYPE = "ResourceManagement";

    //岗位管理接口
    public final static String JOB_MANAGEMENT_TYPE = "JobManagement";

    //操作管理接口
    public static final String OPER_MANAGEMENT_TYPE = "OperManagement";

    //机构管理接口
    public static final String ORG_MANAGEMENT_TYPE = "OrgManagement";
    
    //字典管理接口
    public static final String DICT_MANAGEMENT_TYPE = "DictManagement";
    //日志管理接口
	public static final String LOG_MANAGEMENT_TYPE = "LogManagement";

}
