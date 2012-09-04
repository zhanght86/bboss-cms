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
 *  角色管理时抛出的异常的祖先类
 *  @author 张建会。
 */
public class RoleManageException 
				extends SecurityException
				implements Serializable
{

	/**
	 * 构造函数
	 */
	public RoleManageException()
	{

	}

	/**
	* 传来message的构造函数
	*/
	public RoleManageException(String msg)
	{
		super(msg);
	}
}
