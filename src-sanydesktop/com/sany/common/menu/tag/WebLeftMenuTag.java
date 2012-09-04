package com.sany.common.menu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.frameworkset.web.servlet.support.RequestContextUtils;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.framework.Root;
import com.frameworkset.platform.security.AccessControl;

/**
 * <DIV class="leftmenu">
    <H4>Product information</H4>
    <UL class="expand">
      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Machinery.html" target="rightFrame" class="current">Concrete Machinery</A></LI>
      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Pump_Trucks.html" target="rightFrame">Concrete Pump Trucks</A></LI>
    </UL>
    <H4>Product information</H4>
    <UL>
      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Machinery.html" target="rightFrame">Concrete Machinery</A></LI>
      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Pump_Trucks.html" target="rightFrame">Concrete Pump Trucks</A></LI>
    </UL>
  </DIV>
 * @author yinbp
 *
 */
public class WebLeftMenuTag extends BaseTag{
	
	private static String header = "<div class=\"leftmenu\">"; 
	private static String rooter = "</div>";
	
	private String menupath ;
//	public final static String  sanymenupath_menuid = "sany_menupath";
	private String target = "rightFrame";
	
	private void fromTag(StringBuffer datas,String tokenurl ) throws JspException {	
		
		AccessControl control = AccessControl.getAccessControl();
		
		String contextpath = request.getContextPath();
		
		datas.append(header);		
		
		if(menupath == null || menupath.equals(""))
		{
			menupath = request.getParameter(MenuHelper.sanymenupath);			
		}
		
		if(menupath == null || menupath.equals(""))			
			;
		else
		{
			MenuHelper menuHelper = MenuHelper.getMenuHelper(request);
			String personcenter = Framework.getInstance(control.getCurrentSystemID()).getMessage("sany.pdp.module.personcenter", RequestContextUtils.getRequestContextLocal(request));
			
			MenuItem menu = null;
			if("isany_personcenter".equals(menupath))
			{
				
			}
			else
			{
				menu = menuHelper.getCurrentSystemMenu(menupath);
			}
			if(menu == null )
			{			
				if("isany_personcenter".equals(menupath))//根路径下的菜单模块
				{
					/**
					 * <H4>Product information</H4>
    <UL class="expand">
      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Machinery.html" target="rightFrame" class="current">Concrete Machinery</A></LI>
      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Pump_Trucks.html" target="rightFrame">Concrete Pump Trucks</A></LI>
    </UL>
					 */
					datas.append("<H4>").append(personcenter).append("</H4>");
					datas.append("<ul class=\"expand\">");
					
					ItemQueue iq = menuHelper.getItems();
					if(iq.size() > 0)
					{	
						
						appendItems(datas,iq,null,contextpath,control,true,tokenurl);
						
					}
					datas.append("</ul>");
				}
			}
			else
			{				
				boolean isroot = false;
				String selectModule = null;
				String selectItem = null;
				if(menu instanceof Item )
				{
					selectItem = menu.getPath();
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
				
				Module module = (Module)menu;
				if(selectModule == null)
				{
					selectModule = module.getPath();
				}
//				datas.append("<ul>");
				ModuleQueue mq = module.getSubModules();
				ItemQueue iq = module.getItems();
				for(int i = 0; i < mq.size(); i ++)
				{
					Module submodule = mq.getModule(i);
					if(selectModule != null && selectModule.equals(submodule.getPath()))
					{
						/**
						 * <H4>Product information</H4>
	    <UL class="expand">
	      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Machinery.html" target="rightFrame" class="current">Concrete Machinery</A></LI>
	      <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Pump_Trucks.html" target="rightFrame">Concrete Pump Trucks</A></LI>
	    </UL>
						 */
						datas.append("<H4>");
						
						datas.append(submodule.getName(request)).append("</H4>");
						datas.append("<UL class=\"expand\">");
						appendItems(datas,submodule.getItems(),selectItem,contextpath,control,true,tokenurl);
						datas.append("</UL>");
					}
					else
					{
						datas.append("<H4>");
						
						datas.append(submodule.getName(request)).append("</H4>");
						datas.append("<UL >");
						
						appendItems(datas,submodule.getItems(),selectItem,contextpath,control,false, tokenurl);
						datas.append("</UL>");
					}
				}
				if(iq.size() > 0)
				{
					if(selectModule != null 
							&& selectModule.equals(module.getPath()))
					{
						datas.append("<H4>");
						
						datas.append(module.getName(request)).append("</H4>");
						datas.append("<UL class=\"expand\">");
//						datas.append("<li class=\"select_links\"><a href=\"#\">").append(module.getName(request)).append("</a>");
						appendItems(datas,iq,selectItem,contextpath,control,true, tokenurl);
						datas.append("</UL>");
					}
					else
					{
//						datas.append("<li ><a href=\"#\">")
//						.append(module.getName(request)).append("</a>");
						datas.append("<H4>");	
						
						datas.append(module.getName(request)).append("</H4>");
						datas.append("<UL >");
						appendItems(datas,iq,selectItem,contextpath,control,false, tokenurl);
						datas.append("</UL>");
					}
								
					
//					datas.append("</li>");		
				}
//				datas.append("</ul>");
			}
		}
		datas.append(rooter);
	
	}
	
	public int doStartTag() throws JspException {	
		int ret = super.doStartTag();
		String tokenurl = request.getContextPath() + "/token/getParameterToken.freepage"; 
		StringBuffer datas = new StringBuffer();
		if(menupath == null || menupath.equals(""))
		{
			fromTopMenu(datas, tokenurl )	;
		}
		else
		{
			fromTag(datas,tokenurl);
		}
		try {
			this.out.write(datas.toString());
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return ret;
	}
	
	private void fromTopMenu(StringBuffer datas,String tokenurl )
	{
		AccessControl control = AccessControl.getAccessControl();
		String rootmodulepath = (String)request.getAttribute("rootmodulepath");
		String selectModule = (String)request.getAttribute("selectModule");
		String selectItem = (String)request.getAttribute("selectItem");
		String selectUrl = (String)request.getAttribute("selectUrl");
		String contextpath = request.getContextPath();
		MenuItem menu = (MenuItem)request.getAttribute("rootmodule");
	
		datas.append(header);	
		MenuHelper menuHelper = (MenuHelper)request.getAttribute("menuHelper");
		String personcenter = Framework.getInstance(control.getCurrentSystemID()).getMessage("sany.pdp.module.personcenter", RequestContextUtils.getRequestContextLocal(request));
						
		if("isany_personcenter".equals(rootmodulepath))//根路径下的菜单模块
		{
			datas.append("<H4>");			
			datas.append(personcenter).append("</H4>");
			
			
			ItemQueue iq = menuHelper.getItems();
			if(iq.size() > 0)
			{	
//				datas.append("<li class=\"select_links\"><a href=\"#\">").append(personcenter).append("</a>");
				datas.append("<UL class=\"expand\">");
				appendItems(datas,iq,selectItem,contextpath,control,true, tokenurl);
				datas.append("</ul>");		
			}
			
		}
		
		else
		{				
			
			Module module = (Module)menu;
			
//			datas.append("<ul>");
			ModuleQueue mq = module.getSubModules();
			ItemQueue iq = module.getItems();
			if(iq.size() > 0)
			{
				if(selectModule != null 
						&& selectModule.equals(module.getPath()))
				{
//					datas.append("<li class=\"select_links\"><a href=\"#\">").append(module.getName(request)).append("</a>");
					datas.append("<H4>");			
					datas.append(module.getName(request)).append("</H4>");
					datas.append("<UL class=\"expand\">");
					appendItems(datas,iq,selectItem,contextpath,control,true, tokenurl);
					datas.append("</ul>");		
				}
				else
				{
					
//					datas.append("<li ><a href=\"#\">")
//					.append(module.getName(request)).append("</a>");
					datas.append("<H4>");			
					datas.append(module.getName(request)).append("</H4>");
					datas.append("<UL >");
					appendItems(datas,iq,selectItem,contextpath,control,false, tokenurl);
					datas.append("</ul>");		
				}
							
				
//				datas.append("</li>");		
			}
			for(int i = 0; i < mq.size(); i ++)
			{
				Module submodule = mq.getModule(i);
				if(selectModule != null && selectModule.equals(submodule.getPath()))
				{
//					datas.append("<li class=\"select_links\"><a href=\"#\">");
//					datas.append(submodule.getName(request)).append("</a>");
					datas.append("<H4>");			
					datas.append(submodule.getName(request)).append("</H4>");
					datas.append("<UL class=\"expand\">");
					appendItems(datas,submodule.getItems(),selectItem,contextpath,control,true, tokenurl);
					datas.append("</ul>");		
				}
				else
				{
//					datas.append("<li><a href=\"#\">");
//					datas.append(submodule.getName(request)).append("</a>");
					datas.append("<H4>");			
					datas.append(submodule.getName(request)).append("</H4>");
					datas.append("<UL >");
					appendItems(datas,submodule.getItems(),selectItem,contextpath,control,false, tokenurl);
					datas.append("</ul>");
				}
				
//				datas.append("</li>");					
			}
			
//			datas.append("</ul>");
		}
		datas.append(rooter);
	}
	
	/**
	 * 
	 * @param datas
	 * @param iq
	 * @param selectmodulepath
	 * @param selectItem
	 */
	private void appendItems(StringBuffer datas,ItemQueue iq,String selectItem,String contextpath,AccessControl control,boolean selectedModule,String tokenurl)
	{

		 for(int i = 0; i < iq.size(); i ++)
		 {
			 Item subitem = iq.getItem(i);
			 String area = subitem.getArea();
			String url = null;
			if(area != null && area.equals("main"))
			{
				url = MenuHelper.getMainUrl(contextpath, subitem,
						(java.util.Map) null);
			}
			else
			{
				url = MenuHelper.getRealUrl(contextpath, Framework.getWorkspaceContent(subitem,control),MenuHelper.sanymenupath_menuid,subitem.getId());
			}
			 if(selectItem != null && subitem.getPath().equals(selectItem))
			 {
				 /**
				  * <LI><A href="${pageContext.request.contextPath}/html2/Concrete_Machinery.html" target="rightFrame" class="current">Concrete Machinery</A></LI>
				  */
//				 datas.append("<li  class=\"select_links\"><a href=\"").append(url).append("\" target=\"").append(target).append("\">").append(subitem.getName(request)).append("</a></li>");
//				 datas.append("<li  ><a href=\"").append(url).append("\" target=\"").append(target).append("\" class=\"current\">").append(subitem.getName(request)).append("</a></li>");
				 datas.append("<li  ><a href=\"#\" onclick=\"leftnavto_sany_MenuItem('").append(tokenurl)
				.append("','").append(url).append("','").append(target).append("')\" class=\"current\">").append(subitem.getName(request)).append("</a></li>");
			 }
			 else
			 {
//				 datas.append("<li><a href=\"").append(url).append("\" target=\"").append(target).append("\">").append(subitem.getName(request)).append("</a></li>");
				 datas.append("<li  ><a href=\"#\" onclick=\"leftnavto_sany_MenuItem('").append(tokenurl)
				.append("','").append(url).append("','").append(target).append("')\">").append(subitem.getName(request)).append("</a></li>");
			 }
		 }  
//	     datas.append("</ul>");
	}
	@Override
	public void doFinally() {
		
		this.menupath = null;
		this.target = "rightFrame";
		super.doFinally();
		
		
	}
	public String getMenupath() {
		return menupath;
	}
	public void setMenupath(String menupath) {
		this.menupath = menupath;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
