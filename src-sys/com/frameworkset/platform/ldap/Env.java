/* 初始化环境的构造参数
 * @author wanghh
 */


package com.frameworkset.platform.ldap;



public class Env implements java.io.Serializable {

/*
 * 初始化LDAP环境工厂
 */
public static String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";

/*
 *LDAP服务器(主机加端口)
 */
//public static String MY_SERVICE = "ldap://localhost:389";

/*
 * 目录管理DN号具有整个目录的访问权限
 */
//public static String MGR_DN = "cn=Directory Manager";

/*
 * 目录管理的密码
 */
//public static String MGR_PW = "wanghh422300";

/*
 * 登陆时验证的安全级别 分三种 "none", "simple", "strong"
 */
public static String MY_AUTHENTICATION = "simple";


/*
 * 查询子目录时的入口 此处定义为目录服务器的根
 */
//public static String MY_SEARCHBASE = "dc=onceportal.com";

/*
 * 更改时的入口(与过滤条件一起构成一个唯一的条目)此处定义为目录服务器的根
 */
//public static String MY_MODBASE = "dc=onceportal.com";

/*
 * 默认的查询过滤条件 表示查询入口处以下子目录的所有类别的对象(目录和条目)
 */
//public static String MY_FILTER = "(objectClass=*)";

/*
 *目录服务器的根
 */
//public static String ENTRYDN = "dc=onceportal.com";
}

