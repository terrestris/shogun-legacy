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
SHOGun.module.Login = Ext.extend(SHOGun.module.BorderLayout, {

    /**
     * initializes this component
     */
    initComponent : function() {

        // the map-toolbar of the module
        this.mapPanelElements.tbarItems = [];
        this.westPanelElements.items = [];
        this.eastPanelElements.items = [];
        this.northPanelElements.items = [];
        this.southPanelElements.items = [];
        this.menuElements.menuToolbar = new Ext.Toolbar();
        this.menuElements.menuToolbar.items = [];

        // apply config
        Ext.apply(this, this.initialConfig);

        /**
         * Handler to do the submit to authorize.
         * Called on button click and ENTER press
         */
        var doSubmit = function() {
            login.getForm().submit({
                method : 'POST',
                
                /**
                 * 
                 * @param {Object} form
                 * @param {Object} responseObj
                 */
                success : function(form, responseObj) {
                    var jsonResponse = Ext.decode(responseObj.response.responseText);
                    var role = jsonResponse.role;
                    // test which application we should load
                    // ROLE_SUPERADMIN loads the super backoffice
                    // other roles get the map-client
                    var pattern = /ROLE_SUPERADMIN/;
                    var test = role.match(pattern);
                    if(test !== null) {
                        top.location = "admin.jsp";
                    } else {
                        top.location = "index_dev.jsp";
                    }
                },
                
                /**
                 * 
                 * @param {Object} form
                 * @param {Object} responseObj
                 */
                failure : function(form, responseObj) {
                    if(Ext.isDefined(responseObj.response) && !Ext.isEmpty(responseObj.response.statusText && responseObj.statusText === "Unauthorized")) {
                        Ext.Msg.alert('Login Failed!', 'Authentifizierung Fehlgeschlagen');
                    } else if(!Ext.isEmpty(responseObj.failureType) && responseObj.failureType === "client") {
                        // do nothing
                    } else {
                        Ext.Msg.alert('Login Failed!', '');
                    }
                }
            });
        };
        
        /**
         * Create a variable to hold our EXT login panel.
         */
        var login = new Ext.FormPanel({
            labelWidth : 80,
            url : '../j_spring_security_check',
            frame : true,
            title: false,
            defaultType : 'textfield',
            width : 300,
            height : 150,
            monitorValid : true,
            items : [{
                fieldLabel : 'Username',
                name : 'j_username',
                allowBlank : false
            }, {
                fieldLabel : 'Password',
                name : 'j_password',
                inputType : 'password',
                allowBlank : false
            }],
            buttons : [{
                text : 'Login',
                formBind : true,
                handler : doSubmit
            }],
            keys : [{
                key : [Ext.EventObject.ENTER],
                handler : doSubmit
            }]

        });

        /**
         * This just creates a window to wrap the login form.
         * The login form is passed to the items collection.
         */
        var win = new Ext.Window({
            layout : 'fit',
            title : 'Login',
            width : 300,
            height : 150,
            closable : true,
            resizable : false,
            modal : true,
            plain : true,
            border : false,
            items : [login]
        });

        var actionLogin = new GeoExt.Action({
            tooltip : "Login",
            icon : 'images/map_icons/key.png',
            handler : function() {
                win.show();
            },
            helpId : SHOGun.help.HELP_KEYS.LOGIN
        });

        this.mapPanelElements.tbarItems.push('->');
        this.mapPanelElements.tbarItems.push(actionLogin);

        this.menuElements.menuToolbar.items = [{
            xtype : 'menuseparator',
            menuPath : 'Datei|'
        }, {
            xtype : 'menuitem',
            menuPath : 'Datei|Login',
            scope : this,
            handler : function() {
                win.show();
            }
        }];

        SHOGun.module.Login.superclass.initComponent.apply(this, arguments);
    },
    
    /**
     *
     * @param {Object} appMapPanelToolbar
     */
    mergeMapPanelToolbar : function(appMapPanelToolbar) {
        if(Ext.isObject(appMapPanelToolbar) && appMapPanelToolbar instanceof Ext.Toolbar) {
            Ext.each(this.mapPanelElements.tbarItems, function(value, key) {
                appMapPanelToolbar.addItem(value);
            });
        }
    }
});

Ext.reg('shogun_login_module', SHOGun.module.Login);
