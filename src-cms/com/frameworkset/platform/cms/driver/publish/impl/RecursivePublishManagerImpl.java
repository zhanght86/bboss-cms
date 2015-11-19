package com.frameworkset.platform.cms.driver.publish.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.context.BatchContext;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.publish.PubObjectReference;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishException;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSDBFunction;

/**
 * 递归发布管理类，主要是记录当前的发布元素和发布对象之间的关系， 以便监控到发布元素的内容发生变化时，系统自动发布与该元素相关的发布对象
 * <p>
 * Title: RecursivePublishManagerImpl
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2007-6-13 17:04:36 
 * @author biaoping.yin
 * @version 1.0
 */
public class RecursivePublishManagerImpl implements RecursivePublishManager {

	/**
	 * 记录系统发布对象与发布元素之间的关系，当 发布元素的内容发生变化时发布相关的发布对象 本方法在发布时调用，执行时需要跟踪本关系是否已经存在
	 * 如果存在则不需要处理，否则需要处理
	 * 
	 * @param context
	 * @param pubObjectReference
	 */
	public void trace(Context context, PubObjectReference pubObjectReference)
			throws RecursivePublishException {
		/*
		 * add by ge.tao date 2007-6-26 判断记录的唯一性
		 */
		try {
			
				/* end add */
				 
				// StringBuffer sql = new StringBuffer().append("insert into
				// td_cms_pubobject_relation")
				// .append("(PUBLISHOBJECT,REFERENCEOBJECT,PUBLISHTYPE,PUBLISH_SITE,")
				// .append("REFERENCETYPE,REFERENCE_SITE) values(?,?,?,?,?,?)");
				// dbUtil.preparedInsert(context.getDBName(), sql.toString());
				// dbUtil.setString(1, pubObjectReference.getPubobject());
				// dbUtil.setString(2, pubObjectReference.getReferenceObject());
				// dbUtil.setInt(3, pubObjectReference.getPubobjectType());
				// dbUtil.setString(4, pubObjectReference.getPubSite());
				// dbUtil.setInt(5, pubObjectReference.getRefobjectType());
				// dbUtil.setString(6, pubObjectReference.getRefSite());//
				// dbUtil.executePrepared();
				
				//add by ge.tao
				//insert into TD_CMS_ADDWATERIMAGE(url,adsfa,adf)
				//(select '1' as url,2 as adsfa from dual where not exists
				//(select * from TD_CMS_ADDWATERIMAGE where url='1'))
				
//				StringBuffer tmp = new StringBuffer();
//				tmp.append("insert into td_cms_pubobject_relation")
//				   .append("(PUBLISHOBJECT,REFERENCEOBJECT,PUBLISHTYPE,PUBLISH_SITE,")
//				   .append("REFERENCETYPE,REFERENCE_SITE) (select ")
//				   .append("'").append(pubObjectReference.getPubobject()).append("' as PUBLISHOBJECT,")
//				   .append("'").append(pubObjectReference.getReferenceObject()).append("' as REFERENCEOBJECT,")
//				   .append("'").append(pubObjectReference.getReferenceObject()).append("' as REFERENCEOBJECT,")
//				   .append("'").append(pubObjectReference.getReferenceObject()).append("' as REFERENCEOBJECT,")
//				   .append("'").append(pubObjectReference.getReferenceObject()).append("' as REFERENCEOBJECT,")
//				   .append("(select )");
				   
				String pubobject = pubObjectReference.getPubobject();
				String referenceObject = pubObjectReference
						.getReferenceObject();
				int pubobjectType = pubObjectReference.getPubobjectType();
				String pubSite = pubObjectReference.getPubSite();
				int refobjectType = pubObjectReference.getRefobjectType();
				String refSite = pubObjectReference.getRefSite();
				CMSDBFunction.recordpubrelation_proc(pubobject,referenceObject,pubobjectType,pubSite,
						refobjectType,refSite);

//				StringBuffer sql = new StringBuffer()
//						.append("insert into td_cms_pubobject_relation")
//						.append("(PUBLISHOBJECT,REFERENCEOBJECT,PUBLISHTYPE,PUBLISH_SITE,")
//						.append("REFERENCETYPE,REFERENCE_SITE) values(")
//						.append("'").append(pubObjectReference.getPubobject())
//						.append("',").append("'").append(
//								pubObjectReference.getReferenceObject())
//						.append("',").append(
//								pubObjectReference.getPubobjectType()).append(
//								",").append("'").append(
//								pubObjectReference.getPubSite()).append("',")
//						.append(pubObjectReference.getRefobjectType()).append(
//								",").append("'").append(
//								pubObjectReference.getRefSite()).append("'")
//						.append(")");
				
		} catch ( Exception e) {
			 
			throw new RecursivePublishException(e);
		}

	}

	/**
	 * 判断发布对象和发布元素的关系是否存在 PUBLISHTYPE REFERENCETYPE 数据库number类型
	 * 
	 * @param context
	 * @param pubObjectReference
	 * @return 存在时返回true，不存在时返回false
	 */
	public boolean exist(Context context, PubObjectReference pubObjectReference)
			throws RecursivePublishException {
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer(
				"select count(PUBLISH_SITE) as num from td_cms_pubobject_relation ")
				.append("where REFERENCEOBJECT='").append(
						pubObjectReference.getReferenceObject()).append(
						"' and PUBLISHTYPE=").append(
						pubObjectReference.getPubobjectType()).append(
						" and PUBLISH_SITE='").append(
						pubObjectReference.getPubSite()).append(
						"' and PUBLISHOBJECT='").append(
						pubObjectReference.getPubobject()).append(
						"' and REFERENCETYPE=").append(
						pubObjectReference.getRefobjectType()).append(
						" and REFERENCE_SITE='").append(
						pubObjectReference.getRefSite()).append("'");

		try {
			dbUtil.executeSelect(context.getDBName(), sql.toString());
			return dbUtil.getInt(0, 0) > 0;
		} catch (SQLException e) {
			throw new RecursivePublishException("判断"
					+ pubObjectReference.toString() + "是否存在异常："
					+ e.getMessage());
		}

	}

	/**
	 * 移除元素和发布对象之间的关系
	 * 
	 * @param context
	 * @param pubObjectReference
	 */
	public void remove(Context context, PubObjectReference pubObjectReference)
			throws RecursivePublishException {
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		String sql = "delete from td_cms_pubobject_relation where PUBLISHOBJECT=? and REFERENCEOBJECT=? and PUBLISHTYPE=? and PUBLISH_SITE=? and REFERENCETYPE=? and REFERENCE_SITE=?";

		try {
			dbUtil.preparedDelete(context.getDBName(), sql);
			dbUtil.setString(1, pubObjectReference.getPubobject());
			dbUtil.setString(2, pubObjectReference.getReferenceObject());
			dbUtil.setInt(3, pubObjectReference.getPubobjectType());
			dbUtil.setString(4, pubObjectReference.getPubSite());
			dbUtil.setInt(5, pubObjectReference.getRefobjectType());
			dbUtil.setString(6, pubObjectReference.getRefSite());

			dbUtil.executePrepared();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 删除和引用元素相关的所有发布对象
	 * 
	 * @param context
	 * @param refobject
	 * @param refobjectType
	 */
	public void removeAllPubObjectOfRefelement(Context context,
			String refobject, String refobjectType)
			throws RecursivePublishException {
		StringBuffer sql = new StringBuffer(
				"delete from td_cms_pubobject_relation where REFERENCEOBJECT='")
				.append(refobject).append("' and PUBLISH_SITE='").append(
						context.getSite().getSecondName()).append("'").append(
						" and REFERENCETYPE=").append(refobjectType);
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.executeDelete(context.getDBName(), sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void removeAllPubObjectOfRefelement(Site site, String refobject, String refobjectType) throws RecursivePublishException {
		StringBuffer sql = new StringBuffer(
		"delete from td_cms_pubobject_relation where REFERENCEOBJECT='")
		.append(refobject).append("' and PUBLISH_SITE='").append(
				site.getSecondName()).append("'").append(
				" and REFERENCETYPE=").append(refobjectType);
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.executeDelete(site.getDbName(), sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 删除和引用元素相关的所有发布对象
	 * 
	 * @param dbname
	 * @param refobject
	 * @param refobjectType
	 */
	public void removeAllPubObjectOfRefelement(Site site,
			List refobjects, String refobjectType)
			throws RecursivePublishException {
		if(refobjects == null || refobjects.size() == 0)
			return ;
		StringBuffer sql = new StringBuffer(
				"delete from td_cms_pubobject_relation where REFERENCEOBJECT in (");
		for(int i = 0; i < refobjects.size(); i ++)
		{
			if(i > 0)
				sql.append(",'")
				   .append(refobjects.get(i).toString())
				   .append("'");
			else
				sql.append("'")
				   .append(refobjects.get(i).toString())
				   .append("'");
			
		}
		sql.append(") and PUBLISH_SITE='")
		   .append(site.getSecondName())
		   .append("'")
		   .append(" and REFERENCETYPE=")
		   .append(refobjectType);
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.executeDelete(site.getDbName(), sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 删除和引用元素相关的所有发布对象
	 * 
	 * @param dbname
	 * @param refobject
	 * @param refobjectType
	 */
	public void removeAllPubObjectOfRefelement(Site site,
			String[] refobjects, String refobjectType)
			throws RecursivePublishException {
		if(refobjects == null || refobjects.length == 0)
			return ;
		StringBuffer sql = new StringBuffer(
				"delete from td_cms_pubobject_relation where REFERENCEOBJECT in (");
		for(int i = 0; i < refobjects.length; i ++)
		{
			if(i > 0)
				sql.append(",'")
				   .append(refobjects[i])
				   .append("'");
			else
				sql.append("'")
				   .append(refobjects[i])
				   .append("'");
			
		}
		sql.append(") and PUBLISH_SITE='")
		   .append(site.getSecondName())
		   .append("'")
		   .append(" and REFERENCETYPE=")
		   .append(refobjectType);
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.executeDelete(site.getDbName(), sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * 获取已删除文档的递归发布对象
	 * @param context 上下文实例
	 * @param document 文档对象
	 */
	public Set getAllPubObjectsOfDocument(Context context, Document document)throws RecursivePublishException {
		StringBuffer sql = new StringBuffer("select t.* from td_cms_pubobject_relation t where t.REFERENCEOBJECT='");
		sql.append(document.getDocument_id())	
		.append("' and t.REFERENCETYPE='")
		.append(REFOBJECTTYPE_DOCUMENT)
		.append("' union ")
		.append("select a.* from td_cms_pubobject_relation a where a.REFERENCEOBJECT='")
		.append(document.getChanel_id()).append("'")
		.append(" and a.REFERENCETYPE='")
		.append(REFOBJECTTYPE_CHANNEL_ANSESTOR)
		.append("'");
		DBUtil dbUtil = new DBUtil();

		try {
			Set list = new TreeSet();
			dbUtil.executeSelect(context.getDBName(), sql.toString());
			for (int i = 0; i < dbUtil.size(); i++) {
				PubObjectReference pubObjectReference = new PubObjectReference();
				pubObjectReference.setPubobject(dbUtil.getString(i,
						"PUBLISHOBJECT"));
				pubObjectReference.setPubobjectType(dbUtil.getInt(i,
						"PUBLISHTYPE"));
				pubObjectReference.setReferenceObject(dbUtil.getString(i,
						"REFERENCEOBJECT"));
				pubObjectReference.setRefobjectType(dbUtil.getInt(i,
						"REFERENCETYPE"));
				pubObjectReference.setPubSite(dbUtil.getString(i,
						"PUBLISH_SITE"));
				pubObjectReference.setRefSite(dbUtil.getString(i,
						"REFERENCE_SITE"));
				pubObjectReference.setForpublish(true);
				list.add(pubObjectReference);
			}
			return list;
		} catch (SQLException e) {
			throw new RecursivePublishException("获取文档所有的相关发布对象异常："
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * 获取已删除文档集合的递归发布对象
	 * @param context 上下文实例
	 * @param documents 文档对象 List<Document>
	 * @throws RecursivePublishException 
	 */
	public Set getAllPubObjectsOfDocuments(Context context, List documents) throws RecursivePublishException {
		if (documents == null || documents.size() == 0)
			return null;
		Set list = new TreeSet();
		if (documents.size() == 1)
		{
			return this.getAllPubObjectsOfDocument(context, (Document)documents.get(0));
		}
		else
		{
			for(int i = 0; i < documents.size(); i ++)
			{
				Set sets = this.getAllPubObjectsOfDocument(context, (Document)documents.get(i));
				list.addAll(sets);
			}
		}
		return list;
		
	}
	/**
	 * 获取文档相关的所有的相关的发布对象，除删除文档以外的情况调用本方法
	 * @param context
	 * @param refdocument
	 * @return
	 * @throws RecursivePublishException
	 */
	public Set getAllPubObjectsOfDocument(Context context, String refdocument)
			throws RecursivePublishException {

		StringBuffer sql = new StringBuffer(
				"select t.* from td_cms_pubobject_relation t where t.REFERENCEOBJECT='");
		sql.append(refdocument)
				// .append( "' and t.PUBLISH_SITE='")
				// .append(context.getSite().getSecondName())
				.append("' and t.REFERENCETYPE=")
				.append(REFOBJECTTYPE_DOCUMENT)
				.append(" union ")
				.append("select a.* from td_cms_pubobject_relation a inner join td_cms_document b on a.REFERENCEOBJECT=b.channel_id")
				.append(DBUtil.getPool(context.getDBName()).getDbAdapter().getOROPR())
				.append("''")
				.append(" where b.document_id=")
				.append(refdocument)
				.append(" and a.REFERENCETYPE=")
				.append(REFOBJECTTYPE_CHANNEL_ANSESTOR)
				//modify by ge.tao
				//2007-7-30
				//引用该文档的发布对象(频道)
				.append(" union ")
				.append("select d.* from td_cms_pubobject_relation d inner join TD_CMS_CHNL_REF_DOC c on d.REFERENCEOBJECT=c.chnl_id")
				.append(DBUtil.getPool(context.getDBName()).getDbAdapter().getOROPR())
				.append("''")
				.append(" where c.doc_id=")
				.append(refdocument)
				.append(" and d.REFERENCETYPE=")
				.append(REFOBJECTTYPE_CHANNEL_ANSESTOR);

		DBUtil dbUtil = new DBUtil();

		try {
			Set list = new TreeSet();
			dbUtil.executeSelect(context.getDBName(), sql.toString());
			for (int i = 0; i < dbUtil.size(); i++) {
				PubObjectReference pubObjectReference = new PubObjectReference();
				pubObjectReference.setPubobject(dbUtil.getString(i,
						"PUBLISHOBJECT"));
				pubObjectReference.setPubobjectType(dbUtil.getInt(i,
						"PUBLISHTYPE"));
				pubObjectReference.setReferenceObject(dbUtil.getString(i,
						"REFERENCEOBJECT"));
				pubObjectReference.setRefobjectType(dbUtil.getInt(i,
						"REFERENCETYPE"));
				pubObjectReference.setPubSite(dbUtil.getString(i,
						"PUBLISH_SITE"));
				pubObjectReference.setRefSite(dbUtil.getString(i,
						"REFERENCE_SITE"));
				pubObjectReference.setForpublish(true);
				list.add(pubObjectReference);
			}
			return list;
		} catch (SQLException e) {
			throw new RecursivePublishException("获取文档所有的相关发布对象异常："
					+ e.getMessage(), e);
		}
		// return getAllPubObjects(context, refdocument,REFOBJECTTYPE_DOCUMENT);

	}
	
	/**
	 * 获取频道下文档相关的发布对象，然后进行发布
	 * @param context 系统上下文
	 * @param ancesterchannel 文档对应的频道
	 * @return
	 */
	public Set getAllPubObjectsOfChannelAncester(Context context,  String ancesterchannel) throws RecursivePublishException
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from td_cms_pubobject_relation a where a.REFERENCEOBJECT='")
		.append(ancesterchannel).append("'")
		.append(" and a.REFERENCETYPE='")
		.append(REFOBJECTTYPE_CHANNEL_ANSESTOR)
		.append("'");
		DBUtil dbUtil = new DBUtil();

		try {
			Set list = new TreeSet();
			dbUtil.executeSelect(context.getDBName(), sql.toString());
			for (int i = 0; i < dbUtil.size(); i++) {
				PubObjectReference pubObjectReference = new PubObjectReference();
				pubObjectReference.setPubobject(dbUtil.getString(i,
						"PUBLISHOBJECT"));
				pubObjectReference.setPubobjectType(dbUtil.getInt(i,
						"PUBLISHTYPE"));
				pubObjectReference.setReferenceObject(dbUtil.getString(i,
						"REFERENCEOBJECT"));
				pubObjectReference.setRefobjectType(dbUtil.getInt(i,
						"REFERENCETYPE"));
				pubObjectReference.setPubSite(dbUtil.getString(i,
						"PUBLISH_SITE"));
				pubObjectReference.setRefSite(dbUtil.getString(i,
						"REFERENCE_SITE"));
				pubObjectReference.setForpublish(true);
				list.add(pubObjectReference);
			}
			return list;
		} catch (SQLException e) {
			throw new RecursivePublishException("获取文档所有的相关发布对象异常："
					+ e.getMessage(), e);
		}
	}

	/**
	 * 递归发布频道对应的发布对象
	 * 
	 * @param context
	 * @param refchannel
	 */
	public void recursivePubObjectOfChannel(Context context, String refchannel)
			throws RecursivePublishException {

	}

	/**
	 * 递归发布文档对应的发布对象,
	 * 
	 * @param context
	 * @param refdocument
	 * @parma actiontype 对文档采取的操作，删除，置顶，撤发，发布
	 */
	public void recursivePubObjectOfDocument(Context context,
			String refdocument, String actiontype)
			throws RecursivePublishException {

	}

	private Set getAllPubObjects(Context context, String refobject, int reftype)
			throws RecursivePublishException {
		String sql = "select * from td_cms_pubobject_relation where REFERENCEOBJECT='"
				+ refobject
				+ "' and PUBLISH_SITE='"
				+ context.getSite().getSecondName()
				+ "' and REFERENCETYPE="
				+ reftype;
		DBUtil dbUtil = new DBUtil();
		try {
			Set list = new TreeSet();
			dbUtil.executeSelect(context.getDBName(), sql);
			for (int i = 0; i < dbUtil.size(); i++) {
				PubObjectReference pubObjectReference = new PubObjectReference();
				pubObjectReference.setPubobject(dbUtil.getString(i,
						"PUBLISHOBJECT"));
				pubObjectReference.setPubobjectType(dbUtil.getInt(i,
						"PUBLISHTYPE"));
				pubObjectReference.setReferenceObject(dbUtil.getString(i,
						"REFERENCEOBJECT"));
				pubObjectReference.setRefobjectType(dbUtil.getInt(i,
						"REFERENCETYPE"));
				pubObjectReference.setPubSite(dbUtil.getString(i,
						"PUBLISH_SITE"));
				pubObjectReference.setPubSite(dbUtil.getString(i,
						"REFERENCE_SITE"));
				pubObjectReference.setForpublish(true);
				list.add(pubObjectReference);
			}
			return list;
		} catch (SQLException e) {
			throw new RecursivePublishException("获取文档所有的相关发布对象异常："
					+ e.getMessage(), e);
		}
	}

	public Set getAllPubObjectsOfChannel(Context context, String refchannel)
			throws RecursivePublishException {

		return getAllPubObjects(context, refchannel, REFOBJECTTYPE_CHANNEL);
	}

	public Set getAllPubObjectsOfPage(Context context, String pagePath)
			throws RecursivePublishException {

		return getAllPubObjects(context, pagePath, REFOBJECTTYPE_PAGE);
	}

	public Set getAllPubObjectsOfSite(Context context, String site)
			throws RecursivePublishException {
		return getAllPubObjects(context, site, REFOBJECTTYPE_SITE);
	} 

	/**
	 * 删除站点site中发布对象所引用的界面元素
	 * @param site
	 * @param pubdocuments
	 * @param pubobjecttype
	 * @throws RecursivePublishException
	 * @author 陶格
	 */
	public void deleteRefObjectsOfPubobject(Site site,List pubdocuments,int pubobjecttype)throws RecursivePublishException
	{
		if(pubdocuments == null || pubdocuments.size() == 0)
			return;
		String siteEname = site.getSecondName();
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete td_cms_pubobject_relation t where ")
		   .append("t.PUBLISHOBJECT in (");
		for(int i = 0; i < pubdocuments.size(); i ++)
		{
			if(i > 0)
				sql.append(",'").append(pubdocuments.get(i).toString())
				   .append("'");
			else
				sql.append("'").append(pubdocuments.get(i).toString())
				   .append("'");
				
		}
		sql.append(") and t.REFERENCE_SITE='")
		   .append(siteEname)
		   .append("' and t.PUBLISHTYPE=")
		   .append(pubobjecttype);
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			
			dbUtil.executeDelete(site.getDbName(),sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteRefObjectsOfPubobject(Site site, String[] pubdocuments, int pubobjecttype)throws RecursivePublishException 
	{
		if(pubdocuments == null || pubdocuments.length == 0)
			return;
		String siteEname = site.getSecondName();
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete td_cms_pubobject_relation t where ")
		   .append("t.PUBLISHOBJECT in (");
		for(int i = 0; i < pubdocuments.length; i ++)
		{
			if(i > 0)
				sql.append(",'").append(pubdocuments[i])
				   .append("'");
			else
				sql.append("'").append(pubdocuments[i])
				   .append("'");
				
		}
		sql.append(") and t.REFERENCE_SITE='")
		   .append(siteEname)
		   .append("' and t.PUBLISHTYPE=")
		   .append(pubobjecttype);
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			
			dbUtil.executeDelete(site.getDbName(),sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteRefObjectsOfPubobject(Site site, String pubobject, int pubobjecttype)throws RecursivePublishException 
	{
		if(pubobject == null )
			return;
		String siteEname = site.getSecondName();
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete td_cms_pubobject_relation t where ")
		   .append("t.PUBLISHOBJECT in (")
		   .append(pubobject)
		   .append(") and t.REFERENCE_SITE='")
		   .append(siteEname)
		   .append("' and t.PUBLISHTYPE=")
		   .append(pubobjecttype);
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			
			dbUtil.executeDelete(site.getDbName(),sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 删除站点名为site中发布对象所引用的界面元素
	 * 
	 * @param context
	 * @param pubobject
	 * @param pubobjtype
	 * @throws RecursivePublishException
	 * @author 陶格
	 */
	public void deleteRefObjectsOfPubobject(Context context, String pubobject,
			int pubobjtype) throws RecursivePublishException {
		String siteEname = context.getSite().getSecondName();
		StringBuffer sql = new StringBuffer();
		sql.append("delete td_cms_pubobject_relation t where ").append(
				"t.PUBLISHOBJECT=? and t.REFERENCE_SITE=? and t.PUBLISHTYPE=?");
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedDelete(context.getDBName(), sql.toString());
			dbUtil.setString(1, pubobject);
			dbUtil.setString(2, siteEname);
			dbUtil.setInt(3, pubobjtype);
			dbUtil.executePrepared();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 记录系统发布对象与发布元素集合中各元素之间的关系，当其中 发布元素的内容发生变化时发布相关的发布对象
	 * 本方法在发布时调用，执行时需要跟踪本关系是否已经存在 如果存在则不需要处理，否则需要处理 采用批量插入的方法实现
	 * 
	 * @param context
	 * @param pubObjectReferences
	 * @author 陶格
	 */
	public void trace(Context context, Set pubObjectReferences)
			throws RecursivePublishException {

		if (!pubObjectReferences.isEmpty()) {
			Iterator it = pubObjectReferences.iterator();
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin();
				while (it.hasNext()) {
					PubObjectReference pubObjectReference = (PubObjectReference) it
							.next();

					String pubobject = pubObjectReference.getPubobject();
					String referenceObject = pubObjectReference
							.getReferenceObject();
					int pubobjectType = pubObjectReference.getPubobjectType();
					String pubSite = pubObjectReference.getPubSite();
					int refobjectType = pubObjectReference.getRefobjectType();
					String refSite = pubObjectReference.getRefSite();
					CMSDBFunction.recordpubrelation_proc(pubobject,referenceObject,pubobjectType,pubSite,
							refobjectType,refSite);
					
				}
				// }
				tm.commit();
			} catch (Exception e) {
				throw new RecursivePublishException(e);
			}
			finally
			{
				tm.release();
			}
		}
	}

	public Set getAllPubObjectsOfDocuments(Context context,
			String[] refdocuments) throws RecursivePublishException {
		if (refdocuments == null || refdocuments.length == 0)
			return null;
		if (refdocuments.length == 1)
			return this.getAllPubObjectsOfDocument(context, refdocuments[0]);
		else
		{
			Set allPubObjectsOfDocuments = new TreeSet();
			
			for(int i = 0; i < refdocuments.length; i ++ )
			{
				Set docs = this.getAllPubObjectsOfDocument(context, refdocuments[i]);
				allPubObjectsOfDocuments.addAll(docs);
				
			}
			return allPubObjectsOfDocuments;
		}
		
	}
	/**
	 * 修改站点首页模板删除站点名为site中发布对象所引用的元素
	 * 
	 * @param siteid
	 * @throws RecursivePublishException
	 * @author 胡灿阳
	 */
	public void deletesiteRelation(String siteid) throws RecursivePublishException {
		StringBuffer sql = new StringBuffer();
		sql.append("delete td_cms_pubobject_relation t where ").append(
				"t.PUBLISHOBJECT=? and t.PUBLISHTYPE=?");
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedDelete(sql.toString());
			dbUtil.setString(1, siteid);
			dbUtil.setInt(2,RecursivePublishManager.PUBOBJECTTYPE_SITE);
			dbUtil.executePrepared();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 修改频道概览频道模板,删除频道id为channelid中发布对象所引用的界面元素
	 * 
	 * @param channelid
	 * @param site
	 * @throws RecursivePublishException
	 * @author 胡灿阳
	 */
	public void deleteChannelOutRelation(String channelid,Site site) throws RecursivePublishException {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("delete td_cms_pubobject_relation t where ").append(
				"t.PUBLISHOBJECT=?  and t.PUBLISHTYPE=? and t.PUBLISH_SITE=?");
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedDelete(sql.toString());
			dbUtil.setString(1, channelid);
			dbUtil.setInt(2, RecursivePublishManager.REFOBJECTTYPE_CHANNEL);
			dbUtil.setString(3,site.getSecondName());
			dbUtil.executePrepared();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 修改频道细览频道模板,删除频道id为channelid中所有继承该频道细览模板的已发布的文档对象所引用的界面元素
	 * 
	 * @param channelid
	 * @param site
	 * @throws RecursivePublishException
	 * @author 胡灿阳
	 */
	public void deleteChannelDetailRelation(String channelid,Site site) throws RecursivePublishException {
		StringBuffer sql = new StringBuffer();
		sql.append("delete td_cms_pubobject_relation t where PUBLISHOBJECT in(").append("select document_id from td_cms_document  where  parent_detail_tpl=1 and status=5 and  channel_id=? )")
		.append(" and t.PUBLISHTYPE=? and t.PUBLISH_SITE=? ");
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedDelete(sql.toString());
			dbUtil.setString(1, channelid);
			dbUtil.setInt(2, RecursivePublishManager.REFOBJECTTYPE_DOCUMENT);
			dbUtil.setString(3,site.getSecondName());
			dbUtil.executePrepared();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 修改文档细览模板,删除文档id为docid中发布对象所引用的界面元素
	 * 
	 * @param docid
	 * @param site
	 * @throws RecursivePublishException
	 * @author 胡灿阳
	 */
	public void deleteDocumentDetailRelation(String docid,Site site) throws RecursivePublishException {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("delete td_cms_pubobject_relation t where ").append(
				"t.PUBLISHOBJECT=?  and t.PUBLISHTYPE=? and t.PUBLISH_SITE=?");
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedDelete(sql.toString());
			dbUtil.setString(1, docid);
			dbUtil.setInt(2, RecursivePublishManager.REFOBJECTTYPE_DOCUMENT);
			dbUtil.setString(3,site.getSecondName());
			dbUtil.executePrepared();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 引用频道文档
	 * 
	 */
	public void recordRecursivePubObj(Context context_o,String refobj,int reftype,String refsite)
	{
		Context _context = context_o;
		if(_context instanceof DefaultContextImpl)
		{
			_context = ((DefaultContextImpl)_context).getOldContext();
		}
		/**
		 * 记录站点首页与首页上元素之间的关系
		 */
		if((_context instanceof CMSContext) && !(_context instanceof BatchContext))
		{
			String pubobj = _context.getSite().getSiteId() + "";
			int pubtype = RecursivePublishManager.PUBOBJECTTYPE_SITE;
			String  pubsite = _context.getSite().getSecondName();				
			PubObjectReference object = new PubObjectReference();
			object.setPubobject(pubobj);
			object.setPubobjectType(pubtype);
			object.setPubSite(pubsite);
			object.setReferenceObject(refobj);
			object.setRefobjectType(reftype);
			object.setRefSite(refsite);		
			_context.addRefObject(object);
		} 
		/**
		 * 记录频道首页与频道首页元素之间的关系
		 */
		else if(_context instanceof ChannelContext)
		{
			String pubobj = ((ChannelContext)_context).getChannelID();
			int pubtype = RecursivePublishManager.PUBOBJECTTYPE_CHANNEL;
			String pubsite = _context.getSite().getSecondName();
			PubObjectReference object = new PubObjectReference();
			object.setPubobject(pubobj);
			object.setPubobjectType(pubtype);
			object.setPubSite(pubsite);
			object.setReferenceObject(refobj);
			object.setRefobjectType(reftype);
			object.setRefSite(refsite);		
			_context.addRefObject(object);
		}
		/**
		 * 记录页面与页面概览元素之间的关系
		 */
		else if(_context instanceof PageContext)
		{
			String pubobj = ((PageContext)_context).getPagePath();
			int pubtype = RecursivePublishManager.PUBOBJECTTYPE_PAGE;
			String pubsite = _context.getSite().getSecondName();
			PubObjectReference object = new PubObjectReference();
			object.setPubobject(pubobj);
			object.setPubobjectType(pubtype);
			object.setPubSite(pubsite);
			object.setReferenceObject(refobj);
			object.setRefobjectType(reftype);
			object.setRefSite(refsite);		
			_context.addRefObject(object);
		}
		/**
		 * 记录文档首页与文档首页概览元素以及相关文档之间的关系，大关系有以下两种：
		 * a.引用对象类型为频道类型-REFOBJECTTYPE_CHANNEL_ANSESTOR
		 * 	1.概览频道与文档的频道一致，一致的情况又分两种
		 *      a.文档的细览模板和文档频道的细览模板一致
		 *      b.文档的细览模板和文档频道的细览模板不一致
		 *      
		 * 	2.概览频道和文档的频道不一致
		 * b.根据引用对象类型为文档类型-REFOBJECTTYPE_DOCUMENT
		 * 递归发布时，需要对这种类型进行，特殊处理
		 * 
		 */
		else if(_context instanceof ContentContext)
		{
			ContentContext contentctx = (ContentContext)_context;
			
//			//非文档类型的处理
//			if(reftype != REFOBJECTTYPE_DOCUMENT)
//			{
//				if(contentctx.getChannelid().equals(refobj))
//				{
//					
//					try {
//						Channel channel_ = CMSUtil.getChannelCacheManager(_context.getSiteID()).getChannel(refobj);				
//						if(contentctx.getDetailTemplate().getTemplateId() != channel_.getDetailTemplateId())
//						{
//							String pubobj = contentctx.getContentid() + "";
//							int pubtype = RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT;
//							String pubsite = _context.getSite().getSecondName();
//							PubObjectReference object = new PubObjectReference();
//							object.setPubobject(pubobj);
//							object.setPubobjectType(pubtype);
//							object.setPubSite(pubsite);
//							object.setReferenceObject(refobj);
//							object.setRefobjectType(reftype);
//							object.setRefSite(refsite);		
//							_context.addRefObject(object);
//						}
//						else
//						{
//							String pubobj = contentctx.getChannelid() ;
//							int pubtype = RecursivePublishManager.PUBOBJECTTYPE_CHANNELDOCUMENT;
//							String pubsite = _context.getSite().getSecondName();
//							PubObjectReference object = new PubObjectReference();
//							object.setPubobject(pubobj);
//							object.setPubobjectType(pubtype);
//							object.setPubSite(pubsite);
//							object.setReferenceObject(refobj);
//							object.setRefobjectType(reftype);
//							object.setRefSite(refsite);		
//							_context.addRefObject(object);
//						}
//					} catch (Exception e) {
//						
//						e.printStackTrace();
//					}
//				}
//				else
//				{
//					try {
//						Channel channel_ = CMSUtil.getChannelCacheManager(_context.getSiteID()).getChannel(contentctx.getChannelid());				
//						if(contentctx.getDetailTemplate().getTemplateId() != channel_.getDetailTemplateId())
//						{
//							String pubobj = contentctx.getContentid() + "";
//							int pubtype = RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT;
//							String pubsite = _context.getSite().getSecondName();
//							PubObjectReference object = new PubObjectReference();
//							object.setPubobject(pubobj);
//							object.setPubobjectType(pubtype);
//							object.setPubSite(pubsite);
//							object.setReferenceObject(refobj);
//							object.setRefobjectType(reftype);
//							object.setRefSite(refsite);		
//							_context.addRefObject(object);
//						}
//						else
//						{
//							String pubobj = contentctx.getChannelid() ;
//							int pubtype = RecursivePublishManager.PUBOBJECTTYPE_CHANNELDOCUMENT;
//							String pubsite = _context.getSite().getSecondName();
//							PubObjectReference object = new PubObjectReference();
//							object.setPubobject(pubobj);
//							object.setPubobjectType(pubtype);
//							object.setPubSite(pubsite);
//							object.setReferenceObject(refobj);
//							object.setRefobjectType(reftype);
//							object.setRefSite(refsite);		
//							_context.addRefObject(object);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			else//文档类型的处理，文档相关文档的情况处理
			{
				String pubobj = contentctx.getContentid() + "";
				int pubtype = RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT;
				String pubsite = _context.getSite().getSecondName();
				PubObjectReference object = new PubObjectReference();
				object.setPubobject(pubobj);
				object.setPubobjectType(pubtype);
				object.setPubSite(pubsite);
				object.setReferenceObject(refobj);
				object.setRefobjectType(reftype);
				object.setRefSite(refsite);		
				_context.addRefObject(object);
			}
		}
	}
	
	
	
	
	

	
}
