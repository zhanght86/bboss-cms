package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.ecs.IMG;
import com.frameworkset.common.tag.BaseCellTag;
import com.frameworkset.common.tag.CMSTagUtil;

/**
 * 图片标签
 * <p>Title: ImageTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-14 23:02:32
 * @author biaoping.yin
 * @version 1.0
 */
public class ImageTag extends BaseCellTag {
	private int border = 0;

	private String width = null;

	private String height = null;
	
	private String alt = null;
	
	private String align = null;
	
	private String onClick = null;
	
	private String onDblClick = null;
	
	private String title = null;
	
	
	public int doStartTag() throws JspException {

		super.doStartTag();
		String outStr = super.getOutStr();
		if (outStr != null && !outStr.equals("")) {
			IMG image = new IMG();
			
			image.setSrc(CMSTagUtil.getPublishedLinkPath(context, outStr));
			image.setBorder(this.border);
			if (this.width != null && !this.width.equals("")) {
				image.setWidth(width);
			}

			if (this.height != null && !this.height.equals("")) {
				image.setHeight(height);
			}

			if (this.alt != null && !this.alt.equals("")) {
				image.setAlt(alt);
			}

			if (this.align != null && !this.align.equals("")) {
				image.setAlign(align);
			}

			if (this.onClick != null && !this.onClick.equals("")) {
				image.setOnClick(onClick);
			}

			if (this.onDblClick != null && !this.onDblClick.equals("")) {
				image.setOnDblClick(onDblClick);
			}

			if (this.title != null && !this.title.equals("")) {
				image.setTitle(title);
			}
			
			try {
				out.print(image.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}
	
	

	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
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

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getOnDblClick() {
		return onDblClick;
	}

	public void setOnDblClick(String onDblClick) {
		this.onDblClick = onDblClick;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



	@Override
	public void doFinally() {
		border = 0;

		 width = null;

		 height = null;
		
		 alt = null;
		
		 align = null;
		
		 onClick = null;
		
		 onDblClick = null;
		
		 title = null;
		super.doFinally();
	}

}
