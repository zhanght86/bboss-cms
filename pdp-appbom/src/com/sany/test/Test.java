/*
 * @(#)Test.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.test;

import org.frameworkset.spi.DefaultApplicationContext;

/**
 * @author yinbp
 * @since 2012-3-21 上午9:53:19
 */
public class Test {
	public static void main(String[] args)
	{
		DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("WebRoot/WEB-INF/conf/appbom/bboss-appbom.xml");
		com.sany.appbom.service.AppBomServiceImpl appBomService = context.getTBeanObject("appBomService",  com.sany.appbom.service.AppBomServiceImpl.class);
		
		
		
	}

}
