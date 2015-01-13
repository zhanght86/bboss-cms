/*
 * Created on 2006-3-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.util.List;

import com.frameworkset.platform.sysmgrcore.manager.OperManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;



/**
 * @author ok
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ResourceAction   {
	 
	
	/**
	 * 获得资源对应的所有角色及操作列表
	 * @param resId
	 * @param restypeId
	 * @return
	 */
	public static List getRoleOperList(String resId, String restypeId) {
		List list = null;
		try {
			OperManager om = SecurityDatabase.getOperManager();
			list = om.getRoleOperList(resId,restypeId);
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}
	 
	
}
