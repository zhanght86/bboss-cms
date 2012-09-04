package com.frameworkset.platform.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * <p>Title: MapContainer.java</p>
 *
 * <p>Description: map缓冲器，事先java.util.Map的基本功能，并提供缓冲的集群功能</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: 三一集团</p>
 * @Date May 6, 2008 10:50:04 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class MapContainer implements java.util.Map{
	/**
	 * 判断系统是否启用集群机制
	 */
//	private static final boolean enablecluster = ConfigManager.getInstance().getConfigBooleanValue("enablecluster",false);
	private Map map = new HashMap();	
	public void clear() {
		
		map.clear();		
	}

	public boolean containsKey(Object key) {
		
		return this.map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		
		return this.map.containsValue(value);
	}

	public Set entrySet() {
		
		return this.map.entrySet();
	}

	public Object get(Object key) {
		
		return this.map.get(key);
	}

	public boolean isEmpty() {
		
		return this.map.isEmpty();
	}

	public Set keySet() {
		
		return this.map.keySet();
	}

	public Object put(Object key, Object value) {
		
		return this.map.put(key, value);
	}

	public void putAll(Map t) {
		this.map.putAll(t);
		
	}

	public Object remove(Object key) {
		
		return this.map.remove(key);
	}

	public int size() {
		
		return this.map.size();
	}

	public Collection values() {
		
		return this.map.values();
	}
	

}
