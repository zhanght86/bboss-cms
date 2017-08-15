package com.frameworkset.platform.menu;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.framework.Framework;
import com.frameworkset.platform.framework.FrameworkServlet;
import com.frameworkset.platform.framework.Item;
import com.frameworkset.platform.framework.ItemQueue;
import com.frameworkset.platform.framework.MenuHelper;
import com.frameworkset.platform.framework.MenuItem;
import com.frameworkset.platform.framework.Module;
import com.frameworkset.platform.framework.ModuleQueue;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.VelocityUtil;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

/**
 * <p>Title: 生成系统主菜单</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: iSany</p>
 *
 * @author biaoping.yin
 * @version 1.0 
 */
public class OutlookbarMenu implements Serializable{
    private static final Logger log = LoggerFactory.getLogger(OutlookbarMenu.class);

    private String parent;
    private PageContext pageContext;
    private HttpServletRequest request;
    //private HttpServletResponse response;
    private HttpSession session;
    private boolean flag = false;
    private boolean needTitleImg = false;
    private PrintWriter out;
    private static final String COLUMN_HEADER = "outlookbar/column_head.vm";
    private static final String COLUMN_TAILER = "outlookbar/column_tailer.vm";
    private static final String COLUMN = "outlookbar/column.vm";
    
    
    //mainmenu/left_head.vm
    private static final String NAVIGATOR = "outlookbar/navigator.vm";
    private static final String LEFT_HEAD = "outlookbar/left_head.vm";
    private static final String LEFT_TAILER = "outlookbar/left_tailer.vm";
    //mainmenu/left_tailer.vm
    private static final String PARENT_KEY = "parent_key";
    private static Map imageIDs = new HashMap();

    private String super_parent = "";
    private String selectImage;
    private String currentPath;
    private Framework framework = null;
    /**
     * 根据当前默认选中的栏目菜单项来确定栏目的根路径，
     * 需要知道是将当前栏目菜单项的父节点的路径作为根路径呢，
     * 还是将当前栏目菜单项的父节点所属父节点的路径作为根路径
     * 用属性ancestor的值来标识上述两种情况：
     * 1：当前栏目菜单项的父节点的路径作为根路径
     * 2：当前栏目菜单项的父节点所属父节点的路径作为根路径
     */
    private String ancestor = "2";

    private MenuItem currentMenu ;
    private String default_module = null;
    
    private AccessControl control = AccessControl.getInstance();    
    
    private MenuHelper menuHelper;
    
    boolean fromRequest = false;
    String menu_temp_path = "";
    private String subsystem = "";
    

    /**
     * 控制栏目层级
     */
    private int level = -1;
    
    /**
     * outlookbar的显示模式，0表示module title前面不带图标，item title图标和文字中间折行
     * 					   1表示module title前面带图标，item title图标和文字中间不折行
     * 缺省值为0。
     */
    private String showMode = "0";

    /**
     * 模板存放相对路径，针对不同的模板生成不同样式和风格的栏目菜单
     */
    private String templatePath = "";

    /**
     * 栏目菜单开始节点
     */
    private String rootID = "";
    
    public void setShowMode(String showMode)
    {
    	this.showMode = showMode;
    }
    


    /**
     * 获取当前菜单所属的module
     * @return Module
     */
    public MenuItem getCurrentModule()
    {
    	if(this.currentMenu == null)
    		return null;
    	return currentMenu .getParent();
    }

    /**
     * 获取选中图片的id
     * @param path
     * @return
     */

    private String getSelectImage(String path)
    {
        String ret = (String)imageIDs.get(path);
        if(ret != null)
        {
            return ret;
        }
        ret = StringUtil.replace(path,":","_");
        ret = StringUtil.replace(ret,"/","_");
        imageIDs.put(path,ret);
        return ret;

    }

    /**
     * 初始化生成栏目菜单树的环境
     * @param pageContext PageContext
     * @param control AccessControl
     */
    public void init(PageContext pageContext,AccessControl control)
    {
    	
        this.pageContext = pageContext;
        request = (HttpServletRequest)pageContext.getRequest();
        session = request.getSession();
        this.subsystem = FrameworkServlet.getSubSystem(request,(HttpServletResponse)pageContext.getResponse(),control.getUserAccount());
        this.super_parent = Framework.getSuperMenu(subsystem);
        
        menuHelper = new MenuHelper(subsystem,control);
        this.framework = Framework.getInstance(subsystem);

        if(control != null)
            this.control = control;
        else
            control.checkAccess(pageContext,false);
        
		out = new PrintWriter(pageContext.getOut());
		
		//如果点击进入某个模块或返回上一级时可以通过request获取当前的parentmodule
        this.parent = request.getParameter(PARENT_KEY);
        if(parent == null)
        {
            parent = (String) session.getAttribute(PARENT_KEY);
            session.removeAttribute(PARENT_KEY);

        }
        else
        	fromRequest = true;

        currentMenu  = FrameworkServlet.getCurrent(subsystem,session);
        if(currentMenu != null)
        {
            currentPath = currentMenu.getPath();
            //selectImage = currentMenu.getId();

            selectImage = getSelectImage(currentPath);
        }
        
        //如果路径不存在，则根据当前选中的item的路径计算出父路径
        if(parent == null)
        {
            if(currentMenu != null)
            {
            	int code = currentMenu.getCode();
            	if(code == 2)
            	{
            		parent = currentMenu.getParent().getPath();
        			default_module = "功能项";
            	}
            	else if(code == 0)
            	{
        			parent = currentMenu.getParent().getParentPath();
        			default_module = currentMenu.getParent().getName(request);
            	}
            	//如果是publicitem，则获取之前访问的item，根据该item获取当前parent和default_module
            	else if(code == 3)
            	{
            		String temp_current_path = (String)session.getAttribute("menu_temp_path_" + subsystem);
            		if(temp_current_path != null && !temp_current_path.equals(""))
            		{
            			Item item_t = Framework.getInstance(subsystem).getItem(temp_current_path);
            			int code_t = item_t.getCode();
                    	if(code_t == 2)
                    	{

                    		parent = item_t.getParent().getPath();
                			default_module = "功能项";
                    	}
                    	else if(code_t == 0)
                    	{
    	        			parent = item_t.getParent().getParentPath();
    	        			default_module = item_t.getParent().getName(request);
                    	}
            		}
            	}
            }
            else
            {
            	String temp_current_path = (String)session.getAttribute("menu_temp_path_" + subsystem);
        		if(temp_current_path != null && !temp_current_path.equals(""))
        		{
        			Item item_t = Framework.getInstance(subsystem).getItem(temp_current_path);
        			int code_t = item_t.getCode();
                	if(code_t == 2)
                	{

                		parent = item_t.getParent().getPath();
            			default_module = "功能项";
                	}
                	else if(code_t == 0)
                	{
	        			parent = item_t.getParent().getParentPath();
	        			default_module = item_t.getParent().getName(request);
                	}
        		}
            }
        }
        else if(fromRequest)//如果parent不为空，session的情况暂时不考虑
        {
        	String action_type = request.getParameter("action_type");
        	if(action_type.equals("up"))
    		{
        		String tt = request.getParameter(Framework.MENU_PATH);
        		default_module = Framework.getInstance(subsystem).getModule(tt).getParent().getName(request);
    		}
        	else
        	{
        		String temp = request.getParameter(Framework.MENU_PATH);
        		ModuleQueue mq = menuHelper.getModule(temp).getSubModules();
        		if(mq != null && mq.size() > 0)
        			default_module = mq.getModule(0).getName(request);
        		else {
        			ItemQueue iq = menuHelper.getModule(temp).getItems();
        			if(iq.size() > 0)
        			{
        				default_module = "功能项";
        			}
        		}
        		
        	}
        	
        }
        
        if(parent != null)
        {
        	if(parent.equals(Framework.getSuperMenu(subsystem)))
        		this.super_parent = null;
        	else
        	{
        		this.super_parent = Framework.getMenu(parent).getParent().getParentPath();
        	}        		
        }
    }

    public void init(PageContext pageContext)
    {
        this.init(pageContext,null);
    }



    public void buildColumn()
    {
        this.evaluateLeftheader(out);
        if(this.parent == null || parent.equals(Framework.getSuperMenu(subsystem)))
        {

            
            if(ConfigManager.getInstance().securityEnabled())
            {  
                evaluateColumn(menuHelper.getModules(),out);
                evaluateItemColumn(menuHelper.getItems(),out);
            }
            else
            {
                ModuleQueue modules = framework.getModules();
                ItemQueue items = framework.getItems();
                this.evaluateColumn(modules, out);
                this.evaluateItemColumn(items,out);
            }
        }
        else
        {
            Object object = Framework.getMenu(parent);
            if(object instanceof Module)
            {
                Module module = (Module)object;
                
                //super_parent = module.getParent().getParentPath();
                this.fromRequest = true;
                if(parent != null && !parent.equals(Framework.getSuperMenu(subsystem)))
                	this.evaluateNavigator(parent,out);
                if(ConfigManager.getInstance().securityEnabled())
                {
                    this.evaluateColumn(menuHelper.getSubModules(parent), out);
                    this.evaluateItemColumn(menuHelper.getSubItems(parent), out);
                }
                else
                {
                    ModuleQueue modules = module.getSubModules();
                    ItemQueue items = module.getItems();
                    this.evaluateColumn(modules, out);
                    this.evaluateItemColumn(items, out);
                }
                //this.evaluateNavigator(super_parent,out);

            }
            else if(object instanceof Item)
            {

            }
        }
        this.evaluateBottom(out);
        out.flush();
    }



    public void evaluateLeftheader(PrintWriter out)
    {
        Template template = null;
        VelocityContext context = null;
        try
        {
            template = VelocityUtil.getTemplate(LEFT_HEAD);
            context = new VelocityContext();
            context.put("menu_title",framework.getDescription(request));
            template.merge(context, out);
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void evaluateBottom(PrintWriter out)
    {
        Template template = null;
        VelocityContext context = null;
        try
        {
           template = VelocityUtil.getTemplate(LEFT_TAILER);
           context = new VelocityContext();
           if(this.getSelectImage() == null)
               context.put("selectImg","");
           else
               context.put("selectImg","selectImg=img_" + this.getSelectImage() + ";");
           if(this.default_module != null)
           {
        	   context.put("default_module",default_module);
           }
           else
           {
        	   context.put("default_module","");
           }
           
//    	   if(fromRequest)
//    	   {
//    		   Object object = Framework.getInstance(subsystem).getMenu(parent);
//    		   if(object instanceof Module)
//    		   {
//    			   Module temp  = (Module)object;
//    			   if(temp.getSubModules().size() > 0)
//    				   context.put("default_module",temp.getSubModules().getModule(0).getName());
//    			   else if(temp.getItems().size() > 0)
//    				   context.put("default_module","功能项");
//    		   }
//    		   else if(object instanceof Root || parent.equals(Framework.SUPER_MENU))
//    		   {
//    			   if(Framework.getInstance(subsystem).getModules().size() > 0)
//    				   context.put("default_module",Framework.getInstance(subsystem).getModules().getModule(0).getName());
//    			   else if(Framework.getInstance(subsystem).getItems().size() > 0)
//    				   context.put("default_module","功能项");
//    		   }
//    		   else
//    			   context.put("default_module","");
//    			   
//    	   }
//    	   else
//    	   {
//        	   if(this.getCurrentModule() != null && !(getCurrentModule() instanceof Root))
//        	   {
//        		   if(!getCurrentModule().isMain())
//        			   context.put("default_module",getCurrentModule().getName());
//        		   else
//        			   context.put("default_module","");
//        	   }
//        	   else
//        	   {
//        		   context.put("default_module","功能项");
//        	   }
//    	   }
           template.merge(context, out);
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }

    }

    public void evaluateNavigator(String parent,PrintWriter out)
    {
        Template template = null;
        VelocityContext context = null;
        try
        {
            template = VelocityUtil.getTemplate(NAVIGATOR);
            context = new VelocityContext();
            context.put("title_dec", "返回上一级");
            context.put("onmouseup_img", "sysmanager/images/back.gif");
            context.put("onmousedown_img", "sysmanager/images/back_enabled.gif");
            context.put("onmouseover_img", "sysmanager/images/back_highlighted.gif");
            context.put("onmouseout_img", "sysmanager/images/back.gif");
            context.put("default_img", "sysmanager/images/back.gif");
            context.put("head_img", "sysmanager/images/back_highlighted.gif");

            context.put("navigator_link",
                        request.getContextPath() + "/" + Framework.OUTLOOKBAR_CONTAINER_URL + "?"
                        + Framework.MENU_PATH + "=" +
                        StringUtil.encode(parent, null) +
                        "&"
                        + Framework.MENU_TYPE + "=" +
                        Framework.LEFTSIDE_CONTAINER +
                        "&"
                        //+ PARENT_KEY + "=" + StringUtil.encode(Framework.getInstance(subsystem).getModule(parent).getParentPath(), null));
                        + PARENT_KEY + "=" + StringUtil.encode(super_parent, null) + "&action_type=up" );

            context.put("target", "");
            template.merge(context, out);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void evaluateColumn(ModuleQueue modules,PrintWriter out)
    {
        
        
        
        Template template = null;
        VelocityContext context = null;
        Module module = null;
        Module subModule = null;
        ItemQueue items = null;
        ModuleQueue submodules = null;
        Item item = null;
        //String currentPath = FrameworkServlet.getCurrentPath(session);

        for(int i = 0; modules != null &&  i < modules.size(); i ++)
        {
            module = modules.getModule(i);
            //如果模块暂未使用，不显示该模块
            if(!module.isUsed())
                continue;
            try
            {
                template = VelocityUtil.getTemplate(COLUMN_HEADER);
                context = new VelocityContext();
                context.put("fieldset_tile", module.getName(request));
                if(module.getHeadimg(request) != null && showMode.equals("1"))
                {
                	context.put("head_img",module.getHeadimg(request));
                	needTitleImg = true;
                }
                else
                	context.put("head_img","");
                	
                
                //context.put("fieldset_id", module.getId());
                template.merge(context, out);
        
                submodules = module.getSubModules();
        
                items = module.getItems();
                for (int j = 0; items != null && j < items.size(); j++) {
                    item = (Item) items.getItem(j);
                    //如果子栏目未使用，不显示该模块
                    if(!item.isUsed())
                        continue;

                    String workspaceContent = null;
                    if(!item.hasWorkspaceContentVariables()){
                    	workspaceContent = item.getWorkspaceContent();
                    }else{
                    	workspaceContent = Framework.getWorkspaceContent(item, control);
                    }
                    
                    template = VelocityUtil.getTemplate(COLUMN);
                    context = new VelocityContext();
                    context.put("title_dec", item.getName(request));
                    context.put("onmouseup_img", item.getMouseupimg(request));
                    context.put("onmousedown_img", item.getMouseclickimg(request));
                    context.put("onmouseover_img", item.getMouseoverimg(request));
                    context.put("onmouseout_img", item.getMouseoutimg(request));
                    context.put("head_img", item.getHeadimg(request));
                    context.put("id", getSelectImage(item.getPath()));
                    context.put("showpage",new Boolean(item.isShowpage()));
                    context.put("page_url",workspaceContent);
                    if(showMode.equals("0"))
                    	context.put("br", "<br>");
                    else
                    	context.put("br", "&nbsp;&nbsp;");

                    if (currentPath != null && currentPath.equals(item.getPath()))
                    {
                        context.put("default_img", item.getMouseupimg(request));
                        context.put("class", "select");
                        flag = true;
             
                    }
                    else
                    {
                        context.put("default_img", item.getMouseoutimg(request));
                        context.put("class", "normal");
                    }



                    context.put("link_url",
                                request.getContextPath() + "/" + Framework.CONTENT_CONTAINER_URL + "?"
                                + Framework.MENU_PATH + "=" +
                                StringUtil.encode(item.getPath(), null) +
                                "&"
                                + Framework.MENU_TYPE + "=" + Framework.CONTENT_CONTAINER
                                +"&ancestor=2");
                    if(item.getTarget() != null && !item.getTarget().equals(""))
                    	context.put("target",item.getTarget());
                    else
                    	context.put("target",framework.getGlobal_target());


                    template.merge(context,out);
                }

                for(int j = 0; submodules != null && j < submodules.size(); j ++)
                {

                    subModule = submodules.getModule(j);
                    //如果子模块未使用，不显示该模块
                    if(!subModule.isUsed())
                        continue;
                    template = VelocityUtil.getTemplate(COLUMN);
                    context = new VelocityContext();
//                    context.put("title_dec", subModule.getName()  + ",点击进入");
                    context.put("title_dec", subModule.getName(request));
                    context.put("onmouseup_img", subModule.getMouseupimg(request));
                    context.put("onmousedown_img", subModule.getMouseclickimg(request));
                    context.put("onmouseover_img", subModule.getMouseoverimg(request));
                    context.put("onmouseout_img", subModule.getMouseoutimg(request));
                    context.put("head_img", subModule.getHeadimg(request));
                    if(showMode.equals("0"))
                    	context.put("br", "<br>");
                    else
                    	context.put("br", "&nbsp;&nbsp;");
                    context.put("id", getSelectImage(subModule.getPath()));

                    if (currentPath != null && currentPath.equals(subModule.getPath()))
                    {
                        context.put("default_img", subModule.getMouseupimg(request));
                        context.put("class", "select");
                        flag = true;
 
                    }
                    else
                    {
                        context.put("default_img", subModule.getMouseoutimg(request));
                        context.put("class", "normal");
                    }
                    context.put("link_url",
                                request.getContextPath() + "/" + Framework.OUTLOOKBAR_CONTAINER_URL + "?"
                                + Framework.MENU_PATH + "=" +
                                StringUtil.encode(subModule.getPath(), null) +
                                "&"
                                + Framework.MENU_TYPE + "=" + Framework.LEFTSIDE_CONTAINER +
                                "&"
                                + PARENT_KEY + "=" + StringUtil.encode(subModule.getPath(), null) + "&action_type=down");
                    context.put("target","");
                    template.merge(context,out);
                }

                template = VelocityUtil.getTemplate(COLUMN_TAILER);
                template.merge(new VelocityContext(),out);

            } catch (MethodInvocationException ex) {
                ex.printStackTrace();
            } catch (ParseErrorException ex) {
                ex.printStackTrace();
            } catch (ResourceNotFoundException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }        
    }
    
    /**
     * 计算功能项
     * @param items
     * @param out
     */
    public void evaluateItemColumn(ItemQueue items,PrintWriter out)
    {
        Template template = null;
        VelocityContext context = null;
        Item item = null;
        if(!(items.size() > 0))
            return ;
 
        try
        {
            template = VelocityUtil.getTemplate(COLUMN_HEADER);
            context = new VelocityContext();
            context.put("fieldset_tile", "功能项");
            
            if(needTitleImg && this.showMode.equals("1"))
            	context.put("head_img","sysmanager/bbs/images/exec1.gif");
            else
            	context.put("head_img","");
            
            template.merge(context, out);

            for (int i = 0; items != null && i < items.size(); i++) {
                item = (Item) items.getItem(i);
                if(!item.isUsed())
                    continue;
                template = VelocityUtil.getTemplate(COLUMN);
                context = new VelocityContext();
                
                String workspaceContent = null;
                if(!item.hasWorkspaceContentVariables()){
                	workspaceContent = item.getWorkspaceContent();
                }else{
                	workspaceContent = Framework.getWorkspaceContent(item, control);
                }
                
                context.put("title_dec", item.getName(request));
                context.put("onmouseup_img", item.getMouseupimg(request));
                context.put("onmousedown_img", item.getMouseclickimg(request));
                context.put("onmouseover_img", item.getMouseoverimg(request));
                context.put("onmouseout_img", item.getMouseoutimg(request));
                context.put("head_img", item.getHeadimg(request));
                context.put("showpage",new Boolean(item.isShowpage()));
                context.put("page_url",workspaceContent);
                
                if(showMode.equals("0"))
                	context.put("br", "<br>");
                else
                	context.put("br", "&nbsp;&nbsp;");
                context.put("id", getSelectImage(item.getPath()));
                if (currentPath != null && currentPath.equals(item.getPath())) {
                    context.put("default_img", item.getMouseupimg(request));
                    context.put("class", "select");
                    flag = true;


                } else {
                    context.put("class", "normal");
                    context.put("default_img", item.getMouseoutimg(request));
                }

                context.put("link_url",
                            request.getContextPath() + "/" + Framework.CONTENT_CONTAINER_URL + "?"
                            + Framework.MENU_PATH + "=" +
                            StringUtil.encode(item.getPath(), null) + "&"
                            + Framework.MENU_TYPE + "=" +
                            Framework.CONTENT_CONTAINER
                            + "&ancestor=1"
                            );
                if(item.getTarget() != null && !item.getTarget().equals(""))
                	context.put("target",item.getTarget());
                else
                	context.put("target",framework.getGlobal_target());
                template.merge(context, out);
            }
            template = VelocityUtil.getTemplate(COLUMN_TAILER);
            template.merge(new VelocityContext(), out);
        } catch (MethodInvocationException ex) {
            ex.printStackTrace();
        } catch (ParseErrorException ex) {
            ex.printStackTrace();
        } catch (ResourceNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String getSelectImage()
    {
        return flag?selectImage:null;
    }

}
