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
