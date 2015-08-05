package com.frameworkset.platform.cms.driver.publish.impl;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.prefs.Preferences;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.jsp.Cache;
import com.frameworkset.platform.cms.driver.jsp.FileTimestamp;
import com.frameworkset.platform.cms.driver.jsp.JspFile;
import com.frameworkset.platform.cms.driver.publish.Scriptlet;
import com.frameworkset.util.StringUtil;

/**
 * 
 * <p>
 * Title: ScriptletUtil
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
 * @author biaoping.yin
 * @version 1.0
 */
public class ScriptletUtil  {

	/**
	 * 创建文件类型模版对应的脚本
	 * 
	 * @param context
	 * @param template
	 * @return
	 */
	public static Scriptlet createScriptlet(Context context, Template template) {
		Scriptlet scriptlet = new Scriptlet();
		scriptlet.setContext(context);
		scriptlet.setTemplate(template);

		return scriptlet;

	}

	/**
	 * 创建页面对应的脚本
	 * 
	 * @param context
	 * @return
	 */
	public static Scriptlet createScriptlet(PageContext context) {
		Scriptlet scriptlet = new Scriptlet();
		scriptlet.setContext(context);
		return scriptlet;
	}

	protected static Map metas = new ConcurrentHashMap();
	public static void resetCache()
	{
		metas.clear();
	}
	public static void destroy()
	{
		if(metas != null)
		{
			metas.clear();
			metas = null;
		}
	}
	/**
	 * 根据路径获取缓冲中存在的模板文件信息，暂时不考虑并发同步控制问题
	 * 
	 * @param path
	 *            完整的文件路径，在linux环境下应该是/local/file.ext, windows
	 *            下应该是c:/local/file.ext 保持统一的路径
	 * 
	 * @return
	 */
	public static Cache getCache(JspFile file,
			FileTimestamp templateFileTimestamp) {
		String temp = prepath(file.getAbsolutePath());
//		synchronized()
		Cache cache = (Cache) metas.get(temp);
		if(cache != null)
		{
			file.setCache(cache);
			file.beenModified(templateFileTimestamp);
//			jspFile.setModified(modified);
		}
		else
		{
			file.setCache(cache);
		}
		return cache;
	}
	
	
	/**
	 * 根据路径获取缓冲中存在的模板文件信息，暂时不考虑并发同步控制问题
	 * 
	 * @param path
	 *            完整的文件路径，在linux环境下应该是/local/file.ext, windows
	 *            下应该是c:/local/file.ext 保持统一的路径
	 * 
	 * @return
	 */
	public static Cache getCache(JspFile file,
			long templateTimestamp) {
		String temp = prepath(file.getAbsolutePath());
//		synchronized()
		Cache cache = (Cache) metas.get(temp);
		if(cache != null)
		{
			
			file.setCache(cache);
			boolean modified =  file.beenModified(templateTimestamp);
			if(modified)
			{
				metas.remove(temp);
				cache = null;
			}
//			jspFile.setModified(modified);
		}
		else
		{
			file.setCache(cache);
		}
		return cache;
	}
	
	public static void addCache(JspFile file)
	{
		String temp = prepath(file.getAbsolutePath());
		metas.put(temp, file.getCache());
	}
	
	private static String prepath(String path)
	{
		if(path == null || path.trim().equals(""))
			return "";
		else
		{
			path = StringUtil.replaceAll(path, "//", "/");
			path = StringUtil.replaceAll(path, "\\\\", "/");
			path = StringUtil.replaceAll(path, "//", "/");
			return path;
		}
	}

	public static void main(String[] args) {
		System.out.println(Preferences.systemRoot());
		System.out.println(Preferences.userRoot());

		String keys[] = { "sunway", "copyright", "author" };
		String values[] = { "sunway technology company", "copyright 2002",
				"hh@163.com" };
		// 建立一个位于user root 下的/com/sunway/spc节点参数项
		// Preferences
		// prefsdemo=Preferences.userRoot().node("com/sunway/spc");
		Preferences prefsdemo = Preferences.systemRoot().node("com/sunway/spc");
		System.out.println(prefsdemo);
		try {
			prefsdemo.put("key1", "iamlitertiger");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 存储参数项目
		for (int i = 0; i < keys.length; i++) {
			prefsdemo.put(keys[i], values[i]);
		}
		// 导出到xml文件
		try {
			FileOutputStream fos = new FileOutputStream("pref.xml");
			prefsdemo.exportNode(fos);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
