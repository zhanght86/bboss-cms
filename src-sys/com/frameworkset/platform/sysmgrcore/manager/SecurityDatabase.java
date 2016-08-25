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
 * Created on 2004-5-19
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 */
package com.frameworkset.platform.sysmgrcore.manager;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.SPIException;


/**
 * 为PMIP底层安全机制提供数据及服务支持
 *
 * @author biaoping.yin
 * @version 1.0
 * @modelguid {20A4C33F-4AB6-4B13-8540-9B2D8AE3C554}
 */
public class SecurityDatabase  {
	private static BaseApplicationContext ioc = null;
	static 
	{
		ioc = DefaultApplicationContext.getApplicationContext("manager-sys.xml");
	}

    /** @modelguid {18CAAF39-FDE0-416C-93CA-3CF6CB5008A3} */

    /**
     * added by biaoping.yin on 2004/11/10
     * 用户管理接口
     * @modelguid {AFCE68DF-688D-4F19-8AEF-ECF75C2DD297}
     */


//    private static Map userManagers = java.util.Collections.synchronizedMap(new
//            java.util.WeakHashMap());
//    ;
//    protected static Map providers = java.util.Collections.synchronizedMap(new
//            java.util.WeakHashMap());
//
//
//
//
//
//    /**
//     * 缺省接口key
//     */
//    public static final String DEFAULT_CACHE_KEY = "DEFAULT_CACHE_KEY";
//
//    /**
//     * 同步缓冲key
//     */
//
//    public static final String SYNCHRO_CACHE_KEY = "SYNCHRO_CACHE_KEY";

//    private static Map userGroupManagers = java.util.Collections.
//                                           synchronizedMap(new java.util.
//            WeakHashMap());



//    private static Map roleManagers = java.util.Collections.synchronizedMap(new
//            java.util.WeakHashMap());

//    private static Map jobManagers = java.util.Collections.synchronizedMap(new
//            java.util.WeakHashMap());


    /**
     * added by biaoping.yin on 2004/11/10
     * 资源管理接口
     * @modelguid {3D5C75CA-1248-4CD0-AAB7-0D8085BA9E0F}
     */

//    private static Map resourceManagers = java.util.Collections.synchronizedMap(new
//            java.util.WeakHashMap());



//    private static Map orgManagers = java.util.Collections.synchronizedMap(new
//            java.util.WeakHashMap());
//
//    private static Map operManagers = java.util.Collections.synchronizedMap(new
//            java.util.WeakHashMap());

//	/**
//	 * added by biaoping.yin on 2004/11/10
//	 * 权限许可管理接口
//	 * @modelguid {E27FDEF8-5AAB-4F04-8CC1-6E62CAED3B3B}
//	 */
//	private static PermissionManagement permissionManager;

//	/**
//	 * added by biaoping.yin on 2004/11/10
//	 * 系统环境上下文变量
//	 */
//	static Context context = null;

    /**构造方法
     *
     * @modelguid {B672D771-1723-4A2B-855D-DD2F64BE51AF}
     */
    public SecurityDatabase() {
        super();

    }

    /**
     * Description:获取用户管理接口实例
     * @return
     * UserManagement
     * @modelguid {40C65BD7-29FB-4D2A-9F05-2709F9EF405E}
     */
    public static UserManager getUserManager() throws SPIException {
//        UserManager userManager = (UserManager) getProvider(userManagers,SecurityConstants.USER_MANAGEMENT_TYPE);
        UserManager userManager = (UserManager) ioc.getBeanObject(SecurityConstants.USER_MANAGEMENT_TYPE);
        return userManager;
    }



    /**
     * Description:获取用户组管理接口实例
     * @return
     * GroupManagement
     * @modelguid {CEA3B71E-4A0A-49E0-80FE-7E1F015816A8}
     */
    public static GroupManager getGroupManager() throws SPIException {
        GroupManager userGroupManager = (GroupManager) ioc.getBeanObject(SecurityConstants.GROUP_MANAGEMENT_TYPE);
        return userGroupManager;
    }



    /**
     * Description:获取角色管理接口实例
     * @return
     * RoleManagement
     * @modelguid {2BE258C4-920B-422C-B676-2E83A50E1102}
     */
    public static RoleManager getRoleManager() throws SPIException {
        RoleManager roleManager = (RoleManager) ioc.getBeanObject(SecurityConstants.ROLE_MANAGEMENT_TYPE);
        return roleManager;
    }

    /**
     * Description:获取资源管理接口实例
     * @return
     * RoleManagement
     * @modelguid {F02D2A0D-B09C-41E0-B3E7-BB304CA14BCE}
     */
    public static ResManager getResourceManager() throws SPIException {
        ResManager resManager = (ResManager) ioc.getBeanObject(SecurityConstants.ENTRY_MANAGEMENT_TYPE);
        return resManager;
    }

    public static JobManager getJobManager() throws SPIException {
        JobManager jobManager = (JobManager) ioc.getBeanObject(SecurityConstants.JOB_MANAGEMENT_TYPE);
        return jobManager;
    }
    
    public static LogManager getLogManager() throws SPIException {
        LogManager logManager = (LogManager) ioc.getBeanObject(SecurityConstants.LOG_MANAGEMENT_TYPE);
        return logManager;
    }
    public static OperManager getOperManager() throws SPIException {
        OperManager operManager = (OperManager) ioc.getBeanObject(SecurityConstants.OPER_MANAGEMENT_TYPE);
        return operManager;
    }

    public static OrgManager getOrgManager(String type) throws SPIException {
        OrgManager orgManager = (OrgManager) ioc.getBeanObject(SecurityConstants.
                                            ORG_MANAGEMENT_TYPE);
        return orgManager;
    }


    /**
     * Description:获取用户管理接口实例
     * @return
     * UserManagement
     * @modelguid {40C65BD7-29FB-4D2A-9F05-2709F9EF405E}
     */
    public static UserManager getUserManager(String type) throws SPIException {
        //UserManagement um = null;
        UserManager userManager = (UserManager) ioc.getBeanObject(SecurityConstants.
                                            USER_MANAGEMENT_TYPE);
        return userManager;

    }

    /**
     * Description:获取用户组管理接口实例
     * @return
     * GroupManagement
     * @modelguid {CEA3B71E-4A0A-49E0-80FE-7E1F015816A8}
     */
    public static GroupManager getGroupManager(String type) throws SPIException {
        GroupManager userGroupManager = (GroupManager)ioc.getBeanObject(SecurityConstants.
                                            GROUP_MANAGEMENT_TYPE);


        return userGroupManager;
    }

    /**
     * Description:获取角色管理接口实例
     * @return
     * RoleManagement
     * @modelguid {2BE258C4-920B-422C-B676-2E83A50E1102}
     */
    public static RoleManager getRoleManager(String type) throws SPIException {
        RoleManager roleManager = (RoleManager) ioc.getBeanObject(SecurityConstants.
                                            ROLE_MANAGEMENT_TYPE);

        return roleManager;
    }

    /**
     * Description:获取资源管理接口实例
     * @return
     * RoleManagement
     * @modelguid {F02D2A0D-B09C-41E0-B3E7-BB304CA14BCE}
     */
    public static ResManager getResourceManager(String type) throws SPIException {
        ResManager resourceManager = (ResManager) ioc.getBeanObject(SecurityConstants.
                                            ENTRY_MANAGEMENT_TYPE);

        return resourceManager;
    }

    public static JobManager getJobManager(String type) throws SPIException {

        JobManager jobManager = (JobManager) ioc.getBeanObject(SecurityConstants.
                                            JOB_MANAGEMENT_TYPE);
        return jobManager;

    }
    
    public static LogManager getLogManager(String type) throws SPIException {

        LogManager logManager = (LogManager)ioc.getBeanObject(SecurityConstants.
                                            LOG_MANAGEMENT_TYPE);
        return logManager;

    }

    public static OperManager getOperManager(String type) throws SPIException {
        OperManager operManager = (OperManager) ioc.getBeanObject(SecurityConstants.
                                            OPER_MANAGEMENT_TYPE);
        return operManager;
    }

    public static OrgManager getOrgManager() throws SPIException {
        OrgManager orgManager = (OrgManager) ioc.getBeanObject(SecurityConstants.ORG_MANAGEMENT_TYPE);
        return orgManager;
    }
    
//    /**
//     * 获取字典管理接口实例
//     * @return
//     * @throws SPIException
//     */
//    public static DictManager getDictManager() throws SPIException {
//    	DictManager dictManager = (DictManager) getProvider(SecurityConstants.DICT_MANAGEMENT_TYPE);
//        return dictManager;
//    }

    //	/**
//	* Description:获取许可管理接口实例
//	* @return
//	* RoleManagement
//	* @modelguid {83994CEF-286E-474D-A0DB-58A4D63521ED}
//	*/
//	public static PermissionManagement getPermissionManager() throws SPIException {
//		if(permissionManager != null)
//		{
//			return permissionManager;
//		}
//		else
//			try {
//				permissionManager =
//					(PermissionManagement) ConfigManager.getInstance().getProviderInstance(SecurityConstants.PERMISSION_MANAGEMENT_TYPE);
//			} catch (SPIException e) {
//				throw new SPIException(
//					"Failed to get GroupManagement class instance..."
//						+ e.toString());
//			}
//		return permissionManager;
//	}

    /**
     * Description:获取配置文件中的JNDI名，进行安全信息管理接口绑定
     * @throws SPIException
     * void
     * @modelguid {849C4610-677D-4FED-B7D8-1913D2088E72}
     */
    public static void bindManager() throws SPIException {
//        List providerManagerTypes = ConfigManager.getInstance().
//                                    getProviderManagerTypes();
//        System.out.println("Starting create security manager context...");
//        //Environment env = new Environment();
//        //Context ctx = env.getInitialContext();
//        Context context = null;
//        try {
//            context = new InitialContext();
//        } catch (NamingException ex1) {
//            throw new SPIException(ex1);
//        }
////		if (ctx == null)
////			System.out.println("Could not get weblogic jndi context...");
////		else
////			System.out.println("Got weblogic jndi context : " + ctx.toString());
//
//
//        String bindName = null;
//        for (Iterator iter = providerManagerTypes.iterator(); iter.hasNext(); ) {
//            bindName = (String) iter.next(); //设定JNDI绑定名
//            System.out.println(
//                    "Starting create Security manager context : " + bindName);
//            Object mgr = ConfigManager.getInstance().getProviderInstance(
//                    bindName);
//
//            /*
//                System.out.println(
//             "Finished get manager instance by name..." + mgr.toString());
//             */
//            //System.out.println("Finished create security manager context...");
//            //JndiHelper.recursiveBind(ctx,bindName,mgr);
//            //ctx.bind(bindName, mgr);
//            //System.out.println("context:"+context);
//            //context.createSubcontext();
//            try {
//                context.bind(bindName, mgr);
//            } catch (NamingException ex) {
//                throw new SPIException(ex);
//            }
//            //System.out.println("lookup context:" + context.lookup(bindName));
//            //JndiHelper.recursiveBind(context,bindName,mgr);
//        }
    }

    /**
     * Description:撤销安全信息管理接口JNDI绑定
     * @throws SPIException
     * void
     * @modelguid {D9E79144-8268-4E66-B7AE-725D08C7F9E2}
     */
    public static void destoryManager() throws SPIException {
//        System.out.println("Starting close security manager context...");
////		Environment env = new Environment();  //获取服务器环境
////		Context ctx = env.getInitialContext(); //获取上下文实例
//        Context context = null;
//        try {
//            context = new InitialContext();
//        } catch (NamingException ex1) {
//            throw new SPIException(ex1);
//        }
//        if (context == null) {
//            System.out.println("Could not get websphere jndi context...");
//        } else {
//            System.out.println("Got websphere jndi context : " +
//                               context.toString());
//        }
//        List providerManagerTypes = ConfigManager.getInstance().
//                                    getProviderManagerTypes(); //获取全部安全接口绑定名
//        String bindName;
//        for (Iterator iter = providerManagerTypes.iterator();
//                             iter.hasNext();
//                             System.out.println(
//                                     "Finished clear security manager context : " +
//                                     bindName)) {
//            bindName = (String) iter.next();
//            System.out.println("Starting clear security manager context...");
//            try {
//                context.unbind(bindName);
//            } catch (NamingException ex) {
//                throw new SPIException(ex);
//            }
//        }
//
//        System.out.println("Finished close security manager context...");
    }

    public static void main(String[] args)
    {
        try {
            if(SecurityDatabase.getUserManager() instanceof UserManager)
                System.out.println("ok");

        } catch (SPIException ex) {
            ex.printStackTrace();
        }
    }



}
