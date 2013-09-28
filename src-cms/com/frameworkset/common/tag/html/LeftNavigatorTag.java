package com.frameworkset.common.tag.html;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.context.Context;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;

public class LeftNavigatorTag extends CMSBaseTag{
	private ChannelManagerImpl impl = new ChannelManagerImpl();
    private CMSTagUtil tagUtil = new CMSTagUtil();
	private String siteid = "";		
	private String channel = "";
	private String displayName = "";
	private String width = "200";
	private String parentBgColor = "";
	private String childBgColor = "";
	private String onMouseOver = "";
	private String nextMenuPrompt = ">>";
	private int column_num = 1;	
	private int levelDegree = 1;

	public int getLevelDegree() {
		return levelDegree;
	}

	public void setLevelDegree(int levelDegree) {
		this.levelDegree = levelDegree;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	
	public void setPageContext(PageContext pageContext){
		super.setPageContext(pageContext);	
	}
	
	public int String2Int(String str){
		int intNumber = 1;
		try {
			intNumber = Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}
		return intNumber;
	}
	
	/**
	 * 通过Layout得到CSS vm
	 * @param layout
	 * @return
	 */
	public String getCssVmPath(String layout){
		String vmPath = "publish/navigator/normalCss.vm";
		
		return vmPath;
	}
	
	
	public StringBuffer navigatorTagShow() throws Exception{
		int currentlevel = 0;
		/* 加载外部模板 */
		StringBuffer sb = new StringBuffer();			
		Context head = new VelocityContext();
		/* 背景图片的优先级比背景颜色优先级高 */		
		head.put("parentBgColor",this.parentBgColor);
		head.put("childBgColor",this.childBgColor);
		sb.append(tagUtil.loadTemplate("publish/leftNavigator/otherElement.vm",head));
		Context top = new VelocityContext();
		top.put("width",this.width);
		/* parent_channel=="" 为所以导航层次为0的所有频道 */
		String channelName = "";
		if(this.channel.length()<=0) {
			top.put("displayName",this.displayName);
			if(context!=null){	
				String tmp_site_id = context.getSiteID();
				Channel chl = impl.getChannelInfo(tmp_site_id);
				channelName = chl.getDisplayName();
			}
			
		}else{
			channelName = this.channel;
		    top.put("displayName",this.channel);
		}
		sb.append(tagUtil.loadTemplate("publish/leftNavigator/content-top.vm",top));
		/* 最顶层 和 一般指定层*/
		if(this.siteid.length()<=0){
			if(context!=null){				
				this.siteid = context.getSiteID();
			}
		}
		String site_id = context.getSiteID();
		if(this.siteid.length()>0){
			site_id = this.siteid;
		}
		List list = impl.getNavigatChannel(site_id,channelName);				
		/* 左边菜单 */
		for(int i=1;i<=list.size();i++){	
			Channel channel = (Channel)list.get(i-1);
			sb.append("<DIV  id=KB"+i+"Parent>");
			sb.append("<TABLE class='parenttable' cellSpacing=0 cellPadding=0 width='100%' > ");			
		    sb.append(createTDHTMl(channel,i, true,0));
		    sb.append("</TABLE></DIV>");
		    
		    
		    sb.append(createSubHTML(channel, i,currentlevel + 1));
		    
		}		
		Context down = new VelocityContext();
		down.put("count",(list.size())+"");
		sb.append(tagUtil.loadTemplate("publish/leftNavigator/content-down.vm",down));
		return sb;
	}
	
	/**
	 * 递归生成HTML 先考虑两级
	 * @param Channel channel
	 * @param boolean isN true导航,false不导航
	 * @return
	 * @throws Exception
	 */
	public String createSubHTML(Channel channel,int i,int currentlevel)throws Exception{
		StringBuffer output = new StringBuffer();		
		List sublist = null;
		sublist = impl.getNavigatChannel(context.getSiteID(),channel.getDisplayName());
		output.append("<DIV  id=KB"+i+"Child>");
		output.append("<TABLE class='leftsubtable' cellSpacing=0 cellPadding=0 width='100%' > ");	
		for(int j=0;j<sublist.size();j++){
			Channel subchannel = (Channel)sublist.get(j);			
			output.append(createTDHTMl(subchannel,i,false,j));
		}
		output.append("</TABLE></DIV>");
		String tmp = output.toString();
		return tmp; 
	}
	
	/**
	 * 生成根据图片类型/文字类型生成导航HTML
	 * @param channel 
	 * @param i 顶级菜单序号
	 * @param flag 顶级/二级菜单
	 * @param child_i 二级菜单序号
	 * @return String
	 * @throws Exception
	 */
	public String createTDHTMl(Channel channel, int i,boolean flag,int child_i)throws Exception{
		String DIV = "KB"+i;
		String function = "expandIt('"+DIV+"')";
		StringBuffer output = new StringBuffer();
		String hspace = "";
	    String className = "";
	    String br = "";
	    String imageName = "";
	    if(child_i%this.column_num==0 && this.column_num>1){
	    	br = "true";
	    }
	    if(this.column_num-(child_i%this.column_num)==1 && this.column_num>1){
	    	br = "false";
	    }
		if(flag){
			DIV += "Parent";	
			imageName = "leftNavigator_dot_red.gif";
		}else{
			DIV += "Child'";
			function = "";
			hspace = "12";
			className = "treebg01";
			imageName = "leftNavigator_dot.gif";
		}		
		Context content = new VelocityContext();
		content.put("divId",DIV);
		content.put("className",className);
		content.put("br",br);
		content.put("hspace",hspace);
		content.put("function",function);
		content.put("linkPath",CMSTagUtil.getPublishedChannelPath(context,channel) );
		/*displayName --> name*/
		content.put("displayName",channel.getName());	
		content.put("imageName",imageName);
		content.put("onMouseOver",onMouseOver);
		/*对于顶级菜单的 下级子菜单提示*/
		content.put("nextMenu","");
		if(flag){
			if(impl.hasChildChannel(channel)){
				content.put("nextMenu",this.nextMenuPrompt);
			}
		}
		if(this.column_num<=1){
			output.append(tagUtil.loadTemplate("publish/leftNavigator/loopOutline.vm",content));
		}else{
			output.append(tagUtil.loadTemplate("publish/leftNavigator/loopInline.vm",content));
		}

		String html = output.toString();
		return html;
	}
	

	
	public int doEndTag() throws JspException{		
		try {
			pageContext.getOut().println(navigatorTagShow().toString());			
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

	public int getColumn_num() {
		return column_num;
	}

	public void setColumn_num(int column_num) {
		this.column_num = column_num;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getParentBgColor() {
		return parentBgColor;
	}

	public void setParentBgColor(String parentBgColor) {
		this.parentBgColor = parentBgColor;
	}

	public String getChildBgColor() {
		return childBgColor;
	}

	public void setChildBgColor(String childBgColor) {
		this.childBgColor = childBgColor;
	}

	public String getOnMouseOver() {
		return onMouseOver;
	}

	public void setOnMouseOver(String onMouseOver) {
		this.onMouseOver = onMouseOver;
	}

	public String getNextMenuPrompt() {
		return nextMenuPrompt;
	}

	public void setNextMenuPrompt(String nextMenuPrompt) {
		this.nextMenuPrompt = nextMenuPrompt;
	}



	
	
}
