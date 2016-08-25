package com.frameworkset.platform.cache;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * <p>Title: OSCacheContainer</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class OSCacheContainer implements CacheContainer {
	protected static Logger log = Logger.getLogger(OSCacheContainer.class);
	private GeneralCacheAdministrator cache = null; 
	private static String fileName="oscache.properties";

    /* (non-Javadoc)
     * @see com.frameworkset.platform.cache.CacheContainer#cache(java.lang.Object, java.lang.Object)
     */
	public OSCacheContainer(){
		try{
		Properties prop = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		prop.load(is);
		is.close();
		cache = new GeneralCacheAdministrator(prop); 	
		}catch(Exception e){
			log.error("通过OSCacheContainer装载配置文件发生错误。", e);
			e.printStackTrace();
		}
	}
    public void cache(Object key, Object value) {
    	cache.putInCache(String.valueOf(key), value); 


    }

    /* (non-Javadoc)
     * @see com.frameworkset.platform.cache.CacheContainer#get(java.lang.Object)
     */
    public Object get(Object key) {
    	try { 
    		return cache.getFromCache(String.valueOf(key),3600); 
    		} catch (Exception e) { 
    		cache.cancelUpdate(String.valueOf(key)); 
    		return null; 
    		} 

    }

    /* (non-Javadoc)
     * @see com.frameworkset.platform.cache.CacheContainer#remove(java.lang.Object)
     */
    public void remove(Object key) {
    	cache.flushEntry(String.valueOf(key));

    }

    /* (non-Javadoc)
     * @see com.frameworkset.platform.cache.CacheContainer#update(java.lang.Object, java.lang.Object)
     */
    public void update(Object key, Object value) {
    	cache.putInCache(String.valueOf(key), value); 


    }

    /* (non-Javadoc)
     * @see com.frameworkset.platform.cache.CacheContainer#destoryCacheContainer()
     */
    public void destoryCacheContainer() {
    	cache.destroy();
    }

}
