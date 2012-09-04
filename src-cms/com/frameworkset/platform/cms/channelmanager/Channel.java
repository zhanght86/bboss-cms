package com.frameworkset.platform.cms.channelmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Channel implements Comparable,java.io.Serializable{
	/**
	 * 频道评论审核开关：
        0：评论无需审核
        1：评论需要审核
                     如果频道指定了评论不需要审核，那么频道下所有文档都无需审核
                     如果频道指定了评论需要审核，那么频道下所有文档都需审核，除非文档本身设置为不需要审核          
	 */
    private int auditflag = 0;
    private String channel_desc;
	private long channelId;

	private String name;

	private String displayName;

	private long parentChannelId;

	private String channelPath;

	private long createUser;

	private Date createTime;

	private int orderNo = 0;

	private long siteId;
	
	private String siteName;
	
	// 频道状态:0,正常;1,已回收
	private int staus = 0;

	// 概览模板id
	private int outlineTemplateId;

	// 细览模板id
	private int detailTemplateId;
	
	//评论模板 id
	private int commentTemplateId;

	// 工作流程:0表示引用站点的工作流程
	private int workflow = 0;
	
	//1 表示继承"父"的工作流程;0,表示不继承
	private int workflowIsFromParent = 1;
	
	// 概览模板是否是动态的:0,静态的;1,动态的
	private int outlineIsDynamic = 0;

	// 文档是否是动态的:0,静态的;1,动态的
	private int docIsDynamic = 0;

	// 概览模板是否受保护:0,不保护;1,保护. 
	private int outlineIsProtect = 0;

	// 文档是否受保护:0,不保护;1,保护
	private int docIsProtect = 0;

	//频道概览页面的文件名(不含扩展名)
	private String pubFileName;
	//	频道概览页面的扩展名
	private String pubFileNameSuffix;
	
	//频道首页地址,手动指定
	private String indexpagepath;	
	//频道首页生成方式控制标识0－通过概览模版发布生成首页;1－通过指定的页面发布生成首页
	private int pageflag;
	
	//评论地址
	private String commentPagePath;
	
	// 站点的绝对路径,在创建频道时候用
	private String path; 
	
	//标识频道是否作为导航频道，0－普通频道，1－导航频道
	private boolean isNavigator;
	
	//导航频道的级别
	private int navigatorLevel = 0;
	/**
	 * 子频道列表
	 */
	private List subchnls;
	
	/**
	 * 自定义表单集
	 */
	private List customforms;	
    
	/**
	 * 频道概览图片
	 */
	private String outlinepicture;
	/**
	 * 频道图片导航
	 */
	private String mouseInImage;
	
	private String mouseOutImage;
	
	private String mouseClickImage;
	
	private String mouseUpImage;
	
	/**
	 * 专题报道标记
	 */
    private int specialflag;
    
    private String channelpuburl;
    
	/**
	 * @return Returns the specialflag.
	 */
	public int getSpecialflag() {
		return specialflag;
	}

	/**
	 * @param specialflag The specialflag to set.
	 */
	public void setSpecialflag(int specialflag) {
		this.specialflag = specialflag;
	}

	public String getMouseClickImage() {
		return mouseClickImage;
	}

	public void setMouseClickImage(String mouseClickImage) {
		this.mouseClickImage = mouseClickImage;
	}

	public String getMouseInImage() {
		return mouseInImage;
	}

	public void setMouseInImage(String mouseInImage) {
		this.mouseInImage = mouseInImage;
	}

	public String getMouseOutImage() {
		return mouseOutImage;
	}

	public void setMouseOutImage(String mouseOutImage) {
		this.mouseOutImage = mouseOutImage;
	}

	public String getMouseUpImage() {
		return mouseUpImage;
	}

	public void setMouseUpImage(String mouseUpImage) {
		this.mouseUpImage = mouseUpImage;
	}

	public List getCustomforms() {
		return customforms;
	}

	public void setCustomforms(List customforms) {
		this.customforms = customforms;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getChannelPath() {
		return channelPath;
	}

	public void setChannelPath(String channelPath) {
		this.channelPath = channelPath;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(long createUser) {
		this.createUser = createUser;
	}

	public int getDetailTemplateId() {
		return detailTemplateId;
	}

	public void setDetailTemplateId(int detailTemplateId) {
		this.detailTemplateId = detailTemplateId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getDocIsDynamic() {
		return docIsDynamic;
	}

	public void setDocIsDynamic(int docIsDynamic) {
		this.docIsDynamic = docIsDynamic;
	}

	public int getDocIsProtect() {
		return docIsProtect;
	}

	public void setDocIsProtect(int docIsProtect) {
		this.docIsProtect = docIsProtect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getOutlineIsDynamic() {
		return outlineIsDynamic;
	}

	public void setOutlineIsDynamic(int outlineIsDynamic) {
		this.outlineIsDynamic = outlineIsDynamic;
	}

	public int getOutlineIsProtect() {
		return outlineIsProtect;
	}

	public void setOutlineIsProtect(int outlineIsProtect) {
		this.outlineIsProtect = outlineIsProtect;
	}

	public int getOutlineTemplateId() {
		return outlineTemplateId;
	}

	public void setOutlineTemplateId(int outlineTemplateId) {
		this.outlineTemplateId = outlineTemplateId;
	}

	public long getParentChannelId() {
		return parentChannelId;
	}

	public void setParentChannelId(long parentChannelId) {
		this.parentChannelId = parentChannelId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public int getStaus() {
		return staus;
	}

	public void setStaus(int staus) {
		this.staus = staus;
	}

	public int getWorkflow() {
		return workflow;
	}

	public void setWorkflow(int workflow) {
		this.workflow = workflow;
	}
	
	/**
	 * 返回 1 表示当前流程是继承"父"的,具体流程总是由get/setWorkFlow来设置和获取的
	 * 返回 0 表示当前流程是自己定义好了的
	 */
	public int workflowIsFromParent() {
		return workflowIsFromParent;
	}

	public void setWorkflowIsFromParent(int workflowIsFromParent) {
		this.workflowIsFromParent = workflowIsFromParent;
	}

	public String getPubFileName() {
		return pubFileName;
	}

	public void setPubFileName(String pubFileName) {
		this.pubFileName = pubFileName;
	}

	public String getPubFileNameSuffix() {
		return pubFileNameSuffix;
	}

	public void setPubFileNameSuffix(String pubFileNameSuffix) {
		this.pubFileNameSuffix = pubFileNameSuffix;
	}

	public List getSubchnls() {
		return subchnls;
	}

	public void setSubchnls(List subchnls) {
		this.subchnls = subchnls;
	}
	
	public void addSubChnl(Channel chnl) {
		if(this.subchnls == null)
		{
			subchnls = new ArrayList();			
		}
		subchnls.add(chnl);
			
		
	}
	
	//标识频道是否作为导航频道，0－普通频道，1－导航频道
	public boolean isNavigator() {
		return isNavigator;
	}

	public void setNavigator(boolean isNavigator) {
		this.isNavigator = isNavigator;
	}

	//导航频道的级别
	public int getNavigatorLevel() {
		return navigatorLevel;
	}

	public void setNavigatorLevel(int navigatorLevel) {
		this.navigatorLevel = navigatorLevel;
	}

	public String getOutlinepicture() {
		return outlinepicture;
	}

	public void setOutlinepicture(String outlinepicture) {
		this.outlinepicture = outlinepicture;
	}

	public int getPageflag() {
		return pageflag;
	}

	public void setPageflag(int pageflag) {
		this.pageflag = pageflag;
	}

	public String getIndexpagepath() {
		return indexpagepath;
	}

	public void setIndexpagepath(String indexpagepath) {
		this.indexpagepath = indexpagepath;
	}

	public int getCommentTemplateId() {
		return commentTemplateId;
	}

	public void setCommentTemplateId(int commentTemplateId) {
		this.commentTemplateId = commentTemplateId;
	}

	public String getCommentPagePath() {
		return commentPagePath;
	}

	public void setCommentPagePath(String commentPagePath) {
		this.commentPagePath = commentPagePath;
	}


	public int compareTo(Object o) {
		if(o instanceof Channel)
		{
			Channel other = (Channel)o;
			if(other.getOrderNo() == this.getOrderNo())
				return 0;
			else if(other.getOrderNo() > this.getOrderNo())
			{
				return -1;
			}
			else
			{
				return 1;
			}
				
		}
		else
			return 0;
		
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public int getAuditflag() {
		return auditflag;
	}

	public void setAuditflag(int auditflag) {
		this.auditflag = auditflag;
	}

	public String getChannel_desc() {
		return channel_desc;
	}

	public void setChannel_desc(String channel_desc) {
		this.channel_desc = channel_desc;
	}

	public String getChannelpuburl() {
		return channelpuburl;
	}

	public void setChannelpuburl(String channelpuburl) {
		this.channelpuburl = channelpuburl;
	}
	
	
}
