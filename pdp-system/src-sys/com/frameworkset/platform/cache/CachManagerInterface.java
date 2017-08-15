package com.frameworkset.platform.cache;

/**
 * 缓冲器初始化程序
 * <p>Title: CachManagerInterface</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-5-18 10:28:18
 * @author biaoping.yin
 * @version 1.0
 */
public interface CachManagerInterface extends java.io.Serializable{
	public void init();
	public void destroy();
}
