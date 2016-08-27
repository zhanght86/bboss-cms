<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=accesscontroler.getCurrentSystemName() %></title>

    <link rel="stylesheet" type="text/css" href="resources/css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="css/desktop.css" />
    <style>
 	 	<pg:list requestKey="menulist">
 	 	.<pg:cell colName="id"/>-shortcut {
 	 	      width:48px;
    		height:48px;
		    background: url(${pageContext.request.contextPath}/<pg:cell colName="imageUrl"/>) center no-repeat;
		}
		
		.x-ie6 .<pg:cell colName="id"/>-shortcut {
		    background-image: none;
		    filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath()%>/<pg:cell colName="imageUrl"/>', sizingMethod='scale');
		}
 		
		</pg:list>
 	</style>

    <!-- GC -->
 	<!-- LIBS -->
 		
 
    
      
 



<script type="text/javascript" src="ext-core.js"></script>
<script type="text/javascript" src="js/ext-lang-zh_CN.js"></script>

    <script type="text/javascript">
       
     Ext.Loader.setConfig({enabled: true});
     
     Ext.Loader.setPath('MyDesktop', '${pageContext.request.contextPath}/desktop/');
    
      Ext.require([
       'MyDesktop.Settings'
    ]);
  </script>
    
  <script type="text/javascript" src="classes.js"></script>


    <script type="text/javascript"> 
        MyDesktop = new Ext.ux.desktop.App({
        
	init: function() {
        var me = this, desktopCfg;

        if (me.useQuickTips) {
            Ext.QuickTips.init();
        }

        me.modules = me.getModules();
        if (me.modules) {
            me.initModules(me.modules);
        }

        desktopCfg = me.getDesktopConfig();
        me.desktop = new Ext.ux.desktop.Desktop(desktopCfg);

        me.viewport = new Ext.container.Viewport({
            layout: 'fit',
            items: [ me.desktop ]
        });

        Ext.EventManager.on(window, 'beforeunload', me.onUnload, me);

        me.isReady = true;
        me.fireEvent('ready', me);
        this.initShortcut();
        loadRootItems();
        
    },

	getModules : function(){
	
		        return [];
	},

   getDesktopConfig: function () {
        var me = this, cfg = {
            app: me,
            taskbarConfig: me.getTaskbarConfig()
        };

        Ext.apply(cfg, me.desktopConfig);
        //return cfg;

        return Ext.apply(cfg, {
            //cls: 'ux-desktop-black',

            contextMenuItems: [
                { text: '背景设置', handler: me.onSettings, scope: me },
                 { text: '桌面快捷', handler: me.onShortcutSettings, scope: me }
            ],

            shortcuts: Ext.create('Ext.data.Store', {
                model: 'Ext.ux.desktop.ShortcutModel',
                data: [
                <pg:list requestKey="menulist">
                	<%if(dataSet.getRowid() > 0){%>
                	,
                	<%}%>
                	{ name: '<pg:cell colName="name"/>', iconCls: '<pg:cell colName="id"/>-shortcut',onclick:"ShortCut('<pg:cell colName="id"/>','<pg:cell colName="name"/>','<pg:cell colName="pathU"/>','<pg:cell colName="desktop_width"/>','<pg:cell colName="desktop_height"/>');"}
                
                </pg:list>
                ]
            }),
            <pg:beaninfo  requestKey="blackGround">
            wallpaper: 'wallpapers/<pg:cell colName="filename"/>',
            
            //wallpaper: 'wallpapers/Blue-Sencha.jpg',
            //wallpaperStretch: false
        		
            wallpaperStretch: <pg:cell colName="fit"/>
            </pg:beaninfo>
        });
    	},
    
   
    
    // config for the start menu
    getStartConfig : function() {
       
         var me = this, cfg = {
            app: me,
            menu: []
        };

        Ext.apply(cfg, me.startConfig);

        Ext.each(me.modules, function (module) {
            if (module.launcher) {
                cfg.menu.push(module.launcher);
            }
        });

        return Ext.apply(cfg, {
            title: '<%=accesscontroler.getUserAccount()%>(<%=accesscontroler.getUserName()%>)',
            iconCls: 'user',
            height: 300,
            toolConfig: {
                width: 100,
                items: [
                    {
                        text:'背景设置',
                        iconCls:'settings',
                        handler: me.onSettings,
                        scope: me
                    },
                    '-',
                    {
                        text:'桌面快捷',
                        iconCls:'settings',
                        handler: me.onShortcutSettings,
                        scope: me
                    },
                    '-',
                    {
                        text:'登出',
                        iconCls:'logout',
                        handler: me.onLogout,
                        scope: me
                    }
                ]
            }
        });
    },

    getTaskbarConfig: function () {
        var me = this, cfg = {
            app: me,
            startConfig: me.getStartConfig()
        };

        Ext.apply(cfg, me.taskbarConfig);

        return Ext.apply(cfg, {
            quickStart: [
               <pg:list requestKey="tackbarlist">
                	<%if(dataSet.getRowid() > 0){%>
                	,
                	<%}
                	//itemname:c.itemname,itemid:c.itemid,itempath:c.itempath
                	
                	%>
                	{ name: '<pg:cell colName="name"/>', iconCls: 'accordion',handler:me.onTaskbarItemClick
                	,itemid:'<pg:cell colName="id"/>',itemname:'<pg:cell colName="name"/>',itempath:'<pg:cell colName="pathU"/>',
                	desktop_width:'<pg:cell colName="desktop_width"/>',desktop_height:'<pg:cell colName="desktop_height"/>'
                	}
                
                </pg:list>
            ],
            trayItems: [
                { xtype: 'trayclock', flex: 1 }
            ]
        });
    },
    
    onTaskbarItemClick:function(item)
    {
    	ShortCut(item.itemid,item.itemname,item.itempath,item.desktop_width,item.desktop_height);
    //alert(item.itemid);
    },

    onLogout: function () {
        Ext.Msg.confirm('登出-<%=accesscontroler.getUserAccount()%>(<%=accesscontroler.getUserName()%>)', '确定要退出系统吗？',function(btn){
       		if(btn == 'yes')
       		   		window.location.href='<%=request.getContextPath()%>/logout.jsp';
    
       	});
      
    },

    onSettings: function () {
      
      
       var dlg =  new MyDesktop.Settings({desktop: this.desktop});
       dlg.show();
       
    }
    ,
    onShortcutSettings:function() {
    	ShortCutSet('shortcutsetting','快捷设置','<%=request.getContextPath()%>/menu/setting.page','777','500');
    },
    initShortcut : function() {
				var btnHeight = 60;
				var btnWidth = 60;
				var btnPadding = 20;
				var btnPaddingX=5;
				var col = null;
				var row = null;
				var wraped = false;
				var bottom;
				var numberofIterms = 0;
				var bodyHeight = Ext.getBody().getHeight();
				var colNum=parseInt((bodyHeight-2*btnPadding)/(btnHeight+btnPadding));
				
				function initColRow() {
					col = {
						index : 1,
						x : btnPaddingX
					};
					row = {
						index : 1,
						y : 20
					};
				}

				initColRow();

				function isOverflow(value, bottom) {
					//alert("numberofIterms:"+numberofIterms);
					var overflow = wraped ? value >= colNum || bodyHeight < bottom:value > colNum || bodyHeight < bottom;
					//alert("value:"+value);
					if (overflow) {
					//	alert(" bottom:"+(bodyHeight < bottom));
						wraped  = true;
						return true;
					}
					return false;
				}

				this.setXY = function(item) {
					numberofIterms += 1;
					bottom = row.y + btnHeight;
					overflow = isOverflow(numberofIterms, bottom);
					if (overflow && bottom > (btnHeight + btnPadding)) {
						numberofIterms = 0;
						col = {
							index : col.index++,
							x : col.x + btnWidth + btnPaddingX
						};
						row = {
							index : 1,
							y : btnPadding
						};
					}
					Ext.fly(item).setXY([col.x, row.y]);
					row.index++;
					row.y = row.y + btnHeight + btnPadding;
				};

				this.handleUpdate = function() {
					initColRow();
					var items = Ext.query(".ux-desktop-shortcut");
					for (var i = 0, len = items.length; i < len; i++) {
						this.setXY(items[i]);
					}
				};
				this.handleUpdate();
			}
});
    </script>

    <script type="text/javascript">







function ShortCut(id,titles,url,desktop_width,desktop_height)
{
    var newWind = MyDesktop.getDesktop().getWindow(id);
    var exist=newWind;
    
    if(!newWind)
    {
    desktop_width = desktop_width>document.body.clientWidth?document.body.clientWidth:desktop_width;
    desktop_height = desktop_height>document.body.clientHeight?document.body.clientHeight-15:desktop_height;
    
    newWind= MyDesktop.getDesktop().createWindow({
                                        id:id,
                                        title:titles,
                                        width:desktop_width,
                                        height:desktop_height,
                                        iconCls: 'tabs',
                                        shim:false,
                                        animCollapse:false,
                                        border:false,
                                        constrainHeader:false,
                                        layout: 'fit',
                                        html:'<iframe frameborder="auto" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width=100% height=100%></iframe>'
                                    });}
       newWind.show();    
       if(!exist)
       { 
            var wbody = Ext.getCmp(newWind.id);
            wbody.body.mask('Loading', 'x-mask-loading');
            setTimeout(function(){wbody.body.unmask();                                    
                                      }, 1000);
           
        }
    }
    
    function ShortModuleWindow(id,titles,url,desktop_width,desktop_height)
{
    var newWind = MyDesktop.getDesktop().getWindow(id);
    var exist=newWind;
    if(!newWind)
    {
    //alert(desktop_width+"  "+desktop_height);
    newWind= MyDesktop.getDesktop().createWindow({
                                        id:id,
                                        title:titles,
                                        width:desktop_width,
                                        height:desktop_height,
                                        iconCls: 'tabs',
                                        shim:false,
                                        animCollapse:false,
                                        border:false,
                                        constrainHeader:false,
                                        modal: true,
                                        layout: 'fit',
                                        html:'<iframe frameborder="auto" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width=100% height=100%></iframe>'
                                    });}
       newWind.show();    
       if(!exist)
       { 
            var wbody = Ext.getCmp(newWind.id);
            wbody.body.mask('Loading', 'x-mask-loading');
            setTimeout(function(){wbody.body.unmask();                                    
                                      }, 1000);
           
        }
    }
    
    function ShortCutSet(id,titles,url,desktop_width,desktop_height)
{
    var newWind = MyDesktop.getDesktop().getWindow(id);
    var exist=newWind;
    if(!newWind)
    {newWind= MyDesktop.getDesktop().createWindow({
                                        id:id,
                                        title:titles,
                                        width:desktop_width,
                                        height:desktop_height,
                                        iconCls: 'tabs',
                                        shim:false,
                                        animCollapse:false,
                                        border:false,
                                        constrainHeader:false,
                                        //bodyStyle :'overflow-x:scroll;overflow-y:scroll',
                                        layout: 'fit',
                                        html:'<iframe frameborder="auto" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width=100% height=100%></iframe>'
                                    });}
       newWind.show();    
       if(!exist)
       { 
            var wbody = Ext.getCmp(newWind.id);
            wbody.body.mask('Loading', 'x-mask-loading');
            setTimeout(function(){wbody.body.unmask();                                    
                                      }, 1000);
            newWind.addListener('resize',function(){
                            var Obj=Ext.get("ifr"+id);
                            Obj.setHeight(newWind.getInnerHeight());
                            Obj.setWidth(newWind.getInnerWidth())                
                        });
                        
             newWind.addListener('beforeclose',function(){
                            //reloadshortcut(); 
                             Ext.Msg.confirm('刷新桌面提示', '确定要刷新桌面快捷吗？',function(btn){
       							if(btn == 'yes')
                            	window.location.href=window.location.href;
                            });               
                        });
        }
    }
    

		function loadRootItems()
		{
			Ext.Ajax.request({   
        		url: '<%=request.getContextPath()%>/menu/queryRootItemsBean.page', 
        			success: function (response, opts) {   
        				renderRootNodes(Ext.decode(response.responseText));
        				
          			 
        			}
        		});	
		}
		
		function renderRootNodes(data)
		{
			//alert(response.parentId);
        			//var data=Ext.decode(response.responseText);
         		 	var menuArr = data.listItems; 
         		 	var ppath= null;
         		 	//alert(menuArr.length);
         		 	
         		 	for (var i = 0; i < menuArr.length; i++) {   
            			var a=menuArr[i].id;
            			var b=menuArr[i].name;
            			var c=menuArr[i].pathU;
            			MyDesktop.getDesktop().taskbar.startMenu.menu.add(
            			{
            				itemId:a,
 							text: b,
 							iconCls:'icon-grid',
 							handler : function(m){
 								ShortCut(m.itemId,m.text,m.urlpath,m.desktop_width,m.desktop_height);
 							},
 							scope: this
 							,urlpath:c
 							,desktop_width:menuArr[i].desktop_width
 							,desktop_height:menuArr[i].desktop_height
            			});
          			} 
          			var moudleArr=   data.listModules; 
          			for(var j=0;j<moudleArr.length;j++){
          				var a=moudleArr[j].id;
            			var b=moudleArr[j].name;
            			var c=moudleArr[j].pathU;
            			var hasSon = moudleArr[j].hasSon;
            			
            			if(hasSon)
            			{
	            			var menu_item = new Ext.menu.Menu();
	            			//alert(MyDesktop.getDesktop().taskbar.startMenu.menu.id);
	            			
	            			var windows__ = MyDesktop.getDesktop().taskbar.startMenu.menu.add({
					    	 	itemId:a,
					 			text: b,
					 			iconCls:'icon-grid',
					 			handler : function(){
					 				return false;
					 			},
					 			menu:menu_item,
					 			listeners: {   
					    		'beforerender': function (m) {  
					   				Ext.Ajax.request({   
					        		url: '<%=request.getContextPath()%>/menu/queryItemsBean.page',  
					        		params: {   
					         	 		moduleId:m.itemId
					        		} ,  
					        		success: function (response, opts) {   
					        			renderNodes(Ext.decode(response.responseText),m)
					        		} 
					     		});   
					   			}},
					 			scope: this
							});
							
	            		}
	            		else
	            		{
	            			MyDesktop.getDesktop().taskbar.startMenu.add({
					    	 	itemId:a,
					 			text: text,
					 			iconCls:'icon-grid',
					 			handler : function(){
					 				return false;
					 			},
					 			
					 			scope: this
							});
	            		}
          			
          			}
		}
		
		function renderNodes(data,m)
		{
			//alert(response.parentId);
        			//var data=Ext.decode(response.responseText);
         		 	var menuArr = data.listItems; 
         		 	var ppath=data.parentPath;
         		 	//alert(menuArr.length);
         		 	for (var i = 0; i < menuArr.length; i++) {   
            			var a=menuArr[i].id;
            			var b=menuArr[i].name;
            			var c=menuArr[i].pathU;
            			m.menu.add(
            			{
            				itemId:menuArr[i].id,
 							text: menuArr[i].name,
 							iconCls:'icon-grid',
 							handler : function(m){
 								ShortCut(m.itemId,m.text,m.urlpath,m.desktop_width,m.desktop_height);
 							},
 							urlpath:c
 							,desktop_width:menuArr[i].desktop_width
 							,desktop_height:menuArr[i].desktop_height
            			}
            			);
          			} 
          			var moudleArr=   data.listModules; 
          			for(var j=0;j<moudleArr.length;j++){
          				var a=moudleArr[j].id;
            			var b=moudleArr[j].name;
            			var c=moudleArr[j].pathU;
            			var hasSon = moudleArr[j].hasSon;
            			
            			if(hasSon)
            			{
	            			var menu_item = new Ext.menu.Menu();
	            			m.menu.add(
	            			{
	            				itemId:a,
	 							text: b,
	 							iconCls:'icon-grid',
	 							handler : function(){
	 								return false;
	 							},
	 							menu:menu_item, 							
	 							listeners:{
	 								'beforerender': function (m) { 
	 									if(m.hasson)
	 									{
	 										loadMoudleItems(m.itemId,m.parentPath,m);
	 									}
	 									else
	 									{
	 										//alert("没有儿子");
	 										//return false;
	 									}
	 								}
	 							}
	 							,
	 							hasson:hasSon,
	 							parentPath:ppath
	            			}
	            			);
	            		}
	            		else
	            		{
	            			m.menu.add(
	            			{
	            				itemId:a,
	 							text: b,
	 							iconCls:'icon-grid',
	 							handler : function(){
	 								return false;
	 							}
	 							,
	 							hasson:hasSon
	            			}
	            			);
	            		}
          			
          			}
		}
		
		
		
		function loadMoudleItems(id,parentPath,node){
			
			Ext.Ajax.request({   
        		url: '<%=request.getContextPath()%>/menu/queryItemsBean.page',  
        		params: {   
         	 		moduleId:id,
         	 		parentPath:parentPath
        		} ,  
        			success: function (response, opts) {   
        				renderNodes(Ext.decode(response.responseText),node);
        				
          			 
        			}
        		});	
		
		}

    
    </script>
</head>
<body>

   

</body>

</html>
