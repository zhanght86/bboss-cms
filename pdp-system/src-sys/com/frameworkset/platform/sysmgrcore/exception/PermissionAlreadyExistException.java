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
 * 许可已经存在异常。
 * @author 张建会
 */
public class PermissionAlreadyExistException 
				extends PermissionManageException
				implements Serializable
{

   /**
    * 构造函数
    */
   public PermissionAlreadyExistException()
   {

   }
}
