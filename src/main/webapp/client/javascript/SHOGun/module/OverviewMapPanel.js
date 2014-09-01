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
Ext.ns('SHOGun.module.mapclient');

/**
 *
 */
SHOGun.module.OverviewMapPanel = Ext.extend(SHOGun.module.BorderLayout, {

    /**
     * initializes this component
     */
    initComponent: function(){
        
        /**
         * the items to be rendered in the west
         */
        this.westPanelElements.items = [];
        
        // apply config
        Ext.apply(this, this.initialConfig);
        
        /**
         * private function called after the div is rendered
         */
        var addOverview = function() {
            var ovEl = Ext.get('overviewmap'),
                ovWidthOffset = 30;
            
            var overview_map = new OpenLayers.Control.OverviewMap({
                size: new OpenLayers.Size(ovEl.getWidth()-ovWidthOffset, 200),
                autopan: false,
                div: ovEl,
                // destroy issues in IE to be fixed, workaround for now 
                destroy: function() {},
                mouseMode: 'dragRectangle',
                mapOptions: {
                    maxExtent: map.getMaxExtent(),
                    zoom: new OpenLayers.Control.ZoomToMaxExtent(),
                    numZoomLevels: 1,
                    projection: map.getProjection(),
                    displayProjection: map.getProjection(),
                    units: 'm'
                }
            });
            map.addControl(overview_map);
            overview_map.activate();
        };
        
        /**
         * the pinable panel
         */
        var overview = new SHOGun.widget.PinablePanel({
            border: false,
            title: "Überblick",
            id : 'overviewmap_panel',
            helpId : this.helpId,
            collapsed : false,
            collapsible : true,
            parentPanelId : 'terrestris-west',
            childPanelId: 'overviewmap_panel',
            draggable : {
                insertProxy : true,
                ddGroup : 'pinablePanel'
            },
            items: [{
                border: false,
                id: 'overviewmap',
                xtype: "panel", 
                listeners: {
                    'afterlayout': {
                        fn: function() {
                            addOverview();
                        }, 
                        single: true
                    }
                }
            }]
        });
        
        this.westPanelElements.items = [ overview ];
        
        SHOGun.module.OverviewMapPanel.superclass.initComponent.apply(this, arguments);
    }, // eo function initComponent
    
    /**
     * we just call the default merge function of super class
     *
     * @param appWestPanel
     */
    mergeWestPanel: function(appWestPanel){
        SHOGun.module.OverviewMapPanel.superclass.mergeWestPanel.apply(this, arguments);
    }
    
});

Ext.reg('shogun_overviewmappanel_module', SHOGun.module.OverviewMapPanel);
