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
Ext.ns('SHOGun');

/**
 * The main class of the application
 * Just instanciating the needed classes
 *
 * @constructor
 */
SHOGun.BaseApplication = function(data) {
    
    /**
     * 
     */
    this.data = data || null;
    
    /**
     * the application config object
     */
    this.appConfig = {};
    
    /**
     * the user config object
     */
    this.userConfig = {
        berechtigungen: []
    };
    
    /**
     * TODO is this meaningful?
     */
    this.RIGHT_KEYS = window.SHOGun.RIGHT_KEYS;
    
    /**
     * options for OL map  and GeoExt map panel, 
     * will be set from AppContext
     */
    this.mapOpts = null;    
    
    /**
     * 
     */
    this.appModules = [];
    
    /**
     * 
     */
    this.viewport = null;
    
    /**
     * 
     */
    this.mapPanel = null;
    
    /**
     * 
     */
    this.westPanel = null;
    
    /**
     * 
     */
    this.mainMenu = null;
    
    /**
     * 
     */
    this.eastPanel = null;
    
    /**
     * 
     */
    this.northPanel = null;
    
    /**
     * 
     */
    this.southPanel = null;
    
    /**
     * Initializes the application
     */
    this.initApplication = function() {
        
        if (this.data !== null && Ext.isDefined(this.data.user) && Ext.isDefined(this.data.app)) {
            this.setUserContext(this.data.user);
            this.setAppContext(this.data.app);
        }
        else {
            Ext.Msg.alert('Error', 'Could not read application data!', function(btn, text){
                window.location.href = "./j_spring_security_logout";
            });
        }
        
        // store for application modules
        this.appModules = [];
        
        // apply some defaults
        var opts = Ext.apply({
            displayProjection:"EPSG:4326",
            numZoomLevels : 24,
            maxExtent: [-20037508.34,-20037508.34,20037508.34,20037508.34], // world
            controls : [ 
                'LoadingPanel',
                'Navigation',
                'MousePosition',
                'Scale',
                'KeyboardDefaults',
                'Attribution'],
            units : 'm',
            allOverlays : false,
            panMethod : null
        }, this.userConfig.mapConfig);

        // create OL map
        this.mapOpts = opts;
        window.map = SHOGun.Config.createMap(this.mapOpts);
        
        // create OL layers
        var layerObjects = SHOGun.Config.createLayers(this.userConfig.mapLayers);
        window.map.addLayers(layerObjects);
        
        
        // ADD MODULES TO APPLICATION
        
        /*
         * check if right for GUI is there 
         * if not LOGOUT
         */
        if (this.hasRightFor('MODULE_BASEGUI')) {
            this.addModule(new SHOGun.module.MainComponent());
        }
        else {
            Ext.Msg.alert('CAUTION', 'Erronious module configuration detected!', function() {
                window.location.href = "./j_spring_security_logout";
            });
            throw ('NO RIGHT FOR BASE GUI');
        }
        
        // header
        if (this.hasRightFor('MODULE_HEADER')) {
            this.addModule(new SHOGun.module.Header());
        }
        
        // combo for baselayers
        if (this.hasRightFor('MODULE_BASELAYERCOMBO')) {
            this.addModule(new SHOGun.module.BaseLayerComboBox());
        }
        
        // nominatim geocoder
        if (this.hasRightFor('MODULE_NOMINATINMCOMBO')) {
            this.addModule(new SHOGun.module.NominatimSearchCombo());
        }
        
        // standard layer tree
        if (this.hasRightFor('MODULE_STANDARDTREE')) {
            this.addModule(new SHOGun.module.StandardLayerTree());
        }
        
        // adding standard legend panel
        if (this.hasRightFor('MODULE_LEGEND')) {
            this.addModule(new SHOGun.module.StandardLegendPanel());
        }
        
        // adding overview panel
        if (this.hasRightFor('MODULE_OVERVIEWMAPPANEL')) {
            this.addModule(new SHOGun.module.OverviewMapPanel());
        }
        
        // WINDOW MANAGER
        this.addModule(new SHOGun.module.WindowManagerModule({
            appMainMenu: this.mainMenu
        }));
        
        // GET FEATURE INFO
        if (this.hasRightFor('MODULE_WMSGETFEATUREINFO')) {
            this.addModule(new SHOGun.module.WmsGetFeatureInfo({
                map: window.map
            }));
        }
        
        // HELP
        if (this.hasRightFor('MODULE_HELP')) {
            this.addModule(new SHOGun.module.HelpModule());
        }
        
        if (this.hasRightFor('MODULE_GETFEATUREINFO')) {
            //TODO: resolve this variable? make own ComponentManager? / ModuleManager?
            this.gfiModule = new SHOGun.module.GetFeatureInfoModule();
            this.addModule(this.gfiModule);
        }
                        
        if (this.hasRightFor('MODULE_USERCONFIGURATION')) {
            this.addModule(new SHOGun.module.administration.UserConfiguration());
        }
        
        
        if (this.hasRightFor('MODULE_WMSUPLOADMODULE')) {
            this.addModule(new SHOGun.module.WmsUploadModule());
        }
        
        if (this.hasRightFor('MODULE_LOGIN')) {
            this.addModule(new SHOGun.module.Login());
        }
        
        if (this.hasRightFor('MODULE_LOGOUT')) {
            this.addModule(new SHOGun.module.LogoutModule());
        }

        
        var screenItems = [];
        
        if (this.mapPanel.map) {
            screenItems.push(this.mapPanel);
        }
        if (this.westPanel.items.length > 0) {
            screenItems.push(this.westPanel);
        }
        if (this.eastPanel.items.length > 0) {
            screenItems.push(this.eastPanel);
        }
        screenItems.push(this.northPanel);
        if (this.southPanel.items.length > 0) {
            screenItems.push(this.southPanel);
        }
        
        var containerPanel = {
            region: 'center',
            header: false,
            height: 120,
            title: 'SHOGun',
            border: false,
            layout: 'border',
            items: screenItems
        };
        
        this.appViewport = new Ext.Viewport({
            items: containerPanel,
            layout: 'border'
        });
    };
    
    /**
     * Add a module to this application
     *
     * @param {Object} module
     */
    this.addModule = function(module){
        
        if (Ext.isObject(module)) {
        
            //MapPanel
            if (!Ext.isObject(this.mapPanel) || !(this.mapPanel instanceof GeoExt.MapPanel)) {
            
                if (module.mapPanelElements.map && module.mapPanelElements.tbarItems) {
                    this.mapPanel = new GeoExt.MapPanel({
                        id: 'app-mappanel',
                        region: 'center',
                        map: module.mapPanelElements.map,
                        tbar: module.mapPanelElements.tbarItems,
                        border: true,
                        center: this.mapOpts.center,
                        zoom: this.mapOpts.zoom,
                        extent: this.mapOpts.extent || [-20037508.34,-20037508.34,20037508.34,20037508.34] // Germany
                    });
                }
            }
            else {
                module.mergeMapPanelToolbar(this.mapPanel.getTopToolbar());
            }
            
            //WestPanel
            if (!Ext.isObject(this.westPanel) || !(this.westPanel instanceof SHOGun.widget.DropZonePanel)) {
                if (module.westPanelElements.items) {
                    this.westPanel = new SHOGun.widget.DropZonePanel({
                        id: 'terrestris-west',
                        region: 'west',
                        title: 'Funktionen',
                        collapsible: true,
                        ddGroup: 'pinablePanel',
                        width: 225,
                        items: module.westPanelElements.items,
                        layout: 'fit'
                    });
                    // We need to attach the resizing eventhandler after the
                    // initialisation, so that we do not overwrite the listeners
                    // object defined for all SHOGun.widget.DropZonePanel
                    // This is a fix for Ticket #8. 
                    //TODO: we need to check how event attaching can be done in
                    // subclasses in a non destructive way.
                    this.westPanel.on('afterrender', function(panel){
                        panel.on({
                            'resize': function(panel){
                                panel.doLayout();
                            }
                        });
                    });
                }
            }
            else {
                module.mergeWestPanel(this.westPanel);
            }
            
            //EastPanel
            if (!Ext.isObject(this.eastPanel) || !(this.eastPanel instanceof SHOGun.widget.DropZonePanel)) {
                if (module.eastPanelElements.items) {
                    this.eastPanel = new SHOGun.widget.DropZonePanel({
                        id: 'terrestris-east',
                        region: 'east',
                        title: 'weitere Funktionen',
                        collapsible: true,
                        ddGroup: 'pinablePanel',
                        width: 430,
                        collapsed: true,
                        items: module.eastPanelElements.items
                    });
                    // We need to attach the resizing eventhandler after the
                    // initialisation, so that we do not overwrite the listeners
                    // object defined for all SHOGun.widget.DropZonePanel
                    // This is a fix for Ticket #8. 
                    //TODO: we need to check how event attaching can be done in
                    // subclasses in a non destructive way.
                    this.eastPanel.on('afterrender', function(panel){
                        panel.on({
                            'resize': function(panel){
                                panel.doLayout();
                            }
                        });
                    });
                }
            }
            else {
                module.mergeEastPanel(this.eastPanel);
            }
            
            //SouthPanel
            if (!Ext.isObject(this.southPanel) || !(this.southPanel instanceof Ext.Panel)) {
                if (module.southPanelElements.items) {
                    this.southPanel = new Ext.Panel({
                        id: 'terrestris-south',
                        region: 'south',
                        margins: '0',
                        border: true,
                        defaults: {
                            border: false
                        },
                        title: '',
                        width: 210,
                        height: 400,
                        layout: 'fit',
                        enableDD: true,
                        items: module.southPanelElements.items,
                        split: true,
                        collapsible: true,
                        collapsed: true
                    });
                }
            }
            else {
                module.mergeSouthPanel(this.southPanel);
            }
            
            
            //Menu
            if (!Ext.isObject(this.mainMenu) || !(this.mainMenu instanceof SHOGun.widget.MenuToolbar)) {
                if (module.menuElements) {
                    this.mainMenu = new SHOGun.widget.MenuToolbar();
                    var tmpMenu = this.mainMenu;
                    Ext.each(module.menuElements.menuToolbar.items, function(value, key){
                        if (value.xtype === 'button' || value instanceof Ext.Button) {
                            tmpMenu.add(value);
                        }
                    });
                }
            }
            else {
                if (module.menuElements.menuToolbar.items.length > 0) {
                    module.mergeMenuToolbar(this.mainMenu);
                }
            }
            
            //NorthPanel
            if (!Ext.isObject(this.northPanel) || !(this.northPanel instanceof Ext.Panel)) {
                if (module.northPanelElements.items) {
                    this.northPanel = new Ext.Panel({
                        id: 'terrestris-north',
                        region: 'north',
                        margins: '0',
                        border: false,
                        defaults: {
                            border: false
                        },
                        header: false,
                        title: 'north',
                        autoHeight: true,
                        bbar: this.mainMenu,
                        items: module.northPanelElements.items,
                        split: true,
                        collapsible: false,
                        collapsed: false
                    });
                }
            }
            else {
                module.mergeNorthPanel(this.northPanel);
            }
            
            this.appModules.push(module);
        }
    };
    
    /**
     * set user dependend values from backend
     * 
     * @param {Object} userdata
     */
    this.setUserContext = function(userdata){
        
        if (Ext.isDefined(userdata.user_lang) && userdata.user_lang !== null) {
            SHOGun.config.LANGUAGE = userdata.user_lang;
        }
        
        var cfg = this.userConfig;
        cfg.modules = {};
        Ext.each(userdata.modules, function(value, key){
            cfg.berechtigungen.push(value.module_name);
            cfg.modules[value.id] = {
                module_name: value.module_name,
                module_fullname: value.module_fullname
            };
        });
        cfg.group_id = userdata.group_id;
        cfg.username = userdata.user_name;
        
        // read out map config
        if(userdata.mapConfig) {
            cfg.mapConfig = userdata.mapConfig;
        }
        
        // read out stored map layers
        if(Ext.isArray(userdata.mapLayers)) {
            cfg.mapLayers = userdata.mapLayers;
        }
        
    };
    
    /**
     * set Application dependend values from backend
     * 
     * @param {Object} appdata
     */
    this.setAppContext = function(appdata){
        this.appConfig.licenseAmount = appdata.license_amount;
    };
    
    /**
     * Checks whether the current user has the right for the rightKey given. E.g.
     * hasRightFor(); // true|false
     *
     * @param {String} rightKey
     * @return {Boolean} whether the user has the specific right or not, defaults to false
     * @see rightConfig
     */
    this.hasRightFor = function(rightKey){
        var allowed = false;
        var compareKey = null;
        if (this.RIGHT_KEYS && !Ext.isEmpty(this.RIGHT_KEYS[rightKey])) {
            compareKey = this.RIGHT_KEYS[rightKey];
            
            Ext.iterate(this.userConfig.berechtigungen, function(userRight){
                if (userRight === compareKey) {
                    allowed = true;
                    return false;
                }
            });
        }
        return allowed;
    }; 
};
