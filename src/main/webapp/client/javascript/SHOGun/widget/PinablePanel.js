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
