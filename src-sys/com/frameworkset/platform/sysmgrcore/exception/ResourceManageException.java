/*
 *
 * Title:
 *
 * Copyright: Copyright (c) 2004
 *
 * Company: iSany Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-6-10
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 */

package com.frameworkset.platform.sysmgrcore.exception;

import java.io.Serializable;


/**
 * 资源管理时抛出的异常的祖先类
 * @author 张建会
 */
public class ResourceManageException 
				extends SecurityException
				implements Serializable
{

   /**
    * 构造函数
    */
   public ResourceManageException()
   {

   }

   /**
    * 构造函数
    */
   public ResourceManageException(String msg)
   {
    super(msg);
   }
}
