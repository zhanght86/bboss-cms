package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;

/**
 * 视频播放标签
 * <p>Title: MediaPlayerTag.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-17 18:23:56
 * @author kai.hu
 * @version 1.0
 */
public class MediaPlayerTag extends BaseCellTag {
	private String filename = "";

	private String width = "200";

	private String height = "200";
	
	private String autostart = "true";
	
	private static final String PROMPT = "如果播放异常，<BR>请点击下载最新的视频播放器！";
	private static final String DOWNLOADPATH = "http://film.738.cn/down/StormCodec5.07.exe";

	public StringBuffer mediaPlayerTagShow() throws Exception {
		StringBuffer sb = new StringBuffer();
		String vidio = "";
		
		if (filename != null && !"".equals(filename))
			vidio = CMSTagUtil.getPublishedLinkPath(context, filename);
		else {
			if(this.getColName() != null )
//				this.setColName("mediapath");
				vidio = this.getOutStr();
			else
				vidio = this.dataSet.getString("mediapath");
			if (vidio != null && !vidio.equals(""))
				vidio = CMSTagUtil.getPublishedLinkPath(context, vidio);
	  	}

		if (vidio != null && !vidio.equals("")) {
			sb.append("<embed id=\"videos\" " + "src=\"" + vidio
					+ "\" autostart=\""+autostart+"\" " + "width=\"" + width + "\" "
					+ "height=\"" + height + "\"  " + "ShowTracker=\"true\" "
					+ "ShowPositionControls=\"true\" "
					+ "EnableContextMenu=\"true\" " + "loop=\"true\" "
					+ "controls=\"smallconsole\"/> ").append("<br>")
					.append("<a style='color:red' href=").append(DOWNLOADPATH).append(">").append(PROMPT).append("</a>");
			
		}
		else
			sb.append("没有文件信息！！");
		
//		System.out.println(sb.toString());
		return sb;
	}

	public int doStartTag() throws JspException {
		super.doStartTag();
		try {
			out.println(mediaPlayerTagShow().toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
	}

	public String getAutostart() {
		return autostart;
	}

	public void setAutostart(String autostart) {
		this.autostart = autostart;
	}

}
