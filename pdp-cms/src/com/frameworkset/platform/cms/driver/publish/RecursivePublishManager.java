package com.frameworkset.platform.cms.driver.publish;

import java.util.List;
import java.util.Set;

import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.sitemanager.Site;

/**
 * 递归发布管理类，主要是记录当前的发布元素和发布对象之间的关系，
 * 以便监控到发布元素的内容发生变化时，系统自动发布与该元素相关的发布对象
 * 删除站点、频道和文档时需要将与之相关的数据删除
 * <p>Title: RecursivePublishManager</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-6-14 17:15:27
 * @author biaoping.yin
 * @version 1.0
 */
public interface RecursivePublishManager extends java.io.Serializable {
	/**站点首页   如果引用的对象发生变化就发布站点首页*/
	public static final int PUBOBJECTTYPE_SITE = 0;
	/**频道首页   如果引用的对象发布变化就发布频道首页*/
	public static final int PUBOBJECTTYPE_CHANNEL = 1;
	/**页面发布   如果引用的对象发布变化就重新发布页面*/
	public static final int PUBOBJECTTYPE_PAGE = 2;
	/**频道文档引用频道概览   如果频道的文档发生变化就发布同频道下所有已发布的其他文档*/
	public static final int PUBOBJECTTYPE_CHANNELDOCUMENT = 3;
	
	/**频道文档引用其他元素*/
	public static final int PUBOBJECTTYPE_DOCUMENT = 4;
	/**其他*/
	public static final int PUBOBJECTTYPE_OTHER = 5;
	/**
	 * 站点类型
	 */
	public static final int REFOBJECTTYPE_SITE = 0;

	/**频道*/
	public static final int REFOBJECTTYPE_CHANNEL = 1;
	
	/**
	 * 页面类型
	 */
	public static final int REFOBJECTTYPE_PAGE = 2;
	
	/**文档*/
	public static final int REFOBJECTTYPE_DOCUMENT = 3;
	
	/**
	 * 引用文档相关的频道的发布对象
	 */
	public static final int REFOBJECTTYPE_CHANNEL_ANSESTOR = 4;
	
	public static final int REFOBJECTTYPE_CHANNELDOCUMENT = 5;
	
	
	
	
	/**
	 * 需要进行递归发布的操作
	 */
	
	/* 改变文档状态操作引发的递归 */
	public static final int ACTIONTYPE_ARCHIVE = 0;//归档处理
	
	public static final int ACTIONTYPE_GARBAGE = 1;//回收处理
	
	public static final int ACTIONTYPE_WITHDRAW_PUBLISH = 2;//撤销发布处理
	
	public static final int ACTIONTYPE_DELETE = 3;//删除处理
	
	/* 改变置顶文档状态操作引发的递归 */
	public static final int RECURSIVE_PUB_ADD_ARRANGEDOC = 4;//递归发布情景一：新增加置顶文档
	
	public static final int RECURSIVE_PUB_CHANGE_ARRANGEDOCNO = 5;//递归发布情景二：更改置顶文档的排序
	
	public static final int RECURSIVE_PUB_UPDATE_ARRANGEDOC = 6;//递归发布情景三：更新置顶时间
	
	public static final int RECURSIVE_PUB_DEL_ARRANGEDOC = 7;//递归发布情景四：删除置顶

	
 	
	/**
	 * 记录系统发布对象与发布元素之间的关系，当
	 * 发布元素的内容发生变化时发布相关的发布对象
	 * 本方法在发布时调用，执行时需要跟踪本关系是否已经存在
	 * 如果存在则不需要处理，否则需要处理 
	 * @param context
	 * @param pubObjectReference
	 */
	public void trace(Context context,PubObjectReference pubObjectReference ) throws RecursivePublishException;
	
	/**
	 * 记录系统发布对象与发布元素集合中各元素之间的关系，当其中
	 * 发布元素的内容发生变化时发布相关的发布对象
	 * 本方法在发布时调用，执行时需要跟踪本关系是否已经存在
	 * 如果存在则不需要处理，否则需要处理
	 * 采用批量插入的方法实现
	 * @param context
	 * @param pubObjectReferences
	 */
	public void trace(Context context,Set pubObjectReferences ) throws RecursivePublishException;
	
	/**
	 * 判断发布对象和发布元素的关系是否存在
	 * @param context
	 * @param pubObjectReference
	 * @return 存在时返回true，不存在时返回false
	 */
	public boolean exist(Context context,PubObjectReference pubObjectReference) throws RecursivePublishException;
	
	/**
	 * 移除元素和发布对象之间的关系
	 * @param context
	 * @param pubObjectReference
	 */
	public void remove(Context context,PubObjectReference pubObjectReference) throws RecursivePublishException;
	
	/**
	 * 删除和引用元素相关的所有发布对象
	 * @param context
	 * @param refobject
	 * @param refobjectType
	 */
	public void removeAllPubObjectOfRefelement(Context context,String refobject,String refobjectType)throws RecursivePublishException;
	
	/**
	 * 删除和引用元素相关的所有发布对象，用户文档的批量删除处理
	 * @param context
	 * @param refobjects
	 * @param refobjectType
	 */
	public void removeAllPubObjectOfRefelement(Site site,List refobjects,String refobjectType)throws RecursivePublishException;
	
	
	/**
	 * 删除和引用元素相关的所有发布对象，用户文档的批量删除处理
	 * @param context
	 * @param refobjects
	 * @param refobjectType
	 */
	public void removeAllPubObjectOfRefelement(Site site,String refobject,String refobjectType)throws RecursivePublishException;
	
	/**
	 * 递归发布文档对应的发布对象,
	 * @param context
	 * @param refdocument
	 * @parma actiontype 对文档采取的操作
	 */
	public void recursivePubObjectOfDocument(Context context,String refdocument,String actiontype) throws RecursivePublishException;
	
	/**
	 * 递归发布频道对应的发布对象
	 * @param context
	 * @param refchannel
	 */
	public void recursivePubObjectOfChannel(Context context,String refchannel) throws RecursivePublishException;	
	
	
	/**
	 * 获取文档相关的所有的相关的发布对象
	 * @param context
	 * @param refdocument
	 * @return
	 * @throws RecursivePublishException
	 */
	public Set getAllPubObjectsOfDocument(Context context, String refdocument) throws RecursivePublishException;
	
	/**
	 * 获取频道相关的发布对象
	 * @param context
	 * @param refchannel
	 * @return
	 */
	public Set getAllPubObjectsOfChannel(Context context,  String refchannel) throws RecursivePublishException;
	
	
	/**
	 * 获取频道下文档相关的发布对象，然后进行发布
	 * @param context 系统上下文
	 * @param ancesterchannel 文档对应的频道
	 * @return
	 */
	public Set getAllPubObjectsOfChannelAncester(Context context,  String ancesterchannel) throws RecursivePublishException;

	/**
	 * 获取页面相关的发布对象
	 * @param context
	 * @param pagePath
	 * @return
	 */
	public Set getAllPubObjectsOfPage(Context context, String pagePath)throws RecursivePublishException;
	
	/**
	 * 获取站点相关的发布对象
	 * @param context
	 * @param site 站点英文名称
	 * @return
	 */
	public Set getAllPubObjectsOfSite(Context context, String site)throws RecursivePublishException;
	
	/**
	 * 删除站点site中发布对象所引用的界面元素,执行删除已发文档操作时，然后进行文档联动发布操作，最后调用
	 * 本方法清除发布关系表中记录的该文档细览页面和其他引用元素之间的关系。
	 * @param site
	 * @param pubdocuments
	 * @param pubobjecttype
	 * @throws RecursivePublishException
	 * @author 陶格
	 */
	public void deleteRefObjectsOfPubobject(Site site,List pubdocuments,int pubobjecttype)throws RecursivePublishException;

	/**
	 * 删除站点site中发布对象所引用的界面元素,执行删除已发文档操作时，然后进行文档联动发布操作，最后调用
	 * 本方法清除发布关系表中记录的该文档细览页面和其他引用元素之间的关系。
	 * @param site
	 * @param pubdocuments
	 * @param pubobjecttype
	 * @throws RecursivePublishException
	 * @author 陶格
	 */
	public void deleteRefObjectsOfPubobject(Site site,String pubdocument,int pubobjecttype)throws RecursivePublishException;

	/**
	 * 删除站点site中发布对象所引用的界面元素
	 * @param site
	 * @param pubdocuments
	 * @param pubobjecttype
	 * @throws RecursivePublishException
	 * @author 陶格
	 */
	public void deleteRefObjectsOfPubobject(Context context,String pubobject,int pubobjecttype) throws RecursivePublishException;
	/**
	 * 获取和文档数组中所有需要发布的递归发布对象
	 * @param context 上下文信息，包含站点，站点对应的数据库信息
	 * @param refdocuments 要做递归发布的文档文档数据
	 * @return
	 */
	public Set getAllPubObjectsOfDocuments(Context context, String[] refdocuments)throws RecursivePublishException;
	
	public void recordRecursivePubObj(Context context,String refobj,int reftype,String refsite)throws RecursivePublishException;
	

	/**
	 * 获取已删除文档的递归发布对象
	 * @param context 上下文实例
	 * @param document 文档对象
	 */
	public Set getAllPubObjectsOfDocument(Context context, Document document)throws RecursivePublishException;
	
	/**
	 * 获取已删除文档集合的递归发布对象
	 * @param context 上下文实例
	 * @param documents 文档对象 List<Document>
	 */
	public Set getAllPubObjectsOfDocuments(Context context, List documents)throws RecursivePublishException;
	public void removeAllPubObjectOfRefelement(Site site,
			String[] refobjects, String refobjectType)
			throws RecursivePublishException ;

	public void deleteRefObjectsOfPubobject(Site site, String[] refdocuments, int pubobjecttype)throws RecursivePublishException ;
	
}

