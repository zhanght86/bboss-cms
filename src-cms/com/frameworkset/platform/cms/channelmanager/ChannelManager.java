package com.frameworkset.platform.cms.channelmanager;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.event.Notifiable;

import com.frameworkset.platform.cms.channelmanager.bean.ChannelCondition;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.util.ListInfo;

public interface ChannelManager extends Notifiable {
	/**
	 * 判断频道是否存在
	 * @param parentChannelId TODO
	 * @param channelName TODO
	 */
	public boolean channelIsExist(String siteId, String parentChannelId, String channelName)
			throws ChannelManagerException;

	/**
	 * 新建频道
	 */
	public void createChannel(Channel channel) throws ChannelManagerException;

	/**
	 * 更新频道信息
	 */
	public boolean updateChannel(Channel channel)
			throws ChannelManagerException;

	/**
	 * 删除频道
	 */
	public boolean deleteChannel(HttpServletRequest request,String channelId,String siteId) throws ChannelManagerException;
	
	public boolean deleteChannel(String channelId
			) throws ChannelManagerException ;

	/**
	 * 根据频道id查找频道信息,如果没有找到,返回null
	 */
	public Channel getChannelInfo(String channelId) throws ChannelManagerException;

	/**
	 * 取某个频道的父频道信息,如果没有找到,返回null
	 */
	public Channel getParentChannelInfo(String channelId)
			throws ChannelManagerException;

	/**
	 * 频道启用
	 */

	public boolean startChannel(String channelId)
			throws ChannelManagerException;
	/**
	 * 判断站点是否是活动的，即站点的status是否为0（正常）
	 * @author xinwang.jiao
	 * @return boolean
	 * @throws ChannelManagerException
	 */
	public boolean isActive(String channelId) throws ChannelManagerException;
	
	/**
	 * 改变频道的流程
	 */
	public boolean changeWorkflow(String channalId,int newFlowId,int workflowIsFromParent) throws ChannelManagerException ;
	
	/**
	 * 改变频道的概览图片
	 * @param channalId
	 * @param outlinePicture
	 * @return
	 * @throws ChannelManagerException
	 */
	public boolean changeIndexPic(String channalId, String outlinePicture) throws ChannelManagerException;
	
	/**
	 * 判断频道有没有子频道
	 */
	public boolean hasSubChannel(String channelId)
			throws ChannelManagerException;

	/**
	 * 根据频道id取直接子频道
	 */
	public List getDirectSubChannels(String channelId)
			throws ChannelManagerException;
	/**
	 * 根据频道显示名称取直接子频道
	 */
	public List getDirectSubChannels(String siteid,String displayName,int count)
			throws ChannelManagerException;	


	/**
	 * 根据频道显示名称取直接子频道，该频道必须有首页
	 */
	public List getDirectSubNaviChannels(String siteid,String displayName,int count)
			throws ChannelManagerException;
	/**
	 * 根据频道id取所有子频道
	 */
	public List getAllSubChannels(String channelId)
			throws ChannelManagerException;
	
	

	/**
	 * 获取频道文档可发布的状态
	 * 
	 * @return Collection<DocumentStatus>
	 */
	public List getSupportedChannelPublishStatus(String channelId)throws ChannelManagerException;

	/**
	 * 获取频道审核人列表
	 */
	public List getAuditorList(String channelId)throws ChannelManagerException;
	/**
	 * 获取具有审核权限的角色列表，
	 * 包括频道文档、站点文档两种资源类型，对应于频道、站点两中资源
	 * @param channelId
	 * @return
	 * @throws ChannelManagerException
	 */
	public List getAuditRoleList(String channelId) throws ChannelManagerException;
	/**
	 * 获取具有审核权限的机构列表
	 * 包括频道文档、站点文档两种资源类型，对应于频道、站点两中资源
	 * @param channelId
	 * @return
	 * @throws ChannelManagerException
	 */
	public List getAuditOrgList(String channelId) throws ChannelManagerException;
	/**
	 * 获取具有文档发布权限的角色列表
	 * 包括频道文档、站点文档两种资源类型，对应于频道、站点两中资源
	 * @param channelId
	 * @return
	 * @throws ChannelManagerException
	 */
	public List getPublishRoleList(String channelId) throws ChannelManagerException;
	/**
	 * 获取具有文档发布权限的机构列表
	 * 包括频道文档、站点文档两种资源类型，对应于频道、站点两中资源
	 * @param channelId
	 * @return
	 * @throws ChannelManagerException
	 */
	public List getPublishOrgList(String channelId) throws ChannelManagerException;
	/**
	 * 获取具有文档发布权限的发布人列表
	 */
	public List getPublisherList(String channelId) throws ChannelManagerException;
	
	/**
	 * 根据用户id取有审核权限频道列表
	 */
	public List getChnlList(String userId)throws ChannelManagerException;
	/**
	 * 获取所有频道列表
	 */
	public List getAllChnlList()throws ChannelManagerException;
	
	public int[] getChnlDistributeManners(String channelId)throws ChannelManagerException;
	
	/**
	 * 返回当前频道的流程id,和名称.没有的话返回null
	 */
	public List getFlowInfo(String channelId)throws ChannelManagerException;

	public List getAllUNPublishDocumentOfChannel(String channelID);
	
	/**
	 * 获取频道的概览模板
	 */
	public Template getOutlineTemplateOfChannel(String channelId) throws ChannelManagerException;;
	
	/**
	 * 获取频道的细览模板
	 */
	public Template getDetailTemplateOfChannel(String channelId) throws ChannelManagerException;

	public List getAllCanPubDocsOfChnl(String channelId)
			throws ChannelManagerException;

	public List getIncCanPubDocsOfChnl(String channelId)
			throws ChannelManagerException;
	
	/**
	 * 频道是否设置了细览模板
	 * @param channalId
	 * @return
	 * @throws ChannelManagerException
	 */
	public boolean hasSetDetailTemplate(String channalId)throws ChannelManagerException;
	
	/**
	 * 频道是否设置了概览模板
	 * @param channalId
	 * @return
	 * @throws ChannelManagerException
	 */
	public boolean hasSetOutlineTemplate(String channalId)throws ChannelManagerException;
	/**
	 * 获取某个频道的引用文档集合
	 * return List<Document>
	 * param chnId(频道id)
	 */
	public List getRefDocsOfChnl(String chnlId) throws ChannelManagerException;
	public void logCreateChannel(Channel channel,HttpServletRequest request, HttpServletResponse response) throws ChannelManagerException ;
	public boolean logDeleteChannel(String channelId,HttpServletRequest request, HttpServletResponse response) throws ChannelManagerException ;
	public boolean logUpdateChannel(Channel channel,HttpServletRequest request, HttpServletResponse response) throws ChannelManagerException ;
	
	public boolean updateChannelOutputTemplateId(int channelId,int ouputTemplateId) throws ChannelManagerException;
	public boolean updateChannelDetailTemplateId(int channelId,int detailTemplateId) throws ChannelManagerException;
	
	public String getChannelAbsolutePath(String channelId)throws ChannelManagerException;
	
	
	
	/**
	 * 是否有子频道
	 * @author ge.tao
	 * @param channel
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean hasChildChannel(Channel channel) throws Exception;
	
	/**
	 * 取得子频道
	 * @author ge.tao
	 * @param channel
	 * @param levelDegree
	 * @return List
	 * @throws Exception
	 */
	public List getChildChannel(Channel channel,int levelDegree) throws Exception;
	/**
	 * 取得该频道下所有有权限的用户id
	 * @author xinwang.jiao
	 * @param channelId
	 * @return String[] String[0] 为userids,String[1] 为usernames，String[2] 为“user”
	 * @throws ChannelManagerException
	 */
	public String[] getUsersOfChl(String channelId) throws ChannelManagerException;
	/**
	 * 取得该站点下所有有权限的角色id
	 * @author xinwang.jiao
	 * @param channelId
	 * @return String[] String[0] 为角色id,String[1] 为角色名称，String[2] 为“role”
	 * @throws SiteManagerException
	 */
	public String[] getRolesOfChl(String channelId) throws ChannelManagerException;
	
	/**
	 * 获取制定频道下的所有有权限的机构id
	 * @param channelId
	 * @return String[] String[0] 为角色id,String[1] 为角色名称，String[2] 为“organization”
	 * @throws ChannelManagerException
	 */
	public String[] getOrgsOfChl(String channelId) throws ChannelManagerException;
	
	/**
	 * 判断是否已存在该频道显示名称（DisplayName）
	 * 频道显示名称唯一性判断
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return boolean (存在返回true，不存在返回false)
	 * @throws ChannelManagerException
	 */
	public boolean hasSameDisplayName(String siteid,String DisplayName) throws ChannelManagerException;
	
	/**
	 * Update时判断是否已存在该频道显示名称（DisplayName）
	 * 频道显示名称唯一性判断
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @param long chlId
	 * @return boolean (存在返回true，不存在返回false)
	 * @throws ChannelManagerException
	 */
	public boolean hasSameDisplayNameForUpdate(String siteid,String DisplayName,long chlId) throws ChannelManagerException;
	
	/**
	 * 根据频道显示名称获取该频道的信息
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return <Channel>
	 * @throws ChannelManagerException
	 */
	public Channel getChannelInfoByDisplayName(String siteid,String DisplayName) throws ChannelManagerException;
	
	/**
	 * 通过频道显示名称获取频道最近发布的相应数目的文档列表
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @param int count（指定返回的文档数目）
	 * @return ListInfo<Document>
	 * @throws ChannelManagerException
	 */
	public List getLatestPubDocList(String siteid,String DisplayName,int count) throws ChannelManagerException;
	
	/**
	 * 按 文档发布时间 排序 不翻页!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的文档列表
	 * 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @param int count（指定返回的文档数目）
	 * @return List<Document>
	 * @throws ChannelManagerException
	 * add by xinwang.jiao 2007.6.11
	 * 
	 */
	public List getLatestPubAndPubingDocList(String siteid,String DisplayName,int count) throws ChannelManagerException;
	
	/**
	 * 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @param int count（指定返回的文档数目）
	 * @return List<Document>
	 * @throws ChannelManagerException
	 */
	public ListInfo getLatestPubDocList(String siteid,String DisplayName,int offset,int count) throws ChannelManagerException;
	
	/**
	 * 按 文档发布时间 排序 翻页!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 * add by xinwang.jiao 2007.6.11
	 */
	public ListInfo getLatestPubAndPubingDocList(String siteid,String DisplayName,int offset,int maxItem) throws ChannelManagerException;
	
	
	/**
	 * 按 文档发布时间 排序 翻页!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 排除filters数组中的文档
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 * add by xinwang.jiao 2007.6.11
	 */
	public ListInfo getLatestPubAndPubingDocListByFilter(String siteid,String DisplayName,int offset,int maxItem,String[] filterIDs) throws ChannelManagerException;
	
	
	/**
	 * 按 文档发布时间 排序 列表!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 排除filters数组中的文档
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 * add by xinwang.jiao 2007.6.11
	 */
	public List getLatestPubAndPubingDocListByFilter(String siteid,String DisplayName,int count,String[] filterIDs) throws ChannelManagerException;
	
	/**
	 * 通过频道显示名称获取频道的父频道信息,如果没有父频道,将返回null
	 */
	public Channel getParentChannelInfoByDisplayName(String siteid,String channelDisplayName)
	throws ChannelManagerException;
	
	/**
	 * 判断文档是否可以发布
	 * @param docid
	 * @param increament
	 * @return
	 */
	public boolean canPublishDocument(String docid,boolean increament);
	
	/**
	 * 通过频道显示名称获取子频道中最近发布的相应数目的分页文档列表
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 */
	public List getLatestDocListOfSubChnls(String siteid,String fatherDisplayName,int count,String doctype) throws ChannelManagerException;
	
	/**
	 * 翻页!!!
	 * 通过频道显示名称获取子频道中最近发布的相应数目的分页文档列表
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @param siteid
	 * @param fatherDisplayName
	 * @param offset
	 * @param maxpageitems
	 * @return
	 * @throws ChannelManagerException ListInfo
	 * ChannelManager.java
	 * 陶格
	 */
	public ListInfo getLatestDocListOfSubChnls(String siteid,String fatherDisplayName,long offset,int maxpageitems,String docType) throws ChannelManagerException;
	 /**
	  * 根据文档id获取文档所属的频道
	  * @param docid
	  * @return
	  */
	public Channel getChannelOfDocument(String docid);
	
	public boolean doesHaveHomePageOfChnl(Channel channel) throws ChannelManagerException;
	
	/**
	 * 获得频道下所有发布的带主题图片的文档
	 * @param siteid
	 * @param DisplayName
	 * @param count
	 * @return
	 * @throws ChannelManagerException
	 */
	public List getPubDocListOfChannel(String siteid,String DisplayName,int count) throws ChannelManagerException;
	
	/**
	 * 移动频道
	 * @param String fromId（源频道）
	 * @param String toId（目的频道）
	 * @return boolean
	 * @throws ChannelManagerException
	 */
	public boolean moveChannel(String fromId,String toId) throws ChannelManagerException;
	
	/**
	 * 根据频道显示名称取直接子频道，该频道必须有首页 kai.hu 070530
	 */
	public ListInfo getDirectSubNaviChannels(String siteid,String displayName,long offset, int pageitems)
			throws ChannelManagerException;
	
	/**
	 * 根据频道显示名称取直接子频道，该频道必须有首页 kai.hu 070530
	 */
	public ListInfo getDirectSubChannels(String siteid,String displayName,long offset, int pageitems)
			throws ChannelManagerException;
	/**
	 * 将频道下所有未发布能发布文档的状态修改为已发布
	 * @param channel	频道显示名称
	 */
	public void publishUnpublishDocs(String channel) throws ChannelManagerException;
	/**
	 * 将频道下所有未发布能发布文档的状态修改为已发布
	 * @param channelId
	 * @throws ChannelManagerException
	 */
	public void publishUnpublishDocs(int channelId) throws ChannelManagerException;
	/**
	 * 按 创建发布时间 排序 不翻页!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的文档列表
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @param siteid
	 * @param DisplayName
	 * @param count
	 * @return List
	 * @throws ChannelManagerException 
	 * ChannelManagerImpl.java
	 * @author: 陶格
	 */
	public List getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int count,String docType) throws ChannelManagerException;
	public List getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int count,String docType,boolean loaddocrelatepic) throws ChannelManagerException;
	/**
	 * 按 文档创建时间 排序 翻页!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * @param siteid
	 * @param DisplayName
	 * @param offset
	 * @param maxItem
	 * @return ListInfo
	 * @throws ChannelManagerException 
	 * ChannelManagerImpl.java
	 * @author: 陶格
	 */
	public ListInfo getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int offset,int maxItem,String doctype) throws ChannelManagerException;
	public ListInfo getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int offset,int maxItem,String doctype,boolean loaddocrelatepic) throws ChannelManagerException;
	/**
	 * 通过频道显示名称,获取兄弟频道列表 翻页
	 * @param siteid
	 * @param displayName
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws ChannelManagerException 
	 * ChannelManager.java
	 * @author: 陶格
	 */
	public ListInfo getBrotherChannels(String siteid,String displayName,long offset, int pageitems)throws ChannelManagerException;
	/**
	 * 通过频道显示名称,获取兄弟频道列表 不翻页
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws ChannelManagerException 
	 * ChannelManager.java
	 * @author: 陶格
	 */
	public List getBrotherChannels(String siteid,String displayName,int count)throws ChannelManagerException;
	
	/**
	 * 通过频道显示名称,获取父频道的兄弟频道列表 翻页
	 * @param siteid
	 * @param displayName
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws ChannelManagerException 
	 * ChannelManager.java
	 * @author: 陶格
	 */
	public ListInfo getParentBrotherChannels(String siteid,String displayName,long offset, int pageitems)throws ChannelManagerException;
	
	/**
	 * 通过频道显示名称,获取父频道的兄弟频道列表 不翻页
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws ChannelManagerException 
	 * ChannelManager.java
	 * @author: 陶格
	 */
	public List getParentBrotherChannels(String siteid,String displayName,int count)throws ChannelManagerException;
	/**
	 * 获取频道id为channelId的频道中所有与频道设定的细览模板一致的已发文档列表
	 * add by xinwang.jiao 2007.06.25
	 * @param String channelId
	 * @return List<Document>
	 * @throws ChannelManagerException
	 * 
	 */
	public List getPubDocWithSameTplOfChannel(String channelId,String excludedocids[])throws ChannelManagerException;

	/**
	 * 获取站点频道中下带主题图片的文档分页列表信息
	 * @param siteid
	 * @param channel
	 * @param offset
	 * @param maxpagesize
	 * @return
	 */
	public ListInfo getPubDocListOfChannel(String siteid, String channel, long offset, int maxpagesize)  throws ChannelManagerException ;
	
	/**
	 * 根据频道id获取频道所属的站点id
	 * @param channelid
	 * @return
	 * added by biaoping.yin on 2007.9.20
	 */
	public String getSiteIDOfchannel(String channelid) throws ChannelManagerException;

	/**
	 * 判断给定的频道是否存在
	 * @param channelID
	 * @return
	 */
	public boolean channelIsExist(String channelID);
	
	
	/**
	 * 按 文档创建时间,发布时间,id 排序 翻页!!! 通过频道显示名称获取频道最近发布的相应数目的分页文档附件列表
	 * (对于那些包含于聚合文档的文档不在其列) 最近发布的相应数目的文档附件列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param offset
	 * @param maxItem
	 * @return ListInfo
	 * @throws ChannelManagerException
	 *             ChannelManagerImpl.java
	 * @author: biaoping.yin added on 2007.11.18
	 */
	public ListInfo getLatestPubDocAttachListOrderByDocwtime(String siteid,
			String DisplayName, int offset, int maxItem,String docType)
			throws ChannelManagerException;
	
	/**
	 * 按 创建时间,编稿时间 排序 不翻页!!! 通过频道显示名称获取频道最近发布的相应数目的文档附件列表 (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param count
	 * @return List
	 * @throws ChannelManagerException
	 *             ChannelManagerImpl.java
	 * @author: biaoping.yin
	 */
	public List getLatestPubDocAttachListOrderByDocwtime(String siteid,
			String DisplayName, int count,String docType) throws ChannelManagerException;
	
	/**
	 * 查询所有子频道
	 * @param channelId 频道ID
	 * @param offset
	 * @param pagesize
	 * @param condition
	 * @return ListInfo
	 */
	public ListInfo querySubChannels(int offset, int pagesize, ChannelCondition condition) throws SQLException;
	
}
