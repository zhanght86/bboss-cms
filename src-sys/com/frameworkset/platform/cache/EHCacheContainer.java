package com.frameworkset.platform.cache;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: EHCacheContainer
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
public class EHCacheContainer implements CacheContainer,java.io.Serializable {
	protected static Logger log = Logger.getLogger(EHCacheContainer.class);
	private static CacheManager manager=getInstance();
	private static Cache cache = manager.getCache("com.sany");
	public static CacheManager getInstance() {	
		try {		
			net.sf.ehcache.CacheManager  manager = CacheManager.create("ehcache.xml");
			return manager;
		} catch (CacheException e) {
			log.error("请检查EHCacheContainer中的配置文件ehcache.xml文件是非正确。", e);
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cache.CacheContainer#cache(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void cache(Object key, Object value) {
		Element element = new Element((String)key, (Serializable)value);
		cache.put(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cache.CacheContainer#get(java.lang.Object)
	 */
	public Object get(Object key) {
		try {
			Element element = cache.get(String.valueOf(key));
			return element.getValue();
		} catch (IllegalStateException e) {
			log.error("通过EHCacheContainer取出对象发生错误。", e);
			e.printStackTrace();
		} catch (CacheException e) {
			log.error("通过EHCacheContainer取出对象发生错误。", e);
			e.printStackTrace();
		}	
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cache.CacheContainer#remove(java.lang.Object)
	 */
	public void remove(Object key) {
		try {
			Element element = cache.get(String.valueOf(key));
			Serializable  value = element.getValue();
			cache.remove(value);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cache.CacheContainer#update(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void update(Object key, Object value) {
		Element element = new Element((String)key, (Serializable)value);
		cache.put(element);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cache.CacheContainer#destoryCacheContainer()
	 */
	public void destoryCacheContainer() {
		manager.removeCache("com.sany");

	}

}
