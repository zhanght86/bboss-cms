package com.frameworkset.common.tag.html;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * <p>
 * Title: SanyLeftNavTag.java
 * </p>
 * 
 * <p>
 * Description: <div class="leftmenu">
      	  <h1>新闻</h1>
      	  <ul>
          	<li class="select"><a href="#">最新新闻</a></li>
            <li><a href="#">重点关注</a></li>
            <li><a href="#">专题报道</a></li>
          </ul>
      </div>
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
 * @Date 2012-7-18 上午10:33:57
 * @author biaoping.yin
 * @version 1.0.0
 */
public class SanyLeftNavTag extends CMSBaseTag{
	private ChannelManagerImpl impl = new ChannelManagerImpl();
    private CMSTagUtil tagUtil = new CMSTagUtil();
			
	private String channel = "";
	
	private String target = "_self";
	private int level = 3;
	
	private static final String NAV_HEADER = "<div class=\"leftmenu\">";
	private static final String NAV_FOOT = "</div>";

	
	
	
	
	public StringBuffer navigatorTagShow() throws Exception{
		
		/* 加载外部模板 */
		StringBuffer sb = new StringBuffer();			
		sb.append(NAV_HEADER);
		/* parent_channel=="" 为所以导航层次为0的所有频道 */
		String currentChannel = null;
		String channelDisplayName = "";
		String channelName= "";
		
		List list = null;
		if(this.channel.length()<=0) {

			if(context instanceof CMSContext)
			{

			}
			if(context instanceof ChannelContext)
			{
				ChannelContext channelContext = (ChannelContext)context;				
				Channel chl = channelContext.getChannel();
				currentChannel = chl.getDisplayName();
				Channel parent = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(chl.getParentChannelId() + "");
				channelDisplayName = parent.getDisplayName();
				channelName = parent.getName();
				//获取子导航列表
				list = impl.getNavigatChannel(context.getSiteID(),channelDisplayName);
			}
			if(context instanceof ContentContext)
			{
				ContentContext channelContext = (ContentContext)context;				
				Channel chl = channelContext.getChannel();
				currentChannel = chl.getDisplayName();
				Channel parent = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(chl.getParentChannelId() + "");
				channelDisplayName = parent.getDisplayName();
				channelName = parent.getName();
				//获取子导航列表
				list = impl.getNavigatChannel(context.getSiteID(),channelDisplayName);
			}
			
		}else{
			channelDisplayName = this.channel;
			Channel chnl = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(channelDisplayName);
			channelName = chnl.getName();
			if(context instanceof CMSContext)
			{

			}
			if(context instanceof ChannelContext)
			{
				ChannelContext channelContext = (ChannelContext)context;				
				Channel chl = channelContext.getChannel();
				currentChannel = chl.getDisplayName();
				
			}
			if(context instanceof ContentContext)
			{
				ContentContext channelContext = (ContentContext)context;				
				Channel chl = channelContext.getChannel();
				currentChannel = chl.getDisplayName();
				
			}
			//获取子导航列表
			list = impl.getNavigatChannel(context.getSiteID(),channelDisplayName);
		}
		String target_str = "";
		if(!target.equals("_self"))
			target_str = "target=\"" + target + "\"";
		
			
		/* 左边菜单
		 * <h1>新闻</h1>
      	  <ul>
          	<li class="select"><a href="#">最新新闻</a></li>
            <li><a href="#">重点关注</a></li>
            <li><a href="#">专题报道</a></li>
          </ul>
		 *  */
		int currentLevel = 2;
		sb.append("<h1>").append(channelName).append("</h1>");
		if(list != null && list.size() > 0)
		{
			sb.append("<ul>");
			String url = null;
			String level1channelName = null;
			for(int i=1;i<=list.size();i++){	
				Channel channel = (Channel)list.get(i-1);
				level1channelName = channel.getDisplayName();
				String channelName_ = channel.getName();
				url = CMSTagUtil.getPublishedChannelPath(context,channel);
				if(currentChannel != null && currentChannel.equals(level1channelName))
				{
					sb.append("<li class=\"select\"><a ").append(target_str).append(" href=\"").append(url).append("\">").append(channelName_).append("</a>");
					//获取子导航列表
					buildThirdNav(level1channelName,currentChannel,sb,target_str,currentLevel ++);
					sb.append("</li>");
				}
				else
				{
					sb.append("<li ><a ").append(target_str).append(" href=\"").append(url).append("\">").append(channelName_).append("</a>");
					//获取子导航列表
					//<ul><li><a href="#">企业发展</a></li><li class="select"><a href="#">产品研发</a></li><li><a href="#">公益活动</a></li></ul>
					buildThirdNav(level1channelName,currentChannel,sb,target_str,currentLevel ++);
					sb.append("</li>");
				}
			    
			}	
			sb.append("</ul>");
		}
		sb.append(NAV_FOOT);
		
		return sb;
	}
	
	private void buildThirdNav(String level1channelName,String currentChannel,StringBuffer sb,String target_str,int currentLevel) throws Exception
	{
		if(currentLevel > level)
			return;
		//获取子导航列表
		List list = impl.getNavigatChannel(context.getSiteID(),level1channelName);
		if(list != null && list.size() > 0)
		{
			sb.append("<ul>");
			String url = null;
			
			for(int i=1;i<=list.size();i++){	
				Channel channel = (Channel)list.get(i-1);
				level1channelName = channel.getDisplayName();
				String channelName_ = channel.getName();
				url = CMSTagUtil.getPublishedChannelPath(context,channel);
				if(currentChannel != null && currentChannel.equals(level1channelName))
				{
					sb.append("<li class=\"select\"><a ").append(target_str).append(" href=\"").append(url).append("\">").append(channelName_).append("</a>");
					buildThirdNav(level1channelName,currentChannel,sb,target_str,currentLevel + 1);
					sb.append("</li>");
				}
				else
				{
					sb.append("<li ><a ").append(target_str).append(" href=\"").append(url).append("\">").append(channelName_).append("</a>");
					buildThirdNav(level1channelName,currentChannel,sb,target_str,currentLevel + 1);
					sb.append("</li>");
				}
			    
			}	
			sb.append("</ul>");
		}
	}

	
	public int doEndTag() throws JspException{		
		try {
			out.print(navigatorTagShow().toString());			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}



	@Override
	public void doFinally() {
		channel = "";
		target = "_self";
		level = 3;
		super.doFinally();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}


}
