package com.frameworkset.platform.esb.datareuse.common.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelAndView;

import com.frameworkset.platform.esb.datareuse.common.entity.DeskTopMenuBean;
import com.frameworkset.platform.esb.datareuse.common.entity.ItemsBean;
import com.frameworkset.platform.esb.datareuse.common.entity.MenuItemU;
import com.frameworkset.platform.esb.datareuse.common.service.DeskTopMenuShorcutManager;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;


public class MenuItemController {
	
	private DeskTopMenuShorcutManager deskTopMenuShorcutManager;
	
	public DeskTopMenuShorcutManager getDeskTopMenuShorcutManager() {
		return deskTopMenuShorcutManager;
	}

	public void setDeskTopMenuShorcutManager(
			DeskTopMenuShorcutManager deskTopMenuShorcutManager) {
		this.deskTopMenuShorcutManager = deskTopMenuShorcutManager;
	}

	public @ResponseBody(datatype="json")
	List<MenuItemU> queryMenu(@RequestParam(name = "moduleId") String moduleId,
			HttpServletRequest request) {
		// String returnJsonString = "";
		try {
			AccessControl control = AccessControl.getAccessControl();
			// control.checkAccess(request, response);

			String modulePath = control.getCurrentSystemID()
					+ "::menu://sysmenu$root/" + moduleId + "$module";
			MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
			ItemQueue itemQueue = menuHelper.getSubItems(modulePath);
			ModuleQueue moduleQueue = menuHelper.getSubModules(modulePath);

			List<MenuItemU> list = new ArrayList<MenuItemU>();
			for (int i = 0; itemQueue != null && i < itemQueue.size(); i++) {
				Item item = itemQueue.getItem(i);
				if (!item.isUsed()) {
					continue;
				}
				String contextPath = request.getContextPath();
				String url = MenuHelper.getMainUrl(contextPath, item,
						(java.util.Map) null);
				MenuItemU menuItemU = new MenuItemU();
				menuItemU.setId(item.getId());
				menuItemU.setName(item.getName(request));
				menuItemU.setImageUrl(item.getMouseclickimg(request));
				menuItemU.setPathU(url);
				menuItemU.setType("item");
				menuItemU.setDesktop_height(item.getDesktop_height());
				menuItemU.setDesktop_width(item.getDesktop_width());
				list.add(menuItemU);
				// System.out.println("=========="+url+"===="+item.getName());
			}

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
				list.add(menuItemU);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// putIntoResponse("text/javascript;charset=utf-8", returnJsonString,
		// "utf-8", response);
		// return null;
		return null;
	}

	public @ResponseBody(datatype="json")
	ItemsBean queryItemsBean(@RequestParam(name = "moduleId") String moduleId,
			@RequestParam(name = "parentPath") String parentPath,
			HttpServletRequest request) {
		// String returnJsonString = "";
		ItemsBean bean = new ItemsBean();
		bean.setParentId(moduleId);
		
		try {
			AccessControl control = AccessControl.getAccessControl();
			// control.checkAccess(request, response);
			DeskTopMenuBean deskTopMenuBean = new DeskTopMenuBean();
			deskTopMenuBean.setUserid(control.getUserID());
			deskTopMenuBean.setSubsystem(control.getCurrentSystemID());
			Map deskTopMap = deskTopMenuShorcutManager.queryMenuCustom(deskTopMenuBean);
			
			MenuHelper menuHelper = MenuHelper.getMenuHelper(control.getRequest());
			if(!moduleId.equals("rootitems"))
			{
				
			
			
				String modulePath = null;
				if (parentPath == null) {
					modulePath = control.getCurrentSystemID()
							+ "::menu://sysmenu$root/" + moduleId + "$module";
				} else {
					modulePath = parentPath + "/" + moduleId + "$module";
				}
				Module module_parent = menuHelper.getModule(modulePath);
				bean.setParentPath(modulePath);
				bean.setParentName(module_parent.getName(request));
	
				ItemQueue itemQueue = module_parent.getItems();
				ModuleQueue moduleQueue = module_parent.getSubModules();
	
				List<MenuItemU> list = new ArrayList<MenuItemU>();
				for (int i = 0; itemQueue != null && i < itemQueue.size(); i++) {
					Item item = itemQueue.getItem(i);
					if (!item.isUsed()) {
						continue;
					}
					String contextPath = request.getContextPath();
					String url = MenuHelper.getMainUrl(contextPath, item,
							(java.util.Map) null);
					MenuItemU menuItemU = new MenuItemU();
					menuItemU.setId(item.getId());
					menuItemU.setName(item.getName(request));
					menuItemU.setImageUrl(item.getMouseclickimg(request));
					menuItemU.setPathU(url);
					menuItemU.setType("item");
					
					
					DeskTopMenuBean deskTop = (DeskTopMenuBean) deskTopMap.get(item.getPath());
					
					
					//找到列表对应的菜单项，并赋予窗口大小
					menuItemU.setDesktop_height((deskTop == null||deskTop.getHeight() == null||deskTop.getHeight().equals(""))?item.getDesktop_height():deskTop.getHeight());
					menuItemU.setDesktop_width((deskTop == null||deskTop.getWidth() == null||deskTop.getWidth().equals(""))?item.getDesktop_width():deskTop.getWidth());
					
					list.add(menuItemU);
					// System.out.println("=========="+url+"===="+item.getName());
				}
				bean.setListItems(list);
				list = new ArrayList<MenuItemU>();
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
					menuItemU.setHasSon(module.getSubModules() != null
							&& module.getSubModules().size() > 0
							|| (module.getItems() != null && module.getItems()
									.size() > 0));
					list.add(menuItemU);
				}
				bean.setListModules(list);
			}
			else
			{
				String modulePath = control.getCurrentSystemID()
							+ "::menu://sysmenu$root";
				bean.setParentPath(modulePath);
				bean.setParentName("个人设置");
				ItemQueue itemQueue = menuHelper.getItems();				
	
				List<MenuItemU> list = new ArrayList<MenuItemU>();
				for (int i = 0; itemQueue != null && i < itemQueue.size(); i++) {
					Item item = itemQueue.getItem(i);
					if (!item.isUsed()) {
						continue;
					}
					String contextPath = request.getContextPath();
					String url = MenuHelper.getMainUrl(contextPath, item,
							(java.util.Map) null);
					MenuItemU menuItemU = new MenuItemU();
					menuItemU.setId(item.getId());
					menuItemU.setName(item.getName(request));
					menuItemU.setImageUrl(item.getMouseclickimg(request));
					menuItemU.setPathU(url);
					menuItemU.setType("item");
					
					
					DeskTopMenuBean deskTop = (DeskTopMenuBean) deskTopMap.get(item.getPath());
					
					
					//找到列表对应的菜单项，并赋予窗口大小
					menuItemU.setDesktop_height((deskTop == null||deskTop.getHeight() == null||deskTop.getHeight().equals(""))?item.getDesktop_height():deskTop.getHeight());
					menuItemU.setDesktop_width((deskTop == null||deskTop.getWidth() == null||deskTop.getWidth().equals(""))?item.getDesktop_width():deskTop.getWidth());
					
					list.add(menuItemU);
					// System.out.println("=========="+url+"===="+item.getName());
				}
				bean.setListItems(list);
			}
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// putIntoResponse("text/javascript;charset=utf-8", returnJsonString,
		// "utf-8", response);
		// return null;
		return bean;
	}

	public @ResponseBody(datatype="json")
	ItemsBean queryRootItemsBean(HttpServletRequest request) {
		// String returnJsonString = "";
		ItemsBean bean = new ItemsBean();
		
		try {
			AccessControl control = AccessControl.getAccessControl();
			// control.checkAccess(request, response);
			DeskTopMenuBean deskTopMenuBean = new DeskTopMenuBean();
			deskTopMenuBean.setUserid(control.getUserID());
			deskTopMenuBean.setSubsystem(control.getCurrentSystemID());
			Map deskTopMap = deskTopMenuShorcutManager.queryMenuCustom(deskTopMenuBean);
			
			
			MenuHelper menuHelper = MenuHelper.getMenuHelper(control.getRequest());

			ModuleQueue moduleQueue = menuHelper.getModules();

			ItemQueue itemQueue = menuHelper.getItems();

			List<MenuItemU> list = new ArrayList<MenuItemU>();
			for (int i = 0; itemQueue != null && i < itemQueue.size(); i++) {
				Item item = itemQueue.getItem(i);
				if (!item.isUsed()) {
					continue;
				}
				String contextPath = request.getContextPath();
				String url = MenuHelper.getMainUrl(contextPath, item,
						(java.util.Map) null);
				MenuItemU menuItemU = new MenuItemU();
				menuItemU.setId(item.getId());
				menuItemU.setName(item.getName(request));
				menuItemU.setImageUrl(item.getMouseclickimg(request));
				menuItemU.setPathU(url);
				menuItemU.setType("item");
				
				DeskTopMenuBean deskTop = (DeskTopMenuBean) deskTopMap.get(item.getPath());
				
				
				//找到列表对应的菜单项，并赋予窗口大小
				menuItemU.setDesktop_height((deskTop == null||deskTop.getHeight() == null||deskTop.getHeight().equals(""))?item.getDesktop_height():deskTop.getHeight());
				menuItemU.setDesktop_width((deskTop == null||deskTop.getWidth() == null||deskTop.getWidth().equals(""))?item.getDesktop_width():deskTop.getWidth());
				list.add(menuItemU);
				// System.out.println("=========="+url+"===="+item.getName());
			}
			bean.setListItems(list);
			list = new ArrayList<MenuItemU>();
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
				menuItemU.setHasSon(module.getSubModules() != null
						&& module.getSubModules().size() > 0
						|| (module.getItems() != null && module.getItems()
								.size() > 0));
				list.add(menuItemU);
			}
			bean.setListModules(list);
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// putIntoResponse("text/javascript;charset=utf-8", returnJsonString,
		// "utf-8", response);
		// return null;
		return bean;
	}

	public ModelAndView header() {
		AccessControl control = AccessControl.getAccessControl();
		String modulePath = control.getCurrentSystemID()
				+ "::menu://sysmenu$root";
		MenuHelper menuHelper = MenuHelper.getMenuHelper(control.getRequest());
		ModuleQueue moduleQueue = menuHelper.getSubModules(modulePath);
		ModelAndView view = new ModelAndView("path:goheader");
		view.addObject("moduleQueue", moduleQueue);
		return view;
	}

	public ModelAndView headersimple() {
		AccessControl control = AccessControl.getAccessControl();
		String modulePath = control.getCurrentSystemID()
				+ "::menu://sysmenu$root";
		MenuHelper menuHelper = MenuHelper.getMenuHelper(control.getRequest());
		ModuleQueue moduleQueue = menuHelper.getSubModules(modulePath);
		
			
		ModelAndView view = new ModelAndView("path:goheadersimple");
		view.addObject("moduleQueue", moduleQueue);
		ItemQueue roots = menuHelper.getItems();
		if(roots.size() > 0)
			view.addObject("hasrootitems", "true");
		return view;
	}

	public ModelAndView contentoutlook(HttpServletRequest request) {
		AccessControl control = AccessControl.getAccessControl();
		String modulePath = control.getCurrentSystemID()
				+ "::menu://sysmenu$root";
		MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
		ModuleQueue moduleQueue = menuHelper.getSubModules(modulePath);
		ModelAndView view = new ModelAndView("path:gocontentoutlook");
		view.addObject("moduleQueue", moduleQueue);
		ItemQueue roots = menuHelper.getItems();
		if(roots.size() > 0)
			view.addObject("hasrootitems", "true");
		view.addObject("publicitem",menuHelper.getPublicItem());
		
		return view;
	}

	public ModelAndView jf() {
		AccessControl control = AccessControl.getAccessControl();
		String modulePath = control.getCurrentSystemID()
				+ "::menu://sysmenu$root";
		MenuHelper menuHelper = MenuHelper.getMenuHelper(control.getRequest());
		ModuleQueue moduleQueue = menuHelper.getSubModules(modulePath);
		ModelAndView view = new ModelAndView("path:jf");
		view.addObject("moduleQueue", moduleQueue);
		return view;
	}

	public ModelAndView setting() {
		ModelAndView view = new ModelAndView("path:gosetting");
		AccessControl control = AccessControl.getAccessControl();
		// control.checkAccess(request, response);
		MenuHelper menuHelper = MenuHelper.getMenuHelper(control.getRequest());
		ItemQueue iq=menuHelper.getItems();
		List list=iq.getList();
		view.addObject("list",list);
		return view;
	}

	// /**
	// * 写入HttpServletResponse对象
	// *
	// * @param contentType 类似于"text/javascript;charset=utf-8"
	// * @param content 待写入的内容
	// * @param characterCode 字符编码:utf-8; gbk; iso-8859-1......
	// */
	// public void putIntoResponse(String contentType, String content,
	// String characterCode, HttpServletResponse response ) { // 写入response
	//		
	// response.setCharacterEncoding(characterCode);
	// response.setContentType(contentType);
	// PrintWriter out = null;
	// try {
	// out = response.getWriter();
	// } catch (IOException e) {
	// throw new RuntimeException("写入输出流失败", e);
	// }
	//
	// out.print(content);
	// out.close();
	// }
}
