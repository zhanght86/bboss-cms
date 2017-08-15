package com.frameworkset.platform.menu;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
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
public class MainColumn implements Serializable{
    private static final Logger log = LoggerFactory.getLogger(MainColumn.class);

    private String parent;
    private PageContext pageContext;
    private HttpServletRequest request;
    //private HttpServletResponse response;
    private HttpSession session;
    private boolean flag = false;
    private PrintWriter out;
    private static final String COLUMN_HEADER = "mainmenu/column_head.vm";
    private static final String COLUMN_TAILER = "mainmenu/column_tailer.vm";
    private static final String COLUMN = "mainmenu/column.vm";
    private static final String NAVIGATOR = "mainmenu/navigator.vm";
    private static final String PARENT_KEY = "parent_key";
    private static Map imageIDs = new HashMap();

    private String super_parent = "";
    private String selectImage;
    private String currentPath;
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
    private AccessControl control = AccessControl.getInstance();    
    
    private MenuHelper menuHelper;
    private String subsystem;

    /**
     * 控制栏目层级
     */
    private int level = -1;

    /**
     * 模板存放相对路径，针对不同的模板生成不同样式和风格的栏目菜单
     */
    private String templatePath = "";

    /**
     * 栏目菜单开始节点
     */
    private String rootID = "";





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
        //System.out.println("selectImage:>>>>>>>>>>" + path);
        //return path;
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
        subsystem = FrameworkServlet.getSubSystem(request,(HttpServletResponse)pageContext.getResponse(),control.getUserAccount());
        this.super_parent = Framework.getSuperMenu(subsystem);
        //response = (HttpServletResponse)pageContext.getResponse();
        menuHelper = new MenuHelper(subsystem,control);

        if(control != null)
            this.control = control;
        else
            control.checkAccess(pageContext,false);
        //out = pageContext.getOut();
		out = new PrintWriter(pageContext.getOut());

        this.parent = request.getParameter(PARENT_KEY);
        if(parent == null)
        {
            parent = (String) session.getAttribute("parent_key");
            session.removeAttribute("parent_key");

        }

        
        currentMenu  = FrameworkServlet.getCurrent(subsystem,session);
        if(currentMenu != null)
        {
            currentPath = currentMenu.getPath();
            //selectImage = currentMenu.getId();

            selectImage = getSelectImage(currentPath);
        }
        //从session中获取路径
        if(parent == null)
        {
            if(currentMenu != null)
            {
                String key  = currentMenu.getPath();
                String cookieKey = control.getUserAccount() + "@" + key;
                this.ancestor = (String)session.getAttribute(key);
                if(ancestor == null)
                {
                    Cookie[] cookies = request.getCookies();
                    Cookie cookie = null;
                    for(int i = 0; cookies != null && i < cookies.length; i ++ )
                    {
                        cookie = cookies[i];
                        if(cookie.getName().equals(cookieKey))
                        {
                           ancestor = cookie.getValue();

                           break;
                        }
                    }
                }

                if(ancestor == null || ancestor.equals("2"))
                {
                    parent = currentMenu.getParent().getParentPath();
                }

                else if(ancestor.equals("1"))
                {
                    parent = currentMenu.getParentPath();
                }
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
                ModuleQueue modules = Framework.getInstance().getModules();
                ItemQueue items = Framework.getInstance().getItems();
                this.evaluateColumn(modules, out);
                this.evaluateItemColumn(items,out);
            }
        }
        else
        {
            Object object = Framework.getInstance().getMenu(parent);
            if(object instanceof Module)
            {
                Module module = (Module)object;
                
                super_parent = module.getParent().getParentPath();
                this.evaluateNavigator(super_parent,out);
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
                this.evaluateNavigator(super_parent,out);

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
            template = VelocityUtil.getTemplate("mainmenu/left_head.vm");
            context = new VelocityContext();
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
           template = VelocityUtil.getTemplate("mainmenu/left_tailer.vm");
           context = new VelocityContext();
           if(this.getSelectImage() == null)
               context.put("selectImg","");
           else
               context.put("selectImg","selectImg=img_" + this.getSelectImage() + ";");
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

            context.put("navigator_link",
                        request.getContextPath() + "/" + Framework.LeftSIDE_CONTAINER_URL + "?"
                        + Framework.MENU_PATH + "=" +
                        StringUtil.encode(parent, null) +
                        "&"
                        + Framework.MENU_TYPE + "=" +
                        Framework.LEFTSIDE_CONTAINER +
                        "&"
                        //+ PARENT_KEY + "=" + StringUtil.encode(Framework.getInstance().getModule(parent).getParentPath(), null));
                        + PARENT_KEY + "=" + StringUtil.encode(super_parent, null));

            context.put("target", "perspective_toolbar");
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
                context.put("fieldset_tile", module.getDescription());
                //context.put("fieldset_id", module.getId());
                template.merge(context, out);
        
                submodules = module.getSubModules();
        
                items = module.getItems();
                for (int j = 0; items != null && j < items.size(); j++) {
                    item = (Item) items.getItem(j);
                    //如果子栏目未使用，不显示该模块
                    if(!item.isUsed())
                        continue;

                    template = VelocityUtil.getTemplate(COLUMN);
                    context = new VelocityContext();
                    context.put("title_dec", item.getName(request));
                    context.put("onmouseup_img", item.getMouseupimg(request));
                    context.put("onmousedown_img", item.getMouseclickimg(request));
                    context.put("onmouseover_img", item.getMouseoverimg(request));
                    context.put("onmouseout_img", item.getMouseoutimg(request));
                    //context.put("id", item.getId());
                    context.put("id", getSelectImage(item.getPath()));

                    if (currentPath != null && currentPath.equals(item.getPath()))
                    {
                        context.put("default_img", item.getMouseupimg(request));
                        context.put("class", "select");
                        flag = true;
                        //selectImage = item.getId();
                    }
                    else
                    {
                        context.put("default_img", item.getMouseoutimg(request));
                        context.put("class", "normal");
                    }

//                    context.put("link_url",
//                                "/wapsite/actions/ShowPerspectiveActionJSP.jsp?persperctive=0&"
//                                + Framework.MENU_PATH + "=" +
//                                StringUtil.encode(item.getPath(), null) +
//                                "&"
//                                + Framework.MENU_TYPE + "=" + Framework.CONTENT_CONTAINER);

                    context.put("link_url",
                                request.getContextPath() + "/" + Framework.CONTENT_CONTAINER_URL + "?"
                                + Framework.MENU_PATH + "=" +
                                StringUtil.encode(item.getPath(), null) +
                                "&"
                                + Framework.MENU_TYPE + "=" + Framework.CONTENT_CONTAINER
                                +"&ancestor=2");
                    context.put("target","perspective_content");

//                    else if(type == Framework.LEFTSIDE_CONTAINER)
//                    {
//                        context.put("link_url",
//                                    item.getLeft() + "?"
//                                    + Framework.MENU_PATH + "=" +
//                                    StringUtil.encode(item.getPath(), null) +
//                                    "&"
//                                    + Framework.MENU_TYPE + "=" + type);
//                        context.put("target","perspective_toolbar");
//
//                    }
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
                    context.put("title_dec", subModule.getName(request)  + ",点击进入");
                    context.put("onmouseup_img", subModule.getMouseupimg(request));
                    context.put("onmousedown_img", subModule.getMouseclickimg(request));
                    context.put("onmouseover_img", subModule.getMouseoverimg(request));
                    context.put("onmouseout_img", subModule.getMouseoutimg(request));
                    //context.put("id", subModule.getId());
                    context.put("id", getSelectImage(subModule.getPath()));

                    if (currentPath != null && currentPath.equals(subModule.getPath()))
                    {
                        context.put("default_img", subModule.getMouseupimg(request));
                        context.put("class", "select");
                        flag = true;
                        //selectImage = subModule.getId();
                    }
                    else
                    {
                        context.put("default_img", subModule.getMouseoutimg(request));
                        context.put("class", "normal");
                    }
                    context.put("link_url",
                                request.getContextPath() + "/" + Framework.LeftSIDE_CONTAINER_URL + "?"
                                + Framework.MENU_PATH + "=" +
                                StringUtil.encode(subModule.getPath(), null) +
                                "&"
                                + Framework.MENU_TYPE + "=" + Framework.LEFTSIDE_CONTAINER +
                                "&"
                                + PARENT_KEY + "=" + StringUtil.encode(subModule.getPath(), null));
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

    public void evaluateItemColumn(ItemQueue items,PrintWriter out)
    {
        Template template = null;
        VelocityContext context = null;
        Item item = null;
        if(!(items.size() > 0))
            return ;
        //String currentPath = FrameworkServlet.getCurrentPath(session);
        try
        {
            template = VelocityUtil.getTemplate(COLUMN_HEADER);
            context = new VelocityContext();
            context.put("fieldset_tile", "功能项");
            //context.put("fieldset_id", "workitem");
            template.merge(context, out);

            for (int i = 0; items != null && i < items.size(); i++) {
                item = (Item) items.getItem(i);
                if(!item.isUsed())
                    continue;
                template = VelocityUtil.getTemplate(COLUMN);
                context = new VelocityContext();
                context.put("title_dec", item.getName(request));
                context.put("onmouseup_img", item.getMouseupimg(request));
                context.put("onmousedown_img", item.getMouseclickimg(request));
                context.put("onmouseover_img", item.getMouseoverimg(request));
                context.put("onmouseout_img", item.getMouseoutimg(request));
                //context.put("id", item.getId());
                context.put("id", getSelectImage(item.getPath()));
                if (currentPath != null && currentPath.equals(item.getPath())) {
                    context.put("default_img", item.getMouseupimg(request));
                    context.put("class", "select");
                    flag = true;
                    //selectImage = item.getId();

                } else {
                    context.put("class", "normal");
                    context.put("default_img", item.getMouseoutimg(request));
                }
//                context.put("link_url",
//                            "/wapsite/actions/ShowPerspectiveActionJSP.jsp?persperctive=0&"
//                            + Framework.MENU_PATH + "=" +
//                            StringUtil.encode(item.getPath(), null) +
//                            "&"
//                            + Framework.MENU_TYPE + "=" + Framework.CONTENT_CONTAINER);

                context.put("link_url",
                            request.getContextPath() + "/" + Framework.CONTENT_CONTAINER_URL + "?"
                            + Framework.MENU_PATH + "=" +
                            StringUtil.encode(item.getPath(), null) + "&"
                            + Framework.MENU_TYPE + "=" +
                            Framework.CONTENT_CONTAINER
                            + "&ancestor=1"
                            );
                context.put("target", "perspective_content");
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
