package com.frameworkset.platform.sysmgrcore.purviewmanager;

import java.util.Map;

/**
 * 自动生成服务类：用户帐号
 * 
 * <p>Title: GenerateService.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date  Apr 26, 2008 11:00:44 AM
 * @author gao.tang
 * @version 1.0
 */
public interface GenerateService {
	
	/**
	 * 判断是否自动生成用户帐号
	 * @return
	 */
	public boolean enableUserNameGenerate();
	/**
	 * 根据preferences中的信息来生成用户帐号
	 * @param preferences
	 * @return
	 */
	public String generateUserName(Map preferences) throws Exception ;
	

}
