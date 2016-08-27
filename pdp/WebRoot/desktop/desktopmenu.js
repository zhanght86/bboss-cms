$(document).ready(function() {
				$("#x-desktop").load("shortcut.page #shortcutcontainer")
			});

MyDesktop = new Ext.app.App({
	init :function(){
		Ext.QuickTips.init();
		loadRootItems();
		
	},

	getModules : function(){
	
		return [];
	},

    // config for the start menu
    getStartConfig : function(){
        return {
            title: '<%=accesscontroler.getUserAccount()%>(<%=accesscontroler.getUserName()%>)',
            iconCls: 'user',
            toolItems: [{
                text:'设置',
                iconCls:'settings',
                handler :function(){
                ShortCutSet('setting','设置','<%=request.getContextPath()%>/menu/setting.page');
               // window.top.location.href = window.top.location.href ;
                	//window.location.href='<%=request.getContextPath()%>/desktop/gosetting.page';
                },
                scope:this
            },'-',{
                text:'登出',
                iconCls:'logout',
                handler :function(){
                	window.location.href='<%=request.getContextPath()%>/logout.jsp';
                },
                scope:this
            }]
        };
    }
});




function ShortCut(id,titles,url)
{
    var newWind = MyDesktop.getDesktop().getWindow(id);
    var exist=newWind;
    if(!newWind)
    {newWind= MyDesktop.getDesktop().createWindow({
                                        id:id,
                                        title:titles,
                                        width:777,
                                        height:500,
                                        iconCls: 'tabs',
                                        shim:false,
                                        animCollapse:false,
                                        border:false,
                                        constrainHeader:true,
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
    
    function ShortCutSet(id,titles,url)
{
    var newWind = MyDesktop.getDesktop().getWindow(id);
    var exist=newWind;
    if(!newWind)
    {newWind= MyDesktop.getDesktop().createWindow({
                                        id:id,
                                        title:titles,
                                        width:777,
                                        height:500,
                                        iconCls: 'tabs',
                                        shim:false,
                                        animCollapse:false,
                                        border:false,
                                        constrainHeader:true,
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
                        
             newWind.addListener('resize',function(){
                            var Obj=Ext.get("ifr"+id);
                            Obj.setHeight(newWind.getInnerHeight());
                            Obj.setWidth(newWind.getInnerWidth())                
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
            			MyDesktop.launcher.add(
            			{
            				itemId:a,
 							text: b,
 							iconCls:'icon-grid',
 							handler : function(m){
 								ShortCut(m.itemId,m.text,m.urlpath);
 							},
 							scope: this
 							,urlpath:c
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
	            			var windows__ = MyDesktop.launcher.add({
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
	            			MyDesktop.launcher.add({
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
            			m.menu.addItem(
            			{
            				itemId:menuArr[i].id,
 							text: menuArr[i].name,
 							iconCls:'icon-grid',
 							handler : function(m){
 								ShortCut(m.itemId,m.text,m.urlpath);
 							},
 							urlpath:c
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
	            			m.menu.addItem(
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
	            			m.menu.addItem(
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
