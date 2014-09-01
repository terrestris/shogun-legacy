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
 * responsible for map and base components in border layout
 */
SHOGun.module.MainComponent = Ext.extend(SHOGun.module.BorderLayout, {
    
    /**
     * initializes this component
     */
    initComponent: function(){
        var constants = {
            id: 'terrestris_main_component',
            helpId: null,
            userConf: null,
            rightTo: null
        }; // eo constants object
        var mapObject = {};
        // the map of the module
        this.mapPanelElements.map = window.map;
        // the map-toolbar of the module
           this.mapPanelElements.tbarItems = new SHOGun.util.ToolbarItems().getItems();
        
        this.westPanelElements.items = [];
        this.westPanelElements.items.push(new SHOGun.widget.DummyPanel().getPanel());
        
        // the main toolbar of the module
        this.menuElements.menuToolbar = new SHOGun.widget.MenuToolbar();
        this.menuElements.menuToolbar.items = [];
        
        this.eastPanelElements.items = [];
        this.eastPanelElements.items.push(new SHOGun.widget.DummyPanel().getPanel());
        
        this.southPanelElements.items = [];
        this.northPanelElements.items = [];

        
        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, constants));
        
        // call parent
        SHOGun.module.MainComponent.superclass.initComponent.apply(this, arguments);
    }

});

Ext.reg('shogun_maincomponent_module', SHOGun.module.MainComponent);