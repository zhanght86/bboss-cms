package com.frameworkset.common.tag.html;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;

public class CZNavigatorTag extends CMSBaseTag{
	private ChannelManagerImpl impl = new ChannelManagerImpl();
    private CMSTagUtil tagUtil = new CMSTagUtil();
	private String style = "text";
	private String channel ;
	private int column_num = 3;	
	private String homePageStr = "首页";
	private String divName = "navDivPart";
	private String target = "_self";
	private String menuBorderImg = "";
	private String bgColor = "#ED383B";
	private int colWidth = 65;
	private int subColWidth = 50;
	private int totalWidth = 800;	
	private int subWidth = 280;	
	private int height = 26;
	private int paddingLeft = 0;
	private int paddingRight = 0;
	private boolean showSiteName = false; 
	//wei.li新增
	private String fontColor = "#ffffff";
	//
	
//	private String bgImage = "";
//	private String onMouseOverColor = "#FAFAFA";
//	private String onMouseOverImage = "";
	private String subBgColor = "#F5F5F5";
//	private String subBgImage = "";
	
	
	/**
	 * 类型转换方法:String转换成Int
	 * @param String str
	 * @return Int
	 */
	public int String2Int(String str){
		int intNumber = 1;
		try {
			intNumber = Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}
		return intNumber;
	}
	
	/**
	 * 通过Layout得到样式 vm
	 * @param String layout 布局
	 * @return String 返回样式布局模板
	 */
	public String getCssVmPath(String layout){
		String vmPath = "publish/navigator/normalCss.vm";
		
		return vmPath;
	}
	
	/**
     * 生成导航标签的HTML代码.
     * 加载外部的模板.vm文件,辅助生成HTML
     * 其中一级导航栏目是单独生成,其他层级的子导航栏目采用递归的方式生成
     * 导航频道数据从缓存里面获取
     * 根据layout的不同取值,输出不同的导航布局
     * @return StringBuffer
     * @throws Exception
     */
	public StringBuffer navigatorTagShow() throws Exception{
		String site_id = "";  
		String channelName = "";
		/* 加载外部模板 */
		StringBuffer sb = new StringBuffer();		 
		Context top = new VelocityContext();
		top.put("colWidth",this.colWidth+"");
		top.put("totalWidth",this.totalWidth+"");
		top.put("height",String.valueOf(this.height));
		top.put("bgColor",this.bgColor);
//		top.put("bgImage",this.bgImage);
		top.put("subBgColor",this.subBgColor);
//		top.put("subBgImage",this.subBgImage);		
//		top.put("fontsize",String.valueOf(this.fontsize));
//		top.put("onMouseOverColor",this.onMouseOverColor);
//		top.put("onMouseOverImage",CMSTagUtil.getPublishedLinkPath(context,this.onMouseOverImage));
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
				CMSContext channelContext = (CMSContext)context;
				channelName = "";
			}
			if(context instanceof ChannelContext)
			{
				ChannelContext channelContext = (ChannelContext)context;				
				Channel chl = channelContext.getChannel();
				channelName = chl.getDisplayName();
			}
			if(context instanceof ContentContext)
			{
				ContentContext channelContext = (ContentContext)context;				
				Channel chl = channelContext.getChannel();
				channelName = chl.getDisplayName();
			}
		}
		if("root".equalsIgnoreCase(channelName)) channelName = "";
		List list = impl.getNavigatChannel(site_id,channelName);
		Channel topChannel = null; 
		/* 获得首页链接地址 未指定频道 首页是站点地址*/
		String indexURL = context.getSite().getIndexFileName();
		indexURL = CMSTagUtil.getPublishedSitePath(context,indexURL);
		/* 指定了频道 首页是频道地址 */
		if(this.channel != null && this.channel.length()>0 && !"root".equalsIgnoreCase(this.channel)){
			topChannel = impl.getChannelInfoByDisplayName(site_id,this.channel);
			indexURL = CMSUtil.getPublishedChannelPath(context,topChannel);
		}		
		/* 首页链接地址 */
		top.put("indexURL",indexURL);
		/* 首页字符 */
		String inedxStr = this.homePageStr;
		if(this.showSiteName){
			String siteName = "";
			if(context != null && context.getSite() != null){
				siteName = context.getSite().getName();
			}
			inedxStr = siteName + this.homePageStr;
		}
		top.put("homePageStr",inedxStr);
		top.put("divName",this.divName);
		top.put("count",String.valueOf(list.size()));
		top.put("bgColor",this.bgColor);
		top.put("fontColor",this.fontColor+"");
		sb.append(tagUtil.loadTemplate("publish/CZNavigator/top.vm",top));
		/* 页面初始化完成 */
		
		for(int i=0;i<list.size();i++){				
			topChannel = (Channel)list.get(i);
		    sb.append(createHTMlByStyle(getStyle(),topChannel,i,list.size()));		    
		}
		sb.append("</TR></TABLE></TD></TR></TABLE>");
		/* 顶层菜单结束 */
		
		/* 子菜单 */
		Channel subChannel = null;
		List subList = new java.util.ArrayList();
		sb.append("<DIV style=\"Z-INDEX: 1000; POSITION: relative\" align=left>");
		for(int i=0;i<list.size();i++){				
			topChannel = (Channel)list.get(i);
			subList = impl.getNavigatChannel(site_id,topChannel.getDisplayName());
			if(subList.size()>0){
				sb.append(createSubHTML(subList,i));
			}else{
				sb.append("<div id=" + this.divName + i +">").append("</div>");
			}
		}		
		/* 子菜单结束 */		
		return sb;
	}
	
	/**
	 * 生成根据图片类型/文字类型生成导航HTML
	 * @param style
	 * @param channel
	 * @param count
	 * @param size
	 * @return
	 * @throws Exception 
	 * CZNavigatorTag.java
	 * @author: ge.tao
	 */
	public String createHTMlByStyle(String style,Channel channel,int count,int size)throws Exception{
		IMG img = new IMG();
		A a = new A();		
		StringBuffer sb = new StringBuffer();		 
		Context menu = new VelocityContext();		
		/*加分隔图片 第一层*/
		if((this.menuBorderImg.length()>0)){
			sb.append("<td><img src='"+CMSTagUtil.getPublishedLinkPath(context,this.menuBorderImg)+"'/></td>");
		}
		if("image".equals(getStyle())){
//			img.setSrc(CMSTagUtil.getPublishedLinkPath(context,channel.getMouseUpImage()));
//			img.setOnClick("change(this,'"+CMSTagUtil.getPublishedLinkPath(context,channel.getMouseClickImage())+"'");
//			img.setOnMouseOver("over(this,'"+CMSTagUtil.getPublishedLinkPath(context,channel.getMouseInImage())+"')");
//			img.setOnMouseOut("out(this,'"+CMSTagUtil.getPublishedLinkPath(context,channel.getMouseOutImage())+"')");
//			output.append(img.toString());				
		} else{	
			String linkPath = CMSTagUtil.getPublishedChannelPath(context,channel);
			String linkName = channel.getName();			
			menu.put("linkPath",linkPath);
			menu.put("linkName",linkName);
			menu.put("count",String.valueOf(count));
			menu.put("colWidth",this.colWidth+"");
			menu.put("fontColor",this.fontColor+"");
			sb.append(tagUtil.loadTemplate("publish/CZNavigator/menuLoop.vm",menu));
			if(count != size-1){
				sb.append("<TD vAlign=center align=middle><FONT color="+fontColor+">|</FONT></TD>");
			}
		} 
		return sb.toString();
	}
	
	/**
	 * 生成HTML 子菜单
	 * @param sublist
	 * @param position
	 * @return
	 * @throws Exception 
	 * CZNavigatorTag.java
	 * @author: ge.tao
	 */
	public String createSubHTML(List sublist,int position)throws Exception{
		StringBuffer sb = new StringBuffer();
		Channel subchannel = null;
		Context top = new VelocityContext();
		Context loop = new VelocityContext();
		Context down = new VelocityContext();
		int left = (this.colWidth - this.paddingLeft + this.paddingRight);
		if(position > 0)
			left = (position+1) * (this.colWidth+6)-this.paddingLeft + this.paddingRight;
		int size = sublist.size();
		String imgStr = "<TR>";
//		sb.append(tagUtil.loadTemplate("publish/navigator/js.vm",pub));
		top.put("divName",this.divName);
		top.put("count",String.valueOf(position));
		top.put("left",String.valueOf(left));
		top.put("subWidth",String.valueOf(this.subWidth));
		top.put("innerWidth",String.valueOf(this.subWidth-10));
		top.put("outerWidth",String.valueOf(this.subWidth+2));
		//设置图片
		String imageUrl = CMSTagUtil.getPublishedLinkPath(context,"images/simpleNavigator_layer_bar.gif");
		top.put("imageUrl",imageUrl);		
		sb.append(tagUtil.loadTemplate("publish/CZNavigator/subMenuTop.vm",top));
		imageUrl = CMSTagUtil.getPublishedLinkPath(context,"images/simpleNavigator_three.gif");
		loop.put("imageUrl",imageUrl);
		for(int i=0; i<size ;i++){
			subchannel = (Channel)sublist.get(i);	
			String linkPath = CMSTagUtil.getPublishedChannelPath(context,subchannel);
			String linkName = subchannel.getName();
			
			loop.put("linkPath",linkPath);
			loop.put("linkName",linkName);
			loop.put("target",this.target);
			loop.put("subColWidth",String.valueOf(this.subColWidth));
			loop.put("bgColor",this.bgColor);
			
			//判断是否换行 column_num
			//第一条 或者 换行后的头一条
			if(i==0 || (i!=0 && (i%this.column_num==0))){
				sb.append(imgStr);
			}
			sb.append(tagUtil.loadTemplate("publish/CZNavigator/subLoop.vm",loop));
			//换行的最后一条 或者整个记录的最后一条
			if((i%this.column_num == this.column_num-1) || (i==size-1)){
				sb.append("</tr>");
			}
			
		} 
		sb.append(tagUtil.loadTemplate("publish/CZNavigator/subMenuDown.vm",down));
		return sb.toString();
	}
	
	public int doStartTag() throws JspException{
		int ret = super.doStartTag();
		try {
			String outstr = navigatorTagShow().toString().replaceAll("<ul></ul>","");
			pageContext.getOut().println(outstr);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
		
	}
	
	public static void main(String[] args){
		
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getHomePageStr() {
		return homePageStr;
	}

	public void setHomePageStr(String homePageStr) {
		this.homePageStr = homePageStr;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getSubWidth() {
		return subWidth;
	}

	public void setSubWidth(int subWidth) {
		this.subWidth = subWidth;
	}

	public int getColumn_num() {
		return column_num;
	}

	public void setColumn_num(int column_num) {
		this.column_num = column_num;
	}

	public int getSubColWidth() {
		return subColWidth;
	}

	public void setSubColWidth(int subColWidth) {
		this.subColWidth = subColWidth;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getColWidth() {
		return colWidth;
	}

	public void setColWidth(int colWidth) {
		this.colWidth = colWidth;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getMenuBorderImg() {
		return menuBorderImg;
	}

	public void setMenuBorderImg(String menuBorderImg) {
		this.menuBorderImg = menuBorderImg;
	}

	public int getTotalWidth() {
		return totalWidth;
	}

	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public boolean isShowSiteName() {
		return showSiteName;
	}

	public void setShowSiteName(boolean showSiteName) {
		this.showSiteName = showSiteName;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}


	
	
}
