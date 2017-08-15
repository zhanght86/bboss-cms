/*
 * @(#)MenuManageAction.java
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
package com.frameworkset.platform.menu.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.SubSystem;
import com.frameworkset.platform.resource.ResourceManager;

/**
 * @author gw_hel
 *
 */
public class MenuManageAction {

	/**
	 * 查看菜单详细信息
	 * @param request HttpServletRequest
	 * @param resId resId 
	 * @param resName resName
	 * @param columnID columnID
	 * @param menuPath menuPath
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String viewMenuInfo(HttpServletRequest request) {

		String resId = request.getParameter("resId");
		String menuPath = request.getParameter("nodePath");

		String resTypeId2 = "column"; // 资源类型id
		String resId2 = resId; // 资源id

		SubSystem subSystem = null;
		Framework framework = null;
		MenuItem menu = Framework.getMenu(menuPath);
		if (menu == null) {
			subSystem = Framework.getInstance().getSubsystem(resId2);
		}
		if (subSystem == null) {
			framework = Framework.getInstance(resId);
		}

		if (menu instanceof Item) {
			request.setAttribute("menu", (Item) menu);
			request.setAttribute("menuType", "item");
		} else if (menu instanceof Module) {
			request.setAttribute("menu", (Module) menu);
			request.setAttribute("menuType", "module");
		}
		else if (subSystem != null) {
			request.setAttribute("menu", subSystem);
			request.setAttribute("publicItem", subSystem.getFramework().getPublicItem());
			request.setAttribute("menuType", "subSystem");
		}
		else if (framework != null) {
			request.setAttribute("menu", framework);
			request.setAttribute("publicItem", framework.getPublicItem());
			request.setAttribute("subSystems", framework.getSubsystems());
			request.setAttribute("menuType", "framework");
		}

		if (resId2 == null) {
			resId2 = (String) request.getAttribute("resId2");
		}

		ResourceManager resManager = new ResourceManager();

		List operList = resManager.getOperations(resTypeId2);
		if (operList == null) {
			operList = new ArrayList();
		}
		request.setAttribute("operList", operList);
		String stored = (String) request.getAttribute("stored");
		if (stored == null) {
			stored = "0";
		}

		request.setAttribute("resTypeId2", resTypeId2);
		request.setAttribute("resId2", resId2);
		request.setAttribute("menuPath", menuPath);

		return "path:viewMenuInfo";
	}
}
