package com.sany.common.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.servlet.support.RequestContextUtils;
import org.frameworkset.web.token.MemTokenManager;
import org.frameworkset.web.token.MemTokenManagerFactory;

import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.FrameworkServlet;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.framework.Root;
import com.frameworkset.platform.security.AccessControl;
import common.Logger;

public class DesktopController {
	private static Logger log = Logger.getLogger(DesktopController.class);
	public String products()
	{
		return "path:products";
	}
	public String cookieLocale(String language,HttpServletResponse response,HttpServletRequest request)
	{
	
		
		
//		StringUtil.addCookieValue(request, response, "cookie.localkey", language, 3600 * 24);
//		
//		
//		if(language.equals("en_US"))
//		{
//			request.getSession().setAttribute("session.localkey",java.util.Locale.US);
//		}
//		else
//		{
//			request.getSession().setAttribute("session.localkey",java.util.Locale.CHINA);
//		}
		
		try {
			RequestContextUtils.getLocaleResolver(request).setLocale(request, response, language);
		} catch (Exception e) {
			log.error("",e);
		}
//			loginPathCookie.setPath(request.getContextPath());
		
		return "path:login";
	}
	
	public String index(String sany_menupath,String sany_selecturl,HttpServletRequest request,ModelMap model)
	{
		return _index(sany_menupath,sany_selecturl,request,model,false);
			
	}
	
	
	private String _index(String sany_menupath,String url,HttpServletRequest request,ModelMap model,boolean isweb)
	{
		
		MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
		Item publicitem = menuHelper.getPublicItem();
		String contextpath = request.getContextPath();
		if(sany_menupath == null || sany_menupath.equals(""))
		{
			
			if(publicitem != null && publicitem.isMain())
			{
				String isanypage = publicitem.getWorkspacecontentExtendAttribute("isany");
				if(isanypage == null)
					isanypage = "jf.jsp";
				if(url == null || url.equals(""))
				{
					url = MenuHelper.getRealUrl(contextpath,isanypage);
					
					
				}
				else
				{
					url = MenuHelper.getRealUrl(contextpath,url);
					
				}
				model.addAttribute("mainurl", url);
			}
		}
		else
		{
			MenuItem menu = menuHelper.getFramework().getMenuByPath(sany_menupath);
			
			String framepath = isweb?new StringBuffer().append(contextpath).append("/sanydesktop/webframe.page").toString()
					:new StringBuffer().append(contextpath).append("/sanydesktop/frame.page").toString();
			
			if(menu instanceof Module)
			{
				
				if(url == null || url.equals(""))
				{
					url =  new StringBuffer().append(framepath ).append( "?") .append(MenuHelper.sanymenupath)
							 .append("=")
							 .append(sany_menupath).toString();
				}
				else
				{
					
					url = java.net.URLEncoder.encode(url);
					
					url =  new StringBuffer().append(framepath ).append( "?") .append(MenuHelper.sanymenupath)
							 .append("=")
							 .append(sany_menupath).append("&").append(MenuHelper.sany_selecturl).append("=").append(url).toString();
				}
				model.addAttribute("mainurl", url); 
			}
			else
			{
				
				
				
				url = MenuHelper.getItemUrl((Item )menu, contextpath,framepath,url,AccessControl.getAccessControl());
				
//				String url =  new StringBuffer().append(contextpath ).append( "/sanydesktop/frame.page?") .append(LeftMenuTag.sanymenupath_menuid)
//						 .append("=")
//						 .append(sany_menupath);
				model.addAttribute("mainurl", url);
			}
		}
		
		String temp = publicitem.getLogoImage(request);
		String logoimage =contextpath + "/html/images/top_logo.jpg";
		if(temp != null)				
			logoimage = MenuHelper.getRealUrl(contextpath,temp);
		model.addAttribute("logoimage", logoimage);
		model.addAttribute("fromwebseal", AccessControl.fromWebseal(request));
		model.addAttribute("isweb", isweb);
		model.addAttribute("selected", AccessControl.getAccessControl().getCurrentSystemID());
		String selectedmenuid = request.getParameter(MenuHelper.sanyselectedmodule);//查找选择的菜单项path
		
		if(selectedmenuid != null)
		{
			model.addAttribute("selectedmenuid", selectedmenuid);
		}
		else
			model.addAttribute("selectedmenuid", "publicitem");
		if(!isweb)
		{
			return "path:index";
		}
		else
			return "path:webindex";
	}
	public String webindex(String sany_menupath,String sany_selecturl,HttpServletRequest request,ModelMap model)
	{
//		System.out.println("---------------------------------------sany_selecturl:" + sany_selecturl);
		return _index(sany_menupath,sany_selecturl,request,model,true);
		
			
	}
	
	/**
	 * 应用于第三方系统（例如：辅料系统应用）单点登录后的应用菜单集成
	 * @param sany_menupath
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String singleframe(String sany_menupath,HttpServletRequest request,ModelMap model)throws Exception
	{
		MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
		AccessControl control = AccessControl.getAccessControl();		
		if(sany_menupath == null || sany_menupath.equals(""))			
			throw new Exception("必须指定菜单路径");
		String rootmodulepath = null;
		String selectModule = null;
		String selectItem = null;
		String selectUrl = null;
		String contextpath = request.getContextPath();
		MenuItem menu = null;
		if("isany_personcenter".equals(sany_menupath))
		{
			rootmodulepath = sany_menupath;
			selectModule = sany_menupath;
			ItemQueue iq = menuHelper.getItems();
			Item item = iq.size() > 0?iq.getItem(0):null; 			
			if(item != null)
			{
				selectItem = item.getPath();
				String area = item.getArea();
				
				if(area != null && area.equals("main"))
				{
					java.util.Map<String,String> param = new HashMap<String,String>();
					param.put(MenuHelper.sanymenupath_menuid, item.getId());
					selectUrl = MenuHelper.getMainUrl(contextpath, item,
							param);
				}
				else
				{
					selectUrl = MenuHelper.getRealUrl(contextpath, 
							Framework.getWorkspaceContent((Item)item,control),MenuHelper.sanymenupath_menuid,item.getId());
				}
			}
		}
		else
		{
			menu = menuHelper.getCurrentSystemMenu(sany_menupath);
		}
		if(menu == null )
		{			
			if("isany_personcenter".equals(sany_menupath))//根路径下的菜单模块
			{
				
			}
			else
			{
				throw new Exception("必须指定菜单路径[" + sany_menupath + "]对应的模块不存在，请检查系统" + control.getCurrentSystemID()+"已经配置好菜单");
			}
		}
		else
		{				
			boolean isroot = false;
			if(menu instanceof Item )
			{
				selectItem = menu.getPath();
				String area = menu.getArea();
				if(area != null && area.equals("main"))
				{
					java.util.Map<String,String> param = new HashMap<String,String>();
					param.put(MenuHelper.sanymenupath_menuid, menu.getId());
					selectUrl = MenuHelper.getMainUrl(contextpath, menu,
							param);
				}
				else
				{
					selectUrl = MenuHelper.getRealUrl(contextpath, 
							Framework.getWorkspaceContent((Item)menu,control),MenuHelper.sanymenupath_menuid,menu.getId());
				}
				
				
			}
			else
			{
					
				    Module module = (Module)menu;
				    if(module.getSubModules() != null && module.getSubModules() .size() > 0)
			    	{
				    	selectModule = module.getSubModules() .getModule(0).getPath();
			    	}
				    else
				    {
				    	selectModule = menu.getPath();
				    }
					

				
			}
			
			if(menu instanceof Module)
			{
				Module module = (Module)menu;
				if(selectModule == null)
				{
					selectModule = module.getPath();
				}
				rootmodulepath = module.getPath();
			}
			else
			{
				rootmodulepath = "isany_personcenter";
				selectModule = "isany_personcenter";
				
				Item item = (Item)menu; 			
				if(item != null)
				{
					selectItem = item.getPath();
					String area = item.getArea();
					
					if(area != null && area.equals("main"))
					{
						java.util.Map<String,String> param = new HashMap<String,String>();
						param.put(MenuHelper.sanymenupath_menuid, item.getId());
						selectUrl = MenuHelper.getMainUrl(contextpath, item,
								 param);
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								Framework.getWorkspaceContent(item,control),MenuHelper.sanymenupath_menuid,item.getId());
					}
				}
			}
			
		}
		if(selectModule != null)
		{
			model.addAttribute("selectModule", selectModule);
			if(selectItem == null)
			{
				Module m_temp = (Module)menuHelper.getCurrentSystemMenu(selectModule);
				ItemQueue its = m_temp.getItems();
				Item item = its.size() > 0?its.getItem(0):null; 			
				if(item != null)
				{
					selectItem = item.getPath();
					String area = item.getArea();
					
					if(area != null && area.equals("main"))
					{
						java.util.Map<String,String> param = new HashMap<String,String>();
						param.put(MenuHelper.sanymenupath_menuid, item.getId());
						selectUrl = MenuHelper.getMainUrl(contextpath, item,
								param);
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								Framework.getWorkspaceContent((Item)item,control),MenuHelper.sanymenupath_menuid,item.getId());
					}
				}
			}
		}
		if(selectItem != null)
			model.addAttribute("selectItem", selectItem);
		if(selectUrl != null)
		{
			MemTokenManager memtokenmanager = MemTokenManagerFactory.getMemTokenManagerNoexception();
			if(memtokenmanager != null)
			{
				selectUrl = memtokenmanager.appendDTokenToURL(request, selectUrl);
			}
			
			model.addAttribute("selectUrl", selectUrl);
		}
		
		if(rootmodulepath != null)
		{
			model.addAttribute("rootmodulepath", rootmodulepath);
			model.addAttribute("rootmodule", menu);
		}
		if(menu != null)
			model.addAttribute("rootmodule", menu);
		model.addAttribute("menuHelper", menuHelper);
		return "path:frame";
	}
	public String frame(String sany_menupath,String sany_selecturl,HttpServletRequest request,ModelMap model) throws Exception
	{
		return _frame(sany_menupath, sany_selecturl,request,model,false);
	}
	public String webframe(String sany_menupath,String sany_selecturl,HttpServletRequest request,ModelMap model) throws Exception
	{
		return _frame(sany_menupath, sany_selecturl,request,model,true);
	}
	
	private String specialModuleHandle(Module module,String sany_selecturl,AccessControl control)
	{
		String ret = null;
		if(!module.isShowleftmenu())
		{
			if(module.getUrl() == null)
			{
				if(sany_selecturl != null && !sany_selecturl.equals(""))
					ret = MenuHelper.getRealUrl(null, 
							sany_selecturl,MenuHelper.sanymenupath_menuid,module.getId());
//				datas.append("<li><a id=\"anchor_").append(module.getId()).append("\"  ").append(selectedclass).append(" href=\"#\">").append(module.getName(request)).append("</a>");
			}
			else
			{
				if(sany_selecturl != null && !sany_selecturl.equals(""))
					ret = MenuHelper.getRealUrl(null, 
							sany_selecturl,MenuHelper.sanymenupath_menuid,module.getId());
				else
					ret = MenuHelper.getModuleUrl(module, null,  control);
							
			}
		}
		else
		{
			
			
			boolean hasson = module.hasSonOfModule();
			if(hasson)
			{
				
			}
			else
			{
				if(module.getUrl() == null)
				{
					if(sany_selecturl != null && !sany_selecturl.equals(""))
						ret = MenuHelper.getRealUrl(null, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,module.getId());
				}
				else
				{
					if(sany_selecturl != null && !sany_selecturl.equals(""))
						ret = MenuHelper.getRealUrl(null, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,module.getId());
					else
						ret = MenuHelper.getModuleUrl(module, null,  control);
					
				}
			}
			
			
		}
		if(ret != null && !ret.startsWith("redirect:"))
			ret = "redirect:"+ret;
		return ret;
	}
	
	private String specialItemHandle(Item module,String sany_selecturl,AccessControl control)
	{
		String ret = null;
		if(!module.isShowleftmenu())
		{
			if(sany_selecturl != null && !sany_selecturl.equals(""))
				ret = MenuHelper.getRealUrl(null, 
						sany_selecturl,MenuHelper.sanymenupath_menuid,module.getId());
			else
				ret = MenuHelper.getRealUrl(null, 
					Framework.getWorkspaceContent(module,control),MenuHelper.sanymenupath_menuid,module.getId());
		}
		
		if(ret != null && !ret.startsWith("redirect:"))
		{
			ret = "redirect:"+ret;
		}
		return ret;
	}
	/**
	 * 带左侧菜单的主页面
	 * @param response
	 * @throws IOException
	 */
	private String _frame(String sany_menupath,String sany_selecturl,HttpServletRequest request,ModelMap model,boolean isweb) throws Exception
	{
		MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
		AccessControl control = AccessControl.getAccessControl();
		
		if(sany_menupath == null || sany_menupath.equals(""))			
			throw new Exception("必须指定菜单路径");
		String rootmodulepath = null;
		String selectModule = null;
		String selectItem = null;
		String selectUrl = null;
		String contextpath = request.getContextPath();
		MenuItem menu = null;
		Module temp_m = null;
		boolean useModuleUrl = false;
		if("isany_personcenter".equals(sany_menupath))
		{
			rootmodulepath = sany_menupath;
			selectModule = sany_menupath;
			ItemQueue iq = menuHelper.getItems();
			Item item = iq.size() > 0?iq.getItem(0):null; 			
			if(item != null)
			{
				
				selectItem = item.getPath();
				String area = item.getArea();
				
				if(area != null && area.equals("main"))
				{
					
					if(sany_selecturl == null || sany_selecturl.equals(""))
					{
						java.util.Map<String,String> param = new HashMap<String,String>();
						param.put(MenuHelper.sanymenupath_menuid, item.getId());
						selectUrl = MenuHelper.getMainUrl(contextpath, item,
								param);
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,item.getId());
					}
				}
				else
				{
					String special = specialItemHandle(item,sany_selecturl,control);
					if(special != null)
						return special;
					if(sany_selecturl == null || sany_selecturl.equals(""))
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								Framework.getWorkspaceContent((Item)item,control),MenuHelper.sanymenupath_menuid,item.getId());
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,item.getId());
					}
					
				}
			}
		}
		else
		{
			menu = menuHelper.getCurrentSystemMenu(sany_menupath);
		}
		if(menu == null )
		{			
			if("isany_personcenter".equals(sany_menupath))//根路径下的菜单模块
			{
				
			}
			else
			{
				throw new Exception("必须指定菜单路径[" + sany_menupath + "]对应的模块不存在，请检查系统" + control.getCurrentSystemID()+"已经配置好菜单");
			}
		}
		else
		{				
			boolean isroot = false;
			if(menu instanceof Item )
			{
				String special = specialItemHandle((Item )menu,sany_selecturl,control);
				if(special != null)
					return special;
				selectItem = menu.getPath();
				String area = menu.getArea();
				if(area != null && area.equals("main"))
				{
					if(sany_selecturl == null || sany_selecturl.equals(""))
					{
						java.util.Map<String,String> param = new HashMap<String,String>();
						param.put(MenuHelper.sanymenupath_menuid, menu.getId());
						selectUrl = MenuHelper.getMainUrl(contextpath, menu,
								param);
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,menu.getId());
					}
					
				}
				else
				{
					if(sany_selecturl == null || sany_selecturl.equals(""))
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								Framework.getWorkspaceContent((Item)menu,control),MenuHelper.sanymenupath_menuid,menu.getId());
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,menu.getId());
					}
					
				}
				if(menu.getParent() instanceof Root)
				{
					
				}
				else if(menu.getParent().getParent() instanceof Root)
				{
					selectModule = menu.getParent().getPath(); 
					menu = menu.getParent();
				}
				else
				{						
					do
					{	
						isroot = menu.getParent() instanceof Root;							
						if(isroot)
						{						
							break;						
						}
						selectModule = menu.getPath();
						menu = menu.getParent();
						
					}while(menu != null && !isroot);
				}
			}
			else
			{
				
				temp_m = (Module)menu;
				String specialurl = specialModuleHandle(temp_m,sany_selecturl,control);
				if(specialurl != null)
				{
					return specialurl;
				}
				if(temp_m.getUrl() != null)
				{
					
					if(sany_selecturl == null || sany_selecturl.equals(""))
					{
						selectUrl = MenuHelper.getRealUrl(contextpath,temp_m.getUrl(),MenuHelper.sanymenupath_menuid,temp_m.getId());
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,temp_m.getId());
					}
					useModuleUrl = true;
				}
				else
				{
					if(sany_selecturl == null || sany_selecturl.equals(""))
					{
//						selectUrl = MenuHelper.getRealUrl(contextpath,temp_m.getUrl(),MenuHelper.sanymenupath_menuid,temp_m.getId());
					}
					else
					{
						selectUrl = MenuHelper.getRealUrl(contextpath, 
								sany_selecturl,MenuHelper.sanymenupath_menuid,temp_m.getId());
						useModuleUrl = true;
					}
					
				}
				isroot = menu.getParent() instanceof Root;
				if(isroot)
				{						
					selectModule = menu.getPath();						
				}
				else
				{
					if(!useModuleUrl)
					{
						do
						{	
							menu = menu.getParent();
							selectModule = menu.getPath();
							isroot = menu.getParent() instanceof Root;
							if(isroot)
							{						
								break;						
							}
							
							
						}while(menu != null && !isroot);
					}
					else
					{
						selectModule = menu.getPath();
						do
						{	
							menu = menu.getParent();							
							isroot = menu.getParent() instanceof Root;
							if(isroot)
							{						
								break;						
							}
							
							
						}while(menu != null && !isroot);
					}
				}
			}
			
			if(menu instanceof Module)
			{
				Module module = (Module)menu;
				if(selectModule == null)
				{
					selectModule = module.getPath();
				}
				rootmodulepath = module.getPath();
			}
			else
			{
				rootmodulepath = "isany_personcenter";
				selectModule = "isany_personcenter";
				
				Item item = (Item)menu; 			
				if(item != null)
				{
					selectItem = item.getPath();
					String area = item.getArea();
					
					if(area != null && area.equals("main"))
					{
						
						
						if(sany_selecturl == null || sany_selecturl.equals(""))
						{
							java.util.Map<String,String> param = new HashMap<String,String>();
							param.put(MenuHelper.sanymenupath_menuid, item.getId());
							selectUrl = MenuHelper.getMainUrl(contextpath, item,
									 param);
						}
						else
						{
							selectUrl = MenuHelper.getRealUrl(contextpath, 
									sany_selecturl,MenuHelper.sanymenupath_menuid,item.getId());
						}
					}
					else
					{
						
						if(sany_selecturl == null || sany_selecturl.equals(""))
						{
							selectUrl = MenuHelper.getRealUrl(contextpath, 
									Framework.getWorkspaceContent(item,control),MenuHelper.sanymenupath_menuid,item.getId());
						}
						else
						{
							selectUrl = MenuHelper.getRealUrl(contextpath, 
									sany_selecturl,MenuHelper.sanymenupath_menuid,item.getId());
						}
						
					}
				}
			}
			
		}
		if(selectModule != null)
		{
			
			if(selectItem == null && !useModuleUrl)
			{
				Module m_temp = (Module)menuHelper.getCurrentSystemMenu(selectModule);
				ItemQueue its = m_temp.getItems();
				if(its == null || its.size() == 0 )
				{
					ModuleQueue mq = m_temp.getSubModules();
					if(mq != null && mq.size() > 0)
					{
						for(int i = 0; i < mq.size(); i ++)
						{
							m_temp = mq.getModule(i);
							its = m_temp.getItems();
							if(its != null && its.size() > 0 )
							{
								selectModule = m_temp.getPath();
								break;
							}
						}
					}
					
				}
				Item item = its.size() > 0?its.getItem(0):null; 			
				if(item != null)
				{
					selectItem = item.getPath();
					String area = item.getArea();
					
					if(area != null && area.equals("main"))
					{
						
						
						if(sany_selecturl == null || sany_selecturl.equals(""))
						{
							java.util.Map<String,String> param = new HashMap<String,String>();
							param.put(MenuHelper.sanymenupath_menuid, item.getId());
							selectUrl = MenuHelper.getMainUrl(contextpath, item,
									param);
						}
						else
						{
							selectUrl = MenuHelper.getRealUrl(contextpath, 
									sany_selecturl,MenuHelper.sanymenupath_menuid,item.getId());
						}
					}
					else
					{
						if(sany_selecturl == null || sany_selecturl.equals(""))
						{
							selectUrl = MenuHelper.getRealUrl(contextpath, 
									Framework.getWorkspaceContent((Item)item,control),MenuHelper.sanymenupath_menuid,item.getId());
						}
						else
						{
							selectUrl = MenuHelper.getRealUrl(contextpath, 
									sany_selecturl,MenuHelper.sanymenupath_menuid,item.getId());
						}
						
					}
				}
				
			}
			model.addAttribute("selectModule", selectModule);
		}
		if(selectItem != null)
			model.addAttribute("selectItem", selectItem);
		if(selectUrl != null)
		{
			MemTokenManager memtokenmanager = MemTokenManagerFactory.getMemTokenManagerNoexception();
			if(memtokenmanager != null)
			{
				selectUrl = memtokenmanager.appendDTokenToURL(request, selectUrl);
			}
			
			model.addAttribute("selectUrl", selectUrl);
			
		}
		if(rootmodulepath != null)
		{
			model.addAttribute("rootmodulepath", rootmodulepath);
			model.addAttribute("rootmodule", menu);
		}
		if(menu != null)
			model.addAttribute("rootmodule", menu);
		model.addAttribute("isweb", isweb);
		model.addAttribute("menuHelper", menuHelper);
		if(!isweb)
			return "path:frame";
		else
			return "path:webframe";
	}
	
	/**
	 * 左侧菜单页面
	 * @param response
	 * @throws IOException
	 */
	public void menuleft(HttpServletResponse response) throws IOException
	{
		response.setContentType(FrameworkServlet.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
	}
	
	public void menurigt(HttpServletResponse response) throws IOException
	{
		response.setContentType(FrameworkServlet.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
	}
	/**
	 * 在系统首页切换平台
	 * @param request
	 * @return
	 */
	public String switchSystem(HttpServletRequest request,ModelMap model)
	{
		MenuHelper menuHelper = MenuHelper.getMenuHelper(request,true);
		
		String indexpage = AccessControl.getIndexPage(request);
		if(!indexpage.startsWith("/"))
			indexpage = "/"+indexpage;
		model.addAttribute("selected", AccessControl.getAccessControl().getCurrentSystemID());
		return indexpage;
			
	}
	

}
