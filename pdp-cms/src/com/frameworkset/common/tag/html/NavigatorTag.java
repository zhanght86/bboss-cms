package com.frameworkset.common.tag.html;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.A;
import com.frameworkset.common.ecs.IMG;
import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.util.CMSUtil;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;

public class NavigatorTag extends CMSBaseTag{
	private ChannelManagerImpl impl = new ChannelManagerImpl();
    private CMSTagUtil tagUtil = new CMSTagUtil();
	private int level = 1;
	private String siteid = "";
	private String style = ""; 
	private String layout = "normal";
	private String channel = "";
	private String bgColor = "";
	private String bgImage = "";
	private String onMouseOverColor = "#FAFAFA";
	private String onMouseOverImage = "";
	private String subBgColor = "#F5F5F5";
	private String subBgImage = "";
	private String menuBorderImg = "";
	private int levelDegree = 1;
	private int colWidth = 84;
	private int totalWidth = 800;	
	private int column_num = 0;	
	private int height = 26;
	private int fontsize = 14;

	public int getLevelDegree() {
		return levelDegree;
	}

	public void setLevelDegree(int levelDegree) {
		this.levelDegree = levelDegree;
	}
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public int getColumn_num() {
		return column_num;
	}

	public void setColumn_num(int column_num) {
		this.column_num = column_num;
	}
	
	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) { 
		this.layout = layout;
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

	public int getTotalWidth() {
		return totalWidth;
	}

	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public String getOnMouseOverColor() {
		return onMouseOverColor;
	}

	public void setOnMouseOverColor(String onMouseOverColor) {
		this.onMouseOverColor = onMouseOverColor;
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

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

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

	public String getOnMouseOverImage() {
		return onMouseOverImage;
	}

	public void setOnMouseOverImage(String onMouseOverImage) {
		this.onMouseOverImage = onMouseOverImage;
	}
	
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
		if("top-down".equals(getLayout())){
			vmPath = "publish/navigator/topDownCss.vm";
		}else if("top-left".equals(getLayout())){
			vmPath = "publish/navigator/topLeftCss.vm";
		}else if("left".equals(getLayout())){
			vmPath = "publish/navigator/leftCss.vm";
		}
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
		int currentlevel = 0;
		String site_id = ""; 
		String channelName = "";
		/* 加载外部模板 */
		StringBuffer sb = new StringBuffer();		 
		Context head = new VelocityContext();
		int left = this.colWidth-2;		
		head.put("left",left+"");
		head.put("colWidth",this.colWidth+"");
		head.put("totalWidth",this.totalWidth+"");
		head.put("bgColor",this.bgColor);
		head.put("bgImage",this.bgImage);
		head.put("subBgColor",this.subBgColor);
		head.put("subBgImage",this.subBgImage);
		head.put("height",String.valueOf(this.height));
		head.put("fontsize",String.valueOf(this.fontsize));
		head.put("onMouseOverColor",this.onMouseOverColor);
		head.put("onMouseOverImage",CMSTagUtil.getPublishedLinkPath(context,this.onMouseOverImage));
		//if(this.onMouseOverImage.length()>0) head.put("onMouseOverColor",""); 
		sb.append(tagUtil.loadTemplate(getCssVmPath(getLayout()),head));
		if(context!=null){	
			site_id = context.getSiteID(); 
		}		
	
		/** 外部设定不为空 则优先 
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
		}
		List list = impl.getNavigatChannel(site_id,channelName);
		if("left".equals(getLayout()) || "top-down".equals(getLayout()) || "top-left".equals(getLayout())){			
			/* 特殊布局 */
			Channel topChannel = null;
			sb.append("<div class='leftNavMenu'><ul>");
			/* 获得首页链接地址 未指定频道 首页是站点地址*/
			String indexURL = context.getSite().getIndexFileName();
			indexURL = CMSTagUtil.getPublishedSitePath(context,indexURL);
			/* 指定了频道 首页是频道地址 */
			if(this.channel.length()>0){
				topChannel = impl.getChannelInfoByDisplayName(site_id,this.channel);
				indexURL = CMSUtil.getPublishedChannelPath(context,topChannel);
			}
			/*汗,变态*/
			sb.append("<li><!--[if lte IE 6]><a href="+indexURL+">首页<table class='subtable'><tr><td><![endif]--><!--[if lte IE 6]></td></tr></table></a><![endif]-->");
			for(int i=0;i<list.size();i++){				
				Channel channel = (Channel)list.get(i);
			    sb.append(createHTMlByStyle(getStyle(),channel,true));
			    if(this.levelDegree>=1){
			        sb.append(createSubHTML(channel,currentlevel + 1));
			    }
			    sb.append("<!--[if lte IE 6]></td></tr></table></a><![endif]-->");
			    sb.append("</li>");
			}
			sb.append("</ul></div>");
		}else{/* 普通布局 */	
			//<div id="logonav">
			/* 加载外部模板 */
			Context normal = new VelocityContext();	
			normal.put("width",this.totalWidth+"");
			sb.append(tagUtil.loadTemplate("publish/navigator/normal.vm",normal));
			/* 导航内容 */					
			for(int i=0;i<list.size();i++){	
				String flag="br";
				Channel channel = (Channel)list.get(i);	
				/* 一行的列数 */
				if(getColumn_num()>0){
					column_num =  getColumn_num();
				}else{
					/* 缺省是10列 */
					column_num = 10;
				}
				if((i+1)%column_num!=0 || i==0){
				    flag = "";
				}
				if("image".equals(getStyle())){					
					Context img = new VelocityContext();		
					img.put("mouseUpImage",CMSTagUtil.getPublishedLinkPath(context,channel.getMouseOutImage()));
					img.put("mouseClickImage",CMSTagUtil.getPublishedLinkPath(context,channel.getMouseClickImage()));
					img.put("mouseInImage",CMSTagUtil.getPublishedLinkPath(context,channel.getMouseInImage()));
					img.put("mouseOutImage",CMSTagUtil.getPublishedLinkPath(context,channel.getMouseOutImage()));
					img.put("flag",flag);
					sb.append(tagUtil.loadTemplate("publish/navigator/imageloop.vm",img));						
				}else{
					//show_info.append(channel.getDisplayName());
					Context text = new VelocityContext();
					text.put("displayName",channel.getName());
					text.put("channelPath",channel.getChannelPath());
					text.put("flag",flag);
					sb.append(tagUtil.loadTemplate("publish/navigator/textloop.vm",text));						
				}				
				//sb.append("<td nowrap><a href=\""+super.handleHref(channel.getChannelPath())+"\">"+show_info.toString()+"</a></td> \n");
			}
			sb.append("</table> \n");
			String html = sb.toString();						
		}
		Context pub = new VelocityContext();
		sb.append(tagUtil.loadTemplate("publish/navigator/js.vm",pub));
		return sb;
	}
	
	/**
	 * 递归生成HTML 多级菜单
	 * @param Channel channel 频道对象
	 * @param int currentlevel 当前导航深度,每递归一次加一
	 * @return String
	 * @throws Exception
	 */
	public String createSubHTML(Channel channel,int currentlevel)throws Exception{
		StringBuffer output = new StringBuffer();		
		List sublist = null;
		String out = "";/*输出*/
		String param = "";/*本身*/
		String subparam = "";/*子串*/
		output.append("<ul>");
		String site_id = context.getSiteID();
		/* 外部设定不为空 则优先 否则是上下文site_id */
		if(this.siteid.length()>0){
			site_id = this.siteid;
		}
		sublist = impl.getNavigatChannel(site_id,channel.getDisplayName());
		if(sublist==null)return "";
		for(int j=0;j<sublist.size();j++){
			Channel subchannel = (Channel)sublist.get(j);			
			param = createHTMlByStyle(getStyle(),subchannel,false);			
			if(currentlevel < this.levelDegree)
				/*加入递归 前面加++*/
				subparam = createSubHTML(subchannel,currentlevel + 1);	
			if(subparam.length()>0){
				/*有字串 前面的li要特殊处理*/
				param =  createHTMlByStyle("text",subchannel,false);
				/*加前面串*/				
				output.append(param);
				/*加子串*/
				output.append(subparam);
				/*加后串*/
				/*<!--[if lte IE 6]></td></tr></table></a><![endif]-->	*/
				output.append("<!--[if lte IE 6]></td></tr></table></a><![endif]-->");
				output.append("</li>");
				
			}			
		}
		output.append("</ul>");
		String tmp = output.toString();
		return tmp;
	}
	
	/**
	 * 生成根据图片类型/文字类型生成导航HTML
	 * @param style:image/text
	 * @param channel
	 * @param flag true 一级菜单,false 子菜单
	 * @return
	 */
	public String createHTMlByStyle(String style,Channel channel,boolean flag)throws Exception{
		IMG img = new IMG();
		A a = new A();		
		StringBuffer output = new StringBuffer();		
		/*加分隔图片 第一层*/
		if(flag && (this.menuBorderImg.length()>0)){
			output.append("<li><img src='"+CMSTagUtil.getPublishedLinkPath(context,this.menuBorderImg)+"'/></li><li>");
		}else output.append("<li>");
		if("image".equals(getStyle())){	
			img.setSrc(CMSTagUtil.getPublishedLinkPath(context,channel.getMouseUpImage()));
			img.setOnClick("change(this,'"+CMSTagUtil.getPublishedLinkPath(context,channel.getMouseClickImage())+"'");
			img.setOnMouseOver("over(this,'"+CMSTagUtil.getPublishedLinkPath(context,channel.getMouseInImage())+"')");
			img.setOnMouseOut("out(this,'"+CMSTagUtil.getPublishedLinkPath(context,channel.getMouseOutImage())+"')");
			output.append(img.toString());				
		} else{	
			a.setHref(CMSTagUtil.getPublishedChannelPath(context,channel ));
			a.setTagText(channel.getName());	
			a.setStyle("display:none");
			output.append(a.toString());
			output.append("<!--[if lte IE 6]>");
			
			output.append("<a href='"+CMSTagUtil.getPublishedChannelPath(context,channel));
			output.append("'>"+channel.getName());
			output.append("<table class='subtable'><tr><td><![endif]-->");
			
		} 
		String html = output.toString();
		return html;
	}

	
	public int doStartTag() throws JspException{
		int ret = super.doStartTag();
		try {
			String outstr = navigatorTagShow().toString().replaceAll("<ul></ul>","");
			pageContext.getOut().println(outstr);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
		
	}
	
	public static void main(String[] args){
		String[] abc1 = {};
		String[] abc = new String[5];
	}

	@Override
	public void doFinally() {
		level = 1;
		 siteid = "";
		  style = ""; 
		 layout = "normal";
		 channel = "";
		  bgColor = "";
		 bgImage = "";
		onMouseOverColor = "#FAFAFA";
		onMouseOverImage = "";
		subBgColor = "#F5F5F5";
		subBgImage = "";
		menuBorderImg = "";
		levelDegree = 1;
		colWidth = 84;
		totalWidth = 800;	
		column_num = 0;	
		height = 26;
		fontsize = 14;
		super.doFinally();
	}


	
	
}
