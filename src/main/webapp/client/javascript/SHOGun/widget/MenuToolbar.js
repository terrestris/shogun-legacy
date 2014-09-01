/*
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
 *
 */
Ext.ns('SHOGun.widget');

/**
 * Specified MenuToolbar class.
 * Equals Ext.Toolbar atm.
 */
SHOGun.widget.MenuToolbar = Ext.extend(Ext.Toolbar, {
    
    /**
     * 
     */
    initComponent: function(){
        
        SHOGun.widget.MenuToolbar.superclass.initComponent.call(this);
        
        /**
         * 
         */
//        var rebuildManagedWindowMenu = function(){
//            // 'this' ist wg. scope die eigtl. toolbar
//            var managedCmps = Ext.ComponentMgr.all.filterBy(function(cmp){
//                return (cmp && cmp.managed && cmp.managed === true);
//            });
//            
//            var toggleManagedCmp = function(managedCmp, forcedState){
//                var targetObj = managedCmp;
//                if (managedCmp && managedCmp.ownerCt && managedCmp.ownerCt instanceof Ext.Window) {
//                    targetObj = managedCmp.ownerCt;
//                }
//                
//                if (Ext.isDefined(forcedState)) {
//                    if (forcedState === 'show') {
//                        targetObj.show();
//                    }
//                    else {
//                        if (forcedState === 'hide') {
//                            targetObj.hide();
//                        }
//                    }
//                }
//                else {
//                    if (!targetObj.isVisible()) {
//                        targetObj.show();
//                    }
//                    else {
//                        targetObj.hide();
//                    }
//                }
//            };
//            
//            if (this instanceof Ext.Toolbar) {
//                var fensterBtn = this.find('text', 'Fenster')[0];
//                fensterBtn.menu.removeAll();
//                
//                // fixe einträge neu schreiben:
//                var showAllWindowsItem = {
//                    xtype: 'menuitem',
//                    text: 'Alle einblenden',
//                    handler: function(){
//                        managedCmps.each(function(managedCmp){
//                            toggleManagedCmp(managedCmp, 'show');
//                        });
//                    }
//                };
//                fensterBtn.menu.addItem(showAllWindowsItem);
//                
//                var hideAllWindowsItem = {
//                    xtype: 'menuitem',
//                    text: 'Alle ausblenden',
//                    handler: function(){
//                        managedCmps.each(function(managedCmp){
//                            toggleManagedCmp(managedCmp, 'hide');
//                        });
//                    }
//                };
//                
//                fensterBtn.menu.addItem(hideAllWindowsItem);
//                
//                var dividerItem = '-';
//                fensterBtn.menu.addItem(dividerItem);
//                
//                managedCmps.each(function(managedCmp){
//                    var item = {
//                        xtype: 'menucheckitem',
//                        checked: managedCmp.isVisible(),
//                        text: managedCmp.title || 'untitled',
//                        handler: function(){
//                            toggleManagedCmp(managedCmp);
//                        }
//                    };
//                    fensterBtn.menu.addItem(item);
//                });
//            }
//        };
        
        
//        var fensterItem = {
//            xtype: 'button',
//            text: 'Fenster',
//            listeners: {
//                'scope': this,
//                'mouseover': rebuildManagedWindowMenu
//            },
//            menu: {
//                xtype: 'menu',
//                items: []
//            }
//        };
//        this.add(fensterItem);
    }
});
Ext.reg('shogun_menutoolbar', SHOGun.widget.MenuToolbar);
