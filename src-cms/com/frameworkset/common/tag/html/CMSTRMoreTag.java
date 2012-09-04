package com.frameworkset.common.tag.html;

import org.apache.ecs.html.TR;

/**
 * 通过直接生成tr的more脚本行
  * <p>Title: CMSTRMoreTag</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-12 16:52:00
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSTRMoreTag extends CMSTDMoreTag {
	protected String getTR(String trText)
	{
		TR tr = new TR();
		tr.setTagText(trText);
		return tr.toString();
	}
	
	public String generatorMoreScript()
	{
		String trText = super.generatorMoreScript();
		return this.getTR(trText);
	}

}
