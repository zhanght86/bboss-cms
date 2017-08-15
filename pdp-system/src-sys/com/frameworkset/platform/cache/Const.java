package com.frameworkset.platform.cache;


/**
 * 
 * 定义缓冲机制类型常量
 *
 * <p>Title: Const</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class Const implements java.io.Serializable
{
   
   /**
    * 针对JCS的缓冲机制实现
    */
   public static int JCS_TYPE = 1;
   
   /**
    * OScache机制类型
    */
   public static int OS_TYPE = 2;
   public int JBOSSTREE_TYPE = 3;
   
   /**
    * @roseuid 43FD333B03C8
    */
   public Const() 
   {
    
   }
}
