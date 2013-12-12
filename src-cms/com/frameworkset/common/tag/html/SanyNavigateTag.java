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
 * Title: SanyNavigateTage.java
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
 * @Date 2012-7-18 上午8:33:02
 * @author biaoping.yin
 * @version 1.0.0
 */
public class SanyNavigateTag extends CMSBaseTag{
	private ChannelManagerImpl impl = new ChannelManagerImpl();
  
	
	private String channel ;
	
	private String homePageStr = "首页";

	private String target = "_self";
	
	private boolean showSiteName = false; 
	
	
	

	private static final String NAV_HEADER = "<div class=\"dhleft\"><div id=\"bluemenu\" class=\"bluetabs\"><ul>"; 
	private static final String NAV_FOOT = "</ul></div><script type=\"text/javascript\">tabdropdown.init(\"bluemenu\");</script></div>"; 
	/**
     * 三一外网导航
     * <div class="dhleft">
      <div id="bluemenu" class="bluetabs">
        <ul>
          <li><a href="#">首页</a></li>
          <li><a href="#" rel="dropmenu1_a">新闻</a>
            <div id="dropmenu1_a" class="dropmenudiv_b"> <a href="#">最新新闻</a> <a href="#">重点关注</a> <a href="introduce_department.html">专题报道</a></div>
          </li>
          <li><a href="#" rel="dropmenu1_b">视频</a>
            <div id="dropmenu1_b" class="dropmenudiv_b"> <a href="#">内部视频</a> <a href="#">媒体视频</a></div>
          </li>
          <li><a href="#" rel="dropmenu1_c">品牌与文化读物</a>
            <div id="dropmenu1_c" class="dropmenudiv_b"> <a href="#">品牌与文化读物</a> <a href="#">企业文化刊物</a> <a href="introduce_department.html">品牌故事</a></div>
          </li>
          <li><a href="#" rel="dropmenu1_d">VI规范</a>
            <div id="dropmenu1_d" class="dropmenudiv_b"> <a href="#">环境指示类</a> <a href="#">建筑指示类</a> <a href="#">办公指示类</a> <a href="#">公共指示类</a> <a href="#">文化形象类</a> <a href="#">警示类</a></div>
          </li>
        </ul>
      </div>
      <script type="text/javascript">
tabdropdown.init("bluemenu")
  </script>
    </div>
     * 
     * 
     * @return StringBuffer
     * @throws Exception
     */
	public StringBuffer navigatorTagShow() throws Exception{
		String site_id = "";  
		String channelName = "";
		String currentchannelName = "";
		/* 加载外部模板 */
		StringBuffer sb = new StringBuffer();	//定义输出串对象	
		sb.append(NAV_HEADER);
		
		if(context!=null){	
			site_id = context.getSiteID(); 
		}		
	
		/** 外部设定不为空 则优先 
         *  若果设定了导航层数且大于1 则取当前频道的导航
         *  否则 取0级导航 
         */
		if(this.channel != null && this.channel.length()>0){
			channelName = this.channel;
		}else{			
			if(context instanceof CMSContext)
			{
//				CMSContext channelContext = (CMSContext)context;
				channelName = "";
			}
			if(context instanceof ChannelContext)
			{
				ChannelContext channelContext = (ChannelContext)context;				
				Channel chl = channelContext.getChannel();
				currentchannelName = chl.getDisplayName();
			}
			if(context instanceof ContentContext)
			{
				ContentContext channelContext = (ContentContext)context;				
				Channel chl = channelContext.getChannel();
				currentchannelName = chl.getDisplayName();
			}
		}
		if("root".equalsIgnoreCase(channelName)) channelName = "";
		List list = impl.getNavigatChannel(site_id,channelName);//获取频道下的导航列表或者站点下的一级导航
		Channel topChannel = null; 
		/* 获得首页链接地址 未指定频道 首页是站点地址*/
		String indexURL = context.getSite().getIndexFileName();
		indexURL = CMSTagUtil.getPublishedSitePath(context,indexURL);//首页地址
		/* 指定了频道 首页是频道地址 */
		if(this.channel != null && this.channel.length()>0 && !"root".equalsIgnoreCase(this.channel)){
			topChannel = impl.getChannelInfoByDisplayName(site_id,this.channel);
			indexURL = CMSUtil.getPublishedChannelPath(context,topChannel);
		}		
		/* 首页链接地址 */
//		top.put("indexURL",indexURL);
		String target_str = "";
		if(!target.equals("_self"))
			target_str = "target=\"" + target + "\"";
		sb.append("<li><a ").append(target_str).append(" href=\"").append(indexURL).append("\">");
		/* 首页字符 */
		String inedxStr = this.homePageStr;
		if(this.showSiteName){
			String siteName = "";
			if(context != null && context.getSite() != null){
				siteName = context.getSite().getName();
			}
			inedxStr = siteName + this.homePageStr;
		}
		sb.append(inedxStr)
		  .append("</a></li>");//输出首页元素
		
		/* 页面初始化完成 */
		List subList = null;
		Channel level1channel = null;
		
		for(int i=0;i<list.size();i++){	//遍历一级导航			
			level1channel = (Channel)list.get(i);
			String linkPath = CMSTagUtil.getPublishedChannelPath(context,level1channel);
			String linkName = level1channel.getName();
			String openTarget= level1channel.getOpenTarget();
			if(! (openTarget==null||"".equals(openTarget))){
				target_str="target=\"" + openTarget + "\"";;
			}
			else
			{
				target_str="target=\"_self\"";
			}
			subList = impl.getNavigatChannel(site_id,level1channel.getDisplayName());//获取频道子导航列表
			boolean hasson = subList != null && subList.size() > 0;
			if(hasson)
				sb.append("<li><a ").append(target_str).append(" href=\"").append(linkPath).append("\" rel=\"dropmenu1_").append(level1channel.getChannelId()).append("\">").append(linkName).append("</a>");
			else
				sb.append("<li><a ").append(target_str).append(" href=\"").append(linkPath).append("\">").append(linkName).append("</a>");
			
			
			if(hasson)
			{
				Channel level2channel = null;
				sb.append("<div id=\"dropmenu1_").append(level1channel.getChannelId()).append("\" class=\"dropmenudiv_b\">");
				for(int j = 0; j < subList.size(); j++)//遍历子导航列表
				{
					level2channel = (Channel)subList.get(j);
					String linkPath_ = CMSTagUtil.getPublishedChannelPath(context,level2channel);
					String linkName_ = level2channel.getName();
					openTarget = level2channel.getOpenTarget();
					if(! (openTarget==null||"".equals(openTarget))){
						target_str="target=\"" + openTarget + "\"";
					}
					else
					{
						target_str="target=\"_self\"";
					}
					sb.append("<a ").append(target_str).append(" href=\"").append(linkPath_).append("\">").append(linkName_).append("</a>");
				}
				sb.append("</div>");
			}
		  	sb.append("</li>")    ;
		}
		sb.append(NAV_FOOT);	
		return sb;
	}
	
	
	public int doStartTag() throws JspException{
		int ret = super.doStartTag();
		try {
			String outstr = navigatorTagShow().toString();
			out.print(outstr);			
		} catch (Throwable e) {
			throw new JspException(e);
		}
		return ret;
		
	}
	
	public static void main(String[] args){
		
	}

	

	public String getHomePageStr() {
		return homePageStr;
	}

	public void setHomePageStr(String homePageStr) {
		this.homePageStr = homePageStr;
	}

	

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public boolean isShowSiteName() {
		return showSiteName;
	}

	public void setShowSiteName(boolean showSiteName) {
		this.showSiteName = showSiteName;
	}


	@Override
	public void doFinally() {
		channel = null;
		
		homePageStr = "首页";

		target = "_self";
		
		showSiteName = false; 
		
		super.doFinally();
	}



}
