/* Copyright (c) 2012-2014, terrestris GmbH & Co. KG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * (This is the BSD 3-Clause, sometimes called 'BSD New' or 'BSD Simplified',
 * see http://opensource.org/licenses/BSD-3-Clause)
 *
 * The client-side code of SHOGun partly depends on the ExtJS-framework (see
 * http://www.sencha.com/products/extjs, available separately under various
 * licensing options: http://www.sencha.com/products/extjs/licensing); it is
 * released in accordance with Sencha's Exception for Development Version 1.04
 * from January 18, 2013 (see http://www.sencha.com/legal/open-source-faq/
 * open-source-license-exception-for-development/).
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
