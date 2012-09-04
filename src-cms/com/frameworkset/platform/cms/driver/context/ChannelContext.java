package com.frameworkset.platform.cms.driver.context;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.publish.PublishMode;


public interface ChannelContext extends PagineContext{
	/**
	 * 获取频道id
	 * @return
	 */
	public String getChannelID();
	
	/**
	 * 获取频道的细览模版
	 * @return
	 */
	public Template getDetailTemplate();
	/**
	 * 获取频道的概览模版
	 * @return
	 */
	public Template getOutlineTemplate();
	
	public String getSiteID();
	
	/**
	 * 判断频道是否有概览模版
	 * @return
	 */
	public boolean haveOutlineTemplate();
	
	
	/**
	 * 判断频道是否有概览模版
	 * @return
	 */
	public boolean haveDetailTemplate();

	public String getChannelPath();
	
	public Channel getChannel();
	
	
	/**
	 * 获取频道首页来源类型
	 * 0－频道模版首页类型
	 * 1－直接指定首页类型
	 * 2－指定文档为频道首页
	 * @return
	 */
	public int getChannelIndexType();
	
	/**
	 * 判断频道首页是否是由模版生成
	 * @return
	 */
	public boolean isTemplateType();
	
	/**
	 * 判断频道首页是否是由指定的页面生成
	 * @return
	 */
	public boolean isCustomPageType();
	
	/**
	 * 判断频道的首页是否是由细览页面生成
	 * @return
	 */
	public boolean isDocDetailPageType();
	

	/**
	 * 判断频道的首页是否是由其他频道的首页生成
	 * @return
	 */
	public boolean isRefchannelType();

	/**
	 * 获取频道文档的发布模式
	 * @return
	 */
	public PublishMode getDocumentPublishMode() ;

}
