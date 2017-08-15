package com.sany.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.frameworkset.spi.InitializingBean;

import com.frameworkset.common.poolman.security.DESCipher;



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
public class LdapManagerFactory  implements InitializingBean,org.frameworkset.spi.DisposableBean{
	private static Logger logger = Logger.getLogger(LdapManagerFactory.class);
	private DirContext ctx;
	private String initial_context_factory;
	private String provider_url;
	private String security_user;
	private String security_password;
	private String security_authentication;
   
   

    /**
     * 获取缺省的ldap上下文
     * @return DirContext
     */
    public DirContext getDirContext() {        
        return ctx;
    }

    


    public void reset() {
        try {
            init();
        } catch (Exception ex) {
        	logger.error("", ex);
        }
    }

    private DirContext init() throws Exception {
        Hashtable<String,String> env = new Hashtable<String,String>(5, 0.75f);

        env.put(Context.INITIAL_CONTEXT_FACTORY,
        		initial_context_factory);

        /* Specify host and port to use for directory service */
        DESCipher s = new DESCipher();
        env.put(Context.PROVIDER_URL,
        		provider_url);
        env.put(Context.SECURITY_AUTHENTICATION,
        		security_authentication); //
        env.put(Context.SECURITY_PRINCIPAL,
        		security_user); //载入登陆帐户和登录密码
        env.put(Context.SECURITY_CREDENTIALS,
        		s.decrypt(security_password));
        ctx = new InitialDirContext(env);
        return ctx;
    }
    public DirContext authenticate(String user,String password)
    {
    	try
    	{
    		return bind(user,password);
//    		return true;
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    }
    public DirContext bind(String user,String password) throws Exception {
        Hashtable<String,String> env = new Hashtable<String,String>(5, 0.75f);

        env.put(Context.INITIAL_CONTEXT_FACTORY,
        		initial_context_factory);

        /* Specify host and port to use for directory service */
        DESCipher s = new DESCipher();
        env.put(Context.PROVIDER_URL,
        		provider_url);
        env.put(Context.SECURITY_AUTHENTICATION,
        		security_authentication); //
        env.put(Context.SECURITY_PRINCIPAL,
        		user); //载入登陆帐户和登录密码
        env.put(Context.SECURITY_CREDENTIALS,
        		password);
        DirContext ctx = new InitialDirContext(env);
        return ctx;
    }
    
    

   

	@Override
	public void destroy() throws Exception {
		this.ctx.close();
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		
	}
	
	

	
	public List<Map<String,String>> getAttibutes(String searchObjectCategory,String searchDC,String propertyName,String propertyValue) throws Exception
	{
		 String filter = "(&(objectCategory=" + searchObjectCategory + ")("+propertyName+"=" + propertyValue + "))";
		 List<Map<String,String>> list = null;
//         LDAPSearchResults lsrs = lc.search(searchDC, LDAPConnection.SCOPE_SUB, filter, null, false);
         SearchControls control = new SearchControls(SearchControls.SUBTREE_SCOPE, 0, 0, null, false, false);
         try {
             list = new ArrayList();
             NamingEnumeration enu = this.ctx.search(searchDC, filter, control);

             if (enu.hasMore()) {
                 while (enu.hasMoreElements()) {
                     SearchResult result = (SearchResult) enu.next();
                     Map<String,String> record = new HashMap<String,String>();
                     Attributes attrs = result.getAttributes();
                     NamingEnumeration<? extends Attribute> attrsenum = attrs.getAll();
                     while(attrsenum.hasMore())
                     {
                    	 Attribute attr = attrsenum.next();
                    	 record.put(attr.getID(), String.valueOf(attr.get()));
                     }
                     list.add(record);
                 }
             }
         } catch (NamingException e) {
             throw new Exception(e);
         }

         return list;
//         if (lsrs.hasMore()) {
//             LDAPEntry ent = lsrs.next();
//             
//             response.put("distinguishedName", ent.getAttribute("distinguishedName").getStringValue());
//             response.put("displayName", ent.getAttribute("displayName").getStringValue());
//             response.put("pwdLastSet", ent.getAttribute("pwdLastSet").getStringValue());
//         }
         
         
//         LdapControl dc = new LdapControl();
//
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//
//			p.getProperties().setProperty("parentdn", userBase);
//			p.getProperties().setProperty(
//					"filter",
//					"(&(objectclass=inetorgperson)|(" + propName + "=" + value
//							+ "))");
//
//			List list = (List) dc.execute(p);
//			if (list != null && !list.isEmpty()) {
//				SearchResult sr = (SearchResult) list.get(0);
//				user = new User();
//				Attributes attrs = sr.getAttributes();
//
//				// 更新开始：UserID 已经修改 Integer 型，从 LDAP 中取数据时设置 UserID 为 -1 ，而如果
//				// 需要保存数据到 DB 中的话则判断 UserID 是否为 -1 是则表明该数据来自 LDAP 中。
//				// user.setUserId(attrs.get("uid").get().toString());
//				user.setUserId(new Integer(-1));
//				// 更新结束
//
//				user.setUserName(attrs.get("cn").get().toString());
//				user
//						.setUserPassword(attrs.get("userPassword").get()
//								.toString());
//				user.setUserRealname(attrs.get("displayName").get().toString());
//			}
	}




	public DirContext getCtx() {
		return ctx;
	}




	public void setCtx(DirContext ctx) {
		this.ctx = ctx;
	}




	public String getInitial_context_factory() {
		return initial_context_factory;
	}




	public void setInitial_context_factory(String initial_context_factory) {
		this.initial_context_factory = initial_context_factory;
	}




	public String getProvider_url() {
		return provider_url;
	}




	public void setProvider_url(String provider_url) {
		this.provider_url = provider_url;
	}




	public String getSecurity_user() {
		return security_user;
	}




	public void setSecurity_user(String security_user) {
		this.security_user = security_user;
	}




	public String getSecurity_password() {
		return security_password;
	}




	public void setSecurity_password(String security_password) {
		this.security_password = security_password;
	}




	public String getSecurity_authentication() {
		return security_authentication;
	}




	public void setSecurity_authentication(String security_authentication) {
		this.security_authentication = security_authentication;
	}


}
