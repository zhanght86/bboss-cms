/*

This file is part of Ext JS 4

Copyright (c) 2011 Sencha Inc

Contact:  http://www.sencha.com/contact

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

*/
/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('MyDesktop.Settings', {
    extend: 'Ext.window.Window',

    uses: [
        'Ext.tree.Panel',
        'Ext.tree.View',
        'Ext.form.field.Checkbox',
        'Ext.layout.container.Anchor',
        'Ext.layout.container.Border',

        'Ext.ux.desktop.Wallpaper',

        'MyDesktop.WallpaperModel'
    ],

    layout: 'anchor',
    title: '背景设置',
    modal: true,
    width: 640,
    height: 480,
    border: false,
    
    initComponent: function () {
        var me = this;

        me.selected = me.desktop.getWallpaper();
        me.stretch = me.desktop.wallpaper.stretch;

        me.preview = Ext.create('widget.wallpaper');
        me.preview.setWallpaper(me.selected);
        me.tree = me.createTree();
        
        me.buttons = [
            { text: '确定', handler: me.onOK, scope: me },
            { text: '取消', handler: me.close, scope: me }
        ];

        me.items = [
            {
                anchor: '0 -30',
                border: false,
                layout: 'border',
                items: [
                    me.tree,
                    {
                        xtype: 'panel',
                        title: '预览',
                        region: 'center',
                        layout: 'fit',
                        items: [ me.preview ]
                    }
                ]
            },
           
            new Ext.Button({ 
                text:"自定义背景管理", 
                handler : function() { 
                   ShortModuleWindow("自定义背景列表","自定义背景列表","background/main.page","777",500);
					} 
				}),
            {
                xtype: 'checkbox',
                boxLabel: '自适应',
                checked: me.stretch,
                listeners: {
                    change: function (comp) {
                        me.stretch = comp.checked;
                    }
                }
            }
            
        ];

        me.callParent();
    },
    
    createTree : function() {
        var me = this;
        
        

        function child (img) {
            return { img: img, text: me.getTextOfWallpaper(img), iconCls: '', leaf: true };
        }
        
        

        var tree = new Ext.tree.Panel({
            title: '桌面背景',
            rootVisible: false,
            lines: false,
            autoScroll: true,
            width: 150,
            region: 'west',
            split: true,
            minWidth: 100,
            
            listeners: {
                afterrender: { fn: this.setInitialSelection, delay: 100 },
                
                select: this.onSelect,  
                //beforeitemexpand:this.loadCustomBackgrounds,
                scope:this 
            },
           
            store: new Ext.data.TreeStore({
                model: 'MyDesktop.WallpaperModel',
                proxy: {
	            	type: 'ajax',
	            	url: 'background/getListBlackGround.page'
	        	},
                root: {
                    text:'Wallpaper',
                    expanded: true,
                    children:[
                        {
                        	text: "默认背景", iconCls: '', leaf: false ,                        
		                        children:[{ text: "None", iconCls: '', leaf: true },
					                        child('Blue-Sencha.jpg'),
					                        child('Dark-Sencha.jpg'),
					                        child('Wood-Sencha.jpg'),
					                        child('blue.jpg'),
					                        child('desk.jpg'),
					                        child('desktop.jpg'),
					                        child('desktop2.jpg'),
					                        child('sky.jpg')]
                        },
                         {
                        	text: "自定义", iconCls: '', leaf: false  		                    
                        }
                        
                    ]
                }
            })         
        });
        
        //tree.on('beforeitemcollapse', 
		  //   function(node,opt){ 
		    // loadCustomBackgrounds(node);
		  //});    
        
        return tree;
    },
     
     
    

    getTextOfWallpaper: function (path) {
        var text = path, slash = path.lastIndexOf('/');
        if (slash >= 0) {
            text = text.substring(slash+1);
        }
        var dot = text.lastIndexOf('.');
        text = Ext.String.capitalize(text.substring(0, dot));
        text = text.replace(/[-]/g, ' ');
        return text;
    },

    onOK: function () {
        var me = this;
        if (me.selected) {
            me.desktop.setWallpaper(me.selected, me.stretch);
        }
        var fit = "0";
        if(me.stretch)fit = "1";
        Ext.Ajax.request({   
	        		url: 'background/saveblackground.page',  
	        		params: {   
	         	 		filename:me.selected,
	         	 		fit:fit
	        		} ,  
	        		success: function (response, opts) {   
	        		 //alert(Ext.decode(response.responseText));
	        			if(Ext.decode(response.responseText) != "ok")alert("保存失败!");
	        		} 
	     		});   
        me.destroy();
    },

    onSelect: function (tree, record) {
        var me = this;

        if (record.data.img) {
            me.selected = 'wallpapers/' + record.data.img;
        } else {
            me.selected = Ext.BLANK_IMAGE_URL;
        }

        me.preview.setWallpaper(me.selected);
    },
    loadCustomBackgrounds: function (record) {
       
       if(record.data.text == '自定义' && !record.data.customexpanded)
       {
	        var me = this;
			
	        Ext.Ajax.request({   
		        		url: 'background/getListBlackGround.page',  
		        		params: {   
		         	 		
		        		} ,  
		        		success: function (response, opts) {
		        			alert(Ext.decode(response.responseText))   	        		 
		        			//record.childNodes = Ext.decode(response.responseText);
		        			record.data.customexpanded = true;
		        		} 
		     		});
		     		
		     
	    }  

        
    },

    setInitialSelection: function () {
        var s = this.desktop.getWallpaper();
        if (s) {
            var path = '/Wallpaper/' + this.getTextOfWallpaper(s);
            this.tree.selectPath(path, 'text');
        }
    }
});

