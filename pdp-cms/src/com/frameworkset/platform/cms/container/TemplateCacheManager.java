package com.frameworkset.platform.cms.container;

import java.util.HashMap;
import java.util.Map;

import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;

import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;

/**
 * 模版缓冲器
 * @author biaoping.yin
 *
 */
public class TemplateCacheManager implements Listener{
	private static TemplateCacheManager instance;
	
	private Map templates = new HashMap();
	private TemplateCacheManager()
	{
		
	}
	public TemplateCacheManager getInstance()
	{
		if(instance == null)
		{
			this.instance = new TemplateCacheManager();
		}
		return this.instance;
	}
	public void handle(Event e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 获取模版得缓冲Link表
	 * @return
	 */
	public CmsLinkTable getCacheTemplateLink(String templateID)
	{
		return null;
	}
	
	

}
