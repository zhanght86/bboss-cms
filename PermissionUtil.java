package com.bestride.pms.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.config.model.Operation;
import com.frameworkset.platform.config.model.OperationQueue;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.esb.datareuse.common.entity.MenuItemU;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;

public class PermissionUtil {
	public static Map<String,MenuItemU> geMenus(HttpServletRequest request,AccessControl accesscontroler)
	{
		Map<String,MenuItemU> permissionMenus = new HashMap<String,MenuItemU>();
		MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
		ModuleQueue moduleQueue = menuHelper.getModules();
		for (int i = 0; moduleQueue != null && i < moduleQueue.size(); i++) {
			Module module = moduleQueue.getModule(i);
			if (!module.isUsed()) {
				continue;
			}
			MenuItemU menuItemU = new MenuItemU();
			menuItemU.setId(module.getId());
			menuItemU.setName(module.getName(request));
			menuItemU.setImageUrl(module.getMouseclickimg(request));
			menuItemU.setType("module");
			permissionMenus.put(module.getId(), menuItemU);
			geSubMenus(  permissionMenus,  module,  request,  accesscontroler);
		}
		return permissionMenus;
	}
	
	public static void geSubMenus(Map<String,MenuItemU> permissionMenus,Module module,HttpServletRequest request,AccessControl accesscontroler)
	{
		
		ModuleQueue moduleQueue = module.getSubModules();
		for (int i = 0; moduleQueue != null && i < moduleQueue.size(); i++) {
			Module submodule = moduleQueue.getModule(i);
			if (!submodule.isUsed()) {
				continue;
			}
			MenuItemU menuItemU = new MenuItemU();
			menuItemU.setId(submodule.getId());
			menuItemU.setName(submodule.getName(request));
			menuItemU.setImageUrl(submodule.getMouseclickimg(request));
			menuItemU.setType("module");
			permissionMenus.put(submodule.getId(), menuItemU);
		}
		ItemQueue itemQueue = module.getItems();
		String contextpath = request.getContextPath();
		for (int i = 0; itemQueue != null && i < itemQueue.size(); i++) {
			Item item = itemQueue.getItem(i);
			if (!item.isUsed()) {
				continue;
			}
			String contextPath = request.getContextPath();
			String url = null;
			String area = item.getArea();
			if(area != null && area.equals("main"))
			{
				url = MenuHelper.getMainUrl(contextpath, item,
						(java.util.Map) null);
			}
			else
			{
				url = MenuHelper.getRealUrl(contextpath, Framework.getWorkspaceContent(item,accesscontroler),MenuHelper.sanymenupath_menuid,item.getId());
			}
			MenuItemU menuItemU = new MenuItemU();
			menuItemU.setId(item.getId());
			menuItemU.setName(item.getName(request));
			menuItemU.setImageUrl(item.getMouseclickimg(request));
			menuItemU.setPathU(url);
			menuItemU.setType("item");
			menuItemU.setDesktop_height(item.getDesktop_height());
			menuItemU.setDesktop_width(item.getDesktop_width());
			permissionMenus.put(item.getId(), menuItemU);
			// System.out.println("=========="+url+"===="+item.getName());
		}
	}
	
	public static Map<String,List<String>> getResourcePermissions(AccessControl accesscontroler,String resourceType) throws Exception
	{
		Map<String,List<String>> cmPermissions = new HashMap<String,List<String>>();
		List<String> cmresources = SQLExecutor.queryList(String.class, "select title from td_sm_res where restype_id=?", resourceType);
		if(cmresources == null)
		{
			cmresources = new ArrayList<String>();
		}
		
		ResourceManager resourceManager = new ResourceManager();
		ResourceInfo resourceInfo = resourceManager.getResourceInfoByType(resourceType);
		if(resourceInfo == null)
			return cmPermissions;
		String globalid = resourceInfo.getGlobalresourceid();
		if(StringUtil.isNotEmpty(globalid))
		{
			cmresources.add(globalid);
			
		}
		
		OperationQueue operationQueue = resourceInfo.getOperationQueue();
		if(operationQueue == null)
			return cmPermissions;
		for(int i = 0; i < cmresources.size(); i ++)
		{
			String resid = cmresources.get(i);
			List<String> ops = new ArrayList<String>();
			for(int j = 0; j < operationQueue.size(); j ++)
			{
				Operation op = operationQueue.getOperation(j);
				if(accesscontroler.checkPermission(resid, op.getId(), "cm"))
				{
					ops.add(op.getId());
				}
			}
			if(ops.size()> 0)
				cmPermissions.put(resid, ops);
		}
		
		
		return cmPermissions;
	}

}
