package com.frameworkset.platform.cache;


/**
 * 
 * 缓冲机制管理
 *
 * <p>Title: CacheManager</p>
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
public class CacheManager implements java.io.Serializable  
{
   
   /**
    * @roseuid 43FD3345032C
    */
   public CacheManager() 
   {
    
   }
   
   /**
    * 创建缓冲器，缓冲器的名称为containerName，缓冲机制根据参数cacheType指定
    * 返回类型为CachContainer的缓冲器接口。
    * 目前系统中提供JCSCachContainer实现，采用JCS实现对象的缓冲功能
    * @param containerName - 缓冲器的名称
    * @param cacheType - 标识缓冲机制类型，主要有以下几类
    * 1:JCS,已经实现
    * 2:OSCache，未实现
    * 3:JBOSSCache，未实现
    * 
    * @return com.frameworkset.platform.cache.CacheContainer
    * @roseuid 43F574A80157
    */
   public CacheContainer createCacheContainer(String containerName, int cacheType) 
   {
    return null;
   }
   
   /**
    * 获取缓冲容器
    * @param containerName
    * @return com.frameworkset.platform.cache.CacheContainer
    * @roseuid 43F58B6F03B9
    */
   public CacheContainer getCacheContainer(String containerName) 
   {
    return null;
   }
   
   /**
    * @param containerName
    * @roseuid 43F583C503D8
    */
   public void destroyCacheContainer(String containerName) 
   {
    
   }
   
   /**
    * @param cacheContainer
    * @roseuid 43F58948007D
    */
   public void destruyCacheContainer(CacheContainer cacheContainer) 
   {
    
   }
}
/**
 * void CacheManager.main(java.lang.String[]){
 *         CacheManager cachemanager = new CacheManager();
 *         CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
 *         Properties props = new Properties();
 *         try {
 *             props.load(null);
 *         } catch (IOException ex) {
 *         }
 *         ccm.configure(props);
 *         try {
 *             JCS cach = JCS.getInstance(null);
 *         } catch (CacheException ex1) {
 *             ex1.printStackTrace();
 *         }
 *     }
 * void CacheManager.test(){
 *     }
 */
