//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemanager\\SiteManager.java
package com.frameworkset.platform.cms.docsourcemanager;


/**
 * @author huaihai.ou
 * 日期:Dec 19, 2006
 * 版本:1.0
 * 版权所有:三一重工
 */
public interface DocsourceManager extends java.io.Serializable
{
	/**
	 * 新通讯录名单
	 * @return
	 * @throws DocsourceManagerException
	 */
	public boolean creatorDsrc(Docsource docsource) throws DocsourceManagerException;
	/**
	 * 修改通讯录名单
	 * @return
	 * @throws DocsourceManagerException
	 */
	public boolean updateDsrc(Docsource docsource) throws DocsourceManagerException;
   /**
    * 删除通讯录名单
    * @return
    * @throws DocsourceManagerException
    */
    public boolean deleteDsrc(int docsrcid) throws DocsourceManagerException;
    /**
     * 获得所有通讯录名单列表
     * @return
     * @throws DocsourceManagerException
     */
	public Docsource getDsrcList(int docsrcid) throws DocsourceManagerException;

}