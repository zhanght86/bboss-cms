package com.frameworkset.platform.ca;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.spi.BaseSPIManager2;

/** 
 * <p>类说明:</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: bbossgroups</p>
 * @author  gao.tang 
 * @version V1.0  创建时间：Oct 23, 2009 12:00:45 PM 
 */
public class CookieProperties {
	
	private static final Log LOG = LogFactory.getLog(CookieProperties.class);
	
	/**
	 * 客户端IP地址
	 */
	public static final String CLIENT_IP = BaseSPIManager2.getProperty("CLIENT_IP","KOAL_CLIENT_IP");
	
	/**
	 * 证书有效期的起始时间
	 */
	public static final String NOT_BEFORE = BaseSPIManager2.getProperty("NOT_BEFORE","KOAL_NOT_BEFORE");
	
	/**
	 * 证书有效期的中止时间
	 */
	public static final String NOT_AFTER = BaseSPIManager2.getProperty("NOT_AFTER","KOAL_NOT_AFTER");
	
	/**
	 * 证书序列号，简称SN，对于同一CA颁发的证书，这是一个唯一值（不同CA的证书序列号可能重复）
	 */
	public static final String CERT_SN = BaseSPIManager2.getProperty("CERT_SN","KOAL_CERT_SN");
	
	/**
	 * 可识别名，简称DN，全称为DistinguishedName，可以作为证书的唯一标识
	 */
	public static final String CERT_DN = BaseSPIManager2.getProperty("CERT_DN","KOAL_CERT_DN");
	
	/**
	 * 通用名项，简称CN，对应X509规范中的id-at-commonName，如果是个人证书，则一般为人名，如“张三”
	 */
	public static final String CERT_CN = BaseSPIManager2.getProperty("CERT_CN","KOAL_CERT_CN");
	
	/**
	 * 国家，简称C，对应X509规范中的id-at-countryName，例如“中国”
	 */
	public static final String CERT_C = BaseSPIManager2.getProperty("CERT_C","KOAL_CERT_C");
	
	/**
	 * 地点，简称L，对应X509规范中的id-at-localityName，一般为城市名，例如“上海”
	 */
	public static final String CERT_L = BaseSPIManager2.getProperty("CERT_L","KOAL_CERT_L");
	
	/**
	 * 省份，简称ST，对应X509规范中的id-at-stateOrProvinceName，一般为省份名称，例如“浙江”
	 */
	public static final String CERT_ST = BaseSPIManager2.getProperty("CERT_ST","KOAL_CERT_ST");
	
	/**
	 * 组织，简称O，对应X509规范中的id-at-organizationName，一般为公司或团体名，如“格尔软件” 
	 */
	public static final String CERT_O = BaseSPIManager2.getProperty("CERT_O","KOAL_CERT_O");
	
	/**
	 * 部门，简称OU，对应X509规范中的id-at-organizationalUnitName，一般为部门名，如“市场部”
	 */
	public static final String KOAL_CERT_OU = BaseSPIManager2.getProperty("CERT_OU","KOAL_CERT_OU");
	
	/**
	 * 电子邮件，对应X509规范中的pkcs9-emailAddress，一般为用户的电子邮件，如abc@koal.com
	 */
	public static final String CERT_E = BaseSPIManager2.getProperty("CERT_E","KOAL_CERT_E");
	
	/**
	 * 简称GN，对应X509规范中的id-at-givenName
	 */
	public static final String CERT_GN = BaseSPIManager2.getProperty("CERT_GN","KOAL_CERT_GN");
	
	/**
	 * 简称T，对应X509规范中的id-at-title（较少使用）
	 */
	public static final String CERT_T = BaseSPIManager2.getProperty("CERT_T","KOAL_CERT_T");
	
	/**
	 * 简称S，对应X509规范中的id-at-surname（较少使用）
	 */
	public static final String CERT_S = BaseSPIManager2.getProperty("CERT_S","KOAL_CERT_S");
	
	/**
	 * 证书别名，某些CA颁发的证书可能有这一项
	 */
	public static final String CERT_ALIAS = BaseSPIManager2.getProperty("CERT_ALIAS","KOAL_CERT_ALIAS");
	
	/**
	 * 电话号码，某些CA颁发的证书可能有这一项
	 */
	public static final String CERT_PHONE = BaseSPIManager2.getProperty("CERT_PHONE","KOAL_CERT_PHONE");
	
	/**
	 * 证书颁发者的CN，也即颁发用户证书的上级CA证书的id-at-commonName项 
	 */
	public static final String CERT_ISSUER_CN = BaseSPIManager2.getProperty("CERT_ISSUER_CN","KOAL_CERT_ISSUER_CN");
	
	/**
	 * 证书颁发者的O，也即颁发用户证书的上级CA证书的id-at-organizationName项 
	 */
	public static final String CERT_ISSUER_O = BaseSPIManager2.getProperty("CERT_ISSUER_O","KOAL_CERT_ISSUER_O");
	
	/**
	 * 客户端证书，PEM格式，这是大约1k左右的数据，如果不是特殊情况不建议绑定到cookie，否则会给WEB服务器带来比较大的负担
	 */
	public static final String CERT = BaseSPIManager2.getProperty("CERT","KOAL_CERT");
	
	
}
