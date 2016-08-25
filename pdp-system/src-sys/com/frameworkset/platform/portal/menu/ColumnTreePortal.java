package com.frameworkset.platform.portal.menu;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.framework.SubSystem;

/**
 * <p>
 * 类说明:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author gao.tang
 * @version V1.0 创建时间：Oct 15, 2009 2:55:01 PM
 */
public class ColumnTreePortal extends COMTree implements Serializable {
	private static final Logger log = Logger.getLogger(ColumnTreePortal.class);
	private Map menuHelpers = null;

	@Override
	public void setPageContext(PageContext pageContext) {
		// TODO Auto-generated method stub
		super.setPageContext(pageContext);
		if (menuHelpers == null) {
			menuHelpers = new HashMap();
			MenuHelper menuHelper = new MenuHelper(accessControl);
			menuHelpers.put("module", menuHelper);
			Map subsystems = Framework.getInstance().getSubsystems();
			if (subsystems != null && !subsystems.isEmpty()) {

				Set set = subsystems.keySet();
				for (Iterator it = set.iterator(); it.hasNext();) {
					String subsystem = (String) it.next();
					menuHelper = new MenuHelper(subsystem, accessControl);
					menuHelpers.put(subsystem, menuHelper);

				}
			}
		} else {
			Collection set = menuHelpers.values();
			for (Iterator it = set.iterator(); it.hasNext();) {
				MenuHelper menuHelper = (MenuHelper) it.next();
				menuHelper.resetControl(accessControl);
			}

		}
	}

	public boolean hasSon(ITreeNode father) {
		String path = father.getPath();
		MenuHelper menuHelper = null;

		if (father.isRoot()) {
			return true;
		}
		if (father.getType().equals("subsystem")) {
			ModuleQueue queue = null;
			ItemQueue iqueue = null;
			menuHelper = (MenuHelper) this.menuHelpers.get(father.getId());
			queue = menuHelper.getModules();
			iqueue = menuHelper.getItems();
			if (iqueue.size() > 0)
				return true;
			if (queue.size() > 0)
				return true;
		} else {
			int idx = path.indexOf("::");
			String subsystem = path.substring(0, idx);
			menuHelper = (MenuHelper) this.menuHelpers.get(subsystem);
			MenuItem menuItem = menuHelper.getMenu(path);
			if (menuItem instanceof Item) {
				return false;
			} else if (menuItem instanceof Module) {
				Module module = (Module) menuItem;
				ItemQueue iqueue = null;
				ModuleQueue mqueue = null;
				if (ConfigManager.getInstance().securityEnabled()) {
					iqueue = menuHelper.getSubItems(module.getPath());
					mqueue = menuHelper.getSubModules(module.getPath());
				} else {
					iqueue = module.getItems();
					mqueue = module.getSubModules();
				}

				if (iqueue.size() > 0)
					return true;
				if (mqueue.size() > 0)
					return true;
			}
		}

		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String parentPath = father.getPath();
		String id = father.getId();
		if (father.isRoot()) {
			addNode(father, "module", Framework.getInstance().getDescription(request),
					"subsystem", false, curLevel, "", (String) null,
					(String) null, (String) null);
			Map subsystems = Framework.getInstance().getSubsystems();
			if (subsystems != null && !subsystems.isEmpty()) {
				Set set = subsystems.keySet();
				for (Iterator it = set.iterator(); it.hasNext();) {
					String subsystem = (String) it.next();
					SubSystem sys = (SubSystem) subsystems.get(subsystem);
					addNode(father, subsystem, sys.getName(request), "subsystem",
							false, curLevel, "", (String) null, (String) null,
							(String) null);

				}
			}
			return true;
		}
		MenuHelper menuHelper = null;
		ModuleQueue submodules = null;
		ItemQueue items = null;
		if (father.getType().equals("subsystem")) {
			menuHelper = (MenuHelper) this.menuHelpers.get(father.getId());
			submodules = menuHelper.getModules();
			items = menuHelper.getItems();
		} else {
			int idx = parentPath.indexOf("::");
			String subsystem = parentPath.substring(0, idx);
			menuHelper = (MenuHelper) menuHelpers.get(subsystem);

			if (ConfigManager.getInstance().securityEnabled()) {
				items = menuHelper.getSubItems(parentPath);
				submodules = menuHelper.getSubModules(parentPath);
			}
		}
		String treeid = "";
		String treeName = "";
		String moduleType = "module";
		String itemType = "item";

		boolean showHref = true;
		String memo = null;
		String radioValue = null;
		String checkboxValue = null;
		String path = "";
		String areaType = "";
		for (int i = 0; i < submodules.size(); i++) {

			Map params = new HashMap();
			Module submodule = submodules.getModule(i);
			treeid = submodule.getPath();
			treeName = submodule.getName(request);
			path = submodule.getPath();
			params.put("resTypeId", "column");
			params.put("columnID", treeid);
			params.put("resId", submodule.getId());
			params.put("resName", submodule.getName(request));
			// radioValue = path;
			// checkboxValue = path;
			showHref = true;
			if (submodule.isUsed()) {
				addNode(father, treeid, treeName, moduleType, showHref,
						curLevel, memo, radioValue, checkboxValue, path, params);
			}
		}
		for (int i = 0; i < items.size(); i++) {
			Map params = new HashMap();
			Item subitem = (Item) items.getItem(i);
			subitem.getStatusContent();
			treeid = subitem.getPath();
			treeName = subitem.getName(request);
			path = subitem.getPath();
			areaType = checkArea(subitem);
			showHref = true;
			params.put("resTypeId", "column");
			params.put("columnID", treeid);
			params.put("resId", subitem.getId());
			params.put("resName", subitem.getName(request));
			radioValue = path;
			checkboxValue = path + "^-^" + subitem.getName(request)+"^-^"+areaType;
			if (subitem.isUsed()) {
				addNode(father, treeid, treeName, itemType, showHref, curLevel,
						memo, radioValue, checkboxValue, path, params);
			}
		}
		return true;
	}

	/**
	 * actionContainer,navigatorContainer,status,workspace
	 * @param subitem
	 * @return
	 */
	private String checkArea(Item subitem) {
		boolean workspaceState = (subitem.getWorkspaceContent() == null || ""
				.equals(subitem.getWorkspaceContent().trim())) ? false : true;
		boolean navigatorState = (subitem.getNavigatorContent() == null || ""
				.equals(subitem.getNavigatorContent().trim())) ? false : true;
		boolean statusState = (subitem.getStatusContent() == null || ""
				.equals(subitem.getStatusContent().trim())) ? false : true;
		String[] areaType = new String[4];
		if(workspaceState || statusState)
			areaType[0] = "actionContainer";
		if(navigatorState)
			areaType[1] = "navigatorContainer";
		if(statusState)
			areaType[2] = "status";
		if(workspaceState){
			areaType[3] = "workspace";
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < 4; i++){
			if(areaType[i] != null && !"".equals(areaType)){
				if(sb.length() > 0){
					sb.append(":").append(areaType[i]);
				}else{
					sb.append(areaType[i]);
				}
			}
		}
		return sb.toString();
	}

}
