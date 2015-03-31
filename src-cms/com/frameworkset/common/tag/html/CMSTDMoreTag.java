package com.frameworkset.common.tag.html;

import org.apache.ecs.html.TD;

/**
 * 生成td的more脚本标签
  * <p>Title: CMSTDMoreTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-12 16:52:35
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSTDMoreTag extends CMSMoreTag {
	protected int colspan = -1;
	
	protected int rowspan = -1;
	
	public String generatorMoreScript()
	{
		String tdText = super.generatorMoreScript();
		return this.getTD(tdText);
	}
	
	protected String getTD(String tdText)
	{
		TD td = new TD();
		if(this.colspan != -1)
			td.setColSpan(this.colspan);
		td.setTagText(tdText);
		if(this.rowspan != -1)
			td.setRowSpan(rowspan);
		return td.toString();
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	@Override
	public void doFinally() {
		if(this.listTag != null){
			//在listTag里面执行
			//在回调完more.generatorMoreScript()后
			//执行more.clearParameter()
		}else{
			colspan = -1;
			
			rowspan = -1;
		}
		super.doFinally();
	}

}
