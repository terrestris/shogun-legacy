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
 * 
 */
Ext.ns('SHOGun.module');

/**
 * Abstract class to be used within Border Layouts
 * Offers members and functions needed for border layout modules
 */
SHOGun.module.BorderLayout = Ext.extend(Ext.Component, {

    /**
     *
     */
    border: false,
    
    /**
     *
     */
    northPanelElements: {},
    
    /**
     *
     */
    southPanelElements: {},
    
    /**
     *
     */
    menuElements: {
        menuToolbar: {
            items: []
        }
    },
    
    /**
     *
     */
    mapPanelElements: {},
    
    /**
     *
     */
    westPanelElements: {},
    
    /**
     *
     */
    eastPanelElements: {},
    
    
    /**
     * initializes this component
     */
    initComponent: function(){
        var constants = {}; // eo constants object
        Ext.apply(this, Ext.apply(this.initialConfig, constants));
        SHOGun.module.BorderLayout.superclass.initComponent.apply(this, arguments);
    }, // eo function initComponent
    
    /**
     * In abgeleiteten Klassen kann man ggf. noch Änderungen vornehmen,
     * um parent::mergeMenuToolbar z.B. mit angepassten Werten aufrufen zu konnen.
     *
     *  Beispiel:
     *  <code>
     *  Ext.each(this.menuElements.menuToolbar.items, function(item) {
     *      item.menuPath += "MJCM";
     *  });
     *  Terrestris.module.mapclient.LogoutComponent.superclass.mergeMenuToolbar.apply(this, arguments);
     *  </code>
     *
     * @param {Object} appMenuToolbar
     */
    mergeMenuToolbar: function(appMenuToolbar){
    
        // set a local alias because of multiple calls
        var findFirstMenuElementByText = SHOGun.util.General.findFirstMenuElementByText;
        
        if (Ext.isObject(appMenuToolbar) && appMenuToolbar instanceof SHOGun.widget.MenuToolbar) {
            // grab the given components Path to determine whether we already 
            // have the needed structure for this modules menu
            
            // iterate over the array of items
            Ext.each(this.menuElements.menuToolbar.items, function(menuToolbarElement, numMenuToolbarElement){
            
                var menuPath = menuToolbarElement.menuPath;
                var finalElement = menuToolbarElement;
                var menuParts = menuPath.split("|");
                var lastAnchor;
                
                // iterate over the pipe-splitted strings (Datei|Bla|...)
                Ext.each(menuParts, function(menuPart, depth){
                    var xtype = 'menu';
                    if (depth === 0) {
                        lastAnchor = appMenuToolbar;
                    }
                    
                    // erster Eintrag -> muss ja ein Button sein
                    // für das erste Element ist lastAnchor zwingend die Toolbar!
                    if (depth === 0 && menuParts.length !== 1) { //ggf. >1???
                        var elementButton = findFirstMenuElementByText(lastAnchor, menuPart);
                        
                        if (!elementButton) {
                            var tmpButton = new Ext.Button({
                                xtype: 'button',
                                text: menuPart,
                                menu: new Ext.menu.Menu()
                            });
                            // last anchor is the toolbar
                            lastAnchor.add(tmpButton);
                            elementButton = tmpButton;
                        }
                        lastAnchor = elementButton;
                    }
                    
                    else {
                        var elementForPart;
                        //letzter Eintrag -> muss ja menuitem sein
                        if (depth === menuParts.length - 1 && lastAnchor) {
                        
                            elementForPart = findFirstMenuElementByText(lastAnchor.menu, menuPart);
                            if (!elementForPart) {
                            
                                var dynMenuItem = {
                                    //xtype: 'menuitem',
                                    xtype: finalElement.xtype,
                                    text: menuPart,
                                    handler: finalElement.handler,
                                    scope: finalElement.scope,
                                    listeners: finalElement.listeners,
                                    menu: finalElement.menu
                                };
                                if (lastAnchor.menu) {
                                    lastAnchor.menu.addItem(dynMenuItem);
                                }
                                else {
                                    lastAnchor.add(dynMenuItem);
                                }
                            }
                            
                        }
                        //ist nicht das erste und nicht das letzte Element -> muss einfach nur ein Menu sein
                        else {
                        
                            if (lastAnchor && lastAnchor.menu) {
                                elementForPart = findFirstMenuElementByText(lastAnchor.menu, menuPart);
                                if (!elementForPart) {
                                    // this one ist not existing, so we need to create it
                                    var tmpMenu = new Ext.menu.Item({
                                        menu: new Ext.menu.Menu(),
                                        text: menuPart
                                    });
                                    lastAnchor.menu.addMenuItem(tmpMenu);
                                    elementForPart = tmpMenu;
                                }
                                lastAnchor = elementForPart;
                            }
                        }
                    }
                });
            });
        }
        
    },
    
    /**
     *
     * @param {Object} appMapPanelToolbar
     */
    mergeMapPanelToolbar: function(appMapPanelToolbar){
        if (Ext.isObject(appMapPanelToolbar) && appMapPanelToolbar instanceof Ext.Toolbar) {
            Ext.each(this.mapPanelElements.tbarItems, function(value, key){
                appMapPanelToolbar.addItem(value);
            });
        }
    },
    
    /**
     *
     * @param {Object} appwestPanel
     */
    mergeWestPanel: function(appWestPanel){
        if (Ext.isObject(appWestPanel) &&
        appWestPanel instanceof Ext.Panel) {
            appWestPanel.add(this.westPanelElements.items);
        }
    },
    
    /**
     * 
     * @param {Object} appEastPanel
     */
    mergeEastPanel: function(appEastPanel){
        if (Ext.isObject(appEastPanel) &&
        appEastPanel instanceof Ext.Panel) {
            appEastPanel.add(this.eastPanelElements.items);
        }
    },
    
    /**
     * 
     * @param {Object} appSouthPanel
     */
    mergeSouthPanel: function(appSouthPanel){
        if (Ext.isObject(appSouthPanel) &&
        appSouthPanel instanceof Ext.Panel) {
            appSouthPanel.add(this.southPanelElements.items);
        }
    },
    
    /**
     * 
     * @param {Object} appNorthPanel
     */
    mergeNorthPanel: function(appNorthPanel){
        if (Ext.isObject(appNorthPanel) &&
        appNorthPanel instanceof Ext.Panel) {
            appNorthPanel.add(this.northPanelElements.items);
        }
    }
});

Ext.reg('shogun_border_layout_module', SHOGun.module.BorderLayout);
