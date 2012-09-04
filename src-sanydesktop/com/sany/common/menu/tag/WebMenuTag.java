package com.sany.common.menu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.frameworkset.web.servlet.support.RequestContextUtils;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;

public class WebMenuTag extends BaseTag {
	private static String header = "<div id=\"menubar\" class=\"ddsmoothmenu \"><ul>"; 
	private static String rooter = "</ul><br style=\"clear: left\" /></div>";
	public static final String personcenter = "个人中心";
	
	
	
	private int level = 3;
	private String web = "true";
	
	@Override
	public int doStartTag() throws JspException {	
		int ret = super.doStartTag();
		String tokenurl = request.getContextPath() + "/token/getParameterToken.freepage"; 
		AccessControl control = AccessControl.getAccessControl();
		MenuHelper menuHelper =  MenuHelper.getMenuHelper(request);
		String personcenter = Framework.getInstance(control.getCurrentSystemID()).getMessage("sany.pdp.module.personcenter", RequestContextUtils.getRequestContextLocal(request));
		StringBuffer datas = new StringBuffer();
		String selectedmenuid = request.getParameter(MenuHelper.sanyselectedmodule);//查找选择的菜单项path
		datas.append(header);
		StringBuffer sysmanagermodule_path = new StringBuffer();
		
		sysmanagermodule_path 
							  .append(control.getCurrentSystemID())
							  .append("::menu://sysmenu$root/sysmanagermodule$module");
		StringBuffer sysmanagermodule_url = new StringBuffer();
		
		sysmanagermodule_url .append(request.getContextPath())
							  .append("/sanydesktop/frame.page?sany_menupath=")
							  .append(sysmanagermodule_path)
							 ;
		
		String contextpath = request.getContextPath();
		ItemQueue itemQueue = menuHelper.getItems();
		ModuleQueue moduleQueue = menuHelper.getModules();
		Item publicitem = menuHelper.getPublicItem();
		
		String framepath = web != null && web.equals("true")?contextpath + "/sanydesktop/webframe.page":contextpath + "/sanydesktop/frame.page";
		if(publicitem != null && publicitem.isMain())
		{

			String target = publicitem.getTarget() == null ?"indexFrame":publicitem.getTarget();
			String url = MenuHelper.getRealUrl(contextpath,publicitem.getWorkspacecontentExtendAttribute("isany"));
			String selectedclass = "";
			if(selectedmenuid == null || selectedmenuid.equals("publicitem"))
			{
				selectedclass = "class=\"select\"";
			}
			if(target.equals("indexFrame"))
			{
				datas.append("<li><a href=\"#\" id=\"anchor_").append("publicitem").append("\"  ").append(selectedclass).append(" onClick=\"navto_sany_MenuItem('").append(tokenurl)
				.append("','").append("publicitem")
				.append("','").append(url).append("','")
				.append(target).append("')\">")
				.append(publicitem.getName(request))
				.append("</a></li>");
			}
			else
			{
				
				datas.append("<li><a href=\"#\" id=\"anchor_").append("publicitem").append("\"  ").append(selectedclass).append(" onClick=\"navto_sany_MenuItem_window('").append(tokenurl)
				.append("','").append(publicitem.getName(request))
				.append("','").append("publicitem")
				.append("','','").append(contextpath)
				 .append("','")
				.append(target).append("')\">")
				.append(publicitem.getName(request))
				.append("</a></li>");
			}
		}
		
		
		
		for (int i = 0; moduleQueue != null && i < moduleQueue.size(); i++) {
			Module module = moduleQueue.getModule(i);
			if (!module.isUsed()) {
				continue;
			}
			String selectedclass = "";
			if(selectedmenuid != null && selectedmenuid.startsWith(module.getId()))
			{
				selectedclass = "class=\"select\"";
			}
			if(!module.isShowleftmenu())
			{
				datas.append("<li><a id=\"anchor_").append(module.getId()).append("\"  ").append(selectedclass).append(" href=\"#\">").append(module.getName(request)).append("</a>");
			}
			else
			{
				String target = module.getTarget() == null ?"indexFrame":module.getTarget();
				if(target.equals("indexFrame"))
				{
					datas.append("<li><a href=\"#\"  ").append(selectedclass).append("  id=\"anchor_")
						 .append(module.getId())
						 .append("\" onClick=\"navto_sany_MenuItem('").append(tokenurl)
				.append("','")
						 .append(module.getId())
						 .append("','")
						 .append(framepath).append("?")
						 .append(MenuHelper.sanymenupath)
						 .append("=")
						 .append(module.getPath())
						 .append("','")
						 .append(target)
						 .append("')\">")
						 .append(module.getName(request))
						 .append("</a>");
				}
				else
				{
					datas.append("<li><a href=\"#\"  ").append(selectedclass).append(" id=\"anchor_")
					 .append(module.getId())
					 .append("\" onClick=\"navto_sany_MenuItem_window('").append(tokenurl)
				.append("','")
					 .append(module.getName(request))
					 .append("','")
					 .append(module.getId())
					 .append("','")
					 .append(module.getPath())
					 .append("','").append(contextpath)
					 .append("','")
					 .append(target)
					 .append("')\">")
					 .append(module.getName(request))
					 .append("</a>");
				}
			}
			datas.append("<ul >");  //class=\"second\"
			
			ItemQueue subitems = module.getItems() != null ?module.getItems():null;
			for(int j = 0; subitems != null && j < subitems.size(); j ++)
			{
				Item subitem = subitems.getItem(j);
				
				
				String target = subitem.getTarget() == null ?"indexFrame":subitem.getTarget();	
				
				
				
				
				if(target.equals("indexFrame"))
				{

					String url = MenuHelper.getItemUrl(subitem, contextpath, framepath, control);
					datas.append("<li><a id=\"anchor_").append(subitem.getId()).append("\" href=\"#\" onclick=\"navto_sany_MenuItem('").append(tokenurl)
				.append("','").append(module.getId()).append("','").append(url).append("','").append(target).append("')\"><span></span>")
						.append("<div>").append(subitem.getName(request)).append("</div>")
						.append("</a></li>");
				}
				else
				{
					datas.append("<li><a id=\"anchor_").append(subitem.getId())
					.append("\" href=\"#\" onclick=\"navto_sany_MenuItem_window('").append(tokenurl)
				.append("','")
					.append(subitem.getName(request)).append("','")
					.append(module.getId()).append("','")
					.append(subitem.getPath()).append("','").append(contextpath)
					 .append("','")
					.append(target).append("')\"><span></span>")
					.append("<div>").append(subitem.getName(request)).append("</div>")
					.append("</a></li>");
				}
				
			}
			ModuleQueue submodules = module.getSubModules() != null?  module.getSubModules():null;
			for(int j = 0; submodules != null && j < submodules.size(); j ++)
			{
				Module submodule = submodules.getModule(j);

				String target = submodule.getTarget() == null ?"indexFrame":submodule.getTarget();
				
				renderSubMenus(submodule, datas, contextpath, target,control,module.getId(),framepath,2,tokenurl);

			}
			
			
			datas.append("</ul></li>");
			
		}
		//系统维护
		Module mainmodule =menuHelper.getModule(sysmanagermodule_path.toString());
		boolean enableinmenu = mainmodule.getStringExtendAttribute("enableinmenu","true").equals("true");
		if(mainmodule != null && enableinmenu)
		{
			String selectedclass = "";
			String t_title = mainmodule.getName(request);
			if(selectedmenuid != null && selectedmenuid.equals("sysmanagermodule"))
			{
				selectedclass = "class=\"select\"";
			}
			if(!menuHelper.isShowrootmenuleft())
			{
				datas.append("<li><a id=\"anchor_").append("sysmanagermodule").append("\" ").append(selectedclass).append(" href=\"#\">").append(t_title).append("</a>");
			}
			else
			{
				String target = "indexFrame";	
				datas.append("<li><a href=\"#\" id=\"anchor_")
				 .append("isany_personcenter")
				 .append("\" onClick=\"navto_sany_sysmanagermodule('")
				 .append("sysmanagermodule")
				 .append("','")
				 .append(sysmanagermodule_url)
				 .append("','")
				 .append(t_title)
				 .append("')\">")
				 .append(t_title)
				 .append("</a>");
			}
			
		    datas.append("</li>");
		}
		
		/**
		 * 个人中心
		 */
		if(itemQueue != null && itemQueue.size() > 0)
		{
			String selectedclass = "";
			if(selectedmenuid != null && selectedmenuid.equals("isany_personcenter"))
			{
				selectedclass = "class=\"select\"";
			}
			if(!menuHelper.isShowrootmenuleft())
			{
				datas.append("<li><a id=\"anchor_").append("isany_personcenter").append("\" ").append(selectedclass).append(" href=\"#\">").append(personcenter).append("</a>");
			}
			else
			{
				String target = "indexFrame";	
				datas.append("<li><a href=\"#\" id=\"anchor_")
				 .append("isany_personcenter")
				 .append("\" onClick=\"navto_sany_MenuItem('").append(tokenurl)
				.append("','")
				 .append("isany_personcenter")
				 .append("','")
				 .append(framepath).append("?")
				 .append(MenuHelper.sanymenupath)
				 .append("=")
				 .append("isany_personcenter")
				 .append("','")
				 .append(target)
				 .append("')\">")
				 .append(personcenter)
				 .append("</a>");
			}
			datas.append("<ul class=\"second\">");
			Item item = null ;
			for(int i = 0; i < itemQueue.size(); i ++)
			{
				item = itemQueue.getItem(i);

				String target = item.getTarget() == null ?"indexFrame":item.getTarget();	
				if(target.equals("indexFrame"))
				{

					String url = MenuHelper.getItemUrl(item, contextpath, framepath, control);
					datas.append("<li><a href=\"#\" id=\"anchor_").append(item.getId()).append("\" onClick=\"navto_sany_MenuItem('").append(tokenurl)
				.append("','").append("isany_personcenter").append("','").append(url).append("','").append(target).append("')\"><span></span>")
					.append("<div>").append(item.getName(request)).append("</div>")
					.append("</a></li>");
				}
				else
				{
					datas.append("<li><a href=\"#\" id=\"anchor_").append(item.getId()).append("\" onClick=\"navto_sany_MenuItem_window('").append(tokenurl)
				.append("','")
					.append("isany_personcenter").append("','")
					.append(item.getPath())
					.append("','").append(contextpath)
					 .append("','")
					.append(target)
					.append("')\"><span></span>")
					.append("<div>").append(item.getName(request)).append("</div>")
					.append("</a></li>");
				}
		        
			}
		    datas.append("</ul></li>");
		}
		  
		datas.append(rooter);
		try {
			//System.out.println(datas.toString());
			this.out.write(datas.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			datas = null;
		}
		return ret;
	}
	
	private void renderSubMenus(Module module,StringBuffer datas,String contextpath,String target,AccessControl control,String selectedID,String framepath,int current_level,String tokenurl)
	{
		
		if (!module.isUsed()) {
			return ;
		}			
		if(!module.isShowleftmenu())
		{
			datas.append("<li><a  href=\"#\"><span></span><div>").append(module.getName(request)).append("</div></a>");
		}
		else
		{
			if(target.equals("indexFrame"))
			{
				datas.append("<li><a  href=\"#\" ").append("\" onClick=\"navto_sany_MenuItem('").append(tokenurl)
				.append("','")
				 .append(selectedID)
				 .append("','")
				 .append(framepath).append("?")
				 .append(MenuHelper.sanymenupath)
				 .append("=")
				 .append(module.getPath())
				 .append("','")
				 .append(target)
				 .append("')\">").append(module.getName(request)).append("</a>");
			}
			else
			{
				datas.append("<li><a  href=\"#\" ").append("\" onClick=\"navto_sany_MenuItem_window('").append(tokenurl)
				.append("','")
				 .append(module.getName(request))
				 .append("','")
				 .append(selectedID)
				 .append("','")
				 .append(module.getPath())
				 .append("','")
				 .append(contextpath)
				 .append("','")
				 .append(target)
				 .append("')\">").append(module.getName(request)).append("</a>");
			}
			
		}
		if(current_level < level)
		{
			datas.append("<ul>"); // class=\"third\"
			
			current_level ++;
			ModuleQueue submodules = module.getSubModules() != null?  module.getSubModules():null;
			for(int j = 0; submodules != null && j < submodules.size(); j ++)
			{
				
				Module submodule = submodules.getModule(j);
				String target_ = submodule.getTarget() == null?"indexFrame":submodule.getTarget(); 
				renderSubMenus(submodule,datas,contextpath,target_,control,selectedID,framepath,current_level,tokenurl);

			}
			ItemQueue subitems = module.getItems() != null ?module.getItems():null;
			for(int j = 0; subitems != null && j < subitems.size(); j ++)
			{
				Item subitem = subitems.getItem(j);
				String area = subitem.getArea();
				

				String target_ = subitem.getTarget() == null?"indexFrame":subitem.getTarget(); 
				if(target.equals("indexFrame"))
				{
	
					String url = MenuHelper.getItemUrl(subitem, contextpath, framepath, control);
					datas.append("<li><a  href=\"#\" id=\"anchor_").append(subitem.getId()).append("\" onClick=\"navto_sany_MenuItem('").append(tokenurl)
				.append("','").append(selectedID).append("','").append(url).append("','").append(target_).append("')\">")
						.append(subitem.getName(request))
						.append("</a></li>");
				}
				else
				{
					datas.append("<li><a  href=\"#\" id=\"anchor_").append(subitem.getId())
					.append("\" onClick=\"navto_sany_MenuItem_window('").append(tokenurl)
				.append("','")
					.append(subitem.getName(request)).append("','")
					.append(selectedID).append("','").append(subitem.getPath()).append("','").append(contextpath)
					 .append("','").append(target_).append("')\">")
					.append(subitem.getName(request))
					.append("</a></li>");
				}
				
			}
			datas.append("</ul>");
		}
		
		datas.append("</li>");
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void doFinally() {
		// TODO Auto-generated method stub
		super.doFinally();
		this.level = 3;
		this.web = "true";
	}

	

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

}
