<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.framework.Framework,com.frameworkset.platform.framework.FrameworkServlet,com.frameworkset.platform.security.*"%>
<%
    String menu_path = request.getParameter("menu_path");
    AccessControl accesscontroler = AccessControl.getInstance();
  	accesscontroler.checkAccess(request, response);
    String subsystem = FrameworkServlet.getSubSystem(request,response,accesscontroler.getUserAccount());
    Framework framework = Framework.getInstance(subsystem);
%>

<html>
<head>

    
<script language="javascript" >
  //var rootContainer;
  var perspective_mainContainer;
  var perspective_contentContainer;
  var navigatorContainer;
  var actionsContainer;
  var propertiesContainer;
  var statusContainer;	
  
  var perspectiveWorkArea;
  var perspectiveToolbar;
  var perspectiveContent;
  var perspectiveMain;

  var baseNavigatorToolbar;
  var baseNavigatorContent;
  
  var basePropertiesToolbar;
  var basePropertiesContent;
  
  var baseStatusToolbar;
  var baseStatusContent;  
  
  

  var framework_frameset  ;
  var perspective_main_frameset;
  
  var perspective_content_frameset;
  var navigator_container_frameset;  
  var actions_container_frameset;
  var properties_container_frameset;  
  var status_container_frameset; 
  //getFrameworkContain();
 
  
  function getPerspectiveContainer()
  {  	
  
  		if(perspective_contentContainer == null)
  			return perspective_contentContainer = getContainer("perspective_content");
  		else
  			return perspective_contentContainer;
  }
  
  function getNavigatorContainer()
  {  	
  
  		if(navigatorContainer == null)
	  		return navigatorContainer = getContainer("base_navigator_container");
	  	else
	  		return navigatorContainer;
  }
  
  function getActionsContainer()
  {  	

  		if(actionsContainer == null)
	  		return actionsContainer = getContainer("base_actions_container");
	  	else
	  		return actionsContainer;
  }
  
  function getPropertiesContainer()
  {  	

  		if(propertiesContainer == null)
  		{
  			propertiesContainer = getContainer("base_properties_container");
	  	}
	  	//alert(propertiesContainer.name)
	  	//alert("properties_container_frameset:"+properties_container_frameset);
	  	return propertiesContainer ;
  }
  
  function getStatusContainer()
  {  	  		
		if(statusContainer == null)
		{
  			return statusContainer = getContainer("base_status_container");
	  	}
	  	else
	  		return statusContainer;
	  	
  }
  
  function getPerspectiveMainContainer()
  {
  
  		if(perspective_mainContainer == null)
  		{
	  		return perspective_mainContainer = getContainer("perspective_main");
	  	}
	  	else
	  		return perspective_mainContainer;
  	
  }
   
  
  function getContainer(container_name)
  {
  		//frameSet容器
  		var containerok = false;
  		
  		//alert("container_name:"+container_name);
  		if(self.name == container_name)
  		{
  			return self;
  		}
  		if(self == top)
  			return null;
  		var root_container_ = parent;
  		
  		if(parent == top)
  			return null;
  		var curwindow = self;
  		var ret = null;
		while(!containerok)
	  	{
			
	  		var temps = root_container_.frames;
	  		
			for(var i = 0; temps && i < temps.length; i ++)
	  		{		
	  			
	  			
	  	 		var temp_frameset = temps(i);
	  	 		
	  	 		
	  	 		if(temp_frameset.name == container_name)
	  	 		{
	  	 			containerok = true;
	  	 			return temp_frameset;
	  	 		
	  	 		}
	  	 		
	  	 		
	  	 		var sub_frameset = temp_frameset.frames;
	  	 		for(var j = 0; sub_frameset && j < sub_frameset.length; j ++)
	  	 		{
	  	 			if(sub_frameset(j).name == curwindow.name)
	  	 				continue;
	  	 			ret = searchContainer(sub_frameset(j),container_name)
	  	 			if(ret)
	  	 			{
	  	 				containerok = true;
	  	 				return ret;
	  	 			}	  	 			
	  	 		}
	  	 	}
	  	 	if(root_container_ == top)
	  	 	{
	  	 		containerok = true;
	  	 		return null;
	  	 	}
	  	 	curwindow = root_container_;	  	 	
	  	 	root_container_ = root_container_.parent;  	 		 	
	  	}
  	  
  }
  
  /**
  * 递归查找当前窗口下的所有frameset已查找相关的container
  */
  function searchContainer(root_container_,container_name)
  {		
  		if(root_container_.name == "perspective_workarea"
  			||
  			root_container_.name == "perspective_toolbar"
  			||
  			root_container_.name == "base_navigator_toolbar"
  			||
  			root_container_.name == "base_navigator_content"
  			||
  			root_container_.name == "base_properties_toolbar"
  			||
  			root_container_.name == "base_properties_content"
  			||
  			root_container_.name == "base_status_toolbar"
  			||
  			root_container_.name == "base_status_content"
  			)
  			return null;
  		
  		
  	
  		
  		var t_frames = root_container_.frames;
  		
  		
  		var ret = null;
  		if(!t_frames)
  			return null;
		
		var i = 0;
		
		for(; t_frames && i < t_frames.length; i ++)
 		{		
 			
 			
 	 		var temp_frameset = t_frames(i);
 	 		
 	 		
 	 		if(temp_frameset.name == container_name)
 	 		{
 	 		
 	 			return temp_frameset;
 	 		
 	 		}
 	 		
 	 		
 	 		ret = searchContainer(temp_frameset,container_name);
 	 		if(ret)
 	 			return ret; 	 			 		
 	 	}
 	 	return ret;
  }
  //getFrameworkContainerFrameset();
    function getFrameworkContainerFrameset()
  {  	
  
  		if(framework_frameset == null)
	  		return framework_frameset = getContainerFrameSet("framework_container");
	  	else
	  		return framework_frameset;
  }
  //getPerspectiveContainerFrameset();
  function getPerspectiveContainerFrameset()
  {  	
  		
  		if(perspective_content_frameset == null)
  			return perspective_content_frameset = getContainerFrameSet("perspective_content_container");
  		else
  			return perspective_content_frameset;
  }
  //getNavigatorContainerFrameset();
  function getNavigatorContainerFrameset()
  {  	
  
  		if(navigator_container_frameset == null)
	  		return navigator_container_frameset = getContainerFrameSet("navigator_container");
	  	else
	  		return navigator_container_frameset;
  }
  //getActionsContainerFrameset();
  function getActionsContainerFrameset()
  {  	
	
  		if(actions_container_frameset == null)
	  		return actions_container_frameset = getContainerFrameSet("actions_container");
	  	else
	  		return actions_container_frameset;
  }
  //getPropertiesContainerFrameset();
  function getPropertiesContainerFrameset()
  {  	
  	
    
  		if(properties_container_frameset == null)
  		{
  			properties_container_frameset = getContainerFrameSet("properties_container");
	  		
	  	}
	  	//alert("properties_container_frameset:"+properties_container_frameset);
	  	return properties_container_frameset ;
  }
  //getStatusContainerFrameset();
  function getStatusContainerFrameset()
  {  	  		
  
		if(status_container_frameset == null)
		{
  			return status_container_frameset = getContainerFrameSet("status_container");
	  	}
	  	else
	  		return status_container_frameset;
	  	
  }
  //getPerspectiveMainContainerFrameset();
  function getPerspectiveMainContainerFrameset()
  {
  
  		if(perspective_mainContainer == null)
  		{
	  		return perspective_mainContainer = getContainerFrameSet("perspective_main_container");
	  	}
	  	else
	  		return perspective_mainContainer;
  	
  }
  /**
   * 获取页面名称为container_name的frameSet对象
   */
  function getContainerFrameSet(container_name)
  {
  		var container = null;
  		//alert(container_name);
  		
  		var root = parent;
  		var curwindow = self;
  		//alert(curwindow.name);
  		while(!container)
	  	{
	  		var temps = root.document.getElementsByTagName("frameset");
	  		for(var i = 0; temps && i < temps.length; i ++)
	  		{		
	  			
	  			//alert("length:" + i);
	  	 		var temp_frameset = temps.item(i);
	  	 			  	 		//alert(temp_frameset.name);
	  	 		//alert("temp_frameset.name == container_name:" + (temp_frameset.name == container_name));
	  	 		if(temp_frameset.name == container_name)
	  	 		{
	  	 			container = temp_frameset;
	  	 			//alert("container:"+container.frames);
	  	 		}
	  	 		
	  	 	}
	  	 	if(container)
	  	 		return container;
	  	 	if(root == top)
	  	 		return container;
			root = root.parent;  	 		  	 			  	 	
	  	}
  	  
  }

  //getPropertiesToolbar();
   function getPropertiesToolbar()
  {
       if(basePropertiesToolbar)
            return basePropertiesToolbar;
       var temp = getPropertiesContainer();
       if(temp)
	       basePropertiesToolbar = temp.frames["base_properties_toolbar"];              
       //alert("basePropertiesToolbar:"+basePropertiesToolbar);
       //alert("basePropertiesToolbar.name:"+basePropertiesToolbar.name);
       return basePropertiesToolbar;
  }
  //getPropertiesContent();	
  function getPropertiesContent()
  {
       if(basePropertiesContent)
            return basePropertiesContent;       
       
       var temp = getPropertiesContainer();    
       
       if(temp)
	       basePropertiesContent = temp.frames["base_properties_content"];       	     
       return basePropertiesContent;
  }
  //getNavigatorToolbar();	
  function getNavigatorToolbar()
  {
       if(baseNavigatorToolbar)
            return baseNavigatorToolbar;
       var temp = getNavigatorContainer();
       if(temp)
	       baseNavigatorToolbar = temp.frames["base_navigator_toolbar"];
       return baseNavigatorToolbar;
  }
  //getNavigatorContent();	
  function getNavigatorContent()
  {
       if(baseNavigatorContent)
            return baseNavigatorContent;
       //init();
       var temp = getNavigatorContainer();
       //alert("temp.name:" + temp.name);
       if(temp)
	       baseNavigatorContent = temp.frames["base_navigator_content"];
       //alert("baseNavigatorContent:" + baseNavigatorContent);
       return baseNavigatorContent;
  }
  //getStatusToolbar();
  function getStatusToolbar()
  {
       if(baseStatusToolbar)
            return baseStatusToolbar;
       var temp = getStatusContainer();
       if(temp)
	       baseStatusToolbar = temp.frames["base_status_toolbar"];
       return baseStatusToolbar;
  }
  //getStatusContent();	
  function getStatusContent()
  {
       if(baseStatusContent)
            return baseStatusContent;
       //init();
       var temp = getStatusContainer();
       if(temp)
	       baseStatusContent = temp.frames["base_status_content"];
       return baseStatusContent;
  }

  

  

  function getHiddenCols()
  {
        var oldCols = getOldLeftCols();
        var cols = oldCols.split(",");
        return "0," + cols[1] + "," + cols[2];
  }
  


  /**
   * 
   * 0:
   * 1:
   */
  var flag = 0;

  /**
   * 
   *
   
   * 
   * framework
   *     1-left
   *     2-navigator
   *     3-workspace
   *     4-status
   * switchType
   *         1：persepective_content
   *         2：perspective_main
   */
  function switchFrameworks(framework,swtichType,eventSource)
  {

      if(framework == 1) //left
      {   
          
          if(flag == 0) //隐藏
          {
          	  if(getPerspectiveMainContainerFrameset())
	              getPerspectiveMainContainerFrameset().setAttribute("cols",getHiddenCols());

              flag = 1
              
              
              {
                  document.body.title = document.body.showtitle;
                  document.body.style.background = "transparent url(images/arrow.jpg) 100% 0 no-repeat";
              }


          }
          else //还原
          {
          	  if(getPerspectiveMainContainerFrameset())
	              getPerspectiveMainContainerFrameset().setAttribute("cols",getOldLeftCols());

              flag = 0
              
              
              {
                  
                  document.body.title = document.body.hiddentitle;
                  document.body.style.background = "transparent url(images/backarrow.jpg) 100% 0 no-repeat";
              }
          }
      }
  }

  function getOldLeftCols()
  {
      return "<%=framework.getFramesetAttribute(menu_path).getFRAMEWORKSET_COLS()%>";
  }

  
</script>

</head>
<body oncontextmenu="return true;" showtitle="显示" hiddentitle="隐藏" scroll="no" name="doubleclickcolumn" onclick="switchFrameworks(<%=Framework.SWITCH_LEFT%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);" height="100%" width="100%" style="background:transparent url(images/backarrow.jpg) 100% 0 repeat;bgColor:"#DCDCDC;" title="隐藏" >
</body>
</html>
