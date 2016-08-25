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
 *  @author 张建会
 *  用户登录名不唯一异常。
 */
public class NotUniqueUserException 
	extends SecurityException implements Serializable
{

   /**
	* 构造函数
    */
   public NotUniqueUserException()
   {

   }
}
