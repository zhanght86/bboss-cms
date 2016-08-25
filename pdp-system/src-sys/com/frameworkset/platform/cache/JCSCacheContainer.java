//Source file: D:\\workspace\\console\\src\\com\\frameworkset\\platform\\cache\\JCSCacheContainer.java

package com.frameworkset.platform.cache;

import org.apache.jcs.JCS;

/**
 * JCS缓冲容器类，保持缓冲器org.apache.jcs.JCS实例的引用，所有的缓冲功能由该实例实
 * 现
 */
public class JCSCacheContainer implements CacheContainer
{

   /**
    * jcs缓冲器实例，CacheContainer对象实例中所有的缓冲功能由该容器实现
    */
   private JCS jcsCacheContainer;

   /**
    * @roseuid 43FD33720242
    */
   public JCSCacheContainer()
   {

   }

   /**
    * @param key
    * @param value
    * @roseuid 43FD33720261
    */
   public void cache(Object key, Object value)
   {

   }

   /**
    * @param key
    * @return Object
    * @roseuid 43FD337202CE
    */
   public Object get(Object key)
   {
    return null;
   }

   /**
    * @param key
    * @roseuid 43FD337202FD
    */
   public void remove(Object key)
   {

   }

   /**
    * @param key
    * @param value
    * @roseuid 43FD3372034B
    */
   public void update(Object key, Object value)
   {

   }

   /**
    * @roseuid 43FD337203B9
    */
   public void destoryCacheContainer()
   {

   }
}
