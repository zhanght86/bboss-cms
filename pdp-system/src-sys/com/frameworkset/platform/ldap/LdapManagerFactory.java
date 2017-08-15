package com.frameworkset.platform.ldap;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.frameworkset.platform.config.ConfigManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class LdapManagerFactory implements java.io.Serializable {
    private static Map ctxCache = java.util.Collections.synchronizedMap(new java.util.WeakHashMap());

    public static final String DEFAULT_CACHE_KEY = "DEFAULT_CACHE_KEY";
    public static void main(String[] args) {
        LdapManagerFactory ldapmanagerfactory = new LdapManagerFactory();
    }

    /**
     * 获取缺省的ldap上下文
     * @return DirContext
     */
    public static DirContext getDirContext() {
        DirContext ctx = (DirContext) ctxCache.get(DEFAULT_CACHE_KEY);

        if (ctx == null) {
            try {
                ctx = init();
                if (ctx != null) {
                    ctxCache.put(DEFAULT_CACHE_KEY, ctx);
                }

            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
        return ctx;
    }

    /**
     * 获取指定ldap服务器类型的上下文
     * @param ldapType String
     * @return DirContext
     */
    public static DirContext getDirContext(String ldapType) {
        if (ldapType == null) {
            return getDirContext();
        }
        DirContext ctx = (DirContext) ctxCache.get(ldapType);
        if (ctx == null) {
            try {
                ctx = init(ldapType);
                if (ctx != null) {
                    ctxCache.put(ldapType, ctx);
                }
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }

        return ctx;
    }


    public static void reset() {
        try {
            init();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    private static DirContext init() throws NamingException {
        Hashtable env = new Hashtable(5, 0.75f);

        env.put(Context.INITIAL_CONTEXT_FACTORY,
                ConfigManager.getInstance().getLDAPConfig().
                getInitial_context_factory());

        /* Specify host and port to use for directory service */

        env.put(Context.PROVIDER_URL,
                ConfigManager.getInstance().getLDAPConfig().getProvider_url());
        env.put(Context.SECURITY_AUTHENTICATION,
                ConfigManager.getInstance().getLDAPConfig().
                getSecurity_authentication()); //
        env.put(Context.SECURITY_PRINCIPAL,
                ConfigManager.getInstance().getLDAPConfig().
                getSecurity_pricipal()); //载入登陆帐户和登录密码
        env.put(Context.SECURITY_CREDENTIALS,
                ConfigManager.getInstance().getLDAPConfig().
                getSecurity_credentials());
        DirContext ctx = new InitialDirContext(env);
        return ctx;
    }

    private static DirContext init(String ldapType) throws NamingException {
        Hashtable env = new Hashtable(5, 0.75f);

        env.put(Context.INITIAL_CONTEXT_FACTORY,
                ConfigManager.getInstance().getLDAPConfig(ldapType).
                getInitial_context_factory());

        /* Specify host and port to use for directory service */

        env.put(Context.PROVIDER_URL,
                ConfigManager.getInstance().getLDAPConfig(ldapType).
                getProvider_url());
        env.put(Context.SECURITY_AUTHENTICATION,
                ConfigManager.getInstance().getLDAPConfig(ldapType).
                getSecurity_authentication()); //
        env.put(Context.SECURITY_PRINCIPAL,
                ConfigManager.getInstance().getLDAPConfig(ldapType).
                getSecurity_pricipal()); //载入登陆帐户和登录密码
        env.put(Context.SECURITY_CREDENTIALS,
                ConfigManager.getInstance().getLDAPConfig(ldapType).
                getSecurity_credentials());
        DirContext ctx = new InitialDirContext(env);
        return ctx;
    }


}
