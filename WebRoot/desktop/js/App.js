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

Ext.define('Ext.ux.desktop.App', {
    mixins: {
        observable: 'Ext.util.Observable'
    },

    requires: [
        'Ext.container.Viewport',

        'Ext.ux.desktop.Desktop'
    ],

    isReady: false,
    modules: null,
    useQuickTips: true,

    constructor: function (config) {
        var me = this;
        me.addEvents(
            'ready',
            'beforeunload'
        );

        me.mixins.observable.constructor.call(this, config);

        if (Ext.isReady) {
            Ext.Function.defer(me.init, 10, me);
        } else {
            Ext.onReady(me.init, me);
        }
    },

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
    },

    /**
     * This method returns the configuration object for the Desktop object. A derived
     * class can override this method, call the base version to build the config and
     * then modify the returned object before returning it.
     */
    getDesktopConfig: function () {
        var me = this, cfg = {
            app: me,
            taskbarConfig: me.getTaskbarConfig()
        };

        Ext.apply(cfg, me.desktopConfig);
        return cfg;
    },

    getModules: Ext.emptyFn,

    /**
     * This method returns the configuration object for the Start Button. A derived
     * class can override this method, call the base version to build the config and
     * then modify the returned object before returning it.
     */
    getStartConfig: function () {
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

        return cfg;
    },

    /**
     * This method returns the configuration object for the TaskBar. A derived class
     * can override this method, call the base version to build the config and then
     * modify the returned object before returning it.
     */
    getTaskbarConfig: function () {
        var me = this, cfg = {
            app: me,
            startConfig: me.getStartConfig()
        };

        Ext.apply(cfg, me.taskbarConfig);
        return cfg;
    },

    initModules : function(modules) {
        var me = this;
        Ext.each(modules, function (module) {
            module.app = me;
        });
    },

    getModule : function(name) {
    	var ms = this.modules;
        for (var i = 0, len = ms.length; i < len; i++) {
            var m = ms[i];
            if (m.id == name || m.appType == name) {
                return m;
            }
        }
        return null;
    },

    onReady : function(fn, scope) {
        if (this.isReady) {
            fn.call(scope, this);
        } else {
            this.on({
                ready: fn,
                scope: scope,
                single: true
            });
        }
    },

    getDesktop : function() {
        return this.desktop;
    },

    onUnload : function(e) {
        if (this.fireEvent('beforeunload', this) === false) {
            e.stopEvent();
        }
    },
    initShortcut:function(){
    	alert('initShortcut');

        //shortcuts 自动换行

        var btnHeight = 70;//61

        var btnWidth = 64;

        var btnPadding = 35;//15

        var col = null;

        var row = null;

        function initColRow(){

            col = {

                index: 1, 

                x: btnPadding

            };

            row = {

                index: 1, 

                y: btnPadding

            };

        }

 

        initColRow();

 

        function isOverflow(y){

           //if(y > (Ext.lib.Dom.getViewHeight() - taskbarEl.getHeight())){

            if(y>Ext.get('ext-gen1003').getHeight()-Ext.get('taskbar-1024').getHeight()){

                return true;

            }

            return false;

        }

        this.setXY = function(item){

            var bottom = row.y + btnHeight,

            overflow = isOverflow(row.y + btnHeight);

            if(overflow && bottom > (btnHeight + btnPadding)){

                col = {

                    index: col.index++, 

                    x: col.x + btnWidth + btnPadding

                };

                row = {

                    index: 1,

                    y: btnPadding

                };

            }

            Ext.fly(item).setXY([

                col.x

                , row.y

                ]);

            row.index++;

            row.y = row.y + btnHeight + btnPadding;

        };

        this.handleUpdate = function(){

            initColRow();

            //var items=shortcuts.dom.children;

            var items = Ext.query(".ux-desktop-shortcut"); 

            for(var i = 0, len = items.length; i < len; i++){

                this.setXY(items[i]);

            }

        }

        this.handleUpdate();

        //每过 500 毫秒重绘页面

        Ext.EventManager.onWindowResize(this.handleUpdate, this, {

            delay:500

        });

    //end shortcuts 自动换行

    }


});

