package com.frameworkset.common.tag.html; 
 
import java.util.List;

import javax.servlet.jsp.JspException;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.container.Container;
import com.frameworkset.platform.cms.container.ContainerImpl;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;

public class OverFrameNavigator extends CMSBaseTag{
	private ChannelManagerImpl impl = new ChannelManagerImpl();
	private Container container = new ContainerImpl();
    private CMSTagUtil tagUtil = new CMSTagUtil();
	private int level = 1;
	private String siteid = "";
	private String style = ""; 
	private String channel = "";
	private String bgColor = ""; 
	private String bgImage = "";
	private String onMouseOverColor = "#FAFAFA"; 
	private String onMouseOverImage = "";
	private String subBgColor = "#F5F5F5";  
	private String subBgImage = "";
	private String menuBorderImg = "";
	private String subOutImg = "images/menuico_on.gif";
	private String subOverImg = "images/menuico_over.gif";
	private int levelDegree = 1;
	private int colWidth = 84;
	private int totalWidth = 800;	
	private int column_num = 0;	
	private int height = 26;
	private int fontsize = 12; 
	
	public int getFontsize() {
		return fontsize;
	}

	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}

	public String getMenuBorderImg() {
		return menuBorderImg;
	}

	public void setMenuBorderImg(String menuBorderImg) {
		this.menuBorderImg = menuBorderImg;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getBgImage() {
		return bgImage;
	}

	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getColumn_num() {
		return column_num;
	}

	public void setColumn_num(int column_num) {
		this.column_num = column_num;
	}

	public int getColWidth() {
		return colWidth;
	}

	public void setColWidth(int colWidth) {
		this.colWidth = colWidth;
	}	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevelDegree() {
		return levelDegree;
	}

	public void setLevelDegree(int levelDegree) {
		this.levelDegree = levelDegree;
	}

	public String getOnMouseOverColor() {
		return onMouseOverColor;
	}

	public void setOnMouseOverColor(String onMouseOverColor) {
		this.onMouseOverColor = onMouseOverColor;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSubBgColor() {
		return subBgColor;
	}

	public void setSubBgColor(String subBgColor) {
		this.subBgColor = subBgColor;
	}

	public String getSubBgImage() {
		return subBgImage;
	}

	public void setSubBgImage(String subBgImage) {
		this.subBgImage = subBgImage;
	}

	public int getTotalWidth() {
		return totalWidth;
	}

	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getOnMouseOverImage() {
		return onMouseOverImage;
	}

	public void setOnMouseOverImage(String onMouseOverImage) {
		this.onMouseOverImage = onMouseOverImage;
	}
    

    /**
     * 生成跨帧导航标签的HTML代码.
     * 加载外部的模板.vm文件,辅助生成HTML
     * 其中一级导航栏目是单独生成,其他层级的子导航栏目采用递归的方式生成
     * 导航频道数据从缓存里面获取
     * @return StringBuffer
     * @throws Exception
     */
	public StringBuffer navigatorTagShow() throws Exception{
		int currentlevel = 0;
		String site_id = "";
		String channelName = "";
		/* 加载外部模板 */
		StringBuffer sb = new StringBuffer();		 
		Context head = new VelocityContext();	
		head.put("bgImage",this.bgImage);
		head.put("bgColor",this.bgColor);
		head.put("fontsize",String.valueOf(fontsize));
		sb.append(tagUtil.loadTemplate("publish/overFrameNavigator/otherElement.vm",head));	
		if(context!=null){	
			site_id = context.getSiteID();
		}			
		/** 
		 * 外部设定不为空 则优先 
         *  若果设定了导航层数且大于1 则取当前频道的导航
         *  否则 取0级导航 
         */
		if(this.channel.length()>0){
			channelName = this.channel;
		}else{
			if(this.level>1){
				if(context instanceof CMSContext)
				{
					CMSContext channelContext = (CMSContext)context;
					channelName = "";
					site_id = channelContext.getSiteID();
				}
				if(context instanceof ChannelContext)
				{
					ChannelContext channelContext = (ChannelContext)context;				
					Channel chl = channelContext.getChannel();
					channelName = chl.getDisplayName();
					site_id = String.valueOf(chl.getSiteId());
				}
				if(context instanceof ContentContext)
				{
					ContentContext channelContext = (ContentContext)context;				
					Channel chl = channelContext.getChannel();
					channelName = chl.getDisplayName();
					site_id = String.valueOf(chl.getSiteId());
				}
			}
		}
		/*数据从缓存里面获取*/
		List list = impl.getNavigatChannel(site_id,channelName);
		sb.append("<div id='overFrameTopDown'><TABLE border=0 cellPadding=0 cellSpacing=0 class=overFrameindiv >");
		sb.append("<TR><TD><TABLE border=0 cellPadding=0 cellSpacing=0 height="+this.height+"><TR>");
		/* 获得首页链接地址 未指定频道 首页是站点地址*/
		Channel topChannel = null; 
//		String indexURL = context.getSite().getIndexFileName();
		String indexURL = "";
		//初始化容器
		
		if(site_id == null || "".equalsIgnoreCase(site_id)){
			String siteEname = SiteCacheManager.getInstance().getSite(site_id).getSecondName();
			container.init(siteEname, request,session, response);			
		}else{
			container.init(request,session, response);
		}
		indexURL = container.getPulishedSiteIndexUrlBySiteID(site_id);

//		indexURL = CMSTagUtil.getPublishedSitePath(context,indexURL);
		/* 指定了频道 首页是频道地址 */
		if(this.channel != null && this.channel.length()>0 && !"root".equalsIgnoreCase(this.channel)){			
			topChannel = impl.getChannelInfoByDisplayName(site_id,this.channel);
			//indexURL = CMSUtil.getPublishedChannelPath(context,topChannel);
			indexURL = container.getPublishedChannelUrlByDisplayName(this.channel);
		}	
		String TDOnMouseOverimg = "this.style.backgroundImage='url("+CMSTagUtil.getPublishedLinkPath(context,this.onMouseOverImage)+")';this.style.backgroundRepeat='repeat-x'" ;
		sb.append("<td width="+this.colWidth+" onmouseover=\""+TDOnMouseOverimg+"\" onmouseout=\"this.style.backgroundImage='url()'\" align=center><a href=\"javascript:parent.location.href='"+indexURL
				+"'\" onmouseover=\"clearpop();"
				+"\" onmouseout=\"this.style.backgroundImage='url()'\">首页</td>");
		
		/*一级菜单*/
		for(int i=0;i<list.size();i++){				
			Channel channel = (Channel)list.get(i);
			boolean flag = false;
			if(i==list.size()-1) flag = true;
			List sublist = impl.getNavigatChannel(site_id,channel.getDisplayName());
		    sb.append(createTopMenu(getStyle(),channel,sublist.size(),i,flag));			    
		}
		sb.append("</TR></TABLE></TD></TR></TABLE>");
		/*递归多级菜单*/
		for(int i=0;i<list.size();i++){	
			Channel channel = (Channel)list.get(i);
			if(this.levelDegree>=1){
		        sb.append(createSubHTML(channel,currentlevel + 1,i));
		    }
		}
		sb.append("</table> \n");
		String html = sb.toString();
		return sb;
	}
	
	/**
	 * 生成一级菜单的HTML
	 * 加载模板.vm文件辅助生成HTML
	 * @param String style:image/text 导航类型(文字/图片)导航
	 * @param Channel channel 频道对象
     * @param int size  一级频道的个数
	 * @param int count  当前频道计数
     * @param boolean flag 一级频道和其他频道的标示
	 * @return String 
	 */
	public String createTopMenu(String style,Channel channel,int size,int count,boolean flag)throws Exception{	    
		StringBuffer output = new StringBuffer();
		Context loop = new VelocityContext();
		String divName = String.valueOf(channel.getChannelId());
		String showName = channel.getName();
		String subObj = "subN"+divName;
		String parentObj = "N"+divName;
		String TDOnMouseOverimg = "this.style.backgroundImage='url("+CMSTagUtil.getPublishedLinkPath(context,this.onMouseOverImage)+")';this.style.backgroundRepeat='repeat-x'" ;
		if(size>0){/*有子频道*/
			//onmouseover="showMenu(this,'down',this,this,${subname},0,${top},${colWidth},${totalHeight},0)"				
			String obj = "document.all('"+parentObj+"')"; 
			String top = String.valueOf(this.height+2);			
			int totalHeight = (size) * (this.height + 1);
			String subHTML = "showMenu("+obj+",'down',"+obj+","+obj+","+subObj+",0,"+top+","+(this.colWidth-3)+","+totalHeight+",0);";
			loop.put("subHTML",subHTML);
			//showMenu(this,'out',this,this,${subObj},0,21,162,190,0);"			
			String subHTMLOut = "showMenu("+obj+",'out',"+obj+","+obj+","+subObj+",0,21,162,190,0);";
			loop.put("subHTMLOut",subHTMLOut);
		}else{/*无子频道*/
			loop.put("subHTML","pops[0].hide();");
			loop.put("subHTMLOut","");
		}
		loop.put("subObj","subN"+divName);
		loop.put("colWidth",String.valueOf(this.colWidth));
		loop.put("name",parentObj);
		loop.put("channelPath",container.getPublishedChannelUrlByDisplayName(channel.getDisplayName()));
		loop.put("displayName",showName);
		loop.put("height",String.valueOf(this.height));
		loop.put("bgColor",this.bgColor);
		loop.put("TDOnMouseOverimg",TDOnMouseOverimg);
		/*分割图片*/		
		loop.put("menuBorderImg",CMSTagUtil.getPublishedLinkPath(context,this.menuBorderImg));
		output.append(tagUtil.loadTemplate("publish/overFrameNavigator/loop.vm",loop));	
		String html = output.toString();
		return html;
	}
	
	/**
	 * 递归生成HTML 多级菜单
	 * 加载模板.vm文件辅助生成HTML
	 * @param Channel channel  频道对象
	 * @param int currentlevel  当前导航深度,每递归一次加一
	 * @param int count  当前导航个数计数
	 * @return String
	 * @throws Exception
	 */
	public String createSubHTML(Channel channel,int currentlevel,int count)throws Exception{
    	StringBuffer output = new StringBuffer();		
		List sublist = null;
		String site_id = context.getSiteID();
		/* 外部设定不为空 则优先 否则是上下文site_id */
		if(this.siteid.length()>0){
			site_id = this.siteid;
		}		
		sublist = impl.getNavigatChannel(site_id,channel.getDisplayName());
		if(sublist.size()<=0) return "";
		output.append("<DIV ID=\"subN"+channel.getChannelId()+"\" STYLE=\"display:none\"><DIV class='overFrameOuterDiv'>");
		for(int j=0;j<sublist.size();j++){
			Channel subchannel = (Channel)sublist.get(j);			
			Context text = new VelocityContext();
			text.put("channelPath",container.getPublishedChannelUrlByDisplayName(subchannel.getDisplayName()));
			text.put("displayName",subchannel.getName());
			text.put("height",String.valueOf(this.height));
			text.put("onMouseOverColor",this.onMouseOverColor);
			text.put("subBgColor",this.subBgColor);	
			String parentFix = "";
			for(int i=1;i<=currentlevel;i++){
				parentFix += "parent.";
			}
			text.put("parentFix",parentFix);
			text.put("subHTML",parentFix+"pops["+currentlevel+"].hide()");
			text.put("subHTMLOut","");
			text.put("fontsize",String.valueOf(this.fontsize));
			text.put("subOverImg",CMSTagUtil.getPublishedLinkPath(context,subOverImg));
			text.put("subOutImg",CMSTagUtil.getPublishedLinkPath(context,subOutImg));
			if(impl.hasNavChildChannel(subchannel) && (currentlevel < (this.levelDegree-1))){
				//,0,${top},${colWidth},${totalHeight},0
				List sub_subList = impl.getNavigatChannel(site_id,subchannel.getDisplayName());
				String parentObj = "parent.N"+channel.getChannelId();
				String subObj = "subN"+subchannel.getChannelId();
				String left = String.valueOf(this.colWidth-3);
				String top = String.valueOf(j*this.height);
				String totalHeight = String.valueOf(sub_subList.size() * (this.height + 1));	
				String subHTML = "parent.showMenu(this,'down',"+parentObj+",this.parentElement,";
				subHTML += subObj;
				subHTML += ","+left;
				subHTML += ","+top;
				subHTML += ","+this.colWidth+","+totalHeight+","+currentlevel+");";
				text.put("subHTML",subHTML);
				
				String subHTMLOut = "parent.showMenu(this,'out',this,this,"+subObj+",0,21,162,190,"+currentlevel+");";
				text.put("subHTMLOut",subHTMLOut);
			}			
			output.append(tagUtil.loadTemplate("publish/overFrameNavigator/subloop.vm",text));						
		}
		output.append("</DIV>");
		for(int j=0;j<sublist.size();j++){
			Channel subchannel = (Channel)sublist.get(j);
			/*递归 */
		    if(currentlevel < (this.levelDegree-1)) output.append(createSubHTML(subchannel,currentlevel + 1,j));
		}
		
		String tmp = output.toString();
		return tmp;
	}
	
	/**
	 * 输出HTML到页面
	 */
	public int doStartTag() throws JspException {
		int ret = super.doStartTag();
		
		try {
			pageContext.getOut().println(navigatorTagShow());		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	
	
	
	public static void main(String[] args){
	
	}

}
