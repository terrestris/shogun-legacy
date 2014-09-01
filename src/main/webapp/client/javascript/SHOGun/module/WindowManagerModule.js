/*
 *
 * Copyright (C) 2012  terrestris GmbH & Co. KG, info@terrestris.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.-
 *
 * @author terrestris GmbH & Co. KG
 */
Ext.ns('SHOGun.module');

/**
 * 
 */
SHOGun.module.WindowManagerModule = Ext.extend(SHOGun.module.BorderLayout, {
    appMainMenu: null,
    initComponent: function(){
        var constants = {
            id: 'terrestris_window_mananger_module',
            helpId: null,
            userConf: null,
            rightTo: null
        
        }; // eo config object
        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, constants));
        
        // the map-toolbar of the module
        this.mapPanelElements.tbarItems = [];
        
        
        // the accordions of the modules
        this.westPanelElements.items = [];
        
        
        /**
         * the central managed window function that gets called whenever
         * the mouse hovers the button 'Fenster'
         * This way the status information of the managed windows is always correct
         * 
         */
        var rebuildManagedWindowMenu = function(){
            
            // 'this' ist wg. scope die eigtl. toolbar
            
            /**
             * get the components have to be managed
             * 
             * @param {Object} cmp
             */
            var managedCmps = Ext.ComponentMgr.all.filterBy(function(cmp){
                
                if (!Ext.isEmpty(cmp) && !Ext.isEmpty(cmp.managed) && cmp.managed === true) {
                    return true;
                }
                else {
                    return false;
                }
            });
            
            /**
             * helper function to toggle visible state of a managed component
             * 
             * @param {Object} managedCmp
             * @param {Object} forcedState
             */
            var toggleManagedCmp = function(managedCmp, forcedState){
                var targetObj = managedCmp;
                if (managedCmp && managedCmp.ownerCt && managedCmp.ownerCt instanceof Ext.Window) {
                    targetObj = managedCmp.ownerCt;
                }
                
                if (Ext.isDefined(forcedState)) {
                    if (forcedState === 'show') {
                        targetObj.show();
                    }
                    else {
                        if (forcedState === 'hide') {
                            targetObj.hide();
                        }
                    }
                }
                else {
                    if (!targetObj.isVisible()) {
                        targetObj.show();
                    }
                    else {
                        targetObj.hide();
                    }
                }
            };

            if (this instanceof Ext.Toolbar) {

                var fensterBtn = SHOGun.util.General.findFirstMenuElementByText(this, 'Fenster');
                fensterBtn.menu.removeAll();
                
                // fixe einträge neu schreiben:
                var showAllWindowsItem = {
                    xtype: 'menuitem',
                    text: 'Alle einblenden',
                    handler: function(){
                        managedCmps.each(function(managedCmp){
                            toggleManagedCmp(managedCmp, 'show');
                        });
                    }
                };
                fensterBtn.menu.addItem(showAllWindowsItem);
                
                var hideAllWindowsItem = {
                    xtype: 'menuitem',
                    text: 'Alle ausblenden',
                    handler: function(){
                        managedCmps.each(function(managedCmp){
                            toggleManagedCmp(managedCmp, 'hide');
                        });
                    }
                };
                
                fensterBtn.menu.addItem(hideAllWindowsItem);
                
                var dividerItem = '-';
                fensterBtn.menu.addItem(dividerItem);
                
                managedCmps.each(function(managedCmp){
                    var item = {
                        xtype: 'menucheckitem',
                        checked: managedCmp.isVisible(),
                        text: managedCmp.title || 'untitled',
                        handler: function(){
                            toggleManagedCmp(managedCmp);
                        }
                    };
                    fensterBtn.menu.addItem(item);
                });
            }
        };
        
        
        // the main toolbar of the module
        this.menuElements.menuToolbar = new SHOGun.widget.MenuToolbar();
        this.menuElements.menuToolbar.items = [{
            xtype: 'button',
            menuPath: 'Fenster',
            listeners: {
                'scope': this.appMainMenu,
                'mouseover': rebuildManagedWindowMenu
            },
            menu: {
                xtype: 'menu',
                items: []
            }
        }];
        

        SHOGun.module.WindowManagerModule.superclass.initComponent.apply(this, arguments);
    } // eo function initComponent
});

Ext.reg('shogun_window_manager_module', SHOGun.module.WindowManagerModule);