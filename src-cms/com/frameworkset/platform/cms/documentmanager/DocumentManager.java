//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemanager\\SiteManager.java
package com.frameworkset.platform.cms.documentmanager;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.documentmanager.bean.ArrangeDoc;
import com.frameworkset.platform.cms.documentmanager.bean.DocAggregation;
import com.frameworkset.platform.cms.documentmanager.bean.DocClass;
import com.frameworkset.platform.cms.documentmanager.bean.DocRelated;
import com.frameworkset.platform.cms.documentmanager.bean.DocTemplate;
import com.frameworkset.platform.cms.documentmanager.bean.DocumentCondition;
import com.frameworkset.platform.cms.documentmanager.bean.TaskDocument;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
/**
 * @author huaihai.ou
 * 日期:Dec 19, 2006
 * 版本:1.0
 * 版权所有:三一重工
 */
public interface DocumentManager extends java.io.Serializable
{
	/**
	 * 新增文档
	 * @return
	 * @throws DocumentManagerException
	 */
	public int creatorDoc(Document doc) throws DocumentManagerException;
	/**
	 * 修改文档
	 * @return
	 * @throws DocumentManagerException
	 */
	public boolean updateDoc(Document doc) throws DocumentManagerException;
	/**
	 * 获取文档状态
	 * @param docId
	 * @return
	 * @throws DocumentManagerException
	 */
	public int getDocStatus(int docId) throws DocumentManagerException;
	
	/**
	 * 获取文档类型
	 * @param docId
	 * @return
	 * @throws DocumentManagerException
	 */
	public int getDocType(int docId) throws DocumentManagerException;
	/**
	 * 获取文档所在频道的id
	 * @param docId
	 * @return
	 * @throws DocumentManagerException
	 */
	public int getDocChnlId(int docId) throws DocumentManagerException;
	/**
	 * 获取文档所在站点的id
	 * @param docId
	 * @return
	 * @throws DocumentManagerException
	 */
	 public int getDocSiteId(int docId) throws DocumentManagerException;
	 /**
	  * 判断当前用户对指定文档是否有指定任务
	  * @param userId
	  * @param docId
	  * @param task 1：审核任务 2：返工任务 3：发布任务
	  * @return 返回任务id，没有则返回0
	  */
	 public int hasTask(int userId,int docId,int task);
	 /**
	  * 判断当前用户对指定文档数组是否有指定任务，返回任务id数组
	  * @param userId
	  * @param docId
	  * @param task
	  * @return
	  */
	 public int[] hasTask(int userId,int docId[],int task);
	 /**
	  * 获取文档所具有的操作列表,{送审，撤消送审，提交发布，归档，转发，保存版本，版本管理，设置置顶，删除}的子集
	  * @param docId
	  * @return
	  * @throws DocumentManagerException
	  */
	 public Map getDocOpers(int docId,AccessControl accessControl) throws DocumentManagerException;
	/**
    * 删除文档,仅仅删除数据库的相关记录
    * @param docid
    * @return
    * @throws DocumentManagerException
    */
    public void deleteDoc(int docid) throws DocumentManagerException;
    /**
     * 批量删除文档,仅仅删除数据库的相关记录
     * @param docid
     * @throws DocumentManagerException
     */
    public void deleteDoc(int[] docids) throws DocumentManagerException;
    /**
     * 删除文档,不仅删除数据库的相关记录，还删除物理文件，包括发布和预览的相关文件
     * @param docid
     * @return
     * @throws DocumentManagerException
     * add by xinwang.jiao 2007.07.05
     */
     public void deleteDoc(HttpServletRequest request,int docid,String siteid) throws DocumentManagerException;
     /**
      * 批量删除文档,不仅删除数据库的相关记录，还删除物理文件，包括发布和预览的相关文件
      * @param docid
      * @throws DocumentManagerException
      * add by xinwang.jiao 2007.07.05
      */
     public void deleteDoc(HttpServletRequest request,int[] docids,String siteid) throws DocumentManagerException;
     
     /**
 	 * 筛选文档，排除那些被频道引用为首页文档的文档
 	 * 这些文档不能进行删除，回收，归档操作。
 	 * @param docids
 	 * @return Map
 	 * key "errormsg" 存放 error.toString());
 	 * key "docids" 存放 docids; 
 	 * @throws DocumentManagerException
 	 */
	public Map eliminateUnOpDoc(int[] docids) throws DocumentManagerException;
 	
     /**
      * 删除文档附件，包括发布和预览的相关文件（同时删除采集目录下的附件）
      * @param request
      * @param docid
      * @param siteid
      * @throws DocumentManagerException
      */
     public void deleteDocAttFiles(HttpServletRequest request,int docid,String siteid) throws DocumentManagerException;
     /**
      * 删除已发文档的附件，包括发布和预览的相关文件（不删除采集目录下的附件），用于文档的撤发
      * @param request
      * @param docid
      * @param siteid
      * @throws DocumentManagerException
      */
     public void deletePubDocAttFiles(HttpServletRequest request,int docid,String siteid) throws DocumentManagerException;
    /**
     * 取所有文档列表
     * @return
     * @throws DocumentManagerException
     */
    public List getDocList() throws DocumentManagerException;
    /**
     * 根据频道id取该频道下面的所有文档列表
     * @param channelid
     * @return
     * @throws DocumentManagerException
     */
    public List getDocList(int channelid) throws DocumentManagerException;
    /**
	 * 根据SQL语法取文档列表，例如：<br>
	
	 * @throws DocumentManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 DocumentManagerException 异常
	 */
	public ListInfo getDocList(String sql,int offset,int maxItem) throws DocumentManagerException;
	/**
	 * 根据SQL语句获取引用文档列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getCitedDocList(String sql,int offset,int maxItem) throws DocumentManagerException;
	/**
	 * 获取引用文档的引用类型
	 * @param citedDocId
	 * @return
	 * @throws DocumentManagerException
	 */
	public int getDocCitedType(String citedDocId) throws DocumentManagerException;
	/**
	 * 获取指定用户的审核列表
	 * @param userid
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getAuditDocList(String sql,int offset,int maxItem) throws DocumentManagerException;
	/**
	 * 获取返工文档列表
	 * @param userid 当前用户
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getReboundtDocList(String sql,int offset,int maxItem) throws DocumentManagerException;
	/**
	 * 获取待发文档列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getPubishDocList(String sql,int offset,int maxItem) throws DocumentManagerException;
	/**
	 * 获取发布中的文档列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getPubishingDocList(String sql,int offset,int maxItem) throws DocumentManagerException;
	/*
	 * 获取已经归档的文档列表
	 */
	public ListInfo getPigeonholeDocList(String sql,int offset,int maxItem) throws DocumentManagerException;
	/**
	 * 根据文档id得文档对象
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public Document getDoc(String docid) throws DocumentManagerException;
	public Document getDocWithNoContent(String docid) throws DocumentManagerException;
	public String getContent(Document doc);
	
	public String getContent(int doc);
	
	/**
	 * 根据环境上下文获取文档对象
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public Document getDoc(ContentContext context) throws DocumentManagerException;
	/**
	 * 将文档丢入垃圾站
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public void garbageDoc(int docid, int userid) throws DocumentManagerException;
	/**
	 * 批量将文档丢入垃圾站
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public void garbageDoc(int[] docids,int userid) throws DocumentManagerException;
	/**
	 * 将文档从垃圾站复原
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public void recoverDoc(int docid) throws DocumentManagerException;
	/**
	 * 批量恢复已经回收的文档
	 * @param docids
	 * @throws DocumentManagerException
	 */
	public void recoverDoc(int[] docids) throws DocumentManagerException;
	/**
	 * 从文档来源表中取文档来源列表
	 * @return
	 * @throws DocumentManagerException
	 */
	 public List getDocSourceList() throws DocumentManagerException;
	 
	 /**
	  * 根据频道id得在该频道下发过文档的用户列表
	  * @param siteid
	  * @return
	  * @throws DocumentManagerException
	  */
	 public List getDistributeList(String channelid) throws DocumentManagerException;
	 /**
	  * 根据频道id获取这个频道所有引用文档的引用人列表
	  * @param channelid
	  * @return
	  * @throws DocumentManagerException
	  */
	 public List getCiteUserList(String channelid) throws DocumentManagerException;
	 /**
	  * 根据文档id获取对该文档有保存版本的保存人列表
	  * @param docid
	  * @return
	  * @throws DocumentManagerException
	  */
	 public List getDocVerUserList(String docid) throws DocumentManagerException;
	 
	 /**
	  * 根据频道id获取这个频道所有引用文档的所在原频道列表
	  * @param channelid
	  * @return
	  * @throws DocumentManagerException
	  */
	 public List getCiteSrcChnlList(String channelid) throws DocumentManagerException;
	 /**
	  * 取文档状态列表
	  * @return
	  * @throws DocumentManagerException
	  */
	 public List getStatusList() throws DocumentManagerException;
	 /**
	  * 获取指定流程的状态列表
	  * @param flowId
	  * @return
	  * @throws DocumentManagerException
	  */
	 public List getStatusList(int flowId) throws DocumentManagerException;
	 
	 /**
	  * 获取聚合文档的文档列表
	  * @param document_id
	  * @return
	  */
	 public List getCompositeDocs(ContentContext contentcontext) throws DocumentManagerException;
	
	/**
	 * 获取多页文档列表
	 * @param document_id
	 * @return 
	 */
	public List getPagingDocs(int documentId) throws DocumentManagerException;
	
	public List getPagingDocs(ContentContext contentcontext);
	/**
	 * 判断指定文档能否进行向指定状态迁移的操作
	 *  能迁移则返回迁移id，不能则返回－1
	 * @param docid
	 * @param destStatus
	 * @return
	 * @throws DocumentManagerException
	 */
	public int canTransition(int docid, int destStatus) throws DocumentManagerException;
	/**
	 * 判断一组文档能否状态迁移，如果有一个不能迁移，则整组都不能进行迁移，需提示用户
	 * 要么全部进行状态迁移操作，要么一个也不进行
	 * 返回一个文档迁移的映射，若能迁移则map的size应该为数组的长度，否则map的size为0
	 * @param docids
	 * @param destStatus
	 * @return
	 * @throws DocumentManagerException
	 */
	public Map canTransition(int[] docids,int destStatus) throws DocumentManagerException;
	/**
	 * 判断指定频道有没有从原状态到目标状态的迁移，
	 * @param channelId
	 * @param srcStatus
	 * @param destStatus
	 * @return 返回迁移id，不能迁移返回－1
	 * @throws DocumentManagerException
	 */
	public int canTransition(int channelId, int srcStatus,int destStatus) throws DocumentManagerException;
	/**
	 * 判断能不能在指定频道的引用指定文档,
	 * 返回为不能引用的文档名称，若长度为0则可以执行引用操作
	 * @param docids
	 * @param channelid
	 * @param citeType  引用类型，0－文档引用；1－频道引用
	 * @return
	 * @throws DocumentManagerException
	 */
	public String canCite(int docids, int channelid,int citeType) throws DocumentManagerException;
	/**
	 * 判断能不能在指定频道批量的引用指定文档，
	 * 返回为不能引用的文档名称串，若长度为0则可以该组文档都能执行引用操作
	 * @param docids
	 * @param channelid
	 * @param citeType  引用类型，0－文档引用；1－频道引用
	 * @return
	 * @throws DocumentManagerException
	 */
	public String canCite(int[] docids, int channelid,int citeType) throws DocumentManagerException;
	/**
	 * 当前用户向指定审核人呈送文档
	 * @param docid 
	 * @param auditorids
	 * @param userid 当前用户
	 * @param preTaskid   呈送这个动作产生的任务的前驱任务，
	 * 对于新稿，前驱任务为null,但参数需传0过来
	 * @return
	 * @throws DocumentManagerException
	 */
	public void deliverDoc(int docid,int[] auditorids,int userid,int preTaskid) throws DocumentManagerException; 
	/**
	 * 当前用户批量呈送文档,这批文档处于同一种状态
	 * @param docids
	 * @return
	 * @throws DocumentManagerException
	 */
	public void deliverDoc(int[] docids, int[] auditorids, int userid, int preTaskid) throws DocumentManagerException;
	/**
	 * 当前用户批量呈送文档，这批文档处于不同的状态，如新稿和返工
	 * @param docids
	 * @param auditorids
	 * @param userid
	 * @param preTaskid
	 * @throws DocumentManagerException
	 */
	public void deliverDoc(int[] docids, int[] auditorids, int userid, int[] preTaskid) throws DocumentManagerException;
	/**
	 * 撤销呈送
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public void withdrawDeliver(int docid) throws DocumentManagerException;
	/**
	 * 批量撤销呈送
	 * @param docids
	 * @return
	 * @throws DocumentManagerException
	 */
	public void withdrawDeliver(int[] docids) throws DocumentManagerException;
	/**
	 * 撤销发布
	 * @param docid
	 * @throws DocumentManagerException
	 */
	public void withdrawPublish(int docid) throws DocumentManagerException;
	/**
	 * 批量撤销发布
	 * @param docids
	 * @throws DocumentManagerException
	 */
	public void withdrawPublish(int[] docids) throws DocumentManagerException;
	/**
	 * 将处于待发布状态的文档退回到返工状态，
	 * 删除当前任务
	 * 同时为提交发布的人增加一条返工任务
	 * @param docid
	 * @param preTaskId
	 * @param submitterId
	 * @param executers
	 * @throws DocumentManagerException
	 */
	public void toPubWithdraw(int docid,int preTaskId,int submitterId,int[] executers) throws DocumentManagerException;
	/**
	 * 提交发布
	 * @param docid 要提交发布的文档
	 * @param publisherids 发布人
	 * @param userid 任务提交人
	 * @param preTaskid 前驱任务（审核）
	 * @throws DocumentManagerException
	 */
	public void subPublishDoc(int docid,int[] publisherids,int userid, int preTaskid) throws DocumentManagerException;
	/**
	 * 批量提交发布
	 * @param docid
	 * @throws DocumentManagerException
	 */
	public void subPublishDoc(int[] docids,int[] publisherids,int userid, int preTaskid) throws DocumentManagerException;
	/**
	 * 文档归档
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public void pigeonholeDoc(int docid,int userId) throws DocumentManagerException;
	/**
	 * 批量文档归档
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 */
	public void pigeonholeDoc(int[] docids,int userId) throws DocumentManagerException;
	/**
	 * 判断当前用户是否为归档管理员
	 * @param userId
	 * @return
	 * @throws DocumentManagerException
	 */
	public boolean isPigeonholeManager(int userId) ;
	/**
	 * 将文档操作记录到数据库中
	 * @param docids
	 * @param userid
	 * @param operType 取值应该为tb_cms_doc_oper表中存在的操作名
	 * @param tranID 迁移id，为整型，可以在canTansition判断时返回，若不存在迁移id，如删除、新建等则传0
	 * @return
	 * @throws DocumentManagerException
	 */
	public void recordDocOperate(int docid,int userid,String operType,int tranId,String operContent) throws DocumentManagerException;
	/**
	 * 批量的将文档操作记录到数据库中
	 * @param docids
	 * @param userid
	 * @param operType
	 * @param tranID 为map类型，可以在canTansition判断时返回文档组可以进行的迁移 
	 * @return
	 * @throws DocumentManagerException
	 */
	public void recordDocOperate(int[] docids,int userid,String operType,Map tranIdMap,String operContent) throws DocumentManagerException;
	/**
	 * 将指定文档移动到目的频道，单个文档移动
	 * @param docid
	 * @param channelid
	 * @throws DocumentManagerException
	 */
	public void moveDoc(HttpServletRequest request,int docid, int channelid) throws DocumentManagerException;
	/**
	 * 将指定文档组移动到目的频道，批量移动
	 * @param docids
	 * @param channelid
	 * @throws DocumentManagerException
	 */
	public void moveDoc(HttpServletRequest request,int[] docids, int channelid) throws DocumentManagerException;
	/**
	 * 将指定文档复制到指定的目的频道，单个文档复制
	 * @param docid
	 * @param channelid
	 * @throws DocumentManagerException
	 */
	public void copyDoc(HttpServletRequest request,int docid, int channelid) throws DocumentManagerException;
	/**
	 * 将指定文档复制到指定的目的频道，批量文档复制
	 * @param docids
	 * @param channelid
	 * @throws DocumentManagerException
	 */
	public void copyDoc(HttpServletRequest request,int[] docids, int channelid) throws DocumentManagerException;
	/**
	 * 在指定频道引用指定文档
	 * @param docid		//引用文档id或者引用频道id
	 * @param channelid  //文档引用得目的频道
	 * @param userid
	 * @param type    	//引用类型标志，0－引用文档；1－引用频道 缺省值为0
	 * @param siteid  	//引用文档（或引用频道）所在的站点
	 * @throws DocumentManagerException
	 */
	public void citeDoc(int docid, int channelid, int userid,int type,int siteid) throws DocumentManagerException;
	/**
	 * 在指定频道批量的引用指定文档
	 * @param docids
	 * @param channelid
	 * @param userid
	 * @param siteid  	//引用文档（或引用频道）所在的站点
	 * @throws DocumentManagerException
	 */
	public void citeDoc(int[] docids, int channelid,int userid,int type,int siteid) throws DocumentManagerException;
	/**
	 * 撤销指定频道对指定文档的引用
	 * @param docid
	 * @param channelid
	 * @throws DocumentManagerException
	 */
	public void withdrawCite(int docid, int channelid) throws DocumentManagerException;
	/**
	 * 批量撤销指定频道对指定文档的引用
	 * @param docids
	 * @param channelid
	 * @throws DocumentManagerException
	 */
	public void withdrawCite(int[] docids, int channelid) throws DocumentManagerException;
	/**
	 * 指定用户审核指定的文档
	 * @param docid
	 * @param taskid 待审的任务id  
	 * @param auditorid  审核人id
	 * @param comment
	 * @param flag   用于标志审核通过还是不通过，1表通过，0表不通过        
	 * @throws DocumentManagerException
	 */
	public void audit(int docid, int taskid, int auditorid, String comment,int flag) throws DocumentManagerException;
	/**
	 * 指定用户审核批量文档
	 * @param docids
	 * @param taskids 所有待审的任务id  
	 * @param auditorid  审核人id
	 * @param comment
	 * @param flag   用于标志审核通过还是不通过，1表通过，0表不通过 
	 * @throws DocumentManagerException 
	 */
	public void audit(int[] docids, int[] taskids, int auditorid, String comment,int flag) throws DocumentManagerException;
	/**
	 * 获取制定的返工任务的前驱任务(待审任务的)的执行者
	 * @param taskid
	 * @throws DocumentManagerException
	 */ 
	public int getPreSubmitterId(int taskid) throws DocumentManagerException;
	
	/**
	 * 获取当前返工任务的审核意见
	 * @param taskid
	 * @return
	 * @throws DocumentManagerException
	 */
	public TaskDocument getTaskInfo(int taskid) throws DocumentManagerException;
	/**
	 * 任务完成：将指定文档处于指定srcStatus状态对应的任务设为指定执行人完成，
	 * 任务完成后文档状态为desStatus
	 * 而其他执行人对该任务为无效 
	 * @param context
	 * @param srcStatus	任务未完成时文档的状态
	 * @param desStatus	任务完成时文档的状态
	 * @param performer	任务执行人id
	 * @param opinion  任务完成的意见
	 * @throws DocumentManagerException
	 */
	public void completeTask(ContentContext context,DocumentStatus srcStatus,DocumentStatus desStatus,int performer,String opinion) throws DocumentManagerException;
	/**
	 * 任务完成：将指定文档处于指定srcStatus状态对应的任务设为指定执行人完成，
	 * 任务完成后文档状态为desStatus
	 * 而其他执行人对该任务为无效 
	 * @param taskId
	 * @param desStatus 任务完成时文档的状态
	 * @param performer 任务执行人id
	 * @param opinion 任务完成的意见
	 * @throws DocumentManagerException
	 */
	public void completeOneTask(int taskId,int desStatus,int performer,String opinion) throws DocumentManagerException;
	
	/**
	 * 为指定文档增加一条任务
	 * @param docid
	 * @param preTaskId 前驱任务id，不存在前驱任务，则传值0
	 * @param preStatus 当前状态
	 * @param submitId  任务提交人id
	 * @param executerIds 任务执行人数组
	 * @return 返回新增任务id
	 * @throws DocumentManagerException
	 */
	public int addOneTask(int docid,int preTaskId,int preStatus,int submitId,int[] executerIds) throws DocumentManagerException;
	/**
	 * 清除任务：将指定文档对应的所有任务从数据库中删除
	 * @throws DocumentManagerException
	 */
	public void clearTask(ContentContext context)throws DocumentManagerException;
	/**
	 * get文档级别
	 * @return List
	 * @throws DocumentManagerException
	 */
	public List getDocLevelList() throws DocumentManagerException;
	/**
	 * 新增文档的相关文档
	 * param DocRelated d
	 * @return boolean
	 */
	public boolean creatorDocRelated(DocRelated d) throws DocumentManagerException;
	/**
	 * 新增文档的相关文档
	 * param DocRelated d
	 * param int[] related_doc_ids
	 * @return boolean
	 */
	public boolean creatorDocRelated(DocRelated d,int[] relatedDocIds) throws DocumentManagerException;
	/**
	 * 删除文档的相关文档
	 * param docid
	 * @return boolean
	 */
	public boolean deleteDocRelated(int docid) throws DocumentManagerException;
	/**
	 * get文档的相关文档的信息列表
	 * param docid
	 * @return List
	 */
	public List getDocRelatedList(int docid) throws DocumentManagerException;
	/**
	 * get文档的相关文档的信息列表
	 * param docid
	 * @return String[]
	 */
	public String[] getDocRelatedString(int docid) throws DocumentManagerException;
	
	/**
	 * 获取文档的内容附件
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getAttachmentsOfDocument(int docid) throws DocumentManagerException;
	
	/**
	 * 获取文档的相关图片
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List<Attachment> getPicturesOfDocument(int docid) throws DocumentManagerException;
	public ListInfo getPicturesOfDocument(int docid, long offSet,
			int pageItemsize) throws DocumentManagerException;
	
	/**
	 * 获取文档的相关图片
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List<Attachment> getPicturesOfDocument(Document doc) throws DocumentManagerException;
	public ListInfo getPicturesOfDocument(Document doc, long offSet,
			int pageItemsize) throws DocumentManagerException;
	
	/**
	 * 获取文档的相关附件
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getRelationAttachmentsOfDocument(int docid) throws DocumentManagerException;
	
	/**
	 * 获取文档的所有的相关东西，包括内容附件，相关图片，相关附件等
	 * param docid(文档id)
	 * param type(附件类型)
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getAllRelationOfDocument(int docid,int type) throws DocumentManagerException;
	
	/**
	 * 获取文档的所有的相关东西，包括内容附件，相关图片，相关附件等
	 * 返回所有附件的绝对路径
	 * param HttpServletRequest request（从页面传进来）
	 * param docid(文档id)
	 * param channelId
	 * @return List<String>
	 * @throws DocumentManagerException
	 */
	public List getAllAttachmentOfDocument(HttpServletRequest request,int docid) throws DocumentManagerException;
	
	/**
	 * 获取文档的所有的相关东西，包括内容附件，相关图片，相关附件等
	 * param docid(文档id)
	 * param type(附件类型)
	 * @return String[]<Attachment>
	 * @throws DocumentManagerException
	 */
	public String[] getAllRelationOfDocument2String(int docid,int type) throws DocumentManagerException;
	
	/**
	 * 新增文档附件
	 * param <Attachment>
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean createAttachment(Attachment attachment) throws DocumentManagerException;
	
	/**
	 * 删除文档附件
	 * param 文档id
	 * param type
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean deleteAttachment(int docid,int type) throws DocumentManagerException;
	
	/**
	 * 更新文档附件
	 * param <Attachment>
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean UpdateAttachment(Attachment attachment) throws DocumentManagerException;
	
	/**
	 * 更新文档附件的状态（字段：valid）
	 * @param int docId(文档id)
	 * @param int type(文档type)
	 * @param String sqlstr(sql条件，所有有效的attr的url拼成的string)
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean UpdateAttachmentState(int docId, int type, String sqlstr) throws DocumentManagerException;
	
	/**
	 * 更新文档的状态,此方法用于发布时的状态更新
	 * @param published
	 */
	public void updateDocumentStatus(ContentContext context,DocumentStatus newStatus) throws DocumentManagerException;
	/**
	 * 更新文档的状态
	 * @param docid
	 * @param newStatus
	 * @throws DocumentManagerException
	 */
	public void updateDocumentStatus(int docid,int newStatus) throws DocumentManagerException;
	/**
	 * 更新文档发布时间
	 * @param docid
	 * @throws DocumentManagerException
	 */
	public void updateDocPublishTime(ContentContext context) throws DocumentManagerException;
	/**
	 * 更新文档发布时间
	 * @param publishTime
	 * @throws DocumentManagerException
	 */
	public void updateDocPublishTime(int docid) throws DocumentManagerException;
	/**
	 * 返回文档发布方式的列表
	 * return int[]
	 */
	public int[] getDocDistributeManners(int docId) throws DocumentManagerException;
	/**
	 * get具体某一文档的最高版本号
	 * return int
	 */
	public int getMaxDocVersion(int docId) throws DocumentManagerException;
	/**
	 * get具体某一文档的所有版本
	 * return List<Document> only(version,subtitle,op_user,op_time)
	 */
	public List getAllDocVersion(int docId) throws DocumentManagerException;
	
	
	/**
	 * get文档指定版本的文档的详细信息
	 * @param docId
	 * @param version
	 * @return
	 * @throws DocumentManagerException
	 */
	public Document getDocVerInfo(String docId,String version) throws DocumentManagerException;
	/**
	 * 新增版本
	 */
	public void addDocVersion(int docId,int userId,String verLable, String verComment) throws DocumentManagerException;
	/**
	 * 批量新增版本
	 */
	public void addDocVersions(int[] docIds,int userId,String verLable, String verComment) throws DocumentManagerException;
	/**
	 * 删除版本
	 * return int 
	 */
	public int delDocVersion(int docId,int docVersion) throws DocumentManagerException;
	/**
	 * 恢复到某一版本状态
	 * return int 
	 */
	public int resumeDocVersion(int docId,int docVersion,int userId) throws DocumentManagerException;
	/**
	 * 获取某个文档的相关文档集合
	 * return List<Document>
	 * param docId(文档id)
	 */
	public List getRelatedDocsOfDoc(String docId) throws DocumentManagerException;
	/**
	 * new 聚合文档
	 * param <DocAggregation> aggrDoc
	 */
	public boolean addAggrDoc(DocAggregation aggrDoc) throws DocumentManagerException;
	/**
	 * del 聚合文档
	 * param int docId
	 */
	public boolean delAggrDoc(int docId) throws DocumentManagerException;
	/**
	 * 根据文档ID获取文档聚合信息
	 * param String docId
	 * return List<DocAggregation>
	 */
	public List getDocAggregationInfo(String docId) throws DocumentManagerException;
	/**
	 * 根据文档ID获取文档聚合中所有已经发布的被聚合的文档
	 * 那些没发布的被聚合文档不取进来
	 * param String docId
	 * return List<DocAggregation>
	 */
	public List getPubAggrDocList(String docId) throws DocumentManagerException;
	/**
	 * get 聚合文档List2Str
	 * param String docId
	 * return String[]<DocAggregation>
	 */
	public String[] getAggrDocList2Str(String docId) throws DocumentManagerException;
	
	/**
	 * 获取本身设置的细览模版，如果本身的细览模版不存在
	 * 则返回null值
	 * @param docId
	 * @return
	 * @throws DocumentManagerException
	 */
	public Template getDetailTemplate(String docId) throws DocumentManagerException;
	/**
	 * 置顶文档已经存在
	 * @param docId
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean ArrangeDocExist(String docId) throws DocumentManagerException;
	/**
	 * 新增置顶文档
	 * @param <ArrangeDoc>arrangeDoc
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean addArrangeDoc(ArrangeDoc arrangeDoc) throws DocumentManagerException;
	/**
	 * get置顶文档信息
	 * @param String docid
	 * @return <ArrangeDoc>arrangeDoc
	 * @throws DocumentManagerException
	 */
	public ArrangeDoc getArrangeDoc(String docid) throws DocumentManagerException;
	/**
	 * 删除置顶文档
	 * @param docId
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean delArrangeDoc(String[] docId) throws DocumentManagerException;
	/**
	 * 更新置顶文档信息
	 * @param <ArrangeDoc>arrangeDoc
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean updateArrangeDoc(ArrangeDoc arrangeDoc) throws DocumentManagerException;
	/**
	 * 设置更新置顶文档的权重
	 * @param docId
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean updateArrangeDocOrderNo(String[] docIds) throws DocumentManagerException;
	/**
	 * get文档的所有扩展字段信息的Map
	 * @param String docid 
	 * @return Map
	 * 		Map对应的key：扩展字段的name    
	 * 		Map对应的value：文档对应该扩展字段的内容
	 * @throws DocumentManagerException
	 */
	public Map getDocExtFieldMap(String docid) throws DocumentManagerException;
	
	/**
	 * 根据频道id,扩展字段id获得当前文档枚举扩展字段的值
	 * @return
	 * @throws DocumentManagerException
	 */
	 public List getFieldValueList(String channelId,String fieldId,String type) throws DocumentManagerException;
	 /**
	  * 存储radiobox，checkbox扩展字段的值
	  * @param docid
	  * @param fieldid
	  * @param clobvalue
	  * @throws DocumentManagerException
	  */
	 public void saveExtFieldMap(String docid,String fieldid,String clobvalue,String checked,String flag) throws DocumentManagerException;
	
	 /**
		 * get文档的相关文档的信息列表
		 * param docid
		 * @return List
		 */
	public List getRelatedDocList(String docid,int count) throws DocumentManagerException;
	
	/**
	 * 保存文档发布之前的状态
	 * 防止文档发布时由于特殊情况"死"在"发布中"状态，所以发布前得保存文档发布前的状态 
	 * @param docid
	 * @param oldStatus
	 * @throws DocumentManagerException
	 */
	public void storeOldStatus(ContentContext context,DocumentStatus oldStatus) throws DocumentManagerException;
	/**
	 * 获取文档发布之前的状态，以便在发布异常中止时能恢复到该状态
	 * @param context
	 * @throws DocumentManagerException
	 */
	public int getOldStatus(String docId) throws DocumentManagerException;
	/**
	 * 删除文档发布之前的状态
	 * @param docId
	 * @return
	 * @throws DocumentManagerException
	 */
	public void deleteOldStatus(String docId) throws DocumentManagerException;
	
	/**
	 * 不翻页 --ge.tao
	 * 获取文档的相关附件 kaihu 070601
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getRelationAttachmentsOfDocument(int docid, int count) throws DocumentManagerException;
	
	/**
	 * 翻页 --ge.tao
	 * 获取文档的相关附件 kaihu 070601
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public ListInfo getRelationAttachmentsOfDocument(Document doc, long offset, int pageitems) throws DocumentManagerException;
	
	
	/**
	 * 不翻页 --ge.tao
	 * 获取文档的相关附件 kaihu 070601
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getRelationAttachmentsOfDocument(Document doc, int count) throws DocumentManagerException;
	
	/**
	 * 翻页 --ge.tao
	 * 获取文档的相关附件 kaihu 070601
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public ListInfo getRelationAttachmentsOfDocument(int docid, long offset, int pageitems) throws DocumentManagerException;
	
	/**
	 * get文档的相关文档的信息列表
	 * param docid
	 * @return List
	 */
	public ListInfo getRelatedDocList(String docid,long offset, int pageitems) throws DocumentManagerException;
	
	/**
	 * 获取指定频道下所有文档模板列表
	 * @param chnlId
	 * @return	List<DocTemplate>
	 * @throws DocumentManagerException
	 */
	public List getAllDocTPLList(String chnlId) throws DocumentManagerException;
	
	/**
	 * 获取指定频道指定用户（由该用户创建）的文档模板列表
	 * @param chnlId
	 * @param userId
	 * @return	List<DocTemplate>
	 * @throws DocumentManagerException
	 */
	public List getDocTPLList(String chnlId,String userId) throws DocumentManagerException;
	
	/**
	 * 增加一个文档采集模板
	 * @param docTPL
	 * @throws DocumentManagerException
	 */
	public void addDocTPL(DocTemplate docTPL) throws DocumentManagerException;
	/**
	 * 更新指定文档采集模板
	 * @param docTPL
	 * @throws DocumentManagerException
	 */
	public void updateDocTPL(DocTemplate docTPL) throws DocumentManagerException;
	/**
	 * 删除文档采集模板
	 * @param docTPLId
	 * @throws DocumentManagerException
	 */
	public void deleteDocTPL(String docTPLId) throws DocumentManagerException;
	/**
	 * 批量删除文档采集模板
	 * @param docTPLId
	 * @throws DocumentManagerException
	 */
	public void deleteDocTPLs(String[] docTPLIds) throws DocumentManagerException;
	/**
	 * 获取文档采集模板列表
	 * @param sql
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getDocTPLList(String sql,long offset, int pageitems) throws DocumentManagerException;
	/**
	 * 获取指定的文档采集模板
	 * @param docTPLId
	 * @return
	 * @throws DocumentManagerException
	 */
	public DocTemplate getDocTPL(String docTPLId) throws DocumentManagerException;
	
	/**
	 * 获取引用了某一个文档的频道列表
	 * @param sql
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getChnlListOfDocCited(String docId,int offset, int maxitem) throws DocumentManagerException;
	
	/**
	 * 获取频道下 直接频道 和 文档的混合列表 不翻页!!!
	 * @param siteid
	 * @param channel
	 * @param count
	 * @return List
	 * @throws DocumentManagerException 
	 * DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public List getDirectSpecialDocList(String siteid,String channel,int count) throws DocumentManagerException;
	
	/**
	 * 获取频道下 直接频道 和 文档的混合列表 翻页!!!
	 * @param siteid
	 * @param channel
	 * @param offset
	 * @param pageitems
	 * @return ListInfo
	 * @throws DocumentManagerException 
	 * DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public ListInfo getDirectSpecialDocList(String siteid,String channel,long offset, int pageitems) throws DocumentManagerException;
	
	/**
	 * 获取频道下 文档 引用文档 引用频道 混合列表 不翻页!!!
	 * @param channel
	 * @param count
	 * @return List
	 * @throws DocumentManagerException 
	 * @author: 陶格
	 */
	public List getArrangeSpecialDocList(String channel,int count) throws DocumentManagerException;
	/**
	 * 获取频道下 文档 引用文档 引用频道 混合列表 翻页!!!
	 * @param channel
	 * @param offset
	 * @param pageitems
	 * @return ListInfo
	 * @throws DocumentManagerException 
	 * @author: 陶格
	 */
	public ListInfo getArrangeSpecialDocList(String channel,long offset, int pageitems) throws DocumentManagerException;
	/**
	 * 恢复发布中的文档,
	 * 返回1：恢复操作成功；2：文档处于真的发布中，不能恢复；
	 * @param docId
	 * @throws DocumentManagerException
	 */
	public int recoverPublishingDoc(String docId) throws DocumentManagerException;
	
	/**
	 * 恢复发布中的文档,
	 * 返回true：恢复操作成功；false：恢复失败，数据库操作失败；
	 * @param docId
	 * @throws DocumentManagerException
	 * add by xinwang.jiao 2007.8.21
	 */
	public boolean recoverAllPublishingDoc() throws DocumentManagerException;
	
	/**
	 * 批量恢复发布中的文档，将所有能恢复的文档恢复
	 * @param docId
	 * @throws DocumentManagerException
	 */
	public void recoverPublishingDocs(String docIds[]) throws DocumentManagerException;
	/**
	 * 获取当前用户的任务数,flag:0-审核任务数；1-新稿文档数;2-返工文档数;3-待发布任务数;
	 * @param curUserid
	 * @param flag
	 * @return
	 * @throws DocumentManagerException
	 */
	public int getTaskCount(String curUserid,int flag) throws DocumentManagerException;
	/**
	 * 更新频道下继承原来频道模板的文档模板 
	 * DocumentManager.java
	 * @author: ge.tao
	 */
	public void updatePreviousDocInChannel(String channelId,String tmpeleteId)throws DocumentManagerException ;
	/**
	 * 频道下采集的文档 
	 * DocumentManager.java
	 * @author: ge.tao
	 */
	public boolean channelHasDoc(String channelId)throws DocumentManagerException ;
	
	/**
	 * 通过文档ID获取文档的部分信息 不要clob字段 content 
	 * @param docid
	 * @return Document
	 * @throws DocumentManagerException 
	 * DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public Document getPartDocInfoById(String docid) throws DocumentManagerException;
	
	
	/**
	 * 获取引用改文档的文档列表
	 * list.add(Dcoument)
	 * @param docids 11;222;33;
	 * @return
	 * @throws DocumentManagerException 
	 * DocumentManager.java
	 * @author: ge.tao
	 */
	public List getDocInfoByIds(String docids) throws DocumentManagerException;
	
	/**
	 * 批量复制文档组到多个频道下 可指定文档状态status
	 * 并且 文档流程更新为 频道的流程
	 * add by ge.tao
	 * 2007-09-17
	 * String[] channelid
	 * {200:1,201:2,202,3....}
	 * 返回 新增文档的ID 的数组
	 */	
	public int[] copyDoc(HttpServletRequest request,int[] docids, String[] channelid,int status) throws DocumentManagerException ;
	
	/**
	 * 批量复制文档组到多个频道下 可指定文档状态status
	 * @param request
	 * @param docids 文档ID数组
	 * @param channelids 目的频道数组
	 * @param siteids 目的站点数组
	 * @param status 取值范围为
	 *        DocumentStatus.NEW
	 *        DocumentStatus.AUDITING
	 *        DocumentStatus.PREPUBLISH
	 *        DocumentStatus.PUBLISHED
	 * @param userId 操作用户ID
	 * @return int[][] 新产生的{站点ID,频道ID,文档ID}数组
	 * @throws DocumentManagerException 
	 * DocumentManager.java
	 * @author: ge.tao
	 */                    
	public int[][] copyDocs(HttpServletRequest request,int[] docids, int[] channelids, int[] siteids,int status, int userId) throws DocumentManagerException ;
	
	/**
	 * 根据文档关键字获取文档所属的站点中所有已发文档中标题
     * 内容包含本关键字的文档列表
     * 翻页
	 * @param docid 文档ID int
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws DocumentManagerException 
	 * DocumentManager.java
	 * @author: ge.tao
	 */
	public ListInfo getKeyWordRelationOfDocument(String keywords,int docid, String siteid,long offset, int pageitems) throws DocumentManagerException;
	/**
	 * 根据文档标题获取文档所属的站点中指定频道已发文档中作者
     * 与本文档标题相同的文档列表
     * 翻页
	 * @param docid 文档ID int
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws DocumentManagerException 
	 * DocumentManager.java
	 * @author: wei.li
	 */
         public ListInfo getKeyWord2RelationOfDocument(String keywords,int docid, String channel,String siteid,long offset, int pageitems) throws DocumentManagerException;
	
         /**
     	 * 根据文档标题获取文档所属的站点中指定频道已发文档中关键字
          * 与本文档标题相同的文档列表
          * 翻页
     	 * @param docid 文档ID int
     	 * @param offset
     	 * @param pageitems
     	 * @return
     	 * @throws DocumentManagerException 
     	 * DocumentManager.java
     	 * @author: wei.li
     	 */
      public ListInfo getKeyWord3RelationOfDocument(String keywords,int docid, String channel,String siteid,long offset, int pageitems) throws DocumentManagerException;
     /**
	 * 根据文档关键字获取文档所属的站点中所有已发文档中标题
     * 内容包含本关键字的文档列表
     * 不翻页
	 * @param docid 文档ID int
	 * @param count
	 * @return
	 * @throws DocumentManagerException 
	 * DocumentManager.java
	 * @author: ge.tao
	 */
	public List getKeyWordRelationOfDocument(String keywords,int docid, String siteid,int count) throws DocumentManagerException;
	/**
	  * 根据文档标题获取文档所属的站点中指定频道已发文档中作者
     * 与本文档标题相同的文档列表
     * 不翻页
	 * @param docid 文档ID int
	 * @param count
	 * @return
	 * @throws DocumentManagerException 
	 * DocumentManager.java
	 * @author: ge.tao
	 */
         public List getKeyWord2RelationOfDocument(String keywords,int docid,String channel, String siteid,int count) throws DocumentManagerException;
         
         /**
    	  * 根据文档标题获取文档所属的站点中指定频道已发文档中关键字
         * 与本文档标题相同的文档列表
         * 不翻页
    	 * @param docid 文档ID int
    	 * @param count
    	 * @return
    	 * @throws DocumentManagerException 
    	 * DocumentManager.java
    	 * @author: ge.tao
    	 */
     public List getKeyWord3RelationOfDocument(String keywords,int docid,String channel, String siteid,int count) throws DocumentManagerException;
	/**
	 * 更新文档排序时间
	 * @param docId
	 * @param ordertime 
	 * DocumentManager.java
	 * @author: ge.tao
	 */
	public void updateDocOrdertime(String docId, Date ordertime);
	
	/**
	 * 批量修改常用字段
	 * @param doc
	 * @param docIdStr
	 * @return
	 */
	public boolean updateDocs(HashMap map,String docIdStr);
	
	/**
	 * 获取文档未处理评论数
	 */
	public int getDocCommentByDocId(int docId, Integer status);
	public String ext_orgByDocId(String docId);
	
	/**
	 * 根据发布时间(开始时间~结束时间)和关键字(匹配标题)查询新闻列表
	 * @param condition 查询条件
	 * @param offset 偏移量
	 * @param pagesize 每页记录数
	 * @return 新闻集合
	 * @throws SQLException
	 */
	public ListInfo queryDocumentsByPublishTimeAndKeywords(DocumentCondition condition, long offset, int pagesize) throws SQLException;
	
	/**
	 * 根据视频类别和排序类别(最新发布，最多播放，最多评论)查询视频列表
	 * @param condition 查询条件
	 * @param offset 偏移量
	 * @param pagesize 每页记录数
	 * @return 视频集合
	 * @throws SQLException
	 */
	public ListInfo queryVideosByOrderType(DocumentCondition condition, long offset, int pagesize) throws SQLException;
	
	public List getAllPublishedAttachmentOfDocument(HttpServletRequest request,
			Document document, String siteId, List docattachements,
			CMSTemplateLinkTable contentOrigineTemplateLinkTable) throws DocumentManagerException;
}