package com.frameworkset.platform.cache;

import java.io.Serializable;

/**
 * <p>Title: CacheContainer</p>
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
public interface CacheContainer extends Serializable  
{

   /**
    * 缓冲操作方法，实现对象的缓冲功能
    * @param key
    * @param value
    * @roseuid 43F587F00399
    */
   public void cache(Object key, Object value);

   /**
    * 获取缓冲容器中缓冲对象
    * @param key
    * @return Object
    * @roseuid 43F587F1003E
    */
   public Object get(Object key);

   /**
    * 删除缓冲器中的对象实例
    * @param key
    * @roseuid 43F58B35000F
    */
   public void remove(Object key);

   /**
    * 更新缓冲器中已有的对象信息
    * @param key
    * @param value
    * @roseuid 43F58BBB0196
    */
   public void update(Object key, Object value);

   /**
    * 销毁缓冲容器的操作
    * @roseuid 43F58F5F01A5
    */
   public void destoryCacheContainer();
}
