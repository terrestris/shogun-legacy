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
 * A panel that can be unpinned from its initial parent component
 * in order to become a floating window
 */
SHOGun.widget.PinablePanel = Ext.extend(SHOGun.widget.ManagedPanel, {
    parentPanelId: 'terrestris-west',
    childPanelId: '',
    parentPanelPos: 0,
    
    /**
     * initializes this component
     */
    initComponent: function(){
        var constants = {
            userConf: null,
            rightTo: null
        }; // eo config object
        // apply config
        
        var unpinTool = {
            id: 'unpin',
            qtip: 'Als verschiebbares Fenster anzeigen',
            handler: this.undock,
            scope: this
        };
        
        if (this.initialConfig.tools && Ext.isArray(this.initialConfig.tools)) {
            this.initialConfig.tools.push(unpinTool);
        }
        else {
            this.initialConfig.tools = [unpinTool];
        }
        
        Ext.apply(this, Ext.apply(this.initialConfig, constants));
        
        SHOGun.widget.PinablePanel.superclass.initComponent.apply(this, arguments);
    },
    
    /**
     * 
     */
    undock: function(){
        var activePanel = Ext.getCmp(this.childPanelId);
        var parentpanel = Ext.getCmp(this.parentPanelId);
        //save original Position
        this.parentPanelPos = parentpanel.items.indexOf(activePanel);
        
        var header_orig;
        if (activePanel.header) {
            header_orig_h = activePanel.header.getHeight();
            activePanel.header.setHeight(0);
            activePanel.header.hide();
        }
        if (activePanel.collapsed) {
            activePanel.expand();
        }
        
        var w = activePanel.getEl().getWidth();
        
        var win;
        win = new Ext.Window({
            renderTo: Ext.getBody(),
            // autoHeight: true,
            title: activePanel.title,
            constrain: true,
            items: activePanel,
            closeAction: 'hide',
            tools: [{
                id: 'pin',
                qtip: 'Als Element in der Informationsleiste anzeigen',
                handler: function(){
                    if (activePanel.header) {
                        activePanel.header.show();
                        activePanel.header.setHeight(header_orig_h);
                    }
                    var originalParent = Ext.getCmp(this.parentPanelId);
                    
                    //originalParent.add(activePanel);
                    //insert at original position
                    originalParent.insert(this.parentPanelPos, activePanel);
                    originalParent.doLayout();
                    win.destroy();
                },
                scope: this
            }],
            constrainHeader: true
        });
        win.setWidth(w);
        win.show();
    }
});
Ext.reg('terrestris_pinablepanel', SHOGun.widget.PinablePanel);
