//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemanager\\SiteManager.java
package com.frameworkset.platform.cms.addressmanager;

import com.frameworkset.util.ListInfo;

/**
 * @author huaihai.ou
 * 日期:Dec 19, 2006
 * 版本:1.0
 * 版权所有:三一重工
 */
public interface AddressManager extends java.io.Serializable
{
	/**
	 * 新通讯录名单
	 * @return
	 * @throws AddressManagerException
	 */
	public boolean creatorAddress(Address address) throws AddressManagerException;
	/**
	 * 修改通讯录名单
	 * @return
	 * @throws AddressManagerException
	 */
	public boolean updateAddress(Address address) throws AddressManagerException;
   /**
    * 删除通讯录名单
    * @param docid
    * @return
    * @throws AddressManagerException
    */
	public boolean deleteAddress(String[] addressBookIds) throws AddressManagerException;
    /**
     * 获得所有通讯录名单列表
     * @return
     * @throws AddressManagerException
     */
	public ListInfo getAddressList(String sql, int offset, int maxItem) throws AddressManagerException;
	
	/**
	 * 通过ID返回对象
	 * @param addressBookId
	 * @return
	 * @throws AddressManagerException
	 */
	public Address getAddressById(String addressBookId)throws AddressManagerException;

}