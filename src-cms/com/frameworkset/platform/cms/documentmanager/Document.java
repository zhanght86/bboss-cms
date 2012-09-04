//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\contentmanager\\Document.java

package com.frameworkset.platform.cms.documentmanager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.task.PublishSchedular;

/**
 * 文档管理
 * 1.html/纯文本文档
 * 2.链接文档
 * 3.附件文档
 */
public class Document implements java.io.Serializable
{
	/**
	 * 文档评论审核开关：
        0：评论无需审核
        1：评论需要审核
                       如果频道已经关闭审核功能，则文档审核也会关闭，反之，以文档标志为准              
	 */
    private int auditflag = 0;
    /**
     * 新闻标题
     */
    private String title;
    
    /**
     * 新闻显示标题
     */
    private String subtitle;
    
    /**
     * 新闻副标题
     */
    private String secondtitle;
    
    /**
     * 文档类型:
     * 0:普通文档(可带附件)
     * 1:外部链接
     * 2:外部文件
     * 3:聚合文档
     * 4:引用频道
     */
    private int doctype;
    
    public static final int DOCUMENT_NORMAL = 0;
    public static final int DOCUMENT_OUTLINK = 1;
    public static final int DOCUMENT_OUTFILE = 2;
    public static final int DOCUMENT_AGGRATION = 3;
    public static final int DOCUMENT_CHANNEL = 4;
    
    private Channel refChannel;
    
    /**
     * 新闻作者
     */
    private String author;
    
    /**
     * 文档分类编号
     */
    private String dockind;
    
    /**
     * 文档输入编号
     */
    private String docoutputid;
    
    /**
     * 新闻内容
     */
    private String content;
    
    /**
     * 文档的状态：
     * 0：新稿
     * 1：已编
     * 2：返工
     * 3：已发
     * 4：已否
     * 5：已签
     * 6：正审
     * 8：归档
     */
    private int status;
    /**
     * 文档状态显示名称
     */
    private String  statusname;
    
    /**
     * 新闻关键词
     */
    private String keywords;
    
    /**
     * 新闻摘要
     */
    private String docabstract;
    
    /**
     * 新闻类型：
     * 0:普通新闻
     * 1.聚合新闻
     * 2.分页新闻
     */
    private int type;
    
    /**
     * 图片新闻中的
     * 概览图片路径
     * add by xinwang.jiao 2007.04.15
     */
    private String picPath;
    
    /**
     * 多媒体文件路径
     * mediapath
     * add by xinwang.jiao 2007.04.19
     */
    private String mediapath;
    
    /**
     * 文档权限级别
     * 0:普通
     * 1:中
     * 2:高
     */
    private int level;
    
    /**
     * 文档撰写时间
     */
    private Date docwtime;
    
    /**
     * 标题色
     */
    private String titlecolor;
    
    /**
     * 文档发布时间
     */
    private Date publishTime;
    /**
     * 文档回收时间
     */
    private Date recycleTime;
    /**
     * 文档归档时间
     */
    private Date pigeonholeTime;
    /**
     * 文档回收执行人
     */
    private String recyclePerfomer;
    /**
     * 文档归档执行人
     */
    private String pigeonholePerfomer;
    /**
     * 文档标记：
     * 0：正常
     * 1：删除标记
     */
    private int docflag;
    
    /**
     * 文档发布URL
     * 
     * 例如:/pub/zndxtsg/zydh/zwsjk/t20050405_0784.htm
     */
    private String docpuburl;
    
    /**
     * 文档权重
     */
    private int prior;
    
    /**
     * 创建人或者引用人
     */
    private int createUser;
    private Date createTime;
    private int doclevel;
    /**
     * 文档来源名称
     */
    private String docsource_name;
    /**
     * 发布模板名称
     */
    private String detailtemplate_name;
    /**
     * 发稿人
     */
	  private String username;
	  private int islock;
	  private String linktarget;
	  private String linkfile;
	  private int document_id;
	  private int user_id;
	  private int documentprior;
	  private int parentdocument_id;
	  private int docsource_id;
	  private int detailtemplate_id;
	  private int chanel_id;
	  private String channelName;
	  private int reference_channel_id;
	  /**
	   * PARENT_DETAIL_TPL
	   * 是否引用父对象的细览模板设置，1：是，0：否
	   */
	  private String parentDetailTpl;
	  /**
	   * 文档级别编码
	   */
	  private int doc_level;
	  /**
	   * 文档类别编码
	   */
	  private int doc_kind;
	  /**
	   * 文档逻辑分类，对一个频道下的文档进行进一步分类
	   * 维护时在表TD_CMS_DOCUMENT_CLASS中选择对应于CLASS_NAME的值
	   */
	  private String doc_class;
	  
	  /**
	   * 是否是聚合文档：0：非聚合文档，1：聚合文档
	   */
	  private int aggregation;
	  /**
	   * FLOW_ID
	   */
	  private int flowId;
	  private String flowName;
	  /**
	   * version(文档版本),verLable(版本标签),verDiscription(版本注释)
	   */
	  private int version;
	  private String verLable;
	  private String verDiscription;
	  /**
	   * op_user（版本建立人id）
	   */
	  private int opUser;
	  /**
	   * op_time（版本时间）
	   */
	  private Date opTime;
	
    /**
     * 最后访问时间
     */
    private Date lastVisitTime;
    /**
     * 聚合文档信息
     */
    private List compositeDocs;
    
    
    //文档所在站点id
    private int siteid = -1;
    //文档所在站点名
    private String siteName;
    
    /**
     * 是否是引用的文档
     */
    boolean isRef = false;
    /**
     * 分页文档信息
     */
    private java.util.List pagingDocs;
    private java.util.List theUser;
    private java.util.List theAttachDocument;
    private PublishSchedular thePublishSchedular;
    private java.util.List theDocversion;
    private java.util.List theDocComment; 
    private HashMap extColumn;
    private List<Attachment> relatePics;
    /**
     *文档发布指定名称 
     */
    private String publishfilename;
    /**
     * 文档相关文档
     * List<Document>
     */
    private List relationDocumemnts;
    
    /**
     * 文档扩展字段
     */
    private Map docExtField;
    
    
    
    /**
     * 文档被引用的频道
     */
    private java.util.List refChannels;
    /**
     * 文档旧状态
     */
	private String docOldStatus;
	
	/**
	 * 是否是置顶文档
	 */
	private boolean isArrangeDoc;
	
	/**
	 * 置顶是否过期
	 */
	private boolean isArrangeOverTime;

	/**
	 * 是否要标记为new文档
	 * 1：是，0：否 ，缺省为0
	 * add by xinwang.jiao 2007.09.19
	 */
	private int isNew = 0;
	
	/**
	 * new标记图片路径
	 * add by xinwang.jiao 2007.09.19
	 */
	private String newPicPath = "";
	
	/**
	 * 文档的排序时间
	 * @return 
	 * Document.java
	 * @author: ge.tao
	 */
	protected Date ordertime;
	
	/**
	 * 文档排序号
	 * @return
	 */
	private int seq;
	
	/**
	 * 评论开关
	 * @return
	 */
	private int commentswitch;
	
	/**
	 * 文档点击数
	 * @return
	 */
	private int count;
	
	/**
	 * 信息文号
	 * @return
	 */
	private String ext_wh;
	/**
	 * 信息类别
	 * @return
	 */
	private String ext_class;
	/**
	 * 信息索引
	 * @return
	 */
	private String ext_index;
	/**
	 * 信息机构
	 * @return
	 */
	private String ext_org;
	/**
	 * 信息登记号
	 * @return
	 */
	private String ext_djh;
	
	/**
	 * 播放次数
	 */
	private Long playedCount;
	
	/**
	 * 评论次数
	 */
	private Long cmCount;
	
    public String getExt_class() {
		return ext_class;
	}

	public void setExt_class(String ext_class) {
		this.ext_class = ext_class;
	}

	public String getExt_djh() {
		return ext_djh;
	}

	public void setExt_djh(String ext_djh) {
		this.ext_djh = ext_djh;
	}

	public String getExt_index() {
		return ext_index;
	}

	public void setExt_index(String ext_index) {
		this.ext_index = ext_index;
	}

	public String getExt_org() {
		return ext_org;
	}

	public void setExt_org(String ext_org) {
		this.ext_org = ext_org;
	}

	public String getExt_wh() {
		return ext_wh;
	}

	public void setExt_wh(String ext_wh) {
		this.ext_wh = ext_wh;
	}

	public boolean isArrangeDoc() {
		return isArrangeDoc;
	}

	public void setArrangeDoc(boolean isArrangeDoc) {
		this.isArrangeDoc = isArrangeDoc;
	}

	public boolean isArrangeOverTime() {
		return isArrangeOverTime;
	}

	public void setArrangeOverTime(boolean isArrangeOverTime) {
		this.isArrangeOverTime = isArrangeOverTime;
	}

	/**
     * @since 2006.12
     */
    public Document() 
    {
     
    }
    
    /**
     * Access method for the title property.
     * 
     * @return   the current value of the title property
     */
    public String getTitle() 
    {
        return title;     
    }
    
    /**
     * Sets the value of the title property.
     * 
     * @param aTitle the new value of the title property
     */
    public void setTitle(String aTitle) 
    {
        title = aTitle;     
    }
    
    /**
     * Access method for the subtitle property.
     * 
     * @return   the current value of the subtitle property
     */
    public String getSubtitle() 
    {
        return subtitle;     
    }
    
    /**
     * Sets the value of the subtitle property.
     * 
     * @param aSubtitle the new value of the subtitle property
     */
    public void setSubtitle(String aSubtitle) 
    {
        subtitle = aSubtitle;     
    }
    
    /**
     * Access method for the doctype property.
     * 
     * @return   the current value of the doctype property
     */
    public int getDoctype() 
    {
        return doctype;     
    }
    
    /**
     * Sets the value of the doctype property.
     * 
     * @param aDoctype the new value of the doctype property
     */
    public void setDoctype(int aDoctype) 
    {
        doctype = aDoctype;     
    }
    
    /**
     * Access method for the author property.
     * 
     * @return   the current value of the author property
     */
    public String getAuthor() 
    {
        return author;     
    }
    
    /**
     * Sets the value of the author property.
     * 
     * @param aAuthor the new value of the author property
     */
    public void setAuthor(String aAuthor) 
    {
        author = aAuthor;     
    }
    
    /**
     * Access method for the dockind property.
     * 
     * @return   the current value of the dockind property
     */
    public String getDockind() 
    {
        return dockind;     
    }
    
    /**
     * Sets the value of the dockind property.
     * 
     * @param aDockind the new value of the dockind property
     */
    public void setDockind(String aDockind) 
    {
        dockind = aDockind;     
    }
    
    /**
     * Access method for the docoutputid property.
     * 
     * @return   the current value of the docoutputid property
     */
    public String getDocoutputid() 
    {
        return docoutputid;     
    }
    
    /**
     * Sets the value of the docoutputid property.
     * 
     * @param aDocoutputid the new value of the docoutputid property
     */
    public void setDocoutputid(String aDocoutputid) 
    {
        docoutputid = aDocoutputid;     
    }
    
    /**
     * Access method for the content property.
     * 
     * @return   the current value of the content property
     */
    public String getContent() 
    {
        return content;     
    }
    
    /**
     * Sets the value of the content property.
     * 
     * @param aContent the new value of the content property
     */
    public void setContent(String aContent) 
    {
        content = aContent;     
    }
    
    /**
     * Access method for the status property.
     * 
     * @return   the current value of the status property
     */
    public int getStatus() 
    {
        return status;     
    }
    
    /**
     * Sets the value of the status property.
     * 
     * @param aStatus the new value of the status property
     */
    public void setStatus(int aStatus) 
    {
        status = aStatus;     
    }
    
    /**
     * Access method for the keywords property.
     * 
     * @return   the current value of the keywords property
     */
    public String getKeywords() 
    {
        return keywords;     
    }
    
    /**
     * Sets the value of the keywords property.
     * 
     * @param aKeywords the new value of the keywords property
     */
    public void setKeywords(String aKeywords) 
    {
        keywords = aKeywords;     
    }
    
    /**
     * Access method for the docabstract property.
     * 
     * @return   the current value of the docabstract property
     */
    public String getDocabstract() 
    {
        return docabstract;     
    }
    
    /**
     * Sets the value of the docabstract property.
     * 
     * @param aDocabstract the new value of the docabstract property
     */
    public void setDocabstract(String aDocabstract) 
    {
        docabstract = aDocabstract;     
    }
    
    /**
     * Access method for the type property.
     * 
     * @return   the current value of the type property
     */
    public int getType() 
    {
        return type;     
    }
    
    /**
     * Sets the value of the type property.
     * 
     * @param aType the new value of the type property
     */
    public void setType(int aType) 
    {
        type = aType;     
    }
    
    /**
     * Access method for the level property.
     * 
     * @return   the current value of the level property
     */
    public int getLevel() 
    {
        return level;     
    }
    
    /**
     * Sets the value of the level property.
     * 
     * @param aLevel the new value of the level property
     */
    public void setLevel(int aLevel) 
    {
        level = aLevel;     
    }
    
  
    
    /**
     * Access method for the titlecolor property.
     * 
     * @return   the current value of the titlecolor property
     */
    public String getTitlecolor() 
    {
        return titlecolor;     
    }
    
    /**
     * Sets the value of the titlecolor property.
     * 
     * @param aTitlecolor the new value of the titlecolor property
     */
    public void setTitlecolor(String aTitlecolor) 
    {
        titlecolor = aTitlecolor;     
    }
    
 
    
    /**
     * Access method for the docflag property.
     * 
     * @return   the current value of the docflag property
     */
    public int getDocflag() 
    {
        return docflag;     
    }
    
    /**
     * Sets the value of the docflag property.
     * 
     * @param aDocflag the new value of the docflag property
     */
    public void setDocflag(int aDocflag) 
    {
        docflag = aDocflag;     
    }
    
    /**
     * Access method for the docpuburl property.
     * 
     * @return   the current value of the docpuburl property
     */
    public String getDocpuburl() 
    {
        return docpuburl;     
    }
    
    /**
     * Sets the value of the docpuburl property.
     * 
     * @param aDocpuburl the new value of the docpuburl property
     */
    public void setDocpuburl(String aDocpuburl) 
    {
        docpuburl = aDocpuburl;     
    }
    
    /**
     * Access method for the prior property.
     * 
     * @return   the current value of the prior property
     */
    public int getPrior() 
    {
        return prior;     
    }
    
    /**
     * Sets the value of the prior property.
     * 
     * @param aPrior the new value of the prior property
     */
    public void setPrior(int aPrior) 
    {
        prior = aPrior;     
    }
    
 
    
  
    
 

	public String getDetailtemplate_name() {
		return detailtemplate_name;
	}

	public void setDetailtemplate_name(String detailtemplate_name) {
		this.detailtemplate_name = detailtemplate_name;
	}

	public int getDoclevel() {
		return doclevel;
	}

	public void setDoclevel(int doclevel) {
		this.doclevel = doclevel;
	}

	public String getDocsource_name() {
		return docsource_name;
	}

	public void setDocsource_name(String docsource_name) {
		this.docsource_name = docsource_name;
	}

	public int getIslock() {
		return islock;
	}

	public void setIslock(int islock) {
		this.islock = islock;
	}

	public String getLinkfile() {
		return linkfile;
	}

	public void setLinkfile(String linkfile) {
		this.linkfile = linkfile;
	}

	public String getLinktarget() {
		return linktarget;
	}

	public void setLinktarget(String linktarget) {
		this.linktarget = linktarget;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getDocument_id() {
		return document_id;
	}

	public void setDocument_id(int document_id) {
		this.document_id = document_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDocwtime() {
		return docwtime;
	}

	public void setDocwtime(Date docwtime) {
		this.docwtime = docwtime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public int getDocumentprior() {
		return documentprior;
	}

	public void setDocumentprior(int documentprior) {
		this.documentprior = documentprior;
	}

	public int getParentdocument_id() {
		return parentdocument_id;
	}

	public void setParentdocument_id(int parentdocument_id) {
		this.parentdocument_id = parentdocument_id;
	}

	public int getDocsource_id() {
		return docsource_id;
	}

	public void setDocsource_id(int docsource_id) {
		this.docsource_id = docsource_id;
	}

	public int getDetailtemplate_id() {
		return detailtemplate_id;
	}

	public void setDetailtemplate_id(int detailtemplate_id) {
		this.detailtemplate_id = detailtemplate_id;
	}

	public int getChanel_id() {
		return chanel_id;
	}

	public void setChanel_id(int chanel_id) {
		this.chanel_id = chanel_id;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public List getCompositeDocs() {
		return compositeDocs;
	}

	public void setCompositeDocs(List compositeDocs) {
		this.compositeDocs = compositeDocs;
	}

	public java.util.List getPagingDocs() {
		return pagingDocs;
	}

	public void setPagingDocs(java.util.List pagingDocs) {
		this.pagingDocs = pagingDocs;
	}

	public String getStatusname() {
		return statusname;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getAggregation() {
		return aggregation;
	}

	public void setAggregation(int aggregation) {
		this.aggregation = aggregation;
	}

	public int getDoc_kind() {
		return doc_kind;
	}

	public void setDoc_kind(int doc_kind) {
		this.doc_kind = doc_kind;
	}

	public int getDoc_level() {
		return doc_level;
	}

	public void setDoc_level(int doc_level) {
		this.doc_level = doc_level;
	}

	public int getFlowId() 
	{
		return flowId;
	}

	public void setFlowId(int flowId) 
	{
		this.flowId = flowId;
	}

	public Date getOpTime() 
	{
		return opTime;
	}

	public void setOpTime(Date opTime) 
	{
		this.opTime = opTime;
	}

	public int getOpUser() {
		return opUser;
	}

	public void setOpUser(int opUser) 
	{
		this.opUser = opUser;
	}

	public int getVersion() 
	{
		return version;
	}

	public void setVersion(int version) 
	{
		this.version = version;
	}

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public List getRelationDocumemnts() {
		return relationDocumemnts;
	}

	public void setRelationDocumemnts(List relationDocumemnts) {
		this.relationDocumemnts = relationDocumemnts;
	}

	public String getParentDetailTpl() {
		return parentDetailTpl;
	}

	public void setParentDetailTpl(String parentDetailTpl) {
		this.parentDetailTpl = parentDetailTpl;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public Date getPigeonholeTime() {
		return pigeonholeTime;
	}

	public void setPigeonholeTime(Date pigeonholeTime) {
		this.pigeonholeTime = pigeonholeTime;
	}

	public Date getRecycleTime() {
		return recycleTime;
	}

	public void setRecycleTime(Date recycleTime) {
		this.recycleTime = recycleTime;
	}

	public Map getDocExtField() {
		return docExtField;
	}

	public void setDocExtField(Map docExtField) {
		this.docExtField = docExtField;
	}

	public String getPigeonholePerfomer() {
		return pigeonholePerfomer;
	}

	public void setPigeonholePerfomer(String pigeonholePerfomer) {
		this.pigeonholePerfomer = pigeonholePerfomer;
	}

	public String getRecyclePerfomer() {
		return recyclePerfomer;
	}

	public void setRecyclePerfomer(String recyclePerfomer) {
		this.recyclePerfomer = recyclePerfomer;
	}

	public String getVerDiscription() {
		return verDiscription;
	}

	public void setVerDiscription(String verDiscription) {
		this.verDiscription = verDiscription;
	}

	public String getVerLable() {
		return verLable;
	}

	public void setVerLable(String verLable) {
		this.verLable = verLable;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getMediapath() {
		return mediapath;
	}

	public void setMediapath(String mediapath) {
		this.mediapath = mediapath;
	}

	public String getPublishfilename() {
		return publishfilename;
	}

	public void setPublishfilename(String publishfilename) {
		this.publishfilename = publishfilename;
	}

	public boolean isRef() {
		return isRef;
	}

	public void setRef(boolean isRef) {
		this.isRef = isRef;
	}

	public HashMap getExtColumn() {
		return extColumn;
	}

	public void setExtColumn(HashMap extColumn) {
		this.extColumn = extColumn;
	}

	public Channel getRefChannel() {
		return refChannel;
	}

	public void setRefChannel(Channel refChannel) {
		this.refChannel = refChannel;
	}

	public String getSecondtitle() {
		return secondtitle;
	}

	public void setSecondtitle(String secondtitle) {
		this.secondtitle = secondtitle;
	}

	public void setDocOldStatus(String docOldStatus) {
		this.docOldStatus = docOldStatus;
		
	}

	public String getDocOldStatus() {
		return docOldStatus;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public String getNewPicPath() {
		return newPicPath;
	}

	public void setNewPicPath(String newPicPath) {
		this.newPicPath = newPicPath;
	}

	public Date getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getCommentswitch() {
		return commentswitch;
	}

	public void setCommentswitch(int commentswitch) {
		this.commentswitch = commentswitch;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getReference_channel_id() {
		return reference_channel_id;
	}

	public void setReference_channel_id(int reference_channel_id) {
		this.reference_channel_id = reference_channel_id;
	}

	public Long getPlayedCount() {
		return playedCount;
	}

	public void setPlayedCount(Long playedCount) {
		this.playedCount = playedCount;
	}

	public Long getCmCount() {
		return cmCount;
	}

	public void setCmCount(Long cmCount) {
		this.cmCount = cmCount;
	}

	public String getDoc_class() {
		return doc_class;
	}

	public void setDoc_class(String doc_class) {
		this.doc_class = doc_class;
	}

	public int getAuditflag() {
		return auditflag;
	}

	public void setAuditflag(int auditflag) {
		this.auditflag = auditflag;
	}

	public List<Attachment> getRelatePics() {
		return relatePics;
	}

	public void setRelatePics(List<Attachment> relatePics) {
		this.relatePics = relatePics;
		for(int j = 0; relatePics != null&& j < relatePics.size(); j ++)
		{
			Attachment aa = relatePics.get(j);
			aa.setDocument(this);
		}
	}

	
}
