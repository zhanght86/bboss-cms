package com.frameworkset.platform.esb.datareuse.common.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.esb.datareuse.common.entity.DeskTopBackGround;
import com.frameworkset.platform.esb.datareuse.common.entity.DeskTopMenuBean;
import com.frameworkset.platform.esb.datareuse.common.entity.ItemMenuCustom;
import com.frameworkset.platform.esb.datareuse.common.entity.MenuItemU;
import com.frameworkset.platform.esb.datareuse.common.service.DeskTopMenuShorcutManager;
import com.frameworkset.platform.framework.BaseMenuItem;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;


public class DeskTopController {
	public static final String isany_personcenter_menuid = "isany_personcenter";
	DeskTopMenuShorcutManager deskTopMenuShorcutManager;
	public @ResponseBody String cleardeskmenu()
	{
		
		AccessControl control = AccessControl.getAccessControl();
		try
		{
			deskTopMenuShorcutManager.cleardeskmenu( control.getUserID(), control.getCurrentSystemID());
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
		return "ok";
	}
	
	public @ResponseBody String cleardefaultdeskmenu()
	{
		
		AccessControl control = AccessControl.getAccessControl();
		try
		{
			deskTopMenuShorcutManager.cleardeskmenu( "-1", control.getCurrentSystemID());
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
		return "ok";
	}
	
	/**
	 * 桌面快捷
	 * 
	 * @return
	 */
	public ModelAndView shortcut(HttpServletRequest request) throws Exception {
		ModelAndView view = new ModelAndView("path:shortcut");
		AccessControl control = AccessControl.getAccessControl();
		
		Framework frame = Framework.getInstance(control.getCurrentSystemID()); 
		
		
		DeskTopMenuBean bean = new DeskTopMenuBean();
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		List<DeskTopMenuBean> list = deskTopMenuShorcutManager.getUserDeskMenus(bean);
		List<MenuItemU> listreturn=new ArrayList<MenuItemU>();
		DeskTopMenuBean deskTopMenuBean = null;
		MenuItemU mb= null;
		if (list != null && list.size() > 0) {
			for(int i=0;i<list.size();i++){
				mb=new MenuItemU();
				deskTopMenuBean = list.get(i);
				BaseMenuItem item=(BaseMenuItem)frame.getMenuByPath(deskTopMenuBean.getMenupath());
				if(item == null)
				{
					System.out.println("deskTopMenuBean.getMenupath():>>>>>>>>>>>>>>>>>>>>>>>>"+deskTopMenuBean.getMenupath());
					continue;
				}
				if(!control.checkPermission(item.getId(), "visible", "column"))
					continue;
				mb.setId(item.getId());
				mb.setName(item.getName(request));
				String contextPath = request.getContextPath();
				String url = MenuHelper.getMainUrl(contextPath, item,
						(java.util.Map) null);
				mb.setPathU(url);
				mb.setImageUrl(item.getMouseclickimg(request));
				//判断表里面是否有自定义窗口大小数据，如果有就取该数据，如果无，就使用默认的数据
				mb.setDesktop_height((deskTopMenuBean.getWidth()!= null&&!deskTopMenuBean.getWidth().equals(""))? deskTopMenuBean.getWidth():item.getDesktop_width());
				mb.setDesktop_width((deskTopMenuBean.getHeight() != null&&!deskTopMenuBean.getHeight().equals(""))? deskTopMenuBean.getHeight():item.getDesktop_height());
				listreturn.add(mb);
			}
		}
		view.addObject("menulist", listreturn);
		return view;
	}
	
	private String parserID(String path)
	{
		//dp::menu://sysmenu$root/products$module/xxxx$item
		String menuid = "://sysmenu$root/";
		
		int start = path.indexOf(menuid)+menuid.length();
		int end = path.indexOf('$', start);
		if(path.charAt(end + 1) == 'i')
		{
			return isany_personcenter_menuid;
		}
		else
		{
			return path.substring(start,end);
		}
	}
	
	public static void main(String[] args)
	{
		String path ="dp::menu://sysmenu$root/products$module/xxxx$item";
				String menuid = "://sysmenu$root/";
				
				int start = path.indexOf(menuid)+menuid.length();
				int end = path.indexOf('$', start);
				
				
				System.out.println(path.substring(start,end));
	}
	
	public @ResponseBody(datatype="json") List<MenuItemU> getCustomMenus(HttpServletRequest request,String urltype) throws Exception
	{
		AccessControl control = AccessControl.getAccessControl();
		List<DeskTopMenuBean> list = deskTopMenuShorcutManager.getUserDeskMenus(control.getUserID(),control.getCurrentSystemID());
		if(list == null || list.size() == 0)
		{
			list = deskTopMenuShorcutManager.getUserDeskMenus("-1",control.getCurrentSystemID());
		}
		List<MenuItemU> listreturn=new ArrayList<MenuItemU>();
		DeskTopMenuBean deskTopMenuBean = null;
		
		Framework frame = Framework.getInstance(control.getCurrentSystemID()); 
		
		if (list != null && list.size() > 0) {
			for(int i=0;i<list.size();i++){
				MenuItemU mb=new MenuItemU();
				BaseMenuItem item=(BaseMenuItem)frame.getMenuByPath(list.get(i).getMenupath());
				deskTopMenuBean = list.get(i);
				if(item == null)
				{
					
					continue;
				}
				if(!control.checkPermission(item.getId(), "visible", "column"))
					continue;
				mb.setId(item.getId());
				mb.setName(item.getName(request));
				mb.setOption(item.getOption());
				String contextPath = request.getContextPath();
//				if(urltype == null || urltype.equals("1"))
				String url = contextPath+"/sanydesktop/frame.page?sany_menupath="+item.getPath();
				mb.setPathPopu(url);
			
			
				url = contextPath+"/sanydesktop/index.page?sany_menupath="+item.getPath() + "&sanyselectedmodule="+parserID(item.getPath());
				mb.setPathU(url);
				
				
//				else
				
				
				
				mb.setImageUrl(StringUtil.getRealPath(contextPath, item.getMouseclickimg(request),true));
				//判断表里面是否有自定义窗口大小数据，如果有就取该数据，如果无，就使用默认的数据
				mb.setDesktop_width((deskTopMenuBean.getWidth()!= null&&!deskTopMenuBean.getWidth().equals(""))? deskTopMenuBean.getWidth():item.getDesktop_width());
				mb.setDesktop_height((deskTopMenuBean.getHeight() != null&&!deskTopMenuBean.getHeight().equals(""))? deskTopMenuBean.getHeight():item.getDesktop_height());
				listreturn.add(mb);
			}
		}
		return listreturn;
	}
	/**
	 * 桌面主页
	 * 
	 * @return
	 */
	public ModelAndView desktop(HttpServletRequest request) throws Exception {
		ModelAndView view = new ModelAndView("path:desktop");
//		AccessControl control = AccessControl.getAccessControl();
//		
//		Framework frame = Framework.getInstance(control.getCurrentSystemID()); 
//		
//		
//		DeskTopMenuBean bean = new DeskTopMenuBean();
//		bean.setUserid(control.getUserID());
//		bean.setSubsystem(control.getCurrentSystemID());
//		List<DeskTopMenuBean> list = deskTopMenuShorcutManager.getUserDeskMenus(bean);
//		List<MenuItemU> listreturn=new ArrayList<MenuItemU>();
//		if (list != null && list.size() > 0) {
//			for(int i=0;i<list.size();i++){
//				MenuItemU mb=new MenuItemU();
//				Item item=frame.getItem(list.get(i).getMenupath());
//				if(item == null)
//				{
//					System.out.println("list.get(i).getMenupath():>>>>>>>>>>>>>>>>>>>>>>>>"+list.get(i).getMenupath());
//					continue;
//				}
//				if(!control.checkPermission(item.getId(), "visible", "column"))
//					continue;
//				mb.setId(item.getId());
//				mb.setName(item.getName());
//				mb.setPathU(StringUtil.getRealPath(request.getContextPath(), item.getWorkspaceContent()));
//				mb.setImageUrl(item.getMouseclickimg());
//				listreturn.add(mb);
//			}
//		}
//		view.addObject("menulist", listreturn);
		return view;
	}
	
	
	public ModelAndView desktop1(HttpServletRequest request) throws Exception {
		ModelAndView view = new ModelAndView("path:desktop1");
		AccessControl control = AccessControl.getAccessControl();
		
		Framework frame = Framework.getInstance(control.getCurrentSystemID()); 
		
		
		DeskTopMenuBean bean = new DeskTopMenuBean();
		
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		List<DeskTopMenuBean> list = deskTopMenuShorcutManager.getUserDeskMenus(bean);
		if(list == null || list.size() == 0)
		{
			list = deskTopMenuShorcutManager.getUserDeskMenus("-1",control.getCurrentSystemID());
		}
		List<MenuItemU> listreturn=new ArrayList<MenuItemU>();
		DeskTopMenuBean deskTopMenuBean = null;
		
		if (list != null && list.size() > 0) {
			for(int i=0;i<list.size();i++){
				MenuItemU mb=new MenuItemU();
				BaseMenuItem item=(BaseMenuItem)frame.getMenuByPath(list.get(i).getMenupath());
//				Item item=frame.getItem(list.get(i).getMenupath());
				deskTopMenuBean = list.get(i);
				if(item == null)
				{
					System.out.println("list.get(i).getMenupath():>>>>>>>>>>>>>>>>>>>>>>>>"+list.get(i).getMenupath());
					continue;
				}
				if(!control.checkPermission(item.getId(), "visible", "column"))
					continue;
				mb.setId(item.getId());
				mb.setName(item.getName(request));
				String contextPath = request.getContextPath();
				String url = MenuHelper.getMainUrl(contextPath, item,
						(java.util.Map) null);
				mb.setPathU(url);
				mb.setImageUrl(item.getMouseclickimg(request));
				//判断表里面是否有自定义窗口大小数据，如果有就取该数据，如果无，就使用默认的数据
				mb.setDesktop_width((deskTopMenuBean.getWidth()!= null&&!deskTopMenuBean.getWidth().equals(""))? deskTopMenuBean.getWidth():item.getDesktop_width());
				mb.setDesktop_height((deskTopMenuBean.getHeight() != null&&!deskTopMenuBean.getHeight().equals(""))? deskTopMenuBean.getHeight():item.getDesktop_height());
				listreturn.add(mb);
			}
		}
//		AccessControl control = AccessControl.getAccessControl();
		// control.checkAccess(request, response);

		
		
		Map deskTopMap = deskTopMenuShorcutManager.queryMenuCustom(bean);
		DeskTopMenuBean deskTop = null;
		MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
		List taskbarlist_ = new ArrayList();
		ItemQueue taskbarlist = menuHelper.getItems();
		if (taskbarlist != null && taskbarlist.size() > 0) {
			for(int i=0;i<taskbarlist.size();i++){
				MenuItemU mb=new MenuItemU();
				Item item=taskbarlist.getItem(i);
				
				if(item == null)
				{
					System.out.println("list.get(i).getMenupath():>>>>>>>>>>>>>>>>>>>>>>>>"+list.get(i).getMenupath());
					continue;
				}
				if(!control.checkPermission(item.getId(), "visible", "column"))
					continue;
				mb.setId(item.getId());
				mb.setName(item.getName(request));
				String contextPath = request.getContextPath();
				String url = MenuHelper.getMainUrl(contextPath, item,
						(java.util.Map) null);
				mb.setPathU(url);
				mb.setImageUrl(item.getMouseclickimg(request));
				
				deskTop = (DeskTopMenuBean) deskTopMap.get(item.getPath());
				//判断表里面是否有自定义窗口大小数据，如果有就取该数据，如果无，就使用默认的数据
				if(deskTop != null)
				{
				mb.setDesktop_width((deskTop.getWidth()!= null&&!deskTop.getWidth().equals(""))? deskTop.getWidth():item.getDesktop_width());
				mb.setDesktop_height((deskTop.getHeight() != null&&!deskTop.getHeight().equals(""))? deskTop.getHeight():item.getDesktop_height());
				}else{
					mb.setDesktop_width(item.getDesktop_width());
					mb.setDesktop_height(item.getDesktop_height());
				}
				taskbarlist_.add(mb);
			}
		}
		
		DeskTopBackGround deskTopBlackGround = null;
		DeskTopBackGround blackGroundBean = new DeskTopBackGround();
//		String blackGround = "";
		blackGroundBean.setUserid(control.getUserID());
		blackGroundBean.setSubsystem(control.getCurrentSystemID());
		try {
			deskTopBlackGround = deskTopMenuShorcutManager.getDesktopBlackGround(blackGroundBean);
			if(deskTopBlackGround != null)
			{
				File newFile = new File(request.getRealPath("/")+"desktop/wallpapers/"+deskTopBlackGround.getFilename());
				deskTopMenuShorcutManager.storeDesktopStyleCustomPictureToDisk(newFile, deskTopBlackGround);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(deskTopBlackGround != null){
			deskTopBlackGround.setFit(deskTopBlackGround.getFit().equals("0")?"false":"true");
			view.addObject("blackGround", deskTopBlackGround);
		}else{ 
			blackGroundBean.setFilename("desktop.jpg");
			blackGroundBean.setFit("false");
			view.addObject("blackGround", blackGroundBean);
			
		}
		
		view.addObject("tackbarlist", taskbarlist_);
		view.addObject("menulist", listreturn);
		return view;
	}

	public @ResponseBody String updatedeskmenu(List<DeskTopMenuBean> list,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		AccessControl control = AccessControl.getAccessControl();
		deskTopMenuShorcutManager.update(list, control.getUserID(), control.getCurrentSystemID());
		return "ok";
	}
	
	public @ResponseBody String updatedefaultdeskmenu(List<DeskTopMenuBean> list,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		AccessControl control = AccessControl.getAccessControl();
		deskTopMenuShorcutManager.update(list, "-1", control.getCurrentSystemID());
		return "ok";
	}
	
	public ModelAndView desktopsort(HttpServletRequest request,boolean isdefault) throws Exception {
		ModelAndView view = new ModelAndView("path:sort");
		AccessControl control = AccessControl.getAccessControl();
		
		Framework frame = Framework.getInstance(control.getCurrentSystemID()); 
		
		
		DeskTopMenuBean bean = new DeskTopMenuBean();
		if(!isdefault)
			bean.setUserid(control.getUserID());
		else
			bean.setUserid("-1");
		bean.setSubsystem(control.getCurrentSystemID());
		List<DeskTopMenuBean> list = deskTopMenuShorcutManager.getUserDeskMenus(bean);
//		List<MenuItemU> listreturn=new ArrayList<MenuItemU>();
		if (list != null && list.size() > 0) {
			for(int i=0;i<list.size();i++){
//				MenuItemU mb=new MenuItemU();
				MenuItem item=frame.getMenuByPath(list.get(i).getMenupath());
				if(item == null)
				{
					System.out.println("list.get(i).getMenupath():>>>>>>>>>>>>>>>>>>>>>>>>"+list.get(i).getMenupath());
					continue;
				}
				if(!control.checkPermission(item.getId(), "visible", "column"))
					continue;
				list.get(i).setMenuname(item.getName(request));
			}
		}
		view.addObject("menulist", list);
		return view;
	}
	
	public @ResponseBody String updatemenusort(List<DeskTopMenuBean> menuBeans){
		
		try {
			deskTopMenuShorcutManager.updateMenuSort(menuBeans);
			return "ok";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
	}
	
	public void updatemenusortBymenuName(HttpServletRequest request,HttpServletResponse response){
		
		AccessControl control = AccessControl.getAccessControl();
		
		Framework frame = Framework.getInstance(control.getCurrentSystemID()); 
		
		
		DeskTopMenuBean bean = new DeskTopMenuBean();
		bean.setUserid(control.getUserID());
		bean.setSubsystem(control.getCurrentSystemID());
		List<DeskTopMenuBean> list = null;
		try {
			list = deskTopMenuShorcutManager.getUserDeskMenus(bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arr = new String[list.size()];
		if (list != null && list.size() > 0) {
			for(int i=0;i<list.size();i++){
				BaseMenuItem item=(BaseMenuItem)frame.getMenuByPath(list.get(i).getMenupath());
//				Item item=frame.getItem(list.get(i).getMenupath());
				if(item == null)
				{
					System.out.println("list.get(i).getMenupath():>>>>>>>>>>>>>>>>>>>>>>>>"+list.get(i).getMenupath());
					continue;
				}
				if(!control.checkPermission(item.getId(), "visible", "column"))
					continue;
				list.get(i).setMenuname(item.getName(request));
				arr[i] = list.get(i).getMenuname();
			}
		}
		
		List<DeskTopMenuBean> listbyName = new ArrayList<DeskTopMenuBean>();
		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
		Arrays.sort(arr, cmp);  
		for (int i = 0; i < arr.length; i++){
			for(int j=0;j<list.size();j++){
			  DeskTopMenuBean dtmb = list.get(j);
			  if(arr[i].equals(dtmb.getMenuname())){
				  dtmb.setItem_order(i+"");
				  listbyName.add(dtmb);
			  }
			}
		} 
		
		PrintWriter pw = null; 
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			deskTopMenuShorcutManager.updateMenuSort(listbyName);
			pw.print("ok");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pw.print("fail");
		}

	}
	
	
	public String getMenuCustom(HttpServletRequest request,
			@RequestParam(name="columnID")String columnID,
			ModelMap model){
		AccessControl control = AccessControl.getAccessControl();
		Framework frame = Framework.getInstance(control.getCurrentSystemID()); 
		
//		Item item=frame.getItem(columnID);
		BaseMenuItem item=(BaseMenuItem)frame.getMenuByID(columnID);
		
		ItemMenuCustom imc = new ItemMenuCustom();
		imc.setUserid(control.getUserID());
		imc.setSubsystem(control.getCurrentSystemID());
		imc.setMenupath(columnID);
		if(item != null)
		model.addAttribute("operaName", item.getName(request));
		ItemMenuCustom newImc = null;
		try {
			newImc = deskTopMenuShorcutManager.queryMenuWinSize(imc);
			if(newImc == null){
				newImc = new ItemMenuCustom();
				newImc.setUserid(imc.getUserid());
				newImc.setSubsystem(imc.getSubsystem());
				newImc.setMenupath(imc.getMenupath());
				if(item != null){
				newImc.setHeight(item.getDesktop_height());
				newImc.setWidth(item.getDesktop_width());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("menuBean", newImc);
		return "path:menucustom";
	}
	
	public @ResponseBody String savemenusize(ItemMenuCustom imc){
		ItemMenuCustom newIMC = null;
			try {
				newIMC = deskTopMenuShorcutManager.queryMenuWinSize(imc);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			if(newIMC != null){
					try {
						deskTopMenuShorcutManager.updateMenuWinSize(imc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "fail";
					}
			}else if(newIMC == null){
					try {
						deskTopMenuShorcutManager.insertMenuWinSize(imc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "fail";
					}
			}
		  return "ok";
	}
	
	public @ResponseBody String reset(DeskTopMenuBean bean){
		
		try {
			deskTopMenuShorcutManager.resetDeskMenuCustom(bean);
			return "ok";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		
		
	}
	
	public @ResponseBody String resetall(HttpServletRequest request){
		AccessControl control = AccessControl.getAccessControl();
		// control.checkAccess(request, response);
		String userId = control.getUserID();
		String subsystem = control.getCurrentSystemID();
		
		try {
			deskTopMenuShorcutManager.deleteAllDeskMenuCustom(userId, subsystem);
			return "ok";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	public void setDeskTopMenuShorcutManager(
			DeskTopMenuShorcutManager deskTopMenuShorcutManager)
	{
	
		this.deskTopMenuShorcutManager = deskTopMenuShorcutManager;
	}

	
	
	
	
}
