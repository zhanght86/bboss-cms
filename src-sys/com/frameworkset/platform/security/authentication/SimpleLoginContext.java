/*
 *  Copyright 2008 bbossgroups
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.platform.security.authentication;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.AuthPermission;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.config.LoginModuleInfoQueue;
import com.frameworkset.platform.config.model.LoginModuleInfo;

/**
 * <p>Title: SimpleLoginContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年5月8日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SimpleLoginContext {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(LoginContext.class);
    private static final String INIT_METHOD		= "initialize";
    private static final String LOGIN_METHOD		= "login";
    private static final String COMMIT_METHOD		= "commit";
    private static final String ABORT_METHOD		= "abort";
    private static final String LOGOUT_METHOD		= "logout";
    private static final String OTHER			= "other";
    private static final String DEFAULT_HANDLER		=
                                "auth.login.defaultCallbackHandler";
    /**应用名称*/
    private String appName;
    /**模块名称*/
    private String moduleName;
    private Subject subject = null;
    private boolean subjectProvided = false;
    private boolean loginSucceeded = false;
    private transient CallbackHandler callbackHandler;
    private Map state = new HashMap();

    private LoginModuleInfoQueue moduleQueue;


    private ModuleInfo[] moduleStack;
    private transient ClassLoader contextClassLoader = null;
    private static final Class[] PARAMS = { };

//    private static final sun.security.util.Debug debug =
//        sun.security.util.Debug.getInstance("logincontext", "\t[LoginContext]");

    /**
     * 初始化系统环境上下文
     * @param appName String
     * @param moduleName String
     */
    public void initContext(String appName,String moduleName)
    {
        this.appName = appName;
        this.moduleName = moduleName;
    }
    /**
     * 初始化登录模块
     * @param name String
     * @throws LoginException
     */
    private void init(String name) throws LoginException {

        java.lang.SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission
                                ("createLoginContext." + name));
        }

        if (name == null)
            throw new LoginException
                (ResourcesMgr.getString("Invalid null input: name"));
        if(this.appName == null && this.moduleName == null)
        {
            moduleQueue = ConfigManager.getInstance().getDefaultApplicationInfo().getLoginModuleInfos();
        }
        else
        {
            moduleQueue = ConfigManager.getInstance().getApplicationInfo(appName).getLoginModuleInfos();
        }


        // get the Configuration
//        if (config == null) {
//            config = (Configuration)java.security.AccessController.doPrivileged
//                (new java.security.PrivilegedAction() {
//                public Object run() {
//                    return Configuration.getConfiguration();
//                }
//            });
//        }

        // get the LoginModules configured for this application
//        AppConfigurationEntry[] entries = config.getAppConfigurationEntry(name);
//        if (entries == null) {
//
//            if (sm != null) {
//                sm.checkPermission(new AuthPermission
//                                ("createLoginContext." + OTHER));
//            }
//
//            entries = config.getAppConfigurationEntry(OTHER);
//            if (entries == null) {
//                MessageFormat form = new MessageFormat(ResourcesMgr.getString
//                        ("No LoginModules configured for name"));
//                Object[] source = {name};
//                throw new LoginException(form.format(source));
//            }
//        }
        moduleStack = new ModuleInfo[moduleQueue.size()];
        //复制
        for (int i = 0; i < moduleQueue.size(); i++) {
            // clone returned array
//            moduleStack[i] = new ModuleInfo
//                                (new AppConfigurationEntry
//                                        (entries[i].getLoginModuleName(),
//                                        entries[i].getControlFlag(),
//                                        entries[i].getOptions()),
//                                null);
            moduleStack[i] = new ModuleInfo
                                (moduleQueue.getLoginModuleInfo(i),
                                null);

        }


        contextClassLoader = Thread.currentThread().getContextClassLoader();
//                (ClassLoader)java.security.AccessController.doPrivileged
//                (new java.security.PrivilegedAction() {
//                public Object run() {
//                    return Thread.currentThread().getContextClassLoader();
//                }
//        });
    }

    

   
   


    /**
     * Constructor for the <code>LoginContext</code> class.
     *
     * <p> Initialize the new <code>LoginContext</code> object with a name
     * and a <code>CallbackHandler</code> object.
     *
     * <p> <code>LoginContext</code> uses the name as the index
     * into the <code>Configuration</code> to determine which LoginModules
     * should be used.  If the provided name does not match any in the
     * <code>Configuration</code>, then the <code>LoginContext</code>
     * uses the default <code>Configuration</code> entry, "<i>other</i>".
     * If there is no <code>Configuration</code> entry for "<i>other</i>",
     * then a <code>LoginException</code> is thrown.
     *
     * <p> <code>LoginContext</code> passes the <code>CallbackHandler</code>
     * object to configured LoginModules so they may communicate with the user.
     * The <code>CallbackHandler</code> object therefore allows LoginModules to
     * remain independent of the different ways applications interact with
     * users.  This <code>LoginContext</code> must wrap the
     * application-provided <code>CallbackHandler</code> in a new
     * <code>CallbackHandler</code> implementation, whose <code>handle</code>
     * method implementation invokes the application-provided
     * CallbackHandler's <code>handle</code> method in a
     * <code>java.security.AccessController.doPrivileged</code> call
     * constrained by the caller's current <code>AccessControlContext</code>.
     *
     * <p> Since no <code>Subject</code> can be specified to this constructor,
     * it instantiates a <code>Subject</code> itself.
     *
     * <p>
     *
     * @param name the name used as the index into the
     *		<code>Configuration</code>. <p>
     *
     * @param callbackHandler the <code>CallbackHandler</code> object used by
     *		LoginModules to communicate with the user.
     *
     * @exception LoginException if the specified <code>name</code>
     *          does not appear in the <code>Configuration</code>
     *          and there is no <code>Configuration</code> entry
     *          for "<i>other</i>", or if the specified
     *		<code>callbackHandler</code> is <code>null</code>.
     */
    public SimpleLoginContext(String name, CallbackHandler callbackHandler)
    throws LoginException {
        init(name);
        if (callbackHandler == null)
            throw new LoginException(ResourcesMgr.getString
                                ("invalid null CallbackHandler provided"));
        this.callbackHandler = new SecureCallbackHandler
                                (
//                                		java.security.AccessController.getContext(),
                                callbackHandler);
    }

    

    /**
     * Perform the authentication and, if successful,
     * associate Principals and Credentials with the authenticated
     * <code>Subject</code>.
     *
     * <p> This method invokes the <code>login</code> method for each
     * LoginModule configured for the <i>name</i> provided to the
     * <code>LoginContext</code> constructor, as determined by the login
     * <code>Configuration</code>.  Each <code>LoginModule</code>
     * then performs its respective type of authentication
     * (username/password, smart card pin verification, etc.).
     *
     * <p> This method completes a 2-phase authentication process by
     * calling each configured LoginModule's <code>commit</code> method
     * if the overall authentication succeeded (the relevant REQUIRED,
     * REQUISITE, SUFFICIENT, and OPTIONAL LoginModules succeeded),
     * or by calling each configured LoginModule's <code>abort</code> method
     * if the overall authentication failed.  If authentication succeeded,
     * each successful LoginModule's <code>commit</code> method associates
     * the relevant Principals and Credentials with the <code>Subject</code>.
     * If authentication failed, each LoginModule's <code>abort</code> method
     * removes/destroys any previously stored state.
     *
     * <p> If the <code>commit</code> phase of the authentication process
     * fails, then the overall authentication fails and this method
     * invokes the <code>abort</code> method for each configured
     * <code>LoginModule</code>.
     *
     * <p> If the <code>abort</code> phase
     * fails for any reason, then this method propagates the
     * original exception thrown either during the <code>login</code> phase
     * or the <code>commit</code> phase.  In either case, the overall
     * authentication fails.
     *
     * <p> In the case where multiple LoginModules fail,
     * this method propagates the exception raised by the first
     * <code>LoginModule</code> which failed.
     *
     * <p> Note that if this method enters the <code>abort</code> phase
     * (either the <code>login</code> or <code>commit</code> phase failed),
     * this method invokes all LoginModules configured for the specified
     * application regardless of their respective <code>Configuration</code>
     * flag parameters.  Essentially this means that <code>Requisite</code>
     * and <code>Sufficient</code> semantics are ignored during the
     * <code>abort</code> phase.  This guarantees that proper cleanup
     * and state restoration can take place.
     *
     * <p>
     *
     * @exception LoginException if the authentication fails.
     */
    public void login() throws LoginException {

        loginSucceeded = false;

        

        try {
            //执行第一阶段登录
            this._invoke(LOGIN_METHOD);
            //执行第二阶段登录，提交整体登录信息
            this._invoke(COMMIT_METHOD);
            loginSucceeded = true;
        } catch (LoginException le) {

            try {
                //登录失败则退出登录
            	this._invoke(ABORT_METHOD);
            } catch (LoginException le2) {
                throw le;
            }
            throw le;
        }
    }
    
    private void _invoke(String action) throws LoginException
    {
    	

        if (subject == null) {
            subject = new Subject();
        }
        
        LoginException firstError = null;
        LoginException firstRequiredError = null;
        boolean success = false;

        for (int i = 0; i < moduleStack.length; i++) {

            try {

                int mIndex = 0;
                Method[] methods = null;

                if (moduleStack[i].module != null) {//如果登录模块已经初始化，直接提取所有登录模块可访问方法
                   
                } else {//初始化登陆模块，提取所有登录模块可访问方法

                    // instantiate the LoginModule

                    Class c = Class.forName
                                (moduleStack[i].getLoginModuleInfo().getLoginModule(),
                                true,
                                contextClassLoader);

                    Constructor constructor = c.getConstructor(PARAMS);
                    Object[] args = { };

                    // allow any object to be a LoginModule
                    // as long as it conforms to the interface
                    moduleStack[i].module = (LoginModule)constructor.newInstance(args);
                    //检测登录模块是否是ACLLoginModule类型的模块，如果是设置登录模块的名称
                    if(moduleStack[i].module instanceof ACLLoginModule)
                    {
                        ((ACLLoginModule)moduleStack[i].module)
                                .setLoginModuleName(moduleStack[i].getLoginModuleInfo().getName());
                        ((ACLLoginModule)moduleStack[i].module)
                                .setRegistTable(moduleStack[i].getLoginModuleInfo().getRegistTable());

                    }

                   
                    CallbackHandler handler = callbackHandler;
                    //如果登录模块指定了特定的回调函数，则使用特定的回调函数对登录模块进行初始化
                    if(moduleStack[i].getLoginModuleInfo().getCallBackHandler() != null
                       && !moduleStack[i].getLoginModuleInfo().getCallBackHandler().equals(""))
                    {
                        try
                        {
                            handler = (CallbackHandler) Class.forName(
                                    moduleStack[i].getLoginModuleInfo().
                                    getCallBackHandler()).newInstance();
                        }
                        catch(Exception e)
                        {
                            log.error("",e);
                            handler = callbackHandler;
                        }
                    }

                    Object[] initArgs = {subject,
                                        handler
                                        };//options(java.util.Map) is null
                    // invoke the LoginModule initialize method
                    moduleStack[i].module.initialize(subject, handler);
                   
                }

             

                // set up the arguments to be passed to the LoginModule method
                Object[] args = { };
                boolean status = false;
                // invoke the LoginModule method
                if(action.equals(LOGIN_METHOD))
                {
                	status = moduleStack[i].module.login();
                }
                else if(action.equals(COMMIT_METHOD))
                {
                	status = moduleStack[i].module.commit();
                }
                else if(action.equals(ABORT_METHOD))
                {
                	status = moduleStack[i].module.abort();
                }
                else if(action.equals(LOGOUT_METHOD))
                {
                	status = moduleStack[i].module.logout();
                }

                //方法返回值为true,表示方法执行成功，做SUFFICIENT检查
                if (status == true) {

                    // if SUFFICIENT, return if no prior REQUIRED errors
                    if (moduleStack[i].getLoginModuleInfo().getControlFlag().equals(LoginModuleControlFlag.SUFFICIENT.controlFlag)
                     &&
                        firstRequiredError == null) {

                        //if (log != null)
                            log.debug(action+" SUFFICIENT success");
                        return;
                    }
                    log.debug(action+"  success");
                    success = true;
                } else {
                   log.debug(action+" failed.");
                }

            } catch (NoSuchMethodException nsme) {
                //nsme.printStackTrace();
                MessageFormat form = new MessageFormat(ResourcesMgr.getString
                        ("unable to instantiate LoginModule, module, because " +
                        "it does not provide a no-argument constructor"));
                Object[] source = {moduleStack[i].entry.getLoginModule()};
                throw new LoginException(form.format(source));
            } catch (InstantiationException ie) {
                //ie.printStackTrace();
                throw new LoginException(ResourcesMgr.getString
                        ("unable to instantiate LoginModule: ") +
                        ie.getMessage());
            } catch (ClassNotFoundException cnfe) {
                //cnfe.printStackTrace();
                throw new LoginException(ResourcesMgr.getString
                        ("unable to find LoginModule class: ") +
                        cnfe.getMessage());
            } catch (IllegalAccessException iae) {
                //iae.printStackTrace();
                throw new LoginException(ResourcesMgr.getString
                        ("unable to access LoginModule: ") +
                        iae.getMessage());
            } catch (InvocationTargetException ite) {
                //ite.printStackTrace();

                // failure cases
                LoginException le;
//                if (ite.getTargetException() instanceof javax.security.auth.login.LoginException) {
//                    le = (LoginException)ite.getTargetException();
//                } else 
                {
                    // capture an unexpected LoginModule exception
//                    java.io.StringWriter sw = new java.io.StringWriter();
//                    ite.getCause().printStackTrace
//                                                (new java.io.PrintWriter(sw));
//                    sw.flush();
//                    le = new LoginException(sw.toString());
                	if(ite.getTargetException() instanceof LoginException)
                	{
                		le = (LoginException)ite.getTargetException();
                	}
                	else
                	{
                		le = new LoginException(ite.getTargetException().getMessage(),ite.getTargetException());
                	}
                    
                }

                //如果是REQUISITE，那么立即抛出异常
                if(action.equals(LOGIN_METHOD) || action.equals(COMMIT_METHOD))
                {
	                if (moduleStack[i].entry.getControlFlag().equals(
	                    LoginModuleControlFlag.REQUISITE.controlFlag)) {
	
	                    log.debug(action+" REQUISITE failure");
	
	                    // if REQUISITE, then immediately throw an exception
	//                    if (methodName.equals(ABORT_METHOD) ||
	//                        methodName.equals(LOGOUT_METHOD)) {
	//                        if (firstRequiredError == null)
	//                            firstRequiredError = le;
	//                    } else 
	                    {
	                        throwException(firstRequiredError, le);
	                    }
	
	                } else if (moduleStack[i].entry.getControlFlag().equals(LoginModuleControlFlag.REQUIRED.controlFlag)) {
	
	                    log.debug(action+" REQUIRED failure");
	
	                    // mark down that a REQUIRED module failed
	                    if (firstRequiredError == null)
	                        firstRequiredError = le;
	
	                } else {
	
	                    log.debug(action+" OPTIONAL failure");
	
	                    // mark down that an OPTIONAL module failed
	                    if (firstError == null)
	                        firstError = le;
	                }
                }
            } catch (LoginException e) {
            	throw e;
				
			}
            catch (Exception e) {
            	throw new LoginException(e);
				
			}
            
        }
    }

    /**
     * Logout the <code>Subject</code>.
     *
     * <p> This method invokes the <code>logout</code> method for each
     * <code>LoginModule</code> configured for this <code>LoginContext</code>.
     * Each <code>LoginModule</code> performs its respective logout procedure
     * which may include removing/destroying
     * <code>Principal</code> and <code>Credential</code> information
     * from the <code>Subject</code> and state cleanup.
     *
     * <p> Note that this method invokes all LoginModules configured for the
     * specified application regardless of their respective
     * <code>Configuration</code> flag parameters.  Essentially this means
     * that <code>Requisite</code> and <code>Sufficient</code> semantics are
     * ignored for this method.  This guarantees that proper cleanup
     * and state restoration can take place.
     *
     * <p>
     *
     * @exception LoginException if the logout fails.
     */
    public void logout() throws LoginException {
        if (subject == null) {
            throw new LoginException(ResourcesMgr.getString
                ("null subject - logout called before login"));
        }

        this._invoke(LOGOUT_METHOD);
    }

    /**
     * Return the authenticated Subject.
     *
     * <p>
     *
     * @return the authenticated Subject.  If authentication fails
     *		and a Subject was not provided to this LoginContext's
     *		constructor, this method returns <code>null</code>.
     *		Otherwise, this method returns the provided Subject.
     */
    public Subject getSubject() {
        if (!loginSucceeded && !subjectProvided)
            return null;
        return subject;
    }

    private void throwException(LoginException originalError, LoginException le)
    throws LoginException {
        LoginException error = (originalError != null) ? originalError : le;
        throw error;
    }

   

    /**
     * Wrap the application-provided CallbackHandler in our own
     * and invoke it within a privileged block, constrained by
     * the caller's AccessControlContext.
     */
    private static class SecureCallbackHandler implements CallbackHandler,Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
//		private final java.security.AccessControlContext acc;
        private final CallbackHandler ch;

        SecureCallbackHandler(CallbackHandler ch) {
//            this.acc = acc;
            this.ch = ch;
        }
        
//        SecureCallbackHandler(java.security.AccessControlContext acc,
//                CallbackHandler ch) {
//    this.acc = acc;
//    this.ch = ch;
//}
        

        public void handle(Callback[] callbacks) throws java.io.IOException,
                                                UnsupportedCallbackException {
//            try {
                final Callback[] finalCallbacks = callbacks;
//                java.security.AccessController.doPrivileged
//                    (new java.security.PrivilegedExceptionAction() {
//                    public Object run() throws java.io.IOException,
//                                        UnsupportedCallbackException {
                        ch.handle(finalCallbacks);
//                        return null;
//                    }
//                }, acc);
//            } catch (java.security.PrivilegedActionException pae) {
//                if (pae.getException() instanceof java.io.IOException) {
//                    throw (java.io.IOException)pae.getException();
//                } else {
//                    throw (UnsupportedCallbackException)pae.getException();
//                }
//            }
        }
    }

    /**
     * LoginModule information -
     *		incapsulates Configuration info and actual module instances
     */
    private static class ModuleInfo implements Serializable{
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		LoginModuleInfo entry;
        LoginModule module;

        ModuleInfo(LoginModuleInfo newEntry, LoginModule newModule) {
            this.entry = newEntry;
            this.module = newModule;
        }

        public LoginModuleInfo getLoginModuleInfo() {
            return this.entry;
        }

        public LoginModule getModule() {
            return this.module;
        }
    }

    /**
     * This class represents whether or not a <code>LoginModule</code>
     * is REQUIRED, REQUISITE, SUFFICIENT or OPTIONAL.
     */
    public static class LoginModuleControlFlag implements Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = 6514118449819585015L;

		private String controlFlag;

        /**
         * Required <code>LoginModule</code>.
         */
        public static final LoginModuleControlFlag REQUIRED =
                                new LoginModuleControlFlag("required");

        /**
         * Requisite <code>LoginModule</code>.
         */
        public static final LoginModuleControlFlag REQUISITE =
                                new LoginModuleControlFlag("requisite");

        /**
         * Sufficient <code>LoginModule</code>.
         */
        public static final LoginModuleControlFlag SUFFICIENT =
                                new LoginModuleControlFlag("sufficient");

        /**
         * Optional <code>LoginModule</code>.
         */
        public static final LoginModuleControlFlag OPTIONAL =
                                new LoginModuleControlFlag("optional");

        private LoginModuleControlFlag(String controlFlag) {
            this.controlFlag = controlFlag;
        }

        /**
         * Return a String representation of this controlFlag.
         *
         * @return a String representation of this controlFlag.
         */
        public String toString() {
            return (sun.security.util.ResourcesMgr.getString
                ("LoginModuleControlFlag: ") + controlFlag);
        }
    }
    
	public static void resetUserAttribute(HttpServletRequest request,
			CheckCallBack checkCallBack, String userAttribute) {
		LoginModuleInfoQueue moduleQueue = ConfigManager.getInstance().getDefaultApplicationInfo().getLoginModuleInfos();
		int size = moduleQueue.getACLLoginModuleSize();
		for(int i = 0; i < size;i ++)
		{
			ACLLoginModule aclLoginModule = moduleQueue.getACLLoginModule(i);
			aclLoginModule.resetUserAttribute(request, checkCallBack, userAttribute);
		}
		
	}
	
	public static void resetUserAttribute(HttpServletRequest request,
			CheckCallBack checkCallBack) {
		LoginModuleInfoQueue moduleQueue = ConfigManager.getInstance().getDefaultApplicationInfo().getLoginModuleInfos();
		int size = moduleQueue.getACLLoginModuleSize();
		for(int i = 0; i < size;i ++)
		{
			ACLLoginModule aclLoginModule = moduleQueue.getACLLoginModule(i);
			aclLoginModule.resetUserAttributes(request, checkCallBack);
		}
		
	}

}
