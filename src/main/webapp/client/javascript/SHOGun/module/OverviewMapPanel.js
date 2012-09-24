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
    			    zoom: new OpenLayers.Control.ZoomToMaxExtent,
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
