package com.sany.common.menu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.frameworkset.web.servlet.support.RequestContextUtils;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Root;
import com.frameworkset.platform.security.AccessControl;

/**
 * <div class="titlebox">
				<span class="location">您的当前位置： <a href="#">应用开发基础平台</a> &gt;
					台账管理</span>
			</div>
 * @author yinbp
 *
 */
public class MenuPathTag extends BaseTag {
	private static String header = "<div class=\"titlebox\"><span class=\"location\">"; 
	private static String rooter = "</span></div>";
	private final static String PRE_TITLE = "您的当前位置：";
	private String menuid ;
	private boolean showappname = false;
	public boolean isShowappname() {
		return showappname;
	}
	public void setShowappname(boolean showappname) {
		this.showappname = showappname;
	}
//	public final static String  sanymenupath_menuid = "sanymenupath_menuid";
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	private String pretitle = null;
	public String getPretitle() {
		return pretitle;
	}
	public void setPretitle(String pretitle) {
		this.pretitle = pretitle;
	}
	@Override
	public int doStartTag() throws JspException {	
		int ret = super.doStartTag();
		AccessControl control = AccessControl.getAccessControl(this.request,this.response);		
		Framework framework = Framework.getInstance(control.getCurrentSystemID());
		StringBuffer datas = new StringBuffer();
		if(this.pretitle == null)
			pretitle = framework.getMessage("sany.pdp.module.currentposition", RequestContextUtils.getRequestContextLocal(request));
		datas.append(header);
		datas.append(pretitle);
		if(showappname)
			datas.append(framework.getDescription(request));
		if(menuid == null || menuid.equals(""))
		{
			menuid = request.getParameter(MenuHelper.sanymenupath_menuid);			
		}
		
		if(menuid == null || menuid.equals(""))			
			return ret;
		else
		{
			
		}
		MenuItem menu = framework.getMenuByID(menuid);
		datas.append("<a href=\"javascript:void(0)\">")
			.append(framework.getPublicItem().getName(request))
			.append("</a>&gt;");
		if(menu == null )
		{			
			datas.append(rooter);
		}
		else
		{
			StringBuffer path = new StringBuffer();
			int i = 0;
			boolean isroot = false;
			do
			{	
				isroot = menu.getParent() instanceof Root;
				
				if(!showappname && isroot)
				{
					
					if(i == 0)
					{
						path.insert(0, menu.getName(request));
					}
					else
					{
						path.insert(0, "<a href=\"javascript:void(0)\">"+menu.getName(request) + "</a>");
					}
					
				}
				else
				{
					if(i == 0)
					{
						path.insert(0, "&gt;"+menu.getName(request));
					}
					else
					{
						path.insert(0, "&gt;<a href=\"javascript:void(0)\">"+menu.getName(request) + "</a>");
					}
				}
				menu = menu.getParent();
				i ++;
				
				
			}while(menu != null && !isroot);
			datas.append(path);
			datas.append(rooter);
		}	
		
		try {
			this.out.write(datas.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	@Override
	public void doFinally() {
		
		super.doFinally();
		this.menuid = null;
		this.showappname = false;
		this.pretitle = null;
	}
	
	
	
	
}
